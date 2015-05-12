package com.smartmenu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.smartmenu.entity.ServiceCharge;

public class DBTable{

	//check whether the table is valid
	public boolean checkTableId(String tableId, String shopId){
		String sql="select top 1 [shop_id], [table_id] from [dbo].[table] " +
	               " where shop_id='" + shopId + "' and table_id='" + tableId + "';";
		return DBCommonUtil.checkExist(sql);
	}
	//get all tables
	public String[] getTables(String shopId){
		String sql="select [table_id] from [dbo].[table] where shop_id='" + shopId + "';";
		List<Object> ls = DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> ls = new ArrayList<Object>();
				while(rs.next()){
					String tableId = rs.getString("table_id");
					ls.add(tableId);
				}
				if(ls==null || ls.size() == 0)
					return null;
				else 
					return ls;
				
			}
			
		});
		return ls.toArray(new String[]{});
	}
	
	public ServiceCharge getServiceCharge(String tableId, String shopId){
		String sql="select c.id, c.desc1 as desc, c.desc2 as desc2, c.value from dbo.[table] a, dbo.section b, dbo.charges c "+
	               " where a.shop_id='"+shopId+"' and a.table_id='"+tableId+"' and a.section_id=b.section_id and b.chg_id=c.id; ";
		List<Object> ls = DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){
			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				if(rs.next()){
                    ServiceCharge sc = new ServiceCharge();
					sc.setId(rs.getString("id"));
					sc.setDesc(rs.getString("desc"));
					sc.setDesc2(rs.getString("desc2"));
                    sc.setValue(rs.getBigDecimal("value"));
					List<Object> lsResult = new ArrayList<Object>();
					lsResult.add(sc);
					return lsResult;
				}
				return null;
			}
		});
		if(ls!=null&&ls.size()!=0)
			return (ServiceCharge)ls.get(0);
		return null;
	}

}
