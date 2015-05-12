package com.smartmenu.model.test;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.smartmenu.model.LoginHandler;

public class LoginHandlerTest {

	@Test
	public void testDealLogin() {
		String userId="3328";
		String password="3328";
		String shopId="D01";
		String tableId="21";
		LoginHandler handler = new LoginHandler();
		JSONObject json = handler.dealLogin(shopId, userId, password, tableId);
		System.out.print(json.toString());
	}
}
