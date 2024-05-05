package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

static class EntitySilverfish.AISummonSilverfish
extends EntityAIBase {
    private EntitySilverfish silverfish;
    private int field_179463_b;

    public EntitySilverfish.AISummonSilverfish(EntitySilverfish silverfishIn) {
        this.silverfish = silverfishIn;
    }

    public void func_179462_f() {
        if (this.field_179463_b == 0) {
            this.field_179463_b = 20;
        }
    }

    public boolean shouldExecute() {
        return this.field_179463_b > 0;
    }

    public void updateTask() {
        --this.field_179463_b;
        if (this.field_179463_b <= 0) {
            World world = this.silverfish.worldObj;
            Random random = this.silverfish.getRNG();
            BlockPos blockpos = new BlockPos((Entity)this.silverfish);
            int i = 0;
            while (i <= 5 && i >= -5) {
                int j = 0;
                while (j <= 10 && j >= -10) {
                    int k = 0;
                    while (k <= 10 && k >= -10) {
                        BlockPos blockpos1 = blockpos.add(j, i, k);
                        IBlockState iblockstate = world.getBlockState(blockpos1);
                        if (iblockstate.getBlock() == Blocks.monster_egg) {
                            if (world.getGameRules().getBoolean("mobGriefing")) {
                                world.destroyBlock(blockpos1, true);
                            } else {
                                world.setBlockState(blockpos1, ((BlockSilverfish.EnumType)iblockstate.getValue((IProperty)BlockSilverfish.VARIANT)).getModelBlock(), 3);
                            }
                            if (random.nextBoolean()) {
                                return;
                            }
                        }
                        k = k <= 0 ? 1 - k : 0 - k;
                    }
                    j = j <= 0 ? 1 - j : 0 - j;
                }
                i = i <= 0 ? 1 - i : 0 - i;
            }
        }
    }
}
