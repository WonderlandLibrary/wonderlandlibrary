package net.minecraft.world.gen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.gen.ChunkProviderSettings;

public static class ChunkProviderSettings.Factory {
    static final Gson JSON_ADAPTER = new GsonBuilder().registerTypeAdapter(ChunkProviderSettings.Factory.class, (Object)new ChunkProviderSettings.Serializer()).create();
    public float coordinateScale = 684.412f;
    public float heightScale = 684.412f;
    public float upperLimitScale = 512.0f;
    public float lowerLimitScale = 512.0f;
    public float depthNoiseScaleX = 200.0f;
    public float depthNoiseScaleZ = 200.0f;
    public float depthNoiseScaleExponent = 0.5f;
    public float mainNoiseScaleX = 80.0f;
    public float mainNoiseScaleY = 160.0f;
    public float mainNoiseScaleZ = 80.0f;
    public float baseSize = 8.5f;
    public float stretchY = 12.0f;
    public float biomeDepthWeight = 1.0f;
    public float biomeDepthOffset = 0.0f;
    public float biomeScaleWeight = 1.0f;
    public float biomeScaleOffset = 0.0f;
    public int seaLevel = 63;
    public boolean useCaves = true;
    public boolean useDungeons = true;
    public int dungeonChance = 8;
    public boolean useStrongholds = true;
    public boolean useVillages = true;
    public boolean useMineShafts = true;
    public boolean useTemples = true;
    public boolean useMonuments = true;
    public boolean useRavines = true;
    public boolean useWaterLakes = true;
    public int waterLakeChance = 4;
    public boolean useLavaLakes = true;
    public int lavaLakeChance = 80;
    public boolean useLavaOceans = false;
    public int fixedBiome = -1;
    public int biomeSize = 4;
    public int riverSize = 4;
    public int dirtSize = 33;
    public int dirtCount = 10;
    public int dirtMinHeight = 0;
    public int dirtMaxHeight = 256;
    public int gravelSize = 33;
    public int gravelCount = 8;
    public int gravelMinHeight = 0;
    public int gravelMaxHeight = 256;
    public int graniteSize = 33;
    public int graniteCount = 10;
    public int graniteMinHeight = 0;
    public int graniteMaxHeight = 80;
    public int dioriteSize = 33;
    public int dioriteCount = 10;
    public int dioriteMinHeight = 0;
    public int dioriteMaxHeight = 80;
    public int andesiteSize = 33;
    public int andesiteCount = 10;
    public int andesiteMinHeight = 0;
    public int andesiteMaxHeight = 80;
    public int coalSize = 17;
    public int coalCount = 20;
    public int coalMinHeight = 0;
    public int coalMaxHeight = 128;
    public int ironSize = 9;
    public int ironCount = 20;
    public int ironMinHeight = 0;
    public int ironMaxHeight = 64;
    public int goldSize = 9;
    public int goldCount = 2;
    public int goldMinHeight = 0;
    public int goldMaxHeight = 32;
    public int redstoneSize = 8;
    public int redstoneCount = 8;
    public int redstoneMinHeight = 0;
    public int redstoneMaxHeight = 16;
    public int diamondSize = 8;
    public int diamondCount = 1;
    public int diamondMinHeight = 0;
    public int diamondMaxHeight = 16;
    public int lapisSize = 7;
    public int lapisCount = 1;
    public int lapisCenterHeight = 16;
    public int lapisSpread = 16;

    public static ChunkProviderSettings.Factory jsonToFactory(String p_177865_0_) {
        if (p_177865_0_.length() == 0) {
            return new ChunkProviderSettings.Factory();
        }
        try {
            return (ChunkProviderSettings.Factory)JSON_ADAPTER.fromJson(p_177865_0_, ChunkProviderSettings.Factory.class);
        }
        catch (Exception var2) {
            return new ChunkProviderSettings.Factory();
        }
    }

    public String toString() {
        return JSON_ADAPTER.toJson((Object)this);
    }

    public ChunkProviderSettings.Factory() {
        this.func_177863_a();
    }

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

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            ChunkProviderSettings.Factory chunkprovidersettings$factory = (ChunkProviderSettings.Factory)p_equals_1_;
            return this.andesiteCount != chunkprovidersettings$factory.andesiteCount ? false : (this.andesiteMaxHeight != chunkprovidersettings$factory.andesiteMaxHeight ? false : (this.andesiteMinHeight != chunkprovidersettings$factory.andesiteMinHeight ? false : (this.andesiteSize != chunkprovidersettings$factory.andesiteSize ? false : (Float.compare((float)chunkprovidersettings$factory.baseSize, (float)this.baseSize) != 0 ? false : (Float.compare((float)chunkprovidersettings$factory.biomeDepthOffset, (float)this.biomeDepthOffset) != 0 ? false : (Float.compare((float)chunkprovidersettings$factory.biomeDepthWeight, (float)this.biomeDepthWeight) != 0 ? false : (Float.compare((float)chunkprovidersettings$factory.biomeScaleOffset, (float)this.biomeScaleOffset) != 0 ? false : (Float.compare((float)chunkprovidersettings$factory.biomeScaleWeight, (float)this.biomeScaleWeight) != 0 ? false : (this.biomeSize != chunkprovidersettings$factory.biomeSize ? false : (this.coalCount != chunkprovidersettings$factory.coalCount ? false : (this.coalMaxHeight != chunkprovidersettings$factory.coalMaxHeight ? false : (this.coalMinHeight != chunkprovidersettings$factory.coalMinHeight ? false : (this.coalSize != chunkprovidersettings$factory.coalSize ? false : (Float.compare((float)chunkprovidersettings$factory.coordinateScale, (float)this.coordinateScale) != 0 ? false : (Float.compare((float)chunkprovidersettings$factory.depthNoiseScaleExponent, (float)this.depthNoiseScaleExponent) != 0 ? false : (Float.compare((float)chunkprovidersettings$factory.depthNoiseScaleX, (float)this.depthNoiseScaleX) != 0 ? false : (Float.compare((float)chunkprovidersettings$factory.depthNoiseScaleZ, (float)this.depthNoiseScaleZ) != 0 ? false : (this.diamondCount != chunkprovidersettings$factory.diamondCount ? false : (this.diamondMaxHeight != chunkprovidersettings$factory.diamondMaxHeight ? false : (this.diamondMinHeight != chunkprovidersettings$factory.diamondMinHeight ? false : (this.diamondSize != chunkprovidersettings$factory.diamondSize ? false : (this.dioriteCount != chunkprovidersettings$factory.dioriteCount ? false : (this.dioriteMaxHeight != chunkprovidersettings$factory.dioriteMaxHeight ? false : (this.dioriteMinHeight != chunkprovidersettings$factory.dioriteMinHeight ? false : (this.dioriteSize != chunkprovidersettings$factory.dioriteSize ? false : (this.dirtCount != chunkprovidersettings$factory.dirtCount ? false : (this.dirtMaxHeight != chunkprovidersettings$factory.dirtMaxHeight ? false : (this.dirtMinHeight != chunkprovidersettings$factory.dirtMinHeight ? false : (this.dirtSize != chunkprovidersettings$factory.dirtSize ? false : (this.dungeonChance != chunkprovidersettings$factory.dungeonChance ? false : (this.fixedBiome != chunkprovidersettings$factory.fixedBiome ? false : (this.goldCount != chunkprovidersettings$factory.goldCount ? false : (this.goldMaxHeight != chunkprovidersettings$factory.goldMaxHeight ? false : (this.goldMinHeight != chunkprovidersettings$factory.goldMinHeight ? false : (this.goldSize != chunkprovidersettings$factory.goldSize ? false : (this.graniteCount != chunkprovidersettings$factory.graniteCount ? false : (this.graniteMaxHeight != chunkprovidersettings$factory.graniteMaxHeight ? false : (this.graniteMinHeight != chunkprovidersettings$factory.graniteMinHeight ? false : (this.graniteSize != chunkprovidersettings$factory.graniteSize ? false : (this.gravelCount != chunkprovidersettings$factory.gravelCount ? false : (this.gravelMaxHeight != chunkprovidersettings$factory.gravelMaxHeight ? false : (this.gravelMinHeight != chunkprovidersettings$factory.gravelMinHeight ? false : (this.gravelSize != chunkprovidersettings$factory.gravelSize ? false : (Float.compare((float)chunkprovidersettings$factory.heightScale, (float)this.heightScale) != 0 ? false : (this.ironCount != chunkprovidersettings$factory.ironCount ? false : (this.ironMaxHeight != chunkprovidersettings$factory.ironMaxHeight ? false : (this.ironMinHeight != chunkprovidersettings$factory.ironMinHeight ? false : (this.ironSize != chunkprovidersettings$factory.ironSize ? false : (this.lapisCenterHeight != chunkprovidersettings$factory.lapisCenterHeight ? false : (this.lapisCount != chunkprovidersettings$factory.lapisCount ? false : (this.lapisSize != chunkprovidersettings$factory.lapisSize ? false : (this.lapisSpread != chunkprovidersettings$factory.lapisSpread ? false : (this.lavaLakeChance != chunkprovidersettings$factory.lavaLakeChance ? false : (Float.compare((float)chunkprovidersettings$factory.lowerLimitScale, (float)this.lowerLimitScale) != 0 ? false : (Float.compare((float)chunkprovidersettings$factory.mainNoiseScaleX, (float)this.mainNoiseScaleX) != 0 ? false : (Float.compare((float)chunkprovidersettings$factory.mainNoiseScaleY, (float)this.mainNoiseScaleY) != 0 ? false : (Float.compare((float)chunkprovidersettings$factory.mainNoiseScaleZ, (float)this.mainNoiseScaleZ) != 0 ? false : (this.redstoneCount != chunkprovidersettings$factory.redstoneCount ? false : (this.redstoneMaxHeight != chunkprovidersettings$factory.redstoneMaxHeight ? false : (this.redstoneMinHeight != chunkprovidersettings$factory.redstoneMinHeight ? false : (this.redstoneSize != chunkprovidersettings$factory.redstoneSize ? false : (this.riverSize != chunkprovidersettings$factory.riverSize ? false : (this.seaLevel != chunkprovidersettings$factory.seaLevel ? false : (Float.compare((float)chunkprovidersettings$factory.stretchY, (float)this.stretchY) != 0 ? false : (Float.compare((float)chunkprovidersettings$factory.upperLimitScale, (float)this.upperLimitScale) != 0 ? false : (this.useCaves != chunkprovidersettings$factory.useCaves ? false : (this.useDungeons != chunkprovidersettings$factory.useDungeons ? false : (this.useLavaLakes != chunkprovidersettings$factory.useLavaLakes ? false : (this.useLavaOceans != chunkprovidersettings$factory.useLavaOceans ? false : (this.useMineShafts != chunkprovidersettings$factory.useMineShafts ? false : (this.useRavines != chunkprovidersettings$factory.useRavines ? false : (this.useStrongholds != chunkprovidersettings$factory.useStrongholds ? false : (this.useTemples != chunkprovidersettings$factory.useTemples ? false : (this.useMonuments != chunkprovidersettings$factory.useMonuments ? false : (this.useVillages != chunkprovidersettings$factory.useVillages ? false : (this.useWaterLakes != chunkprovidersettings$factory.useWaterLakes ? false : this.waterLakeChance == chunkprovidersettings$factory.waterLakeChance))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))));
        }
        return false;
    }

    public int hashCode() {
        int i = this.coordinateScale != 0.0f ? Float.floatToIntBits((float)this.coordinateScale) : 0;
        i = 31 * i + (this.heightScale != 0.0f ? Float.floatToIntBits((float)this.heightScale) : 0);
        i = 31 * i + (this.upperLimitScale != 0.0f ? Float.floatToIntBits((float)this.upperLimitScale) : 0);
        i = 31 * i + (this.lowerLimitScale != 0.0f ? Float.floatToIntBits((float)this.lowerLimitScale) : 0);
        i = 31 * i + (this.depthNoiseScaleX != 0.0f ? Float.floatToIntBits((float)this.depthNoiseScaleX) : 0);
        i = 31 * i + (this.depthNoiseScaleZ != 0.0f ? Float.floatToIntBits((float)this.depthNoiseScaleZ) : 0);
        i = 31 * i + (this.depthNoiseScaleExponent != 0.0f ? Float.floatToIntBits((float)this.depthNoiseScaleExponent) : 0);
        i = 31 * i + (this.mainNoiseScaleX != 0.0f ? Float.floatToIntBits((float)this.mainNoiseScaleX) : 0);
        i = 31 * i + (this.mainNoiseScaleY != 0.0f ? Float.floatToIntBits((float)this.mainNoiseScaleY) : 0);
        i = 31 * i + (this.mainNoiseScaleZ != 0.0f ? Float.floatToIntBits((float)this.mainNoiseScaleZ) : 0);
        i = 31 * i + (this.baseSize != 0.0f ? Float.floatToIntBits((float)this.baseSize) : 0);
        i = 31 * i + (this.stretchY != 0.0f ? Float.floatToIntBits((float)this.stretchY) : 0);
        i = 31 * i + (this.biomeDepthWeight != 0.0f ? Float.floatToIntBits((float)this.biomeDepthWeight) : 0);
        i = 31 * i + (this.biomeDepthOffset != 0.0f ? Float.floatToIntBits((float)this.biomeDepthOffset) : 0);
        i = 31 * i + (this.biomeScaleWeight != 0.0f ? Float.floatToIntBits((float)this.biomeScaleWeight) : 0);
        i = 31 * i + (this.biomeScaleOffset != 0.0f ? Float.floatToIntBits((float)this.biomeScaleOffset) : 0);
        i = 31 * i + this.seaLevel;
        i = 31 * i + (this.useCaves ? 1 : 0);
        i = 31 * i + (this.useDungeons ? 1 : 0);
        i = 31 * i + this.dungeonChance;
        i = 31 * i + (this.useStrongholds ? 1 : 0);
        i = 31 * i + (this.useVillages ? 1 : 0);
        i = 31 * i + (this.useMineShafts ? 1 : 0);
        i = 31 * i + (this.useTemples ? 1 : 0);
        i = 31 * i + (this.useMonuments ? 1 : 0);
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

    public ChunkProviderSettings func_177864_b() {
        return new ChunkProviderSettings(this, null);
    }
}
