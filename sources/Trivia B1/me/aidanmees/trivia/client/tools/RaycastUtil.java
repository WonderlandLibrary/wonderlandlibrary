package me.aidanmees.trivia.client.tools;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class RaycastUtil {
    private static Entity pointedEntity;

    public static EntityLivingBase raycastEntity(EntityLivingBase e) {
        EntityPlayerSP pl = Minecraft.getMinecraft().thePlayer;
        Entity point = null;
        if (pl != null && Minecraft.getMinecraft().theWorld != null) {
            double reach = 100.0;
            Vec3 var7 = pl.getLook(0.0f);
            float[] prot = RotationUtil.faceEntity(e, 360.0f, 360.0f);
            if (prot == null) {
                return null;
            }
            Vec3 var8 = RaycastUtil.toLookVec3(prot[0], prot[1]);
            Vec3 var9 = var7.addVector(var8.xCoord * reach, var8.yCoord * reach, var8.zCoord * reach);
            Vec3 var10 = null;
            List var12 = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABBExcludingEntity(pl, pl.getEntityBoundingBox().addCoord(var8.xCoord * reach, var8.yCoord * reach, var8.zCoord * reach).expand(1.0, 1.0, 1.0));
            double var13 = reach;
            int var15 = 0;
            while (var15 < var12.size()) {
                Entity var16 = (Entity)var12.get(var15);
                if (var16.canBeCollidedWith()) {
                    double var20;
                    float var17 = var16.getCollisionBorderSize();
                    AxisAlignedBB var18 = var16.getEntityBoundingBox().expand(var17, var17, var17);
                    MovingObjectPosition var19 = var18.calculateIntercept(var7, var9);
                    if (var18.isVecInside(var7)) {
                        if (0.0 < var13 || var13 == 0.0) {
                            point = var16;
                            var10 = var19 == null ? var7 : var19.hitVec;
                            var13 = 0.0;
                        }
                    } else if (var19 != null && ((var20 = var7.distanceTo(var19.hitVec)) < var13 || var13 == 0.0)) {
                        point = var16;
                        var10 = var19.hitVec;
                        var13 = var20;
                    }
                }
                ++var15;
            }
            if (point != null && var13 < reach && point instanceof EntityLivingBase) {
                return (EntityLivingBase)point;
            }
        }
        return e;
    }

    public static Vec3 toLookVec3(float yaw, float pitch) {
        float rvar3 = MathHelper.cos((- yaw) * 0.017453292f - 3.1415927f);
        float rvar4 = MathHelper.sin((- yaw) * 0.017453292f - 3.1415927f);
        float rvar5 = - MathHelper.cos((- pitch) * 0.017453292f);
        float rvar6 = MathHelper.sin((- pitch) * 0.017453292f);
        return new Vec3(rvar4 * rvar5, rvar6, rvar3 * rvar5);
    }
}

