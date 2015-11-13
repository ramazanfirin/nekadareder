package org.slevin.common;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes={
@Index(name="ilan_no",columnList="ilanNo")
})
public class Emlak {
	@Id    
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	String fiyat;
	String sehir;
	String ilce;
	String mah ;

	BigDecimal fiyatLong;
	Long ilanNo;
	String yil;
	String ay;
	String gun;
	String ilanTarihi;
	String emlakTipi;
	String m2 ;
	String odaSayisi;
	String banyoSayisi;
	String binaYasi;
	String binaKatSayisi;
	String bulunduguKat;
	String isitma="";
	String kullanimDurumu="";
	String krediyeUygun="";
	String kimden ="";
	String takas="";
	String siteIcinde="";
	
	String lat;
	String lng;
	
	String currency;
	
	Date insertDate;
	Boolean exportComplated;

	BigDecimal t1;
	BigDecimal t2;
	BigDecimal t3;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFiyat() {
		return fiyat;
	}
	public void setFiyat(String fiyat) {
		this.fiyat = fiyat;
	}
	public String getSehir() {
		return sehir;
	}
	public void setSehir(String sehir) {
		this.sehir = sehir;
	}
	public String getIlce() {
		return ilce;
	}
	public void setIlce(String ilce) {
		this.ilce = ilce;
	}
	public String getMah() {
		return mah;
	}
	public void setMah(String mah) {
		this.mah = mah;
	}
	public Long getIlanNo() {
		return ilanNo;
	}
	public void setIlanNo(Long ilanNo) {
		this.ilanNo = ilanNo;
	}
	public String getYil() {
		return yil;
	}
	public void setYil(String yil) {
		this.yil = yil;
	}
	public String getAy() {
		return ay;
	}
	public void setAy(String ay) {
		this.ay = ay;
	}
	public String getGun() {
		return gun;
	}
	public void setGun(String gun) {
		this.gun = gun;
	}
	public String getIlanTarihi() {
		return ilanTarihi;
	}
	public void setIlanTarihi(String ilanTarihi) {
		this.ilanTarihi = ilanTarihi;
	}
	public String getEmlakTipi() {
		return emlakTipi;
	}
	public void setEmlakTipi(String emlakTipi) {
		this.emlakTipi = emlakTipi;
	}
	public String getM2() {
		return m2;
	}
	public void setM2(String m2) {
		this.m2 = m2;
	}
	public String getOdaSayisi() {
		return odaSayisi;
	}
	public void setOdaSayisi(String odaSayisi) {
		this.odaSayisi = odaSayisi;
	}
	public String getBanyoSayisi() {
		return banyoSayisi;
	}
	public void setBanyoSayisi(String banyoSayisi) {
		this.banyoSayisi = banyoSayisi;
	}
	public String getBinaYasi() {
		return binaYasi;
	}
	public void setBinaYasi(String binaYasi) {
		this.binaYasi = binaYasi;
	}
	public String getBinaKatSayisi() {
		return binaKatSayisi;
	}
	public void setBinaKatSayisi(String binaKatSayisi) {
		this.binaKatSayisi = binaKatSayisi;
	}
	public String getBulunduguKat() {
		return bulunduguKat;
	}
	public void setBulunduguKat(String bulunduguKat) {
		this.bulunduguKat = bulunduguKat;
	}
	public String getIsitma() {
		return isitma;
	}
	public void setIsitma(String isitma) {
		this.isitma = isitma;
	}
	public String getKullanimDurumu() {
		return kullanimDurumu;
	}
	public void setKullanimDurumu(String kullanimDurumu) {
		this.kullanimDurumu = kullanimDurumu;
	}
	public String getKrediyeUygun() {
		return krediyeUygun;
	}
	public void setKrediyeUygun(String krediyeUygun) {
		this.krediyeUygun = krediyeUygun;
	}
	public String getKimden() {
		return kimden;
	}
	public void setKimden(String kimden) {
		this.kimden = kimden;
	}
	public String getTakas() {
		return takas;
	}
	public void setTakas(String takas) {
		this.takas = takas;
	}
	public String getSiteIcinde() {
		return siteIcinde;
	}
	public void setSiteIcinde(String siteIcinde) {
		this.siteIcinde = siteIcinde;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Boolean getExportComplated() {
		return exportComplated;
	}
	public void setExportComplated(Boolean exportComplated) {
		this.exportComplated = exportComplated;
	}
	public BigDecimal getFiyatLong() {
		return fiyatLong;
	}
	public void setFiyatLong(BigDecimal fiyatLong) {
		this.fiyatLong = fiyatLong;
	}
	public BigDecimal getT1() {
		return t1;
	}
	public void setT1(BigDecimal t1) {
		this.t1 = t1;
	}
	public BigDecimal getT2() {
		return t2;
	}
	public void setT2(BigDecimal t2) {
		this.t2 = t2;
	}
	public BigDecimal getT3() {
		return t3;
	}
	public void setT3(BigDecimal t3) {
		this.t3 = t3;
	}

	
}
