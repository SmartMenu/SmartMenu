package com.smartmenu.model;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.smartmenu.db.DBTable;
import com.smartmenu.db.DBUser;
import com.smartmenu.entity.User;

public class LoginHandler {
	private DBUser dbUser=null;
	private DBTable dbTable=null;
	public LoginHandler(){
		dbUser = new DBUser();
		dbTable = new DBTable();
	}
	public JSONObject dealLogin(String shopId, String userId, String password, String tableId){
		JSONObject json = new JSONObject();
		JSONArray jaMsg = new JSONArray();
		JSONObject jData = new JSONObject();
		int status=0;
		System.out.println(this.getClass().getName()+": get user info from db");
		User user=dbUser.getUseInfo(shopId, userId);
		if(user == null){
			status=1;
			jaMsg.add("USER_NOT_EXIST");
			System.out.println(this.getClass().getName()+": user doesn't exist.");
		}else{
			if(user.getPassword()==null && !user.getPassword().trim().equals(password.trim())){
				status=1;
				jaMsg.add("PASSWORD_NOT_RIGHT");
				System.out.println(this.getClass().getName()+": password error");
			}
			long currTime=System.currentTimeMillis();
			if(user!=null && user.getEffectiveDate()!=null&&user.getEffectiveDate().getTime()>currTime){
				status=1;
				jaMsg.add("USER_NOT_EFFECTIVE");
				System.out.println(this.getClass().getName()+": user is not effective");
			}
			if(user.getExpiryDate()!=null&&user.getExpiryDate().getTime()<currTime){
				status=1;
				jaMsg.add("USER_EXPIRY");
				System.out.println(this.getClass().getName()+": user is expiry");
			}
			String[] rights=dbUser.getRights(shopId, user.getGroupId());
			boolean rightFlag=false;
			for(String right: rights){
				if(right!=null&&right.equals("00001")){
					rightFlag=true;
					break;
				}
			}
			if(!rightFlag){
				status=1;
				jaMsg.add("USER_NOT_AUTHORITY");
				System.out.println(this.getClass().getName()+": user hasn't the right to operate");
			}
		}
		System.out.println(this.getClass().getName()+": check if tableno in db");
		if(!dbTable.checkTableId(tableId, shopId)){
			status=1;
			jaMsg.add("TABLE_NOT_EXIST");
			System.out.println(this.getClass().getName()+": table no doesn't exist.");
		}
		if(status==0){
			jaMsg.add("SUCCESS");
			jData.put("userId", userId);
			jData.put("tableId", tableId);
			jData.put("shopId", shopId);
			System.out.println(this.getClass().getName()+": pass validation.");
		}
		try {
			json.put("status", status);
			json.put("msg", jaMsg);
			json.put("data", jData);
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
