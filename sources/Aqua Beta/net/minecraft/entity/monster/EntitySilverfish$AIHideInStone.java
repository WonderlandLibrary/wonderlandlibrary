package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

static class EntitySilverfish.AIHideInStone
extends EntityAIWander {
    private final EntitySilverfish silverfish;
    private EnumFacing facing;
    private boolean field_179484_c;

    public EntitySilverfish.AIHideInStone(EntitySilverfish silverfishIn) {
        super((EntityCreature)silverfishIn, 1.0, 10);
        this.silverfish = silverfishIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (this.silverfish.getAttackTarget() != null) {
            return false;
        }
        if (!this.silverfish.getNavigator().noPath()) {
            return false;
        }
        Random random = this.silverfish.getRNG();
        if (random.nextInt(10) == 0) {
            this.facing = EnumFacing.random((Random)random);
            BlockPos blockpos = new BlockPos(this.silverfish.posX, this.silverfish.posY + 0.5, this.silverfish.posZ).offset(this.facing);
            IBlockState iblockstate = this.silverfish.worldObj.getBlockState(blockpos);
            if (BlockSilverfish.canContainSilverfish((IBlockState)iblockstate)) {
                this.field_179484_c = true;
                return true;
            }
        }
        this.field_179484_c = false;
        return super.shouldExecute();
    }

    public boolean continueExecuting() {
        return this.field_179484_c ? false : super.continueExecuting();
    }

    public void startExecuting() {
        if (!this.field_179484_c) {
            super.startExecuting();
        } else {
            World world = this.silverfish.worldObj;
            BlockPos blockpos = new BlockPos(this.silverfish.posX, this.silverfish.posY + 0.5, this.silverfish.posZ).offset(this.facing);
            IBlockState iblockstate = world.getBlockState(blockpos);
            if (BlockSilverfish.canContainSilverfish((IBlockState)iblockstate)) {
                world.setBlockState(blockpos, Blocks.monster_egg.getDefaultState().withProperty((IProperty)BlockSilverfish.VARIANT, (Comparable)BlockSilverfish.EnumType.forModelBlock((IBlockState)iblockstate)), 3);
                this.silverfish.spawnExplosionParticle();
                this.silverfish.setDead();
            }
        }
    }
}
