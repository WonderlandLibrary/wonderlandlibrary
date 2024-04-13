package dev.tenacity.util.misc;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.*;
import net.optifine.reflect.Reflector;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public final class RaycastUtil {

    private static final Minecraft MC = Minecraft.getMinecraft();

    private RaycastUtil() {
    }

    // Edited method from @EntityRenderer getMouseOver()
    public static MovingObjectPosition getMouseOver(final Vector2f rotation, final double range)
    {
        Entity entity = MC.getRenderViewEntity();
        Entity pointedEntity;
        
        MovingObjectPosition objectMouseOver;

        if (entity != null && MC.theWorld != null)
        {
            MC.mcProfiler.startSection("pick");
            pointedEntity = null;
            double d0 = range;
            objectMouseOver = MC.thePlayer.rayTrace(d0, rotation.x, rotation.y);
            double d1 = d0;
            Vec3 vec3 = entity.getPositionEyes(1);
            boolean flag = false;
            int i = 3;

            if (MC.playerController.extendedReach())
            {
                d0 = 3.0D;
                d1 = 6.0D;
            }
            else if (d0 > 3.0D)
            {
                flag = true;
            }

            if (objectMouseOver != null)
            {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            Vec3 vec31 = entity.getLook(1);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            Vec3 vec33 = null;
            float f = 1.0F;
            List<Entity> list = MC.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
            {
                public boolean apply(Entity p_apply_1_)
                {
                    return p_apply_1_.canBeCollidedWith();
                }
            }));
            double d2 = d1;

            for (int j = 0; j < list.size(); ++j)
            {
                Entity entity1 = (Entity)list.get(j);
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3))
                {
                    if (d2 >= 0.0D)
                    {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                }
                else if (movingobjectposition != null)
                {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D)
                    {
                        boolean flag1 = false;

                        if (Reflector.ForgeEntity_canRiderInteract.exists())
                        {
                            flag1 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }

                        if (!flag1 && entity1 == entity.ridingEntity)
                        {
                            if (d2 == 0.0D)
                            {
                                pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        }
                        else
                        {
                            pointedEntity = entity1;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > 3.0D)
            {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing)null, new BlockPos(vec33));
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null))
            {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
            }

            MC.mcProfiler.endSection();
            return objectMouseOver;
        }
        return null;
    }

}
