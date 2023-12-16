// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.ResourceLocation;

public class RandomMobsRule
{
    private ResourceLocation baseResLoc;
    private int[] skins;
    private ResourceLocation[] resourceLocations;
    private int[] weights;
    private BiomeGenBase[] biomes;
    private RangeListInt heights;
    public int[] sumWeights;
    public int sumAllWeights;
    
    public RandomMobsRule(final ResourceLocation baseResLoc, final int[] skins, final int[] weights, final BiomeGenBase[] biomes, final RangeListInt heights) {
        this.baseResLoc = null;
        this.skins = null;
        this.resourceLocations = null;
        this.weights = null;
        this.biomes = null;
        this.heights = null;
        this.sumWeights = null;
        this.sumAllWeights = 1;
        this.baseResLoc = baseResLoc;
        this.skins = skins;
        this.weights = weights;
        this.biomes = biomes;
        this.heights = heights;
    }
    
    public boolean isValid(final String path) {
        this.resourceLocations = new ResourceLocation[this.skins.length];
        final ResourceLocation locMcp = RandomMobs.getMcpatcherLocation(this.baseResLoc);
        if (locMcp == null) {
            Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
            return false;
        }
        for (int sum = 0; sum < this.resourceLocations.length; ++sum) {
            final int i = this.skins[sum];
            if (i <= 1) {
                this.resourceLocations[sum] = this.baseResLoc;
            }
            else {
                final ResourceLocation i2 = RandomMobs.getLocationIndexed(locMcp, i);
                if (i2 == null) {
                    Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
                    return false;
                }
                if (!Config.hasResource(i2)) {
                    Config.warn("Texture not found: " + i2.getResourcePath());
                    return false;
                }
                this.resourceLocations[sum] = i2;
            }
        }
        if (this.weights != null) {
            if (this.weights.length > this.resourceLocations.length) {
                Config.warn("More weights defined than skins, trimming weights: " + path);
                final int[] var6 = new int[this.resourceLocations.length];
                System.arraycopy(this.weights, 0, var6, 0, var6.length);
                this.weights = var6;
            }
            if (this.weights.length < this.resourceLocations.length) {
                Config.warn("Less weights defined than skins, expanding weights: " + path);
                final int[] var6 = new int[this.resourceLocations.length];
                System.arraycopy(this.weights, 0, var6, 0, this.weights.length);
                final int i = MathUtils.getAverage(this.weights);
                for (int var7 = this.weights.length; var7 < var6.length; ++var7) {
                    var6[var7] = i;
                }
                this.weights = var6;
            }
            this.sumWeights = new int[this.weights.length];
            int sum = 0;
            for (int i = 0; i < this.weights.length; ++i) {
                if (this.weights[i] < 0) {
                    Config.warn("Invalid weight: " + this.weights[i]);
                    return false;
                }
                sum += this.weights[i];
                this.sumWeights[i] = sum;
            }
            this.sumAllWeights = sum;
            if (this.sumAllWeights <= 0) {
                Config.warn("Invalid sum of all weights: " + sum);
                this.sumAllWeights = 1;
            }
        }
        return true;
    }
    
    public boolean matches(final EntityLiving el) {
        return Matches.biome(el.spawnBiome, this.biomes) && (this.heights == null || el.spawnPosition == null || this.heights.isInRange(el.spawnPosition.getY()));
    }
    
    public ResourceLocation getTextureLocation(final ResourceLocation loc, final int randomId) {
        int index = 0;
        if (this.weights == null) {
            index = randomId % this.resourceLocations.length;
        }
        else {
            final int randWeight = randomId % this.sumAllWeights;
            for (int i = 0; i < this.sumWeights.length; ++i) {
                if (this.sumWeights[i] > randWeight) {
                    index = i;
                    break;
                }
            }
        }
        return this.resourceLocations[index];
    }
}
