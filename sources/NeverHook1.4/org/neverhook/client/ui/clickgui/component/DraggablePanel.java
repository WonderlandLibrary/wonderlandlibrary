/*    */ package org.neverhook.client.ui.clickgui.component;
/*    */ 
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ 
/*    */ public abstract class DraggablePanel
/*    */   extends ExpandableComponent
/*    */ {
/*    */   private boolean dragging;
/*    */   private int prevX;
/*    */   private int prevY;
/*    */   
/*    */   public DraggablePanel(Component parent, String name, int x, int y, int width, int height) {
/* 13 */     super(parent, name, x, y, width, height);
/* 14 */     this.prevX = x;
/* 15 */     this.prevY = y;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
/* 20 */     if (this.dragging) {
/* 21 */       setX(mouseX - this.prevX);
/* 22 */       setY(mouseY - this.prevY);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onPress(int mouseX, int mouseY, int button) {
/* 28 */     if (button == 0) {
/* 29 */       this.dragging = true;
/* 30 */       this.prevX = mouseX - getX();
/* 31 */       this.prevY = mouseY - getY();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMouseRelease(int button) {
/* 37 */     super.onMouseRelease(button);
/* 38 */     this.dragging = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\component\DraggablePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */