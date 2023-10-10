package my.NewSnake.Tank.module.modules.MOVEMENT;

import java.awt.Color;
import java.util.Iterator;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.utils.ClientUtils;
import my.NewSnake.utils.Colors;
import my.NewSnake.utils.RenderUtils;
import my.NewSnake.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@Module.Mod
public class Radar extends Module {
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorDaImagem2;
   @Option.Op
   private boolean players;
   @Option.Op
   private boolean invisibles;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorDaImagem3;
   @Option.Op
   private boolean animals;
   @Option.Op
   private boolean passives;
   @Option.Op
   private boolean items;
   @Option.Op
   private boolean monsters;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorTraço2;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorLinha3;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorLinha;
   @Option.Op(
      min = 0.0D,
      max = 200.0D,
      increment = 0.5D
   )
   private double Tamanho;
   @Option.Op(
      min = 0.0D,
      max = 800.0D,
      increment = 0.5D
   )
   private double x;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorTraço3;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorTraço;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorLinha2;
   @Option.Op(
      min = 0.0D,
      max = 400.0D,
      increment = 0.5D
   )
   private double y;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorDaImagem;

   public static void drawRectSized(float var0, float var1, float var2, float var3, int var4) {
      drawRect2(var0, var1, var0 + var2, var1 + var3, var4);
   }

   public static void glColor(int var0) {
      float var1 = (float)(var0 >> 24 & 255) / 255.0F;
      float var2 = (float)(var0 >> 16 & 255) / 255.0F;
      float var3 = (float)(var0 >> 8 & 255) / 255.0F;
      float var4 = (float)(var0 & 255) / 255.0F;
      GL11.glColor4f(var2, var3, var4, var1);
   }

   public static void rectangleBordered(double var0, double var2, double var4, double var6, double var8, int var10, int var11) {
      rectangle(var0 + var8, var2 + var8, var4 - var8, var6 - var8, var10);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(var0 + var8, var2, var4 - var8, var2 + var8, var11);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(var0, var2, var0 + var8, var6, var11);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(var4 - var8, var2, var4, var6, var11);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(var0 + var8, var6 - var8, var4 - var8, var6, var11);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static Color effect(long var0, float var2, int var3) {
      float var4 = (float)(System.nanoTime() + var0 * (long)var3) / 1.0E10F % 1.0F;
      long var5 = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(var4, var2, 1.0F))), 16);
      Color var7 = new Color((int)var5);
      return new Color((float)var7.getRed() / 255.0F, (float)var7.getGreen() / 255.0F, (float)var7.getBlue() / 255.0F, (float)var7.getAlpha() / 255.0F);
   }

   public static void enableGL2D() {
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glDepthMask(true);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
   }

   public double[] getLookVector(float var1) {
      var1 *= 0.017453292F;
      return new double[]{(double)(-MathHelper.sin(var1)), (double)MathHelper.cos(var1)};
   }

   public static void drawRect2(float var0, float var1, float var2, float var3, int var4) {
      enableGL2D();
      glColor(var4);
      drawRect3(var0, var1, var2, var3);
      disableGL2D();
   }

   public static void disableGL2D() {
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   @EventTarget
   private void onOverlayDraw(Render2DEvent var1) {
      if (!mc.gameSettings.showDebugInfo) {
         GL11.glPushMatrix();
         new Color((int)this.CorDaImagem, (int)this.CorDaImagem2, (int)this.CorDaImagem3);
         int var3 = (int)this.x;
         int var4 = (int)this.y;
         int var5 = (int)this.Tamanho;
         int var6 = (int)this.Tamanho;
         float var7 = (float)var3 + (float)var5 / 2.0F;
         float var8 = (float)var4 + (float)var6 / 2.0F;
         rectangleBordered((double)((float)var3 + 3.0F), (double)((float)var4 + 3.0F), (double)((float)(var3 + var6) - 3.0F), (double)((float)(var4 + var5) - 3.0F), 0.2D, Colors.getColor((int)this.CorDaImagem, (int)this.CorDaImagem2, (int)this.CorDaImagem3), Colors.getColor(88));
         rectangle((double)var3 + ((double)((float)var6 / 2.0F) - 0.5D), (double)var4 + 3.5D, (double)var3 + (double)((float)var6 / 2.0F) + 0.2D, (double)(var4 + var5) - 3.5D, Colors.getColor((int)this.CorLinha, (int)this.CorLinha2, (int)this.CorLinha3));
         rectangle((double)var3 + 3.5D, (double)var4 + ((double)((float)var5 / 2.0F) - 0.2D), (double)(var3 + var6) - 3.5D, (double)var4 + (double)((float)var5 / 2.0F) + 0.5D, Colors.getColor((int)this.CorTraço, (int)this.CorTraço2, (int)this.CorTraço3));
         drawRectSized(var7 - 1.0F, var8 - 1.0F, 2.0F, 2.0F, -256);
         int var9 = (int)(this.Tamanho / 2.0D);
         ClientUtils.mc();
         Iterator var11 = Minecraft.theWorld.loadedEntityList.iterator();

         label45:
         while(true) {
            while(true) {
               Entity var10;
               double var12;
               double var14;
               do {
                  do {
                     if (!var11.hasNext()) {
                        break label45;
                     }

                     var10 = (Entity)var11.next();
                  } while(var10 == false);

                  var12 = RenderUtils.lerp(var10.prevPosX, var10.posX, (double)var1.getTicks()) - RenderUtils.lerp(Minecraft.thePlayer.prevPosX, Minecraft.thePlayer.posX, (double)var1.getTicks());
                  var14 = RenderUtils.lerp(var10.prevPosZ, var10.posZ, (double)var1.getTicks()) - RenderUtils.lerp(Minecraft.thePlayer.prevPosZ, Minecraft.thePlayer.posZ, (double)var1.getTicks());
               } while(!(var12 * var12 + var14 * var14 <= (double)(var9 * var9)));

               float var16 = MathHelper.sqrt_double(var12 * var12 + var14 * var14);
               double[] var17 = this.getLookVector(RotationUtils.getRotations2(var10)[0] - (float)RenderUtils.lerp((double)Minecraft.thePlayer.prevRotationYawHead, (double)Minecraft.thePlayer.rotationYawHead, (double)var1.getTicks()));
               if (var10 instanceof EntityMob) {
                  drawRectSized(var7 - 1.0F - (float)var17[0] * var16, var8 - 1.0F - (float)var17[1] * var16, 2.0F, 2.0F, (new Color(0, 252, 103)).getRGB());
               } else if (var10 instanceof EntityPlayer) {
                  drawRectSized(var7 - 1.0F - (float)var17[0] * var16, var8 - 1.0F - (float)var17[1] * var16, 2.0F, 2.0F, (new Color(248, 0, 0)).getRGB());
               } else if (!(var10 instanceof EntityAnimal) && !(var10 instanceof EntitySquid) && !(var10 instanceof EntityVillager) && !(var10 instanceof EntityGolem)) {
                  if (var10 instanceof EntityItem) {
                     drawRectSized(var7 - 1.0F - (float)var17[0] * var16, var8 - 1.0F - (float)var17[1] * var16, 2.0F, 2.0F, (new Color(0, 147, 241)).getRGB());
                  }
               } else {
                  drawRectSized(var7 - 1.0F - (float)var17[0] * var16, var8 - 1.0F - (float)var17[1] * var16, 2.0F, 2.0F, (new Color(248, 178, 0)).getRGB());
               }
            }
         }
      }

      GL11.glPopMatrix();
   }

   public static void rectangle(double var0, double var2, double var4, double var6, int var8) {
      Gui.drawRect(var0, var2, var4, var6, var8);
   }

   public static Color TwoColoreffect(Color var0, Color var1, double var2) {
      double var4;
      if (var2 > 1.0D) {
         var4 = var2 % 1.0D;
         var2 = (int)var2 % 2 == 0 ? var4 : 1.0D - var4;
      }

      var4 = 1.0D - var2;
      return new Color((int)((double)var0.getRed() * var4 + (double)var1.getRed() * var2), (int)((double)var0.getGreen() * var4 + (double)var1.getGreen() * var2), (int)((double)var0.getBlue() * var4 + (double)var1.getBlue() * var2), (int)((double)var0.getAlpha() * var4 + (double)var1.getAlpha() * var2));
   }

   public static void drawRect3(float var0, float var1, float var2, float var3) {
      GL11.glBegin(7);
      GL11.glVertex2f(var0, var3);
      GL11.glVertex2f(var2, var3);
      GL11.glVertex2f(var2, var1);
      GL11.glVertex2f(var0, var1);
      GL11.glEnd();
   }
}
