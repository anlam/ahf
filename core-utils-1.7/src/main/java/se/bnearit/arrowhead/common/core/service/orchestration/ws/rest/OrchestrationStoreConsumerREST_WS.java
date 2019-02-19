package se.bnearit.arrowhead.common.core.service.orchestration.ws.rest;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import se.bnearit.arrowhead.common.core.service.discovery.endpoint.HttpEndpoint;
import se.bnearit.arrowhead.common.core.service.orchestration.OrchestrationStore;
import se.bnearit.arrowhead.common.core.service.orchestration.data.OrchestrationConfig;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.GetActiveConfigurationResponse;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.OrchestrationConfigResponse;
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

/**
 * Rest consumer for orchestration store. 
 * This class will print any errors in the java.util.logging.Logger at warning level.
 * @author Thorsten
 */
public class OrchestrationStoreConsumerREST_WS implements OrchestrationStore {

    private static final Logger LOG = Logger.getLogger(OrchestrationStoreConsumerREST_WS.class.getName());

    private HttpEndpoint endpoint;

    private ClientFactoryREST_WS clientFactory;

    /**
     * Sets up an orchestration store consumer
     *
     * @param endpoint Endpoint to orchestration provider service instance
     * @param clientFactory Factory to create a client
     */
    public OrchestrationStoreConsumerREST_WS(HttpEndpoint endpoint, ClientFactoryREST_WS clientFactory) {
        this.endpoint = endpoint;
        this.clientFactory = clientFactory;
    }

    @Override
    public OrchestrationConfig getActiveConfiguration() {
        OrchestrationConfig result = null;

        try {
            Client client = clientFactory.createClient(endpoint.isSecure());
            WebTarget target = client.target(endpoint.toURL().toURI()).path("orchestration").path("active-config");
            GetActiveConfigurationResponse response = target.request(MediaType.APPLICATION_XML_TYPE).get(GetActiveConfigurationResponse.class);
            String activeConfig = response.getConfig().getName();
            if (activeConfig != null) {
                LOG.info("Got active config: " + activeConfig);
                result = getConfiguration(client, activeConfig);
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

        return result;
    }

    private OrchestrationConfig getConfiguration(Client client, String configName) {
        OrchestrationConfig result = null;

        try {
            WebTarget target = client.target(endpoint.toURL().toURI()).path("orchestration").path("configurations").path(configName);
            OrchestrationConfigResponse response = target.request(MediaType.APPLICATION_XML_TYPE).get(OrchestrationConfigResponse.class);
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
        } catch (ProcessingException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (WebApplicationException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }

        return result;
    }
}
