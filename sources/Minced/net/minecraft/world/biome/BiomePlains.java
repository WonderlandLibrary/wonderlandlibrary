// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.world.World;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityHorse;

public class BiomePlains extends Biome
{
    protected boolean sunflowers;
    
    protected BiomePlains(final boolean p_i46699_1_, final BiomeProperties properties) {
        super(properties);
        this.sunflowers = p_i46699_1_;
        this.spawnableCreatureList.add(new SpawnListEntry(EntityHorse.class, 5, 2, 6));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityDonkey.class, 1, 1, 3));
        this.decorator.treesPerChunk = 0;
        this.decorator.extraTreeChance = 0.05f;
        this.decorator.flowersPerChunk = 4;
        this.decorator.grassPerChunk = 10;
    }
    
    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(final Random rand, final BlockPos pos) {
        final double d0 = BiomePlains.GRASS_COLOR_NOISE.getValue(pos.getX() / 200.0, pos.getZ() / 200.0);
        if (d0 < -0.8) {
            final int j = rand.nextInt(4);
            switch (j) {
                case 0: {
                    return BlockFlower.EnumFlowerType.ORANGE_TULIP;
                }
                case 1: {
                    return BlockFlower.EnumFlowerType.RED_TULIP;
                }
                case 2: {
                    return BlockFlower.EnumFlowerType.PINK_TULIP;
                }
                default: {
                    return BlockFlower.EnumFlowerType.WHITE_TULIP;
                }
            }
        }
        else {
            if (rand.nextInt(3) <= 0) {
                return BlockFlower.EnumFlowerType.DANDELION;
            }
            final int i = rand.nextInt(3);
            if (i == 0) {
                return BlockFlower.EnumFlowerType.POPPY;
            }
            return (i == 1) ? BlockFlower.EnumFlowerType.HOUSTONIA : BlockFlower.EnumFlowerType.OXEYE_DAISY;
        }
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        final double d0 = BiomePlains.GRASS_COLOR_NOISE.getValue((pos.getX() + 8) / 200.0, (pos.getZ() + 8) / 200.0);
        if (d0 < -0.8) {
            this.decorator.flowersPerChunk = 15;
            this.decorator.grassPerChunk = 5;
        }
        else {
            this.decorator.flowersPerChunk = 4;
            this.decorator.grassPerChunk = 10;
            BiomePlains.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
            for (int i = 0; i < 7; ++i) {
                final int j = rand.nextInt(16) + 8;
                final int k = rand.nextInt(16) + 8;
                final int l = rand.nextInt(worldIn.getHeight(pos.add(j, 0, k)).getY() + 32);
                BiomePlains.DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j, l, k));
            }
        }
        if (this.sunflowers) {
            BiomePlains.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SUNFLOWER);
            for (int i2 = 0; i2 < 10; ++i2) {
                final int j2 = rand.nextInt(16) + 8;
                final int k2 = rand.nextInt(16) + 8;
                final int l2 = rand.nextInt(worldIn.getHeight(pos.add(j2, 0, k2)).getY() + 32);
                BiomePlains.DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j2, l2, k2));
            }
        }
        super.decorate(worldIn, rand, pos);
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(final Random rand) {
        return (rand.nextInt(3) == 0) ? BiomePlains.BIG_TREE_FEATURE : BiomePlains.TREE_FEATURE;
    }
}
