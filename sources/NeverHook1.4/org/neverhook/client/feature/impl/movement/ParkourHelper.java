/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class ParkourHelper extends Feature {
/*    */   public ParkourHelper() {
/* 11 */     super("ParkourHelper", "Автоматически прыгает на конце блока", Type.Movement);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 16 */     if (mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(-0.001D, 0.0D, -0.001D)).isEmpty() && mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown())
/* 17 */       mc.player.jump(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\ParkourHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */