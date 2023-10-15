/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ 
/*    */ public class WorldColor
/*    */   extends Feature {
/* 11 */   public static ColorSetting worldColor = new ColorSetting("World Color", Color.RED.getRGB(), () -> Boolean.valueOf(true));
/*    */   
/*    */   public WorldColor() {
/* 14 */     super("WorldColor", "Меняет цвет игры", Type.Visuals);
/* 15 */     addSettings(new Setting[] { (Setting)worldColor });
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\WorldColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */