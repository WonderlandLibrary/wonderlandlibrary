package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenScatteredFeature
extends MapGenStructure {
    private static final List<BiomeGenBase> biomelist = Arrays.asList((Object[])new BiomeGenBase[]{BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.swampland});
    private List<BiomeGenBase.SpawnListEntry> scatteredFeatureSpawnList = Lists.newArrayList();
    private int maxDistanceBetweenScatteredFeatures = 32;
    private int minDistanceBetweenScatteredFeatures = 8;

    public MapGenScatteredFeature() {
        this.scatteredFeatureSpawnList.add((Object)new BiomeGenBase.SpawnListEntry(EntityWitch.class, 1, 1, 1));
    }

    public MapGenScatteredFeature(Map<String, String> p_i2061_1_) {
        this();
        for (Map.Entry entry : p_i2061_1_.entrySet()) {
            if (!((String)entry.getKey()).equals((Object)"distance")) continue;
            this.maxDistanceBetweenScatteredFeatures = MathHelper.parseIntWithDefaultAndMax((String)((String)entry.getValue()), (int)this.maxDistanceBetweenScatteredFeatures, (int)(this.minDistanceBetweenScatteredFeatures + 1));
        }
    }

    public String getStructureName() {
        return "Temple";
    }

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
        if (i == (k += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures)) && j == (l += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures))) {
            BiomeGenBase biomegenbase = this.worldObj.getWorldChunkManager().getBiomeGenerator(new BlockPos(i * 16 + 8, 0, j * 16 + 8));
            if (biomegenbase == null) {
                return false;
            }
            for (BiomeGenBase biomegenbase1 : biomelist) {
                if (biomegenbase != biomegenbase1) continue;
                return true;
            }
        }
        return false;
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new Start(this.worldObj, this.rand, chunkX, chunkZ);
    }

    public boolean func_175798_a(BlockPos p_175798_1_) {
        StructureStart structurestart = this.func_175797_c(p_175798_1_);
        if (structurestart != null && structurestart instanceof Start && !structurestart.components.isEmpty()) {
            StructureComponent structurecomponent = (StructureComponent)structurestart.components.getFirst();
            return structurecomponent instanceof ComponentScatteredFeaturePieces.SwampHut;
        }
        return false;
    }

    public List<BiomeGenBase.SpawnListEntry> getScatteredFeatureSpawnList() {
        return this.scatteredFeatureSpawnList;
    }
}
