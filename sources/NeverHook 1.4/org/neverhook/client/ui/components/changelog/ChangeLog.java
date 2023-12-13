/*    */ package org.neverhook.client.ui.components.changelog;
/*    */ 
/*    */ public class ChangeLog
/*    */ {
/*    */   protected String changeName;
/*    */   protected ChangeType type;
/*    */   
/*    */   public ChangeLog(String name, ChangeType type) {
/*  9 */     this.changeName = name;
/* 10 */     this.type = type;
/*    */     
/* 12 */     switch (type) {
/*    */       case NEW:
/* 14 */         this.changeName = "    §7[§6!§7] New §f" + this.changeName;
/*    */         break;
/*    */       case ADD:
/* 17 */         this.changeName = "    §7[§a+§7] Added §f" + this.changeName;
/*    */         break;
/*    */       case RECODE:
/* 20 */         this.changeName = "    §7[§9*§7] Recoded §f" + this.changeName;
/*    */         break;
/*    */       case IMPROVED:
/* 23 */         this.changeName = "    §7[§d/§7] Improved §f" + this.changeName;
/*    */         break;
/*    */       case DELETE:
/* 26 */         this.changeName = "    §7[§c-§7] Deleted §f" + this.changeName;
/*    */         break;
/*    */       case FIXED:
/* 29 */         this.changeName = "    §7[§b/§7] Fixed §f" + this.changeName;
/*    */         break;
/*    */       case MOVED:
/* 32 */         this.changeName = "    §7[§9->§7] Moved §f" + this.changeName;
/*    */         break;
/*    */       case RENAMED:
/* 35 */         this.changeName = "    §7[§9!§7] Renamed §f" + this.changeName;
/*    */         break;
/*    */       case NONE:
/* 38 */         this.changeName = " " + this.changeName;
/*    */         break;
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getLogName() {
/* 44 */     return this.changeName;
/*    */   }
/*    */   
/*    */   public ChangeType getType() {
/* 48 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\changelog\ChangeLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */