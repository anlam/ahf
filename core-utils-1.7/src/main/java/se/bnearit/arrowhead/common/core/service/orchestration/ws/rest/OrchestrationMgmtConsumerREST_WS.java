package se.bnearit.arrowhead.common.core.service.orchestration.ws.rest;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
//import java.util.Comparator;

import javax.ws.rs.core.MediaType;

import se.bnearit.arrowhead.common.core.service.discovery.endpoint.HttpEndpoint;
import se.bnearit.arrowhead.common.core.service.orchestration.OrchestrationManagement;
import se.bnearit.arrowhead.common.core.service.orchestration.data.OrchestrationConfig;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.ConfigurationReference;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.GetActiveConfigurationResponse;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.GetConfigurationsResponse;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.GetConfigurationTargetsResponse;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.OrchestrationConfigRequest;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.OrchestrationConfigResponse;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.OrchestrationRulesRequest;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.SetActiveConfigurationRequest;
import se.bnearit.arrowhead.common.service.ws.rest.ClientFactoryREST_WS;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
//import java.util.Collections;
import java.util.logging.Level;

//import se.bnearit.arrowhead.common.core.service.discovery.dnssd.ServiceDiscoveryDnsSD;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.AvailableUnits;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.ServiceTypesResponse;

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

public class OrchestrationMgmtConsumerREST_WS implements OrchestrationManagement {

    private static final Logger LOG = Logger.getLogger(OrchestrationMgmtConsumerREST_WS.class.getName());

    private final HttpEndpoint endpoint;
    private final ClientFactoryREST_WS clientFactory;
    //private final OrchestrationPushConsumerREST_WS pushConsumer;
    
    public OrchestrationMgmtConsumerREST_WS(HttpEndpoint endpoint, ClientFactoryREST_WS clientFactory) {
        this.endpoint = endpoint;
        this.clientFactory = clientFactory;
        //pushConsumer = new OrchestrationPushConsumerREST_WS( new ServiceDiscoveryDnsSD(), clientFactory);
    }

    @Override
    public List<String> getConfigTargets() {
        List<String> result = new ArrayList<String>();
        

        try {
            Client client = clientFactory.createClient_v18(endpoint.isSecure());
            WebResource target = client.resource(endpoint.toURL().toURI()).path("orchestration-mgmt").path("targets");
            GetConfigurationTargetsResponse response = target.accept(MediaType.APPLICATION_XML_TYPE).get(GetConfigurationTargetsResponse.class);
            result.addAll(response.getTargets());
        } catch (MalformedURLException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (URISyntaxException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (UniformInterfaceException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }

        return result;
    }

    @Override
    public List<String> getConfigurationsForTarget(String target) {
        List<String> result = new ArrayList<String>();

        try {
            Client client = clientFactory.createClient_v18(endpoint.isSecure());
            WebResource webTarget = client.resource(endpoint.toURL().toURI()).path("orchestration-mgmt").path("configurations").path(target);
            GetConfigurationsResponse response = webTarget
                    .accept(MediaType.APPLICATION_XML_TYPE)
                    .get(GetConfigurationsResponse.class);
            for (ConfigurationReference cfgRef : response.getConfigurations()) {
                result.add(cfgRef.getName());
            }
        } catch (MalformedURLException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (URISyntaxException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (UniformInterfaceException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }

        return result;
    }

    @Override
    public OrchestrationConfig getConfiguration(String target, String configName) {
        OrchestrationConfig result = null;

        try {
            Client client = clientFactory.createClient_v18(endpoint.isSecure());
            WebResource webTarget = client.resource(endpoint.toURL().toURI()).path("orchestration-mgmt").path("configurations").path(target).path(configName);
            OrchestrationConfigResponse response = webTarget
                    .accept(MediaType.APPLICATION_XML_TYPE)
                    .get(OrchestrationConfigResponse.class);
            result = new OrchestrationConfig(
                    response.getTarget(),
                    response.getName(),
                    response.getSerialNumber(),
                    response.getLastUpdated(),
                    response.getRules());
        } catch (MalformedURLException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (URISyntaxException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (UniformInterfaceException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }

        return result;
    }

    @Override
    public OrchestrationConfig createConfiguration(String target, String configName, List<String> rules) {
        OrchestrationConfig result = null;

        try {
            Client client = clientFactory.createClient_v18(endpoint.isSecure());
            WebResource webTarget = client.resource(endpoint.toURL().toURI()).path("orchestration-mgmt").path("configurations");
            OrchestrationConfigRequest request = new OrchestrationConfigRequest(target, configName, rules);
            OrchestrationConfigResponse response = webTarget
                    .accept(MediaType.APPLICATION_XML_TYPE)
                    .post(OrchestrationConfigResponse.class, request);
            result = new OrchestrationConfig(
                    response.getTarget(),
                    response.getName(),
                    response.getSerialNumber(),
                    response.getLastUpdated(),
                    response.getRules());
            
            //pushConsumer.pushConfiguration( result);
        } catch (MalformedURLException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (URISyntaxException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (UniformInterfaceException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }

        return result;
    }

    @Override
    public OrchestrationConfig updateConfiguration(String target, String configName, List<String> rules) {
        OrchestrationConfig result = null;

        try {
            Client client = clientFactory.createClient_v18(endpoint.isSecure());
            WebResource webTarget = client.resource(endpoint.toURL().toURI()).path("orchestration-mgmt").path("configurations").path(target).path(configName);
            OrchestrationRulesRequest request = new OrchestrationRulesRequest(rules);
            OrchestrationConfigResponse response = webTarget
                    .accept(MediaType.APPLICATION_XML_TYPE)
                    .put(OrchestrationConfigResponse.class, request);
            result = new OrchestrationConfig(
                    response.getTarget(),
                    response.getName(),
                    response.getSerialNumber(),
                    response.getLastUpdated(),
                    response.getRules());
            
            //pushConsumer.pushConfiguration( result);
        } catch (MalformedURLException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (URISyntaxException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (UniformInterfaceException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }

        return result;
    }

    @Override
    public boolean deleteConfiguration(String target, String configName) {
        boolean result = false;

        try {
            Client client = clientFactory.createClient_v18(endpoint.isSecure());
            WebResource webTarget = client.resource(endpoint.toURL().toURI()).path("orchestration-mgmt").path("configurations").path(target).path(configName);
            webTarget.accept(MediaType.APPLICATION_XML_TYPE).delete();
            result = true;
            
            // Hämta alla configuration (names) för target, hämta konfigurationer, sortera på lastUpdated, push() på nyaste?
            /*
            List<String> configs = getConfigurationsForTarget( target);
            if ( !configs.isEmpty())
            {
                List<OrchestrationConfig> configObjs = new ArrayList<OrchestrationConfig>();
                for ( String cfgName : configs)
                {
                    OrchestrationConfig ocfg = getConfiguration(target, cfgName);
                    if (null != ocfg)
                    {
                        configObjs.add( ocfg);
                    }
                }                
                if ( !configObjs.isEmpty())
                {
                    Collections.sort(configObjs, new Comparator<OrchestrationConfig>() 
                        { 
                            @Override
                            public int compare(OrchestrationConfig c1, OrchestrationConfig c2) 
                            { 
                                return c2.getLastUpdated().compareTo(c1.getLastUpdated()); 
                            } 
                        } 
                    );
                    pushConsumer.pushConfiguration( configObjs.get( 0));
                }
            }
            */
        } catch (MalformedURLException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (URISyntaxException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (UniformInterfaceException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }

        return result;
    }

    @Override
    public String getActiveConfiguration(String target) {
        String result = null;

        try {
            Client client = clientFactory.createClient_v18(endpoint.isSecure());
            WebResource webTarget = client.resource(endpoint.toURL().toURI()).path("orchestration-mgmt").path("active-config").path(target);
            GetActiveConfigurationResponse response = webTarget
                    .accept(MediaType.APPLICATION_XML_TYPE)
                    .get(GetActiveConfigurationResponse.class);
            result = response.getConfig().getName();
        } catch (MalformedURLException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (URISyntaxException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (UniformInterfaceException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }

        return result;
    }

    @Override
    public boolean setActiveConfig(String target, String configName) {
        boolean result = false;

        try {
            Client client = clientFactory.createClient_v18(endpoint.isSecure());
            WebResource webTarget = client.resource(endpoint.toURL().toURI()).path("orchestration-mgmt").path("active-config").path(target);
            SetActiveConfigurationRequest request = new SetActiveConfigurationRequest(target, configName);
            webTarget.accept(MediaType.APPLICATION_XML_TYPE).put(request);
            result = true;
            
            /*
            OrchestrationConfig config = getConfiguration(target, configName);
            if (null != config)
            {
                pushConsumer.pushConfiguration( config);
            }
            */

        } catch (MalformedURLException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (URISyntaxException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (UniformInterfaceException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }

        return result;
    }

    @Override
    public List<String> getSupportedConsumptionServiceTypesForTarget(String target) {
        List<String> result = null;
        try {
            /*javax.ws.rs.client.Client client = clientFactory.createClient(endpoint.isSecure());
            WebTarget webTarget = client.target(endpoint.toURL().toURI()).path("orchestration-mgmt").path("capabilities").path("consumertypes").path(target);
            LOG.info("GetSupportedServiceTypesForTarget requests " + webTarget.getUri());
            Response resp = webTarget.request().get();
            ServiceTypesResponse response = resp.readEntity(ServiceTypesResponse.class);
            for (String s : response.getServiceTypes()){
                LOG.info("GetSupportedServiceTypesForTarget type:" + s);
            }*/
            Client client = clientFactory.createClient_v18(endpoint.isSecure());
            WebResource webTarget = client.resource(endpoint.toURL().toURI()).path("orchestration-mgmt").path("capabilities").path("consumertypes").path(target);
            LOG.log(Level.INFO, "GetSupportedServiceTypesForTarget requested {0}", webTarget);
            ServiceTypesResponse response = webTarget.accept(MediaType.APPLICATION_XML_TYPE).get(ServiceTypesResponse.class);
            for (String s : response.getServiceTypes()){
                LOG.log(Level.INFO, "GetSupportedServiceTypesForTarget type:{0}", s);
            }
            result = response.getServiceTypes();
        } catch (MalformedURLException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (URISyntaxException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (UniformInterfaceException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }

        return result;
    }

    @Override
    public Integer getAvailableConsumptionsForServiceType(String target, String serviceType) {
        Integer result = null;
        try {
            Client client = clientFactory.createClient_v18(endpoint.isSecure());
            WebResource webTarget = client.resource(endpoint.toURL().toURI()).path("orchestration-mgmt").path("capabilities").path("consumertypes").path(target).path(serviceType);
            AvailableUnits response = webTarget
                    .accept(MediaType.APPLICATION_XML_TYPE)
                    .get(AvailableUnits.class);
            result = response.getAvailable();
        } catch (MalformedURLException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (URISyntaxException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (UniformInterfaceException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }
        return result;
    }

}
