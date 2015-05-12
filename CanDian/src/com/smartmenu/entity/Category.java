package com.smartmenu.entity;

public class Category {
	private String cId;
	private String cName;
	private String cName2;
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getcName2() {
		return cName2;
	}
	public void setcName2(String cName2) {
		this.cName2 = cName2;
	}
	public Category() {
		super();
	}
	public Category(String cId, String cName, String cName2) {
		super();
		this.cId = cId;
		this.cName = cName;
		this.cName2 = cName2;
	}
	
	
}
