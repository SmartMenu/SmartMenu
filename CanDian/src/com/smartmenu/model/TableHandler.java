package com.smartmenu.model;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.smartmenu.db.DBTable;
import com.smartmenu.entity.ServiceCharge;

public class TableHandler {
	private DBTable dbTable=null;
	public TableHandler(){
		dbTable = new DBTable();
	}
	public JSONObject dealServiceChargeReq(String tableId, String shopId){
		ServiceCharge serviceCharge = dbTable.getServiceCharge(tableId, shopId);
		JSONObject json = new JSONObject();
		JSONArray jaMsg = new JSONArray();
		JSONObject jData = new JSONObject();
		int status=0;
		if(serviceCharge==null){
			status=1;
			jaMsg.add("NOT_EXIST");
		}else{
			status=0;
			jaMsg.add("SUCCESS");
			jData.put("id", serviceCharge.getId());
			jData.put("desc", serviceCharge.getDesc());
			jData.put("value", serviceCharge.getValue());
		}
		json.put("status", status);
		json.put("msg", jaMsg);
		json.put("data", jData);
		return json;
	}
	public JSONObject dealTableSymbolReq(String shopId){
		String[] tables = dbTable.getTables(shopId);
		JSONObject json = new JSONObject();
		JSONArray jaMsg = new JSONArray();
		JSONArray jaData = new JSONArray();
		int status=0;
		if(tables==null){
			status=1;
			jaMsg.add("NOT_EXIST");
		}else{
			status=0;
		    jaMsg.add("SUCCESS");
			StringBuffer sb = new StringBuffer();
			for(String table: tables){
				char[] tmp = table.toCharArray();
				for(char c: tmp){
					if(sb.indexOf(String.valueOf(c))==-1)
						sb.append(c);
				}
			}
			char[] symbols = sb.toString().toCharArray();
			for(char c: symbols){
				jaData.add(c);
			}
		}
		json.put("status", status);
		json.put("msg", jaMsg);
		json.put("data", jaData);
		return json;
	}
	
	public JSONObject dealTableReq(String shopId){
		String[] tables = dbTable.getTables(shopId);
		JSONObject json = new JSONObject();
		JSONArray jaMsg = new JSONArray();
		JSONArray jaData = new JSONArray();
		int status=0;
		if(tables==null){
			status=1;
			jaMsg.add("NOT_EXIST");
		}else{
			status=0;
		    jaMsg.add("SUCCESS");
			StringBuffer sb = new StringBuffer();
			for(String table: tables){
				jaData.add(table);
			}
		}
		json.put("status", status);
		json.put("msg", jaMsg);
		json.put("data", jaData);
		return json;
	}
	
}
