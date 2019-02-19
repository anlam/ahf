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
 * Helper class for a command providing the basic functionality
 *
 * @author BnearIT
 */
public abstract class AbstractCommand implements Command {

    private String name;
    private String help;

    /**
     * 
     * @param name Name of the command.
     * @param help help text of the command
     */
    public AbstractCommand(String name, String help) {
        this.name = name;
        this.help = help;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHelp() {
        return help;
    }

    @Override
    public abstract void execute(PrintStream stream, String[] args);

}
