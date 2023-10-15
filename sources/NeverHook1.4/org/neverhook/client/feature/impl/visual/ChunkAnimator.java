/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.render.EventRenderChunk;
/*    */ import org.neverhook.client.event.events.impl.render.EventRenderChunkContainer;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class ChunkAnimator
/*    */   extends Feature
/*    */ {
/* 16 */   private final HashMap<RenderChunk, AtomicLong> renderChunkMap = new HashMap<>();
/*    */   
/*    */   public ChunkAnimator() {
/* 19 */     super("ChunkAnimator", "Анимирует твои чанки", Type.Visuals);
/*    */   }
/*    */   
/*    */   private double easeOutCubic(double t) {
/* 23 */     return --t * t * t + 1.0D;
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   private void onRenderChunk(EventRenderChunk event) {
/* 28 */     if (mc.player != null && 
/* 29 */       !this.renderChunkMap.containsKey(event.getRenderChunk())) {
/* 30 */       this.renderChunkMap.put(event.getRenderChunk(), new AtomicLong(-1L));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   @EventTarget
/*    */   private void onChunkRender(EventRenderChunkContainer event) {
/* 37 */     if (this.renderChunkMap.containsKey(event.getRenderChunk())) {
/* 38 */       AtomicLong timeAlive = this.renderChunkMap.get(event.getRenderChunk());
/* 39 */       long timeClone = timeAlive.get();
/* 40 */       if (timeClone == -1L) {
/* 41 */         timeClone = System.currentTimeMillis();
/* 42 */         timeAlive.set(timeClone);
/*    */       } 
/*    */       
/* 45 */       long timeDifference = System.currentTimeMillis() - timeClone;
/* 46 */       if (timeDifference <= 250L) {
/* 47 */         double chunkY = event.getRenderChunk().getPosition().getY();
/*    */         
/* 49 */         double offsetY = chunkY * easeOutCubic(((float)timeDifference / 250.0F));
/* 50 */         GlStateManager.translate(0.0D, -chunkY + offsetY, 0.0D);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\ChunkAnimator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */