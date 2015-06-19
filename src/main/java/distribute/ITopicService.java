package distribute;

import simpleModule.ISimpleService;

public interface ITopicService extends ISimpleService {
	public void broadcastMessage(String message);
	public void unicastMessage(String id,String message);
}
