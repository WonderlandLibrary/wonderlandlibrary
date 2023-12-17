// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import java.util.Iterator;
import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import java.util.List;

public class ChunkGeneratorDebug implements IChunkGenerator
{
    private static final List<IBlockState> ALL_VALID_STATES;
    private static final int GRID_WIDTH;
    private static final int GRID_HEIGHT;
    protected static final IBlockState AIR;
    protected static final IBlockState BARRIER;
    private final World world;
    
    public ChunkGeneratorDebug(final World worldIn) {
        this.world = worldIn;
    }
    
    @Override
    public Chunk generateChunk(final int x, final int z) {
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                final int k = x * 16 + i;
                final int l = z * 16 + j;
                chunkprimer.setBlockState(i, 60, j, ChunkGeneratorDebug.BARRIER);
                final IBlockState iblockstate = getBlockStateFor(k, l);
                if (iblockstate != null) {
                    chunkprimer.setBlockState(i, 70, j, iblockstate);
                }
            }
        }
        final Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        chunk.generateSkylightMap();
        final Biome[] abiome = this.world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
        final byte[] abyte = chunk.getBiomeArray();
        for (int i2 = 0; i2 < abyte.length; ++i2) {
            abyte[i2] = (byte)Biome.getIdForBiome(abiome[i2]);
        }
        chunk.generateSkylightMap();
        return chunk;
    }
    
    public static IBlockState getBlockStateFor(int p_177461_0_, int p_177461_1_) {
        IBlockState iblockstate = ChunkGeneratorDebug.AIR;
        if (p_177461_0_ > 0 && p_177461_1_ > 0 && p_177461_0_ % 2 != 0 && p_177461_1_ % 2 != 0) {
            p_177461_0_ /= 2;
            p_177461_1_ /= 2;
            if (p_177461_0_ <= ChunkGeneratorDebug.GRID_WIDTH && p_177461_1_ <= ChunkGeneratorDebug.GRID_HEIGHT) {
                final int i = MathHelper.abs(p_177461_0_ * ChunkGeneratorDebug.GRID_WIDTH + p_177461_1_);
                if (i < ChunkGeneratorDebug.ALL_VALID_STATES.size()) {
                    iblockstate = ChunkGeneratorDebug.ALL_VALID_STATES.get(i);
                }
            }
        }
        return iblockstate;
    }
    
    @Override
    public void populate(final int x, final int z) {
    }
    
    @Override
    public boolean generateStructures(final Chunk chunkIn, final int x, final int z) {
        return false;
    }
    
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        final Biome biome = this.world.getBiome(pos);
        return biome.getSpawnableList(creatureType);
    }
    
    @Nullable
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final String structureName, final BlockPos position, final boolean findUnexplored) {
        return null;
    }
    
    @Override
    public boolean isInsideStructure(final World worldIn, final String structureName, final BlockPos pos) {
        return false;
    }
    
    @Override
    public void recreateStructures(final Chunk chunkIn, final int x, final int z) {
    }
    
    static {
        ALL_VALID_STATES = Lists.newArrayList();
        AIR = Blocks.AIR.getDefaultState();
        BARRIER = Blocks.BARRIER.getDefaultState();
        for (final Block block : Block.REGISTRY) {
            ChunkGeneratorDebug.ALL_VALID_STATES.addAll((Collection<? extends IBlockState>)block.getBlockState().getValidStates());
        }
        GRID_WIDTH = MathHelper.ceil(MathHelper.sqrt((float)ChunkGeneratorDebug.ALL_VALID_STATES.size()));
        GRID_HEIGHT = MathHelper.ceil(ChunkGeneratorDebug.ALL_VALID_STATES.size() / (float)ChunkGeneratorDebug.GRID_WIDTH);
    }
}
