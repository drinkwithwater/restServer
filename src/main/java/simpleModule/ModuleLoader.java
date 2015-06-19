package simpleModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import restlet.IRestApiService;

public class ModuleLoader {
    
    
	protected ModuleContext moduleContext;
    protected Collection<IModule> moduleSet;
    protected Collection<Class<? extends IModule>> classSet;

    public ModuleLoader(){
    	moduleSet=new HashSet<IModule>();
    	classSet=new HashSet<Class<? extends IModule>>();
    	moduleContext=new ModuleContext();
    }
    public void configFromFile(String fileName) throws ClassNotFoundException, IOException, JSONException{
    	if(fileName.lastIndexOf(".json")==fileName.length()-5){
    		this.configFromJSONFile(fileName);
    	}else if(fileName.lastIndexOf(".xml")==fileName.length()-4){
    		this.configFromXMLFile(fileName);
    	}
    }
    @SuppressWarnings("unchecked")
	public void configFromJSONFile(String fileName) throws IOException, JSONException, ClassNotFoundException{
    	File file=new File(fileName);
    	FileInputStream fileStream=new FileInputStream(file);
    	BufferedReader reader=new BufferedReader(new InputStreamReader(fileStream));
    	StringBuilder sb=new StringBuilder();
    	String line=null;
    	while((line=reader.readLine())!=null){
    		sb.append(line);
    		sb.append("\n");
    	}
    	JSONObject configJson=new JSONObject(sb.toString());
    	JSONArray moduleArray=configJson.getJSONArray("modules");

    	//load class
    	ClassLoader classLoader=ClassLoader.getSystemClassLoader();
    	for(int i=0,length=moduleArray.length();i<length;i++){
    		String moduleName=moduleArray.getString(i);
    		Class<? extends IModule> clazz;
    		clazz=(Class<? extends IModule>) classLoader.loadClass(moduleName);
    		classSet.add(clazz);
    	}

    	//add config for each module into context
    	for(Class<? extends IModule> clazz:classSet){
    		JSONObject moduleConfig;
    		try{
    			moduleConfig=configJson.getJSONObject(clazz.getCanonicalName());
    		}catch(JSONException e){
    			continue;
    		}
    		Iterator<?> iter=moduleConfig.keys();
    		while(iter.hasNext()){
    			String key=(String) iter.next();
    			String value=moduleConfig.getString(key);
    			this.moduleContext.addConfigParam(clazz, key, value);
    		}
    	}

    	reader.close();
    }
    @SuppressWarnings("unchecked")
	public void configFromXMLFile(String fileName) throws IOException, JSONException, ClassNotFoundException{
    	//TODO
    	throw new IOException();
    }

    public void start() throws InstantiationException, IllegalAccessException{

    	//new intance
    	for(Class<? extends IModule> clazz:classSet){
    		IModule module=(IModule)clazz.newInstance();
    		this.moduleSet.add(module);
    	}
    	//init context
    	for(IModule module:moduleSet){
    		moduleContext.addModule(module);
    		for(Entry<Class<? extends ISimpleService>,ISimpleService> e:module.getServiceImpls().entrySet()){
    			moduleContext.addService(e.getKey(), e.getValue());
    		}
    	}
    	//module start
    	for(IModule module:moduleSet){
    		module.init(moduleContext);
    	}
    	for(IModule module:moduleSet){
    		module.startUp(moduleContext);
    	}
    }
    public ModuleContext getContext(){
    	return moduleContext;
    }
    public static void main(String args[]) throws IOException, InterruptedException, JSONException, ClassNotFoundException, InstantiationException, IllegalAccessException{
    	ModuleLoader loader=new ModuleLoader();
    	loader.configFromFile("resources/config.json");
    	loader.start();
    	loader.getContext().getServiceImpl(IRestApiService.class).run();
    }
}
