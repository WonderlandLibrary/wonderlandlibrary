// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.config;

import java.util.Iterator;
import com.google.gson.JsonElement;
import ru.tuskevich.modules.Module;
import ru.tuskevich.Minced;
import com.google.gson.JsonObject;
import java.io.File;

public final class Config implements ConfigUpdater
{
    private final String name;
    private final File file;
    
    public Config(final String name) {
        this.name = name;
        this.file = new File(ConfigManager.configDirectory, name + ".json");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            }
            catch (Exception ex) {}
        }
    }
    
    public File getFile() {
        return this.file;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public JsonObject save() {
        final JsonObject jsonObject = new JsonObject();
        final JsonObject modulesObject = new JsonObject();
        for (final Module module : Minced.getInstance().manager.getModules()) {
            modulesObject.add(module.name, (JsonElement)module.save());
        }
        jsonObject.add("Features", (JsonElement)modulesObject);
        return jsonObject;
    }
    
    @Override
    public void load(final JsonObject object) {
        if (object.has("Features")) {
            final JsonObject modulesObject = object.getAsJsonObject("Features");
            for (final Module module : Minced.getInstance().manager.getModules()) {
                module.load(modulesObject.getAsJsonObject(module.name));
            }
        }
    }
}
