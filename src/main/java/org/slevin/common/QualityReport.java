package org.slevin.common;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class QualityReport {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	String sehir;
	String ilce;
	BigDecimal fiyat;
	BigDecimal prediction;
	BigDecimal predictionOriginal;
	Double successRate;
	String ilanNo;
	
	Long segment;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public BigDecimal getFiyat() {
		return fiyat;
	}
	public void setFiyat(BigDecimal fiyat) {
		this.fiyat = fiyat;
	}
	public BigDecimal getPrediction() {
		return prediction;
	}
	public void setPrediction(BigDecimal prediction) {
		this.prediction = prediction;
	}
	public Double getSuccessRate() {
		return successRate;
	}
	public void setSuccessRate(Double successRate) {
		this.successRate = successRate;
	}
	public BigDecimal getPredictionOriginal() {
		return predictionOriginal;
	}
	public void setPredictionOriginal(BigDecimal predictionOriginal) {
		this.predictionOriginal = predictionOriginal;
	}
	public Long getSegment() {
		return segment;
	}
	public void setSegment(Long segment) {
		this.segment = segment;
	}
	public String getIlanNo() {
		return ilanNo;
	}
	public void setIlanNo(String ilanNo) {
		this.ilanNo = ilanNo;
	}
	
	
}
