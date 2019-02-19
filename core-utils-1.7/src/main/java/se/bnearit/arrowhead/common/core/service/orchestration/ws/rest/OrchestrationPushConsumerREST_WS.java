package se.bnearit.arrowhead.common.core.service.orchestration.ws.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.logging.Logger;
//import java.util.logging.Level;

import org.apache.log4j.Logger;

import se.bnearit.arrowhead.common.core.service.discovery.ServiceDiscovery;
import se.bnearit.arrowhead.common.core.service.orchestration.OrchestrationPush;
import se.bnearit.arrowhead.common.core.service.orchestration.data.OrchestrationConfig;
import se.bnearit.arrowhead.common.core.service.discovery.endpoint.HttpEndpoint;
import se.bnearit.arrowhead.common.core.service.discovery.dnssd.ServiceDiscoveryDnsSD;

import javax.ws.rs.core.MediaType;
//import javax.ws.rs.client.Client;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.apache.log4j.Priority;
import se.bnearit.arrowhead.common.core.service.orchestration.data.OrchestrationResult;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.ConfigurationRequest;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.OrchestrationConfigResponse;

import se.bnearit.arrowhead.common.service.ServiceIdentity;
import se.bnearit.arrowhead.common.service.ServiceInformation;
import se.bnearit.arrowhead.common.service.ws.rest.ClientFactoryREST_WS;

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

public class OrchestrationPushConsumerREST_WS implements OrchestrationPush 
{
    private static final Logger LOG = Logger.getLogger(OrchestrationPushConsumerREST_WS.class.getName());
    private final ClientFactoryREST_WS ClientFact;
    private final ServiceDiscovery serviceDiscovery;

    public OrchestrationPushConsumerREST_WS(ServiceDiscovery servDisc, ClientFactoryREST_WS Cfac)
    {        
        ClientFact = Cfac;
        serviceDiscovery = servDisc;
    }
    
    private boolean findAndCallPush(String ServiceType, OrchestrationConfig config)
    {        
        LOG.info("findAndCallPush(), servicetype = " + ServiceType);
        List<ServiceIdentity> servicesByType = this.serviceDiscovery.getServicesByType(ServiceType);
        LOG.info("findAndCallPush(), servicebytype returned " + servicesByType.size() + " elements");
        boolean response = false;
                 
        for (ServiceIdentity si : servicesByType)
        {
            LOG.info("Found service identity: " + si);
            
            try 
            {                   
                ServiceInformation sinf = this.serviceDiscovery.getServiceInformation( si, HttpEndpoint.ENDPOINT_TYPE);
                HttpEndpoint endp = (HttpEndpoint)sinf.getEndpoint();
                String hostFromEndpoint = endp.getHost();

                if (hostFromEndpoint.endsWith("."))
                {
                    hostFromEndpoint = hostFromEndpoint.substring(0, hostFromEndpoint.length() - 1);                    
                }
                
                if (hostFromEndpoint.equals(config.getTarget()))
                {
                    LOG.info("Endpoint hostname: " + hostFromEndpoint + " matches configuration target: " + config.getTarget() + " attempting to call service");
                    Client client = ClientFact.createClient_v18(endp.isSecure());
                    WebResource target = client.resource(endp.toURL().toURI()).path("push-config");                    
                    LOG.info("Calling remote service at uri: " + target.getURI().toString());
                    
                    ConfigurationRequest cfg = new ConfigurationRequest(config.getTarget(), config.getName(),config.getSerialNumber(), 
                            config.getLastUpdated(), config.getRules());
                    OrchestrationResult res = target.accept(MediaType.APPLICATION_XML_TYPE).post(OrchestrationResult.class, cfg);
                    response = res.getSuccess();
                    LOG.info("Response from service: " + (response?"Ok!":"Not Ok!"));
                } else {
                    LOG.info("No match! hostFromEndpoint: " + hostFromEndpoint);
                }
            }
            
            catch( MalformedURLException e)
            {
                //LOG.severe("Malformed URL from endpoint");
                LOG.error("Malformed URL from endpoint");
            } catch (URISyntaxException e) {
                LOG.error("URI Syntax error from endpoint");
            }            
            catch( Exception e) {
                LOG.error("Exception: " + e.toString());
            }
        }
        return response;
    }
    
    @Override
    public boolean pushConfiguration(OrchestrationConfig config)
    {
        //LOG.log(Level.INFO, "pushConfiguration() called with config: {0}", config);
        LOG.info("pushConfiguration() called with config: " + config);
        
        boolean sec = findAndCallPush(OrchestrationServiceTypes.REST_WS_ORCHESTRATION_PUSH_SECURE, config);
        boolean unsec = findAndCallPush(OrchestrationServiceTypes.REST_WS_ORCHESTRATION_PUSH_UNSECURE, config);
        
        return sec || unsec;
    }
    
    // for testing purposes...
    public static void main(String args[]) 
    {
        ServiceDiscovery sd;
        // check for tsig file
        if (args.length > 0)
        {
            // Tsig file name provided as argument
            sd  = new ServiceDiscoveryDnsSD(args[0]);
        }
        else
        {
            sd = new ServiceDiscoveryDnsSD();
        }
        
        // Start the provider
        OrchestrationPushConsumerREST_WS service = new OrchestrationPushConsumerREST_WS(sd, new ClientFactoryREST_WS());
        OrchestrationConfig cfg = new OrchestrationConfig();
        List<String> rules = new ArrayList<String>();
        rules.add("A rule");
        rules.add("Another rule");
        rules.add("And yet another rule...");
                
        cfg.setName("TestName");
        cfg.setTarget("PT2015102001.bnit.local");
        cfg.setRules( rules);
        cfg.setLastUpdated(new Date());
        service.pushConfiguration(cfg);
        
        try 
        {
            System.in.read();
        } 
        catch (java.io.IOException ex) 
        {
            //Logger.getLogger(OrchestrationPushConsumerREST_WS.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(OrchestrationPushConsumerREST_WS.class.getName()).log(Priority.ERROR, null, ex);
        }
    }
}
