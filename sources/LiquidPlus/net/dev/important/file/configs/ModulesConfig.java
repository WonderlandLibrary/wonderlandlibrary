/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 */
package net.dev.important.file.configs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Map;
import net.dev.important.Client;
import net.dev.important.file.FileConfig;
import net.dev.important.file.FileManager;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.misc.AutoDisable;

public class ModulesConfig
extends FileConfig {
    public ModulesConfig(File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {
        JsonElement jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(this.getFile())));
        if (jsonElement instanceof JsonNull) {
            return;
        }
        for (Map.Entry entry : jsonElement.getAsJsonObject().entrySet()) {
            Module module2 = Client.moduleManager.getModule((String)entry.getKey());
            if (module2 == null) continue;
            JsonObject jsonModule = (JsonObject)entry.getValue();
            module2.setState(jsonModule.get("State").getAsBoolean());
            module2.setKeyBind(jsonModule.get("KeyBind").getAsInt());
            if (jsonModule.has("Array")) {
                module2.setArray(jsonModule.get("Array").getAsBoolean());
            }
            if (!jsonModule.has("AutoDisable")) continue;
            module2.getAutoDisables().clear();
            try {
                JsonArray jsonAD = jsonModule.getAsJsonArray("AutoDisable");
                if (jsonAD.size() <= 0) continue;
                for (int i = 0; i <= jsonAD.size() - 1; ++i) {
                    try {
                        AutoDisable.DisableEvent disableEvent = AutoDisable.DisableEvent.valueOf(jsonAD.get(i).getAsString());
                        module2.getAutoDisables().add(disableEvent);
                        continue;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
            catch (Exception exception) {
            }
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        JsonObject jsonObject = new JsonObject();
        for (Module module2 : Client.moduleManager.getModules()) {
            JsonObject jsonMod = new JsonObject();
            jsonMod.addProperty("State", Boolean.valueOf(module2.getState()));
            jsonMod.addProperty("KeyBind", (Number)module2.getKeyBind());
            jsonMod.addProperty("Array", Boolean.valueOf(module2.getArray()));
            JsonArray jsonAD = new JsonArray();
            for (AutoDisable.DisableEvent e : module2.getAutoDisables()) {
                jsonAD.add((JsonElement)new JsonPrimitive(e.toString()));
            }
            jsonMod.add("AutoDisable", (JsonElement)jsonAD);
            jsonObject.add(module2.getName(), (JsonElement)jsonMod);
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonObject));
        printWriter.close();
    }
}

