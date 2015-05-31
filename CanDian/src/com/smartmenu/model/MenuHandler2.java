package com.smartmenu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.smartmenu.db.DBMenu;
import com.smartmenu.entity.Item;
import com.smartmenu.entity.LookupCategory;
import com.smartmenu.entity.LookupDetail;
import com.smartmenu.menu.Lookup;
import com.smartmenu.menu.item.BasicItem;
import com.smartmenu.menu.item.SetItem;
import com.smartmenu.menu.item.SimpleItem;
import com.smartmenu.menu.modifier.ModifierContainer;
import com.smartmenu.menu.modifier.ModifierDetail;
import com.smartmenu.menu.setter.SetterContainer;
import com.smartmenu.menu.setter.SetterDetail;

public class MenuHandler2 {
private DBMenu dbMenu=null;
	
	public MenuHandler2(){
		dbMenu = new DBMenu();
	}
	public JSONObject getMenu(String shopId, String posId, String deviceId){
		JSONObject json = new JSONObject();
		JSONArray jaMsg = new JSONArray();
		JSONArray jaData = new JSONArray();
		
		List<Lookup> ls = this.organizeItem(shopId, posId, deviceId);
		if(ls==null || ls.size()==0){
			jaMsg.add("MENU_IS_NULL");
			json.put("status", 1);
			json.put("msg", jaMsg);
			json.put("data", "");
			return json;
		}else{
			jaMsg.add("SUCCESS");
		}
		
		for(Lookup lookup: ls){
			JSONObject jLc = new JSONObject();
			jLc.put("lookup-id", lookup.getLookupId());
			jLc.put("lookup-name", lookup.getLookupName());
			jLc.put("lookup-name2", lookup.getLookupName2());
			jLc.put("lookup-type", lookup.getLookupType());
			List<BasicItem> lsItems = lookup.getLsItems();
			
			JSONArray jaItems = new JSONArray();
			if(lsItems!=null){
				for(BasicItem bi:lsItems){
					JSONObject jItem = bi.toJson();
					jaItems.add(jItem);
				}
			}
			jLc.put("items", jaItems);				
			jaData.add(jLc);
		}
		json.put("status", 0);
		json.put("msg", jaMsg);
		json.put("data", jaData);
		
		return json;
	}
	private List<Lookup> organizeItem(String shopId, String posId, String deviceId){
		Item[] items = dbMenu.getItems(shopId);

		Map<String, Item> mapItem=new HashMap<String, Item>();
		if(items!=null){
			for(Item item: items){
				mapItem.put(item.getItemId(), item);
			}
		}
		SetterContainer[] setters = dbMenu.getSetterLookup(shopId);
		Map<String, SetterContainer> mapSetter = new HashMap<String, SetterContainer>();
		if(setters != null){
			for(SetterContainer setter: setters){
				mapSetter.put(setter.getId(), setter);
			}
		}
		ModifierContainer[] modifiers = dbMenu.getModifierLookup(shopId);
		Map<String, ModifierContainer> mapModifier = new HashMap<String, ModifierContainer>();
		if(modifiers!=null){
			for(ModifierContainer modifier: modifiers){
				mapModifier.put(modifier.getId(), modifier);
			}
		}
		LookupCategory[] shownLookups = dbMenu.getShownLookup(shopId, posId, deviceId);
		Map<String, LookupCategory> mapShownLookup = new HashMap<String, LookupCategory>();
		if(shownLookups!=null){
			for(LookupCategory lc: shownLookups){
				mapShownLookup.put(lc.getLookupId(), lc);
			}
		}
		Map<String, BasicItem> newMapBasicItem = new HashMap<String, BasicItem>();
		Map<String, Lookup> mapLookup = new HashMap<String, Lookup>();
		
		LookupDetail[] lookupDetails = dbMenu.getLookupDetail(shopId);
		for(LookupDetail lookupDetail: lookupDetails){
			String lookupId = lookupDetail.getLookupId();
			int lookupType = lookupDetail.getLookupType();
			int itemType = lookupDetail.getItemType();
			String code = lookupDetail.getCode();
			//int seq = lookupDetail.getSeq();
			if(lookupType == LookupDetail.MODIFIER_FLAG_FOR_LOOKUPTYPE && mapModifier.containsKey(lookupId)){
				ModifierContainer m = mapModifier.get(lookupId);
				if(itemType==LookupDetail.ITEM_FLAG_FOR_ITEMTYPE && mapItem.containsKey(code)){
					ModifierDetail md = new ModifierDetail();
					Item item = mapItem.get(code);
					md.setId(item.getItemId());
					md.setName(item.getItemName());
					md.setName2(item.getItemName2());
					md.setPrice(item.getItemPrice());
					//md.setSeq(seq);
					m.addModifier(md);
				}else if(itemType==LookupDetail.GROUP_FLAG_FOR_ITEMTYPE && mapModifier.containsKey(code)){
					ModifierContainer mc = mapModifier.get(code);
					m.addModifier(mc);
				}
			}else if(lookupType== LookupDetail.SETTER_FLAG_FOR_LOOKUPTYPE && mapSetter.containsKey(lookupId)){
				SetterContainer sc = mapSetter.get(lookupId);
				if(itemType==LookupDetail.ITEM_FLAG_FOR_ITEMTYPE && mapItem.containsKey(code)){
					SimpleItem si;
					Item item = mapItem.get(code);
					if(newMapBasicItem.containsKey(code)){
						BasicItem bi = newMapBasicItem.get(code);
						if(bi instanceof SimpleItem){
							si = (SimpleItem) bi;
						}else{
							System.out.println("ERROR: menu unstanding error");
							continue;
						}
					}else{
						si = new SimpleItem(item);
						if(item.getModifierId() != null && item.getModifierId().trim().length()!=0){
							ModifierContainer tmpMc = new ModifierContainer();
							tmpMc.setId(item.getModifierId());
							si.setModifier(tmpMc);
						}
						newMapBasicItem.put(si.getItemId(), si);
					}
						
					SetterDetail sd = new SetterDetail(si);
					sc.addSetter(sd);
				}else if(itemType==LookupDetail.GROUP_FLAG_FOR_ITEMTYPE && mapSetter.containsKey(code)){
					SetterContainer subSc = mapSetter.get(code);
					sc.addSetter(subSc);
				}
			}else if(lookupType == LookupDetail.NORMAL_FLAG_FOR_LOOKUPTYPE && mapShownLookup.containsKey(lookupId)){
				Lookup lookup;
				if(mapLookup.containsKey(lookupId))
					lookup = mapLookup.get(lookupId);
				else{
					LookupCategory lc = mapShownLookup.get(lookupId);
					lookup = new Lookup(lc);
					mapLookup.put(lookup.getLookupId(), lookup);
				}
								
				if(itemType == LookupDetail.ITEM_FLAG_FOR_ITEMTYPE && mapItem.containsKey(code)){
					Item item = mapItem.get(code);
					BasicItem bi; 
					if(newMapBasicItem.containsKey(code)){
						bi = newMapBasicItem.get(code);
					}
					else{
						if(item.getSetId() == null || item.getSetId().trim().length() == 0){
							SimpleItem si = new SimpleItem(item);
							if(item.getModifierId() != null && item.getModifierId().trim().length()!=0){
								ModifierContainer tmpMc = new ModifierContainer();
								tmpMc.setId(item.getModifierId());
								si.setModifier(tmpMc);
							}
							bi = si;
						}
						else{
							SetItem setItem = new SetItem(item);
							SetterContainer tmpSc = new SetterContainer();
							tmpSc.setId(item.getSetId());
							setItem.setSetter(tmpSc);
							bi = setItem;
						} 
						newMapBasicItem.put(bi.getItemId(), bi);
					}
					lookup.addItem(bi);
				}
			}
			
			
		}
		for(String itemId: newMapBasicItem.keySet()){
			BasicItem bi = newMapBasicItem.get(itemId);
			if(bi instanceof SimpleItem){
				SimpleItem si = (SimpleItem)bi;
				if(si.getModifier()!=null){
					String modifierId = si.getModifier().getId();
					if(mapModifier.containsKey(modifierId))
						si.setModifier(mapModifier.get(modifierId));
					else
						si.setModifier(null);
				}
			}
			if(bi instanceof SetItem){
				SetItem si = (SetItem)bi;
				if(si.getSetter()!=null){
					String setterId = si.getSetter().getId();
					if(mapSetter.containsKey(setterId))
						si.setSetter(mapSetter.get(setterId));
					else
						si.setSetter(null);
				}
			}
		}
		List<Lookup> lsResult = new ArrayList<Lookup>();
		for(LookupCategory lc : shownLookups){
			String lookupId = lc.getLookupId();
			if(mapLookup.containsKey(lookupId)){
				Lookup lookup = mapLookup.get(lookupId);
				lsResult.add(lookup);
			}
		}

		return lsResult;
	}
}
