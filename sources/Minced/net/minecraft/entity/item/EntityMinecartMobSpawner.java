// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.tileentity.MobSpawnerBaseLogic;

public class EntityMinecartMobSpawner extends EntityMinecart
{
    private final MobSpawnerBaseLogic mobSpawnerLogic;
    
    public EntityMinecartMobSpawner(final World worldIn) {
        super(worldIn);
        this.mobSpawnerLogic = new MobSpawnerBaseLogic() {
            @Override
            public void broadcastEvent(final int id) {
                EntityMinecartMobSpawner.this.world.setEntityState(EntityMinecartMobSpawner.this, (byte)id);
            }
            
            @Override
            public World getSpawnerWorld() {
                return EntityMinecartMobSpawner.this.world;
            }
            
            @Override
            public BlockPos getSpawnerPosition() {
                return new BlockPos(EntityMinecartMobSpawner.this);
            }
        };
    }
    
    public EntityMinecartMobSpawner(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
        this.mobSpawnerLogic = new MobSpawnerBaseLogic() {
            @Override
            public void broadcastEvent(final int id) {
                EntityMinecartMobSpawner.this.world.setEntityState(EntityMinecartMobSpawner.this, (byte)id);
            }
            
            @Override
            public World getSpawnerWorld() {
                return EntityMinecartMobSpawner.this.world;
            }
            
            @Override
            public BlockPos getSpawnerPosition() {
                return new BlockPos(EntityMinecartMobSpawner.this);
            }
        };
    }
    
    public static void registerFixesMinecartMobSpawner(final DataFixer fixer) {
        EntityMinecart.registerFixesMinecart(fixer, EntityMinecartMobSpawner.class);
        fixer.registerWalker(FixTypes.ENTITY, new IDataWalker() {
            @Override
            public NBTTagCompound process(final IDataFixer fixer, final NBTTagCompound compound, final int versionIn) {
                final String s = compound.getString("id");
                if (EntityList.getKey(EntityMinecartMobSpawner.class).equals(new ResourceLocation(s))) {
                    compound.setString("id", TileEntity.getKey(TileEntityMobSpawner.class).toString());
                    fixer.process(FixTypes.BLOCK_ENTITY, compound, versionIn);
                    compound.setString("id", s);
                }
                return compound;
            }
        });
    }
    
    @Override
    public Type getType() {
        return Type.SPAWNER;
    }
    
    @Override
    public IBlockState getDefaultDisplayTile() {
        return Blocks.MOB_SPAWNER.getDefaultState();
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.mobSpawnerLogic.readFromNBT(compound);
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        this.mobSpawnerLogic.writeToNBT(compound);
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        this.mobSpawnerLogic.setDelayToMin(id);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.mobSpawnerLogic.updateSpawner();
    }
}
