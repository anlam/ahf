package se.bnearit.arrowhead.common.core.service.discovery.ws.rest;


import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="serviceList")
public class GetAllServiceResponse {

	private List<ServiceInformationResponse> services;

	@XmlElement(name="service")
	public List<ServiceInformationResponse> getServices() {
		return services;
	}

	public void setServices(List<ServiceInformationResponse> services) {
		this.services = services;
	}
	
	
	
	
}
