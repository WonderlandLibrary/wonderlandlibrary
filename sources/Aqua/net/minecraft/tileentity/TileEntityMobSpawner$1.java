package net.minecraft.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

class TileEntityMobSpawner.1
extends MobSpawnerBaseLogic {
    TileEntityMobSpawner.1() {
    }

    public void func_98267_a(int id) {
        TileEntityMobSpawner.this.worldObj.addBlockEvent(TileEntityMobSpawner.this.pos, Blocks.mob_spawner, id, 0);
    }

    public World getSpawnerWorld() {
        return TileEntityMobSpawner.this.worldObj;
    }

    public BlockPos getSpawnerPosition() {
        return TileEntityMobSpawner.this.pos;
    }

    public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_) {
        super.setRandomEntity(p_98277_1_);
        if (this.getSpawnerWorld() != null) {
            this.getSpawnerWorld().markBlockForUpdate(TileEntityMobSpawner.this.pos);
        }
    }
}
