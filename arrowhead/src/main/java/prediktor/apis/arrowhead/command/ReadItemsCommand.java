package prediktor.apis.arrowhead.command;

import java.io.PrintStream;
import java.util.List;

import prediktor.apis.arrowhead.service.ApisService;
import prediktor.apis.arrowhead.service.data.ApisItem;
import prediktor.apis.arrowhead.service.data.ApisItemValue;
import se.bnearit.shell.command.AbstractCommand;

public class ReadItemsCommand extends AbstractCommand{

private ApisService apisService;
	
	public ReadItemsCommand(ApisService apisService) {
		super("read", "Read item value");
		this.apisService = apisService;
	}

	@Override
	public void execute(PrintStream stream, String[] args) {
		if (args.length >= 1) {
			
			for(String name : args)
			{
				ApisItemValue itemValue = apisService.getItemByName(name);
				if(itemValue != null)
					stream.println(name + " = " + itemValue);
			}

		} else {
			stream.println("Usage: read <name> ... <name>");
		}
		
	}

}
