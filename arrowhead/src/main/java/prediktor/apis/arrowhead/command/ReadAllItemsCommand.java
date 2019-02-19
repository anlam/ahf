package prediktor.apis.arrowhead.command;

import java.io.PrintStream;
import java.util.List;

import prediktor.apis.arrowhead.service.ApisService;
import prediktor.apis.arrowhead.service.data.ApisItem;
import se.bnearit.shell.command.AbstractCommand;

public class ReadAllItemsCommand extends AbstractCommand{

	private ApisService apisService;
	
	public ReadAllItemsCommand(ApisService apisService) {
		super("reada", "Read all items from the module");
		this.apisService = apisService;
	}

	@Override
	public void execute(PrintStream stream, String[] args) {
		if (args.length == 0) {
			
			List<ApisItem> itemList = apisService.getAllItems();

			if(itemList != null)
				for(ApisItem item : itemList)
					stream.println(item);

		} else {
			stream.println("Usage: reada");
		}
		
	}

}
