/*    */ package org.neverhook.client.feature.impl.player;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ import net.minecraft.world.World;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.player.InventoryHelper;
/*    */ 
/*    */ public class MLG extends Feature {
/*    */   public MLG() {
/* 14 */     super("MLG", "Ставит воду под тобой", Type.Player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventPreMotion event) {
/* 19 */     int oldSlot = mc.player.inventory.currentItem;
/* 20 */     if (mc.player.fallDistance > 5.0F) {
/* 21 */       RayTraceResult traceResult = mc.world.rayTraceBlocks(mc.player.getPositionVector(), mc.player.getPositionVector().addVector(0.0D, -15.0D, 0.0D), true, false, false);
/* 22 */       if (traceResult != null && InventoryHelper.findWaterBucket() > 0 && traceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
/* 23 */         event.setPitch(86.0F);
/* 24 */         mc.player.inventory.currentItem = InventoryHelper.findWaterBucket();
/* 25 */         mc.playerController.processRightClick((EntityPlayer)mc.player, (World)mc.world, EnumHand.MAIN_HAND);
/* 26 */         mc.player.inventory.currentItem = oldSlot;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\MLG.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */