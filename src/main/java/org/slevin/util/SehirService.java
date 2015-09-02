package org.slevin.util;


import org.slevin.common.Sehir;
import org.slevin.dao.SehirDao;
import org.slevin.dao.service.EntityService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class SehirService extends EntityService<Sehir> implements SehirDao {

	
}

