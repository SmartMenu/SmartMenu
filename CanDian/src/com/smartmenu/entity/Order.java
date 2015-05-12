package com.smartmenu.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {
	private String tranNo;
	private String shopId;
	private String posId;
	private Timestamp tranDate;
	private String tableId;
	private String sectionId;
	private ServiceCharge svchg=null;
	private BigDecimal svchgAmount;
	private Tax tax=null;
	private BigDecimal taxAmount;
	private Discount discount=null;
	private BigDecimal discAmount;
	private BigDecimal subtotalAmount;
	private BigDecimal totalAmount;
	private String userId;

	private int cover=0;
	private String checkNo;
	public String getTranNo() {
		return tranNo;
	}
	public void setTranNo(String tranNo) {
		this.tranNo = tranNo;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public Timestamp getTranDate() {
		return tranDate;
	}
	public void setTranDate(Timestamp tranDate) {
		this.tranDate = tranDate;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public ServiceCharge getSvchg() {
		return svchg;
	}
	public void setSvchg(ServiceCharge svchg) {
		this.svchg = svchg;
	}
	public BigDecimal getSvchgAmount() {
		return svchgAmount;
	}
	public void setSvchgAmount(BigDecimal svchgAmount) {
		this.svchgAmount = svchgAmount;
	}
	public Tax getTax() {
		return tax;
	}
	public void setTax(Tax tax) {
		this.tax = tax;
	}
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	public BigDecimal getSubtotalAmount() {
		return subtotalAmount;
	}
	public void setSubtotalAmount(BigDecimal subtotalAmount) {
		this.subtotalAmount = subtotalAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Discount getDiscount() {
		return discount;
	}
	public void setDiscount(Discount discount) {
		this.discount = discount;
	}
	public BigDecimal getDiscAmount() {
		return discAmount;
	}
	public void setDiscAmount(BigDecimal discAmount) {
		this.discAmount = discAmount;
	}
	public int getCover() {
		return cover;
	}
	public void setCover(int cover) {
		this.cover = cover;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	
}
