package cc.novoline.modules;

import cc.novoline.Novoline;
import cc.novoline.modules.binds.BindManager;
import cc.novoline.modules.configurations.ConfigManager;
import cc.novoline.modules.configurations.holder.CreatingModuleHolder;
import cc.novoline.modules.exceptions.UnregisteredModuleException;
import cc.novoline.utils.java.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ModuleManager {

    /* fields */
    private final Novoline novoline;
    private final ConfigManager configManager;
    private final ModuleArrayMap moduleManager = new ModuleArrayMap();
    private final BindManager bindManager;

    private final List<AbstractModule> abstractModules = new CopyOnWriteArrayList<>();

    /* constructors */
    public ModuleManager(@NotNull Novoline novoline, int configVersion) {
        this.novoline = novoline;
        this.configManager = new ConfigManager(this, configVersion);
        this.bindManager = new BindManager(this, configVersion);
    }

    /* methods */
    public <Module extends AbstractModule> @NotNull Module getModule(Class<? extends Module> moduleClass) {
        return moduleManager.getByClass(moduleClass);
    }

    public <Module extends AbstractModule> @Nullable Module getByNameIgnoreCase(String name) {
        return moduleManager.getByNameIgnoreCase(name);
    }

    public @NotNull List<AbstractModule> getModuleListByCategory(EnumModuleType enumModuleType) {
        return moduleManager.getByCategory(enumModuleType);
    }

    @SuppressWarnings("unchecked")
    public <Module extends AbstractModule> void register(@NotNull String name, @NotNull ModuleCreator<Module> moduleCreator) {
        Checks.notBlank(name, "name");

        CreatingModuleHolder<Module> holder = CreatingModuleHolder.of(this, name, moduleCreator);

        if (isRegistered((Class<? extends Module>) holder.getTypeToken().getRawType())) {
            throw new IllegalStateException("module may not be registered twice");
        }

        moduleManager.put(name, holder);
    }

    public boolean isRegistered(@NotNull Class<? extends AbstractModule> moduleClass) {
        try {
            moduleManager.getByClass(moduleClass);
            return true;
        } catch (UnregisteredModuleException e) {
            return false;
        }
    }

    //region Lombok
    public @NotNull Novoline getNovoline() { return novoline; }
    public @NotNull ConfigManager getConfigManager() { return configManager; }
    public @NotNull BindManager getBindManager() { return bindManager; }
    public @NotNull ModuleArrayMap getModuleManager() { return moduleManager; }
    public @NotNull List<AbstractModule> getAbstractModules() { return abstractModules; }
    //endregion

    @FunctionalInterface
    public interface ModuleCreator<Module extends AbstractModule> {

        @NotNull Module create(@NotNull ModuleManager moduleManager);
    }
}
