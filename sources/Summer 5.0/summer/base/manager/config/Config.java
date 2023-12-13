package summer.base.manager.config;

import java.io.File;
import java.io.IOException;

import com.google.gson.JsonObject;

import summer.Summer;

public final class Config implements Serializable {

    private final String name;
    private final File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(ConfigManager.CONFIGS_DIR, name + ConfigManager.EXTENTION);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    @Override
    public JsonObject save() {
        JsonObject jsonObject = new JsonObject();
        JsonObject modulesObject = new JsonObject();

        for (Cheats module : Summer.INSTANCE.cheatManager.getModuleList())
            modulesObject.add(module.getName(), module.save());

        jsonObject.add("Modules", modulesObject);

        return jsonObject;
    }

    @Override
    public void load(JsonObject object) {
        if (object.has("Modules")) {
            JsonObject modulesObject = object.getAsJsonObject("Modules");

            for (Cheats module : Summer.INSTANCE.cheatManager.getModuleList()) {
                if (modulesObject.has(module.getName()))
                    module.load(modulesObject.getAsJsonObject(module.getName()));
            }
        }
    }
}
