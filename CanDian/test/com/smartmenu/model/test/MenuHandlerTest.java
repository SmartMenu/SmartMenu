package com.smartmenu.model.test;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.smartmenu.model.MenuHandler;

public class MenuHandlerTest {

	@Test
	public void testGetMenu() {
		MenuHandler handler = new MenuHandler();
		JSONObject json = handler.getMenu("D01","100","AA:EE:34:30");
		System.out.println(json.toString());
	}

}
