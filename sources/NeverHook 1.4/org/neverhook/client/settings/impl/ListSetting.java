/*    */ package org.neverhook.client.settings.impl;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ 
/*    */ public class ListSetting
/*    */   extends Setting
/*    */ {
/*    */   public final List<String> modes;
/*    */   public String currentMode;
/*    */   public int index;
/*    */   
/*    */   public ListSetting(String name, String currentMode, Supplier<Boolean> visible, String... options) {
/* 16 */     this.name = name;
/* 17 */     this.modes = Arrays.asList(options);
/* 18 */     this.index = this.modes.indexOf(currentMode);
/* 19 */     this.currentMode = this.modes.get(this.index);
/* 20 */     setVisible(visible);
/* 21 */     addSettings(new Setting[] { this });
/*    */   }
/*    */   
/*    */   public String getCurrentMode() {
/* 25 */     return this.currentMode;
/*    */   }
/*    */   
/*    */   public void setListMode(String selected) {
/* 29 */     this.currentMode = selected;
/* 30 */     this.index = this.modes.indexOf(selected);
/*    */   }
/*    */   
/*    */   public List<String> getModes() {
/* 34 */     return this.modes;
/*    */   }
/*    */   
/*    */   public String getOptions() {
/* 38 */     return this.modes.get(this.index);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\settings\impl\ListSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */