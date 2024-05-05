/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityAIEatGrass
extends EntityAIBase {
    private static final Predicate<IBlockState> field_179505_b = BlockStateHelper.forBlock(Blocks.tallgrass).where(BlockTallGrass.TYPE, Predicates.equalTo(BlockTallGrass.EnumType.GRASS));
    private EntityLiving grassEaterEntity;
    private World entityWorld;
    int eatingGrassTimer;

    public EntityAIEatGrass(EntityLiving grassEaterEntityIn) {
        this.grassEaterEntity = grassEaterEntityIn;
        this.entityWorld = grassEaterEntityIn.worldObj;
        this.setMutexBits(7);
    }

    @Override
    public boolean shouldExecute() {
        if (this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 50 : 1000) != 0) {
            return false;
        }
        BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
        if (field_179505_b.apply(this.entityWorld.getBlockState(blockpos))) {
            return true;
        }
        if (this.entityWorld.getBlockState(blockpos.down()).getBlock() != Blocks.grass) return false;
        return true;
    }

    @Override
    public void startExecuting() {
        this.eatingGrassTimer = 40;
        this.entityWorld.setEntityState(this.grassEaterEntity, (byte)10);
        this.grassEaterEntity.getNavigator().clearPathEntity();
    }

    @Override
    public void resetTask() {
        this.eatingGrassTimer = 0;
    }

    @Override
    public boolean continueExecuting() {
        if (this.eatingGrassTimer <= 0) return false;
        return true;
    }

    public int getEatingGrassTimer() {
        return this.eatingGrassTimer;
    }

    @Override
    public void updateTask() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
        if (this.eatingGrassTimer != 4) return;
        BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
        if (field_179505_b.apply(this.entityWorld.getBlockState(blockpos))) {
            if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
                this.entityWorld.destroyBlock(blockpos, false);
            }
            this.grassEaterEntity.eatGrassBonus();
            return;
        }
        BlockPos blockpos1 = blockpos.down();
        if (this.entityWorld.getBlockState(blockpos1).getBlock() != Blocks.grass) return;
        if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
            this.entityWorld.playAuxSFX(2001, blockpos1, Block.getIdFromBlock(Blocks.grass));
            this.entityWorld.setBlockState(blockpos1, Blocks.dirt.getDefaultState(), 2);
        }
        this.grassEaterEntity.eatGrassBonus();
    }
}

