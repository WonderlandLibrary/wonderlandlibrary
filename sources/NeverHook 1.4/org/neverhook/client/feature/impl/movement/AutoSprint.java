/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.feature.impl.combat.KillAura;
/*    */ import org.neverhook.client.feature.impl.misc.TeleportExploit;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ 
/*    */ public class AutoSprint
/*    */   extends Feature {
/*    */   public AutoSprint() {
/* 15 */     super("AutoSprint", "Зажимает CTRL за вас, что бы быстро бежать", Type.Movement);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 20 */     if (!NeverHook.instance.featureManager.getFeatureByClass(TeleportExploit.class).getState() && (
/* 21 */       !NeverHook.instance.featureManager.getFeatureByClass(Scaffold.class).getState() || !Scaffold.sprintoff.getBoolValue()) && (
/* 22 */       !NeverHook.instance.featureManager.getFeatureByClass(KillAura.class).getState() || !KillAura.sprinting.getBoolValue() || KillAura.target == null))
/* 23 */       mc.player.setSprinting(MovementHelper.isMoving()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\AutoSprint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */