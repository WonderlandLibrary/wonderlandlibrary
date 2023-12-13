package org.alphacentauri.modules;

import net.minecraft.src.Config;
import org.alphacentauri.AC;
import org.alphacentauri.management.modules.Module;

public class ModulePlayerESP extends Module {
   public ModulePlayerESP() {
      super("PlayerESP", "See Player outlines though walls", new String[]{"playeresp"}, Module.Category.Render, 5447611);
   }

   public void setEnabledSilent(boolean enabled) {
      if(Config.isFastRender()) {
         enabled = false;
         AC.addChat(this.getName(), "Please disable Fast Render in Video Settings");
      } else if(Config.isShaders()) {
         enabled = false;
         AC.addChat(this.getName(), "Please disable Shaders");
      } else if(Config.isAntialiasing()) {
         enabled = false;
         AC.addChat(this.getName(), "Please disable AntiAliasing in Video Settings");
      }

      super.setEnabledSilent(enabled);
   }
}
