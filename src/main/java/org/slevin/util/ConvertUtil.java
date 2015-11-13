package org.slevin.util;

import java.util.ArrayList;
import java.util.List;

import org.slevin.common.Emlak;

public class ConvertUtil {
public static String convertCVSFormat(Emlak emlak){
	StringBuffer result = new StringBuffer();
	result.append(preparePrice(emlak.getFiyatLong().longValue(),emlak.getCurrency()));
	result.append(",");
	result.append(appendQuata(emlak.getSehir()));
	result.append(",");
	result.append(appendQuata(emlak.getIlce()).replaceFirst("\\s+", ""));
	result.append(",");
	result.append(appendQuata(emlak.getMah()).replaceFirst("\\s+", ""));
	result.append(",");
	result.append(appendQuata(emlak.getKrediyeUygun()));
	result.append(",");
	result.append(appendQuata(emlak.getEmlakTipi()));
	result.append(",");
	result.append(emlak.getYil());
	result.append(",");
	result.append(emlak.getM2());
	result.append(",");
	result.append(prepareOdaSayisi(emlak.getOdaSayisi().replaceAll("\"", "")));
	result.append(",");
	result.append(prepareBanyoSayisi(emlak.getBanyoSayisi()));
	result.append(",");
	result.append(prepareBinaYasi(emlak.getBinaYasi().replaceAll("\"", "")));
	result.append(",");
	result.append(prepareBinaKatSayisi(emlak.getBinaKatSayisi()));
	result.append(",");
	result.append(prepareBulunduguKat(emlak.getBulunduguKat()));
	result.append(",");
	result.append(prepareIsitma(emlak.getIsitma()));
	result.append(",");
	result.append(prepareKullanimDurumu(emlak.getKullanimDurumu()));
	result.append(",");
	result.append(prepareSiteIcinde(emlak.getSiteIcinde()));
	result.append(",");
	result.append(prepareKimden(emlak.getKimden()));

	return result.toString();
}

public static String prepareIsitma(String string){
	if(string.equals("") || string.contains("-1"))
		return "Doğalgaz (Kombi)";
	else
		return string;
}

public static String prepareKullanimDurumu(String string){
	if(string.contains("-1"))
		return "Boş";
	else
		return string;
}

public static String prepareKimden(String string){
	if(string.equals("0") || string.contains("-1"))
		return "Sahibinden";
	else
		return string;
}

public static String prepareSiteIcinde(String string){
	if(string.equals("") || string.contains("-1"))
		return "Hayır";
	else
		return string;
}


public static String preparePrice(Long price,String currency){
	Double d = new Double(price);
	if(currency.contains("EUR"))
		d = d*ConstantsUtil.EUR;
	if(currency.contains("USD"))
		d = d*ConstantsUtil.USD;
	
	return d.toString();
}

public static String appendQuata(String string){
	return "\""+string+"\"";
}

public static String prepareOdaSayisi(String odaSayisi){
	String result ="";
	//System.out.println(odaSayisi);
	if(odaSayisi.equals("Stüdyo (1+0)"))
		return "0";
	if(odaSayisi.equals("10 Üzeri"))
		return "15";
	
	String[] temp=odaSayisi.split("\\+");
	Long a = new Long(temp[0]);
	Long b = new Long(0);
	if(temp.length>1)
		b = new Long(temp[1]);	
	Long c = a+b;
	
	return c.toString();
}


public static String prepareBanyoSayisi(String banyoSayisi){
	if(banyoSayisi.equals("Yok"))
		return "0";
	if(banyoSayisi.equals("6 Üzeri"))
		return "7";
	
	return banyoSayisi;
}

public static String prepareBinaYasi(String binaYasi){
	if(binaYasi.equals("5-10 arası"))
		return "7";
	if(binaYasi.equals("11-15 arası"))
		return "12";
	if(binaYasi.equals("16-20 arası"))
		return "17";
	if(binaYasi.equals("21-25 arası"))
		return "22";
	if(binaYasi.equals("26-30 arası"))
		return "17";
	if(binaYasi.equals("31 ve üzeri"))
		return "35";
	
	return binaYasi;
}

public static String prepareBinaKatSayisi(String binaKatSayisi){
	if(binaKatSayisi.equals("30 ve üzeri"))
		return "40";
	return binaKatSayisi;
}

public static String prepareBulunduguKat(String bulunduguKat){
	if(bulunduguKat.equals("Kot 4"))
		return "0";
	if(bulunduguKat.equals("Kot 3"))
		return "0";
	if(bulunduguKat.equals("Kot 2"))
		return "0";
	if(bulunduguKat.equals("Kot 1"))
		return "0";
	if(bulunduguKat.equals("Bodrum Kat"))
		return "1";
	if(bulunduguKat.equals("Zemin Kat"))
		return "1";
	if(bulunduguKat.equals("Bahçe Katı"))
		return "1";
	if(bulunduguKat.equals("Giriş Katı"))
		return "1";
	if(bulunduguKat.equals("Yüksek Giriş"))
		return "1";
	if(bulunduguKat.equals("Müstakil"))
		return "2";
	if(bulunduguKat.equals("Villa Tipi"))
		return "2";
	if(bulunduguKat.equals("Çatı Katı"))
		return "1";
	if(bulunduguKat.equals("30 ve üzeri"))
		return "3";
	
	
//	Long a = new Long(bulunduguKat);
//	a= a*100;
//	return a.toString();
	
	return bulunduguKat;
}

public static EmlakQueryItem convertToEmlakQueryItem(Emlak emlak){
	EmlakQueryItem emlakQueryItem = new EmlakQueryItem();
	emlakQueryItem.setAy(emlak.getAy().replace("\"", ""));
	emlakQueryItem.setBanyoSayisi(emlak.getBanyoSayisi().replace("\"", ""));
	emlakQueryItem.setBinaKatSayisi(emlak.getBinaKatSayisi().replace("\"", ""));
	emlakQueryItem.setBinaYasi(emlak.getBinaYasi().replace("\"", ""));
	emlakQueryItem.setBulunduguKat(emlak.getBulunduguKat().replace("\"", ""));
	emlakQueryItem.setCurrency(emlak.getCurrency().replace("\"", ""));
	emlakQueryItem.setEmlakTipi(emlak.getEmlakTipi().replace("\"", ""));
	emlakQueryItem.setExportComplated(emlak.getExportComplated());
	emlakQueryItem.setFiyat(emlak.getFiyat().replace("\"", ""));
	emlakQueryItem.setFiyatLong(emlak.getFiyatLong());
	
	emlakQueryItem.setGun(emlak.getGun().replace("\"", ""));
	emlakQueryItem.setId(emlak.getId());
	emlakQueryItem.setIlanNo(emlak.getIlanNo());
	//emlakQueryItem.setIlanTarihi(emlak.getIlanTarihi().replace("\"", ""));
	emlakQueryItem.setIlce(emlak.getIlce().replace("\"", "").replaceFirst(" ", ""));
	emlakQueryItem.setInsertDate(emlak.getInsertDate());
	
	emlakQueryItem.setIsitma(emlak.getIsitma().replace("\"", ""));
	emlakQueryItem.setKimden(emlak.getKimden().replace("\"", ""));
	emlakQueryItem.setKrediyeUygun(emlak.getKrediyeUygun().replace("\"", ""));
	
	emlakQueryItem.setKullanimDurumu(emlak.getKullanimDurumu().replace("\"", ""));
	emlakQueryItem.setLat(emlak.getLat().replace("\"", ""));
	emlakQueryItem.setLng(emlak.getLng().replace("\"", ""));
	
	emlakQueryItem.setM2(emlak.getM2().replace("\"", ""));
	emlakQueryItem.setMah(emlak.getMah().replace("\"", "").replaceFirst(" ", ""));
	emlakQueryItem.setOdaSayisi(emlak.getOdaSayisi().replace("\"", ""));
	emlakQueryItem.setSehir(emlak.getSehir().replace("\"", ""));
	emlakQueryItem.setSiteIcinde(emlak.getSiteIcinde().replace("\"", ""));
	
	emlakQueryItem.setTakas(emlak.getTakas().replace("\"", ""));
	emlakQueryItem.setYil(emlak.getYil().replace("\"", ""));
	return emlakQueryItem;
}


public static List<Object> convertToObjectList(EmlakQueryItem emlakQueryItem){
	List<Object> list = new ArrayList<Object>();
	list.add(emlakQueryItem.getSehir());
	list.add(emlakQueryItem.getIlce());
	list.add(emlakQueryItem.getMah());
	list.add(emlakQueryItem.getKrediyeUygun());
	list.add(emlakQueryItem.getEmlakTipi());
	list.add(emlakQueryItem.getYil());
	list.add(emlakQueryItem.getM2());
	
	list.add(emlakQueryItem.getOdaSayisi());
	list.add(emlakQueryItem.getBanyoSayisi());
	list.add(emlakQueryItem.getBinaYasi());
	list.add(emlakQueryItem.getBinaKatSayisi());
	list.add(emlakQueryItem.getBulunduguKat());
	list.add(emlakQueryItem.getIsitma());
	list.add(emlakQueryItem.getKullanimDurumu());
	list.add(emlakQueryItem.getSiteIcinde());
	list.add(emlakQueryItem.getKimden());
	
	return list;
}
}
