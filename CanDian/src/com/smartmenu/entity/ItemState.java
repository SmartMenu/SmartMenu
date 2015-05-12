package com.smartmenu.entity;

import java.math.BigDecimal;

public class ItemState {
	private String itemId;
	private BigDecimal soldoutBal;
	private int soldoutFlag;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public BigDecimal getSoldoutBal() {
		return soldoutBal;
	}
	public void setSoldoutBal(BigDecimal soldoutBal) {
		this.soldoutBal = soldoutBal;
	}
	public int getSoldoutFlag() {
		return soldoutFlag;
	}
	public void setSoldoutFlag(int soldoutFlag) {
		this.soldoutFlag = soldoutFlag;
	}
	
}
