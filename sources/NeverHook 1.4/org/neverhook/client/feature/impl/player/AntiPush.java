/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ 
/*    */ public class AntiPush extends Feature {
/*    */   public static BooleanSetting water;
/*    */   public static BooleanSetting players;
/*    */   public static BooleanSetting blocks;
/*    */   
/*    */   public AntiPush() {
/* 14 */     super("AntiPush", "Убирает отталкивание от игроков, воды и блоков", Type.Player);
/* 15 */     players = new BooleanSetting("Entity", true, () -> Boolean.valueOf(true));
/* 16 */     water = new BooleanSetting("Liquid", true, () -> Boolean.valueOf(true));
/* 17 */     blocks = new BooleanSetting("Blocks", true, () -> Boolean.valueOf(true));
/* 18 */     addSettings(new Setting[] { (Setting)players, (Setting)water, (Setting)blocks });
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\AntiPush.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */