package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class RealmsGenericErrorScreen extends RealmsScreen {
   private final RealmsScreen nextScreen;
   private static final int OK_BUTTON_ID = 10;
   private String line1;
   private String line2;

   public RealmsGenericErrorScreen(RealmsServiceException var1, RealmsScreen var2) {
      this.nextScreen = var2;
      this.errorMessage(var1);
   }

   public RealmsGenericErrorScreen(String var1, RealmsScreen var2) {
      this.nextScreen = var2;
      this.errorMessage(var1);
   }

   private void errorMessage(RealmsServiceException var1) {
      if (var1.errorCode != -1) {
         this.line1 = "Realms (" + var1.errorCode + "):";
         this.line2 = getLocalizedString("mco.errorMessage." + var1.errorCode);
      } else {
         this.line1 = "An error occurred (" + var1.httpResultCode + "):";
         this.line2 = var1.httpResponseContent;
      }

   }

   private void errorMessage(String var1) {
      this.line1 = "An error occurred: ";
      this.line2 = var1;
   }

   public void init() {
      this.buttonsClear();
      this.buttonsAdd(newButton(10, this.width() / 2 - 100, this.height() - 52, 200, 20, "Ok"));
   }

   public void tick() {
      super.tick();
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.id() == 10) {
         Realms.setScreen(this.nextScreen);
      }

   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(this.line1, this.width() / 2, 80, 16777215);
      this.drawCenteredString(this.line2, this.width() / 2, 100, 16711680);
      super.render(var1, var2, var3);
   }
}
