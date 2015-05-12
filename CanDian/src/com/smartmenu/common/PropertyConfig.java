package com.smartmenu.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyConfig {
	private Properties props=null;
	public PropertyConfig(){
		props = new Properties();
		String url = this.getClass().getClassLoader().getResource("db.property").toString().substring(6);
		String empUrl = url.replace("%20", " ");// 如果你的文件路径中包含空格，是必定会报错的
		System.out.println(empUrl);
	    InputStream in = null;
	    try {
	    	in = new BufferedInputStream(new FileInputStream(empUrl));
	    	props.load(in);
	    } catch (FileNotFoundException e1) {
	    	System.out.println("config file: " + url +" initialized failed");
	    	e1.printStackTrace();
	    } catch (IOException e1) {
	    	System.out.println("config file: " + url +" initialized failed");
	    	e1.printStackTrace();
	    }
    }
	
	public String getDBUser(){
		if(props==null)
			return "sa";
		else
			return props.getProperty("username");
	}
	
	public String getDBPassword(){
		if(props==null)
			return "1234";
		else
			return props.getProperty("password");
	}
	
	public String getDBName(){
		if(props==null)
			return "smartmenu";
		else
			return props.getProperty("dbname");
	}
	
	public String getDBIP(){
		if(props==null)
			return "59.148.230.27:1600";
		else
			return props.getProperty("dbip");
	}
	
	public static void main(String[] args){
		PropertyConfig config = new PropertyConfig();
		System.out.println(config.getDBUser());
		System.out.println(config.getDBPassword());
		System.out.println(config.getDBName());
		System.out.println(config.getDBIP());
	}
}
