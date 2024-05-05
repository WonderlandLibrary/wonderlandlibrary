package club.async.util;

import club.async.interfaces.MinecraftInterface;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;

import java.util.List;
import java.util.Objects;

public final class WorldUtil implements MinecraftInterface {

    public static IBlockState getBlockState(BlockPos pos) {
        return mc.theWorld.getBlockState(pos);
    }

    public static Block getBlock(BlockPos pos) {
        return getBlockState(pos).getBlock();
    }

    public static EntityLivingBase raycast(double range, float[] rotations) {
        // code from getMouseOver() in EntityRenderer
        // get position vector with yPos of eyes
        Vec3 eyes = mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks);
        // get vector for rotation
        Vec3 look = mc.thePlayer.getVectorForRotation(rotations[1], rotations[0]);
        // make a vector encasing the range
        Vec3 vec = eyes.addVector(look.xCoord * range, look.yCoord * range, look.zCoord * range);
        // get all entities in the bounding box (only players)
        List<Entity> entities = mc.theWorld.getEntitiesInAABBexcluding(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(look.xCoord * range, look.yCoord * range, look.zCoord * range).expand(1, 1, 1), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        EntityLivingBase raycastedEntity = null;
        for(Entity ent : entities) {
            final EntityLivingBase entity = (EntityLivingBase) ent;
            if(entity == mc.thePlayer) continue;

            // get border size of the bounding box
            final float borderSize = entity.getCollisionBorderSize();
            // expand it so that its not part of the collision
            final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(borderSize, borderSize, borderSize);
            // calculate where the eyes and the vec intercept
            final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(eyes, vec);

            // check if player is inside mc.thePlayer
            if (axisalignedbb.isVecInside(eyes)) {
                if (range >= 0) {
                    raycastedEntity = entity;
                    range = 0;
                }
                // if found object and not in mc.thePlayer
            } else if (movingobjectposition != null) {
                // get the distance to the object
                double distance = eyes.distanceTo(movingobjectposition.hitVec);
                // check if the distance is in range or 0
                if (distance < range || range == 0) {
                    // check if riding entity
                    if (entity == entity.ridingEntity) {
                        // set the raycastedEntity to the current entity in the loop
                        if (range == 0) raycastedEntity = entity;
                        // not the riding entity
                    } else {
                        // set raycastedEntity and range
                        raycastedEntity = entity;
                        range = distance;
                    }
                }
            }
        }
        return raycastedEntity;
    }

    public static void destroy(BlockPos blockPos) {
        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, Objects.requireNonNull(ScaffoldUtil.getBlockData(blockPos)).face));
        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, Objects.requireNonNull(ScaffoldUtil.getBlockData(blockPos)).face));
    }

}
