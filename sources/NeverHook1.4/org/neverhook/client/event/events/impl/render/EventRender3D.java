/*    */ package org.neverhook.client.event.events.impl.render;
/*    */ 
/*    */ import org.neverhook.client.event.events.Event;
/*    */ 
/*    */ public class EventRender3D implements Event {
/*    */   private final float partialTicks;
/*    */   
/*    */   public EventRender3D(float partialTicks) {
/*  9 */     this.partialTicks = partialTicks;
/*    */   }
/*    */   
/*    */   public float getPartialTicks() {
/* 13 */     return this.partialTicks;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\render\EventRender3D.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */