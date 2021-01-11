/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.palichmos.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for IP_Geometry
 *  @author iDempiere (generated) 
 *  @version Release 3.1 - $Id$ */
public class X_IP_Geometry extends PO implements I_IP_Geometry, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210105L;

    /** Standard Constructor */
    public X_IP_Geometry (Properties ctx, int IP_Geometry_ID, String trxName)
    {
      super (ctx, IP_Geometry_ID, trxName);
      /** if (IP_Geometry_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_IP_Geometry (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_IP_Geometry[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException
    {
		return (org.compiere.model.I_C_SalesRegion)MTable.get(getCtx(), org.compiere.model.I_C_SalesRegion.Table_Name)
			.getPO(getC_SalesRegion_ID(), get_TrxName());	}

	/** Set Sales Region.
		@param C_SalesRegion_ID 
		Sales coverage region
	  */
	public void setC_SalesRegion_ID (int C_SalesRegion_ID)
	{
		if (C_SalesRegion_ID < 1) 
			set_Value (COLUMNNAME_C_SalesRegion_ID, null);
		else 
			set_Value (COLUMNNAME_C_SalesRegion_ID, Integer.valueOf(C_SalesRegion_ID));
	}

	/** Get Sales Region.
		@return Sales coverage region
	  */
	public int getC_SalesRegion_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SalesRegion_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set IP_Geometry_ID.
		@param IP_Geometry_ID IP_Geometry_ID	  */
	public void setIP_Geometry_ID (int IP_Geometry_ID)
	{
		if (IP_Geometry_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_IP_Geometry_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_IP_Geometry_ID, Integer.valueOf(IP_Geometry_ID));
	}

	/** Get IP_Geometry_ID.
		@return IP_Geometry_ID	  */
	public int getIP_Geometry_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_IP_Geometry_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set IP_Geometry_UU.
		@param IP_Geometry_UU IP_Geometry_UU	  */
	public void setIP_Geometry_UU (String IP_Geometry_UU)
	{
		set_ValueNoCheck (COLUMNNAME_IP_Geometry_UU, IP_Geometry_UU);
	}

	/** Get IP_Geometry_UU.
		@return IP_Geometry_UU	  */
	public String getIP_Geometry_UU () 
	{
		return (String)get_Value(COLUMNNAME_IP_Geometry_UU);
	}

	/** Set IsGeometry.
		@param IsGeometry IsGeometry	  */
	public void setIsGeometry (boolean IsGeometry)
	{
		throw new IllegalArgumentException ("IsGeometry is virtual column");	}

	/** Get IsGeometry.
		@return IsGeometry	  */
	public boolean isGeometry () 
	{
		Object oo = get_Value(COLUMNNAME_IsGeometry);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Latitude.
		@param Latitude Latitude	  */
	public void setLatitude (BigDecimal Latitude)
	{
		set_Value (COLUMNNAME_Latitude, Latitude);
	}

	/** Get Latitude.
		@return Latitude	  */
	public BigDecimal getLatitude () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Latitude);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Longitude.
		@param Longitude Longitude	  */
	public void setLongitude (BigDecimal Longitude)
	{
		set_Value (COLUMNNAME_Longitude, Longitude);
	}

	/** Get Longitude.
		@return Longitude	  */
	public BigDecimal getLongitude () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Longitude);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Polygon = P */
	public static final String TYPEGEOMETRY_Polygon = "P";
	/** Set Type Geometry.
		@param TypeGeometry Type Geometry	  */
	public void setTypeGeometry (String TypeGeometry)
	{

		set_Value (COLUMNNAME_TypeGeometry, TypeGeometry);
	}

	/** Get Type Geometry.
		@return Type Geometry	  */
	public String getTypeGeometry () 
	{
		return (String)get_Value(COLUMNNAME_TypeGeometry);
	}

	/** Set Valid from.
		@param ValidFrom 
		Valid from including this date (first day)
	  */
	public void setValidFrom (Timestamp ValidFrom)
	{
		set_Value (COLUMNNAME_ValidFrom, ValidFrom);
	}

	/** Get Valid from.
		@return Valid from including this date (first day)
	  */
	public Timestamp getValidFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidFrom);
	}
}