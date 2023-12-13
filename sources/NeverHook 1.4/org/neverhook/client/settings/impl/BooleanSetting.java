/*    */ package org.neverhook.client.settings.impl;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ 
/*    */ public class BooleanSetting
/*    */   extends Setting
/*    */ {
/*    */   private boolean state;
/*    */   private String desc;
/*    */   
/*    */   public BooleanSetting(String name, String desc, boolean state, Supplier<Boolean> visible) {
/* 13 */     this.name = name;
/* 14 */     this.desc = desc;
/* 15 */     this.state = state;
/* 16 */     setVisible(visible);
/*    */   }
/*    */   
/*    */   public BooleanSetting(String name, boolean state, Supplier<Boolean> visible) {
/* 20 */     this.name = name;
/* 21 */     this.state = state;
/* 22 */     setVisible(visible);
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 26 */     return this.desc;
/*    */   }
/*    */   
/*    */   public void setDesc(String desc) {
/* 30 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public boolean getBoolValue() {
/* 34 */     return this.state;
/*    */   }
/*    */   
/*    */   public void setBoolValue(boolean state) {
/* 38 */     this.state = state;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\settings\impl\BooleanSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */