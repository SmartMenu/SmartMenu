package com.smartmenu.entity;

public class LookupDetail {
	public final static int NORMAL_FLAG_FOR_LOOKUPTYPE=1;
	public final static int MODIFIER_FLAG_FOR_LOOKUPTYPE=2;
	public final static int SETTER_FLAG_FOR_LOOKUPTYPE=3;
	public final static int ITEM_FLAG_FOR_ITEMTYPE=0;
	public final static int GROUP_FLAG_FOR_ITEMTYPE=1;
	
	private String lookupId;  
	private int lookupType;  //Lookup_header.lookup_type = 1 普通类别 2 改码类别 3 套餐类别
	private int itemType;   //item_type: 0表示item, 1表示lookup
	private String code;
	private int seq;
	public String getLookupId() {
		return lookupId;
	}
	public void setLookupId(String lookupId) {
		this.lookupId = lookupId;
	}
	public int getLookupType() {
		return lookupType;
	}
	public void setLookupType(int lookupType) {
		this.lookupType = lookupType;
	}
	public int getItemType() {
		return itemType;
	}
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	
}
