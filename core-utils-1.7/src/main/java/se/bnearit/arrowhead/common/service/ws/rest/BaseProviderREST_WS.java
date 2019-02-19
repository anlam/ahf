package se.bnearit.arrowhead.common.service.ws.rest;

import java.util.logging.Logger;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import se.bnearit.arrowhead.common.core.service.discovery.ServiceDiscovery;
import se.bnearit.arrowhead.common.core.service.discovery.endpoint.HttpEndpoint;
import se.bnearit.arrowhead.common.core.service.discovery.exception.MetadataException;
import se.bnearit.arrowhead.common.core.service.discovery.exception.ServiceRegisterException;
import se.bnearit.arrowhead.common.service.ServiceEndpoint;
import se.bnearit.arrowhead.common.service.ServiceIdentity;
import se.bnearit.arrowhead.common.service.ServiceInformation;
import se.bnearit.arrowhead.common.service.ServiceMetadata;
import se.bnearit.arrowhead.common.service.ServiceProducer;
import se.bnearit.arrowhead.common.service.exception.ServiceNotStartedException;

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
 * A base for a rest provider
 *
 * @author BnearIT
 */
public class BaseProviderREST_WS implements ServiceProducer {

    /**
     * Name of the provider
     */
    protected final String name;
    /**
     * Service type of this provider
     */
    protected final String serviceType;
    /**
     * Uses https or not
     */
    protected final boolean secure;
    private final String keyStoreFile;
    private final String keyStorePassword;
    private final String trustStoreFile;
    private final String trustStorePassword;
    private final String serverCertAlias;
    /**
     * Service id of this service
     */
    protected ServiceIdentity serviceId;
    private Server httpServer;
    private boolean published;
    private Logger LOG;

    /**
     * The endpoint of this service
     */
    protected HttpEndpoint endpoint;
    /**
     * Object to be registered as a resource in the web countainer See the
     * demonstration application for usage
     */
    protected Object resource;
    /**
     * Service discovery used by this provider.
     */
    protected ServiceDiscovery serviceDiscovery;

    /**
     * This flag will switch verification of settings upon start of the
     * provider. Default value is true
     */
    protected boolean verifySettings = true;

    /**
     * Sets up the provider. If https not is used the keystore settings can be
     * null.
     *
     * @param name Name of the provider
     * @param serviceType Type of the service this provider offers
     * @param secure true for https or false for http
     * @param keyStoreFile keystore filename for server certificate and trusted
     * certificates when using https
     * @param keyStorePassword password to keystore file
     * @param keyStoreAlias alias of server certificate to use when using https
     * @param LOG Logger to be used
     * @param serviceDiscovery Service discovery to use when registering this
     * provider.
     */
    public BaseProviderREST_WS(
            String name,
            String serviceType,
            boolean secure,
            String keyStoreFile,
            String keyStorePassword,
            String keyStoreAlias,
            Logger LOG,
            ServiceDiscovery serviceDiscovery) {
        this(name, serviceType, secure, keyStoreFile, keyStorePassword, keyStoreAlias, keyStoreFile, keyStorePassword, LOG, serviceDiscovery);

    }

    /**
     *
     * @param name Name of the provider
     * @param serviceType Type of the service this provider offers
     * @param secure true for https or false for http
     * @param keyStoreFile keystore filename for server certificate when secure
     * @param keyStorePassword password to keystore file
     * @param keyStoreAlias alias of server certificate to use when using https
     * @param trustStoreFile keystore containing all trusted certificates.
     * @param trustStorePassword password to truststore
     * @param LOG Logger to be used
     * @param serviceDiscovery Service discovery to use when registering this
     * provider.
     */
    public BaseProviderREST_WS(
            String name,
            String serviceType,
            boolean secure,
            String keyStoreFile,
            String keyStorePassword,
            String keyStoreAlias,
            String trustStoreFile,
            String trustStorePassword,
            Logger LOG,
            ServiceDiscovery serviceDiscovery) {

        this.name = name;
        this.serviceType = serviceType;
        this.secure = secure;
        this.keyStoreFile = keyStoreFile;
        this.keyStorePassword = keyStorePassword;
        this.LOG = LOG;
        this.serviceDiscovery = serviceDiscovery;
        this.trustStoreFile = trustStoreFile;
        this.trustStorePassword = trustStorePassword;
        published = false;
        serverCertAlias = keyStoreAlias;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getServiceType() {
        return serviceType;
    }

    @Override
    public ServiceEndpoint getEndpoint() throws ServiceNotStartedException {
        return endpoint;
    }

    @Override
    public void publish() throws ServiceRegisterException {
        if (!published) {
            String serviceName = serviceDiscovery.createServiceName(name, serviceType);
            serviceId = new ServiceIdentity(serviceName, serviceType);
            ServiceMetadata meta = new ServiceMetadata();
            meta.put("version", "1.0");
            ServiceInformation info = new ServiceInformation(serviceId, endpoint, meta);
            try {
                serviceDiscovery.publish(info);
                published = true;
                LOG.info("Published service: " + info);
            } catch (MetadataException e) {
                LOG.severe("Failed to publish service: " + info + ". Reason: " + e.getMessage());
            } catch (ServiceRegisterException e) {
                LOG.severe("Failed to publish service: " + info + ". Reason: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean isPublished() {
        return published;
    }

    @Override
    public void unpublish() {
        if (published) {
            try {
                serviceDiscovery.unpublish(serviceId);
                published = false;
                LOG.info("Unpublished service: " + serviceId);
            } catch (ServiceRegisterException e) {
                LOG.severe("Failed to unpublish service: " + serviceId + ". Reason: " + e.getMessage());
            }
        }
    }
    
    public void unpublish(boolean force) {
        if (force) {
        	published = true;
        }
        unpublish();
    }

    @Override
    public void start() {
        if (verifySettings) {
            verifySettings();
        }

        if (secure) {
            httpServer = new Server();
            HttpConfiguration https = new HttpConfiguration();
            https.addCustomizer(new SecureRequestCustomizer());
            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setKeyStorePath(keyStoreFile);
            sslContextFactory.setKeyStorePassword(keyStorePassword);
            sslContextFactory.setKeyManagerPassword(keyStorePassword);

            if (serverCertAlias != null) {
                sslContextFactory.setCertAlias(serverCertAlias);
            }

            sslContextFactory.setEnableCRLDP(false);

            // TODO: Is it mandatory to use client certificates when running https? 
            sslContextFactory.setNeedClientAuth(true);

            sslContextFactory.setTrustStorePath(trustStoreFile);
            sslContextFactory.setTrustStorePassword(trustStorePassword);

            ServerConnector sslConnector = new ServerConnector(
                    httpServer,
                    new SslConnectionFactory(sslContextFactory, "http/1.1"),
                    new HttpConnectionFactory(https));
            sslConnector.setPort(endpoint.getPort());
            httpServer.setConnectors(new Connector[]{sslConnector});
        } else {
            httpServer = new Server(endpoint.getPort());
        }

        ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.setContextPath("/");
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(resource);
        ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));

        // The path provided must end with a / or the registration for a service will fail.
        servletContext.addServlet(servletHolder, getPathWithSlashEnding(endpoint.getPath()) + "*");
        httpServer.setHandler(servletContext);

        try {
            httpServer.start();
            LOG.info("Started WS-REST service producer with name:" + name + " endpoint:" + endpoint + " secure: " + secure);
        } catch (Exception e) {
            LOG.severe("Failed to start HTTP server: " + e.getMessage());
            httpServer = null;
        }
    }

    private void verifySettings() throws RuntimeException {
        // Do some verification of API user input
        if (endpoint == null) {
            throw new RuntimeException("Variable endpoint is null");
        }
        if (!endpoint.getPath().endsWith("/")) {
            LOG.severe("Endpoint '" + endpoint.getPath() + "' does not end with '/'. Service registration may mismatch the actual listening path as this '/*' will be added.");
        }
        if (resource == null) {
            throw new RuntimeException("Variable resource is null");
        }
        if (secure && (keyStoreFile == null || keyStorePassword == null)) {
            throw new RuntimeException("Some of the keystore setting variables are null for a secure provider");
        }
    }

    @Override
    public boolean isRunning() {
        return httpServer != null;
    }

    @Override
    public void stop() {
        try {
            httpServer.stop();
            httpServer.join();
        } catch (Exception e) {
            LOG.warning("Failed to stop HTTP server: " + e.getMessage());
        }

        httpServer = null;
    }

    private static String getPathWithSlashEnding(String path) {
        String result = path;
        if (!path.endsWith("/")) {
            result = result + "/";
        }
        return result;
    }

}
