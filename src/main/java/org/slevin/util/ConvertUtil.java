package org.slevin.util;

import java.util.ArrayList;
import java.util.List;

import org.slevin.common.Emlak;
import org.springframework.util.StringUtils;

public class ConvertUtil {
public static String convertCVSFormat(Emlak emlak){
	StringBuffer result = new StringBuffer();
	result.append(preparePrice(emlak.getFiyatLong().longValue(),emlak.getCurrency()));
	result.append(",");
	result.append(appendQuata(emlak.getSehir()));
	result.append(",");
	result.append(appendQuata(emlak.getIlce()).replaceFirst("\\s+", "").replace(" ", "_"));
	result.append(",");
	result.append(appendQuata(emlak.getMah()).replaceFirst("\\s+", "").replace(" ", "_"));
	result.append(",");
	result.append(appendQuata(emlak.getKrediyeUygun()));
	result.append(",");
	result.append(appendQuata(emlak.getEmlakTipi()).replace(" ", "_"));
	result.append(",");
	result.append(emlak.getYil());
	result.append(",");
	result.append(prepareM2(emlak.getM2()));
	result.append(",");
	result.append(prepareOdaSayisi(emlak.getOdaSayisi().replaceAll("\"", "")));
	result.append(",");
//	result.append(prepareBanyoSayisi(emlak.getBanyoSayisi()));
//	result.append(",");
	result.append(prepareBinaYasi(emlak.getBinaYasi().replaceAll("\"", "")));
	result.append(",");
//	result.append(prepareBinaKatSayisi(emlak.getBinaKatSayisi()));
//	result.append(",");
	result.append(prepareBulunduguKat(emlak.getBulunduguKat()));
	result.append(",");
	result.append(prepareIsitma(emlak.getIsitma()).replace(" ", "_"));
	result.append(",");
	result.append(prepareKullanimDurumu(emlak.getKullanimDurumu()).replace(" ", "_"));
	result.append(",");
	result.append(prepareSiteIcinde(emlak.getSiteIcinde()).replace(" ", "_"));
	result.append(",");
	result.append(prepareKimden(emlak.getKimden()).replace(" ", "_"));

	return result.toString();
	
	//100000.0,"İstanbul","Adalar","Kaşıkadası","false","Satılık Daire",2015,150 ,10  ,50   ,50  ,100,    50,   100   ,10      ,10,      95
	//  m2,oda, banyo ,yaş,k.sayisi,b.kat,isitma,kullanım,siteiçi,kimden                                                 
}
public static String prepareKrediyeUygun(String string){
	int result = Integer.parseInt(string);
	result = result+50;
	return String.valueOf(result);
}

public static String prepareM2(String string){
	int result = Integer.parseInt(string);
	result = result+1000;
	return String.valueOf(result);
}

public static String prepareIsitma(String string){
	int result =100;
	if(string.equals("") || string.contains("-1") )
		result =100;
	else if(string.equals("Yok"))
		result =100;
	else if(string.equals("Soba"))
		result =110;
	else if(string.equals("Doğalgaz Sobası"))
		result =120;
	else if(string.equals("Kat Kaloriferi"))
		result =130;
	else if(string.equals("Merkezi Sistem"))
		result =140;
	else if(string.equals("Merkezi Sistem (Isı Pay Ölçer)"))
		result =150;
	else if(string.equals("Doğalgaz (Kombi)"))
		result =120;
	else if(string.equals("Yerden Isıtma"))
		result =160;
	else if(string.equals("Güneş Enerjisi"))
		result =170;
	else if(string.equals("Jeotermal"))
		result =180;
	
		//return String.valueOf(result);
	return string;
}

public static String prepareKullanimDurumu(String string){
	int result=0;
	if(string.contains("-1"))
		result = 7;
	else if(string.contains("Boş"))
		result =10;
	else if(string.contains("Kiracılı"))
		result =8;
	else if(string.contains("Mülk Sahibi"))
		result =9;
	
		//return String.valueOf(result);
		return string;
}

public static String prepareKimden(String string){
	int result =0;
	if(string.equals("0") || string.contains("-1"))
		result= 100;
	if(string.contains("Sahibinden"))
		result= 100;
	if(string.contains("Emlak Ofisinden"))
		result =95;
	if(string.contains("İnşaat Firmasından"))
		result =90;
	if(string.contains("Bankadan"))
		result =85;
	
	//return String.valueOf(result);
	return string;
}

public static String prepareSiteIcinde(String string){
	int result=0;
	if(string.equals("") || string.contains("-1"))
		result=8;

	if(string.contains("Evet"))
			result=10;
	else if(string.contains("Hayır"))
			result=8;		
	//return String.valueOf(result);
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
	int result =0;
	//System.out.println(odaSayisi);
	if(odaSayisi.equals("Stüdyo (1+0)"))
		result= 0;
	else if(odaSayisi.equals("10 Üzeri"))
		result=15;
	else{
	String[] temp=odaSayisi.split("\\+");
	Long a = new Long(temp[0]);
	Long b = new Long(0);
	if(temp.length>1)
		b = new Long(temp[1]);	
		Long c= a+b;
	result = c.intValue();
	}
	result=result+50;
	return String.valueOf(result);
}


public static String prepareBanyoSayisi(String banyoSayisi){
	int result=0;
	if(banyoSayisi.equals("Yok"))
		result =0;;
	if(banyoSayisi.equals("6 Üzeri"))
		result = 7;
	
	result = result+50;
	return String.valueOf(result);
}

public static String prepareBinaYasi(String binaYasi){
	int result=0;
	if(binaYasi.equals("5-10 arası"))
		result =7;
	if(binaYasi.equals("11-15 arası"))
		result =12;
	if(binaYasi.equals("16-20 arası"))
		result= 17;
	if(binaYasi.equals("21-25 arası"))
		result= 22;
	if(binaYasi.equals("26-30 arası"))
		result= 17;
	if(binaYasi.equals("31 ve üzeri"))
		result= 35;
	
	result = result+50;
	return String.valueOf(result);
}

public static String prepareBinaKatSayisi(String binaKatSayisi){
	int result=0;
	if(binaKatSayisi.equals("30 ve üzeri"))
		result=40;
	else
		result = Integer.parseInt(binaKatSayisi);
	result = result+100;
	return String.valueOf(result);
}

public static String prepareBulunduguKat(String bulunduguKat){
	int result=0;
	if(bulunduguKat.equals("Kot 4"))
		result= 0;
	else if(bulunduguKat.equals("Kot 3"))
		result =0;
	else if(bulunduguKat.equals("Kot 2"))
		result =0;
	else if(bulunduguKat.equals("Kot 1"))
		result =0;
	else if(bulunduguKat.equals("Bodrum Kat"))
		result =1;
	else if(bulunduguKat.equals("Zemin Kat"))
		result =1;
	else if(bulunduguKat.equals("Bahçe Katı"))
		result =1;
	else if(bulunduguKat.equals("Giriş Katı"))
		result =1;
	else if(bulunduguKat.equals("Yüksek Giriş"))
		result =1;
	else if(bulunduguKat.equals("Müstakil"))
		result =2;
	else if(bulunduguKat.equals("Villa Tipi"))
		result =2;
	else if(bulunduguKat.equals("Çatı Katı"))
		result =1;
	else if(bulunduguKat.equals("30 ve üzeri"))
		result =3;
	else
		result = Integer.parseInt(bulunduguKat);
//	Long a = new Long(bulunduguKat);
//	a= a*100;
//	return a.toString();
	result = result+100;
	return String.valueOf(result);
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
	list.add(emlakQueryItem.getSehir().replace(" ", "_"));
	list.add(emlakQueryItem.getIlce().replace(" ", "_"));
	list.add(emlakQueryItem.getMah().replace(" ", "_"));
	list.add(emlakQueryItem.getKrediyeUygun());
	list.add(emlakQueryItem.getEmlakTipi().replace(" ", "_"));
	list.add(emlakQueryItem.getYil());
	list.add(emlakQueryItem.getM2());
	
	list.add(emlakQueryItem.getOdaSayisi());
	//list.add(new Long(emlakQueryItem.getBanyoSayisi()));
	list.add(emlakQueryItem.getBinaYasi());
	//list.add(new Long(emlakQueryItem.getBinaKatSayisi()));
	list.add(emlakQueryItem.getBulunduguKat());
	list.add(emlakQueryItem.getIsitma().replace(" ", "_"));
	list.add(emlakQueryItem.getKullanimDurumu().replace(" ", "_"));
	list.add(emlakQueryItem.getSiteIcinde().replace(" ", "_"));
	list.add(emlakQueryItem.getKimden().replace(" ", "_"));
	
	return list;
}

public static String getSegment(Long fiyat){
	String segment="5";
   	if(fiyat>10000 && fiyat<=150000)
		segment="0";
	else if(fiyat>150000 && fiyat<=350000)
		segment="1";
	else if(fiyat>350000 && fiyat<=550000)
		segment="2";
	else if(fiyat>550000 && fiyat<=800000)
		segment="3";
	else if(fiyat>800000 && fiyat<10000000)
		segment="4";
	else
		segment="5";
	
	return segment;
}

public static String prepareKrediyeUygunValue(String value){
	if(!StringUtils.isEmpty(value)){
		if(value.equalsIgnoreCase("Evet"))
			return "true";
		else if (value.equalsIgnoreCase("Hayır"))
			return "false";	
		else 
			return value;
	}
	return value;
}

public static EmlakQueryItem prepareEmlakQueryItem(EmlakQueryItem emlakQueryItem){
	emlakQueryItem.setOdaSayisi(ConvertUtil.prepareOdaSayisi(emlakQueryItem.getOdaSayisi()));
	emlakQueryItem.setBanyoSayisi(ConvertUtil.prepareBanyoSayisi(emlakQueryItem.getBanyoSayisi()));
	emlakQueryItem.setBinaYasi(ConvertUtil.prepareBinaYasi(emlakQueryItem.getBinaYasi()));
	emlakQueryItem.setBinaKatSayisi(ConvertUtil.prepareBinaKatSayisi(emlakQueryItem.getBinaKatSayisi()));
	emlakQueryItem.setBulunduguKat(ConvertUtil.prepareBulunduguKat(emlakQueryItem.getBulunduguKat()));
	emlakQueryItem.setM2(ConvertUtil.prepareM2(emlakQueryItem.getM2()));

	
	return emlakQueryItem;
}



}
