package org.slevin.dao.service;

import org.slevin.common.Order;
import org.slevin.dao.OrdersDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderService extends EntityService<Order> implements OrdersDao {

}
