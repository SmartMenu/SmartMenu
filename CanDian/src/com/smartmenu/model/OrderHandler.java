
package com.smartmenu.model;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.smartmenu.db.DBOrder;
import com.smartmenu.entity.Discount;
import com.smartmenu.entity.Order;
import com.smartmenu.entity.OrderDetail;
import com.smartmenu.entity.ServiceCharge;
import com.smartmenu.entity.Tax;

public class OrderHandler {
	private DBOrder dbOrder=null;
	
	public OrderHandler(){
		dbOrder = new DBOrder();
	}
	
	public JSONObject makeNewOrder(JSONObject data){
		JSONObject json = new JSONObject();
		JSONArray jaMsg = new JSONArray();
		int status=0;
	
		JSONObject jOrder=data.getJSONObject("order");
		JSONArray jaDetail=data.getJSONArray("details");
		Order order = this.parseOrder(jOrder);
		List<OrderDetail> lsOrderDetail = this.parseDetails(jaDetail);
		if(order==null||lsOrderDetail==null){
			status=1;
			jaMsg.add("LACK_OF_INFO");
		}else{
			try {
				String result = dbOrder.addNewOrder(order, lsOrderDetail);
				if(result.equals("SUCCESS")){
					status=0;
					jaMsg.add(result);
				}else{
					status=1;
					jaMsg.add(result);
				}
			} catch (SQLException e) {
				status=1;
				jaMsg.add("CONN_DB_ERROR");
				e.printStackTrace();
			}
		}
		json.put("status", status);
		json.put("msg", jaMsg);
		return json;
	}
	public JSONObject getOldOrder(String shopId, String tableId){
		JSONObject json=new JSONObject();
		JSONArray jaMsg=new JSONArray();
		JSONObject jData=new JSONObject();
		int status=0;
		Order order=dbOrder.getExistOrder(shopId, tableId);
		if(order==null){
			status=1;
			jaMsg.add("NO_EXIST_ORDER");
			json.put("status", status);
			json.put("msg", jaMsg);
			json.put("data", jData);
			return json;
		}
		OrderDetail[] orderDetails=dbOrder.getOrderDetail(shopId, order.getTranNo());
		if(orderDetails==null || orderDetails.length==0){
			status=1;
			jaMsg.add("NO_EXIST_ORDER_DETAILS");
			json.put("status", status);
			json.put("msg", jaMsg);
			json.put("data", jData);
			return json;
		}
		jData.put("order-no", order.getTranNo());
		jData.put("check-no", order.getCheckNo());
		JSONArray jaDetails = new JSONArray();
		for(OrderDetail orderDetail: orderDetails){
			JSONObject jDetail = new JSONObject();
			jDetail.put("item-id", orderDetail.getItemId());
			jDetail.put("item-name", orderDetail.getDesc());
			jDetail.put("item-name2", orderDetail.getDesc2());
			jDetail.put("seq", orderDetail.getSeqNo());
			jDetail.put("qty", orderDetail.getQty());
			jDetail.put("price", orderDetail.getPrice());
			jDetail.put("cat-id", orderDetail.getCatId());
			jDetail.put("cat-name", orderDetail.getCatName());
			jDetail.put("cat-name2", orderDetail.getCatName2());
			jaDetails.add(jDetail);
		}
		jData.put("details", jaDetails);
		jaMsg.add("SUCCESS");
		json.put("status", status);
		json.put("msg", jaMsg);
		json.put("data", jData);
		return json;
	}
	
	public JSONObject appendOldOrder(JSONObject data){
		JSONObject json = new JSONObject();
		JSONArray jaMsg = new JSONArray();
		int status=0;
		JSONObject jOrder=data.getJSONObject("order");
		JSONArray jaDetail=data.getJSONArray("details");
		Order order = this.parseOrder(jOrder);
		List<OrderDetail> lsNewOrderDetail = this.parseDetails(jaDetail);
		if(order==null||lsNewOrderDetail==null){
			status=1;
			jaDetail.add("LACK_OF_INFO");
		}else{
			try {
				String result = dbOrder.appendExistOrder(order, lsNewOrderDetail);
				if(result.equals("SUCCESS")){
					status=0;
					jaMsg.add(result);
				}else{
					status=1;
					jaMsg.add(result);
				}
			} catch (SQLException e) {
				status=1;
				jaMsg.add("CONN_DB_ERROR");
				e.printStackTrace();
			}
		}
		json.put("status", status);
		json.put("msg", jaMsg);
		return json;
	}
/*
"order":{"shop-id": "","pos-id":"","table-id":"","section-id":"", "order-no": "",  ///////////"order-date":"",
"service-charge":{"id":,"desc":"","desc2":"","value":, "type":},"service-charge-amount":,
"tax":{"id":"","value":},"tax-amount":,
"discount":{"id":"","disc-type":,"rate":,"desc":"","desc2":""},"discount-amount":,
"subtotal-amount":,"total-amount":,"user-id":""}
 * */	
	private Order parseOrder(JSONObject jOrder){
		int status=0;
		Order order = new Order();
		String shopId = jOrder.getString("shop-id");
		if(shopId==null||shopId.trim().length()==0)
		{
			System.out.println("shop id can't be null");
			status=1;
		}else
			order.setShopId(shopId);
		String posId=jOrder.getString("pos-id");
		if(posId==null||posId.trim().length()==0)
		{
			System.out.println("pos id can't be null");
			status=1;
		}else
			order.setPosId(posId);
		String tableId = jOrder.getString("table-id");
		if(tableId==null||tableId.trim().length()==0){
			System.out.println("table id can't be null");
			status=1;
		}else
			order.setTableId(tableId);
		
		String sectionId = jOrder.getString("section-id");
		if(sectionId==null||sectionId.trim().length()==0){
			System.out.println("table id can't be null");
			status=1;
		}else
			order.setSectionId(sectionId);
		if(jOrder.containsKey("order-no")){
			String tranNo=jOrder.getString("order-no");
			if(tranNo==null||tranNo.trim().length()==0)
				order.setTranNo(null);
			else
				order.setTranNo(tranNo);
		}else
			order.setTranNo(null);
		
		if(jOrder.containsKey("check-no")){
			String checkNo=jOrder.getString("check-no");
			if(checkNo==null||checkNo.trim().length()==0)
				order.setCheckNo(null);
			else
				order.setCheckNo(checkNo);
		}else
			order.setCheckNo(null);		
		
		if(jOrder.containsKey("service-charge")){
			JSONObject svchg=jOrder.getJSONObject("service-charge");
			if(svchg==null || svchg.isEmpty()||svchg.isNullObject()){
				order.setSvchg(null);
			}else{
				//"service-charge":{"id":,"desc":"","desc2":"","value":, "type":},"service-charge-amount":,
				ServiceCharge serviceCharge = new ServiceCharge();
				serviceCharge.setId(svchg.getString("id"));
				serviceCharge.setDesc(svchg.getString("desc"));
				serviceCharge.setDesc2(svchg.getString("desc2"));
				serviceCharge.setType(svchg.getInt("type"));
				serviceCharge.setValue(new BigDecimal(svchg.getString("value")));
				order.setSvchg(serviceCharge);
			}
		}else
			order.setSvchg(null);
		String svchgAmount=jOrder.getString("service-charge-amount");
		if(svchgAmount==null||svchgAmount.trim().length()==0){
			System.out.println("service charge amount can be null.");
			status=1;
		}else{
			order.setSvchgAmount(new BigDecimal(svchgAmount));
		}
		//"tax":{"id":"","value":},"tax-amount":,
		if(jOrder.containsKey("tax")){
			JSONObject jTax = jOrder.getJSONObject("tax");
			if(jTax!=null&&!jTax.isEmpty()&&!jTax.isNullObject()){
				Tax tax = new Tax();
				tax.setTaxId(jTax.getString("id"));
				tax.setTaxRate(new BigDecimal(jTax.getString("value")));
				order.setTax(tax);
			}else
				order.setTax(null);
		}else
			order.setTax(null);
		String taxAmount=jOrder.getString("tax-amount");
		if(taxAmount==null||taxAmount.trim().length()==0){
			System.out.println("Tax amount can't be null.");
			status=1;
		}else{
			order.setTaxAmount(new BigDecimal(taxAmount));
		}
		//"discount":{"id":"","disc-type":,"rate":,"desc":"","desc2":""},"discount-amount":,
		if(jOrder.containsKey("discount")){
			JSONObject jDisc=jOrder.getJSONObject("discount");
			if(jDisc==null||jDisc.isEmpty()||jDisc.isNullObject()){
				order.setDiscount(null);
			}else{
				Discount disc = new Discount();
				disc.setDiscId(jDisc.getString("id"));
				disc.setDiscType(jDisc.getInt("disc-type"));
				disc.setDiscRate(new BigDecimal(jDisc.getString("rate")));
				disc.setDiscDesc(jDisc.getString("desc"));
				disc.setDiscDesc2(jDisc.getString("desc2"));
				order.setDiscount(disc);
			}
		}else
			order.setDiscount(null);
		String discAmount=jOrder.getString("discount-amount");
		if(discAmount==null||discAmount.trim().length()==0){
			System.out.println("Discount amount can't be null.");
			status=1;
		}else{
			order.setDiscAmount(new BigDecimal(discAmount));
		}
		//"subtotal-amount":,"total-amount":,"user-id":""
		String subTotal=jOrder.getString("subtotal-amount");
		if(subTotal==null||subTotal.trim().length()==0){
			System.out.println("Sub total amount can't be null.");
			status=1;
		}else
			order.setSubtotalAmount(new BigDecimal(subTotal));
		String totalAmount=jOrder.getString("total-amount");
		if(totalAmount==null||totalAmount.trim().length()==0){
			System.out.println("Sub total amount can't be null.");
			status=1;
		}else
			order.setTotalAmount(new BigDecimal(totalAmount));
		String userId=jOrder.getString("user-id");
		if(userId==null||userId.trim().length()==0){
			System.out.println("User id can't be null.");
			status=1;
		}else
			order.setUserId(userId);
		if(status==1){
			System.out.println("lack of information");
			return null;
		}else			
			return order;
	}
/*
 * "details":[{"item-id":,"seq":,"qty":,"price":,total-amount:,discount-able:,
            discount:{"id":"","disc-type":,"rate":,"desc":"","desc2":""},discount-amount:,
			service-charge-able:,
			service-charge:{"id":,"desc":"","desc2":"","value":, "type":},service-charge-amount:,
			pay-amount:,cat-id:,desc:,desc2:,unit:,take-away:}]
 * */
	private List<OrderDetail> parseDetails(JSONArray jaDetail){
		if(jaDetail==null || jaDetail.size()==0)
			return null;
		List<OrderDetail> ls = new ArrayList<OrderDetail>();
		int status=0;
		for(int i=0;i<jaDetail.size();i++){
			JSONObject json = jaDetail.getJSONObject(i);
			OrderDetail orderDetail = new OrderDetail();
			String itemId=json.getString("item-id");
			if(itemId==null||itemId.trim().length()==0){
				System.out.println("Item id can't be null.");
				status=1;
			}else
				orderDetail.setItemId(itemId);
			orderDetail.setSeqNo(json.getInt("seq"));
			orderDetail.setQty(json.getInt("qty"));
			orderDetail.setPrice(new BigDecimal(json.getString("price")));
			orderDetail.setTotalAmount(new BigDecimal(json.getString("total-amount")));
			orderDetail.setDiscAble(json.getInt("discount-able"));
			//"discount":{"id":"","disc-type":,"rate":,"desc":"","desc2":""},"discount-amount":,
			if(json.containsKey("discount")){
				JSONObject jDisc=json.getJSONObject("discount");
				if(jDisc==null||jDisc.isEmpty()||jDisc.isNullObject()){
					orderDetail.setDiscount(null);
				}else{
					Discount disc = new Discount();
					disc.setDiscId(jDisc.getString("id"));
					disc.setDiscType(jDisc.getInt("disc-type"));
					disc.setDiscRate(new BigDecimal(jDisc.getString("rate")));
					disc.setDiscDesc(jDisc.getString("desc"));
					disc.setDiscDesc2(jDisc.getString("desc2"));
					orderDetail.setDiscount(disc);
				}
			}else
				orderDetail.setDiscount(null);
			String discAmount=json.getString("discount-amount");
			if(discAmount==null||discAmount.trim().length()==0){
				System.out.println("Discount amount can't be null.");
				status=1;
			}else{
				orderDetail.setDiscAmount(new BigDecimal(discAmount));
			}
			orderDetail.setDiscAble(json.getInt("service-charge-able"));
			if(json.containsKey("service-charge")){
				JSONObject svchg=json.getJSONObject("service-charge");
				if(svchg==null || svchg.isEmpty()||svchg.isNullObject()){
					orderDetail.setServiceCharge(null);
				}else{
					//"service-charge":{"id":,"desc":"","desc2":"","value":, "type":},"service-charge-amount":,
					ServiceCharge serviceCharge = new ServiceCharge();
					serviceCharge.setId(svchg.getString("id"));
					serviceCharge.setDesc(svchg.getString("desc"));
					serviceCharge.setDesc2(svchg.getString("desc2"));
					serviceCharge.setType(svchg.getInt("type"));
					serviceCharge.setValue(new BigDecimal(svchg.getString("value")));
					orderDetail.setServiceCharge(serviceCharge);
				}
			}else
				orderDetail.setServiceCharge(null);
			String svchgAmount=json.getString("service-charge-amount");
			if(svchgAmount==null||svchgAmount.trim().length()==0){
				System.out.println("service charge amount can be null.");
				status=1;
			}else{
				orderDetail.setSvchgAmount(new BigDecimal(svchgAmount));
			}
			//pay-amount:,cat-id:,desc:,desc2:,unit:,take-away:
			String payAmount=json.getString("pay-amount");
			if(payAmount==null||payAmount.trim().length()==0){
				System.out.println("pay amount can be null.");
				status=1;
			}else
				orderDetail.setPayAmount(new BigDecimal(payAmount));
			orderDetail.setCatId(json.getString("cat-id"));
			orderDetail.setDesc(json.getString("desc"));
			orderDetail.setDesc2(json.getString("desc2"));
			orderDetail.setUnit(json.getString("unit"));
			orderDetail.setTakeAway(json.getInt("take-away"));
			if(status==1)
				return null;
			else
				ls.add(orderDetail);
		}
		if(ls.size()==0)
			return null;
		else
			return ls;
	}
	
}
