/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
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
    private static final Predicate<IBlockState> field_179505_b = BlockStateHelper.forBlock(Blocks.tallgrass).where(BlockTallGrass.TYPE, Predicates.equalTo((Object)BlockTallGrass.EnumType.GRASS));
    int eatingGrassTimer;
    private World entityWorld;
    private EntityLiving grassEaterEntity;

    @Override
    public void resetTask() {
        this.eatingGrassTimer = 0;
    }

    public int getEatingGrassTimer() {
        return this.eatingGrassTimer;
    }

    @Override
    public void updateTask() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
        if (this.eatingGrassTimer == 4) {
            BlockPos blockPos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
            if (field_179505_b.apply((Object)this.entityWorld.getBlockState(blockPos))) {
                if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
                    this.entityWorld.destroyBlock(blockPos, false);
                }
                this.grassEaterEntity.eatGrassBonus();
            } else {
                BlockPos blockPos2 = blockPos.down();
                if (this.entityWorld.getBlockState(blockPos2).getBlock() == Blocks.grass) {
                    if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
                        this.entityWorld.playAuxSFX(2001, blockPos2, Block.getIdFromBlock(Blocks.grass));
                        this.entityWorld.setBlockState(blockPos2, Blocks.dirt.getDefaultState(), 2);
                    }
                    this.grassEaterEntity.eatGrassBonus();
                }
            }
        }
    }

    @Override
    public boolean shouldExecute() {
        if (this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 50 : 1000) != 0) {
            return false;
        }
        BlockPos blockPos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
        return field_179505_b.apply((Object)this.entityWorld.getBlockState(blockPos)) ? true : this.entityWorld.getBlockState(blockPos.down()).getBlock() == Blocks.grass;
    }

    @Override
    public void startExecuting() {
        this.eatingGrassTimer = 40;
        this.entityWorld.setEntityState(this.grassEaterEntity, (byte)10);
        this.grassEaterEntity.getNavigator().clearPathEntity();
    }

    @Override
    public boolean continueExecuting() {
        return this.eatingGrassTimer > 0;
    }

    public EntityAIEatGrass(EntityLiving entityLiving) {
        this.grassEaterEntity = entityLiving;
        this.entityWorld = entityLiving.worldObj;
        this.setMutexBits(7);
    }
}

