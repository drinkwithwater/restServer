package simpleModule;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.json.JSONException;

import restlet.IRestApiService;

public class Main {
	public static void main(String args[]) throws IOException, JSONException, ClassNotFoundException, InstantiationException, IllegalAccessException{

        System.setProperty("org.restlet.engine.loggerFacadeClass", 
                "org.restlet.ext.slf4j.Slf4jLoggerFacade");

		String configFileName="resources/config.json";
		if(args.length>0){
			File f=new File(args[0]);
			if(f.exists()&&f.isFile()){
				configFileName=args[0];
			}else{
				System.out.println(args[0]+" is not file or not existed");
			}
		}
		final ModuleLoader moduleLoader=new ModuleLoader();
		moduleLoader.configFromFile(configFileName);
		moduleLoader.start();
		Thread thread=new Thread(new Runnable(){
			@Override
			public void run() {
				moduleLoader.getContext().getServiceImpl(IRestApiService.class).run();
			}
		});
		thread.start();
	}

}
