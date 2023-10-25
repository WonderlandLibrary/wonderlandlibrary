package net.minecraft.world.biome;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.WeightedRandom;

public static class BiomeGenBase.SpawnListEntry
extends WeightedRandom.Item {
    public Class<? extends EntityLiving> entityClass;
    public int minGroupCount;
    public int maxGroupCount;

    public BiomeGenBase.SpawnListEntry(Class<? extends EntityLiving> entityclassIn, int weight, int groupCountMin, int groupCountMax) {
        super(weight);
        this.entityClass = entityclassIn;
        this.minGroupCount = groupCountMin;
        this.maxGroupCount = groupCountMax;
    }

    public String toString() {
        return this.entityClass.getSimpleName() + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):" + this.itemWeight;
    }
}
