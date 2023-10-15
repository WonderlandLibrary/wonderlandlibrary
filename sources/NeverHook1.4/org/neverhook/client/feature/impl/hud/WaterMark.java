/*    */ package org.neverhook.client.feature.impl.hud;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.render.EventRender2D;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.ui.components.draggable.DraggableModule;
/*    */ 
/*    */ public class WaterMark
/*    */   extends Feature {
/*    */   public static ListSetting logoMode;
/*    */   public static ListSetting logoColor;
/* 19 */   public static ListSetting colorRectPosition = new ListSetting("ColorRect Position", "Top", () -> Boolean.valueOf((!logoMode.currentMode.equals("Skeet") && !logoMode.currentMode.equals("NeverLose") && !logoMode.currentMode.equals("Default"))), new String[] { "Bottom", "Top" });
/*    */   
/*    */   public static ColorSetting customRect;
/*    */   public static ColorSetting customRectTwo;
/* 23 */   public static BooleanSetting glowEffect = new BooleanSetting("Glow Effect", false, () -> Boolean.valueOf((logoMode.currentMode.equals("OneTap v3") || logoMode.currentMode.equals("OneTap v2"))));
/* 24 */   public static BooleanSetting shadowEffect = new BooleanSetting("Shadow Effect", false, () -> Boolean.valueOf((logoMode.currentMode.equals("NeverLose") || logoMode.currentMode.equals("OneTap v3") || logoMode.currentMode.equals("OneTap v2"))));
/*    */   
/*    */   public WaterMark() {
/* 27 */     super("WaterMark", "Ватермарк чита", Type.Hud);
/* 28 */     logoMode = new ListSetting("Logo Mode", "Default", () -> Boolean.valueOf(true), new String[] { "Default", "Skeet", "OneTap v2", "OneTap v3", "NeverLose", "NoRender" });
/* 29 */     logoColor = new ListSetting("Logo Color", "Default", () -> Boolean.valueOf(!logoMode.currentMode.equals("NeverLose")), new String[] { "Client", "Rainbow", "Gradient", "Static", "Default" });
/* 30 */     customRect = new ColorSetting("Custom Rect Color", Color.PINK.getRGB(), () -> Boolean.valueOf(!logoMode.currentMode.equals("NeverLose")));
/* 31 */     customRectTwo = new ColorSetting("Custom Rect Color Two", Color.BLUE.getRGB(), () -> Boolean.valueOf((!logoMode.currentMode.equals("NeverLose") && !logoMode.currentMode.equals("Default"))));
/* 32 */     addSettings(new Setting[] { (Setting)logoMode, (Setting)colorRectPosition, (Setting)logoColor, (Setting)glowEffect, (Setting)shadowEffect, (Setting)customRect, (Setting)customRectTwo });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender2D(EventRender2D event) {
/* 37 */     for (DraggableModule draggableModule : NeverHook.instance.draggableManager.getMods()) {
/* 38 */       if (getState() && !draggableModule.name.equals("ClientInfoComponent") && !draggableModule.name.equals("InfoComponent") && !draggableModule.name.equals("PotionComponent") && !draggableModule.name.equals("ArmorComponent") && !draggableModule.name.equals("TargetHUDComponent"))
/* 39 */         draggableModule.draw(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\hud\WaterMark.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */