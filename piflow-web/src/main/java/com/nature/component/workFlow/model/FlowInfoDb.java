package com.nature.component.workFlow.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

@Entity
@Table(name =  "flow_info")
public class FlowInfoDb extends BaseHibernateModelUUIDNoCorpAgentId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String state;
	private String startTime;
	private String endTime;
	private String progress;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

}
