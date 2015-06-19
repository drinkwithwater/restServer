package distribute;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

import simpleModule.IModule;
import simpleModule.ISimpleService;
import simpleModule.ModuleContext;

public class TopicManager implements IModule,ITopicService{

	public static String BROADCAST_TOPIC="broadcast";
	public static Logger logger=LoggerFactory.getLogger(TopicManager.class);
	IDistributeService distribute;
	ITopic<BroadcastEvent> topic;
	@Override
	public void init(ModuleContext context) {
		distribute=context.getServiceImpl(IDistributeService.class);
	}

	@Override
	public void startUp(ModuleContext context) {
		topic=distribute.getHazelcast().getTopic(BROADCAST_TOPIC);
		topic.addMessageListener(new BroadcastListener());
	}

	@Override
	public Map<Class<? extends ISimpleService>, ISimpleService> getServiceImpls() {
        Map<Class<? extends ISimpleService>, ISimpleService> m = new HashMap<Class<? extends ISimpleService>, ISimpleService>();
        m.put(ITopicService.class, this);
        return m;
	}

	@Override
	public void broadcastMessage(String message) {
		topic.publish(new BroadcastEvent(message));
	}

	@Override
	public void unicastMessage(String id, String message) {
	}
	
	class BroadcastListener implements MessageListener<BroadcastEvent>{
		@Override
		public void onMessage(Message<BroadcastEvent> msg) {
			logger.info(msg.getMessageObject().getContent());
		}
	}
}
