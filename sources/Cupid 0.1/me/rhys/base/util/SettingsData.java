package me.rhys.base.util;

import me.rhys.base.module.setting.Setting;

public class SettingsData {
  private String name;
  
  private Setting setting;
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setSetting(Setting setting) {
    this.setting = setting;
  }
  
  public String getName() {
    return this.name;
  }
  
  public Setting getSetting() {
    return this.setting;
  }
  
  public SettingsData(String name, Setting setting) {
    this.name = name;
    this.setting = setting;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\SettingsData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */