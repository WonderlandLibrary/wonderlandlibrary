/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderDebug
implements IChunkProvider {
    private final World world;
    private static final List<IBlockState> field_177464_a = Lists.newArrayList();
    private static final int field_177462_b;
    private static final int field_181039_c;

    @Override
    public String makeString() {
        return "DebugLevelSource";
    }

    @Override
    public boolean saveChunks(boolean bl, IProgressUpdate iProgressUpdate) {
        return true;
    }

    @Override
    public void recreateStructures(Chunk chunk, int n, int n2) {
    }

    public ChunkProviderDebug(World world) {
        this.world = world;
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumCreatureType, BlockPos blockPos) {
        BiomeGenBase biomeGenBase = this.world.getBiomeGenForCoords(blockPos);
        return biomeGenBase.getSpawnableList(enumCreatureType);
    }

    @Override
    public BlockPos getStrongholdGen(World world, String string, BlockPos blockPos) {
        return null;
    }

    @Override
    public Chunk provideChunk(BlockPos blockPos) {
        return this.provideChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4);
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public void populate(IChunkProvider iChunkProvider, int n, int n2) {
    }

    public static IBlockState func_177461_b(int n, int n2) {
        int n3;
        IBlockState iBlockState = null;
        if (n > 0 && n2 > 0 && n % 2 != 0 && n2 % 2 != 0 && (n /= 2) <= field_177462_b && (n2 /= 2) <= field_181039_c && (n3 = MathHelper.abs_int(n * field_177462_b + n2)) < field_177464_a.size()) {
            iBlockState = field_177464_a.get(n3);
        }
        return iBlockState;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    static {
        for (Block block : Block.blockRegistry) {
            field_177464_a.addAll((Collection<IBlockState>)block.getBlockState().getValidStates());
        }
        field_177462_b = MathHelper.ceiling_float_int(MathHelper.sqrt_float(field_177464_a.size()));
        field_181039_c = MathHelper.ceiling_float_int((float)field_177464_a.size() / (float)field_177462_b);
    }

    @Override
    public void saveExtraData() {
    }

    @Override
    public boolean chunkExists(int n, int n2) {
        return true;
    }

    @Override
    public Chunk provideChunk(int n, int n2) {
        int n3;
        ChunkPrimer chunkPrimer = new ChunkPrimer();
        int n4 = 0;
        while (n4 < 16) {
            int n5 = 0;
            while (n5 < 16) {
                int n6 = n * 16 + n4;
                n3 = n2 * 16 + n5;
                chunkPrimer.setBlockState(n4, 60, n5, Blocks.barrier.getDefaultState());
                IBlockState iBlockState = ChunkProviderDebug.func_177461_b(n6, n3);
                if (iBlockState != null) {
                    chunkPrimer.setBlockState(n4, 70, n5, iBlockState);
                }
                ++n5;
            }
            ++n4;
        }
        Chunk chunk = new Chunk(this.world, chunkPrimer, n, n2);
        chunk.generateSkylightMap();
        BiomeGenBase[] biomeGenBaseArray = this.world.getWorldChunkManager().loadBlockGeneratorData(null, n * 16, n2 * 16, 16, 16);
        byte[] byArray = chunk.getBiomeArray();
        n3 = 0;
        while (n3 < byArray.length) {
            byArray[n3] = (byte)biomeGenBaseArray[n3].biomeID;
            ++n3;
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public boolean func_177460_a(IChunkProvider iChunkProvider, Chunk chunk, int n, int n2) {
        return false;
    }
}

