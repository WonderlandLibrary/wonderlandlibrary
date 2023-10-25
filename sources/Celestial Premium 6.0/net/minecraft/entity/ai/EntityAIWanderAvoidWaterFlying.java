/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderAvoidWaterFlying
extends EntityAIWanderAvoidWater {
    public EntityAIWanderAvoidWaterFlying(EntityCreature p_i47413_1_, double p_i47413_2_) {
        super(p_i47413_1_, p_i47413_2_);
    }

    @Override
    @Nullable
    protected Vec3d func_190864_f() {
        Vec3d vec3d = null;
        if (this.entity.isInWater() || this.entity.func_191953_am()) {
            vec3d = RandomPositionGenerator.func_191377_b(this.entity, 15, 15);
        }
        if (this.entity.getRNG().nextFloat() >= this.field_190865_h) {
            vec3d = this.func_192385_j();
        }
        return vec3d == null ? super.func_190864_f() : vec3d;
    }

    @Nullable
    private Vec3d func_192385_j() {
        Block block;
        boolean flag;
        BlockPos blockpos1;
        BlockPos blockpos = new BlockPos(this.entity);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
        Iterable<BlockPos.MutableBlockPos> iterable = BlockPos.MutableBlockPos.func_191531_b(MathHelper.floor(this.entity.posX - 3.0), MathHelper.floor(this.entity.posY - 6.0), MathHelper.floor(this.entity.posZ - 3.0), MathHelper.floor(this.entity.posX + 3.0), MathHelper.floor(this.entity.posY + 6.0), MathHelper.floor(this.entity.posZ + 3.0));
        Iterator<BlockPos.MutableBlockPos> iterator = iterable.iterator();
        do {
            if (iterator.hasNext()) continue;
            return null;
        } while (blockpos.equals(blockpos1 = (BlockPos)iterator.next()) || !(flag = (block = this.entity.world.getBlockState(blockpos$mutableblockpos1.setPos(blockpos1).move(EnumFacing.DOWN)).getBlock()) instanceof BlockLeaves || block == Blocks.LOG || block == Blocks.LOG2) || !this.entity.world.isAirBlock(blockpos1) || !this.entity.world.isAirBlock(blockpos$mutableblockpos.setPos(blockpos1).move(EnumFacing.UP)));
        return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
    }
}

