/*     */ package org.neverhook.client.feature.impl.combat;
/*     */ 
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.packet.EventAttackSilent;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.math.MathematicHelper;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ 
/*     */ public class Criticals
/*     */   extends Feature {
/*     */   private final BooleanSetting jump;
/*     */   private final ListSetting critMode;
/*  21 */   private final TimerHelper ncpTimer = new TimerHelper();
/*     */   
/*     */   public Criticals() {
/*  24 */     super("Criticals", "Автоматически наносит сущности критичиский урон при ударе", Type.Combat);
/*  25 */     this.critMode = new ListSetting("Criticals Mode", "Packet", () -> Boolean.valueOf(true), new String[] { "Packet", "WatchDog", "Ncp", "Matrix Packet 14", "Test" });
/*  26 */     this.jump = new BooleanSetting("Mini-Jump", false, () -> Boolean.valueOf(!this.critMode.currentMode.equals("Matrix Packet 14")));
/*  27 */     addSettings(new Setting[] { (Setting)this.critMode, (Setting)this.jump });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onAttackSilent(EventAttackSilent event) {
/*  32 */     String mode = this.critMode.getOptions();
/*  33 */     double x = mc.player.posX;
/*  34 */     double y = mc.player.posY;
/*  35 */     double z = mc.player.posZ;
/*  36 */     if (mode.equalsIgnoreCase("Packet")) {
/*  37 */       if (this.jump.getBoolValue()) {
/*  38 */         mc.player.setPosition(x, y + 0.04D, z);
/*     */       }
/*  40 */       sendPacket((Packet)new CPacketPlayer.Position(x, y + 0.11D, z, false));
/*  41 */       sendPacket((Packet)new CPacketPlayer.Position(x, y + 0.1100013579D, z, false));
/*  42 */       sendPacket((Packet)new CPacketPlayer.Position(x, y + 1.3579E-6D, z, false));
/*  43 */       mc.player.onCriticalHit(event.getTargetEntity());
/*  44 */     } else if (mode.equalsIgnoreCase("Matrix Packet 14")) {
/*  45 */       mc.player.onGround = false;
/*  46 */       double yMotion = 1.0E-12D;
/*  47 */       mc.player.fallDistance = (float)yMotion;
/*  48 */       mc.player.motionY = yMotion;
/*  49 */       mc.player.isCollidedVertically = true;
/*  50 */       sendPacket((Packet)new CPacketPlayer.Position(x, y + MathematicHelper.randomizeFloat(1.0E-8F, 4.0E-7F), z, false));
/*  51 */       sendPacket((Packet)new CPacketPlayer.Position(x, y + MathematicHelper.randomizeFloat(1.0E-8F, 2.0E-7F), z, false));
/*     */     }
/*  53 */     else if (mode.equalsIgnoreCase("Test")) {
/*     */       
/*  55 */       mc.player.onGround = false;
/*  56 */       double yMotion = 1.0E-12D;
/*  57 */       mc.player.fallDistance = (float)yMotion;
/*  58 */       mc.player.motionY = yMotion;
/*  59 */       mc.player.isCollidedVertically = true;
/*  60 */     } else if (mode.equalsIgnoreCase("Ncp")) {
/*  61 */       if (this.jump.getBoolValue()) {
/*  62 */         mc.player.setPosition(x, y + 0.04D, z);
/*     */       }
/*  64 */       if (timerHelper.hasReached(150.0F) && mc.player.onGround) {
/*  65 */         sendPacket((Packet)new CPacketPlayer.Position(x, y + 0.11D, z, false));
/*  66 */         sendPacket((Packet)new CPacketPlayer.Position(x, y + 0.1100013579D, z, false));
/*  67 */         sendPacket((Packet)new CPacketPlayer.Position(x, y + 1.3579E-6D, z, false));
/*  68 */         mc.player.onCriticalHit(event.getTargetEntity());
/*  69 */         timerHelper.reset();
/*     */       } 
/*  71 */     } else if (mode.equalsIgnoreCase("WatchDog")) {
/*  72 */       if (this.jump.getBoolValue()) {
/*  73 */         mc.player.setPosition(x, y + 0.04D, z);
/*     */       }
/*  75 */       double random = MathematicHelper.randomizeFloat(4.0E-7F, 4.0E-5F);
/*  76 */       if (timerHelper.hasReached(100.0F)) {
/*  77 */         for (double value : new double[] { 0.007017625D + random, 0.007349825D + random, 0.006102874D + random }) {
/*  78 */           mc.player.fallDistance = (float)value;
/*  79 */           mc.player.isCollidedVertically = true;
/*  80 */           mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY + value, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, false));
/*  81 */           timerHelper.reset();
/*     */         } 
/*     */       }
/*  84 */       mc.player.onCriticalHit(event.getTargetEntity());
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/*  90 */     String mode = this.critMode.getOptions();
/*  91 */     if (mode.equalsIgnoreCase("Test") && 
/*  92 */       mc.player.onGround) {
/*  93 */       mc.player.onGround = false;
/*  94 */       mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + MathematicHelper.randomizeFloat(0.2F, 0.2F), mc.player.posZ);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {
/* 101 */     String mode = this.critMode.getOptions();
/* 102 */     setSuffix(mode);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\Criticals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */