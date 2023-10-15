/*    */ package org.neverhook.client.feature.impl;
/*    */ 
/*    */ import java.awt.Color;
/*    */ 
/*    */ public enum Type
/*    */ {
/*  7 */   Combat((new Color(220, 20, 60)).getRGB(), (new Color(137, 3, 42)).getRGB(), "Combat"),
/*  8 */   Movement((new Color(123, 104, 238)).getRGB(), (new Color(73, 63, 151)).getRGB(), "Movement"),
/*  9 */   Visuals((new Color(0, 206, 209)).getRGB(), (new Color(2, 121, 123)).getRGB(), "Visuals"),
/* 10 */   Player((new Color(244, 164, 96)).getRGB(), (new Color(132, 68, 9)).getRGB(), "Player"),
/* 11 */   Misc((new Color(60, 179, 113)).getRGB(), (new Color(28, 88, 57)).getRGB(), "Misc"),
/* 12 */   Ghost((new Color(90, 10, 190)).getRGB(), (new Color(90, 10, 120)).getRGB(), "Ghost"),
/* 13 */   Hud((new Color(186, 85, 211)).getRGB(), (new Color(91, 41, 102)).getRGB(), "Hud");
/*    */   
/*    */   private final int color;
/*    */   
/*    */   private final int color2;
/*    */   public String name;
/*    */   
/*    */   Type(int color, int color2, String name) {
/* 21 */     this.color = color;
/* 22 */     this.color2 = color2;
/* 23 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 27 */     return this.name;
/*    */   }
/*    */   
/*    */   public int getColor() {
/* 31 */     return this.color;
/*    */   }
/*    */   
/*    */   public int getColor2() {
/* 35 */     return this.color2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */