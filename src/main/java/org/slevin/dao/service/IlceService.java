package org.slevin.dao.service;


import org.slevin.common.Ilce;
import org.slevin.dao.IlceDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class IlceService extends EntityService<Ilce> implements IlceDao {

	
}

