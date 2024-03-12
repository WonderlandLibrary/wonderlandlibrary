// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.util.TileEntityUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntity;

public class RandomTileEntity implements IRandomEntity
{
    private TileEntity tileEntity;
    
    @Override
    public int getId() {
        return Config.getRandom(this.tileEntity.getPos(), this.tileEntity.getBlockMetadata());
    }
    
    @Override
    public BlockPos getSpawnPosition() {
        return this.tileEntity.getPos();
    }
    
    @Override
    public String getName() {
        final String s = TileEntityUtils.getTileEntityName(this.tileEntity);
        return s;
    }
    
    @Override
    public BiomeGenBase getSpawnBiome() {
        return this.tileEntity.getWorld().getBiomeGenForCoords(this.tileEntity.getPos());
    }
    
    @Override
    public int getHealth() {
        return -1;
    }
    
    @Override
    public int getMaxHealth() {
        return -1;
    }
    
    public TileEntity getTileEntity() {
        return this.tileEntity;
    }
    
    public void setTileEntity(final TileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }
}
