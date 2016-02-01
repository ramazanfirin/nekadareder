package org.slevin.util;

import org.slevin.common.Emlak;

public class EmlakQueryItem extends Emlak implements Cloneable{
	String imageUrl;
	String description;
	
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
