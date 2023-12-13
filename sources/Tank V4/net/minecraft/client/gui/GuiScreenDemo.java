package net.minecraft.client.gui;

import java.io.IOException;
import java.net.URI;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiScreenDemo extends GuiScreen {
   private static final ResourceLocation field_146348_f = new ResourceLocation("textures/gui/demo_background.png");
   private static final Logger logger = LogManager.getLogger();

   protected void actionPerformed(GuiButton var1) throws IOException {
      switch(var1.id) {
      case 1:
         var1.enabled = false;

         try {
            Class var2 = Class.forName("java.awt.Desktop");
            Object var3 = var2.getMethod("getDesktop").invoke((Object)null);
            var2.getMethod("browse", URI.class).invoke(var3, new URI("http://www.minecraft.net/store?source=demo"));
         } catch (Throwable var4) {
            logger.error("Couldn't open link", var4);
         }
         break;
      case 2:
         this.mc.displayGuiScreen((GuiScreen)null);
         this.mc.setIngameFocus();
      }

   }

   public void initGui() {
      this.buttonList.clear();
      byte var1 = -16;
      this.buttonList.add(new GuiButton(1, width / 2 - 116, height / 2 + 62 + var1, 114, 20, I18n.format("demo.help.buy")));
      this.buttonList.add(new GuiButton(2, width / 2 + 2, height / 2 + 62 + var1, 114, 20, I18n.format("demo.help.later")));
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      int var4 = (width - 248) / 2 + 10;
      int var5 = (height - 166) / 2 + 8;
      this.fontRendererObj.drawString(I18n.format("demo.help.title"), (double)var4, (double)var5, 2039583);
      var5 += 12;
      GameSettings var6 = this.mc.gameSettings;
      this.fontRendererObj.drawString(I18n.format("demo.help.movementShort", GameSettings.getKeyDisplayString(var6.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(var6.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(var6.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(var6.keyBindRight.getKeyCode())), (double)var4, (double)var5, 5197647);
      this.fontRendererObj.drawString(I18n.format("demo.help.movementMouse"), (double)var4, (double)(var5 + 12), 5197647);
      this.fontRendererObj.drawString(I18n.format("demo.help.jump", GameSettings.getKeyDisplayString(var6.keyBindJump.getKeyCode())), (double)var4, (double)(var5 + 24), 5197647);
      this.fontRendererObj.drawString(I18n.format("demo.help.inventory", GameSettings.getKeyDisplayString(var6.keyBindInventory.getKeyCode())), (double)var4, (double)(var5 + 36), 5197647);
      this.fontRendererObj.drawSplitString(I18n.format("demo.help.fullWrapped"), var4, var5 + 68, 218, 2039583);
      super.drawScreen(var1, var2, var3);
   }

   public void drawDefaultBackground() {
      super.drawDefaultBackground();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(field_146348_f);
      int var1 = (width - 248) / 2;
      int var2 = (height - 166) / 2;
      drawTexturedModalRect(var1, var2, 0, 0, 248, 166);
   }

   public void updateScreen() {
      super.updateScreen();
   }
}
