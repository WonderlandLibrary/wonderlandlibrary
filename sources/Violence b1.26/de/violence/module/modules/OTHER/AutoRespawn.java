package de.violence.module.modules.OTHER;

import de.violence.module.Module;
import de.violence.module.ui.Category;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;

public class AutoRespawn extends Module {
   public AutoRespawn() {
      super("AutoRespawn", Category.OTHER);
   }

   public void onFrameRender() {
      if(this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiDownloadTerrain) {
         this.mc.displayGuiScreen((GuiScreen)null);
      }

      if(this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiGameOver) {
         this.mc.currentScreen.confirmClicked(false, 0);
      }

      super.onFrameRender();
   }
}
