package se.bnearit.arrowhead.common.core.service.discovery.ws.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="service")
public class ServiceInformationResponse {
	
	private String domain;
	private String host;
	private String name;
	private int port;
	private List<ServicePropertyResponse> properties;
	private String type;
	
	
	@XmlElement(name="domain")
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@XmlElement(name="host")
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	@XmlElement(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="port")
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	@XmlElementWrapper(name="properties")
	@XmlElement(name="property")
	public List<ServicePropertyResponse> getProperties() {
		return properties;
	}
	public void setProperties(List<ServicePropertyResponse> properties) {
		this.properties = properties;
	}
	
	@XmlElement(name="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	
}
