package org.slevin.common;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Sehir {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	String name;
	String itemId;
	

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="SEHIR_ID")
	private Collection<Ilce> ilceList = new LinkedHashSet<Ilce>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
//	public List<Ilce> getIlceList() {
//		return ilceList;
//	}
//	public void setIlceList(List<Ilce> ilceList) {
//		this.ilceList = ilceList;
//	}
	public Collection<Ilce> getIlceList() {
		return ilceList;
	}
	public void setIlceList(Collection<Ilce> ilceList) {
		this.ilceList = ilceList;
	}
}
