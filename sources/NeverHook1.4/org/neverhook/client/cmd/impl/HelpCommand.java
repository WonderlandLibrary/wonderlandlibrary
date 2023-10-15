/*    */ package org.neverhook.client.cmd.impl;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import org.neverhook.client.cmd.CommandAbstract;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ 
/*    */ public class HelpCommand
/*    */   extends CommandAbstract {
/*    */   public HelpCommand() {
/* 10 */     super("help", "help", ".help", new String[] { "help" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String... args) {
/* 15 */     if (args.length == 1) {
/* 16 */       if (args[0].equals("help")) {
/* 17 */         ChatHelper.addChatMessage(ChatFormatting.RED + "All Commands:");
/* 18 */         ChatHelper.addChatMessage(ChatFormatting.DARK_AQUA + ".bind");
/* 19 */         ChatHelper.addChatMessage(ChatFormatting.DARK_AQUA + ".macro");
/* 20 */         ChatHelper.addChatMessage(ChatFormatting.DARK_AQUA + ".clip");
/* 21 */         ChatHelper.addChatMessage(ChatFormatting.DARK_AQUA + ".fakehack");
/* 22 */         ChatHelper.addChatMessage(ChatFormatting.DARK_AQUA + ".friend");
/* 23 */         ChatHelper.addChatMessage(ChatFormatting.DARK_AQUA + ".config");
/* 24 */         ChatHelper.addChatMessage(ChatFormatting.DARK_AQUA + ".clip");
/* 25 */         ChatHelper.addChatMessage(ChatFormatting.DARK_AQUA + ".xray");
/*    */       } 
/*    */     } else {
/* 28 */       ChatHelper.addChatMessage(getUsage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\impl\HelpCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */