/*    */ package org.neverhook.client.event.events.impl.player;
/*    */ 
/*    */ import net.minecraft.block.BlockLiquid;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventLiquidSolid
/*    */   extends EventCancellable {
/*    */   private final BlockLiquid blockLiquid;
/*    */   private final BlockPos pos;
/*    */   
/*    */   public EventLiquidSolid(BlockLiquid blockLiquid, BlockPos pos) {
/* 13 */     this.blockLiquid = blockLiquid;
/* 14 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public BlockLiquid getBlock() {
/* 18 */     return this.blockLiquid;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 22 */     return this.pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\player\EventLiquidSolid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */