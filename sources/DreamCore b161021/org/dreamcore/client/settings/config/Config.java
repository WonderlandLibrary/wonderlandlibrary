package org.dreamcore.client.settings.config;

import com.google.gson.JsonObject;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.feature.Feature;

import java.io.File;

public final class Config implements ConfigUpdater {

    private final String name;
    private final File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(ConfigManager.configDirectory, name + ".json");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
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
        JsonObject panelObject = new JsonObject();

        for (Feature module : dreamcore.instance.featureManager.getFeatureList()) {
            modulesObject.add(module.getLabel(), module.save());
        }

        jsonObject.add("Features", modulesObject);

        return jsonObject;
    }

    @Override
    public void load(JsonObject object) {
        if (object.has("Features")) {
            JsonObject modulesObject = object.getAsJsonObject("Features");
            for (Feature module : dreamcore.instance.featureManager.getFeatureList()) {
                module.setState(false);
                module.load(modulesObject.getAsJsonObject(module.getLabel()));
            }
        }
    }
}