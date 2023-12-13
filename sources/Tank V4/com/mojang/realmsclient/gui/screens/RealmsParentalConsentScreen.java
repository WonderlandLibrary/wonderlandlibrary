package com.mojang.realmsclient.gui.screens;

import java.net.URI;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class RealmsParentalConsentScreen extends RealmsScreen {
   private final RealmsScreen nextScreen;
   private static final int OK_BUTTON_ID = 10;
   private static final int BACK_BUTTON_ID = 5;
   private String line1;
   private String line2;

   public RealmsParentalConsentScreen(RealmsScreen var1) {
      this.nextScreen = var1;
   }

   public void init() {
      this.buttonsClear();
      this.line1 = "You need parental consent to access Minecraft Realms.";
      this.line2 = "This is not currently set up on your account.";
      this.buttonsAdd(newButton(10, this.width() / 2 - 100, this.height() - 65, 200, 20, "Set up parental consent"));
      this.buttonsAdd(newButton(5, this.width() / 2 - 100, this.height() - 42, 200, 20, "Back"));
   }

   public void tick() {
      super.tick();
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.id() == 10) {
         this.browseTo("https://accounts.mojang.com/account/requestConsent/" + Realms.getSessionId().split(":", 3)[2]);
      } else if (var1.id() == 5) {
         Realms.setScreen(this.nextScreen);
      }

   }

   private void browseTo(String var1) {
      try {
         URI var2 = new URI(var1);
         Class var3 = Class.forName("java.awt.Desktop");
         Object var4 = var3.getMethod("getDesktop").invoke((Object)null);
         var3.getMethod("browse", URI.class).invoke(var4, var2);
      } catch (Throwable var5) {
      }

   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(this.line1, this.width() / 2, 80, 16777215);
      this.drawCenteredString(this.line2, this.width() / 2, 100, 16777215);
      super.render(var1, var2, var3);
   }
}
