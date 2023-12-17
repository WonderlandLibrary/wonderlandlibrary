// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

public class BiomeSavanna extends Biome
{
    private static final WorldGenSavannaTree SAVANNA_TREE;
    
    protected BiomeSavanna(final BiomeProperties properties) {
        super(properties);
        this.spawnableCreatureList.add(new SpawnListEntry(EntityHorse.class, 1, 2, 6));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityDonkey.class, 1, 1, 1));
        if (this.getBaseHeight() > 1.1f) {
            this.spawnableCreatureList.add(new SpawnListEntry(EntityLlama.class, 8, 4, 4));
        }
        this.decorator.treesPerChunk = 1;
        this.decorator.flowersPerChunk = 4;
        this.decorator.grassPerChunk = 20;
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(final Random rand) {
        return (rand.nextInt(5) > 0) ? BiomeSavanna.SAVANNA_TREE : BiomeSavanna.TREE_FEATURE;
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        BiomeSavanna.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
        for (int i = 0; i < 7; ++i) {
            final int j = rand.nextInt(16) + 8;
            final int k = rand.nextInt(16) + 8;
            final int l = rand.nextInt(worldIn.getHeight(pos.add(j, 0, k)).getY() + 32);
            BiomeSavanna.DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j, l, k));
        }
        super.decorate(worldIn, rand, pos);
    }
    
    @Override
    public Class<? extends Biome> getBiomeClass() {
        return BiomeSavanna.class;
    }
    
    static {
        SAVANNA_TREE = new WorldGenSavannaTree(false);
    }
}
