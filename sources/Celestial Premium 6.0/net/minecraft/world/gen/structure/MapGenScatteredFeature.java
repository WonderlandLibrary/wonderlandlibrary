/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenScatteredFeature
extends MapGenStructure {
    private static final List<Biome> BIOMELIST = Arrays.asList(Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.SWAMPLAND, Biomes.ICE_PLAINS, Biomes.COLD_TAIGA);
    private final List<Biome.SpawnListEntry> scatteredFeatureSpawnList = Lists.newArrayList();
    private int maxDistanceBetweenScatteredFeatures = 32;
    private final int minDistanceBetweenScatteredFeatures;

    public MapGenScatteredFeature() {
        this.minDistanceBetweenScatteredFeatures = 8;
        this.scatteredFeatureSpawnList.add(new Biome.SpawnListEntry(EntityWitch.class, 1, 1, 1));
    }

    public MapGenScatteredFeature(Map<String, String> p_i2061_1_) {
        this();
        for (Map.Entry<String, String> entry : p_i2061_1_.entrySet()) {
            if (!entry.getKey().equals("distance")) continue;
            this.maxDistanceBetweenScatteredFeatures = MathHelper.getInt(entry.getValue(), this.maxDistanceBetweenScatteredFeatures, 9);
        }
    }

    @Override
    public String getStructureName() {
        return "Temple";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        int i = chunkX;
        int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.maxDistanceBetweenScatteredFeatures - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.maxDistanceBetweenScatteredFeatures - 1;
        }
        int k = chunkX / this.maxDistanceBetweenScatteredFeatures;
        int l = chunkZ / this.maxDistanceBetweenScatteredFeatures;
        Random random = this.worldObj.setRandomSeed(k, l, 14357617);
        k *= this.maxDistanceBetweenScatteredFeatures;
        l *= this.maxDistanceBetweenScatteredFeatures;
        if (i == (k += random.nextInt(this.maxDistanceBetweenScatteredFeatures - 8)) && j == (l += random.nextInt(this.maxDistanceBetweenScatteredFeatures - 8))) {
            Biome biome = this.worldObj.getBiomeProvider().getBiome(new BlockPos(i * 16 + 8, 0, j * 16 + 8));
            if (biome == null) {
                return false;
            }
            for (Biome biome1 : BIOMELIST) {
                if (biome != biome1) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean p_180706_3_) {
        this.worldObj = worldIn;
        return MapGenScatteredFeature.func_191069_a(worldIn, this, pos, this.maxDistanceBetweenScatteredFeatures, 8, 14357617, false, 100, p_180706_3_);
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new Start(this.worldObj, this.rand, chunkX, chunkZ);
    }

    public boolean isSwampHut(BlockPos p_175798_1_) {
        StructureStart structurestart = this.getStructureAt(p_175798_1_);
        if (structurestart != null && structurestart instanceof Start && !structurestart.components.isEmpty()) {
            StructureComponent structurecomponent = structurestart.components.get(0);
            return structurecomponent instanceof ComponentScatteredFeaturePieces.SwampHut;
        }
        return false;
    }

    public List<Biome.SpawnListEntry> getScatteredFeatureSpawnList() {
        return this.scatteredFeatureSpawnList;
    }

    public static class Start
    extends StructureStart {
        public Start() {
        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ) {
            this(worldIn, random, chunkX, chunkZ, worldIn.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8)));
        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ, Biome biomeIn) {
            super(chunkX, chunkZ);
            if (biomeIn != Biomes.JUNGLE && biomeIn != Biomes.JUNGLE_HILLS) {
                if (biomeIn == Biomes.SWAMPLAND) {
                    ComponentScatteredFeaturePieces.SwampHut componentscatteredfeaturepieces$swamphut = new ComponentScatteredFeaturePieces.SwampHut(random, chunkX * 16, chunkZ * 16);
                    this.components.add(componentscatteredfeaturepieces$swamphut);
                } else if (biomeIn != Biomes.DESERT && biomeIn != Biomes.DESERT_HILLS) {
                    if (biomeIn == Biomes.ICE_PLAINS || biomeIn == Biomes.COLD_TAIGA) {
                        ComponentScatteredFeaturePieces.Igloo componentscatteredfeaturepieces$igloo = new ComponentScatteredFeaturePieces.Igloo(random, chunkX * 16, chunkZ * 16);
                        this.components.add(componentscatteredfeaturepieces$igloo);
                    }
                } else {
                    ComponentScatteredFeaturePieces.DesertPyramid componentscatteredfeaturepieces$desertpyramid = new ComponentScatteredFeaturePieces.DesertPyramid(random, chunkX * 16, chunkZ * 16);
                    this.components.add(componentscatteredfeaturepieces$desertpyramid);
                }
            } else {
                ComponentScatteredFeaturePieces.JunglePyramid componentscatteredfeaturepieces$junglepyramid = new ComponentScatteredFeaturePieces.JunglePyramid(random, chunkX * 16, chunkZ * 16);
                this.components.add(componentscatteredfeaturepieces$junglepyramid);
            }
            this.updateBoundingBox();
        }
    }
}

