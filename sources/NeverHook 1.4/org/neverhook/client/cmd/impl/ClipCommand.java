/*    */ package org.neverhook.client.cmd.impl;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import org.neverhook.client.cmd.CommandAbstract;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ 
/*    */ public class ClipCommand
/*    */   extends CommandAbstract {
/*  9 */   Minecraft mc = Minecraft.getInstance();
/*    */   
/*    */   public ClipCommand() {
/* 12 */     super("clip", "clip | hclip", "§6.clip | (hclip) + | - §3<value> | down", new String[] { "clip", "hclip" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String... args) {
/* 17 */     if (args.length > 1) {
/* 18 */       if (args[0].equalsIgnoreCase("clip")) {
/*    */         try {
/* 20 */           if (args[1].equals("down")) {
/* 21 */             this.mc.player.setPositionAndUpdate(this.mc.player.posX, -2.0D, this.mc.player.posZ);
/*    */           }
/* 23 */           if (args[1].equals("+")) {
/* 24 */             this.mc.player.setPositionAndUpdate(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args[2]), this.mc.player.posZ);
/*    */           }
/* 26 */           if (args[1].equals("-")) {
/* 27 */             this.mc.player.setPositionAndUpdate(this.mc.player.posX, this.mc.player.posY - Double.parseDouble(args[2]), this.mc.player.posZ);
/*    */           }
/* 29 */         } catch (Exception exception) {}
/*    */       }
/*    */       
/* 32 */       if (args[0].equalsIgnoreCase("hclip")) {
/* 33 */         double x = this.mc.player.posX;
/* 34 */         double y = this.mc.player.posY;
/* 35 */         double z = this.mc.player.posZ;
/* 36 */         double yaw = this.mc.player.rotationYaw * 0.017453292D;
/*    */         try {
/* 38 */           if (args[1].equals("+")) {
/* 39 */             this.mc.player.setPositionAndUpdate(x - Math.sin(yaw) * Double.parseDouble(args[2]), y, z + Math.cos(yaw) * Double.parseDouble(args[2]));
/*    */           }
/* 41 */           if (args[1].equals("-")) {
/* 42 */             this.mc.player.setPositionAndUpdate(x + Math.sin(yaw) * Double.parseDouble(args[2]), y, z - Math.cos(yaw) * Double.parseDouble(args[2]));
/*    */           }
/* 44 */         } catch (Exception exception) {}
/*    */       } 
/*    */     } else {
/*    */       
/* 48 */       ChatHelper.addChatMessage(getUsage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\impl\ClipCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */