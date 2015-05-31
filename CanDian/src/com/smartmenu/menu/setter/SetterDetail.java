package com.smartmenu.menu.setter;

import net.sf.json.JSONObject;

import com.smartmenu.menu.item.SimpleItem;

public class SetterDetail extends Setter{
	private SimpleItem item;
	public SetterDetail(SimpleItem item){
		this.item = item;
	}
	
	public JSONObject toJson(){
		JSONObject json = item.toJson();
		json.put("price", item.getItemSetPrice());
		json.put("seq", getSeq());
		json.put("subtype", 4);  //Ì×²Ísubtype=4,sale_detailsÖÐÓÃ
		return json;
	}
}
