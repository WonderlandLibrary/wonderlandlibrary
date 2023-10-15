/*    */ package org.neverhook.client.event.events.impl.packet;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventAttackSilent
/*    */   extends EventCancellable {
/*    */   private final Entity targetEntity;
/*    */   
/*    */   public EventAttackSilent(Entity targetEntity) {
/* 11 */     this.targetEntity = targetEntity;
/*    */   }
/*    */   
/*    */   public Entity getTargetEntity() {
/* 15 */     return this.targetEntity;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\packet\EventAttackSilent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */