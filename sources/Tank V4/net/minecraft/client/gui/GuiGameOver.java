package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class GuiGameOver extends GuiScreen implements GuiYesNoCallback {
   private int enableButtonsTimer;
   private boolean field_146346_f = false;

   public void updateScreen() {
      super.updateScreen();
      ++this.enableButtonsTimer;
      GuiButton var1;
      if (this.enableButtonsTimer == 20) {
         for(Iterator var2 = this.buttonList.iterator(); var2.hasNext(); var1.enabled = true) {
            var1 = (GuiButton)var2.next();
         }
      }

   }

   public void confirmClicked(boolean var1, int var2) {
      if (var1) {
         Minecraft.theWorld.sendQuittingDisconnectingPacket();
         this.mc.loadWorld((WorldClient)null);
         this.mc.displayGuiScreen(new GuiMainMenu());
      } else {
         Minecraft.thePlayer.respawnPlayer();
         this.mc.displayGuiScreen((GuiScreen)null);
      }

   }

   public void drawScreen(int var1, int var2, float var3) {
      drawGradientRect(0.0D, 0.0D, (float)width, (float)height, 1615855616, -1602211792);
      GlStateManager.pushMatrix();
      GlStateManager.scale(2.0F, 2.0F, 2.0F);
      boolean var4 = Minecraft.theWorld.getWorldInfo().isHardcoreModeEnabled();
      String var5 = var4 ? I18n.format("deathScreen.title.hardcore") : I18n.format("deathScreen.title");
      this.drawCenteredString(this.fontRendererObj, var5, width / 2 / 2, 30, 16777215);
      GlStateManager.popMatrix();
      if (var4) {
         this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.hardcoreInfo"), width / 2, 144, 16777215);
      }

      this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score") + ": " + EnumChatFormatting.YELLOW + Minecraft.thePlayer.getScore(), width / 2, 100, 16777215);
      super.drawScreen(var1, var2, var3);
   }

   protected void keyTyped(char var1, int var2) throws IOException {
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      switch(var1.id) {
      case 0:
         Minecraft.thePlayer.respawnPlayer();
         this.mc.displayGuiScreen((GuiScreen)null);
         break;
      case 1:
         if (Minecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            this.mc.displayGuiScreen(new GuiMainMenu());
         } else {
            GuiYesNo var2 = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm"), "", I18n.format("deathScreen.titleScreen"), I18n.format("deathScreen.respawn"), 0);
            this.mc.displayGuiScreen(var2);
            var2.setButtonDelay(20);
         }
      }

   }

   public void initGui() {
      this.buttonList.clear();
      if (Minecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
         if (this.mc.isIntegratedServerRunning()) {
            this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96, I18n.format("deathScreen.deleteWorld")));
         } else {
            this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96, I18n.format("deathScreen.leaveServer")));
         }
      } else {
         this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 72, I18n.format("deathScreen.respawn")));
         this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96, I18n.format("deathScreen.titleScreen")));
         if (this.mc.getSession() == null) {
            ((GuiButton)this.buttonList.get(1)).enabled = false;
         }
      }

      GuiButton var1;
      for(Iterator var2 = this.buttonList.iterator(); var2.hasNext(); var1.enabled = false) {
         var1 = (GuiButton)var2.next();
      }

   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
