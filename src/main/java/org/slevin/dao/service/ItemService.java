package org.slevin.dao.service;


import org.slevin.common.Item;
import org.slevin.dao.ItemsDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class ItemService extends EntityService<Item> implements ItemsDao {

	
}

