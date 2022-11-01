package vn.com.irtech.irbot.business.dto.response;

import java.io.Serializable;

public class RobotProcessRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long processId;

	private String syncId;

	private String result;

	private String errorMsg;

	private RobotProcessDataRes data;

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getSyncId() {
		return syncId;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public RobotProcessDataRes getData() {
		return data;
	}

	public void setData(RobotProcessDataRes data) {
		this.data = data;
	}

}
