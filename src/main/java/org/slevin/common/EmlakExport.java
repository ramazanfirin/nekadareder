package org.slevin.common;

import java.math.BigDecimal;

import javax.persistence.Entity;

@Entity
public class EmlakExport extends Emlak{

	BigDecimal t1;
	BigDecimal t2;
	BigDecimal t3;
	
	Boolean calculatedComplated;

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

	public Boolean getCalculatedComplated() {
		return calculatedComplated;
	}

	public void setCalculatedComplated(Boolean calculatedComplated) {
		this.calculatedComplated = calculatedComplated;
	}
	
	
}
