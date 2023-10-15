/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ 
/*    */ public class HitColor
/*    */   extends Feature {
/* 11 */   public static ColorSetting hitColor = new ColorSetting("Hit Color", Color.RED.getRGB(), () -> Boolean.valueOf(true));
/*    */   
/*    */   public HitColor() {
/* 14 */     super("HitColor", "Изменяет цвет удара", Type.Visuals);
/* 15 */     addSettings(new Setting[] { (Setting)hitColor });
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\HitColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */