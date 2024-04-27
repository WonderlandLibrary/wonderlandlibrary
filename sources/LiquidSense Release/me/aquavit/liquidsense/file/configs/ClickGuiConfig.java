package me.aquavit.liquidsense.file.configs;

import com.google.gson.*;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.file.FileConfig;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.hud.Config;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.value.Value;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class ClickGuiConfig extends FileConfig {
    /**
     * Constructor of config
     *
     * @param file of config
     */
    public ClickGuiConfig(final File file) {
        super(file);
    }

    /**
     * Load config from file
     *
     * @throws IOException
     */
    @Override
    protected void loadConfig() throws IOException {
        try {
            File file = getFile();
            if (file != null && file.exists()) {
                String fileContent = FileUtils.readFileToString(file);
                if (fileContent != null) {
                    JsonArray jsonArray = new Gson().fromJson(fileContent, JsonArray.class);
                    if (jsonArray != null) {
                        for (final JsonElement jsonElement : jsonArray) {
                            try {
                                if (jsonElement == null || jsonElement instanceof JsonNull || !jsonElement.isJsonObject()) {
                                    continue;
                                }

                                final JsonObject jsonObject = jsonElement.getAsJsonObject();

                                if (jsonObject == null) continue;

                                for (Module module : LiquidSense.moduleManager.getModules()) {
                                    if (jsonObject.has("Type") && module.getName().equals(jsonObject.get("Type").getAsString())) {
                                        module.setRenderx(jsonObject.get("X").getAsInt());
                                        module.setRendery(jsonObject.get("Y").getAsInt());

                                        for (Value<?> value : module.getValues()) {
                                            if (jsonObject.has(value.getName()))
                                                value.fromJson(jsonObject.get(value.getName()));
                                        }

                                        module.setUseConfig(true);
                                    }

                                    if (!module.getUseConfig()) setDefaultConfig(module);
                                }

                            } catch (Exception e) {
                                ClientUtils.getLogger().error("Error while loading hud from config.", e);
                            }
                        }
                    } else {
                        for (Module module : LiquidSense.moduleManager.getModules()) {
                            setDefaultConfig(module);
                        }
                        ClientUtils.getLogger().info("Can't load hud config, default hud config has been loaded");
                    }
                }
            }

        } catch (Exception e) {
            ClientUtils.getLogger().error("Error while loading hud config.", e);
        }

    }



    private void setDefaultConfig(Module module){
        if (module.getCategory() == ModuleCategory.HUD){
            module.setRenderx(module.getDefaultx());
            module.setRendery(module.getDefaulty());
        }
    }

    /**
     * Save config to file
     *
     * @throws IOException
     */
    @Override
    protected void saveConfig() throws IOException {
        final PrintWriter printWriter = new PrintWriter(new FileWriter(getFile()));
        printWriter.println(new Config().toJson());
        printWriter.close();
    }
}
