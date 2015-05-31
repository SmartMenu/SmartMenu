package com.smartmenu.menu.item;

import net.sf.json.JSONObject;

import com.smartmenu.entity.Item;
import com.smartmenu.menu.setter.Setter;
import com.smartmenu.menu.setter.SetterContainer;

public class SetItem extends BasicItem{
	private Setter setter=null;
	
	public SetItem() {
		super();
	}

	public SetItem(Item item) {
		super(item);
	}

	public Setter getSetter() {
		return setter;
	}

	public void setSetter(Setter setter) {
		this.setter = setter;
	}
	
	public JSONObject toJson(){
		JSONObject json = super.toJson();
		if(setter==null)
			json.put("setter", "");
		else
			json.put("setter", setter.toJson());
		return json;
	}
}
