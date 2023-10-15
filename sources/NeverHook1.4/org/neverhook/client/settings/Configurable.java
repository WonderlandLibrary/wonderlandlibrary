/*    */ package org.neverhook.client.settings;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Configurable
/*    */ {
/*  9 */   private final ArrayList<Setting> settingList = new ArrayList<>();
/*    */   
/*    */   public final void addSettings(Setting... options) {
/* 12 */     this.settingList.addAll(Arrays.asList(options));
/*    */   }
/*    */   
/*    */   public final List<Setting> getSettings() {
/* 16 */     return this.settingList;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\settings\Configurable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */