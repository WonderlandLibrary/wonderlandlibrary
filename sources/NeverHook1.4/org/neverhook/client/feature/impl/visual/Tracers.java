/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.ClientHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Tracers
/*    */   extends Feature
/*    */ {
/*    */   public static BooleanSetting clientColor;
/*    */   public static ColorSetting colorGlobal;
/*    */   public static BooleanSetting friend;
/* 26 */   public static BooleanSetting onlyPlayer = new BooleanSetting("Only Player", true, () -> Boolean.valueOf(true));
/*    */   public static NumberSetting width;
/* 28 */   public static BooleanSetting seeOnly = new BooleanSetting("See Only", false, () -> Boolean.valueOf(true));
/*    */   
/*    */   public Tracers() {
/* 31 */     super("Tracers", "Рисует линию к игрокам", Type.Visuals);
/* 32 */     clientColor = new BooleanSetting("Client Colored", false, () -> Boolean.valueOf(true));
/* 33 */     friend = new BooleanSetting("Friend Highlight", true, () -> Boolean.valueOf(true));
/* 34 */     colorGlobal = new ColorSetting("Tracers Color", (new Color(16777215)).getRGB(), () -> Boolean.valueOf(!clientColor.getBoolValue()));
/* 35 */     width = new NumberSetting("Tracers Width", 1.5F, 0.1F, 5.0F, 0.1F, () -> Boolean.valueOf(true));
/* 36 */     addSettings(new Setting[] { (Setting)colorGlobal, (Setting)friend, (Setting)seeOnly, (Setting)onlyPlayer, (Setting)width, (Setting)clientColor });
/*    */   }
/*    */   
/*    */   public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
/* 40 */     double diffX = entityLiving.posX - mc.player.posX;
/* 41 */     double diffZ = entityLiving.posZ - mc.player.posZ;
/* 42 */     float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D);
/* 43 */     double difference = angleDifference(yaw, mc.player.rotationYaw);
/* 44 */     return (difference <= scope);
/*    */   }
/*    */   
/*    */   public static double angleDifference(float oldYaw, float newYaw) {
/* 48 */     float yaw = Math.abs(oldYaw - newYaw) % 360.0F;
/* 49 */     if (yaw > 180.0F) {
/* 50 */       yaw = 360.0F - yaw;
/*    */     }
/* 52 */     return yaw;
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onEvent3D(EventRender3D event) {
/* 57 */     Color color = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(colorGlobal.getColorValue());
/* 58 */     for (Entity entity : mc.world.loadedEntityList) {
/* 59 */       if (entity == mc.player || (
/* 60 */         onlyPlayer.getBoolValue() && !(entity instanceof net.minecraft.entity.player.EntityPlayer))) {
/*    */         continue;
/*    */       }
/* 63 */       if (seeOnly.getBoolValue() && !canSeeEntityAtFov(entity, 150.0F)) {
/*    */         return;
/*    */       }
/* 66 */       boolean old = mc.gameSettings.viewBobbing;
/* 67 */       mc.gameSettings.viewBobbing = false;
/* 68 */       mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
/* 69 */       mc.gameSettings.viewBobbing = old;
/*    */       
/* 71 */       double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - (mc.getRenderManager()).renderPosX;
/* 72 */       double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - (mc.getRenderManager()).renderPosY - 1.0D;
/* 73 */       double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - (mc.getRenderManager()).renderPosZ;
/*    */       
/* 75 */       GlStateManager.blendFunc(770, 771);
/* 76 */       GlStateManager.enable(3042);
/* 77 */       GlStateManager.enable(2848);
/* 78 */       GlStateManager.glLineWidth(width.getNumberValue());
/* 79 */       GlStateManager.disable(3553);
/* 80 */       GlStateManager.disable(2929);
/* 81 */       GlStateManager.depthMask(false);
/* 82 */       if (NeverHook.instance.friendManager.isFriend(entity.getName()) && friend.getBoolValue()) {
/* 83 */         GlStateManager.color(0.0F, 255.0F, 0.0F, 255.0F);
/*    */       } else {
/* 85 */         GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
/*    */       } 
/* 87 */       GlStateManager.glBegin(3);
/* 88 */       Vec3d vec = (new Vec3d(0.0D, 0.0D, 1.0D)).rotatePitch((float)-Math.toRadians(mc.player.rotationPitch)).rotateYaw((float)-Math.toRadians(mc.player.rotationYaw));
/* 89 */       GlStateManager.glVertex3d(vec.xCoord, mc.player.getEyeHeight() + vec.yCoord, vec.zCoord);
/* 90 */       GlStateManager.glVertex3d(x, y + 1.1D, z);
/* 91 */       GlStateManager.glEnd();
/* 92 */       GlStateManager.enable(3553);
/* 93 */       GlStateManager.disable(2848);
/* 94 */       GlStateManager.enable(2929);
/* 95 */       GlStateManager.depthMask(true);
/* 96 */       GlStateManager.disable(3042);
/* 97 */       GlStateManager.resetColor();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\Tracers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */