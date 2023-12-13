/*    */ package org.neverhook.client.cmd.impl;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.cmd.CommandAbstract;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.settings.config.Config;
/*    */ import org.neverhook.client.settings.config.ConfigManager;
/*    */ import org.neverhook.client.ui.notification.NotificationManager;
/*    */ import org.neverhook.client.ui.notification.NotificationType;
/*    */ 
/*    */ public class ConfigCommand
/*    */   extends CommandAbstract {
/*    */   public ConfigCommand() {
/* 15 */     super("config", "configurations", "§6.config" + ChatFormatting.LIGHT_PURPLE + " save | load | delete §3<name>", new String[] { "config" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String... args) {
/*    */     try {
/* 21 */       if (args.length >= 2) {
/* 22 */         String upperCase = args[1].toUpperCase();
/* 23 */         if (args.length == 3) {
/* 24 */           switch (upperCase) {
/*    */             case "LOAD":
/* 26 */               if (NeverHook.instance.configManager.loadConfig(args[2])) {
/* 27 */                 ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
/* 28 */                 NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS); break;
/*    */               } 
/* 30 */               ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
/* 31 */               NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
/*    */               break;
/*    */             
/*    */             case "SAVE":
/* 35 */               if (NeverHook.instance.configManager.saveConfig(args[2])) {
/* 36 */                 ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
/* 37 */                 NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
/* 38 */                 ConfigManager.getLoadedConfigs().clear();
/* 39 */                 NeverHook.instance.configManager.load(); break;
/*    */               } 
/* 41 */               ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
/* 42 */               NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
/*    */               break;
/*    */             
/*    */             case "DELETE":
/* 46 */               if (NeverHook.instance.configManager.deleteConfig(args[2])) {
/* 47 */                 ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
/* 48 */                 NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS); break;
/*    */               } 
/* 50 */               ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
/* 51 */               NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
/*    */               break;
/*    */           } 
/*    */         
/* 55 */         } else if (args.length == 2 && upperCase.equalsIgnoreCase("LIST")) {
/* 56 */           ChatHelper.addChatMessage(ChatFormatting.GREEN + "Configs:");
/* 57 */           for (Config config : NeverHook.instance.configManager.getContents()) {
/* 58 */             ChatHelper.addChatMessage(ChatFormatting.RED + config.getName());
/*    */           }
/*    */         } 
/*    */       } else {
/* 62 */         ChatHelper.addChatMessage(getUsage());
/*    */       } 
/* 64 */     } catch (Exception e) {
/* 65 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\impl\ConfigCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */