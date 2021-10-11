package ch.cern.cmms.wshub.tools;

import ch.cern.cmms.plugins.SharedPlugin;
import ch.cern.cmms.plugins.SharedPluginImpl;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class PluginProducer {
    @Produces
    private SharedPlugin sharedPlugin;

    @PostConstruct
    public void init() {
        sharedPlugin = new SharedPluginImpl();
    }

    public SharedPlugin getSharedPlugin() {
        return sharedPlugin;
    }
}
