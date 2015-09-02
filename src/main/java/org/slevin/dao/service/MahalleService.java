package org.slevin.dao.service;


import org.slevin.common.Mahalle;
import org.slevin.dao.MahalleDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class MahalleService extends EntityService<Mahalle> implements MahalleDao {

	
}

