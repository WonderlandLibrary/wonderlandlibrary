/*    */ package org.neverhook.client.event.events.impl.render;
/*    */ 
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.renderer.BufferBuilder;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventRenderBlock
/*    */   extends EventCancellable {
/*    */   private final IBlockState state;
/*    */   private final BlockPos pos;
/*    */   private final IBlockAccess access;
/*    */   private final BufferBuilder bufferBuilder;
/*    */   
/*    */   public EventRenderBlock(IBlockState state, BlockPos pos, IBlockAccess access, BufferBuilder bufferBuilder) {
/* 17 */     this.state = state;
/* 18 */     this.pos = pos;
/* 19 */     this.access = access;
/* 20 */     this.bufferBuilder = bufferBuilder;
/*    */   }
/*    */   
/*    */   public IBlockState getState() {
/* 24 */     return this.state;
/*    */   }
/*    */   
/*    */   public BufferBuilder getBufferBuilder() {
/* 28 */     return this.bufferBuilder;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 32 */     return this.pos;
/*    */   }
/*    */   
/*    */   public IBlockAccess getAccess() {
/* 36 */     return this.access;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\render\EventRenderBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */