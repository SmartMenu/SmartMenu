package com.smartmenu.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.smartmenu.db.DBMenu;
import com.smartmenu.entity.Item;
import com.smartmenu.entity.ItemState;
import com.smartmenu.entity.LookupCategory;

public class MenuHandler {
	private DBMenu dbMenu=null;
	
	public MenuHandler(){
		dbMenu = new DBMenu();
	}
	
	public JSONObject getMenu(String shopId, String posId, String deviceId){
		JSONObject json = new JSONObject();
		JSONArray jaMsg = new JSONArray();
		JSONArray jaData = new JSONArray();
		
		System.out.println(this.getClass().getName()+".getMenu(): get item info");
		Item[] items = dbMenu.getItems(shopId);
		System.out.println(this.getClass().getName()+".getMenu(): get lookup category info");
		LookupCategory[] lcs = dbMenu.getLookupCategory(shopId, posId, deviceId);
		System.out.println(this.getClass().getName()+".getMenu(): get mapping relationship between item and lookup category");
		Map<String, List<String[]>> map = dbMenu.getLookupAndItemMapping(shopId);
		
		int status=0;
		if(items==null || items.length == 0){
			status=1;
			jaMsg.add("ITEM_IS_NULL");
			System.out.println(this.getClass().getName()+".getMenu(): ITEM_IS_NULL");
		}
		if(lcs==null || lcs.length == 0){
			status=1;
			jaMsg.add("LOOKUP_IS_NULL");
			System.out.println(this.getClass().getName()+".getMenu(): LOOKUP_IS_NULL");
		}
		if(map==null || map.isEmpty()){
			status=1;
			jaMsg.add("MAPPING_IS_NULL");
			System.out.println(this.getClass().getName()+".getMenu(): MAPPING_IS_NULL");
		}
		if(status==1){
			json.put("status", status);
			json.put("msg", jaMsg);
			json.put("data", "");
			return json;
		}else{
			jaMsg.add("SUCCESS");
			System.out.println(this.getClass().getName()+".getMenu(): SUCCESS");
		}
		
		for(LookupCategory lc: lcs){
			List<String[]> lsItemIdsAndSeq = map.get(lc.getLookupId());
			if(lsItemIdsAndSeq==null || lsItemIdsAndSeq.size()==0)
				continue;
			List<Item> lsTmpItem = getItemsForLookupCategory(lsItemIdsAndSeq, items);
			JSONObject jLc = new JSONObject();
			jLc.put("lookup-id", lc.getLookupId());
			jLc.put("lookup-name", lc.getLookupName());
			jLc.put("lookup-name2", lc.getLookupName2());
			jLc.put("lookup-type", lc.getLookupType());
			
			JSONArray jaItems = new JSONArray();
			for(Item item:lsTmpItem){
				JSONObject jItem = new JSONObject();
				jItem.put("item-id", item.getItemId());
				jItem.put("item-name", item.getItemName());
				jItem.put("item_name2", item.getItemName2());
				jItem.put("item-seq", item.getSeq());
				jItem.put("price", item.getItemPrice());
				if(item.getCategory()==null){
					jItem.put("cat-id", " ");
					jItem.put("cat-name", " ");
					jItem.put("cat-name2", " ");
				}
				else{
					jItem.put("cat-id", item.getCategory().getcId());
					jItem.put("cat-name", item.getCategory().getcName());
					jItem.put("cat-name2", item.getCategory().getcName2());
				}
				jItem.put("item-pic", item.getItemPic()==null?" ": item.getItemPic());
				jaItems.add(jItem);
			}
			jLc.put("items", jaItems);
			jaData.add(jLc);
		}
		json.put("status", status);
		json.put("msg", jaMsg);
		json.put("data", jaData);
		
		return json;
	}
	
	private List<Item> getItemsForLookupCategory(List<String[]> lsItemIdsAndSeq, Item[] items){
		List<Item> lsResult = new ArrayList<Item>();
		for(String[] strs:lsItemIdsAndSeq){
			String itemId = strs[0];
			String seq=strs[1];
			for(Item item: items){
				if(item.getItemId().equals(itemId)){
					item.setSeq(Integer.valueOf(seq));
					lsResult.add(item);
				}
			}
		}
		return lsResult;
	}
	
	public JSONObject getItemSoldoutInfo(String shopId){
		JSONObject json = new JSONObject();
		JSONArray jaMsg = new JSONArray();
		JSONArray jaData = new JSONArray();
		
		ItemState[] itemStates = dbMenu.getItemStates(shopId);
		int status=0;
		if(itemStates==null){
			status=1;
			jaMsg.add("DB_ERROR");
		}else if (itemStates.length==0){
			status=1;
			jaMsg.add("ITEMSTATE_IS_NULL");
		}else{
			status=0;
			jaMsg.add("SUCCESS");
		}
		if(status==1){
			json.put("status", status);
			json.put("msg", jaMsg);
			json.put("data", "[]");
			return json;
		}
		
		for(ItemState itemState: itemStates){
			JSONObject jIS = new JSONObject();
			jIS.put("item-id", itemState.getItemId());
			if(itemState.getSoldoutBal()==null)
				jIS.put("soldout-bal", 0);
			else{
				jIS.put("soldout-bal", itemState.getSoldoutBal().intValue());
			}
			jIS.put("soldout-flag", itemState.getSoldoutFlag());
			jaData.add("jIS");
		}
		json.put("status", status);
		json.put("msg", jaMsg);
		json.put("data", jaData);
		return json;
	}
	
}
