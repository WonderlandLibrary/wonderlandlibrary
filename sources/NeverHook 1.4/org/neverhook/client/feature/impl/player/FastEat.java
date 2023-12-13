/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ 
/*    */ public class FastEat
/*    */   extends Feature {
/*    */   public FastEat() {
/* 18 */     super("FastEat", "Позволяет быстро использовать еду", Type.Player);
/* 19 */     this.modeFastEat = new ListSetting("FastEat Mode", "Matrix", () -> Boolean.valueOf(true), new String[] { "Matrix", "Vanilla" });
/* 20 */     addSettings(new Setting[] { (Setting)this.modeFastEat });
/*    */   }
/*    */   private final ListSetting modeFastEat;
/*    */   @EventTarget
/*    */   public void onUpdate(EventPreMotion event) {
/* 25 */     String mode = this.modeFastEat.getOptions();
/* 26 */     setSuffix(mode);
/* 27 */     if (mode.equalsIgnoreCase("Matrix")) {
/* 28 */       if (mc.player.getItemInUseMaxCount() >= 12 && (mc.player.isEating() || mc.player.isDrinking())) {
/* 29 */         for (int i = 0; i < 10; i++) {
/* 30 */           mc.player.connection.sendPacket((Packet)new CPacketPlayer(mc.player.onGround));
/*    */         }
/* 32 */         mc.player.stopActiveHand();
/*    */       } 
/* 34 */     } else if (mode.equalsIgnoreCase("Vanilla") && 
/* 35 */       mc.player.getItemInUseMaxCount() == 16 && (mc.player.isEating() || mc.player.isDrinking())) {
/* 36 */       for (int i = 0; i < 21; i++) {
/* 37 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer(true));
/*    */       }
/* 39 */       mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 46 */     mc.timer.timerSpeed = 1.0F;
/* 47 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\FastEat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */