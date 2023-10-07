package cc.novoline.modules.configurations;

import cc.novoline.modules.ModuleArrayMap;
import cc.novoline.utils.java.Checks;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author xDelsy
 */
@ConfigSerializable
public final class ClientConfig {

    /* fields @off */
    @Setting("modules")
    private ModuleArrayMap modules;
    @Setting("config-version")
    private int configVersion;

    /* constructors @on */
    public ClientConfig() {
    }

    private ClientConfig(@Nullable ModuleArrayMap options, int configVersion) {
        this.modules = options;
        this.configVersion = configVersion;
    }

    @NonNull
    public static ClientConfig of(@NonNull String name, @Nullable ModuleArrayMap options, int configVersion) {
        Checks.notNull(options, "modules");
        return new ClientConfig(options, configVersion);
    }

    @NonNull
    public static ClientConfig of(@NonNull ConfigManager configManager, @NonNull String name) {
        Checks.notNull(configManager, "module manager");
        return new ClientConfig(configManager.getModuleManager().getModuleManager(), configManager.getConfigVersion());
    }

    /* methods */
    //region Lombok
    public ModuleArrayMap getModules() {
        return this.modules;
    }

    public int getConfigVersion() {
        return this.configVersion;
    }

    @Override
    @NonNull
    public String toString() {
        return "ClientConfig{options=" + this.modules + ", configVersion=" + this.configVersion + '}';
    }
    //endregion

}
