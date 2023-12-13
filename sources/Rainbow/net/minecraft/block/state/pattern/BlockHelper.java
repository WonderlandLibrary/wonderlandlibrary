package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import slavikcodd3r.rainbow.utils.ClientUtils;

public class BlockHelper implements Predicate
{
    private final Block block;
    private static final String __OBFID = "CL_00002020";

    private BlockHelper(Block p_i45654_1_)
    {
        this.block = p_i45654_1_;
    }

    public static BlockHelper forBlock(Block p_177642_0_)
    {
        return new BlockHelper(p_177642_0_);
    }

    public boolean isBlockEqualTo(IBlockState p_177643_1_)
    {
        return p_177643_1_ != null && p_177643_1_.getBlock() == this.block;
    }

    public boolean apply(Object p_apply_1_)
    {
        return this.isBlockEqualTo((IBlockState)p_apply_1_);
    }

    public static EnumFacing getFacing(final BlockPos pos) {
        final EnumFacing[] orderedValues = { EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN };
        EnumFacing[] arrayOfEnumFacing1;
        for (int j = (arrayOfEnumFacing1 = orderedValues).length, i = 0; i < j; ++i) {
            final EnumFacing facing = arrayOfEnumFacing1[i];
            final Entity temp = new EntitySnowball(ClientUtils.world());
            temp.posX = pos.getX() + 0.5;
            temp.posY = pos.getY() + 0.5;
            temp.posZ = pos.getZ() + 0.5;
            final Entity entity4;
            final Entity entity = entity4 = temp;
            entity4.posX += facing.getDirectionVec().getX() * 0.5;
            final Entity entity5;
            final Entity entity2 = entity5 = temp;
            entity5.posY += facing.getDirectionVec().getY() * 0.5;
            final Entity entity6;
            final Entity entity3 = entity6 = temp;
            entity6.posZ += facing.getDirectionVec().getZ() * 0.5;
            if (ClientUtils.player().canEntityBeSeen(temp)) {
                return facing;
            }
        }
        return null;
    }

    public static float[] getFacingRotations(final int x, final int y, final int z, final EnumFacing facing) {
        final Entity temp = new EntitySnowball(ClientUtils.world());
        temp.posX = x + 0.5;
        temp.posY = y + 0.5;
        temp.posZ = z + 0.5;
        try {
            final Entity entity4;
            final Entity entity = entity4 = temp;
            entity4.posX += facing.getDirectionVec().getX() * 0.25;
            final Entity entity5;
            final Entity entity2 = entity5 = temp;
            entity5.posY += facing.getDirectionVec().getY() * 0.25;
            final Entity entity6;
            final Entity entity3 = entity6 = temp;
            entity6.posZ += facing.getDirectionVec().getZ() * 0.25;
        }
        catch (NullPointerException ex) {}
        return getAngles(temp);
    }

    public static float[] getAngles(final Entity e) {
        return new float[] { getYawChangeToEntity(e) + ClientUtils.player().rotationYaw, getPitchChangeToEntity(e) + ClientUtils.player().rotationPitch };
    }

    public static float getPitchChangeToEntity(final Entity entity) {
        final double deltaX = entity.posX - ClientUtils.player().posX;
        final double deltaZ = entity.posZ - ClientUtils.player().posZ;
        final double deltaY = entity.posY - 1.6 + entity.getEyeHeight() - ClientUtils.player().posY;
        final double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(ClientUtils.player().rotationPitch - (float)pitchToEntity);
    }

	public static float getYawChangeToEntity(final Entity entity) {
        final double deltaX = entity.posX - ClientUtils.player().posX;
        final double deltaZ = entity.posZ - ClientUtils.player().posZ;
        double yawToEntity;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(ClientUtils.player().rotationYaw - (float)yawToEntity));
    }
}