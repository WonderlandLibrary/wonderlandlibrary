/*     */ package org.neverhook.client.feature.impl.movement;
/*     */ 
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.player.MovementHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class Flight extends Feature {
/*     */   public static ListSetting flyMode;
/*     */   
/*     */   public Flight() {
/*  20 */     super("Flight", "Позволяет вам летать без креатив режима", Type.Movement);
/*  21 */     flyMode = new ListSetting("Flight Mode", "Vanilla", () -> Boolean.valueOf(true), new String[] { "Vanilla", "MineLand", "Matrix 6.3.0", "WellMore", "Matrix Boost 6.2.2", "Matrix Glide", "Matrix Web" });
/*  22 */     speed = new NumberSetting("Flight Speed", 5.0F, 0.1F, 15.0F, 0.1F, () -> Boolean.valueOf((flyMode.currentMode.equals("Vanilla") || flyMode.currentMode.equals("WellMore"))));
/*     */     
/*  24 */     addSettings(new Setting[] { (Setting)flyMode, (Setting)speed });
/*     */   }
/*     */   public static NumberSetting speed;
/*     */   
/*     */   public void onDisable() {
/*  29 */     super.onDisable();
/*  30 */     mc.player.speedInAir = 0.02F;
/*  31 */     mc.timer.timerSpeed = 1.0F;
/*  32 */     mc.player.capabilities.isFlying = false;
/*  33 */     if (flyMode.getOptions().equalsIgnoreCase("WellMore")) {
/*  34 */       mc.player.motionY = 0.0D;
/*  35 */       mc.player.motionX = 0.0D;
/*  36 */       mc.player.motionZ = 0.0D;
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate eventUpdate) {
/*  42 */     String mode = flyMode.getCurrentMode();
/*  43 */     if (mode.equalsIgnoreCase("Matrix Glide")) {
/*  44 */       if (!mc.player.onGround && mc.player.fallDistance >= 1.0F) {
/*  45 */         mc.timer.timerSpeed = (mc.player.ticksExisted % 4 == 0) ? 0.08F : 0.5F;
/*  46 */         mc.player.motionY *= 0.007D;
/*  47 */         mc.player.fallDistance = 0.0F;
/*     */       } else {
/*  49 */         mc.timer.timerSpeed = 1.0F;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/*  56 */     String mode = flyMode.getCurrentMode();
/*     */     
/*  58 */     setSuffix(flyMode.getCurrentMode());
/*     */     
/*  60 */     if (mode.equalsIgnoreCase("Matrix 6.3.0")) {
/*  61 */       double yMotion = 1.0E-12D;
/*  62 */       mc.player.fallDistance = (float)yMotion;
/*  63 */       mc.player.motionY = 0.4D;
/*  64 */       mc.player.onGround = false;
/*  65 */       double f = Math.toRadians(mc.player.rotationYaw);
/*  66 */       mc.player.motionX -= (MathHelper.sin((float)f) * 0.35F);
/*  67 */       mc.player.motionZ += (MathHelper.cos((float)f) * 0.35F);
/*  68 */       if (mc.gameSettings.keyBindSneak.isKeyDown()) {
/*  69 */         mc.player.motionY -= speed.getNumberValue();
/*  70 */       } else if (mc.gameSettings.keyBindJump.isKeyDown()) {
/*  71 */         mc.player.motionY += speed.getNumberValue();
/*     */       } 
/*  73 */     } else if (mode.equals("MineLand")) {
/*  74 */       if (mc.gameSettings.keyBindForward.isKeyDown()) {
/*  75 */         float yaw = (float)Math.toRadians(mc.player.rotationYaw);
/*  76 */         double x = -MathHelper.sin(yaw) * 0.25D;
/*  77 */         double z = MathHelper.cos(yaw) * 0.25D;
/*  78 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY, mc.player.posZ + z, false));
/*  79 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY - 490.0D, mc.player.posZ + z, true));
/*  80 */         mc.player.motionY = 0.0D;
/*     */       } 
/*  82 */     } else if (mode.equalsIgnoreCase("Wellmore")) {
/*  83 */       if (mc.player.onGround) {
/*  84 */         mc.player.jump();
/*     */       } else {
/*  86 */         mc.player.motionX = 0.0D;
/*  87 */         mc.player.motionZ = 0.0D;
/*  88 */         mc.player.motionY = -0.01D;
/*  89 */         MovementHelper.setSpeed(speed.getNumberValue());
/*  90 */         mc.player.speedInAir = 0.3F;
/*  91 */         if (mc.gameSettings.keyBindSneak.isKeyDown()) {
/*  92 */           mc.player.motionY -= speed.getNumberValue();
/*  93 */         } else if (mc.gameSettings.keyBindJump.isKeyDown()) {
/*  94 */           mc.player.motionY += speed.getNumberValue();
/*     */         } 
/*     */       } 
/*  97 */     } else if (mode.equalsIgnoreCase("Matrix Boost 6.2.2")) {
/*  98 */       mc.player.motionY = 0.6D;
/*  99 */       if (mc.gameSettings.keyBindForward.pressed && !mc.player.onGround) {
/* 100 */         double f = Math.toRadians(mc.player.rotationYaw);
/* 101 */         mc.player.motionX -= (MathHelper.sin((float)f) * 0.27F);
/* 102 */         mc.player.motionZ += (MathHelper.cos((float)f) * 0.27F);
/*     */       } 
/* 104 */     } else if (mode.equalsIgnoreCase("Vanilla")) {
/* 105 */       mc.player.capabilities.isFlying = true;
/* 106 */       MovementHelper.setSpeed(speed.getNumberValue());
/* 107 */       if (mc.gameSettings.keyBindSneak.isKeyDown()) {
/* 108 */         mc.player.motionY -= speed.getNumberValue();
/* 109 */       } else if (mc.gameSettings.keyBindJump.isKeyDown()) {
/* 110 */         mc.player.motionY += speed.getNumberValue();
/*     */       }
/*     */     
/* 113 */     } else if (mode.equalsIgnoreCase("Matrix Web") && 
/* 114 */       mc.player.isInWeb) {
/* 115 */       mc.player.isInWeb = false;
/* 116 */       mc.player.motionY *= (mc.player.ticksExisted % 2 == 0) ? -100.0D : -0.05D;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\Flight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */