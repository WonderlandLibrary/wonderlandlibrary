/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartMobSpawner
extends EntityMinecart {
    private final MobSpawnerBaseLogic mobSpawnerLogic = new MobSpawnerBaseLogic(){

        @Override
        public void func_98267_a(int n) {
            EntityMinecartMobSpawner.this.worldObj.setEntityState(EntityMinecartMobSpawner.this, (byte)n);
        }

        @Override
        public BlockPos getSpawnerPosition() {
            return new BlockPos(EntityMinecartMobSpawner.this);
        }

        @Override
        public World getSpawnerWorld() {
            return EntityMinecartMobSpawner.this.worldObj;
        }
    };

    @Override
    protected void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.mobSpawnerLogic.readFromNBT(nBTTagCompound);
    }

    @Override
    public EntityMinecart.EnumMinecartType getMinecartType() {
        return EntityMinecart.EnumMinecartType.SPAWNER;
    }

    @Override
    public IBlockState getDefaultDisplayTile() {
        return Blocks.mob_spawner.getDefaultState();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.mobSpawnerLogic.updateSpawner();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        this.mobSpawnerLogic.writeToNBT(nBTTagCompound);
    }

    @Override
    public void handleStatusUpdate(byte by) {
        this.mobSpawnerLogic.setDelayToMin(by);
    }

    public EntityMinecartMobSpawner(World world, double d, double d2, double d3) {
        super(world, d, d2, d3);
    }

    public EntityMinecartMobSpawner(World world) {
        super(world);
    }

    public MobSpawnerBaseLogic func_98039_d() {
        return this.mobSpawnerLogic;
    }
}

