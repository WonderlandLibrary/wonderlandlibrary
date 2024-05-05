package graphite.api.config;

import com.google.gson.JsonObject;
import graphite.api.management.config.ConfigManager;
import graphite.api.module.Module;
import graphite.impl.Graphite;

import java.io.File;
import java.io.IOException;

public final class Config implements Serializable {
    private final String name;
    private final File file;

    public Config(final String name) {
        this.name = name;
        this.file = new File(Graphite.INSTANCE.getFileManager().getFile("configs"), name + ConfigManager.EXTENSION);

        if (!this.file.exists()) {
            try {
                boolean ignored = this.file.createNewFile();

            } catch (IOException ignored) {
                // TODO :: Error log
            }
        }
    }

    @Override
    public void load(final JsonObject object) {
        if (object.has("modules")) {
            final JsonObject modulesObject = object.getAsJsonObject("modules");

            for (final Module module : Graphite.INSTANCE.getModuleManager().getModules()) {
                if (modulesObject.has(module.getName())) {
                    module.load(modulesObject.getAsJsonObject(module.getName()));
                }
            }
        }
    }

    @Override
    public void save(final JsonObject object) {
        final JsonObject modulesObject = new JsonObject();

        for (final Module module : Graphite.INSTANCE.getModuleManager().getModules()) {
            module.save(modulesObject);
        }

        object.add("modules", modulesObject);
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }
}