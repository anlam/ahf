package se.bnearit.arrowhead.common.core.service.orchestration;

import java.util.List;

import se.bnearit.arrowhead.common.core.service.orchestration.data.OrchestrationConfig;

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
 * Interface for using an orchestration service.
 *
 * @author BnearIT
 */
public interface OrchestrationManagement {

    public abstract List<String> getConfigTargets();

    public abstract List<String> getConfigurationsForTarget(String target);
    
    public abstract List<String> getSupportedConsumptionServiceTypesForTarget(String target);
    
    public abstract Integer getAvailableConsumptionsForServiceType(String target, String serviceType);

    public abstract OrchestrationConfig getConfiguration(String target, String configName);

    public abstract OrchestrationConfig createConfiguration(String target, String configName, List<String> rules);

    public abstract OrchestrationConfig updateConfiguration(String target, String configName, List<String> rules);

    public abstract boolean deleteConfiguration(String target, String configName);

    public abstract String getActiveConfiguration(String target);

    public abstract boolean setActiveConfig(String target, String configName);

}
