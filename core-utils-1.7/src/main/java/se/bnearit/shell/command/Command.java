package se.bnearit.shell.command;

import java.io.PrintStream;

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
 * Interface for a Command
 *
 * @see se.bnearit.shell.Shell
 * @author BnearIT
 */
public interface Command {
    
    /**
     * 
     * @return the name of the command 
     */
    public String getName();

    /**
     * 
     * @return the help string for this command 
     */
    public String getHelp();

    /**
     * Executes this command using given resources.
     * @param stream The stream to use for interaction
     * @param args the arguments that follows the command
     */
    public void execute(PrintStream stream, String[] args);

}
