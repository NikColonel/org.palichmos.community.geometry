package org.palichmos.factory;

import java.sql.ResultSet;
import org.adempiere.base.IModelFactory;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.palichmos.model.MGeometry;

public class ModelFactory implements IModelFactory
{
	@Override
	public Class<?> getClass(String tableName)
	{
		if (tableName.equals(MGeometry.Table_Name))
			return MGeometry.class;
		
		return null;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName)
	{
		if (tableName.equals(MGeometry.Table_Name))
			return new MGeometry(Env.getCtx(), Record_ID, trxName);
		
		return null;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName)
	{
		if (tableName.equals(MGeometry.Table_Name))
			return new MGeometry(Env.getCtx(), rs, trxName);
		
		return null;
	}

}