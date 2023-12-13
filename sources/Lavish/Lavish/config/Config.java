// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.config;

import java.util.Iterator;
import com.google.gson.JsonElement;
import Lavish.modules.Module;
import Lavish.Client;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.File;
import java.io.Serializable;

public final class Config implements Serializable
{
    private final String name;
    private final File file;
    
    public Config(final String name) {
        this.name = name;
        this.file = new File(ConfigManager.CONFIGS_DIR, String.valueOf(name) + ".json");
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
    
    public JsonObject save() {
        final JsonObject jsonObject = new JsonObject();
        final JsonObject modulesObject = new JsonObject();
        for (final Module module : Client.instance.moduleManager.getModules()) {
            modulesObject.add(module.getName(), (JsonElement)module.save(true));
        }
        jsonObject.add("Modules", (JsonElement)modulesObject);
        return jsonObject;
    }
    
    public void load(final JsonObject object) {
        if (object.has("Modules")) {
            final JsonObject modulesObject = object.getAsJsonObject("Modules");
            for (final Module module : Client.instance.moduleManager.getModules()) {
                if (modulesObject.has(module.getName())) {
                    module.load(modulesObject.getAsJsonObject(module.getName()), true);
                }
            }
        }
    }
}
