/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventBlockInteract;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class AutoTool
/*    */   extends Feature {
/*    */   public AutoTool() {
/* 14 */     super("AutoTool", "Автоматически берет лучший инструмент в руки при ломании блока", Type.Player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onBlockInteract(EventBlockInteract event) {
/* 19 */     BlockPos blockPos = mc.objectMouseOver.getBlockPos();
/* 20 */     Block block = mc.world.getBlockState(blockPos).getBlock();
/* 21 */     float power = 1.0F;
/* 22 */     int itemCount = -1;
/* 23 */     for (int i = 0; i < 9; i++) {
/* 24 */       ItemStack itemStack = (ItemStack)mc.player.inventory.mainInventory.get(i);
/* 25 */       ItemStack current = mc.player.inventory.getCurrentItem();
/* 26 */       if (itemStack.getStrVsBlock(block.getDefaultState()) > power && current.getStrVsBlock(block.getDefaultState()) <= power) {
/* 27 */         power = itemStack.getStrVsBlock(block.getDefaultState());
/* 28 */         itemCount = i;
/*    */       } 
/*    */     } 
/* 31 */     if (itemCount != -1)
/* 32 */       mc.player.inventory.currentItem = itemCount; 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\AutoTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */