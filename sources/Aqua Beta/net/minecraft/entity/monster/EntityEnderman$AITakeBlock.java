package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
static class EntityEnderman.AITakeBlock
extends EntityAIBase {
    private EntityEnderman enderman;

    public EntityEnderman.AITakeBlock(EntityEnderman p_i45841_1_) {
        this.enderman = p_i45841_1_;
    }

    public boolean shouldExecute() {
        return !this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") ? false : (this.enderman.getHeldBlockState().getBlock().getMaterial() != Material.air ? false : this.enderman.getRNG().nextInt(20) == 0);
    }

    public void updateTask() {
        Random random = this.enderman.getRNG();
        World world = this.enderman.worldObj;
        int i = MathHelper.floor_double((double)(this.enderman.posX - 2.0 + random.nextDouble() * 4.0));
        int j = MathHelper.floor_double((double)(this.enderman.posY + random.nextDouble() * 3.0));
        int k = MathHelper.floor_double((double)(this.enderman.posZ - 2.0 + random.nextDouble() * 4.0));
        BlockPos blockpos = new BlockPos(i, j, k);
        IBlockState iblockstate = world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (EntityEnderman.access$300().contains((Object)block)) {
            this.enderman.setHeldBlockState(iblockstate);
            world.setBlockState(blockpos, Blocks.air.getDefaultState());
        }
    }
}
