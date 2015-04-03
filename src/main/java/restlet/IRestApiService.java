package restlet;

import simpleModule.ISimpleService;



public interface IRestApiService extends ISimpleService{
    /**
     * Adds a REST API
     * @param routeable
     */
    public void addRestletRoutable(RestletRoutable routable);

    /**
     * Runs the REST API server
     */
    public void run();
}
