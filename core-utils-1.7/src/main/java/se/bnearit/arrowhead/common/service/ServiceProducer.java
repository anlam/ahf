package se.bnearit.arrowhead.common.service;

import se.bnearit.arrowhead.common.service.exception.ServiceNotStartedException;
import se.bnearit.arrowhead.common.core.service.discovery.exception.ServiceRegisterException;

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

public interface ServiceProducer {

    /**
     * 
     * @return the name of this service provider 
     */
    public String getName();

    /**
     * 
     * @return service type as seen by the current used service registry 
     */
    public String getServiceType();

    /**
     * 
     * @return the service endpoint where the provider can be accessed
     * @throws ServiceNotStartedException 
     */
    public ServiceEndpoint getEndpoint() throws ServiceNotStartedException;

    /**
     * Publishes the provider in current service registry
     * @throws ServiceRegisterException 
     */
    public void publish() throws ServiceRegisterException;
    
    /**
     * 
     * @return if this provider is published in the service registry 
     */
    public boolean isPublished();

    /**
     * Removes this provider from current service registry
     */
    public void unpublish();

    public void start();

    public boolean isRunning();

    public void stop();

//	public int getNumberOfConsumers();
//	
//	public List<String> getConsumers();
//	
//	public void kickConsumer(String consumer);
}
