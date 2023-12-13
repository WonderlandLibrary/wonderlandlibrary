/*     */ package org.neverhook.client.ui.newclickgui.settings;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*     */ import org.neverhook.client.helpers.math.MathematicHelper;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.AnimationHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.ScreenHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ import org.neverhook.client.ui.newclickgui.FeaturePanel;
/*     */ import org.neverhook.client.ui.newclickgui.Theme;
/*     */ 
/*     */ public class NumberSettingComponent
/*     */   extends Component {
/*  22 */   private final Theme theme = new Theme();
/*     */   private final ScreenHelper screenHelper;
/*  24 */   public float currentValueAnimate = 0.0F;
/*     */   private boolean dragging;
/*     */   private float renderOffset;
/*     */   
/*     */   public NumberSettingComponent(FeaturePanel featurePanel, NumberSetting setting) {
/*  29 */     this.featurePanel = featurePanel;
/*  30 */     this.setting = (Setting)setting;
/*  31 */     this.screenHelper = new ScreenHelper(0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY) {
/*  36 */     ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getInstance());
/*     */     
/*  38 */     float min = ((NumberSetting)this.setting).getMinValue();
/*  39 */     float max = ((NumberSetting)this.setting).getMaxValue();
/*  40 */     float value = (float)MathematicHelper.round(((NumberSetting)this.setting).getNumberValue(), ((NumberSetting)this.setting).getIncrement());
/*  41 */     float amountWidth = (((NumberSetting)this.setting).getNumberValue() - min) / (max - min);
/*     */     
/*  43 */     int color = 0;
/*  44 */     Color onecolor = new Color(ClickGui.color.getColorValue());
/*  45 */     Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
/*  46 */     double speed = ClickGui.speed.getNumberValue();
/*  47 */     switch (ClickGui.clickGuiColor.currentMode) {
/*     */       case "Client":
/*  49 */         color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (this.height * 6.0F / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Fade":
/*  52 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (this.height * 6.0F / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Color Two":
/*  55 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (this.height * 6.0F / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Astolfo":
/*  58 */         color = PaletteHelper.astolfo(true, (int)this.height).getRGB();
/*     */         break;
/*     */       case "Static":
/*  61 */         color = onecolor.getRGB();
/*     */         break;
/*     */       case "Rainbow":
/*  64 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */     } 
/*     */     
/*  68 */     mc.circleregular.drawStringWithOutline(this.setting.getName(), (scaledResolution.getScaledWidth() / 2.0F - 140.0F), (this.y + this.height / 2.0F - mc.circleregular.getFontHeight() / 2.0F - 1.0F), this.theme.textColor.getRGB());
/*  69 */     mc.circleregular.drawStringWithOutline(this.setting.getName(), (scaledResolution.getScaledWidth() / 2.0F - 140.0F), (this.y + this.height / 2.0F - mc.circleregular.getFontHeight() / 2.0F - 1.0F), this.theme.textColor.getRGB());
/*     */     
/*  71 */     this.currentValueAnimate = AnimationHelper.animation(this.currentValueAnimate, amountWidth, 0.0F);
/*  72 */     RectHelper.drawSmoothRect(this.x, this.y + this.height / 2.0F - 1.0F, this.x + this.width, this.y + this.height / 2.0F + 2.0F, this.theme.panelColor.getRGB());
/*     */     
/*  74 */     RectHelper.drawSmoothGradientRect(this.x, (this.y + this.height / 2.0F - 1.0F), (this.x + this.currentValueAnimate * this.width), (this.y + this.height / 2.0F + 2.0F), color, (new Color(color)).darker().getRGB());
/*     */     
/*  76 */     String valueString = "";
/*     */     
/*  78 */     NumberSetting.NumberType numberType = ((NumberSetting)this.setting).getType();
/*     */     
/*  80 */     switch (numberType) {
/*     */       case PERCENTAGE:
/*  82 */         valueString = valueString + '%';
/*     */         break;
/*     */       case MS:
/*  85 */         valueString = valueString + "ms";
/*     */         break;
/*     */       case DISTANCE:
/*  88 */         valueString = valueString + 'm';
/*     */       case SIZE:
/*  90 */         valueString = valueString + "SIZE";
/*     */       case APS:
/*  92 */         valueString = valueString + "APS";
/*     */         break;
/*     */       default:
/*  95 */         valueString = "";
/*     */         break;
/*     */     } 
/*  98 */     if (isHovering(mouseX, mouseY) || this.dragging) {
/*  99 */       this.screenHelper.interpolate(scaledResolution.getScaledWidth(), (scaledResolution.getScaledHeight() + 5), 6.0D);
/* 100 */       if (this.renderOffset < 0.5F) {
/* 101 */         this.renderOffset = (float)(this.renderOffset + 0.002D * RectHelper.delta);
/*     */       }
/*     */     } else {
/* 104 */       this.screenHelper.interpolate(0.0F, 0.0F, 6.0D);
/* 105 */       this.renderOffset = 0.0F;
/*     */     } 
/*     */     
/* 108 */     GL11.glPushMatrix();
/* 109 */     GL11.glTranslatef(this.x + this.currentValueAnimate * this.width, this.y + this.height / 2.0F, 0.0F);
/* 110 */     GL11.glScaled(0.5D + (this.screenHelper.getX() / scaledResolution.getScaledWidth()) - this.renderOffset, 0.5D + (this.screenHelper.getY() / scaledResolution.getScaledHeight()) - this.renderOffset, 0.0D);
/* 111 */     GL11.glTranslatef(-(this.x + this.currentValueAnimate * this.width), -(this.y + this.height / 2.0F), 0.0F);
/*     */     
/* 113 */     if (this.screenHelper.getX() > 250.0F) {
/* 114 */       RenderHelper.drawCircle(this.x + this.currentValueAnimate * this.width, this.y + this.height / 2.0F + 0.5F, 2.0F, true, Color.WHITE);
/*     */     }
/*     */     
/* 117 */     GL11.glPopMatrix();
/*     */     
/* 119 */     if (isHovering(mouseX, mouseY)) {
/* 120 */       mc.clickguismall.drawStringWithOutline(value + valueString, (this.x - 5.0F - mc.clickguismall.getStringWidth(value + valueString)), (this.y + 5.0F), this.theme.textColor.getRGB());
/*     */     }
/*     */     
/* 123 */     if (this.dragging) {
/* 124 */       ((NumberSetting)this.setting).setValueNumber((float)MathematicHelper.round(((mouseX - this.x) * (max - min) / this.width + min), ((NumberSetting)this.setting).getIncrement()));
/* 125 */       if (((NumberSetting)this.setting).getNumberValue() > max) {
/* 126 */         ((NumberSetting)this.setting).setValueNumber(max);
/* 127 */       } else if (((NumberSetting)this.setting).getNumberValue() < min) {
/* 128 */         ((NumberSetting)this.setting).setValueNumber(min);
/*     */       } 
/*     */     } 
/*     */     
/* 132 */     super.drawScreen(mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 137 */     if (isHovering(mouseX, mouseY) && mouseButton == 0) {
/* 138 */       this.dragging = true;
/*     */     }
/* 140 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   public boolean isHovering(int mouseX, int mouseY) {
/* 144 */     return (mouseX > this.x - 10.0F && mouseX < this.x + this.width && mouseY > this.y + this.height / 2.0F - 5.0F && mouseY < this.y + this.height / 2.0F + 5.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY, int state) {
/* 149 */     this.dragging = false;
/* 150 */     super.mouseReleased(mouseX, mouseY, state);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\settings\NumberSettingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */