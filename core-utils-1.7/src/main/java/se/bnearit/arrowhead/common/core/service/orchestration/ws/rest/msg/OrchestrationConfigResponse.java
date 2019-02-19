package se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/*****************************************************************************/
//  Copyright (c) 2016 BnearIT AB
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//     The Eclipse Public License is available at
//       http://www.eclipse.org/legal/epl-v10.html
//
//     The Apache License v2.0 is available at
//       http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
/*****************************************************************************/

@XmlRootElement(name="orchestrationConfiguration")
public class OrchestrationConfigResponse {

	private String target;
	private String name;
	private int serialNumber;
	private Date lastUpdated;
	private List<String> rules;
	
	public OrchestrationConfigResponse() {
	}

	public OrchestrationConfigResponse(String target, String name,
			int serialNumber, Date lastUpdated, List<String> rules) {
		this.target = target;
		this.name = name;
		this.serialNumber = serialNumber;
		this.lastUpdated = lastUpdated;
		this.rules = rules;
	}

	@XmlElement(name="target")
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@XmlElementWrapper(name="orchestrationRules")
	@XmlElement(name="rule")
	public List<String> getRules() {
		return rules;
	}

	public void setRules(List<String> rules) {
		this.rules = rules;
	}

}
