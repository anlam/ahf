package se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg;

import javax.xml.bind.annotation.XmlElement;
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

@XmlRootElement(name="activeConfigurationRequest")
public class SetActiveConfigurationRequest {

	private String target;
	private String config;
	
	public SetActiveConfigurationRequest() {
	}

	public SetActiveConfigurationRequest(String target, String config) {
		super();
		this.target = target;
		this.config = config;
	}

	@XmlElement(name="target")
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}
	
}
