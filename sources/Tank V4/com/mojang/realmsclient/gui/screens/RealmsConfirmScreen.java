package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.RealmsConfirmResultListener;
import java.util.Iterator;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class RealmsConfirmScreen extends RealmsScreen {
   protected RealmsConfirmResultListener parent;
   protected String title1;
   private String title2;
   protected String yesButton;
   protected String noButton;
   protected int id;
   private int delayTicker;

   public RealmsConfirmScreen(RealmsConfirmResultListener var1, String var2, String var3, int var4) {
      this.parent = var1;
      this.title1 = var2;
      this.title2 = var3;
      this.id = var4;
      this.yesButton = getLocalizedString("gui.yes");
      this.noButton = getLocalizedString("gui.no");
   }

   public RealmsConfirmScreen(RealmsConfirmResultListener var1, String var2, String var3, String var4, String var5, int var6) {
      this.parent = var1;
      this.title1 = var2;
      this.title2 = var3;
      this.yesButton = var4;
      this.noButton = var5;
      this.id = var6;
   }

   public void init() {
      this.buttonsAdd(newButton(0, this.width() / 2 - 105, this.height() / 6 + 96, 100, 20, this.yesButton));
      this.buttonsAdd(newButton(1, this.width() / 2 + 5, this.height() / 6 + 96, 100, 20, this.noButton));
   }

   public void buttonClicked(RealmsButton var1) {
      this.parent.confirmResult(var1.id() == 0, this.id);
   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(this.title1, this.width() / 2, 70, 16777215);
      this.drawCenteredString(this.title2, this.width() / 2, 90, 16777215);
      super.render(var1, var2, var3);
   }

   public void setDelay(int var1) {
      this.delayTicker = var1;
      Iterator var2 = this.buttons().iterator();

      while(var2.hasNext()) {
         RealmsButton var3 = (RealmsButton)var2.next();
         var3.active(false);
      }

   }

   public void tick() {
      super.tick();
      if (--this.delayTicker == 0) {
         Iterator var1 = this.buttons().iterator();

         while(var1.hasNext()) {
            RealmsButton var2 = (RealmsButton)var1.next();
            var2.active(true);
         }
      }

   }
}
