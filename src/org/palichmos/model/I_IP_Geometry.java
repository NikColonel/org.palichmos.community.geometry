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
package org.palichmos.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for IP_Geometry
 *  @author iDempiere (generated) 
 *  @version Release 3.1
 */
@SuppressWarnings("all")
public interface I_IP_Geometry 
{

    /** TableName=IP_Geometry */
    public static final String Table_Name = "IP_Geometry";

    /** AD_Table_ID=1000457 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name C_SalesRegion_ID */
    public static final String COLUMNNAME_C_SalesRegion_ID = "C_SalesRegion_ID";

	/** Set Sales Region.
	  * Sales coverage region
	  */
	public void setC_SalesRegion_ID (int C_SalesRegion_ID);

	/** Get Sales Region.
	  * Sales coverage region
	  */
	public int getC_SalesRegion_ID();

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException;

    /** Column name IP_Geometry_ID */
    public static final String COLUMNNAME_IP_Geometry_ID = "IP_Geometry_ID";

	/** Set IP_Geometry_ID	  */
	public void setIP_Geometry_ID (int IP_Geometry_ID);

	/** Get IP_Geometry_ID	  */
	public int getIP_Geometry_ID();

    /** Column name IP_Geometry_UU */
    public static final String COLUMNNAME_IP_Geometry_UU = "IP_Geometry_UU";

	/** Set IP_Geometry_UU	  */
	public void setIP_Geometry_UU (String IP_Geometry_UU);

	/** Get IP_Geometry_UU	  */
	public String getIP_Geometry_UU();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsGeometry */
    public static final String COLUMNNAME_IsGeometry = "IsGeometry";

	/** Set IsGeometry	  */
	public void setIsGeometry (boolean IsGeometry);

	/** Get IsGeometry	  */
	public boolean isGeometry();

    /** Column name Latitude */
    public static final String COLUMNNAME_Latitude = "Latitude";

	/** Set Latitude	  */
	public void setLatitude (BigDecimal Latitude);

	/** Get Latitude	  */
	public BigDecimal getLatitude();

    /** Column name Longitude */
    public static final String COLUMNNAME_Longitude = "Longitude";

	/** Set Longitude	  */
	public void setLongitude (BigDecimal Longitude);

	/** Get Longitude	  */
	public BigDecimal getLongitude();

    /** Column name TypeGeometry */
    public static final String COLUMNNAME_TypeGeometry = "TypeGeometry";

	/** Set Type Geometry	  */
	public void setTypeGeometry (String TypeGeometry);

	/** Get Type Geometry	  */
	public String getTypeGeometry();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name ValidFrom */
    public static final String COLUMNNAME_ValidFrom = "ValidFrom";

	/** Set Valid from.
	  * Valid from including this date (first day)
	  */
	public void setValidFrom (Timestamp ValidFrom);

	/** Get Valid from.
	  * Valid from including this date (first day)
	  */
	public Timestamp getValidFrom();
}
