// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.world.chunk.ChunkPrimer;
import java.util.Random;
import net.minecraft.world.World;

public class BiomeGenOcean extends BiomeGenBase
{
    public BiomeGenOcean(final int id) {
        super(id);
        this.spawnableCreatureList.clear();
    }
    
    @Override
    public TempCategory getTempCategory() {
        return TempCategory.OCEAN;
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int x, final int z, final double noiseVal) {
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}
