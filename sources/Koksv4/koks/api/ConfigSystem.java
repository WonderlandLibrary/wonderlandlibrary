package koks.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import koks.Koks;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.manager.value.Value;
import koks.api.manager.value.ValueManager;
import net.minecraft.util.Tuple;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
public class ConfigSystem implements Methods {

    public static final File DIR = new File(Koks.getKoks().DIR, "Configs");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public JsonObject toConfigToJson(String creator) {
        JsonObject configJsonObject = new JsonObject();
        configJsonObject.addProperty("Creator", creator);
        configJsonObject.addProperty("Date", System.currentTimeMillis());
        for (Module module : ModuleRegistry.getModules()) {
            if (isValid(module)) {
                JsonObject moduleJsonObject = new JsonObject();
                moduleJsonObject.addProperty("toggled", module.isToggled());
                moduleJsonObject.addProperty("bypass", module.isBypass());
                JsonObject valueJsonObject = new JsonObject();
                for (Value<?> value : ValueManager.getInstance().getValues(module.getClass())) {
                    valueJsonObject.addProperty(value.getName(), String.valueOf(value.getValue()));
                }
                moduleJsonObject.add("values", valueJsonObject);
                configJsonObject.add(module.getName(), moduleJsonObject);
            }
        }
        return configJsonObject;
    }

    /**
     * @param json The json of the config
     * @return Tuble<The creator of the config, creation date as ms>
     */
    public Tuple<String, Long> fromJsonToConfig(String json) {
        JsonObject configJsonObject = GSON.fromJson(json, JsonObject.class);
        for (Module module : ModuleRegistry.getModules()) {
            if (isValid(module)) {
                if (configJsonObject.has(module.getName())) {
                    JsonObject moduleJsonObject = configJsonObject.getAsJsonObject(module.getName());
                    if (moduleJsonObject.has("toggled")) {
                        if (!(module instanceof Module.NotUnToggle) && !(module instanceof Module.Unsafe)) {
                            if (module.isToggled() != moduleJsonObject.get("toggled").getAsBoolean()) {
                                module.setToggled(moduleJsonObject.get("toggled").getAsBoolean());
                            }
                        } else if (!(module instanceof Module.NotUnToggle)) {
                            if (module.isToggled())
                                module.setToggled(false);
                        }
                    }
                    if (moduleJsonObject.has("bypass")) {
                        module.setBypass(moduleJsonObject.get("bypass").getAsBoolean());
                    }
                    if (moduleJsonObject.has("values")) {
                        JsonObject valueJsonObject = moduleJsonObject.getAsJsonObject("values");
                        for (Value<?> value : ValueManager.getInstance().getValues(module.getClass())) {
                            if (valueJsonObject.has(value.getName()) && !value.isVisual()) {
                                value.castSavedIfPossible(valueJsonObject.get(value.getName()).getAsString());
                                value.castIfPossible(valueJsonObject.get(value.getName()).getAsString());
                            }
                        }
                    }
                }
            }
        }
        return new Tuple<>(configJsonObject.has("Creator") ? configJsonObject.get("Creator").getAsString() : "unknown", configJsonObject.has("Date") ? configJsonObject.get("Date").getAsLong() : LocalDate.of(2001, Month.SEPTEMBER, 11).toEpochDay());
    }

    public void createConfig(String name, String creator) throws IOException {
        if (!DIR.exists())
            DIR.mkdirs();

        final File config = new File(DIR, name + ".koks");
        config.createNewFile();

        final FileWriter fw = new FileWriter(config);
        fw.write(GSON.toJson(toConfigToJson(creator)));
        fw.close();
    }

    public void loadConfig(String name) {
        if (!DIR.exists())
            DIR.mkdirs();

        new Thread(() -> {
            try {
                resetModules();
                resetValues();

                final File config = new File(DIR, name + ".koks");
                if (config.exists()) {
                    StringBuilder stringBuilder = new StringBuilder();

                    final BufferedReader br = new BufferedReader(new FileReader(config));
                    boolean oldConfig = false, jsonConfig = false;
                    String line;
                    while ((line = br.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                        if (line.contains("Module:") || line.contains("Value:")) {
                            if (!oldConfig) {
                                sendMessage("Old Config! This config will be converted afterwards!");
                            }
                            oldConfig = true;
                        } else {
                            jsonConfig = true;
                            continue;
                        }
                        final String[] args = line.split(":");
                        if (args.length == 4) {
                            final Module module = ModuleRegistry.getModule(args[1]);
                            if (isValid(module)) {
                                switch (args[0]) {
                                    case "Module":
                                        if (!(module instanceof Module.NotUnToggle) && !(module instanceof Module.Unsafe)) {
                                            if (module.isToggled() != Boolean.parseBoolean(args[2]))
                                                module.setToggled(Boolean.parseBoolean(args[2]));
                                        } else if (!(module instanceof Module.NotUnToggle)) {
                                            if (module.isToggled())
                                                module.setToggled(false);
                                        }
                                        module.setBypass(Boolean.parseBoolean(args[3]));
                                        break;
                                    case "Value":
                                        final Value<?> value = ValueManager.getInstance().getValue(args[2], module);
                                        if (value != null && !value.isVisual()) {
                                            value.castSavedIfPossible(args[3]);
                                            value.castIfPossible(args[3]);
                                            break;
                                        }
                                }
                            } else if (module != null) {
                                module.setBypass(false);
                            }
                        }
                    }
                    if (oldConfig) {
                        sendMessage("Converting to new config format!");
                        createConfig(name, "unknown");
                    }
                    String addition = "";
                    if (jsonConfig) {
                        Tuple<String, Long> stringLongTuple = fromJsonToConfig(stringBuilder.toString());
                        addition += "(Created by " + stringLongTuple.getFirst() + " on " + new SimpleDateFormat((mc.getLanguageManager().getCurrentLanguage().getLanguageCode().equals("de_DE") ? "dd.MM" : "MM.dd") + ".yyyy").format(new Date(stringLongTuple.getSecond())) + ") ";
                    }
                    br.close();
                    sendMessage("Loaded §e" + name + " §7(§aLocal§7) " + addition);
                } else {
                    sendError("Config not found", name + " not exist!");
                }
            } catch (IOException e) {
                sendError("Config not found", name + " not exist!");
            }
        }).start();
    }

    public void loadOnline(String name) {
        new Thread(() -> {
            try {
                String configName = name;
                for (String configs : configList())
                    if (configs.equalsIgnoreCase(configName))
                        configName = configs;
                final URL url = new URL("https://raw.githubusercontent.com/Koks-Team/Koks-Configs/v4/" + configName + ".koks");

                final Scanner scanner = new Scanner(url.openStream(), "UTF-8");

                resetModules();
                resetValues();

                StringBuilder stringBuilder = new StringBuilder();
                boolean oldConfig = false, jsonConfig = false;
                String line;
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    stringBuilder.append(line).append("\n");
                    if (line.contains("Module:") || line.contains("Value:")) {
                        if (!oldConfig) {
                            sendMessage("Warning this config is old, it's probably outdated!");
                        }
                        oldConfig = true;
                    } else {
                        jsonConfig = true;
                        continue;
                    }

                    final String[] args = line.split(":");

                    if (args.length == 4) {
                        final Module module = ModuleRegistry.getModule(args[1]);

                        if (isValid(module)) {
                            switch (args[0]) {
                                case "Module":
                                    if (!(module instanceof Module.NotUnToggle) && !(module instanceof Module.Unsafe)) {
                                        if (module.isToggled() != Boolean.parseBoolean(args[2]))
                                            module.setToggled(Boolean.parseBoolean(args[2]));
                                    } else if (!(module instanceof Module.NotUnToggle)) {
                                        if (module.isToggled())
                                            module.setToggled(false);
                                    }
                                    module.setBypass(Boolean.parseBoolean(args[3]));
                                    break;
                                case "Value":
                                    final Value<?> value = ValueManager.getInstance().getValue(args[2], module);
                                    if (value != null && !value.isVisual()) {
                                        value.castSavedIfPossible(args[3]);
                                        value.castIfPossible(args[3]);
                                    }
                                    break;
                            }
                        } else if (module != null) {
                            module.setBypass(false);
                        }
                    }
                }
                String addition = "";
                if (jsonConfig) {
                    Tuple<String, Long> stringLongTuple = fromJsonToConfig(stringBuilder.toString());
                    addition += "(Created by " + stringLongTuple.getFirst() + " on " + new SimpleDateFormat((mc.getLanguageManager().getCurrentLanguage().getLanguageCode().equals("de_DE") ? "dd.MM" : "MM.dd") + ".yyyy").format(new Date(stringLongTuple.getSecond())) + ") ";
                }
                scanner.close();
                sendMessage("Loaded §e" + configName + " §7(§aOnline§7)");
            } catch (IOException e) {
                sendError("Config not found", name + " not exist!");
            }
        }).start();
    }

    public List<String> configList() {
        URL url = null;
        List<String> resolut = new ArrayList<>();
        try {
            url = new URL("https://github.com/Koks-Team/Koks-Configs");
            Scanner sc = new Scanner(url.openStream());
            String line;
            while (sc.hasNext()) {
                line = sc.nextLine();
                if (line.contains("koks")) {
                    resolut.add(line.replaceAll("\\<.*?\\>", "").replace(" ", "").replace(".koks", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resolut;
    }

    private boolean isValid(Module module) {
        return module != null && !(module instanceof Module.NotBypass) && !module.getCategory().equals(Module.Category.VISUAL) && !module.getCategory().equals(Module.Category.GUI);
    }

    private void resetModules() {
        ModuleRegistry.getModules().forEach(module -> {
            if (isValid(module) && !(module instanceof Module.NotUnToggle)) {
                if (module.isToggled())
                    module.setToggled(false);
                module.setBypass(false);
            } else {
                module.setBypass(false);
            }
        });
    }

    public void resetValues() {
        ValueManager.getInstance().getValues().stream().filter(value -> isValid((Module) value.getObject())).forEach(Value::setToDefault);
    }
}

