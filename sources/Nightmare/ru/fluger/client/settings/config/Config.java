// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.settings.config;

import java.util.Iterator;
import com.google.gson.JsonElement;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.Fluger;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.File;

public final class Config implements ConfigUpdater
{
    private String name;
    private final File file;
    
    public Config(final String name) {
        this.name = name;
        this.file = new File(ConfigManager.cfg_dir, name + ".json");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            }
            catch (IOException ex) {}
        }
    }
    
    public File getFile() {
        return this.file;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public JsonObject save() {
        final JsonObject jsonObject = new JsonObject();
        final JsonObject modulesObject = new JsonObject();
        for (final Feature module : Fluger.instance.featureManager.getFeatureList()) {
            modulesObject.add(module.getLabel(), (JsonElement)module.save());
        }
        jsonObject.addProperty("Version", Fluger.version);
        jsonObject.add("Features", (JsonElement)modulesObject);
        return jsonObject;
    }
    
    @Override
    public void load(final JsonObject object) {
        if (object.has("Features")) {
            final JsonObject modulesObject = object.getAsJsonObject("Features");
            for (final Feature module : Fluger.instance.featureManager.getFeatureList()) {
                module.setState(false);
                module.load(modulesObject.getAsJsonObject(module.getLabel()));
            }
        }
    }
}
