/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.feature.impl.combat.KillAura;
/*    */ import org.neverhook.client.feature.impl.combat.TargetStrafe;
/*    */ import org.neverhook.client.feature.impl.movement.FastClimb;
/*    */ import org.neverhook.client.feature.impl.movement.Flight;
/*    */ import org.neverhook.client.feature.impl.movement.LiquidWalk;
/*    */ import org.neverhook.client.feature.impl.movement.LongJump;
/*    */ import org.neverhook.client.feature.impl.movement.Speed;
/*    */ import org.neverhook.client.feature.impl.movement.Timer;
/*    */ import org.neverhook.client.ui.notification.NotificationManager;
/*    */ import org.neverhook.client.ui.notification.NotificationType;
/*    */ 
/*    */ public class FlagDetector extends Feature {
/*    */   public FlagDetector() {
/* 18 */     super("FlagDetector", "Автоматически выключает модуль при его детекте", Type.Misc);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 23 */     if (getState() && 
/* 24 */       event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerPosLook) {
/* 25 */       if (NeverHook.instance.featureManager.getFeatureByClass(Speed.class).getState()) {
/* 26 */         featureAlert("Speed");
/* 27 */         NeverHook.instance.featureManager.getFeatureByClass(Speed.class).state();
/* 28 */       } else if (NeverHook.instance.featureManager.getFeatureByClass(Flight.class).getState()) {
/* 29 */         featureAlert("Flight");
/* 30 */         NeverHook.instance.featureManager.getFeatureByClass(Flight.class).state();
/* 31 */       } else if (NeverHook.instance.featureManager.getFeatureByClass(FastClimb.class).getState() && mc.player.isOnLadder() && !mc.player.isUsingItem()) {
/* 32 */         featureAlert("FastClimb");
/* 33 */         NeverHook.instance.featureManager.getFeatureByClass(FastClimb.class).state();
/* 34 */       } else if (NeverHook.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() && KillAura.target != null) {
/* 35 */         featureAlert("TargetStrafe");
/* 36 */         NeverHook.instance.featureManager.getFeatureByClass(TargetStrafe.class).state();
/* 37 */       } else if (NeverHook.instance.featureManager.getFeatureByClass(LongJump.class).getState()) {
/* 38 */         featureAlert("LongJump");
/* 39 */         NeverHook.instance.featureManager.getFeatureByClass(LongJump.class).state();
/* 40 */       } else if (NeverHook.instance.featureManager.getFeatureByClass(LiquidWalk.class).getState() && mc.player.isInLiquid()) {
/* 41 */         featureAlert("LiquidWalk");
/* 42 */         NeverHook.instance.featureManager.getFeatureByClass(LiquidWalk.class).state();
/* 43 */       } else if (NeverHook.instance.featureManager.getFeatureByClass(Timer.class).getState()) {
/* 44 */         featureAlert("Timer");
/* 45 */         NeverHook.instance.featureManager.getFeatureByClass(Timer.class).state();
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void featureAlert(String feature) {
/* 52 */     NotificationManager.publicity(feature, "Disabling due to lag back", 3, NotificationType.WARNING);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\FlagDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */