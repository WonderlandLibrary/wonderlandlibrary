/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.lwjgl.input.Keyboard
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
import net.ccbluex.liquidbounce.value.Value;
import org.lwjgl.input.Keyboard;

public class ProfilesConfig
extends FileConfig {
    public float yPos = 0.0f;

    public ProfilesConfig(File file) {
        super(file);
    }

    @Override
    public void loadConfig() throws IOException {
        JsonElement jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(this.getFile())));
        if (jsonElement instanceof JsonNull) {
            return;
        }
        for (Map.Entry entry : jsonElement.getAsJsonObject().entrySet()) {
            Module module = LiquidBounce.moduleManager.getModule((String)entry.getKey());
            if (module == null) continue;
            JsonObject jsonModule = (JsonObject)entry.getValue();
            if (jsonModule.has("Active")) {
                module.setState(jsonModule.get("Active").getAsBoolean());
            }
            if (jsonModule.has("KeyBind")) {
                module.setKeyBind(Keyboard.getKeyIndex((String)jsonModule.get("KeyBind").getAsString()));
            }
            if (module.getValues().isEmpty()) continue;
            for (Value<?> moduleValue : module.getValues()) {
                JsonElement element = jsonModule.get(moduleValue.getName());
                if (element == null) continue;
                moduleValue.fromJson(element);
            }
        }
    }

    @Override
    public void saveConfig() throws IOException {
        JsonObject jsonObject = new JsonObject();
        LiquidBounce.moduleManager.getModules().forEach(module -> {
            JsonObject jsonModule = new JsonObject();
            jsonModule.addProperty("Active", Boolean.valueOf(module.getState()));
            jsonModule.addProperty("KeyBind", Keyboard.getKeyName((int)module.getKeyBind()));
            if (!module.getValues().isEmpty()) {
                module.getValues().forEach(value -> jsonModule.add(value.getName(), value.toJson()));
            }
            jsonObject.add(module.getName(), (JsonElement)jsonModule);
        });
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonObject));
        printWriter.close();
    }
}

