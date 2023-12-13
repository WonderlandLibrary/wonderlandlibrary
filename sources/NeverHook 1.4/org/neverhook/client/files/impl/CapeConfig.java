/*    */ package org.neverhook.client.files.impl;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.FileInputStream;
/*    */ import org.neverhook.client.files.FileManager;
/*    */ import org.neverhook.client.ui.GuiCapeSelector;
/*    */ 
/*    */ public class CapeConfig extends FileManager.CustomFile {
/*    */   public CapeConfig(String name, boolean loadOnStart) {
/* 11 */     super(name, loadOnStart);
/*    */   }
/*    */   
/*    */   public void loadFile() {
/*    */     try {
/* 16 */       FileInputStream fileInputStream = new FileInputStream(getFile().getAbsolutePath());
/* 17 */       DataInputStream in = new DataInputStream(fileInputStream);
/* 18 */       BufferedReader br = new BufferedReader(new InputStreamReader(in));
/*    */       String line;
/* 20 */       while ((line = br.readLine()) != null) {
/* 21 */         String curLine = line.trim();
/* 22 */         if (GuiCapeSelector.Selector.getCapeName() != null) {
/* 23 */           GuiCapeSelector.Selector.setCapeName(curLine);
/*    */         }
/*    */       } 
/* 26 */       br.close();
/* 27 */     } catch (Exception e) {
/* 28 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void saveFile() {
/*    */     try {
/* 34 */       BufferedWriter out = new BufferedWriter(new FileWriter(getFile()));
/* 35 */       out.write(GuiCapeSelector.Selector.getCapeName());
/* 36 */       out.write("\r\n");
/* 37 */       out.close();
/* 38 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\files\impl\CapeConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */