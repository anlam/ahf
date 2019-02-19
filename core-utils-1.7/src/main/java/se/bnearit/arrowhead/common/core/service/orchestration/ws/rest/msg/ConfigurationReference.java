package se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

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

@XmlRootElement
public class ConfigurationReference {

	private String name;
	private String link;
	
	public ConfigurationReference() {
	}

	public ConfigurationReference(String name, String link) {
		this.name = name;
		this.link = link;
	}

	@XmlValue
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name="href")
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
