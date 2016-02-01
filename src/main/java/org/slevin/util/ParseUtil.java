package org.slevin.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slevin.common.BinaQueryItem;
import org.slevin.common.Emlak;
import org.slevin.common.EmlakRawData;
import org.slevin.common.Ilce;
import org.slevin.common.Mahalle;
import org.slevin.common.Sehir;
import org.slevin.common.Semt;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ParseUtil {
public static void main(String[] args) throws IOException {
	
	Reader reader = new FileReader("C:\\Users\\ETR00529\\Desktop\\sahibinden\\outputForSingle.txt");
	
	JsonValue value = Json.parse(reader);
	JsonObject object  = value.asObject();
	
	Boolean success = object.get("success").asBoolean();
	System.out.println("success:"+success);
	
	JsonObject response = object.get("response").asObject();
	response.get("id");
	
	JsonArray crumb=response.get("categoryBreadcrumb").asArray();
	JsonObject a = crumb.get(0).asObject();
	a.get("label");
	
	JsonArray sections=response.get("sections").asArray();
	JsonObject b = sections.get(0).asObject();
	JsonArray preferences = b.get("attributes").asArray();
	JsonObject emlaktipi = preferences.get(0).asObject();
	emlaktipi.get("value");
	JsonObject m2 = preferences.get(1).asObject();
	m2.get("value");
	
	System.out.println(emlaktipi.get("value")+ " "+ m2.get("value"));
	
	System.out.println("bitti");
}

public static  List<Emlak> parseRawData(BinaQueryItem item) throws Exception{
	List<Emlak> emlakList = new ArrayList<Emlak>();
	
	
	JsonValue value = Json.parse(item.getContent());
	JsonObject object  = value.asObject();
	
	Boolean success = object.get("success").asBoolean();
	System.out.println("success:"+success);
	
	JsonObject response = object.get("response").asObject();
	response.get("id");
	
	JsonArray array = response.get("classifieds").asArray();
	
	
	for (Iterator iterator = array.iterator(); iterator.hasNext();) {
		JsonObject jsonObject = (JsonObject) iterator.next();
		Emlak emlak;
		try {
			emlak = parseEmlakData(jsonObject,item.getCreditSutitable());
			emlakList.add(emlak);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	return emlakList;
}


public static  List<EmlakRawData> parseRawDataForSave(BinaQueryItem item) throws Exception{
	List<EmlakRawData> emlakList = new ArrayList<EmlakRawData>();
	
	
	JsonValue value = Json.parse(item.getContent());
	JsonObject object  = value.asObject();
	
	Boolean success = object.get("success").asBoolean();
	System.out.println("success:"+success);
	
	JsonObject response = object.get("response").asObject();
	response.get("id");
	
	JsonArray array = response.get("classifieds").asArray();
	
	
	for (Iterator iterator = array.iterator(); iterator.hasNext();) {
		JsonObject jsonObject = (JsonObject) iterator.next();
		EmlakRawData emlak;
		try {
			emlak = parseEmlakRawData(jsonObject,item.getCreditSutitable());
			emlakList.add(emlak);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	return emlakList;
}

public static EmlakRawData parseEmlakRawData(JsonObject object,Boolean krediUygun){
	EmlakRawData emlakRawData= new EmlakRawData();
	Long id = new Long(object.get("id").toString().replace("\"", ""));
	emlakRawData.setIlanNo(id);
	emlakRawData.setContent(object.toString());
	emlakRawData.setCreditSutitable(krediUygun);
	return emlakRawData;
}


public static Emlak parseEmlakData(JsonObject object,Boolean krediUygun) throws ParseException{
	Emlak emlak= new Emlak();
	
	emlak.setIlanNo(new Long(object.get("id").toString().replace("\"","")));
	emlak.setFiyat(object.get("price").toString().replace("\"",""));
	emlak.setCurrency(object.get("currency").toString());
	emlak.setLat(object.get("latitude").toString());
	emlak.setLng(object.get("longitude").toString());
	emlak.setEmlakTipi(object.get("categoryShortName").asString());
	
	String location = object.get("location").asString();
	String[] temp = location.split(",");
	
	emlak.setSehir(temp[0]);
	emlak.setIlce(temp[1]);
	emlak.setMah(temp[2]);
	
	JsonObject attributes = object.get("attributes").asObject();
	emlak.setKimden(attributes.get("a27").toString());
	emlak.setKullanimDurumu(attributes.get("a98426").toString());
	emlak.setBanyoSayisi(attributes.get("a22").toString().replace("\"",""));
	emlak.setBulunduguKat(attributes.get("a811").toString().replace("\"",""));
	emlak.setBinaKatSayisi(attributes.get("a810").toString().replace("\"",""));
	emlak.setM2(attributes.get("a24").toString().replace("\"",""));
	emlak.setOdaSayisi(attributes.get("a20").toString());
	emlak.setBinaYasi(attributes.get("a812").toString());
	//gun ay yıl ekle.
	if(attributes.get("a103651")!=null)
		emlak.setSiteIcinde(attributes.get("a103651").toString());
	
	if(attributes.get("a23")!=null)
		emlak.setIsitma(attributes.get("a23").toString());
	
	emlak.setKrediyeUygun(krediUygun.toString());
	
	String date = object.get("classifiedDate").toString().replace("\"", "");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	Date ilanTarihi = sdf.parse(date);
	
	Calendar cal = Calendar.getInstance();
    cal.setTime(ilanTarihi);
	emlak.setYil(String.valueOf(cal.get(Calendar.YEAR)));
	emlak.setAy(String.valueOf(cal.get(Calendar.MONTH)));
	emlak.setGun(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
	return emlak;
	
}


public static List<Sehir> parseSehirList(String result){
	List<Sehir> sehirList = new ArrayList<Sehir>();
	

	
	
	JsonValue value = Json.parse(result);
	JsonObject object  = value.asObject();
	
	Boolean success = object.get("success").asBoolean();
	System.out.println("success:"+success);
	
	JsonObject response = object.get("response").asObject();
	response.get("id");
	
	JsonArray array = response.get("cities").asArray();
	for (Iterator iterator = array.iterator(); iterator.hasNext();) {
		JsonObject object3 = (JsonObject) iterator.next();
		Sehir sehir = new Sehir();
		sehir.setName(object3.get("label").asString());
		sehir.setItemId(object3.get("id").asString());
		sehirList.add(sehir);
	}
	
	return sehirList;
}

public static List<Ilce> parseIlceList(String result){
	List<Ilce> ilceList = new ArrayList<Ilce>();

	JsonValue value = Json.parse(result);
	JsonObject object  = value.asObject();
	
	Boolean success = object.get("success").asBoolean();
	System.out.println("success:"+success);
	
	JsonObject response = object.get("response").asObject();
	response.get("id");
	
	JsonArray array = response.get("towns").asArray();
	for (Iterator iterator = array.iterator(); iterator.hasNext();) {
		JsonObject object3 = (JsonObject) iterator.next();
		Ilce ilce = new Ilce();
		ilce.setName(object3.get("label").asString());
		ilce.setItemId(object3.get("id").asString());
		ilceList.add(ilce);
	}
	
	return ilceList;
}

public static List<Semt> parseSemtList(String result){
	List<Semt> semtList = new ArrayList<Semt>();

	JsonValue value = Json.parse(result);
	JsonObject object  = value.asObject();
	
	Boolean success = object.get("success").asBoolean();
	System.out.println("success:"+success);
	
	JsonObject response = object.get("response").asObject();
	response.get("id");
	
	JsonArray array = response.get("districts").asArray();
	for (Iterator iterator = array.iterator(); iterator.hasNext();) {
		JsonObject object3 = (JsonObject) iterator.next();
		Semt semt = new Semt();
		semt.setName(object3.get("label").asString());
		semt.setItemId(object3.get("id").toString());
		
		if(object3.get("quarters")!=null){
			JsonArray quarters =object3.get("quarters").asArray();
			for (Iterator iterator2 = quarters.iterator(); iterator2.hasNext();) {
				JsonObject querter = (JsonObject) iterator2.next();
				Mahalle mahalle = new Mahalle();
				mahalle.setName(querter.get("label").asString());
				mahalle.setItemId(querter.get("id").toString());
				semt.getMahalleList().add(mahalle);
			}
		}
		semtList.add(semt);
	}
	
	return semtList;
}


public static Emlak parseSingleEmlakData(EmlakQueryItem emlak,String result) throws ParseException{
	//Emlak emlak= new Emlak();
	JsonValue value = Json.parse(result);
	JsonObject objectMain  = value.asObject();
	
	JsonObject object = objectMain.get("response").asObject();
	
	emlak.setIlanNo(new Long(object.get("id").toString().replace("\"","")));
	emlak.setFiyat(object.get("price").toString().replace("\"",""));
	emlak.setCurrency(object.get("currency").toString().replace("\"",""));
	emlak.setLat(object.get("latitude").toString());
	emlak.setLng(object.get("longitude").toString());
	//
	try {
		JsonArray images = object.get("images").asArray();
		JsonObject imeges1= images.get(0).asObject();
		emlak.setImageUrl(imeges1.get("normal").toString().replace("\"", ""));
		
		emlak.setDescription(object.get("title").toString().replace("\"", ""));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	String location = object.get("location").asString();
	String[] temp = location.split(",");
	
	emlak.setSehir(temp[0].replaceFirst(" ", ""));
	emlak.setIlce(temp[1].replaceFirst(" ", ""));
	emlak.setMah(temp[2].replaceFirst(" ", ""));
	JsonArray sections = object.get("sections").asArray();
	JsonObject attributesss = sections.get(0).asObject();
	JsonArray attributesArray = attributesss.get("attributes").asArray();
	
	
	emlak.setKrediyeUygun(getObjectFromArray(attributesArray, "Krediye Uygun"));
	emlak.setEmlakTipi(getObjectFromArray(attributesArray, "Emlak Tipi"));
	emlak.setKimden(getObjectFromArray(attributesArray, "Kimden"));
	emlak.setKullanimDurumu(getObjectFromArray(attributesArray, "Kullanım Durumu"));
	emlak.setBanyoSayisi(getObjectFromArray(attributesArray, "Banyo Sayısı"));
	emlak.setBulunduguKat(getObjectFromArray(attributesArray, "Bulunduğu Kat"));
	emlak.setBinaKatSayisi(getObjectFromArray(attributesArray, "Kat Sayısı"));
	emlak.setM2(getObjectFromArray(attributesArray, "m²"));
	emlak.setOdaSayisi(getObjectFromArray(attributesArray, "Oda Sayısı"));
	emlak.setBinaYasi(getObjectFromArray(attributesArray, "Bina Yaşı"));
	//gun ay yıl ekle.
	
		emlak.setSiteIcinde(getObjectFromArray(attributesArray, "Site İçerisinde"));
	
	
		emlak.setIsitma(getObjectFromArray(attributesArray, "Isıtma"));
	
	//emlak.setKrediyeUygun(krediUygun.toString());
	
	String date = object.get("classifiedDate").toString().replace("\"", "");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	Date ilanTarihi = sdf.parse(date);
	
	Calendar cal = Calendar.getInstance();
    cal.setTime(ilanTarihi);
	emlak.setYil(String.valueOf(cal.get(Calendar.YEAR)));
	emlak.setAy(String.valueOf(cal.get(Calendar.MONTH)));
	emlak.setGun(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
	return emlak;
	
	
}	
	
public static String getObjectFromArray(JsonArray attributesArray,String name){
	for (int i = 0; i < attributesArray.size(); i++) {
		JsonObject object = attributesArray.get(i).asObject();
		String key = object.get("name").asString();
		if(key.equals(name)){
			//System.out.println("aa");
			return object.get("value").toString().replace("\"","");
		}
		}
	return "";
	
}

public static String getStringFromInputStream(InputStream is) {

	BufferedReader br = null;
	StringBuilder sb = new StringBuilder();

	String line;
	try {

		br = new BufferedReader(new InputStreamReader(is));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	return sb.toString();

}

public static String getValueFromAzureResponse(String string) throws UnsupportedEncodingException{
	String result="";
	string = string.replace("b'", "");
	string = string.replace("'", "");
	
	String s2 = new String(string.getBytes(), "UTF-8");
	s2 = s2.replace("{\"Results\":{\"output1\":{\"type\":\"table\",\"value\":", "");
	s2 = s2.replace("{\"ColumnNames\":[\"fiyat\",\"il\",\"ilce\",\"mahalle\",\"krediyeUygun\",\"emlaktipi\",\"yil\",\"m2\",\"odasayisi\",\"banyosayisi\",\"binayasi\",\"binakatsayisi\",\"bulundugukat\",\"isitma\",\"kullanimdurumu\",\"siteicinde\",\"kimden\",\"Scored Labels\"],", "");
	s2 = s2.replace("\"ColumnTypes\":[\"Double\",\"String\",\"String\",\"String\",\"Boolean\",\"String\",\"Int32\",\"Int32\",\"Int32\",\"Int32\",\"Int32\",\"Int32\",\"Int32\",\"String\",\"String\",\"String\",\"String\",\"Double\"],", "");
	
	String[] aa = s2.split(",");
	//JsonValue value = Json.parse(s2);
	//JsonObject object  = value.asObject();
	result = aa[17].replace("]]}}}}", "").replace("\"","");
	
	
	return result;
}


}
