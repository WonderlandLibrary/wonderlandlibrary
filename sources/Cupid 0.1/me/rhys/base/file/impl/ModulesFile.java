package me.rhys.base.file.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import me.rhys.base.Lite;
import me.rhys.base.file.IFile;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.data.ModuleData;
import me.rhys.base.module.setting.Setting;
import me.rhys.base.module.setting.SettingFactory;
import me.rhys.base.module.setting.impl.BooleanSetting;
import me.rhys.base.module.setting.impl.EnumSetting;
import me.rhys.base.module.setting.impl.number.impl.ByteNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.DoubleNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.FloatNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.IntNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.ShortNumberSetting;
import me.rhys.client.module.render.ClickGui;

public class ModulesFile implements IFile {
  private File file;
  
  public void save(Gson gson) {
    JsonArray array = new JsonArray();
    Lite.MODULE_FACTORY.forEach(module -> {
          JsonObject object = new JsonObject();
          ModuleData data = module.getData();
          object.addProperty("name", data.getName());
          object.addProperty("description", data.getDescription());
          object.addProperty("category", data.getCategory().toString());
          object.addProperty("keyCode", Integer.valueOf(data.getKeyCode()));
          if (module.getClass().equals(ClickGui.class)) {
            object.addProperty("enabled", Boolean.valueOf(false));
          } else {
            object.addProperty("enabled", Boolean.valueOf(data.isEnabled()));
          } 
          object.addProperty("mode", Integer.valueOf(data.getCurrentMode()));
          SettingFactory.SettingContainer container = (SettingFactory.SettingContainer)Lite.SETTING_FACTORY.get(module);
          if (!container.settings.isEmpty()) {
            JsonArray settingsArray = new JsonArray();
            container.settings.forEach(());
            object.add("settings", (JsonElement)settingsArray);
          } 
          if (!container.settingManager.isEmpty()) {
            JsonArray settingsArray = new JsonArray();
            container.settingManager.forEach(());
            object.add("modeSettings", (JsonElement)settingsArray);
          } 
          array.add((JsonElement)object);
        });
    writeFile(gson.toJson((JsonElement)array), this.file);
  }
  
  public void load(Gson gson) {
    if (!this.file.exists())
      return; 
    DataEntry[] dataEntries = (DataEntry[])gson.fromJson(readFile(this.file), DataEntry[].class);
    for (DataEntry entry : dataEntries) {
      Module module = Lite.MODULE_FACTORY.findByName(entry.getName());
      if (module != null) {
        ModuleData data = module.getData();
        data.setKeyCode(entry.getKeyCode());
        int currentMode;
        if ((currentMode = entry.getCurrentMode()) != -1)
          module.setCurrentMode(currentMode); 
        if (entry.settings != null)
          for (SettingEntry settingEntry : entry.settings) {
            Setting setting = Lite.SETTING_FACTORY.getSetting(module, settingEntry.name);
            if (setting != null)
              if (setting instanceof BooleanSetting) {
                BooleanSetting booleanSetting = (BooleanSetting)setting;
                booleanSetting.set(Boolean.parseBoolean(settingEntry.value.toString()));
              } else if (setting instanceof ByteNumberSetting) {
                ByteNumberSetting byteNumberSetting = (ByteNumberSetting)setting;
                byteNumberSetting.set(Byte.valueOf((byte)(int)Double.parseDouble(settingEntry.value.toString())));
              } else if (setting instanceof DoubleNumberSetting) {
                DoubleNumberSetting doubleNumberSetting = (DoubleNumberSetting)setting;
                doubleNumberSetting.set(Double.valueOf(Double.parseDouble(settingEntry.value.toString())));
              } else if (setting instanceof FloatNumberSetting) {
                FloatNumberSetting floatNumberSetting = (FloatNumberSetting)setting;
                floatNumberSetting.set(Float.valueOf(Float.parseFloat(settingEntry.value.toString())));
              } else if (setting instanceof IntNumberSetting) {
                IntNumberSetting intNumberSetting = (IntNumberSetting)setting;
                intNumberSetting.set(Integer.valueOf((int)Double.parseDouble(settingEntry.value.toString())));
              } else if (setting instanceof ShortNumberSetting) {
                ShortNumberSetting shortNumberSetting = (ShortNumberSetting)setting;
                shortNumberSetting.set(Short.valueOf((short)(int)Double.parseDouble(settingEntry.value.toString())));
              } else if (setting instanceof EnumSetting) {
                EnumSetting enumSetting = (EnumSetting)setting;
                enumSetting.set(settingEntry.value.toString());
              }  
          }  
        if (entry.modeSettings != null)
          for (ModeSettings modeSettings : entry.modeSettings) {
            for (SettingEntry settingEntry : modeSettings.settings) {
              Setting setting = ((SettingFactory.SettingContainer)Lite.SETTING_FACTORY.get(module)).getModeSetting(modeSettings.name, settingEntry.name);
              if (setting != null)
                if (setting instanceof BooleanSetting) {
                  BooleanSetting booleanSetting = (BooleanSetting)setting;
                  booleanSetting.set(Boolean.parseBoolean(settingEntry.value.toString()));
                } else if (setting instanceof ByteNumberSetting) {
                  ByteNumberSetting byteNumberSetting = (ByteNumberSetting)setting;
                  byteNumberSetting.set(Byte.valueOf((byte)(int)Double.parseDouble(settingEntry.value.toString())));
                } else if (setting instanceof DoubleNumberSetting) {
                  DoubleNumberSetting doubleNumberSetting = (DoubleNumberSetting)setting;
                  doubleNumberSetting.set(Double.valueOf(Double.parseDouble(settingEntry.value.toString())));
                } else if (setting instanceof FloatNumberSetting) {
                  FloatNumberSetting floatNumberSetting = (FloatNumberSetting)setting;
                  floatNumberSetting.set(Float.valueOf(Float.parseFloat(settingEntry.value.toString())));
                } else if (setting instanceof IntNumberSetting) {
                  IntNumberSetting intNumberSetting = (IntNumberSetting)setting;
                  intNumberSetting.set(Integer.valueOf((int)Double.parseDouble(settingEntry.value.toString())));
                } else if (setting instanceof ShortNumberSetting) {
                  ShortNumberSetting shortNumberSetting = (ShortNumberSetting)setting;
                  shortNumberSetting.set(Short.valueOf((short)(int)Double.parseDouble(settingEntry.value.toString())));
                } else if (setting instanceof EnumSetting) {
                  EnumSetting enumSetting = (EnumSetting)setting;
                  enumSetting.set(settingEntry.value.toString());
                }  
            } 
          }  
        module.toggle(entry.isEnabled());
      } 
    } 
  }
  
  public void setFile(File root) {
    this.file = new File(root, "/modules.json");
  }
  
  private static class DataEntry extends ModuleData {
    @SerializedName("settings")
    private final ModulesFile.SettingEntry[] settings;
    
    @SerializedName("modeSettings")
    private final ModulesFile.ModeSettings[] modeSettings;
    
    public ModulesFile.SettingEntry[] getSettings() {
      return this.settings;
    }
    
    public ModulesFile.ModeSettings[] getModeSettings() {
      return this.modeSettings;
    }
    
    public DataEntry(String name, String description, Category category, int keyCode, int currentMode, boolean enabled, ModulesFile.SettingEntry[] settings, ModulesFile.ModeSettings[] modeSettings) {
      super(name, description, category, keyCode, currentMode, enabled);
      this.settings = settings;
      this.modeSettings = modeSettings;
    }
    
    public String toString() {
      return "DataEntry{settings=" + 
        Arrays.toString((Object[])this.settings) + ", modeSettings=" + 
        Arrays.toString((Object[])this.modeSettings) + '}';
    }
  }
  
  private static class SettingEntry {
    @SerializedName("name")
    private final String name;
    
    @SerializedName("value")
    private final Object value;
    
    public SettingEntry(String name, Object value) {
      this.name = name;
      this.value = value;
    }
    
    public String getName() {
      return this.name;
    }
    
    public Object getValue() {
      return this.value;
    }
    
    public String toString() {
      return "SettingEntry{name='" + this.name + '\'' + ", value=" + this.value + '}';
    }
  }
  
  private static class ModeSettings {
    @SerializedName("name")
    private final String name;
    
    @SerializedName("settings")
    private final ModulesFile.SettingEntry[] settings;
    
    public ModeSettings(String name, ModulesFile.SettingEntry[] settings) {
      this.name = name;
      this.settings = settings;
    }
    
    public String toString() {
      return "ModeSettings{name='" + this.name + '\'' + ", settings=" + 
        
        Arrays.toString((Object[])this.settings) + '}';
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\file\impl\ModulesFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */