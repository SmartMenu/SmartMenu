package com.smartmenu.menu.setter;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SetterContainer extends Setter {
	private boolean defaultSelectAll;
	private int compulsory;
	private int minCount;
	private int maxCount;
	private List<Setter> lsSetters;
	public boolean isDefaultSelectAll() {
		return defaultSelectAll;
	}
	public void setDefaultSelectAll(boolean defaultSelectAll) {
		this.defaultSelectAll = defaultSelectAll;
	}
	public int getCompulsory() {
		return compulsory;
	}
	public void setCompulsory(int compulsory) {
		this.compulsory = compulsory;
	}
	public int getMinCount() {
		return minCount;
	}
	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public List<Setter> getLsSetters() {
		return lsSetters;
	}

	public void setLsSetters(List<Setter> lsSetters) {
		this.lsSetters = lsSetters;
	}
	
	public void addSetter(Setter setter){
		if(lsSetters == null)
			lsSetters = new ArrayList<Setter>();
		lsSetters.add(setter);
	}
	
	public SetterContainer() {
	}

	@Override
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("type", "Setter");
		json.put("lookup-id", getId());
		json.put("lookup-name", getName());
		json.put("lookup-name2", getName2());
		json.put("seq", getSeq());
		json.put("compulsory", this.getCompulsory());
		json.put("min-count", this.getMinCount());
		json.put("max-count", this.getMaxCount());
				
		if(defaultSelectAll)
			json.put("select-all", "true");
		else
			json.put("select-all", "false");

		JSONArray ja= new JSONArray();
		for(Setter setter: lsSetters){
			ja.add(setter.toJson());
		}
		json.put("details", ja);
		return json;
	}
	
}
