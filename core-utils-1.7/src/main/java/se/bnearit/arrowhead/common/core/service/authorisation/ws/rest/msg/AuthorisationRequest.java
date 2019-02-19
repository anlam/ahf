package se.bnearit.arrowhead.common.core.service.authorisation.ws.rest.msg;

import javax.xml.bind.annotation.XmlRootElement;

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

@XmlRootElement
public class AuthorisationRequest {

    private String distinguishedName;
    private String serviceType;
    private String serviceName;

    /**
     * Creates an empty object
     */
    public AuthorisationRequest() {
    }

    /**
     * 
     * 
     * @param distinguishedName Identification of the system that is requesting authorisation
     * @param serviceType the service type to get accessed
     * @param serviceName the service name to be accessed
     */
    public AuthorisationRequest(String distinguishedName, String serviceType, String serviceName) {
        this.distinguishedName = distinguishedName;
        this.serviceType = serviceType;
        this.serviceName = serviceName;
    }

    /**
     * Gets the DN of this request
     * @return DN
     */
    public String getDistinguishedName() {
        return distinguishedName;
    }

    /**
     * 
     * @return service type 
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * 
     * @return service name 
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * 
     * @param distinguishedName DN 
     */
    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    /**
     * 
     * @param serviceType 
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * 
     * @param serviceName 
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "AuthorisationRequest [distinguishedName=" + distinguishedName
                + ", serviceType=" + serviceType + ", serviceName="
                + serviceName + "]";
    }

}
