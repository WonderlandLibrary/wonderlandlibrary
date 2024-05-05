// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.config.impl;

import java.util.Iterator;
import com.google.gson.JsonElement;
import Lavish.modules.Module;
import Lavish.Client;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import java.io.File;
import Lavish.config.IFile;

public class ModulesFile implements IFile
{
    private File file;
    
    @Override
    public void save(final Gson gson) {
        final JsonObject object = new JsonObject();
        final JsonObject modulesObject = new JsonObject();
        for (final Module module : Client.instance.moduleManager.getModules()) {
            modulesObject.add(module.getName(), (JsonElement)module.save(false));
        }
        object.add("Modules", (JsonElement)modulesObject);
        this.writeFile(gson.toJson((JsonElement)object), this.file);
    }
    
    @Override
    public void load(final Gson gson) {
        if (!this.file.exists()) {
            return;
        }
        final JsonObject object = (JsonObject)gson.fromJson(this.readFile(this.file), (Class)JsonObject.class);
        if (object.has("Modules")) {
            final JsonObject modulesObject = object.getAsJsonObject("Modules");
            for (final Module module : Client.instance.moduleManager.getModules()) {
                if (modulesObject.has(module.getName())) {
                    module.load(modulesObject.getAsJsonObject(module.getName()), false);
                }
            }
        }
    }
    
    @Override
    public void setFile(final File root) {
        this.file = new File(root, "/modules.json");
    }
}
