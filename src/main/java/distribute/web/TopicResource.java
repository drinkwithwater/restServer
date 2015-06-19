package distribute.web;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import distribute.ITopicService;


public class TopicResource  extends ServerResource{
	Logger logger=LoggerFactory.getLogger(TopicResource.class);
	@Get("json")
	public String retrieve() {
		String s=(String)this.getRequestAttributes().get("message");
        ITopicService topicManager= (ITopicService)getContext().getAttributes().
                get(ITopicService.class.getCanonicalName());
        topicManager.broadcastMessage(s);
		return "broadcast";
	}

}
