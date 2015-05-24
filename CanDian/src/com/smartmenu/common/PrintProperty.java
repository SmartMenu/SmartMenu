package com.smartmenu.common;

import java.io.IOException;

public class PrintProperty {
	private static Property property=null;
	private static PrintProperty printProperty=new PrintProperty();
	private PrintProperty(){
		try {
			property = new Property("print.property");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static PrintProperty getInstance(){
		if(property==null)
			return null;
		else
			return printProperty;
	}
	//先获得打印的序列号，然后序列号加1并保存
	public synchronized int getSeqNo(){
		int seq;
		if(property.get("seq")==null||property.get("seq").length()==0)
			seq=1;
		else
			seq = Integer.parseInt(property.get("seq"));
		String newSeq=(seq+1)+"";
		property.set("seq", newSeq);
		return seq;
	}
	
	public String getFolder(){
		if(property.get("folder")==null||property.get("folder").length()==0)
			return null;
		else
			return property.get("folder");
	}
}
