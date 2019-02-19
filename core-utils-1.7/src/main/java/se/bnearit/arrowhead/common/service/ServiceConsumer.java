package se.bnearit.arrowhead.common.service;

import se.bnearit.arrowhead.common.service.exception.ConsumerNotAuthorisedException;

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

public interface ServiceConsumer /*extends Resource*/ {
	
	public String getName();
	
	public String getServiceType();
	
	public ServiceEndpoint getConfiguredEndpoint();
	
	public boolean isConsuming();
	
	public void start() throws ConsumerNotAuthorisedException;
	
	public void stop();

}
