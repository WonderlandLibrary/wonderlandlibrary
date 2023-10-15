/*    */ package org.neverhook.client.event.events.impl.packet;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventAttackClient
/*    */   extends EventCancellable {
/*    */   private final Entity entity;
/*    */   
/*    */   public EventAttackClient(Entity targetEntity) {
/* 11 */     this.entity = targetEntity;
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 15 */     return this.entity;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\packet\EventAttackClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */