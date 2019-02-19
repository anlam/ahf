package se.bnearit.resource;

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
 * Helper to create unique id:s in increasing order starting from 1
 *
 * @author BnearIT
 */
public class ResourceAllocator {

    private static ResourceAllocator instance;

    public static ResourceAllocator getInstance() {
        synchronized (ResourceAllocator.class) {
            if (instance == null) {
                instance = new ResourceAllocator();
            }
        }
        return instance;
    }

    private int nextId = 1;

    /**
     *
     * @return the next id in order.
     */
    public synchronized int allocateResourceId() {
        return nextId++;
    }

}
