/*    */ package org.neverhook.client.event.events.impl.packet;
/*    */ 
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventReceiveMessage
/*    */   extends EventCancellable {
/*    */   public String message;
/*    */   public boolean cancelled;
/*    */   
/*    */   public EventReceiveMessage(String chat) {
/* 11 */     this.message = chat;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 15 */     return this.message;
/*    */   }
/*    */   
/*    */   public void setCancelled(boolean b) {
/* 19 */     this.cancelled = b;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\packet\EventReceiveMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */