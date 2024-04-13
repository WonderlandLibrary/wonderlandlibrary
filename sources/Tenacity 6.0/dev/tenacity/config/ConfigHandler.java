package dev.tenacity.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.tenacity.Tenacity;
import dev.tenacity.exception.InvalidConfigException;
import dev.tenacity.module.Module;
import dev.tenacity.setting.Setting;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class ConfigHandler {

    private final String location = Minecraft.getMinecraft().mcDataDir + "/Tenacity/Configs/";

    private final File defaultConfig = new File(Minecraft.getMinecraft().mcDataDir + "/Tenacity/Config.json");

    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    public ConfigHandler() {
        // If defaultConfig path does not exist, create it.
        if (!defaultConfig.getParentFile().isDirectory())
            defaultConfig.getParentFile().mkdirs();
    }

    private void serialize(final ConfigRunnable function) throws IOException {
        // To serialize the settings into a savable object, we have to loop through each module and add it to a separate list of ConfigSetting's.
        Tenacity.getInstance().getModuleRepository().getModules().forEach(module -> {
            final List<ConfigSetting> settings = new ArrayList<>();

            module.getSettingList().forEach(setting ->
                    settings.add(new ConfigSetting(setting.name, setting.getConfigValue())));
            module.setConfigSettings(settings.toArray(new ConfigSetting[0]));
        });

        function.run();
    }

    public void saveDefaultConfig() throws IOException {
        // Write each Module object stored in Tenacity's Module Repository to the defaultConfig path.
        serialize(() -> Files.write(defaultConfig.toPath(),
                gson.toJson(Tenacity.getInstance().getModuleRepository().getModules())
                        .getBytes(StandardCharsets.UTF_8)));
    }

    public void saveConfig(final String configName) throws IOException {
        // Write each Module object stored in Tenacity's Module Repository to the specified path.
        final File config = new File(location + configName + ".json");
        if(!config.exists()) {
            defaultConfig.mkdir();
            if(!config.createNewFile()) throw new IOException("Failed to create new file.");
        }
        serialize(() -> Files.write(config.toPath(),
                gson.toJson(Tenacity.getInstance().getModuleRepository().getModules())
                        .getBytes(StandardCharsets.UTF_8)));
    }

    public void loadConfig(final String configName) throws InvalidConfigException, IOException {
        // Make sure the specified config exists before attempting to load it. If it doesn't, an exception will be thrown.
        final File config = new File(location + configName + ".json");
        if (!config.isFile())
            throw new InvalidConfigException();

        loadConfig(gson.fromJson(new FileReader(config), Module[].class), false);
    }

    public void loadDefaultConfig() {
        // If the defaultConfig doesn't exist, do nothing. This will happen on the client's first launch and is completely fine to ignore.
        final Module[] modules;
        try {
            modules = gson.fromJson(new FileReader(defaultConfig), Module[].class);
        } catch (final FileNotFoundException e) {
            return;
        }

        loadConfig(modules, true);
    }

    public void loadConfig(final Module[] modules, final boolean loadBinds) {
        // Loop through all the modules in the JSON file, and the modules in Tenacity's ModuleRepository.
        for (final Module jsonModule : modules) {
            for (final Module module : Tenacity.getInstance().getModuleRepository().getModules()) {
                if (jsonModule.getName().equals(module.getName())) {

                    // Set all the data for each module stored in Tenacity's ModuleRepository to the same as the JSON module.
                    if(loadBinds)
                        module.setKeyCode(jsonModule.getKeyCode());
                    if (jsonModule.isEnabled())
                        module.setEnabled(true);

                    for (final ConfigSetting jsonSetting : jsonModule.getConfigSettings()) {
                        for (final Setting<?> setting : module.getSettingList()) {
                            if (jsonSetting.getName().equals(setting.name)) {
                                if (setting instanceof NumberSetting) {
                                    final NumberSetting numberSetting = (NumberSetting) setting;
                                    numberSetting.setCurrentValue((Double) jsonSetting.getValue());
                                }

                                if (setting instanceof BooleanSetting) {
                                    final BooleanSetting booleanSetting = (BooleanSetting) setting;
                                    booleanSetting.setState((Boolean) jsonSetting.getValue());
                                }

                                if (setting instanceof ModeSetting) {
                                    final ModeSetting modeSetting = (ModeSetting) setting;
                                    modeSetting.setCurrentMode((String) jsonSetting.getValue());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
