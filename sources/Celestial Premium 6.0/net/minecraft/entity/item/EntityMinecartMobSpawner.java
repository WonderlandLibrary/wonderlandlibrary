/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartMobSpawner
extends EntityMinecart {
    private final MobSpawnerBaseLogic mobSpawnerLogic = new MobSpawnerBaseLogic(){

        @Override
        public void broadcastEvent(int id) {
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

    public EntityMinecartMobSpawner(World worldIn) {
        super(worldIn);
    }

    public EntityMinecartMobSpawner(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public static void registerFixesMinecartMobSpawner(DataFixer fixer) {
        EntityMinecartMobSpawner.registerFixesMinecart(fixer, EntityMinecartMobSpawner.class);
        fixer.registerWalker(FixTypes.ENTITY, new IDataWalker(){

            @Override
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
                String s = compound.getString("id");
                if (EntityList.getKey(EntityMinecartMobSpawner.class).equals(new ResourceLocation(s))) {
                    compound.setString("id", TileEntity.func_190559_a(TileEntityMobSpawner.class).toString());
                    fixer.process(FixTypes.BLOCK_ENTITY, compound, versionIn);
                    compound.setString("id", s);
                }
                return compound;
            }
        });
    }

    @Override
    public EntityMinecart.Type getType() {
        return EntityMinecart.Type.SPAWNER;
    }

    @Override
    public IBlockState getDefaultDisplayTile() {
        return Blocks.MOB_SPAWNER.getDefaultState();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.mobSpawnerLogic.readFromNBT(compound);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        this.mobSpawnerLogic.writeToNBT(compound);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        this.mobSpawnerLogic.setDelayToMin(id);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.mobSpawnerLogic.updateSpawner();
    }
}

