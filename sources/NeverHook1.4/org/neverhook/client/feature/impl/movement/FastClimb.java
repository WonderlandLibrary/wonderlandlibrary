/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class FastClimb extends Feature {
/*    */   public static ListSetting ladderMode;
/*    */   public static NumberSetting ladderSpeed;
/*    */   
/*    */   public FastClimb() {
/* 17 */     super("FastClimb", "Позволяет быстро забираться по лестницам и лианам", Type.Movement);
/* 18 */     ladderMode = new ListSetting("FastClimb Mode", "Matrix", () -> Boolean.valueOf(true), new String[] { "Matrix", "Vanilla" });
/* 19 */     ladderSpeed = new NumberSetting("Ladder Speed", 0.5F, 0.1F, 2.0F, 0.1F, () -> Boolean.valueOf(ladderMode.currentMode.equals("Vanilla")));
/* 20 */     addSettings(new Setting[] { (Setting)ladderMode });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPreMotion(EventPreMotion event) {
/* 25 */     setSuffix(ladderMode.getCurrentMode());
/* 26 */     if (mc.player == null || mc.world == null)
/*    */       return; 
/* 28 */     switch (ladderMode.getOptions()) {
/*    */       case "Matrix":
/* 30 */         if (mc.player.isOnLadder() && mc.player.isCollidedHorizontally && MovementHelper.isMoving()) {
/* 31 */           mc.player.motionY += 0.31200000643730164D;
/* 32 */           event.setOnGround(true);
/*    */         } 
/*    */         break;
/*    */       case "Vanilla":
/* 36 */         if (mc.player.isOnLadder() && mc.player.isCollidedHorizontally && MovementHelper.isMoving())
/* 37 */           mc.player.motionY += ladderSpeed.getNumberValue(); 
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\FastClimb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */