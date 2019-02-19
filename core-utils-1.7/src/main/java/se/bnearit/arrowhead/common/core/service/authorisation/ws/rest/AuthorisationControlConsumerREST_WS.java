package se.bnearit.arrowhead.common.core.service.authorisation.ws.rest;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import se.bnearit.arrowhead.common.core.service.authorisation.AuthorisationControl;
import se.bnearit.arrowhead.common.core.service.authorisation.ws.rest.msg.AuthorisationRequest;
import se.bnearit.arrowhead.common.core.service.authorisation.ws.rest.msg.AuthorisationResponse;
import se.bnearit.arrowhead.common.core.service.discovery.endpoint.HttpEndpoint;
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
 *
 * Implementation of rest based authorisation control.
 *
 * @author BnearIT
 */
public class AuthorisationControlConsumerREST_WS implements AuthorisationControl {

    private static final Logger LOG = Logger.getLogger(AuthorisationControlConsumerREST_WS.class.getName());

    private HttpEndpoint endpoint;

    private ClientFactoryREST_WS clientFactory;

    /**
     * Constructor of authorisation service consumer
     *
     * @param endpoint Endpoint of authorisation service instance
     * @param clientFactory client factory
     */
    public AuthorisationControlConsumerREST_WS(HttpEndpoint endpoint, ClientFactoryREST_WS clientFactory) {
        this.endpoint = endpoint;
        this.clientFactory = clientFactory;
    }

    @Override
    public boolean isAuthorized(String dn, String serviceType, String serviceName) {
        boolean result = false;

        try {
            LOG.info("Sending authorisation request dn:" + dn + " serviceType:" + serviceType + " serviceName:" + serviceName);
            Client client = clientFactory.createClient(endpoint.isSecure());
            WebTarget target = client.target(endpoint.toURL().toURI());
            AuthorisationResponse authResponse = target
                    .path("authorisation")
                    .request(MediaType.APPLICATION_XML_TYPE)
                    .put(Entity.xml(new AuthorisationRequest(dn, serviceType, serviceName)), AuthorisationResponse.class);
            LOG.info(String.format("Got response from %s: %s%n", endpoint, authResponse));
            result = authResponse.isAuthorised();
            client.close();
        } catch (URISyntaxException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (MalformedURLException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (ProcessingException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        } catch (WebApplicationException e) {
            LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
        }

        return result;
    }

}
