// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.savings;

import com.google.gson.GsonBuilder;
import net.augustus.savings.parts.ConfigPart;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import net.augustus.savings.parts.ClickGuiPart;
import net.augustus.clickgui.buttons.CategoryButton;
import net.augustus.settings.StringValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.BooleanValue;
import net.augustus.savings.parts.SettingPart;
import net.augustus.settings.Setting;
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
import java.util.Locale;
import net.augustus.Augustus;
import com.google.gson.Gson;
import net.augustus.utils.interfaces.SM;
import net.augustus.utils.interfaces.MM;
import net.augustus.utils.interfaces.MC;

public class Converter implements MC, MM, SM
{
    private final String path;
    public static final Gson GSON;
    public static final Gson NORMALGSON;
    
    public Converter() {
        this.path = Augustus.getInstance().getName().toLowerCase(Locale.ROOT);
    }
    
    public void moduleSaver(final List<Module> moduleList) {
        final FileManager<Module> fileManager = new FileManager<Module>();
        fileManager.saveFile(this.path + "/settings", "module.json", moduleList);
    }
    
    public void moduleReader(final List<Module> moduleList) {
        final File file = new File(this.path + "/settings/module.json");
        if (!file.exists()) {
            this.moduleSaver(moduleList);
        }
        ArrayList<Module> loadedModules = new ArrayList<Module>();
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(this.path + "/settings/module.json", new String[0]));
            loadedModules = Converter.GSON.fromJson(reader, new TypeToken<List<Module>>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (final Module module : moduleList) {
            for (final Module loadedModule : loadedModules) {
                if (loadedModule != null) {
                    if (module == null) {
                        continue;
                    }
                    if (!loadedModule.getName().equalsIgnoreCase(module.getName())) {
                        continue;
                    }
                    module.readModule(loadedModule);
                }
            }
        }
    }
    
    public void saveBackground(final ShaderUtil shaderUtil) {
        final FileManager<String> fileManager = new FileManager<String>();
        fileManager.saveFile(this.path + "/settings", "background.json", shaderUtil.getName());
    }
    
    public String readBackground() {
        final File file = new File(this.path + "/settings/background.json");
        if (!file.exists()) {
            final ShaderUtil shaderUtil = new ShaderUtil();
            shaderUtil.setName("Trinity");
            shaderUtil.createBackgroundShader(this.path + "/shaders/trinity.frag");
            this.saveBackground(shaderUtil);
        }
        String shaderName = null;
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(this.path + "/settings/background.json", new String[0]));
            shaderName = new Gson().fromJson(reader, new TypeToken<String>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return shaderName;
    }
    
    public void saveLastAlts(final List<String> lastAlts) {
        final FileManager<List<String>> fileManager = new FileManager<List<String>>();
        fileManager.saveFile(this.path + "/alts", "lastAlts.json", lastAlts);
    }
    
    public void readLastAlts() {
        final File file = new File(this.path + "/alts/lastAlts.json");
        if (!file.exists()) {
            this.saveLastAlts(new ArrayList<String>());
        }
        ArrayList<String> altList = new ArrayList<String>();
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(this.path + "/alts/lastAlts.json", new String[0]));
            altList = new Gson().fromJson(reader, new TypeToken<List<String>>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (!altList.isEmpty()) {
            Augustus.getInstance().setLastAlts(altList);
        }
        else {
            System.err.println("No last alts found!");
        }
    }
    
    public void settingSaver(final ArrayList<Setting> settingList) {
        final FileManager<Setting> fileManager = new FileManager<Setting>();
        fileManager.saveFile(this.path + "/settings", "settings.json", settingList);
    }
    
    public void settingReader(final ArrayList<Setting> settingList) {
        final File file = new File(this.path + "/settings/settings.json");
        if (!file.exists()) {
            this.settingSaver(settingList);
        }
        ArrayList<SettingPart> settings = new ArrayList<SettingPart>();
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(this.path + "/settings/settings.json", new String[0]));
            settings = Converter.GSON.fromJson(reader, new TypeToken<ArrayList<SettingPart>>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (final SettingPart loadedSetting : settings) {
            for (final Setting setting : settingList) {
                if (setting != null) {
                    if (loadedSetting == null) {
                        continue;
                    }
                    if (loadedSetting.getId() != setting.getId() || !loadedSetting.getName().equalsIgnoreCase(setting.getName()) || !loadedSetting.getParentName().equalsIgnoreCase(setting.getParentName())) {
                        continue;
                    }
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
        final FileManager<ClickGuiPart> fileManager = new FileManager<ClickGuiPart>();
        fileManager.saveAllFile(this.path + "/settings", "clickGui.json", clickGuiParts);
    }
    
    public void clickGuiLoader(final List<CategoryButton> categoryButtons) {
        final File file = new File(this.path + "/settings/clickGui.json");
        if (!file.exists()) {
            this.clickGuiSaver(categoryButtons);
        }
        ArrayList<ClickGuiPart> clickGuiParts = new ArrayList<ClickGuiPart>();
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(this.path + "/settings/clickGui.json", new String[0]));
            clickGuiParts = new Gson().fromJson(reader, new TypeToken<List<ClickGuiPart>>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
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
        final FileManager<ConfigPart> fileManager = new FileManager<ConfigPart>();
        fileManager.saveFile(this.path + "/configs", name + ".json", new ConfigPart(name, df.format(now.getTime()), df2.format(now.getTime()), (ArrayList)Converter.mm.getModules(), settingParts));
    }
    
    public String[] configReader(final String name) {
        final File file = new File(this.path + "/configs/" + name + ".json");
        if (!file.exists()) {
            this.configSaver(name);
        }
        ConfigPart configPart = null;
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(this.path + "/configs/" + name + ".json", new String[0]));
            configPart = Converter.GSON.fromJson(reader, ConfigPart.class);
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (configPart != null) {
            return new String[] { configPart.getName(), configPart.getDate(), configPart.getTime() };
        }
        return null;
    }
    
    public void configLoader(final String name) {
        final File file = new File(this.path + "/configs/" + name + ".json");
        if (!file.exists()) {
            this.configSaver(name);
        }
        ConfigPart configPart = null;
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(this.path + "/configs/" + name + ".json", new String[0]));
            configPart = new Gson().fromJson(reader, new TypeToken<ConfigPart>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (configPart == null) {
            System.err.println("Config wasn't loaded correctly");
            return;
        }
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
    
    public void soundSaver(final String soundName) {
        final FileManager<String> fileManager = new FileManager<String>();
        fileManager.saveAllFile(this.path + "/settings", "sound.json", soundName);
    }
    
    public String readSound() {
        final File file = new File(this.path + "/settings/sound.json");
        if (!file.exists()) {
            this.soundSaver("Vanilla");
        }
        String soundName = null;
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(this.path + "/settings/sound.json", new String[0]));
            soundName = new Gson().fromJson(reader, new TypeToken<String>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return soundName;
    }
    
    public void apiSaver(final String apiKey) {
        final FileManager<String> fileManager = new FileManager<String>();
        fileManager.saveAllFile(this.path + "/settings", "api.json", apiKey);
    }
    
    public String apiLoader() {
        final File file = new File(this.path + "/settings/api.json");
        if (!file.exists()) {
            this.apiSaver("");
        }
        String apiKey = null;
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(this.path + "/settings/api.json", new String[0]));
            apiKey = new Gson().fromJson(reader, new TypeToken<String>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return apiKey;
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        NORMALGSON = new GsonBuilder().setPrettyPrinting().create();
    }
}
