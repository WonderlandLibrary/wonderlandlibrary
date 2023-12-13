/*    */ package org.neverhook.client.ui.notification;
/*    */ 
/*    */ import java.awt.Color;
/*    */ 
/*    */ public enum NotificationType
/*    */ {
/*  7 */   SUCCESS((new Color(100, 255, 100)).getRGB()),
/*  8 */   INFO((new Color(225, 225, 255)).getRGB()),
/*  9 */   ERROR((new Color(255, 100, 100)).getRGB()),
/* 10 */   WARNING((new Color(255, 211, 53)).getRGB());
/*    */   
/*    */   private final int color;
/*    */   
/*    */   NotificationType(int color) {
/* 15 */     this.color = color;
/*    */   }
/*    */   
/*    */   public int getColor() {
/* 19 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\notification\NotificationType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */