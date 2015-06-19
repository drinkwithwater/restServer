
package distribute;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import restlet.IRestApiService;
import simpleModule.IModule;
import simpleModule.ISimpleService;
import simpleModule.ModuleContext;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ISet;
import com.hazelcast.core.ITopic;
import com.hazelcast.logging.LogEvent;
import com.hazelcast.logging.LogListener;

import distribute.web.DistributeRoutable;


public class DistributeServer implements IModule,IDistributeService{
	public static String HAZELCAST_FILE="resources/hazelcast.xml";
    HazelcastInstance hazelcast;
    Long myId;
    @Override
    public void init(ModuleContext context) {
        Config config;
        try {
            config=new XmlConfigBuilder(HAZELCAST_FILE).build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ;
        }
        hazelcast=Hazelcast.newHazelcastInstance(config);
        //gen id
        IAtomicLong atomicLong=hazelcast.getAtomicLong("atomicLong");
        myId=atomicLong.addAndGet(1);
    }
    @Override
    public void startUp(ModuleContext context){
    	IRestApiService restApi=context.getServiceImpl(IRestApiService.class);
    	restApi.addRestletRoutable(new DistributeRoutable());
    }
    @Override
	public Map<Class<? extends ISimpleService>,ISimpleService> getServiceImpls(){
        Map<Class<? extends ISimpleService>, ISimpleService> m = new HashMap<Class<? extends ISimpleService>, ISimpleService>();
        m.put(IDistributeService.class, this);
        return m;
    }
    @Override
    public Long getMyId(){
        return myId;
    }
    @Override
    public IMap getMap(String name){
        return hazelcast.getMap(name);
    }
    @Override
    public ISet getSet(String name){
        return hazelcast.getSet(name);
    }
    public HazelcastInstance getHazelcast(){
    	return this.hazelcast;
    }
    class DebugListener implements LogListener{
		@Override
		public void log(LogEvent arg0) {
		}
    }
}