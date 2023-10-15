/*    */ package org.neverhook.client.feature.impl.hud;
/*    */ 
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ 
/*    */ public class Notifications extends Feature {
/*    */   public static BooleanSetting state;
/*    */   
/*    */   public Notifications() {
/* 12 */     super("Notifications", "Показывает необходимую информацию о модулях", Type.Hud);
/* 13 */     state = new BooleanSetting("Module State", true, () -> Boolean.valueOf(true));
/* 14 */     addSettings(new Setting[] { (Setting)state });
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\hud\Notifications.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */