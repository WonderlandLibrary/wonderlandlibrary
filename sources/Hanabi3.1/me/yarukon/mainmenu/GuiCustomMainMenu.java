package me.yarukon.mainmenu;

import cn.hanabi.Client;
import cn.hanabi.Hanabi;
import cn.hanabi.altmanager.GuiAltManager;
import cn.hanabi.utils.ParticleUtils;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.fontmanager.HanabiFonts;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import me.yarukon.BlurBuffer;
import me.yarukon.YRenderUtil;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;

public class GuiCustomMainMenu extends GuiScreen {
   public ArrayList butt = new ArrayList();
   private float currentX;
   private float currentY;
   private ScaledResolution res;

   public void initGui() {
      this.butt.clear();
      this.butt.add(new GuiMainMenuButton(this, 0, "G", "SinglePlayer", () -> {
         this.mc.displayGuiScreen(new GuiSelectWorld(this));
      }));
      this.butt.add(new GuiMainMenuButton(this, 1, "H", "MultiPlayer", () -> {
         this.mc.displayGuiScreen(new GuiMultiplayer(this));
      }));
      this.butt.add(new GuiMainMenuButton(this, 2, "I", "AltManager", () -> {
         this.mc.displayGuiScreen(new GuiAltManager());
      }));
      this.butt.add(new GuiMainMenuButton(this, 3, "J", "Mods", () -> {
         this.mc.displayGuiScreen(new GuiModList(this));
      }, 0.5F));
      this.butt.add(new GuiMainMenuButton(this, 4, "K", "Options", () -> {
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
      }));
      this.butt.add(new GuiMainMenuButton(this, 5, "L", "Languages", () -> {
         this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
      }));
      this.butt.add(new GuiMainMenuButton(this, 6, "M", "Quit", () -> {
         this.mc.shutdown();
      }));
      this.res = new ScaledResolution(this.mc);
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawGradientRect(0, 0, this.width, this.height, 16777215, 16777215);
      int h = this.height;
      int w = this.width;
      float xDiff = ((float)(mouseX - h / 2) - this.currentX) / (float)this.res.getScaleFactor();
      float yDiff = ((float)(mouseY - w / 2) - this.currentY) / (float)this.res.getScaleFactor();
      this.currentX += xDiff * 0.3F;
      this.currentY += yDiff * 0.3F;
      GlStateManager.translate(this.currentX / 30.0F, this.currentY / 15.0F, 0.0F);
      if (!Client.onDebug) {
         RenderUtil.drawImage(new ResourceLocation("Client/mainmenu/mainmenu.png"), -30, -30, this.res.getScaledWidth() + 60, this.res.getScaledHeight() + 60);
      } else {
         RenderUtil.drawImage(new ResourceLocation("Client/mainmenu/scifi.png"), -30, -30, this.res.getScaledWidth() + 60, this.res.getScaledHeight() + 60);
      }

      GlStateManager.translate(-this.currentX / 30.0F, -this.currentY / 15.0F, 0.0F);
      BlurBuffer.updateBlurBuffer(true);
      ParticleUtils.drawParticles(mouseX, mouseY);
      BlurBuffer.blurArea((float)this.width / 2.0F - 50.0F * ((float)this.butt.size() / 2.0F), (float)this.height / 2.0F - 50.0F, (float)(50 * this.butt.size()), 100.0F, true);
      YRenderUtil.drawRectNormal((float)this.width / 2.0F - 50.0F * ((float)this.butt.size() / 2.0F), (float)this.height / 2.0F - 50.0F, (float)this.width / 2.0F + 50.0F * ((float)this.butt.size() / 2.0F), (float)this.height / 2.0F + 50.0F, 2097152000);
      YRenderUtil.drawRectNormal((float)this.width / 2.0F - 50.0F * ((float)this.butt.size() / 2.0F), (float)this.height / 2.0F + 20.0F, (float)this.width / 2.0F + 50.0F * ((float)this.butt.size() / 2.0F), (float)this.height / 2.0F + 50.0F, 1040187392);
      float startX = (float)this.width / 2.0F - 50.0F * ((float)this.butt.size() / 2.0F);

      for(Iterator var9 = this.butt.iterator(); var9.hasNext(); startX += 50.0F) {
         GuiMainMenuButton button = (GuiMainMenuButton)var9.next();
         button.draw(startX, (float)this.height / 2.0F + 20.0F, mouseX, mouseY);
      }

      Hanabi.INSTANCE.fontManager.icon130.drawString(HanabiFonts.ICON_HANABI_LOGO, (float)this.width / 2.0F - 50.0F * ((float)this.butt.size() / 2.0F) + 10.0F, (float)this.height / 2.0F + 5.0F, -13671171);
      Hanabi.INSTANCE.fontManager.usans25.drawString("Hanabi client", (float)this.width / 2.0F - 50.0F * ((float)this.butt.size() / 2.0F) + 80.0F, (float)this.height / 2.0F - 30.0F, -1);
      Hanabi.INSTANCE.fontManager.usans20.drawString("Build 3.1", (float)this.width / 2.0F - 50.0F * ((float)this.butt.size() / 2.0F) + 80.0F, (float)this.height / 2.0F - 10.0F, -1);
      String s = "Logged in as " + Client.username;
      Hanabi.INSTANCE.fontManager.usans20.drawString(s, (float)this.width / 2.0F + 50.0F * ((float)this.butt.size() / 2.0F) - (float)Hanabi.INSTANCE.fontManager.usans20.getStringWidth(s) - 10.0F, (float)this.height / 2.0F + 5.0F, -1);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      Iterator var4 = this.butt.iterator();

      while(var4.hasNext()) {
         GuiMainMenuButton button = (GuiMainMenuButton)var4.next();
         button.mouseClick(mouseX, mouseY, mouseButton);
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void updateScreen() {
      this.res = new ScaledResolution(this.mc);
      super.updateScreen();
   }
}
