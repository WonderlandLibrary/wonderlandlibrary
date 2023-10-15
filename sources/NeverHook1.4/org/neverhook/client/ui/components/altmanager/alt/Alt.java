/*    */ package org.neverhook.client.ui.components.altmanager.alt;
/*    */ 
/*    */ public class Alt
/*    */ {
/*    */   private final String username;
/*    */   private String mask;
/*    */   private String password;
/*    */   private Status status;
/*    */   
/*    */   public Alt(String username, String password) {
/* 11 */     this(username, password, Status.Unchecked);
/*    */   }
/*    */   
/*    */   public Alt(String username, String password, Status status) {
/* 15 */     this(username, password, "", status);
/*    */   }
/*    */   
/*    */   public Alt(String username, String password, String mask, Status status) {
/* 19 */     this.username = username;
/* 20 */     this.password = password;
/* 21 */     this.mask = mask;
/* 22 */     this.status = status;
/*    */   }
/*    */   
/*    */   public Status getStatus() {
/* 26 */     return this.status;
/*    */   }
/*    */   
/*    */   public void setStatus(Status status) {
/* 30 */     this.status = status;
/*    */   }
/*    */   
/*    */   public String getMask() {
/* 34 */     return this.mask;
/*    */   }
/*    */   
/*    */   public void setMask(String mask) {
/* 38 */     this.mask = mask;
/*    */   }
/*    */   
/*    */   public String getPassword() {
/* 42 */     return this.password;
/*    */   }
/*    */   
/*    */   public void setPassword(String password) {
/* 46 */     this.password = password;
/*    */   }
/*    */   
/*    */   public String getUsername() {
/* 50 */     return this.username;
/*    */   }
/*    */   
/*    */   public enum Status {
/* 54 */     Working("§aWorking"),
/* 55 */     Banned("§cBanned"),
/* 56 */     Unchecked("§eUnchecked"),
/* 57 */     NotWorking("§4Not Working");
/*    */     
/*    */     private final String formatted;
/*    */     
/*    */     Status(String string) {
/* 62 */       this.formatted = string;
/*    */     }
/*    */     
/*    */     public String toFormatted() {
/* 66 */       return this.formatted;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\altmanager\alt\Alt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */