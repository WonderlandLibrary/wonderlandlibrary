package info.sigmaclient.sigma.config;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.alts.Alt;
import info.sigmaclient.sigma.config.alts.AltConfig;
import info.sigmaclient.sigma.config.alts.AltConfigLoader;
import info.sigmaclient.sigma.config.antisniper.AntiSniperValues;
import info.sigmaclient.sigma.config.antisniper.DropGUI_;
import info.sigmaclient.sigma.config.antisniper.LastConfigData;
import info.sigmaclient.sigma.config.values.*;
import info.sigmaclient.sigma.gui.other.clickgui.NursultanClickGui;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.gui.hide.ClickGUI;
import info.sigmaclient.sigma.gui.clickgui.simple.JelloSubUI;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static info.sigmaclient.sigma.modules.Module.mc;

public class ConfigManager {
    public static File normalDir = new File(mc.gameDir, "/sigma5ng");
    public static File configDir = new File(mc.gameDir, "/sigma5ng/profiles");
    public static File musicDir = new File(mc.gameDir, "/sigma5ng/musics");
//    public static File altDir = new File(mc.gameDir, "/sigma5ng/tokens");
    public File guiFile = new File(normalDir, "gui.json");
    public File altFile = new File(normalDir, "alts.json");
    public File friendFile = new File(normalDir, "friend.json");
    public File premiumFile = new File(normalDir, "jello.lic");
    public File lastConfigFine = new File(normalDir, "lastConfig.json");
    public static String currentProfile = "Default";
    public static String profileLast = ".profile";
    public File getConfigFile(){
        return new File(configDir, currentProfile + profileLast);
    }
    public File getConfigFile(String fileName){
        return new File(configDir, fileName + profileLast);
    }
    private final Gson gson2 =
            new GsonBuilder().
                    excludeFieldsWithoutExposeAnnotation()
//                    setPrettyPrinting()
//                    .serializeNulls()
                    .create();
    private final Gson gson =
            new GsonBuilder()
//                    excludeFieldsWithoutExposeAnnotation().
//                    setPrettyPrinting()
//                    .serializeNulls()
                    .create();
    public boolean loadGui(String data) {
        GuiConfigs gui = gson.fromJson(data, GuiConfigs.class);
        SigmaNG.getSigmaNG().guiBlur = gui.guiBlur;
        SigmaNG.getSigmaNG().inGameGuiBlur = gui.inGameGuiBlur;
        for(JelloSubUI subUI : ClickGUI.clickGui.dropGUIS){
            for(DropGUI_ subUI2 : gui.guis){
                if(subUI2.TITLE.equalsIgnoreCase(subUI.title)){
                    // equal
                    subUI.x = subUI2.x;
                    subUI.y = subUI2.y;
                }
            }
        }
//        for(NursultanClickGui.SubGui subUI : ClickGUI.clickGui2.dropGUIS){
//            for(DropGUI_ subUI2 : gui.guis){
//                if(subUI2.TITLE.equalsIgnoreCase(subUI.title)){
//                    // equal
//                    subUI.x = (float) subUI2.x;
//                    subUI.y = (float) subUI2.y;
//                }
//            }
//        }
        // 音乐
        ClickGUI.clickGui.musicPlayer.x = gui.musicplayer.x;
        ClickGUI.clickGui.musicPlayer.y = gui.musicplayer.y;
        return true;
    }
    public boolean loadAlts(String data) {
        AltConfigLoader[] gui = gson.fromJson(data, AltConfigLoader[].class);
        AltConfig.Instance.alts.clear();
        for(AltConfigLoader alt : gui){
            Alt loader = new Alt("", "");
            loader.name = alt.name;
            loader.time = alt.time;
            loader.token = alt.token;
            loader.offline = alt.offline;
            loader.uuid = alt.uuid;
            AltConfig.Instance.alts.add(loader);
        }
        return true;
    }
    public boolean loadFriends(String data) {
        String[] gui = gson.fromJson(data, String[].class);
        SigmaNG.getSigmaNG().friendsManager.friends.clear();
        for(String af : gui){
            SigmaNG.getSigmaNG().friendsManager.friends.add(af);
        }
        return true;
    }
    public boolean loadLastConfigs(String data) {
        LastConfigData gui = gson.fromJson(data, LastConfigData.class);
        if(gui.last != null)
            currentProfile = gui.last;
        return true;
    }
    public boolean loadConfig(String data) {
        boolean jrMode = false;
        if(JSONObject.parseArray(data).getJSONObject(0).getString("version") == null){
            jrMode = true;
        }
        AntiSniperValues[] modules = gson2.fromJson(data, AntiSniperValues[].class);
        for (Module module : SigmaNG.getSigmaNG().moduleManager.modules) {
            for (AntiSniperValues configModule : modules) {
                if(jrMode) {
//                    configModule.key = -1;
                }
                if (module.name.equalsIgnoreCase(configModule.name)) {
                    try {
                        boolean last = module.enabled;
                        module.enabled = configModule.enable;
                        if(mc.player != null && mc.world != null && last != module.enabled){
                            if(module.enabled){
                                module.onEnable();
                            }else{
                                module.onDisable();
                            }
                        }
                        module.key = configModule.key;
                        for (Value<?> setting : module.values) {
                            for (Value<?> to : configModule.values) {
                                if(to == null) continue;
                                if (setting.id.equalsIgnoreCase(to.id)) {
                                    if (setting instanceof BooleanValue) {
                                        ((BooleanValue) setting).setValue((Boolean) to.getValue());
                                    }
                                    if (setting instanceof ColorValue) {
                                        ((ColorValue) setting).setValue(((Number) to.getValue()).intValue());
                                    }
                                    if (setting instanceof ModeValue) {
                                        for (String str : ((ModeValue) setting).values) {
                                            if (str.equals(to.getValue()))
                                                ((ModeValue) setting).setValue(str);
                                        }
                                    }
                                    if (setting instanceof NumberValue) {
                                        ((NumberValue) setting).setValue((Number) to.getValue());
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
        return true;
    }
    public static boolean saveConfig(String content, File file) {
        if(!configDir.exists()){
            configDir.mkdirs();
        }
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String serializeEmpty() {
        List<AntiSniperValues> antiSniperValues = new ArrayList<>();
        for(Module m : SigmaNG.SigmaNG.moduleManager.modules){
            AntiSniperValues antiSniperValues1 = new AntiSniperValues();
            antiSniperValues1.enable = false;
            antiSniperValues1.key = -1;
            antiSniperValues1.name = m.name;
            antiSniperValues1.values = m.values;
            antiSniperValues.add(antiSniperValues1);
        }
        return gson2.toJson(antiSniperValues);
    }
    public String serialize() {
        List<AntiSniperValues> antiSniperValues = new ArrayList<>();
        for(Module m : SigmaNG.SigmaNG.moduleManager.modules){
            AntiSniperValues antiSniperValues1 = new AntiSniperValues();
            antiSniperValues1.enable = m.enabled;
            antiSniperValues1.key = m.key;
            antiSniperValues1.name = m.name;
            antiSniperValues1.values = m.values;
            antiSniperValues.add(antiSniperValues1);
        }
        return gson2.toJson(antiSniperValues);
    }
    public String saveGui() {
        GuiConfigs guiConfigs = new GuiConfigs();
            for (JelloSubUI jelloSubUI : ClickGUI.clickGui.dropGUIS) {
                DropGUI_ gui = new DropGUI_();
                gui.x = jelloSubUI.x;
                gui.y = jelloSubUI.y;
                gui.TITLE = jelloSubUI.title;
                guiConfigs.guis.add(gui);
            }
        guiConfigs.guiBlur = SigmaNG.getSigmaNG().guiBlur;
        guiConfigs.inGameGuiBlur = SigmaNG.getSigmaNG().inGameGuiBlur;

        guiConfigs.musicplayer.x = ClickGUI.clickGui.musicPlayer.x;
        guiConfigs.musicplayer.y = ClickGUI.clickGui.musicPlayer.y;
        return gson.toJson(guiConfigs);
    }
    public String saveLastConfig() {
        LastConfigData aa = new LastConfigData();
        aa.last = currentProfile;
        return gson.toJson(aa);
    }
    public String saveFriends() {
        List<String> list = new ArrayList<>(SigmaNG.getSigmaNG().friendsManager.friends);
        return gson.toJson(list);
    }
    public String saveAlts() {
        List<AltConfigLoader> list = new ArrayList<>();
        for(Alt alt : AltConfig.Instance.alts){
            AltConfigLoader loader = new AltConfigLoader();
            loader.name = alt.name;
            loader.time = alt.time;
            loader.token = alt.token;
            loader.offline = alt.offline;
            loader.uuid = alt.uuid;
            list.add(loader);
        }
        return gson.toJson(list);
    }
    public static String readConfigData(Path configPath) {
        try {
            return new String(Files.readAllBytes(configPath));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public void loadDefaultConfig(){
        if(lastConfigFine.exists())
            loadLastConfigs(readConfigData(lastConfigFine.toPath()));
        if(getConfigFile().exists())
            loadConfig(readConfigData(getConfigFile().toPath()));
        if(guiFile.exists())
            loadGui(readConfigData(guiFile.toPath()));
        if(altFile.exists())
            loadAlts(readConfigData(altFile.toPath()));
        if(friendFile.exists())
            loadFriends(readConfigData(friendFile.toPath()));
    }
    public void saveLastConfigData(){
        saveConfig(saveLastConfig(), lastConfigFine);
    }
    public void saveDupeConfig(String empy){
        saveConfig(serialize(), new File(configDir, empy + profileLast)); // ok
    }
    public void saveEmptyConfig(String empy){
        saveConfig(serializeEmpty(), new File(configDir, empy + profileLast)); // ok
    }
    public void saveDefaultConfig(){
        saveLastConfigData();
        saveConfig(serialize(), getConfigFile()); // ok
        saveConfig(saveGui(), guiFile); // ok
        saveConfig(saveAlts(), altFile); // ok
        saveConfig(saveFriends(), friendFile); // ok
    }
    public void saveDefaultConfigWithoutAlt(){
        saveLastConfigData();
        saveConfig(serialize(), getConfigFile()); // ok
        saveConfig(saveGui(), guiFile); // ok
        saveConfig(saveFriends(), friendFile); // ok
    }
}
