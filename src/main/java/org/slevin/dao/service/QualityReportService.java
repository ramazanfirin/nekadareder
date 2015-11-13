package org.slevin.dao.service;


import org.slevin.common.Ilce;
import org.slevin.common.QualityReport;
import org.slevin.dao.IlceDao;
import org.slevin.dao.QualityReportDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class QualityReportService extends EntityService<QualityReport> implements QualityReportDao {

	
}

