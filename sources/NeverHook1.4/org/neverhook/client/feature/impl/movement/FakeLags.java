/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.Packet;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.TimerHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class FakeLags
/*    */   extends Feature
/*    */ {
/* 20 */   public final LinkedList<double[]> positions = (LinkedList)new LinkedList<>();
/* 21 */   public List<Packet<?>> packets = new ArrayList<>();
/* 22 */   public TimerHelper pulseTimer = new TimerHelper();
/* 23 */   public NumberSetting ticks = new NumberSetting("Ticks", 8.0F, 1.0F, 30.0F, 1.0F, () -> Boolean.valueOf(true));
/*    */   private boolean enableFakeLags;
/*    */   
/*    */   public FakeLags() {
/* 27 */     super("Fake Lags", "У других вы лагаете", Type.Movement);
/* 28 */     addSettings(new Setting[] { (Setting)this.ticks });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 33 */     synchronized (this.positions) {
/* 34 */       this.positions.add(new double[] { mc.player.posX, (mc.player.getEntityBoundingBox()).minY + (mc.player.getEyeHeight() / 2.0F), mc.player.posZ });
/* 35 */       this.positions.add(new double[] { mc.player.posX, (mc.player.getEntityBoundingBox()).minY, mc.player.posZ });
/*    */     } 
/* 37 */     super.onEnable();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 42 */     this.packets.clear();
/* 43 */     this.positions.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 49 */     synchronized (this.positions) {
/* 50 */       this.positions.add(new double[] { mc.player.posX, (mc.player.getEntityBoundingBox()).minY, mc.player.posZ });
/*    */     } 
/* 52 */     if (this.pulseTimer.hasReached(this.ticks.getNumberValue() * 50.0F)) {
/*    */       try {
/* 54 */         this.enableFakeLags = true;
/* 55 */         Iterator<Packet<?>> packetIterator = this.packets.iterator();
/* 56 */         while (packetIterator.hasNext()) {
/* 57 */           mc.player.connection.sendPacket(packetIterator.next());
/* 58 */           packetIterator.remove();
/*    */         } 
/* 60 */         this.enableFakeLags = false;
/* 61 */       } catch (Exception e) {
/* 62 */         this.enableFakeLags = false;
/*    */       } 
/* 64 */       synchronized (this.positions) {
/* 65 */         this.positions.clear();
/*    */       } 
/* 67 */       this.pulseTimer.reset();
/*    */     } 
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onSendPacket(EventSendPacket event) {
/* 73 */     if (mc.player == null || !(event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer) || this.enableFakeLags) {
/*    */       return;
/*    */     }
/* 76 */     event.setCancelled(true);
/* 77 */     if (!(event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer.Position) && !(event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer.PositionRotation)) {
/*    */       return;
/*    */     }
/* 80 */     this.packets.add(event.getPacket());
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\FakeLags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */