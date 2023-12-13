/*    */ package org.neverhook.client.cmd.impl;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.cmd.CommandAbstract;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.ui.notification.NotificationManager;
/*    */ import org.neverhook.client.ui.notification.NotificationType;
/*    */ 
/*    */ public class BindCommand
/*    */   extends CommandAbstract {
/*    */   public BindCommand() {
/* 15 */     super("bind", "bind", "§6.bind" + ChatFormatting.LIGHT_PURPLE + " add §3<name> §3<key> | §6.bind " + ChatFormatting.LIGHT_PURPLE + "remove §3<name> §3<key>", new String[] { "§6.bind" + ChatFormatting.LIGHT_PURPLE + " add §3<name> §3<key> | §6.bind" + ChatFormatting.LIGHT_PURPLE + "remove §3<name> <key> | §6.bind" + ChatFormatting.LIGHT_PURPLE + "clear", "bind" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String... arguments) {
/*    */     try {
/* 21 */       if (arguments.length == 4) {
/* 22 */         String moduleName = arguments[2];
/* 23 */         String bind = arguments[3].toUpperCase();
/* 24 */         Feature feature = NeverHook.instance.featureManager.getFeatureByLabel(moduleName);
/* 25 */         if (arguments[0].equals("bind"))
/* 26 */           switch (arguments[1]) {
/*    */             case "add":
/* 28 */               feature.setBind(Keyboard.getKeyIndex(bind));
/* 29 */               ChatHelper.addChatMessage(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"");
/* 30 */               NotificationManager.publicity("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"", 4, NotificationType.SUCCESS);
/*    */               break;
/*    */             case "remove":
/* 33 */               feature.setBind(0);
/* 34 */               ChatHelper.addChatMessage(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"");
/* 35 */               NotificationManager.publicity("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"", 4, NotificationType.SUCCESS);
/*    */               break;
/*    */             case "clear":
/* 38 */               if (!feature.getLabel().equals("ClickGui")) {
/* 39 */                 feature.setBind(0);
/*    */               }
/*    */               break;
/*    */             case "list":
/* 43 */               if (feature.getBind() == 0) {
/* 44 */                 ChatHelper.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
/* 45 */                 NotificationManager.publicity("Macro Manager", "Your macros list is empty!", 4, NotificationType.ERROR);
/*    */                 return;
/*    */               } 
/* 48 */               NeverHook.instance.featureManager.getFeatureList().forEach(feature1 -> ChatHelper.addChatMessage(ChatFormatting.GREEN + "Binds list: " + ChatFormatting.WHITE + "Binds Name: " + ChatFormatting.RED + feature1.getBind() + ChatFormatting.WHITE + ", Macro Bind: " + ChatFormatting.RED + Keyboard.getKeyName(feature1.getBind())));
/*    */               break;
/*    */           }  
/*    */       } else {
/* 52 */         ChatHelper.addChatMessage(getUsage());
/*    */       } 
/* 54 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\impl\BindCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */