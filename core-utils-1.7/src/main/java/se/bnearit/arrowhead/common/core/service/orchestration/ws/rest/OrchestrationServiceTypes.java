package se.bnearit.arrowhead.common.core.service.orchestration.ws.rest;

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
 * Provides the service types used in orchestration services
 *
 * @author BnearIT
 */
public final class OrchestrationServiceTypes {
    /**
     * Uses http
     */
    public static final String REST_WS_ORCHESTRATION_PUSH_UNSECURE = "_orch-p-ws-http._tcp";
    /**
     * Uses https
     */
    public static final String REST_WS_ORCHESTRATION_PUSH_SECURE = "_orch-p-ws-https._tcp";

    
    /**
     * Uses http
     */
    public static final String REST_WS_ORCHESTRATION_STORE_UNSECURE = "_orch-s-ws-http._tcp";
    /**
     * Uses https
     */
    public static final String REST_WS_ORCHESTRATION_STORE_SECURE = "_orch-s-ws-https._tcp";

    /**
     * Uses http
     */
    public static final String REST_WS_ORCHESTRATION_MGMT_UNSECURE = "_orch-m-ws-http._tcp";
    /**
     * Uses https
     */
    public static final String REST_WS_ORCHESTRATION_MGMT_SECURE = "_orch-m-ws-https._tcp";

}
