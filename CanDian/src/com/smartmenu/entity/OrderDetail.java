package com.smartmenu.entity;

import java.math.BigDecimal;

public class OrderDetail {
	private String itemId;
	private int qty;
	private BigDecimal price;
	private BigDecimal totalAmount;
	private int discAble;
	private Discount discount;
	private BigDecimal discAmount;
	private int svchgAble;
	private ServiceCharge serviceCharge;
	private BigDecimal svchgAmount;
	private BigDecimal payAmount;
	private int seqNo;
	private String catId;
	private String desc;
	private String desc2;
	private String unit;//µ¥Î»
	private int takeAway;
	
	private String catName;  //for detail shows up
	private String catName2;  //for detail shows up
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getDiscAble() {
		return discAble;
	}
	public void setDiscAble(int discAble) {
		this.discAble = discAble;
	}
	public BigDecimal getDiscAmount() {
		return discAmount;
	}
	public void setDiscAmount(BigDecimal discAmount) {
		this.discAmount = discAmount;
	}
	
	public Discount getDiscount() {
		return discount;
	}
	public void setDiscount(Discount discount) {
		this.discount = discount;
	}
	public int getSvchgAble() {
		return svchgAble;
	}
	public void setSvchgAble(int svchgAble) {
		this.svchgAble = svchgAble;
	}
	public ServiceCharge getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(ServiceCharge serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	public BigDecimal getSvchgAmount() {
		return svchgAmount;
	}
	public void setSvchgAmount(BigDecimal svchgAmount) {
		this.svchgAmount = svchgAmount;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDesc2() {
		return desc2;
	}
	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getTakeAway() {
		return takeAway;
	}
	public void setTakeAway(int takeAway) {
		this.takeAway = takeAway;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getCatName2() {
		return catName2;
	}
	public void setCatName2(String catName2) {
		this.catName2 = catName2;
	}
	
	
}
