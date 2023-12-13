/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class WaterLeave extends Feature {
/*    */   private final NumberSetting leaveMotion;
/*    */   
/*    */   public WaterLeave() {
/* 16 */     super("WaterLeave", "Игрок высоко прыгает при погружении в воду", Type.Movement);
/* 17 */     this.leaveMotion = new NumberSetting("Leave Motion", 10.0F, 0.5F, 20.0F, 0.5F, () -> Boolean.valueOf(true));
/* 18 */     addSettings(new Setting[] { (Setting)this.leaveMotion });
/*    */   }
/*    */ 
/*    */   
/*    */   @EventTarget
/*    */   public void onPreMotion(EventPreMotion event) {
/* 24 */     if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.95D, mc.player.posZ)).getBlock() == Blocks.WATER) {
/* 25 */       mc.player.motionY = this.leaveMotion.getNumberValue();
/* 26 */       mc.player.onGround = true;
/* 27 */       mc.player.isAirBorne = true;
/*    */     } 
/* 29 */     if (mc.player.isInWater() || mc.player.isInLava()) {
/* 30 */       mc.player.onGround = true;
/*    */     }
/* 32 */     if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1.0E-7D, mc.player.posZ)).getBlock() == Blocks.WATER)
/* 33 */       mc.player.motionY = 0.05999999865889549D; 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\WaterLeave.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */