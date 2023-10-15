/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.render.EventFogColor;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.ClientHelper;
/*    */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class FogColor
/*    */   extends Feature {
/*    */   public static NumberSetting distance;
/*    */   public ListSetting colorMode;
/*    */   public ColorSetting customColor;
/*    */   
/*    */   public FogColor() {
/* 22 */     super("FogColor", "Делает цвет тумана другим", Type.Visuals);
/* 23 */     this.colorMode = new ListSetting("Fog Color", "Rainbow", () -> Boolean.valueOf(true), new String[] { "Rainbow", "Client", "Custom" });
/* 24 */     distance = new NumberSetting("Distance", 0.1F, 0.001F, 2.0F, 0.01F, () -> Boolean.valueOf(true));
/* 25 */     this.customColor = new ColorSetting("Custom Fog", (new Color(16777215)).getRGB(), () -> Boolean.valueOf(this.colorMode.currentMode.equals("Custom")));
/* 26 */     addSettings(new Setting[] { (Setting)this.colorMode, (Setting)distance, (Setting)this.customColor });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onFogColor(EventFogColor event) {
/* 31 */     String colorModeValue = this.colorMode.getOptions();
/* 32 */     if (colorModeValue.equalsIgnoreCase("Rainbow")) {
/* 33 */       Color color = PaletteHelper.rainbow(1, 1.0F, 1.0F);
/* 34 */       event.setRed(color.getRed());
/* 35 */       event.setGreen(color.getGreen());
/* 36 */       event.setBlue(color.getBlue());
/* 37 */     } else if (colorModeValue.equalsIgnoreCase("Client")) {
/* 38 */       Color clientColor = ClientHelper.getClientColor();
/* 39 */       event.setRed(clientColor.getRed());
/* 40 */       event.setGreen(clientColor.getGreen());
/* 41 */       event.setBlue(clientColor.getBlue());
/* 42 */     } else if (colorModeValue.equalsIgnoreCase("Custom")) {
/* 43 */       Color customColorValue = new Color(this.customColor.getColorValue());
/* 44 */       event.setRed(customColorValue.getRed());
/* 45 */       event.setGreen(customColorValue.getGreen());
/* 46 */       event.setBlue(customColorValue.getBlue());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\FogColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */