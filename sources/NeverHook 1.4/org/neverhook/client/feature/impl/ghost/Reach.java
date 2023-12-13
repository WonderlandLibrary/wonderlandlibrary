/*    */ package org.neverhook.client.feature.impl.ghost;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.math.MathematicHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class Reach extends Feature {
/*    */   public static NumberSetting reachValue;
/*    */   
/*    */   public Reach() {
/* 15 */     super("Reach", "Увеличивает дистанцию удара", Type.Ghost);
/* 16 */     reachValue = new NumberSetting("Expand", 3.2F, 3.0F, 5.0F, 0.1F, () -> Boolean.valueOf(true));
/* 17 */     addSettings(new Setting[] { (Setting)reachValue });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 22 */     setSuffix("" + MathematicHelper.round(reachValue.getNumberValue(), 1));
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\ghost\Reach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */