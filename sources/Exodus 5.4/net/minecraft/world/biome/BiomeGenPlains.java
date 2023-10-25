/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenPlains
extends BiomeGenBase {
    protected boolean field_150628_aC;

    protected BiomeGenPlains(int n) {
        super(n);
        this.setTemperatureRainfall(0.8f, 0.4f);
        this.setHeight(height_LowPlains);
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 5, 2, 6));
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.flowersPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 10;
    }

    @Override
    public void decorate(World world, Random random, BlockPos blockPos) {
        int n;
        int n2;
        int n3;
        int n4;
        double d = GRASS_COLOR_NOISE.func_151601_a((double)(blockPos.getX() + 8) / 200.0, (double)(blockPos.getZ() + 8) / 200.0);
        if (d < -0.8) {
            this.theBiomeDecorator.flowersPerChunk = 15;
            this.theBiomeDecorator.grassPerChunk = 5;
        } else {
            this.theBiomeDecorator.flowersPerChunk = 4;
            this.theBiomeDecorator.grassPerChunk = 10;
            DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
            n4 = 0;
            while (n4 < 7) {
                n3 = random.nextInt(16) + 8;
                n2 = random.nextInt(16) + 8;
                n = random.nextInt(world.getHeight(blockPos.add(n3, 0, n2)).getY() + 32);
                DOUBLE_PLANT_GENERATOR.generate(world, random, blockPos.add(n3, n, n2));
                ++n4;
            }
        }
        if (this.field_150628_aC) {
            DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SUNFLOWER);
            n4 = 0;
            while (n4 < 10) {
                n3 = random.nextInt(16) + 8;
                n2 = random.nextInt(16) + 8;
                n = random.nextInt(world.getHeight(blockPos.add(n3, 0, n2)).getY() + 32);
                DOUBLE_PLANT_GENERATOR.generate(world, random, blockPos.add(n3, n, n2));
                ++n4;
            }
        }
        super.decorate(world, random, blockPos);
    }

    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(Random random, BlockPos blockPos) {
        double d = GRASS_COLOR_NOISE.func_151601_a((double)blockPos.getX() / 200.0, (double)blockPos.getZ() / 200.0);
        if (d < -0.8) {
            int n = random.nextInt(4);
            switch (n) {
                case 0: {
                    return BlockFlower.EnumFlowerType.ORANGE_TULIP;
                }
                case 1: {
                    return BlockFlower.EnumFlowerType.RED_TULIP;
                }
                case 2: {
                    return BlockFlower.EnumFlowerType.PINK_TULIP;
                }
            }
            return BlockFlower.EnumFlowerType.WHITE_TULIP;
        }
        if (random.nextInt(3) > 0) {
            int n = random.nextInt(3);
            return n == 0 ? BlockFlower.EnumFlowerType.POPPY : (n == 1 ? BlockFlower.EnumFlowerType.HOUSTONIA : BlockFlower.EnumFlowerType.OXEYE_DAISY);
        }
        return BlockFlower.EnumFlowerType.DANDELION;
    }

    @Override
    protected BiomeGenBase createMutatedBiome(int n) {
        BiomeGenPlains biomeGenPlains = new BiomeGenPlains(n);
        biomeGenPlains.setBiomeName("Sunflower Plains");
        biomeGenPlains.field_150628_aC = true;
        biomeGenPlains.setColor(9286496);
        biomeGenPlains.field_150609_ah = 14273354;
        return biomeGenPlains;
    }
}

