/*     */ package org.neverhook.client.feature.impl.movement;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class HighJump extends Feature {
/*     */   public ListSetting mode;
/*  18 */   public BooleanSetting motionYBoost = new BooleanSetting("MotionY boost", false, () -> Boolean.valueOf(true)); public NumberSetting motionBoost;
/*     */   public boolean jump = false;
/*  20 */   public TimerHelper helper = new TimerHelper();
/*     */   
/*  22 */   private int ticks = 0;
/*     */   private boolean timerEnable = false;
/*     */   
/*     */   public HighJump() {
/*  26 */     super("HighJump", "Вы подлетаете на большую высоту", Type.Movement);
/*  27 */     this.mode = new ListSetting("HighJump Mode", "AntiCheatA", () -> Boolean.valueOf(true), new String[] { "AntiCheatA", "AntiCheatB", "Matrix", "Vanilla", "Matrix 6.3.0" });
/*  28 */     this.motionBoost = new NumberSetting("Motion Boost", 0.6F, 0.1F, 8.0F, 0.1F, () -> Boolean.valueOf(((this.mode.currentMode.equals("AntiCheatA") && this.motionYBoost.getBoolValue()) || this.mode.currentMode.equals("Vanilla"))));
/*  29 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.motionYBoost, (Setting)this.motionBoost });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreUpdate(EventUpdate event) {
/*  34 */     String highJumpMode = this.mode.currentMode;
/*  35 */     setSuffix(highJumpMode);
/*  36 */     if (!getState()) {
/*     */       return;
/*     */     }
/*  39 */     if (highJumpMode.equals("Matrix")) {
/*  40 */       if (mc.gameSettings.keyBindJump.pressed) {
/*  41 */         mc.player.motionY += 0.42D;
/*     */       }
/*  43 */     } else if (highJumpMode.equalsIgnoreCase("AntiCheatB")) {
/*  44 */       if (mc.player.hurtTime > 0) {
/*  45 */         mc.player.motionY += 1.3D;
/*     */       }
/*  47 */     } else if (highJumpMode.equals("Matrix 6.3.0")) {
/*  48 */       if (this.timerEnable) {
/*  49 */         mc.timer.timerSpeed = 1.0F;
/*  50 */         this.timerEnable = false;
/*     */       } 
/*  52 */       if ((!mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(0.0D, mc.player.motionY, 0.0D).expand(0.0D, 0.0D, 0.0D)).isEmpty() || !mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(0.0D, -4.0D, 0.0D).expand(0.0D, 0.0D, 0.0D)).isEmpty()) && mc.player.fallDistance > 10.0F && 
/*  53 */         !mc.player.onGround) {
/*  54 */         mc.timer.timerSpeed = 0.1F;
/*  55 */         this.timerEnable = true;
/*     */       } 
/*     */       
/*  58 */       if (this.helper.hasReached(1000.0F) && this.ticks == 1) {
/*  59 */         mc.timer.timerSpeed = 1.0F;
/*  60 */         mc.player.motionX = 0.0D;
/*  61 */         mc.player.motionZ = 0.0D;
/*  62 */         this.ticks = 0;
/*     */       } 
/*  64 */       if (this.ticks == 1 && mc.player.hurtTime > 0) {
/*  65 */         mc.timer.timerSpeed = 1.0F;
/*  66 */         mc.player.motionY = 9.0D;
/*  67 */         mc.player.motionX = 0.0D;
/*  68 */         mc.player.motionZ = 0.0D;
/*  69 */         this.ticks = 0;
/*     */       } 
/*  71 */       if (this.ticks == 2) {
/*  72 */         for (int i = 0; i < 9; i++) {
/*  73 */           mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
/*  74 */           mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.399D, mc.player.posZ, false));
/*     */         } 
/*  76 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
/*  77 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
/*  78 */         mc.timer.timerSpeed = 0.6F;
/*  79 */         this.ticks = 1;
/*  80 */         this.helper.reset();
/*     */       } 
/*  82 */       if (mc.player.isCollidedHorizontally && this.ticks == 0 && mc.player.onGround) {
/*  83 */         this.ticks = 2;
/*  84 */         mc.timer.timerSpeed = 0.05F;
/*     */       } 
/*  86 */       if (mc.player.isCollidedHorizontally && mc.player.onGround) {
/*  87 */         mc.player.motionX = 0.0D;
/*  88 */         mc.player.motionZ = 0.0D;
/*  89 */         mc.player.onGround = false;
/*     */       } 
/*  91 */     } else if (highJumpMode.equalsIgnoreCase("AntiCheatA")) {
/*  92 */       if (mc.player.hurtTime > 0) {
/*  93 */         mc.player.motionY += this.motionBoost.getNumberValue();
/*     */       }
/*  95 */     } else if (highJumpMode.equalsIgnoreCase("Vanilla") && !this.jump) {
/*  96 */       this.jump = true;
/*  97 */       if (mc.gameSettings.keyBindJump.pressed) {
/*  98 */         mc.player.motionY += this.motionBoost.getNumberValue();
/*     */       }
/*     */     } 
/* 101 */     if (mc.player.onGround) {
/* 102 */       this.jump = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 108 */     mc.timer.timerSpeed = 1.0F;
/* 109 */     this.ticks = 0;
/* 110 */     this.timerEnable = false;
/* 111 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\HighJump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */