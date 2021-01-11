package org.palichmos.process;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.logging.Level;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.window.MultiFileDownloadDialog;
import org.apache.commons.io.FileUtils;
import org.compiere.model.MClientInfo;
import org.compiere.model.MSalesRegion;
import org.compiere.model.MTree_Base;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ExportGeoJSONToSalesRegion extends SvrProcess
{
	private int IP_GEOMETRY_ID = 0;
	private Timestamp ACTUAL_DATE_GEOMETRY = null;
	
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] parameters = getParameter();
		
		for (ProcessInfoParameter para: parameters)
		{
			String name = para.getParameterName();

			if (name.equals("IP_Geometry_ID"))
				IP_GEOMETRY_ID = para.getParameterAsInt();
			else if (name.equals("ACTUAL_DATE_GEOMETRY"))
				ACTUAL_DATE_GEOMETRY = para.getParameterAsTimestamp();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception
	{
		if (getRecord_ID() <= 0)
			throw new AdempiereException(Msg.getMsg(getCtx(), "expgm_salesregionnotfound"));
		
		MSalesRegion salesRegion = new MSalesRegion(getCtx(), getRecord_ID(), get_TrxName());
		
		File geoJSONFile = createGeoJsonFile(salesRegion);
		
		if (geoJSONFile == null)
			throw new AdempiereException(Msg.getMsg(getCtx(), "expgm_fileisnull"));
		
		AEnv.executeDesktopTask(new Runnable()
		{
			@Override
			public void run()
			{
				MultiFileDownloadDialog downloadDialog = new MultiFileDownloadDialog(new File[] {geoJSONFile});
				downloadDialog.setPage(SessionManager.getAppDesktop().getActiveWindow().getPage());
				downloadDialog.setTitle(Msg.getMsg(getCtx(), "expgm_downloadjsonfile"));
				
				Events.postEvent(downloadDialog, new Event(MultiFileDownloadDialog.ON_SHOW));
			}
		});
		
		return "OK";
	}

	private File createGeoJsonFile(MSalesRegion salesRegion) throws SQLException, IOException
	{
		JsonParser jsonParser = new JsonParser();
		
		LinkedList<Integer> geometryIDs = new LinkedList<Integer>();
		
		if (IP_GEOMETRY_ID > 0)
		{
			geometryIDs.add(IP_GEOMETRY_ID);
		}
		else if (salesRegion.isSummary())
		{
			if (ACTUAL_DATE_GEOMETRY == null)
				throw new AdempiereException(Msg.getMsg(getCtx(), "expgm_actualdatenotfound"));
			
			MClientInfo ci = MClientInfo.get(getCtx());
			MTree_Base tree = new MTree_Base(getCtx(), ci.getAD_Tree_SalesRegion_ID(), get_TrxName());
			
			String sql = "SELECT sr.C_SalesRegion_ID " +
						 "  FROM AD_TreeNode tn " +  
						 "  JOIN C_SalesRegion sr ON sr.C_SalesRegion_ID = tn.Node_ID AND sr.IsActive = 'Y' " +  
						 " WHERE tn.AD_Tree_ID = ? AND tn.Parent_ID = ? " + //#1,2
						 " GROUP BY sr.C_SalesRegion_ID";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, tree.get_ID());
				pstmt.setInt(2, salesRegion.get_ID());
				rs = pstmt.executeQuery();
				
				while (rs.next())
				{
					int IP_Geometry_ID = getActualGeometry_ID(rs.getInt("C_SalesRegion_ID"));
					
					if (IP_Geometry_ID > 0)
						geometryIDs.add(IP_Geometry_ID);
				}
			}
			catch (SQLException e)
			{
				log.warning(e.getMessage());
				throw e;
			}
			finally
			{
				DB.close(rs, pstmt);
			}
		}
		else
		{
			if (ACTUAL_DATE_GEOMETRY == null)
				throw new AdempiereException(Msg.getMsg(getCtx(), "expgm_actualdatenotfound"));
			
			geometryIDs.add(getActualGeometry_ID(salesRegion.get_ID()));
		}
		
		JsonObject mainObject = new JsonObject();
		mainObject.addProperty("type", "FeatureCollection");
		
		JsonArray features = new JsonArray();
		mainObject.add("features", features);
		
		String sql = "SELECT public.ST_AsGeoJson(geom_object) FROM IP_Geometry WHERE IP_Geometry_ID = ?"; //#1
		
		for (Integer IP_Geometry_ID : geometryIDs)
		{
			JsonObject featureObject = new JsonObject();
			features.add(featureObject);
			
			String geometryJSON = DB.getSQLValueStringEx(get_TrxName(), sql, IP_Geometry_ID);
			JsonObject geom_object = (JsonObject) jsonParser.parse(geometryJSON);
			
			featureObject.addProperty("type", "Feature");
			featureObject.addProperty("id", IP_Geometry_ID);
			featureObject.add("geometry", geom_object);
			
			JsonObject properties = new JsonObject();
			properties.addProperty("description", getNameGeometry(IP_Geometry_ID));
			featureObject.add("properties", properties);
		}
		
		File exportFile = File.createTempFile(salesRegion.getName() + "_", ".geojson");
		FileUtils.writeStringToFile(exportFile, mainObject.toString());
		
		return exportFile;
	}

	private int getActualGeometry_ID(int C_SalesRegion_ID)
	{
		String sql = "SELECT geom.IP_Geometry_ID " +
					 "  FROM IP_Geometry geom " +
					 "  JOIN C_SalesRegion sr ON sr.C_SalesRegion_ID = geom.C_SalesRegion_ID AND sr.IsActive = 'Y' AND sr.C_SalesRegion_ID = ? " + //#1
					 " WHERE geom.IsActive = 'Y' AND geom.ValidFrom <= ? AND geom.geom_object IS NOT NULL " + //#2
					 " ORDER BY geom.ValidFrom DESC";
		
		return DB.getSQLValueEx(get_TrxName(), sql, C_SalesRegion_ID, ACTUAL_DATE_GEOMETRY);
	}

	private String getNameGeometry(int iP_Geometry_ID)
	{
		String sql = "SELECT sr.Name " +
					 "  FROM IP_Geometry g " +
					 "  JOIN C_SalesRegion sr ON sr.C_SalesRegion_ID = g.C_SalesRegion_ID " +
					 " WHERE g.IP_Geometry_ID = ?"; //#1
		
		return DB.getSQLValueStringEx(get_TrxName(), sql, iP_Geometry_ID);
	}

}