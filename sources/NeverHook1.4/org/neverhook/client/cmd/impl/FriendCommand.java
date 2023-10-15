/*    */ package org.neverhook.client.cmd.impl;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.cmd.CommandAbstract;
/*    */ import org.neverhook.client.friend.Friend;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.ui.notification.NotificationManager;
/*    */ import org.neverhook.client.ui.notification.NotificationType;
/*    */ 
/*    */ public class FriendCommand extends CommandAbstract {
/*    */   public FriendCommand() {
/* 14 */     super("friend", "friend list", "§6.friend" + ChatFormatting.LIGHT_PURPLE + " add §3<nickname> | §6.friend" + ChatFormatting.LIGHT_PURPLE + " del §3<nickname> | §6.friend" + ChatFormatting.LIGHT_PURPLE + " list | §6.friend" + ChatFormatting.LIGHT_PURPLE + " clear", new String[] { "friend" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String... arguments) {
/*    */     try {
/* 20 */       if (arguments.length > 1) {
/* 21 */         if (arguments[0].equalsIgnoreCase("friend")) {
/* 22 */           if (arguments[1].equalsIgnoreCase("add")) {
/* 23 */             String name = arguments[2];
/* 24 */             if (name.equals((Minecraft.getInstance()).player.getName())) {
/* 25 */               ChatHelper.addChatMessage(ChatFormatting.RED + "You can't add yourself!");
/* 26 */               NotificationManager.publicity("Friend Manager", "You can't add yourself!", 4, NotificationType.ERROR);
/*    */               return;
/*    */             } 
/* 29 */             if (!NeverHook.instance.friendManager.isFriend(name)) {
/* 30 */               NeverHook.instance.friendManager.addFriend(name);
/* 31 */               ChatHelper.addChatMessage("Friend " + ChatFormatting.GREEN + name + ChatFormatting.WHITE + " successfully added to your friend list!");
/* 32 */               NotificationManager.publicity("Friend Manager", "Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!", 4, NotificationType.SUCCESS);
/*    */             } 
/*    */           } 
/* 35 */           if (arguments[1].equalsIgnoreCase("del")) {
/* 36 */             String name = arguments[2];
/* 37 */             if (NeverHook.instance.friendManager.isFriend(name)) {
/* 38 */               NeverHook.instance.friendManager.removeFriend(name);
/* 39 */               ChatHelper.addChatMessage("Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!");
/* 40 */               NotificationManager.publicity("Friend Manager", "Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!", 4, NotificationType.SUCCESS);
/*    */             } 
/*    */           } 
/* 43 */           if (arguments[1].equalsIgnoreCase("clear")) {
/* 44 */             if (NeverHook.instance.friendManager.getFriends().isEmpty()) {
/* 45 */               ChatHelper.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
/* 46 */               NotificationManager.publicity("Friend Manager", "Your friend list is empty!", 4, NotificationType.ERROR);
/*    */               return;
/*    */             } 
/* 49 */             NeverHook.instance.friendManager.getFriends().clear();
/* 50 */             ChatHelper.addChatMessage("Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!");
/* 51 */             NotificationManager.publicity("Friend Manager", "Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!", 4, NotificationType.SUCCESS);
/*    */           } 
/* 53 */           if (arguments[1].equalsIgnoreCase("list")) {
/* 54 */             if (NeverHook.instance.friendManager.getFriends().isEmpty()) {
/* 55 */               ChatHelper.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
/* 56 */               NotificationManager.publicity("Friend Manager", "Your friend list is empty!", 4, NotificationType.ERROR);
/*    */               return;
/*    */             } 
/* 59 */             NeverHook.instance.friendManager.getFriends().forEach(friend -> ChatHelper.addChatMessage(ChatFormatting.GREEN + "Friend list: " + ChatFormatting.RED + friend.getName()));
/*    */           } 
/*    */         } 
/*    */       } else {
/* 63 */         ChatHelper.addChatMessage(getUsage());
/*    */       } 
/* 65 */     } catch (Exception e) {
/* 66 */       ChatHelper.addChatMessage("§cNo, no, no. Usage: " + getUsage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\impl\FriendCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */