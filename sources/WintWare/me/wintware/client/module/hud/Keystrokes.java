package me.wintware.client.module.hud;

import java.awt.Color;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;

public class Keystrokes extends Module {
   int lastA = 0;
   int lastW = 0;
   int lastS = 0;
   int lastD = 0;
   int lastJump = 0;
   double lastX = 0.0D;
   double lastZ = 0.0D;

   public Keystrokes() {
      super("Keystrokes", Category.Hud);
   }

   @EventTarget
   public void onRender(Event2D event) {
      boolean A = mc.gameSettings.keyBindLeft.pressed;
      boolean W = mc.gameSettings.keyBindForward.pressed;
      boolean S = mc.gameSettings.keyBindBack.pressed;
      boolean D = mc.gameSettings.keyBindRight.pressed;
      int alphaA = A ? 255 : 0;
      int alphaW = W ? 255 : 0;
      int alphaS = S ? 255 : 0;
      int alphaD = D ? 255 : 0;
      float diff;
      if (this.lastA != alphaA) {
         diff = (float)(alphaA - this.lastA);
         this.lastA = (int)((float)this.lastA + diff / 15.0F);
      }

      if (this.lastW != alphaW) {
         diff = (float)(alphaW - this.lastW);
         this.lastW = (int)((float)this.lastW + diff / 15.0F);
      }

      if (this.lastS != alphaS) {
         diff = (float)(alphaS - this.lastS);
         this.lastS = (int)((float)this.lastS + diff / 15.0F);
      }

      if (this.lastD != alphaD) {
         diff = (float)(alphaD - this.lastD);
         this.lastD = (int)((float)this.lastD + diff / 15.0F);
      }

      RenderUtil.drawSmoothRect(5.0F, 49.0F, 25.0F, 69.0F, (new Color(this.lastA, this.lastA, this.lastA, 150)).getRGB());
      Minecraft.getMinecraft().arraylist.drawCenteredString("A", 15.0F, 57.0F, (new Color(this.flop(this.lastA, 255), this.flop(this.lastA, 255), this.flop(this.lastA, 255), 255)).getRGB());
      RenderUtil.drawSmoothRect(27.0F, 27.0F, 47.0F, 47.0F, (new Color(this.lastW, this.lastW, this.lastW, 150)).getRGB());
      Minecraft.getMinecraft().arraylist.drawCenteredString("W", 37.0F, 35.0F, (new Color(this.flop(this.lastW, 255), this.flop(this.lastW, 255), this.flop(this.lastW, 255), 255)).getRGB());
      RenderUtil.drawSmoothRect(27.0F, 49.0F, 47.0F, 69.0F, (new Color(this.lastS, this.lastS, this.lastS, 150)).getRGB());
      Minecraft.getMinecraft().arraylist.drawCenteredString("S", 37.0F, 57.0F, (new Color(this.flop(this.lastS, 255), this.flop(this.lastS, 255), this.flop(this.lastS, 255), 255)).getRGB());
      RenderUtil.drawSmoothRect(49.0F, 49.0F, 69.0F, 69.0F, (new Color(this.lastD, this.lastD, this.lastD, 150)).getRGB());
      Minecraft.getMinecraft().arraylist.drawCenteredString("D", 59.0F, 57.0F, (new Color(this.flop(this.lastD, 255), this.flop(this.lastD, 255), this.flop(this.lastD, 255), 255)).getRGB());
   }

   public int flop(int a, int b) {
      return b - a;
   }
}
