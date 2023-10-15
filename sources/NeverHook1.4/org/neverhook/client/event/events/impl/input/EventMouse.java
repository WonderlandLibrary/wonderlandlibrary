/*    */ package org.neverhook.client.event.events.impl.input;
/*    */ 
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventMouse
/*    */   extends EventCancellable {
/*    */   private int key;
/*    */   
/*    */   public EventMouse(int key) {
/* 10 */     this.key = key;
/*    */   }
/*    */   
/*    */   public int getKey() {
/* 14 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(int key) {
/* 18 */     this.key = key;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\input\EventMouse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */