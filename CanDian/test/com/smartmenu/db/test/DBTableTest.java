package com.smartmenu.db.test;

import junit.framework.Assert;

import org.junit.Test;

import com.smartmenu.db.DBTable;

public class DBTableTest {

	@Test
	public void testCheckTableId() {
		DBTable table = new DBTable();
		Assert.assertTrue(table.checkTableId("11", "D01"));
	}

}
