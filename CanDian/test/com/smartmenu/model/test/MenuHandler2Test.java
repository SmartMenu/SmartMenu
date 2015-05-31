package com.smartmenu.model.test;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.smartmenu.model.MenuHandler2;

public class MenuHandler2Test {

	@Test
	public void testGetMenu() {
		MenuHandler2 handler = new MenuHandler2();
		JSONObject json = handler.getMenu("001","100","AA:EE:34:30");
		System.out.println(json.toString());
	}

}
