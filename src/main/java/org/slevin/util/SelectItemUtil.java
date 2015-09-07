package org.slevin.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.slevin.common.Sehir;
import org.springframework.stereotype.Component;

@Component
public class SelectItemUtil {

	
	public List<SelectItem> prepareSehirList(List<Sehir> list) throws Exception{
		List<SelectItem> resultList = new ArrayList<SelectItem>();

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Sehir sehir = (Sehir) iterator.next();
			resultList.add(new SelectItem(sehir.getName(),sehir.getName()));
		}
		return resultList;
	}

	public List<SelectItem> prepareCreditSuitableList() {
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		resultList.add(new SelectItem("true","Evet"));
		resultList.add(new SelectItem("false","Hayır"));
		return resultList;
	}

	public List<SelectItem> prepareEmlakTipiList() {
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		resultList.add(new SelectItem("Satılık Daire","Satılık Daire"));
		return resultList;
	}

	public List<SelectItem> prepareOdaSayisiList() {
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		resultList.add(new SelectItem("Stüdyo (1+0)","Stüdyo (1+0)"));
		resultList.add(new SelectItem("1","1"));
		resultList.add(new SelectItem("1+1","1+1"));
		resultList.add(new SelectItem("2+1","2+1"));
		resultList.add(new SelectItem("2+2","2+2"));
		resultList.add(new SelectItem("3+1","3+1"));
		resultList.add(new SelectItem("3+2","3+2"));
		resultList.add(new SelectItem("4+1","4+1"));
		resultList.add(new SelectItem("4+2","4+2"));
		resultList.add(new SelectItem("4+3","4+3"));
		resultList.add(new SelectItem("4+4","4+4"));
		resultList.add(new SelectItem("5","5"));
		resultList.add(new SelectItem("5+1","5+1"));
		resultList.add(new SelectItem("5+2","5+2"));
		resultList.add(new SelectItem("5+3","5+3"));
		resultList.add(new SelectItem("5+4","5+4"));
		resultList.add(new SelectItem("6+1","6+1"));
		resultList.add(new SelectItem("6+2","6+2"));
		resultList.add(new SelectItem("6+3","6+3"));
		resultList.add(new SelectItem("7+1","7+1"));
		resultList.add(new SelectItem("7+2","7+2"));
		resultList.add(new SelectItem("7+3","7+3"));
		resultList.add(new SelectItem("8+1","8+1"));
		resultList.add(new SelectItem("8+2","8+2"));
		resultList.add(new SelectItem("8+3","8+3"));
		resultList.add(new SelectItem("8+4","8+4"));
		resultList.add(new SelectItem("9+1","9+1"));
		resultList.add(new SelectItem("9+2","9+2"));
		resultList.add(new SelectItem("9+3","9+3"));
		resultList.add(new SelectItem("9+4","9+4"));
		resultList.add(new SelectItem("9+5","9+5"));
		resultList.add(new SelectItem("9+6","9+6"));
		resultList.add(new SelectItem("10+1","10+1"));
		resultList.add(new SelectItem("10+2","10+1"));
		resultList.add(new SelectItem("10 Üzeri","10 Üzeri"));
		return resultList;
	}

	public List<SelectItem> prepareBanyoSayisiList() {
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		resultList.add(new SelectItem("Yok","Yok"));
		resultList.add(new SelectItem("1","1"));
		resultList.add(new SelectItem("2","2"));
		resultList.add(new SelectItem("3","3"));
		resultList.add(new SelectItem("4","4"));
		resultList.add(new SelectItem("5","5"));
		resultList.add(new SelectItem("6","6"));
		resultList.add(new SelectItem("6 Üzeri","6 Üzeri"));
		return resultList;
	}

	public List<SelectItem> prepareBinayasiList() {
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		resultList.add(new SelectItem("0","0"));
		resultList.add(new SelectItem("1","1"));
		resultList.add(new SelectItem("2","2"));
		resultList.add(new SelectItem("3","3"));
		resultList.add(new SelectItem("4","4"));
		resultList.add(new SelectItem("5-10 arası","5-10 arası"));
		resultList.add(new SelectItem("11-15 arası","11-15 arası"));
		resultList.add(new SelectItem("21-25 arası","21-25 arası"));
		resultList.add(new SelectItem("26-30 arası","26-30 arası"));
		resultList.add(new SelectItem("31 ve üzeri","31 ve üzeri"));
		return resultList;
	}

	public List<SelectItem> prepareBinaKatSayisiList() {
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		resultList.add(new SelectItem("0","0"));
		resultList.add(new SelectItem("1","1"));
		resultList.add(new SelectItem("2","2"));
		resultList.add(new SelectItem("3","3"));
		resultList.add(new SelectItem("4","4"));
		resultList.add(new SelectItem("5","5"));
		resultList.add(new SelectItem("6","6"));
		resultList.add(new SelectItem("7","7"));
		resultList.add(new SelectItem("8","8"));
		resultList.add(new SelectItem("9","9"));
		resultList.add(new SelectItem("10","10"));
		resultList.add(new SelectItem("11","11"));
		resultList.add(new SelectItem("12","12"));
		resultList.add(new SelectItem("13","13"));
		resultList.add(new SelectItem("14","14"));
		resultList.add(new SelectItem("15","15"));
		resultList.add(new SelectItem("16","16"));
		resultList.add(new SelectItem("17","17"));
		resultList.add(new SelectItem("18","18"));
		resultList.add(new SelectItem("19","19"));
		resultList.add(new SelectItem("20","20"));
		resultList.add(new SelectItem("21","21"));
		resultList.add(new SelectItem("22","22"));
		resultList.add(new SelectItem("23","23"));
		resultList.add(new SelectItem("24","24"));
		resultList.add(new SelectItem("25","25"));
		resultList.add(new SelectItem("26","26"));
		resultList.add(new SelectItem("27","27"));
		resultList.add(new SelectItem("28","28"));
		resultList.add(new SelectItem("29","29"));
		resultList.add(new SelectItem("30 ve üzeri","30 ve üzeri"));
		
		return resultList;
	}

	public List<SelectItem> prepareBulunduguKatList() {
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		resultList.add(new SelectItem("Kot 4","Kot 4"));
		resultList.add(new SelectItem("Kot 3","Kot 3"));
		resultList.add(new SelectItem("Kot 2","Kot 2"));
		resultList.add(new SelectItem("Kot 1","Kot 1"));
		resultList.add(new SelectItem("Bodrum Kat","Bodrum Kat"));
		resultList.add(new SelectItem("Zemin Kat","Zemin Kat"));
		resultList.add(new SelectItem("Bahçe Katı","Bahçe Katı"));
		resultList.add(new SelectItem("Giriş Katı","Giriş Katı"));
		resultList.add(new SelectItem("Yüksek Giriş","Yüksek Giriş"));
		resultList.add(new SelectItem("Müstakil","Müstakil"));
		resultList.add(new SelectItem("Villa Tipi","Villa Tipi"));
		resultList.add(new SelectItem("Çatı Katı","Çatı Katı"));
		resultList.add(new SelectItem("1","1"));
		resultList.add(new SelectItem("2","2"));
		resultList.add(new SelectItem("3","3"));
		resultList.add(new SelectItem("4","4"));
		resultList.add(new SelectItem("5","5"));
		resultList.add(new SelectItem("6","6"));
		resultList.add(new SelectItem("7","7"));
		resultList.add(new SelectItem("8","8"));
		resultList.add(new SelectItem("9","9"));
		resultList.add(new SelectItem("10","10"));
		resultList.add(new SelectItem("11","11"));
		resultList.add(new SelectItem("12","12"));
		resultList.add(new SelectItem("13","13"));
		resultList.add(new SelectItem("14","14"));
		resultList.add(new SelectItem("15","15"));
		resultList.add(new SelectItem("16","16"));
		resultList.add(new SelectItem("17","17"));
		resultList.add(new SelectItem("18","18"));
		resultList.add(new SelectItem("19","19"));
		resultList.add(new SelectItem("20","20"));
		resultList.add(new SelectItem("21","21"));
		resultList.add(new SelectItem("22","22"));
		resultList.add(new SelectItem("23","23"));
		resultList.add(new SelectItem("24","24"));
		resultList.add(new SelectItem("25","25"));
		resultList.add(new SelectItem("26","26"));
		resultList.add(new SelectItem("27","27"));
		resultList.add(new SelectItem("28","28"));
		resultList.add(new SelectItem("29","29"));
		resultList.add(new SelectItem("30 ve üzeri","30 ve üzeri"));
		return resultList;
	}

	public List<SelectItem> prepareIsitmaList() {
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		resultList.add(new SelectItem("Yok","Yok"));
		resultList.add(new SelectItem("Soba","Soba"));
		resultList.add(new SelectItem("Doğalgaz Sobası","Doğalgaz Sobası"));
		resultList.add(new SelectItem("Kat Kaloriferi","Kat Kaloriferi"));
		resultList.add(new SelectItem("Merkezi Sistem","Merkezi Sistem"));
		resultList.add(new SelectItem("Merkezi Sistem (Isı Pay Ölçer)","Merkezi Sistem (Isı Pay Ölçer)"));
		resultList.add(new SelectItem("Doğalgaz (Kombi)","Doğalgaz (Kombi)"));
		resultList.add(new SelectItem("Yerden Isıtma","Yerden Isıtma"));
		resultList.add(new SelectItem("Klima","Klima"));
		resultList.add(new SelectItem("Güneş Enerjisi","Güneş Enerjisi"));
		resultList.add(new SelectItem("Jeotermal","Jeotermal"));
		
		return resultList;
	}

	public List<SelectItem> prepareKullanimDurumuList() {
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		resultList.add(new SelectItem("Boş","Boş"));
		resultList.add(new SelectItem("Kiracılı","Kiracılı"));
		resultList.add(new SelectItem("Mülk Sahibi","Mülk Sahibi"));
		return resultList;
	}

	public List<SelectItem> prepareSiteIcindeList() {
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		resultList.add(new SelectItem("Evet","Evet"));
		resultList.add(new SelectItem("Hayır","Hayır"));
		return resultList;
	}

	public List<SelectItem> prepareKimdenList() {
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		resultList.add(new SelectItem("Sahibinden","Sahibinden"));
		resultList.add(new SelectItem("Emlak Ofisinden","Emlak Ofisinden"));
		resultList.add(new SelectItem("İnşaat Firmasından","İnşaat Firmasından"));
		resultList.add(new SelectItem("Bankadan","Bankadan"));
		return resultList;
	}
}
