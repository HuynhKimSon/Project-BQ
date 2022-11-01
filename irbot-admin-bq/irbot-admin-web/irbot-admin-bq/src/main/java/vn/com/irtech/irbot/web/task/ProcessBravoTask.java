package vn.com.irtech.irbot.web.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.irtech.irbot.business.service.IProcessBravoService;

@Component("processBravoTask")
public class ProcessBravoTask {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessBravoTask.class);
	
	@Autowired
	private IProcessBravoService processBravoService;
	
	public void executeTask() {
		logger.info(">>>>>>>>>>>>>> SYNC BRAVO TASK - START!");
		
		try {
			
			processBravoService.sync();
			
		} catch (Exception e) {
			
			logger.error(">>>>>> Error: " + e.getMessage());
		}
		
		logger.info(">>>>>>>>>>>>>> SYNC BRAVO TASK - FINISH!");
	}
}
