package se.bnearit.arrowhead.common.core.service.discovery.endpoint;

import se.bnearit.arrowhead.common.service.ServiceEndpoint;

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

public class UdpEndpoint implements ServiceEndpoint {

    public static final String ENDPOINT_TYPE = "UdpEndpoint";

    private String host;
    private int port;

    /**
     *
     * @param host hostname or fully qualified domain name
     * @param port
     */
    public UdpEndpoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String getType() {
        return ENDPOINT_TYPE;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.host != null ? this.host.hashCode() : 0);
        hash = 47 * hash + this.port;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UdpEndpoint other = (UdpEndpoint) obj;
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!host.equals(other.host)) {
            return false;
        }
        if (port != other.port) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UdpEndpoint [host=" + host + ", port=" + port + "]";
    }

}
