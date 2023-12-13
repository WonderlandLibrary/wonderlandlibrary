/*     */ package org.neverhook.client.helpers.player;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import org.neverhook.client.event.events.impl.motion.EventMove;
/*     */ import org.neverhook.client.event.events.impl.motion.EventStrafe;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ 
/*     */ public class MovementHelper implements Helper {
/*     */   public static boolean isMoving() {
/*  13 */     return (mc.player.movementInput.moveForward != 0.0F || mc.player.movementInput.moveStrafe != 0.0F);
/*     */   }
/*     */   
/*     */   public static float getBaseMoveSpeed() {
/*  17 */     float baseSpeed = 0.2873F;
/*  18 */     if (mc.player != null && mc.player.isPotionActive(MobEffects.SPEED)) {
/*  19 */       int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
/*  20 */       baseSpeed = (float)(baseSpeed * (1.0D + 0.2D * (amplifier + 1)));
/*     */     } 
/*  22 */     return baseSpeed;
/*     */   }
/*     */   
/*     */   public static int getSpeedEffect() {
/*  26 */     if (mc.player.isPotionActive(MobEffects.SPEED)) {
/*  27 */       return mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier() + 1;
/*     */     }
/*  29 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getMoveDirection() {
/*  34 */     double motionX = mc.player.motionX;
/*  35 */     double motionZ = mc.player.motionZ;
/*  36 */     float direction = (float)(Math.atan2(motionX, motionZ) / Math.PI * 180.0D);
/*  37 */     return -direction;
/*     */   }
/*     */   
/*     */   public static boolean isBlockAboveHead() {
/*  41 */     AxisAlignedBB axisAlignedBB = new AxisAlignedBB(mc.player.posX - 0.3D, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ + 0.3D, mc.player.posX + 0.3D, mc.player.posY + (!mc.player.onGround ? 1.5D : 2.5D), mc.player.posZ - 0.3D);
/*  42 */     return mc.world.getCollisionBoxes((Entity)mc.player, axisAlignedBB).isEmpty();
/*     */   }
/*     */   
/*     */   public static void calculateSilentMove(EventStrafe event, float yaw) {
/*  46 */     float strafe = event.getStrafe();
/*  47 */     float forward = event.getForward();
/*  48 */     float friction = event.getFriction();
/*  49 */     int difference = (int)((MathHelper.wrapDegrees(mc.player.rotationYaw - yaw - 23.5F - 135.0F) + 180.0F) / 45.0F);
/*  50 */     float calcForward = 0.0F;
/*  51 */     float calcStrafe = 0.0F;
/*  52 */     switch (difference) {
/*     */       case 0:
/*  54 */         calcForward = forward;
/*  55 */         calcStrafe = strafe;
/*     */         break;
/*     */       case 1:
/*  58 */         calcForward += forward;
/*  59 */         calcStrafe -= forward;
/*  60 */         calcForward += strafe;
/*  61 */         calcStrafe += strafe;
/*     */         break;
/*     */       case 2:
/*  64 */         calcForward = strafe;
/*  65 */         calcStrafe = -forward;
/*     */         break;
/*     */       case 3:
/*  68 */         calcForward -= forward;
/*  69 */         calcStrafe -= forward;
/*  70 */         calcForward += strafe;
/*  71 */         calcStrafe -= strafe;
/*     */         break;
/*     */       case 4:
/*  74 */         calcForward = -forward;
/*  75 */         calcStrafe = -strafe;
/*     */         break;
/*     */       case 5:
/*  78 */         calcForward -= forward;
/*  79 */         calcStrafe += forward;
/*  80 */         calcForward -= strafe;
/*  81 */         calcStrafe -= strafe;
/*     */         break;
/*     */       case 6:
/*  84 */         calcForward = -strafe;
/*  85 */         calcStrafe = forward;
/*     */         break;
/*     */       case 7:
/*  88 */         calcForward += forward;
/*  89 */         calcStrafe += forward;
/*  90 */         calcForward -= strafe;
/*  91 */         calcStrafe += strafe;
/*     */         break;
/*     */     } 
/*  94 */     if (calcForward > 1.0F || (calcForward < 0.9F && calcForward > 0.3F) || calcForward < -1.0F || (calcForward > -0.9F && calcForward < -0.3F)) {
/*  95 */       calcForward *= 0.5F;
/*     */     }
/*  97 */     if (calcStrafe > 1.0F || (calcStrafe < 0.9F && calcStrafe > 0.3F) || calcStrafe < -1.0F || (calcStrafe > -0.9F && calcStrafe < -0.3F)) {
/*  98 */       calcStrafe *= 0.5F;
/*     */     }
/* 100 */     float dist = calcStrafe * calcStrafe + calcForward * calcForward;
/* 101 */     if (dist >= 1.0E-4F) {
/* 102 */       dist = (float)(friction / Math.max(1.0D, Math.sqrt(dist)));
/* 103 */       calcStrafe *= dist;
/* 104 */       calcForward *= dist;
/* 105 */       float yawSin = MathHelper.sin((float)(yaw * Math.PI / 180.0D));
/* 106 */       float yawCos = MathHelper.cos((float)(yaw * Math.PI / 180.0D));
/* 107 */       mc.player.motionX += (calcStrafe * yawCos - calcForward * yawSin);
/* 108 */       mc.player.motionZ += (calcForward * yawCos + calcStrafe * yawSin);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setEventSpeed(EventMove event, double speed) {
/* 113 */     double forward = mc.player.movementInput.moveForward;
/* 114 */     double strafe = mc.player.movementInput.moveStrafe;
/* 115 */     float yaw = mc.player.rotationYaw;
/* 116 */     if (forward == 0.0D && strafe == 0.0D) {
/* 117 */       event.setX(0.0D);
/* 118 */       event.setZ(0.0D);
/*     */     } else {
/* 120 */       if (forward != 0.0D) {
/* 121 */         if (strafe > 0.0D) {
/* 122 */           yaw += ((forward > 0.0D) ? -45 : 45);
/* 123 */         } else if (strafe < 0.0D) {
/* 124 */           yaw += ((forward > 0.0D) ? 45 : -45);
/*     */         } 
/* 126 */         strafe = 0.0D;
/* 127 */         if (forward > 0.0D) {
/* 128 */           forward = 1.0D;
/* 129 */         } else if (forward < 0.0D) {
/* 130 */           forward = -1.0D;
/*     */         } 
/*     */       } 
/* 133 */       event.setX(forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F))));
/* 134 */       event.setZ(forward * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F))));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static float getSpeed() {
/* 139 */     return (float)Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
/*     */   }
/*     */   
/*     */   public static void setSpeed(float speed) {
/* 143 */     float yaw = mc.player.rotationYaw;
/* 144 */     float forward = mc.player.movementInput.moveForward;
/* 145 */     float strafe = mc.player.movementInput.moveStrafe;
/* 146 */     if (forward != 0.0F) {
/* 147 */       if (strafe > 0.0F) {
/* 148 */         yaw += ((forward > 0.0F) ? -45 : 45);
/* 149 */       } else if (strafe < 0.0F) {
/* 150 */         yaw += ((forward > 0.0F) ? 45 : -45);
/*     */       } 
/* 152 */       strafe = 0.0F;
/* 153 */       if (forward > 0.0F) {
/* 154 */         forward = 1.0F;
/* 155 */       } else if (forward < 0.0F) {
/* 156 */         forward = -1.0F;
/*     */       } 
/*     */     } 
/* 159 */     mc.player.motionX = (forward * speed) * Math.cos(Math.toRadians((yaw + 90.0F))) + (strafe * speed) * Math.sin(Math.toRadians((yaw + 90.0F)));
/* 160 */     mc.player.motionZ = (forward * speed) * Math.sin(Math.toRadians((yaw + 90.0F))) - (strafe * speed) * Math.cos(Math.toRadians((yaw + 90.0F)));
/*     */   }
/*     */   
/*     */   public static double getDirectionAll() {
/* 164 */     float rotationYaw = mc.player.rotationYaw;
/* 165 */     float forward = 1.0F;
/* 166 */     if (mc.player.moveForward < 0.0F)
/* 167 */       rotationYaw += 180.0F; 
/* 168 */     if (mc.player.moveForward < 0.0F) {
/* 169 */       forward = -0.5F;
/* 170 */     } else if (mc.player.moveForward > 0.0F) {
/* 171 */       forward = 0.5F;
/* 172 */     }  if (mc.player.moveStrafing > 0.0F)
/* 173 */       rotationYaw -= 90.0F * forward; 
/* 174 */     if (mc.player.moveStrafing < 0.0F)
/* 175 */       rotationYaw += 90.0F * forward; 
/* 176 */     return Math.toRadians(rotationYaw);
/*     */   }
/*     */   
/*     */   public static void strafePlayer(float speed) {
/* 180 */     double yaw = getDirectionAll();
/* 181 */     float getSpeed = (speed == 0.0F) ? getSpeed() : speed;
/* 182 */     mc.player.motionX = -Math.sin(yaw) * getSpeed;
/* 183 */     mc.player.motionZ = Math.cos(yaw) * getSpeed;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\player\MovementHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */