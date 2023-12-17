// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.Vec3i;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;

public class EntityAIPanic extends EntityAIBase
{
    protected final EntityCreature creature;
    protected double speed;
    protected double randPosX;
    protected double randPosY;
    protected double randPosZ;
    
    public EntityAIPanic(final EntityCreature creature, final double speedIn) {
        this.creature = creature;
        this.speed = speedIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.creature.getRevengeTarget() == null && !this.creature.isBurning()) {
            return false;
        }
        if (this.creature.isBurning()) {
            final BlockPos blockpos = this.getRandPos(this.creature.world, this.creature, 5, 4);
            if (blockpos != null) {
                this.randPosX = blockpos.getX();
                this.randPosY = blockpos.getY();
                this.randPosZ = blockpos.getZ();
                return true;
            }
        }
        return this.findRandomPosition();
    }
    
    protected boolean findRandomPosition() {
        final Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 5, 4);
        if (vec3d == null) {
            return false;
        }
        this.randPosX = vec3d.x;
        this.randPosY = vec3d.y;
        this.randPosZ = vec3d.z;
        return true;
    }
    
    @Override
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath();
    }
    
    @Nullable
    private BlockPos getRandPos(final World worldIn, final Entity entityIn, final int horizontalRange, final int verticalRange) {
        final BlockPos blockpos = new BlockPos(entityIn);
        final int i = blockpos.getX();
        final int j = blockpos.getY();
        final int k = blockpos.getZ();
        float f = (float)(horizontalRange * horizontalRange * verticalRange * 2);
        BlockPos blockpos2 = null;
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int l = i - horizontalRange; l <= i + horizontalRange; ++l) {
            for (int i2 = j - verticalRange; i2 <= j + verticalRange; ++i2) {
                for (int j2 = k - horizontalRange; j2 <= k + horizontalRange; ++j2) {
                    blockpos$mutableblockpos.setPos(l, i2, j2);
                    final IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);
                    if (iblockstate.getMaterial() == Material.WATER) {
                        final float f2 = (float)((l - i) * (l - i) + (i2 - j) * (i2 - j) + (j2 - k) * (j2 - k));
                        if (f2 < f) {
                            f = f2;
                            blockpos2 = new BlockPos(blockpos$mutableblockpos);
                        }
                    }
                }
            }
        }
        return blockpos2;
    }
}
