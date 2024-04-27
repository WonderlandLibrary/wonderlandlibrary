package me.aquavit.liquidsense.file.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.ui.client.gui.elements.AntiForge;
import me.aquavit.liquidsense.ui.client.gui.elements.BungeeCordSpoof;
import me.aquavit.liquidsense.file.FileConfig;
import me.aquavit.liquidsense.file.FileManager;
import me.aquavit.liquidsense.ui.client.gui.GuiBackground;
import me.aquavit.liquidsense.value.Value;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class ValuesConfig extends FileConfig {

    /**
     * Constructor of config
     *
     * @param file of config
     */
    public ValuesConfig(final File file) {
        super(file);
    }

    /**
     * Load config from file
     *
     * @throws IOException
     */
    @Override
    protected void loadConfig() throws IOException {
        final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(getFile())));

        if(jsonElement instanceof JsonNull)
            return;

        final JsonObject jsonObject = (JsonObject) jsonElement;

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("CommandPrefix")) {
                LiquidSense.commandManager.setPrefix(entry.getValue().getAsCharacter());
            } else if (entry.getKey().equalsIgnoreCase("features")) {
                JsonObject jsonValue = (JsonObject) entry.getValue();

                if (jsonValue.has("AntiForge"))
                    AntiForge.enabled = jsonValue.get("AntiForge").getAsBoolean();
                if (jsonValue.has("AntiForgeFML"))
                    AntiForge.blockFML = jsonValue.get("AntiForgeFML").getAsBoolean();
                if (jsonValue.has("AntiForgeProxy"))
                    AntiForge.blockProxyPacket = jsonValue.get("AntiForgeProxy").getAsBoolean();
                if (jsonValue.has("AntiForgePayloads"))
                    AntiForge.blockPayloadPackets = jsonValue.get("AntiForgePayloads").getAsBoolean();
                if (jsonValue.has("BungeeSpoof"))
                    BungeeCordSpoof.enabled = jsonValue.get("BungeeSpoof").getAsBoolean();
            } else if (entry.getKey().equalsIgnoreCase("Background")) {
                JsonObject jsonValue = (JsonObject) entry.getValue();

                if (jsonValue.has("Enabled"))
                    GuiBackground.Companion.setEnabled(jsonValue.get("Enabled").getAsBoolean());

                if (jsonValue.has("Particles"))
                    GuiBackground.Companion.setParticles(jsonValue.get("Particles").getAsBoolean());
            } else {
                final Module module = LiquidSense.moduleManager.getModule(entry.getKey());

                if (module != null && module.getCategory() != ModuleCategory.HUD) {
                    final JsonObject jsonModule = (JsonObject) entry.getValue();

                    for (final Value moduleValue : module.getValues()) {
                        final JsonElement element = jsonModule.get(moduleValue.getName());

                        if (element != null) moduleValue.fromJson(element);
                    }
                }
            }
        }
    }

    /**
     * Save config to file
     *
     * @throws IOException
     */
    @Override
    protected void saveConfig() throws IOException {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("CommandPrefix", LiquidSense.commandManager.getPrefix());

        final JsonObject jsonFeatures = new JsonObject();
        jsonFeatures.addProperty("AntiForge", AntiForge.enabled);
        jsonFeatures.addProperty("AntiForgeFML", AntiForge.blockFML);
        jsonFeatures.addProperty("AntiForgeProxy", AntiForge.blockProxyPacket);
        jsonFeatures.addProperty("AntiForgePayloads", AntiForge.blockPayloadPackets);
        jsonFeatures.addProperty("BungeeSpoof", BungeeCordSpoof.enabled);
        jsonObject.add("features", jsonFeatures);

        final JsonObject backgroundObject = new JsonObject();
        backgroundObject.addProperty("Enabled", GuiBackground.Companion.getEnabled());
        backgroundObject.addProperty("Particles", GuiBackground.Companion.getParticles());
        jsonObject.add("Background", backgroundObject);

        LiquidSense.moduleManager.getModules().stream().filter(module -> module.getCategory() != ModuleCategory.HUD && !module.getValues().isEmpty()).forEach(module -> {
            final JsonObject jsonModule = new JsonObject();
            module.getValues().forEach(value -> jsonModule.add(value.getName(), value.toJson()));
            jsonObject.add(module.getName(), jsonModule);
        });

        final PrintWriter printWriter = new PrintWriter(new FileWriter(getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson(jsonObject));
        printWriter.close();
    }
}
