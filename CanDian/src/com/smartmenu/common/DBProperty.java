package com.smartmenu.common;

public class DBProperty {
	Property property=null;
	public DBProperty(){
		try {
			property = new Property("db.property");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public String getDBUser(){
		if(property==null || property.get("username") == null)
			return "sa";
		else
			return property.get("username");
	}
	
	public String getDBPassword(){
		if(property==null || property.get("password") == null)
			return "1234";
		else
			return property.get("password");
	}
	
	public String getDBName(){
		if(property==null || property.get("dbname") == null)
			return "smartmenu";
		else
			return property.get("dbname");
	}
	
	public String getDBIP(){
		if(property==null || property.get("dbip") == null)
			return "59.148.230.27:1600";
		else
			return property.get("dbip");
	}
	
	public static void main(String[] args){
		DBProperty config = new DBProperty();
		System.out.println(config.getDBUser());
		System.out.println(config.getDBPassword());
		System.out.println(config.getDBName());
		System.out.println(config.getDBIP());
	}
}
