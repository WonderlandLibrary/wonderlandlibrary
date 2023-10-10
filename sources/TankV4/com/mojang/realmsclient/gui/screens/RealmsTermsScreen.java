package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsConnectTask;
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

public class RealmsTermsScreen extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final int AGREE_BUTTON_ID = 1;
   private static final int DISAGREE_BUTTON_ID = 2;
   private final RealmsScreen lastScreen;
   private final RealmsServer realmsServer;
   private RealmsButton agreeButton;
   private boolean onLink = false;
   private String realmsToSUrl = "https://minecraft.net/realms/terms";

   public RealmsTermsScreen(RealmsScreen var1, RealmsServer var2) {
      this.lastScreen = var1;
      this.realmsServer = var2;
   }

   public void tick() {
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      int var1 = this.width() / 4;
      int var2 = this.width() / 4 - 2;
      int var3 = this.width() / 2 + 4;
      this.buttonsAdd(this.agreeButton = newButton(1, var1, this.height() / 5 + 96 + 22, var2, 20, getLocalizedString("mco.terms.buttons.agree")));
      this.buttonsAdd(newButton(2, var3, this.height() / 5 + 96 + 22, var2, 20, getLocalizedString("mco.terms.buttons.disagree")));
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 2) {
            Realms.setScreen(this.lastScreen);
         } else if (var1.id() == 1) {
            this.agreedToTos();
         }

      }
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         Realms.setScreen(this.lastScreen);
      }

   }

   private void agreedToTos() {
      RealmsClient var1 = RealmsClient.createRealmsClient();

      try {
         var1.agreeToTos();
         LongRunningMcoTaskScreen var2 = new LongRunningMcoTaskScreen(this.lastScreen, new RealmsConnectTask(this.lastScreen, this.realmsServer));
         var2.start();
         Realms.setScreen(var2);
      } catch (RealmsServiceException var3) {
         LOGGER.error("Couldn't agree to TOS");
      }

   }

   public void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      if (this.onLink) {
         Clipboard var4 = Toolkit.getDefaultToolkit().getSystemClipboard();
         var4.setContents(new StringSelection(this.realmsToSUrl), (ClipboardOwner)null);
         this.browseTo(this.realmsToSUrl);
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
      this.drawCenteredString(getLocalizedString("mco.terms.title"), this.width() / 2, 17, 16777215);
      this.drawString(getLocalizedString("mco.terms.sentence.1"), this.width() / 2 - 120, 87, 16777215);
      int var4 = this.fontWidth(getLocalizedString("mco.terms.sentence.1"));
      int var5 = 3368635;
      int var6 = 7107012;
      int var7 = this.width() / 2 - 121 + var4;
      byte var8 = 86;
      int var9 = var7 + this.fontWidth("mco.terms.sentence.2") + 1;
      int var10 = 87 + this.fontLineHeight();
      if (var7 <= var1 && var1 <= var9 && var8 <= var2 && var2 <= var10) {
         this.onLink = true;
         this.drawString(" " + getLocalizedString("mco.terms.sentence.2"), this.width() / 2 - 120 + var4, 87, var6);
      } else {
         this.onLink = false;
         this.drawString(" " + getLocalizedString("mco.terms.sentence.2"), this.width() / 2 - 120 + var4, 87, var5);
      }

      super.render(var1, var2, var3);
   }
}
