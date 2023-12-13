/*    */ package org.neverhook.client.ui.newclickgui.settings;
/*    */ 
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.ui.newclickgui.FeaturePanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Component
/*    */   implements Helper
/*    */ {
/*    */   public FeaturePanel featurePanel;
/*    */   public Setting setting;
/*    */   public float x;
/*    */   public float y;
/*    */   public float width;
/*    */   public float height;
/*    */   public boolean extended;
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY) {}
/*    */   
/*    */   public void setInformations(float x, float y, float width, float height) {
/* 23 */     this.x = x;
/* 24 */     this.y = y;
/* 25 */     this.width = width;
/* 26 */     this.height = height;
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}
/*    */ 
/*    */   
/*    */   public void keyTyped(char chars, int key) {}
/*    */ 
/*    */   
/*    */   public void mouseReleased(int mouseX, int mouseY, int state) {}
/*    */ 
/*    */   
/*    */   public boolean isSettingVisible() {
/* 40 */     return this.setting.isVisible();
/*    */   }
/*    */   
/*    */   public boolean isHovered(int mouseX, int mouseY) {
/* 44 */     return (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\settings\Component.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */