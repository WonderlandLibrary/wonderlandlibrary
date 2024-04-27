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
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.features.special.AutoReconnect;
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.file.FileManager;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator.GuiTheAltening;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.value.Value;

public class ValuesConfig
extends FileConfig {
    public ValuesConfig(File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {
        JsonElement jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(this.getFile())));
        if (jsonElement instanceof JsonNull) {
            return;
        }
        JsonObject jsonObject = (JsonObject)jsonElement;
        for (Map.Entry entry : jsonObject.entrySet()) {
            Object object;
            if (((String)entry.getKey()).equalsIgnoreCase("CommandPrefix")) {
                LiquidBounce.commandManager.setPrefix(((JsonElement)entry.getValue()).getAsCharacter());
                continue;
            }
            if (((String)entry.getKey()).equalsIgnoreCase("targets")) {
                object = (JsonObject)entry.getValue();
                if (object.has("TargetPlayer")) {
                    EntityUtils.targetPlayer = object.get("TargetPlayer").getAsBoolean();
                }
                if (object.has("TargetMobs")) {
                    EntityUtils.targetMobs = object.get("TargetMobs").getAsBoolean();
                }
                if (object.has("TargetAnimals")) {
                    EntityUtils.targetAnimals = object.get("TargetAnimals").getAsBoolean();
                }
                if (object.has("TargetInvisible")) {
                    EntityUtils.targetInvisible = object.get("TargetInvisible").getAsBoolean();
                }
                if (!object.has("TargetDead")) continue;
                EntityUtils.targetDead = object.get("TargetDead").getAsBoolean();
                continue;
            }
            if (((String)entry.getKey()).equalsIgnoreCase("features")) {
                object = (JsonObject)entry.getValue();
                if (object.has("AntiForge")) {
                    AntiForge.enabled = object.get("AntiForge").getAsBoolean();
                }
                if (object.has("AntiForgeFML")) {
                    AntiForge.blockFML = object.get("AntiForgeFML").getAsBoolean();
                }
                if (object.has("AntiForgeProxy")) {
                    AntiForge.blockProxyPacket = object.get("AntiForgeProxy").getAsBoolean();
                }
                if (object.has("AntiForgePayloads")) {
                    AntiForge.blockPayloadPackets = object.get("AntiForgePayloads").getAsBoolean();
                }
                if (object.has("BungeeSpoof")) {
                    BungeeCordSpoof.enabled = object.get("BungeeSpoof").getAsBoolean();
                }
                if (!object.has("AutoReconnectDelay")) continue;
                AutoReconnect.INSTANCE.setDelay(object.get("AutoReconnectDelay").getAsInt());
                continue;
            }
            if (((String)entry.getKey()).equalsIgnoreCase("thealtening")) {
                object = (JsonObject)entry.getValue();
                if (!object.has("API-Key")) continue;
                GuiTheAltening.Companion.setApiKey(object.get("API-Key").getAsString());
                continue;
            }
            object = LiquidBounce.moduleManager.getModule((String)entry.getKey());
            if (object == null) continue;
            JsonObject jsonObject2 = (JsonObject)entry.getValue();
            ((Module)object).setState(jsonObject2.get("State").getAsBoolean());
            ((Module)object).setKeyBind(jsonObject2.get("KeyBind").getAsInt());
            if (jsonObject2.has("Array")) {
                ((Module)object).setArray(jsonObject2.get("Array").getAsBoolean());
            }
            for (Value value : ((Module)object).getValues()) {
                JsonElement jsonElement2 = jsonObject2.get(value.getName());
                if (jsonElement2 == null) continue;
                value.fromJson(jsonElement2);
            }
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("CommandPrefix", Character.valueOf(LiquidBounce.commandManager.getPrefix()));
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("TargetPlayer", Boolean.valueOf(EntityUtils.targetPlayer));
        jsonObject2.addProperty("TargetMobs", Boolean.valueOf(EntityUtils.targetMobs));
        jsonObject2.addProperty("TargetAnimals", Boolean.valueOf(EntityUtils.targetAnimals));
        jsonObject2.addProperty("TargetInvisible", Boolean.valueOf(EntityUtils.targetInvisible));
        jsonObject2.addProperty("TargetDead", Boolean.valueOf(EntityUtils.targetDead));
        jsonObject.add("targets", (JsonElement)jsonObject2);
        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.addProperty("AntiForge", Boolean.valueOf(AntiForge.enabled));
        jsonObject3.addProperty("AntiForgeFML", Boolean.valueOf(AntiForge.blockFML));
        jsonObject3.addProperty("AntiForgeProxy", Boolean.valueOf(AntiForge.blockProxyPacket));
        jsonObject3.addProperty("AntiForgePayloads", Boolean.valueOf(AntiForge.blockPayloadPackets));
        jsonObject3.addProperty("BungeeSpoof", Boolean.valueOf(BungeeCordSpoof.enabled));
        jsonObject3.addProperty("AutoReconnectDelay", (Number)AutoReconnect.INSTANCE.getDelay());
        jsonObject.add("features", (JsonElement)jsonObject3);
        JsonObject jsonObject4 = new JsonObject();
        jsonObject4.addProperty("API-Key", GuiTheAltening.Companion.getApiKey());
        jsonObject.add("thealtening", (JsonElement)jsonObject4);
        LiquidBounce.moduleManager.getModules().forEach(arg_0 -> ValuesConfig.lambda$saveConfig$1(jsonObject, arg_0));
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonObject));
        printWriter.close();
    }

    private static void lambda$null$0(JsonObject jsonObject, Value value) {
        jsonObject.add(value.getName(), value.toJson());
    }

    private static void lambda$saveConfig$1(JsonObject jsonObject, Module module) {
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("State", Boolean.valueOf(module.getState()));
        jsonObject2.addProperty("KeyBind", (Number)module.getKeyBind());
        jsonObject2.addProperty("Array", Boolean.valueOf(module.getArray()));
        module.getValues().forEach(arg_0 -> ValuesConfig.lambda$null$0(jsonObject2, arg_0));
        jsonObject.add(module.getName(), (JsonElement)jsonObject2);
    }
}

