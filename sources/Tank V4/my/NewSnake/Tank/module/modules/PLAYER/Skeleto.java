package my.NewSnake.Tank.module.modules.PLAYER;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render3DEvent;
import my.NewSnake.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

@Module.Mod
public class Skeleto extends Module {
   private static Map entities = new HashMap();
   @Option.Op(
      min = 1.0D,
      max = 10.0D,
      increment = 1.0D,
      name = "width"
   )
   public static double width;

   private void drawSkeleton(Render3DEvent var1, EntityPlayer var2) {
      if (!var2.isInvisible()) {
         float[][] var3 = (float[][])entities.get(var2);
         if (var3 != null && var2.isEntityAlive() && RenderUtils.isInViewFrustrum((Entity)var2) && !var2.isDead) {
            Minecraft var10001 = mc;
            if (var2 != Minecraft.thePlayer && !var2.isPlayerSleeping()) {
               GL11.glPushMatrix();
               GL11.glEnable(2848);
               GL11.glLineWidth((float)((int)width));
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Vec3 var4 = this.getVec3(var1, var2);
               double var10000 = var4.xCoord;
               mc.getRenderManager();
               double var5 = var10000 - RenderManager.renderPosX;
               var10000 = var4.yCoord;
               mc.getRenderManager();
               double var7 = var10000 - RenderManager.renderPosY;
               var10000 = var4.zCoord;
               mc.getRenderManager();
               double var9 = var10000 - RenderManager.renderPosZ;
               GL11.glTranslated(var5, var7, var9);
               float var11 = var2.prevRenderYawOffset + (var2.renderYawOffset - var2.prevRenderYawOffset) * var1.getPartialTicks();
               GL11.glRotatef(-var11, 0.0F, 1.0F, 0.0F);
               GL11.glTranslated(0.0D, 0.0D, var2.isSneaking() ? -0.235D : 0.0D);
               float var12 = var2.isSneaking() ? 0.6F : 0.75F;
               GL11.glPushMatrix();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glTranslated(-0.125D, (double)var12, 0.0D);
               if (var3[3][0] != 0.0F) {
                  GL11.glRotatef(var3[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
               }

               if (var3[3][1] != 0.0F) {
                  GL11.glRotatef(var3[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
               }

               if (var3[3][2] != 0.0F) {
                  GL11.glRotatef(var3[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
               }

               GL11.glBegin(3);
               GL11.glVertex3d(0.0D, 0.0D, 0.0D);
               GL11.glVertex3d(0.0D, (double)(-var12), 0.0D);
               GL11.glEnd();
               GL11.glPopMatrix();
               GL11.glPushMatrix();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glTranslated(0.125D, (double)var12, 0.0D);
               if (var3[4][0] != 0.0F) {
                  GL11.glRotatef(var3[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
               }

               if (var3[4][1] != 0.0F) {
                  GL11.glRotatef(var3[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
               }

               if (var3[4][2] != 0.0F) {
                  GL11.glRotatef(var3[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
               }

               GL11.glBegin(3);
               GL11.glVertex3d(0.0D, 0.0D, 0.0D);
               GL11.glVertex3d(0.0D, (double)(-var12), 0.0D);
               GL11.glEnd();
               GL11.glPopMatrix();
               GL11.glTranslated(0.0D, 0.0D, var2.isSneaking() ? 0.25D : 0.0D);
               GL11.glPushMatrix();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glTranslated(0.0D, var2.isSneaking() ? -0.05D : 0.0D, var2.isSneaking() ? -0.01725D : 0.0D);
               GL11.glPushMatrix();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glTranslated(-0.375D, (double)var12 + 0.55D, 0.0D);
               if (var3[1][0] != 0.0F) {
                  GL11.glRotatef(var3[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
               }

               if (var3[1][1] != 0.0F) {
                  GL11.glRotatef(var3[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
               }

               if (var3[1][2] != 0.0F) {
                  GL11.glRotatef(-var3[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
               }

               GL11.glBegin(3);
               GL11.glVertex3d(0.0D, 0.0D, 0.0D);
               GL11.glVertex3d(0.0D, -0.5D, 0.0D);
               GL11.glEnd();
               GL11.glPopMatrix();
               GL11.glPushMatrix();
               GL11.glTranslated(0.375D, (double)var12 + 0.55D, 0.0D);
               if (var3[2][0] != 0.0F) {
                  GL11.glRotatef(var3[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
               }

               if (var3[2][1] != 0.0F) {
                  GL11.glRotatef(var3[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
               }

               if (var3[2][2] != 0.0F) {
                  GL11.glRotatef(-var3[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
               }

               GL11.glBegin(3);
               GL11.glVertex3d(0.0D, 0.0D, 0.0D);
               GL11.glVertex3d(0.0D, -0.5D, 0.0D);
               GL11.glEnd();
               GL11.glPopMatrix();
               GL11.glRotatef(var11 - var2.rotationYawHead, 0.0F, 1.0F, 0.0F);
               GL11.glPushMatrix();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glTranslated(0.0D, (double)var12 + 0.55D, 0.0D);
               if (var3[0][0] != 0.0F) {
                  GL11.glRotatef(var3[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
               }

               GL11.glBegin(3);
               GL11.glVertex3d(0.0D, 0.0D, 0.0D);
               GL11.glVertex3d(0.0D, 0.3D, 0.0D);
               GL11.glEnd();
               GL11.glPopMatrix();
               GL11.glPopMatrix();
               GL11.glRotatef(var2.isSneaking() ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
               GL11.glTranslated(0.0D, var2.isSneaking() ? -0.16175D : 0.0D, var2.isSneaking() ? -0.48025D : 0.0D);
               GL11.glPushMatrix();
               GL11.glTranslated(0.0D, (double)var12, 0.0D);
               GL11.glBegin(3);
               GL11.glVertex3d(-0.125D, 0.0D, 0.0D);
               GL11.glVertex3d(0.125D, 0.0D, 0.0D);
               GL11.glEnd();
               GL11.glPopMatrix();
               GL11.glPushMatrix();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glTranslated(0.0D, (double)var12, 0.0D);
               GL11.glBegin(3);
               GL11.glVertex3d(0.0D, 0.0D, 0.0D);
               GL11.glVertex3d(0.0D, 0.55D, 0.0D);
               GL11.glEnd();
               GL11.glPopMatrix();
               GL11.glPushMatrix();
               GL11.glTranslated(0.0D, (double)var12 + 0.55D, 0.0D);
               GL11.glBegin(3);
               GL11.glVertex3d(-0.375D, 0.0D, 0.0D);
               GL11.glVertex3d(0.375D, 0.0D, 0.0D);
               GL11.glEnd();
               GL11.glPopMatrix();
               GL11.glPopMatrix();
            }
         }
      }

   }

   private boolean doesntContain(EntityPlayer var1) {
      Minecraft var10000 = mc;
      return !Minecraft.theWorld.playerEntities.contains(var1);
   }

   public static void addEntity(EntityPlayer var0, ModelPlayer var1) {
      entities.put(var0, new float[][]{{var1.bipedHead.rotateAngleX, var1.bipedHead.rotateAngleY, var1.bipedHead.rotateAngleZ}, {var1.bipedRightArm.rotateAngleX, var1.bipedRightArm.rotateAngleY, var1.bipedRightArm.rotateAngleZ}, {var1.bipedLeftArm.rotateAngleX, var1.bipedLeftArm.rotateAngleY, var1.bipedLeftArm.rotateAngleZ}, {var1.bipedRightLeg.rotateAngleX, var1.bipedRightLeg.rotateAngleY, var1.bipedRightLeg.rotateAngleZ}, {var1.bipedLeftLeg.rotateAngleX, var1.bipedLeftLeg.rotateAngleY, var1.bipedLeftLeg.rotateAngleZ}});
   }

   @EventTarget
   private void onRender3D(Render3DEvent var1) {
      this.startEnd(true);
      GL11.glEnable(2903);
      GL11.glDisable(2848);
      entities.keySet().removeIf(this::doesntContain);
      Minecraft var10000 = mc;
      Iterator var4 = Minecraft.theWorld.playerEntities.iterator();

      while(var4.hasNext()) {
         EntityPlayer var3 = (EntityPlayer)var4.next();
         this.drawSkeleton(var1, var3);
      }

      Gui.drawRect(0.0D, 0.0D, 0.0D, 0.0D, 0);
      this.startEnd(false);
   }

   public Skeleto() {
      width = 1.0D;
   }

   private Vec3 getVec3(Render3DEvent var1, EntityPlayer var2) {
      float var3 = var1.getPartialTicks();
      double var4 = var2.lastTickPosX + (var2.posX - var2.lastTickPosX) * (double)var3;
      double var6 = var2.lastTickPosY + (var2.posY - var2.lastTickPosY) * (double)var3;
      double var8 = var2.lastTickPosZ + (var2.posZ - var2.lastTickPosZ) * (double)var3;
      return new Vec3(var4, var6, var8);
   }

   private void startEnd(boolean var1) {
      if (var1) {
         GlStateManager.pushMatrix();
         GlStateManager.enableBlend();
         GL11.glEnable(2848);
         GlStateManager.disableDepth();
         GlStateManager.disableTexture2D();
         GL11.glHint(3154, 4354);
      } else {
         GlStateManager.disableBlend();
         GlStateManager.enableTexture2D();
         GL11.glDisable(2848);
         GlStateManager.enableDepth();
         GlStateManager.popMatrix();
         GlStateManager.depthMask(!var1);
      }

   }
}
