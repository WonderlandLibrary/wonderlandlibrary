/*    */ package org.neverhook.client.event.events;
/*    */ 
/*    */ public abstract class EventStoppable
/*    */   implements Event {
/*    */   private boolean stopped;
/*    */   
/*    */   public void stop() {
/*  8 */     this.stopped = true;
/*    */   }
/*    */   
/*    */   public boolean isStopped() {
/* 12 */     return this.stopped;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\EventStoppable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */