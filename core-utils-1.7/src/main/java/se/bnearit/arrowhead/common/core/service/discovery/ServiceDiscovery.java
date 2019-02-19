package se.bnearit.arrowhead.common.core.service.discovery;

import java.util.List;

import se.bnearit.arrowhead.common.core.service.discovery.exception.MetadataException;
import se.bnearit.arrowhead.common.core.service.discovery.exception.ServiceRegisterException;
import se.bnearit.arrowhead.common.service.ServiceIdentity;
import se.bnearit.arrowhead.common.service.ServiceInformation;

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
 * Interface for accessing a service registry.
 * 
 * @author BnearIT
 */
public interface ServiceDiscovery {

    /**
     * Gets all kinds of endpoints that the implementing service discovery mechanism supports 
     * @return endpoint types 
     */
    public abstract List<String> getSupportedEndpointTypes();

    /**
     * Gets all service instances found by this service discovery mechanism
     * 
     * @return all services found and supported by this discovery
     */
    public abstract List<ServiceIdentity> getAllServices();

    /**
     * Gets all service instances found by this service discovery mechanism.
     * 
     * @param type Specific service type. 
     * @return all instances supported by this discovery and of the given type.
     */
    public abstract List<ServiceIdentity> getServicesByType(String type);

    /**
     * Get details of a specific service instance and provides an endpoint from given type.
     * @param identity an identity that exist in the service registry
     * @param endpointType one of the supported endpoint types
     * @return ServiceInformation from the identity with an endpoint of the given type.  
     */
    public abstract ServiceInformation getServiceInformation(
            ServiceIdentity identity, String endpointType);

    /**
     * Makes a full service name which can be used in the implementing service registry.
     * @param name name of the service. 
     * @param serviceType a service type
     * @return a service name which is specific for the implementing service discovery mechanism.
     * @throws ServiceRegisterException 
     */
    public abstract String createServiceName(String name, String serviceType)
            throws ServiceRegisterException;

    /**
     * Checks if the given service instance already is published in the service registry
     * @param information service information to be checked against the service registry
     * @return true if the service is published false otherwise
     */
    public abstract boolean isPublished(ServiceInformation information);

    /**
     * Add a service instance in the service registry.
     * @param information
     * @throws MetadataException on malformed metadata 
     * @throws ServiceRegisterException on malformed service or if any specific service registry error occurs.
     */
    public abstract void publish(ServiceInformation information)
            throws MetadataException, ServiceRegisterException;

    /**
     * Remove a service registration from the service registry
     * @param identity an entity corresponds to an identity in the service registry
     * @throws ServiceRegisterException on any error providing description of the error
     */
    public abstract void unpublish(ServiceIdentity identity)
            throws ServiceRegisterException;

    /**
     * Gets this hosts name as the implementing service discovery sees it.
     * @return a the host name
     */
    public abstract String getHostName();

}
