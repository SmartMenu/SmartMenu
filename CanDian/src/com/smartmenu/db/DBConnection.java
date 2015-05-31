package com.smartmenu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.smartmenu.common.DBProperty;

public class DBConnection {
    private static Connection conn=null;
    private static String connURL;
    private static String username;
    private static String password;
    
    private DBConnection() throws SQLException{
    	try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	DBProperty config=new DBProperty();
    	username=config.getDBUser();
    	password=config.getDBPassword();
    	String dbName=config.getDBName();
    	String dbIp = config.getDBIP();
    	connURL="jdbc:sqlserver://"+dbIp+";databaseName="+dbName;
    	conn = DriverManager.getConnection(connURL, username, password);
    }
    public static synchronized Connection getConnection() throws SQLException{
    	if(conn==null)
    		new DBConnection();
    	else if(conn.isClosed())
    		conn = DriverManager.getConnection(connURL, username, password);
    	return conn;
    }
    public static synchronized void dbClose(){
    	if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
    	}
    }
	@Override
	protected void finalize() throws Throwable {
		dbClose();
		super.finalize();
	}
    
}
