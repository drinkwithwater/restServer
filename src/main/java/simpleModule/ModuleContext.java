package simpleModule;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ModuleContext {
	protected Map<Class<? extends ISimpleService>,ISimpleService> serviceMap;
	protected Map<Class<? extends IModule>,Map<String,String>> configParams;
	protected Collection<IModule> moduleSet;
	//configure param

	public ModuleContext(){
		serviceMap=new HashMap<Class<? extends ISimpleService>,ISimpleService>();
		configParams=new HashMap<Class<? extends IModule>,Map<String,String>>();
		moduleSet=new HashSet<IModule>();
	}
	public void addService(Class<? extends ISimpleService> clazz,ISimpleService service){
		serviceMap.put(clazz, service);
	}
	public void addModule(IModule module){
		moduleSet.add(module);
	}
	public Collection<Class<? extends ISimpleService>> getAllServices(){
		return serviceMap.keySet();
	}
	@SuppressWarnings("unchecked")
	public <T extends ISimpleService> T getServiceImpl(Class<T> serviceClass){
		ISimpleService s=serviceMap.get(serviceClass);
		return (T)s;
	}
	public Map<String,String> getConfigParams(Class<? extends IModule> clazz){
		Map<String,String> reMap=this.configParams.get(clazz);
		if(reMap==null){
			reMap=new HashMap<String,String>();
			configParams.put(clazz,reMap);
		}
		/*
		 * TODO
		 * Maybe i should check the super class configuration in the future.
		 */
		return reMap;
	}
	/**
	 * for the module loader configure parameter
	 * @param clazz
	 * @param key
	 * @param value
	 */
	public void addConfigParam(Class<? extends IModule> clazz,String key,String value){
        Map<String, String> moduleParams = configParams.get(clazz);
        if (moduleParams == null) {
            moduleParams = new HashMap<String, String>();
            configParams.put(clazz, moduleParams);
        }
        moduleParams.put(key, value);
	}
}
