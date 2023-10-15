/*     */ package org.neverhook.client.feature.impl.hud;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender2D;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.ScreenHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class FeatureList
/*     */   extends Feature
/*     */ {
/*     */   public static ListSetting sortMode;
/*     */   public static ListSetting fontRenderType;
/*     */   public static ListSetting borderMode;
/*     */   public static BooleanSetting backGround;
/*  32 */   public static BooleanSetting backGroundGradient = new BooleanSetting("BackGround Gradient", false, () -> Boolean.valueOf(backGround.getBoolValue()));
/*  33 */   public static ColorSetting backGroundColor2 = new ColorSetting("BackGround Color Two", Color.BLACK.getRGB(), () -> Boolean.valueOf((backGround.getBoolValue() && backGroundGradient.getBoolValue())));
/*  34 */   public static ColorSetting backGroundColor = new ColorSetting("BackGround Color", Color.BLACK.getRGB(), () -> Boolean.valueOf(backGround.getBoolValue()));
/*     */   public static BooleanSetting border;
/*     */   public static BooleanSetting rightBorder;
/*     */   public static NumberSetting x;
/*     */   public static NumberSetting y;
/*     */   public static NumberSetting offset;
/*     */   public static NumberSetting size;
/*     */   public static NumberSetting borderWidth;
/*     */   public static NumberSetting rainbowSaturation;
/*     */   public static NumberSetting rainbowBright;
/*     */   public static NumberSetting fontX;
/*     */   public static NumberSetting fontY;
/*  46 */   public static BooleanSetting blur = new BooleanSetting("Blur", false, () -> Boolean.valueOf(backGround.getBoolValue()));
/*     */   public static BooleanSetting suffix;
/*  48 */   public static ListSetting colorSuffixMode = new ListSetting("Suffix Mode Color", "Default", () -> Boolean.valueOf(suffix.getBoolValue()), new String[] { "Astolfo", "Default", "Static", "Rainbow", "Custom", "Category" });
/*  49 */   public static ColorSetting suffixColor = new ColorSetting("Suffix Color", Color.GRAY.getRGB(), () -> Boolean.valueOf((colorSuffixMode.currentMode.equals("Custom") || (colorSuffixMode.currentMode.equals("Static") && suffix.getBoolValue()))));
/*  50 */   public static ListSetting position = new ListSetting("Position", "Right", () -> Boolean.valueOf(true), new String[] { "Right", "Left" });
/*     */   
/*     */   public FeatureList() {
/*  53 */     super("FeatureList", "Показывает список всех включенных модулей", Type.Hud);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     borderMode = new ListSetting("Border Mode", "Full", () -> Boolean.valueOf(border.getBoolValue()), new String[] { "Full", "Single" });
/*  60 */     sortMode = new ListSetting("FeatureList Sort", "Length", () -> Boolean.valueOf(true), new String[] { "Length", "Alphabetical" });
/*  61 */     fontRenderType = new ListSetting("FontRender Type", "Shadow", () -> Boolean.valueOf(true), new String[] { "Default", "Shadow", "Outline" });
/*  62 */     backGround = new BooleanSetting("Background", true, () -> Boolean.valueOf(true));
/*  63 */     border = new BooleanSetting("Border", true, () -> Boolean.valueOf(true));
/*  64 */     rightBorder = new BooleanSetting("Right Border", true, () -> Boolean.valueOf(true));
/*  65 */     suffix = new BooleanSetting("Suffix", true, () -> Boolean.valueOf(true));
/*     */ 
/*     */     
/*  68 */     rainbowSaturation = new NumberSetting("Rainbow Saturation", 0.8F, 0.1F, 1.0F, 0.1F, () -> Boolean.valueOf((HUD.colorList.currentMode.equals("Rainbow") || colorSuffixMode.currentMode.equals("Rainbow"))));
/*  69 */     rainbowBright = new NumberSetting("Rainbow Brightness", 1.0F, 0.1F, 1.0F, 0.1F, () -> Boolean.valueOf((HUD.colorList.currentMode.equals("Rainbow") || colorSuffixMode.currentMode.equals("Rainbow"))));
/*  70 */     fontX = new NumberSetting("FontX", 0.0F, -4.0F, 20.0F, 0.1F, () -> Boolean.valueOf(true));
/*  71 */     fontY = new NumberSetting("FontY", 0.0F, -4.0F, 20.0F, 0.01F, () -> Boolean.valueOf(true));
/*  72 */     x = new NumberSetting("FeatureList X", 0.0F, 0.0F, 500.0F, 1.0F, () -> Boolean.valueOf(!blur.getBoolValue()));
/*  73 */     y = new NumberSetting("FeatureList Y", 0.0F, 0.0F, 500.0F, 1.0F, () -> Boolean.valueOf(!blur.getBoolValue()));
/*  74 */     offset = new NumberSetting("Font Offset", 11.0F, 7.0F, 20.0F, 0.5F, () -> Boolean.valueOf(true));
/*  75 */     borderWidth = new NumberSetting("Border Width", 1.0F, 0.0F, 10.0F, 0.1F, () -> Boolean.valueOf(rightBorder.getBoolValue()));
/*  76 */     addSettings(new Setting[] { (Setting)position, (Setting)sortMode, (Setting)fontRenderType, (Setting)borderMode, (Setting)colorSuffixMode, (Setting)suffixColor, (Setting)fontX, (Setting)fontY, (Setting)border, (Setting)rightBorder, (Setting)suffix, (Setting)borderWidth, (Setting)backGround, (Setting)backGroundGradient, (Setting)backGroundColor, (Setting)backGroundColor2, (Setting)rainbowSaturation, (Setting)rainbowBright, (Setting)x, (Setting)y, (Setting)offset });
/*     */   }
/*     */   
/*     */   private static Feature getNextEnabledFeature(List<Feature> features, int index) {
/*  80 */     for (int i = index; i < features.size(); i++) {
/*  81 */       Feature feature = features.get(i);
/*  82 */       if (feature.getState() && feature.visible && 
/*  83 */         !feature.getSuffix().equals("ClickGui") && feature.visible) {
/*  84 */         return feature;
/*     */       }
/*     */     } 
/*     */     
/*  88 */     return null;
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {
/*  93 */     String mode = HUD.colorList.getCurrentMode();
/*  94 */     setSuffix(mode);
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender2D(EventRender2D event) {
/*  99 */     float width = event.getResolution().getScaledWidth() - (rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0.0F);
/* 100 */     float y = 1.0F;
/* 101 */     String arraySort = sortMode.getCurrentMode();
/* 102 */     if (NeverHook.instance.featureManager.getFeatureByClass(FeatureList.class).getState() && !mc.gameSettings.showDebugInfo) {
/* 103 */       NeverHook.instance.featureManager.getFeatureList().sort(arraySort.equalsIgnoreCase("Alphabetical") ? Comparator.<T, Comparable>comparing(Feature::getLabel) : Comparator.<T>comparingInt(module -> !HUD.font.currentMode.equals("Minecraft") ? -ClientHelper.getFontRender().getStringWidth(suffix.getBoolValue() ? module.getSuffix() : module.getLabel()) : -mc.fontRendererObj.getStringWidth(suffix.getBoolValue() ? module.getSuffix() : module.getLabel())));
/* 104 */       for (Feature feature : NeverHook.instance.featureManager.getFeatureList()) {
/* 105 */         ScreenHelper animationHelper = feature.getScreenHelper();
/* 106 */         String featureSuffix = suffix.getBoolValue() ? feature.getSuffix() : feature.getLabel();
/* 107 */         float listOffset = offset.getNumberValue();
/* 108 */         float length = !HUD.font.currentMode.equals("Minecraft") ? ClientHelper.getFontRender().getStringWidth(featureSuffix) : mc.fontRendererObj.getStringWidth(featureSuffix);
/* 109 */         float featureX = width - length;
/* 110 */         boolean state = (feature.getState() && feature.visible);
/*     */         
/* 112 */         if (state) {
/* 113 */           animationHelper.interpolate(featureX, y, 4.0D * Minecraft.frameTime / 6.0D);
/*     */         } else {
/* 115 */           animationHelper.interpolate(width, y, 4.0D * Minecraft.frameTime / 6.0D);
/*     */         } 
/*     */         
/* 118 */         float yPotion = 2.0F;
/*     */         
/* 120 */         for (PotionEffect potionEffect : mc.player.getActivePotionEffects()) {
/* 121 */           if (potionEffect.getPotion().isBeneficial()) {
/* 122 */             yPotion = 26.0F;
/*     */           }
/* 124 */           if (potionEffect.getPotion().isBadEffect()) {
/* 125 */             yPotion = 52.0F;
/*     */           }
/*     */         } 
/*     */         
/* 129 */         float translateY = animationHelper.getY() + yPotion;
/* 130 */         float translateX = animationHelper.getX() - (rightBorder.getBoolValue() ? 2.5F : 1.5F) - fontX.getNumberValue();
/* 131 */         int color = 0;
/* 132 */         int colorCustom = HUD.onecolor.getColorValue();
/* 133 */         int colorCustom2 = HUD.twocolor.getColorValue();
/* 134 */         double time = HUD.time.getNumberValue();
/* 135 */         String mode = HUD.colorList.getOptions();
/* 136 */         boolean visible = (animationHelper.getX() < width);
/*     */         
/* 138 */         if (visible) {
/* 139 */           switch (mode.toLowerCase()) {
/*     */             case "rainbow":
/* 141 */               color = PaletteHelper.rainbow((int)(y * time), rainbowSaturation.getNumberValue(), rainbowBright.getNumberValue()).getRGB();
/*     */               break;
/*     */             case "astolfo":
/* 144 */               color = PaletteHelper.astolfo(false, (int)y * 4).getRGB();
/*     */               break;
/*     */             case "static":
/* 147 */               color = (new Color(colorCustom)).getRGB();
/*     */               break;
/*     */             case "custom":
/* 150 */               color = PaletteHelper.fadeColor((new Color(colorCustom)).getRGB(), (new Color(colorCustom2)).getRGB(), (float)Math.abs((System.currentTimeMillis() / time / time + (y * 6.0F / 61.0F * 2.0F)) % 2.0D));
/*     */               break;
/*     */             case "fade":
/* 153 */               color = PaletteHelper.fadeColor((new Color(colorCustom)).getRGB(), (new Color(colorCustom)).darker().darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / time / time + (y * 6.0F / 60.0F * 2.0F)) % 2.0D));
/*     */               break;
/*     */             case "none":
/* 156 */               color = -1;
/*     */               break;
/*     */             case "category":
/* 159 */               color = feature.getType().getColor();
/*     */               break;
/*     */           } 
/*     */           
/* 163 */           int colorFuffix = 0;
/* 164 */           String modeSuffix = colorSuffixMode.getOptions();
/* 165 */           switch (modeSuffix.toLowerCase()) {
/*     */             case "rainbow":
/* 167 */               colorFuffix = PaletteHelper.rainbow((int)(y * time), rainbowSaturation.getNumberValue(), rainbowBright.getNumberValue()).getRGB();
/*     */               break;
/*     */             case "astolfo":
/* 170 */               colorFuffix = PaletteHelper.astolfo(false, (int)y * 4).getRGB();
/*     */               break;
/*     */             case "static":
/* 173 */               colorFuffix = (new Color(suffixColor.getColorValue())).getRGB();
/*     */               break;
/*     */             case "default":
/* 176 */               colorFuffix = (new Color(192, 192, 192)).getRGB();
/*     */               break;
/*     */             case "category":
/* 179 */               colorFuffix = feature.getType().getColor();
/*     */               break;
/*     */           } 
/* 182 */           GlStateManager.pushMatrix();
/* 183 */           GlStateManager.translate(-x.getNumberValue(), FeatureList.y.getNumberValue(), 1.0F);
/*     */           
/* 185 */           Feature nextFeature = null;
/* 186 */           int index = NeverHook.instance.featureManager.getFeatureList().indexOf(feature) + 1;
/*     */           
/* 188 */           if (NeverHook.instance.featureManager.getFeatureList().size() > index) {
/* 189 */             nextFeature = getNextEnabledFeature(NeverHook.instance.featureManager.getFeatureList(), index);
/*     */           }
/*     */ 
/*     */           
/* 193 */           if (border.getBoolValue() && borderMode.currentMode.equals("Full")) {
/* 194 */             RectHelper.drawRect(translateX - 3.5D, (translateY - 1.0F), (translateX - 2.0F), (translateY + listOffset - 1.0F), color);
/*     */           }
/*     */           
/* 197 */           if (nextFeature != null && borderMode.currentMode.equals("Full")) {
/* 198 */             String name = suffix.getBoolValue() ? nextFeature.getSuffix() : nextFeature.getLabel();
/* 199 */             float font = !HUD.font.currentMode.equals("Minecraft") ? ClientHelper.getFontRender().getStringWidth(name) : mc.fontRendererObj.getStringWidth(name);
/* 200 */             float dif = length - font;
/* 201 */             if (border.getBoolValue() && borderMode.currentMode.equals("Full")) {
/* 202 */               RectHelper.drawRect(translateX - 3.5D, (translateY + listOffset + 1.0F), (translateX - 2.0F + dif), (translateY + listOffset - 1.0F), color);
/*     */             }
/*     */           }
/* 205 */           else if (border.getBoolValue() && borderMode.currentMode.equals("Full")) {
/* 206 */             RectHelper.drawRect(translateX - 3.5D, (translateY + listOffset + 1.0F), width, (translateY + listOffset - 1.0F), color);
/*     */           } 
/*     */ 
/*     */           
/* 210 */           if (borderMode.currentMode.equals("Single") && border.getBoolValue()) {
/* 211 */             RectHelper.drawRect(translateX - 3.5D, (translateY - 1.0F), (translateX - 2.0F), (translateY + listOffset - 1.0F), color);
/*     */           }
/*     */           
/* 214 */           if (backGround.getBoolValue()) {
/* 215 */             if (!backGroundGradient.getBoolValue()) {
/* 216 */               RectHelper.drawRect((translateX - 2.0F), (translateY - 1.0F), width, (translateY + listOffset - 1.0F), backGroundColor.getColorValue());
/*     */             } else {
/* 218 */               RectHelper.drawGradientRect((translateX - 2.0F), (translateY - 1.0F), width, (translateY + listOffset - 1.0F), backGroundColor.getColorValue(), backGroundColor2.getColorValue());
/*     */             } 
/*     */           }
/*     */           
/* 222 */           if (!HUD.font.currentMode.equals("Minecraft")) {
/* 223 */             String modeArrayFont = HUD.font.getOptions();
/* 224 */             float yOffset = modeArrayFont.equalsIgnoreCase("Verdana") ? 0.5F : (modeArrayFont.equalsIgnoreCase("Comfortaa") ? 3.0F : (modeArrayFont.equalsIgnoreCase("CircleRegular") ? 0.5F : (modeArrayFont.equalsIgnoreCase("Arial") ? 1.3F : (modeArrayFont.equalsIgnoreCase("Kollektif") ? 0.9F : (modeArrayFont.equalsIgnoreCase("Product Sans") ? 0.5F : (modeArrayFont.equalsIgnoreCase("RaleWay") ? 0.3F : (modeArrayFont.equalsIgnoreCase("LucidaConsole") ? 3.0F : (modeArrayFont.equalsIgnoreCase("Lato") ? 1.2F : (modeArrayFont.equalsIgnoreCase("Open Sans") ? 0.5F : (modeArrayFont.equalsIgnoreCase("SF UI") ? 1.3F : 2.0F))))))))));
/* 225 */             if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Shadow")) {
/* 226 */               if (suffix.getBoolValue()) {
/* 227 */                 ClientHelper.getFontRender().drawStringWithShadow(feature.getSuffix(), translateX, (translateY + yOffset + fontY.getNumberValue()), colorFuffix);
/*     */               }
/* 229 */               ClientHelper.getFontRender().drawStringWithShadow(feature.getLabel(), translateX, (translateY + yOffset + fontY.getNumberValue()), color);
/* 230 */             } else if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Default")) {
/* 231 */               if (suffix.getBoolValue()) {
/* 232 */                 ClientHelper.getFontRender().drawString(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), colorFuffix);
/*     */               }
/* 234 */               ClientHelper.getFontRender().drawString(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);
/* 235 */             } else if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Outline")) {
/* 236 */               if (suffix.getBoolValue()) {
/* 237 */                 ClientHelper.getFontRender().drawStringWithOutline(feature.getSuffix(), translateX, (translateY + yOffset + fontY.getNumberValue()), colorFuffix);
/*     */               }
/* 239 */               ClientHelper.getFontRender().drawStringWithOutline(feature.getLabel(), translateX, (translateY + yOffset + fontY.getNumberValue()), color);
/*     */             } 
/* 241 */           } else if (fontRenderType.currentMode.equals("Shadow")) {
/* 242 */             if (suffix.getBoolValue()) {
/* 243 */               mc.fontRendererObj.drawStringWithShadow(feature.getSuffix(), translateX, translateY + 1.0F + fontY.getNumberValue(), colorFuffix);
/*     */             }
/* 245 */             mc.fontRendererObj.drawStringWithShadow(feature.getLabel(), translateX, translateY + 1.0F + fontY.getNumberValue(), color);
/* 246 */           } else if (fontRenderType.currentMode.equals("Default")) {
/* 247 */             if (suffix.getBoolValue()) {
/* 248 */               mc.fontRendererObj.drawString(feature.getSuffix(), translateX, translateY + 1.0F + fontY.getNumberValue(), colorFuffix);
/*     */             }
/* 250 */             mc.fontRendererObj.drawString(feature.getLabel(), translateX, translateY + 1.0F + fontY.getNumberValue(), color);
/* 251 */           } else if (fontRenderType.currentMode.equals("Outline")) {
/* 252 */             if (suffix.getBoolValue()) {
/* 253 */               mc.fontRendererObj.drawStringWithOutline(feature.getSuffix(), translateX, translateY + 1.0F + fontY.getNumberValue(), colorFuffix);
/*     */             }
/* 255 */             mc.fontRendererObj.drawStringWithOutline(feature.getLabel(), translateX, translateY + 1.0F + fontY.getNumberValue(), color);
/*     */           } 
/*     */           
/* 258 */           y += listOffset;
/*     */           
/* 260 */           if (rightBorder.getBoolValue()) {
/* 261 */             float checkY = border.getBoolValue() ? 0.0F : 0.6F;
/* 262 */             RectHelper.drawRect(width, (translateY - 1.0F), (width + borderWidth.getNumberValue()), (translateY + listOffset - checkY), color);
/*     */           } 
/*     */           
/* 265 */           GlStateManager.popMatrix();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\hud\FeatureList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */