/*    */ package org.neverhook.client.event.events.impl.packet;
/*    */ 
/*    */ import net.minecraft.network.Packet;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventSendPacket
/*    */   extends EventCancellable {
/*    */   private final Packet<?> packet;
/*    */   
/*    */   public EventSendPacket(Packet<?> packet) {
/* 11 */     this.packet = packet;
/*    */   }
/*    */   
/*    */   public Packet getPacket() {
/* 15 */     return this.packet;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\packet\EventSendPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */