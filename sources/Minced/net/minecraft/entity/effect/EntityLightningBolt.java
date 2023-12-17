// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.effect;

import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityLightningBolt extends EntityWeatherEffect
{
    private int lightningState;
    public long boltVertex;
    private int boltLivingTime;
    private final boolean effectOnly;
    
    public EntityLightningBolt(final World worldIn, final double x, final double y, final double z, final boolean effectOnlyIn) {
        super(worldIn);
        this.setLocationAndAngles(x, y, z, 0.0f, 0.0f);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
        this.effectOnly = effectOnlyIn;
        final BlockPos blockpos = new BlockPos(this);
        if (!effectOnlyIn && !worldIn.isRemote && worldIn.getGameRules().getBoolean("doFireTick") && (worldIn.getDifficulty() == EnumDifficulty.NORMAL || worldIn.getDifficulty() == EnumDifficulty.HARD) && worldIn.isAreaLoaded(blockpos, 10)) {
            if (worldIn.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(worldIn, blockpos)) {
                worldIn.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
            }
            for (int i = 0; i < 4; ++i) {
                final BlockPos blockpos2 = blockpos.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);
                if (worldIn.getBlockState(blockpos2).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(worldIn, blockpos2)) {
                    worldIn.setBlockState(blockpos2, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }
    
    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.WEATHER;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.lightningState == 2) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0f, 0.8f + this.rand.nextFloat() * 0.2f);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0f, 0.5f + this.rand.nextFloat() * 0.2f);
        }
        --this.lightningState;
        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                this.setDead();
            }
            else if (this.lightningState < -this.rand.nextInt(10)) {
                --this.boltLivingTime;
                this.lightningState = 1;
                if (!this.effectOnly && !this.world.isRemote) {
                    this.boltVertex = this.rand.nextLong();
                    final BlockPos blockpos = new BlockPos(this);
                    if (this.world.getGameRules().getBoolean("doFireTick") && this.world.isAreaLoaded(blockpos, 10) && this.world.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(this.world, blockpos)) {
                        this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                    }
                }
            }
        }
        if (this.lightningState >= 0) {
            if (this.world.isRemote) {
                this.world.setLastLightningBolt(2);
            }
            else if (!this.effectOnly) {
                final double d0 = 3.0;
                final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - 3.0, this.posY - 3.0, this.posZ - 3.0, this.posX + 3.0, this.posY + 6.0 + 3.0, this.posZ + 3.0));
                for (int i = 0; i < list.size(); ++i) {
                    final Entity entity = list.get(i);
                    entity.onStruckByLightning(this);
                }
            }
        }
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
    }
}
