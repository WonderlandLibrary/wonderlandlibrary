/*    */ package org.neverhook.client.event.events.impl.render;
/*    */ 
/*    */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventRenderChunk
/*    */   extends EventCancellable {
/*    */   public BlockPos blockPos;
/*    */   public RenderChunk renderChunk;
/*    */   
/*    */   public EventRenderChunk(RenderChunk renderChunk, BlockPos blockPos) {
/* 13 */     this.blockPos = blockPos;
/* 14 */     this.renderChunk = renderChunk;
/*    */   }
/*    */   
/*    */   public BlockPos getBlockPos() {
/* 18 */     return this.blockPos;
/*    */   }
/*    */   
/*    */   public void setBlockPos(BlockPos blockPos) {
/* 22 */     this.blockPos = blockPos;
/*    */   }
/*    */   
/*    */   public RenderChunk getRenderChunk() {
/* 26 */     return this.renderChunk;
/*    */   }
/*    */   
/*    */   public void setRenderChunk(RenderChunk renderChunk) {
/* 30 */     this.renderChunk = renderChunk;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\render\EventRenderChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */