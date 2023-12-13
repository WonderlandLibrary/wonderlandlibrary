/*    */ package org.neverhook.client.files.impl;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.FileReader;
/*    */ import java.io.FileWriter;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.files.FileManager;
/*    */ import org.neverhook.client.friend.Friend;
/*    */ 
/*    */ public class FriendConfig
/*    */   extends FileManager.CustomFile
/*    */ {
/*    */   public FriendConfig(String name, boolean loadOnStart) {
/* 15 */     super(name, loadOnStart);
/*    */   }
/*    */   
/*    */   public void loadFile() {
/*    */     try {
/* 20 */       BufferedReader br = new BufferedReader(new FileReader(getFile()));
/*    */       String line;
/* 22 */       while ((line = br.readLine()) != null) {
/* 23 */         String curLine = line.trim();
/* 24 */         String name = curLine.split(":")[0];
/* 25 */         NeverHook.instance.friendManager.addFriend(name);
/*    */       } 
/* 27 */       br.close();
/* 28 */     } catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void saveFile() {
/*    */     try {
/* 35 */       BufferedWriter out = new BufferedWriter(new FileWriter(getFile()));
/* 36 */       for (Friend friend : NeverHook.instance.friendManager.getFriends()) {
/* 37 */         out.write(friend.getName().replace(" ", ""));
/* 38 */         out.write("\r\n");
/*    */       } 
/* 40 */       out.close();
/* 41 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\files\impl\FriendConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */