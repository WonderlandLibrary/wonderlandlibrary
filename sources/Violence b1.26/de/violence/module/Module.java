package de.violence.module;

import de.violence.Violence;
import de.violence.config.SaveConfig;
import de.violence.module.ui.Category;
import de.violence.save.manager.FileManager;
import de.violence.ui.Colours;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class Module {
   public String displayName;
   private String name;
   public int keybind;
   private Category category;
   public boolean toggled;
   public Minecraft mc = Minecraft.getMinecraft();
   public int x;
   public int moduleColor;
   public String nameAddon;
   static List moduleList = new ArrayList();
   protected long lastUpdate;

   public static List getModuleList() {
      return moduleList;
   }

   public static Module getByName(String name) {
      Iterator var2 = getModuleList().iterator();

      while(var2.hasNext()) {
         Module modules = (Module)var2.next();
         if(modules.getName().equalsIgnoreCase(name)) {
            return modules;
         }
      }

      return null;
   }

   public Module(String name, Category category) {
      this.name = name;
      this.displayName = name;
      this.category = category;
      moduleList.add(this);
      this.moduleColor = Colours.getColor((new Random()).nextInt(191) + 64, (new Random()).nextInt(191) + 64, (new Random()).nextInt(64) + 191, 255);
   }

   public Module(String name, Category category, int key) {
      this.keybind = key;
      this.name = name;
      this.displayName = name;
      this.category = category;
      moduleList.add(this);
      this.moduleColor = Colours.getColor((new Random()).nextInt(191) + 64, (new Random()).nextInt(191) + 64, (new Random()).nextInt(64) + 191, 255);
   }

   public void onToggle() {
      if(this.getCategory() == Category.CONFIGS) {
         try {
            (new SaveConfig("§" + this.getName())).loadConfig();
         } catch (Exception var2) {
            Violence.getViolence().sendChat("§cError while loading config.");
         }

      } else {
         this.toggled = !this.toggled;
         FileManager.toggledModules.setBoolean(this.name, Boolean.valueOf(this.toggled));
         FileManager.toggledModules.save();
         if(this.toggled) {
            this.onEnable();
         } else {
            this.onDisable();
            this.x = 0;
         }

      }
   }

   public void onWalkUpdate() {
   }

   public void sendModuleChat(String s) {
      Violence.getViolence().sendChat("§7[§e" + this.name + "§7] §f" + s);
   }

   public void onUpdate() {
      if(this.nameAddon != null) {
         this.displayName = this.name + " " + this.nameAddon;
      }

      this.lastUpdate = System.currentTimeMillis();
   }

   public void setKeybind(int keybind) {
      this.keybind = keybind;
      FileManager.moduleKeybinds.setInteger(this.name, Integer.valueOf(this.keybind));
      FileManager.moduleKeybinds.save();
   }

   public void portMove(float yaw, float multiplyer, float up) {
      double moveX = -Math.sin(Math.toRadians((double)yaw)) * (double)multiplyer;
      double moveZ = Math.cos(Math.toRadians((double)yaw)) * (double)multiplyer;
      double moveY = (double)up;
      this.mc.thePlayer.setPosition(moveX + this.mc.thePlayer.posX, moveY + this.mc.thePlayer.posY, moveZ + this.mc.thePlayer.posZ);
   }

   public void move(float yaw, float multiplyer, float up) {
      double moveX = -Math.sin(Math.toRadians((double)yaw)) * (double)multiplyer;
      double moveZ = Math.cos(Math.toRadians((double)yaw)) * (double)multiplyer;
      this.mc.thePlayer.motionX = moveX;
      this.mc.thePlayer.motionY = (double)up;
      this.mc.thePlayer.motionZ = moveZ;
   }

   public void move(float yaw, float multiplyer) {
      double moveX = -Math.sin(Math.toRadians((double)yaw)) * (double)multiplyer;
      double moveZ = Math.cos(Math.toRadians((double)yaw)) * (double)multiplyer;
      this.mc.thePlayer.motionX = moveX;
      this.mc.thePlayer.motionZ = moveZ;
   }

   public void onFrameRender() {
   }

   public void onDisable() {
   }

   public void onEnable() {
   }

   public void setToggled(boolean toggled) {
      if(this.getCategory() == Category.CONFIGS) {
         try {
            (new SaveConfig("§" + this.getName())).loadConfig();
         } catch (Exception var3) {
            Violence.getViolence().sendChat("§cError while loading config.");
         }

      } else {
         this.toggled = toggled;
         FileManager.toggledModules.setBoolean(this.name, Boolean.valueOf(this.toggled));
         FileManager.toggledModules.save();
         if(this.toggled) {
            this.onEnable();
         } else {
            this.onDisable();
            this.x = 0;
         }

      }
   }

   public boolean isToggled() {
      return this.toggled;
   }

   public String getName() {
      return this.name;
   }

   public int getKeybind() {
      return this.keybind;
   }

   public Category getCategory() {
      return this.category;
   }
}
