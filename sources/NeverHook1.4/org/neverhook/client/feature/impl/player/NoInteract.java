/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ 
/*    */ public class NoInteract extends Feature {
/*    */   public static BooleanSetting armorStands;
/* 10 */   public static BooleanSetting craftTable = new BooleanSetting("Craft Table", true, () -> Boolean.valueOf(true));
/* 11 */   public static BooleanSetting standing = new BooleanSetting("Standing Sign", true, () -> Boolean.valueOf(true));
/* 12 */   public static BooleanSetting door = new BooleanSetting("Door", true, () -> Boolean.valueOf(true));
/* 13 */   public static BooleanSetting hopper = new BooleanSetting("Hopper", true, () -> Boolean.valueOf(true));
/* 14 */   public static BooleanSetting furnace = new BooleanSetting("Furnace", true, () -> Boolean.valueOf(true));
/* 15 */   public static BooleanSetting dispenser = new BooleanSetting("Dispenser", true, () -> Boolean.valueOf(true));
/* 16 */   public static BooleanSetting anvil = new BooleanSetting("Furnace", true, () -> Boolean.valueOf(true));
/* 17 */   public static BooleanSetting woodenslab = new BooleanSetting("Wooden Slab", true, () -> Boolean.valueOf(true));
/* 18 */   public static BooleanSetting lever = new BooleanSetting("Lever", true, () -> Boolean.valueOf(true));
/*    */   
/*    */   public NoInteract() {
/* 21 */     super("NoInteract", "Позволяет не нажимать ПКМ по верстакам, печкам и т.д", Type.Player);
/* 22 */     armorStands = new BooleanSetting("Armor Stand", true, () -> Boolean.valueOf(true));
/* 23 */     addSettings(new Setting[] { (Setting)armorStands, (Setting)craftTable, (Setting)standing, (Setting)door, (Setting)hopper, (Setting)furnace, (Setting)dispenser, (Setting)anvil, (Setting)woodenslab, (Setting)lever });
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\NoInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */