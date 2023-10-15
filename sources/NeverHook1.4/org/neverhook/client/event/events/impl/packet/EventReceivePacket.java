/*    */ package org.neverhook.client.event.events.impl.packet;
/*    */ 
/*    */ import net.minecraft.network.Packet;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventReceivePacket
/*    */   extends EventCancellable {
/*    */   private Packet<?> packet;
/*    */   
/*    */   public EventReceivePacket(Packet<?> packet) {
/* 11 */     this.packet = packet;
/*    */   }
/*    */   
/*    */   public Packet<?> getPacket() {
/* 15 */     return this.packet;
/*    */   }
/*    */   
/*    */   public void setPacket(Packet<?> packet) {
/* 19 */     this.packet = packet;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\packet\EventReceivePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */