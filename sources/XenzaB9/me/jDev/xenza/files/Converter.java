// 
// Decompiled by Procyon v0.6.0
// 

package me.jDev.xenza.files;

import com.google.gson.GsonBuilder;
import me.jDev.xenza.files.parts.ConfigPart;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import me.jDev.xenza.files.parts.ClickGuiPart;
import net.augustus.clickgui.buttons.CategoryButton;
import net.augustus.settings.StringValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.BooleanValue;
import me.jDev.xenza.files.parts.SettingPart;
import net.augustus.settings.Setting;
import net.augustus.Augustus;
import net.augustus.utils.shader.ShaderUtil;
import java.util.Iterator;
import java.io.Reader;
import java.io.IOException;
import com.google.gson.reflect.TypeToken;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.File;
import net.augustus.modules.Module;
import java.util.List;
import com.google.gson.Gson;
import net.augustus.utils.interfaces.SM;
import net.augustus.utils.interfaces.MM;
import net.augustus.utils.interfaces.MC;

public class Converter implements MC, MM, SM
{
    private final String path = "xenzarecode";
    public static final Gson GSON;
    public static final Gson NORMALGSON;
    
    public void moduleSaver(final List<Module> moduleList) {
        final FileManager<Module> fileManager2;
        final FileManager<Module> fileManager = fileManager2 = new FileManager<Module>();
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        fileManager2.saveFile(sb.append("xenzarecode").append("/settings").toString(), "module.json", moduleList);
    }
    
    public void moduleReader(final List<Module> moduleList) {
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        final File file = new File(sb.append("xenzarecode").append("/settings/module.json").toString());
        if (!file.exists()) {
            this.moduleSaver(moduleList);
        }
        ArrayList<Module> loadedModules = new ArrayList<Module>();
        try {
            final StringBuilder sb2 = new StringBuilder();
            this.getClass();
            final Reader reader = Files.newBufferedReader(Paths.get(sb2.append("xenzarecode").append("/settings/module.json").toString(), new String[0]));
            loadedModules = (ArrayList)Converter.GSON.fromJson(reader, new TypeToken<List<Module>>() {}.getType());
            reader.close();
        }
        catch (final IOException var8) {
            var8.printStackTrace();
        }
        for (final Module module : moduleList) {
            for (final Module loadedModule : loadedModules) {
                if (loadedModule != null && module != null && loadedModule.getName().equalsIgnoreCase(module.getName())) {
                    module.readModule(loadedModule);
                }
            }
        }
    }
    
    public void saveBackground(final ShaderUtil shaderUtil) {
        final FileManager<String> fileManager2;
        final FileManager<String> fileManager = fileManager2 = new FileManager<String>();
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        fileManager2.saveFile(sb.append("xenzarecode").append("/settings").toString(), "background.json", shaderUtil.getName());
    }
    
    public String readBackground() {
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        final File file = new File(sb.append("xenzarecode").append("/settings/background.json").toString());
        if (!file.exists()) {
            final ShaderUtil shaderUtil = new ShaderUtil();
            shaderUtil.setName("Trinity");
            final ShaderUtil shaderUtil2 = shaderUtil;
            final StringBuilder sb2 = new StringBuilder();
            this.getClass();
            shaderUtil2.createBackgroundShader(sb2.append("xenzarecode").append("/shaders/trinity.frag").toString());
            this.saveBackground(shaderUtil);
        }
        String shaderName = null;
        try {
            final StringBuilder sb3 = new StringBuilder();
            this.getClass();
            final Reader reader = Files.newBufferedReader(Paths.get(sb3.append("xenzarecode").append("/settings/background.json").toString(), new String[0]));
            shaderName = (String)new Gson().fromJson(reader, new TypeToken<String>() {}.getType());
            reader.close();
        }
        catch (final IOException var4) {
            var4.printStackTrace();
        }
        return shaderName;
    }
    
    public void saveLastAlts(final List<String> lastAlts) {
        final FileManager<List<String>> fileManager2;
        final FileManager<List<String>> fileManager = fileManager2 = new FileManager<List<String>>();
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        fileManager2.saveFile(sb.append("xenzarecode").append("/alts").toString(), "lastAlts.json", lastAlts);
    }
    
    public void readLastAlts() {
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        final File file = new File(sb.append("xenzarecode").append("/alts/lastAlts.json").toString());
        if (!file.exists()) {
            this.saveLastAlts(new ArrayList<String>());
        }
        ArrayList<String> altList = new ArrayList<String>();
        try {
            final StringBuilder sb2 = new StringBuilder();
            this.getClass();
            final Reader reader = Files.newBufferedReader(Paths.get(sb2.append("xenzarecode").append("/alts/lastAlts.json").toString(), new String[0]));
            altList = (ArrayList)new Gson().fromJson(reader, new TypeToken<List<String>>() {}.getType());
            reader.close();
        }
        catch (final IOException var4) {
            var4.printStackTrace();
        }
        if (!altList.isEmpty()) {
            Augustus.getInstance().setLastAlts(altList);
        }
        else {
            System.err.println("No last alts found!");
        }
    }
    
    public void settingSaver(final ArrayList<Setting> settingList) {
        final FileManager<Setting> fileManager2;
        final FileManager<Setting> fileManager = fileManager2 = new FileManager<Setting>();
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        fileManager2.saveFile(sb.append("xenzarecode").append("/settings").toString(), "settings.json", settingList);
    }
    
    public void settingReader(final ArrayList<Setting> settingList) {
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        final File file = new File(sb.append("xenzarecode").append("/settings/settings.json").toString());
        if (!file.exists()) {
            this.settingSaver(settingList);
        }
        ArrayList<SettingPart> settings = new ArrayList<SettingPart>();
        try {
            final StringBuilder sb2 = new StringBuilder();
            this.getClass();
            final Reader reader = Files.newBufferedReader(Paths.get(sb2.append("xenzarecode").append("/settings/settings.json").toString(), new String[0]));
            settings = (ArrayList)Converter.GSON.fromJson(reader, new TypeToken<ArrayList<SettingPart>>() {}.getType());
            reader.close();
        }
        catch (final IOException var9) {
            var9.printStackTrace();
        }
        for (final SettingPart loadedSetting : settings) {
            for (final Setting setting : settingList) {
                if (setting != null && loadedSetting != null && loadedSetting.getId() == setting.getId() && loadedSetting.getName().equalsIgnoreCase(setting.getName()) && loadedSetting.getParentName().equalsIgnoreCase(setting.getParentName())) {
                    if (setting instanceof BooleanValue) {
                        final BooleanValue booleanValue = (BooleanValue)setting;
                        booleanValue.readSetting(loadedSetting);
                    }
                    else if (setting instanceof ColorSetting) {
                        final ColorSetting booleanValue2 = (ColorSetting)setting;
                        booleanValue2.readSetting(loadedSetting);
                    }
                    else if (setting instanceof DoubleValue) {
                        final DoubleValue booleanValue3 = (DoubleValue)setting;
                        booleanValue3.readSetting(loadedSetting);
                    }
                    else {
                        if (!(setting instanceof StringValue)) {
                            continue;
                        }
                        final StringValue booleanValue4 = (StringValue)setting;
                        booleanValue4.readSetting(loadedSetting);
                    }
                }
            }
        }
    }
    
    public void clickGuiSaver(final List<CategoryButton> categoryButtons) {
        final List<ClickGuiPart> clickGuiParts = new ArrayList<ClickGuiPart>();
        for (final CategoryButton categoryButton : categoryButtons) {
            clickGuiParts.add(new ClickGuiPart(categoryButton.xPosition, categoryButton.yPosition, categoryButton.isUnfolded(), categoryButton.getCategory()));
        }
        final FileManager<ClickGuiPart> fileManager2;
        final FileManager<ClickGuiPart> fileManager = fileManager2 = new FileManager<ClickGuiPart>();
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        fileManager2.saveAllFile(sb.append("xenzarecode").append("/settings").toString(), "clickGui.json", clickGuiParts);
    }
    
    public void clickGuiLoader(final List<CategoryButton> categoryButtons) {
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        final File file = new File(sb.append("xenzarecode").append("/settings/clickGui.json").toString());
        if (!file.exists()) {
            this.clickGuiSaver(categoryButtons);
        }
        ArrayList<ClickGuiPart> clickGuiParts = new ArrayList<ClickGuiPart>();
        try {
            final StringBuilder sb2 = new StringBuilder();
            this.getClass();
            final Reader reader = Files.newBufferedReader(Paths.get(sb2.append("xenzarecode").append("/settings/clickGui.json").toString(), new String[0]));
            clickGuiParts = (ArrayList)new Gson().fromJson(reader, new TypeToken<List<ClickGuiPart>>() {}.getType());
            reader.close();
        }
        catch (final IOException var8) {
            var8.printStackTrace();
        }
        for (final CategoryButton categoryButton : Augustus.getInstance().getClickGui().getCategoryButtons()) {
            for (final ClickGuiPart clickGuiPart : clickGuiParts) {
                if (categoryButton.getCategory() == clickGuiPart.getCategory()) {
                    if (clickGuiPart.getX() >= 0 && clickGuiPart.getY() >= 0) {
                        categoryButton.xPosition = clickGuiPart.getX();
                        categoryButton.yPosition = clickGuiPart.getY();
                    }
                    categoryButton.setUnfolded(clickGuiPart.isOpen());
                }
            }
        }
    }
    
    public void configSaver(final String name) {
        final GregorianCalendar now = new GregorianCalendar();
        final DateFormat df = DateFormat.getDateInstance(2);
        final DateFormat df2 = DateFormat.getTimeInstance(3);
        final ArrayList<SettingPart> settingParts = new ArrayList<SettingPart>();
        for (final Setting setting : Converter.sm.getStgs()) {
            if (setting instanceof BooleanValue) {
                settingParts.add(new SettingPart(setting.getId(), setting.getName(), setting.getParent(), ((BooleanValue)setting).getBoolean()));
            }
            else if (setting instanceof ColorSetting) {
                final ColorSetting colorSetting = (ColorSetting)setting;
                settingParts.add(new SettingPart(colorSetting.getId(), colorSetting.getName(), colorSetting.getParent(), colorSetting.getColor().getRed(), colorSetting.getColor().getGreen(), colorSetting.getColor().getBlue(), colorSetting.getColor().getAlpha()));
            }
            else if (setting instanceof StringValue) {
                settingParts.add(new SettingPart(setting.getId(), setting.getName(), setting.getParent(), ((StringValue)setting).getSelected(), ((StringValue)setting).getStringList()));
            }
            else {
                if (!(setting instanceof DoubleValue)) {
                    continue;
                }
                settingParts.add(new SettingPart(setting.getId(), setting.getName(), setting.getParent(), ((DoubleValue)setting).getValue(), ((DoubleValue)setting).getMinValue(), ((DoubleValue)setting).getMaxValue(), ((DoubleValue)setting).getDecimalPlaces()));
            }
        }
        final FileManager<ConfigPart> fileManager2;
        final FileManager<ConfigPart> fileManager = fileManager2 = new FileManager<ConfigPart>();
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        fileManager2.saveFile(sb.append("xenzarecode").append("/configs").toString(), name + ".json", new ConfigPart(name, df.format(now.getTime()), df2.format(now.getTime()), (ArrayList)Converter.mm.getModules(), settingParts));
    }
    
    public String[] configReader(final String name) {
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        final File file = new File(sb.append("xenzarecode").append("/configs/").append(name).append(".json").toString());
        if (!file.exists()) {
            this.configSaver(name);
        }
        ConfigPart configPart = null;
        try {
            final StringBuilder sb2 = new StringBuilder();
            this.getClass();
            final Reader reader = Files.newBufferedReader(Paths.get(sb2.append("xenzarecode").append("/configs/").append(name).append(".json").toString(), new String[0]));
            configPart = (ConfigPart)Converter.GSON.fromJson(reader, (Class)ConfigPart.class);
            reader.close();
        }
        catch (final IOException var5) {
            var5.printStackTrace();
        }
        return (String[])((configPart != null) ? new String[] { configPart.getName(), configPart.getDate(), configPart.getTime() } : null);
    }
    
    public void configLoader(final String name) {
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        final File file = new File(sb.append("xenzarecode").append("/configs/").append(name).append(".json").toString());
        if (!file.exists()) {
            this.configSaver(name);
        }
        ConfigPart configPart = null;
        try {
            final StringBuilder sb2 = new StringBuilder();
            this.getClass();
            final Reader reader = Files.newBufferedReader(Paths.get(sb2.append("xenzarecode").append("/configs/").append(name).append(".json").toString(), new String[0]));
            configPart = (ConfigPart)new Gson().fromJson(reader, new TypeToken<ConfigPart>() {}.getType());
            reader.close();
        }
        catch (final IOException var9) {
            var9.printStackTrace();
        }
        if (configPart == null) {
            System.err.println("Config wasn't loaded correctly");
        }
        else {
            for (final Module module : Converter.mm.getModules()) {
                for (final Module loadedModule : configPart.getModules()) {
                    if (loadedModule.getName().equalsIgnoreCase(module.getName())) {
                        module.readConfig(loadedModule);
                    }
                }
            }
            for (final Setting setting : Converter.sm.getStgs()) {
                for (final SettingPart loadedSetting : configPart.getSettingParts()) {
                    if (loadedSetting.getId() == setting.getId() && loadedSetting.getName().equalsIgnoreCase(setting.getName()) && loadedSetting.getParentName().equalsIgnoreCase(setting.getParent().getName())) {
                        if (setting instanceof BooleanValue) {
                            final BooleanValue booleanValue = (BooleanValue)setting;
                            booleanValue.readConfigSetting(loadedSetting);
                        }
                        else if (setting instanceof ColorSetting) {
                            final ColorSetting booleanValue2 = (ColorSetting)setting;
                            booleanValue2.readConfigSetting(loadedSetting);
                        }
                        else if (setting instanceof DoubleValue) {
                            final DoubleValue booleanValue3 = (DoubleValue)setting;
                            booleanValue3.readConfigSetting(loadedSetting);
                        }
                        else {
                            if (!(setting instanceof StringValue)) {
                                continue;
                            }
                            final StringValue booleanValue4 = (StringValue)setting;
                            booleanValue4.readConfigSetting(loadedSetting);
                        }
                    }
                }
            }
        }
    }
    
    public void soundSaver(final String soundName) {
        final FileManager<String> fileManager2;
        final FileManager<String> fileManager = fileManager2 = new FileManager<String>();
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        fileManager2.saveAllFile(sb.append("xenzarecode").append("/settings").toString(), "sound.json", soundName);
    }
    
    public String readSound() {
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        final File file = new File(sb.append("xenzarecode").append("/settings/sound.json").toString());
        if (!file.exists()) {
            this.soundSaver("Vanilla");
        }
        String soundName = null;
        try {
            final StringBuilder sb2 = new StringBuilder();
            this.getClass();
            final Reader reader = Files.newBufferedReader(Paths.get(sb2.append("xenzarecode").append("/settings/sound.json").toString(), new String[0]));
            soundName = (String)new Gson().fromJson(reader, new TypeToken<String>() {}.getType());
            reader.close();
        }
        catch (final IOException var4) {
            var4.printStackTrace();
        }
        return soundName;
    }
    
    public void apiSaver(final String apiKey) {
        final FileManager<String> fileManager2;
        final FileManager<String> fileManager = fileManager2 = new FileManager<String>();
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        fileManager2.saveAllFile(sb.append("xenzarecode").append("/settings").toString(), "api.json", apiKey);
    }
    
    public String apiLoader() {
        final StringBuilder sb = new StringBuilder();
        this.getClass();
        final File file = new File(sb.append("xenzarecode").append("/settings/api.json").toString());
        if (!file.exists()) {
            this.apiSaver("");
        }
        String apiKey = null;
        try {
            final StringBuilder sb2 = new StringBuilder();
            this.getClass();
            final Reader reader = Files.newBufferedReader(Paths.get(sb2.append("xenzarecode").append("/settings/api.json").toString(), new String[0]));
            apiKey = (String)new Gson().fromJson(reader, new TypeToken<String>() {}.getType());
            reader.close();
        }
        catch (final IOException var4) {
            var4.printStackTrace();
        }
        return apiKey;
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        NORMALGSON = new GsonBuilder().setPrettyPrinting().create();
    }
}
