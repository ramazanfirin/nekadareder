package org.slevin.dao.service;


import org.slevin.dao.BinaQueryDao;
import org.slevin.dao.CronJobDao;
import org.slevin.dao.EmlakDao;
import org.slevin.dao.IlceDao;
import org.slevin.dao.SahibindenDao;
import org.slevin.dao.SehirDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class CronJobService  implements CronJobDao {
	@Autowired
	BinaQueryDao binaQueryDao;
	
	@Autowired
	SehirDao sehirDao;
	
	@Autowired
	IlceDao ilceDao;
	
	@Autowired
	EmlakDao emlakDao;
	
	@Autowired
	SahibindenDao sahibindenDao;
	
	@Scheduled(cron = "10 * * * * *") //second, minute, hour, day of month, month, day(s) of week
	public void cronTask(){
	// System.out.println(new Date() + " This runs in a cron schedule");
	}

	@Scheduled(cron = "0 40 16 ? * *") //Fire at 10:15am every day
	public void jobDaily() throws Exception{
		System.out.println("gunluk job basliyor");
		sahibindenDao.daireTapuluLast24Hours();
		System.out.println("gunluk job bitiyor");
	}
	
	@Scheduled(cron = "0 15 10 ? * FRI") // 0 15 10 ? * MON-FRI // Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday
	public void weeklyDaily() throws Exception{
		//sahibindenDao.daireTapusuzUpdate();
	}
	
	

	public void mothlyDailyTapulu() throws Exception{
		//@Scheduled(cron = "0 15 10 L * ?") //Fire at 10:15am on the last day of every month
		sahibindenDao.rezidansTapuluUpdate() ;
		sahibindenDao.mustakilEvTapuluUpdate();
		sahibindenDao.villaTapuluUpdate();
		sahibindenDao.ciftlikEviTapuluUpdate() ;
		sahibindenDao.koskTapuluUpdate();
		sahibindenDao.yaliTapuluUpdate();
		sahibindenDao.yaliDairesiTapuluUpdate();
		sahibindenDao.yazlikDairesiTapuluUpdate();
		sahibindenDao.prefabrikTapuluUpdate();
		sahibindenDao.kooperatifTapuluUpdate();
	}
	
	public void mothlyDailyTapusuz() throws Exception{
		sahibindenDao.rezidansTapuluUpdate() ;
		sahibindenDao.mustakilEvTapuluUpdate();
		sahibindenDao.villaTapuluUpdate();
		sahibindenDao.ciftlikEviTapuluUpdate() ;
		sahibindenDao.koskTapuluUpdate();
		sahibindenDao.yaliTapuluUpdate();
		sahibindenDao.yaliDairesiTapuluUpdate();
		sahibindenDao.yazlikDairesiTapuluUpdate();
		sahibindenDao.prefabrikTapuluUpdate();
		sahibindenDao.kooperatifTapuluUpdate();
	}
	
}

