package org.slevin.common;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="BinaQueryItem")
public class BinaQueryItem {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	String url;
	
	@Lob
	String content;

	Boolean complated;
	
	Boolean parsingComplated;
	
	Boolean creditSutitable;
	
	Long itemIndex;
	
	Long totalCount;
	
	public Boolean getCreditSutitable() {
		return creditSutitable;
	}

	public void setCreditSutitable(Boolean creditSutitable) {
		this.creditSutitable = creditSutitable;
	}

	public Boolean getParsingComplated() {
		return parsingComplated;
	}

	public void setParsingComplated(Boolean parsingComplated) {
		this.parsingComplated = parsingComplated;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getComplated() {
		return complated;
	}

	public void setComplated(Boolean complated) {
		this.complated = complated;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(Long itemIndex) {
		this.itemIndex = itemIndex;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
}
