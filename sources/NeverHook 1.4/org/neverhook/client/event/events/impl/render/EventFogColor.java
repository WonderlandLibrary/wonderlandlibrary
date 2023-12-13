/*    */ package org.neverhook.client.event.events.impl.render;
/*    */ 
/*    */ import org.neverhook.client.event.events.Event;
/*    */ 
/*    */ public class EventFogColor
/*    */   implements Event {
/*    */   public float red;
/*    */   public float green;
/*    */   public float blue;
/*    */   public int alpha;
/*    */   
/*    */   public EventFogColor(float red, float green, float blue, int alpha) {
/* 13 */     this.red = red;
/* 14 */     this.green = green;
/* 15 */     this.blue = blue;
/* 16 */     this.alpha = alpha;
/*    */   }
/*    */   
/*    */   public float getRed() {
/* 20 */     return this.red;
/*    */   }
/*    */   
/*    */   public void setRed(float red) {
/* 24 */     this.red = red;
/*    */   }
/*    */   
/*    */   public float getGreen() {
/* 28 */     return this.green;
/*    */   }
/*    */   
/*    */   public void setGreen(float green) {
/* 32 */     this.green = green;
/*    */   }
/*    */   
/*    */   public float getBlue() {
/* 36 */     return this.blue;
/*    */   }
/*    */   
/*    */   public void setBlue(float blue) {
/* 40 */     this.blue = blue;
/*    */   }
/*    */   
/*    */   public int getAlpha() {
/* 44 */     return this.alpha;
/*    */   }
/*    */   
/*    */   public void setAlpha(int alpha) {
/* 48 */     this.alpha = alpha;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\render\EventFogColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */