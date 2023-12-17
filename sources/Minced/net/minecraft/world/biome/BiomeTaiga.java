// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTaiga1;

public class BiomeTaiga extends Biome
{
    private static final WorldGenTaiga1 PINE_GENERATOR;
    private static final WorldGenTaiga2 SPRUCE_GENERATOR;
    private static final WorldGenMegaPineTree MEGA_PINE_GENERATOR;
    private static final WorldGenMegaPineTree MEGA_SPRUCE_GENERATOR;
    private static final WorldGenBlockBlob FOREST_ROCK_GENERATOR;
    private final Type type;
    
    public BiomeTaiga(final Type typeIn, final BiomeProperties properties) {
        super(properties);
        this.type = typeIn;
        this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 8, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        this.decorator.treesPerChunk = 10;
        if (typeIn != Type.MEGA && typeIn != Type.MEGA_SPRUCE) {
            this.decorator.grassPerChunk = 1;
            this.decorator.mushroomsPerChunk = 1;
        }
        else {
            this.decorator.grassPerChunk = 7;
            this.decorator.deadBushPerChunk = 1;
            this.decorator.mushroomsPerChunk = 3;
        }
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(final Random rand) {
        if ((this.type == Type.MEGA || this.type == Type.MEGA_SPRUCE) && rand.nextInt(3) == 0) {
            return (this.type != Type.MEGA_SPRUCE && rand.nextInt(13) != 0) ? BiomeTaiga.MEGA_PINE_GENERATOR : BiomeTaiga.MEGA_SPRUCE_GENERATOR;
        }
        return (rand.nextInt(3) == 0) ? BiomeTaiga.PINE_GENERATOR : BiomeTaiga.SPRUCE_GENERATOR;
    }
    
    @Override
    public WorldGenerator getRandomWorldGenForGrass(final Random rand) {
        return (rand.nextInt(5) > 0) ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        if (this.type == Type.MEGA || this.type == Type.MEGA_SPRUCE) {
            for (int i = rand.nextInt(3), j = 0; j < i; ++j) {
                final int k = rand.nextInt(16) + 8;
                final int l = rand.nextInt(16) + 8;
                final BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
                BiomeTaiga.FOREST_ROCK_GENERATOR.generate(worldIn, rand, blockpos);
            }
        }
        BiomeTaiga.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);
        for (int i2 = 0; i2 < 7; ++i2) {
            final int j2 = rand.nextInt(16) + 8;
            final int k2 = rand.nextInt(16) + 8;
            final int l2 = rand.nextInt(worldIn.getHeight(pos.add(j2, 0, k2)).getY() + 32);
            BiomeTaiga.DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j2, l2, k2));
        }
        super.decorate(worldIn, rand, pos);
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int x, final int z, final double noiseVal) {
        if (this.type == Type.MEGA || this.type == Type.MEGA_SPRUCE) {
            this.topBlock = Blocks.GRASS.getDefaultState();
            this.fillerBlock = Blocks.DIRT.getDefaultState();
            if (noiseVal > 1.75) {
                this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
            }
            else if (noiseVal > -0.95) {
                this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
            }
        }
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
    
    static {
        PINE_GENERATOR = new WorldGenTaiga1();
        SPRUCE_GENERATOR = new WorldGenTaiga2(false);
        MEGA_PINE_GENERATOR = new WorldGenMegaPineTree(false, false);
        MEGA_SPRUCE_GENERATOR = new WorldGenMegaPineTree(false, true);
        FOREST_ROCK_GENERATOR = new WorldGenBlockBlob(Blocks.MOSSY_COBBLESTONE, 0);
    }
    
    public enum Type
    {
        NORMAL, 
        MEGA, 
        MEGA_SPRUCE;
    }
}
