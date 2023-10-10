/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.util.ArrayList;
import java.util.Properties;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import optfine.Config;
import optfine.ConnectedParser;
import optfine.RandomMobsRule;
import optfine.RangeInt;
import optfine.RangeListInt;

public class RandomMobsProperties {
    public String name = null;
    public String basePath = null;
    public ResourceLocation[] resourceLocations = null;
    public RandomMobsRule[] rules = null;

    public RandomMobsProperties(String p_i51_1_, ResourceLocation[] p_i51_2_) {
        ConnectedParser connectedparser = new ConnectedParser("RandomMobs");
        this.name = connectedparser.parseName(p_i51_1_);
        this.basePath = connectedparser.parseBasePath(p_i51_1_);
        this.resourceLocations = p_i51_2_;
    }

    public RandomMobsProperties(Properties p_i52_1_, String p_i52_2_, ResourceLocation p_i52_3_) {
        ConnectedParser connectedparser = new ConnectedParser("RandomMobs");
        this.name = connectedparser.parseName(p_i52_2_);
        this.basePath = connectedparser.parseBasePath(p_i52_2_);
        this.rules = this.parseRules(p_i52_1_, p_i52_3_, connectedparser);
    }

    public ResourceLocation getTextureLocation(ResourceLocation p_getTextureLocation_1_, EntityLiving p_getTextureLocation_2_) {
        if (this.rules != null) {
            for (int i2 = 0; i2 < this.rules.length; ++i2) {
                RandomMobsRule randommobsrule = this.rules[i2];
                if (!randommobsrule.matches(p_getTextureLocation_2_)) continue;
                return randommobsrule.getTextureLocation(p_getTextureLocation_1_, p_getTextureLocation_2_.randomMobsId);
            }
        }
        if (this.resourceLocations != null) {
            int j2 = p_getTextureLocation_2_.randomMobsId;
            int k2 = j2 % this.resourceLocations.length;
            return this.resourceLocations[k2];
        }
        return p_getTextureLocation_1_;
    }

    private RandomMobsRule[] parseRules(Properties p_parseRules_1_, ResourceLocation p_parseRules_2_, ConnectedParser p_parseRules_3_) {
        ArrayList<RandomMobsRule> list = new ArrayList<RandomMobsRule>();
        int i2 = p_parseRules_1_.size();
        for (int j2 = 0; j2 < i2; ++j2) {
            int k2 = j2 + 1;
            String s2 = p_parseRules_1_.getProperty("skins." + k2);
            if (s2 == null) continue;
            int[] aint = p_parseRules_3_.parseIntList(s2);
            int[] aint1 = p_parseRules_3_.parseIntList(p_parseRules_1_.getProperty("weights." + k2));
            BiomeGenBase[] abiomegenbase = p_parseRules_3_.parseBiomes(p_parseRules_1_.getProperty("biomes." + k2));
            RangeListInt rangelistint = p_parseRules_3_.parseRangeListInt(p_parseRules_1_.getProperty("heights." + k2));
            if (rangelistint == null) {
                rangelistint = this.parseMinMaxHeight(p_parseRules_1_, k2);
            }
            RandomMobsRule randommobsrule = new RandomMobsRule(p_parseRules_2_, aint, aint1, abiomegenbase, rangelistint);
            list.add(randommobsrule);
        }
        RandomMobsRule[] arandommobsrule = list.toArray(new RandomMobsRule[list.size()]);
        return arandommobsrule;
    }

    private RangeListInt parseMinMaxHeight(Properties p_parseMinMaxHeight_1_, int p_parseMinMaxHeight_2_) {
        String s2 = p_parseMinMaxHeight_1_.getProperty("minHeight." + p_parseMinMaxHeight_2_);
        String s1 = p_parseMinMaxHeight_1_.getProperty("maxHeight." + p_parseMinMaxHeight_2_);
        if (s2 == null && s1 == null) {
            return null;
        }
        int i2 = 0;
        if (s2 != null && (i2 = Config.parseInt(s2, -1)) < 0) {
            Config.warn("Invalid minHeight: " + s2);
            return null;
        }
        int j2 = 256;
        if (s1 != null && (j2 = Config.parseInt(s1, -1)) < 0) {
            Config.warn("Invalid maxHeight: " + s1);
            return null;
        }
        if (j2 < 0) {
            Config.warn("Invalid minHeight, maxHeight: " + s2 + ", " + s1);
            return null;
        }
        RangeListInt rangelistint = new RangeListInt();
        rangelistint.addRange(new RangeInt(i2, j2));
        return rangelistint;
    }

    public boolean isValid(String p_isValid_1_) {
        if (this.resourceLocations == null && this.rules == null) {
            Config.warn("No skins specified: " + p_isValid_1_);
            return false;
        }
        if (this.rules != null) {
            for (int i2 = 0; i2 < this.rules.length; ++i2) {
                RandomMobsRule randommobsrule = this.rules[i2];
                if (randommobsrule.isValid(p_isValid_1_)) continue;
                return false;
            }
        }
        if (this.resourceLocations != null) {
            for (int j2 = 0; j2 < this.resourceLocations.length; ++j2) {
                ResourceLocation resourcelocation = this.resourceLocations[j2];
                if (Config.hasResource(resourcelocation)) continue;
                Config.warn("Texture not found: " + resourcelocation.getResourcePath());
                return false;
            }
        }
        return true;
    }
}

