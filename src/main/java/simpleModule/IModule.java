package simpleModule;

import java.util.Map;

public interface IModule {
	public void init(ModuleContext context);
	public void startUp(ModuleContext context);
	public Map<Class<? extends ISimpleService>,ISimpleService> getServiceImpls();
}
