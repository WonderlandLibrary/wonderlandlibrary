/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 */
package me.Tengoku.Terror.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import me.Tengoku.Terror.util.ScaffoldUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class RayCastUtil {
    private static final Minecraft MC = Minecraft.getMinecraft();

    public final Entity surroundEntity(Entity entity) {
        Entity entity2 = entity;
        for (Entity entity3 : Minecraft.theWorld.loadedEntityList) {
            if (!entity3.isInvisible() || (double)entity.getDistanceToEntity(entity3) > 0.5 || !(Minecraft.thePlayer.getDistanceToEntity(entity3) < Minecraft.thePlayer.getDistanceToEntity(entity2))) continue;
            entity2 = entity3;
        }
        return entity;
    }

    public final Entity raycastEntity(double d, float[] fArray) {
        Entity entity = MC.getRenderViewEntity();
        if (entity != null && Minecraft.theWorld != null) {
            Vec3 vec3 = entity.getPositionEyes(RayCastUtil.MC.timer.renderPartialTicks);
            Vec3 vec32 = ScaffoldUtils.getVectorForRotation(fArray[0], fArray[1]);
            Vec3 vec33 = vec3.addVector(vec32.xCoord * d, vec32.yCoord * d, vec32.zCoord * d);
            List<Entity> list = Minecraft.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec32.xCoord * d, vec32.yCoord * d, vec32.zCoord * d).expand(1.0, 1.0, 1.0), (Predicate<? super Entity>)Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            Entity entity2 = null;
            for (Entity entity3 : list) {
                double d2;
                if (!(entity3 instanceof EntityLivingBase)) continue;
                float f = entity3.getCollisionBorderSize();
                AxisAlignedBB axisAlignedBB = entity3.getEntityBoundingBox().expand(f, f, f);
                MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(vec3, vec33);
                if (axisAlignedBB.isVecInside(vec3)) {
                    if (!(d >= 0.0)) continue;
                    entity2 = entity3;
                    d = 0.0;
                    continue;
                }
                if (movingObjectPosition == null || !((d2 = vec3.distanceTo(movingObjectPosition.hitVec)) < d) && d != 0.0) continue;
                if (entity3 == entity.ridingEntity) {
                    if (d != 0.0) continue;
                    entity2 = entity3;
                    continue;
                }
                entity2 = entity3;
                d = d2;
            }
            return entity2;
        }
        return null;
    }

    public static Vec3 getVectorForRotation(float f, float f2) {
        float f3 = MathHelper.cos(-f2 * ((float)Math.PI / 180) - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * ((float)Math.PI / 180) - (float)Math.PI);
        float f5 = -MathHelper.cos(-f * ((float)Math.PI / 180));
        float f6 = MathHelper.sin(-f * ((float)Math.PI / 180));
        return new Vec3(f4 * f5, f6, f3 * f5);
    }

    public final BlockPos raycastPosition(double d) {
        Entity entity = MC.getRenderViewEntity();
        if (entity != null && Minecraft.theWorld != null) {
            MovingObjectPosition movingObjectPosition = entity.rayTrace(d, 1.0f);
            if (Minecraft.theWorld.getBlockState(movingObjectPosition.getBlockPos()).getBlock() instanceof BlockAir) {
                return null;
            }
            return movingObjectPosition.getBlockPos();
        }
        return null;
    }
}

