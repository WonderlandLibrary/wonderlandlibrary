package me.rhys.base.module.setting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.rhys.base.Lite;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.impl.BooleanSetting;
import me.rhys.base.module.setting.impl.EnumSetting;
import me.rhys.base.module.setting.impl.number.impl.ByteNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.DoubleNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.FloatNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.IntNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.ShortNumberSetting;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.container.MapContainer;

public class SettingFactory extends MapContainer<Module, SettingFactory.SettingContainer> {
  public void fetchSettings() {
    Lite.MODULE_FACTORY.forEach(module -> {
          if (!getMap().containsKey(module))
            put(module, new SettingContainer(module)); 
          for (Field field : module.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent((Class)Name.class)) {
              if (!field.isAccessible())
                field.setAccessible(true); 
              Setting setting = makeSetting(module, field);
              ((SettingContainer)get(module)).settings.add(setting);
            } 
          } 
          if (!module.isEmpty())
            module.forEach(()); 
        });
  }
  
  public Setting getSetting(Module module, String name) {
    return ((SettingContainer)get(module)).settings.stream().filter(setting -> setting.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
  }
  
  private Setting makeSetting(Object parent, Field field) {
    Setting setting;
    if (field.getType().equals(double.class)) {
      DoubleNumberSetting doubleNumberSetting = new DoubleNumberSetting(parent, field);
    } else if (field.getType().equals(float.class)) {
      FloatNumberSetting floatNumberSetting = new FloatNumberSetting(parent, field);
    } else if (field.getType().equals(int.class)) {
      IntNumberSetting intNumberSetting = new IntNumberSetting(parent, field);
    } else if (field.getType().equals(byte.class)) {
      ByteNumberSetting byteNumberSetting = new ByteNumberSetting(parent, field);
    } else if (field.getType().equals(short.class)) {
      ShortNumberSetting shortNumberSetting = new ShortNumberSetting(parent, field);
    } else if (field.getType().equals(boolean.class)) {
      BooleanSetting booleanSetting = new BooleanSetting(parent, field);
    } else if (field.getType().equals(Enum.class) || (field
      .getType().getSuperclass() != null && field
      .getType().getSuperclass().equals(Enum.class))) {
      EnumSetting enumSetting = new EnumSetting(parent, field);
    } else {
      setting = new Setting(parent, field);
    } 
    return setting;
  }
  
  public static final class SettingContainer {
    public Module module;
    
    public List<Setting> settings;
    
    public Map<ModuleMode<?>, List<Setting>> settingManager;
    
    public SettingContainer(Module module) {
      this.module = module;
      this.settings = new ArrayList<>();
      this.settingManager = new HashMap<>();
    }
    
    public Setting getModeSetting(String mode, String setting) {
      try {
        return ((List<Setting>)Objects.<List<Setting>>requireNonNull(getModeSettings(mode))).stream().filter(s -> s.getName().equalsIgnoreCase(setting)).findFirst().orElse(null);
      } catch (Exception ignored) {
        return null;
      } 
    }
    
    public List<Setting> getModeSettings(String mode) {
      ModuleMode<?> moduleMode = (ModuleMode)this.module.find(mm -> mm.getName().equalsIgnoreCase(mode));
      if (moduleMode == null)
        return null; 
      return new ArrayList<>(this.settingManager.get(moduleMode));
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\module\setting\SettingFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */