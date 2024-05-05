package graphite.api.management.module;

import graphite.api.module.Module;

import java.util.Collection;

public interface ModuleManager {
    <T extends Module> void register(final T module);

    Collection<Module> getModules();
}
