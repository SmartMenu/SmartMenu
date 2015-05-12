package com.smartmenu.model.test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.smartmenu.model.OrderHandler;

public class OrderHandlerTest {

	OrderHandler orderHandler=new OrderHandler();
	String shopId="001";
	String posId="105";
	String tableId="11";
	
	private JSONObject generateOrder(String orderNo, String checkNo){
		JSONObject jorder = new JSONObject();
		jorder.put("shop-id", shopId);
		jorder.put("pos-id", posId);
		jorder.put("table-id", tableId);
		jorder.put("section-id", "01");
		jorder.put("order-no", orderNo);
		jorder.put("check-no", checkNo);
		jorder.put("order-date","2015-04-26 17:19:00");
		jorder.put("service-charge-amount", 0);
		jorder.put("tax-amount",0);
		jorder.put("discount-amount", 0);
		jorder.put("subtotal-amount", 878);
		jorder.put("total-amount", 878);
		jorder.put("user-id", "3328");
		
		JSONArray jaDetails = new JSONArray();
		JSONObject jItem1= new JSONObject();
		jItem1.put("item-id", "2001");
		jItem1.put("qty", 2);
		jItem1.put("seq", 1);
		jItem1.put("price", 390);
		jItem1.put("total-amount", 780);
		jItem1.put("discount-able", 0);
		jItem1.put("discount-amount", 0);
		jItem1.put("service-charge-able", 0);
		jItem1.put("service-charge-amount", 0);
		jItem1.put("pay-amount", 780);
		jItem1.put("cat-id", "01");
		jItem1.put("desc", "Roasted Duck (each)");
		jItem1.put("desc2", "Roasted Duck (each)");
		jItem1.put("unit", "·Ý");
		jItem1.put("take-away",0);
		
		
		JSONObject jItem2= new JSONObject();
		jItem2.put("item-id", "6012");
		jItem2.put("qty", 1);
		jItem2.put("seq", 2);
		jItem2.put("price", 98);
		jItem2.put("total-amount", 98);
		jItem2.put("discount-able", 0);
		jItem2.put("discount-amount", 0);
		jItem2.put("service-charge-able", 0);
		jItem2.put("service-charge-amount", 0);
		jItem2.put("pay-amount", 98);
		jItem2.put("cat-id", "01");
		jItem2.put("desc", "Çå³´»ÊµÛ²Ë");
		jItem2.put("desc2", "Çå³´»ÊµÛ²Ë");
		jItem2.put("unit", "·Ý");
		jItem2.put("take-away",0);
		
		jaDetails.add(jItem1);
		jaDetails.add(jItem2);
		
		JSONObject json = new JSONObject();
		json.put("order", jorder);
		json.put("details", jaDetails);
		
		return json;
	}
	@Test
	public void testMakeNewOrder() {
		JSONObject order = this.generateOrder(null,null);
		JSONObject ret = orderHandler.makeNewOrder(order);
		System.out.println(ret.toString());
	}

	@Test
	public void testGetOldOrder() {
		JSONObject json = orderHandler.getOldOrder(shopId, posId, tableId);
		System.out.println(json.toString());
	}

	@Test
	public void testAppendOldOrder() {
//		JSONObject order = this.generateOrder("00001246", "00073");
	//System.out.println(order.toString());
	//	JSONObject ret = orderHandler.appendOldOrder(order);
		//System.out.println(ret.toString());
	}

}
