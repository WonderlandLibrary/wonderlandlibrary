package de.tired.base.config;


import com.google.gson.*;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.guis.newclickgui.setting.Setting;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.interfaces.IHook;
import de.tired.base.module.Module;
import de.tired.Tired;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ConfigManager implements IHook {

    private static final ArrayList<Config> CONFIGS = new ArrayList<>();
    private static File CONFIG_DIRECTORY;
    private static File CONFIG_DIRECTORYONLINE;
    private Config activeConfig;


    public void init() {
        CONFIG_DIRECTORY = new File(MC.mcDataDir + "/" + Tired.INSTANCE.CLIENT_NAME, "configs");
        CONFIG_DIRECTORYONLINE = new File(MC.mcDataDir + "/" + Tired.INSTANCE.CLIENT_NAME, "onlineconfigs");
        if (!CONFIG_DIRECTORY.exists()) CONFIG_DIRECTORY.mkdirs();
        CONFIGS.clear();
        for (File file : Objects.requireNonNull(CONFIG_DIRECTORY.listFiles())) {
            CONFIGS.add(new Config(file.getName().replace(".json", "")));
        }

    }

    public boolean create(Config config) {
        CONFIGS.add(config);
        return save(config);
    }

    public boolean loadNoSet(Config config) {
        try {
            activeConfig = config;
            if (config != null) {

                return config.loadOnlyNoSet(new JsonParser().parse(new FileReader(CONFIG_DIRECTORY + "/" + config.name().replace("Online", "") + ".json")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadConfigOnline(String configName) {
        new Thread(() -> {
            for (String configs : listConfigsOnline()) {
                final List<Module> antiStackOverflow = Tired.INSTANCE.moduleManager.getModuleList();
                if (configs.equalsIgnoreCase(configName)) {
                    try {
                        final URL url = new URL("https://raw.githubusercontent.com/FelixH2012/NextGen-Configs/main/" + configName + ".json");

                        System.out.println(url);
                        final Scanner scanner = new Scanner(url.openStream(), "UTF-8");

                        final StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while (scanner.hasNextLine()) {
                            line = scanner.nextLine();
                            stringBuilder.append(line).append("\n");
                            final String[] args = line.split(":");


                            System.out.println(Arrays.toString(args));
                            final JsonElement jsonElement = new JsonParser().parse(args[0]);
                            for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                                for (final Module module : antiStackOverflow) {
                                    if (entry.getKey().equalsIgnoreCase(module.getName())) {
                                        final JsonObject jsonModule = (JsonObject) entry.getValue();
                                        if (!module.getName().equalsIgnoreCase("configs")) {
                                            if (module.isState() != jsonModule.get("State").getAsBoolean()) {
                                                module.executeMod();
                                            }

//                            module.setKey(jsonModule.get("Keybind").getAsInt());

                                            JsonObject settings = jsonModule.get("Settings").getAsJsonObject();
                                            for (Map.Entry<String, JsonElement> setting : settings.entrySet()) {
                                                if (Tired.INSTANCE.settingsManager.settingBy(setting.getKey(), module.getName()) != null) {
                                                    Setting set = Tired.INSTANCE.settingsManager.settingBy(setting.getKey(), module.getName());

                                                    if (set instanceof BooleanSetting) {
                                                        ((BooleanSetting) set).setValue(setting.getValue().getAsBoolean());
                                                    }

                                                    if (set instanceof ModeSetting) {
                                                        ((ModeSetting) set).setValue(setting.getValue().getAsString());
                                                        if (Arrays.asList(((ModeSetting) set).getOptions()).contains(setting.getValue().getAsString())) {
                                                            ((ModeSetting) set).setModeIndex(Arrays.asList(((ModeSetting) set).getOptions()).indexOf(setting.getValue().getAsString()));
                                                        }
                                                    }
                                                    if (set instanceof NumberSetting) {
                                                        ((NumberSetting) set).setValue(setting.getValue().getAsDouble());
                                                    }

                                                } else {
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    public List<String> listConfigsOnline() {
        URL url;
        List<String> result = new ArrayList<>();
        try {
            url = new URL("https://github.com/FelixH2012/NextGen-Configs");
            final Scanner scanner = new Scanner(url.openStream());
            String line;
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                if (line.contains("json")) {
                    result.add(line.replaceAll("\\<.*?\\>", "").replace(" ", "").replace(".json", ""));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public boolean loadOnline(Config config) {
        try {
            activeConfig = config;
            if (config != null) {
                return config.deserialize(new JsonParser().parse(new FileReader(CONFIG_DIRECTORYONLINE + "/" + config.name().replace("Online", "") + ".json")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean load(Config config) {
        try {
            activeConfig = config;
            if (config != null) {
                loadOnline(config);
                return config.deserialize(new JsonParser().parse(new FileReader(CONFIG_DIRECTORY + "/" + config.name().replace("Online", "") + ".json")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean load(String name) {
        Config config = configBy(name);
        if (config == null) {
            System.out.println("Cannot find " + config.name());
            return false;
        }
        return load(config);
    }

    public boolean save(Config config) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fileWriter = new FileWriter(CONFIG_DIRECTORY + "\\" + config.name() + ".json");
            gson.toJson(config.serialize(), fileWriter);
            fileWriter.close();
            System.out.println("Saved " + config.name());
            return true;
        } catch (Exception e) {
            System.out.println("Failed to save " + config.name());
            e.printStackTrace();
        }
        return false;
    }

    public boolean save(String name) {
        Config config = configBy(name);
        if (config == null) {
            System.out.println("Cannot find " + config.name());
            return false;
        }
        return save(config);
    }

    public Config configBy(String name) {
        for (Config config : CONFIGS) {
            if (config.name().equalsIgnoreCase(name)) return config;
        }
        return null;
    }

    public boolean delete(String configname) {
        for (File file : Objects.requireNonNull(CONFIG_DIRECTORY.listFiles())) {
            if (file.getName().equalsIgnoreCase(configname)) {
                if (file.exists()) {
                    return file.delete();

                }
            }
        }
        return false;
    }

    public ArrayList<Config> configs() {
        return CONFIGS;
    }

    public Config activeConfig() {
        return activeConfig;
    }
}