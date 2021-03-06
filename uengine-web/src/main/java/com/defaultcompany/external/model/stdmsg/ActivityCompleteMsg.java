package com.defaultcompany.external.model.stdmsg;

public class ActivityCompleteMsg extends BaseStdMsg {
	
	private static final long serialVersionUID = 1L;
	
	private String endpoint;
	private String instanceId;
	private String taskId;
	private String tracingTag;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTracingTag() {
		return tracingTag;
	}

	public void setTracingTag(String tracingTag) {
		this.tracingTag = tracingTag;
	}

}
