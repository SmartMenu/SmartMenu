package com.smartmenu.menu.item;

import java.math.BigDecimal;

import net.sf.json.JSONObject;

import com.smartmenu.entity.Item;

public class BasicItem {
	private String itemId;
	private String itemName;
	private String itemName2;
	private String pluNo;
	private int seq;
	private String itemDesc;
	private String itemDesc2;
	private String itemPic;
	private BigDecimal itemPrice;
	private BigDecimal itemSetPrice;
	private String unit;
	
	public BasicItem(){	
	}
	
	public BasicItem(Item item){
		itemId = item.getItemId();
		itemName = item.getItemName();
		itemName2 = item.getItemName2();
		pluNo = item.getPluNo();
		//seq = item.getSeq();
		itemDesc = item.getItemDesc();
		itemDesc2 = item.getItemDesc2();
		itemPic = item.getItemPic();
		itemPrice = item.getItemPrice();
		itemSetPrice = item.getItemSetPrice();
		unit = item.getUnit();
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemName2() {
		return itemName2;
	}
	public void setItemName2(String itemName2) {
		this.itemName2 = itemName2;
	}
	public String getPluNo() {
		return pluNo;
	}
	public void setPluNo(String pluNo) {
		this.pluNo = pluNo;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getItemDesc2() {
		return itemDesc2;
	}
	public void setItemDesc2(String itemDesc2) {
		this.itemDesc2 = itemDesc2;
	}
	public String getItemPic() {
		return itemPic;
	}
	public void setItemPic(String itemPic) {
		this.itemPic = itemPic;
	}
	public BigDecimal getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}
	public BigDecimal getItemSetPrice() {
		return itemSetPrice;
	}
	public void setItemSetPrice(BigDecimal itemSetPrice) {
		this.itemSetPrice = itemSetPrice;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public JSONObject toJson(){
		JSONObject json = new JSONObject();
		json.put("item-id", itemId);
		json.put("item-name", itemName);
		json.put("item_name2", itemName2);
		json.put("plu-no", pluNo);
		json.put("price", itemPrice);
		json.put("item-pic", itemPic==null?" ": itemPic);
		return json;
	}
	
}
