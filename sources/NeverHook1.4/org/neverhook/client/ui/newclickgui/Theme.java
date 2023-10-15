/*    */ package org.neverhook.client.ui.newclickgui;
/*    */ 
/*    */ import java.awt.Color;
/*    */ 
/*    */ public class Theme
/*    */ {
/*    */   public boolean dark = true;
/*    */   public Color bgColor;
/*    */   public Color elementColor;
/*    */   public Color opposite;
/*    */   public Color panelColor;
/*    */   public Color guiColor1;
/*    */   public Color guiColor2;
/*    */   public Color guiColor3;
/*    */   public Color guiColor4;
/*    */   public Color textColor;
/*    */   
/*    */   public Theme() {
/* 19 */     int difference = 16;
/* 20 */     if (this.dark) {
/* 21 */       this.bgColor = new Color(14, 14, 14, 255);
/* 22 */       this.elementColor = new Color(0, 0, 0, 255);
/* 23 */       this.opposite = new Color(255, 255, 255, 255);
/* 24 */       this.panelColor = new Color(42, 42, 42, 255);
/* 25 */       this.guiColor1 = new Color(0, 0, 0, 255);
/* 26 */       this.guiColor2 = new Color(difference, difference, difference, 255);
/* 27 */       this.guiColor3 = new Color(difference * 2, difference * 2, difference * 2, 255);
/* 28 */       this.guiColor4 = new Color(26, 26, 26, 255);
/* 29 */       this.textColor = new Color(241, 241, 241, 255);
/*    */     } else {
/* 31 */       this.bgColor = new Color(241, 241, 241, 255);
/* 32 */       this.elementColor = new Color(255, 255, 255, 255);
/* 33 */       this.opposite = new Color(0, 0, 0, 255);
/* 34 */       this.panelColor = new Color(213, 213, 213, 255);
/* 35 */       this.guiColor1 = new Color(255, 255, 255, 255);
/* 36 */       this.guiColor2 = new Color(255 - difference, 255 - difference, 255 - difference, 255);
/* 37 */       this.guiColor3 = new Color(255 - difference * 2, 255 - difference * 2, 255 - difference * 2, 255);
/* 38 */       this.guiColor4 = new Color(229, 229, 229, 255);
/* 39 */       this.textColor = new Color(14, 14, 14, 255);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\Theme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */