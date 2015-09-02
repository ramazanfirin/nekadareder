package org.slevin.prime.faces.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.slevin.common.Item;
import org.slevin.dao.ItemsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component(value="itemMB")
@ViewScoped
public class ItemMB implements Serializable {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Autowired
	private ItemsDao itemService;

	private Item bean;
	private Item beanSelected;
	private List<Item> list;
	private List<Item> listSelected;
	
	@PostConstruct
    public void init() {
		refreshList();
    }



	public void refreshList() {
		this.bean = new Item();
		this.beanSelected = new Item();
		this.list = new ArrayList<Item>();
		this.listSelected = new ArrayList<Item>();
		try {
			this.list.addAll(itemService.findAll());
			this.listSelected.addAll(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	public void save() {
		try {
		    
			itemService.persist(this.bean);
			refreshList();
			notificationSuccess("persist item");
		} catch (Exception e) {
			notificationError(e,"persist item");
			e.printStackTrace();
		}
	}

	public void update() {
		try {
			itemService.merge(this.beanSelected);
			refreshList();
			notificationSuccess("update item");
		} catch (Exception e) {
			notificationError(e,"update item");
		}
	}

	public void delete() {
		try {
			itemService.remove(this.beanSelected.getId());
			refreshList();
			notificationSuccess("delete item");
		} catch (Exception e) {
			notificationError(e,"delete item");
		}
	}

	public void onCancel(RowEditEvent event) {
		refreshList();
	}

	
	public void reset() {
		refreshList();
        RequestContext.getCurrentInstance().reset("form1:panel");  
	}

	
	public void notificationSuccess(String operation) {
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Operation "+operation+" success");
		FacesMessage msg = null;  
		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notification", "Success"); 
		FacesContext.getCurrentInstance().addMessage(null, msg);  
	}


	public void notificationError(Exception e, String operation) {
		Logger.getLogger(this.getClass().getName()).log(Level.ERROR, "Operation "+operation+" Error ",e);
		FacesMessage msg = null;  
		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notification", "Une erreur est survenue");  
		FacesContext.getCurrentInstance().addMessage(null, msg);  
	}
	
	public ItemsDao getItemService() {
		return itemService;
	}

	public void setItemService(ItemsDao itemService) {
		this.itemService = itemService;
	}

	public Item getBean() {
		return bean;
	}

	public void setBean(Item bean) {
		this.bean = bean;
	}

	public Item getBeanSelected() {
		return beanSelected;
	}

	public void setBeanSelected(Item beanSelected) {
		this.beanSelected = beanSelected;
	}

	public List<Item> getList() {
		if(list == null){
			list = new ArrayList<Item>();
		}
		return list;
	}

	public void setList(List<Item> list) {
		this.list = list;
	}

	public List<Item> getListSelected() {
		return listSelected;
	}

	public void setListSelected(List<Item> listSelected) {
		this.listSelected = listSelected;
	}

	
	
	
	
}
