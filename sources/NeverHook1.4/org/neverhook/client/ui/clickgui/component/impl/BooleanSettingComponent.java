/*    */ package org.neverhook.client.ui.clickgui.component.impl;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*    */ import org.neverhook.client.helpers.misc.ClientHelper;
/*    */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*    */ import org.neverhook.client.helpers.render.AnimationHelper;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.ui.clickgui.component.Component;
/*    */ import org.neverhook.client.ui.clickgui.component.PropertyComponent;
/*    */ 
/*    */ public class BooleanSettingComponent
/*    */   extends Component
/*    */   implements PropertyComponent {
/*    */   public BooleanSetting booleanSetting;
/* 19 */   public float textHoverAnimate = 0.0F;
/* 20 */   public float leftRectAnimation = 0.0F;
/* 21 */   public float rightRectAnimation = 0.0F;
/*    */   
/*    */   public BooleanSettingComponent(Component parent, BooleanSetting booleanSetting, int x, int y, int width, int height) {
/* 24 */     super(parent, booleanSetting.getName(), x, y, width, height);
/* 25 */     this.booleanSetting = booleanSetting;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
/* 30 */     if (this.booleanSetting.isVisible()) {
/* 31 */       int x = getX();
/* 32 */       int y = getY();
/* 33 */       int width = getWidth();
/* 34 */       int height = getHeight();
/* 35 */       int middleHeight = getHeight() / 2;
/* 36 */       boolean hovered = isHovered(mouseX, mouseY);
/* 37 */       int color = 0;
/* 38 */       Color onecolor = new Color(ClickGui.color.getColorValue());
/* 39 */       Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
/* 40 */       double speed = ClickGui.speed.getNumberValue();
/* 41 */       switch (ClickGui.clickGuiColor.currentMode) {
/*    */         case "Client":
/* 43 */           color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (y * 6L / 60L * 2L)) % 2.0D - 1.0D));
/*    */           break;
/*    */         case "Fade":
/* 46 */           color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + y + ((float)(height * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*    */           break;
/*    */         case "Color Two":
/* 49 */           color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + y + ((float)(height * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*    */           break;
/*    */         case "Astolfo":
/* 52 */           color = PaletteHelper.astolfo(true, y).getRGB();
/*    */           break;
/*    */         case "Static":
/* 55 */           color = onecolor.getRGB();
/*    */           break;
/*    */         case "Rainbow":
/* 58 */           color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*    */           break;
/*    */       } 
/*    */       
/* 62 */       RectHelper.drawRect(x, y, (x + width), (y + height), (new Color(20, 20, 20, 160)).getRGB());
/* 63 */       mc.circleregularSmall.drawStringWithShadow(getName(), (x + 3), (y + middleHeight - 2), Color.GRAY.getRGB());
/* 64 */       this.textHoverAnimate = AnimationHelper.animation(this.textHoverAnimate, hovered ? 2.3F : 2.0F, 0.0F);
/* 65 */       this.leftRectAnimation = AnimationHelper.animation(this.leftRectAnimation, this.booleanSetting.getBoolValue() ? 10.0F : 17.0F, 0.0F);
/* 66 */       this.rightRectAnimation = AnimationHelper.animation(this.rightRectAnimation, (this.booleanSetting.getBoolValue() ? 3 : 10), 0.0F);
/* 67 */       RectHelper.drawSmoothRect((x + width - 18), (y + 5), (x + width - 2), (y + height - 5), (new Color(14, 14, 14)).getRGB());
/* 68 */       RectHelper.drawSmoothRect((x + width) - this.leftRectAnimation, (y + 6), (x + width) - this.rightRectAnimation, (y + height - 6), this.booleanSetting.getBoolValue() ? color : (new Color(50, 50, 50)).getRGB());
/* 69 */       if (hovered && 
/* 70 */         this.booleanSetting.getDesc() != null) {
/* 71 */         RectHelper.drawBorderedRect((x + 120), y + height / 1.5F + 3.5F, (x + 138 + mc.fontRendererObj.getStringWidth(this.booleanSetting.getDesc()) - 10), y + 3.5F, 0.5F, (new Color(30, 30, 30, 255)).getRGB(), color, true);
/* 72 */         mc.fontRendererObj.drawStringWithShadow(this.booleanSetting.getDesc(), (x + 124), y + height / 1.5F - 6.0F, -1);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onMouseClick(int mouseX, int mouseY, int button) {
/* 80 */     if (button == 0 && isHovered(mouseX, mouseY) && this.booleanSetting.isVisible()) {
/* 81 */       this.booleanSetting.setBoolValue(!this.booleanSetting.getBoolValue());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Setting getSetting() {
/* 87 */     return (Setting)this.booleanSetting;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\component\impl\BooleanSettingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */