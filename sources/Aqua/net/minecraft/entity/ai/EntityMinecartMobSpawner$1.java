package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

class EntityMinecartMobSpawner.1
extends MobSpawnerBaseLogic {
    EntityMinecartMobSpawner.1() {
    }

    public void func_98267_a(int id) {
        EntityMinecartMobSpawner.this.worldObj.setEntityState((Entity)EntityMinecartMobSpawner.this, (byte)id);
    }

    public World getSpawnerWorld() {
        return EntityMinecartMobSpawner.this.worldObj;
    }

    public BlockPos getSpawnerPosition() {
        return new BlockPos((Entity)EntityMinecartMobSpawner.this);
    }
}
