package my.NewSnake.Tank.module.modules.COMBAT;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render3DEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

@Module.Mod
public class ESP extends Module {
   private static final Map glCapMap = new HashMap();
   @Option.Op
   public boolean Esp = true;

   public static int novoline(int var0) {
      double var1 = Math.ceil((double)(System.currentTimeMillis() + (long)var0) / 10.0D);
      var1 %= 360.0D;
      return Color.getHSBColor((float)(var1 / 180.0D), 0.3F, 1.0F).getRGB();
   }

   private void drawVerticalLine(double var1, double var3, double var5, double var7, Color var9) {
      Tessellator var10 = Tessellator.getInstance();
      WorldRenderer var11 = var10.getWorldRenderer();
      var11.begin(7, DefaultVertexFormats.POSITION_COLOR);
      var11.pos(var1 - var5, var3 - var7 / 2.0D, 0.0D).color((float)var9.getRed() / 255.0F, (float)var9.getGreen() / 255.0F, (float)var9.getBlue() / 255.0F, (float)var9.getAlpha() / 255.0F).endVertex();
      var11.pos(var1 - var5, var3 + var7 / 2.0D, 0.0D).color((float)var9.getRed() / 255.0F, (float)var9.getGreen() / 255.0F, (float)var9.getBlue() / 255.0F, (float)var9.getAlpha() / 255.0F).endVertex();
      var11.pos(var1 + var5, var3 + var7 / 2.0D, 0.0D).color((float)var9.getRed() / 255.0F, (float)var9.getGreen() / 255.0F, (float)var9.getBlue() / 255.0F, (float)var9.getAlpha() / 255.0F).endVertex();
      var11.pos(var1 + var5, var3 - var7 / 2.0D, 0.0D).color((float)var9.getRed() / 255.0F, (float)var9.getGreen() / 255.0F, (float)var9.getBlue() / 255.0F, (float)var9.getAlpha() / 255.0F).endVertex();
      var10.draw();
   }

   private void doCornerESP() {
      Minecraft var10000 = mc;
      Iterator var1 = Minecraft.theWorld.playerEntities.iterator();

      while(var1.hasNext()) {
         EntityPlayer var2 = (EntityPlayer)var1.next();
         Minecraft var10001 = mc;
         if (var2 != Minecraft.thePlayer) {
            if (this == var2) {
               return;
            }

            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.enableBlend();
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            float var3 = mc.timer.renderPartialTicks;
            double var29 = var2.lastTickPosX + (var2.posX - var2.lastTickPosX) * (double)var3;
            mc.getRenderManager();
            double var4 = var29 - RenderManager.renderPosX;
            var29 = var2.lastTickPosY + (var2.posY - var2.lastTickPosY) * (double)var3;
            mc.getRenderManager();
            double var6 = var29 - RenderManager.renderPosY;
            var29 = var2.lastTickPosZ + (var2.posZ - var2.lastTickPosZ) * (double)var3;
            mc.getRenderManager();
            double var8 = var29 - RenderManager.renderPosZ;
            var10000 = mc;
            float var10 = Minecraft.thePlayer.getDistanceToEntity(var2);
            float var11 = Math.min(var10 * 0.15F, 2.5F);
            float var12 = 0.035F;
            var12 /= 2.0F;
            GlStateManager.translate((float)var4, (float)var6 + var2.height + 0.5F - (var2.isChild() ? var2.height / 2.0F : 0.0F), (float)var8);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            mc.getRenderManager();
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-var12, -var12, -var12);
            Tessellator var13 = Tessellator.getInstance();
            WorldRenderer var14 = var13.getWorldRenderer();
            Color var15 = new Color(16776960);
            if (var2.hurtTime > 0) {
               var15 = new Color(16711680);
            }

            Color var16 = new Color(0, 0, 0);
            double var17 = (double)(2.0F + var10 * 0.08F);
            double var19 = -30.0D;
            double var21 = 30.0D;
            double var23 = 20.0D;
            double var25 = 130.0D;
            double var27 = 10.0D;
            this.drawVerticalLine(var19 + var27 / 2.0D + 1.0D, var23 + 1.0D, var27 / 2.0D, var17, var16);
            this.drawHorizontalLine(var19 + 1.0D, var23 + var27 + 1.0D, var27, var17, var16);
            this.drawVerticalLine(var19 + var27 / 2.0D, var23, var27 / 2.0D, var17, var15);
            this.drawHorizontalLine(var19, var23 + var27, var27, var17, var15);
            this.drawVerticalLine(var21 - var27 / 2.0D + 1.0D, var23 + 1.0D, var27 / 2.0D, var17, var16);
            this.drawHorizontalLine(var21 + 1.0D, var23 + var27 + 1.0D, var27, var17, var16);
            this.drawVerticalLine(var21 - var27 / 2.0D, var23, var27 / 2.0D, var17, var15);
            this.drawHorizontalLine(var21, var23 + var27, var27, var17, var15);
            this.drawVerticalLine(var19 + var27 / 2.0D + 1.0D, var25 + 1.0D, var27 / 2.0D, var17, var16);
            this.drawHorizontalLine(var19 + 1.0D, var25 + 1.0D - var27, var27, var17, var16);
            this.drawVerticalLine(var19 + var27 / 2.0D, var25, var27 / 2.0D, var17, var15);
            this.drawHorizontalLine(var19, var25 - var27, var27, var17, var15);
            this.drawVerticalLine(var21 - var27 / 2.0D + 1.0D, var25 + 1.0D, var27 / 2.0D, var17, var16);
            this.drawHorizontalLine(var21 + 1.0D, var25 - var27 + 1.0D, var27, var17, var16);
            this.drawVerticalLine(var21 - var27 / 2.0D, var25, var27 / 2.0D, var17, var15);
            this.drawHorizontalLine(var21, var25 - var27, var27, var17, var15);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GlStateManager.disableBlend();
            GL11.glDisable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glNormal3f(1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
         }
      }

   }

   @EventTarget
   private void onRender3D(Render3DEvent var1) {
      Iterator var3 = ClientUtils.loadedEntityList().iterator();

      while(var3.hasNext()) {
         Entity var2 = (Entity)var3.next();
         if (var2 instanceof EntityLivingBase) {
            EntityLivingBase var4 = (EntityLivingBase)var2;
            if (this == var4) {
               this.drawEntityBox(var4);
            }
         }
      }

   }

   private void drawHorizontalLine(double var1, double var3, double var5, double var7, Color var9) {
      Tessellator var10 = Tessellator.getInstance();
      WorldRenderer var11 = var10.getWorldRenderer();
      var11.begin(7, DefaultVertexFormats.POSITION_COLOR);
      var11.pos(var1 - var7 / 2.0D, var3 - var5, 0.0D).color((float)var9.getRed() / 255.0F, (float)var9.getGreen() / 255.0F, (float)var9.getBlue() / 255.0F, (float)var9.getAlpha() / 255.0F).endVertex();
      var11.pos(var1 - var7 / 2.0D, var3 + var5, 0.0D).color((float)var9.getRed() / 255.0F, (float)var9.getGreen() / 255.0F, (float)var9.getBlue() / 255.0F, (float)var9.getAlpha() / 255.0F).endVertex();
      var11.pos(var1 + var7 / 2.0D, var3 + var5, 0.0D).color((float)var9.getRed() / 255.0F, (float)var9.getGreen() / 255.0F, (float)var9.getBlue() / 255.0F, (float)var9.getAlpha() / 255.0F).endVertex();
      var11.pos(var1 + var7 / 2.0D, var3 - var5, 0.0D).color((float)var9.getRed() / 255.0F, (float)var9.getGreen() / 255.0F, (float)var9.getBlue() / 255.0F, (float)var9.getAlpha() / 255.0F).endVertex();
      var10.draw();
   }

   public void drawEntityBox(EntityLivingBase var1) {
      double var10000 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)mc.timer.renderPartialTicks;
      mc.getRenderManager();
      double var2 = var10000 - RenderManager.renderPosX;
      var10000 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)mc.timer.renderPartialTicks;
      mc.getRenderManager();
      double var4 = var10000 - RenderManager.renderPosY;
      var10000 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)mc.timer.renderPartialTicks;
      mc.getRenderManager();
      double var6 = var10000 - RenderManager.renderPosZ;
      int var8 = Color.RED.getRGB();
      GL11.glPushMatrix();
      GlStateManager.enableBlend();
      GL11.glTranslated(var2, var4, var6);
      GL11.glScalef(0.03F, 0.03F, 0.03F);
      mc.getRenderManager();
      GL11.glRotated((double)(-RenderManager.playerViewY), 0.0D, 1.0D, 0.0D);
      GlStateManager.disableDepth();
      Color var9 = Color.WHITE;
      if (var1.hurtTime != 0) {
         var9 = Color.RED;
      }

      double var10 = (double)var1.getHealth();
      Color var12 = Color.lightGray;
      if (var10 >= 15.0D) {
         var12 = Color.green;
      } else if (var10 < 15.0D && var10 >= 6.0D) {
         var12 = Color.yellow;
      } else {
         var12 = Color.red;
      }

      if (this.Esp) {
         Gui.drawRect(-20.0D, 73.0D, 19.0D, 72.0D, (new Color(255, 255, 255)).getRGB());
         Gui.drawRect(-20.0D, 73.0D, -19.0D, 0.0D, (new Color(255, 255, 255)).getRGB());
         Gui.drawRect(-20.0D, 1.0D, 19.0D, 0.0D, (new Color(255, 255, 255)).getRGB());
         Gui.drawRect(20.0D, 73.0D, 19.0D, 0.0D, (new Color(255, 255, 255)).getRGB());
         float var13 = var1.getHealth() / var1.getMaxHealth();
         byte var14 = 73;
         Gui.drawRect(20.0D, 73.0D, 19.0D, 0.0D, (new Color(255, 255, 255)).getRGB());
         Gui.drawRect(30.0D, (double)var14, 29.0D, 0.0D, (new Color(255, 255, 255)).getRGB());
         Gui.drawRect(30.0D, (double)((float)var14 * var13), 29.0D, 0.0D, var12.getRGB());
      }

      boolean var15 = true;
      boolean var16 = true;
      if (this.Esp) {
         GlStateManager.enableDepth();
      }

      GL11.glPopMatrix();
   }
}
