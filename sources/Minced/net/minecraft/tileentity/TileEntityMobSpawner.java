// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.util.ITickable;

public class TileEntityMobSpawner extends TileEntity implements ITickable
{
    private final MobSpawnerBaseLogic spawnerLogic;
    
    public TileEntityMobSpawner() {
        this.spawnerLogic = new MobSpawnerBaseLogic() {
            @Override
            public void broadcastEvent(final int id) {
                TileEntityMobSpawner.this.world.addBlockEvent(TileEntityMobSpawner.this.pos, Blocks.MOB_SPAWNER, id, 0);
            }
            
            @Override
            public World getSpawnerWorld() {
                return TileEntityMobSpawner.this.world;
            }
            
            @Override
            public BlockPos getSpawnerPosition() {
                return TileEntityMobSpawner.this.pos;
            }
            
            @Override
            public void setNextSpawnData(final WeightedSpawnerEntity p_184993_1_) {
                super.setNextSpawnData(p_184993_1_);
                if (this.getSpawnerWorld() != null) {
                    final IBlockState iblockstate = this.getSpawnerWorld().getBlockState(this.getSpawnerPosition());
                    this.getSpawnerWorld().notifyBlockUpdate(TileEntityMobSpawner.this.pos, iblockstate, iblockstate, 4);
                }
            }
        };
    }
    
    public static void registerFixesMobSpawner(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new IDataWalker() {
            @Override
            public NBTTagCompound process(final IDataFixer fixer, final NBTTagCompound compound, final int versionIn) {
                if (TileEntity.getKey(TileEntityMobSpawner.class).equals(new ResourceLocation(compound.getString("id")))) {
                    if (compound.hasKey("SpawnPotentials", 9)) {
                        final NBTTagList nbttaglist = compound.getTagList("SpawnPotentials", 10);
                        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                            nbttagcompound.setTag("Entity", fixer.process(FixTypes.ENTITY, nbttagcompound.getCompoundTag("Entity"), versionIn));
                        }
                    }
                    compound.setTag("SpawnData", fixer.process(FixTypes.ENTITY, compound.getCompoundTag("SpawnData"), versionIn));
                }
                return compound;
            }
        });
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.spawnerLogic.readFromNBT(compound);
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.spawnerLogic.writeToNBT(compound);
        return compound;
    }
    
    @Override
    public void update() {
        this.spawnerLogic.updateSpawner();
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        final NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
        nbttagcompound.removeTag("SpawnPotentials");
        return nbttagcompound;
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        return this.spawnerLogic.setDelayToMin(id) || super.receiveClientEvent(id, type);
    }
    
    @Override
    public boolean onlyOpsCanSetNbt() {
        return true;
    }
    
    public MobSpawnerBaseLogic getSpawnerBaseLogic() {
        return this.spawnerLogic;
    }
}
