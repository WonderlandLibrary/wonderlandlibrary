/*    */ package org.neverhook.client.ui.clickgui.component.impl;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.ui.clickgui.component.Component;
/*    */ import org.neverhook.client.ui.clickgui.component.PropertyComponent;
/*    */ 
/*    */ public class VisibleComponent
/*    */   extends Component
/*    */   implements PropertyComponent {
/*    */   public Feature feature;
/*    */   
/*    */   public VisibleComponent(Feature feature, Component parent, int x, int y, int width, int height) {
/* 18 */     super(parent, "", x, y, width, height);
/* 19 */     this.feature = feature;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
/* 24 */     int x = getX();
/* 25 */     int y = getY();
/*    */     
/* 27 */     int height = getHeight();
/* 28 */     int width = getWidth();
/*    */     
/* 30 */     RectHelper.drawRect(x, y, (x + width), (y + height), (new Color(20, 20, 20, 160)).getRGB());
/* 31 */     mc.circleregularSmall.drawStringWithShadow("Visible: " + ChatFormatting.WHITE + this.feature.visible, (x + 2), (y + height / 2.5F - 2.0F), Color.LIGHT_GRAY.getRGB());
/* 32 */     super.drawComponent(scaledResolution, mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMouseClick(int mouseX, int mouseY, int button) {
/* 37 */     if (isHovered(mouseX, mouseY) && button == 0) {
/* 38 */       this.feature.setVisible(!this.feature.isVisible());
/*    */     }
/* 40 */     super.onMouseClick(mouseX, mouseY, button);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMouseRelease(int button) {
/* 45 */     super.onMouseRelease(button);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onKeyPress(int keyCode) {
/* 50 */     super.onKeyPress(keyCode);
/*    */   }
/*    */ 
/*    */   
/*    */   public Setting getSetting() {
/* 55 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\component\impl\VisibleComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */