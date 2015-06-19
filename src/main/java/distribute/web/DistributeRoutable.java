package distribute.web;

import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import restlet.RestletRoutable;

public class DistributeRoutable implements RestletRoutable{

	@Override
	public Restlet getRestlet(Context context) {
        Router router = new Router(context);
        router.attach("/test",TestResource.class);
        router.attach("/topic/{message}",TopicResource.class);
        return router;
	}

	@Override
	public String basePath() {
		// TODO Auto-generated method stub
		return "/distribute";
	}

}
