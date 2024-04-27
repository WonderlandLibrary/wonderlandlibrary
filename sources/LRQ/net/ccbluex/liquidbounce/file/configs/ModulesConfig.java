/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package net.ccbluex.liquidbounce.file.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Map;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.file.FileManager;

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
            Module module = LiquidBounce.moduleManager.getModule((String)entry.getKey());
            if (module == null) continue;
            JsonObject jsonModule = (JsonObject)entry.getValue();
            module.setState(jsonModule.get("State").getAsBoolean());
            module.setKeyBind(jsonModule.get("KeyBind").getAsInt());
            if (!jsonModule.has("Array")) continue;
            module.setArray(jsonModule.get("Array").getAsBoolean());
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        JsonObject jsonObject = new JsonObject();
        for (Module module : LiquidBounce.moduleManager.getModules()) {
            JsonObject jsonMod = new JsonObject();
            jsonMod.addProperty("State", Boolean.valueOf(module.getState()));
            jsonMod.addProperty("KeyBind", (Number)module.getKeyBind());
            jsonMod.addProperty("Array", Boolean.valueOf(module.getArray()));
            jsonObject.add(module.getName(), (JsonElement)jsonMod);
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonObject));
        printWriter.close();
    }
}

