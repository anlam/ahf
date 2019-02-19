package se.bnearit.arrowhead.common.core.service.orchestration.ws.rest;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import se.bnearit.arrowhead.common.core.service.orchestration.data.OrchestrationConfig;
import se.bnearit.arrowhead.common.core.service.orchestration.ws.rest.msg.ConfigurationRequest;

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

@Path("/")
@Produces(MediaType.APPLICATION_XML)
public class OrchestrationPushProviderExampleREST_WS 
{
    private static final Logger log = Logger.getLogger(OrchestrationPushProviderExampleREST_WS.class.getName());
    
    @Path("/push-config")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public JAXBElement<Boolean> pushConfiguration(ConfigurationRequest configArg, @Context HttpServletRequest request)
    {
        OrchestrationConfig config = new OrchestrationConfig(configArg.getTarget(), configArg.getName(), configArg.getSerialNumber(),configArg.getLastUpdated(),configArg.getRules());
        OrchestrationPushProviderExampleREST_WS.log.log(Level.INFO, "pushConfig() - received config with {0}", config);
        
        return new JAXBElement<Boolean>(new QName("boolean"), Boolean.class, Boolean.TRUE);
    }
}
