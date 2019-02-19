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

@XmlRootElement(name="configurationTargets")
public class GetConfigurationTargetsResponse {

	private List<String> targets;
	
	public GetConfigurationTargetsResponse() {
	}

	public GetConfigurationTargetsResponse(List<String> targets) {
		super();
		this.targets = targets;
	}

	@XmlElementWrapper(name="targets")
	@XmlElement(name="target")
	public List<String> getTargets() {
		return targets;
	}

	public void setTargets(List<String> targets) {
		this.targets = targets;
	}

}
