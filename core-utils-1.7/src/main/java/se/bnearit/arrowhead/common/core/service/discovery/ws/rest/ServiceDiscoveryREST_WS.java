package se.bnearit.arrowhead.common.core.service.discovery.ws.rest;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.github.danieln.dnssdjava.DnsSDDomainEnumerator;
import com.github.danieln.dnssdjava.DnsSDException;
import com.github.danieln.dnssdjava.DnsSDFactory;
import com.github.danieln.dnssdjava.ServiceData;
import com.github.danieln.dnssdjava.ServiceName;
import com.github.danieln.dnssdjava.ServiceType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import se.bnearit.arrowhead.common.core.service.discovery.ServiceDiscovery;
import se.bnearit.arrowhead.common.core.service.discovery.dnssd.ServiceDiscoveryDnsSD;
import se.bnearit.arrowhead.common.core.service.discovery.endpoint.HttpEndpoint;
import se.bnearit.arrowhead.common.core.service.discovery.endpoint.TcpEndpoint;
import se.bnearit.arrowhead.common.core.service.discovery.endpoint.UdpEndpoint;
import se.bnearit.arrowhead.common.core.service.discovery.exception.MetadataException;
import se.bnearit.arrowhead.common.core.service.discovery.exception.ServiceRegisterException;
import se.bnearit.arrowhead.common.core.service.orchestration.data.OrchestrationResult;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.ConfigurationRequest;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.GetActiveConfigurationResponse;
import se.bnearit.arrowhead.common.service.ServiceEndpoint;
import se.bnearit.arrowhead.common.service.ServiceIdentity;
import se.bnearit.arrowhead.common.service.ServiceInformation;
import se.bnearit.arrowhead.common.service.ServiceMetadata;
import se.bnearit.arrowhead.common.service.ws.rest.ClientFactoryREST_WS;

public class ServiceDiscoveryREST_WS implements ServiceDiscovery {

	private static final Logger LOG = Logger.getLogger(ServiceDiscoveryDnsSD.class.getName());

	private final HttpEndpoint endpoint;
	private final ClientFactoryREST_WS clientFactory;

	private String registerDomain;
	//private String computerDomain;

	public ServiceDiscoveryREST_WS(HttpEndpoint endpoint, ClientFactoryREST_WS clientFactory) {
		this.endpoint = endpoint;
		this.clientFactory = clientFactory;

		// This is for getting registering domain in order to make service name.
		// DNS Server Address and domain should be specified
		registerDomain = System.getProperty("dnssd.registerDomain");
		if (registerDomain == null) {
			String computerDomain = System.getProperty("dnssd.domain");
			DnsSDDomainEnumerator de;
			if (computerDomain != null) {
				de = DnsSDFactory.getInstance().createDomainEnumerator(computerDomain);
			} else {
				de = DnsSDFactory.getInstance().createDomainEnumerator();
				computerDomain = de.getDefaultBrowsingDomain();

			}
			registerDomain = de.getDefaultRegisteringDomain();
		}
	

	}
	
	public ServiceDiscoveryREST_WS(HttpEndpoint endpoint, ClientFactoryREST_WS clientFactory, String registerDomain) {
		this.registerDomain = registerDomain;
		this.endpoint = endpoint;
		this.clientFactory = clientFactory;
		
	}

	@Override
	public List<String> getSupportedEndpointTypes() {

		return Arrays.asList(TcpEndpoint.ENDPOINT_TYPE, HttpEndpoint.ENDPOINT_TYPE, UdpEndpoint.ENDPOINT_TYPE);

	}

	@Override
	public List<ServiceIdentity> getAllServices() {

		List<ServiceIdentity> results = new ArrayList<ServiceIdentity>();

		try {
			Client client = clientFactory.createClient(endpoint.isSecure());
			WebTarget target = client.target(endpoint.toURL().toURI()).path("service");
			GetAllServiceResponse response = target.request(MediaType.APPLICATION_XML_TYPE)
					.get(GetAllServiceResponse.class);
			List<ServiceInformationResponse> services = response.getServices();
			if (services != null) {
				LOG.info("Got services information: " + services);

				for (ServiceInformationResponse service : services) {
					ServiceIdentity identity = new ServiceIdentity(service.getName(), service.getType());
					results.add(identity);
				}

			}
		} catch (MalformedURLException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (URISyntaxException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (ProcessingException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (WebApplicationException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		}
		return results;

	}

	@Override
	public List<ServiceIdentity> getServicesByType(String type) {
		List<ServiceIdentity> results = new ArrayList<ServiceIdentity>();

		try {

			Client client = clientFactory.createClient(endpoint.isSecure());
			WebTarget target = client.target(endpoint.toURL().toURI()).path("type").path(type);

			GetAllServiceResponse response = target.request(MediaType.APPLICATION_XML_TYPE).get(GetAllServiceResponse.class);
			List<ServiceInformationResponse> services = response.getServices();
			if (services != null) {
				LOG.info("Got services information: " + services);

				for (ServiceInformationResponse service : services) {
					ServiceIdentity identity = new ServiceIdentity(service.getName(), service.getType());
					results.add(identity);
				}
			}
		} catch (MalformedURLException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (URISyntaxException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (ProcessingException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (WebApplicationException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		}
		return results;
	}

	@Override
	public ServiceInformation getServiceInformation(ServiceIdentity identity, String endpointType) {

		ServiceInformation info = null;

		try {
			Client client = clientFactory.createClient(endpoint.isSecure());
			WebTarget target = client.target(endpoint.toURL().toURI()).path("service").path(identity.getId());

			ServiceInformationResponse response = target.request(MediaType.APPLICATION_XML_TYPE)
					.get(ServiceInformationResponse.class);

			if (response != null) {
				LOG.info("Got service information: " + response);

				ServiceEndpoint endpoint;
				ServiceMetadata metadata;
				if (endpointType.equals(TcpEndpoint.ENDPOINT_TYPE)) {
					endpoint = new TcpEndpoint(response.getHost(), response.getPort());
					metadata = new ServiceMetadata();

					for (ServicePropertyResponse property : response.getProperties()) {
						metadata.put(property.getName(), property.getValue());
					}

				} else if (endpointType.equals(HttpEndpoint.ENDPOINT_TYPE)) {

					metadata = new ServiceMetadata();
					for (ServicePropertyResponse property : response.getProperties()) {
						metadata.put(property.getName(), property.getValue());
					}

					endpoint = new HttpEndpoint(response.getHost(), response.getPort(), metadata.get("path"),
							identity.getType().contains("https"));

					metadata.remove("path");
				} else if (endpointType.equals(UdpEndpoint.ENDPOINT_TYPE)) {
					endpoint = new UdpEndpoint(response.getHost(), response.getPort());
					metadata = new ServiceMetadata();
					for (ServicePropertyResponse property : response.getProperties()) {
						metadata.put(property.getName(), property.getValue());
					}
				} else {
					LOG.severe("DNS-SD can't handle endpoint type " + endpointType);
					throw new RuntimeException("Can't handle endpoint type: " + endpointType);
				}
				info = new ServiceInformation(new ServiceIdentity(response.getName(), response.getType()), endpoint, metadata);

			}
		} catch (MalformedURLException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (URISyntaxException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (ProcessingException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (WebApplicationException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		}

		return info;
	}

	@Override
	public String createServiceName(String name, String serviceType) throws ServiceRegisterException {
		ServiceName serviceName = new ServiceName(name, ServiceType.valueOf(serviceType), registerDomain);

		try {
			Client client = clientFactory.createClient(endpoint.isSecure());
			WebTarget target = client.target(endpoint.toURL().toURI()).path("service").path(serviceName.toString());

			ServiceInformationResponse response = target.request(MediaType.APPLICATION_XML_TYPE)
					.get(ServiceInformationResponse.class);

			if (response != null) {
				LOG.info("Got service information: " + response);

				LOG.info("DNS-SD service id " + serviceName + " is already registered");
				throw new ServiceRegisterException(
						String.format("Service name %s is already registered", serviceName.toString()));

			}
		} catch (MalformedURLException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (URISyntaxException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (ProcessingException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (WebApplicationException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		}

		LOG.info("DNS-SD created service id " + serviceName + " from name " + name + " and type " + serviceType);

		return serviceName.toString();
	}

	@Override
	public boolean isPublished(ServiceInformation information) {
		boolean published = false;

		try {
			Client client = clientFactory.createClient(endpoint.isSecure());
			WebTarget target = client.target(endpoint.toURL().toURI()).path("service")
					.path(information.getIdentity().getId());

			ServiceInformationResponse response = target.request(MediaType.APPLICATION_XML_TYPE)
					.get(ServiceInformationResponse.class);

			if (response != null) {
				LOG.info("Got service information: " + response);

				String epHost, sdHost;
				if (information.getEndpoint().getType().equals(TcpEndpoint.ENDPOINT_TYPE)) {
					TcpEndpoint tcpEndpoint = (TcpEndpoint) information.getEndpoint();
					epHost = ServiceDiscoveryDnsSD.addDotAtEnd(tcpEndpoint.getHost());
					sdHost = ServiceDiscoveryDnsSD.addDotAtEnd(response.getHost());
					if (epHost.equalsIgnoreCase(sdHost) && tcpEndpoint.getPort() == response.getPort()) {
						published = true;
					}
				} else if (information.getEndpoint().getType().equals(UdpEndpoint.ENDPOINT_TYPE)) {
					UdpEndpoint udpEndpoint = (UdpEndpoint) information.getEndpoint();
					epHost = ServiceDiscoveryDnsSD.addDotAtEnd(udpEndpoint.getHost());
					sdHost = ServiceDiscoveryDnsSD.addDotAtEnd(response.getHost());
					if (epHost.equalsIgnoreCase(sdHost) && udpEndpoint.getPort() == response.getPort()) {
						published = true;
					}
				} else if (information.getEndpoint().getType().equals(HttpEndpoint.ENDPOINT_TYPE)) {
					HttpEndpoint httpEndpoint = (HttpEndpoint) information.getEndpoint();
					epHost = ServiceDiscoveryDnsSD.addDotAtEnd(httpEndpoint.getHost());
					sdHost = ServiceDiscoveryDnsSD.addDotAtEnd(response.getHost());

					ServiceMetadata metadata = new ServiceMetadata();
					for (ServicePropertyResponse property : response.getProperties()) {
						metadata.put(property.getName(), property.getValue());
					}

					if (epHost.equalsIgnoreCase(sdHost) && httpEndpoint.getPort() == response.getPort()
							&& httpEndpoint.getPath().equals(metadata.get("path"))) {
						published = true;
					}
				} else {
					LOG.severe("DNS-SD can't handle endpoint type " + information.getEndpoint().getType());
					throw new RuntimeException("Can't handle endpoint type: " + information.getEndpoint().getType());
				}

			}
		} catch (MalformedURLException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (URISyntaxException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (ProcessingException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		} catch (WebApplicationException e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		}

		return published;
	}

	@Override
	public void publish(ServiceInformation information) throws MetadataException, ServiceRegisterException {
		try {
			
			String serviceName = information.getIdentity().getId();
			if(serviceName.contains("."))
			{
				String[] split = serviceName.split("\\.");
				serviceName = split[0];
			}
			
			ServiceInformationResponse serviceInfo = new ServiceInformationResponse();
			//serviceInfo.setDomain(computerDomain);
			serviceInfo.setName(serviceName);
			serviceInfo.setType(information.getIdentity().getType());
			
			List<ServicePropertyResponse> serviceProperties = new ArrayList<ServicePropertyResponse>();
            ServiceMetadata metadata = information.getMetadata();
            
            if (information.getEndpoint() instanceof TcpEndpoint) {
                TcpEndpoint endpoint = (TcpEndpoint) information.getEndpoint();
                serviceInfo.setHost(ServiceDiscoveryDnsSD.addDotAtEnd(endpoint.getHost()));
                serviceInfo.setPort(endpoint.getPort());
                
                for(String propName : metadata.keySet())
                {
                	ServicePropertyResponse property = new ServicePropertyResponse();
                	property.setName(propName);
                	property.setValue(metadata.get(propName));
                	serviceProperties.add(property);
                }
                
            } else if (information.getEndpoint() instanceof UdpEndpoint) {
                UdpEndpoint endpoint = (UdpEndpoint) information.getEndpoint();
                serviceInfo.setHost(ServiceDiscoveryDnsSD.addDotAtEnd(endpoint.getHost()));
                serviceInfo.setPort(endpoint.getPort());
                
                
                for(String propName : metadata.keySet())
                {
                	ServicePropertyResponse property = new ServicePropertyResponse();
                	property.setName(propName);
                	property.setValue(metadata.get(propName));
                	serviceProperties.add(property);
                }
                
            } else if (information.getEndpoint() instanceof HttpEndpoint) {
                HttpEndpoint endpoint = (HttpEndpoint) information.getEndpoint();
                serviceInfo.setHost(ServiceDiscoveryDnsSD.addDotAtEnd(endpoint.getHost()));
                serviceInfo.setPort(endpoint.getPort());
                
                ServicePropertyResponse property = new ServicePropertyResponse();
            	property.setName("path");
            	property.setValue(endpoint.getPath());
            	serviceProperties.add(property);
            	
                if (metadata.containsKey("path")) {
                    throw new MetadataException("Reserved key 'path' in metadata");
                }
                
                
                for(String propName : metadata.keySet())
                {
                	property = new ServicePropertyResponse();
                	property.setName(propName);
                	property.setValue(metadata.get(propName));
                	serviceProperties.add(property);
                }
                
                
            } else {
                LOG.severe("DNS-SD can't handle endpoint class " + information.getEndpoint().getClass());
                throw new ServiceRegisterException("Can't handle endpoint class: " + information.getEndpoint().getClass());
            }
            
            serviceInfo.setProperties(serviceProperties);
            
            
            
            com.sun.jersey.api.client.Client client = clientFactory.createClient_v18(endpoint.isSecure());
            WebResource target = client.resource(endpoint.toURL().toURI()).path("publish");                    
            LOG.info("Calling remote service at uri: " + target.getURI().toString());
            
            target.accept(MediaType.APPLICATION_XML_TYPE).post(serviceInfo);
            
        } catch (UniformInterfaceException e) {
            LOG.severe("DNS-SD failed to register service " + information.getIdentity() + " with endpoint " + information.getEndpoint() + ": " + e.getMessage());
            throw new ServiceRegisterException(e);
        } catch (MalformedURLException e) {
        	 LOG.severe("DNS-SD failed to register service " + information.getIdentity() + " with endpoint " + information.getEndpoint() + ": " + e.getMessage());
             throw new ServiceRegisterException(e);
		} catch (URISyntaxException e) {
			 LOG.severe("DNS-SD failed to register service " + information.getIdentity() + " with endpoint " + information.getEndpoint() + ": " + e.getMessage());
	            throw new ServiceRegisterException(e);
		}
        LOG.info("DNS-SD registered service " + information.getIdentity() + " with endpoint " + information.getEndpoint());

	}

	@Override
	public void unpublish(ServiceIdentity identity) throws ServiceRegisterException {
		try {
			
			
			String serviceName = identity.getId();
			if(serviceName.contains("."))
			{
				String[] split = serviceName.split("\\.");
				serviceName = split[0];
			}
			
			
			ServiceInformationResponse serviceInfo = new ServiceInformationResponse();
			serviceInfo.setName(serviceName);
			
          
			 com.sun.jersey.api.client.Client client = clientFactory.createClient_v18(endpoint.isSecure());
	         WebResource target = client.resource(endpoint.toURL().toURI()).path("unpublish");                    
	         LOG.info("Calling remote service at uri: " + target.getURI().toString());
	            
	         target.accept(MediaType.APPLICATION_XML_TYPE).post(serviceInfo);
	            
        } catch (Exception e) {
            LOG.severe("DNS-SD failed to unregister service " + identity + ": " + e.getMessage());
            throw new ServiceRegisterException(e);
        }
        LOG.info("DNS-SD unregistered service " + identity);
	}

	@Override
	public String getHostName() {
		String hostname = System.getProperty("dnssd.hostname");

		if (hostname == null) {
			try {
				hostname = InetAddress.getLocalHost().getCanonicalHostName();
			} catch (UnknownHostException e) {
				throw new RuntimeException("Failed to find hostname", e);
			}
		}

		return hostname;
	}
	


	
	
}
