/*    */ package org.neverhook.client.settings.impl;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ 
/*    */ public class ColorSetting
/*    */   extends Setting
/*    */ {
/*    */   private int color;
/*    */   
/*    */   public ColorSetting(String name, int color, Supplier<Boolean> visible) {
/* 12 */     this.name = name;
/* 13 */     this.color = color;
/* 14 */     setVisible(visible);
/*    */   }
/*    */   
/*    */   public int getColorValue() {
/* 18 */     return this.color;
/*    */   }
/*    */   
/*    */   public void setColorValue(int color) {
/* 22 */     this.color = color;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\settings\impl\ColorSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */