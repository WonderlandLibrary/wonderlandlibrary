/*    */ package org.neverhook.client.files.impl;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import org.neverhook.client.files.FileManager;
/*    */ import org.neverhook.client.ui.components.altmanager.alt.Alt;
/*    */ import org.neverhook.client.ui.components.altmanager.alt.AltManager;
/*    */ 
/*    */ public class AltConfig extends FileManager.CustomFile {
/*    */   public AltConfig(String name, boolean loadOnStart) {
/* 11 */     super(name, loadOnStart);
/*    */   }
/*    */   
/*    */   public void loadFile() throws IOException {
/* 15 */     BufferedReader bufferedReader = new BufferedReader(new FileReader(getFile()));
/*    */     
/*    */     String line;
/* 18 */     while ((line = bufferedReader.readLine()) != null) {
/* 19 */       String[] arguments = line.split(":");
/*    */       
/* 21 */       if (arguments.length > 2) {
/* 22 */         AltManager.registry.add(new Alt(arguments[0], arguments[1], arguments[2], (arguments.length > 3) ? Alt.Status.valueOf(arguments[3]) : Alt.Status.Unchecked)); continue;
/*    */       } 
/* 24 */       AltManager.registry.add(new Alt(arguments[0], arguments[1]));
/*    */     } 
/*    */ 
/*    */     
/* 28 */     bufferedReader.close();
/*    */   }
/*    */   
/*    */   public void saveFile() throws IOException {
/* 32 */     PrintWriter alts = new PrintWriter(new FileWriter(getFile()));
/*    */     
/* 34 */     for (Alt alt : AltManager.registry) {
/* 35 */       if (alt.getMask().equals("")) {
/* 36 */         alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getUsername() + ":" + alt.getStatus()); continue;
/*    */       } 
/* 38 */       alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getMask() + ":" + alt.getStatus());
/*    */     } 
/*    */     
/* 41 */     alts.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\files\impl\AltConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */