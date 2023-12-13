// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen;

import java.util.Iterator;
import java.util.Collection;
import net.minecraft.block.Block;
import com.google.common.collect.Lists;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import java.util.List;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderDebug implements IChunkProvider
{
    private static final List field_177464_a;
    private static final int field_177462_b;
    private final World field_177463_c;
    private static final String __OBFID = "CL_00002002";
    
    public ChunkProviderDebug(final World worldIn) {
        this.field_177463_c = worldIn;
    }
    
    @Override
    public Chunk provideChunk(final int p_73154_1_, final int p_73154_2_) {
        final ChunkPrimer var3 = new ChunkPrimer();
        for (int var4 = 0; var4 < 16; ++var4) {
            for (int var5 = 0; var5 < 16; ++var5) {
                final int var6 = p_73154_1_ * 16 + var4;
                final int var7 = p_73154_2_ * 16 + var5;
                var3.setBlockState(var4, 60, var5, Blocks.barrier.getDefaultState());
                final IBlockState var8 = func_177461_b(var6, var7);
                if (var8 != null) {
                    var3.setBlockState(var4, 70, var5, var8);
                }
            }
        }
        final Chunk var9 = new Chunk(this.field_177463_c, var3, p_73154_1_, p_73154_2_);
        var9.generateSkylightMap();
        final BiomeGenBase[] var10 = this.field_177463_c.getWorldChunkManager().loadBlockGeneratorData(null, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        int var7;
        byte[] var11;
        for (var11 = var9.getBiomeArray(), var7 = 0; var7 < var11.length; ++var7) {
            var11[var7] = (byte)var10[var7].biomeID;
        }
        var9.generateSkylightMap();
        return var9;
    }
    
    public static IBlockState func_177461_b(int p_177461_0_, int p_177461_1_) {
        IBlockState var2 = null;
        if (p_177461_0_ > 0 && p_177461_1_ > 0 && p_177461_0_ % 2 != 0 && p_177461_1_ % 2 != 0) {
            p_177461_0_ /= 2;
            p_177461_1_ /= 2;
            if (p_177461_0_ <= ChunkProviderDebug.field_177462_b && p_177461_1_ <= ChunkProviderDebug.field_177462_b) {
                final int var3 = MathHelper.abs_int(p_177461_0_ * ChunkProviderDebug.field_177462_b + p_177461_1_);
                if (var3 < ChunkProviderDebug.field_177464_a.size()) {
                    var2 = ChunkProviderDebug.field_177464_a.get(var3);
                }
            }
        }
        return var2;
    }
    
    @Override
    public boolean chunkExists(final int p_73149_1_, final int p_73149_2_) {
        return true;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        return false;
    }
    
    @Override
    public boolean saveChunks(final boolean p_73151_1_, final IProgressUpdate p_73151_2_) {
        return true;
    }
    
    @Override
    public void saveExtraData() {
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }
    
    @Override
    public boolean canSave() {
        return true;
    }
    
    @Override
    public String makeString() {
        return "DebugLevelSource";
    }
    
    @Override
    public List func_177458_a(final EnumCreatureType p_177458_1_, final BlockPos p_177458_2_) {
        final BiomeGenBase var3 = this.field_177463_c.getBiomeGenForCoords(p_177458_2_);
        return var3.getSpawnableList(p_177458_1_);
    }
    
    @Override
    public BlockPos func_180513_a(final World worldIn, final String p_180513_2_, final BlockPos p_180513_3_) {
        return null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return 0;
    }
    
    @Override
    public void func_180514_a(final Chunk p_180514_1_, final int p_180514_2_, final int p_180514_3_) {
    }
    
    @Override
    public Chunk func_177459_a(final BlockPos p_177459_1_) {
        return this.provideChunk(p_177459_1_.getX() >> 4, p_177459_1_.getZ() >> 4);
    }
    
    static {
        field_177464_a = Lists.newArrayList();
        for (final Block var2 : Block.blockRegistry) {
            ChunkProviderDebug.field_177464_a.addAll((Collection)var2.getBlockState().getValidStates());
        }
        field_177462_b = MathHelper.ceiling_float_int(MathHelper.sqrt_float(ChunkProviderDebug.field_177464_a.size()));
    }
}
