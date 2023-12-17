// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenFossils;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.World;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.block.state.IBlockState;

public class BiomeSwamp extends Biome
{
    protected static final IBlockState WATER_LILY;
    
    protected BiomeSwamp(final BiomeProperties properties) {
        super(properties);
        this.decorator.treesPerChunk = 2;
        this.decorator.flowersPerChunk = 1;
        this.decorator.deadBushPerChunk = 1;
        this.decorator.mushroomsPerChunk = 8;
        this.decorator.reedsPerChunk = 10;
        this.decorator.clayPerChunk = 1;
        this.decorator.waterlilyPerChunk = 4;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 0;
        this.decorator.grassPerChunk = 5;
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 1, 1, 1));
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(final Random rand) {
        return BiomeSwamp.SWAMP_FEATURE;
    }
    
    @Override
    public int getGrassColorAtPos(final BlockPos pos) {
        final double d0 = BiomeSwamp.GRASS_COLOR_NOISE.getValue(pos.getX() * 0.0225, pos.getZ() * 0.0225);
        return (d0 < -0.1) ? 5011004 : 6975545;
    }
    
    @Override
    public int getFoliageColorAtPos(final BlockPos pos) {
        return 6975545;
    }
    
    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(final Random rand, final BlockPos pos) {
        return BlockFlower.EnumFlowerType.BLUE_ORCHID;
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int x, final int z, final double noiseVal) {
        final double d0 = BiomeSwamp.GRASS_COLOR_NOISE.getValue(x * 0.25, z * 0.25);
        if (d0 > 0.0) {
            final int i = x & 0xF;
            final int j = z & 0xF;
            int k = 255;
            while (k >= 0) {
                if (chunkPrimerIn.getBlockState(j, k, i).getMaterial() != Material.AIR) {
                    if (k != 62 || chunkPrimerIn.getBlockState(j, k, i).getBlock() == Blocks.WATER) {
                        break;
                    }
                    chunkPrimerIn.setBlockState(j, k, i, BiomeSwamp.WATER);
                    if (d0 < 0.12) {
                        chunkPrimerIn.setBlockState(j, k + 1, i, BiomeSwamp.WATER_LILY);
                        break;
                    }
                    break;
                }
                else {
                    --k;
                }
            }
        }
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        if (rand.nextInt(64) == 0) {
            new WorldGenFossils().generate(worldIn, rand, pos);
        }
    }
    
    static {
        WATER_LILY = Blocks.WATERLILY.getDefaultState();
    }
}
