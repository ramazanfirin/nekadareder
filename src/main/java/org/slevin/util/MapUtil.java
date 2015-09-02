package org.slevin.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

	public static HashMap<String,String> getDefaultMap(){
		HashMap<String,String> hashMap = new HashMap<String, String>();
		hashMap.put("__category__", ConstantsUtil.CATEGORY_DAIRE);
		hashMap.put("__sorting__", ConstantsUtil.SORTING_DESC);
		hashMap.put("__tapuDurumu__", ConstantsUtil.TAPU_DURUMU_EVET);
		
		return hashMap;
	}
	
	public static HashMap<String,String> getQueryMap(String category,String sorting,String tapuDurumu){
		HashMap<String,String> hashMap = new HashMap<String, String>();
		hashMap.put("__category__", category);
		hashMap.put("__sorting__", sorting);
		hashMap.put("__tapuDurumu__", tapuDurumu);
		
		return hashMap;
	}
}
