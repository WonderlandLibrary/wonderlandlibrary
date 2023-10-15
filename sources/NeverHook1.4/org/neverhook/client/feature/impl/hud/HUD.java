/*     */ package org.neverhook.client.feature.impl.hud;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import javafx.animation.Interpolator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender2D;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.player.MovementHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ import org.neverhook.client.ui.components.draggable.DraggableModule;
/*     */ 
/*     */ 
/*     */ public class HUD
/*     */   extends Feature
/*     */ {
/*     */   public static float globalOffset;
/*     */   public static ListSetting font;
/*     */   public static BooleanSetting clientInfo;
/*     */   public static BooleanSetting worldInfo;
/*     */   public static BooleanSetting potion;
/*     */   public static BooleanSetting potionIcons;
/*     */   public static BooleanSetting potionTimeColor;
/*     */   public static BooleanSetting armor;
/*     */   public static BooleanSetting rustHUD;
/*     */   public static BooleanSetting indicator;
/*     */   public static ListSetting indicatorMode;
/*  35 */   public static BooleanSetting blur = new BooleanSetting("Blur", false, () -> Boolean.valueOf(true));
/*  36 */   public static ListSetting colorList = new ListSetting("Global Color", "Astolfo", () -> Boolean.valueOf(true), new String[] { "Astolfo", "Static", "Fade", "Rainbow", "Custom", "None", "Category" });
/*  37 */   public static NumberSetting time = new NumberSetting("Color Time", 30.0F, 1.0F, 100.0F, 1.0F, () -> Boolean.valueOf(true));
/*  38 */   public static ColorSetting onecolor = new ColorSetting("One Color", (new Color(16777215)).getRGB(), () -> Boolean.valueOf((colorList.currentMode.equals("Fade") || colorList.currentMode.equals("Custom") || colorList.currentMode.equals("Static"))));
/*  39 */   public static ColorSetting twocolor = new ColorSetting("Two Color", (new Color(16711680)).getRGB(), () -> Boolean.valueOf(colorList.currentMode.equals("Custom")));
/*  40 */   public float animation = 0.0F;
/*  41 */   private float cooledAttackStrength = 0.0F;
/*  42 */   private float move = 0.0F;
/*     */   
/*     */   public HUD() {
/*  45 */     super("HUD", "Показывает информацию на экране", Type.Hud);
/*  46 */     clientInfo = new BooleanSetting("Client Info", true, () -> Boolean.valueOf(true));
/*  47 */     worldInfo = new BooleanSetting("World Info", true, () -> Boolean.valueOf(true));
/*  48 */     potion = new BooleanSetting("Potion Status", true, () -> Boolean.valueOf(true));
/*  49 */     potionIcons = new BooleanSetting("Potion Icons", true, potion::getBoolValue);
/*  50 */     potionTimeColor = new BooleanSetting("Potion Time Color", true, potion::getBoolValue);
/*  51 */     armor = new BooleanSetting("Armor Status", true, () -> Boolean.valueOf(true));
/*  52 */     rustHUD = new BooleanSetting("Rust", false, () -> Boolean.valueOf(true));
/*  53 */     indicator = new BooleanSetting("Indicator", false, () -> Boolean.valueOf(true));
/*  54 */     indicatorMode = new ListSetting("Indicator Mode", "Onetap v2", () -> Boolean.valueOf(indicator.getBoolValue()), new String[] { "Onetap v2", "Skeet" });
/*  55 */     font = new ListSetting("FontList", "RobotoRegular", () -> Boolean.valueOf(true), new String[] { "Minecraft", "MontserratRegular", "MontserratLight", "Menlo", "Comfortaa", "SF UI", "Calibri", "Verdana", "CircleRegular", "RobotoRegular", "JetBrains Mono", "LucidaConsole", "Corporative Sans", "Lato", "RaleWay", "Product Sans", "Arial", "Open Sans", "Kollektif", "Ubuntu" });
/*  56 */     addSettings(new Setting[] { (Setting)colorList, (Setting)time, (Setting)onecolor, (Setting)twocolor, (Setting)font, (Setting)clientInfo, (Setting)worldInfo, (Setting)potion, (Setting)potionIcons, (Setting)potionTimeColor, (Setting)armor, (Setting)rustHUD, (Setting)blur, (Setting)indicator, (Setting)indicatorMode });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender2(EventRender2D e) {
/*  61 */     if (indicator.getBoolValue()) {
/*  62 */       if (Float.isNaN(this.cooledAttackStrength)) {
/*  63 */         this.cooledAttackStrength = mc.player.getCooledAttackStrength(0.0F);
/*     */       }
/*  65 */       if (Float.isNaN(this.move)) {
/*  66 */         this.move = MovementHelper.getSpeed();
/*     */       }
/*     */       
/*  69 */       this.cooledAttackStrength = (float)Interpolator.LINEAR.interpolate(this.cooledAttackStrength, (mc.player.getCooledAttackStrength(0.0F) * 50.0F), (5.0F / Minecraft.getDebugFPS()));
/*  70 */       this.move = (float)Interpolator.LINEAR.interpolate(this.move, (MovementHelper.getSpeed() * 50.0F), (5.0F / Minecraft.getDebugFPS()));
/*     */       
/*  72 */       if (indicatorMode.currentMode.equals("Onetap v2")) {
/*  73 */         RectHelper.drawSmoothRectBetter(6.0F, e.getResolution().getScaledHeight() / 2.0F - 80.0F, 105.0F, 26.0F, (new Color(61, 58, 58)).getRGB());
/*  74 */         RectHelper.drawSmoothRectBetter(6.0F, e.getResolution().getScaledHeight() / 2.0F - 97.0F, 105.0F, 2.0F, (new Color(123, 0, 255)).getRGB());
/*  75 */         RectHelper.drawSmoothRectBetter(6.0F, e.getResolution().getScaledHeight() / 2.0F - 95.0F, 105.0F, 12.0F, (new Color(61, 58, 58)).getRGB());
/*  76 */         mc.clickguismall.drawStringWithShadow("flags", (32 + mc.clickguismall.getStringWidth("flags")), (e.getResolution().getScaledHeight() / 2.0F - 92.0F), -1);
/*  77 */         mc.clickguismall.drawStringWithShadow("cooldown", 10.0D, (e.getResolution().getScaledHeight() / 2.0F - 75.0F), -1);
/*  78 */         RectHelper.drawSmoothRectBetter((15 + mc.clickguismall.getStringWidth("cooldown")), e.getResolution().getScaledHeight() / 2.0F - 73.0F, 50.0F, 1.5F, (new Color(30, 30, 30, 70)).getRGB());
/*  79 */         RectHelper.drawSmoothRectBetter((15 + mc.clickguismall.getStringWidth("cooldown")), e.getResolution().getScaledHeight() / 2.0F - 73.0F, this.cooledAttackStrength, 1.5F, (new Color(123, 0, 255)).getRGB());
/*  80 */         mc.clickguismall.drawStringWithShadow("move", 10.0D, (e.getResolution().getScaledHeight() / 2.0F - 64.0F), -1);
/*  81 */         RectHelper.drawSmoothRectBetter((15 + mc.clickguismall.getStringWidth("cooldown")), e.getResolution().getScaledHeight() / 2.0F - 61.0F, 50.0F, 1.5F, (new Color(30, 30, 30, 70)).getRGB());
/*  82 */         RectHelper.drawSmoothRectBetter((15 + mc.clickguismall.getStringWidth("cooldown")), e.getResolution().getScaledHeight() / 2.0F - 61.0F, this.move, 1.5F, (new Color(123, 0, 255)).getRGB());
/*     */         
/*  84 */         float y = e.getResolution().getScaledHeight() / 2.0F - -7.0F;
/*     */         
/*  86 */         for (Feature feature : NeverHook.instance.featureManager.getFeatureList()) {
/*  87 */           if (feature.getBind() != 0 && !feature.getLabel().equals("ClickGui") && !feature.getLabel().equals("ClientFont")) {
/*  88 */             RectHelper.drawSmoothRectBetter(6.0F, y, 105.0F, 13.0F, (new Color(61, 58, 58)).getRGB());
/*  89 */             RectHelper.drawSmoothRectBetter(6.0F, e.getResolution().getScaledHeight() / 2.0F - 10.0F, 105.0F, 2.0F, (new Color(123, 0, 255)).getRGB());
/*  90 */             RectHelper.drawSmoothRectBetter(6.0F, e.getResolution().getScaledHeight() / 2.0F - 8.0F, 105.0F, 12.0F, (new Color(61, 58, 58)).getRGB());
/*  91 */             mc.clickguismall.drawStringWithShadow("keybinds", (10 + mc.clickguismall.getStringWidth("keybinds")), (e.getResolution().getScaledHeight() / 2.0F - 5.0F), -1);
/*  92 */             String toggled = feature.getState() ? " [toggled]" : " [disable]";
/*  93 */             mc.smallfontRenderer.drawStringWithShadow(feature.getLabel().toLowerCase(), 10.0D, (y + 4.0F), -1);
/*  94 */             mc.smallfontRenderer.drawStringWithShadow(toggled, 75.0D, (y + 4.0F), -1);
/*  95 */             y += 12.0F;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 100 */     if (indicatorMode.currentMode.equals("Skeet") && indicator.getBoolValue()) {
/* 101 */       RectHelper.drawSmoothRectBetter(6.0F, e.getResolution().getScaledHeight() / 2.0F - 80.0F, 105.0F, 26.0F, (new Color(61, 58, 58, 140)).getRGB());
/* 102 */       RectHelper.drawGradientRectBetter(5.5F, e.getResolution().getScaledHeight() / 2.0F - 97.0F, 106.0F, 2.0F, (new Color(255, 255, 255)).getRGB(), (new Color(255, 255, 255)).darker().darker().getRGB());
/* 103 */       RectHelper.drawSmoothRectBetter(6.0F, e.getResolution().getScaledHeight() / 2.0F - 95.0F, 105.0F, 12.0F, (new Color(61, 58, 58, 140)).getRGB());
/* 104 */       mc.clickguismall.drawStringWithShadow("flags", (32 + mc.clickguismall.getStringWidth("flags")), (e.getResolution().getScaledHeight() / 2.0F - 92.0F), -1);
/* 105 */       mc.clickguismall.drawStringWithShadow("cooldown", 10.0D, (e.getResolution().getScaledHeight() / 2.0F - 75.0F), -1);
/* 106 */       RectHelper.drawSmoothRectBetter((15 + mc.clickguismall.getStringWidth("cooldown")), e.getResolution().getScaledHeight() / 2.0F - 73.0F, 50.0F, 1.5F, (new Color(30, 30, 30, 70)).getRGB());
/* 107 */       RectHelper.drawGradientRectBetter((15 + mc.clickguismall.getStringWidth("cooldown")), e.getResolution().getScaledHeight() / 2.0F - 73.0F, this.cooledAttackStrength, 1.5F, (new Color(255, 255, 255)).getRGB(), (new Color(255, 255, 255)).darker().darker().getRGB());
/* 108 */       mc.clickguismall.drawStringWithShadow("move", 10.0D, (e.getResolution().getScaledHeight() / 2.0F - 64.0F), -1);
/* 109 */       RectHelper.drawSmoothRectBetter((15 + mc.clickguismall.getStringWidth("cooldown")), e.getResolution().getScaledHeight() / 2.0F - 61.0F, 50.0F, 1.5F, (new Color(30, 30, 30, 70)).getRGB());
/* 110 */       RectHelper.drawGradientRectBetter((15 + mc.clickguismall.getStringWidth("cooldown")), e.getResolution().getScaledHeight() / 2.0F - 61.0F, this.move, 1.5F, (new Color(255, 255, 255)).getRGB(), (new Color(255, 255, 255)).darker().darker().getRGB());
/*     */       
/* 112 */       float y = e.getResolution().getScaledHeight() / 2.0F - -7.0F;
/*     */       
/* 114 */       for (Feature feature : NeverHook.instance.featureManager.getFeatureList()) {
/* 115 */         if (feature.getBind() != 0 && !feature.getLabel().equals("ClickGui") && !feature.getLabel().equals("ClientFont")) {
/* 116 */           RectHelper.drawSmoothRectBetter(6.0F, y, 105.0F, 13.0F, (new Color(61, 58, 58, 140)).getRGB());
/* 117 */           RectHelper.drawGradientRectBetter(5.5F, e.getResolution().getScaledHeight() / 2.0F - 10.0F, 106.0F, 2.0F, (new Color(255, 255, 255)).getRGB(), (new Color(255, 255, 255)).darker().darker().getRGB());
/* 118 */           RectHelper.drawSmoothRectBetter(6.0F, e.getResolution().getScaledHeight() / 2.0F - 8.0F, 105.0F, 12.0F, (new Color(61, 58, 58, 140)).getRGB());
/* 119 */           mc.clickguismall.drawStringWithShadow("keybinds", (10 + mc.clickguismall.getStringWidth("keybinds")), (e.getResolution().getScaledHeight() / 2.0F - 5.0F), -1);
/* 120 */           String toggled = feature.getState() ? " [toggled]" : " [disable]";
/* 121 */           mc.smallfontRenderer.drawStringWithShadow(feature.getLabel().toLowerCase(), 10.0D, (y + 5.0F), -1);
/* 122 */           mc.smallfontRenderer.drawStringWithShadow(toggled, 75.0D, (y + 5.0F), -1);
/* 123 */           y += 12.0F;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onRender2D(EventRender2D e) {
/* 132 */     for (DraggableModule draggableModule : NeverHook.instance.draggableManager.getMods()) {
/* 133 */       if (getState() && !draggableModule.name.equals("LogoComponent")) {
/* 134 */         draggableModule.draw();
/*     */       }
/*     */     } 
/* 137 */     float target = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat) ? 15.0F : 0.0F;
/* 138 */     float delta = globalOffset - target;
/* 139 */     globalOffset -= delta / Math.max(1, Minecraft.getDebugFPS()) * 10.0F;
/* 140 */     if (!Double.isFinite(globalOffset)) {
/* 141 */       globalOffset = 0.0F;
/*     */     }
/* 143 */     if (globalOffset > 15.0F) {
/* 144 */       globalOffset = 15.0F;
/*     */     }
/* 146 */     if (globalOffset < 0.0F)
/* 147 */       globalOffset = 0.0F; 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\hud\HUD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */