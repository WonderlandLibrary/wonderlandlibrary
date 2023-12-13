/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class Timer extends Feature {
/*    */   public NumberSetting timer;
/*    */   
/*    */   public Timer() {
/* 14 */     super("Timer", "Увеличивает скорость игры", Type.Movement);
/* 15 */     this.timer = new NumberSetting("Timer", 2.0F, 0.1F, 10.0F, 0.1F, () -> Boolean.valueOf(true));
/* 16 */     addSettings(new Setting[] { (Setting)this.timer });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 21 */     if (!getState())
/*    */       return; 
/* 23 */     setSuffix("" + this.timer.getNumberValue());
/* 24 */     mc.timer.timerSpeed = this.timer.getNumberValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 29 */     super.onDisable();
/* 30 */     mc.timer.timerSpeed = 1.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */