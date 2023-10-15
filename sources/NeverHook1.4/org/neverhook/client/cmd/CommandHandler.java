/*    */ package org.neverhook.client.cmd;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventMessage;
/*    */ 
/*    */ public class CommandHandler
/*    */ {
/*    */   public CommandManager commandManager;
/*    */   
/*    */   public CommandHandler(CommandManager commandManager) {
/* 11 */     this.commandManager = commandManager;
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onMessage(EventMessage event) {
/* 16 */     String msg = event.getMessage();
/* 17 */     if (msg.length() > 0 && msg.startsWith("."))
/* 18 */       event.setCancelled(this.commandManager.execute(msg)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\CommandHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */