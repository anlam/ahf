package se.bnearit.arrowhead.common.core.service.discovery.ws.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="property")
public class ServicePropertyResponse {

	private String name;
	private String value;
	
	@XmlElement(name="name", required=true)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="value", required=true)
	public String getValue() {
		return value;
	}
	
	
	public void setValue(String value) {
		this.value = value;
	}
}
