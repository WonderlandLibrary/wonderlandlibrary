/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventMessage;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class ChatAppend
/*    */   extends Feature {
/*    */   public ChatAppend() {
/* 12 */     super("ChatAppend", "Дописывает название чита в сообщение", Type.Misc);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onChatMessage(EventMessage event) {
/* 17 */     if (event.getMessage().startsWith("/")) {
/*    */       return;
/*    */     }
/* 20 */     event.message += " | " + NeverHook.instance.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\ChatAppend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */