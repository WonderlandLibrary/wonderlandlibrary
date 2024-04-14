package de.violence.config;

import de.violence.Violence;
import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import net.minecraft.client.Minecraft;

public class SaveConfig {
   private String configName;
   private File file;
   private FileWriter fileWriter = null;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$de$violence$gui$VSetting$SettingType;

   public SaveConfig(String configName) throws Exception {
      this.configName = configName;
      this.setFile(new File(Minecraft.getMinecraft().mcDataDir + "/violence/Configs/" + this.configName.replace("§", "") + ".txt"));
      this.file.getParentFile().mkdirs();
      if(!this.configName.startsWith("§")) {
         this.setFileWriter(new FileWriter(this.file));
      }

   }

   public void setFileWriter(FileWriter fileWriter) {
      this.fileWriter = fileWriter;
   }

   public void setConfigName(String configName) {
      this.configName = configName;
   }

   public void setFile(File file) {
      this.file = file;
   }

   public File getFile() {
      return this.file;
   }

   public String getConfigName() {
      return this.configName;
   }

   public void saveConfig() throws Exception {
      this.writeString("client_Version:" + Violence.VERSION);
      Iterator var2 = VSetting.getSettings().iterator();

      while(var2.hasNext()) {
         VSetting modules = (VSetting)var2.next();
         switch($SWITCH_TABLE$de$violence$gui$VSetting$SettingType()[modules.getSettingType().ordinal()]) {
         case 1:
            this.writeString("B:" + modules.getName() + ":" + modules.isToggled() + ":" + modules.getModule().getName());
            break;
         case 2:
            this.writeString("S:" + modules.getName() + ":" + modules.getCurrent() + ":" + modules.getModule().getName());
            break;
         case 3:
            this.writeString("M:" + modules.getName() + ":" + modules.getActiveMode() + ":" + modules.getModule().getName());
         }
      }

      var2 = Module.getModuleList().iterator();

      while(var2.hasNext()) {
         Module modules1 = (Module)var2.next();
         if(modules1.getCategory() != Category.CONFIGS) {
            this.writeString("Module:" + modules1.getName() + ":" + modules1.isToggled());
         }
      }

      this.fileWriter.close();
   }

   public void loadConfig() {
      (new Thread(new Runnable() {
         public void run() {
            try {
               Iterator var2 = SaveConfig.this.readFile().iterator();

               while(true) {
                  while(var2.hasNext()) {
                     String e = (String)var2.next();
                     String type = e.split(":")[0];
                     String name = e.split(":")[1];
                     if(type.equalsIgnoreCase("client_Version")) {
                        if(!name.replace("B", "").replace("b", "").equalsIgnoreCase(Violence.VERSION.replace("b", ""))) {
                           Violence.getViolence().sendChat("§cThe config was created for the version §3" + name.replace("b", "") + "§c.");
                           return;
                        }
                     } else {
                        String object = e.split(":")[2];
                        if(type.equalsIgnoreCase("client_Version") && !name.replace("B", "").replace("b", "").equalsIgnoreCase(Violence.VERSION.replace("b", ""))) {
                           Violence.getViolence().sendChat("§cThe config was created for the version §3" + name.replace("b", "") + "§c.");
                           return;
                        }

                        if(type.equalsIgnoreCase("Module")) {
                           Module anotherName = Module.getByName(name);
                           if(anotherName != null) {
                              anotherName.setToggled(Boolean.parseBoolean(String.valueOf(object)));
                           }
                        } else {
                           Module module;
                           VSetting vSetting;
                           String anotherName1;
                           if(type.equalsIgnoreCase("B")) {
                              anotherName1 = e.split(":")[3];
                              module = Module.getByName(anotherName1);
                              if(module == null) {
                                 continue;
                              }

                              vSetting = VSetting.getByName(name, module);
                              vSetting.toggled = Boolean.parseBoolean(String.valueOf(object));
                           } else if(type.equalsIgnoreCase("S")) {
                              anotherName1 = e.split(":")[3];
                              module = Module.getByName(anotherName1);
                              if(module == null) {
                                 continue;
                              }

                              vSetting = VSetting.getByName(name, module);
                              vSetting.current = Double.parseDouble(String.valueOf(object));
                           } else if(type.equalsIgnoreCase("M")) {
                              anotherName1 = e.split(":")[3];
                              module = Module.getByName(anotherName1);
                              if(module == null) {
                                 continue;
                              }

                              vSetting = VSetting.getByName(name, module);
                              vSetting.activeMode = String.valueOf(object);
                           }
                        }

                        Violence.getViolence().sendChat("§3" + name + " §7changed to: §3" + object);
                     }
                  }

                  return;
               }
            } catch (Exception var9) {
               var9.printStackTrace();
               Violence.getViolence().sendChat("§cError.");
            }
         }
      })).start();
   }

   private List readFile() throws Exception {
      Scanner scanner = new Scanner(this.file);
      ArrayList arrayList = new ArrayList();

      while(scanner.hasNextLine()) {
         arrayList.add(scanner.nextLine());
      }

      scanner.close();
      return arrayList;
   }

   private void writeString(String s) throws Exception {
      this.fileWriter.write(s + "\n");
      this.fileWriter.flush();
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$de$violence$gui$VSetting$SettingType() {
      int[] var10000 = $SWITCH_TABLE$de$violence$gui$VSetting$SettingType;
      if($SWITCH_TABLE$de$violence$gui$VSetting$SettingType != null) {
         return var10000;
      } else {
         int[] var0 = new int[VSetting.SettingType.values().length];

         try {
            var0[VSetting.SettingType.BUTTON.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[VSetting.SettingType.MODE.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[VSetting.SettingType.SLIDER.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$de$violence$gui$VSetting$SettingType = var0;
         return var0;
      }
   }
}
