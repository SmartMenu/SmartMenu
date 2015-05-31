package com.smartmenu.menu.modifier;

import net.sf.json.JSONObject;

public abstract class Modifier {
	private String id;
	private String name;
	private String name2;
	private int seq;
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getName2() {
		return name2;
	}


	public void setName2(String name2) {
		this.name2 = name2;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public abstract JSONObject toJson();
}
