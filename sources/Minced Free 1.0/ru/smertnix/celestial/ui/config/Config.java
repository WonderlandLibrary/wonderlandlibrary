package ru.smertnix.celestial.ui.config;

import com.google.gson.JsonObject;

import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.Feature;

import java.io.File;

public final class Config implements ConfigUpdater {

    private final String name;
    private final File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(ConfigManager.configDirectory, name + ".celka");

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

        for (Feature module : Celestial.instance.featureManager.getAllFeatures()) {
            modulesObject.add(module.getLabel(), module.save());
        }

        jsonObject.add("Features", modulesObject);

        return jsonObject;
    }

    @Override
    public void load(JsonObject object) {
        if (object.has("Features")) {
            JsonObject modulesObject = object.getAsJsonObject("Features");
            for (Feature module : Celestial.instance.featureManager.getAllFeatures()) {
                module.setEnabled(false);
                module.load(modulesObject.getAsJsonObject(module.getLabel()));
            }
        }
    }
}