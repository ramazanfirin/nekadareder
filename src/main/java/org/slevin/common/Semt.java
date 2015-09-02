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
public class Semt {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	String name;
	String itemId;
	
	@ManyToOne
	private Ilce ilce;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="SEMT_ID")
	private Collection<Mahalle> mahalleList = new LinkedHashSet<Mahalle>();
	
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
	public Ilce getIlce() {
		return ilce;
	}
	public void setIlce(Ilce ilce) {
		this.ilce = ilce;
	}
	public Collection<Mahalle> getMahalleList() {
		return mahalleList;
	}
	public void setMahalleList(Collection<Mahalle> mahalleList) {
		this.mahalleList = mahalleList;
	}
}
