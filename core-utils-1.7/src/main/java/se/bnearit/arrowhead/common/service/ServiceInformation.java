package se.bnearit.arrowhead.common.service;

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
 * Service instance information
 *
 * @author BnearIT
 */
public class ServiceInformation {

    private ServiceIdentity identity;
    private ServiceEndpoint endpoint;
    private ServiceMetadata metadata;

    /**
     * Creates a new object
     *
     * @param identity identity of service instance
     * @param endpoint Endpoint of service
     * @param metadata Metadata of service
     */
    public ServiceInformation(ServiceIdentity identity, ServiceEndpoint endpoint, ServiceMetadata metadata) {
        this.identity = identity;
        this.endpoint = endpoint;
        this.metadata = metadata;
    }

    public ServiceIdentity getIdentity() {
        return identity;
    }

    public ServiceEndpoint getEndpoint() {
        return endpoint;
    }

    public ServiceMetadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return identity + ", " + endpoint + ", " + metadata;
    }

}
