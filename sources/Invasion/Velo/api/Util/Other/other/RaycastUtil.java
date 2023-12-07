package Velo.api.Util.Other.other;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import optifine.Reflector;

import java.util.List;

public class RaycastUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Entity rayCast(double range, float yaw, float pitch) {   	
        double d0 = range;
        double d1 = d0;
        Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0f);
        boolean flag = false;
        boolean flag1 = true;

        if (d0 > 3.0D)
        {
            flag = true;
        }

        /*if (this.mc.objectMouseOver != null)
        {
            d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
        }*/

        Vec3 vec31 = getVectorForRotation(pitch, yaw);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);

        Entity pointedEntity = null;

        Vec3 vec33 = null;
        float f = 1.0F;
        List list = mc.theWorld.getEntitiesInAABBexcluding(mc.getRenderViewEntity(), mc.getRenderViewEntity().getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double d2 = d1;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity1 = (Entity)list.get(i);
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
                    boolean flag2 = false;

                    if (Reflector.ForgeEntity_canRiderInteract.exists())
                    {
                        flag2 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                    }

                    if (entity1 == mc.getRenderViewEntity().ridingEntity && !flag2)
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

        return pointedEntity;
    }
    
    public static boolean isLookingAtEntity( float yaw,  float pitch,  Entity entity,  double range,  boolean rayTrace) {
        final EntityPlayer player = mc.thePlayer;
        final Vec3 src = mc.thePlayer.getPositionEyes(1.0f); 
        final Vec3 rotationVec = Entity.getVectorForRotation(pitch, yaw);
        final Vec3 dest = src.addVector(rotationVec.xCoord * range, rotationVec.yCoord * range, rotationVec.zCoord * range);
        final MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(src, dest, false, false, true);
        if (obj == null) {
            return false;
        }
        if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (rayTrace) {
                return false;
            }
            if (player.getDistanceToEntity(entity) > 3.0) {
                return false;
            }
        }
        return entity.getEntityBoundingBox().expand(0.10000000149011612, 0.10000000149011612, 0.10000000149011612).calculateIntercept(src, dest) != null;
    }

    public static Vec3 getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
}
