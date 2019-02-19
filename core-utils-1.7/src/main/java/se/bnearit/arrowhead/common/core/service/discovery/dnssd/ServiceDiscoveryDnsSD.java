package se.bnearit.arrowhead.common.core.service.discovery.dnssd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import se.bnearit.arrowhead.common.core.service.discovery.ServiceDiscovery;
import se.bnearit.arrowhead.common.core.service.discovery.endpoint.HttpEndpoint;
import se.bnearit.arrowhead.common.core.service.discovery.endpoint.TcpEndpoint;
import se.bnearit.arrowhead.common.core.service.discovery.exception.MetadataException;
import se.bnearit.arrowhead.common.core.service.discovery.exception.ServiceRegisterException;
import se.bnearit.arrowhead.common.service.ServiceEndpoint;
import se.bnearit.arrowhead.common.service.ServiceIdentity;
import se.bnearit.arrowhead.common.service.ServiceInformation;
import se.bnearit.arrowhead.common.service.ServiceMetadata;

import com.github.danieln.dnssdjava.DnsSDBrowser;
import com.github.danieln.dnssdjava.DnsSDDomainEnumerator;
import com.github.danieln.dnssdjava.DnsSDException;
import com.github.danieln.dnssdjava.DnsSDFactory;
import com.github.danieln.dnssdjava.DnsSDRegistrator;
import com.github.danieln.dnssdjava.ServiceData;
import com.github.danieln.dnssdjava.ServiceName;
import com.github.danieln.dnssdjava.ServiceType;
import se.bnearit.arrowhead.common.core.service.discovery.endpoint.UdpEndpoint;

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
 * Implementation of a DNS-SD service discovery mechanism.
 *
 * System properties that is used by this service discovery: dns.server, dnssd.hostname, dnssd.domain,
 * dnssd.browsingDomains, dnssd.registerDomain, dnssd.tsig. 
 * All the properties can be left out and each one of them can be set individually.
 *
 * @author BnearIT
 */
public class ServiceDiscoveryDnsSD implements ServiceDiscovery {

    private static final Logger LOG = Logger.getLogger(ServiceDiscoveryDnsSD.class.getName());

    private String tsigKeyName;
    private String tsigKey;
    private DnsSDBrowser browser;
    private DnsSDRegistrator registrator;

    /**
     * Creates a DNS-SD service discovery using default computer dns settings. These can be
     * overridden by using java system properties dns.server, dnssd.domain,
     * dnssd.browsingDomains (comma (,) separated list), dnssd.registerDomain.
     * TSIG file name can also be specified by dnssd.tsig property.
     *
     * @param tsig a file name for a two line file including a TSIG key and
     * secret, or null if no TSIG shall be used
     */
    public ServiceDiscoveryDnsSD(String tsig) {
        try {
            DnsSDDomainEnumerator de;
            String computerDomain = System.getProperty("dnssd.domain");
            if (computerDomain != null) {
                LOG.info("DNS-SD overriding computer domain: " + computerDomain);
                de = DnsSDFactory.getInstance().createDomainEnumerator(computerDomain);
            } else {
                de = DnsSDFactory.getInstance().createDomainEnumerator();
            }
            String browsingDomains = System.getProperty("dnssd.browsingDomains");
            if (browsingDomains != null) {
                List<String> bDomains = Arrays.asList(browsingDomains.split(","));
                LOG.info("DNS-SD overriding browsing domains: " + bDomains);
                browser = DnsSDFactory.getInstance().createBrowser(bDomains);
            } else {
                browser = DnsSDFactory.getInstance().createBrowser(de.getBrowsingDomains());
            }
            String registerDomain = System.getProperty("dnssd.registerDomain");
            if (registerDomain != null) {
                LOG.info("DNS-SD overriding register domain: " + registerDomain);
                registrator = DnsSDFactory.getInstance().createRegistrator(registerDomain);
            } else {
                registrator = DnsSDFactory.getInstance().createRegistrator(de.getDefaultRegisteringDomain());
            }
            String tsigFileName;
            if (tsig != null) {
                tsigFileName = tsig;
            } else {
                tsigFileName = System.getProperty("dnssd.tsig");
            }
            if (tsigFileName != null) {
                loadTsig(tsigFileName);
                if (tsigKeyName != null && tsigKey != null) {
                    registrator.setTSIGKey(tsigKeyName, DnsSDRegistrator.TSIG_ALGORITHM_HMAC_MD5, tsigKey);
                    LOG.info("DNS-SD using TSIG: " + tsigKeyName);
                }
            }
            LOG.info("DNS-SD service register started (endpointTypes: " + getSupportedEndpointTypes() + ")");
        } catch (DnsSDException e) {
            LOG.severe("DNS-SD service register failed to start: " + e.getMessage());
        }
    }

    /**
     * Creates a DNS-SD service discovery using default settings. Host dns settings can be
     * overridden by using java system properties dns.server, dnssd.domain,
     * dnssd.browsingDomains (comma (,) separated list), dnssd.registerDomain.
     * dnssd.tsig can also be used to specify a tsig file name.
     */
    public ServiceDiscoveryDnsSD() {
        this(null);
    }

    @Override
    public List<String> getSupportedEndpointTypes() {
        return Arrays.asList(TcpEndpoint.ENDPOINT_TYPE, HttpEndpoint.ENDPOINT_TYPE, UdpEndpoint.ENDPOINT_TYPE);
    }

    @Override
    public List<ServiceIdentity> getAllServices() {
        List<ServiceIdentity> results = new ArrayList<ServiceIdentity>();
        for (ServiceType type : browser.getServiceTypes()) {
            for (ServiceName name : browser.getServiceInstances(type)) {
                ServiceIdentity identity = new ServiceIdentity(name.toString(), name.getType().toString());
                results.add(identity);
            }
        }
        return results;
    }

    @Override
    public List<ServiceIdentity> getServicesByType(String type) {
        Collection<ServiceName> services = browser.getServiceInstances(ServiceType.valueOf(type));
        List<ServiceIdentity> results = new ArrayList<ServiceIdentity>(services.size());
        for (ServiceName name : services) {
            ServiceIdentity identity = new ServiceIdentity(name.toString(), name.getType().toString());
            results.add(identity);
        }
        return results;
    }

    @Override
    public ServiceInformation getServiceInformation(ServiceIdentity identity, String endpointType) {
        ServiceInformation info = null;
        ServiceData data = browser.getServiceData(ServiceName.valueOf(identity.getId()));
        if (data != null) {
            ServiceEndpoint endpoint;
            ServiceMetadata metadata;
            if (endpointType.equals(TcpEndpoint.ENDPOINT_TYPE)) {
                endpoint = new TcpEndpoint(data.getHost(), data.getPort());
                metadata = new ServiceMetadata();
                metadata.putAll(data.getProperties());
            } else if (endpointType.equals(HttpEndpoint.ENDPOINT_TYPE)) {
                endpoint = new HttpEndpoint(
                        data.getHost(),
                        data.getPort(),
                        data.getProperties().get("path"),
                        identity.getType().contains("https"));
                metadata = new ServiceMetadata();
                metadata.putAll(data.getProperties());
                metadata.remove("path");
            } else if (endpointType.equals(UdpEndpoint.ENDPOINT_TYPE)){
                endpoint = new UdpEndpoint(data.getHost(), data.getPort());
                metadata = new ServiceMetadata();
                metadata.putAll(data.getProperties());
            } else {
                LOG.severe("DNS-SD can't handle endpoint type " + endpointType);
                throw new RuntimeException("Can't handle endpoint type: " + endpointType);
            }
            info = new ServiceInformation(identity, endpoint, metadata);
        }
        return info;
    }

    @Override
    public String createServiceName(String name, String serviceType) throws ServiceRegisterException {
        ServiceName serviceName = registrator.makeServiceName(name, ServiceType.valueOf(serviceType));
        ServiceData data = browser.getServiceData(serviceName);
        if (data != null) {
            LOG.info("DNS-SD service id " + serviceName + " is already registered");
            throw new ServiceRegisterException(String.format("Service name %s is already registered", serviceName.toString()));
        }

        LOG.info("DNS-SD created service id " + serviceName + " from name " + name + " and type " + serviceType);

        return serviceName.toString();
    }

    @Override
    public boolean isPublished(ServiceInformation information) {
        boolean published = false;
        ServiceData data = browser.getServiceData(ServiceName.valueOf(information.getIdentity().getId()));
        if (data != null) {
            String epHost, sdHost;
            if (information.getEndpoint().getType().equals(TcpEndpoint.ENDPOINT_TYPE)) {
                TcpEndpoint tcpEndpoint = (TcpEndpoint) information.getEndpoint();
                epHost = addDotAtEnd(tcpEndpoint.getHost());
                sdHost = addDotAtEnd(data.getHost());
                if (epHost.equalsIgnoreCase(sdHost) && tcpEndpoint.getPort() == data.getPort()) {
                    published = true;
                }
            } else if (information.getEndpoint().getType().equals(UdpEndpoint.ENDPOINT_TYPE)) {
                UdpEndpoint udpEndpoint = (UdpEndpoint) information.getEndpoint();
                epHost = addDotAtEnd(udpEndpoint.getHost());
                sdHost = addDotAtEnd(data.getHost());
                if (epHost.equalsIgnoreCase(sdHost) && udpEndpoint.getPort() == data.getPort()) {
                    published = true;
                }
            }else if (information.getEndpoint().getType().equals(HttpEndpoint.ENDPOINT_TYPE)) {
                HttpEndpoint httpEndpoint = (HttpEndpoint) information.getEndpoint();
                epHost = addDotAtEnd(httpEndpoint.getHost());
                sdHost = addDotAtEnd(data.getHost());
                if (epHost.equalsIgnoreCase(sdHost)
                        && httpEndpoint.getPort() == data.getPort()
                        && httpEndpoint.getPath().equals(data.getProperties().get("path"))) {
                    published = true;
                }
            } else {
                LOG.severe("DNS-SD can't handle endpoint type " + information.getEndpoint().getType());
                throw new RuntimeException("Can't handle endpoint type: " + information.getEndpoint().getType());
            }
        }
        return published;
    }

    @Override
    public void publish(ServiceInformation information) throws MetadataException, ServiceRegisterException {
        try {
            ServiceName name = ServiceName.valueOf(information.getIdentity().getId());
            ServiceData serviceData = new ServiceData();
            serviceData.setName(name);
            ServiceMetadata metadata = information.getMetadata();
            if (information.getEndpoint() instanceof TcpEndpoint) {
                TcpEndpoint endpoint = (TcpEndpoint) information.getEndpoint();
                serviceData.setHost(addDotAtEnd(endpoint.getHost()));
                serviceData.setPort(endpoint.getPort());
                serviceData.getProperties().putAll(metadata);
            } else if (information.getEndpoint() instanceof UdpEndpoint) {
                UdpEndpoint endpoint = (UdpEndpoint) information.getEndpoint();
                serviceData.setHost(addDotAtEnd(endpoint.getHost()));
                serviceData.setPort(endpoint.getPort());
                serviceData.getProperties().putAll(metadata);
            } else if (information.getEndpoint() instanceof HttpEndpoint) {
                HttpEndpoint endpoint = (HttpEndpoint) information.getEndpoint();
                serviceData.setHost(addDotAtEnd(endpoint.getHost()));
                serviceData.setPort(endpoint.getPort());
                serviceData.getProperties().put("path", endpoint.getPath());
                if (metadata.containsKey("path")) {
                    throw new MetadataException("Reserved key 'path' in metadata");
                }
                serviceData.getProperties().putAll(metadata);
            } else {
                LOG.severe("DNS-SD can't handle endpoint class " + information.getEndpoint().getClass());
                throw new ServiceRegisterException("Can't handle endpoint class: " + information.getEndpoint().getClass());
            }
            registrator.registerService(serviceData);
        } catch (DnsSDException e) {
            LOG.severe("DNS-SD failed to register service " + information.getIdentity() + " with endpoint " + information.getEndpoint() + ": " + e.getMessage());
            throw new ServiceRegisterException(e);
        }
        LOG.info("DNS-SD registered service " + information.getIdentity() + " with endpoint " + information.getEndpoint());
    }

    @Override
    public void unpublish(ServiceIdentity identity) throws ServiceRegisterException {
        try {
            registrator.unregisterService(ServiceName.valueOf(identity.getId()));
        } catch (DnsSDException e) {
            LOG.severe("DNS-SD failed to unregister service " + identity + ": " + e.getMessage());
            throw new ServiceRegisterException(e);
        }
        LOG.info("DNS-SD unregistered service " + identity);
    }

    /**
     * Transform the domain name into a absolute domain name by adding a dot at the end if such doesn't exist. 
     * @param domainName a fully qualified domain name
     * @return domainName with a dot at the end
     */
    public static String addDotAtEnd(String domainName) {
        if (domainName == null) {
            return null;
        }
        if (domainName.endsWith(".")) {
            return domainName;
        }
        return domainName + ".";
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

    private void loadTsig(String fileName) {
        tsigKeyName = tsigKey = null;
        File file = new File(fileName);
        if (file.exists()) {
            BufferedReader in = null;
            try {
                LOG.info("Loading TSIG key from: " + file);
                in = new BufferedReader(new FileReader(file));
                tsigKeyName = in.readLine();
                tsigKey = in.readLine();
                if (tsigKey != null) {
                    LOG.info("Using TSIG key '" + tsigKeyName + "' for registration.");
                } else {
                    tsigKeyName = null;
                }
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Error loading TSIG key", e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        LOG.warning("Error closing file: " + e.getMessage());
                    }
                }
            }
        } else {
            LOG.info("TSIG key file doesn't exits: " + file);
        }
    }

}
