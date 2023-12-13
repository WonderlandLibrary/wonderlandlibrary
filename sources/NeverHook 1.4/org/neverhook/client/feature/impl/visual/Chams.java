/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ 
/*    */ public class Chams
/*    */   extends Feature {
/*    */   public static ColorSetting colorChams;
/*    */   public static BooleanSetting clientColor;
/*    */   public static ListSetting chamsMode;
/*    */   
/*    */   public Chams() {
/* 18 */     super("Chams", "Подсвечивает игроков сквозь стены", Type.Visuals);
/* 19 */     chamsMode = new ListSetting("Chams Mode", "Fill", () -> Boolean.valueOf(true), new String[] { "Fill", "Outline", "Walls" });
/* 20 */     clientColor = new BooleanSetting("Client Colored", false, () -> Boolean.valueOf(!chamsMode.currentMode.equals("Walls")));
/* 21 */     colorChams = new ColorSetting("Chams Color", (new Color(16777215)).getRGB(), () -> Boolean.valueOf((!chamsMode.currentMode.equals("Walls") && !clientColor.getBoolValue())));
/* 22 */     addSettings(new Setting[] { (Setting)chamsMode, (Setting)colorChams, (Setting)clientColor });
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\Chams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */