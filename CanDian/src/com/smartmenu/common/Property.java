package com.smartmenu.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {
	private Properties props=null;
	private String fileUrl;
	public Property(String propertyFile) throws Exception{
		props = new Properties();
		String url = this.getClass().getClassLoader().getResource(propertyFile).toString().substring(6);
		fileUrl = url.replace("%20", " ");// �������ļ�·���а����ո��Ǳض��ᱨ���
		System.out.println(fileUrl);
	    InputStream in = null;
	    in = new BufferedInputStream(new FileInputStream(fileUrl));
	    props.load(in);
	}
	public String get(String key){
		return props.getProperty(key);
	}
	//�ݲ����浽�ļ���
	public void set(String key, String value){
		props.setProperty(key, value);
		//FileOutputStream outputFile=null;
		//try{
		//	outputFile = new FileOutputStream(fileUrl);
		//	props.store(outputFile, "update: "+key+": "+value );// property��ؼ���store����
		//}finally{
		//	if(outputFile!=null)
		//		outputFile.close();
		//}
	}
}
