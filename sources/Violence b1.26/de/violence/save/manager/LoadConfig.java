package de.violence.save.manager;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.save.Value;
import de.violence.save.manager.FileManager;
import java.util.Iterator;

public class LoadConfig {
   public LoadConfig() {
      this.gui();
      this.modules();
      this.keybinds();
   }

   private void gui() {
      String[] toSearch = new String[]{"Button", "Slider", "Mode"};

      try {
         Iterator var3 = FileManager.gui.valueString.getValues().iterator();

         while(var3.hasNext()) {
            Value e = (Value)var3.next();
            String[] var7 = toSearch;
            int var6 = toSearch.length;

            for(int var5 = 0; var5 < var6; ++var5) {
               String s = var7[var5];
               if(e.getValue().getName().contains(s)) {
                  String a = e.getValue().getName().replaceFirst(s, "");
                  String name = a.split(":")[0];
                  Module module = Module.getByName(a.split(":")[1]);
                  VSetting vSetting = VSetting.getByName(name, module);
                  if(module != null && vSetting != null) {
                     System.out.println(vSetting.getName() + ":" + vSetting.getModule().getName());
                     if(s.equalsIgnoreCase("Slider")) {
                        vSetting.current = e.getValue().getDouble().doubleValue();
                     } else if(s.equalsIgnoreCase("Button")) {
                        vSetting.toggled = e.getValue().getBoolean().booleanValue();
                     } else if(s.equalsIgnoreCase("Mode")) {
                        System.out.println(module.getName());
                        vSetting.activeMode = e.getValue().getString();
                     }
                  }
               }
            }
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }

   private void modules() {
      try {
         Iterator var2 = FileManager.toggledModules.valueString.getValues().iterator();

         while(var2.hasNext()) {
            Value value = (Value)var2.next();
            Module module = Module.getByName(value.getValue().getName());
            if(module != null && module != Module.getByName("ESP") && module != Module.getByName("ItemESP")) {
               module.toggled = value.getValue().getBoolean().booleanValue();
            }
         }
      } catch (Exception var4) {
         ;
      }

   }

   private void keybinds() {
      try {
         Iterator var2 = FileManager.moduleKeybinds.valueString.getValues().iterator();

         while(var2.hasNext()) {
            Value value = (Value)var2.next();
            Module module = Module.getByName(value.getValue().getName());
            if(module != null) {
               module.keybind = value.getValue().getInt().intValue();
            }
         }
      } catch (Exception var4) {
         ;
      }

   }
}
