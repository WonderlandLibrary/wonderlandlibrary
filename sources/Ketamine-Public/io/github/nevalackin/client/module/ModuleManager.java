package io.github.nevalackin.client.module;

import java.util.Collection;

public interface ModuleManager {

    <T extends Module> T getModule(final Class<T> clazz);

    Module getModule(final String name);

    <T extends Module> void registerModule(final Class<T> clazz, final T module);

    Collection<Module> getModules();

}
