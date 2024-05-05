package club.dortware.client;

import club.dortware.client.file.FileManager;
import club.dortware.client.event.impl.KeyboardEvent;
import club.dortware.client.manager.impl.ModuleManager;
import club.dortware.client.manager.impl.PropertyManager;
import club.dortware.client.module.Module;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * @author Dort
 */
public enum Client {
    INSTANCE;

    private final String clientName = "Dortware";
    private final String clientVersion = "1.0";
    private PropertyManager propertyManager;
    private ModuleManager moduleManager;
    private EventBus eventBus;
    private FileManager fileManager;

    /**
     * Initializes this {@code Client}
     */
    public void initialize() {
        eventBus = new EventBus(clientName);
        propertyManager = new PropertyManager();
        moduleManager = new ModuleManager();
        fileManager = new FileManager();
        eventBus.register(this);
    }

    @Subscribe
    public void onKeyboardClick(KeyboardEvent event) {
        for (Module module : moduleManager.getList()) {
            if (module.getKeyBind() != 0 && event.getKey() == module.getKeyBind())
                module.toggle();
        }
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public FileManager getFileManager() {
        return fileManager;
    }
}
