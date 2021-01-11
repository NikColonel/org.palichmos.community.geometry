package org.palichmos.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.DB;

public class MGeometry extends X_IP_Geometry
{
	private static final long serialVersionUID = 1L;

	public MGeometry(Properties ctx, int IP_Geometry_ID, String trxName)
	{
		super(ctx, IP_Geometry_ID, trxName);
	}
	
	public MGeometry(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	public boolean isContains(BigDecimal lognitude, BigDecimal latitude)
	{
		String sql = "SELECT IP_Geometry_ID " +
				 	 "  FROM IP_Geometry " + 
				 	 " WHERE IP_Geometry_ID = ? AND public.ST_Contains(geom_object, public.ST_SetSRID(public.ST_Point(?, ?), 4326))"; //#1,2,3
		
		return DB.getSQLValueEx(get_TrxName(), sql, getIP_Geometry_ID(), lognitude, latitude) > 0;
	}
	
}