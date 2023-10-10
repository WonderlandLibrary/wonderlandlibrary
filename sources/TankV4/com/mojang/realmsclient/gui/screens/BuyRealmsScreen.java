package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsState;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class BuyRealmsScreen extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private RealmsScreen lastScreen;
   private static int BACK_BUTTON_ID = 111;
   private volatile RealmsState realmsStatus;
   private boolean onLink = false;

   public BuyRealmsScreen(RealmsScreen var1) {
      this.lastScreen = var1;
   }

   public void tick() {
      super.tick();
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      short var1 = 212;
      this.buttonsAdd(newButton(BACK_BUTTON_ID, this.width() / 2 - var1 / 2, 180, var1, 20, getLocalizedString("gui.back")));
      this.fetchMessage();
   }

   private void fetchMessage() {
      RealmsClient var1 = RealmsClient.createRealmsClient();
      (new Thread(this, "Realms-stat-message", var1) {
         final RealmsClient val$client;
         final BuyRealmsScreen this$0;

         {
            this.this$0 = var1;
            this.val$client = var3;
         }

         public void run() {
            try {
               BuyRealmsScreen.access$002(this.this$0, this.val$client.fetchRealmsState());
            } catch (RealmsServiceException var2) {
               BuyRealmsScreen.access$100().error("Could not get stat");
            }

         }
      }).start();
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == BACK_BUTTON_ID) {
            Realms.setScreen(this.lastScreen);
         }

      }
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         Realms.setScreen(this.lastScreen);
      }

   }

   public void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      if (this.onLink) {
         Clipboard var4 = Toolkit.getDefaultToolkit().getSystemClipboard();
         var4.setContents(new StringSelection(this.realmsStatus.getBuyLink()), (ClipboardOwner)null);
         this.browseTo(this.realmsStatus.getBuyLink());
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

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(getLocalizedString("mco.buy.realms.title"), this.width() / 2, 11, 16777215);
      if (this.realmsStatus != null) {
         String[] var4 = this.realmsStatus.getStatusMessage().split("\n");
         int var5 = 52;
         String[] var6 = var4;
         int var7 = var4.length;

         int var8;
         for(var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            this.drawCenteredString(var9, this.width() / 2, var5, 10526880);
            var5 += 18;
         }

         if (this.realmsStatus.getBuyLink() != null) {
            String var14 = this.realmsStatus.getBuyLink();
            var7 = 3368635;
            var8 = 7107012;
            var5 += 18;
            int var15 = this.fontWidth(var14);
            int var10 = this.width() / 2 - var15 / 2 - 1;
            int var11 = var5 - 1;
            int var12 = var10 + var15 + 1;
            int var13 = var5 + 1 + this.fontLineHeight();
            if (var10 <= var1 && var1 <= var12 && var11 <= var2 && var2 <= var13) {
               this.onLink = true;
               this.drawString("§n" + var14, this.width() / 2 - var15 / 2, var5, var8);
            } else {
               this.onLink = false;
               this.drawString(var14, this.width() / 2 - var15 / 2, var5, var7);
            }
         }

         super.render(var1, var2, var3);
      }
   }

   static RealmsState access$002(BuyRealmsScreen var0, RealmsState var1) {
      return var0.realmsStatus = var1;
   }

   static Logger access$100() {
      return LOGGER;
   }
}
