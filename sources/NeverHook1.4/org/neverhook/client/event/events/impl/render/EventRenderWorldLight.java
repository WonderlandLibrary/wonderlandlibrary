/*    */ package org.neverhook.client.event.events.impl.render;
/*    */ 
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.EnumSkyBlock;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventRenderWorldLight
/*    */   extends EventCancellable {
/*    */   private final EnumSkyBlock enumSkyBlock;
/*    */   private final BlockPos pos;
/*    */   
/*    */   public EventRenderWorldLight(EnumSkyBlock enumSkyBlock, BlockPos pos) {
/* 13 */     this.enumSkyBlock = enumSkyBlock;
/* 14 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public EnumSkyBlock getEnumSkyBlock() {
/* 18 */     return this.enumSkyBlock;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 22 */     return this.pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\render\EventRenderWorldLight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */