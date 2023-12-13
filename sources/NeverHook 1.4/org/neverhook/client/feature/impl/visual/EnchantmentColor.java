/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ 
/*    */ public class EnchantmentColor
/*    */   extends Feature {
/*    */   public static ListSetting colorMode;
/*    */   public static ColorSetting customColor;
/*    */   
/*    */   public EnchantmentColor() {
/* 16 */     super("EnchantmentColor", "Изменяет цвет зачарований", Type.Visuals);
/* 17 */     colorMode = new ListSetting("Crumbs Color", "Rainbow", () -> Boolean.valueOf(true), new String[] { "Rainbow", "Client", "Custom" });
/* 18 */     customColor = new ColorSetting("Custom Enchantment", (new Color(16777215)).getRGB(), () -> Boolean.valueOf(colorMode.currentMode.equals("Custom")));
/* 19 */     addSettings(new Setting[] { (Setting)colorMode, (Setting)customColor });
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\EnchantmentColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */