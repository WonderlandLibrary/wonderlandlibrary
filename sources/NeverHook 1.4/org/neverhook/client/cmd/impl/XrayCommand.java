/*    */ package org.neverhook.client.cmd.impl;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.block.Block;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.cmd.CommandAbstract;
/*    */ import org.neverhook.client.feature.impl.visual.XRay;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.ui.notification.NotificationManager;
/*    */ import org.neverhook.client.ui.notification.NotificationType;
/*    */ 
/*    */ public class XrayCommand
/*    */   extends CommandAbstract
/*    */ {
/* 16 */   public static ArrayList<Integer> blockIDS = new ArrayList<>();
/*    */   
/*    */   public XrayCommand() {
/* 19 */     super("xray", "xray", "§6.xray" + ChatFormatting.LIGHT_PURPLE + " add §3<blockId> | §6.xray" + ChatFormatting.LIGHT_PURPLE + " remove §3<blockId> | §6.xray" + ChatFormatting.LIGHT_PURPLE + " list | §6.xray" + ChatFormatting.LIGHT_PURPLE + " clear", new String[] { "xray" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String... arguments) {
/* 24 */     if (arguments.length >= 2) {
/* 25 */       if (!NeverHook.instance.featureManager.getFeatureByClass(XRay.class).getState()) {
/* 26 */         ChatHelper.addChatMessage(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "please enable XRay module!");
/* 27 */         NotificationManager.publicity("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "please enable XRay module!", 4, NotificationType.SUCCESS);
/*    */         return;
/*    */       } 
/* 30 */       if (arguments[0].equalsIgnoreCase("xray"))
/* 31 */         if (arguments[1].equalsIgnoreCase("add")) {
/* 32 */           if (!arguments[2].isEmpty()) {
/* 33 */             if (!blockIDS.contains(Integer.valueOf(Integer.parseInt(arguments[2])))) {
/* 34 */               blockIDS.add(Integer.valueOf(Integer.parseInt(arguments[2])));
/* 35 */               ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "added block: " + ChatFormatting.RED + "\"" + Block.getBlockById(Integer.parseInt(arguments[2])).getLocalizedName() + "\"");
/* 36 */               NotificationManager.publicity("XrayManager", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "added block: " + ChatFormatting.RED + "\"" + Block.getBlockById(Integer.parseInt(arguments[2])).getLocalizedName() + "\"", 4, NotificationType.SUCCESS);
/*    */             } else {
/* 38 */               ChatHelper.addChatMessage(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block already have in list!");
/* 39 */               NotificationManager.publicity("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block already have in list!", 4, NotificationType.SUCCESS);
/*    */             } 
/*    */           }
/* 42 */         } else if (arguments[1].equalsIgnoreCase("remove")) {
/* 43 */           if (blockIDS.contains(new Integer(arguments[2]))) {
/* 44 */             blockIDS.remove(new Integer(arguments[2]));
/* 45 */             ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "removed block by id " + Integer.parseInt(arguments[2]));
/* 46 */             NotificationManager.publicity("XrayManager", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "removed block by id " + Integer.parseInt(arguments[2]), 4, NotificationType.SUCCESS);
/*    */           } else {
/* 48 */             ChatHelper.addChatMessage(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block doesn't contains in your list!");
/* 49 */             NotificationManager.publicity("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block doesn't contains in your list!", 4, NotificationType.SUCCESS);
/*    */           } 
/* 51 */         } else if (arguments[1].equalsIgnoreCase("list")) {
/* 52 */           if (!blockIDS.isEmpty()) {
/* 53 */             for (Integer integer : blockIDS) {
/* 54 */               ChatHelper.addChatMessage(ChatFormatting.RED + Block.getBlockById(integer.intValue()).getLocalizedName() + ChatFormatting.LIGHT_PURPLE + " ID: " + integer);
/*    */             }
/*    */           } else {
/* 57 */             ChatHelper.addChatMessage(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!");
/* 58 */             NotificationManager.publicity("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!", 4, NotificationType.SUCCESS);
/*    */           } 
/* 60 */         } else if (arguments[1].equalsIgnoreCase("clear")) {
/* 61 */           if (blockIDS.isEmpty()) {
/* 62 */             ChatHelper.addChatMessage(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!");
/* 63 */             NotificationManager.publicity("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!", 4, NotificationType.SUCCESS);
/*    */           } else {
/* 65 */             blockIDS.clear();
/* 66 */             ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "clear block list!");
/* 67 */             NotificationManager.publicity("XrayManager", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "clear block list!", 4, NotificationType.SUCCESS);
/*    */           } 
/*    */         }  
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\impl\XrayCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */