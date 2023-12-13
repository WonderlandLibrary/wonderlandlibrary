package io.github.nevalackin.client.config;

import com.google.gson.JsonObject;
import io.github.nevalackin.client.config.ConfigManager;
import io.github.nevalackin.client.config.Serializable;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.util.render.ColourUtil;

import java.io.File;
import java.io.IOException;

public final class Config implements Serializable {

    private final String name;
    private final File file;

    public Config(final String name) {
        this.name = name;
        this.file = new File(KetamineClient.getInstance().getFileManager().getFile("configs"), name + ConfigManager.EXTENSION);

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
        if (object.has("dateModified")) {
            // TODO :: Configs date modified
        }

        if (object.has("clientColour")) {
            ColourUtil.setClientColour(object.get("clientColour").getAsInt());
        }

        if (object.has("modules")) {
            final JsonObject modulesObject = object.getAsJsonObject("modules");

            for (final Module module : KetamineClient.getInstance().getModuleManager().getModules()) {
                if (modulesObject.has(module.getName())) {
                    module.load(modulesObject.getAsJsonObject(module.getName()));
                }
            }
        }
    }

    @Override
    public void save(final JsonObject object) {
        object.addProperty("dateModified", System.currentTimeMillis());
        object.addProperty("clientColour", ColourUtil.getClientColour());

        final JsonObject modulesObject = new JsonObject();

        for (final Module module : KetamineClient.getInstance().getModuleManager().getModules()) {
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
