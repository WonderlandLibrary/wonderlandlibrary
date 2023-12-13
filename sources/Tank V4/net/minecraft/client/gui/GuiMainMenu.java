package net.minecraft.client.gui;

import java.awt.Color;
import java.io.IOException;
import my.NewSnake.Tank.me.tireman.hexa.alts.GuiAltManager;
import my.NewSnake.utils.MainMenuUtil;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GLContext;

public class GuiMainMenu extends GuiScreen {
   private String openGLWarning1;
   private int field_92022_t;
   private int field_92020_v;
   private String openGLWarningLink;
   private ResourceLocation background = new ResourceLocation("textures/Azul.png");
   public static final String field_96138_a;
   private int field_92023_s;
   private final Object threadLock = new Object();
   private int field_92024_r;
   private int field_92019_w;
   private String openGLWarning2;
   private int field_92021_u;

   static {
      field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.id == 0) {
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
      }

      if (var1.id == 5) {
         this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
      }

      if (var1.id == 1) {
         this.mc.displayGuiScreen(new GuiSelectWorld(this));
      }

      if (var1.id == 2) {
         this.mc.displayGuiScreen(new GuiMultiplayer(this));
      }

      if (var1.id == 3) {
         this.mc.displayGuiScreen(new GuiAltManager());
      }

      if (var1.id == 4) {
         this.mc.shutdown();
      }

   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void drawScreen(int var1, int var2, float var3) {
      MainMenuUtil.drawImg(this.background, 0, 0, width, height);
      String var4 = "Tank V4";
      MainMenuUtil.drawString(5.0D, "Tank V4", (float)(width / 10 - this.fontRendererObj.getStringWidth("Tank V4") / 2), (float)(height / 20), Color.WHITE.getRGB());
      String var5 = "TankV4 Developer By ItzSnakexyz.";
      this.drawString(this.fontRendererObj, var5, 2, height - 10, -1);
      String var6 = " ";
      this.drawString(this.fontRendererObj, var6, width - this.fontRendererObj.getStringWidth(var6) - 2, height - 10, -1);
      String var7 = "";
      this.drawString(this.fontRendererObj, var7, 2, 2, Color.WHITE.getRGB());
      if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
         drawRect((double)(this.field_92022_t - 2), (double)(this.field_92021_u - 2), (double)(this.field_92020_v + 2), (double)(this.field_92019_w - 1), 1428160512);
         this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
         this.drawString(this.fontRendererObj, this.openGLWarning2, (width - this.field_92024_r) / 2, ((GuiButton)this.buttonList.get(0)).yPosition - 12, -1);
      }

      super.drawScreen(var1, var2, var3);
   }

   protected void keyTyped(char var1, int var2) throws IOException {
   }

   public void initGui() {
      byte var1 = 24;
      int var2 = height / 4 + 48;
      this.buttonList.add(new GuiButton(0, width / 2 - 100, var2 + 72 + 12, 98, 20, I18n.format("menu.options")));
      this.buttonList.add(new GuiButton(4, width / 2 + 2, var2 + 72 + 12, 98, 20, I18n.format("menu.quit")));
      this.buttonList.add(new GuiButtonLanguage(5, width / 2 - 124, var2 + 72 + 12));
      this.buttonList.add(new GuiButton(1, width / 2 - 100, var2, I18n.format("menu.singleplayer")));
      this.buttonList.add(new GuiButton(2, width / 2 - 100, var2 + var1 * 1, I18n.format("menu.multiplayer")));
      this.buttonList.add(new GuiButton(3, width / 2 - 100, var2 + var1 * 2, I18n.format("Trocar De Conta")));
      Object var3;
      synchronized(var3 = this.threadLock){}
      this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
      this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
      int var4 = Math.max(this.field_92023_s, this.field_92024_r);
      this.field_92022_t = (width - var4) / 2;
      this.field_92021_u = ((GuiButton)this.buttonList.get(0)).yPosition - 24;
      this.field_92020_v = this.field_92022_t + var4;
      this.field_92019_w = this.field_92021_u + 24;
      this.mc.func_181537_a(false);
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      super.mouseClicked(var1, var2, var3);
      Object var4;
      synchronized(var4 = this.threadLock){}
      if (this.openGLWarning1.length() > 0 && var1 >= this.field_92022_t && var1 <= this.field_92020_v && var2 >= this.field_92021_u && var2 <= this.field_92019_w) {
         GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
         var5.disableSecurityWarning();
         this.mc.displayGuiScreen(var5);
      }

   }

   public GuiMainMenu() {
      this.openGLWarning2 = field_96138_a;
      this.openGLWarning1 = "";
      if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
         this.openGLWarning1 = I18n.format("title.oldgl1");
         this.openGLWarning2 = I18n.format("title.oldgl2");
         this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
      }

   }
}
