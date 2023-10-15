/*    */ package org.neverhook.client.ui.newclickgui.settings;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.Comparator;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import org.neverhook.client.helpers.render.ScreenHelper;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.ui.newclickgui.FeaturePanel;
/*    */ import org.neverhook.client.ui.newclickgui.Theme;
/*    */ 
/*    */ public class ListSettingComponent
/*    */   extends Component {
/*    */   private final ScreenHelper screenHelper;
/* 20 */   private final Theme theme = new Theme();
/*    */   
/*    */   public ListSettingComponent(FeaturePanel featurePanel, ListSetting setting) {
/* 23 */     this.featurePanel = featurePanel;
/* 24 */     this.setting = (Setting)setting;
/* 25 */     this.screenHelper = new ScreenHelper(0.0F, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY) {
/* 30 */     ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getInstance());
/*    */     
/* 32 */     RectHelper.drawSmoothRect(this.x - mc.fontRenderer.getStringWidth(((ListSetting)this.setting).getCurrentMode()) - 15.0F, this.y + 1.0F, this.x, this.y + this.height - 3.0F, (new Color(26, 26, 26)).getRGB());
/* 33 */     mc.circleregular.drawStringWithOutline(this.setting.getName(), (scaledResolution.getScaledWidth() / 2.0F - 140.0F), (this.y + this.height / 2.0F - mc.fontRenderer.getFontHeight() / 2.0F), this.theme.textColor.getRGB());
/* 34 */     mc.clickguismall.drawStringWithOutline(((ListSetting)this.setting).getCurrentMode(), (this.x - mc.clickguismall.getStringWidth(((ListSetting)this.setting).getCurrentMode()) - mc.fontRenderer.getStringWidth(">") - 10.0F), (this.y + this.height / 2.0F - mc.fontRenderer.getFontHeight() / 2.0F), this.theme.textColor.getRGB());
/*    */     
/* 36 */     double xMid2 = (this.x - mc.fontRenderer.getStringWidth(">") / 2.0F - 0.75F);
/* 37 */     double yMid2 = (this.y + this.height / 2.0F + 0.75F);
/*    */     
/* 39 */     if (this.extended) {
/* 40 */       this.screenHelper.interpolate(0.0F, 90.0F, 4.0D);
/*    */     } else {
/* 42 */       this.screenHelper.interpolate(0.0F, 0.0F, 4.0D);
/*    */     } 
/*    */     
/* 45 */     GlStateManager.pushMatrix();
/* 46 */     GlStateManager.translate(xMid2, yMid2, 0.0D);
/* 47 */     GlStateManager.rotate(this.screenHelper.getY(), 0.0F, 0.0F, 1.0F);
/* 48 */     GlStateManager.translate(-xMid2, -yMid2, 0.0D);
/* 49 */     float extendedOffset = this.extended ? 3.0F : 1.5F;
/* 50 */     float extendedYOffset = this.extended ? 2.0F : -1.0F;
/* 51 */     mc.fontRenderer.drawStringWithShadow(">", (this.x - mc.fontRenderer.getStringWidth(">") - extendedOffset), (this.y + this.height / 2.0F - mc.fontRenderer.getFontHeight() / 2.0F + extendedYOffset), this.theme.textColor.getRGB());
/* 52 */     GlStateManager.popMatrix();
/*    */     
/* 54 */     if (!this.extended) {
/*    */       return;
/*    */     }
/* 57 */     int yPlus = (int)this.height;
/* 58 */     for (String sld : ((ListSetting)this.setting).getModes()) {
/* 59 */       if (!((ListSetting)this.setting).getCurrentMode().equals(sld)) {
/* 60 */         ArrayList<String> modesArray = new ArrayList<>(((ListSetting)this.setting).getModes());
/* 61 */         String max = Collections.<String>max(modesArray, Comparator.comparing(String::length));
/* 62 */         RectHelper.drawSmoothRect(this.x - mc.clickguismall.getStringWidth(max) - 10.0F, this.y + yPlus, this.x, this.y + this.height + yPlus - 1.0F, (new Color(29, 29, 29)).getRGB());
/* 63 */         mc.clickguismall.drawStringWithOutline(sld, (this.x - mc.clickguismall.getStringWidth(max) - 6.0F), (this.y + yPlus + this.height / 2.0F - mc.fontRenderer.getFontHeight() / 2.0F + 1.0F), this.theme.textColor.getRGB());
/* 64 */         yPlus = (int)(yPlus + this.height - 1.0F);
/*    */       } 
/*    */     } 
/*    */     
/* 68 */     super.drawScreen(mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 73 */     if (isHovering(mouseX, mouseY) && mouseButton == 0) {
/* 74 */       this.extended = !this.extended;
/*    */     }
/* 76 */     if (!this.extended) {
/*    */       return;
/*    */     }
/* 79 */     int yPlus = (int)this.height;
/* 80 */     for (String modes : ((ListSetting)this.setting).getModes()) {
/* 81 */       if (!((ListSetting)this.setting).getCurrentMode().equals(modes)) {
/* 82 */         if (mouseX > this.x - this.width - 5.0F && mouseX < this.x && mouseY > this.y + yPlus && mouseY < this.y + yPlus + this.height && mouseButton == 0) {
/* 83 */           ((ListSetting)this.setting).setListMode(modes);
/*    */         }
/* 85 */         yPlus = (int)(yPlus + this.height);
/*    */       } 
/*    */     } 
/* 88 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*    */   }
/*    */   
/*    */   public boolean isHovering(int mouseX, int mouseY) {
/* 92 */     return (mouseX > this.x - this.width - 15.0F && mouseX < this.x && mouseY > this.y && mouseY < this.y + this.height - 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\settings\ListSettingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */