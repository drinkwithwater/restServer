package distribute;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ISet;

import simpleModule.ISimpleService;

public interface IDistributeService extends ISimpleService{
	public Long getMyId();
	public IMap getMap(String name);
	public ISet getSet(String name);
	public HazelcastInstance getHazelcast();
}
