/*     */ package org.neverhook.client.feature.impl.misc;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.server.SPacketPlayerPosLook;
/*     */ import org.apache.commons.lang3.RandomUtils;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*     */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoBypass
/*     */   extends Feature
/*     */ {
/*     */   public static NumberSetting delay;
/*  29 */   private final List<Packet<?>> packets = new CopyOnWriteArrayList<>();
/*  30 */   private final TimerHelper timerHelper = new TimerHelper();
/*  31 */   public long StopDecompilingClientFaggot = 0L;
/*  32 */   public ListSetting mode = new ListSetting("Disabler Mode", "PingFreeze", () -> Boolean.valueOf(true), new String[] { "PingFreeze", "Destruction", "LimitNet" });
/*     */   
/*     */   public AutoBypass() {
/*  35 */     super("Auto Bypass", "Отключает некоторые чеки анти читов", Type.Misc);
/*  36 */     delay = new NumberSetting("PingFreezeDelay", 1000.0F, 0.0F, 3000.0F, 0.1F, () -> Boolean.valueOf(this.mode.currentMode.equals("PingFreeze")), NumberSetting.NumberType.MS);
/*  37 */     addSettings(new Setting[] { (Setting)delay });
/*  38 */     addSettings(new Setting[] { (Setting)this.mode });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventPreMotion event) {
/*  43 */     setSuffix(this.mode.currentMode);
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onReceivePacket(EventReceivePacket event) {
/*  48 */     if (this.mode.currentMode.equals("Matrix")) {
/*  49 */       if (event.getPacket() instanceof SPacketPlayerPosLook) {
/*  50 */         SPacketPlayerPosLook posLook = new SPacketPlayerPosLook();
/*  51 */         posLook.yaw = 90.0F;
/*     */       } 
/*  53 */     } else if (this.mode.currentMode.equals("PingFreeze")) {
/*  54 */       if (mc.player.ticksExisted % 100 != 0) {
/*  55 */         if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketKeepAlive) {
/*  56 */           event.setCancelled(true);
/*     */         }
/*  58 */         if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCustomPayload) {
/*  59 */           event.setCancelled(true);
/*     */         }
/*  61 */       } else if (this.mode.currentMode.equals("LimitNet")) {
/*  62 */         this.StopDecompilingClientFaggot = RandomUtils.nextLong(0L, 10L);
/*  63 */         if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketKeepAlive) {
/*     */           try {
/*  65 */             Thread.sleep(150L * this.StopDecompilingClientFaggot);
/*  66 */           } catch (Exception exception) {}
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onSendPacket(EventSendPacket event) {
/*  75 */     if (this.timerHelper.hasReached(delay.getNumberValue()) && this.mode.currentMode.equals("PingFreeze") && 
/*  76 */       event.getPacket() instanceof CPacketPlayer) {
/*  77 */       CPacketPlayer cPacketPlayer = (CPacketPlayer)event.getPacket();
/*  78 */       cPacketPlayer.x = mc.player.posX;
/*  79 */       cPacketPlayer.y = mc.player.posY;
/*  80 */       cPacketPlayer.z = mc.player.posZ;
/*  81 */       mc.player.posX = mc.player.prevPosX;
/*  82 */       mc.player.posY = mc.player.prevPosY;
/*  83 */       mc.player.posZ = mc.player.prevPosZ;
/*  84 */       this.timerHelper.reset();
/*  85 */       if (mc.player.ticksExisted % 100 != 0) {
/*  86 */         if (event.getPacket() instanceof net.minecraft.network.handshake.client.C00Handshake) {
/*  87 */           event.setCancelled(true);
/*     */         }
/*  89 */         if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketClientSettings) {
/*  90 */           event.setCancelled(true);
/*     */         }
/*  92 */       } else if (this.mode.currentMode.equals("Destruction")) {
/*  93 */         if (event.getPacket() instanceof net.minecraft.network.handshake.client.C00Handshake) {
/*  94 */           event.setCancelled(true);
/*     */         }
/*  96 */         if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketKeepAlive) {
/*  97 */           event.setCancelled(true);
/*     */         }
/*  99 */         if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketClientSettings)
/* 100 */           event.setCancelled(true); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\AutoBypass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */