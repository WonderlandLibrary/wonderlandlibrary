package net.minecraft.world.gen;

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
import net.minecraft.world.gen.ChunkProviderSettings;

public static class ChunkProviderSettings.Serializer
implements JsonDeserializer<ChunkProviderSettings.Factory>,
JsonSerializer<ChunkProviderSettings.Factory> {
    public ChunkProviderSettings.Factory deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        ChunkProviderSettings.Factory chunkprovidersettings$factory = new ChunkProviderSettings.Factory();
        try {
            chunkprovidersettings$factory.coordinateScale = JsonUtils.getFloat((JsonObject)jsonobject, (String)"coordinateScale", (float)chunkprovidersettings$factory.coordinateScale);
            chunkprovidersettings$factory.heightScale = JsonUtils.getFloat((JsonObject)jsonobject, (String)"heightScale", (float)chunkprovidersettings$factory.heightScale);
            chunkprovidersettings$factory.lowerLimitScale = JsonUtils.getFloat((JsonObject)jsonobject, (String)"lowerLimitScale", (float)chunkprovidersettings$factory.lowerLimitScale);
            chunkprovidersettings$factory.upperLimitScale = JsonUtils.getFloat((JsonObject)jsonobject, (String)"upperLimitScale", (float)chunkprovidersettings$factory.upperLimitScale);
            chunkprovidersettings$factory.depthNoiseScaleX = JsonUtils.getFloat((JsonObject)jsonobject, (String)"depthNoiseScaleX", (float)chunkprovidersettings$factory.depthNoiseScaleX);
            chunkprovidersettings$factory.depthNoiseScaleZ = JsonUtils.getFloat((JsonObject)jsonobject, (String)"depthNoiseScaleZ", (float)chunkprovidersettings$factory.depthNoiseScaleZ);
            chunkprovidersettings$factory.depthNoiseScaleExponent = JsonUtils.getFloat((JsonObject)jsonobject, (String)"depthNoiseScaleExponent", (float)chunkprovidersettings$factory.depthNoiseScaleExponent);
            chunkprovidersettings$factory.mainNoiseScaleX = JsonUtils.getFloat((JsonObject)jsonobject, (String)"mainNoiseScaleX", (float)chunkprovidersettings$factory.mainNoiseScaleX);
            chunkprovidersettings$factory.mainNoiseScaleY = JsonUtils.getFloat((JsonObject)jsonobject, (String)"mainNoiseScaleY", (float)chunkprovidersettings$factory.mainNoiseScaleY);
            chunkprovidersettings$factory.mainNoiseScaleZ = JsonUtils.getFloat((JsonObject)jsonobject, (String)"mainNoiseScaleZ", (float)chunkprovidersettings$factory.mainNoiseScaleZ);
            chunkprovidersettings$factory.baseSize = JsonUtils.getFloat((JsonObject)jsonobject, (String)"baseSize", (float)chunkprovidersettings$factory.baseSize);
            chunkprovidersettings$factory.stretchY = JsonUtils.getFloat((JsonObject)jsonobject, (String)"stretchY", (float)chunkprovidersettings$factory.stretchY);
            chunkprovidersettings$factory.biomeDepthWeight = JsonUtils.getFloat((JsonObject)jsonobject, (String)"biomeDepthWeight", (float)chunkprovidersettings$factory.biomeDepthWeight);
            chunkprovidersettings$factory.biomeDepthOffset = JsonUtils.getFloat((JsonObject)jsonobject, (String)"biomeDepthOffset", (float)chunkprovidersettings$factory.biomeDepthOffset);
            chunkprovidersettings$factory.biomeScaleWeight = JsonUtils.getFloat((JsonObject)jsonobject, (String)"biomeScaleWeight", (float)chunkprovidersettings$factory.biomeScaleWeight);
            chunkprovidersettings$factory.biomeScaleOffset = JsonUtils.getFloat((JsonObject)jsonobject, (String)"biomeScaleOffset", (float)chunkprovidersettings$factory.biomeScaleOffset);
            chunkprovidersettings$factory.seaLevel = JsonUtils.getInt((JsonObject)jsonobject, (String)"seaLevel", (int)chunkprovidersettings$factory.seaLevel);
            chunkprovidersettings$factory.useCaves = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"useCaves", (boolean)chunkprovidersettings$factory.useCaves);
            chunkprovidersettings$factory.useDungeons = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"useDungeons", (boolean)chunkprovidersettings$factory.useDungeons);
            chunkprovidersettings$factory.dungeonChance = JsonUtils.getInt((JsonObject)jsonobject, (String)"dungeonChance", (int)chunkprovidersettings$factory.dungeonChance);
            chunkprovidersettings$factory.useStrongholds = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"useStrongholds", (boolean)chunkprovidersettings$factory.useStrongholds);
            chunkprovidersettings$factory.useVillages = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"useVillages", (boolean)chunkprovidersettings$factory.useVillages);
            chunkprovidersettings$factory.useMineShafts = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"useMineShafts", (boolean)chunkprovidersettings$factory.useMineShafts);
            chunkprovidersettings$factory.useTemples = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"useTemples", (boolean)chunkprovidersettings$factory.useTemples);
            chunkprovidersettings$factory.useMonuments = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"useMonuments", (boolean)chunkprovidersettings$factory.useMonuments);
            chunkprovidersettings$factory.useRavines = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"useRavines", (boolean)chunkprovidersettings$factory.useRavines);
            chunkprovidersettings$factory.useWaterLakes = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"useWaterLakes", (boolean)chunkprovidersettings$factory.useWaterLakes);
            chunkprovidersettings$factory.waterLakeChance = JsonUtils.getInt((JsonObject)jsonobject, (String)"waterLakeChance", (int)chunkprovidersettings$factory.waterLakeChance);
            chunkprovidersettings$factory.useLavaLakes = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"useLavaLakes", (boolean)chunkprovidersettings$factory.useLavaLakes);
            chunkprovidersettings$factory.lavaLakeChance = JsonUtils.getInt((JsonObject)jsonobject, (String)"lavaLakeChance", (int)chunkprovidersettings$factory.lavaLakeChance);
            chunkprovidersettings$factory.useLavaOceans = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"useLavaOceans", (boolean)chunkprovidersettings$factory.useLavaOceans);
            chunkprovidersettings$factory.fixedBiome = JsonUtils.getInt((JsonObject)jsonobject, (String)"fixedBiome", (int)chunkprovidersettings$factory.fixedBiome);
            if (chunkprovidersettings$factory.fixedBiome < 38 && chunkprovidersettings$factory.fixedBiome >= -1) {
                if (chunkprovidersettings$factory.fixedBiome >= BiomeGenBase.hell.biomeID) {
                    chunkprovidersettings$factory.fixedBiome += 2;
                }
            } else {
                chunkprovidersettings$factory.fixedBiome = -1;
            }
            chunkprovidersettings$factory.biomeSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"biomeSize", (int)chunkprovidersettings$factory.biomeSize);
            chunkprovidersettings$factory.riverSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"riverSize", (int)chunkprovidersettings$factory.riverSize);
            chunkprovidersettings$factory.dirtSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"dirtSize", (int)chunkprovidersettings$factory.dirtSize);
            chunkprovidersettings$factory.dirtCount = JsonUtils.getInt((JsonObject)jsonobject, (String)"dirtCount", (int)chunkprovidersettings$factory.dirtCount);
            chunkprovidersettings$factory.dirtMinHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"dirtMinHeight", (int)chunkprovidersettings$factory.dirtMinHeight);
            chunkprovidersettings$factory.dirtMaxHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"dirtMaxHeight", (int)chunkprovidersettings$factory.dirtMaxHeight);
            chunkprovidersettings$factory.gravelSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"gravelSize", (int)chunkprovidersettings$factory.gravelSize);
            chunkprovidersettings$factory.gravelCount = JsonUtils.getInt((JsonObject)jsonobject, (String)"gravelCount", (int)chunkprovidersettings$factory.gravelCount);
            chunkprovidersettings$factory.gravelMinHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"gravelMinHeight", (int)chunkprovidersettings$factory.gravelMinHeight);
            chunkprovidersettings$factory.gravelMaxHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"gravelMaxHeight", (int)chunkprovidersettings$factory.gravelMaxHeight);
            chunkprovidersettings$factory.graniteSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"graniteSize", (int)chunkprovidersettings$factory.graniteSize);
            chunkprovidersettings$factory.graniteCount = JsonUtils.getInt((JsonObject)jsonobject, (String)"graniteCount", (int)chunkprovidersettings$factory.graniteCount);
            chunkprovidersettings$factory.graniteMinHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"graniteMinHeight", (int)chunkprovidersettings$factory.graniteMinHeight);
            chunkprovidersettings$factory.graniteMaxHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"graniteMaxHeight", (int)chunkprovidersettings$factory.graniteMaxHeight);
            chunkprovidersettings$factory.dioriteSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"dioriteSize", (int)chunkprovidersettings$factory.dioriteSize);
            chunkprovidersettings$factory.dioriteCount = JsonUtils.getInt((JsonObject)jsonobject, (String)"dioriteCount", (int)chunkprovidersettings$factory.dioriteCount);
            chunkprovidersettings$factory.dioriteMinHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"dioriteMinHeight", (int)chunkprovidersettings$factory.dioriteMinHeight);
            chunkprovidersettings$factory.dioriteMaxHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"dioriteMaxHeight", (int)chunkprovidersettings$factory.dioriteMaxHeight);
            chunkprovidersettings$factory.andesiteSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"andesiteSize", (int)chunkprovidersettings$factory.andesiteSize);
            chunkprovidersettings$factory.andesiteCount = JsonUtils.getInt((JsonObject)jsonobject, (String)"andesiteCount", (int)chunkprovidersettings$factory.andesiteCount);
            chunkprovidersettings$factory.andesiteMinHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"andesiteMinHeight", (int)chunkprovidersettings$factory.andesiteMinHeight);
            chunkprovidersettings$factory.andesiteMaxHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"andesiteMaxHeight", (int)chunkprovidersettings$factory.andesiteMaxHeight);
            chunkprovidersettings$factory.coalSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"coalSize", (int)chunkprovidersettings$factory.coalSize);
            chunkprovidersettings$factory.coalCount = JsonUtils.getInt((JsonObject)jsonobject, (String)"coalCount", (int)chunkprovidersettings$factory.coalCount);
            chunkprovidersettings$factory.coalMinHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"coalMinHeight", (int)chunkprovidersettings$factory.coalMinHeight);
            chunkprovidersettings$factory.coalMaxHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"coalMaxHeight", (int)chunkprovidersettings$factory.coalMaxHeight);
            chunkprovidersettings$factory.ironSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"ironSize", (int)chunkprovidersettings$factory.ironSize);
            chunkprovidersettings$factory.ironCount = JsonUtils.getInt((JsonObject)jsonobject, (String)"ironCount", (int)chunkprovidersettings$factory.ironCount);
            chunkprovidersettings$factory.ironMinHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"ironMinHeight", (int)chunkprovidersettings$factory.ironMinHeight);
            chunkprovidersettings$factory.ironMaxHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"ironMaxHeight", (int)chunkprovidersettings$factory.ironMaxHeight);
            chunkprovidersettings$factory.goldSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"goldSize", (int)chunkprovidersettings$factory.goldSize);
            chunkprovidersettings$factory.goldCount = JsonUtils.getInt((JsonObject)jsonobject, (String)"goldCount", (int)chunkprovidersettings$factory.goldCount);
            chunkprovidersettings$factory.goldMinHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"goldMinHeight", (int)chunkprovidersettings$factory.goldMinHeight);
            chunkprovidersettings$factory.goldMaxHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"goldMaxHeight", (int)chunkprovidersettings$factory.goldMaxHeight);
            chunkprovidersettings$factory.redstoneSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"redstoneSize", (int)chunkprovidersettings$factory.redstoneSize);
            chunkprovidersettings$factory.redstoneCount = JsonUtils.getInt((JsonObject)jsonobject, (String)"redstoneCount", (int)chunkprovidersettings$factory.redstoneCount);
            chunkprovidersettings$factory.redstoneMinHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"redstoneMinHeight", (int)chunkprovidersettings$factory.redstoneMinHeight);
            chunkprovidersettings$factory.redstoneMaxHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"redstoneMaxHeight", (int)chunkprovidersettings$factory.redstoneMaxHeight);
            chunkprovidersettings$factory.diamondSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"diamondSize", (int)chunkprovidersettings$factory.diamondSize);
            chunkprovidersettings$factory.diamondCount = JsonUtils.getInt((JsonObject)jsonobject, (String)"diamondCount", (int)chunkprovidersettings$factory.diamondCount);
            chunkprovidersettings$factory.diamondMinHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"diamondMinHeight", (int)chunkprovidersettings$factory.diamondMinHeight);
            chunkprovidersettings$factory.diamondMaxHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"diamondMaxHeight", (int)chunkprovidersettings$factory.diamondMaxHeight);
            chunkprovidersettings$factory.lapisSize = JsonUtils.getInt((JsonObject)jsonobject, (String)"lapisSize", (int)chunkprovidersettings$factory.lapisSize);
            chunkprovidersettings$factory.lapisCount = JsonUtils.getInt((JsonObject)jsonobject, (String)"lapisCount", (int)chunkprovidersettings$factory.lapisCount);
            chunkprovidersettings$factory.lapisCenterHeight = JsonUtils.getInt((JsonObject)jsonobject, (String)"lapisCenterHeight", (int)chunkprovidersettings$factory.lapisCenterHeight);
            chunkprovidersettings$factory.lapisSpread = JsonUtils.getInt((JsonObject)jsonobject, (String)"lapisSpread", (int)chunkprovidersettings$factory.lapisSpread);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return chunkprovidersettings$factory;
    }

    public JsonElement serialize(ChunkProviderSettings.Factory p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("coordinateScale", (Number)Float.valueOf((float)p_serialize_1_.coordinateScale));
        jsonobject.addProperty("heightScale", (Number)Float.valueOf((float)p_serialize_1_.heightScale));
        jsonobject.addProperty("lowerLimitScale", (Number)Float.valueOf((float)p_serialize_1_.lowerLimitScale));
        jsonobject.addProperty("upperLimitScale", (Number)Float.valueOf((float)p_serialize_1_.upperLimitScale));
        jsonobject.addProperty("depthNoiseScaleX", (Number)Float.valueOf((float)p_serialize_1_.depthNoiseScaleX));
        jsonobject.addProperty("depthNoiseScaleZ", (Number)Float.valueOf((float)p_serialize_1_.depthNoiseScaleZ));
        jsonobject.addProperty("depthNoiseScaleExponent", (Number)Float.valueOf((float)p_serialize_1_.depthNoiseScaleExponent));
        jsonobject.addProperty("mainNoiseScaleX", (Number)Float.valueOf((float)p_serialize_1_.mainNoiseScaleX));
        jsonobject.addProperty("mainNoiseScaleY", (Number)Float.valueOf((float)p_serialize_1_.mainNoiseScaleY));
        jsonobject.addProperty("mainNoiseScaleZ", (Number)Float.valueOf((float)p_serialize_1_.mainNoiseScaleZ));
        jsonobject.addProperty("baseSize", (Number)Float.valueOf((float)p_serialize_1_.baseSize));
        jsonobject.addProperty("stretchY", (Number)Float.valueOf((float)p_serialize_1_.stretchY));
        jsonobject.addProperty("biomeDepthWeight", (Number)Float.valueOf((float)p_serialize_1_.biomeDepthWeight));
        jsonobject.addProperty("biomeDepthOffset", (Number)Float.valueOf((float)p_serialize_1_.biomeDepthOffset));
        jsonobject.addProperty("biomeScaleWeight", (Number)Float.valueOf((float)p_serialize_1_.biomeScaleWeight));
        jsonobject.addProperty("biomeScaleOffset", (Number)Float.valueOf((float)p_serialize_1_.biomeScaleOffset));
        jsonobject.addProperty("seaLevel", (Number)Integer.valueOf((int)p_serialize_1_.seaLevel));
        jsonobject.addProperty("useCaves", Boolean.valueOf((boolean)p_serialize_1_.useCaves));
        jsonobject.addProperty("useDungeons", Boolean.valueOf((boolean)p_serialize_1_.useDungeons));
        jsonobject.addProperty("dungeonChance", (Number)Integer.valueOf((int)p_serialize_1_.dungeonChance));
        jsonobject.addProperty("useStrongholds", Boolean.valueOf((boolean)p_serialize_1_.useStrongholds));
        jsonobject.addProperty("useVillages", Boolean.valueOf((boolean)p_serialize_1_.useVillages));
        jsonobject.addProperty("useMineShafts", Boolean.valueOf((boolean)p_serialize_1_.useMineShafts));
        jsonobject.addProperty("useTemples", Boolean.valueOf((boolean)p_serialize_1_.useTemples));
        jsonobject.addProperty("useMonuments", Boolean.valueOf((boolean)p_serialize_1_.useMonuments));
        jsonobject.addProperty("useRavines", Boolean.valueOf((boolean)p_serialize_1_.useRavines));
        jsonobject.addProperty("useWaterLakes", Boolean.valueOf((boolean)p_serialize_1_.useWaterLakes));
        jsonobject.addProperty("waterLakeChance", (Number)Integer.valueOf((int)p_serialize_1_.waterLakeChance));
        jsonobject.addProperty("useLavaLakes", Boolean.valueOf((boolean)p_serialize_1_.useLavaLakes));
        jsonobject.addProperty("lavaLakeChance", (Number)Integer.valueOf((int)p_serialize_1_.lavaLakeChance));
        jsonobject.addProperty("useLavaOceans", Boolean.valueOf((boolean)p_serialize_1_.useLavaOceans));
        jsonobject.addProperty("fixedBiome", (Number)Integer.valueOf((int)p_serialize_1_.fixedBiome));
        jsonobject.addProperty("biomeSize", (Number)Integer.valueOf((int)p_serialize_1_.biomeSize));
        jsonobject.addProperty("riverSize", (Number)Integer.valueOf((int)p_serialize_1_.riverSize));
        jsonobject.addProperty("dirtSize", (Number)Integer.valueOf((int)p_serialize_1_.dirtSize));
        jsonobject.addProperty("dirtCount", (Number)Integer.valueOf((int)p_serialize_1_.dirtCount));
        jsonobject.addProperty("dirtMinHeight", (Number)Integer.valueOf((int)p_serialize_1_.dirtMinHeight));
        jsonobject.addProperty("dirtMaxHeight", (Number)Integer.valueOf((int)p_serialize_1_.dirtMaxHeight));
        jsonobject.addProperty("gravelSize", (Number)Integer.valueOf((int)p_serialize_1_.gravelSize));
        jsonobject.addProperty("gravelCount", (Number)Integer.valueOf((int)p_serialize_1_.gravelCount));
        jsonobject.addProperty("gravelMinHeight", (Number)Integer.valueOf((int)p_serialize_1_.gravelMinHeight));
        jsonobject.addProperty("gravelMaxHeight", (Number)Integer.valueOf((int)p_serialize_1_.gravelMaxHeight));
        jsonobject.addProperty("graniteSize", (Number)Integer.valueOf((int)p_serialize_1_.graniteSize));
        jsonobject.addProperty("graniteCount", (Number)Integer.valueOf((int)p_serialize_1_.graniteCount));
        jsonobject.addProperty("graniteMinHeight", (Number)Integer.valueOf((int)p_serialize_1_.graniteMinHeight));
        jsonobject.addProperty("graniteMaxHeight", (Number)Integer.valueOf((int)p_serialize_1_.graniteMaxHeight));
        jsonobject.addProperty("dioriteSize", (Number)Integer.valueOf((int)p_serialize_1_.dioriteSize));
        jsonobject.addProperty("dioriteCount", (Number)Integer.valueOf((int)p_serialize_1_.dioriteCount));
        jsonobject.addProperty("dioriteMinHeight", (Number)Integer.valueOf((int)p_serialize_1_.dioriteMinHeight));
        jsonobject.addProperty("dioriteMaxHeight", (Number)Integer.valueOf((int)p_serialize_1_.dioriteMaxHeight));
        jsonobject.addProperty("andesiteSize", (Number)Integer.valueOf((int)p_serialize_1_.andesiteSize));
        jsonobject.addProperty("andesiteCount", (Number)Integer.valueOf((int)p_serialize_1_.andesiteCount));
        jsonobject.addProperty("andesiteMinHeight", (Number)Integer.valueOf((int)p_serialize_1_.andesiteMinHeight));
        jsonobject.addProperty("andesiteMaxHeight", (Number)Integer.valueOf((int)p_serialize_1_.andesiteMaxHeight));
        jsonobject.addProperty("coalSize", (Number)Integer.valueOf((int)p_serialize_1_.coalSize));
        jsonobject.addProperty("coalCount", (Number)Integer.valueOf((int)p_serialize_1_.coalCount));
        jsonobject.addProperty("coalMinHeight", (Number)Integer.valueOf((int)p_serialize_1_.coalMinHeight));
        jsonobject.addProperty("coalMaxHeight", (Number)Integer.valueOf((int)p_serialize_1_.coalMaxHeight));
        jsonobject.addProperty("ironSize", (Number)Integer.valueOf((int)p_serialize_1_.ironSize));
        jsonobject.addProperty("ironCount", (Number)Integer.valueOf((int)p_serialize_1_.ironCount));
        jsonobject.addProperty("ironMinHeight", (Number)Integer.valueOf((int)p_serialize_1_.ironMinHeight));
        jsonobject.addProperty("ironMaxHeight", (Number)Integer.valueOf((int)p_serialize_1_.ironMaxHeight));
        jsonobject.addProperty("goldSize", (Number)Integer.valueOf((int)p_serialize_1_.goldSize));
        jsonobject.addProperty("goldCount", (Number)Integer.valueOf((int)p_serialize_1_.goldCount));
        jsonobject.addProperty("goldMinHeight", (Number)Integer.valueOf((int)p_serialize_1_.goldMinHeight));
        jsonobject.addProperty("goldMaxHeight", (Number)Integer.valueOf((int)p_serialize_1_.goldMaxHeight));
        jsonobject.addProperty("redstoneSize", (Number)Integer.valueOf((int)p_serialize_1_.redstoneSize));
        jsonobject.addProperty("redstoneCount", (Number)Integer.valueOf((int)p_serialize_1_.redstoneCount));
        jsonobject.addProperty("redstoneMinHeight", (Number)Integer.valueOf((int)p_serialize_1_.redstoneMinHeight));
        jsonobject.addProperty("redstoneMaxHeight", (Number)Integer.valueOf((int)p_serialize_1_.redstoneMaxHeight));
        jsonobject.addProperty("diamondSize", (Number)Integer.valueOf((int)p_serialize_1_.diamondSize));
        jsonobject.addProperty("diamondCount", (Number)Integer.valueOf((int)p_serialize_1_.diamondCount));
        jsonobject.addProperty("diamondMinHeight", (Number)Integer.valueOf((int)p_serialize_1_.diamondMinHeight));
        jsonobject.addProperty("diamondMaxHeight", (Number)Integer.valueOf((int)p_serialize_1_.diamondMaxHeight));
        jsonobject.addProperty("lapisSize", (Number)Integer.valueOf((int)p_serialize_1_.lapisSize));
        jsonobject.addProperty("lapisCount", (Number)Integer.valueOf((int)p_serialize_1_.lapisCount));
        jsonobject.addProperty("lapisCenterHeight", (Number)Integer.valueOf((int)p_serialize_1_.lapisCenterHeight));
        jsonobject.addProperty("lapisSpread", (Number)Integer.valueOf((int)p_serialize_1_.lapisSpread));
        return jsonobject;
    }
}
