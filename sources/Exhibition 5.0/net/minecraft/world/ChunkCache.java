// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache implements IBlockAccess
{
    protected int chunkX;
    protected int chunkZ;
    protected Chunk[][] chunkArray;
    protected boolean hasExtendedLevels;
    protected World worldObj;
    private static final String __OBFID = "CL_00000155";
    
    public ChunkCache(final World worldIn, final BlockPos p_i45746_2_, final BlockPos p_i45746_3_, final int p_i45746_4_) {
        this.worldObj = worldIn;
        this.chunkX = p_i45746_2_.getX() - p_i45746_4_ >> 4;
        this.chunkZ = p_i45746_2_.getZ() - p_i45746_4_ >> 4;
        final int var5 = p_i45746_3_.getX() + p_i45746_4_ >> 4;
        final int var6 = p_i45746_3_.getZ() + p_i45746_4_ >> 4;
        this.chunkArray = new Chunk[var5 - this.chunkX + 1][var6 - this.chunkZ + 1];
        this.hasExtendedLevels = true;
        for (int var7 = this.chunkX; var7 <= var5; ++var7) {
            for (int var8 = this.chunkZ; var8 <= var6; ++var8) {
                this.chunkArray[var7 - this.chunkX][var8 - this.chunkZ] = worldIn.getChunkFromChunkCoords(var7, var8);
            }
        }
        for (int var7 = p_i45746_2_.getX() >> 4; var7 <= p_i45746_3_.getX() >> 4; ++var7) {
            for (int var8 = p_i45746_2_.getZ() >> 4; var8 <= p_i45746_3_.getZ() >> 4; ++var8) {
                final Chunk var9 = this.chunkArray[var7 - this.chunkX][var8 - this.chunkZ];
                if (var9 != null && !var9.getAreLevelsEmpty(p_i45746_2_.getY(), p_i45746_3_.getY())) {
                    this.hasExtendedLevels = false;
                }
            }
        }
    }
    
    @Override
    public boolean extendedLevelsInChunkCache() {
        return this.hasExtendedLevels;
    }
    
    @Override
    public TileEntity getTileEntity(final BlockPos pos) {
        final int var2 = (pos.getX() >> 4) - this.chunkX;
        final int var3 = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[var2][var3].func_177424_a(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
    }
    
    @Override
    public int getCombinedLight(final BlockPos p_175626_1_, final int p_175626_2_) {
        final int var3 = this.func_175629_a(EnumSkyBlock.SKY, p_175626_1_);
        int var4 = this.func_175629_a(EnumSkyBlock.BLOCK, p_175626_1_);
        if (var4 < p_175626_2_) {
            var4 = p_175626_2_;
        }
        return var3 << 20 | var4 << 4;
    }
    
    @Override
    public IBlockState getBlockState(final BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final int var2 = (pos.getX() >> 4) - this.chunkX;
            final int var3 = (pos.getZ() >> 4) - this.chunkZ;
            if (var2 >= 0 && var2 < this.chunkArray.length && var3 >= 0 && var3 < this.chunkArray[var2].length) {
                final Chunk var4 = this.chunkArray[var2][var3];
                if (var4 != null) {
                    return var4.getBlockState(pos);
                }
            }
        }
        return Blocks.air.getDefaultState();
    }
    
    @Override
    public BiomeGenBase getBiomeGenForCoords(final BlockPos pos) {
        return this.worldObj.getBiomeGenForCoords(pos);
    }
    
    private int func_175629_a(final EnumSkyBlock p_175629_1_, final BlockPos p_175629_2_) {
        if (p_175629_1_ == EnumSkyBlock.SKY && this.worldObj.provider.getHasNoSky()) {
            return 0;
        }
        if (p_175629_2_.getY() < 0 || p_175629_2_.getY() >= 256) {
            return p_175629_1_.defaultLightValue;
        }
        if (this.getBlockState(p_175629_2_).getBlock().getUseNeighborBrightness()) {
            int var3 = 0;
            for (final EnumFacing var7 : EnumFacing.values()) {
                final int var8 = this.func_175628_b(p_175629_1_, p_175629_2_.offset(var7));
                if (var8 > var3) {
                    var3 = var8;
                }
                if (var3 >= 15) {
                    return var3;
                }
            }
            return var3;
        }
        int var3 = (p_175629_2_.getX() >> 4) - this.chunkX;
        final int var9 = (p_175629_2_.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[var3][var9].getLightFor(p_175629_1_, p_175629_2_);
    }
    
    @Override
    public boolean isAirBlock(final BlockPos pos) {
        return this.getBlockState(pos).getBlock().getMaterial() == Material.air;
    }
    
    public int func_175628_b(final EnumSkyBlock p_175628_1_, final BlockPos p_175628_2_) {
        if (p_175628_2_.getY() >= 0 && p_175628_2_.getY() < 256) {
            final int var3 = (p_175628_2_.getX() >> 4) - this.chunkX;
            final int var4 = (p_175628_2_.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[var3][var4].getLightFor(p_175628_1_, p_175628_2_);
        }
        return p_175628_1_.defaultLightValue;
    }
    
    @Override
    public int getStrongPower(final BlockPos pos, final EnumFacing direction) {
        final IBlockState var3 = this.getBlockState(pos);
        return var3.getBlock().isProvidingStrongPower(this, pos, var3, direction);
    }
    
    @Override
    public WorldType getWorldType() {
        return this.worldObj.getWorldType();
    }
}
