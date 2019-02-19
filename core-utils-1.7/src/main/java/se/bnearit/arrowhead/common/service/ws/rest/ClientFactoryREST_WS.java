package se.bnearit.arrowhead.common.service.ws.rest;

import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.SslConfigurator;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import javax.net.ssl.SSLPeerUnverifiedException;

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
 * Client factory for creating some different REST/WS clients.
 * 
 *
 * @author BnearIT
 */
public class ClientFactoryREST_WS {

    private static final Logger LOG = Logger.getLogger(ClientFactoryREST_WS.class.getName());

    private SSLContext sslContext;
    private boolean acceptMismatchingServerNames = false;

    /**
     * Creates a clientfactory with support for TLS.
     *
     * @param trustStoreFile path to truststore containing all trusted
     * certificates
     * @param trustStorePassword password to truststore
     * @param keyStoreFile path to keystore containing client certificate and corresponding keypair
     * @param keyStorePassword password to keystore
     */
    public ClientFactoryREST_WS(String trustStoreFile, String trustStorePassword, String keyStoreFile, String keyStorePassword) {
        SslConfigurator sslConfig = SslConfigurator.newInstance()
                .trustStoreFile(trustStoreFile)
                .trustStorePassword(trustStorePassword)
                .keyStoreFile(keyStoreFile)
                .keyPassword(keyStorePassword);

        sslContext = sslConfig.createSSLContext();
    }

    /**
     * Created a default client factory.
     * If https connections are done from a ClientFactory using this constructor the default java keystores will be used.
     */
    public ClientFactoryREST_WS() {
        sslContext = SslConfigurator.getDefaultContext();
    }

    private Client createSecureClient() {
        Client client = ClientBuilder.newBuilder().hostnameVerifier(new HostnameVerifyerImpl()).sslContext(sslContext).build();
        return client;
    }

    private Client createUnsecureClient() {
        return ClientBuilder.newClient();
    }

    /**
     *
     * @param secureClient true on using https 
     * @return a webclient to access rest based services
     */
    public Client createClient(boolean secureClient) {
        Client client = null;
        if (secureClient) {
            client = createSecureClient();
        } else {
            client = createUnsecureClient();
        }

        return client;
    }

    private com.sun.jersey.api.client.Client createSecureClient_v18() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                new HTTPSProperties(new HostnameVerifyerImpl(), sslContext));

        return com.sun.jersey.api.client.Client.create(clientConfig);
    }

    private com.sun.jersey.api.client.Client createUnsecureClient_v18() {
        return com.sun.jersey.api.client.Client.create();
    }

    /**
     * 
     * @param secureClient true on using https
     * @return a webclient to access rest based services
     */
    public com.sun.jersey.api.client.Client createClient_v18(boolean secureClient) {
        com.sun.jersey.api.client.Client client = null;
        if (secureClient) {
            client = createSecureClient_v18();
        } else {
            client = createUnsecureClient_v18();
        }

        return client;
    }

    /**
     * Set parameter wheiter this client shall accept mismatched server names during https handshake
     * The default value is false.
     * @param accept true if mismatched server names shall be accepted
     */
    public void setAcceptMismatchedHostnames(boolean accept){
        acceptMismatchingServerNames = accept;
    }
    
    private class HostnameVerifyerImpl implements HostnameVerifier {

        
        
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // This function is called when a hostname mismatches during the ssl handshake.
            // The remote certificate is still trusted (by truststore)
            
            if (acceptMismatchingServerNames){
                try {
                    LOG.info("Accepting mismatching hostname: " + hostname + " to server certificate subject: "+session.getPeerPrincipal().getName());
                } catch (SSLPeerUnverifiedException ex) {
                    LOG.info("Accepting mismatching hostname: " + hostname);
                }               
            }else{
                try {
                    LOG.severe("Failed to establish https connection to " + hostname + " due to mismatched hostname to server certificate subject: "+session.getPeerPrincipal().getName());
                } catch (SSLPeerUnverifiedException ex) {
                    LOG.severe("Failed to establish https connection to " + hostname + " due to mismatched hostname.");
                }
            }
            
            return acceptMismatchingServerNames;
        }
    }

}
