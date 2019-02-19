package prediktor.apis.arrowhead.command;

import java.io.PrintStream;
import java.util.Date;

import prediktor.apis.arrowhead.service.ApisService;
import prediktor.apis.arrowhead.service.data.ApisItemValue;
import se.bnearit.shell.command.AbstractCommand;

public class WriteItemsCommand extends AbstractCommand{
	
	private ApisService apisService;
	
	public WriteItemsCommand(ApisService apisService) {
		super("write", "write item value");
		this.apisService = apisService;
	}

	@Override
	public void execute(PrintStream stream, String[] args) {
		if (args.length > 0 && args.length % 2 == 0) {
			
			for(int i = 0; i < args.length; i = i + 2)
			{
				String name = args[i];
				String value = args[i+1];
				boolean result = apisService.setItemValue(name, new ApisItemValue(value, (short) 192, new Date()));
				stream.println("Writing to:");
				stream.println(name + ": " + (result?"successfully":"fail"));
				
			}

		} else {
			stream.println("Usage: write <name> <value> ... <name> <value>");
		}
		
	}
}
