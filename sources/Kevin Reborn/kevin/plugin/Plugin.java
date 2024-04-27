package kevin.plugin;

import kevin.main.KevinClient;
import org.apache.logging.log4j.Logger;

public abstract class Plugin {
    private Logger logger;
    private PluginDescription description;
    public void onLoad() {}

    public void registerModule(PluginModule module) {
        KevinClient.moduleManager.registerModule(module);
    }

    public Logger getLogger() {
        return logger;
    }

    public PluginDescription getDescription() {
        return description;
    }

    public void initialize(Logger logger, PluginDescription description) {
        PluginManager.sensitiveUtils.callerSensitivity(PluginManager.sensitiveUtils.getCallerClass());
        this.logger = logger;
        this.description = description;
    }
}
