package com.smartmenu.entity;

import java.util.List;

public class LookupCategory {
	private String lookupId;
	private String lookupName;
	private String lookupName2;
	private int lookupType;
	private int seq;
	private List<Item> lsItems;
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
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public List<Item> getLsItems() {
		return lsItems;
	}
	public void setLsItems(List<Item> lsItems) {
		this.lsItems = lsItems;
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
