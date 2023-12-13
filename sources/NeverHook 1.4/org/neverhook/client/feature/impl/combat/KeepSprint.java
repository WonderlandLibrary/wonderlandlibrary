/*    */ package org.neverhook.client.feature.impl.combat;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.math.MathematicHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class KeepSprint extends Feature {
/*    */   public static NumberSetting speed;
/*    */   public static BooleanSetting setSprinting;
/*    */   
/*    */   public KeepSprint() {
/* 17 */     super("KeepSprint", "Повзоляет редактировать скорость игрока при ударе", Type.Combat);
/* 18 */     speed = new NumberSetting("Keep Speed", 1.0F, 0.5F, 2.0F, 0.01F, () -> Boolean.valueOf(true));
/* 19 */     setSprinting = new BooleanSetting("Set Sprinting", true, () -> Boolean.valueOf(true));
/* 20 */     addSettings(new Setting[] { (Setting)setSprinting, (Setting)speed });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 25 */     setSuffix("" + MathematicHelper.round(speed.getNumberValue(), 2));
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\KeepSprint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */