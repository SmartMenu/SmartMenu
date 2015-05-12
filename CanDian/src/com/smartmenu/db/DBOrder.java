package com.smartmenu.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartmenu.entity.Discount;
import com.smartmenu.entity.Order;
import com.smartmenu.entity.OrderDetail;
import com.smartmenu.entity.ServiceCharge;
import com.smartmenu.entity.Tax;


public class DBOrder{
	//tranNo, checkNo, SlipNo
	private synchronized Map<String, String> generateTranNo(String shopId, String posId){
		String sql="select id, seq, prefix, suffix, [length] from [dbo].[tranno] with (HOLDLOCK, TABLOCK) " +
					" where shop_id='"+shopId+"' and (pos_id='"+posId+"' or pos_id='') and (id='SALESTRANNO' or id='CHECKNO' or id='SLIPNO') order by pos_id desc;";
		System.out.println("GenerateTranNo: " + sql);
		List<Object> lsResult = DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				Map<String,String> map = new HashMap<String, String>();
				while(rs.next()){
					String id = rs.getString("id");
					String prefix=rs.getString("prefix");
					if(prefix==null)
						prefix="";
					String suffix=rs.getString("suffix");
					if(suffix==null)
						suffix="";
					int length = rs.getInt("length");
					int seq=rs.getInt("seq");
					String no=prefix+(seq+1)+suffix;
					if(no.length()<=length){
						for(int i=0;i<length-no.length();i++){
							no="0"+no;
						}
					}else{
						System.out.println(id + " sequence no is longer than expected.");
						continue;
					}
					map.put(id, no);
				}
				if(map.isEmpty())
					return null;
				List<Object> ls = new ArrayList<Object>();
				ls.add(map);
				return ls;
			}
		});
		
		if(lsResult == null || lsResult.size()== 0)
			return null;
		
		Map<String, String> map = (Map<String, String>)lsResult.get(0);
		return map;
	}

	private boolean updateTranNoSettings(String shopId, String posId){
		String sql="update [dbo].[tranno] set seq=seq+1 where shop_id='"+shopId+"' and (pos_id='"+posId+"' or pos_id='');";
		System.out.println("UpdateTranNoSetting: "+ sql);
		int count = DBCommonUtil.executeUpdateOrInsert(sql);
		if(count>0)
			return true;
		else
			return false;
	}
	
	public String addNewOrder(Order order, List<OrderDetail> lsOrderDetail) throws SQLException {
		String resultMsg;
		Connection conn;
		conn = DBConnection.getConnection();
		try {
			conn.setAutoCommit(false);
			Map<String, String> mapNos = this.generateTranNo(order.getShopId(), order.getPosId());
			if(mapNos!=null&&mapNos.containsKey("SALESTRANNO")){
				updateTranNoSettings(order.getShopId(), order.getPosId());
				String tranNo = mapNos.get("SALESTRANNO");
				order.setTranNo(tranNo);
				if(mapNos.containsKey("CHECKNO")){
					order.setCheckNo(mapNos.get("CHECKNO"));
				}else
					order.setCheckNo(" ");
				Map<String,String> mapOrderProperty = this.buildOrderProperty(order);
				
				if(mapNos.containsKey("SLIPNO"))
					mapOrderProperty.put("slip_no", "'"+mapNos.get("SLIPNO")+"'");
				String orderSql = this.buildInsertSql("[dbo].[sales_header]", mapOrderProperty);
				System.out.println("INSERT ORDER: " + orderSql);
			    int count = DBCommonUtil.executeUpdateOrInsert(orderSql);
			    if(count==0){
				   System.out.println("Insert order failed.");
				   conn.rollback();
				   resultMsg="ADD_ORDER_FAILED";
			    }else{
				   //insert details
				   StringBuffer detailsSql = new StringBuffer();
				   for(OrderDetail orderDetail: lsOrderDetail){
					   Map<String,String> detailProperty = this.buildOrderDetailProperty(order, orderDetail);
					   String sqlTmp = this.buildInsertSql("[dbo].[sales_details]", detailProperty);
					   detailsSql.append(sqlTmp);
				   }
				   System.out.println("INSERT DETAILS: "+detailsSql.toString());
				   int detail_count=DBCommonUtil.executeUpdateOrInsert(detailsSql.toString());
				   if(detail_count==0){
					   System.out.println("Insert order details failed.");
					   conn.rollback();
					   resultMsg="ADD_ORDER_DETAIL_FAILED";
				   }else{
					   //update table status
					   int result = modifyTableStatus(order);
					   
					   if(result==0){
						   System.out.println("Modify table status success.");
						   conn.commit();
						   resultMsg="SUCCESS";	
					   }else if(result==-1){
						   System.out.println("table_status operation_status = 130 or 131");
						   conn.rollback();
						   resultMsg="TABLE_UNAVAILABLE";				   
					   }else
					   {
						   System.out.println("modify table_status failed");
						   conn.rollback();
						   resultMsg="MODIFY_TABLE_STATUS_FAILED";	
					   }
				   }
			   }
			}else{
				resultMsg = "GENERATE_TRANNO_FAILED";
			}
			
		} catch (SQLException e) {
			conn.rollback(); 
			e.printStackTrace();
			resultMsg="SQL_ERROR";
		}
		conn.setAutoCommit(true);
		return resultMsg;
	}
	//update existing order and insert new details
	public String appendExistOrder(Order order, List<OrderDetail> lsNewOrderDetail) throws SQLException{
		String resultMsg="";
		Connection conn;
		conn = DBConnection.getConnection();
		try {
			conn.setAutoCommit(false);
			String updateSql="update [dbo].[sales_header] set svchg_amount="+order.getSvchgAmount().toPlainString()+
					", tax_amount="+order.getTaxAmount().toPlainString()+
					", subtotal_amount="+order.getSubtotalAmount().toPlainString()+
					", balance_amount="+order.getSubtotalAmount().toPlainString()+
					", total_amount="+order.getTotalAmount().toPlainString()+
					", modify_date=GETDATE(), modify_by='"+order.getUserId()+"', "
							+ " lastorder_by='"+order.getUserId()+"'"
					+ " where tran_no='"+order.getTranNo()+"' and shop_id='"+order.getShopId()+"' "
					+ " and pos_id='"+order.getPosId()+"'";
			System.out.println("Update order: " + updateSql);
			int count=DBCommonUtil.executeUpdateOrInsert(updateSql);
			if(count==0){
				System.out.println("Insert order failed.");
				conn.rollback();
				resultMsg="UPDATE_ORDER_FAILED";
			}else{
				//insert details
			   StringBuffer detailsSql = new StringBuffer();
			   for(OrderDetail orderDetail: lsNewOrderDetail){
				   Map<String,String> detailProperty = this.buildOrderDetailProperty(order, orderDetail);
				   String sqlTmp = this.buildInsertSql("[dbo].[sales_details]", detailProperty);
				   detailsSql.append(sqlTmp);
			   }
			   System.out.println("INSERT DETAILS: "+detailsSql.toString());
			   int detail_count=DBCommonUtil.executeUpdateOrInsert(detailsSql.toString());
			   if(detail_count==0){
				   System.out.println("Insert new order details failed.");
				   conn.rollback();
				   resultMsg="ADD_ORDER_DETAIL_FAILED";
			   }else{
				   System.out.println("update order details success.");
				   conn.commit();
				   resultMsg="SUCCESS";
			   }
			}
		}
		catch (SQLException e) {
			conn.rollback(); 
			e.printStackTrace();
			resultMsg="SQL_ERROR";
		}
		conn.setAutoCommit(true);
		return resultMsg;
	}
	
	private Map<String, String> buildOrderProperty(Order order){
		Map<String, String> map = new HashMap<String, String>();
		map.put("shop_id", "'"+order.getShopId()+"'");
		map.put("pos_id", "'"+order.getPosId()+"'");
		map.put("tran_type", "0");
		//map.put("tran_date", "'"+order.getTranDate()+"'");
		map.put("tran_date", "GETDATE()");
		map.put("tran_no", "'"+order.getTranNo()+"'");
		map.put("check_no", "'"+order.getCheckNo()+"'");
		map.put("check_date", "GETDATE()");
		map.put("table_no", "'"+order.getTableId()+"'");
		map.put("section_id", "'"+order.getSectionId()+"'");
		map.put("cur_rate1", "0");
		map.put("cur_rate2", "0");
		ServiceCharge svchg = order.getSvchg();
		if(svchg!=null){
			map.put("chg_id", "'"+svchg.getId()+"'");
			map.put("svchg_rate", svchg.getValue().toPlainString());
			map.put("svchg_desc1", "N'"+svchg.getDesc()+"'");
			map.put("svchg_desc2", "N'"+svchg.getDesc2()+"'");
			map.put("svchg_by", "'"+order.getUserId()+"'");
		}
		map.put("svchg_amount", order.getSvchgAmount().toPlainString());
		Tax tax = order.getTax();
		if(tax!=null){
			map.put("tax_id", "'"+tax.getTaxId()+"'");
			map.put("tax_rate", tax.getTaxRate().toPlainString());
		}else
			map.put("tax_id", "''");
		map.put("tax_amount", order.getTaxAmount().toPlainString());
		map.put("subtotal_amount", order.getSubtotalAmount().toPlainString());
		map.put("balance_amount", order.getTotalAmount().toPlainString());
		map.put("total_amount", order.getTotalAmount().toPlainString());
		map.put("table_code", order.getTableId());
		map.put("create_date", "GETDATE()");
		map.put("create_by", "'"+order.getUserId()+"'");
		map.put("modify_date", "GETDATE()");
		map.put("modify_by", "'"+order.getUserId()+"'");
		map.put("capture_systype", "0");
		//map.put("capture_systime", "GETDATE()");
		map.put("capture_reproc_req", "0");
		map.put("capture_reproc_status", "0");
		map.put("settled", "0");
		map.put("subtype", "1");
		map.put("table_type", "1");
		
		map.put("cashier_closeid", "''");
		map.put("start_id", "''");
		map.put("cashier_startid", "''");
		map.put("rec_shop", "''");
		map.put("receipt_no", "''");
		map.put("void_shop", "''");
		map.put("void_pos", "''");
		map.put("void_by", "''");
		map.put("void_reason_code", "''");
		map.put("void_reason_desc1", "''");
		map.put("void_reason_desc2", "''");
		map.put("void_reference", "''");
		map.put("table_suffix", "''");
		map.put("rec_pos", "''");
		map.put("seat_no", "''");
		map.put("cust_no", "''");
		map.put("cover", ""+order.getCover());
		map.put("cover_charge", "''");
		if(order.getDiscount()!=null){
			map.put("disc_id", "'"+order.getDiscount().getDiscId()+"'");
			map.put("disc_desc1", "N'"+order.getDiscount().getDiscDesc()+"'");
			map.put("disc_desc2", "N'"+order.getDiscount().getDiscDesc2()+"'");
			map.put("disc_type", ""+order.getDiscount().getDiscType());
			map.put("disc_by", "'"+order.getUserId()+"'");
			map.put("disc_ref", "''");
			
		}else{
			map.put("disc_id", "''");
			map.put("disc_desc1", "''");
			map.put("disc_desc2", "''");
			map.put("disc_type", "0");
			map.put("disc_by", "''");
			map.put("disc_ref", "''");
		}
		map.put("svchg_by", "''");
		map.put("minchg_id", "''");
		map.put("cashier_id", "''");
		map.put("sent_by", "''");
		map.put("firstorder_by", "'"+order.getUserId()+"'");
		map.put("lastorder_by", "'"+order.getUserId()+"'");
		return map;
	}
	private Map<String, String> buildOrderDetailProperty(Order order, OrderDetail detail){
		Map<String, String> map = new HashMap<String, String>();
		map.put("shop_id", "'"+order.getShopId()+"'");
		map.put("pos_id", "'"+order.getPosId()+"'");
		map.put("tran_type", "0");
		map.put("tran_no", "'"+order.getTranNo()+"'");
		map.put("check_no", "'"+order.getCheckNo()+"'");
		//map.put("tran_date", "'"+order.getTranDate()+"'");
		map.put("det_type", "0");
		map.put("seqno", ""+detail.getSeqNo()+"");
		map.put("subtype","0"); ///////////////////////////
		//map.put("link_row","");
		map.put("shift_no", "0");
		//map.put("[close]", "0");
		map.put("start_id", "' '");
		map.put("order_date", "GETDATE()");
		//map.put("level_no","");
		map.put("code","'"+detail.getItemId()+"'");
		//map.put("dept_id","");
		map.put("cat_id","'"+detail.getCatId()+"'");
		//map.put("class_id", "");
		map.put("desc1", "N'"+detail.getDesc()+"'");
		map.put("desc2", "N'"+detail.getDesc2()+"'");
		map.put("qty", ""+detail.getQty()+"");
		map.put("rate", "1");
		map.put("unit", "N'"+detail.getUnit()+"'");
		map.put("price_level", "1");
		map.put("price", detail.getPrice().toPlainString());
		map.put("amount", detail.getTotalAmount().toPlainString());
		map.put("discountable", ""+detail.getDiscAble());
		Discount disc = detail.getDiscount();
		if(disc!=null){
			map.put("disc_id", "'"+disc.getDiscId()+"'");
			map.put("disc_type", ""+disc.getDiscType());
			map.put("disc_rate", disc.getDiscRate().toPlainString());
			map.put("disc_by", "'"+order.getUserId()+"'");
		}else
			map.put("disc_type", "0");
		map.put("disc_amount", detail.getDiscAmount().toPlainString());
		map.put("total_amount", detail.getPayAmount().toPlainString());
		map.put("cdisc_amount", "0");
		map.put("svchargeable", detail.getSvchgAble()+"");
		ServiceCharge svchg = detail.getServiceCharge();
		if(svchg!=null){
			map.put("svchg_rate", svchg.getValue().toPlainString());
		}
		//map.put("taxable", value);
		//map.put("tax_id", value);
		map.put("tax_rate", "0");
		map.put("tax_amount", "0");
		map.put("net_amount", detail.getPayAmount().toPlainString());
		map.put("order_by", "'"+order.getUserId()+"'");
		map.put("order_shop", "'"+order.getShopId()+"'");
		map.put("order_pos", "'"+order.getPosId()+"'");
		//map.put("is_modifier", value);
		map.put("cost", "0");
		//map.put("stock_code", value);
		map.put("total_cost", "0");
		map.put("print_count", "0");
		map.put("ticket_print", "0");
		map.put("ticket_printed", "0");
		map.put("ticketprn_updcount", "0");
		map.put("buy_qty", "0");
		map.put("free_qty", "0");
		map.put("create_date", "GETDATE()");
		map.put("create_by", "'"+order.getUserId()+"'");
		map.put("modify_date", "GETDATE()");
		map.put("modify_by", "'"+order.getUserId()+"'");
		map.put("update_count", "1");
		map.put("sent_count", "0");
		map.put("sent_seq", "0");
		map.put("sent_by", "''");
		map.put("posted", "0");
		map.put("posted2", "0");
		map.put("posted3", "0");
		map.put("posted4", "0");
		map.put("posted5", "0");
		//map.put("plu_no", value);
		map.put("split_code", "''");
		map.put("source_table", "' '");
		map.put("modifier1_id","''");
		map.put("modifier1_value","0");
		map.put("modifier1_op","0");
		map.put("modifier2_id","''");
		map.put("modifier2_value","0");
		map.put("modifier2_op","0");
		map.put("rush","0");
		map.put("pantry","0");
		map.put("order_ticket","0");
		map.put("bonus_redeem","0");
		map.put("splitrev_amount","0");
		map.put("nonsales_amount","0");
		map.put("nonsales","0");
		map.put("upload_status1","0");
		map.put("upload_status2","0");
		map.put("upload_status3","0");
		map.put("upload_status4","0");
		map.put("upload_status5","0");
		//map.put("stock_flag",value);
		map.put("rush_count","0");
		map.put("capture_systype","0");
		//map.put("capture_systime","GETDATE()");
		map.put("capture_reproc_req","0");
		map.put("capture_reproc_status","0");
		map.put(" takeaway_mode",detail.getTakeAway()+"");
/////////////////		
		map.put("ivoid_qty", "0");
		map.put("ivoid_amount", "0");
		map.put("ivoid_total", "0");
		map.put("ivoid_printed", "0");
		map.put("ivoid_ktprinted", "0");
		return map;
	}
	
	private String buildInsertSql(String dbTableName, Map<String, String> mapProperty){
		StringBuffer propertyPart = new StringBuffer();
		StringBuffer valuePart = new StringBuffer();
		for(String key: mapProperty.keySet()){
			propertyPart.append(key+",");
			valuePart.append(mapProperty.get(key)+",");
		}
		String sql="insert into " + dbTableName + " ("+propertyPart.substring(0, propertyPart.length()-1)+") " +
					" values ("+valuePart.substring(0, valuePart.length()-1)+");";
		System.out.println("Insert sql: " + sql);
		return sql;
	}
	//Œ¥≈–∂œ «∑Ò“—∏∂øÓ
	public Order getExistOrder(String shopId, String posId, String tableId){
		String sql="select top 1 shop_id, pos_id, tran_type, tran_no, check_no, tran_date, table_no, section_id, " +
				   " svchg_amount, tax_amount,disc_amount,subtotal_amount,total_amount " + 
				   " from dbo.sales_header " +
				   " where shop_id='"+shopId+"' and pos_id='"+posId+"' and table_no='"+tableId+"' and settled=0 order by tran_date desc;";
		System.out.println("GetExistOrder: " + sql);
		List<Object> lsResult = DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> ls = new ArrayList<Object>();
				while(rs.next()){
					Order order = new Order();
					order.setShopId(rs.getString("shop_id"));
					order.setPosId(rs.getString("pos_id"));
					order.setTranNo(rs.getString("tran_no"));
					order.setCheckNo(rs.getString("check_no"));
					order.setTranDate(rs.getTimestamp("tran_date"));
					order.setSectionId(rs.getString("section_id"));
					order.setSvchgAmount(rs.getBigDecimal("svchg_amount"));
					order.setTaxAmount(rs.getBigDecimal("tax_amount"));
					order.setDiscAmount(rs.getBigDecimal("disc_amount"));
					order.setSubtotalAmount(rs.getBigDecimal("subtotal_amount"));
					order.setTotalAmount(rs.getBigDecimal("total_amount"));
					ls.add(order);
				}
				if(ls==null || ls.size()==0)
					return null;
				else
					return ls;
			}
			
		});
		
		if(lsResult==null || lsResult.size()==0)
			return null;
		else
			return (Order)lsResult.get(0);
		
	}
	
	public OrderDetail[] getOrderDetail(String shopId, String posId, String tranNo){
		String sql="  select a.code as item_id, a.seqno, a.qty, a.price, a.desc1 as name, a.desc2 as name2, a.cat_id,b.desc1 as cat_name, b.desc2 as cat_name2 " +
					" from dbo.sales_details a, dbo.category b " +
					" where a.cat_id=b.cat_id and "	+ 
					" shop_id='"+shopId+"' and pos_id='"+posId+"' and tran_no='"+tranNo+"';";
		System.out.println("Exist order details: " + sql);
		List<Object> lsResult = DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> ls=new ArrayList<Object>();
				while(rs.next()){
					OrderDetail detail = new OrderDetail();
					detail.setCatId(rs.getString("cat_id"));
					detail.setCatName(rs.getString("cat_name"));
					detail.setCatName2(rs.getString("cat_name2"));
					detail.setPrice(rs.getBigDecimal("price"));
					detail.setQty(rs.getInt("qty"));
					detail.setItemId(rs.getString("item_id"));
					detail.setDesc(rs.getString("name"));
					detail.setDesc2(rs.getString("name2"));
					detail.setSeqNo(rs.getInt("seqno"));
					ls.add(detail);
				}
				if(ls==null || ls.size()==0)	
					return null;
				else
					return ls;
			}
			
		});
		if(lsResult==null||lsResult.size()==0)
			return null;
		else
			return lsResult.toArray(new OrderDetail[]{});
	}
	
	private Map<String, String> buildTableStatusProperty(Order order){
		Map<String, String> map=new HashMap<String, String>();
		map.put("table_id", "'"+order.getTableId()+"'");
		map.put("operation_status", "2");
		map.put("status_time", "GETDATE()");
		map.put("cover", ""+order.getCover());
		map.put("create_date", "GETDATE()");////////////
		map.put("create_by", "'"+order.getUserId()+"'");
		map.put("modify_date", "GETDATE()");
		map.put("modify_by", "'"+order.getUserId()+"'");
		map.put("shop_id", "'"+order.getShopId()+"'");
		map.put("table_code", "'"+order.getTableId()+"'");
		map.put("pos_id", "'"+order.getPosId()+"'");
		map.put("tran_no", "'"+order.getTranNo()+"'");
		map.put("check_no", "'"+order.getCheckNo()+"'");
		map.put("tran_date", "GETDATE()");
		map.put("check_date", "GETDATE()");
		map.put("total_amount", order.getTotalAmount().toPlainString());
		return map;
	}
	
	
	private int modifyTableStatus(Order order){
		String tableId=order.getTableId();
		String sql = "select * from [dbo].[table_status] where table_id='"+tableId+"' and (operation_status=130 or operation_status=131);";
		boolean flag=DBCommonUtil.checkExist(sql);
		if(flag)
			return -1;
		String tsSql="delete from [dbo].[table_status] where table_id='"+tableId+"';"; 
		DBCommonUtil.executeUpdateOrInsert(tsSql);
		tsSql=this.buildInsertSql("[dbo].[table_status]", this.buildTableStatusProperty(order));
		int ts_count=DBCommonUtil.executeUpdateOrInsert(tsSql);
		if(ts_count==0)
			return 1;
		else
			return 0;
	}
}
