/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 */
package net.minecraft.world.gen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.biome.BiomeGenBase;

public class ChunkProviderSettings {
    public final int dioriteMaxHeight;
    public final int dirtCount;
    public final float biomeScaleWeight;
    public final int lapisCenterHeight;
    public final boolean useLavaOceans;
    public final float stretchY;
    public final float depthNoiseScaleExponent;
    public final int diamondSize;
    public final int dioriteCount;
    public final int redstoneMinHeight;
    public final int coalSize;
    public final int diamondMinHeight;
    public final int goldMaxHeight;
    public final int lapisSpread;
    public final int redstoneSize;
    public final int graniteMaxHeight;
    public final boolean useCaves;
    public final float mainNoiseScaleX;
    public final float baseSize;
    public final boolean useWaterLakes;
    public final float mainNoiseScaleZ;
    public final int goldSize;
    public final int fixedBiome;
    public final int dirtSize;
    public final float upperLimitScale;
    public final int ironCount;
    public final boolean useRavines;
    public final boolean useTemples;
    public final float biomeScaleOffset;
    public final int graniteSize;
    public final int ironMinHeight;
    public final int andesiteMaxHeight;
    public final int goldMinHeight;
    public final float mainNoiseScaleY;
    public final int lavaLakeChance;
    public final boolean useMonuments;
    public final int coalCount;
    public final float biomeDepthWeight;
    public final int dioriteSize;
    public final int biomeSize;
    public final boolean useMineShafts;
    public final float depthNoiseScaleZ;
    public final float lowerLimitScale;
    public final boolean useLavaLakes;
    public final boolean useDungeons;
    public final int ironSize;
    public final float coordinateScale;
    public final int gravelSize;
    public final int dirtMinHeight;
    public final int redstoneMaxHeight;
    public final int dungeonChance;
    public final int lapisCount;
    public final int gravelMaxHeight;
    public final int dirtMaxHeight;
    public final float biomeDepthOffSet;
    public final float heightScale;
    public final int graniteMinHeight;
    public final int graniteCount;
    public final int gravelCount;
    public final int seaLevel;
    public final int redstoneCount;
    public final int dioriteMinHeight;
    public final int diamondCount;
    public final int andesiteMinHeight;
    public final int waterLakeChance;
    public final int diamondMaxHeight;
    public final int ironMaxHeight;
    public final int andesiteSize;
    public final int riverSize;
    public final int gravelMinHeight;
    public final int coalMinHeight;
    public final int lapisSize;
    public final int coalMaxHeight;
    public final int andesiteCount;
    public final boolean useStrongholds;
    public final int goldCount;
    public final float depthNoiseScaleX;
    public final boolean useVillages;

    /* synthetic */ ChunkProviderSettings(Factory factory, ChunkProviderSettings chunkProviderSettings) {
        this(factory);
    }

    private ChunkProviderSettings(Factory factory) {
        this.coordinateScale = factory.coordinateScale;
        this.heightScale = factory.heightScale;
        this.upperLimitScale = factory.upperLimitScale;
        this.lowerLimitScale = factory.lowerLimitScale;
        this.depthNoiseScaleX = factory.depthNoiseScaleX;
        this.depthNoiseScaleZ = factory.depthNoiseScaleZ;
        this.depthNoiseScaleExponent = factory.depthNoiseScaleExponent;
        this.mainNoiseScaleX = factory.mainNoiseScaleX;
        this.mainNoiseScaleY = factory.mainNoiseScaleY;
        this.mainNoiseScaleZ = factory.mainNoiseScaleZ;
        this.baseSize = factory.baseSize;
        this.stretchY = factory.stretchY;
        this.biomeDepthWeight = factory.biomeDepthWeight;
        this.biomeDepthOffSet = factory.biomeDepthOffset;
        this.biomeScaleWeight = factory.biomeScaleWeight;
        this.biomeScaleOffset = factory.biomeScaleOffset;
        this.seaLevel = factory.seaLevel;
        this.useCaves = factory.useCaves;
        this.useDungeons = factory.useDungeons;
        this.dungeonChance = factory.dungeonChance;
        this.useStrongholds = factory.useStrongholds;
        this.useVillages = factory.useVillages;
        this.useMineShafts = factory.useMineShafts;
        this.useTemples = factory.useTemples;
        this.useMonuments = factory.useMonuments;
        this.useRavines = factory.useRavines;
        this.useWaterLakes = factory.useWaterLakes;
        this.waterLakeChance = factory.waterLakeChance;
        this.useLavaLakes = factory.useLavaLakes;
        this.lavaLakeChance = factory.lavaLakeChance;
        this.useLavaOceans = factory.useLavaOceans;
        this.fixedBiome = factory.fixedBiome;
        this.biomeSize = factory.biomeSize;
        this.riverSize = factory.riverSize;
        this.dirtSize = factory.dirtSize;
        this.dirtCount = factory.dirtCount;
        this.dirtMinHeight = factory.dirtMinHeight;
        this.dirtMaxHeight = factory.dirtMaxHeight;
        this.gravelSize = factory.gravelSize;
        this.gravelCount = factory.gravelCount;
        this.gravelMinHeight = factory.gravelMinHeight;
        this.gravelMaxHeight = factory.gravelMaxHeight;
        this.graniteSize = factory.graniteSize;
        this.graniteCount = factory.graniteCount;
        this.graniteMinHeight = factory.graniteMinHeight;
        this.graniteMaxHeight = factory.graniteMaxHeight;
        this.dioriteSize = factory.dioriteSize;
        this.dioriteCount = factory.dioriteCount;
        this.dioriteMinHeight = factory.dioriteMinHeight;
        this.dioriteMaxHeight = factory.dioriteMaxHeight;
        this.andesiteSize = factory.andesiteSize;
        this.andesiteCount = factory.andesiteCount;
        this.andesiteMinHeight = factory.andesiteMinHeight;
        this.andesiteMaxHeight = factory.andesiteMaxHeight;
        this.coalSize = factory.coalSize;
        this.coalCount = factory.coalCount;
        this.coalMinHeight = factory.coalMinHeight;
        this.coalMaxHeight = factory.coalMaxHeight;
        this.ironSize = factory.ironSize;
        this.ironCount = factory.ironCount;
        this.ironMinHeight = factory.ironMinHeight;
        this.ironMaxHeight = factory.ironMaxHeight;
        this.goldSize = factory.goldSize;
        this.goldCount = factory.goldCount;
        this.goldMinHeight = factory.goldMinHeight;
        this.goldMaxHeight = factory.goldMaxHeight;
        this.redstoneSize = factory.redstoneSize;
        this.redstoneCount = factory.redstoneCount;
        this.redstoneMinHeight = factory.redstoneMinHeight;
        this.redstoneMaxHeight = factory.redstoneMaxHeight;
        this.diamondSize = factory.diamondSize;
        this.diamondCount = factory.diamondCount;
        this.diamondMinHeight = factory.diamondMinHeight;
        this.diamondMaxHeight = factory.diamondMaxHeight;
        this.lapisSize = factory.lapisSize;
        this.lapisCount = factory.lapisCount;
        this.lapisCenterHeight = factory.lapisCenterHeight;
        this.lapisSpread = factory.lapisSpread;
    }

    public static class Serializer
    implements JsonDeserializer<Factory>,
    JsonSerializer<Factory> {
        public Factory deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Factory factory = new Factory();
            try {
                factory.coordinateScale = JsonUtils.getFloat(jsonObject, "coordinateScale", factory.coordinateScale);
                factory.heightScale = JsonUtils.getFloat(jsonObject, "heightScale", factory.heightScale);
                factory.lowerLimitScale = JsonUtils.getFloat(jsonObject, "lowerLimitScale", factory.lowerLimitScale);
                factory.upperLimitScale = JsonUtils.getFloat(jsonObject, "upperLimitScale", factory.upperLimitScale);
                factory.depthNoiseScaleX = JsonUtils.getFloat(jsonObject, "depthNoiseScaleX", factory.depthNoiseScaleX);
                factory.depthNoiseScaleZ = JsonUtils.getFloat(jsonObject, "depthNoiseScaleZ", factory.depthNoiseScaleZ);
                factory.depthNoiseScaleExponent = JsonUtils.getFloat(jsonObject, "depthNoiseScaleExponent", factory.depthNoiseScaleExponent);
                factory.mainNoiseScaleX = JsonUtils.getFloat(jsonObject, "mainNoiseScaleX", factory.mainNoiseScaleX);
                factory.mainNoiseScaleY = JsonUtils.getFloat(jsonObject, "mainNoiseScaleY", factory.mainNoiseScaleY);
                factory.mainNoiseScaleZ = JsonUtils.getFloat(jsonObject, "mainNoiseScaleZ", factory.mainNoiseScaleZ);
                factory.baseSize = JsonUtils.getFloat(jsonObject, "baseSize", factory.baseSize);
                factory.stretchY = JsonUtils.getFloat(jsonObject, "stretchY", factory.stretchY);
                factory.biomeDepthWeight = JsonUtils.getFloat(jsonObject, "biomeDepthWeight", factory.biomeDepthWeight);
                factory.biomeDepthOffset = JsonUtils.getFloat(jsonObject, "biomeDepthOffset", factory.biomeDepthOffset);
                factory.biomeScaleWeight = JsonUtils.getFloat(jsonObject, "biomeScaleWeight", factory.biomeScaleWeight);
                factory.biomeScaleOffset = JsonUtils.getFloat(jsonObject, "biomeScaleOffset", factory.biomeScaleOffset);
                factory.seaLevel = JsonUtils.getInt(jsonObject, "seaLevel", factory.seaLevel);
                factory.useCaves = JsonUtils.getBoolean(jsonObject, "useCaves", factory.useCaves);
                factory.useDungeons = JsonUtils.getBoolean(jsonObject, "useDungeons", factory.useDungeons);
                factory.dungeonChance = JsonUtils.getInt(jsonObject, "dungeonChance", factory.dungeonChance);
                factory.useStrongholds = JsonUtils.getBoolean(jsonObject, "useStrongholds", factory.useStrongholds);
                factory.useVillages = JsonUtils.getBoolean(jsonObject, "useVillages", factory.useVillages);
                factory.useMineShafts = JsonUtils.getBoolean(jsonObject, "useMineShafts", factory.useMineShafts);
                factory.useTemples = JsonUtils.getBoolean(jsonObject, "useTemples", factory.useTemples);
                factory.useMonuments = JsonUtils.getBoolean(jsonObject, "useMonuments", factory.useMonuments);
                factory.useRavines = JsonUtils.getBoolean(jsonObject, "useRavines", factory.useRavines);
                factory.useWaterLakes = JsonUtils.getBoolean(jsonObject, "useWaterLakes", factory.useWaterLakes);
                factory.waterLakeChance = JsonUtils.getInt(jsonObject, "waterLakeChance", factory.waterLakeChance);
                factory.useLavaLakes = JsonUtils.getBoolean(jsonObject, "useLavaLakes", factory.useLavaLakes);
                factory.lavaLakeChance = JsonUtils.getInt(jsonObject, "lavaLakeChance", factory.lavaLakeChance);
                factory.useLavaOceans = JsonUtils.getBoolean(jsonObject, "useLavaOceans", factory.useLavaOceans);
                factory.fixedBiome = JsonUtils.getInt(jsonObject, "fixedBiome", factory.fixedBiome);
                if (factory.fixedBiome < 38 && factory.fixedBiome >= -1) {
                    if (factory.fixedBiome >= BiomeGenBase.hell.biomeID) {
                        factory.fixedBiome += 2;
                    }
                } else {
                    factory.fixedBiome = -1;
                }
                factory.biomeSize = JsonUtils.getInt(jsonObject, "biomeSize", factory.biomeSize);
                factory.riverSize = JsonUtils.getInt(jsonObject, "riverSize", factory.riverSize);
                factory.dirtSize = JsonUtils.getInt(jsonObject, "dirtSize", factory.dirtSize);
                factory.dirtCount = JsonUtils.getInt(jsonObject, "dirtCount", factory.dirtCount);
                factory.dirtMinHeight = JsonUtils.getInt(jsonObject, "dirtMinHeight", factory.dirtMinHeight);
                factory.dirtMaxHeight = JsonUtils.getInt(jsonObject, "dirtMaxHeight", factory.dirtMaxHeight);
                factory.gravelSize = JsonUtils.getInt(jsonObject, "gravelSize", factory.gravelSize);
                factory.gravelCount = JsonUtils.getInt(jsonObject, "gravelCount", factory.gravelCount);
                factory.gravelMinHeight = JsonUtils.getInt(jsonObject, "gravelMinHeight", factory.gravelMinHeight);
                factory.gravelMaxHeight = JsonUtils.getInt(jsonObject, "gravelMaxHeight", factory.gravelMaxHeight);
                factory.graniteSize = JsonUtils.getInt(jsonObject, "graniteSize", factory.graniteSize);
                factory.graniteCount = JsonUtils.getInt(jsonObject, "graniteCount", factory.graniteCount);
                factory.graniteMinHeight = JsonUtils.getInt(jsonObject, "graniteMinHeight", factory.graniteMinHeight);
                factory.graniteMaxHeight = JsonUtils.getInt(jsonObject, "graniteMaxHeight", factory.graniteMaxHeight);
                factory.dioriteSize = JsonUtils.getInt(jsonObject, "dioriteSize", factory.dioriteSize);
                factory.dioriteCount = JsonUtils.getInt(jsonObject, "dioriteCount", factory.dioriteCount);
                factory.dioriteMinHeight = JsonUtils.getInt(jsonObject, "dioriteMinHeight", factory.dioriteMinHeight);
                factory.dioriteMaxHeight = JsonUtils.getInt(jsonObject, "dioriteMaxHeight", factory.dioriteMaxHeight);
                factory.andesiteSize = JsonUtils.getInt(jsonObject, "andesiteSize", factory.andesiteSize);
                factory.andesiteCount = JsonUtils.getInt(jsonObject, "andesiteCount", factory.andesiteCount);
                factory.andesiteMinHeight = JsonUtils.getInt(jsonObject, "andesiteMinHeight", factory.andesiteMinHeight);
                factory.andesiteMaxHeight = JsonUtils.getInt(jsonObject, "andesiteMaxHeight", factory.andesiteMaxHeight);
                factory.coalSize = JsonUtils.getInt(jsonObject, "coalSize", factory.coalSize);
                factory.coalCount = JsonUtils.getInt(jsonObject, "coalCount", factory.coalCount);
                factory.coalMinHeight = JsonUtils.getInt(jsonObject, "coalMinHeight", factory.coalMinHeight);
                factory.coalMaxHeight = JsonUtils.getInt(jsonObject, "coalMaxHeight", factory.coalMaxHeight);
                factory.ironSize = JsonUtils.getInt(jsonObject, "ironSize", factory.ironSize);
                factory.ironCount = JsonUtils.getInt(jsonObject, "ironCount", factory.ironCount);
                factory.ironMinHeight = JsonUtils.getInt(jsonObject, "ironMinHeight", factory.ironMinHeight);
                factory.ironMaxHeight = JsonUtils.getInt(jsonObject, "ironMaxHeight", factory.ironMaxHeight);
                factory.goldSize = JsonUtils.getInt(jsonObject, "goldSize", factory.goldSize);
                factory.goldCount = JsonUtils.getInt(jsonObject, "goldCount", factory.goldCount);
                factory.goldMinHeight = JsonUtils.getInt(jsonObject, "goldMinHeight", factory.goldMinHeight);
                factory.goldMaxHeight = JsonUtils.getInt(jsonObject, "goldMaxHeight", factory.goldMaxHeight);
                factory.redstoneSize = JsonUtils.getInt(jsonObject, "redstoneSize", factory.redstoneSize);
                factory.redstoneCount = JsonUtils.getInt(jsonObject, "redstoneCount", factory.redstoneCount);
                factory.redstoneMinHeight = JsonUtils.getInt(jsonObject, "redstoneMinHeight", factory.redstoneMinHeight);
                factory.redstoneMaxHeight = JsonUtils.getInt(jsonObject, "redstoneMaxHeight", factory.redstoneMaxHeight);
                factory.diamondSize = JsonUtils.getInt(jsonObject, "diamondSize", factory.diamondSize);
                factory.diamondCount = JsonUtils.getInt(jsonObject, "diamondCount", factory.diamondCount);
                factory.diamondMinHeight = JsonUtils.getInt(jsonObject, "diamondMinHeight", factory.diamondMinHeight);
                factory.diamondMaxHeight = JsonUtils.getInt(jsonObject, "diamondMaxHeight", factory.diamondMaxHeight);
                factory.lapisSize = JsonUtils.getInt(jsonObject, "lapisSize", factory.lapisSize);
                factory.lapisCount = JsonUtils.getInt(jsonObject, "lapisCount", factory.lapisCount);
                factory.lapisCenterHeight = JsonUtils.getInt(jsonObject, "lapisCenterHeight", factory.lapisCenterHeight);
                factory.lapisSpread = JsonUtils.getInt(jsonObject, "lapisSpread", factory.lapisSpread);
            }
            catch (Exception exception) {
                // empty catch block
            }
            return factory;
        }

        public JsonElement serialize(Factory factory, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("coordinateScale", (Number)Float.valueOf(factory.coordinateScale));
            jsonObject.addProperty("heightScale", (Number)Float.valueOf(factory.heightScale));
            jsonObject.addProperty("lowerLimitScale", (Number)Float.valueOf(factory.lowerLimitScale));
            jsonObject.addProperty("upperLimitScale", (Number)Float.valueOf(factory.upperLimitScale));
            jsonObject.addProperty("depthNoiseScaleX", (Number)Float.valueOf(factory.depthNoiseScaleX));
            jsonObject.addProperty("depthNoiseScaleZ", (Number)Float.valueOf(factory.depthNoiseScaleZ));
            jsonObject.addProperty("depthNoiseScaleExponent", (Number)Float.valueOf(factory.depthNoiseScaleExponent));
            jsonObject.addProperty("mainNoiseScaleX", (Number)Float.valueOf(factory.mainNoiseScaleX));
            jsonObject.addProperty("mainNoiseScaleY", (Number)Float.valueOf(factory.mainNoiseScaleY));
            jsonObject.addProperty("mainNoiseScaleZ", (Number)Float.valueOf(factory.mainNoiseScaleZ));
            jsonObject.addProperty("baseSize", (Number)Float.valueOf(factory.baseSize));
            jsonObject.addProperty("stretchY", (Number)Float.valueOf(factory.stretchY));
            jsonObject.addProperty("biomeDepthWeight", (Number)Float.valueOf(factory.biomeDepthWeight));
            jsonObject.addProperty("biomeDepthOffset", (Number)Float.valueOf(factory.biomeDepthOffset));
            jsonObject.addProperty("biomeScaleWeight", (Number)Float.valueOf(factory.biomeScaleWeight));
            jsonObject.addProperty("biomeScaleOffset", (Number)Float.valueOf(factory.biomeScaleOffset));
            jsonObject.addProperty("seaLevel", (Number)factory.seaLevel);
            jsonObject.addProperty("useCaves", Boolean.valueOf(factory.useCaves));
            jsonObject.addProperty("useDungeons", Boolean.valueOf(factory.useDungeons));
            jsonObject.addProperty("dungeonChance", (Number)factory.dungeonChance);
            jsonObject.addProperty("useStrongholds", Boolean.valueOf(factory.useStrongholds));
            jsonObject.addProperty("useVillages", Boolean.valueOf(factory.useVillages));
            jsonObject.addProperty("useMineShafts", Boolean.valueOf(factory.useMineShafts));
            jsonObject.addProperty("useTemples", Boolean.valueOf(factory.useTemples));
            jsonObject.addProperty("useMonuments", Boolean.valueOf(factory.useMonuments));
            jsonObject.addProperty("useRavines", Boolean.valueOf(factory.useRavines));
            jsonObject.addProperty("useWaterLakes", Boolean.valueOf(factory.useWaterLakes));
            jsonObject.addProperty("waterLakeChance", (Number)factory.waterLakeChance);
            jsonObject.addProperty("useLavaLakes", Boolean.valueOf(factory.useLavaLakes));
            jsonObject.addProperty("lavaLakeChance", (Number)factory.lavaLakeChance);
            jsonObject.addProperty("useLavaOceans", Boolean.valueOf(factory.useLavaOceans));
            jsonObject.addProperty("fixedBiome", (Number)factory.fixedBiome);
            jsonObject.addProperty("biomeSize", (Number)factory.biomeSize);
            jsonObject.addProperty("riverSize", (Number)factory.riverSize);
            jsonObject.addProperty("dirtSize", (Number)factory.dirtSize);
            jsonObject.addProperty("dirtCount", (Number)factory.dirtCount);
            jsonObject.addProperty("dirtMinHeight", (Number)factory.dirtMinHeight);
            jsonObject.addProperty("dirtMaxHeight", (Number)factory.dirtMaxHeight);
            jsonObject.addProperty("gravelSize", (Number)factory.gravelSize);
            jsonObject.addProperty("gravelCount", (Number)factory.gravelCount);
            jsonObject.addProperty("gravelMinHeight", (Number)factory.gravelMinHeight);
            jsonObject.addProperty("gravelMaxHeight", (Number)factory.gravelMaxHeight);
            jsonObject.addProperty("graniteSize", (Number)factory.graniteSize);
            jsonObject.addProperty("graniteCount", (Number)factory.graniteCount);
            jsonObject.addProperty("graniteMinHeight", (Number)factory.graniteMinHeight);
            jsonObject.addProperty("graniteMaxHeight", (Number)factory.graniteMaxHeight);
            jsonObject.addProperty("dioriteSize", (Number)factory.dioriteSize);
            jsonObject.addProperty("dioriteCount", (Number)factory.dioriteCount);
            jsonObject.addProperty("dioriteMinHeight", (Number)factory.dioriteMinHeight);
            jsonObject.addProperty("dioriteMaxHeight", (Number)factory.dioriteMaxHeight);
            jsonObject.addProperty("andesiteSize", (Number)factory.andesiteSize);
            jsonObject.addProperty("andesiteCount", (Number)factory.andesiteCount);
            jsonObject.addProperty("andesiteMinHeight", (Number)factory.andesiteMinHeight);
            jsonObject.addProperty("andesiteMaxHeight", (Number)factory.andesiteMaxHeight);
            jsonObject.addProperty("coalSize", (Number)factory.coalSize);
            jsonObject.addProperty("coalCount", (Number)factory.coalCount);
            jsonObject.addProperty("coalMinHeight", (Number)factory.coalMinHeight);
            jsonObject.addProperty("coalMaxHeight", (Number)factory.coalMaxHeight);
            jsonObject.addProperty("ironSize", (Number)factory.ironSize);
            jsonObject.addProperty("ironCount", (Number)factory.ironCount);
            jsonObject.addProperty("ironMinHeight", (Number)factory.ironMinHeight);
            jsonObject.addProperty("ironMaxHeight", (Number)factory.ironMaxHeight);
            jsonObject.addProperty("goldSize", (Number)factory.goldSize);
            jsonObject.addProperty("goldCount", (Number)factory.goldCount);
            jsonObject.addProperty("goldMinHeight", (Number)factory.goldMinHeight);
            jsonObject.addProperty("goldMaxHeight", (Number)factory.goldMaxHeight);
            jsonObject.addProperty("redstoneSize", (Number)factory.redstoneSize);
            jsonObject.addProperty("redstoneCount", (Number)factory.redstoneCount);
            jsonObject.addProperty("redstoneMinHeight", (Number)factory.redstoneMinHeight);
            jsonObject.addProperty("redstoneMaxHeight", (Number)factory.redstoneMaxHeight);
            jsonObject.addProperty("diamondSize", (Number)factory.diamondSize);
            jsonObject.addProperty("diamondCount", (Number)factory.diamondCount);
            jsonObject.addProperty("diamondMinHeight", (Number)factory.diamondMinHeight);
            jsonObject.addProperty("diamondMaxHeight", (Number)factory.diamondMaxHeight);
            jsonObject.addProperty("lapisSize", (Number)factory.lapisSize);
            jsonObject.addProperty("lapisCount", (Number)factory.lapisCount);
            jsonObject.addProperty("lapisCenterHeight", (Number)factory.lapisCenterHeight);
            jsonObject.addProperty("lapisSpread", (Number)factory.lapisSpread);
            return jsonObject;
        }
    }

    public static class Factory {
        public int gravelSize = 33;
        public int biomeSize = 4;
        public int gravelCount = 8;
        public float biomeScaleOffset = 0.0f;
        public int ironSize = 9;
        public int dirtMaxHeight = 256;
        public int dungeonChance = 8;
        public boolean useLavaOceans = false;
        public boolean useVillages = true;
        public int gravelMaxHeight = 256;
        public float stretchY = 12.0f;
        public float depthNoiseScaleX = 200.0f;
        public int diamondMinHeight = 0;
        public int coalMaxHeight = 128;
        public float coordinateScale = 684.412f;
        public int ironCount = 20;
        public int dirtCount = 10;
        public float upperLimitScale = 512.0f;
        public int dioriteMinHeight = 0;
        public int goldMaxHeight = 32;
        public int graniteMinHeight = 0;
        public float mainNoiseScaleZ = 80.0f;
        public int dioriteMaxHeight = 80;
        public float mainNoiseScaleY = 160.0f;
        public int seaLevel = 63;
        public int redstoneMinHeight = 0;
        public boolean useLavaLakes = true;
        public int dioriteSize = 33;
        public int dirtMinHeight = 0;
        public float mainNoiseScaleX = 80.0f;
        public int ironMaxHeight = 64;
        public int goldCount = 2;
        public float biomeDepthWeight = 1.0f;
        public int redstoneMaxHeight = 16;
        public boolean useDungeons = true;
        public boolean useRavines = true;
        public int lavaLakeChance = 80;
        public float depthNoiseScaleZ = 200.0f;
        public int diamondCount = 1;
        public boolean useTemples = true;
        public int andesiteMaxHeight = 80;
        public float biomeDepthOffset = 0.0f;
        public int lapisCenterHeight = 16;
        public int lapisSpread = 16;
        public float biomeScaleWeight = 1.0f;
        public int goldMinHeight = 0;
        public int fixedBiome = -1;
        public int andesiteSize = 33;
        public int lapisCount = 1;
        public int waterLakeChance = 4;
        public int diamondSize = 8;
        public int coalCount = 20;
        public boolean useWaterLakes = true;
        public int goldSize = 9;
        public boolean useMonuments = true;
        public boolean useMineShafts = true;
        public int graniteSize = 33;
        public int redstoneSize = 8;
        public float depthNoiseScaleExponent = 0.5f;
        public int graniteCount = 10;
        public float lowerLimitScale = 512.0f;
        public int coalSize = 17;
        public int ironMinHeight = 0;
        public int riverSize = 4;
        public boolean useCaves = true;
        public int coalMinHeight = 0;
        public int gravelMinHeight = 0;
        public int lapisSize = 7;
        public int dirtSize = 33;
        public int andesiteMinHeight = 0;
        public float baseSize = 8.5f;
        public boolean useStrongholds = true;
        public float heightScale = 684.412f;
        public int dioriteCount = 10;
        public int diamondMaxHeight = 16;
        public int graniteMaxHeight = 80;
        static final Gson JSON_ADAPTER = new GsonBuilder().registerTypeAdapter(Factory.class, (Object)new Serializer()).create();
        public int andesiteCount = 10;
        public int redstoneCount = 8;

        public void func_177863_a() {
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

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object != null && this.getClass() == object.getClass()) {
                Factory factory = (Factory)object;
                return this.andesiteCount != factory.andesiteCount ? false : (this.andesiteMaxHeight != factory.andesiteMaxHeight ? false : (this.andesiteMinHeight != factory.andesiteMinHeight ? false : (this.andesiteSize != factory.andesiteSize ? false : (Float.compare(factory.baseSize, this.baseSize) != 0 ? false : (Float.compare(factory.biomeDepthOffset, this.biomeDepthOffset) != 0 ? false : (Float.compare(factory.biomeDepthWeight, this.biomeDepthWeight) != 0 ? false : (Float.compare(factory.biomeScaleOffset, this.biomeScaleOffset) != 0 ? false : (Float.compare(factory.biomeScaleWeight, this.biomeScaleWeight) != 0 ? false : (this.biomeSize != factory.biomeSize ? false : (this.coalCount != factory.coalCount ? false : (this.coalMaxHeight != factory.coalMaxHeight ? false : (this.coalMinHeight != factory.coalMinHeight ? false : (this.coalSize != factory.coalSize ? false : (Float.compare(factory.coordinateScale, this.coordinateScale) != 0 ? false : (Float.compare(factory.depthNoiseScaleExponent, this.depthNoiseScaleExponent) != 0 ? false : (Float.compare(factory.depthNoiseScaleX, this.depthNoiseScaleX) != 0 ? false : (Float.compare(factory.depthNoiseScaleZ, this.depthNoiseScaleZ) != 0 ? false : (this.diamondCount != factory.diamondCount ? false : (this.diamondMaxHeight != factory.diamondMaxHeight ? false : (this.diamondMinHeight != factory.diamondMinHeight ? false : (this.diamondSize != factory.diamondSize ? false : (this.dioriteCount != factory.dioriteCount ? false : (this.dioriteMaxHeight != factory.dioriteMaxHeight ? false : (this.dioriteMinHeight != factory.dioriteMinHeight ? false : (this.dioriteSize != factory.dioriteSize ? false : (this.dirtCount != factory.dirtCount ? false : (this.dirtMaxHeight != factory.dirtMaxHeight ? false : (this.dirtMinHeight != factory.dirtMinHeight ? false : (this.dirtSize != factory.dirtSize ? false : (this.dungeonChance != factory.dungeonChance ? false : (this.fixedBiome != factory.fixedBiome ? false : (this.goldCount != factory.goldCount ? false : (this.goldMaxHeight != factory.goldMaxHeight ? false : (this.goldMinHeight != factory.goldMinHeight ? false : (this.goldSize != factory.goldSize ? false : (this.graniteCount != factory.graniteCount ? false : (this.graniteMaxHeight != factory.graniteMaxHeight ? false : (this.graniteMinHeight != factory.graniteMinHeight ? false : (this.graniteSize != factory.graniteSize ? false : (this.gravelCount != factory.gravelCount ? false : (this.gravelMaxHeight != factory.gravelMaxHeight ? false : (this.gravelMinHeight != factory.gravelMinHeight ? false : (this.gravelSize != factory.gravelSize ? false : (Float.compare(factory.heightScale, this.heightScale) != 0 ? false : (this.ironCount != factory.ironCount ? false : (this.ironMaxHeight != factory.ironMaxHeight ? false : (this.ironMinHeight != factory.ironMinHeight ? false : (this.ironSize != factory.ironSize ? false : (this.lapisCenterHeight != factory.lapisCenterHeight ? false : (this.lapisCount != factory.lapisCount ? false : (this.lapisSize != factory.lapisSize ? false : (this.lapisSpread != factory.lapisSpread ? false : (this.lavaLakeChance != factory.lavaLakeChance ? false : (Float.compare(factory.lowerLimitScale, this.lowerLimitScale) != 0 ? false : (Float.compare(factory.mainNoiseScaleX, this.mainNoiseScaleX) != 0 ? false : (Float.compare(factory.mainNoiseScaleY, this.mainNoiseScaleY) != 0 ? false : (Float.compare(factory.mainNoiseScaleZ, this.mainNoiseScaleZ) != 0 ? false : (this.redstoneCount != factory.redstoneCount ? false : (this.redstoneMaxHeight != factory.redstoneMaxHeight ? false : (this.redstoneMinHeight != factory.redstoneMinHeight ? false : (this.redstoneSize != factory.redstoneSize ? false : (this.riverSize != factory.riverSize ? false : (this.seaLevel != factory.seaLevel ? false : (Float.compare(factory.stretchY, this.stretchY) != 0 ? false : (Float.compare(factory.upperLimitScale, this.upperLimitScale) != 0 ? false : (this.useCaves != factory.useCaves ? false : (this.useDungeons != factory.useDungeons ? false : (this.useLavaLakes != factory.useLavaLakes ? false : (this.useLavaOceans != factory.useLavaOceans ? false : (this.useMineShafts != factory.useMineShafts ? false : (this.useRavines != factory.useRavines ? false : (this.useStrongholds != factory.useStrongholds ? false : (this.useTemples != factory.useTemples ? false : (this.useMonuments != factory.useMonuments ? false : (this.useVillages != factory.useVillages ? false : (this.useWaterLakes != factory.useWaterLakes ? false : this.waterLakeChance == factory.waterLakeChance))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))));
            }
            return false;
        }

        public Factory() {
            this.func_177863_a();
        }

        public ChunkProviderSettings func_177864_b() {
            return new ChunkProviderSettings(this, null);
        }

        public int hashCode() {
            int n = this.coordinateScale != 0.0f ? Float.floatToIntBits(this.coordinateScale) : 0;
            n = 31 * n + (this.heightScale != 0.0f ? Float.floatToIntBits(this.heightScale) : 0);
            n = 31 * n + (this.upperLimitScale != 0.0f ? Float.floatToIntBits(this.upperLimitScale) : 0);
            n = 31 * n + (this.lowerLimitScale != 0.0f ? Float.floatToIntBits(this.lowerLimitScale) : 0);
            n = 31 * n + (this.depthNoiseScaleX != 0.0f ? Float.floatToIntBits(this.depthNoiseScaleX) : 0);
            n = 31 * n + (this.depthNoiseScaleZ != 0.0f ? Float.floatToIntBits(this.depthNoiseScaleZ) : 0);
            n = 31 * n + (this.depthNoiseScaleExponent != 0.0f ? Float.floatToIntBits(this.depthNoiseScaleExponent) : 0);
            n = 31 * n + (this.mainNoiseScaleX != 0.0f ? Float.floatToIntBits(this.mainNoiseScaleX) : 0);
            n = 31 * n + (this.mainNoiseScaleY != 0.0f ? Float.floatToIntBits(this.mainNoiseScaleY) : 0);
            n = 31 * n + (this.mainNoiseScaleZ != 0.0f ? Float.floatToIntBits(this.mainNoiseScaleZ) : 0);
            n = 31 * n + (this.baseSize != 0.0f ? Float.floatToIntBits(this.baseSize) : 0);
            n = 31 * n + (this.stretchY != 0.0f ? Float.floatToIntBits(this.stretchY) : 0);
            n = 31 * n + (this.biomeDepthWeight != 0.0f ? Float.floatToIntBits(this.biomeDepthWeight) : 0);
            n = 31 * n + (this.biomeDepthOffset != 0.0f ? Float.floatToIntBits(this.biomeDepthOffset) : 0);
            n = 31 * n + (this.biomeScaleWeight != 0.0f ? Float.floatToIntBits(this.biomeScaleWeight) : 0);
            n = 31 * n + (this.biomeScaleOffset != 0.0f ? Float.floatToIntBits(this.biomeScaleOffset) : 0);
            n = 31 * n + this.seaLevel;
            n = 31 * n + (this.useCaves ? 1 : 0);
            n = 31 * n + (this.useDungeons ? 1 : 0);
            n = 31 * n + this.dungeonChance;
            n = 31 * n + (this.useStrongholds ? 1 : 0);
            n = 31 * n + (this.useVillages ? 1 : 0);
            n = 31 * n + (this.useMineShafts ? 1 : 0);
            n = 31 * n + (this.useTemples ? 1 : 0);
            n = 31 * n + (this.useMonuments ? 1 : 0);
            n = 31 * n + (this.useRavines ? 1 : 0);
            n = 31 * n + (this.useWaterLakes ? 1 : 0);
            n = 31 * n + this.waterLakeChance;
            n = 31 * n + (this.useLavaLakes ? 1 : 0);
            n = 31 * n + this.lavaLakeChance;
            n = 31 * n + (this.useLavaOceans ? 1 : 0);
            n = 31 * n + this.fixedBiome;
            n = 31 * n + this.biomeSize;
            n = 31 * n + this.riverSize;
            n = 31 * n + this.dirtSize;
            n = 31 * n + this.dirtCount;
            n = 31 * n + this.dirtMinHeight;
            n = 31 * n + this.dirtMaxHeight;
            n = 31 * n + this.gravelSize;
            n = 31 * n + this.gravelCount;
            n = 31 * n + this.gravelMinHeight;
            n = 31 * n + this.gravelMaxHeight;
            n = 31 * n + this.graniteSize;
            n = 31 * n + this.graniteCount;
            n = 31 * n + this.graniteMinHeight;
            n = 31 * n + this.graniteMaxHeight;
            n = 31 * n + this.dioriteSize;
            n = 31 * n + this.dioriteCount;
            n = 31 * n + this.dioriteMinHeight;
            n = 31 * n + this.dioriteMaxHeight;
            n = 31 * n + this.andesiteSize;
            n = 31 * n + this.andesiteCount;
            n = 31 * n + this.andesiteMinHeight;
            n = 31 * n + this.andesiteMaxHeight;
            n = 31 * n + this.coalSize;
            n = 31 * n + this.coalCount;
            n = 31 * n + this.coalMinHeight;
            n = 31 * n + this.coalMaxHeight;
            n = 31 * n + this.ironSize;
            n = 31 * n + this.ironCount;
            n = 31 * n + this.ironMinHeight;
            n = 31 * n + this.ironMaxHeight;
            n = 31 * n + this.goldSize;
            n = 31 * n + this.goldCount;
            n = 31 * n + this.goldMinHeight;
            n = 31 * n + this.goldMaxHeight;
            n = 31 * n + this.redstoneSize;
            n = 31 * n + this.redstoneCount;
            n = 31 * n + this.redstoneMinHeight;
            n = 31 * n + this.redstoneMaxHeight;
            n = 31 * n + this.diamondSize;
            n = 31 * n + this.diamondCount;
            n = 31 * n + this.diamondMinHeight;
            n = 31 * n + this.diamondMaxHeight;
            n = 31 * n + this.lapisSize;
            n = 31 * n + this.lapisCount;
            n = 31 * n + this.lapisCenterHeight;
            n = 31 * n + this.lapisSpread;
            return n;
        }

        public String toString() {
            return JSON_ADAPTER.toJson((Object)this);
        }

        public static Factory jsonToFactory(String string) {
            if (string.length() == 0) {
                return new Factory();
            }
            try {
                return (Factory)JSON_ADAPTER.fromJson(string, Factory.class);
            }
            catch (Exception exception) {
                return new Factory();
            }
        }
    }
}

