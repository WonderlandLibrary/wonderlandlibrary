package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.Tezzelator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class RealmsWorldTemplateScreen extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String PLUS_ICON_LOCATION = "realms:textures/gui/realms/plus_icon.png";
   private final ScreenWithCallback lastScreen;
   private WorldTemplate selectedWorldTemplate;
   private List templates = Collections.emptyList();
   private RealmsWorldTemplateScreen.WorldTemplateSelectionList worldTemplateSelectionList;
   private int selectedTemplate = -1;
   private static final int BACK_BUTTON_ID = 0;
   private static final int SELECT_BUTTON_ID = 1;
   private RealmsButton selectButton;
   private String toolTip = null;
   private boolean isMiniGame;

   public RealmsWorldTemplateScreen(ScreenWithCallback var1, WorldTemplate var2, boolean var3) {
      this.lastScreen = var1;
      this.selectedWorldTemplate = var2;
      this.isMiniGame = var3;
   }

   public void mouseEvent() {
      super.mouseEvent();
      this.worldTemplateSelectionList.mouseEvent();
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.worldTemplateSelectionList = new RealmsWorldTemplateScreen.WorldTemplateSelectionList(this);
      boolean var1 = this.isMiniGame;
      (new Thread(this, "Realms-minigame-fetcher", var1) {
         final boolean val$isMiniGame;
         final RealmsWorldTemplateScreen this$0;

         {
            this.this$0 = var1;
            this.val$isMiniGame = var3;
         }

         public void run() {
            RealmsClient var1 = RealmsClient.createRealmsClient();

            try {
               if (this.val$isMiniGame) {
                  RealmsWorldTemplateScreen.access$002(this.this$0, var1.fetchMinigames().templates);
               } else {
                  RealmsWorldTemplateScreen.access$002(this.this$0, var1.fetchWorldTemplates().templates);
               }
            } catch (RealmsServiceException var3) {
               RealmsWorldTemplateScreen.access$100().error("Couldn't fetch templates");
            }

         }
      }).start();
      this.postInit();
   }

   private void postInit() {
      this.buttonsAdd(newButton(0, this.width() / 2 + 6, this.height() - 52, 153, 20, getLocalizedString("gui.cancel")));
      this.buttonsAdd(this.selectButton = newButton(1, this.width() / 2 - 154, this.height() - 52, 153, 20, getLocalizedString("mco.template.button.select")));
   }

   public void tick() {
      super.tick();
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 1) {
            this.selectTemplate();
         } else if (var1.id() == 0) {
            this.backButtonClicked();
         }

      }
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         this.backButtonClicked();
      }

   }

   private void backButtonClicked() {
      this.lastScreen.callback((Object)null);
      Realms.setScreen(this.lastScreen);
   }

   private void selectTemplate() {
      if (this.selectedTemplate >= 0 && this.selectedTemplate < this.templates.size()) {
         this.lastScreen.callback(this.templates.get(this.selectedTemplate));
         Realms.setScreen(this.lastScreen);
      }

   }

   public void render(int var1, int var2, float var3) {
      this.toolTip = null;
      this.renderBackground();
      this.worldTemplateSelectionList.render(var1, var2, var3);
      String var4 = "";
      if (this.isMiniGame) {
         var4 = getLocalizedString("mco.template.title.minigame");
      } else {
         var4 = getLocalizedString("mco.template.title");
      }

      this.drawCenteredString(var4, this.width() / 2, 20, 16777215);
      super.render(var1, var2, var3);
      if (this.toolTip != null) {
         this.renderMousehoverTooltip(this.toolTip, var1, var2);
      }

   }

   private void browseTo(String var1) {
      try {
         URI var2 = new URI(var1);
         Class var3 = Class.forName("java.awt.Desktop");
         Object var4 = var3.getMethod("getDesktop").invoke((Object)null);
         var3.getMethod("browse", URI.class).invoke(var4, var2);
      } catch (Throwable var5) {
         LOGGER.error("Couldn't open link");
      }

   }

   protected void renderMousehoverTooltip(String var1, int var2, int var3) {
      if (var1 != null) {
         int var4 = var2 + 12;
         int var5 = var3 - 12;
         int var6 = this.fontWidth(var1);
         this.fillGradient(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
         this.fontDrawShadow(var1, var4, var5, -1);
      }
   }

   static List access$002(RealmsWorldTemplateScreen var0, List var1) {
      return var0.templates = var1;
   }

   static Logger access$100() {
      return LOGGER;
   }

   static List access$000(RealmsWorldTemplateScreen var0) {
      return var0.templates;
   }

   static int access$202(RealmsWorldTemplateScreen var0, int var1) {
      return var0.selectedTemplate = var1;
   }

   static WorldTemplate access$302(RealmsWorldTemplateScreen var0, WorldTemplate var1) {
      return var0.selectedWorldTemplate = var1;
   }

   static WorldTemplate access$300(RealmsWorldTemplateScreen var0) {
      return var0.selectedWorldTemplate;
   }

   static int access$200(RealmsWorldTemplateScreen var0) {
      return var0.selectedTemplate;
   }

   static void access$400(RealmsWorldTemplateScreen var0, String var1) {
      var0.browseTo(var1);
   }

   static String access$502(RealmsWorldTemplateScreen var0, String var1) {
      return var0.toolTip = var1;
   }

   private class WorldTemplateSelectionList extends RealmsClickableScrolledSelectionList {
      final RealmsWorldTemplateScreen this$0;

      public WorldTemplateSelectionList(RealmsWorldTemplateScreen var1) {
         super(var1.width(), var1.height(), 32, var1.height() - 64, 36);
         this.this$0 = var1;
      }

      public int getItemCount() {
         return RealmsWorldTemplateScreen.access$000(this.this$0).size() + 1;
      }

      public void selectItem(int var1, boolean var2, int var3, int var4) {
         if (var1 < RealmsWorldTemplateScreen.access$000(this.this$0).size()) {
            RealmsWorldTemplateScreen.access$202(this.this$0, var1);
            RealmsWorldTemplateScreen.access$302(this.this$0, (WorldTemplate)null);
         }
      }

      public void customMouseEvent(int var1, int var2, int var3, float var4, int var5) {
         if (Mouse.isButtonDown(0) && this.ym() >= var1 && this.ym() <= var2) {
            int var6 = this.width() / 2 - 92;
            int var7 = this.width();
            int var8 = this.ym() - var1 - var3 + (int)var4 - 4;
            int var9 = var8 / var5;
            if (this.xm() >= var6 && this.xm() <= var7 && var9 >= 0 && var8 >= 0 && var9 < this.getItemCount()) {
               this.itemClicked(var8, var9, this.xm(), this.ym(), this.width());
            }
         }

      }

      public boolean isSelectedItem(int var1) {
         if (RealmsWorldTemplateScreen.access$000(this.this$0).size() == 0) {
            return false;
         } else if (var1 >= RealmsWorldTemplateScreen.access$000(this.this$0).size()) {
            return false;
         } else if (RealmsWorldTemplateScreen.access$300(this.this$0) != null) {
            boolean var2 = RealmsWorldTemplateScreen.access$300(this.this$0).name.equals(((WorldTemplate)RealmsWorldTemplateScreen.access$000(this.this$0).get(var1)).name);
            if (var2) {
               RealmsWorldTemplateScreen.access$202(this.this$0, var1);
            }

            return var2;
         } else {
            return var1 == RealmsWorldTemplateScreen.access$200(this.this$0);
         }
      }

      public void itemClicked(int var1, int var2, int var3, int var4, int var5) {
         int var6 = this.getScrollbarPosition() - 240 + this.this$0.fontWidth(((WorldTemplate)RealmsWorldTemplateScreen.access$000(this.this$0).get(var2)).author) + 12;
         int var7 = var1 + 30 - this.getScroll();
         if (var3 >= var6 && var3 <= var6 + 9 && var4 >= var7 && var4 <= var7 + 9 && !((WorldTemplate)RealmsWorldTemplateScreen.access$000(this.this$0).get(var2)).link.equals("")) {
            RealmsWorldTemplateScreen.access$400(this.this$0, ((WorldTemplate)RealmsWorldTemplateScreen.access$000(this.this$0).get(var2)).link);
         }

      }

      public int getMaxPosition() {
         return this.getItemCount() * 36;
      }

      public void renderBackground() {
         this.this$0.renderBackground();
      }

      public void renderItem(int var1, int var2, int var3, int var4, int var5, int var6) {
         if (var1 < RealmsWorldTemplateScreen.access$000(this.this$0).size()) {
            this.renderWorldTemplateItem(var1, var2, var3, var4);
         }

      }

      public void renderSelected(int var1, int var2, int var3, Tezzelator var4) {
         int var5 = this.getScrollbarPosition() - 240;
         int var6 = this.getScrollbarPosition() - 10;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(3553);
         var4.begin();
         var4.color(8421504);
         var4.vertexUV((double)var5, (double)(var2 + var3 + 2), 0.0D, 0.0D, 1.0D);
         var4.vertexUV((double)var6, (double)(var2 + var3 + 2), 0.0D, 1.0D, 1.0D);
         var4.vertexUV((double)var6, (double)(var2 - 2), 0.0D, 1.0D, 0.0D);
         var4.vertexUV((double)var5, (double)(var2 - 2), 0.0D, 0.0D, 0.0D);
         var4.color(0);
         var4.vertexUV((double)(var5 + 1), (double)(var2 + var3 + 1), 0.0D, 0.0D, 1.0D);
         var4.vertexUV((double)(var6 - 1), (double)(var2 + var3 + 1), 0.0D, 1.0D, 1.0D);
         var4.vertexUV((double)(var6 - 1), (double)(var2 - 1), 0.0D, 1.0D, 0.0D);
         var4.vertexUV((double)(var5 + 1), (double)(var2 - 1), 0.0D, 0.0D, 0.0D);
         var4.end();
         GL11.glEnable(3553);
      }

      private void renderWorldTemplateItem(int var1, int var2, int var3, int var4) {
         WorldTemplate var5 = (WorldTemplate)RealmsWorldTemplateScreen.access$000(this.this$0).get(var1);
         this.this$0.drawString(var5.name, var2 + 2, var3 + 1, 16777215);
         this.this$0.drawString(var5.author, var2 + 2, var3 + 12, 7105644);
         this.this$0.drawString(var5.version, var2 + 2 + 207 - this.this$0.fontWidth(var5.version), var3 + 1, 5000268);
         int var6 = this.getScrollbarPosition() - 240 + this.this$0.fontWidth(var5.author) + 12;
         byte var7 = 12;
         if (!var5.link.equals("")) {
            this.drawInfo(var6, var3 + var7, this.xm(), this.ym());
         }

      }

      private void drawInfo(int var1, int var2, int var3, int var4) {
         RealmsScreen.bind("realms:textures/gui/realms/plus_icon.png");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPushMatrix();
         GL11.glScalef(0.5F, 0.5F, 0.5F);
         RealmsScreen.blit(var1 * 2, var2 * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
         GL11.glPopMatrix();
         if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9 && var4 < this.this$0.height() - 15 && var4 > 32) {
            RealmsWorldTemplateScreen.access$502(this.this$0, RealmsScreen.getLocalizedString("mco.template.info.tooltip"));
         }

      }
   }
}
