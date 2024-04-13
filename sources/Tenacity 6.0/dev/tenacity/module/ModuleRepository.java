package dev.tenacity.module;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import dev.tenacity.exception.UnknownModuleException;

import java.util.*;
import java.util.stream.Collectors;

public final class ModuleRepository {

    private final ClassToInstanceMap<Module> moduleMap;

    private ModuleRepository(final Builder builder) {
        this.moduleMap = builder.moduleMap;
    }

    public Collection<Module> getModules() {
        return moduleMap.values();
    }

    public List<Class<? extends Module>> getModulesInCategory(final ModuleCategory category) {
        final List<Class<? extends Module>> returnList = new ArrayList<>();
        for (final Map.Entry<Class<? extends Module>, Module> entry : moduleMap.entrySet())
            if (entry.getValue().getCategory().equals(category))
                returnList.add(entry.getKey());
        return returnList;
    }

    public <T extends Module> T getModule(final Class<T> moduleClass) {
        return moduleMap.getInstance(moduleClass);
    }

    public Module getModuleByName(final String name) {
        for (final Module module : moduleMap.values())
            if (module.getName().equalsIgnoreCase(name))
                return module;

        throw new UnknownModuleException(name);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ClassToInstanceMap<Module> moduleMap = MutableClassToInstanceMap.create();

        public Builder putAll(final Module... modules) {
            for(final Module module : modules)
                moduleMap.put(module.getClass(), module);
            return this;
        }

        public ModuleRepository build() {
            return new ModuleRepository(this);
        }

    }

}
