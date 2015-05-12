package com.smartmenu.entity;

import java.math.BigDecimal;

public class Tax {
	private String taxId;
	private BigDecimal taxRate;
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	
}
