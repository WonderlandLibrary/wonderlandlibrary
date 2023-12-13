/*    */ package org.neverhook.client.ui.components.draggable;
/*    */ 
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import org.lwjgl.input.Mouse;
/*    */ 
/*    */ 
/*    */ public class DraggableComponent
/*    */ {
/*    */   private final int width;
/*    */   private final int height;
/*    */   private int x;
/*    */   private int y;
/*    */   private int color;
/*    */   private int lastX;
/*    */   private int lastY;
/*    */   private boolean dragging;
/*    */   private boolean canRender = true;
/*    */   
/*    */   public DraggableComponent(int x, int y, int width, int height, int color) {
/* 20 */     this.width = width;
/* 21 */     this.height = height;
/* 22 */     this.x = x;
/* 23 */     this.y = y;
/* 24 */     this.color = color;
/*    */   }
/*    */   
/*    */   public boolean isCanRender() {
/* 28 */     return this.canRender;
/*    */   }
/*    */   
/*    */   public void setCanRender(boolean canRender) {
/* 32 */     this.canRender = canRender;
/*    */   }
/*    */   
/*    */   public int getXPosition() {
/* 36 */     return this.x;
/*    */   }
/*    */   
/*    */   public void setXPosition(int x) {
/* 40 */     this.x = x;
/*    */   }
/*    */   
/*    */   public int getYPosition() {
/* 44 */     return this.y;
/*    */   }
/*    */   
/*    */   public void setYPosition(int y) {
/* 48 */     this.y = y;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 52 */     return this.height;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 56 */     return this.width;
/*    */   }
/*    */   
/*    */   public int getColor() {
/* 60 */     return this.color;
/*    */   }
/*    */   
/*    */   public void setColor(int color) {
/* 64 */     this.color = color;
/*    */   }
/*    */   
/*    */   public void draw(int mouseX, int mouseY) {
/* 68 */     if (this.canRender) {
/* 69 */       draggingFix(mouseX, mouseY);
/* 70 */       Gui.drawRect(getXPosition(), (getYPosition() - getHeight() / 4), (getXPosition() + getWidth()), (getYPosition() + getHeight()), getColor());
/* 71 */       boolean mouseOverX = (mouseX >= getXPosition() && mouseX <= getXPosition() + getWidth());
/* 72 */       boolean mouseOverY = (mouseY >= getYPosition() - getHeight() / 4 && mouseY <= getYPosition() - getHeight() / 4 + getHeight());
/* 73 */       if (mouseOverX && mouseOverY && 
/* 74 */         Mouse.isButtonDown(0) && 
/* 75 */         !this.dragging) {
/* 76 */         this.lastX = this.x - mouseX;
/* 77 */         this.lastY = this.y - mouseY;
/* 78 */         this.dragging = true;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void draggingFix(int mouseX, int mouseY) {
/* 86 */     if (this.dragging) {
/* 87 */       this.x = mouseX + this.lastX;
/* 88 */       this.y = mouseY + this.lastY;
/* 89 */       if (!Mouse.isButtonDown(0)) this.dragging = false; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\draggable\DraggableComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */