
package com.nature.component.workFlow.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nature.component.template.vo.StopTemplateVo;

/**
 * stop的属性
 * 
 * @author Nature
 *
 */
@Entity
@Table(name = "property_template")
public class PropertyVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FK_STOPS_ID")
	private StopTemplateVo stopsVo;
	
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@GeneratedValue(generator = "idGenerator")
	@Column(length = 40)
	private String id;

	private String name;

	private String displayName;

	@Column(name = "description", columnDefinition = "varchar(1000) COMMENT '描述'")
	private String description;

	@Column(name = "CUSTOM_VALUE")
	private String customValue;

	@Column(name = "ALLOWABLE_VALUES")
	private String allowableValues;

	@Column(name = "PROPERTY_REQUIRED")
	private Boolean required;

	@Column(name = "PROPERTY_SENSITIVE")
	private Boolean sensitive;
	
	@Column(nullable = false)
	private Boolean enableFlag = Boolean.TRUE;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public StopTemplateVo getStopsVo() {
		return stopsVo;
	}

	public void setStopsVo(StopTemplateVo stopsVo) {
		this.stopsVo = stopsVo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustomValue() {
		return customValue;
	}

	public void setCustomValue(String customValue) {
		this.customValue = customValue;
	}

	public String getAllowableValues() {
		return allowableValues;
	}

	public void setAllowableValues(String allowableValues) {
		this.allowableValues = allowableValues;
	}

	public Boolean getRequired() {
		return required;
	}

	public Boolean getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(Boolean enableFlag) {
		this.enableFlag = enableFlag;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getSensitive() {
		return sensitive;
	}

	public void setSensitive(Boolean sensitive) {
		this.sensitive = sensitive;
	}
	
	
}