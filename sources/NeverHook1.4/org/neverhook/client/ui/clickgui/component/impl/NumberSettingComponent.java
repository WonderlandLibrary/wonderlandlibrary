/*     */ package org.neverhook.client.ui.clickgui.component.impl;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*     */ import org.neverhook.client.helpers.math.MathematicHelper;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.AnimationHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ import org.neverhook.client.ui.clickgui.component.Component;
/*     */ import org.neverhook.client.ui.clickgui.component.PropertyComponent;
/*     */ 
/*     */ public class NumberSettingComponent
/*     */   extends Component
/*     */   implements PropertyComponent {
/*     */   public NumberSetting numberSetting;
/*  21 */   public float currentValueAnimate = 0.0F;
/*     */   private boolean sliding;
/*     */   
/*     */   public NumberSettingComponent(Component parent, NumberSetting numberSetting, int x, int y, int width, int height) {
/*  25 */     super(parent, numberSetting.getName(), x, y, width, height);
/*  26 */     this.numberSetting = numberSetting;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
/*  31 */     super.drawComponent(scaledResolution, mouseX, mouseY);
/*     */     
/*  33 */     int x = getX();
/*  34 */     int y = getY();
/*  35 */     int width = getWidth();
/*  36 */     int height = getHeight();
/*  37 */     double min = this.numberSetting.getMinValue();
/*  38 */     double max = this.numberSetting.getMaxValue();
/*  39 */     boolean hovered = isHovered(mouseX, mouseY);
/*     */     
/*  41 */     if (this.sliding) {
/*  42 */       this.numberSetting.setValueNumber((float)MathematicHelper.round((mouseX - x) * (max - min) / width + min, this.numberSetting.getIncrement()));
/*  43 */       if (this.numberSetting.getNumberValue() > max) {
/*  44 */         this.numberSetting.setValueNumber((float)max);
/*  45 */       } else if (this.numberSetting.getNumberValue() < min) {
/*  46 */         this.numberSetting.setValueNumber((float)min);
/*     */       } 
/*     */     } 
/*     */     
/*  50 */     float amountWidth = (float)((this.numberSetting.getNumberValue() - min) / (max - min));
/*  51 */     int color = 0;
/*  52 */     Color onecolor = new Color(ClickGui.color.getColorValue());
/*  53 */     Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
/*  54 */     double speed = ClickGui.speed.getNumberValue();
/*  55 */     switch (ClickGui.clickGuiColor.currentMode) {
/*     */       case "Client":
/*  57 */         color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (y * 6L / 60L * 2L)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Fade":
/*  60 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + ((float)(y * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Color Two":
/*  63 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + ((float)(y * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Astolfo":
/*  66 */         color = PaletteHelper.astolfo(true, y).getRGB();
/*     */         break;
/*     */       case "Static":
/*  69 */         color = onecolor.getRGB();
/*     */         break;
/*     */       case "Rainbow":
/*  72 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */     } 
/*     */     
/*  76 */     this.currentValueAnimate = AnimationHelper.animation(this.currentValueAnimate, amountWidth, 0.0F);
/*  77 */     float optionValue = (float)MathematicHelper.round(this.numberSetting.getNumberValue(), this.numberSetting.getIncrement());
/*  78 */     RectHelper.drawRect(x, y, (x + width), (y + height), (new Color(20, 20, 20, 160)).getRGB());
/*  79 */     RectHelper.drawRect((x + 3), (y + height - 5), (x + width - 3), (y + 13), (new Color(40, 39, 39)).getRGB());
/*     */     
/*  81 */     RectHelper.drawGradientRect((x + 3), (y + 13), ((x + 5) + this.currentValueAnimate * (width - 8)), (y + 15.0F), color, RenderHelper.darker(color, 2.0F));
/*  82 */     RenderHelper.drawCircle((x + 5) + this.currentValueAnimate * (width - 8), y + 14.0F, 2.0F, true, new Color(color));
/*     */     
/*  84 */     String valueString = "";
/*     */     
/*  86 */     NumberSetting.NumberType numberType = this.numberSetting.getType();
/*     */     
/*  88 */     switch (numberType) {
/*     */       case PERCENTAGE:
/*  90 */         valueString = valueString + '%';
/*     */         break;
/*     */       case MS:
/*  93 */         valueString = valueString + "ms";
/*     */         break;
/*     */       case DISTANCE:
/*  96 */         valueString = valueString + 'm';
/*     */       case SIZE:
/*  98 */         valueString = valueString + "SIZE";
/*     */       case APS:
/* 100 */         valueString = valueString + "APS";
/*     */         break;
/*     */       default:
/* 103 */         valueString = "";
/*     */         break;
/*     */     } 
/* 106 */     mc.circleregularSmall.drawStringWithShadow(this.numberSetting.getName(), (x + 2.0F), (y + height / 2.5F - 4.0F), Color.lightGray.getRGB());
/* 107 */     mc.circleregularSmall.drawStringWithShadow(optionValue + " " + valueString, (x + width - mc.circleregularSmall.getStringWidth(optionValue + " " + valueString) - 5), (y + height / 2.5F - 4.0F), Color.GRAY.getRGB());
/*     */     
/* 109 */     if (hovered && 
/* 110 */       this.numberSetting.getDesc() != null) {
/* 111 */       RectHelper.drawBorderedRect((x + 120), y + height / 1.5F + 3.5F, (x + 138 + mc.fontRendererObj.getStringWidth(this.numberSetting.getDesc()) - 10), y + 3.5F, 0.5F, (new Color(30, 30, 30, 255)).getRGB(), color, true);
/* 112 */       mc.fontRendererObj.drawStringWithShadow(this.numberSetting.getDesc(), (x + 124), y + height / 1.5F - 6.0F, -1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMouseClick(int mouseX, int mouseY, int button) {
/* 119 */     if (!this.sliding && button == 0 && isHovered(mouseX, mouseY)) {
/* 120 */       this.sliding = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseRelease(int button) {
/* 126 */     this.sliding = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Setting getSetting() {
/* 131 */     return (Setting)this.numberSetting;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\component\impl\NumberSettingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */