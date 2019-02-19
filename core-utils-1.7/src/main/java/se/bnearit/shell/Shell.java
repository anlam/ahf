package se.bnearit.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import se.bnearit.shell.command.AbstractCommand;
import se.bnearit.shell.command.Command;

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
 * Provides a shell for exectuing commands.
 * It has two built in commands which are help and quit. It uses System.console or Standard input for input.
 * 
 * @author BnearIT
 */
public class Shell {

	private boolean quit;
	private Map<String,Command> commands;
	private String name;
	private boolean useConsole;
	
        /**
         * 
         * @param name System name
         */
	public Shell(String name) {
		this.name = name;
		quit = false;
		commands = new HashMap<String, Command>();
		
		useConsole = System.console() != null;
		
		// Add built in commands
		registerCommand(new HelpCommand());
		registerCommand(new QuitCommand());
	}
	
        /**
         * Add a command in this shell
         * @param command
         */
	public void registerCommand(Command command) {
		if (!commands.containsKey(command.getName())) {
			commands.put(command.getName(), command);
		}
	}
	
        /**
         * Starts the shell and executes commands until a 'quit' command is issued.
         */
	public void run() {
		System.out.println("Welcome to system " + name);
		while (!quit) {
			System.out.print(name + "> ");
			String cmdLine = "";
			if (useConsole) {
				cmdLine = System.console().readLine();
			} else {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
				try {
					cmdLine = bufferedReader.readLine();
				} catch (IOException e) {}
			}
			
			if (cmdLine != null && !cmdLine.equals("")) {
				String[] cmds = cmdLine.split(" ");
				if (cmds.length > 0) {
					if (commands.containsKey(cmds[0])) {
						commands.get(cmds[0]).execute(System.out, Arrays.copyOfRange(cmds, 1, cmds.length));
					} else {
						System.out.println("Unknown command: " + cmds[0]);
					}
				}
			}
		}
		System.out.println("Bye bye...");
	}
	
	private class HelpCommand extends AbstractCommand {
		public HelpCommand() {
			super("help", "Displays this help");
		}

		@Override
		public void execute(PrintStream stream, String[] args) {
			// TODO: Sort commands
			for (Command cmd : commands.values()) {
				stream.printf(" %-15s : %s%n", cmd.getName(), cmd.getHelp());
			}
		}
	}

	private class QuitCommand extends AbstractCommand {
		public QuitCommand() {
			super("quit", "Quits the application");
		}

		@Override
		public void execute(PrintStream stream, String[] args) {
			quit = true;
		}
	}

}
