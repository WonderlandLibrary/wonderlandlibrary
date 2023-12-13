/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.render.EventRender2D;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class Crosshair
/*    */   extends Feature {
/*    */   public static ColorSetting colorGlobal;
/*    */   public static ListSetting crosshairMode;
/*    */   public BooleanSetting dynamic;
/*    */   public NumberSetting width;
/*    */   public NumberSetting gap;
/*    */   public NumberSetting length;
/*    */   public NumberSetting dynamicGap;
/*    */   
/*    */   public Crosshair() {
/* 27 */     super("Crosshair", "Изменяет ваш прицел", Type.Visuals);
/* 28 */     crosshairMode = new ListSetting("Crosshair Mode", "Smooth", () -> Boolean.valueOf(true), new String[] { "Smooth", "Border", "Rect" });
/* 29 */     this.dynamic = new BooleanSetting("Dynamic", false, () -> Boolean.valueOf(true));
/* 30 */     this.dynamicGap = new NumberSetting("Dynamic Gap", 3.0F, 1.0F, 20.0F, 1.0F, this.dynamic::getBoolValue);
/* 31 */     this.gap = new NumberSetting("Gap", 2.0F, 0.0F, 10.0F, 0.1F, () -> Boolean.valueOf(true));
/* 32 */     colorGlobal = new ColorSetting("Crosshair Color", (new Color(16777215)).getRGB(), () -> Boolean.valueOf(true));
/* 33 */     this.width = new NumberSetting("Width", 1.0F, 0.0F, 8.0F, 1.0F, () -> Boolean.valueOf(true));
/* 34 */     this.length = new NumberSetting("Length", 3.0F, 0.5F, 30.0F, 1.0F, () -> Boolean.valueOf(true));
/* 35 */     addSettings(new Setting[] { (Setting)crosshairMode, (Setting)this.dynamic, (Setting)this.dynamicGap, (Setting)this.gap, (Setting)colorGlobal, (Setting)this.width, (Setting)this.length });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender2D(EventRender2D event) {
/* 40 */     int crosshairColor = colorGlobal.getColorValue();
/* 41 */     float screenWidth = event.getResolution().getScaledWidth();
/* 42 */     float screenHeight = event.getResolution().getScaledHeight();
/* 43 */     float width = screenWidth / 2.0F;
/* 44 */     float height = screenHeight / 2.0F;
/* 45 */     boolean dyn = this.dynamic.getBoolValue();
/* 46 */     float dyngap = this.dynamicGap.getNumberValue();
/* 47 */     float wid = this.width.getNumberValue();
/* 48 */     float len = this.length.getNumberValue();
/* 49 */     boolean isMoving = (dyn && MovementHelper.isMoving());
/* 50 */     float gaps = isMoving ? dyngap : this.gap.getNumberValue();
/*    */     
/* 52 */     String mode = crosshairMode.getOptions();
/* 53 */     setSuffix(mode);
/*    */     
/* 55 */     if (mode.equalsIgnoreCase("Smooth")) {
/* 56 */       RectHelper.drawSmoothRect(width - gaps - len, height - wid / 2.0F, width - gaps, height + wid / 2.0F, crosshairColor);
/* 57 */       RectHelper.drawSmoothRect(width + gaps, height - wid / 2.0F, width + gaps + len, height + wid / 2.0F, crosshairColor);
/* 58 */       RectHelper.drawSmoothRect(width - wid / 2.0F, height - gaps - len, width + wid / 2.0F, height - gaps, crosshairColor);
/* 59 */       RectHelper.drawSmoothRect(width - wid / 2.0F, height + gaps, width + wid / 2.0F, height + gaps + len, crosshairColor);
/* 60 */     } else if (mode.equalsIgnoreCase("Border")) {
/* 61 */       RectHelper.drawBorderedRect(width - gaps - len, height - wid / 2.0F, width - gaps, height + wid / 2.0F, 0.5F, Color.WHITE.getRGB(), crosshairColor, true);
/* 62 */       RectHelper.drawBorderedRect(width + gaps, height - wid / 2.0F, width + gaps + len, height + wid / 2.0F, 0.5F, Color.WHITE.getRGB(), crosshairColor, true);
/* 63 */       RectHelper.drawBorderedRect(width - wid / 2.0F, height - gaps - len, width + wid / 2.0F, height - gaps, 0.5F, Color.WHITE.getRGB(), crosshairColor, true);
/* 64 */       RectHelper.drawBorderedRect(width - wid / 2.0F, height + gaps, width + wid / 2.0F, height + gaps + len, 0.5F, Color.WHITE.getRGB(), crosshairColor, true);
/* 65 */     } else if (mode.equalsIgnoreCase("Rect")) {
/* 66 */       RectHelper.drawRect((width - gaps - len), (height - wid / 2.0F), (width - gaps), (height + wid / 2.0F), crosshairColor);
/* 67 */       RectHelper.drawRect((width + gaps), (height - wid / 2.0F), (width + gaps + len), (height + wid / 2.0F), crosshairColor);
/* 68 */       RectHelper.drawRect((width - wid / 2.0F), (height - gaps - len), (width + wid / 2.0F), (height - gaps), crosshairColor);
/* 69 */       RectHelper.drawRect((width - wid / 2.0F), (height + gaps), (width + wid / 2.0F), (height + gaps + len), crosshairColor);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\Crosshair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */