package se.bnearit.arrowhead.common.core.service.authorisation;

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
 * Provides an interface for using the authorisation service.
 *
 * @author BnearIT
 */
public interface AuthorisationControl {

    /**
     * Performs an authorisation request for an authorisation system
     *
     * @param dn Identification of the system that is requesting authorisation
     * @param serviceType the service type to get accessed
     * @param serviceName the service name to be accessed.
     * @return true if authorisation service allows consumption
     */
    public boolean isAuthorized(String dn, String serviceType, String serviceName);

}
