package org.slevin.dao.service;


import org.slevin.common.EmlakExport;
import org.slevin.dao.EmlakExportDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class EmlakExportService extends EntityService<EmlakExport> implements EmlakExportDao {

	
}

