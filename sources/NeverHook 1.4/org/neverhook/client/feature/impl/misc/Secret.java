/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketEntityAction;
/*    */ import net.minecraft.network.play.client.CPacketInput;
/*    */ import net.minecraft.network.play.client.CPacketKeepAlive;
/*    */ import org.apache.commons.lang3.RandomUtils;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*    */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ 
/*    */ public class Secret
/*    */   extends Feature
/*    */ {
/* 21 */   public long StopDecompilingClientFaggot = 0L;
/* 22 */   public BooleanSetting matrixDestruction = new BooleanSetting("Matrix Destruction", false, () -> Boolean.valueOf(true));
/*    */   
/*    */   public Secret() {
/* 25 */     super("Secret", "Test", Type.Misc);
/* 26 */     addSettings(new Setting[] { (Setting)this.matrixDestruction });
/*    */   }
/*    */   
/*    */   public void xyesos(double e) {
/* 30 */     for (int i = 0; i < i + 1; i++) {
/* 31 */       xyesos(e * Math.pow(Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), Math.pow(Math.pow(Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)), Math.pow(Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)))));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   @EventTarget
/*    */   public void onPreMotion(EventPreMotion event) {}
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventPreMotion event) {
/* 41 */     mc.player.connection.sendPacket((Packet)new CPacketKeepAlive(0L));
/* 42 */     if (mc.player.ticksExisted % 3 == 0) {
/* 43 */       mc.player.connection.sendPacket((Packet)new CPacketInput());
/*    */     }
/* 45 */     if (this.matrixDestruction.getBoolValue() && 
/* 46 */       mc.player.ticksExisted % 6 == 0) {
/* 47 */       mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   @EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 54 */     this.StopDecompilingClientFaggot = RandomUtils.nextLong(0L, 10L);
/* 55 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketKeepAlive)
/*    */       try {
/* 57 */         Thread.sleep(50L * this.StopDecompilingClientFaggot);
/* 58 */       } catch (InterruptedException e) {
/* 59 */         e.printStackTrace();
/*    */       }  
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onSendPacket(EventSendPacket event) {
/* 65 */     if (event.getPacket() instanceof net.minecraft.network.handshake.client.C00Handshake) {
/* 66 */       event.setCancelled(true);
/*    */     }
/* 68 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketClientSettings)
/* 69 */       event.setCancelled(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\Secret.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */