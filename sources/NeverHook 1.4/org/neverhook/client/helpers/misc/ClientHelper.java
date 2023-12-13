/*    */ package org.neverhook.client.helpers.misc;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.multiplayer.ServerData;
/*    */ import org.neverhook.client.feature.impl.hud.FeatureList;
/*    */ import org.neverhook.client.feature.impl.hud.HUD;
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*    */ import org.neverhook.client.ui.font.MCFontRenderer;
/*    */ 
/*    */ public class ClientHelper
/*    */   implements Helper
/*    */ {
/*    */   public static ServerData serverData;
/*    */   
/*    */   public static Color getClientColor() {
/* 17 */     Color color = Color.white;
/* 18 */     Color onecolor = new Color(HUD.onecolor.getColorValue());
/* 19 */     Color twoColor = new Color(HUD.twocolor.getColorValue());
/* 20 */     double time = HUD.time.getNumberValue();
/* 21 */     String mode = HUD.colorList.getOptions();
/* 22 */     float yDist = 4.0F;
/* 23 */     if (mode.equalsIgnoreCase("Rainbow")) {
/* 24 */       color = PaletteHelper.rainbow((int)((yDist * 200.0F) * time), FeatureList.rainbowSaturation.getNumberValue(), FeatureList.rainbowBright.getNumberValue());
/* 25 */     } else if (mode.equalsIgnoreCase("Astolfo")) {
/* 26 */       color = PaletteHelper.astolfo(false, (int)yDist);
/* 27 */     } else if (mode.equalsIgnoreCase("Fade")) {
/* 28 */       color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / time / time + (yDist * 2.0F / 60.0F * 2.0F)) % 2.0D - 1.0D)));
/* 29 */     } else if (mode.equalsIgnoreCase("Static")) {
/* 30 */       color = new Color(onecolor.getRGB());
/* 31 */     } else if (mode.equalsIgnoreCase("Custom")) {
/* 32 */       color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), twoColor.getRGB(), (float)Math.abs((System.currentTimeMillis() / time / time + (yDist * 2.0F / 60.0F * 2.0F)) % 2.0D - 1.0D)));
/* 33 */     } else if (mode.equalsIgnoreCase("None")) {
/* 34 */       color = new Color(255, 255, 255);
/*    */     } 
/* 36 */     return color;
/*    */   }
/*    */   
/*    */   public static MCFontRenderer getFontRender() {
/* 40 */     MCFontRenderer font = mc.fontRenderer;
/* 41 */     String mode = HUD.font.getOptions();
/* 42 */     if (mode.equalsIgnoreCase("Comfortaa")) {
/* 43 */       font = mc.sfuiFontRender;
/* 44 */     } else if (mode.equalsIgnoreCase("SF UI")) {
/* 45 */       font = mc.fontRenderer;
/* 46 */     } else if (mode.equalsIgnoreCase("Verdana")) {
/* 47 */       font = mc.verdanaFontRender;
/* 48 */     } else if (mode.equalsIgnoreCase("RobotoRegular")) {
/* 49 */       font = mc.robotoRegularFontRender;
/* 50 */     } else if (mode.equalsIgnoreCase("Lato")) {
/* 51 */       font = mc.latoFontRender;
/* 52 */     } else if (mode.equalsIgnoreCase("Open Sans")) {
/* 53 */       font = mc.openSansFontRender;
/* 54 */     } else if (mode.equalsIgnoreCase("Ubuntu")) {
/* 55 */       font = mc.ubuntuFontRender;
/* 56 */     } else if (mode.equalsIgnoreCase("LucidaConsole")) {
/* 57 */       font = mc.lucidaConsoleFontRenderer;
/* 58 */     } else if (mode.equalsIgnoreCase("Calibri")) {
/* 59 */       font = mc.calibri;
/* 60 */     } else if (mode.equalsIgnoreCase("Product Sans")) {
/* 61 */       font = mc.productsans;
/* 62 */     } else if (mode.equalsIgnoreCase("RaleWay")) {
/* 63 */       font = mc.raleway;
/* 64 */     } else if (mode.equalsIgnoreCase("Kollektif")) {
/* 65 */       font = mc.kollektif;
/* 66 */     } else if (mode.equalsIgnoreCase("CircleRegular")) {
/* 67 */       font = mc.circleregular;
/* 68 */     } else if (mode.equalsIgnoreCase("MontserratRegular")) {
/* 69 */       font = mc.montserratRegular;
/* 70 */     } else if (mode.equalsIgnoreCase("MontserratLight")) {
/* 71 */       font = mc.montserratLight;
/* 72 */     } else if (mode.equalsIgnoreCase("Menlo")) {
/* 73 */       font = mc.menlo;
/*    */     } 
/* 75 */     return font;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\misc\ClientHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */