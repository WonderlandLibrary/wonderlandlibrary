// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.world.biome.BiomeGenBase;
import java.util.ArrayList;
import net.minecraft.entity.EntityLiving;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;

public class RandomMobsProperties
{
    public String name;
    public String basePath;
    public ResourceLocation[] resourceLocations;
    public RandomMobsRule[] rules;
    
    public RandomMobsProperties(final String path, final ResourceLocation[] variants) {
        this.name = null;
        this.basePath = null;
        this.resourceLocations = null;
        this.rules = null;
        final ConnectedParser cp = new ConnectedParser("RandomMobs");
        this.name = cp.parseName(path);
        this.basePath = cp.parseBasePath(path);
        this.resourceLocations = variants;
    }
    
    public RandomMobsProperties(final Properties props, final String path, final ResourceLocation baseResLoc) {
        this.name = null;
        this.basePath = null;
        this.resourceLocations = null;
        this.rules = null;
        final ConnectedParser cp = new ConnectedParser("RandomMobs");
        this.name = cp.parseName(path);
        this.basePath = cp.parseBasePath(path);
        this.rules = this.parseRules(props, baseResLoc, cp);
    }
    
    public ResourceLocation getTextureLocation(final ResourceLocation loc, final EntityLiving el) {
        if (this.rules != null) {
            for (int randomId = 0; randomId < this.rules.length; ++randomId) {
                final RandomMobsRule index = this.rules[randomId];
                if (index.matches(el)) {
                    return index.getTextureLocation(loc, el.randomMobsId);
                }
            }
        }
        if (this.resourceLocations != null) {
            final int randomId = el.randomMobsId;
            final int var5 = randomId % this.resourceLocations.length;
            return this.resourceLocations[var5];
        }
        return loc;
    }
    
    private RandomMobsRule[] parseRules(final Properties props, final ResourceLocation baseResLoc, final ConnectedParser cp) {
        final ArrayList list = new ArrayList();
        for (int count = props.size(), rules = 0; rules < count; ++rules) {
            final int index = rules + 1;
            final String valSkins = props.getProperty("skins." + index);
            if (valSkins != null) {
                final int[] skins = cp.parseIntList(valSkins);
                final int[] weights = cp.parseIntList(props.getProperty("weights." + index));
                final BiomeGenBase[] biomes = cp.parseBiomes(props.getProperty("biomes." + index));
                RangeListInt heights = cp.parseRangeListInt(props.getProperty("heights." + index));
                if (heights == null) {
                    heights = this.parseMinMaxHeight(props, index);
                }
                final RandomMobsRule rule = new RandomMobsRule(baseResLoc, skins, weights, biomes, heights);
                list.add(rule);
            }
        }
        final RandomMobsRule[] var14 = list.toArray(new RandomMobsRule[list.size()]);
        return var14;
    }
    
    private RangeListInt parseMinMaxHeight(final Properties props, final int index) {
        final String minHeightStr = props.getProperty("minHeight." + index);
        final String maxHeightStr = props.getProperty("maxHeight." + index);
        if (minHeightStr == null && maxHeightStr == null) {
            return null;
        }
        int minHeight = 0;
        if (minHeightStr != null) {
            minHeight = Config.parseInt(minHeightStr, -1);
            if (minHeight < 0) {
                Config.warn("Invalid minHeight: " + minHeightStr);
                return null;
            }
        }
        int maxHeight = 256;
        if (maxHeightStr != null) {
            maxHeight = Config.parseInt(maxHeightStr, -1);
            if (maxHeight < 0) {
                Config.warn("Invalid maxHeight: " + maxHeightStr);
                return null;
            }
        }
        if (maxHeight < 0) {
            Config.warn("Invalid minHeight, maxHeight: " + minHeightStr + ", " + maxHeightStr);
            return null;
        }
        final RangeListInt list = new RangeListInt();
        list.addRange(new RangeInt(minHeight, maxHeight));
        return list;
    }
    
    public boolean isValid(final String path) {
        if (this.resourceLocations == null && this.rules == null) {
            Config.warn("No skins specified: " + path);
            return false;
        }
        if (this.rules != null) {
            for (int i = 0; i < this.rules.length; ++i) {
                final RandomMobsRule loc = this.rules[i];
                if (!loc.isValid(path)) {
                    return false;
                }
            }
        }
        if (this.resourceLocations != null) {
            for (int i = 0; i < this.resourceLocations.length; ++i) {
                final ResourceLocation var4 = this.resourceLocations[i];
                if (!Config.hasResource(var4)) {
                    Config.warn("Texture not found: " + var4.getResourcePath());
                    return false;
                }
            }
        }
        return true;
    }
}
