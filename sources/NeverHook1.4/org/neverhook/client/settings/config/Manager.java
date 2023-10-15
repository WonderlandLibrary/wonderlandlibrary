/*    */ package org.neverhook.client.settings.config;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public abstract class Manager<T>
/*    */ {
/*  8 */   private List<T> contents = new ArrayList<>();
/*    */   
/*    */   public List<T> getContents() {
/* 11 */     return this.contents;
/*    */   }
/*    */   
/*    */   public void setContents(ArrayList<T> contents) {
/* 15 */     this.contents = contents;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\settings\config\Manager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */