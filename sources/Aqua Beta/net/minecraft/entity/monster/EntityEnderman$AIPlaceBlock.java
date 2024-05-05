package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

static class EntityEnderman.AIPlaceBlock
extends EntityAIBase {
    private EntityEnderman enderman;

    public EntityEnderman.AIPlaceBlock(EntityEnderman p_i45843_1_) {
        this.enderman = p_i45843_1_;
    }

    public boolean shouldExecute() {
        return !this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") ? false : (this.enderman.getHeldBlockState().getBlock().getMaterial() == Material.air ? false : this.enderman.getRNG().nextInt(2000) == 0);
    }

    public void updateTask() {
        Random random = this.enderman.getRNG();
        World world = this.enderman.worldObj;
        int i = MathHelper.floor_double((double)(this.enderman.posX - 1.0 + random.nextDouble() * 2.0));
        int j = MathHelper.floor_double((double)(this.enderman.posY + random.nextDouble() * 2.0));
        int k = MathHelper.floor_double((double)(this.enderman.posZ - 1.0 + random.nextDouble() * 2.0));
        BlockPos blockpos = new BlockPos(i, j, k);
        Block block = world.getBlockState(blockpos).getBlock();
        Block block1 = world.getBlockState(blockpos.down()).getBlock();
        if (this.func_179474_a(world, blockpos, this.enderman.getHeldBlockState().getBlock(), block, block1)) {
            world.setBlockState(blockpos, this.enderman.getHeldBlockState(), 3);
            this.enderman.setHeldBlockState(Blocks.air.getDefaultState());
        }
    }

    private boolean func_179474_a(World worldIn, BlockPos p_179474_2_, Block p_179474_3_, Block p_179474_4_, Block p_179474_5_) {
        return !p_179474_3_.canPlaceBlockAt(worldIn, p_179474_2_) ? false : (p_179474_4_.getMaterial() != Material.air ? false : (p_179474_5_.getMaterial() == Material.air ? false : p_179474_5_.isFullCube()));
    }
}
