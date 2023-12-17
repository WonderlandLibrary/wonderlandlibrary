// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIFollowOwnerFlying extends EntityAIFollowOwner
{
    public EntityAIFollowOwnerFlying(final EntityTameable tameableIn, final double followSpeedIn, final float minDistIn, final float maxDistIn) {
        super(tameableIn, followSpeedIn, minDistIn, maxDistIn);
    }
    
    @Override
    protected boolean isTeleportFriendlyBlock(final int x, final int z, final int y, final int xOffset, final int zOffset) {
        final IBlockState iblockstate = this.world.getBlockState(new BlockPos(x + xOffset, y - 1, z + zOffset));
        return (iblockstate.isTopSolid() || iblockstate.getMaterial() == Material.LEAVES) && this.world.isAirBlock(new BlockPos(x + xOffset, y, z + zOffset)) && this.world.isAirBlock(new BlockPos(x + xOffset, y + 1, z + zOffset));
    }
}
