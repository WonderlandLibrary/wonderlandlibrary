/*    */ package org.neverhook.client.settings.impl;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ 
/*    */ public class StringSetting
/*    */   extends Setting
/*    */ {
/*    */   public String defaultText;
/*    */   public String currentText;
/*    */   
/*    */   public StringSetting(String name, String defaultText, String currentText, Supplier<Boolean> visible) {
/* 13 */     this.name = name;
/* 14 */     this.defaultText = defaultText;
/* 15 */     this.currentText = currentText;
/* 16 */     setVisible(visible);
/*    */   }
/*    */   
/*    */   public String getDefaultText() {
/* 20 */     return this.defaultText;
/*    */   }
/*    */   
/*    */   public void setDefaultText(String defaultText) {
/* 24 */     this.defaultText = defaultText;
/*    */   }
/*    */   
/*    */   public String getCurrentText() {
/* 28 */     return this.currentText;
/*    */   }
/*    */   
/*    */   public void setCurrentText(String currentText) {
/* 32 */     this.currentText = currentText;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\settings\impl\StringSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */