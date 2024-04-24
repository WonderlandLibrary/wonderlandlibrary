package Method.Client.managers;

import Method.Client.Main;
import Method.Client.clickgui.component.Frame;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.Profiles.Profiletem;
import Method.Client.utils.system.Wrapper;
import com.google.gson.*;

import java.io.*;
import java.util.Map;

import static Method.Client.Main.setmgr;

public class FileManager {

    public static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    public static final JsonParser jsonParser = new JsonParser();

    public static final File SaveDir = new File(String.format("%s%s%s-%s-%s%s", Wrapper.INSTANCE.mc().gameDir, File.separator, Main.NAME, Main.MCVERSION, Main.VERSION, File.separator));

    public static final File[] files = {
            new File(SaveDir, "profiles.json"),//0
            new File(SaveDir, "esp.json"),//1
            new File(SaveDir, "xraydata.json"),
            new File(SaveDir, "friends.json"),
            new File(SaveDir, "onscreen.json"),//4
            new File(SaveDir, "search.json"),
            new File(SaveDir, "other.json"),
            new File(SaveDir, "antipacket.json"),
            new File(SaveDir, "mods.json")//8
    };

    public FileManager() {
        if (!SaveDir.exists())
            SaveDir.mkdir();

        for (File file : files) {
            if (!file.exists()) {
                saveData(file);
            } else {
                loadData(file);
            }
        }
    }

    public static void saveData(File file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gsonPretty.toJson(getDataForFile(file)));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData(File file) {
        try {
            JsonElement json = jsonParser.parse(new FileReader(file));
            setDataForFile(file, json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Object getDataForFile(File file) {
        switch (file.getName()) {
            case "esp.json":
                return Main.GuiEsp.toJson();
            case "antipacket.json":
                return Main.AntiPacketgui.toJson();
            case "friends.json":
                return new Gson().toJsonTree(FriendManager.friendsList);
            case "search.json":
                return Main.Search.toJson();
            case "xraydata.json":
                return Main.Xraygui.toJson();
            case "other.json":
                JsonObject Other = new JsonObject();

                JsonObject prefix = new JsonObject();
                prefix.addProperty("PREFIX", CommandManager.cmdPrefix);
                Other.add("PREFIX", prefix);

                JsonObject version = new JsonObject();
                version.addProperty("VERSION", Main.VERSION);
                Other.add("VERSION", version);
                return Other;
            case "onscreen.json":
                JsonObject json = new JsonObject();
                for (Frame frame : Main.ClickGui.frames) {
                    JsonObject currentFrame = new JsonObject();
                    currentFrame.addProperty("x", frame.getX());
                    currentFrame.addProperty("y", frame.getY());
                    currentFrame.addProperty("open", frame.isOpen());
                    json.add(frame.getName(), currentFrame);
                }
                return json;
            case "profiles.json":
                JsonObject Profiles = new JsonObject();
                for (Module m : ModuleManager.getModulesInCategory(Category.PROFILES)) {
                    for (Profiletem.ModuleStored storedSetting : ((Profiletem) m).StoredModules) {
                        JsonObject JsonMod = new JsonObject();

                        JsonMod.addProperty("Profile", storedSetting.parent);
                        JsonMod.addProperty("toggled", storedSetting.toggled);
                        JsonMod.addProperty("key", storedSetting.keys.toString());
                        JsonMod.addProperty("Visible", storedSetting.visible);
                        JsonMod.addProperty("Module", storedSetting.name);
                        Profiles.add(storedSetting.parent + " " + storedSetting.name, JsonMod);

                        for (Setting s : storedSetting.settings) {
                            if (s.getMode() == Setting.SettingType.Slider) {
                                JsonMod.addProperty(s.getName(), s.getValDouble());
                            }
                            if (s.getMode() == Setting.SettingType.Check) {
                                JsonMod.addProperty(s.getName(), s.getValBoolean());
                            }
                            if (s.getMode() == Setting.SettingType.Combo) {
                                JsonMod.addProperty(s.getName(), s.getValString());
                            }
                            if (s.getMode() == Setting.SettingType.Check) {
                                JsonMod.addProperty(s.getName() + "c", s.getValDouble());
                                JsonMod.addProperty(s.getName() + "s", s.getSat());
                                JsonMod.addProperty(s.getName() + "b", s.getBri());
                                JsonMod.addProperty(s.getName() + "a", s.getAlpha());
                            }
                        }
                    }
                }
                return Profiles;
            case "mods.json":
                JsonObject Mods = new JsonObject();
                ModuleManager.getModules().forEach(module -> {
                    JsonObject JsonMod = new JsonObject();
                    JsonMod.addProperty("toggled", module.isToggled());
                    JsonMod.addProperty("key", module.getKeys().toString());
                    JsonMod.addProperty("Visible", module.visible);
                    Mods.add(module.getName(), JsonMod);

                    for (Setting s : setmgr.getSettingsByMod(module)) {
                        if (s.getMode() == Setting.SettingType.Slider) {
                            JsonMod.addProperty(s.getName(), s.getValDouble());
                        }
                        if (s.getMode() == Setting.SettingType.Check) {
                            JsonMod.addProperty(s.getName(), s.getValBoolean());
                        }
                        if (s.getMode() == Setting.SettingType.Combo) {
                            JsonMod.addProperty(s.getName(), s.getValString());
                        }
                        if (s.getMode() == Setting.SettingType.Check) {
                            JsonMod.addProperty(s.getName() + "c", s.getValDouble());
                            JsonMod.addProperty(s.getName() + "s", s.getSat());
                            JsonMod.addProperty(s.getName() + "b", s.getBri());
                            JsonMod.addProperty(s.getName() + "a", s.getAlpha());
                        }
                    }
                });
                return Mods;
            default:
                // Else return null or default data
                return null;
        }
    }

    private static void setDataForFile(File file, JsonElement json) {
        // Set the data loaded from the file
        if (json == null || json.isJsonNull()) {
            return;
        }

        switch (file.getName()) {
            case "esp.json":
                Main.GuiEsp.fromJson(json);
                break;
            case "antipacket.json":
                Main.AntiPacketgui.fromJson(json);
                break;
            case "friends.json":
                JsonArray friendsJson = json.getAsJsonArray();
                FriendManager.friendsList.clear();
                friendsJson.forEach(element -> FriendManager.addFriend(element.getAsString()));
                break;
            case "search.json":
                Main.Search.fromJson(json.getAsJsonArray());
                break;
            case "xraydata.json":
                Main.Xraygui.fromJson(json.getAsJsonArray());
                break;
            case "other.json":
                JsonObject jsonObj = json.getAsJsonObject();
                if (jsonObj.has("PREFIX")) {
                    CommandManager.cmdPrefix = jsonObj.getAsJsonObject("PREFIX").get("PREFIX").getAsCharacter();
                }
                break;
            case "onscreen.json":
                JsonObject moduleJson = json.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : moduleJson.entrySet()) {
                    JsonObject jsonMod = entry.getValue().getAsJsonObject();
                    for (Frame frame : Main.ClickGui.frames) {
                        if (entry.getKey().equals(frame.getName())) {
                            frame.setX(jsonMod.get("x").getAsInt());
                            frame.setY(jsonMod.get("y").getAsInt());
                            frame.setOpen(jsonMod.get("open").getAsBoolean());
                        }
                    }
                }
                break;
            case "profiles.json":
                if (!SaveDir.exists())
                    break;
                boolean noprofile = true;
                JsonObject moduleJason = json.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : moduleJason.entrySet()) {
                    noprofile = false;
                    JsonObject jsonMod = (JsonObject) entry.getValue();
                    if (!jsonMod.get("Profile").isJsonNull()) {
                        Module m;
                        if (ModuleManager.getModuleByName((jsonMod.get("Profile").getAsString())) == null) {
                            m = new Profiletem(jsonMod.get("Profile").getAsString());
                            ModuleManager.addModule(m);
                        } else
                            m = ModuleManager.getModuleByName(jsonMod.get("Profile").getAsString());
                        if (m != null) {
                            Profiletem.ModuleStored settingStored = new Profiletem.ModuleStored();
                            settingStored.name = jsonMod.get("Module").getAsString();
                            for (Setting setting : setmgr.getSettingsByMod(ModuleManager.getModuleByName(jsonMod.get("Module").getAsString()))) {
                                settingStored.settings.add(new Setting(setting));
                            }
                            ProfileModLoad(settingStored, jsonMod);
                            ((Profiletem) m).StoredModules.add(settingStored);
                        }
                    }
                }
                if (noprofile) {
                    ModuleManager.addModule(new Profiletem("Example"));
                    ModuleManager.addModule(new Profiletem("Example2"));
                }
                for (Frame frame : Main.ClickGui.frames) {
                    frame.updateRefresh();
                }
                break;
            case "mods.json":
                JsonObject asJsonObject = json.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : asJsonObject.entrySet()) {
                    Module module = ModuleManager.getModuleByName(entry.getKey());
                    if (module != null) {
                        JsonObject jsonMod = (JsonObject) entry.getValue();
                        module.setKeys(jsonMod.get("key").getAsString());
                        module.visible = jsonMod.get("Visible").getAsBoolean();

                        for (Setting s : setmgr.getSettingsByMod(module)) {
                            ModuleSet(jsonMod, s);
                        }
                        if (jsonMod.get("toggled").getAsBoolean()) {
                            ModuleManager.FileManagerLoader.add(module);
                            module.setToggled(true);
                        }
                    }
                }
                break;
        }
    }

    private static void ProfileModLoad(Profiletem.ModuleStored settingStored, JsonObject jsonMod) {
        settingStored.setKeys(jsonMod.get("key").getAsString());
        settingStored.visible = jsonMod.get("Visible").getAsBoolean();
        settingStored.toggled = jsonMod.get("toggled").getAsBoolean();
        for (Setting s : settingStored.settings) {
            ModuleSet(jsonMod, s);
        }
    }

    private static void ModuleSet(JsonObject jsonMod, Setting s) {
        if (jsonMod.has(s.getName())) {
            if (s.getMode() == Setting.SettingType.Slider) {
                s.setValDouble(jsonMod.get(s.getName()).getAsDouble());
            }
            if (s.getMode() == Setting.SettingType.Check) {
                s.setValBoolean(jsonMod.get(s.getName()).getAsBoolean());
            }
            if (s.getMode() == Setting.SettingType.Combo) {
                s.setValString(jsonMod.get(s.getName()).getAsString());
            }
            if (s.getMode() == Setting.SettingType.Color) {
                s.setValDouble(jsonMod.get(s.getName() + "c").getAsDouble());
                s.setsaturation((float) jsonMod.get(s.getName() + "s").getAsDouble());
                s.setbrightness((float) jsonMod.get(s.getName() + "b").getAsDouble());
                s.setAlpha((float) jsonMod.get(s.getName() + "a").getAsDouble());
            }
        }
    }
}
