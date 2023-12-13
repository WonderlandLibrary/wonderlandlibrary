/*    */ package org.neverhook.client.files.impl;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.FileInputStream;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.files.FileManager;
/*    */ import org.neverhook.client.macro.Macro;
/*    */ 
/*    */ public class MacroConfig extends FileManager.CustomFile {
/*    */   public MacroConfig(String name, boolean loadOnStart) {
/* 13 */     super(name, loadOnStart);
/*    */   }
/*    */   
/*    */   public void loadFile() {
/*    */     try {
/* 18 */       FileInputStream fileInputStream = new FileInputStream(getFile().getAbsolutePath());
/* 19 */       DataInputStream in = new DataInputStream(fileInputStream);
/* 20 */       BufferedReader br = new BufferedReader(new InputStreamReader(in));
/*    */       String line;
/* 22 */       while ((line = br.readLine()) != null) {
/* 23 */         String curLine = line.trim();
/* 24 */         String bind = curLine.split(":")[0];
/* 25 */         String value = curLine.split(":")[1];
/* 26 */         if (NeverHook.instance.macroManager != null) {
/* 27 */           NeverHook.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(bind), value));
/*    */         }
/*    */       } 
/* 30 */       br.close();
/* 31 */     } catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void saveFile() {
/*    */     try {
/* 38 */       BufferedWriter out = new BufferedWriter(new FileWriter(getFile()));
/* 39 */       for (Macro m : NeverHook.instance.macroManager.getMacros()) {
/* 40 */         if (m != null) {
/* 41 */           out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue());
/* 42 */           out.write("\r\n");
/*    */         } 
/*    */       } 
/* 45 */       out.close();
/* 46 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\files\impl\MacroConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */