package tech.atani.client.listener.event.client.module;

import tech.atani.client.feature.module.Module;
import tech.atani.client.listener.event.Event;

public class EnableModuleEvent extends Event {
    private final Module module;
    final EnableModuleEvent.Type type;

    public enum Type {
        PRE, POST
    }

    public EnableModuleEvent(Module module, Type type) {
        this.module = module;
        this.type = type;
    }

    public Module getModule() {
        return module;
    }

    public Type getType() {
        return type;
    }
}
