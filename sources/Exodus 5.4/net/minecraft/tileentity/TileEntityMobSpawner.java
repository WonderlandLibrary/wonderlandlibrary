/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class TileEntityMobSpawner
extends TileEntity
implements ITickable {
    private final MobSpawnerBaseLogic spawnerLogic = new MobSpawnerBaseLogic(){

        @Override
        public BlockPos getSpawnerPosition() {
            return TileEntityMobSpawner.this.pos;
        }

        @Override
        public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart weightedRandomMinecart) {
            super.setRandomEntity(weightedRandomMinecart);
            if (this.getSpawnerWorld() != null) {
                this.getSpawnerWorld().markBlockForUpdate(TileEntityMobSpawner.this.pos);
            }
        }

        @Override
        public void func_98267_a(int n) {
            TileEntityMobSpawner.this.worldObj.addBlockEvent(TileEntityMobSpawner.this.pos, Blocks.mob_spawner, n, 0);
        }

        @Override
        public World getSpawnerWorld() {
            return TileEntityMobSpawner.this.worldObj;
        }
    };

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        this.writeToNBT(nBTTagCompound);
        nBTTagCompound.removeTag("SpawnPotentials");
        return new S35PacketUpdateTileEntity(this.pos, 1, nBTTagCompound);
    }

    public MobSpawnerBaseLogic getSpawnerBaseLogic() {
        return this.spawnerLogic;
    }

    @Override
    public boolean receiveClientEvent(int n, int n2) {
        return this.spawnerLogic.setDelayToMin(n) ? true : super.receiveClientEvent(n, n2);
    }

    @Override
    public boolean func_183000_F() {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        this.spawnerLogic.readFromNBT(nBTTagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        this.spawnerLogic.writeToNBT(nBTTagCompound);
    }

    @Override
    public void update() {
        this.spawnerLogic.updateSpawner();
    }
}

