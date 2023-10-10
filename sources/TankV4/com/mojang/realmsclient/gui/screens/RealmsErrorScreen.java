package com.mojang.realmsclient.gui.screens;

import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class RealmsErrorScreen extends RealmsScreen {
   private String title;
   private String message;

   public RealmsErrorScreen(String var1, String var2) {
      this.title = var1;
      this.message = var2;
   }

   public void init() {
      super.init();
      this.buttonsAdd(new RealmsButton(0, this.width() / 2 - 100, 140, getLocalizedString("gui.cancel")));
   }

   public void render(int var1, int var2, float var3) {
      this.fillGradient(0, 0, this.width(), this.height(), -12574688, -11530224);
      this.drawCenteredString(this.title, this.width() / 2, 90, 16777215);
      this.drawCenteredString(this.message, this.width() / 2, 110, 16777215);
      super.render(var1, var2, var3);
   }

   public void keyPressed(char var1, int var2) {
   }

   public void buttonClicked(RealmsButton var1) {
      Realms.setScreen((RealmsScreen)null);
   }
}
