/*    */ package org.neverhook.client.cmd.impl;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import org.neverhook.client.cmd.CommandAbstract;
/*    */ import org.neverhook.client.feature.impl.misc.FakeHack;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.ui.notification.NotificationManager;
/*    */ import org.neverhook.client.ui.notification.NotificationType;
/*    */ 
/*    */ public class FakeHackCommand
/*    */   extends CommandAbstract {
/*    */   public FakeHackCommand() {
/* 15 */     super("fakehack", "fakehack", "§6.fakehack" + ChatFormatting.LIGHT_PURPLE + " add | remove | clear§3<name>", new String[] { "§6.fakehack" + ChatFormatting.LIGHT_PURPLE + " add | del | clear§3<name>", "fakehack" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String... arguments) {
/*    */     try {
/* 21 */       if (arguments.length > 1) {
/* 22 */         if (arguments[0].equals("fakehack")) {
/* 23 */           if (arguments[1].equals("add")) {
/* 24 */             FakeHack.fakeHackers.add(arguments[2]);
/* 25 */             ChatHelper.addChatMessage(ChatFormatting.GREEN + "Added player " + ChatFormatting.RED + arguments[2] + ChatFormatting.WHITE + " as HACKER!");
/* 26 */             NotificationManager.publicity("FakeHack Manager", ChatFormatting.GREEN + "Added player " + ChatFormatting.RED + arguments[2] + ChatFormatting.WHITE + " as HACKER!", 4, NotificationType.SUCCESS);
/*    */           } 
/* 28 */           if (arguments[1].equals("remove")) {
/* 29 */             EntityPlayer player = (Minecraft.getInstance()).world.getPlayerEntityByName(arguments[2]);
/* 30 */             if (player == null) {
/* 31 */               ChatHelper.addChatMessage("§cThat player could not be found!");
/*    */               return;
/*    */             } 
/* 34 */             if (FakeHack.isFakeHacker(player)) {
/* 35 */               FakeHack.removeHacker(player);
/* 36 */               ChatHelper.addChatMessage(ChatFormatting.GREEN + "Hacker " + ChatFormatting.RED + player.getName() + " " + ChatFormatting.WHITE + "was removed!");
/* 37 */               NotificationManager.publicity("FakeHack Manager", ChatFormatting.GREEN + "Hacker " + ChatFormatting.WHITE + "was removed!", 4, NotificationType.SUCCESS);
/*    */             } 
/*    */           } 
/* 40 */           if (arguments[1].equals("clear")) {
/* 41 */             if (FakeHack.fakeHackers.isEmpty()) {
/* 42 */               ChatHelper.addChatMessage(ChatFormatting.RED + "Your FakeHack list is empty!");
/* 43 */               NotificationManager.publicity("FakeHack Manager", "Your FakeHack list is empty!", 4, NotificationType.ERROR);
/*    */               return;
/*    */             } 
/* 46 */             FakeHack.fakeHackers.clear();
/* 47 */             ChatHelper.addChatMessage(ChatFormatting.GREEN + "Your FakeHack list " + ChatFormatting.WHITE + " successfully cleared!");
/* 48 */             NotificationManager.publicity("FakeHack Manager", ChatFormatting.GREEN + "Your FakeHack list " + ChatFormatting.WHITE + " successfully cleared!", 4, NotificationType.SUCCESS);
/*    */           } 
/*    */         } 
/*    */       } else {
/* 52 */         ChatHelper.addChatMessage(getUsage());
/*    */       } 
/* 54 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\impl\FakeHackCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */