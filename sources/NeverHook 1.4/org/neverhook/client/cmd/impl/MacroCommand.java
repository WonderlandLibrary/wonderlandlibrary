/*    */ package org.neverhook.client.cmd.impl;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.cmd.CommandAbstract;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.macro.Macro;
/*    */ import org.neverhook.client.ui.notification.NotificationManager;
/*    */ import org.neverhook.client.ui.notification.NotificationType;
/*    */ 
/*    */ public class MacroCommand
/*    */   extends CommandAbstract {
/*    */   public MacroCommand() {
/* 15 */     super("macros", "macro", "§6.macro" + ChatFormatting.LIGHT_PURPLE + " add §3<key> /home_home | §6.macro" + ChatFormatting.LIGHT_PURPLE + " remove §3<key> | §6.macro" + ChatFormatting.LIGHT_PURPLE + " clear §3| §6.macro" + ChatFormatting.LIGHT_PURPLE + " list", new String[] { "§6.macro" + ChatFormatting.LIGHT_PURPLE + " add §3<key> </home_home> | §6.macro" + ChatFormatting.LIGHT_PURPLE + " remove §3<key> | §6.macro" + ChatFormatting.LIGHT_PURPLE + " clear | §6.macro" + ChatFormatting.LIGHT_PURPLE + " list", "macro" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String... arguments) {
/*    */     try {
/* 21 */       if (arguments.length > 1) {
/* 22 */         if (arguments[0].equals("macro")) {
/* 23 */           if (arguments[1].equals("add")) {
/* 24 */             StringBuilder command = new StringBuilder();
/* 25 */             for (int i = 3; i < arguments.length; i++) {
/* 26 */               command.append(arguments[i]).append(" ");
/*    */             }
/* 28 */             NeverHook.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(arguments[2].toUpperCase()), command.toString()));
/* 29 */             ChatHelper.addChatMessage(ChatFormatting.GREEN + "Added macros for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + command);
/* 30 */             NotificationManager.publicity("Macro Manager", ChatFormatting.GREEN + "Added macro for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + command, 4, NotificationType.SUCCESS);
/*    */           } 
/* 32 */           if (arguments[1].equals("clear")) {
/* 33 */             if (NeverHook.instance.macroManager.getMacros().isEmpty()) {
/* 34 */               ChatHelper.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
/* 35 */               NotificationManager.publicity("Macro Manager", "Your macro list is empty!", 4, NotificationType.ERROR);
/*    */               return;
/*    */             } 
/* 38 */             NeverHook.instance.macroManager.getMacros().clear();
/* 39 */             ChatHelper.addChatMessage(ChatFormatting.GREEN + "Your macros list " + ChatFormatting.WHITE + " successfully cleared!");
/* 40 */             NotificationManager.publicity("Macro Manager", ChatFormatting.GREEN + "Your macros list " + ChatFormatting.WHITE + " successfully cleared!", 4, NotificationType.SUCCESS);
/*    */           } 
/* 42 */           if (arguments[1].equals("remove")) {
/* 43 */             NeverHook.instance.macroManager.deleteMacroByKey(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
/* 44 */             ChatHelper.addChatMessage(ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"");
/* 45 */             NotificationManager.publicity("Macro Manager", ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"", 4, NotificationType.SUCCESS);
/*    */           } 
/* 47 */           if (arguments[1].equals("list")) {
/* 48 */             if (NeverHook.instance.macroManager.getMacros().isEmpty()) {
/* 49 */               ChatHelper.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
/* 50 */               NotificationManager.publicity("Macro Manager", "Your macros list is empty!", 4, NotificationType.ERROR);
/*    */               return;
/*    */             } 
/* 53 */             NeverHook.instance.macroManager.getMacros().forEach(macro -> ChatHelper.addChatMessage(ChatFormatting.GREEN + "Macros list: " + ChatFormatting.WHITE + "Macros Name: " + ChatFormatting.RED + macro.getValue() + ChatFormatting.WHITE + ", Macro Bind: " + ChatFormatting.RED + Keyboard.getKeyName(macro.getKey())));
/*    */           } 
/*    */         } 
/*    */       } else {
/* 57 */         ChatHelper.addChatMessage(getUsage());
/*    */       } 
/* 59 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\impl\MacroCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */