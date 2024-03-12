// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.config;

import com.google.gson.JsonObject;
import dev.tenacity.module.settings.impl.StringSetting;
import java.awt.Color;
import com.google.gson.JsonParser;
import dev.tenacity.module.settings.impl.ColorSetting;
import com.google.gson.internal.LinkedTreeMap;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.KeybindSetting;
import java.nio.file.Path;
import java.util.Iterator;
import dev.tenacity.module.settings.Setting;
import java.util.ArrayList;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.Tenacity;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import com.google.gson.Gson;
import java.io.File;
import java.util.List;

public class ConfigManager
{
    public static final List<LocalConfig> localConfigs;
    public static boolean loadVisuals;
    public static File defaultConfig;
    public final File file;
    private final Gson gson;
    
    public ConfigManager() {
        this.file = new File(Minecraft.getMinecraft().mcDataDir, "/Tenacity/Configs");
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    }
    
    public void collectConfigs() {
        ConfigManager.localConfigs.clear();
        this.file.mkdirs();
        Arrays.stream((Object[])Objects.requireNonNull((T[])this.file.listFiles())).forEach(f -> ConfigManager.localConfigs.add(new LocalConfig(f.getName().split("\\.")[0])));
    }
    
    public boolean saveConfig(final String name, final String content) {
        final LocalConfig localConfig = new LocalConfig(name);
        localConfig.getFile().getParentFile().mkdirs();
        try {
            Files.write(localConfig.getFile().toPath(), content.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean saveConfig(final String name) {
        return this.saveConfig(name, this.serialize());
    }
    
    public boolean delete(final String configName) {
        final List<LocalConfig> configsMatch = ConfigManager.localConfigs.stream().filter(localConfig -> localConfig.getName().equals(configName)).collect((Collector<? super Object, ?, List<LocalConfig>>)Collectors.toList());
        try {
            final LocalConfig configToDelete = configsMatch.get(0);
            Files.deleteIfExists(configToDelete.getFile().toPath());
        }
        catch (IOException | IndexOutOfBoundsException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            NotificationManager.post(NotificationType.WARNING, "Config Manager", "Failed to delete config!");
            return false;
        }
        return true;
    }
    
    public void saveDefaultConfig() {
        ConfigManager.defaultConfig.getParentFile().mkdirs();
        try {
            Files.write(ConfigManager.defaultConfig.toPath(), this.serialize().getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save " + ConfigManager.defaultConfig);
        }
    }
    
    public String serialize() {
        for (final Module module : Tenacity.INSTANCE.getModuleCollection().getModules()) {
            if (module.getCategory().equals(Category.SCRIPTS)) {
                continue;
            }
            final List<ConfigSetting> settings = new ArrayList<ConfigSetting>();
            for (final Setting setting : module.getSettingsList()) {
                final ConfigSetting cfgSetting = new ConfigSetting(null, null);
                cfgSetting.name = setting.name;
                cfgSetting.value = setting.getConfigValue();
                settings.add(cfgSetting);
            }
            module.cfgSettings = settings.toArray(new ConfigSetting[0]);
        }
        return this.gson.toJson((Object)Tenacity.INSTANCE.getModuleCollection().getModules());
    }
    
    public String readConfigData(final Path configPath) {
        try {
            return new String(Files.readAllBytes(configPath));
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public boolean loadConfig(final String data) {
        return this.loadConfig(data, false);
    }
    
    public boolean loadConfig(final String data, final boolean keybinds) {
        final Module[] modules = (Module[])this.gson.fromJson(data, (Class)Module[].class);
        for (final Module module : Tenacity.INSTANCE.getModuleCollection().getModules()) {
            if (!keybinds && !ConfigManager.loadVisuals && module.getCategory().equals(Category.RENDER)) {
                continue;
            }
            if (module.getCategory().equals(Category.SCRIPTS)) {
                continue;
            }
            for (final Module configModule : modules) {
                if (module.getName().equalsIgnoreCase(configModule.getName())) {
                    try {
                        if (module.isEnabled() != configModule.isEnabled()) {
                            module.toggleSilent();
                        }
                        for (final Setting setting : module.getSettingsList()) {
                            for (final ConfigSetting cfgSetting : configModule.cfgSettings) {
                                if (setting.name.equals(cfgSetting.name)) {
                                    if (setting instanceof KeybindSetting && keybinds) {
                                        final KeybindSetting keybindSetting = (KeybindSetting)setting;
                                        keybindSetting.setCode(Double.valueOf(String.valueOf(cfgSetting.value)).intValue());
                                    }
                                    if (setting instanceof BooleanSetting) {
                                        ((BooleanSetting)setting).setState(Boolean.parseBoolean(String.valueOf(cfgSetting.value)));
                                    }
                                    if (setting instanceof ModeSetting) {
                                        final String value = String.valueOf(cfgSetting.value);
                                        final ModeSetting ms = (ModeSetting)setting;
                                        if (ms.modes.contains(value)) {
                                            ms.setCurrentMode(value);
                                        }
                                        else {
                                            ms.setCurrentMode(ms.modes.get(0));
                                            Tenacity.LOGGER.info(String.format("The value of setting %s in module %s was reset", ms.name, module.getName()));
                                        }
                                    }
                                    if (setting instanceof NumberSetting) {
                                        final NumberSetting ss = (NumberSetting)setting;
                                        double value2;
                                        try {
                                            value2 = Double.parseDouble(String.valueOf(cfgSetting.value));
                                        }
                                        catch (NumberFormatException e2) {
                                            value2 = ss.getDefaultValue();
                                            Tenacity.LOGGER.info(String.format("The value of setting %s in module %s was reset", ss.name, module.getName()));
                                        }
                                        ss.setValue(value2);
                                    }
                                    if (setting instanceof MultipleBoolSetting) {
                                        final LinkedTreeMap<String, Boolean> boolMap = (LinkedTreeMap<String, Boolean>)cfgSetting.value;
                                        final MultipleBoolSetting mbs = (MultipleBoolSetting)setting;
                                        for (final String s : boolMap.keySet()) {
                                            final BooleanSetting childSetting = mbs.getSetting(s);
                                            if (childSetting != null && boolMap.get((Object)s) != null) {
                                                childSetting.setState((boolean)boolMap.get((Object)s));
                                            }
                                        }
                                    }
                                    if (setting instanceof ColorSetting) {
                                        final ColorSetting colorSetting = (ColorSetting)setting;
                                        if (JsonParser.parseString(cfgSetting.value.toString()).isJsonObject()) {
                                            final JsonObject colorObject = JsonParser.parseString(cfgSetting.value.toString()).getAsJsonObject();
                                            colorSetting.setRainbow(true);
                                            final float saturation = colorObject.get("saturation").getAsFloat();
                                            final int speed = colorObject.get("speed").getAsInt();
                                            colorSetting.getRainbow().setSaturation(saturation);
                                            colorSetting.getRainbow().setSpeed(speed);
                                        }
                                        else {
                                            final int color = Double.valueOf(String.valueOf(cfgSetting.value)).intValue();
                                            final Color c = new Color(color);
                                            final float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                                            colorSetting.setColor(hsb[0], hsb[1], hsb[2]);
                                        }
                                    }
                                    if (setting instanceof StringSetting) {
                                        final String value = String.valueOf(cfgSetting.value);
                                        if (value != null) {
                                            ((StringSetting)setting).setString(value);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }
    
    static {
        localConfigs = new ArrayList<LocalConfig>();
    }
}
