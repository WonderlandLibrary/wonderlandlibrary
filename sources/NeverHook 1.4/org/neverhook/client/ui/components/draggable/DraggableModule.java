/*    */ package org.neverhook.client.ui.components.draggable;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ 
/*    */ public class DraggableModule
/*    */   implements Helper {
/*    */   public String name;
/*    */   public int x;
/*    */   public int y;
/*    */   public DraggableComponent drag;
/*    */   
/*    */   public DraggableModule(String name, int x, int y) {
/* 14 */     this.name = name;
/* 15 */     this.x = x;
/* 16 */     this.y = y;
/* 17 */     this.drag = new DraggableComponent(this.x, this.y, getWidth(), getHeight(), (new Color(255, 255, 255, 0)).getRGB());
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw() {}
/*    */ 
/*    */   
/*    */   public void render(int mouseX, int mouseY) {
/* 25 */     this.drag.draw(mouseX, mouseY);
/*    */   }
/*    */   
/*    */   public int getX() {
/* 29 */     return this.drag.getXPosition();
/*    */   }
/*    */   
/*    */   public void setX(int x) {
/* 33 */     this.x = x;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 37 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 41 */     this.name = name;
/*    */   }
/*    */   
/*    */   public int getY() {
/* 45 */     return this.drag.getYPosition();
/*    */   }
/*    */   
/*    */   public void setY(int y) {
/* 49 */     this.y = y;
/*    */   }
/*    */   
/*    */   public int getX2() {
/* 53 */     return this.x;
/*    */   }
/*    */   
/*    */   public void setX2(int x) {
/* 57 */     this.drag.setXPosition(x);
/*    */   }
/*    */   
/*    */   public int getY2() {
/* 61 */     return this.y;
/*    */   }
/*    */   
/*    */   public void setY2(int y) {
/* 65 */     this.drag.setYPosition(y);
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 69 */     return 50;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 73 */     return 50;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\draggable\DraggableModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */