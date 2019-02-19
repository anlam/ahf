package se.bnearit.arrowhead.common.core.service.orchestration.ws.rest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import se.bnearit.arrowhead.common.core.service.discovery.ServiceDiscovery;
import se.bnearit.arrowhead.common.core.service.discovery.dnssd.ServiceDiscoveryDnsSD;
import se.bnearit.arrowhead.common.core.service.discovery.endpoint.HttpEndpoint;
import se.bnearit.arrowhead.common.core.service.discovery.exception.ServiceRegisterException;
import se.bnearit.arrowhead.common.service.ws.rest.BaseProviderREST_WS;

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

public class OrchestrationPushProviderExample extends BaseProviderREST_WS 
{
    private static final Logger log = Logger.getLogger(OrchestrationPushProviderExample.class.getName());
    
    public OrchestrationPushProviderExample(ServiceDiscovery sd)
    {
        super("orchestration-push", OrchestrationServiceTypes.REST_WS_ORCHESTRATION_PUSH_UNSECURE, false, null, null, null, log, sd);
        resource = new OrchestrationPushProviderExampleREST_WS();        
        endpoint = new HttpEndpoint(serviceDiscovery.getHostName(), 8182, "/orchestration-push/");
    }
    
    public static void main(String[] argv)
    {
        ServiceDiscovery sd;
        // check for tsig file
        if (argv.length > 0)
        {
            // Tsig file name provided as argument
            sd  = new ServiceDiscoveryDnsSD(argv[0]);
        }
        else
        {
            sd = new ServiceDiscoveryDnsSD();
        }
        OrchestrationPushProviderExample ex = new OrchestrationPushProviderExample( sd);
        
        ex.start();
        if (!ex.isRunning()) 
        {
            log.severe("Failed to start service. See log for details");
            return;
        }
        // publish it
        try 
        {
            ex.publish();
        } 
        catch (ServiceRegisterException exc) 
        {
            log.log(Level.SEVERE, "Failed to publish provider", exc);
        }
        
        if (ex.isPublished()) 
        {
            log.info("Service is running and is published. Press enter to stop.");
        }

        // Wait until enter
        try 
        {
            System.in.read();
        } catch (IOException exc) 
        {
            log.log(Level.SEVERE, null, exc);
        }
        
        // unpublish and stop.
        if (ex.isPublished()) 
        {
            ex.unpublish();
        }
        ex.stop();
    }
}
