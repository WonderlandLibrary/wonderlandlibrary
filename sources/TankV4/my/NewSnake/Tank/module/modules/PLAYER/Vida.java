package my.NewSnake.Tank.module.modules.PLAYER;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Iterator;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@Module.Mod
public class Vida extends Module {
   private static double a = 0.0D;
   @Option.Op(
      min = 0.1D,
      max = 800.0D,
      increment = 0.1D
   )
   private double z;
   private static double lastP = 0.0D;
   @Option.Op(
      min = 0.1D,
      max = 255.0D,
      increment = 0.1D
   )
   private double Largura;
   private double healthBarWidth;
   private float animated = 20.0F;
   private int width;
   @Option.Op(
      min = 0.1D,
      max = 255.0D,
      increment = 0.1D
   )
   private double Size;
   private double y;
   @Option.Op(
      min = 0.1D,
      max = 1500.0D,
      increment = 0.1D
   )
   private double x;
   int fuck = 0;
   @Option.Op
   private boolean Vida;

   @EventTarget
   public void onUpdate(UpdateEvent var1) {
      ClientUtils.mc();
      if (Minecraft.thePlayer.getHealth() >= 0.0F) {
         ClientUtils.mc();
         if (Minecraft.thePlayer.getHealth() < 6.0F && this.fuck == 0) {
            this.fuck = 1;
            return;
         }
      }

      ClientUtils.mc();
      if (Minecraft.thePlayer.getHealth() >= 0.0F) {
         ClientUtils.mc();
         if (Minecraft.thePlayer.getHealth() > 6.0F && this.fuck == 1) {
            this.fuck = 0;
         }
      }

   }

   public static void drawBorderedRect(double var0, double var2, double var4, double var6, float var8, int var9, int var10) {
      Gui.drawRect((double)((float)var0), (double)((float)var2), (double)((float)var4), (double)((float)var6), var10);
      float var11 = (float)(var9 >> 24 & 255) / 255.0F;
      float var12 = (float)(var9 >> 16 & 255) / 255.0F;
      float var13 = (float)(var9 >> 8 & 255) / 255.0F;
      float var14 = (float)(var9 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      GL11.glColor4f(var12, var13, var14, var11);
      GL11.glLineWidth(var8);
      GL11.glBegin(1);
      GL11.glVertex2d(var0, var2);
      GL11.glVertex2d(var0, var6);
      GL11.glVertex2d(var4, var6);
      GL11.glVertex2d(var4, var2);
      GL11.glVertex2d(var0, var2);
      GL11.glVertex2d(var4, var2);
      GL11.glVertex2d(var0, var6);
      GL11.glVertex2d(var4, var6);
      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GL11.glPopMatrix();
      GL11.glColor4f(255.0F, 1.0F, 1.0F, 255.0F);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
   }

   public static double roundToDecimalPlace(double var0, double var2) {
      double var4 = var2 / 2.0D;
      double var6 = Math.floor(var0 / var2) * var2;
      return var0 >= var6 + var4 ? (new BigDecimal(Math.ceil(var0 / var2) * var2, MathContext.DECIMAL64)).stripTrailingZeros().doubleValue() : (new BigDecimal(var6, MathContext.DECIMAL64)).stripTrailingZeros().doubleValue();
   }

   public static double animate(double var0, double var2, double var4) {
      boolean var6 = var0 > var2;
      if (var4 < 0.0D) {
         var4 = 0.0D;
      } else if (var4 > 1.0D) {
         var4 = 1.0D;
      }

      double var7 = Math.max(var0, var2) - Math.min(var0, var2);
      double var9 = var7 * var4;
      if (var9 < 0.1D) {
         var9 = 0.1D;
      }

      if (var6) {
         var2 += var9;
      } else {
         var2 -= var9;
      }

      return var2;
   }

   private void drawFace(double var1, double var3, float var5, float var6, int var7, int var8, int var9, int var10, float var11, float var12, AbstractClientPlayer var13) {
      try {
         ResourceLocation var14 = var13.getLocationSkin();
         ClientUtils.mc().getTextureManager().bindTexture(var14);
         GL11.glEnable(3042);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         Gui.drawScaledCustomSizeModalRect(var1, var3, var5, var6, var7, var8, var9, var10, var11, var12);
         GL11.glDisable(3042);
      } catch (Exception var16) {
      }

   }

   public static int getColorFromPercentage(float var0, float var1) {
      float var2 = var0 / var1 / 3.0F;
      return Color.HSBtoRGB(var2, 1.0F, 1.0F);
   }

   public void drawRectB(double var1, double var3, float var5, float var6, Color var7) {
      Gui.drawRect(var1, var3, var1 + (double)var5, var3 + (double)var6, var7.getRGB());
   }

   @EventTarget
   private void renderHud(Render2DEvent var1) {
      if (this.Vida) {
         ClientUtils.mc();
         FontRenderer var10000 = Minecraft.fontRendererObj;
         StringBuilder var10001 = new StringBuilder(" Minha Vida : ");
         ClientUtils.mc();
         String var6 = var10001.append(MathHelper.ceiling_float_int(Minecraft.thePlayer.getHealth())).toString();
         float var10002 = (float)((double)(new ScaledResolution(ClientUtils.mc())).getScaledWidth() / this.Largura - (double)this.width);
         new ScaledResolution(ClientUtils.mc());
         float var10003 = (float)(ScaledResolution.getScaledHeight() / 2 - 5) - (float)this.Size;
         ClientUtils.mc();
         var10000.drawStringWithShadow(var6, var10002, var10003, Minecraft.thePlayer.getHealth() <= 10.0F ? (new Color(255, 0, 0)).getRGB() : (new Color(0, 255, 0)).getRGB());
      }

      new ArrayList();
      Minecraft var5 = mc;
      Iterator var3 = Minecraft.theWorld.loadedEntityList.iterator();
   }

   public static double transition(double var0, double var2, double var4) {
      double var6 = Math.abs(var0 - var2);
      int var8 = Minecraft.getDebugFPS();
      if (var6 > 0.0D) {
         double var9 = roundToDecimalPlace(Math.min(10.0D, Math.max(0.0625D, 144.0D / (double)var8 * (var6 / 10.0D) * var4)), 0.0625D);
         if (var6 != 0.0D && var6 < var9) {
            var9 = var6;
         }

         if (var0 < var2) {
            return var0 + var9;
         }

         if (var0 > var2) {
            return var0 - var9;
         }
      }

      return var0;
   }
}
