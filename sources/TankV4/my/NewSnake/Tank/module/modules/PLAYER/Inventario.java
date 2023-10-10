package my.NewSnake.Tank.module.modules.PLAYER;

import java.awt.Color;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@Module.Mod(
   displayName = "New Inventario"
)
public class Inventario extends Module {
   @Option.Op(
      min = 2.3D,
      max = 331.0D,
      increment = 1.0D
   )
   public static double yi;
   @Option.Op
   public static boolean Test;
   FontRenderer fr;
   Inventario inventory;
   static String str;
   @Option.Op(
      min = 2.3D,
      max = 537.0D,
      increment = 1.0D
   )
   public static double xi;
   private float stackk;

   private void renderItemStack(int var1, int var2, ItemStack var3) {
      if (var3 != null) {
         GL11.glPushMatrix();
         RenderHelper.enableGUIStandardItemLighting();
         mc.getRenderItem().renderItemAndEffectIntoGUI(var3, var1, var2);
         GL11.glPopMatrix();
      }
   }

   @EventTarget
   public void onRender2D(Render2DEvent var1) {
      if (Boolean.valueOf(Test)) {
         Gui.drawRect(xi - 1.0D, yi - -53.0D, xi - -147.0D, yi - 15.0D, (new Color(0, 0, 0, 150)).getRGB());
         Gui.drawRect(xi - 1.0D, yi - 1.0D, xi - -147.0D, yi - 2.0D, -1);
         Gui.drawRect(xi - 1.0D, yi - 15.0D, xi - -147.0D, yi - 14.0D, -1);
         Gui.drawRect(xi - 1.0D, yi - -53.0D, xi - -147.0D, yi - -54.0D, -1);
         Gui.drawRect(xi - 1.0D, yi - 15.0D, xi - -0.1D, yi - -54.0D, -1);
         Gui.drawRect(xi - -147.0D, yi - 15.0D, xi - -146.0D, yi - -54.0D, -1);
         ClientUtils.clientFont.drawStringWithShadow("Meu Inventario", xi - -47.0D, yi - 13.0D, -1);
         int var2 = 0;
         byte var3 = 0;
         byte var4 = 0;

         Minecraft var10000;
         int var5;
         for(var5 = 9; var5 < 36; ++var5) {
            if (var5 == 18) {
               var3 = 17;
               var2 = 0;
            }

            if (var2 == 144) {
               var4 = 17;
               var2 = 0;
            }

            var10000 = mc;
            ItemStack var6 = Minecraft.thePlayer.inventory.getStackInSlot(var5);
            this.renderItemStack((int)xi + var2, (int)yi + var3 + var4, var6);
            var2 += 16;
         }

         var5 = 0;
         byte var12 = 0;
         byte var7 = 0;

         for(int var8 = 9; var8 < 36; ++var8) {
            if (var8 == 18) {
               var12 = 17;
               var5 = 0;
            }

            if (var5 == 144) {
               var7 = 17;
               var5 = 0;
            }

            var10000 = mc;
            ItemStack var9 = Minecraft.thePlayer.inventory.getStackInSlot(var8);

            try {
               GL11.glPushMatrix();
               RenderHelper.enableStandardItemLighting();
               mc.getRenderItem().renderItemOverlays(this.fr, var9, (int)xi + var5, (int)yi + var12 + var7);
               GL11.glPopMatrix();
               RenderHelper.disableStandardItemLighting();
            } catch (Exception var11) {
               var11.printStackTrace();
            }

            var5 += 16;
         }
      }

   }

   public Inventario() {
      Minecraft var10001 = mc;
      this.fr = Minecraft.fontRendererObj;
   }

   public static Color getGradientOffset(Color var0, Color var1, double var2) {
      double var4;
      int var6;
      if (var2 > 1.0D) {
         var4 = var2 % 1.0D;
         var6 = (int)var2;
         var2 = var6 % 2 == 0 ? var4 : 1.0D - var4;
      }

      var4 = 1.0D - var2;
      var6 = (int)((double)var0.getRed() * var4 + (double)var1.getRed() * var2);
      int var7 = (int)((double)var0.getGreen() * var4 + (double)var1.getGreen() * var2);
      int var8 = (int)((double)var0.getBlue() * var4 + (double)var1.getBlue() * var2);
      return new Color(var6, var7, var8);
   }

   public static Color fade4(Color var0, int var1, int var2) {
      float[] var3 = new float[3];
      Color.RGBtoHSB(var0.getRed(), var0.getGreen(), var0.getBlue(), var3);
      float var4 = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)(var1 / var2) * 2.0F) % 2.0F - 1.0F);
      var4 = 0.5F + 0.5F * var4;
      var3[2] = var4 % 2.0F;
      return new Color(Color.HSBtoRGB(var3[0], var3[1], var3[2]));
   }

   private static int RainbowCustumRGB(int var0, int var1) {
      float var2 = (float)((System.currentTimeMillis() + (long)var1) % (long)var0);
      var2 /= (float)var0;
      return Color.getHSBColor(var2, 0.8F, 1.0F).getRGB();
   }

   public static int RGB() {
      float var0 = (float)(System.currentTimeMillis() % 4000L) / 6500.0F;
      int var1 = Color.HSBtoRGB(var0, 1.0F, 1.0F);
      return var1;
   }
}
