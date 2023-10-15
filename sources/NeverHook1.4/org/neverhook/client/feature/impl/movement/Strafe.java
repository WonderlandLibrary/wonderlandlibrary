/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class Strafe extends Feature {
/* 12 */   public NumberSetting speed = new NumberSetting("Strafe Speed", 0.1F, 0.1F, 1.0F, 0.01F, () -> Boolean.valueOf(true));
/*    */   
/*    */   public Strafe() {
/* 15 */     super("Strafe", "Ты можешь стрейфиться", Type.Movement);
/* 16 */     addSettings(new Setting[] { (Setting)this.speed });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPlayerTick(EventUpdate e) {
/* 21 */     if (!mc.gameSettings.keyBindForward.pressed) {
/*    */       return;
/*    */     }
/* 24 */     MovementHelper.strafePlayer(this.speed.getNumberValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\Strafe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */