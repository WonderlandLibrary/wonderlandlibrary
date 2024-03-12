// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.optifine.util.ArrayUtils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityWolf;
import net.optifine.reflect.Reflector;
import net.minecraft.entity.passive.EntityVillager;
import net.optifine.config.Matches;
import net.optifine.util.MathUtils;
import net.optifine.config.RangeInt;
import net.minecraft.src.Config;
import net.optifine.config.ConnectedParser;
import java.util.Properties;
import net.optifine.config.Weather;
import net.minecraft.item.EnumDyeColor;
import net.optifine.config.VillagerProfession;
import net.optifine.config.NbtTagValue;
import net.optifine.config.RangeListInt;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.ResourceLocation;

public class RandomEntityRule
{
    private String pathProps;
    private ResourceLocation baseResLoc;
    private int index;
    private int[] textures;
    private ResourceLocation[] resourceLocations;
    private int[] weights;
    private BiomeGenBase[] biomes;
    private RangeListInt heights;
    private RangeListInt healthRange;
    private boolean healthPercent;
    private NbtTagValue nbtName;
    public int[] sumWeights;
    public int sumAllWeights;
    private VillagerProfession[] professions;
    private EnumDyeColor[] collarColors;
    private Boolean baby;
    private RangeListInt moonPhases;
    private RangeListInt dayTimes;
    private Weather[] weatherList;
    
    public RandomEntityRule(final Properties props, final String pathProps, final ResourceLocation baseResLoc, final int index, final String valTextures, final ConnectedParser cp) {
        this.pathProps = null;
        this.baseResLoc = null;
        this.textures = null;
        this.resourceLocations = null;
        this.weights = null;
        this.biomes = null;
        this.heights = null;
        this.healthRange = null;
        this.healthPercent = false;
        this.nbtName = null;
        this.sumWeights = null;
        this.sumAllWeights = 1;
        this.professions = null;
        this.collarColors = null;
        this.baby = null;
        this.moonPhases = null;
        this.dayTimes = null;
        this.weatherList = null;
        this.pathProps = pathProps;
        this.baseResLoc = baseResLoc;
        this.index = index;
        this.textures = cp.parseIntList(valTextures);
        this.weights = cp.parseIntList(props.getProperty("weights." + index));
        this.biomes = cp.parseBiomes(props.getProperty("biomes." + index));
        this.heights = cp.parseRangeListInt(props.getProperty("heights." + index));
        if (this.heights == null) {
            this.heights = this.parseMinMaxHeight(props, index);
        }
        String s = props.getProperty("health." + index);
        if (s != null) {
            this.healthPercent = s.contains("%");
            s = s.replace("%", "");
            this.healthRange = cp.parseRangeListInt(s);
        }
        this.nbtName = cp.parseNbtTagValue("name", props.getProperty("name." + index));
        this.professions = cp.parseProfessions(props.getProperty("professions." + index));
        this.collarColors = cp.parseDyeColors(props.getProperty("collarColors." + index), "collar color", ConnectedParser.DYE_COLORS_INVALID);
        this.baby = cp.parseBooleanObject(props.getProperty("baby." + index));
        this.moonPhases = cp.parseRangeListInt(props.getProperty("moonPhase." + index));
        this.dayTimes = cp.parseRangeListInt(props.getProperty("dayTime." + index));
        this.weatherList = cp.parseWeather(props.getProperty("weather." + index), "weather." + index, null);
    }
    
    private RangeListInt parseMinMaxHeight(final Properties props, final int index) {
        final String s = props.getProperty("minHeight." + index);
        final String s2 = props.getProperty("maxHeight." + index);
        if (s == null && s2 == null) {
            return null;
        }
        int i = 0;
        if (s != null) {
            i = Config.parseInt(s, -1);
            if (i < 0) {
                Config.warn("Invalid minHeight: " + s);
                return null;
            }
        }
        int j = 256;
        if (s2 != null) {
            j = Config.parseInt(s2, -1);
            if (j < 0) {
                Config.warn("Invalid maxHeight: " + s2);
                return null;
            }
        }
        if (j < 0) {
            Config.warn("Invalid minHeight, maxHeight: " + s + ", " + s2);
            return null;
        }
        final RangeListInt rangelistint = new RangeListInt();
        rangelistint.addRange(new RangeInt(i, j));
        return rangelistint;
    }
    
    public boolean isValid(final String path) {
        if (this.textures == null || this.textures.length == 0) {
            Config.warn("Invalid skins for rule: " + this.index);
            return false;
        }
        if (this.resourceLocations != null) {
            return true;
        }
        this.resourceLocations = new ResourceLocation[this.textures.length];
        final boolean flag = this.pathProps.startsWith("mcpatcher/mob/");
        final ResourceLocation resourcelocation = RandomEntities.getLocationRandom(this.baseResLoc, flag);
        if (resourcelocation == null) {
            Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
            return false;
        }
        for (int i = 0; i < this.resourceLocations.length; ++i) {
            final int j = this.textures[i];
            if (j <= 1) {
                this.resourceLocations[i] = this.baseResLoc;
            }
            else {
                final ResourceLocation resourcelocation2 = RandomEntities.getLocationIndexed(resourcelocation, j);
                if (resourcelocation2 == null) {
                    Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
                    return false;
                }
                if (!Config.hasResource(resourcelocation2)) {
                    Config.warn("Texture not found: " + resourcelocation2.getResourcePath());
                    return false;
                }
                this.resourceLocations[i] = resourcelocation2;
            }
        }
        if (this.weights != null) {
            if (this.weights.length > this.resourceLocations.length) {
                Config.warn("More weights defined than skins, trimming weights: " + path);
                final int[] aint = new int[this.resourceLocations.length];
                System.arraycopy(this.weights, 0, aint, 0, aint.length);
                this.weights = aint;
            }
            if (this.weights.length < this.resourceLocations.length) {
                Config.warn("Less weights defined than skins, expanding weights: " + path);
                final int[] aint2 = new int[this.resourceLocations.length];
                System.arraycopy(this.weights, 0, aint2, 0, this.weights.length);
                final int l = MathUtils.getAverage(this.weights);
                for (int j2 = this.weights.length; j2 < aint2.length; ++j2) {
                    aint2[j2] = l;
                }
                this.weights = aint2;
            }
            this.sumWeights = new int[this.weights.length];
            int k = 0;
            for (int i2 = 0; i2 < this.weights.length; ++i2) {
                if (this.weights[i2] < 0) {
                    Config.warn("Invalid weight: " + this.weights[i2]);
                    return false;
                }
                k += this.weights[i2];
                this.sumWeights[i2] = k;
            }
            this.sumAllWeights = k;
            if (this.sumAllWeights <= 0) {
                Config.warn("Invalid sum of all weights: " + k);
                this.sumAllWeights = 1;
            }
        }
        if (this.professions == ConnectedParser.PROFESSIONS_INVALID) {
            Config.warn("Invalid professions or careers: " + path);
            return false;
        }
        if (this.collarColors == ConnectedParser.DYE_COLORS_INVALID) {
            Config.warn("Invalid collar colors: " + path);
            return false;
        }
        return true;
    }
    
    public boolean matches(final IRandomEntity randomEntity) {
        if (this.biomes != null && !Matches.biome(randomEntity.getSpawnBiome(), this.biomes)) {
            return false;
        }
        if (this.heights != null) {
            final BlockPos blockpos = randomEntity.getSpawnPosition();
            if (blockpos != null && !this.heights.isInRange(blockpos.getY())) {
                return false;
            }
        }
        if (this.healthRange != null) {
            int i1 = randomEntity.getHealth();
            if (this.healthPercent) {
                final int j = randomEntity.getMaxHealth();
                if (j > 0) {
                    i1 = (int)(i1 * 100 / (double)j);
                }
            }
            if (!this.healthRange.isInRange(i1)) {
                return false;
            }
        }
        if (this.nbtName != null) {
            final String s = randomEntity.getName();
            if (!this.nbtName.matchesValue(s)) {
                return false;
            }
        }
        if (this.professions != null && randomEntity instanceof RandomEntity) {
            final RandomEntity randomentity = (RandomEntity)randomEntity;
            final Entity entity = randomentity.getEntity();
            if (entity instanceof EntityVillager) {
                final EntityVillager entityvillager = (EntityVillager)entity;
                final int k = entityvillager.getProfession();
                final int l = Reflector.getFieldValueInt(entityvillager, Reflector.EntityVillager_careerId, -1);
                if (k < 0 || l < 0) {
                    return false;
                }
                boolean flag = false;
                for (int m = 0; m < this.professions.length; ++m) {
                    final VillagerProfession villagerprofession = this.professions[m];
                    if (villagerprofession.matches(k, l)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return false;
                }
            }
        }
        if (this.collarColors != null && randomEntity instanceof RandomEntity) {
            final RandomEntity randomentity2 = (RandomEntity)randomEntity;
            final Entity entity2 = randomentity2.getEntity();
            if (entity2 instanceof EntityWolf) {
                final EntityWolf entitywolf = (EntityWolf)entity2;
                if (!entitywolf.isTamed()) {
                    return false;
                }
                final EnumDyeColor enumdyecolor = entitywolf.getCollarColor();
                if (!Config.equalsOne(enumdyecolor, this.collarColors)) {
                    return false;
                }
            }
        }
        if (this.baby != null && randomEntity instanceof RandomEntity) {
            final RandomEntity randomentity3 = (RandomEntity)randomEntity;
            final Entity entity3 = randomentity3.getEntity();
            if (entity3 instanceof EntityLiving) {
                final EntityLiving entityliving = (EntityLiving)entity3;
                if (entityliving.isChild() != this.baby) {
                    return false;
                }
            }
        }
        if (this.moonPhases != null) {
            final World world = Config.getMinecraft().theWorld;
            if (world != null) {
                final int j2 = world.getMoonPhase();
                if (!this.moonPhases.isInRange(j2)) {
                    return false;
                }
            }
        }
        if (this.dayTimes != null) {
            final World world2 = Config.getMinecraft().theWorld;
            if (world2 != null) {
                final int k2 = (int)world2.getWorldInfo().getWorldTime();
                if (!this.dayTimes.isInRange(k2)) {
                    return false;
                }
            }
        }
        if (this.weatherList != null) {
            final World world3 = Config.getMinecraft().theWorld;
            if (world3 != null) {
                final Weather weather = Weather.getWeather(world3, 0.0f);
                if (!ArrayUtils.contains(this.weatherList, weather)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public ResourceLocation getTextureLocation(final ResourceLocation loc, final int randomId) {
        if (this.resourceLocations != null && this.resourceLocations.length != 0) {
            int i = 0;
            if (this.weights == null) {
                i = randomId % this.resourceLocations.length;
            }
            else {
                final int j = randomId % this.sumAllWeights;
                for (int k = 0; k < this.sumWeights.length; ++k) {
                    if (this.sumWeights[k] > j) {
                        i = k;
                        break;
                    }
                }
            }
            return this.resourceLocations[i];
        }
        return loc;
    }
}
