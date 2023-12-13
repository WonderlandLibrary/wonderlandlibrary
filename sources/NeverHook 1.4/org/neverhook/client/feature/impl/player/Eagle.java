/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class Eagle
/*    */   extends Feature {
/*    */   public Eagle() {
/* 13 */     super("Eagle", "Нажимает шифт когда строишься", Type.Ghost);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 18 */     BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ);
/*    */     
/* 20 */     mc.gameSettings.keyBindSneak.pressed = (mc.world.getBlockState(pos).getBlock() == Blocks.AIR);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\Eagle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */