/*    */ package org.neverhook.client.event.events.impl.render;
/*    */ 
/*    */ import org.neverhook.client.event.events.Event;
/*    */ import org.neverhook.client.event.types.EventType;
/*    */ 
/*    */ public class EventRenderScoreboard
/*    */   implements Event {
/*    */   private EventType state;
/*    */   
/*    */   public EventRenderScoreboard(EventType state) {
/* 11 */     this.state = state;
/*    */   }
/*    */   
/*    */   public EventType getState() {
/* 15 */     return this.state;
/*    */   }
/*    */   
/*    */   public void setState(EventType state) {
/* 19 */     this.state = state;
/*    */   }
/*    */   
/*    */   public boolean isPre() {
/* 23 */     return (this.state == EventType.PRE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\render\EventRenderScoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */