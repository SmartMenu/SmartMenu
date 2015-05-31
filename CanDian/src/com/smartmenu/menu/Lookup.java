package com.smartmenu.menu;

import java.util.ArrayList;
import java.util.List;

import com.smartmenu.entity.LookupCategory;
import com.smartmenu.menu.item.BasicItem;

public class Lookup {
	private String lookupId;
	private String lookupName;
	private String lookupName2;
	private int lookupType;    //Õº∆¨œ‘ æ∑Ω Ω
	private List<BasicItem> lsItems;
	
	
	public Lookup() {
		super();
	}
	
	public Lookup(LookupCategory lc) {
		this.lookupId = lc.getLookupId();
		this.lookupName = lc.getLookupName();
		this.lookupName2 = lc.getLookupName2();
		this.lookupType = lc.getLookupType();
	}
	
	public String getLookupId() {
		return lookupId;
	}
	public void setLookupId(String lookupId) {
		this.lookupId = lookupId;
	}
	public String getLookupName() {
		return lookupName;
	}
	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}
	
	public String getLookupName2() {
		return lookupName2;
	}
	public void setLookupName2(String lookupName2) {
		this.lookupName2 = lookupName2;
	}
	public int getLookupType() {
		return lookupType;
	}
	public void setLookupType(int lookupType) {
		this.lookupType = lookupType;
	}
	
	public List<BasicItem> getLsItems() {
		return lsItems;
	}
	public void setLsItems(List<BasicItem> lsBasicItems) {
		this.lsItems = lsBasicItems;
	}
	
	public void addItem(BasicItem bi){
		if(lsItems==null)
			lsItems = new ArrayList<BasicItem>();
		lsItems.add(bi);
	}
	
	@Override
	public boolean equals(Object obj) {
		LookupCategory lc = (LookupCategory) obj;
		if(this.lookupId.equals(lc.getLookupId()))
			return true;
		else
			return false;
	}
}
