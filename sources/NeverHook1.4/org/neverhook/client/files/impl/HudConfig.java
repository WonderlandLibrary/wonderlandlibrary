/*    */ package org.neverhook.client.files.impl;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.FileInputStream;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.files.FileManager;
/*    */ import org.neverhook.client.ui.components.draggable.DraggableModule;
/*    */ 
/*    */ public class HudConfig extends FileManager.CustomFile {
/*    */   public HudConfig(String name, boolean loadOnStart) {
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
/* 23 */         String x = curLine.split(":")[1];
/* 24 */         String y = curLine.split(":")[2];
/* 25 */         for (DraggableModule hudModule : NeverHook.instance.draggableManager.getMods()) {
/* 26 */           if (hudModule.getName().equals(curLine.split(":")[0])) {
/* 27 */             hudModule.drag.setXPosition(Integer.parseInt(x));
/* 28 */             hudModule.drag.setYPosition(Integer.parseInt(y));
/*    */           } 
/*    */         } 
/*    */       } 
/* 32 */       br.close();
/* 33 */     } catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void saveFile() {
/*    */     try {
/* 40 */       BufferedWriter out = new BufferedWriter(new FileWriter(getFile()));
/* 41 */       for (DraggableModule draggableModule : NeverHook.instance.draggableManager.getMods()) {
/* 42 */         out.write(draggableModule.getName() + ":" + draggableModule.drag.getXPosition() + ":" + draggableModule.drag.getYPosition());
/* 43 */         out.write("\r\n");
/*    */       } 
/* 45 */       out.close();
/* 46 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\files\impl\HudConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */