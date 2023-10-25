/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBeach;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraft.world.biome.BiomeGenEnd;
import net.minecraft.world.biome.BiomeGenForest;
import net.minecraft.world.biome.BiomeGenHell;
import net.minecraft.world.biome.BiomeGenHills;
import net.minecraft.world.biome.BiomeGenJungle;
import net.minecraft.world.biome.BiomeGenMesa;
import net.minecraft.world.biome.BiomeGenMushroomIsland;
import net.minecraft.world.biome.BiomeGenMutated;
import net.minecraft.world.biome.BiomeGenOcean;
import net.minecraft.world.biome.BiomeGenPlains;
import net.minecraft.world.biome.BiomeGenRiver;
import net.minecraft.world.biome.BiomeGenSavanna;
import net.minecraft.world.biome.BiomeGenSnow;
import net.minecraft.world.biome.BiomeGenStoneBeach;
import net.minecraft.world.biome.BiomeGenSwamp;
import net.minecraft.world.biome.BiomeGenTaiga;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BiomeGenBase {
    protected static final Height height_LowPlains;
    protected static final NoiseGeneratorPerlin temperatureNoise;
    public static final BiomeGenBase mesa;
    protected static final Height height_RockyWaters;
    public static final BiomeGenBase birchForest;
    protected List<SpawnListEntry> spawnableCreatureList;
    public static final BiomeGenBase desert;
    public static final BiomeGenBase savannaPlateau;
    public static final BiomeGenBase extremeHillsPlus;
    public float minHeight;
    protected WorldGenSwamp worldGeneratorSwamp;
    protected WorldGenBigTree worldGeneratorBigTree;
    public static final BiomeGenBase field_180279_ad;
    public static final BiomeGenBase deepOcean;
    public static final BiomeGenBase taiga;
    public IBlockState topBlock = Blocks.grass.getDefaultState();
    public static final BiomeGenBase megaTaiga;
    public IBlockState fillerBlock = Blocks.dirt.getDefaultState();
    protected static final Height height_PartiallySubmerged;
    public static final BiomeGenBase frozenOcean;
    public float maxHeight;
    public static final BiomeGenBase megaTaigaHills;
    protected boolean enableRain;
    protected static final Height height_ShallowWaters;
    public static final BiomeGenBase jungleHills;
    public static final BiomeGenBase river;
    public static final BiomeGenBase forestHills;
    private static final Logger logger;
    protected static final Height height_LowHills;
    private static final BiomeGenBase[] biomeList;
    protected static final Height height_Oceans;
    public static final BiomeGenBase savanna;
    public static final BiomeGenBase desertHills;
    public static final BiomeGenBase birchForestHills;
    protected static final Height height_Shores;
    public static final BiomeGenBase mushroomIsland;
    public static final BiomeGenBase forest;
    public static final BiomeGenBase ocean;
    public static final BiomeGenBase roofedForest;
    public static final BiomeGenBase icePlains;
    public static final BiomeGenBase sky;
    protected List<SpawnListEntry> spawnableWaterCreatureList;
    public static final BiomeGenBase plains;
    protected boolean enableSnow;
    public static final BiomeGenBase coldTaiga;
    public static final BiomeGenBase swampland;
    protected List<SpawnListEntry> spawnableMonsterList;
    public static final BiomeGenBase beach;
    public static final BiomeGenBase jungle;
    protected List<SpawnListEntry> spawnableCaveCreatureList;
    public final int biomeID;
    public static final BiomeGenBase extremeHillsEdge;
    public static final BiomeGenBase mesaPlateau;
    public static final Map<String, BiomeGenBase> BIOME_ID_MAP;
    public static final BiomeGenBase taigaHills;
    public int waterColorMultiplier;
    public int fillerBlockMetadata = 5169201;
    public static final BiomeGenBase hell;
    protected static final Height height_MidPlains;
    public static final Set<BiomeGenBase> explorationBiomesList;
    public static final BiomeGenBase jungleEdge;
    public static final BiomeGenBase mesaPlateau_F;
    public String biomeName;
    public float rainfall;
    protected static final Height height_LowIslands;
    public static final BiomeGenBase mushroomIslandShore;
    protected WorldGenTrees worldGeneratorTrees;
    protected static final Height height_HighPlateaus;
    public int field_150609_ah;
    protected static final NoiseGeneratorPerlin GRASS_COLOR_NOISE;
    public static final BiomeGenBase coldTaigaHills;
    protected static final WorldGenDoublePlant DOUBLE_PLANT_GENERATOR;
    public float temperature;
    public static final BiomeGenBase iceMountains;
    public static final BiomeGenBase coldBeach;
    protected static final Height height_MidHills;
    public BiomeDecorator theBiomeDecorator;
    protected static final Height height_Default;
    public static final BiomeGenBase stoneBeach;
    protected static final Height height_DeepOceans;
    public int color;
    public static final BiomeGenBase extremeHills;
    public static final BiomeGenBase frozenRiver;

    static {
        logger = LogManager.getLogger();
        height_Default = new Height(0.1f, 0.2f);
        height_ShallowWaters = new Height(-0.5f, 0.0f);
        height_Oceans = new Height(-1.0f, 0.1f);
        height_DeepOceans = new Height(-1.8f, 0.1f);
        height_LowPlains = new Height(0.125f, 0.05f);
        height_MidPlains = new Height(0.2f, 0.2f);
        height_LowHills = new Height(0.45f, 0.3f);
        height_HighPlateaus = new Height(1.5f, 0.025f);
        height_MidHills = new Height(1.0f, 0.5f);
        height_Shores = new Height(0.0f, 0.025f);
        height_RockyWaters = new Height(0.1f, 0.8f);
        height_LowIslands = new Height(0.2f, 0.3f);
        height_PartiallySubmerged = new Height(-0.2f, 0.1f);
        biomeList = new BiomeGenBase[256];
        explorationBiomesList = Sets.newHashSet();
        BIOME_ID_MAP = Maps.newHashMap();
        ocean = new BiomeGenOcean(0).setColor(112).setBiomeName("Ocean").setHeight(height_Oceans);
        plains = new BiomeGenPlains(1).setColor(9286496).setBiomeName("Plains");
        desert = new BiomeGenDesert(2).setColor(16421912).setBiomeName("Desert").setDisableRain().setTemperatureRainfall(2.0f, 0.0f).setHeight(height_LowPlains);
        extremeHills = new BiomeGenHills(3, false).setColor(0x606060).setBiomeName("Extreme Hills").setHeight(height_MidHills).setTemperatureRainfall(0.2f, 0.3f);
        forest = new BiomeGenForest(4, 0).setColor(353825).setBiomeName("Forest");
        taiga = new BiomeGenTaiga(5, 0).setColor(747097).setBiomeName("Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25f, 0.8f).setHeight(height_MidPlains);
        swampland = new BiomeGenSwamp(6).setColor(522674).setBiomeName("Swampland").setFillerBlockMetadata(9154376).setHeight(height_PartiallySubmerged).setTemperatureRainfall(0.8f, 0.9f);
        river = new BiomeGenRiver(7).setColor(255).setBiomeName("River").setHeight(height_ShallowWaters);
        hell = new BiomeGenHell(8).setColor(0xFF0000).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0f, 0.0f);
        sky = new BiomeGenEnd(9).setColor(0x8080FF).setBiomeName("The End").setDisableRain();
        frozenOcean = new BiomeGenOcean(10).setColor(0x9090A0).setBiomeName("FrozenOcean").setEnableSnow().setHeight(height_Oceans).setTemperatureRainfall(0.0f, 0.5f);
        frozenRiver = new BiomeGenRiver(11).setColor(0xA0A0FF).setBiomeName("FrozenRiver").setEnableSnow().setHeight(height_ShallowWaters).setTemperatureRainfall(0.0f, 0.5f);
        icePlains = new BiomeGenSnow(12, false).setColor(0xFFFFFF).setBiomeName("Ice Plains").setEnableSnow().setTemperatureRainfall(0.0f, 0.5f).setHeight(height_LowPlains);
        iceMountains = new BiomeGenSnow(13, false).setColor(0xA0A0A0).setBiomeName("Ice Mountains").setEnableSnow().setHeight(height_LowHills).setTemperatureRainfall(0.0f, 0.5f);
        mushroomIsland = new BiomeGenMushroomIsland(14).setColor(0xFF00FF).setBiomeName("MushroomIsland").setTemperatureRainfall(0.9f, 1.0f).setHeight(height_LowIslands);
        mushroomIslandShore = new BiomeGenMushroomIsland(15).setColor(0xA000FF).setBiomeName("MushroomIslandShore").setTemperatureRainfall(0.9f, 1.0f).setHeight(height_Shores);
        beach = new BiomeGenBeach(16).setColor(16440917).setBiomeName("Beach").setTemperatureRainfall(0.8f, 0.4f).setHeight(height_Shores);
        desertHills = new BiomeGenDesert(17).setColor(13786898).setBiomeName("DesertHills").setDisableRain().setTemperatureRainfall(2.0f, 0.0f).setHeight(height_LowHills);
        forestHills = new BiomeGenForest(18, 0).setColor(2250012).setBiomeName("ForestHills").setHeight(height_LowHills);
        taigaHills = new BiomeGenTaiga(19, 0).setColor(1456435).setBiomeName("TaigaHills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25f, 0.8f).setHeight(height_LowHills);
        extremeHillsEdge = new BiomeGenHills(20, true).setColor(7501978).setBiomeName("Extreme Hills Edge").setHeight(height_MidHills.attenuate()).setTemperatureRainfall(0.2f, 0.3f);
        jungle = new BiomeGenJungle(21, false).setColor(5470985).setBiomeName("Jungle").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95f, 0.9f);
        jungleHills = new BiomeGenJungle(22, false).setColor(2900485).setBiomeName("JungleHills").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95f, 0.9f).setHeight(height_LowHills);
        jungleEdge = new BiomeGenJungle(23, true).setColor(6458135).setBiomeName("JungleEdge").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95f, 0.8f);
        deepOcean = new BiomeGenOcean(24).setColor(48).setBiomeName("Deep Ocean").setHeight(height_DeepOceans);
        stoneBeach = new BiomeGenStoneBeach(25).setColor(10658436).setBiomeName("Stone Beach").setTemperatureRainfall(0.2f, 0.3f).setHeight(height_RockyWaters);
        coldBeach = new BiomeGenBeach(26).setColor(16445632).setBiomeName("Cold Beach").setTemperatureRainfall(0.05f, 0.3f).setHeight(height_Shores).setEnableSnow();
        birchForest = new BiomeGenForest(27, 2).setBiomeName("Birch Forest").setColor(3175492);
        birchForestHills = new BiomeGenForest(28, 2).setBiomeName("Birch Forest Hills").setColor(2055986).setHeight(height_LowHills);
        roofedForest = new BiomeGenForest(29, 3).setColor(4215066).setBiomeName("Roofed Forest");
        coldTaiga = new BiomeGenTaiga(30, 0).setColor(3233098).setBiomeName("Cold Taiga").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5f, 0.4f).setHeight(height_MidPlains).func_150563_c(0xFFFFFF);
        coldTaigaHills = new BiomeGenTaiga(31, 0).setColor(2375478).setBiomeName("Cold Taiga Hills").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5f, 0.4f).setHeight(height_LowHills).func_150563_c(0xFFFFFF);
        megaTaiga = new BiomeGenTaiga(32, 1).setColor(5858897).setBiomeName("Mega Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3f, 0.8f).setHeight(height_MidPlains);
        megaTaigaHills = new BiomeGenTaiga(33, 1).setColor(4542270).setBiomeName("Mega Taiga Hills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3f, 0.8f).setHeight(height_LowHills);
        extremeHillsPlus = new BiomeGenHills(34, true).setColor(0x507050).setBiomeName("Extreme Hills+").setHeight(height_MidHills).setTemperatureRainfall(0.2f, 0.3f);
        savanna = new BiomeGenSavanna(35).setColor(12431967).setBiomeName("Savanna").setTemperatureRainfall(1.2f, 0.0f).setDisableRain().setHeight(height_LowPlains);
        savannaPlateau = new BiomeGenSavanna(36).setColor(10984804).setBiomeName("Savanna Plateau").setTemperatureRainfall(1.0f, 0.0f).setDisableRain().setHeight(height_HighPlateaus);
        mesa = new BiomeGenMesa(37, false, false).setColor(14238997).setBiomeName("Mesa");
        mesaPlateau_F = new BiomeGenMesa(38, false, true).setColor(11573093).setBiomeName("Mesa Plateau F").setHeight(height_HighPlateaus);
        mesaPlateau = new BiomeGenMesa(39, false, false).setColor(13274213).setBiomeName("Mesa Plateau").setHeight(height_HighPlateaus);
        field_180279_ad = ocean;
        plains.createMutation();
        desert.createMutation();
        forest.createMutation();
        taiga.createMutation();
        swampland.createMutation();
        icePlains.createMutation();
        jungle.createMutation();
        jungleEdge.createMutation();
        coldTaiga.createMutation();
        savanna.createMutation();
        savannaPlateau.createMutation();
        mesa.createMutation();
        mesaPlateau_F.createMutation();
        mesaPlateau.createMutation();
        birchForest.createMutation();
        birchForestHills.createMutation();
        roofedForest.createMutation();
        megaTaiga.createMutation();
        extremeHills.createMutation();
        extremeHillsPlus.createMutation();
        megaTaiga.createMutatedBiome(BiomeGenBase.megaTaigaHills.biomeID + 128).setBiomeName("Redwood Taiga Hills M");
        BiomeGenBase[] biomeGenBaseArray = biomeList;
        int n = biomeList.length;
        int n2 = 0;
        while (n2 < n) {
            BiomeGenBase biomeGenBase = biomeGenBaseArray[n2];
            if (biomeGenBase != null) {
                if (BIOME_ID_MAP.containsKey(biomeGenBase.biomeName)) {
                    throw new Error("Biome \"" + biomeGenBase.biomeName + "\" is defined as both ID " + BiomeGenBase.BIOME_ID_MAP.get((Object)biomeGenBase.biomeName).biomeID + " and " + biomeGenBase.biomeID);
                }
                BIOME_ID_MAP.put(biomeGenBase.biomeName, biomeGenBase);
                if (biomeGenBase.biomeID < 128) {
                    explorationBiomesList.add(biomeGenBase);
                }
            }
            ++n2;
        }
        explorationBiomesList.remove(hell);
        explorationBiomesList.remove(sky);
        explorationBiomesList.remove(frozenOcean);
        explorationBiomesList.remove(extremeHillsEdge);
        temperatureNoise = new NoiseGeneratorPerlin(new Random(1234L), 1);
        GRASS_COLOR_NOISE = new NoiseGeneratorPerlin(new Random(2345L), 1);
        DOUBLE_PLANT_GENERATOR = new WorldGenDoublePlant();
    }

    public final void generateBiomeTerrain(World world, Random random, ChunkPrimer chunkPrimer, int n, int n2, double d) {
        int n3 = world.func_181545_F();
        IBlockState iBlockState = this.topBlock;
        IBlockState iBlockState2 = this.fillerBlock;
        int n4 = -1;
        int n5 = (int)(d / 3.0 + 3.0 + random.nextDouble() * 0.25);
        int n6 = n & 0xF;
        int n7 = n2 & 0xF;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n8 = 255;
        while (n8 >= 0) {
            if (n8 <= random.nextInt(5)) {
                chunkPrimer.setBlockState(n7, n8, n6, Blocks.bedrock.getDefaultState());
            } else {
                IBlockState iBlockState3 = chunkPrimer.getBlockState(n7, n8, n6);
                if (iBlockState3.getBlock().getMaterial() == Material.air) {
                    n4 = -1;
                } else if (iBlockState3.getBlock() == Blocks.stone) {
                    if (n4 == -1) {
                        if (n5 <= 0) {
                            iBlockState = null;
                            iBlockState2 = Blocks.stone.getDefaultState();
                        } else if (n8 >= n3 - 4 && n8 <= n3 + 1) {
                            iBlockState = this.topBlock;
                            iBlockState2 = this.fillerBlock;
                        }
                        if (n8 < n3 && (iBlockState == null || iBlockState.getBlock().getMaterial() == Material.air)) {
                            iBlockState = this.getFloatTemperature(mutableBlockPos.func_181079_c(n, n8, n2)) < 0.15f ? Blocks.ice.getDefaultState() : Blocks.water.getDefaultState();
                        }
                        n4 = n5;
                        if (n8 >= n3 - 1) {
                            chunkPrimer.setBlockState(n7, n8, n6, iBlockState);
                        } else if (n8 < n3 - 7 - n5) {
                            iBlockState = null;
                            iBlockState2 = Blocks.stone.getDefaultState();
                            chunkPrimer.setBlockState(n7, n8, n6, Blocks.gravel.getDefaultState());
                        } else {
                            chunkPrimer.setBlockState(n7, n8, n6, iBlockState2);
                        }
                    } else if (n4 > 0) {
                        chunkPrimer.setBlockState(n7, n8, n6, iBlockState2);
                        if (--n4 == 0 && iBlockState2.getBlock() == Blocks.sand) {
                            n4 = random.nextInt(4) + Math.max(0, n8 - 63);
                            iBlockState2 = iBlockState2.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState();
                        }
                    }
                }
            }
            --n8;
        }
    }

    protected final BiomeGenBase setHeight(Height height) {
        this.minHeight = height.rootHeight;
        this.maxHeight = height.variation;
        return this;
    }

    public boolean isSnowyBiome() {
        return this.enableSnow;
    }

    public Class<? extends BiomeGenBase> getBiomeClass() {
        return this.getClass();
    }

    public final float getFloatRainfall() {
        return this.rainfall;
    }

    public int getFoliageColorAtPos(BlockPos blockPos) {
        double d = MathHelper.clamp_float(this.getFloatTemperature(blockPos), 0.0f, 1.0f);
        double d2 = MathHelper.clamp_float(this.getFloatRainfall(), 0.0f, 1.0f);
        return ColorizerFoliage.getFoliageColor(d, d2);
    }

    public void decorate(World world, Random random, BlockPos blockPos) {
        this.theBiomeDecorator.decorate(world, random, this, blockPos);
    }

    protected BiomeGenBase func_150563_c(int n) {
        this.field_150609_ah = n;
        return this;
    }

    protected BiomeDecorator createBiomeDecorator() {
        return new BiomeDecorator();
    }

    public final float getFloatTemperature(BlockPos blockPos) {
        if (blockPos.getY() > 64) {
            float f = (float)(temperatureNoise.func_151601_a((double)blockPos.getX() * 1.0 / 8.0, (double)blockPos.getZ() * 1.0 / 8.0) * 4.0);
            return this.temperature - (f + (float)blockPos.getY() - 64.0f) * 0.05f / 30.0f;
        }
        return this.temperature;
    }

    public boolean canSpawnLightningBolt() {
        return this.isSnowyBiome() ? false : this.enableRain;
    }

    public int getGrassColorAtPos(BlockPos blockPos) {
        double d = MathHelper.clamp_float(this.getFloatTemperature(blockPos), 0.0f, 1.0f);
        double d2 = MathHelper.clamp_float(this.getFloatRainfall(), 0.0f, 1.0f);
        return ColorizerGrass.getGrassColor(d, d2);
    }

    public boolean isHighHumidity() {
        return this.rainfall > 0.85f;
    }

    protected BiomeGenBase setEnableSnow() {
        this.enableSnow = true;
        return this;
    }

    protected BiomeGenBase createMutatedBiome(int n) {
        return new BiomeGenMutated(n, this);
    }

    public boolean getEnableSnow() {
        return this.isSnowyBiome();
    }

    public static BiomeGenBase[] getBiomeGenArray() {
        return biomeList;
    }

    public BlockFlower.EnumFlowerType pickRandomFlower(Random random, BlockPos blockPos) {
        return random.nextInt(3) > 0 ? BlockFlower.EnumFlowerType.DANDELION : BlockFlower.EnumFlowerType.POPPY;
    }

    public boolean isEqualTo(BiomeGenBase biomeGenBase) {
        return biomeGenBase == this ? true : (biomeGenBase == null ? false : this.getBiomeClass() == biomeGenBase.getBiomeClass());
    }

    public WorldGenAbstractTree genBigTreeChance(Random random) {
        return random.nextInt(10) == 0 ? this.worldGeneratorBigTree : this.worldGeneratorTrees;
    }

    public static BiomeGenBase getBiome(int n) {
        return BiomeGenBase.getBiomeFromBiomeList(n, null);
    }

    public float getSpawningChance() {
        return 0.1f;
    }

    public TempCategory getTempCategory() {
        return (double)this.temperature < 0.2 ? TempCategory.COLD : ((double)this.temperature < 1.0 ? TempCategory.MEDIUM : TempCategory.WARM);
    }

    protected BiomeGenBase func_150557_a(int n, boolean bl) {
        this.color = n;
        this.field_150609_ah = bl ? (n & 0xFEFEFE) >> 1 : n;
        return this;
    }

    public static BiomeGenBase getBiomeFromBiomeList(int n, BiomeGenBase biomeGenBase) {
        if (n >= 0 && n <= biomeList.length) {
            BiomeGenBase biomeGenBase2 = biomeList[n];
            return biomeGenBase2 == null ? biomeGenBase : biomeGenBase2;
        }
        logger.warn("Biome ID is out of bounds: " + n + ", defaulting to 0 (Ocean)");
        return ocean;
    }

    protected BiomeGenBase setTemperatureRainfall(float f, float f2) {
        if (f > 0.1f && f < 0.2f) {
            throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
        }
        this.temperature = f;
        this.rainfall = f2;
        return this;
    }

    protected BiomeGenBase setFillerBlockMetadata(int n) {
        this.fillerBlockMetadata = n;
        return this;
    }

    public int getSkyColorByTemp(float f) {
        f /= 3.0f;
        f = MathHelper.clamp_float(f, -1.0f, 1.0f);
        return MathHelper.func_181758_c(0.62222224f - f * 0.05f, 0.5f + f * 0.1f, 1.0f);
    }

    public List<SpawnListEntry> getSpawnableList(EnumCreatureType enumCreatureType) {
        switch (enumCreatureType) {
            case MONSTER: {
                return this.spawnableMonsterList;
            }
            case CREATURE: {
                return this.spawnableCreatureList;
            }
            case WATER_CREATURE: {
                return this.spawnableWaterCreatureList;
            }
            case AMBIENT: {
                return this.spawnableCaveCreatureList;
            }
        }
        return Collections.emptyList();
    }

    protected BiomeGenBase createMutation() {
        return this.createMutatedBiome(this.biomeID + 128);
    }

    public final int getIntRainfall() {
        return (int)(this.rainfall * 65536.0f);
    }

    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunkPrimer, int n, int n2, double d) {
        this.generateBiomeTerrain(world, random, chunkPrimer, n, n2, d);
    }

    protected BiomeGenBase(int n) {
        this.minHeight = BiomeGenBase.height_Default.rootHeight;
        this.maxHeight = BiomeGenBase.height_Default.variation;
        this.temperature = 0.5f;
        this.rainfall = 0.5f;
        this.waterColorMultiplier = 0xFFFFFF;
        this.spawnableMonsterList = Lists.newArrayList();
        this.spawnableCreatureList = Lists.newArrayList();
        this.spawnableWaterCreatureList = Lists.newArrayList();
        this.spawnableCaveCreatureList = Lists.newArrayList();
        this.enableRain = true;
        this.worldGeneratorTrees = new WorldGenTrees(false);
        this.worldGeneratorBigTree = new WorldGenBigTree(false);
        this.worldGeneratorSwamp = new WorldGenSwamp();
        this.biomeID = n;
        BiomeGenBase.biomeList[n] = this;
        this.theBiomeDecorator = this.createBiomeDecorator();
        this.spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 12, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityRabbit.class, 10, 3, 3));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityPig.class, 10, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 8, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 10, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityWitch.class, 5, 1, 1));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 10, 4, 4));
        this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityBat.class, 10, 8, 8));
    }

    protected BiomeGenBase setDisableRain() {
        this.enableRain = false;
        return this;
    }

    protected BiomeGenBase setColor(int n) {
        this.func_150557_a(n, false);
        return this;
    }

    protected BiomeGenBase setBiomeName(String string) {
        this.biomeName = string;
        return this;
    }

    public static class SpawnListEntry
    extends WeightedRandom.Item {
        public Class<? extends EntityLiving> entityClass;
        public int minGroupCount;
        public int maxGroupCount;

        public String toString() {
            return String.valueOf(this.entityClass.getSimpleName()) + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):" + this.itemWeight;
        }

        public SpawnListEntry(Class<? extends EntityLiving> clazz, int n, int n2, int n3) {
            super(n);
            this.entityClass = clazz;
            this.minGroupCount = n2;
            this.maxGroupCount = n3;
        }
    }

    public static enum TempCategory {
        OCEAN,
        COLD,
        MEDIUM,
        WARM;

    }

    public static class Height {
        public float variation;
        public float rootHeight;

        public Height(float f, float f2) {
            this.rootHeight = f;
            this.variation = f2;
        }

        public Height attenuate() {
            return new Height(this.rootHeight * 0.8f, this.variation * 0.6f);
        }
    }
}

