/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenScatteredFeature
extends MapGenStructure {
    private int maxDistanceBetweenScatteredFeatures = 32;
    private static final List<BiomeGenBase> biomelist = Arrays.asList(BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.swampland);
    private int minDistanceBetweenScatteredFeatures = 8;
    private List<BiomeGenBase.SpawnListEntry> scatteredFeatureSpawnList = Lists.newArrayList();

    @Override
    protected StructureStart getStructureStart(int n, int n2) {
        return new Start(this.worldObj, this.rand, n, n2);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int n, int n2) {
        int n3 = n;
        int n4 = n2;
        if (n < 0) {
            n -= this.maxDistanceBetweenScatteredFeatures - 1;
        }
        if (n2 < 0) {
            n2 -= this.maxDistanceBetweenScatteredFeatures - 1;
        }
        int n5 = n / this.maxDistanceBetweenScatteredFeatures;
        int n6 = n2 / this.maxDistanceBetweenScatteredFeatures;
        Random random = this.worldObj.setRandomSeed(n5, n6, 14357617);
        n5 *= this.maxDistanceBetweenScatteredFeatures;
        n6 *= this.maxDistanceBetweenScatteredFeatures;
        if (n3 == (n5 += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures)) && n4 == (n6 += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures))) {
            BiomeGenBase biomeGenBase = this.worldObj.getWorldChunkManager().getBiomeGenerator(new BlockPos(n3 * 16 + 8, 0, n4 * 16 + 8));
            if (biomeGenBase == null) {
                return false;
            }
            for (BiomeGenBase biomeGenBase2 : biomelist) {
                if (biomeGenBase != biomeGenBase2) continue;
                return true;
            }
        }
        return false;
    }

    public MapGenScatteredFeature() {
        this.scatteredFeatureSpawnList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 1, 1, 1));
    }

    @Override
    public String getStructureName() {
        return "Temple";
    }

    public MapGenScatteredFeature(Map<String, String> map) {
        this();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!entry.getKey().equals("distance")) continue;
            this.maxDistanceBetweenScatteredFeatures = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.maxDistanceBetweenScatteredFeatures, this.minDistanceBetweenScatteredFeatures + 1);
        }
    }

    public boolean func_175798_a(BlockPos blockPos) {
        StructureStart structureStart = this.func_175797_c(blockPos);
        if (structureStart != null && structureStart instanceof Start && !structureStart.components.isEmpty()) {
            StructureComponent structureComponent = structureStart.components.getFirst();
            return structureComponent instanceof ComponentScatteredFeaturePieces.SwampHut;
        }
        return false;
    }

    public List<BiomeGenBase.SpawnListEntry> getScatteredFeatureSpawnList() {
        return this.scatteredFeatureSpawnList;
    }

    public static class Start
    extends StructureStart {
        public Start(World world, Random random, int n, int n2) {
            super(n, n2);
            BiomeGenBase biomeGenBase = world.getBiomeGenForCoords(new BlockPos(n * 16 + 8, 0, n2 * 16 + 8));
            if (biomeGenBase != BiomeGenBase.jungle && biomeGenBase != BiomeGenBase.jungleHills) {
                if (biomeGenBase == BiomeGenBase.swampland) {
                    ComponentScatteredFeaturePieces.SwampHut swampHut = new ComponentScatteredFeaturePieces.SwampHut(random, n * 16, n2 * 16);
                    this.components.add(swampHut);
                } else if (biomeGenBase == BiomeGenBase.desert || biomeGenBase == BiomeGenBase.desertHills) {
                    ComponentScatteredFeaturePieces.DesertPyramid desertPyramid = new ComponentScatteredFeaturePieces.DesertPyramid(random, n * 16, n2 * 16);
                    this.components.add(desertPyramid);
                }
            } else {
                ComponentScatteredFeaturePieces.JunglePyramid junglePyramid = new ComponentScatteredFeaturePieces.JunglePyramid(random, n * 16, n2 * 16);
                this.components.add(junglePyramid);
            }
            this.updateBoundingBox();
        }

        public Start() {
        }
    }
}

