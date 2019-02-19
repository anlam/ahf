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
public class AuthorisationResponse {

	private AuthorisationRequest request;
	private boolean authorised;

	public AuthorisationResponse() {
	}
	
        /**
         * 
         * @param request The request that this response corresponds to 
         * @param authorised true if the request  
         */
	public AuthorisationResponse(AuthorisationRequest request, boolean authorised) {
		this.request = request;
		this.authorised = authorised;
	}

	public AuthorisationRequest getRequest() {
		return request;
	}
        
	public boolean isAuthorised() {
		return authorised;
	}

	public void setRequest(AuthorisationRequest request) {
		this.request = request;
	}

	public void setAuthorised(boolean authorised) {
		this.authorised = authorised;
	}

	@Override
	public String toString() {
		return "AuthorisationResponse [request=" + request + ", authorised="
				+ authorised + "]";
	}
	
}
