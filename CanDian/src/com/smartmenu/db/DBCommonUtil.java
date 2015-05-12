package com.smartmenu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBCommonUtil {
	public static boolean checkExist(String sql){
		Statement st=null;
		ResultSet rs=null;
		try {
			st = DBConnection.getConnection().createStatement();
			rs = st.executeQuery(sql);
			if (rs.next())
				return true;
			else 
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try{
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public static List<Object> executeQuery(String sql, ParseResultSetInterface parseResultSet){
		Statement st=null;
		ResultSet rs=null;
		try {
			st = DBConnection.getConnection().createStatement();
			rs = st.executeQuery(sql);
			List<Object> ls = parseResultSet.parseResult(rs); 
			return ls;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try{
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static int executeUpdateOrInsert(String sql){
		Statement st=null;
		ResultSet rs=null;
		try {
			st = DBConnection.getConnection().createStatement();
			int count = st.executeUpdate(sql);
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try{
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}
