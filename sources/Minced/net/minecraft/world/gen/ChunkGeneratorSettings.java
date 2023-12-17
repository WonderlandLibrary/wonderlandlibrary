// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.world.biome.Biome;
import net.minecraft.init.Biomes;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import net.minecraft.util.JsonUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;

public class ChunkGeneratorSettings
{
    public final float coordinateScale;
    public final float heightScale;
    public final float upperLimitScale;
    public final float lowerLimitScale;
    public final float depthNoiseScaleX;
    public final float depthNoiseScaleZ;
    public final float depthNoiseScaleExponent;
    public final float mainNoiseScaleX;
    public final float mainNoiseScaleY;
    public final float mainNoiseScaleZ;
    public final float baseSize;
    public final float stretchY;
    public final float biomeDepthWeight;
    public final float biomeDepthOffSet;
    public final float biomeScaleWeight;
    public final float biomeScaleOffset;
    public final int seaLevel;
    public final boolean useCaves;
    public final boolean useDungeons;
    public final int dungeonChance;
    public final boolean useStrongholds;
    public final boolean useVillages;
    public final boolean useMineShafts;
    public final boolean useTemples;
    public final boolean useMonuments;
    public final boolean useMansions;
    public final boolean useRavines;
    public final boolean useWaterLakes;
    public final int waterLakeChance;
    public final boolean useLavaLakes;
    public final int lavaLakeChance;
    public final boolean useLavaOceans;
    public final int fixedBiome;
    public final int biomeSize;
    public final int riverSize;
    public final int dirtSize;
    public final int dirtCount;
    public final int dirtMinHeight;
    public final int dirtMaxHeight;
    public final int gravelSize;
    public final int gravelCount;
    public final int gravelMinHeight;
    public final int gravelMaxHeight;
    public final int graniteSize;
    public final int graniteCount;
    public final int graniteMinHeight;
    public final int graniteMaxHeight;
    public final int dioriteSize;
    public final int dioriteCount;
    public final int dioriteMinHeight;
    public final int dioriteMaxHeight;
    public final int andesiteSize;
    public final int andesiteCount;
    public final int andesiteMinHeight;
    public final int andesiteMaxHeight;
    public final int coalSize;
    public final int coalCount;
    public final int coalMinHeight;
    public final int coalMaxHeight;
    public final int ironSize;
    public final int ironCount;
    public final int ironMinHeight;
    public final int ironMaxHeight;
    public final int goldSize;
    public final int goldCount;
    public final int goldMinHeight;
    public final int goldMaxHeight;
    public final int redstoneSize;
    public final int redstoneCount;
    public final int redstoneMinHeight;
    public final int redstoneMaxHeight;
    public final int diamondSize;
    public final int diamondCount;
    public final int diamondMinHeight;
    public final int diamondMaxHeight;
    public final int lapisSize;
    public final int lapisCount;
    public final int lapisCenterHeight;
    public final int lapisSpread;
    
    private ChunkGeneratorSettings(final Factory settingsFactory) {
        this.coordinateScale = settingsFactory.coordinateScale;
        this.heightScale = settingsFactory.heightScale;
        this.upperLimitScale = settingsFactory.upperLimitScale;
        this.lowerLimitScale = settingsFactory.lowerLimitScale;
        this.depthNoiseScaleX = settingsFactory.depthNoiseScaleX;
        this.depthNoiseScaleZ = settingsFactory.depthNoiseScaleZ;
        this.depthNoiseScaleExponent = settingsFactory.depthNoiseScaleExponent;
        this.mainNoiseScaleX = settingsFactory.mainNoiseScaleX;
        this.mainNoiseScaleY = settingsFactory.mainNoiseScaleY;
        this.mainNoiseScaleZ = settingsFactory.mainNoiseScaleZ;
        this.baseSize = settingsFactory.baseSize;
        this.stretchY = settingsFactory.stretchY;
        this.biomeDepthWeight = settingsFactory.biomeDepthWeight;
        this.biomeDepthOffSet = settingsFactory.biomeDepthOffset;
        this.biomeScaleWeight = settingsFactory.biomeScaleWeight;
        this.biomeScaleOffset = settingsFactory.biomeScaleOffset;
        this.seaLevel = settingsFactory.seaLevel;
        this.useCaves = settingsFactory.useCaves;
        this.useDungeons = settingsFactory.useDungeons;
        this.dungeonChance = settingsFactory.dungeonChance;
        this.useStrongholds = settingsFactory.useStrongholds;
        this.useVillages = settingsFactory.useVillages;
        this.useMineShafts = settingsFactory.useMineShafts;
        this.useTemples = settingsFactory.useTemples;
        this.useMonuments = settingsFactory.useMonuments;
        this.useMansions = settingsFactory.useMansions;
        this.useRavines = settingsFactory.useRavines;
        this.useWaterLakes = settingsFactory.useWaterLakes;
        this.waterLakeChance = settingsFactory.waterLakeChance;
        this.useLavaLakes = settingsFactory.useLavaLakes;
        this.lavaLakeChance = settingsFactory.lavaLakeChance;
        this.useLavaOceans = settingsFactory.useLavaOceans;
        this.fixedBiome = settingsFactory.fixedBiome;
        this.biomeSize = settingsFactory.biomeSize;
        this.riverSize = settingsFactory.riverSize;
        this.dirtSize = settingsFactory.dirtSize;
        this.dirtCount = settingsFactory.dirtCount;
        this.dirtMinHeight = settingsFactory.dirtMinHeight;
        this.dirtMaxHeight = settingsFactory.dirtMaxHeight;
        this.gravelSize = settingsFactory.gravelSize;
        this.gravelCount = settingsFactory.gravelCount;
        this.gravelMinHeight = settingsFactory.gravelMinHeight;
        this.gravelMaxHeight = settingsFactory.gravelMaxHeight;
        this.graniteSize = settingsFactory.graniteSize;
        this.graniteCount = settingsFactory.graniteCount;
        this.graniteMinHeight = settingsFactory.graniteMinHeight;
        this.graniteMaxHeight = settingsFactory.graniteMaxHeight;
        this.dioriteSize = settingsFactory.dioriteSize;
        this.dioriteCount = settingsFactory.dioriteCount;
        this.dioriteMinHeight = settingsFactory.dioriteMinHeight;
        this.dioriteMaxHeight = settingsFactory.dioriteMaxHeight;
        this.andesiteSize = settingsFactory.andesiteSize;
        this.andesiteCount = settingsFactory.andesiteCount;
        this.andesiteMinHeight = settingsFactory.andesiteMinHeight;
        this.andesiteMaxHeight = settingsFactory.andesiteMaxHeight;
        this.coalSize = settingsFactory.coalSize;
        this.coalCount = settingsFactory.coalCount;
        this.coalMinHeight = settingsFactory.coalMinHeight;
        this.coalMaxHeight = settingsFactory.coalMaxHeight;
        this.ironSize = settingsFactory.ironSize;
        this.ironCount = settingsFactory.ironCount;
        this.ironMinHeight = settingsFactory.ironMinHeight;
        this.ironMaxHeight = settingsFactory.ironMaxHeight;
        this.goldSize = settingsFactory.goldSize;
        this.goldCount = settingsFactory.goldCount;
        this.goldMinHeight = settingsFactory.goldMinHeight;
        this.goldMaxHeight = settingsFactory.goldMaxHeight;
        this.redstoneSize = settingsFactory.redstoneSize;
        this.redstoneCount = settingsFactory.redstoneCount;
        this.redstoneMinHeight = settingsFactory.redstoneMinHeight;
        this.redstoneMaxHeight = settingsFactory.redstoneMaxHeight;
        this.diamondSize = settingsFactory.diamondSize;
        this.diamondCount = settingsFactory.diamondCount;
        this.diamondMinHeight = settingsFactory.diamondMinHeight;
        this.diamondMaxHeight = settingsFactory.diamondMaxHeight;
        this.lapisSize = settingsFactory.lapisSize;
        this.lapisCount = settingsFactory.lapisCount;
        this.lapisCenterHeight = settingsFactory.lapisCenterHeight;
        this.lapisSpread = settingsFactory.lapisSpread;
    }
    
    public static class Factory
    {
        @VisibleForTesting
        static final Gson JSON_ADAPTER;
        public float coordinateScale;
        public float heightScale;
        public float upperLimitScale;
        public float lowerLimitScale;
        public float depthNoiseScaleX;
        public float depthNoiseScaleZ;
        public float depthNoiseScaleExponent;
        public float mainNoiseScaleX;
        public float mainNoiseScaleY;
        public float mainNoiseScaleZ;
        public float baseSize;
        public float stretchY;
        public float biomeDepthWeight;
        public float biomeDepthOffset;
        public float biomeScaleWeight;
        public float biomeScaleOffset;
        public int seaLevel;
        public boolean useCaves;
        public boolean useDungeons;
        public int dungeonChance;
        public boolean useStrongholds;
        public boolean useVillages;
        public boolean useMineShafts;
        public boolean useTemples;
        public boolean useMonuments;
        public boolean useMansions;
        public boolean useRavines;
        public boolean useWaterLakes;
        public int waterLakeChance;
        public boolean useLavaLakes;
        public int lavaLakeChance;
        public boolean useLavaOceans;
        public int fixedBiome;
        public int biomeSize;
        public int riverSize;
        public int dirtSize;
        public int dirtCount;
        public int dirtMinHeight;
        public int dirtMaxHeight;
        public int gravelSize;
        public int gravelCount;
        public int gravelMinHeight;
        public int gravelMaxHeight;
        public int graniteSize;
        public int graniteCount;
        public int graniteMinHeight;
        public int graniteMaxHeight;
        public int dioriteSize;
        public int dioriteCount;
        public int dioriteMinHeight;
        public int dioriteMaxHeight;
        public int andesiteSize;
        public int andesiteCount;
        public int andesiteMinHeight;
        public int andesiteMaxHeight;
        public int coalSize;
        public int coalCount;
        public int coalMinHeight;
        public int coalMaxHeight;
        public int ironSize;
        public int ironCount;
        public int ironMinHeight;
        public int ironMaxHeight;
        public int goldSize;
        public int goldCount;
        public int goldMinHeight;
        public int goldMaxHeight;
        public int redstoneSize;
        public int redstoneCount;
        public int redstoneMinHeight;
        public int redstoneMaxHeight;
        public int diamondSize;
        public int diamondCount;
        public int diamondMinHeight;
        public int diamondMaxHeight;
        public int lapisSize;
        public int lapisCount;
        public int lapisCenterHeight;
        public int lapisSpread;
        
        public static Factory jsonToFactory(final String p_177865_0_) {
            if (p_177865_0_.isEmpty()) {
                return new Factory();
            }
            try {
                return JsonUtils.gsonDeserialize(Factory.JSON_ADAPTER, p_177865_0_, Factory.class);
            }
            catch (Exception var2) {
                return new Factory();
            }
        }
        
        @Override
        public String toString() {
            return Factory.JSON_ADAPTER.toJson((Object)this);
        }
        
        public Factory() {
            this.coordinateScale = 684.412f;
            this.heightScale = 684.412f;
            this.upperLimitScale = 512.0f;
            this.lowerLimitScale = 512.0f;
            this.depthNoiseScaleX = 200.0f;
            this.depthNoiseScaleZ = 200.0f;
            this.depthNoiseScaleExponent = 0.5f;
            this.mainNoiseScaleX = 80.0f;
            this.mainNoiseScaleY = 160.0f;
            this.mainNoiseScaleZ = 80.0f;
            this.baseSize = 8.5f;
            this.stretchY = 12.0f;
            this.biomeDepthWeight = 1.0f;
            this.biomeScaleWeight = 1.0f;
            this.seaLevel = 63;
            this.useCaves = true;
            this.useDungeons = true;
            this.dungeonChance = 8;
            this.useStrongholds = true;
            this.useVillages = true;
            this.useMineShafts = true;
            this.useTemples = true;
            this.useMonuments = true;
            this.useMansions = true;
            this.useRavines = true;
            this.useWaterLakes = true;
            this.waterLakeChance = 4;
            this.useLavaLakes = true;
            this.lavaLakeChance = 80;
            this.fixedBiome = -1;
            this.biomeSize = 4;
            this.riverSize = 4;
            this.dirtSize = 33;
            this.dirtCount = 10;
            this.dirtMaxHeight = 256;
            this.gravelSize = 33;
            this.gravelCount = 8;
            this.gravelMaxHeight = 256;
            this.graniteSize = 33;
            this.graniteCount = 10;
            this.graniteMaxHeight = 80;
            this.dioriteSize = 33;
            this.dioriteCount = 10;
            this.dioriteMaxHeight = 80;
            this.andesiteSize = 33;
            this.andesiteCount = 10;
            this.andesiteMaxHeight = 80;
            this.coalSize = 17;
            this.coalCount = 20;
            this.coalMaxHeight = 128;
            this.ironSize = 9;
            this.ironCount = 20;
            this.ironMaxHeight = 64;
            this.goldSize = 9;
            this.goldCount = 2;
            this.goldMaxHeight = 32;
            this.redstoneSize = 8;
            this.redstoneCount = 8;
            this.redstoneMaxHeight = 16;
            this.diamondSize = 8;
            this.diamondCount = 1;
            this.diamondMaxHeight = 16;
            this.lapisSize = 7;
            this.lapisCount = 1;
            this.lapisCenterHeight = 16;
            this.lapisSpread = 16;
            this.setDefaults();
        }
        
        public void setDefaults() {
            this.coordinateScale = 684.412f;
            this.heightScale = 684.412f;
            this.upperLimitScale = 512.0f;
            this.lowerLimitScale = 512.0f;
            this.depthNoiseScaleX = 200.0f;
            this.depthNoiseScaleZ = 200.0f;
            this.depthNoiseScaleExponent = 0.5f;
            this.mainNoiseScaleX = 80.0f;
            this.mainNoiseScaleY = 160.0f;
            this.mainNoiseScaleZ = 80.0f;
            this.baseSize = 8.5f;
            this.stretchY = 12.0f;
            this.biomeDepthWeight = 1.0f;
            this.biomeDepthOffset = 0.0f;
            this.biomeScaleWeight = 1.0f;
            this.biomeScaleOffset = 0.0f;
            this.seaLevel = 63;
            this.useCaves = true;
            this.useDungeons = true;
            this.dungeonChance = 8;
            this.useStrongholds = true;
            this.useVillages = true;
            this.useMineShafts = true;
            this.useTemples = true;
            this.useMonuments = true;
            this.useMansions = true;
            this.useRavines = true;
            this.useWaterLakes = true;
            this.waterLakeChance = 4;
            this.useLavaLakes = true;
            this.lavaLakeChance = 80;
            this.useLavaOceans = false;
            this.fixedBiome = -1;
            this.biomeSize = 4;
            this.riverSize = 4;
            this.dirtSize = 33;
            this.dirtCount = 10;
            this.dirtMinHeight = 0;
            this.dirtMaxHeight = 256;
            this.gravelSize = 33;
            this.gravelCount = 8;
            this.gravelMinHeight = 0;
            this.gravelMaxHeight = 256;
            this.graniteSize = 33;
            this.graniteCount = 10;
            this.graniteMinHeight = 0;
            this.graniteMaxHeight = 80;
            this.dioriteSize = 33;
            this.dioriteCount = 10;
            this.dioriteMinHeight = 0;
            this.dioriteMaxHeight = 80;
            this.andesiteSize = 33;
            this.andesiteCount = 10;
            this.andesiteMinHeight = 0;
            this.andesiteMaxHeight = 80;
            this.coalSize = 17;
            this.coalCount = 20;
            this.coalMinHeight = 0;
            this.coalMaxHeight = 128;
            this.ironSize = 9;
            this.ironCount = 20;
            this.ironMinHeight = 0;
            this.ironMaxHeight = 64;
            this.goldSize = 9;
            this.goldCount = 2;
            this.goldMinHeight = 0;
            this.goldMaxHeight = 32;
            this.redstoneSize = 8;
            this.redstoneCount = 8;
            this.redstoneMinHeight = 0;
            this.redstoneMaxHeight = 16;
            this.diamondSize = 8;
            this.diamondCount = 1;
            this.diamondMinHeight = 0;
            this.diamondMaxHeight = 16;
            this.lapisSize = 7;
            this.lapisCount = 1;
            this.lapisCenterHeight = 16;
            this.lapisSpread = 16;
        }
        
        @Override
        public boolean equals(final Object p_equals_1_) {
            if (this == p_equals_1_) {
                return true;
            }
            if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
                final Factory chunkgeneratorsettings$factory = (Factory)p_equals_1_;
                return this.andesiteCount == chunkgeneratorsettings$factory.andesiteCount && this.andesiteMaxHeight == chunkgeneratorsettings$factory.andesiteMaxHeight && this.andesiteMinHeight == chunkgeneratorsettings$factory.andesiteMinHeight && this.andesiteSize == chunkgeneratorsettings$factory.andesiteSize && Float.compare(chunkgeneratorsettings$factory.baseSize, this.baseSize) == 0 && Float.compare(chunkgeneratorsettings$factory.biomeDepthOffset, this.biomeDepthOffset) == 0 && Float.compare(chunkgeneratorsettings$factory.biomeDepthWeight, this.biomeDepthWeight) == 0 && Float.compare(chunkgeneratorsettings$factory.biomeScaleOffset, this.biomeScaleOffset) == 0 && Float.compare(chunkgeneratorsettings$factory.biomeScaleWeight, this.biomeScaleWeight) == 0 && this.biomeSize == chunkgeneratorsettings$factory.biomeSize && this.coalCount == chunkgeneratorsettings$factory.coalCount && this.coalMaxHeight == chunkgeneratorsettings$factory.coalMaxHeight && this.coalMinHeight == chunkgeneratorsettings$factory.coalMinHeight && this.coalSize == chunkgeneratorsettings$factory.coalSize && Float.compare(chunkgeneratorsettings$factory.coordinateScale, this.coordinateScale) == 0 && Float.compare(chunkgeneratorsettings$factory.depthNoiseScaleExponent, this.depthNoiseScaleExponent) == 0 && Float.compare(chunkgeneratorsettings$factory.depthNoiseScaleX, this.depthNoiseScaleX) == 0 && Float.compare(chunkgeneratorsettings$factory.depthNoiseScaleZ, this.depthNoiseScaleZ) == 0 && this.diamondCount == chunkgeneratorsettings$factory.diamondCount && this.diamondMaxHeight == chunkgeneratorsettings$factory.diamondMaxHeight && this.diamondMinHeight == chunkgeneratorsettings$factory.diamondMinHeight && this.diamondSize == chunkgeneratorsettings$factory.diamondSize && this.dioriteCount == chunkgeneratorsettings$factory.dioriteCount && this.dioriteMaxHeight == chunkgeneratorsettings$factory.dioriteMaxHeight && this.dioriteMinHeight == chunkgeneratorsettings$factory.dioriteMinHeight && this.dioriteSize == chunkgeneratorsettings$factory.dioriteSize && this.dirtCount == chunkgeneratorsettings$factory.dirtCount && this.dirtMaxHeight == chunkgeneratorsettings$factory.dirtMaxHeight && this.dirtMinHeight == chunkgeneratorsettings$factory.dirtMinHeight && this.dirtSize == chunkgeneratorsettings$factory.dirtSize && this.dungeonChance == chunkgeneratorsettings$factory.dungeonChance && this.fixedBiome == chunkgeneratorsettings$factory.fixedBiome && this.goldCount == chunkgeneratorsettings$factory.goldCount && this.goldMaxHeight == chunkgeneratorsettings$factory.goldMaxHeight && this.goldMinHeight == chunkgeneratorsettings$factory.goldMinHeight && this.goldSize == chunkgeneratorsettings$factory.goldSize && this.graniteCount == chunkgeneratorsettings$factory.graniteCount && this.graniteMaxHeight == chunkgeneratorsettings$factory.graniteMaxHeight && this.graniteMinHeight == chunkgeneratorsettings$factory.graniteMinHeight && this.graniteSize == chunkgeneratorsettings$factory.graniteSize && this.gravelCount == chunkgeneratorsettings$factory.gravelCount && this.gravelMaxHeight == chunkgeneratorsettings$factory.gravelMaxHeight && this.gravelMinHeight == chunkgeneratorsettings$factory.gravelMinHeight && this.gravelSize == chunkgeneratorsettings$factory.gravelSize && Float.compare(chunkgeneratorsettings$factory.heightScale, this.heightScale) == 0 && this.ironCount == chunkgeneratorsettings$factory.ironCount && this.ironMaxHeight == chunkgeneratorsettings$factory.ironMaxHeight && this.ironMinHeight == chunkgeneratorsettings$factory.ironMinHeight && this.ironSize == chunkgeneratorsettings$factory.ironSize && this.lapisCenterHeight == chunkgeneratorsettings$factory.lapisCenterHeight && this.lapisCount == chunkgeneratorsettings$factory.lapisCount && this.lapisSize == chunkgeneratorsettings$factory.lapisSize && this.lapisSpread == chunkgeneratorsettings$factory.lapisSpread && this.lavaLakeChance == chunkgeneratorsettings$factory.lavaLakeChance && Float.compare(chunkgeneratorsettings$factory.lowerLimitScale, this.lowerLimitScale) == 0 && Float.compare(chunkgeneratorsettings$factory.mainNoiseScaleX, this.mainNoiseScaleX) == 0 && Float.compare(chunkgeneratorsettings$factory.mainNoiseScaleY, this.mainNoiseScaleY) == 0 && Float.compare(chunkgeneratorsettings$factory.mainNoiseScaleZ, this.mainNoiseScaleZ) == 0 && this.redstoneCount == chunkgeneratorsettings$factory.redstoneCount && this.redstoneMaxHeight == chunkgeneratorsettings$factory.redstoneMaxHeight && this.redstoneMinHeight == chunkgeneratorsettings$factory.redstoneMinHeight && this.redstoneSize == chunkgeneratorsettings$factory.redstoneSize && this.riverSize == chunkgeneratorsettings$factory.riverSize && this.seaLevel == chunkgeneratorsettings$factory.seaLevel && Float.compare(chunkgeneratorsettings$factory.stretchY, this.stretchY) == 0 && Float.compare(chunkgeneratorsettings$factory.upperLimitScale, this.upperLimitScale) == 0 && this.useCaves == chunkgeneratorsettings$factory.useCaves && this.useDungeons == chunkgeneratorsettings$factory.useDungeons && this.useLavaLakes == chunkgeneratorsettings$factory.useLavaLakes && this.useLavaOceans == chunkgeneratorsettings$factory.useLavaOceans && this.useMineShafts == chunkgeneratorsettings$factory.useMineShafts && this.useRavines == chunkgeneratorsettings$factory.useRavines && this.useStrongholds == chunkgeneratorsettings$factory.useStrongholds && this.useTemples == chunkgeneratorsettings$factory.useTemples && this.useMonuments == chunkgeneratorsettings$factory.useMonuments && this.useMansions == chunkgeneratorsettings$factory.useMansions && this.useVillages == chunkgeneratorsettings$factory.useVillages && this.useWaterLakes == chunkgeneratorsettings$factory.useWaterLakes && this.waterLakeChance == chunkgeneratorsettings$factory.waterLakeChance;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int i = (this.coordinateScale == 0.0f) ? 0 : Float.floatToIntBits(this.coordinateScale);
            i = 31 * i + ((this.heightScale == 0.0f) ? 0 : Float.floatToIntBits(this.heightScale));
            i = 31 * i + ((this.upperLimitScale == 0.0f) ? 0 : Float.floatToIntBits(this.upperLimitScale));
            i = 31 * i + ((this.lowerLimitScale == 0.0f) ? 0 : Float.floatToIntBits(this.lowerLimitScale));
            i = 31 * i + ((this.depthNoiseScaleX == 0.0f) ? 0 : Float.floatToIntBits(this.depthNoiseScaleX));
            i = 31 * i + ((this.depthNoiseScaleZ == 0.0f) ? 0 : Float.floatToIntBits(this.depthNoiseScaleZ));
            i = 31 * i + ((this.depthNoiseScaleExponent == 0.0f) ? 0 : Float.floatToIntBits(this.depthNoiseScaleExponent));
            i = 31 * i + ((this.mainNoiseScaleX == 0.0f) ? 0 : Float.floatToIntBits(this.mainNoiseScaleX));
            i = 31 * i + ((this.mainNoiseScaleY == 0.0f) ? 0 : Float.floatToIntBits(this.mainNoiseScaleY));
            i = 31 * i + ((this.mainNoiseScaleZ == 0.0f) ? 0 : Float.floatToIntBits(this.mainNoiseScaleZ));
            i = 31 * i + ((this.baseSize == 0.0f) ? 0 : Float.floatToIntBits(this.baseSize));
            i = 31 * i + ((this.stretchY == 0.0f) ? 0 : Float.floatToIntBits(this.stretchY));
            i = 31 * i + ((this.biomeDepthWeight == 0.0f) ? 0 : Float.floatToIntBits(this.biomeDepthWeight));
            i = 31 * i + ((this.biomeDepthOffset == 0.0f) ? 0 : Float.floatToIntBits(this.biomeDepthOffset));
            i = 31 * i + ((this.biomeScaleWeight == 0.0f) ? 0 : Float.floatToIntBits(this.biomeScaleWeight));
            i = 31 * i + ((this.biomeScaleOffset == 0.0f) ? 0 : Float.floatToIntBits(this.biomeScaleOffset));
            i = 31 * i + this.seaLevel;
            i = 31 * i + (this.useCaves ? 1 : 0);
            i = 31 * i + (this.useDungeons ? 1 : 0);
            i = 31 * i + this.dungeonChance;
            i = 31 * i + (this.useStrongholds ? 1 : 0);
            i = 31 * i + (this.useVillages ? 1 : 0);
            i = 31 * i + (this.useMineShafts ? 1 : 0);
            i = 31 * i + (this.useTemples ? 1 : 0);
            i = 31 * i + (this.useMonuments ? 1 : 0);
            i = 31 * i + (this.useMansions ? 1 : 0);
            i = 31 * i + (this.useRavines ? 1 : 0);
            i = 31 * i + (this.useWaterLakes ? 1 : 0);
            i = 31 * i + this.waterLakeChance;
            i = 31 * i + (this.useLavaLakes ? 1 : 0);
            i = 31 * i + this.lavaLakeChance;
            i = 31 * i + (this.useLavaOceans ? 1 : 0);
            i = 31 * i + this.fixedBiome;
            i = 31 * i + this.biomeSize;
            i = 31 * i + this.riverSize;
            i = 31 * i + this.dirtSize;
            i = 31 * i + this.dirtCount;
            i = 31 * i + this.dirtMinHeight;
            i = 31 * i + this.dirtMaxHeight;
            i = 31 * i + this.gravelSize;
            i = 31 * i + this.gravelCount;
            i = 31 * i + this.gravelMinHeight;
            i = 31 * i + this.gravelMaxHeight;
            i = 31 * i + this.graniteSize;
            i = 31 * i + this.graniteCount;
            i = 31 * i + this.graniteMinHeight;
            i = 31 * i + this.graniteMaxHeight;
            i = 31 * i + this.dioriteSize;
            i = 31 * i + this.dioriteCount;
            i = 31 * i + this.dioriteMinHeight;
            i = 31 * i + this.dioriteMaxHeight;
            i = 31 * i + this.andesiteSize;
            i = 31 * i + this.andesiteCount;
            i = 31 * i + this.andesiteMinHeight;
            i = 31 * i + this.andesiteMaxHeight;
            i = 31 * i + this.coalSize;
            i = 31 * i + this.coalCount;
            i = 31 * i + this.coalMinHeight;
            i = 31 * i + this.coalMaxHeight;
            i = 31 * i + this.ironSize;
            i = 31 * i + this.ironCount;
            i = 31 * i + this.ironMinHeight;
            i = 31 * i + this.ironMaxHeight;
            i = 31 * i + this.goldSize;
            i = 31 * i + this.goldCount;
            i = 31 * i + this.goldMinHeight;
            i = 31 * i + this.goldMaxHeight;
            i = 31 * i + this.redstoneSize;
            i = 31 * i + this.redstoneCount;
            i = 31 * i + this.redstoneMinHeight;
            i = 31 * i + this.redstoneMaxHeight;
            i = 31 * i + this.diamondSize;
            i = 31 * i + this.diamondCount;
            i = 31 * i + this.diamondMinHeight;
            i = 31 * i + this.diamondMaxHeight;
            i = 31 * i + this.lapisSize;
            i = 31 * i + this.lapisCount;
            i = 31 * i + this.lapisCenterHeight;
            i = 31 * i + this.lapisSpread;
            return i;
        }
        
        public ChunkGeneratorSettings build() {
            return new ChunkGeneratorSettings(this, null);
        }
        
        static {
            JSON_ADAPTER = new GsonBuilder().registerTypeAdapter((Type)Factory.class, (Object)new Serializer()).create();
        }
    }
    
    public static class Serializer implements JsonDeserializer<Factory>, JsonSerializer<Factory>
    {
        public Factory deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final Factory chunkgeneratorsettings$factory = new Factory();
            try {
                chunkgeneratorsettings$factory.coordinateScale = JsonUtils.getFloat(jsonobject, "coordinateScale", chunkgeneratorsettings$factory.coordinateScale);
                chunkgeneratorsettings$factory.heightScale = JsonUtils.getFloat(jsonobject, "heightScale", chunkgeneratorsettings$factory.heightScale);
                chunkgeneratorsettings$factory.lowerLimitScale = JsonUtils.getFloat(jsonobject, "lowerLimitScale", chunkgeneratorsettings$factory.lowerLimitScale);
                chunkgeneratorsettings$factory.upperLimitScale = JsonUtils.getFloat(jsonobject, "upperLimitScale", chunkgeneratorsettings$factory.upperLimitScale);
                chunkgeneratorsettings$factory.depthNoiseScaleX = JsonUtils.getFloat(jsonobject, "depthNoiseScaleX", chunkgeneratorsettings$factory.depthNoiseScaleX);
                chunkgeneratorsettings$factory.depthNoiseScaleZ = JsonUtils.getFloat(jsonobject, "depthNoiseScaleZ", chunkgeneratorsettings$factory.depthNoiseScaleZ);
                chunkgeneratorsettings$factory.depthNoiseScaleExponent = JsonUtils.getFloat(jsonobject, "depthNoiseScaleExponent", chunkgeneratorsettings$factory.depthNoiseScaleExponent);
                chunkgeneratorsettings$factory.mainNoiseScaleX = JsonUtils.getFloat(jsonobject, "mainNoiseScaleX", chunkgeneratorsettings$factory.mainNoiseScaleX);
                chunkgeneratorsettings$factory.mainNoiseScaleY = JsonUtils.getFloat(jsonobject, "mainNoiseScaleY", chunkgeneratorsettings$factory.mainNoiseScaleY);
                chunkgeneratorsettings$factory.mainNoiseScaleZ = JsonUtils.getFloat(jsonobject, "mainNoiseScaleZ", chunkgeneratorsettings$factory.mainNoiseScaleZ);
                chunkgeneratorsettings$factory.baseSize = JsonUtils.getFloat(jsonobject, "baseSize", chunkgeneratorsettings$factory.baseSize);
                chunkgeneratorsettings$factory.stretchY = JsonUtils.getFloat(jsonobject, "stretchY", chunkgeneratorsettings$factory.stretchY);
                chunkgeneratorsettings$factory.biomeDepthWeight = JsonUtils.getFloat(jsonobject, "biomeDepthWeight", chunkgeneratorsettings$factory.biomeDepthWeight);
                chunkgeneratorsettings$factory.biomeDepthOffset = JsonUtils.getFloat(jsonobject, "biomeDepthOffset", chunkgeneratorsettings$factory.biomeDepthOffset);
                chunkgeneratorsettings$factory.biomeScaleWeight = JsonUtils.getFloat(jsonobject, "biomeScaleWeight", chunkgeneratorsettings$factory.biomeScaleWeight);
                chunkgeneratorsettings$factory.biomeScaleOffset = JsonUtils.getFloat(jsonobject, "biomeScaleOffset", chunkgeneratorsettings$factory.biomeScaleOffset);
                chunkgeneratorsettings$factory.seaLevel = JsonUtils.getInt(jsonobject, "seaLevel", chunkgeneratorsettings$factory.seaLevel);
                chunkgeneratorsettings$factory.useCaves = JsonUtils.getBoolean(jsonobject, "useCaves", chunkgeneratorsettings$factory.useCaves);
                chunkgeneratorsettings$factory.useDungeons = JsonUtils.getBoolean(jsonobject, "useDungeons", chunkgeneratorsettings$factory.useDungeons);
                chunkgeneratorsettings$factory.dungeonChance = JsonUtils.getInt(jsonobject, "dungeonChance", chunkgeneratorsettings$factory.dungeonChance);
                chunkgeneratorsettings$factory.useStrongholds = JsonUtils.getBoolean(jsonobject, "useStrongholds", chunkgeneratorsettings$factory.useStrongholds);
                chunkgeneratorsettings$factory.useVillages = JsonUtils.getBoolean(jsonobject, "useVillages", chunkgeneratorsettings$factory.useVillages);
                chunkgeneratorsettings$factory.useMineShafts = JsonUtils.getBoolean(jsonobject, "useMineShafts", chunkgeneratorsettings$factory.useMineShafts);
                chunkgeneratorsettings$factory.useTemples = JsonUtils.getBoolean(jsonobject, "useTemples", chunkgeneratorsettings$factory.useTemples);
                chunkgeneratorsettings$factory.useMonuments = JsonUtils.getBoolean(jsonobject, "useMonuments", chunkgeneratorsettings$factory.useMonuments);
                chunkgeneratorsettings$factory.useMansions = JsonUtils.getBoolean(jsonobject, "useMansions", chunkgeneratorsettings$factory.useMansions);
                chunkgeneratorsettings$factory.useRavines = JsonUtils.getBoolean(jsonobject, "useRavines", chunkgeneratorsettings$factory.useRavines);
                chunkgeneratorsettings$factory.useWaterLakes = JsonUtils.getBoolean(jsonobject, "useWaterLakes", chunkgeneratorsettings$factory.useWaterLakes);
                chunkgeneratorsettings$factory.waterLakeChance = JsonUtils.getInt(jsonobject, "waterLakeChance", chunkgeneratorsettings$factory.waterLakeChance);
                chunkgeneratorsettings$factory.useLavaLakes = JsonUtils.getBoolean(jsonobject, "useLavaLakes", chunkgeneratorsettings$factory.useLavaLakes);
                chunkgeneratorsettings$factory.lavaLakeChance = JsonUtils.getInt(jsonobject, "lavaLakeChance", chunkgeneratorsettings$factory.lavaLakeChance);
                chunkgeneratorsettings$factory.useLavaOceans = JsonUtils.getBoolean(jsonobject, "useLavaOceans", chunkgeneratorsettings$factory.useLavaOceans);
                chunkgeneratorsettings$factory.fixedBiome = JsonUtils.getInt(jsonobject, "fixedBiome", chunkgeneratorsettings$factory.fixedBiome);
                if (chunkgeneratorsettings$factory.fixedBiome < 38 && chunkgeneratorsettings$factory.fixedBiome >= -1) {
                    if (chunkgeneratorsettings$factory.fixedBiome >= Biome.getIdForBiome(Biomes.HELL)) {
                        final Factory factory = chunkgeneratorsettings$factory;
                        factory.fixedBiome += 2;
                    }
                }
                else {
                    chunkgeneratorsettings$factory.fixedBiome = -1;
                }
                chunkgeneratorsettings$factory.biomeSize = JsonUtils.getInt(jsonobject, "biomeSize", chunkgeneratorsettings$factory.biomeSize);
                chunkgeneratorsettings$factory.riverSize = JsonUtils.getInt(jsonobject, "riverSize", chunkgeneratorsettings$factory.riverSize);
                chunkgeneratorsettings$factory.dirtSize = JsonUtils.getInt(jsonobject, "dirtSize", chunkgeneratorsettings$factory.dirtSize);
                chunkgeneratorsettings$factory.dirtCount = JsonUtils.getInt(jsonobject, "dirtCount", chunkgeneratorsettings$factory.dirtCount);
                chunkgeneratorsettings$factory.dirtMinHeight = JsonUtils.getInt(jsonobject, "dirtMinHeight", chunkgeneratorsettings$factory.dirtMinHeight);
                chunkgeneratorsettings$factory.dirtMaxHeight = JsonUtils.getInt(jsonobject, "dirtMaxHeight", chunkgeneratorsettings$factory.dirtMaxHeight);
                chunkgeneratorsettings$factory.gravelSize = JsonUtils.getInt(jsonobject, "gravelSize", chunkgeneratorsettings$factory.gravelSize);
                chunkgeneratorsettings$factory.gravelCount = JsonUtils.getInt(jsonobject, "gravelCount", chunkgeneratorsettings$factory.gravelCount);
                chunkgeneratorsettings$factory.gravelMinHeight = JsonUtils.getInt(jsonobject, "gravelMinHeight", chunkgeneratorsettings$factory.gravelMinHeight);
                chunkgeneratorsettings$factory.gravelMaxHeight = JsonUtils.getInt(jsonobject, "gravelMaxHeight", chunkgeneratorsettings$factory.gravelMaxHeight);
                chunkgeneratorsettings$factory.graniteSize = JsonUtils.getInt(jsonobject, "graniteSize", chunkgeneratorsettings$factory.graniteSize);
                chunkgeneratorsettings$factory.graniteCount = JsonUtils.getInt(jsonobject, "graniteCount", chunkgeneratorsettings$factory.graniteCount);
                chunkgeneratorsettings$factory.graniteMinHeight = JsonUtils.getInt(jsonobject, "graniteMinHeight", chunkgeneratorsettings$factory.graniteMinHeight);
                chunkgeneratorsettings$factory.graniteMaxHeight = JsonUtils.getInt(jsonobject, "graniteMaxHeight", chunkgeneratorsettings$factory.graniteMaxHeight);
                chunkgeneratorsettings$factory.dioriteSize = JsonUtils.getInt(jsonobject, "dioriteSize", chunkgeneratorsettings$factory.dioriteSize);
                chunkgeneratorsettings$factory.dioriteCount = JsonUtils.getInt(jsonobject, "dioriteCount", chunkgeneratorsettings$factory.dioriteCount);
                chunkgeneratorsettings$factory.dioriteMinHeight = JsonUtils.getInt(jsonobject, "dioriteMinHeight", chunkgeneratorsettings$factory.dioriteMinHeight);
                chunkgeneratorsettings$factory.dioriteMaxHeight = JsonUtils.getInt(jsonobject, "dioriteMaxHeight", chunkgeneratorsettings$factory.dioriteMaxHeight);
                chunkgeneratorsettings$factory.andesiteSize = JsonUtils.getInt(jsonobject, "andesiteSize", chunkgeneratorsettings$factory.andesiteSize);
                chunkgeneratorsettings$factory.andesiteCount = JsonUtils.getInt(jsonobject, "andesiteCount", chunkgeneratorsettings$factory.andesiteCount);
                chunkgeneratorsettings$factory.andesiteMinHeight = JsonUtils.getInt(jsonobject, "andesiteMinHeight", chunkgeneratorsettings$factory.andesiteMinHeight);
                chunkgeneratorsettings$factory.andesiteMaxHeight = JsonUtils.getInt(jsonobject, "andesiteMaxHeight", chunkgeneratorsettings$factory.andesiteMaxHeight);
                chunkgeneratorsettings$factory.coalSize = JsonUtils.getInt(jsonobject, "coalSize", chunkgeneratorsettings$factory.coalSize);
                chunkgeneratorsettings$factory.coalCount = JsonUtils.getInt(jsonobject, "coalCount", chunkgeneratorsettings$factory.coalCount);
                chunkgeneratorsettings$factory.coalMinHeight = JsonUtils.getInt(jsonobject, "coalMinHeight", chunkgeneratorsettings$factory.coalMinHeight);
                chunkgeneratorsettings$factory.coalMaxHeight = JsonUtils.getInt(jsonobject, "coalMaxHeight", chunkgeneratorsettings$factory.coalMaxHeight);
                chunkgeneratorsettings$factory.ironSize = JsonUtils.getInt(jsonobject, "ironSize", chunkgeneratorsettings$factory.ironSize);
                chunkgeneratorsettings$factory.ironCount = JsonUtils.getInt(jsonobject, "ironCount", chunkgeneratorsettings$factory.ironCount);
                chunkgeneratorsettings$factory.ironMinHeight = JsonUtils.getInt(jsonobject, "ironMinHeight", chunkgeneratorsettings$factory.ironMinHeight);
                chunkgeneratorsettings$factory.ironMaxHeight = JsonUtils.getInt(jsonobject, "ironMaxHeight", chunkgeneratorsettings$factory.ironMaxHeight);
                chunkgeneratorsettings$factory.goldSize = JsonUtils.getInt(jsonobject, "goldSize", chunkgeneratorsettings$factory.goldSize);
                chunkgeneratorsettings$factory.goldCount = JsonUtils.getInt(jsonobject, "goldCount", chunkgeneratorsettings$factory.goldCount);
                chunkgeneratorsettings$factory.goldMinHeight = JsonUtils.getInt(jsonobject, "goldMinHeight", chunkgeneratorsettings$factory.goldMinHeight);
                chunkgeneratorsettings$factory.goldMaxHeight = JsonUtils.getInt(jsonobject, "goldMaxHeight", chunkgeneratorsettings$factory.goldMaxHeight);
                chunkgeneratorsettings$factory.redstoneSize = JsonUtils.getInt(jsonobject, "redstoneSize", chunkgeneratorsettings$factory.redstoneSize);
                chunkgeneratorsettings$factory.redstoneCount = JsonUtils.getInt(jsonobject, "redstoneCount", chunkgeneratorsettings$factory.redstoneCount);
                chunkgeneratorsettings$factory.redstoneMinHeight = JsonUtils.getInt(jsonobject, "redstoneMinHeight", chunkgeneratorsettings$factory.redstoneMinHeight);
                chunkgeneratorsettings$factory.redstoneMaxHeight = JsonUtils.getInt(jsonobject, "redstoneMaxHeight", chunkgeneratorsettings$factory.redstoneMaxHeight);
                chunkgeneratorsettings$factory.diamondSize = JsonUtils.getInt(jsonobject, "diamondSize", chunkgeneratorsettings$factory.diamondSize);
                chunkgeneratorsettings$factory.diamondCount = JsonUtils.getInt(jsonobject, "diamondCount", chunkgeneratorsettings$factory.diamondCount);
                chunkgeneratorsettings$factory.diamondMinHeight = JsonUtils.getInt(jsonobject, "diamondMinHeight", chunkgeneratorsettings$factory.diamondMinHeight);
                chunkgeneratorsettings$factory.diamondMaxHeight = JsonUtils.getInt(jsonobject, "diamondMaxHeight", chunkgeneratorsettings$factory.diamondMaxHeight);
                chunkgeneratorsettings$factory.lapisSize = JsonUtils.getInt(jsonobject, "lapisSize", chunkgeneratorsettings$factory.lapisSize);
                chunkgeneratorsettings$factory.lapisCount = JsonUtils.getInt(jsonobject, "lapisCount", chunkgeneratorsettings$factory.lapisCount);
                chunkgeneratorsettings$factory.lapisCenterHeight = JsonUtils.getInt(jsonobject, "lapisCenterHeight", chunkgeneratorsettings$factory.lapisCenterHeight);
                chunkgeneratorsettings$factory.lapisSpread = JsonUtils.getInt(jsonobject, "lapisSpread", chunkgeneratorsettings$factory.lapisSpread);
            }
            catch (Exception ex) {}
            return chunkgeneratorsettings$factory;
        }
        
        public JsonElement serialize(final Factory p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("coordinateScale", (Number)p_serialize_1_.coordinateScale);
            jsonobject.addProperty("heightScale", (Number)p_serialize_1_.heightScale);
            jsonobject.addProperty("lowerLimitScale", (Number)p_serialize_1_.lowerLimitScale);
            jsonobject.addProperty("upperLimitScale", (Number)p_serialize_1_.upperLimitScale);
            jsonobject.addProperty("depthNoiseScaleX", (Number)p_serialize_1_.depthNoiseScaleX);
            jsonobject.addProperty("depthNoiseScaleZ", (Number)p_serialize_1_.depthNoiseScaleZ);
            jsonobject.addProperty("depthNoiseScaleExponent", (Number)p_serialize_1_.depthNoiseScaleExponent);
            jsonobject.addProperty("mainNoiseScaleX", (Number)p_serialize_1_.mainNoiseScaleX);
            jsonobject.addProperty("mainNoiseScaleY", (Number)p_serialize_1_.mainNoiseScaleY);
            jsonobject.addProperty("mainNoiseScaleZ", (Number)p_serialize_1_.mainNoiseScaleZ);
            jsonobject.addProperty("baseSize", (Number)p_serialize_1_.baseSize);
            jsonobject.addProperty("stretchY", (Number)p_serialize_1_.stretchY);
            jsonobject.addProperty("biomeDepthWeight", (Number)p_serialize_1_.biomeDepthWeight);
            jsonobject.addProperty("biomeDepthOffset", (Number)p_serialize_1_.biomeDepthOffset);
            jsonobject.addProperty("biomeScaleWeight", (Number)p_serialize_1_.biomeScaleWeight);
            jsonobject.addProperty("biomeScaleOffset", (Number)p_serialize_1_.biomeScaleOffset);
            jsonobject.addProperty("seaLevel", (Number)p_serialize_1_.seaLevel);
            jsonobject.addProperty("useCaves", Boolean.valueOf(p_serialize_1_.useCaves));
            jsonobject.addProperty("useDungeons", Boolean.valueOf(p_serialize_1_.useDungeons));
            jsonobject.addProperty("dungeonChance", (Number)p_serialize_1_.dungeonChance);
            jsonobject.addProperty("useStrongholds", Boolean.valueOf(p_serialize_1_.useStrongholds));
            jsonobject.addProperty("useVillages", Boolean.valueOf(p_serialize_1_.useVillages));
            jsonobject.addProperty("useMineShafts", Boolean.valueOf(p_serialize_1_.useMineShafts));
            jsonobject.addProperty("useTemples", Boolean.valueOf(p_serialize_1_.useTemples));
            jsonobject.addProperty("useMonuments", Boolean.valueOf(p_serialize_1_.useMonuments));
            jsonobject.addProperty("useMansions", Boolean.valueOf(p_serialize_1_.useMansions));
            jsonobject.addProperty("useRavines", Boolean.valueOf(p_serialize_1_.useRavines));
            jsonobject.addProperty("useWaterLakes", Boolean.valueOf(p_serialize_1_.useWaterLakes));
            jsonobject.addProperty("waterLakeChance", (Number)p_serialize_1_.waterLakeChance);
            jsonobject.addProperty("useLavaLakes", Boolean.valueOf(p_serialize_1_.useLavaLakes));
            jsonobject.addProperty("lavaLakeChance", (Number)p_serialize_1_.lavaLakeChance);
            jsonobject.addProperty("useLavaOceans", Boolean.valueOf(p_serialize_1_.useLavaOceans));
            jsonobject.addProperty("fixedBiome", (Number)p_serialize_1_.fixedBiome);
            jsonobject.addProperty("biomeSize", (Number)p_serialize_1_.biomeSize);
            jsonobject.addProperty("riverSize", (Number)p_serialize_1_.riverSize);
            jsonobject.addProperty("dirtSize", (Number)p_serialize_1_.dirtSize);
            jsonobject.addProperty("dirtCount", (Number)p_serialize_1_.dirtCount);
            jsonobject.addProperty("dirtMinHeight", (Number)p_serialize_1_.dirtMinHeight);
            jsonobject.addProperty("dirtMaxHeight", (Number)p_serialize_1_.dirtMaxHeight);
            jsonobject.addProperty("gravelSize", (Number)p_serialize_1_.gravelSize);
            jsonobject.addProperty("gravelCount", (Number)p_serialize_1_.gravelCount);
            jsonobject.addProperty("gravelMinHeight", (Number)p_serialize_1_.gravelMinHeight);
            jsonobject.addProperty("gravelMaxHeight", (Number)p_serialize_1_.gravelMaxHeight);
            jsonobject.addProperty("graniteSize", (Number)p_serialize_1_.graniteSize);
            jsonobject.addProperty("graniteCount", (Number)p_serialize_1_.graniteCount);
            jsonobject.addProperty("graniteMinHeight", (Number)p_serialize_1_.graniteMinHeight);
            jsonobject.addProperty("graniteMaxHeight", (Number)p_serialize_1_.graniteMaxHeight);
            jsonobject.addProperty("dioriteSize", (Number)p_serialize_1_.dioriteSize);
            jsonobject.addProperty("dioriteCount", (Number)p_serialize_1_.dioriteCount);
            jsonobject.addProperty("dioriteMinHeight", (Number)p_serialize_1_.dioriteMinHeight);
            jsonobject.addProperty("dioriteMaxHeight", (Number)p_serialize_1_.dioriteMaxHeight);
            jsonobject.addProperty("andesiteSize", (Number)p_serialize_1_.andesiteSize);
            jsonobject.addProperty("andesiteCount", (Number)p_serialize_1_.andesiteCount);
            jsonobject.addProperty("andesiteMinHeight", (Number)p_serialize_1_.andesiteMinHeight);
            jsonobject.addProperty("andesiteMaxHeight", (Number)p_serialize_1_.andesiteMaxHeight);
            jsonobject.addProperty("coalSize", (Number)p_serialize_1_.coalSize);
            jsonobject.addProperty("coalCount", (Number)p_serialize_1_.coalCount);
            jsonobject.addProperty("coalMinHeight", (Number)p_serialize_1_.coalMinHeight);
            jsonobject.addProperty("coalMaxHeight", (Number)p_serialize_1_.coalMaxHeight);
            jsonobject.addProperty("ironSize", (Number)p_serialize_1_.ironSize);
            jsonobject.addProperty("ironCount", (Number)p_serialize_1_.ironCount);
            jsonobject.addProperty("ironMinHeight", (Number)p_serialize_1_.ironMinHeight);
            jsonobject.addProperty("ironMaxHeight", (Number)p_serialize_1_.ironMaxHeight);
            jsonobject.addProperty("goldSize", (Number)p_serialize_1_.goldSize);
            jsonobject.addProperty("goldCount", (Number)p_serialize_1_.goldCount);
            jsonobject.addProperty("goldMinHeight", (Number)p_serialize_1_.goldMinHeight);
            jsonobject.addProperty("goldMaxHeight", (Number)p_serialize_1_.goldMaxHeight);
            jsonobject.addProperty("redstoneSize", (Number)p_serialize_1_.redstoneSize);
            jsonobject.addProperty("redstoneCount", (Number)p_serialize_1_.redstoneCount);
            jsonobject.addProperty("redstoneMinHeight", (Number)p_serialize_1_.redstoneMinHeight);
            jsonobject.addProperty("redstoneMaxHeight", (Number)p_serialize_1_.redstoneMaxHeight);
            jsonobject.addProperty("diamondSize", (Number)p_serialize_1_.diamondSize);
            jsonobject.addProperty("diamondCount", (Number)p_serialize_1_.diamondCount);
            jsonobject.addProperty("diamondMinHeight", (Number)p_serialize_1_.diamondMinHeight);
            jsonobject.addProperty("diamondMaxHeight", (Number)p_serialize_1_.diamondMaxHeight);
            jsonobject.addProperty("lapisSize", (Number)p_serialize_1_.lapisSize);
            jsonobject.addProperty("lapisCount", (Number)p_serialize_1_.lapisCount);
            jsonobject.addProperty("lapisCenterHeight", (Number)p_serialize_1_.lapisCenterHeight);
            jsonobject.addProperty("lapisSpread", (Number)p_serialize_1_.lapisSpread);
            return (JsonElement)jsonobject;
        }
    }
}
