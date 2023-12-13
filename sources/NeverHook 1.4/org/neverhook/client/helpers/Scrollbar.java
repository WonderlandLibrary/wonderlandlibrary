/*    */ package org.neverhook.client.helpers;
/*    */ public class Scrollbar {
/*    */   public float startX;
/*    */   public float startY;
/*    */   public float endY;
/*    */   
/*    */   public void drawScrollBar() {
/*  8 */     this.maxScroll += this.currentScroll / 14.0F;
/*    */   }
/*    */   public float currentScroll; public float maxScroll; public float minScroll;
/*    */   public void setInformation(float startX, float startY, float endY, float currentScroll, float maxScroll, float minScroll) {
/* 12 */     this.startX = startX;
/* 13 */     this.startY = startY;
/* 14 */     this.endY = endY;
/* 15 */     this.currentScroll = currentScroll;
/* 16 */     this.maxScroll = maxScroll;
/* 17 */     this.minScroll = minScroll;
/*    */   }
/*    */   
/*    */   public float getStartX() {
/* 21 */     return this.startX;
/*    */   }
/*    */   
/*    */   public float getStartY() {
/* 25 */     return this.startY;
/*    */   }
/*    */   
/*    */   public float getEndY() {
/* 29 */     return this.endY;
/*    */   }
/*    */   
/*    */   public float getCurrentScroll() {
/* 33 */     return this.currentScroll;
/*    */   }
/*    */   
/*    */   public float getMaxScroll() {
/* 37 */     return this.maxScroll;
/*    */   }
/*    */   
/*    */   public float getMinScroll() {
/* 41 */     return this.minScroll;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\Scrollbar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */