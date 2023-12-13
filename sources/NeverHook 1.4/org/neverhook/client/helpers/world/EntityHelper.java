/*    */ package org.neverhook.client.helpers.world;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class EntityHelper implements Helper {
/*    */   public static double getDistance(double x, double y, double z, double x1, double y1, double z1) {
/* 12 */     double posX = x - x1;
/* 13 */     double posY = y - y1;
/* 14 */     double posZ = z - z1;
/* 15 */     return Math.sqrt(posX * posX + posY * posY + posZ * posZ);
/*    */   }
/*    */   
/*    */   public static double getDistance(double x1, double z1, double x2, double z2) {
/* 19 */     double deltaX = x1 - x2;
/* 20 */     double deltaZ = z1 - z2;
/* 21 */     return Math.hypot(deltaX, deltaZ);
/*    */   }
/*    */   
/*    */   public static Entity rayCast(Entity entityIn, double range) {
/* 25 */     Vec3d vec = entityIn.getPositionVector().add(new Vec3d(0.0D, entityIn.getEyeHeight(), 0.0D));
/* 26 */     Vec3d vecPositionVector = mc.player.getPositionVector().add(new Vec3d(0.0D, mc.player.getEyeHeight(), 0.0D));
/* 27 */     AxisAlignedBB axis = mc.player.getEntityBoundingBox().addCoord(vec.xCoord - vecPositionVector.xCoord, vec.yCoord - vecPositionVector.yCoord, vec.zCoord - vecPositionVector.zCoord).expand(1.0D, 1.0D, 1.0D);
/* 28 */     Entity entityRayCast = null;
/* 29 */     for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity((Entity)mc.player, axis)) {
/* 30 */       if (entity.canBeCollidedWith() && entity instanceof EntityLivingBase) {
/* 31 */         float size = entity.getCollisionBorderSize();
/* 32 */         AxisAlignedBB axis1 = entity.getEntityBoundingBox().expand(size, size, size);
/* 33 */         RayTraceResult rayTrace = axis1.calculateIntercept(vecPositionVector, vec);
/* 34 */         if (axis1.isVecInside(vecPositionVector)) {
/* 35 */           if (range >= 0.0D) {
/* 36 */             entityRayCast = entity;
/* 37 */             range = 0.0D;
/*    */           }  continue;
/* 39 */         }  if (rayTrace != null) {
/* 40 */           double dist = vecPositionVector.distanceTo(rayTrace.hitVec);
/* 41 */           if (range == 0.0D || dist < range) {
/* 42 */             entityRayCast = entity;
/* 43 */             range = dist;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 49 */     return entityRayCast;
/*    */   }
/*    */   
/*    */   public static boolean isTeamWithYou(EntityLivingBase entity) {
/* 53 */     if (mc.player != null && entity != null && 
/* 54 */       mc.player.getDisplayName() != null && entity.getDisplayName() != null) {
/* 55 */       if (mc.player.getTeam() != null && entity.getTeam() != null && mc.player.getTeam().isSameTeam(entity.getTeam())) {
/* 56 */         return true;
/*    */       }
/* 58 */       String targetName = entity.getDisplayName().getFormattedText().replace("§r", "");
/* 59 */       String clientName = mc.player.getDisplayName().getFormattedText().replace("§r", "");
/* 60 */       return targetName.startsWith("§" + clientName.charAt(1));
/*    */     } 
/*    */     
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean checkArmor(Entity entity) {
/* 67 */     return (((EntityLivingBase)entity).getTotalArmorValue() < 6);
/*    */   }
/*    */   
/*    */   public static int getPing(EntityPlayer entityPlayer) {
/* 71 */     return mc.player.connection.getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime();
/*    */   }
/*    */   
/*    */   public static double getDistanceOfEntityToBlock(Entity entity, BlockPos pos) {
/* 75 */     return getDistance(entity.posX, entity.posY, entity.posZ, pos.getX(), pos.getY(), pos.getZ());
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\world\EntityHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */