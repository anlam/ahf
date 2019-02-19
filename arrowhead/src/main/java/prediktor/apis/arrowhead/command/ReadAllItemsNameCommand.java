package prediktor.apis.arrowhead.command;

import java.io.PrintStream;
import java.util.List;

import prediktor.apis.arrowhead.service.ApisService;
import prediktor.apis.arrowhead.service.data.ApisItem;
import se.bnearit.shell.command.AbstractCommand;

public class ReadAllItemsNameCommand extends AbstractCommand {
private ApisService apisService;
	
	public ReadAllItemsNameCommand(ApisService apisService) {
		super("readn", "Read name of all items from the module");
		this.apisService = apisService;
	}

	@Override
	public void execute(PrintStream stream, String[] args) {
		if (args.length == 0) {
			
			List<String> itemList = apisService.getAllItemsName();
			if(itemList != null)
				for(String item : itemList)
					stream.println(item);

		} else {
			stream.println("Usage: readn");
		}
		
	}
}
