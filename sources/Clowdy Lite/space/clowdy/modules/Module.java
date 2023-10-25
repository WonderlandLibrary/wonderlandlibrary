package space.clowdy.modules;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class Module {
     public String name;
     public Minecraft mc = Minecraft.getInstance();
     public int key;
     public boolean enabled;
     public String description;
     public Category category;

     public void toggle(boolean enabledState) {
          this.enabled = enabledState;
          if (this.enabled) {
               this.onEnable();
          } else {
               this.onDisable();
          }

     }

     public void setName(String name) {
          this.name = name;
     }

     public void setKey(int key) {
          this.key = key;
     }

     public void onDisable() {
          MinecraftForge.EVENT_BUS.unregister(this);
     }

     public Module(String name, String desc, int key, Category cat) {
          this.name = name;
          this.key = key;
          this.category = cat;
          this.description = desc;
     }

     public void onEnable() {
          MinecraftForge.EVENT_BUS.register(this);
     }

     public Category getCategory() {
          return this.category;
     }

     public String getName() {
          return this.name;
     }

     public boolean isEnabled() {
          return this.enabled;
     }

     public void toggle() {
          this.enabled = !this.enabled;
          if (this.enabled) {
               this.onEnable();
          } else {
               this.onDisable();
          }

     }

     public int getKey() {
          return this.key;
     }
}
