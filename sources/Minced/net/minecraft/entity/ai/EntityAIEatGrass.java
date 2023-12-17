// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.block.properties.IProperty;
import com.google.common.base.Predicates;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;

public class EntityAIEatGrass extends EntityAIBase
{
    private static final Predicate<IBlockState> IS_TALL_GRASS;
    private final EntityLiving grassEaterEntity;
    private final World entityWorld;
    int eatingGrassTimer;
    
    public EntityAIEatGrass(final EntityLiving grassEaterEntityIn) {
        this.grassEaterEntity = grassEaterEntityIn;
        this.entityWorld = grassEaterEntityIn.world;
        this.setMutexBits(7);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 50 : 1000) != 0) {
            return false;
        }
        final BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
        return EntityAIEatGrass.IS_TALL_GRASS.apply((Object)this.entityWorld.getBlockState(blockpos)) || this.entityWorld.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS;
    }
    
    @Override
    public void startExecuting() {
        this.eatingGrassTimer = 40;
        this.entityWorld.setEntityState(this.grassEaterEntity, (byte)10);
        this.grassEaterEntity.getNavigator().clearPath();
    }
    
    @Override
    public void resetTask() {
        this.eatingGrassTimer = 0;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.eatingGrassTimer > 0;
    }
    
    public int getEatingGrassTimer() {
        return this.eatingGrassTimer;
    }
    
    @Override
    public void updateTask() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
        if (this.eatingGrassTimer == 4) {
            final BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
            if (EntityAIEatGrass.IS_TALL_GRASS.apply((Object)this.entityWorld.getBlockState(blockpos))) {
                if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
                    this.entityWorld.destroyBlock(blockpos, false);
                }
                this.grassEaterEntity.eatGrassBonus();
            }
            else {
                final BlockPos blockpos2 = blockpos.down();
                if (this.entityWorld.getBlockState(blockpos2).getBlock() == Blocks.GRASS) {
                    if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
                        this.entityWorld.playEvent(2001, blockpos2, Block.getIdFromBlock(Blocks.GRASS));
                        this.entityWorld.setBlockState(blockpos2, Blocks.DIRT.getDefaultState(), 2);
                    }
                    this.grassEaterEntity.eatGrassBonus();
                }
            }
        }
    }
    
    static {
        IS_TALL_GRASS = (Predicate)BlockStateMatcher.forBlock(Blocks.TALLGRASS).where(BlockTallGrass.TYPE, (com.google.common.base.Predicate<? extends BlockTallGrass.EnumType>)Predicates.equalTo((Object)BlockTallGrass.EnumType.GRASS));
    }
}
