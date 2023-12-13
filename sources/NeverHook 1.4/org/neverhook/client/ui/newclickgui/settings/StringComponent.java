/*    */ package org.neverhook.client.ui.newclickgui.settings;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.util.ChatAllowedCharacters;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.StringSetting;
/*    */ import org.neverhook.client.ui.newclickgui.FeaturePanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringComponent
/*    */   extends Component
/*    */ {
/*    */   private boolean active;
/*    */   
/*    */   public StringComponent(FeaturePanel featurePanel, StringSetting setting) {
/* 21 */     this.featurePanel = featurePanel;
/* 22 */     this.setting = (Setting)setting;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY) {
/* 27 */     RectHelper.drawSmoothRect(this.x - 15.0F, this.y + 1.0F, this.x, this.y + this.height - 3.0F, this.active ? (new Color(36, 36, 36)).getRGB() : (new Color(26, 26, 26)).getRGB());
/* 28 */     mc.fontRendererObj.drawStringWithShadow(this.setting.getName(), this.x, this.y + this.height / 2.0F - mc.fontRendererObj.FONT_HEIGHT / 2.0F + 1.0F, (new Color(255, 255, 255)).getRGB());
/* 29 */     mc.fontRendererObj.drawStringWithShadow(((StringSetting)this.setting).currentText, this.x + this.width - 143.0F, this.y + this.height / 2.0F - mc.fontRendererObj.FONT_HEIGHT / 2.0F + 1.0F, (new Color(255, 255, 255)).getRGB());
/* 30 */     super.drawScreen(mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 35 */     if (isHovered(mouseX, mouseY) && mouseButton == 0 && this.featurePanel.usingSettings && NeverHook.instance.newClickGui.usingSetting) {
/* 36 */       this.active = !this.active;
/*    */     }
/* 38 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*    */   }
/*    */ 
/*    */   
/*    */   public void keyTyped(char chars, int key) {
/* 43 */     if (key == 1) {
/*    */       return;
/*    */     }
/* 46 */     if (28 == key && this.active) {
/* 47 */       enterString();
/* 48 */     } else if (key == 14 && this.active) {
/* 49 */       if (!((StringSetting)this.setting).currentText.isEmpty()) {
/* 50 */         ((StringSetting)this.setting).currentText = ((StringSetting)this.setting).currentText.substring(0, ((StringSetting)this.setting).currentText.length() - 1);
/*    */       }
/* 52 */     } else if (ChatAllowedCharacters.isAllowedCharacter(chars) && this.active) {
/* 53 */       ((StringSetting)this.setting).setCurrentText(((StringSetting)this.setting).currentText + chars);
/*    */     } 
/* 55 */     super.keyTyped(chars, key);
/*    */   }
/*    */   
/*    */   private void enterString() {
/* 59 */     this.active = false;
/* 60 */     ((StringSetting)this.setting).setCurrentText(((StringSetting)this.setting).currentText);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseReleased(int mouseX, int mouseY, int state) {
/* 65 */     super.mouseReleased(mouseX, mouseY, state);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\settings\StringComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */