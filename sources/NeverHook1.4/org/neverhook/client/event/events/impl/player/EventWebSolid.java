/*    */ package org.neverhook.client.event.events.impl.player;
/*    */ 
/*    */ import net.minecraft.block.BlockWeb;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventWebSolid
/*    */   extends EventCancellable {
/*    */   private final BlockWeb blockWeb;
/*    */   private final BlockPos pos;
/*    */   
/*    */   public EventWebSolid(BlockWeb blockLiquid, BlockPos pos) {
/* 13 */     this.blockWeb = blockLiquid;
/* 14 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public BlockWeb getBlock() {
/* 18 */     return this.blockWeb;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 22 */     return this.pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\player\EventWebSolid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */