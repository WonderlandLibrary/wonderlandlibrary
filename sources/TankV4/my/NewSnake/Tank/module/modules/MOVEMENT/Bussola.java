package my.NewSnake.Tank.module.modules.MOVEMENT;

import java.awt.Font;
import java.util.Iterator;
import my.NewSnake.Tank.friend.FriendManager;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.utils.ClientUtils;
import my.NewSnake.utils.Colors;
import my.NewSnake.utils.MCFontRenderer;
import my.NewSnake.utils.RenderUtils;
import my.NewSnake.utils.StringConversions;
import my.NewSnake.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@Module.Mod
public class Bussola extends Module {
   @Option.Op(
      min = 0.0D,
      max = 200.0D,
      increment = 1.0D,
      name = "x"
   )
   public static double x;
   @Option.Op(
      min = 0.0D,
      max = 200.0D,
      increment = 1.0D,
      name = "Tamanho"
   )
   public static double size;
   float hue;
   private Timer timer = new Timer();
   private boolean dragging;
   @Option.Op(
      min = 0.0D,
      max = 200.0D,
      increment = 1.0D,
      name = "y"
   )
   public static double y;
   private final MCFontRenderer otherfont = new MCFontRenderer(new Font("Tahoma", 0, 12), true, true);

   public static void disableGL2D() {
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
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

   public static void drawCircle(float var0, float var1, float var2, int var3, int var4) {
      GL11.glPushMatrix();
      var0 *= 2.0F;
      var1 *= 2.0F;
      float var5 = (float)(var4 >> 24 & 255) / 255.0F;
      float var6 = (float)(var4 >> 16 & 255) / 255.0F;
      float var7 = (float)(var4 >> 8 & 255) / 255.0F;
      float var8 = (float)(var4 & 255) / 255.0F;
      float var9 = (float)(6.2831852D / (double)var3);
      float var10 = (float)Math.cos((double)var9);
      float var11 = (float)Math.sin((double)var9);
      float var12 = var2 *= 2.0F;
      float var13 = 0.0F;
      enableGL2D();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glColor4f(var6, var7, var8, var5);
      GL11.glBegin(2);

      for(int var14 = 0; var14 < var3; ++var14) {
         GL11.glVertex2f(var12 + var0, var13 + var1);
         float var15 = var12;
         var12 = var10 * var12 - var11 * var13;
         var13 = var11 * var15 + var10 * var13;
      }

      GL11.glEnd();
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   @EventTarget
   private void onRender2D(Render2DEvent var1) {
      int var2 = (int)size;
      float var3 = (float)((int)x);
      float var4 = (float)((int)y);
      ClientUtils.mc();
      float var5 = (float)Minecraft.thePlayer.posX;
      ClientUtils.mc();
      float var6 = (float)Minecraft.thePlayer.posZ;
      drawFullCircle(var3 + (float)(var2 / 2), var4 + (float)(var2 / 2), (float)(var2 / 2 - 4), Colors.getColor(50, 100));
      GlStateManager.pushMatrix();
      GlStateManager.translate(var3 + (float)(var2 / 2), var4 + (float)(var2 / 2), 0.0F);
      ClientUtils.mc();
      GlStateManager.rotate(-Minecraft.thePlayer.rotationYaw, 0.0F, 0.0F, 1.0F);
      RenderUtils.rectangle(-0.5D, (double)(-var2 / 2 + 4), 0.5D, (double)(var2 / 2 - 4), Colors.getColor(255, 80));
      RenderUtils.rectangle((double)(-var2 / 2 + 4), -0.5D, (double)(var2 / 2 - 4), 0.5D, Colors.getColor(255, 80));
      GlStateManager.popMatrix();
      drawFullCircle(var3 + (float)(var2 / 2), var4 + (float)(var2 / 2), (float)(var2 / 2 - 4), 72);
      ClientUtils.mc();
      float var7 = -Minecraft.thePlayer.rotationYaw + 90.0F;
      float var8 = (float)((double)(var2 / 2 + 4) * Math.cos(Math.toRadians((double)var7))) + var3 + (float)(var2 / 2);
      float var9 = (float)((double)(var2 / 2 + 4) * Math.sin(Math.toRadians((double)var7))) + var4 + (float)(var2 / 2);
      this.otherfont.drawStringWithShadow("N", (double)(var8 - (float)(this.otherfont.getStringWidth("N") / 2)), (double)(var9 - 1.0F), -1);
      var8 = (float)((double)(var2 / 2 + 4) * Math.cos(Math.toRadians((double)(var7 + 90.0F)))) + var3 + (float)(var2 / 2);
      var9 = (float)((double)(var2 / 2 + 4) * Math.sin(Math.toRadians((double)(var7 + 90.0F)))) + var4 + (float)(var2 / 2);
      this.otherfont.drawStringWithShadow("E", (double)(var8 - (float)(this.otherfont.getStringWidth("E") / 2)), (double)(var9 - 1.0F), -1);
      var8 = (float)((double)(var2 / 2 + 4) * Math.cos(Math.toRadians((double)(var7 + 180.0F)))) + var3 + (float)(var2 / 2);
      var9 = (float)((double)(var2 / 2 + 4) * Math.sin(Math.toRadians((double)(var7 + 180.0F)))) + var4 + (float)(var2 / 2);
      this.otherfont.drawStringWithShadow("S", (double)(var8 - (float)(this.otherfont.getStringWidth("S") / 2)), (double)(var9 - 1.0F), -1);
      var8 = (float)((double)(var2 / 2 + 4) * Math.cos(Math.toRadians((double)(var7 - 90.0F)))) + var3 + (float)(var2 / 2);
      var9 = (float)((double)(var2 / 2 + 4) * Math.sin(Math.toRadians((double)(var7 - 90.0F)))) + var4 + (float)(var2 / 2);
      this.otherfont.drawStringWithShadow("W", (double)(var8 - (float)(this.otherfont.getStringWidth("W") / 2)), (double)(var9 - 1.0F), -1);
      new ScaledResolution(ClientUtils.mc());
      float var11 = (float)(var1.getWidth() / 3 + (int)x);
      float var12 = (float)(var1.getHeight() / 3 + (int)y);
      if (var11 >= var3 && var11 <= var3 + (float)var2 && var12 >= var4 - 3.0F && var12 <= var4 + 10.0F && Mouse.getEventButton() == 0 && this.timer.delay(20.0F)) {
         this.timer.reset();
         this.dragging = !this.dragging;
      }

      Object var13;
      if (this.dragging && mc.currentScreen instanceof GuiChat) {
         var13 = StringConversions.castNumber(Double.toString((double)(var11 - (float)(var2 / 2))), 5);
         Double var29 = x;
         Object var14 = StringConversions.castNumber(Double.toString((double)(var12 - 2.0F)), 5);
         Double var30 = x;
      } else {
         this.dragging = false;
      }

      ClientUtils.mc();
      Iterator var31 = Minecraft.theWorld.getLoadedEntityList().iterator();

      while(var31.hasNext()) {
         var13 = var31.next();
         if (var13 instanceof EntityPlayer) {
            EntityPlayer var15 = (EntityPlayer)var13;
            if (var15.isEntityAlive()) {
               ClientUtils.mc();
               if (var15 != Minecraft.thePlayer && !var15.isInvisible()) {
                  ClientUtils.mc();
                  if (!var15.isInvisibleToPlayer(Minecraft.thePlayer)) {
                     float var16 = ClientUtils.mc().timer.renderPartialTicks;
                     float var17 = (float)(var15.posX + (var15.posX - var15.lastTickPosX) * (double)var16 - (double)var5);
                     float var18 = (float)(var15.posZ + (var15.posZ - var15.lastTickPosZ) * (double)var16 - (double)var6);
                     int var19;
                     if (FriendManager.isFriend(var15.getName())) {
                        ClientUtils.mc();
                        var19 = Minecraft.thePlayer.canEntityBeSeen(var15) ? Colors.getColor(0, 195, 255) : Colors.getColor(0, 195, 255);
                     } else {
                        ClientUtils.mc();
                        var19 = Minecraft.thePlayer.canEntityBeSeen(var15) ? Colors.getColor(255, 0, 0) : Colors.getColor(255, 255, 0);
                     }

                     ClientUtils.mc();
                     float var20 = (float)Math.cos((double)Minecraft.thePlayer.rotationYaw * 0.017453292519943295D);
                     ClientUtils.mc();
                     float var21 = (float)Math.sin((double)Minecraft.thePlayer.rotationYaw * 0.017453292519943295D);
                     float var22 = -(var18 * var20 - var17 * var21);
                     float var23 = -(var17 * var20 + var18 * var21);
                     float var24 = 0.0F - var23;
                     float var25 = 0.0F - var22;
                     if (MathHelper.sqrt_double((double)(var24 * var24 + var25 * var25)) > (float)(var2 / 2 - 4)) {
                        float var26 = this.findAngle(0.0F, var23, 0.0F, var22);
                        float var27 = (float)((double)(var2 / 2) * Math.cos(Math.toRadians((double)var26))) + var3 + (float)(var2 / 2);
                        float var28 = (float)((double)(var2 / 2) * Math.sin(Math.toRadians((double)var26))) + var4 + (float)(var2 / 2);
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(var27, var28, 0.0F);
                        GlStateManager.rotate(var26, 0.0F, 0.0F, 1.0F);
                        GlStateManager.scale(1.5D, 0.5D, 0.5D);
                        drawCircle(0.0F, 0.0F, 1.5F, 3, Colors.getColor(46));
                        drawCircle(0.0F, 0.0F, 1.0F, 3, var19);
                        GlStateManager.popMatrix();
                     } else {
                        RenderUtils.rectangleBordered((double)(var3 + (float)(var2 / 2) + var23) - 1.5D, (double)(var4 + (float)(var2 / 2) + var22) - 1.5D, (double)(var3 + (float)(var2 / 2) + var23) + 1.5D, (double)(var4 + (float)(var2 / 2) + var22) + 1.5D, 0.5D, var19, Colors.getColor(46));
                     }
                  }
               }
            }
         }
      }

   }

   public static void drawFullCircle(float var0, float var1, float var2, int var3) {
      var2 *= 2.0F;
      var0 *= 2.0F;
      var1 *= 2.0F;
      float var4 = 0.19634953F;
      float var5 = (float)Math.cos((double)var4);
      float var6 = (float)Math.sin((double)var4);
      float var7 = var2;
      float var8 = 0.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glBegin(2);
      glColor(var3);

      for(int var9 = 0; var9 < 32; ++var9) {
         GL11.glVertex2f(var7 + var0, var8 + var1);
         GL11.glVertex2f(var0, var1);
         float var10 = var7;
         var7 = var5 * var7 - var6 * var8;
         var8 = var6 * var10 + var5 * var8;
      }

      GL11.glEnd();
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
      GL11.glEnable(3553);
   }

   public static void glColor(int var0) {
      float var1 = (float)(var0 >> 24 & 255) / 255.0F;
      float var2 = (float)(var0 >> 16 & 255) / 255.0F;
      float var3 = (float)(var0 >> 8 & 255) / 255.0F;
      float var4 = (float)(var0 & 255) / 255.0F;
      GL11.glColor4f(var2, var3, var4, var1);
   }

   private float findAngle(float var1, float var2, float var3, float var4) {
      return (float)(Math.atan2((double)(var4 - var3), (double)(var2 - var1)) * 180.0D / 3.141592653589793D);
   }
}
