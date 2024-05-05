package Velo.api.Module.Config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.ChatUtil;
import Velo.api.setting.Setting;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.KeybindSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager { //TODO ask user if visuals should be imported from config

    private static final List<Config> configs = new ArrayList<>();
    public final File file = new File(Minecraft.getMinecraft().mcDataDir, "/Velo/configs");
    public final File file1 = new File(Minecraft.getMinecraft().mcDataDir, "/Velo/theme");
    public File config = new File(Minecraft.getMinecraft().mcDataDir + "/Velo/Config.json");
    String[] pathnames;

    public ConfigManager() {
        file.mkdirs();
    }

    public static Config getConfigByName(String name) {
        for (Config config : configs) {
            if (config.getName().equalsIgnoreCase(name)) return config;
        }
        return null;
    }

    public boolean load(String name) {
        Config config = new Config(name);
        try {
            String configString = new String(Files.readAllBytes(config.getFile().toPath()));
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            Module[] modules = gson.fromJson(configString, Module[].class);

            for (Module module : ModuleManager.modules) {
                for (Module configModule : modules) {
                    if (module.getName().equals(configModule.getName())) {
                        try {
                            if (configModule.isToggled() && !module.isToggled())
                                module.toggle();
                            else if (!configModule.isToggled() && module.isToggled())
                                module.setToggled(false);

            

                            for (Setting setting : module.settings) {
                                for (Cfg cfgSetting : configModule.cfgSettings) {
                                    if (setting.name.equals(cfgSetting.name)) {
                                        if (setting instanceof BooleanSetting) {
                                            ((BooleanSetting) setting).setEnabled((boolean) cfgSetting.setting);
                                        }
                                        if (setting instanceof ModeSetting) {
                                            ((ModeSetting) setting).setMode((String) cfgSetting.setting);
                                        }
                                        if (setting instanceof NumberSetting) {
                                            ((NumberSetting) setting).setValue((double) cfgSetting.setting);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean loadConfig() {
        try {
            String configString = new String(Files.readAllBytes(config.toPath()));
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            Module[] modules = gson.fromJson(configString, Module[].class);

            for (Module module : ModuleManager.modules) {
                for (Module configModule : modules) {
                    if (module.getName().equals(configModule.getName())) {
                        try {
                            if (configModule.isToggled() && !module.isToggled())
                                module.toggle();
                            else if (!configModule.isToggled() && module.isToggled())
                                module.setToggled(false);


                            for (Setting setting : module.settings) {
                                for (Cfg cfgSetting : configModule.cfgSettings) {
                                    if (setting.name.equals(cfgSetting.name)) {
                                        if (setting instanceof BooleanSetting) {
                                            ((BooleanSetting) setting).setEnabled((boolean) cfgSetting.setting);
                                        }
                                        if (setting instanceof ModeSetting) {
                                            ((ModeSetting) setting).setMode((String) cfgSetting.setting);
                                        }
                                        if (setting instanceof NumberSetting) {
                                            ((NumberSetting) setting).setValue((double) cfgSetting.setting);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean save(String name) {
        Config config = new Config(name);

        try {
       
            config.getFile().getParentFile().mkdirs();
            Files.write(config.getFile().toPath(), config.serialize().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save " + config);
            return false;
        }
    }

    public void saveConfig() {
        try {
            config.getParentFile().mkdirs();
            Files.write(config.toPath(), serialize().getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save " + config);
        }
    }

    public String serialize() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        for (Module module : ModuleManager.modules) {
            List<Cfg> settings = new ArrayList<>();
            for (Setting setting : module.settings) {
                if (setting instanceof KeybindSetting)
                    continue;

                Cfg cfg = new Cfg(null, null);
                cfg.name = setting.name;
            
            if (setting instanceof ModeSetting) {
                cfg.setting = ((ModeSetting) setting).getSelected();
            }
            if (setting instanceof NumberSetting) {
                cfg.setting = ((NumberSetting) setting).getValue();
            }
                if (setting instanceof BooleanSetting) {
                    cfg.setting = ((BooleanSetting) setting).isEnabled();
                }
                
   

                settings.add(cfg);
            }
            module.cfgSettings = settings.toArray(new Cfg[0]);
        }
        return gson.toJson(ModuleManager.modules);
    }

    public boolean save(Config config) {
        return this.save(config);
    }



    public void loadConfigs() {
        for (File file : file.listFiles()) {
            configs.add(new Config(file.getName().replace(".json", "")));
        }
    }

    public List<Config> getConfigs() {
        return configs;
    }
    public void saveAll() {
        configs.forEach(config -> save(config.getName()));
    }
    public void list() {
        pathnames = file.list();
        for (String pathname : pathnames) {
            ChatUtil.addChatMessage(pathname.substring(0, pathname.length()));
        }
    }

    public void delete(String configName) {
        Config config = new Config(configName);
        try {
            Files.deleteIfExists(config.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
