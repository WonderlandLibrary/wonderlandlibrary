/*     */ package org.neverhook.client.helpers.math;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*     */ import org.neverhook.client.event.events.impl.player.EventPlayerState;
/*     */ import org.neverhook.client.feature.impl.combat.KillAura;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ import org.neverhook.client.helpers.player.MovementHelper;
/*     */ 
/*     */ public class RotationHelper
/*     */   implements Helper
/*     */ {
/*     */   public static Vec3d getEyesPos() {
/*  19 */     return new Vec3d(mc.player.posX, (mc.player.getEntityBoundingBox()).minY + mc.player.getEyeHeight(), mc.player.posZ);
/*     */   }
/*     */   
/*     */   public static boolean isLookingAtEntity(float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
/*  23 */     Vec3d src = mc.player.getPositionEyes(1.0F);
/*  24 */     Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
/*  25 */     Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
/*  26 */     RayTraceResult rayTraceResult = mc.world.rayTraceBlocks(src, dest, false, false, true);
/*  27 */     if (rayTraceResult == null) {
/*  28 */       return false;
/*     */     }
/*  30 */     return (entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null);
/*     */   }
/*     */   
/*     */   public static float[] getRotations(Entity entityIn, boolean random, float maxSpeed, float minSpeed, float yawRandom, float pitchRandom) {
/*  34 */     double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) * KillAura.rotPredict.getNumberValue() - mc.player.posX - mc.player.motionX * KillAura.rotPredict.getNumberValue();
/*  35 */     double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) * KillAura.rotPredict.getNumberValue() - mc.player.posZ - mc.player.motionZ * KillAura.rotPredict.getNumberValue();
/*     */ 
/*     */     
/*  38 */     double diffY = entityIn.posY + entityIn.getEyeHeight() - mc.player.posY + mc.player.getEyeHeight() - KillAura.pitchValue.getNumberValue() - ((KillAura.walls.getBoolValue() && KillAura.bypass.getBoolValue() && !((EntityLivingBase)entityIn).canEntityBeSeen((Entity)mc.player)) ? -0.38D : 0.0D);
/*     */     
/*  40 */     float randomYaw = 0.0F;
/*  41 */     if (random) {
/*  42 */       randomYaw = MathematicHelper.randomizeFloat(yawRandom, -yawRandom);
/*     */     }
/*  44 */     float randomPitch = 0.0F;
/*  45 */     if (random) {
/*  46 */       randomPitch = MathematicHelper.randomizeFloat(pitchRandom, -pitchRandom);
/*     */     }
/*     */     
/*  49 */     double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
/*     */     
/*  51 */     float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI - 90.0D) + randomYaw;
/*  52 */     float pitch = (float)-(Math.atan2(diffY, diffXZ) * 180.0D / Math.PI) + randomPitch;
/*     */     
/*  54 */     yaw = mc.player.rotationYaw + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
/*  55 */     pitch = mc.player.rotationPitch + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
/*  56 */     pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
/*  57 */     yaw = updateRotation(mc.player.rotationYaw, yaw, MathematicHelper.randomizeFloat(minSpeed, maxSpeed));
/*  58 */     pitch = updateRotation(mc.player.rotationPitch, pitch, MathematicHelper.randomizeFloat(minSpeed, maxSpeed));
/*     */     
/*  60 */     return new float[] { yaw, pitch };
/*     */   }
/*     */   
/*     */   public static float[] getRotationVector(Vec3d vec, boolean randomRotation, float yawRandom, float pitchRandom, float speedRotation) {
/*  64 */     Vec3d eyesPos = getEyesPos();
/*  65 */     double diffX = vec.xCoord - eyesPos.xCoord;
/*  66 */     double diffY = vec.yCoord - mc.player.posY + mc.player.getEyeHeight() + 0.5D;
/*  67 */     double diffZ = vec.zCoord - eyesPos.zCoord;
/*  68 */     double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
/*     */     
/*  70 */     float randomYaw = 0.0F;
/*  71 */     if (randomRotation) {
/*  72 */       randomYaw = MathematicHelper.randomizeFloat(-yawRandom, yawRandom);
/*     */     }
/*  74 */     float randomPitch = 0.0F;
/*  75 */     if (randomRotation) {
/*  76 */       randomPitch = MathematicHelper.randomizeFloat(-pitchRandom, pitchRandom);
/*     */     }
/*     */     
/*  79 */     float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D) + randomYaw;
/*  80 */     float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ)) + randomPitch;
/*  81 */     yaw = mc.player.rotationYaw + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
/*  82 */     pitch = mc.player.rotationPitch + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
/*  83 */     pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
/*  84 */     yaw = updateRotation(mc.player.rotationYaw, yaw, speedRotation);
/*  85 */     pitch = updateRotation(mc.player.rotationPitch, pitch, speedRotation);
/*     */     
/*  87 */     return new float[] { yaw, pitch };
/*     */   }
/*     */   
/*     */   public static float getAngleEntity(Entity entity) {
/*  91 */     float yaw = mc.player.rotationYaw;
/*  92 */     double xDist = entity.posX - mc.player.posX;
/*  93 */     double zDist = entity.posZ - mc.player.posZ;
/*  94 */     float yaw1 = (float)(Math.atan2(zDist, xDist) * 180.0D / Math.PI - 90.0D);
/*  95 */     return yaw + MathHelper.wrapDegrees(yaw1 - yaw);
/*     */   }
/*     */   
/*     */   public static float[] getFacePosRemote(EntityLivingBase facing, Entity entity, boolean random) {
/*  99 */     Vec3d src = new Vec3d(facing.posX, facing.posY, facing.posZ);
/* 100 */     Vec3d dest = new Vec3d(entity.posX, entity.posY, entity.posZ);
/* 101 */     double diffX = dest.xCoord - src.xCoord;
/* 102 */     double diffY = dest.yCoord - src.yCoord;
/* 103 */     double diffZ = dest.zCoord - src.zCoord;
/* 104 */     float randomYaw = 0.0F;
/* 105 */     if (random) {
/* 106 */       randomYaw = MathematicHelper.randomizeFloat(-2.0F, 2.0F);
/*     */     }
/* 108 */     float randomPitch = 0.0F;
/* 109 */     if (random) {
/* 110 */       randomPitch = MathematicHelper.randomizeFloat(-2.0F, 2.0F);
/*     */     }
/* 112 */     double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
/* 113 */     float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F + randomYaw;
/* 114 */     float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI) + randomPitch;
/* 115 */     return new float[] { MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch) };
/*     */   }
/*     */   
/*     */   public static float updateRotation(float current, float newValue, float speed) {
/* 119 */     float f = MathHelper.wrapDegrees(newValue - current);
/* 120 */     if (f > speed) {
/* 121 */       f = speed;
/*     */     }
/* 123 */     if (f < -speed) {
/* 124 */       f = -speed;
/*     */     }
/* 126 */     return current + f;
/*     */   }
/*     */   
/*     */   public static class Rotation
/*     */   {
/*     */     public static boolean isReady = false;
/*     */     public static float packetPitch;
/*     */     public static float packetYaw;
/*     */     public static float lastPacketPitch;
/*     */     public static float lastPacketYaw;
/*     */     public static float renderPacketYaw;
/*     */     public static float lastRenderPacketYaw;
/*     */     public static float bodyYaw;
/*     */     public static float lastBodyYaw;
/*     */     public static int rotationCounter;
/*     */     public static boolean isAiming;
/*     */     
/*     */     public static boolean isAiming() {
/* 144 */       return !isAiming;
/*     */     }
/*     */     
/*     */     public static double calcMove() {
/* 148 */       double x = Helper.mc.player.posX - Helper.mc.player.prevPosX;
/* 149 */       double z = Helper.mc.player.posZ - Helper.mc.player.prevPosZ;
/* 150 */       return Math.hypot(x, z);
/*     */     }
/*     */     
/*     */     @EventTarget
/*     */     public void onPlayerState(EventPlayerState event) {
/* 155 */       if (isAiming() && event.isPre()) {
/* 156 */         isReady = true;
/* 157 */       } else if (isAiming() && isReady && event.isPost()) {
/* 158 */         packetPitch = Helper.mc.player.rotationPitch;
/* 159 */         packetYaw = Helper.mc.player.rotationYaw;
/* 160 */         lastPacketPitch = Helper.mc.player.rotationPitch;
/* 161 */         lastPacketYaw = Helper.mc.player.rotationYaw;
/* 162 */         bodyYaw = Helper.mc.player.renderYawOffset;
/* 163 */         isReady = false;
/*     */       } else {
/* 165 */         isReady = false;
/*     */       } 
/* 167 */       if (event.isPre()) {
/* 168 */         lastBodyYaw = bodyYaw;
/* 169 */         lastPacketPitch = packetPitch;
/* 170 */         lastPacketYaw = packetYaw;
/* 171 */         bodyYaw = MathematicHelper.clamp(bodyYaw, packetYaw, 50.0F);
/* 172 */         if (calcMove() > 2.500000277905201E-7D) {
/* 173 */           bodyYaw = MathematicHelper.clamp(MovementHelper.getMoveDirection(), packetYaw, 50.0F);
/* 174 */           rotationCounter = 0;
/* 175 */         } else if (rotationCounter > 0) {
/* 176 */           rotationCounter--;
/* 177 */           bodyYaw = MathematicHelper.checkAngle(packetYaw, bodyYaw, 10.0F);
/*     */         } 
/* 179 */         lastRenderPacketYaw = renderPacketYaw;
/* 180 */         renderPacketYaw = packetYaw;
/*     */       } 
/*     */     }
/*     */     
/*     */     @EventTarget
/*     */     public void onSendPacket(EventSendPacket eventSendPacket) {
/* 186 */       if (eventSendPacket.getPacket() instanceof net.minecraft.network.play.client.CPacketAnimation)
/* 187 */         rotationCounter = 10; 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\math\RotationHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */