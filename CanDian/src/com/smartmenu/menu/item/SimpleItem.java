package com.smartmenu.menu.item;

import net.sf.json.JSONObject;

import com.smartmenu.entity.Category;
import com.smartmenu.entity.Item;
import com.smartmenu.menu.modifier.Modifier;

public class SimpleItem extends BasicItem{
	private Category category=null;
	private Modifier modifier=null;
	
	public SimpleItem(){
		
	}
	
	public SimpleItem(Item item){
		super(item);
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Modifier getModifier() {
		return modifier;
	}
	public void setModifier(Modifier modifier) {
		this.modifier = modifier;
	}
	
	public JSONObject toJson(){
		JSONObject json = super.toJson();
		json.put("subtype", 0); //ÆÕÍ¨ÏîÄ¿
		if(category == null){
			json.put("cat-id", " ");
			json.put("cat-name", " ");
			json.put("cat-name2", " ");
		}
		else{
			json.put("cat-id", category.getcId());
			json.put("cat-name", category.getcName());
			json.put("cat-name2", category.getcName2());
		}
		if(modifier != null){
			json.put("modifier", modifier.toJson());
		}
		return json;
	}
}
