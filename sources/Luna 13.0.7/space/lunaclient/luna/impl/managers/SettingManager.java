package space.lunaclient.luna.impl.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.manager.Manager;
import space.lunaclient.luna.api.setting.BooleanSetting;
import space.lunaclient.luna.api.setting.DoubleSetting;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;

public class SettingManager
  extends Manager<Setting>
{
  public SettingManager()
  {
    loadSettings();
  }
  
  private void loadSettings()
  {
    for (Element module : Luna.INSTANCE.ELEMENT_MANAGER.getContents()) {
      for (Field field : module.getClass().getDeclaredFields()) {
        if (field.isAnnotationPresent(ModeSetting.class))
        {
          ArrayList<String> ar = new ArrayList(Arrays.asList(((ModeSetting)field.getAnnotation(ModeSetting.class)).options()));
          addSetting(new Setting(((ModeSetting)field.getAnnotation(ModeSetting.class)).name(), module, 
          
            ((ModeSetting)field.getAnnotation(ModeSetting.class)).currentOption(), ar, 
            Boolean.valueOf(((ModeSetting)field.getAnnotation(ModeSetting.class)).locked())), module, field);
        }
        else if (field.isAnnotationPresent(BooleanSetting.class))
        {
          addSetting(new Setting(((BooleanSetting)field.getAnnotation(BooleanSetting.class)).name(), module, 
          
            ((BooleanSetting)field.getAnnotation(BooleanSetting.class)).booleanValue()), module, field);
        }
        else if (field.isAnnotationPresent(DoubleSetting.class))
        {
          addSetting(new Setting(((DoubleSetting)field.getAnnotation(DoubleSetting.class)).name(), module, 
          
            ((DoubleSetting)field.getAnnotation(DoubleSetting.class)).currentValue(), 
            ((DoubleSetting)field.getAnnotation(DoubleSetting.class)).minValue(), 
            ((DoubleSetting)field.getAnnotation(DoubleSetting.class)).maxValue(), 
            ((DoubleSetting)field.getAnnotation(DoubleSetting.class)).onlyInt(), Boolean.valueOf(((DoubleSetting)field.getAnnotation(DoubleSetting.class)).locked())), module, field);
        }
      }
    }
  }
  
  private void addSetting(Setting setting, Element module, Field field)
  {
    getContents().add(setting);
    field.setAccessible(true);
    try
    {
      field.set(module, setting);
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
  }
  
  public ArrayList<Setting> getSettingsByMod(Element mod)
  {
    ArrayList<Setting> out = new ArrayList();
    for (Setting s : getContents()) {
      if (s.getParentMod().equals(mod)) {
        out.add(s);
      }
    }
    if (out.isEmpty()) {
      return null;
    }
    return out;
  }
  
  public Setting getSettingByModByName(Element mod, String name)
  {
    for (Setting s : getContents()) {
      if ((s.getParentMod().equals(mod)) && (s.getName().equalsIgnoreCase(name))) {
        return s;
      }
    }
    return null;
  }
}
