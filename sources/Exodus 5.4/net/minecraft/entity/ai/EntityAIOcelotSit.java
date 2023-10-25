/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityAIOcelotSit
extends EntityAIMoveToBlock {
    private final EntityOcelot field_151493_a;

    @Override
    public boolean shouldExecute() {
        return this.field_151493_a.isTamed() && !this.field_151493_a.isSitting() && super.shouldExecute();
    }

    public EntityAIOcelotSit(EntityOcelot entityOcelot, double d) {
        super(entityOcelot, d, 8);
        this.field_151493_a = entityOcelot;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        this.field_151493_a.getAISit().setSitting(false);
        if (!this.getIsAboveDestination()) {
            this.field_151493_a.setSitting(false);
        } else if (!this.field_151493_a.isSitting()) {
            this.field_151493_a.setSitting(true);
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.field_151493_a.setSitting(false);
    }

    @Override
    protected boolean shouldMoveTo(World world, BlockPos blockPos) {
        if (!world.isAirBlock(blockPos.up())) {
            return false;
        }
        IBlockState iBlockState = world.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        if (block == Blocks.chest) {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof TileEntityChest && ((TileEntityChest)tileEntity).numPlayersUsing < 1) {
                return true;
            }
        } else {
            if (block == Blocks.lit_furnace) {
                return true;
            }
            if (block == Blocks.bed && iBlockState.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean continueExecuting() {
        return super.continueExecuting();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.field_151493_a.getAISit().setSitting(false);
    }
}

