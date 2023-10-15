/*    */ package org.neverhook.client.files;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.files.impl.AltConfig;
/*    */ import org.neverhook.client.files.impl.CapeConfig;
/*    */ import org.neverhook.client.files.impl.FriendConfig;
/*    */ import org.neverhook.client.files.impl.HudConfig;
/*    */ import org.neverhook.client.files.impl.MacroConfig;
/*    */ import org.neverhook.client.files.impl.XrayConfig;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileManager
/*    */ {
/* 18 */   public static File directory = new File(NeverHook.instance.name);
/* 19 */   public static ArrayList<CustomFile> files = new ArrayList<>();
/*    */   
/*    */   public FileManager() {
/* 22 */     files.add(new AltConfig("AltConfig", true));
/* 23 */     files.add(new FriendConfig("FriendConfig", true));
/* 24 */     files.add(new MacroConfig("MacroConfig", true));
/* 25 */     files.add(new HudConfig("HudConfig", true));
/* 26 */     files.add(new CapeConfig("CapeConfig", true));
/* 27 */     files.add(new XrayConfig("XrayConfig", true));
/*    */   }
/*    */   
/*    */   public void loadFiles() {
/* 31 */     for (CustomFile file : files) {
/*    */       try {
/* 33 */         if (file.loadOnStart()) {
/* 34 */           file.loadFile();
/*    */         }
/* 36 */       } catch (Exception e) {
/* 37 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void saveFiles() {
/* 43 */     for (CustomFile f : files) {
/*    */       try {
/* 45 */         f.saveFile();
/* 46 */       } catch (Exception exception) {}
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public CustomFile getFile(Class<?> clazz) {
/*    */     CustomFile file;
/* 53 */     Iterator<CustomFile> customFileIterator = files.iterator();
/*    */ 
/*    */     
/*    */     do {
/* 57 */       if (!customFileIterator.hasNext()) {
/* 58 */         return null;
/*    */       }
/*    */       
/* 61 */       file = customFileIterator.next();
/* 62 */     } while (file.getClass() != clazz);
/*    */     
/* 64 */     return file;
/*    */   }
/*    */   
/*    */   public static abstract class CustomFile
/*    */   {
/*    */     private final File file;
/*    */     private final String name;
/*    */     private final boolean load;
/*    */     
/*    */     public CustomFile(String name, boolean loadOnStart) {
/* 74 */       this.name = name;
/* 75 */       this.load = loadOnStart;
/* 76 */       this.file = new File(FileManager.directory, name + ".json");
/* 77 */       if (!this.file.exists()) {
/*    */         try {
/* 79 */           saveFile();
/* 80 */         } catch (Exception exception) {}
/*    */       }
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public final File getFile() {
/* 87 */       return this.file;
/*    */     }
/*    */     
/*    */     private boolean loadOnStart() {
/* 91 */       return this.load;
/*    */     }
/*    */     
/*    */     public final String getName() {
/* 95 */       return this.name;
/*    */     }
/*    */     
/*    */     public abstract void loadFile() throws Exception;
/*    */     
/*    */     public abstract void saveFile() throws Exception;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\files\FileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */