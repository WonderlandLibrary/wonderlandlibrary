/*    */ package org.neverhook.client.files.impl;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import org.neverhook.client.cmd.impl.XrayCommand;
/*    */ import org.neverhook.client.files.FileManager;
/*    */ 
/*    */ public class XrayConfig extends FileManager.CustomFile {
/*    */   public XrayConfig(String name, boolean loadOnStart) {
/* 12 */     super(name, loadOnStart);
/*    */   }
/*    */   
/*    */   public void loadFile() {
/*    */     try {
/* 17 */       FileInputStream fileInputStream = new FileInputStream(getFile().getAbsolutePath());
/* 18 */       DataInputStream in = new DataInputStream(fileInputStream);
/* 19 */       BufferedReader br = new BufferedReader(new InputStreamReader(in));
/*    */       String line;
/* 21 */       while ((line = br.readLine()) != null) {
/* 22 */         String curLine = line.trim();
/* 23 */         String id = curLine.split(":")[1];
/* 24 */         XrayCommand.blockIDS.add(new Integer(id));
/*    */       } 
/* 26 */       br.close();
/* 27 */     } catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void saveFile() {
/*    */     try {
/* 34 */       BufferedWriter out = new BufferedWriter(new FileWriter(getFile()));
/* 35 */       for (Integer integer : XrayCommand.blockIDS) {
/* 36 */         if (integer != null) {
/* 37 */           out.write("blockID:" + integer + ":" + Block.getBlockById(integer.intValue()).getLocalizedName());
/* 38 */           out.write("\r\n");
/*    */         } 
/*    */       } 
/* 41 */       out.close();
/* 42 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\files\impl\XrayConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */