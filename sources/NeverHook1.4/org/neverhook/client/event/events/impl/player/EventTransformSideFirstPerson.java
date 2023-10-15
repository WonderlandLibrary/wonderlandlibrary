/*    */ package org.neverhook.client.event.events.impl.player;
/*    */ 
/*    */ import net.minecraft.util.EnumHandSide;
/*    */ import org.neverhook.client.event.events.Event;
/*    */ 
/*    */ public class EventTransformSideFirstPerson
/*    */   implements Event {
/*    */   private final EnumHandSide enumHandSide;
/*    */   
/*    */   public EventTransformSideFirstPerson(EnumHandSide enumHandSide) {
/* 11 */     this.enumHandSide = enumHandSide;
/*    */   }
/*    */   
/*    */   public EnumHandSide getEnumHandSide() {
/* 15 */     return this.enumHandSide;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\player\EventTransformSideFirstPerson.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */