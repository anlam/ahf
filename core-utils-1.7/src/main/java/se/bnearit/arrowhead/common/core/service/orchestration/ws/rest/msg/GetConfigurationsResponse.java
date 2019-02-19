package se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg;

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

@XmlRootElement(name="configurationList")
public class GetConfigurationsResponse {

	private String target;
	private List<ConfigurationReference> configurations;
	
	public GetConfigurationsResponse() {
	}

	public GetConfigurationsResponse(String target, List<ConfigurationReference> configurations) {
		this.target = target;
		this.configurations = configurations;
	}

	@XmlElement(name="target", required=true)
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@XmlElementWrapper(name="configurations", required=true)
	@XmlElement(name="config")
	public List<ConfigurationReference> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<ConfigurationReference> configurations) {
		this.configurations = configurations;
	}
	
}
