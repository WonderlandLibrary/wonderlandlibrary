/*    */ package org.neverhook.client.event.events.callables;
/*    */ 
/*    */ import org.neverhook.client.event.events.Cancellable;
/*    */ import org.neverhook.client.event.events.Event;
/*    */ 
/*    */ public abstract class EventCancellable
/*    */   implements Event, Cancellable
/*    */ {
/*    */   private boolean cancelled;
/*    */   
/*    */   public boolean isCancelled() {
/* 12 */     return this.cancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean state) {
/* 17 */     this.cancelled = state;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\callables\EventCancellable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */