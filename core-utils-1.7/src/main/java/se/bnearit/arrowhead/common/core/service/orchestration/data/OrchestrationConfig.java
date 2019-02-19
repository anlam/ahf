package se.bnearit.arrowhead.common.core.service.orchestration.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

/**
 * Configuration and connection directives for a consumer.
 * 
 * 
 * 
 * @author BnearIT
 */
public class OrchestrationConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private String target;
	private String name;
	private int serialNumber;
	private Date lastUpdated;
	private List<String> rules;
	
	public OrchestrationConfig() {
	}

        /**
         * 
         * @param target
         * @param name Name of this configuration, used in conjunction with actieconfiguration
         * @param serialNumber serial number to detect updates
         * @param lastUpdated when last update was done on this configuration
         * @param rules a set of connection rules which currently is service instance names. 
         */
	public OrchestrationConfig(String target, String name, int serialNumber,
			Date lastUpdated, List<String> rules) {
		this.target = target;
		this.name = name;
		this.serialNumber = serialNumber;
		this.lastUpdated = lastUpdated;
		this.rules = rules;
	}

        
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

        /**
         * 
         * @return service instance names to connect to.
         */
	public List<String> getRules() {
		return rules;
	}

        /**
         * 
         * @param rules service instance names to connect to.
         */
	public void setRules(List<String> rules) {
		this.rules = rules;
	}

	@Override
	public String toString() {
		return "OrchestrationConfig [target=" + target + ", name=" + name
				+ ", serialNumber=" + serialNumber + ", lastUpdated="
				+ lastUpdated + ", rules=" + Arrays.toString(rules.toArray()) + "]";
	}
	
}
