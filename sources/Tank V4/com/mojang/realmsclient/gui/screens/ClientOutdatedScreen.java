package com.mojang.realmsclient.gui.screens;

import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class ClientOutdatedScreen extends RealmsScreen {
   private static final int BUTTON_BACK_ID = 0;
   private final RealmsScreen lastScreen;

   public ClientOutdatedScreen(RealmsScreen var1) {
      this.lastScreen = var1;
   }

   public void init() {
      this.buttonsClear();
      this.buttonsAdd(newButton(0, this.width() / 2 - 100, this.height() / 4 + 120 + 12, "Back"));
   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      String var4 = getLocalizedString("mco.client.outdated.title");
      String var5 = getLocalizedString("mco.client.outdated.msg");
      this.drawCenteredString(var4, this.width() / 2, this.height() / 2 - 50, 16711680);
      this.drawCenteredString(var5, this.width() / 2, this.height() / 2 - 30, 16777215);
      super.render(var1, var2, var3);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.id() == 0) {
         Realms.setScreen(this.lastScreen);
      }

   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 28 || var2 == 156 || var2 == 1) {
         Realms.setScreen(this.lastScreen);
      }

   }
}
