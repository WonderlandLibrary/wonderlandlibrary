// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeHills extends Biome
{
    private final WorldGenerator silverfishSpawner;
    private final WorldGenTaiga2 spruceGenerator;
    private final Type type;
    
    protected BiomeHills(final Type p_i46710_1_, final BiomeProperties properties) {
        super(properties);
        this.silverfishSpawner = new WorldGenMinable(Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE), 9);
        this.spruceGenerator = new WorldGenTaiga2(false);
        if (p_i46710_1_ == Type.EXTRA_TREES) {
            this.decorator.treesPerChunk = 3;
        }
        this.spawnableCreatureList.add(new SpawnListEntry(EntityLlama.class, 5, 4, 6));
        this.type = p_i46710_1_;
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(final Random rand) {
        return (rand.nextInt(3) > 0) ? this.spruceGenerator : super.getRandomTreeFeature(rand);
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        for (int i = 3 + rand.nextInt(6), j = 0; j < i; ++j) {
            final int k = rand.nextInt(16);
            final int l = rand.nextInt(28) + 4;
            final int i2 = rand.nextInt(16);
            final BlockPos blockpos = pos.add(k, l, i2);
            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.STONE) {
                worldIn.setBlockState(blockpos, Blocks.EMERALD_ORE.getDefaultState(), 2);
            }
        }
        for (int j2 = 0; j2 < 7; ++j2) {
            final int k2 = rand.nextInt(16);
            final int l2 = rand.nextInt(64);
            final int i3 = rand.nextInt(16);
            this.silverfishSpawner.generate(worldIn, rand, pos.add(k2, l2, i3));
        }
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int x, final int z, final double noiseVal) {
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        if ((noiseVal < -1.0 || noiseVal > 2.0) && this.type == Type.MUTATED) {
            this.topBlock = Blocks.GRAVEL.getDefaultState();
            this.fillerBlock = Blocks.GRAVEL.getDefaultState();
        }
        else if (noiseVal > 1.0 && this.type != Type.EXTRA_TREES) {
            this.topBlock = Blocks.STONE.getDefaultState();
            this.fillerBlock = Blocks.STONE.getDefaultState();
        }
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
    
    public enum Type
    {
        NORMAL, 
        EXTRA_TREES, 
        MUTATED;
    }
}
