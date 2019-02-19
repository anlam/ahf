package prediktor.apis.arrowhead.service;

import java.util.List;

import prediktor.apis.arrowhead.service.data.ApisItem;
import prediktor.apis.arrowhead.service.data.ApisItemValue;
import se.bnearit.arrowhead.common.service.ServiceEndpoint;

public interface ApisService {
	
	
	public ServiceEndpoint getConfiguredEndpoint();
	
	public List<ApisItem> getAllItems();
	public List<String> getAllItemsName();
	public ApisItemValue getItemByName(String name);
	public boolean setItemValue(String name, ApisItemValue value);
	public boolean setItemsValue(List<ApisItem> items);
	
	

}
