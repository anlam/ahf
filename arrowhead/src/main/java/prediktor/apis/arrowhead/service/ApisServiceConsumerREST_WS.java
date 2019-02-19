package prediktor.apis.arrowhead.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import prediktor.apis.arrowhead.service.data.ApisItem;
import prediktor.apis.arrowhead.service.data.ApisItemValue;
import se.bnearit.arrowhead.common.core.service.discovery.endpoint.HttpEndpoint;
import se.bnearit.arrowhead.common.service.ServiceEndpoint;
import se.bnearit.arrowhead.common.service.ws.rest.ClientFactoryREST_WS;

public class ApisServiceConsumerREST_WS implements ApisService {

	private static final Logger LOG = Logger.getLogger(ApisServiceConsumerREST_WS.class.getName());

	protected HttpEndpoint endpoint;
	private ClientFactoryREST_WS clientFactory;

	public ApisServiceConsumerREST_WS(HttpEndpoint endpoint, ClientFactoryREST_WS clientFactoryREST_WS) {

		this.endpoint = endpoint;
		this.clientFactory = clientFactoryREST_WS;

	}

	public ServiceEndpoint getConfiguredEndpoint() {

		return endpoint;
	}

	public List<ApisItem> getAllItems() {
		List<ApisItem> result = null;

		try {
			Client client = clientFactory.createClient_v18(endpoint.isSecure());
			WebResource webResource = client.resource(endpoint.toURL().toURI()).path("items");

			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);

			if (response.getStatus() == 200) {
				Gson gson = new Gson();
				ApisItem[] ret = gson.fromJson( response.getEntity(String.class), ApisItem[].class);
				result = Arrays.asList(ret);
			}

		} catch (Exception e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		}

		return result;
	}

	public List<String> getAllItemsName() {
		List<String> result = null;

		try {
			Client client = clientFactory.createClient_v18(endpoint.isSecure());
			WebResource webResource = client.resource(endpoint.toURL().toURI()).path("items").path("ids");

			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);

			if (response.getStatus() == 200) {
				Gson gson = new Gson();
				String[] ret = gson.fromJson( response.getEntity(String.class), String[].class);
				result = Arrays.asList(ret);
			}

		} catch (Exception e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		}

		return result;
	}

	public ApisItemValue getItemByName(String name) {
		ApisItemValue result = null;
		try {
			Client client = clientFactory.createClient_v18(endpoint.isSecure());
			WebResource webResource = client.resource(endpoint.toURL().toURI()).path("items").path(name);

			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);

			if (response.getStatus() == 200) {
				Gson gson = new Gson();
				result = gson.fromJson(response.getEntity(String.class), ApisItemValue.class);
			}

		} catch (Exception e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		}
		
		return result;
	}

	public boolean setItemValue(String name, ApisItemValue value) {
		
		boolean result = false;
		try {
			Client client = clientFactory.createClient_v18(endpoint.isSecure());
			WebResource webResource = client.resource(endpoint.toURL().toURI()).path("items").path(name);

			Gson gson = new Gson();
			String sValue = gson.toJson(value);
			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class, sValue);

			if (response.getStatus() == 200) {
				result = true;
			}

		} catch (Exception e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		}
		return result;
	}

	public boolean setItemsValue(List<ApisItem> items) {
		boolean result = false;
		try {
			Client client = clientFactory.createClient_v18(endpoint.isSecure());
			WebResource webResource = client.resource(endpoint.toURL().toURI()).path("items");

			Gson gson = new Gson();
			String sValue = gson.toJson(items);
			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class, sValue);

			if (response.getStatus() == 200) {
				result = true;
			}

		} catch (Exception e) {
			LOG.warning(String.format("Request to %s failed with exception %s%n", endpoint, e.getMessage()));
		}
		return result;
	}

	public static void main(String[] args) {

		HttpEndpoint endpoint = HttpEndpoint.createFromString("http://localhost:6565/apis");
		ClientFactoryREST_WS clientFactoryREST_WS = new ClientFactoryREST_WS("tester.jks", "changeit", "tester.jks",
				"changeit");

		ApisServiceConsumerREST_WS consumer = new ApisServiceConsumerREST_WS(endpoint, clientFactoryREST_WS);

		List<ApisItem> ret = consumer.getAllItems();
		System.out.println(ret.toString());
		
		List<String> ls = consumer.getAllItemsName();
		System.out.println(ls);
		
		ApisItemValue aiv = consumer.getItemByName("TestItem");
		System.out.println(aiv);
		
		
		
		List<ApisItem> list = new ArrayList();
		aiv.setValue("This Is A New Value of TestItem");
		aiv.setTimestamp(new Date());
		aiv.setQuality((short) 192);
		list.add(new ApisItem("TestItem", aiv) );
		list.add(new ApisItem("IntItem", "This is new value of IntItem", (short) 192, new Date()));
		list.add(new ApisItem("StringItem", "This is new value of StringItem", (short) 192, new Date()));
		boolean br = consumer.setItemsValue(list);
		System.out.println(br);
	}

}
