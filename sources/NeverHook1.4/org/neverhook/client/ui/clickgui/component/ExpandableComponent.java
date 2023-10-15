/*    */ package org.neverhook.client.ui.clickgui.component;
/*    */ 
/*    */ public abstract class ExpandableComponent
/*    */   extends Component {
/*    */   private boolean expanded;
/*    */   
/*    */   public ExpandableComponent(Component parent, String name, int x, int y, int width, int height) {
/*  8 */     super(parent, name, x, y, width, height);
/*    */   }
/*    */   
/*    */   public boolean isExpanded() {
/* 12 */     return this.expanded;
/*    */   }
/*    */   
/*    */   public void setExpanded(boolean expanded) {
/* 16 */     this.expanded = expanded;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMouseClick(int mouseX, int mouseY, int button) {
/* 21 */     if (isHovered(mouseX, mouseY)) {
/* 22 */       onPress(mouseX, mouseY, button);
/* 23 */       if (canExpand() && button == 1)
/* 24 */         this.expanded = !this.expanded; 
/*    */     } 
/* 26 */     if (isExpanded())
/* 27 */       super.onMouseClick(mouseX, mouseY, button); 
/*    */   }
/*    */   
/*    */   public abstract boolean canExpand();
/*    */   
/*    */   public abstract int getHeightWithExpand();
/*    */   
/*    */   public abstract void onPress(int paramInt1, int paramInt2, int paramInt3);
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\component\ExpandableComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */