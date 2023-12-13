/*    */ package org.neverhook.client.feature.impl.combat;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.math.RotationHelper;
/*    */ 
/*    */ public class AimBot
/*    */   extends Feature
/*    */ {
/*    */   public AimBot() {
/* 14 */     super("AimBot", "", Type.Combat);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPreMotion(EventPreMotion event) {
/* 19 */     for (Entity entity : mc.world.loadedEntityList) {
/* 20 */       if (mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemBow && mc.player.getDistanceToEntity(entity) < 6.0F && entity != null) {
/* 21 */         float[] rots = RotationHelper.getRotations(entity, false, 360.0F, 360.0F, 2.0F, 2.0F);
/* 22 */         event.setYaw(rots[0]);
/* 23 */         mc.player.rotationYawHead = rots[0];
/* 24 */         mc.player.renderYawOffset = rots[0];
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\AimBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */