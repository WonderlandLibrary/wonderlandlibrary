/*    */ package org.neverhook.client.ui.newclickgui.settings;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*    */ import org.neverhook.client.helpers.misc.ClientHelper;
/*    */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*    */ import org.neverhook.client.helpers.render.ScreenHelper;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.ui.newclickgui.FeaturePanel;
/*    */ import org.neverhook.client.ui.newclickgui.Theme;
/*    */ 
/*    */ public class BooleanSettingComponent
/*    */   extends Component {
/* 18 */   private final Theme theme = new Theme();
/*    */   private final ScreenHelper screenHelper;
/*    */   private float renderOffset;
/*    */   
/*    */   public BooleanSettingComponent(FeaturePanel featurePanel, BooleanSetting setting) {
/* 23 */     this.featurePanel = featurePanel;
/* 24 */     this.setting = (Setting)setting;
/* 25 */     this.screenHelper = new ScreenHelper(0.0F, 0.0F);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY) {
/* 31 */     ScaledResolution scaledResolution = new ScaledResolution(mc);
/*    */     
/* 33 */     RectHelper.drawSmoothRect(this.x + this.width - 1.0F, this.y + this.height / 2.0F - 5.0F, this.x + this.width + 9.0F, this.y + this.height / 2.0F + 5.0F, this.theme.guiColor3.getRGB());
/*    */     
/* 35 */     mc.circleregular.drawStringWithOutline(this.setting.getName(), this.x, (this.y + this.height / 2.0F - mc.circleregular.getFontHeight() / 2.0F + 0.5F), this.theme.textColor.getRGB());
/* 36 */     mc.circleregular.drawStringWithOutline(this.setting.getName(), this.x, (this.y + this.height / 2.0F - mc.circleregular.getFontHeight() / 2.0F + 0.5F), this.theme.textColor.getRGB());
/*    */     
/* 38 */     int color = 0;
/* 39 */     Color onecolor = new Color(ClickGui.color.getColorValue());
/* 40 */     Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
/* 41 */     double speed = ClickGui.speed.getNumberValue();
/* 42 */     switch (ClickGui.clickGuiColor.currentMode) {
/*    */       case "Client":
/* 44 */         color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (this.height * 6.0F / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*    */         break;
/*    */       case "Fade":
/* 47 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (this.height * 6.0F / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*    */         break;
/*    */       case "Color Two":
/* 50 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (this.height * 6.0F / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*    */         break;
/*    */       case "Astolfo":
/* 53 */         color = PaletteHelper.astolfo(true, (int)this.height).getRGB();
/*    */         break;
/*    */       case "Static":
/* 56 */         color = onecolor.getRGB();
/*    */         break;
/*    */       case "Rainbow":
/* 59 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*    */         break;
/*    */     } 
/*    */     
/* 63 */     if (((BooleanSetting)this.setting).getBoolValue()) {
/* 64 */       this.screenHelper.interpolate(scaledResolution.getScaledWidth(), (scaledResolution.getScaledHeight() + 5), 6.0D);
/* 65 */       if (this.renderOffset < 0.5F) {
/* 66 */         this.renderOffset = (float)(this.renderOffset + 0.002D * RectHelper.delta);
/*    */       }
/*    */     } else {
/* 69 */       this.screenHelper.interpolate(0.0F, 0.0F, 6.0D);
/* 70 */       this.renderOffset = 0.0F;
/*    */     } 
/*    */     
/* 73 */     GL11.glPushMatrix();
/* 74 */     GL11.glTranslatef(this.x + this.width + 2.0F, this.y + this.height / 2.0F - 4.0F, 0.0F);
/* 75 */     GL11.glScaled((this.screenHelper.getX() / scaledResolution.getScaledWidth() - this.renderOffset), (this.screenHelper.getY() / scaledResolution.getScaledHeight() - this.renderOffset), 0.0D);
/* 76 */     GL11.glTranslatef(-(this.x + this.width + 2.0F), -(this.y + this.height / 2.0F - 4.0F), 0.0F);
/*    */     
/* 78 */     if (this.screenHelper.getX() > 250.0F) {
/* 79 */       RectHelper.drawVerticalGradientSmoothRect(this.x + this.width - 1.0F, this.y + this.height / 2.0F - 5.0F, this.x + this.width + 9.0F, this.y + this.height / 2.0F + 5.0F, color, (new Color(color)).darker().getRGB());
/*    */     }
/*    */     
/* 82 */     GL11.glPopMatrix();
/*    */     
/* 84 */     super.drawScreen(mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 89 */     if (isHovering(mouseX, mouseY) && mouseButton == 0) {
/* 90 */       ((BooleanSetting)this.setting).setBoolValue(!((BooleanSetting)this.setting).getBoolValue());
/*    */     }
/* 92 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*    */   }
/*    */   
/*    */   public boolean isHovering(int mouseX, int mouseY) {
/* 96 */     return (mouseX > this.x + this.width && mouseX < this.x + this.width + 12.0F && mouseY > this.y && mouseY < this.y + this.height);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\settings\BooleanSettingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */