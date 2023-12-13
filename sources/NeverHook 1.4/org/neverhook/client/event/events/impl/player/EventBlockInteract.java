/*    */ package org.neverhook.client.event.events.impl.player;
/*    */ 
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventBlockInteract
/*    */   extends EventCancellable {
/*    */   private BlockPos pos;
/*    */   private EnumFacing face;
/*    */   
/*    */   public EventBlockInteract(BlockPos pos, EnumFacing face) {
/* 13 */     this.pos = pos;
/* 14 */     this.face = face;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 18 */     return this.pos;
/*    */   }
/*    */   
/*    */   public void setPos(BlockPos pos) {
/* 22 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public EnumFacing getFace() {
/* 26 */     return this.face;
/*    */   }
/*    */   
/*    */   public void setFace(EnumFacing face) {
/* 30 */     this.face = face;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\player\EventBlockInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */