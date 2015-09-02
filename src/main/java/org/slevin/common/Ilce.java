package org.slevin.common;

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
public class Ilce {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	String name;
	String itemId;

	@ManyToOne
	private Sehir sehir;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="ILCE_ID")
	private Collection<Semt> semtList = new LinkedHashSet<Semt>();
	
	Boolean complated;
	
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
//	public List<Semt> getSemtList() {
//		return semtList;
//	}
//	public void setSemtList(List<Semt> semtList) {
//		this.semtList = semtList;
//	}
	public Sehir getSehir() {
		return sehir;
	}
	public void setSehir(Sehir sehir) {
		this.sehir = sehir;
	}
	public Collection<Semt> getSemtList() {
		return semtList;
	}
	public void setSemtList(Collection<Semt> semtList) {
		this.semtList = semtList;
	}
	public Boolean getComplated() {
		return complated;
	}
	public void setComplated(Boolean complated) {
		this.complated = complated;
	}
}
