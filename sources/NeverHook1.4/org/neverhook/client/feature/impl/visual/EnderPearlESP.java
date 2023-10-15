/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.render.RenderHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ 
/*    */ public class EnderPearlESP
/*    */   extends Feature
/*    */ {
/* 19 */   public BooleanSetting tracers = new BooleanSetting("Tracers", true, () -> Boolean.valueOf(true));
/*    */   
/*    */   public EnderPearlESP() {
/* 22 */     super("EnderPearlESP", "Показывает есп перла", Type.Visuals);
/* 23 */     addSettings(new Setting[] { (Setting)this.tracers });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender3D(EventRender3D event) {
/* 28 */     GlStateManager.pushMatrix();
/* 29 */     for (Entity entity : mc.world.loadedEntityList) {
/* 30 */       if (entity instanceof net.minecraft.entity.item.EntityEnderPearl) {
/* 31 */         boolean viewBobbing = mc.gameSettings.viewBobbing;
/* 32 */         mc.gameSettings.viewBobbing = false;
/* 33 */         mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
/* 34 */         mc.gameSettings.viewBobbing = viewBobbing;
/* 35 */         if (this.tracers.getBoolValue()) {
/* 36 */           GL11.glPushMatrix();
/* 37 */           GL11.glEnable(2848);
/* 38 */           GL11.glDisable(2929);
/* 39 */           GL11.glDisable(3553);
/* 40 */           GL11.glDisable(2896);
/* 41 */           GL11.glDepthMask(false);
/* 42 */           GL11.glBlendFunc(770, 771);
/* 43 */           GL11.glEnable(3042);
/* 44 */           GL11.glLineWidth(1.0F);
/* 45 */           double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - (mc.getRenderManager()).renderPosX;
/* 46 */           double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - (mc.getRenderManager()).renderPosY - 1.0D;
/* 47 */           double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - (mc.getRenderManager()).renderPosZ;
/* 48 */           RenderHelper.setColor(-1);
/* 49 */           Vec3d vec = (new Vec3d(0.0D, 0.0D, 1.0D)).rotatePitch((float)-Math.toRadians(mc.player.rotationPitch)).rotateYaw((float)-Math.toRadians(mc.player.rotationYaw));
/* 50 */           GL11.glBegin(2);
/* 51 */           GL11.glVertex3d(vec.xCoord, mc.player.getEyeHeight() + vec.yCoord, vec.zCoord);
/* 52 */           GL11.glVertex3d(x, y + 1.1D, z);
/* 53 */           GL11.glEnd();
/* 54 */           GL11.glDisable(3042);
/* 55 */           GL11.glDepthMask(true);
/* 56 */           GL11.glEnable(3553);
/* 57 */           GL11.glEnable(2929);
/* 58 */           GL11.glDisable(2848);
/* 59 */           GL11.glPopMatrix();
/*    */         } 
/* 61 */         RenderHelper.drawEntityBox(entity, Color.WHITE, true, 0.2F);
/*    */       } 
/*    */     } 
/* 64 */     GlStateManager.popMatrix();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\EnderPearlESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */