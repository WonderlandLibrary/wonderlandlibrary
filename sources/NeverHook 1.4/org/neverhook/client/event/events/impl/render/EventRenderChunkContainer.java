/*    */ package org.neverhook.client.event.events.impl.render;
/*    */ 
/*    */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventRenderChunkContainer
/*    */   extends EventCancellable {
/*    */   public RenderChunk renderChunk;
/*    */   
/*    */   public EventRenderChunkContainer(RenderChunk renderChunk) {
/* 11 */     this.renderChunk = renderChunk;
/*    */   }
/*    */   
/*    */   public RenderChunk getRenderChunk() {
/* 15 */     return this.renderChunk;
/*    */   }
/*    */   
/*    */   public void setRenderChunk(RenderChunk renderChunk) {
/* 19 */     this.renderChunk = renderChunk;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\render\EventRenderChunkContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */