/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class NoSlowDown extends Feature {
/*    */   public static NumberSetting percentage;
/*    */   public static BooleanSetting soulSand;
/*    */   public static BooleanSetting slimeBlock;
/*    */   public static ListSetting noSlowMode;
/*    */   
/*    */   public NoSlowDown() {
/* 22 */     super("NoSlowDown", "Убирает замедление при использовании еды и других предметов", Type.Movement);
/* 23 */     percentage = new NumberSetting("Percentage", 100.0F, 0.0F, 100.0F, 1.0F, () -> Boolean.valueOf(true));
/* 24 */     noSlowMode = new ListSetting("NoSlow Mode", "Vanilla", () -> Boolean.valueOf(true), new String[] { "Vanilla", "Matrix" });
/* 25 */     soulSand = new BooleanSetting("Soul Sand", false, () -> Boolean.valueOf(true));
/* 26 */     slimeBlock = new BooleanSetting("Slime", true, () -> Boolean.valueOf(true));
/* 27 */     addSettings(new Setting[] { (Setting)noSlowMode, (Setting)percentage, (Setting)soulSand, (Setting)slimeBlock });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 32 */     setSuffix(percentage.getNumberValue() + "% " + noSlowMode.getCurrentMode());
/* 33 */     if (noSlowMode.currentMode.equals("Matrix") && 
/* 34 */       mc.player.isUsingItem() && MovementHelper.isMoving() && mc.player.fallDistance > 0.7D) {
/* 35 */       mc.player.motionX *= 0.9700000286102295D;
/* 36 */       mc.player.motionZ *= 0.9700000286102295D;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @EventTarget
/*    */   public void onSendPacket(EventSendPacket event) {
/* 43 */     if (noSlowMode.currentMode.equals("Matrix") && 
/* 44 */       event.getPacket() instanceof CPacketPlayer) {
/* 45 */       CPacketPlayer cPacketPlayer = (CPacketPlayer)event.getPacket();
/* 46 */       if (mc.player.isUsingItem() && MovementHelper.isMoving() && !mc.gameSettings.keyBindJump.pressed) {
/* 47 */         cPacketPlayer.y = (mc.player.ticksExisted % 2 == 0) ? (cPacketPlayer.y + 6.0E-4D) : (cPacketPlayer.y + 2.0E-4D);
/* 48 */         cPacketPlayer.onGround = false;
/* 49 */         mc.player.onGround = false;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\NoSlowDown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */