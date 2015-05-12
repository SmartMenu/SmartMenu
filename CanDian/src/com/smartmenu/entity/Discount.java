package com.smartmenu.entity;

import java.math.BigDecimal;

public class Discount {
	private String discId;
	private int discType;
	private BigDecimal discRate;
	private String discDesc;
	private String discDesc2;
	public String getDiscId() {
		return discId;
	}
	public void setDiscId(String discId) {
		this.discId = discId;
	}
	public int getDiscType() {
		return discType;
	}
	public void setDiscType(int discType) {
		this.discType = discType;
	}
	public BigDecimal getDiscRate() {
		return discRate;
	}
	public void setDiscRate(BigDecimal discRate) {
		this.discRate = discRate;
	}
	public String getDiscDesc() {
		return discDesc;
	}
	public void setDiscDesc(String discDesc) {
		this.discDesc = discDesc;
	}
	public String getDiscDesc2() {
		return discDesc2;
	}
	public void setDiscDesc2(String discDesc2) {
		this.discDesc2 = discDesc2;
	}
	
}
