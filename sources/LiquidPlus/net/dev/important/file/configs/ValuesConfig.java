/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package net.dev.important.file.configs;

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
import net.dev.important.Client;
import net.dev.important.file.FileConfig;
import net.dev.important.file.FileManager;
import net.dev.important.gui.client.GuiBackground;
import net.dev.important.gui.client.altmanager.sub.altgenerator.GuiTheAltening;
import net.dev.important.modules.command.other.AntiForge;
import net.dev.important.modules.command.other.AutoReconnect;
import net.dev.important.modules.command.other.BungeeCordSpoof;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.EntityUtils;
import net.dev.important.value.Value;

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
            JsonObject jsonValue;
            if (((String)entry.getKey()).equalsIgnoreCase("CommandPrefix")) {
                Client.commandManager.setPrefix(((JsonElement)entry.getValue()).getAsCharacter());
                continue;
            }
            if (((String)entry.getKey()).equalsIgnoreCase("ShowRichPresence")) {
                Client.clientPresence.setShowRichPresenceValue(((JsonElement)entry.getValue()).getAsBoolean());
                continue;
            }
            if (((String)entry.getKey()).equalsIgnoreCase("targets")) {
                jsonValue = (JsonObject)entry.getValue();
                if (jsonValue.has("TargetPlayer")) {
                    EntityUtils.targetPlayer = jsonValue.get("TargetPlayer").getAsBoolean();
                }
                if (jsonValue.has("TargetMobs")) {
                    EntityUtils.targetMobs = jsonValue.get("TargetMobs").getAsBoolean();
                }
                if (jsonValue.has("TargetAnimals")) {
                    EntityUtils.targetAnimals = jsonValue.get("TargetAnimals").getAsBoolean();
                }
                if (jsonValue.has("TargetInvisible")) {
                    EntityUtils.targetInvisible = jsonValue.get("TargetInvisible").getAsBoolean();
                }
                if (!jsonValue.has("TargetDead")) continue;
                EntityUtils.targetDead = jsonValue.get("TargetDead").getAsBoolean();
                continue;
            }
            if (((String)entry.getKey()).equalsIgnoreCase("features")) {
                jsonValue = (JsonObject)entry.getValue();
                if (jsonValue.has("AntiForge")) {
                    AntiForge.enabled = jsonValue.get("AntiForge").getAsBoolean();
                }
                if (jsonValue.has("AntiForgeFML")) {
                    AntiForge.blockFML = jsonValue.get("AntiForgeFML").getAsBoolean();
                }
                if (jsonValue.has("AntiForgeProxy")) {
                    AntiForge.blockProxyPacket = jsonValue.get("AntiForgeProxy").getAsBoolean();
                }
                if (jsonValue.has("AntiForgePayloads")) {
                    AntiForge.blockPayloadPackets = jsonValue.get("AntiForgePayloads").getAsBoolean();
                }
                if (jsonValue.has("BungeeSpoof")) {
                    BungeeCordSpoof.enabled = jsonValue.get("BungeeSpoof").getAsBoolean();
                }
                if (!jsonValue.has("AutoReconnectDelay")) continue;
                AutoReconnect.INSTANCE.setDelay(jsonValue.get("AutoReconnectDelay").getAsInt());
                continue;
            }
            if (((String)entry.getKey()).equalsIgnoreCase("thealtening")) {
                jsonValue = (JsonObject)entry.getValue();
                if (!jsonValue.has("API-Key")) continue;
                GuiTheAltening.Companion.setApiKey(jsonValue.get("API-Key").getAsString());
                continue;
            }
            if (((String)entry.getKey()).equalsIgnoreCase("Background")) {
                jsonValue = (JsonObject)entry.getValue();
                if (jsonValue.has("Enabled")) {
                    GuiBackground.Companion.setEnabled(jsonValue.get("Enabled").getAsBoolean());
                }
                if (!jsonValue.has("Particles")) continue;
                GuiBackground.Companion.setParticles(jsonValue.get("Particles").getAsBoolean());
                continue;
            }
            Module module2 = Client.moduleManager.getModule((String)entry.getKey());
            if (module2 == null) continue;
            JsonObject jsonModule = (JsonObject)entry.getValue();
            for (Value<?> moduleValue : module2.getValues()) {
                JsonElement element = jsonModule.get(moduleValue.getName());
                if (element == null) continue;
                moduleValue.fromJson(element);
            }
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("CommandPrefix", Character.valueOf(Client.commandManager.getPrefix()));
        jsonObject.addProperty("ShowRichPresence", Boolean.valueOf(Client.clientPresence.getShowRichPresenceValue()));
        JsonObject jsonTargets = new JsonObject();
        jsonTargets.addProperty("TargetPlayer", Boolean.valueOf(EntityUtils.targetPlayer));
        jsonTargets.addProperty("TargetMobs", Boolean.valueOf(EntityUtils.targetMobs));
        jsonTargets.addProperty("TargetAnimals", Boolean.valueOf(EntityUtils.targetAnimals));
        jsonTargets.addProperty("TargetInvisible", Boolean.valueOf(EntityUtils.targetInvisible));
        jsonTargets.addProperty("TargetDead", Boolean.valueOf(EntityUtils.targetDead));
        jsonObject.add("targets", (JsonElement)jsonTargets);
        JsonObject jsonFeatures = new JsonObject();
        jsonFeatures.addProperty("AntiForge", Boolean.valueOf(AntiForge.enabled));
        jsonFeatures.addProperty("AntiForgeFML", Boolean.valueOf(AntiForge.blockFML));
        jsonFeatures.addProperty("AntiForgeProxy", Boolean.valueOf(AntiForge.blockProxyPacket));
        jsonFeatures.addProperty("AntiForgePayloads", Boolean.valueOf(AntiForge.blockPayloadPackets));
        jsonFeatures.addProperty("BungeeSpoof", Boolean.valueOf(BungeeCordSpoof.enabled));
        jsonFeatures.addProperty("AutoReconnectDelay", (Number)AutoReconnect.INSTANCE.getDelay());
        jsonObject.add("features", (JsonElement)jsonFeatures);
        JsonObject theAlteningObject = new JsonObject();
        theAlteningObject.addProperty("API-Key", GuiTheAltening.Companion.getApiKey());
        jsonObject.add("thealtening", (JsonElement)theAlteningObject);
        JsonObject backgroundObject = new JsonObject();
        backgroundObject.addProperty("Enabled", Boolean.valueOf(GuiBackground.Companion.getEnabled()));
        backgroundObject.addProperty("Particles", Boolean.valueOf(GuiBackground.Companion.getParticles()));
        jsonObject.add("Background", (JsonElement)backgroundObject);
        Client.moduleManager.getModules().stream().filter(module2 -> !module2.getValues().isEmpty()).forEach(module2 -> {
            JsonObject jsonModule = new JsonObject();
            module2.getValues().forEach(value -> jsonModule.add(value.getName(), value.toJson()));
            jsonObject.add(module2.getName(), (JsonElement)jsonModule);
        });
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonObject));
        printWriter.close();
    }
}

