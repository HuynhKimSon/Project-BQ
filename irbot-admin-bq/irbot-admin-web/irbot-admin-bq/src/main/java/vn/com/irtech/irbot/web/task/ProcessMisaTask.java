package vn.com.irtech.irbot.web.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.irtech.irbot.business.service.IProcessBravoService;
import vn.com.irtech.irbot.business.service.IProcessMisaService;

@Component("processMisaTask")
public class ProcessMisaTask {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessMisaTask.class);
	
	@Autowired
	private IProcessMisaService processMisaService;
	
	public void executeTask() {
		logger.info(">>>>>>>>>>>>>> SYNC MISA TASK - START!");
		
		try {
			
			processMisaService.sync();
			
		} catch (Exception e) {
			
			logger.error(">>>>>> Error: " + e.getMessage());
		}
		
		logger.info(">>>>>>>>>>>>>> SYNC MISA TASK - FINISH!");
	}
}
