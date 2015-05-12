package com.smartmenu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.smartmenu.entity.User;

public class DBUser{
	
	//get valid user by shopid and userid
	public User getUseInfo(String shopId, String userId){
		String sql="SELECT [user_id], [password], [group_id], [name1], [name2], [effective_date], [expiry_date] FROM [dbo].[user] " + 
	               " WHERE user_id='"+userId+"' and shop_id='"+shopId+"' and enable=1";
		
		List<Object> lsUsers = DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){
			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> ls = new ArrayList<Object>();
				while(rs.next()){
			    	User user = new User();
			    	user.setUserId(rs.getString("user_id"));
			    	user.setUserName(rs.getString("name1"));
			    	user.setUserName2(rs.getString("name2"));
			    	user.setPassword(rs.getString("password"));
			    	user.setGroupId(rs.getString("group_id"));
			    	user.setEffectiveDate(rs.getDate("effective_date"));
			    	user.setExpiryDate(rs.getDate("expiry_date"));
			    	ls.add(user);	    	
			    }
				if(ls.size()==0)
					return null;
				else
					return ls;
			}
			
		});
		if(lsUsers==null||lsUsers.size()==0)
			return null;
		return (User)lsUsers.get(0);
	}
		
	public String[] getRights(String shopId, String groupId){
		String sql="select [function] from [dbo].[user_rights] where id_type=2 and rights1=1 and id='"+groupId+"' and shop_id='"+shopId+"'";
		List<Object> ls=DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> ls = new ArrayList<Object>();
				while(rs.next()){
					String right = rs.getString("function");
					ls.add(right);
				}
				if(ls.size()==0)
					return null;
				else
					return ls;
			}
			
		});
		if(ls==null||ls.size()==0)
			return null;
		String[] array = ls.toArray(new String[]{});
		return array;
	}
	
}
