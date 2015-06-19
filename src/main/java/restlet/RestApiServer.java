package restlet;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.service.StatusService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import simpleModule.IModule;
import simpleModule.ISimpleService;
import simpleModule.ModuleContext;


public class RestApiServer
    implements IModule, IRestApiService {
	
	
	public static Logger logger=LoggerFactory.getLogger(RestApiServer.class);
    
    protected List<RestletRoutable> restlets;

    protected int restPort = 7080;

    protected ModuleContext moContext;
    
    // ***********
    // Application
    // ***********
    
    protected class RestApplication extends Application {
        protected Context context;
        
        public RestApplication() {
            super(new Context());
            this.context = getContext();
        }
        
        @Override
        public Restlet createInboundRoot() {
            Router baseRouter = new Router(context);
            baseRouter.setDefaultMatchingMode(Template.MODE_STARTS_WITH);
            for (RestletRoutable rr : restlets) {
                baseRouter.attach(rr.basePath(), rr.getRestlet(context));
            }

            Filter slashFilter = new Filter() {            
                @Override
                protected int beforeHandle(Request request, Response response) {
                    Reference ref = request.getResourceRef();
                    String originalPath = ref.getPath();
                    if (originalPath.contains("//"))
                    {
                        String newPath = originalPath.replaceAll("/+", "/");
                        ref.setPath(newPath);
                    }
                    return Filter.CONTINUE;
                }

            };
            slashFilter.setNext(baseRouter);
            
            return slashFilter;
        }
        
        public void run(ModuleContext moContext, String restHost, int restPort) {
            setStatusService(new StatusService() {
                @Override
                public Representation getRepresentation(Status status,
                                                        Request request,
                                                        Response response) {
                	Representation re=new StringRepresentation(status.toString());
                    return re;
                }                
            });
            
            
            for (Class<? extends ISimpleService> s : moContext.getAllServices()) {
                    logger.info("Adding "+s.getCanonicalName()+" for service {} into context" +moContext.getServiceImpl(s).toString());
                context.getAttributes().put(s.getCanonicalName(), moContext.getServiceImpl(s));
            }
            // Start listening for REST requests
            try {
                final Component component = new Component();
                if (restHost == null) {
                	component.getServers().add(Protocol.HTTP, restPort);
                } else {
                	component.getServers().add(Protocol.HTTP, restHost, restPort);
                }
                component.getClients().add(Protocol.CLAP);
                component.getDefaultHost().attach(this);
                component.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    // ***************
    // IRestApiService
    // ***************
    
    @Override
    public void addRestletRoutable(RestletRoutable routable) {
        restlets.add(routable);
    }

    @Override
    public void run() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("REST API routables: ");
    	for (RestletRoutable routable : restlets) {
    		sb.append(routable.getClass().getSimpleName());
    		sb.append(" (");
    		sb.append(routable.basePath());
    		sb.append("), ");
    	}
    	logger.info(sb.toString());
        
        RestApplication restApp = new RestApplication();
        restApp.run(moContext, null, restPort);
    }
    
    // *****************
    // IWEBridgeModule
    // *****************
    


    @Override
	public Map<Class<? extends ISimpleService>,ISimpleService> getServiceImpls(){
        Map<Class<? extends ISimpleService>,
        ISimpleService> m = new HashMap<Class<? extends ISimpleService>, ISimpleService>();
        m.put(IRestApiService.class, this);
        return m;
    }

    @Override
    public void init(ModuleContext context) {
        // This has to be done here since we don't know what order the
        // startUp methods will be called
        this.restlets = new ArrayList<RestletRoutable>();
        this.moContext=context;
        
        // read our config options
        logger.info("REST port set to "+this.restPort);
    }

    @Override
    public void startUp(ModuleContext context) {
    	//this.addRestletRoutable(new PeerRoutable());
    	//this.addRestletRoutable(new FileRoutable());
    }
    public static void main(String args[]){
    	RestApiServer rs=new RestApiServer();
    	rs.init(null);
    	rs.run();
    	/*
    	RestApiServer rs=new RestApiServer();
    	rs.init(new WEBridgeModuleContext());
    	rs.addRestletRoutable(new PeerRoutable());
    	rs.addRestletRoutable(new FileRoutable());
    	rs.run();*/
    }
}