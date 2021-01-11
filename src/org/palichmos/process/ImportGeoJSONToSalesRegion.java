package org.palichmos.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.logging.Level;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClientInfo;
import org.compiere.model.MSalesRegion;
import org.compiere.model.MTree_Base;
import org.compiere.model.MTree_Node;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.palichmos.model.MGeometry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ImportGeoJSONToSalesRegion extends SvrProcess
{
	private String FILE_PATH_GEOJSON = null;
	
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] parameters = getParameter();
		
		for (ProcessInfoParameter para: parameters)
		{
			String name = para.getParameterName();

			if (name.equals("FILE_PATH_GEOJSON"))
				FILE_PATH_GEOJSON = para.getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception
	{
		if (getRecord_ID() <= 0)
			throw new AdempiereException(Msg.getMsg(getCtx(), "expgm_salesregionnotfound"));
		
		if (FILE_PATH_GEOJSON == null)
			throw new AdempiereException(Msg.getMsg(getCtx(), "expgm_paramfilepathmandatory"));
		
		JsonObject geoJSON = null;
		
		File geoJsonFile = new File(FILE_PATH_GEOJSON);
		
		InputStreamReader isReader = new InputStreamReader(new FileInputStream(geoJsonFile), "UTF-8");
		BufferedReader in = new BufferedReader(isReader);
		String inputLine = null;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}
		
		in.close();
		isReader.close();
		
		if (response == null || response.length() == 0)
			throw new AdempiereException(Msg.getMsg(getCtx(), "expgm_geojsonfileisempty"));
		
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(response.toString());
		
		geoJSON = jsonElement.getAsJsonObject();
		
		if (geoJSON == null)
			throw new AdempiereException(Msg.getMsg(getCtx(), "expgm_jsonfileisnull"));
		
		MSalesRegion salesRegion = new MSalesRegion(getCtx(), getRecord_ID(), get_TrxName());
		
		//Update all child regions
		if (salesRegion.isSummary())
		{
			updateChildRegions(salesRegion, geoJSON);
		}
		else //Update selected region
		{
			updateCurrentRegion(salesRegion, geoJSON);
		}
		
		return "OK";
	}

	private void updateCurrentRegion(MSalesRegion salesRegion, JsonObject geoJSON)
	{
		JsonArray features = geoJSON.getAsJsonArray("features");
		
		if (features.size() > 1)
			throw new AdempiereException(Msg.getMsg(getCtx(), "expgm_countpolygonesmorethat1"));
		
		JsonObject feature = features.get(0).getAsJsonObject();
		JsonObject geometry = feature.getAsJsonObject("geometry");
		
		if (!geometry.get("type").getAsString().equals("Polygon"))
			throw new AdempiereException(Msg.getMsg(getCtx(), "expgm_typenotsupported", new Object[] {geometry.get("type").getAsString()}));
		
		JsonObject properties = feature.getAsJsonObject("properties");
		
		if (properties.get("description") != null)
		{
			salesRegion.setName(properties.get("description").getAsString());
			salesRegion.saveEx();
		}
		
		JsonArray coordinates = geometry.getAsJsonArray("coordinates");
		createGeometry(salesRegion.get_ID(), coordinates.get(0).getAsJsonArray());
	}

	private void updateChildRegions(MSalesRegion salesRegion, JsonObject geoJSON) throws InterruptedException
	{
		MClientInfo ci = MClientInfo.get(getCtx());
		MTree_Base tree = new MTree_Base(getCtx(), ci.getAD_Tree_SalesRegion_ID(), get_TrxName());
		
		JsonArray features = geoJSON.getAsJsonArray("features");
		
		for (int i = 0; i < features.size(); i++)
		{
			JsonObject feature = features.get(i).getAsJsonObject();
			JsonObject geometry = feature.getAsJsonObject("geometry");
			
			if (!geometry.get("type").getAsString().equals("Polygon"))
				throw new AdempiereException(Msg.getMsg(getCtx(), "expgm_typenotsupported", new Object[] {geometry.get("type").getAsString()}));
			
			JsonObject properties = feature.getAsJsonObject("properties");
			
			String sql = "SELECT tn.Node_ID "+
						 "  FROM AD_TreeNode tn " +
						 "  JOIN C_SalesRegion sr ON sr.C_SalesRegion_ID = tn.Node_ID AND sr.IsActive = 'Y' AND sr.Name = ? " + //#1
						 " WHERE tn.AD_Tree_ID = ? AND tn.Parent_ID = ?"; //#2,3
			int C_SalesRegionChild_ID = DB.getSQLValueEx(get_TrxName(), sql, properties.get("description").getAsString(), tree.get_ID(), salesRegion.get_ID());
			
			if (C_SalesRegionChild_ID <= 0)
			{
				MSalesRegion salesRegionChild = new MSalesRegion(getCtx(), 0, get_TrxName());
				
				if (properties.get("description") != null)
					salesRegionChild.setName(properties.get("description").getAsString());
				else
					salesRegionChild.setName("Polygon #" + i);
				
				salesRegionChild.saveEx();

				MTree_Node node = MTree_Node.get(tree, salesRegionChild.get_ID());
				node.setParent_ID(salesRegion.get_ID());
				node.saveEx();

				C_SalesRegionChild_ID = salesRegionChild.get_ID();
			}
			
			JsonArray coordinates = geometry.getAsJsonArray("coordinates");
			createGeometry(C_SalesRegionChild_ID, coordinates.get(0).getAsJsonArray());
		}
	}

	private void createGeometry(int C_SalesRegion_ID, JsonArray coordinates)
	{
		MGeometry polygon = new MGeometry(getCtx(), 0, get_TrxName());
		polygon.setValidFrom(new Timestamp(System.currentTimeMillis()));
		polygon.setC_SalesRegion_ID(C_SalesRegion_ID);
		polygon.saveEx();

		StringBuilder sb = new StringBuilder("");
		sb.append("UPDATE IP_Geometry SET geom_object = public.ST_GeomFromText('POLYGON((");
		
		
		for (int i = 0; i < coordinates.size(); i++)
		{
			JsonArray point = coordinates.get(i).getAsJsonArray();
			
			if (i != 0)
				sb.append(",");
			
			sb.append(point.get(0).getAsString()).append(" ").append(point.get(1).getAsString());
		}
		
		sb.append("))') WHERE IP_Geometry_ID = ?");
		
		DB.executeUpdateEx(sb.toString(), new Object[] {polygon.get_ID()}, get_TrxName());
	}
}