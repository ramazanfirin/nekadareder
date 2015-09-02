package org.slevin.prime.faces.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import org.slevin.common.Order;
import org.slevin.dao.ItemsDao;
import org.slevin.dao.OrdersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component(value="orderMB")
@ViewScoped
public class OrderMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	OrdersDao orderService;
	
	@Autowired
	ItemsDao itemService;
	
	private Order bean;
	private Order beanSelected;
	private List<Order> list;
	private List<Order> listSelected;
	private List<String> listItem;
	
	
	
	@PostConstruct
    public void init() {
		refreshList();
    }
	
	
	public void refreshList() {
		this.bean = new Order();
		this.beanSelected = new Order();
		this.list = new ArrayList<Order>();
		this.listSelected = new ArrayList<Order>();
		this.listItem = new ArrayList<String>();
		try {
			this.list.addAll(orderService.findAll());
			this.listSelected.addAll(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void save() {
		try { 
			for(String itemId : listItem){
				Item item = new Item();
				item = itemService.findById(Long.parseLong(itemId));
				this.bean.getItems().add(item);
			}
			this.bean.setOrderDate(new Date());
			// Use merge instead of persist or you'll have a org.hibernate.PersistentObjectException: detached entity passed to persist: org.slevin.common.Item
			orderService.merge(this.bean);
			refreshList();
			notificationSuccess("persist order");
		} catch (Exception e) {
			notificationError(e,"persist order");
			e.printStackTrace();
		}
	}
	
	public void update() {
		try {
			orderService.merge(this.beanSelected);
			refreshList();
			notificationSuccess("update order");
		} catch (Exception e) {
			notificationError(e,"update order");
		}
	}
	
	
	public void delete() {
		try {
			orderService.remove(this.beanSelected.getId());
			refreshList();
			notificationSuccess("delete order");
		} catch (Exception e) {
			notificationError(e,"delete order");
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
	
	
	public List<Item> getAllItems() {
		List<Item> tmpList = new ArrayList<Item>();
		try {
			tmpList.addAll(itemService.findAll());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmpList;
	}


	public OrdersDao getOrderService() {
		return orderService;
	}


	public void setOrderService(OrdersDao orderService) {
		this.orderService = orderService;
	}


	public Order getBean() {
		return bean;
	}


	public void setBean(Order bean) {
		this.bean = bean;
	}


	public Order getBeanSelected() {
		return beanSelected;
	}


	public void setBeanSelected(Order beanSelected) {
		this.beanSelected = beanSelected;
	}


	public List<Order> getList() {
		return list;
	}


	public void setList(List<Order> list) {
		this.list = list;
	}


	public List<Order> getListSelected() {
		return listSelected;
	}


	public void setListSelected(List<Order> listSelected) {
		this.listSelected = listSelected;
	}


	public ItemsDao getItemService() {
		return itemService;
	}


	public void setItemService(ItemsDao itemService) {
		this.itemService = itemService;
	}


	public List<String> getListItem() {
		return listItem;
	}


	public void setListItem(List<String> listItem) {
		this.listItem = listItem;
	}


	
	
	
	

}
