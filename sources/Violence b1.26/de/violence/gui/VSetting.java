package de.violence.gui;

import de.violence.module.Module;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VSetting {
   static List settings = new ArrayList();
   private String name;
   private Module module;
   private VSetting.SettingType settingType;
   public List hideSettings = new ArrayList();
   public boolean show = true;
   public boolean toggled;
   public double min;
   public double max;
   public double current;
   public boolean round;
   public List modeList = new ArrayList();
   public String activeMode;
   public boolean extend;

   public static VSetting getByName(String name, Module module) {
      Iterator var3 = getSettings().iterator();

      VSetting setting;
      do {
         if(!var3.hasNext()) {
            return null;
         }

         setting = (VSetting)var3.next();
      } while(!setting.getName().equalsIgnoreCase(name) || setting.getModule() != module);

      return setting;
   }

   public static List getSettings() {
      return settings;
   }

   public VSetting.SettingType getSettingType() {
      return this.settingType;
   }

   public Module getModule() {
      return this.module;
   }

   public String getName() {
      return this.name;
   }

   public String getActiveMode() {
      return this.activeMode;
   }

   public boolean isToggled() {
      return this.toggled;
   }

   public double getMin() {
      return this.min;
   }

   public double getMax() {
      return this.max;
   }

   public double getCurrent() {
      return this.current;
   }

   public boolean isRound() {
      return this.round;
   }

   public List getModeList() {
      return this.modeList;
   }

   public VSetting(String name, Module module, boolean toggled) {
      this.name = name;
      this.module = module;
      this.toggled = toggled;
      this.settingType = VSetting.SettingType.BUTTON;
      settings.add(this);
   }

   public VSetting(String name, Module module, boolean toggled, List hideSettings) {
      this.name = name;
      this.module = module;
      this.toggled = toggled;
      this.settingType = VSetting.SettingType.BUTTON;
      this.hideSettings = hideSettings;
      settings.add(this);
   }

   public VSetting(String name, Module module, double min, double max, double current, boolean round) {
      this.name = name;
      this.module = module;
      this.min = min;
      this.max = max;
      this.current = current;
      this.round = round;
      this.settingType = VSetting.SettingType.SLIDER;
      settings.add(this);
   }

   public VSetting(String name, Module module, List modes, String active) {
      this.name = name;
      this.module = module;
      this.modeList = modes;
      this.settingType = VSetting.SettingType.MODE;
      this.activeMode = active;
      settings.add(this);
   }

   public VSetting(String name, Module module, List modes, String active, List hideSettings) {
      this.name = name;
      this.module = module;
      this.modeList = modes;
      this.settingType = VSetting.SettingType.MODE;
      this.activeMode = active;
      this.hideSettings = hideSettings;
      settings.add(this);
   }

   public static enum SettingType {
      BUTTON,
      SLIDER,
      MODE;
   }
}
