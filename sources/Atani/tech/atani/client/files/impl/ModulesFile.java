package tech.atani.client.files.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.files.LocalFile;
import tech.atani.client.files.data.FileData;

@FileData(fileName = "modules")
public class ModulesFile extends LocalFile {

    @Override
    public void save(Gson gson) {
        JsonObject object = new JsonObject();

        JsonObject modulesObject = new JsonObject();

        for (Module module : ModuleStorage.getInstance().getList())
            modulesObject.add(module.getIdentifier(), module.save());

        object.add("Modules", modulesObject);

        writeFile(gson.toJson(object), file);
    }

    @Override
    public void load(Gson gson) {
        if (!file.exists()) {
            return;
        }

        JsonObject object = gson.fromJson(readFile(file), JsonObject.class);
        if (object.has("Modules")){
            JsonObject modulesObject = object.getAsJsonObject("Modules");

            for (Module module : ModuleStorage.getInstance().getList()) {
                if (modulesObject.has(module.getIdentifier()))
                    module.load(modulesObject.getAsJsonObject(module.getIdentifier()));
            }
        }
    }

}