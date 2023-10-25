package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
static class EntityRabbit.AIRaidFarm
extends EntityAIMoveToBlock {
    private final EntityRabbit rabbit;
    private boolean field_179498_d;
    private boolean field_179499_e = false;

    public EntityRabbit.AIRaidFarm(EntityRabbit rabbitIn) {
        super((EntityCreature)rabbitIn, (double)0.7f, 16);
        this.rabbit = rabbitIn;
    }

    public boolean shouldExecute() {
        if (this.runDelay <= 0) {
            if (!this.rabbit.worldObj.getGameRules().getBoolean("mobGriefing")) {
                return false;
            }
            this.field_179499_e = false;
            this.field_179498_d = EntityRabbit.access$000((EntityRabbit)this.rabbit);
        }
        return super.shouldExecute();
    }

    public boolean continueExecuting() {
        return this.field_179499_e && super.continueExecuting();
    }

    public void startExecuting() {
        super.startExecuting();
    }

    public void resetTask() {
        super.resetTask();
    }

    public void updateTask() {
        super.updateTask();
        this.rabbit.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5, 10.0f, (float)this.rabbit.getVerticalFaceSpeed());
        if (this.getIsAboveDestination()) {
            World world = this.rabbit.worldObj;
            BlockPos blockpos = this.destinationBlock.up();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            if (this.field_179499_e && block instanceof BlockCarrot && (Integer)iblockstate.getValue((IProperty)BlockCarrot.AGE) == 7) {
                world.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
                world.destroyBlock(blockpos, true);
                this.rabbit.createEatingParticles();
            }
            this.field_179499_e = false;
            this.runDelay = 10;
        }
    }

    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        IBlockState iblockstate;
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.farmland && (block = (iblockstate = worldIn.getBlockState(pos = pos.up())).getBlock()) instanceof BlockCarrot && (Integer)iblockstate.getValue((IProperty)BlockCarrot.AGE) == 7 && this.field_179498_d && !this.field_179499_e) {
            this.field_179499_e = true;
            return true;
        }
        return false;
    }
}
