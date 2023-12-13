/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import optfine.Config;
import optfine.ConnectedUtils;
import optfine.RandomMobs;
import optfine.RangeListInt;

public class RandomMobsRule {
    private ResourceLocation baseResLoc = null;
    private int[] skins = null;
    private ResourceLocation[] resourceLocations = null;
    private int[] weights = null;
    private BiomeGenBase[] biomes = null;
    private RangeListInt heights = null;
    public int[] sumWeights = null;
    public int sumAllWeights = 1;

    public RandomMobsRule(ResourceLocation p_i53_1_, int[] p_i53_2_, int[] p_i53_3_, BiomeGenBase[] p_i53_4_, RangeListInt p_i53_5_) {
        this.baseResLoc = p_i53_1_;
        this.skins = p_i53_2_;
        this.weights = p_i53_3_;
        this.biomes = p_i53_4_;
        this.heights = p_i53_5_;
    }

    public boolean isValid(String p_isValid_1_) {
        this.resourceLocations = new ResourceLocation[this.skins.length];
        ResourceLocation resourcelocation = RandomMobs.getMcpatcherLocation(this.baseResLoc);
        if (resourcelocation == null) {
            Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
            return false;
        }
        for (int i2 = 0; i2 < this.resourceLocations.length; ++i2) {
            int j2 = this.skins[i2];
            if (j2 <= 1) {
                this.resourceLocations[i2] = this.baseResLoc;
                continue;
            }
            ResourceLocation resourcelocation1 = RandomMobs.getLocationIndexed(resourcelocation, j2);
            if (resourcelocation1 == null) {
                Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
                return false;
            }
            if (!Config.hasResource(resourcelocation1)) {
                Config.warn("Texture not found: " + resourcelocation1.getResourcePath());
                return false;
            }
            this.resourceLocations[i2] = resourcelocation1;
        }
        if (this.weights != null) {
            if (this.weights.length > this.resourceLocations.length) {
                Config.warn("More weights defined than skins, trimming weights: " + p_isValid_1_);
                int[] aint = new int[this.resourceLocations.length];
                System.arraycopy(this.weights, 0, aint, 0, aint.length);
                this.weights = aint;
            }
            if (this.weights.length < this.resourceLocations.length) {
                Config.warn("Less weights defined than skins, expanding weights: " + p_isValid_1_);
                int[] aint1 = new int[this.resourceLocations.length];
                System.arraycopy(this.weights, 0, aint1, 0, this.weights.length);
                int l2 = ConnectedUtils.getAverage(this.weights);
                for (int j1 = this.weights.length; j1 < aint1.length; ++j1) {
                    aint1[j1] = l2;
                }
                this.weights = aint1;
            }
            this.sumWeights = new int[this.weights.length];
            int k2 = 0;
            for (int i1 = 0; i1 < this.weights.length; ++i1) {
                if (this.weights[i1] < 0) {
                    Config.warn("Invalid weight: " + this.weights[i1]);
                    return false;
                }
                this.sumWeights[i1] = k2 += this.weights[i1];
            }
            this.sumAllWeights = k2;
            if (this.sumAllWeights <= 0) {
                Config.warn("Invalid sum of all weights: " + k2);
                this.sumAllWeights = 1;
            }
        }
        return true;
    }

    public boolean matches(EntityLiving p_matches_1_) {
        if (this.biomes != null) {
            BiomeGenBase biomegenbase = p_matches_1_.spawnBiome;
            boolean flag = false;
            for (int i2 = 0; i2 < this.biomes.length; ++i2) {
                BiomeGenBase biomegenbase1 = this.biomes[i2];
                if (biomegenbase1 != biomegenbase) continue;
                flag = true;
                break;
            }
            if (!flag) {
                return false;
            }
        }
        return this.heights != null && p_matches_1_.spawnPosition != null ? this.heights.isInRange(p_matches_1_.spawnPosition.getY()) : true;
    }

    public ResourceLocation getTextureLocation(ResourceLocation p_getTextureLocation_1_, int p_getTextureLocation_2_) {
        int i2 = 0;
        if (this.weights == null) {
            i2 = p_getTextureLocation_2_ % this.resourceLocations.length;
        } else {
            int j2 = p_getTextureLocation_2_ % this.sumAllWeights;
            for (int k2 = 0; k2 < this.sumWeights.length; ++k2) {
                if (this.sumWeights[k2] <= j2) continue;
                i2 = k2;
                break;
            }
        }
        return this.resourceLocations[i2];
    }
}

