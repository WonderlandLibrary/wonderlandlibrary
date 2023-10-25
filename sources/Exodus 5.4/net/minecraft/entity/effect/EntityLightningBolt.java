/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.effect;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityLightningBolt
extends EntityWeatherEffect {
    private int lightningState;
    private int boltLivingTime;
    public long boltVertex;

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.lightningState == 2) {
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0f, 0.8f + this.rand.nextFloat() * 0.2f);
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0f, 0.5f + this.rand.nextFloat() * 0.2f);
        }
        --this.lightningState;
        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                this.setDead();
            } else if (this.lightningState < -this.rand.nextInt(10)) {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();
                BlockPos blockPos = new BlockPos(this);
                if (!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("doFireTick") && this.worldObj.isAreaLoaded(blockPos, 10) && this.worldObj.getBlockState(blockPos).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(this.worldObj, blockPos)) {
                    this.worldObj.setBlockState(blockPos, Blocks.fire.getDefaultState());
                }
            }
        }
        if (this.lightningState >= 0) {
            if (this.worldObj.isRemote) {
                this.worldObj.setLastLightningBolt(2);
            } else {
                double d = 3.0;
                List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - d, this.posY - d, this.posZ - d, this.posX + d, this.posY + 6.0 + d, this.posZ + d));
                int n = 0;
                while (n < list.size()) {
                    Entity entity = list.get(n);
                    entity.onStruckByLightning(this);
                    ++n;
                }
            }
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
    }

    @Override
    protected void entityInit() {
    }

    public EntityLightningBolt(World world, double d, double d2, double d3) {
        super(world);
        this.setLocationAndAngles(d, d2, d3, 0.0f, 0.0f);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
        BlockPos blockPos = new BlockPos(this);
        if (!world.isRemote && world.getGameRules().getBoolean("doFireTick") && (world.getDifficulty() == EnumDifficulty.NORMAL || world.getDifficulty() == EnumDifficulty.HARD) && world.isAreaLoaded(blockPos, 10)) {
            if (world.getBlockState(blockPos).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(world, blockPos)) {
                world.setBlockState(blockPos, Blocks.fire.getDefaultState());
            }
            int n = 0;
            while (n < 4) {
                BlockPos blockPos2 = blockPos.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);
                if (world.getBlockState(blockPos2).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(world, blockPos2)) {
                    world.setBlockState(blockPos2, Blocks.fire.getDefaultState());
                }
                ++n;
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
    }
}

