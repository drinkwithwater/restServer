package distribute.web;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestResource  extends ServerResource{
	Logger logger=LoggerFactory.getLogger(TestResource.class);
	@Get("json")
	public String retrieve() {
		return "fdsfds";
	}

}
