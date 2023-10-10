package com.mojang.realmsclient.gui.screens;

import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class LongConfirmationScreen extends RealmsScreen {
   private final LongConfirmationScreen.Type type;
   private final String line2;
   private final String line3;
   protected final RealmsScreen parent;
   protected final String yesButton;
   protected final String noButton;
   protected final int id;

   public LongConfirmationScreen(RealmsScreen var1, LongConfirmationScreen.Type var2, String var3, String var4, int var5) {
      this.parent = var1;
      this.id = var5;
      this.type = var2;
      this.line2 = var3;
      this.line3 = var4;
      this.yesButton = getLocalizedString("gui.yes");
      this.noButton = getLocalizedString("gui.no");
   }

   public void init() {
      this.buttonsAdd(newButton(0, this.width() / 2 - 105, this.height() / 6 + 112, 100, 20, this.yesButton));
      this.buttonsAdd(newButton(1, this.width() / 2 + 5, this.height() / 6 + 112, 100, 20, this.noButton));
   }

   public void buttonClicked(RealmsButton var1) {
      this.parent.confirmResult(var1.id() == 0, this.id);
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         this.parent.confirmResult(false, 1);
      }

   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(this.type.text, this.width() / 2, 70, this.type.colorCode);
      this.drawCenteredString(this.line2, this.width() / 2, 90, 16777215);
      this.drawCenteredString(this.line3, this.width() / 2, 110, 16777215);
      super.render(var1, var2, var3);
   }

   public static enum Type {
      Warning("Warning!", 16711680),
      Info("Info!", 8226750);

      public final int colorCode;
      public final String text;
      private static final LongConfirmationScreen.Type[] $VALUES = new LongConfirmationScreen.Type[]{Warning, Info};

      private Type(String var3, int var4) {
         this.text = var3;
         this.colorCode = var4;
      }
   }
}
