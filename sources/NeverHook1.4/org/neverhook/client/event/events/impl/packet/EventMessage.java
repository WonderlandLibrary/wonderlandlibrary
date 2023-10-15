/*    */ package org.neverhook.client.event.events.impl.packet;
/*    */ 
/*    */ import org.neverhook.client.event.events.Event;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventMessage
/*    */   extends EventCancellable implements Event {
/*    */   public String message;
/*    */   
/*    */   public EventMessage(String message) {
/* 11 */     this.message = message;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 15 */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\packet\EventMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */