package org.slevin.dao.service;


import org.slevin.common.Semt;
import org.slevin.dao.SemtDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class SemtService extends EntityService<Semt> implements SemtDao {

	
}

