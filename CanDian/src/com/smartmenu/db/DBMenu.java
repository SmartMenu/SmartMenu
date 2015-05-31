package com.smartmenu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartmenu.entity.Category;
import com.smartmenu.entity.Item;
import com.smartmenu.entity.ItemState;
import com.smartmenu.entity.LookupCategory;
import com.smartmenu.entity.LookupDetail;
import com.smartmenu.menu.modifier.ModifierContainer;
import com.smartmenu.menu.setter.SetterContainer;

public class DBMenu{
	public Item[] getItems(String shopId){
		String sql="select item_id, item_name, item_name2, aa.plu_no, aa.unit, aa.cat_id, price, image_file, e.desc1 as cat_name, e.desc2 as cat_name2, aa.is_modifier, aa.modifier, aa.item_set " +
					"from (select a.item_code as item_id, a.pos_desc1 as item_name, a.pos_desc2 as item_name2, a.plu_no as plu_no, a.unit, a.cat_id, b.price, a.is_modifier, a.modifier1 as modifier, a.item_set as item_set " +
					"from [dbo].[item] a, [dbo].[item_price] b " + 
					"where a.item_code=b.item_code and b.shop_id='"+shopId+"' and b.price_no=1) aa " +
					"left join dbo.item_caption d " +
					"on d.item_code=aa.item_id "
					+ "left join [dbo].[category] e "
					+ "on e.cat_id=aa.cat_id ";
		
		System.out.println("GetItems:"+sql);
		List<Object> ls = DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){
			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> lsObj = new ArrayList<Object>();
				while(rs.next()){
					Item item = new Item();
					item.setItemId(rs.getString("item_id"));
					item.setItemName(rs.getString("item_name"));
					item.setItemName2(rs.getString("item_name2"));
					item.setPluNo(rs.getString("plu_no"));
					item.setUnit(rs.getString("unit"));
					if(rs.getString("cat_id")==null)
						item.setCategory(null);
					else{
						Category c = new Category(rs.getString("cat_id"), rs.getString("cat_name"), rs.getString("cat_name2"));
						item.setCategory(c);
					}
					item.setItemPrice(rs.getBigDecimal("price"));
					item.setItemPic(rs.getString("image_file"));
					item.setIsModifier(rs.getInt("is_modifier"));
					item.setModifierId(rs.getString("modifier"));
					item.setSetId(rs.getString("item_set"));
					lsObj.add(item);
				}
				if(lsObj.size()==0)
					return null;
				else
					return lsObj;
			}
			
		});
		if(ls==null|| ls.size()==0)
			return null;
		
		return ls.toArray(new Item[]{});
	}
	
	public LookupCategory[] getShownLookup(String shopId, String posId, String deviceId){
		String sql="select a.lookup_id, a.name1 as name, a.name2 as name2, b.seq, a.pictype_id " +
					" from dbo.lookup_header a, dbo.lookup_details b, dbo.device_map c" +
					" where c.shop_id='"+shopId+"' and c.device_id='"+deviceId+"' and c.pos_id='"+posId+
					"' and b.lookup_id=c.imenu_lookupid and a.id_type=1 and b.item_type=1 and a.lookup_id=b.code order by seq;";
		System.out.println("GetLookup: " + sql);
//		String sql="select a.code as lookup_id, a.name1 as name, a.name2 as name2, a.seq from dbo.lookup_details a " +
//				" where a.shop_id='"+shopId+"' and a.id_type=1 and a.item_type=1 and a.lookup_id='0000' order by seq asc;";
		List<Object> ls=DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> lsObj=new ArrayList<Object>();
				while(rs.next()){
					LookupCategory lc = new LookupCategory();
					lc.setLookupId(rs.getString("lookup_id"));
					lc.setLookupName(rs.getString("name"));
					lc.setLookupName2(rs.getString("name2"));
					lc.setLookupType(rs.getInt("pictype_id"));
					lsObj.add(lc);
				}
				if(lsObj.size()==0)
					return null;
				else
					return lsObj;
			}
			
		});
		if(ls==null || ls.size()==0)
			return null;
		else
			return ls.toArray(new LookupCategory[]{});
	}
	
	public Map<String,List<String[]>> getLookupAndItemMapping(String shopId){
		String sql="select lookup_id, code as item_id, seq " +
					" from dbo.lookup_details b " +
					" where b.shop_id='"+shopId+"' and b.id_type=1 and b.item_type=0 order by lookup_id, seq";
		System.out.println("GetLookupAndItemMapping: " + sql);
		List<Object> ls=DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> lsObj=new ArrayList<Object>();
				while(rs.next()){
					String lookupId = rs.getString("lookup_id");
					String itemId = rs.getString("item_id");
					String seq = rs.getString("seq");
					lsObj.add(new String[]{lookupId, itemId, seq});
				}
				if(lsObj.size()==0)
					return null;
				else
					return lsObj;
			}
			
		});
		if(ls==null||ls.size()==0)	
			return null;
		Map<String,List<String[]>> map = new HashMap<String, List<String[]>>();
		for(Object obj : ls){
			String[] strs = (String[])obj;
			List<String[]> lsStr = null;
			if(map.containsKey(strs[0]))
				lsStr = map.get(strs[0]);
			else
				lsStr = new ArrayList<String[]>();
			lsStr.add(new String[]{strs[1],strs[2]});
			map.put(strs[0], lsStr);
		}
		return map;
	}
	
	public Map<String, String[]> getItemDescription(String shopId){
		String sql="select item_code, description as desc from dbo.item_desc where shop_id='"+shopId+"' and lang=?";
		System.out.println("GetItemDescription: " + sql);
		List<Object> result = DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> ls = new ArrayList<Object>();
				while(rs.next()){
					String itemId = rs.getString("item_code");
					String itemDesc = rs.getString("desc");
					ls.add(new String[]{itemId, itemDesc});
				}
				if(ls==null || ls.size()==0)
					return null;
				else
					return ls;
			}	
		});
		//TODO
		
		return null;
	}
	//get item state
	public ItemState[] getItemStates(String shopId){
		String sql="select item_code as item_id, soldout_bal, soldout_flag from dbo.item_state "
				+ " where shop_id='"+shopId+"';";
		System.out.println("GetItemStates: " + sql);
		List<Object> result = DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> ls = new ArrayList<Object>();
				while(rs.next()){
					ItemState itemState = new ItemState();
					itemState.setItemId(rs.getString("item_id"));
					itemState.setSoldoutBal(rs.getBigDecimal("soldout_bal"));
					itemState.setSoldoutFlag(rs.getInt("soldout_flag"));
					ls.add(itemState);
				}
				return ls;
			}
			
		});
		
		if(result==null )
			return null;
		else if(result.size()==0)
			return new ItemState[]{};
		else
			return result.toArray(new ItemState[]{});
				
	}
	
	public ModifierContainer[] getModifierLookup(String shopId){
		String sql="select lookup_id, name1, name2, compulsory, min_count, max_count"
				+ " from dbo.lookup_header where shop_id='"+shopId+"' and lookup_type=2;";
		System.out.println("GetModifierLookup: " + sql);
		List<Object> ls=DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> lsObjs = new ArrayList<Object>();
				while(rs.next()){
					ModifierContainer modifier = new ModifierContainer();
					modifier.setId(rs.getString("lookup_id"));
					modifier.setName(rs.getString("name1"));
					modifier.setName2(rs.getString("name2"));
					modifier.setCompulsory(rs.getInt("compulsory"));
					modifier.setMinCount(rs.getInt("min_count"));
					modifier.setMaxCount(rs.getInt("max_count"));
					lsObjs.add(modifier);
				}
				return lsObjs;
			}
			
		});
		if(ls==null || ls.size() == 0)
			return null;
		else
			return ls.toArray(new ModifierContainer[]{});
	}
	
	public SetterContainer[] getSetterLookup(String shopId){
		String sql="select lookup_id, name1, name2, compulsory, min_count, max_count, select_all"
				+ " from dbo.lookup_header where shop_id='"+shopId+"' and lookup_type=3;";
		System.out.println("GetSetterLookup: " + sql);
		List<Object> ls=DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> lsObjs = new ArrayList<Object>();
				while(rs.next()){
					SetterContainer setter = new SetterContainer();
					setter.setId(rs.getString("lookup_id"));
					setter.setName(rs.getString("name1"));
					setter.setName2(rs.getString("name2"));
					setter.setCompulsory(rs.getInt("compulsory"));
					setter.setMinCount(rs.getInt("min_count"));
					setter.setMaxCount(rs.getInt("max_count"));
					int selectAll = rs.getInt("select_all");
					if(selectAll==1)
						setter.setDefaultSelectAll(true);
					else
						setter.setDefaultSelectAll(false);
					
					lsObjs.add(setter);
				}
				return lsObjs;
			}
			
		});
		if(ls == null || ls.size() == 0)
			return null;
		else
			return ls.toArray(new SetterContainer[]{});
	}

	public LookupDetail[] getLookupDetail(String shopId){
		String sql="select a.lookup_id, b.lookup_type, a.item_type, a.code, a.seq from dbo.lookup_details a, dbo.lookup_header b " +
					" where a.shop_id='" + shopId + "' and a.lookup_id=b.lookup_id and a.id_type=1 order by a.lookup_id, a.seq;";
		System.out.println("GetLookupDetail: "+ sql);
		List<Object> ls=DBCommonUtil.executeQuery(sql, new ParseResultSetInterface(){

			@Override
			public List<Object> parseResult(ResultSet rs) throws SQLException {
				List<Object> lsObjs = new ArrayList<Object>();
				while(rs.next()){
					LookupDetail lookupDetail = new LookupDetail();
					lookupDetail.setLookupId(rs.getString("lookup_id"));
					lookupDetail.setLookupType(rs.getInt("lookup_type"));
					lookupDetail.setCode(rs.getString("code"));
					lookupDetail.setItemType(rs.getInt("item_type"));
					lookupDetail.setSeq(rs.getInt("seq"));
					lsObjs.add(lookupDetail);
				}
				return lsObjs;
			}
			
		});
		if(ls == null || ls.size() == 0)
			return null;
		else
			return ls.toArray(new LookupDetail[]{});
	}
}
