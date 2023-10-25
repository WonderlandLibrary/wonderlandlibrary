/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.settings.config;

import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import org.celestial.client.Celestial;
import org.celestial.client.feature.Feature;
import org.celestial.client.settings.config.ConfigManager;
import org.celestial.client.settings.config.ConfigUpdater;

public final class Config
implements ConfigUpdater {
    private String name;
    private final File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(ConfigManager.cfg_dir, name + ".json");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JsonObject save() {
        JsonObject jsonObject = new JsonObject();
        JsonObject modulesObject = new JsonObject();
        for (Feature module : Celestial.instance.featureManager.getFeatureList()) {
            modulesObject.add(module.getLabel(), module.save());
        }
        jsonObject.addProperty("Version", Celestial.version);
        jsonObject.add("Features", modulesObject);
        return jsonObject;
    }

    @Override
    public void load(JsonObject object) {
        if (object.has("Features")) {
            JsonObject modulesObject = object.getAsJsonObject("Features");
            for (Feature module : Celestial.instance.featureManager.getFeatureList()) {
                module.setState(false);
                module.load(modulesObject.getAsJsonObject(module.getLabel()));
            }
        }
    }
}

