/*
 * Decompiled with CFR 0.150.
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
import java.awt.Color;
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
    private static final Logger logger = LogManager.getLogger();
    protected static final Height height_Default = new Height(0.1f, 0.2f);
    protected static final Height height_ShallowWaters = new Height(-0.5f, 0.0f);
    protected static final Height height_Oceans = new Height(-1.0f, 0.1f);
    protected static final Height height_DeepOceans = new Height(-1.8f, 0.1f);
    protected static final Height height_LowPlains = new Height(0.125f, 0.05f);
    protected static final Height height_MidPlains = new Height(0.2f, 0.2f);
    protected static final Height height_LowHills = new Height(0.45f, 0.3f);
    protected static final Height height_HighPlateaus = new Height(1.5f, 0.025f);
    protected static final Height height_MidHills = new Height(1.0f, 0.5f);
    protected static final Height height_Shores = new Height(0.0f, 0.025f);
    protected static final Height height_RockyWaters = new Height(0.1f, 0.8f);
    protected static final Height height_LowIslands = new Height(0.2f, 0.3f);
    protected static final Height height_PartiallySubmerged = new Height(-0.2f, 0.1f);
    private static final BiomeGenBase[] biomeList = new BiomeGenBase[256];
    public static final Set explorationBiomesList = Sets.newHashSet();
    public static final Map field_180278_o = Maps.newHashMap();
    public static final BiomeGenBase ocean = new BiomeGenOcean(0).setColor(112).setBiomeName("Ocean").setHeight(height_Oceans);
    public static final BiomeGenBase plains = new BiomeGenPlains(1).setColor(9286496).setBiomeName("Plains");
    public static final BiomeGenBase desert = new BiomeGenDesert(2).setColor(16421912).setBiomeName("Desert").setDisableRain().setTemperatureRainfall(2.0f, 0.0f).setHeight(height_LowPlains);
    public static final BiomeGenBase extremeHills = new BiomeGenHills(3, false).setColor(0x606060).setBiomeName("Extreme Hills").setHeight(height_MidHills).setTemperatureRainfall(0.2f, 0.3f);
    public static final BiomeGenBase forest = new BiomeGenForest(4, 0).setColor(353825).setBiomeName("Forest");
    public static final BiomeGenBase taiga = new BiomeGenTaiga(5, 0).setColor(747097).setBiomeName("Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25f, 0.8f).setHeight(height_MidPlains);
    public static final BiomeGenBase swampland = new BiomeGenSwamp(6).setColor(522674).setBiomeName("Swampland").setFillerBlockMetadata(9154376).setHeight(height_PartiallySubmerged).setTemperatureRainfall(0.8f, 0.9f);
    public static final BiomeGenBase river = new BiomeGenRiver(7).setColor(255).setBiomeName("River").setHeight(height_ShallowWaters);
    public static final BiomeGenBase hell = new BiomeGenHell(8).setColor(0xFF0000).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0f, 0.0f);
    public static final BiomeGenBase sky = new BiomeGenEnd(9).setColor(0x8080FF).setBiomeName("The End").setDisableRain();
    public static final BiomeGenBase frozenOcean = new BiomeGenOcean(10).setColor(0x9090A0).setBiomeName("FrozenOcean").setEnableSnow().setHeight(height_Oceans).setTemperatureRainfall(0.0f, 0.5f);
    public static final BiomeGenBase frozenRiver = new BiomeGenRiver(11).setColor(0xA0A0FF).setBiomeName("FrozenRiver").setEnableSnow().setHeight(height_ShallowWaters).setTemperatureRainfall(0.0f, 0.5f);
    public static final BiomeGenBase icePlains = new BiomeGenSnow(12, false).setColor(0xFFFFFF).setBiomeName("Ice Plains").setEnableSnow().setTemperatureRainfall(0.0f, 0.5f).setHeight(height_LowPlains);
    public static final BiomeGenBase iceMountains = new BiomeGenSnow(13, false).setColor(0xA0A0A0).setBiomeName("Ice Mountains").setEnableSnow().setHeight(height_LowHills).setTemperatureRainfall(0.0f, 0.5f);
    public static final BiomeGenBase mushroomIsland = new BiomeGenMushroomIsland(14).setColor(0xFF00FF).setBiomeName("MushroomIsland").setTemperatureRainfall(0.9f, 1.0f).setHeight(height_LowIslands);
    public static final BiomeGenBase mushroomIslandShore = new BiomeGenMushroomIsland(15).setColor(0xA000FF).setBiomeName("MushroomIslandShore").setTemperatureRainfall(0.9f, 1.0f).setHeight(height_Shores);
    public static final BiomeGenBase beach = new BiomeGenBeach(16).setColor(16440917).setBiomeName("Beach").setTemperatureRainfall(0.8f, 0.4f).setHeight(height_Shores);
    public static final BiomeGenBase desertHills = new BiomeGenDesert(17).setColor(13786898).setBiomeName("DesertHills").setDisableRain().setTemperatureRainfall(2.0f, 0.0f).setHeight(height_LowHills);
    public static final BiomeGenBase forestHills = new BiomeGenForest(18, 0).setColor(2250012).setBiomeName("ForestHills").setHeight(height_LowHills);
    public static final BiomeGenBase taigaHills = new BiomeGenTaiga(19, 0).setColor(1456435).setBiomeName("TaigaHills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25f, 0.8f).setHeight(height_LowHills);
    public static final BiomeGenBase extremeHillsEdge = new BiomeGenHills(20, true).setColor(7501978).setBiomeName("Extreme Hills Edge").setHeight(height_MidHills.attenuate()).setTemperatureRainfall(0.2f, 0.3f);
    public static final BiomeGenBase jungle = new BiomeGenJungle(21, false).setColor(5470985).setBiomeName("Jungle").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95f, 0.9f);
    public static final BiomeGenBase jungleHills = new BiomeGenJungle(22, false).setColor(2900485).setBiomeName("JungleHills").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95f, 0.9f).setHeight(height_LowHills);
    public static final BiomeGenBase jungleEdge = new BiomeGenJungle(23, true).setColor(6458135).setBiomeName("JungleEdge").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95f, 0.8f);
    public static final BiomeGenBase deepOcean = new BiomeGenOcean(24).setColor(48).setBiomeName("Deep Ocean").setHeight(height_DeepOceans);
    public static final BiomeGenBase stoneBeach = new BiomeGenStoneBeach(25).setColor(10658436).setBiomeName("Stone Beach").setTemperatureRainfall(0.2f, 0.3f).setHeight(height_RockyWaters);
    public static final BiomeGenBase coldBeach = new BiomeGenBeach(26).setColor(16445632).setBiomeName("Cold Beach").setTemperatureRainfall(0.05f, 0.3f).setHeight(height_Shores).setEnableSnow();
    public static final BiomeGenBase birchForest = new BiomeGenForest(27, 2).setBiomeName("Birch Forest").setColor(3175492);
    public static final BiomeGenBase birchForestHills = new BiomeGenForest(28, 2).setBiomeName("Birch Forest Hills").setColor(2055986).setHeight(height_LowHills);
    public static final BiomeGenBase roofedForest = new BiomeGenForest(29, 3).setColor(4215066).setBiomeName("Roofed Forest");
    public static final BiomeGenBase coldTaiga = new BiomeGenTaiga(30, 0).setColor(3233098).setBiomeName("Cold Taiga").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5f, 0.4f).setHeight(height_MidPlains).func_150563_c(0xFFFFFF);
    public static final BiomeGenBase coldTaigaHills = new BiomeGenTaiga(31, 0).setColor(2375478).setBiomeName("Cold Taiga Hills").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5f, 0.4f).setHeight(height_LowHills).func_150563_c(0xFFFFFF);
    public static final BiomeGenBase megaTaiga = new BiomeGenTaiga(32, 1).setColor(5858897).setBiomeName("Mega Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3f, 0.8f).setHeight(height_MidPlains);
    public static final BiomeGenBase megaTaigaHills = new BiomeGenTaiga(33, 1).setColor(4542270).setBiomeName("Mega Taiga Hills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3f, 0.8f).setHeight(height_LowHills);
    public static final BiomeGenBase extremeHillsPlus = new BiomeGenHills(34, true).setColor(0x507050).setBiomeName("Extreme Hills+").setHeight(height_MidHills).setTemperatureRainfall(0.2f, 0.3f);
    public static final BiomeGenBase savanna = new BiomeGenSavanna(35).setColor(12431967).setBiomeName("Savanna").setTemperatureRainfall(1.2f, 0.0f).setDisableRain().setHeight(height_LowPlains);
    public static final BiomeGenBase savannaPlateau = new BiomeGenSavanna(36).setColor(10984804).setBiomeName("Savanna Plateau").setTemperatureRainfall(1.0f, 0.0f).setDisableRain().setHeight(height_HighPlateaus);
    public static final BiomeGenBase mesa = new BiomeGenMesa(37, false, false).setColor(14238997).setBiomeName("Mesa");
    public static final BiomeGenBase mesaPlateau_F = new BiomeGenMesa(38, false, true).setColor(11573093).setBiomeName("Mesa Plateau F").setHeight(height_HighPlateaus);
    public static final BiomeGenBase mesaPlateau = new BiomeGenMesa(39, false, false).setColor(13274213).setBiomeName("Mesa Plateau").setHeight(height_HighPlateaus);
    public static final BiomeGenBase field_180279_ad = ocean;
    protected static final NoiseGeneratorPerlin temperatureNoise;
    protected static final NoiseGeneratorPerlin field_180281_af;
    protected static final WorldGenDoublePlant field_180280_ag;
    public String biomeName;
    public int color;
    public int field_150609_ah;
    public IBlockState topBlock = Blocks.grass.getDefaultState();
    public IBlockState fillerBlock = Blocks.dirt.getDefaultState();
    public int fillerBlockMetadata = 5169201;
    public float minHeight;
    public float maxHeight;
    public float temperature;
    public float rainfall;
    public int waterColorMultiplier;
    public BiomeDecorator theBiomeDecorator;
    protected List spawnableMonsterList;
    protected List spawnableCreatureList;
    protected List spawnableWaterCreatureList;
    protected List spawnableCaveCreatureList;
    protected boolean enableSnow;
    protected boolean enableRain;
    public final int biomeID;
    protected WorldGenTrees worldGeneratorTrees;
    protected WorldGenBigTree worldGeneratorBigTree;
    protected WorldGenSwamp worldGeneratorSwamp;
    private static final String __OBFID = "CL_00000158";

    static {
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
        for (BiomeGenBase var3 : biomeList) {
            if (var3 == null) continue;
            if (field_180278_o.containsKey(var3.biomeName)) {
                throw new Error("Biome \"" + var3.biomeName + "\" is defined as both ID " + ((BiomeGenBase)BiomeGenBase.field_180278_o.get((Object)var3.biomeName)).biomeID + " and " + var3.biomeID);
            }
            field_180278_o.put(var3.biomeName, var3);
            if (var3.biomeID >= 128) continue;
            explorationBiomesList.add(var3);
        }
        explorationBiomesList.remove(hell);
        explorationBiomesList.remove(sky);
        explorationBiomesList.remove(frozenOcean);
        explorationBiomesList.remove(extremeHillsEdge);
        temperatureNoise = new NoiseGeneratorPerlin(new Random(1234L), 1);
        field_180281_af = new NoiseGeneratorPerlin(new Random(2345L), 1);
        field_180280_ag = new WorldGenDoublePlant();
    }

    protected BiomeGenBase(int p_i1971_1_) {
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
        this.biomeID = p_i1971_1_;
        BiomeGenBase.biomeList[p_i1971_1_] = this;
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

    protected BiomeDecorator createBiomeDecorator() {
        return new BiomeDecorator();
    }

    protected BiomeGenBase setTemperatureRainfall(float p_76732_1_, float p_76732_2_) {
        if (p_76732_1_ > 0.1f && p_76732_1_ < 0.2f) {
            throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
        }
        this.temperature = p_76732_1_;
        this.rainfall = p_76732_2_;
        return this;
    }

    protected final BiomeGenBase setHeight(Height p_150570_1_) {
        this.minHeight = p_150570_1_.rootHeight;
        this.maxHeight = p_150570_1_.variation;
        return this;
    }

    protected BiomeGenBase setDisableRain() {
        this.enableRain = false;
        return this;
    }

    public WorldGenAbstractTree genBigTreeChance(Random p_150567_1_) {
        return p_150567_1_.nextInt(10) == 0 ? this.worldGeneratorBigTree : this.worldGeneratorTrees;
    }

    public WorldGenerator getRandomWorldGenForGrass(Random p_76730_1_) {
        return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public BlockFlower.EnumFlowerType pickRandomFlower(Random p_180623_1_, BlockPos p_180623_2_) {
        return p_180623_1_.nextInt(3) > 0 ? BlockFlower.EnumFlowerType.DANDELION : BlockFlower.EnumFlowerType.POPPY;
    }

    protected BiomeGenBase setEnableSnow() {
        this.enableSnow = true;
        return this;
    }

    protected BiomeGenBase setBiomeName(String p_76735_1_) {
        this.biomeName = p_76735_1_;
        return this;
    }

    protected BiomeGenBase setFillerBlockMetadata(int p_76733_1_) {
        this.fillerBlockMetadata = p_76733_1_;
        return this;
    }

    protected BiomeGenBase setColor(int p_76739_1_) {
        this.func_150557_a(p_76739_1_, false);
        return this;
    }

    protected BiomeGenBase func_150563_c(int p_150563_1_) {
        this.field_150609_ah = p_150563_1_;
        return this;
    }

    protected BiomeGenBase func_150557_a(int p_150557_1_, boolean p_150557_2_) {
        this.color = p_150557_1_;
        this.field_150609_ah = p_150557_2_ ? (p_150557_1_ & 0xFEFEFE) >> 1 : p_150557_1_;
        return this;
    }

    public int getSkyColorByTemp(float p_76731_1_) {
        p_76731_1_ /= 3.0f;
        p_76731_1_ = MathHelper.clamp_float(p_76731_1_, -1.0f, 1.0f);
        return Color.getHSBColor(0.62222224f - p_76731_1_ * 0.05f, 0.5f + p_76731_1_ * 0.1f, 1.0f).getRGB();
    }

    public List getSpawnableList(EnumCreatureType p_76747_1_) {
        switch (SwitchEnumCreatureType.field_180275_a[p_76747_1_.ordinal()]) {
            case 1: {
                return this.spawnableMonsterList;
            }
            case 2: {
                return this.spawnableCreatureList;
            }
            case 3: {
                return this.spawnableWaterCreatureList;
            }
            case 4: {
                return this.spawnableCaveCreatureList;
            }
        }
        return Collections.emptyList();
    }

    public boolean getEnableSnow() {
        return this.isSnowyBiome();
    }

    public boolean canSpawnLightningBolt() {
        return this.isSnowyBiome() ? false : this.enableRain;
    }

    public boolean isHighHumidity() {
        return this.rainfall > 0.85f;
    }

    public float getSpawningChance() {
        return 0.1f;
    }

    public final int getIntRainfall() {
        return (int)(this.rainfall * 65536.0f);
    }

    public final float getFloatRainfall() {
        return this.rainfall;
    }

    public final float func_180626_a(BlockPos p_180626_1_) {
        if (p_180626_1_.getY() > 64) {
            float var2 = (float)(temperatureNoise.func_151601_a((double)p_180626_1_.getX() * 1.0 / 8.0, (double)p_180626_1_.getZ() * 1.0 / 8.0) * 4.0);
            return this.temperature - (var2 + (float)p_180626_1_.getY() - 64.0f) * 0.05f / 30.0f;
        }
        return this.temperature;
    }

    public void func_180624_a(World worldIn, Random p_180624_2_, BlockPos p_180624_3_) {
        this.theBiomeDecorator.func_180292_a(worldIn, p_180624_2_, this, p_180624_3_);
    }

    public int func_180627_b(BlockPos p_180627_1_) {
        double var2 = MathHelper.clamp_float(this.func_180626_a(p_180627_1_), 0.0f, 1.0f);
        double var4 = MathHelper.clamp_float(this.getFloatRainfall(), 0.0f, 1.0f);
        return ColorizerGrass.getGrassColor(var2, var4);
    }

    public int func_180625_c(BlockPos p_180625_1_) {
        double var2 = MathHelper.clamp_float(this.func_180626_a(p_180625_1_), 0.0f, 1.0f);
        double var4 = MathHelper.clamp_float(this.getFloatRainfall(), 0.0f, 1.0f);
        return ColorizerFoliage.getFoliageColor(var2, var4);
    }

    public boolean isSnowyBiome() {
        return this.enableSnow;
    }

    public void genTerrainBlocks(World worldIn, Random p_180622_2_, ChunkPrimer p_180622_3_, int p_180622_4_, int p_180622_5_, double p_180622_6_) {
        this.func_180628_b(worldIn, p_180622_2_, p_180622_3_, p_180622_4_, p_180622_5_, p_180622_6_);
    }

    public final void func_180628_b(World worldIn, Random p_180628_2_, ChunkPrimer p_180628_3_, int p_180628_4_, int p_180628_5_, double p_180628_6_) {
        boolean var8 = true;
        IBlockState var9 = this.topBlock;
        IBlockState var10 = this.fillerBlock;
        int var11 = -1;
        int var12 = (int)(p_180628_6_ / 3.0 + 3.0 + p_180628_2_.nextDouble() * 0.25);
        int var13 = p_180628_4_ & 0xF;
        int var14 = p_180628_5_ & 0xF;
        for (int var15 = 255; var15 >= 0; --var15) {
            if (var15 <= p_180628_2_.nextInt(5)) {
                p_180628_3_.setBlockState(var14, var15, var13, Blocks.bedrock.getDefaultState());
                continue;
            }
            IBlockState var16 = p_180628_3_.getBlockState(var14, var15, var13);
            if (var16.getBlock().getMaterial() == Material.air) {
                var11 = -1;
                continue;
            }
            if (var16.getBlock() != Blocks.stone) continue;
            if (var11 == -1) {
                if (var12 <= 0) {
                    var9 = null;
                    var10 = Blocks.stone.getDefaultState();
                } else if (var15 >= 59 && var15 <= 64) {
                    var9 = this.topBlock;
                    var10 = this.fillerBlock;
                }
                if (var15 < 63 && (var9 == null || var9.getBlock().getMaterial() == Material.air)) {
                    var9 = this.func_180626_a(new BlockPos(p_180628_4_, var15, p_180628_5_)) < 0.15f ? Blocks.ice.getDefaultState() : Blocks.water.getDefaultState();
                }
                var11 = var12;
                if (var15 >= 62) {
                    p_180628_3_.setBlockState(var14, var15, var13, var9);
                    continue;
                }
                if (var15 < 56 - var12) {
                    var9 = null;
                    var10 = Blocks.stone.getDefaultState();
                    p_180628_3_.setBlockState(var14, var15, var13, Blocks.gravel.getDefaultState());
                    continue;
                }
                p_180628_3_.setBlockState(var14, var15, var13, var10);
                continue;
            }
            if (var11 <= 0) continue;
            p_180628_3_.setBlockState(var14, var15, var13, var10);
            if (--var11 != 0 || var10.getBlock() != Blocks.sand) continue;
            var11 = p_180628_2_.nextInt(4) + Math.max(0, var15 - 63);
            var10 = var10.getValue(BlockSand.VARIANT_PROP) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState();
        }
    }

    protected BiomeGenBase createMutation() {
        return this.createMutatedBiome(this.biomeID + 128);
    }

    protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
        return new BiomeGenMutated(p_180277_1_, this);
    }

    public Class getBiomeClass() {
        return this.getClass();
    }

    public boolean isEqualTo(BiomeGenBase p_150569_1_) {
        return p_150569_1_ == this ? true : (p_150569_1_ == null ? false : this.getBiomeClass() == p_150569_1_.getBiomeClass());
    }

    public TempCategory getTempCategory() {
        return (double)this.temperature < 0.2 ? TempCategory.COLD : ((double)this.temperature < 1.0 ? TempCategory.MEDIUM : TempCategory.WARM);
    }

    public static BiomeGenBase[] getBiomeGenArray() {
        return biomeList;
    }

    public static BiomeGenBase getBiome(int p_150568_0_) {
        return BiomeGenBase.getBiomeFromBiomeList(p_150568_0_, null);
    }

    public static BiomeGenBase getBiomeFromBiomeList(int p_180276_0_, BiomeGenBase p_180276_1_) {
        if (p_180276_0_ >= 0 && p_180276_0_ <= biomeList.length) {
            BiomeGenBase var2 = biomeList[p_180276_0_];
            return var2 == null ? p_180276_1_ : var2;
        }
        logger.warn("Biome ID is out of bounds: " + p_180276_0_ + ", defaulting to 0 (Ocean)");
        return ocean;
    }

    public static class Height {
        public float rootHeight;
        public float variation;
        private static final String __OBFID = "CL_00000159";

        public Height(float p_i45371_1_, float p_i45371_2_) {
            this.rootHeight = p_i45371_1_;
            this.variation = p_i45371_2_;
        }

        public Height attenuate() {
            return new Height(this.rootHeight * 0.8f, this.variation * 0.6f);
        }
    }

    public static class SpawnListEntry
    extends WeightedRandom.Item {
        public Class entityClass;
        public int minGroupCount;
        public int maxGroupCount;
        private static final String __OBFID = "CL_00000161";

        public SpawnListEntry(Class p_i1970_1_, int p_i1970_2_, int p_i1970_3_, int p_i1970_4_) {
            super(p_i1970_2_);
            this.entityClass = p_i1970_1_;
            this.minGroupCount = p_i1970_3_;
            this.maxGroupCount = p_i1970_4_;
        }

        public String toString() {
            return String.valueOf(this.entityClass.getSimpleName()) + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):" + this.itemWeight;
        }
    }

    static final class SwitchEnumCreatureType {
        static final int[] field_180275_a = new int[EnumCreatureType.values().length];
        private static final String __OBFID = "CL_00002150";

        static {
            try {
                SwitchEnumCreatureType.field_180275_a[EnumCreatureType.MONSTER.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumCreatureType.field_180275_a[EnumCreatureType.CREATURE.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumCreatureType.field_180275_a[EnumCreatureType.WATER_CREATURE.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumCreatureType.field_180275_a[EnumCreatureType.AMBIENT.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumCreatureType() {
        }
    }

    public static enum TempCategory {
        OCEAN("OCEAN", 0),
        COLD("COLD", 1),
        MEDIUM("MEDIUM", 2),
        WARM("WARM", 3);

        private static final TempCategory[] $VALUES;
        private static final String __OBFID = "CL_00000160";

        static {
            $VALUES = new TempCategory[]{OCEAN, COLD, MEDIUM, WARM};
        }

        private TempCategory(String p_i45372_1_, int p_i45372_2_) {
        }
    }
}

