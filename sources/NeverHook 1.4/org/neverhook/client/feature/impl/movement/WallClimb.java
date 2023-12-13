/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.TimerHelper;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class WallClimb extends Feature {
/*    */   public static NumberSetting climbTicks;
/* 14 */   private final TimerHelper timerHelper = new TimerHelper();
/*    */   
/*    */   public WallClimb() {
/* 17 */     super("WallClimb", "Автоматически взберается на стены", Type.Movement);
/* 18 */     climbTicks = new NumberSetting("Climb Ticks", 1.0F, 0.0F, 5.0F, 0.1F, () -> Boolean.valueOf(true));
/* 19 */     addSettings(new Setting[] { (Setting)climbTicks });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPreMotion(EventPreMotion event) {
/* 24 */     setSuffix("" + climbTicks.getNumberValue());
/* 25 */     if (MovementHelper.isMoving() && mc.player.isCollidedHorizontally && 
/* 26 */       this.timerHelper.hasReached(climbTicks.getNumberValue() * 100.0F)) {
/* 27 */       event.setOnGround(true);
/* 28 */       mc.player.onGround = true;
/* 29 */       mc.player.isCollidedVertically = true;
/* 30 */       mc.player.isCollidedHorizontally = true;
/* 31 */       mc.player.isAirBorne = true;
/* 32 */       mc.player.jump();
/* 33 */       this.timerHelper.reset();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\WallClimb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */