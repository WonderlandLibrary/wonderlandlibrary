/*    */ package org.neverhook.client.feature.impl.hud;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class ClickGui
/*    */   extends Feature {
/*    */   public static BooleanSetting background;
/* 17 */   public static BooleanSetting blur = new BooleanSetting("Blur", true, () -> Boolean.valueOf(true));
/* 18 */   public static ListSetting clickGuiColor = new ListSetting("ClickGui Color", "Fade", () -> Boolean.valueOf(true), new String[] { "Astolfo", "Rainbow", "Static", "Color Two", "Client", "Fade" });
/*    */   public static ColorSetting color;
/*    */   public static ColorSetting colorTwo;
/* 21 */   public static NumberSetting speed = new NumberSetting("Speed", 35.0F, 10.0F, 100.0F, 1.0F, () -> Boolean.valueOf(true));
/* 22 */   public static NumberSetting scrollSpeed = new NumberSetting("ScrollSpeed", 20.0F, 1.0F, 100.0F, 1.0F, () -> Boolean.valueOf(true));
/* 23 */   public static BooleanSetting glow = new BooleanSetting("Glow Effect", false, () -> Boolean.valueOf(true));
/* 24 */   public static BooleanSetting shadow = new BooleanSetting("Shadow Effect", false, () -> Boolean.valueOf(true));
/* 25 */   public static BooleanSetting sliderGlow = new BooleanSetting("Slider Glow", false, () -> Boolean.valueOf(glow.getBoolValue()));
/* 26 */   public static BooleanSetting checkBoxGlow = new BooleanSetting("CheckBox Effect", false, () -> Boolean.valueOf(glow.getBoolValue()));
/* 27 */   public static BooleanSetting searchGlow = new BooleanSetting("Search Effect", false, () -> Boolean.valueOf(glow.getBoolValue()));
/* 28 */   public ListSetting mode = new ListSetting("ClickGui Mode", "New", () -> Boolean.valueOf(true), new String[] { "Old", "New" });
/*    */   
/*    */   public ClickGui() {
/* 31 */     super("ClickGui", "Открывает клик гуй чита", Type.Hud);
/* 32 */     setBind(54);
/* 33 */     color = new ColorSetting("Color One", (new Color(0, 21, 255, 120)).getRGB(), () -> Boolean.valueOf((clickGuiColor.currentMode.equals("Fade") || clickGuiColor.currentMode.equals("Color Two") || clickGuiColor.currentMode.equals("Static"))));
/* 34 */     colorTwo = new ColorSetting("Color Two", (new Color(132, 0, 255, 120)).getRGB(), () -> Boolean.valueOf(clickGuiColor.currentMode.equals("Color Two")));
/* 35 */     background = new BooleanSetting("Background", true, () -> Boolean.valueOf(true));
/* 36 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)scrollSpeed, (Setting)clickGuiColor, (Setting)color, (Setting)colorTwo, (Setting)speed, (Setting)background, (Setting)blur, (Setting)glow, (Setting)shadow, (Setting)sliderGlow, (Setting)checkBoxGlow, (Setting)searchGlow });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 41 */     if (this.mode.currentMode.equals("New")) {
/* 42 */       mc.displayGuiScreen((GuiScreen)NeverHook.instance.newClickGui);
/* 43 */     } else if (this.mode.currentMode.equals("Old")) {
/* 44 */       mc.displayGuiScreen((GuiScreen)NeverHook.instance.clickGui);
/*    */     } 
/* 46 */     NeverHook.instance.featureManager.getFeatureByClass(ClickGui.class).setState(false);
/* 47 */     super.onEnable();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\hud\ClickGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */