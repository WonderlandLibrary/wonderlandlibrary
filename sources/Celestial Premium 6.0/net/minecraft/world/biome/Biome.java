/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
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
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeBeach;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.biome.BiomeForestMutated;
import net.minecraft.world.biome.BiomeHell;
import net.minecraft.world.biome.BiomeHills;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.world.biome.BiomeMushroomIsland;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.biome.BiomeRiver;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraft.world.biome.BiomeSavannaMutated;
import net.minecraft.world.biome.BiomeSnow;
import net.minecraft.world.biome.BiomeStoneBeach;
import net.minecraft.world.biome.BiomeSwamp;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraft.world.biome.BiomeVoid;
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

public abstract class Biome {
    private static final Logger LOGGER = LogManager.getLogger();
    protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    protected static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
    protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
    protected static final IBlockState RED_SANDSTONE = Blocks.RED_SANDSTONE.getDefaultState();
    protected static final IBlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();
    protected static final IBlockState ICE = Blocks.ICE.getDefaultState();
    protected static final IBlockState WATER = Blocks.WATER.getDefaultState();
    public static final ObjectIntIdentityMap<Biome> MUTATION_TO_BASE_ID_MAP = new ObjectIntIdentityMap();
    protected static final NoiseGeneratorPerlin TEMPERATURE_NOISE = new NoiseGeneratorPerlin(new Random(1234L), 1);
    protected static final NoiseGeneratorPerlin GRASS_COLOR_NOISE = new NoiseGeneratorPerlin(new Random(2345L), 1);
    protected static final WorldGenDoublePlant DOUBLE_PLANT_GENERATOR = new WorldGenDoublePlant();
    protected static final WorldGenTrees TREE_FEATURE = new WorldGenTrees(false);
    protected static final WorldGenBigTree BIG_TREE_FEATURE = new WorldGenBigTree(false);
    protected static final WorldGenSwamp SWAMP_FEATURE = new WorldGenSwamp();
    public static final RegistryNamespaced<ResourceLocation, Biome> REGISTRY = new RegistryNamespaced();
    private final String biomeName;
    private final float baseHeight;
    private final float heightVariation;
    private final float temperature;
    private final float rainfall;
    private final int waterColor;
    private final boolean enableSnow;
    private final boolean enableRain;
    @Nullable
    private final String baseBiomeRegName;
    public IBlockState topBlock = Blocks.GRASS.getDefaultState();
    public IBlockState fillerBlock = Blocks.DIRT.getDefaultState();
    public BiomeDecorator theBiomeDecorator;
    protected List<SpawnListEntry> spawnableMonsterList = Lists.newArrayList();
    protected List<SpawnListEntry> spawnableCreatureList = Lists.newArrayList();
    protected List<SpawnListEntry> spawnableWaterCreatureList = Lists.newArrayList();
    protected List<SpawnListEntry> spawnableCaveCreatureList = Lists.newArrayList();

    public static int getIdForBiome(Biome biome) {
        return REGISTRY.getIDForObject(biome);
    }

    @Nullable
    public static Biome getBiomeForId(int id) {
        return REGISTRY.getObjectById(id);
    }

    @Nullable
    public static Biome getMutationForBiome(Biome biome) {
        return MUTATION_TO_BASE_ID_MAP.getByValue(Biome.getIdForBiome(biome));
    }

    protected Biome(BiomeProperties properties) {
        this.biomeName = properties.biomeName;
        this.baseHeight = properties.baseHeight;
        this.heightVariation = properties.heightVariation;
        this.temperature = properties.temperature;
        this.rainfall = properties.rainfall;
        this.waterColor = properties.waterColor;
        this.enableSnow = properties.enableSnow;
        this.enableRain = properties.enableRain;
        this.baseBiomeRegName = properties.baseBiomeRegName;
        this.theBiomeDecorator = this.createBiomeDecorator();
        this.spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 12, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityPig.class, 10, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 8, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 95, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityZombieVillager.class, 5, 1, 1));
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

    public boolean isMutation() {
        return this.baseBiomeRegName != null;
    }

    public WorldGenAbstractTree genBigTreeChance(Random rand) {
        return rand.nextInt(10) == 0 ? BIG_TREE_FEATURE : TREE_FEATURE;
    }

    public WorldGenerator getRandomWorldGenForGrass(Random rand) {
        return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos) {
        return rand.nextInt(3) > 0 ? BlockFlower.EnumFlowerType.DANDELION : BlockFlower.EnumFlowerType.POPPY;
    }

    public int getSkyColorByTemp(float currentTemperature) {
        currentTemperature /= 3.0f;
        currentTemperature = MathHelper.clamp(currentTemperature, -1.0f, 1.0f);
        return MathHelper.hsvToRGB(0.62222224f - currentTemperature * 0.05f, 0.5f + currentTemperature * 0.1f, 1.0f);
    }

    public List<SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
        switch (creatureType) {
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

    public boolean getEnableSnow() {
        return this.isSnowyBiome();
    }

    public boolean canRain() {
        return this.isSnowyBiome() ? false : this.enableRain;
    }

    public boolean isHighHumidity() {
        return this.getRainfall() > 0.85f;
    }

    public float getSpawningChance() {
        return 0.1f;
    }

    public final float getFloatTemperature(BlockPos pos) {
        if (pos.getY() > 64) {
            float f = (float)(TEMPERATURE_NOISE.getValue((float)pos.getX() / 8.0f, (float)pos.getZ() / 8.0f) * 4.0);
            return this.getTemperature() - (f + (float)pos.getY() - 64.0f) * 0.05f / 30.0f;
        }
        return this.getTemperature();
    }

    public void decorate(World worldIn, Random rand, BlockPos pos) {
        this.theBiomeDecorator.decorate(worldIn, rand, this, pos);
    }

    public int getGrassColorAtPos(BlockPos pos) {
        double d0 = MathHelper.clamp(this.getFloatTemperature(pos), 0.0f, 1.0f);
        double d1 = MathHelper.clamp(this.getRainfall(), 0.0f, 1.0f);
        return ColorizerGrass.getGrassColor(d0, d1);
    }

    public int getFoliageColorAtPos(BlockPos pos) {
        double d0 = MathHelper.clamp(this.getFloatTemperature(pos), 0.0f, 1.0f);
        double d1 = MathHelper.clamp(this.getRainfall(), 0.0f, 1.0f);
        return ColorizerFoliage.getFoliageColor(d0, d1);
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    public final void generateBiomeTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        int i = worldIn.getSeaLevel();
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = this.fillerBlock;
        int j = -1;
        int k = (int)(noiseVal / 3.0 + 3.0 + rand.nextDouble() * 0.25);
        int l = x & 0xF;
        int i1 = z & 0xF;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j1 = 255; j1 >= 0; --j1) {
            if (j1 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK);
                continue;
            }
            IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);
            if (iblockstate2.getMaterial() == Material.AIR) {
                j = -1;
                continue;
            }
            if (iblockstate2.getBlock() != Blocks.STONE) continue;
            if (j == -1) {
                if (k <= 0) {
                    iblockstate = AIR;
                    iblockstate1 = STONE;
                } else if (j1 >= i - 4 && j1 <= i + 1) {
                    iblockstate = this.topBlock;
                    iblockstate1 = this.fillerBlock;
                }
                if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                    iblockstate = this.getFloatTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15f ? ICE : WATER;
                }
                j = k;
                if (j1 >= i - 1) {
                    chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                    continue;
                }
                if (j1 < i - 7 - k) {
                    iblockstate = AIR;
                    iblockstate1 = STONE;
                    chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
                    continue;
                }
                chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                continue;
            }
            if (j <= 0) continue;
            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
            if (--j != 0 || iblockstate1.getBlock() != Blocks.SAND || k <= 1) continue;
            j = rand.nextInt(4) + Math.max(0, j1 - 63);
            iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
        }
    }

    public Class<? extends Biome> getBiomeClass() {
        return this.getClass();
    }

    public TempCategory getTempCategory() {
        if ((double)this.getTemperature() < 0.2) {
            return TempCategory.COLD;
        }
        return (double)this.getTemperature() < 1.0 ? TempCategory.MEDIUM : TempCategory.WARM;
    }

    @Nullable
    public static Biome getBiome(int id) {
        return Biome.getBiome(id, null);
    }

    public static Biome getBiome(int biomeId, Biome fallback) {
        Biome biome = Biome.getBiomeForId(biomeId);
        return biome == null ? fallback : biome;
    }

    public boolean ignorePlayerSpawnSuitability() {
        return false;
    }

    public final float getBaseHeight() {
        return this.baseHeight;
    }

    public final float getRainfall() {
        return this.rainfall;
    }

    public final String getBiomeName() {
        return this.biomeName;
    }

    public final float getHeightVariation() {
        return this.heightVariation;
    }

    public final float getTemperature() {
        return this.temperature;
    }

    public final int getWaterColor() {
        return this.waterColor;
    }

    public final boolean isSnowyBiome() {
        return this.enableSnow;
    }

    public static void registerBiomes() {
        Biome.registerBiome(0, "ocean", new BiomeOcean(new BiomeProperties("Ocean").setBaseHeight(-1.0f).setHeightVariation(0.1f)));
        Biome.registerBiome(1, "plains", new BiomePlains(false, new BiomeProperties("Plains").setBaseHeight(0.125f).setHeightVariation(0.05f).setTemperature(0.8f).setRainfall(0.4f)));
        Biome.registerBiome(2, "desert", new BiomeDesert(new BiomeProperties("Desert").setBaseHeight(0.125f).setHeightVariation(0.05f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(3, "extreme_hills", new BiomeHills(BiomeHills.Type.NORMAL, new BiomeProperties("Extreme Hills").setBaseHeight(1.0f).setHeightVariation(0.5f).setTemperature(0.2f).setRainfall(0.3f)));
        Biome.registerBiome(4, "forest", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeProperties("Forest").setTemperature(0.7f).setRainfall(0.8f)));
        Biome.registerBiome(5, "taiga", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("Taiga").setBaseHeight(0.2f).setHeightVariation(0.2f).setTemperature(0.25f).setRainfall(0.8f)));
        Biome.registerBiome(6, "swampland", new BiomeSwamp(new BiomeProperties("Swampland").setBaseHeight(-0.2f).setHeightVariation(0.1f).setTemperature(0.8f).setRainfall(0.9f).setWaterColor(14745518)));
        Biome.registerBiome(7, "river", new BiomeRiver(new BiomeProperties("River").setBaseHeight(-0.5f).setHeightVariation(0.0f)));
        Biome.registerBiome(8, "hell", new BiomeHell(new BiomeProperties("Hell").setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(9, "sky", new BiomeEnd(new BiomeProperties("The End").setRainDisabled()));
        Biome.registerBiome(10, "frozen_ocean", new BiomeOcean(new BiomeProperties("FrozenOcean").setBaseHeight(-1.0f).setHeightVariation(0.1f).setTemperature(0.0f).setRainfall(0.5f).setSnowEnabled()));
        Biome.registerBiome(11, "frozen_river", new BiomeRiver(new BiomeProperties("FrozenRiver").setBaseHeight(-0.5f).setHeightVariation(0.0f).setTemperature(0.0f).setRainfall(0.5f).setSnowEnabled()));
        Biome.registerBiome(12, "ice_flats", new BiomeSnow(false, new BiomeProperties("Ice Plains").setBaseHeight(0.125f).setHeightVariation(0.05f).setTemperature(0.0f).setRainfall(0.5f).setSnowEnabled()));
        Biome.registerBiome(13, "ice_mountains", new BiomeSnow(false, new BiomeProperties("Ice Mountains").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(0.0f).setRainfall(0.5f).setSnowEnabled()));
        Biome.registerBiome(14, "mushroom_island", new BiomeMushroomIsland(new BiomeProperties("MushroomIsland").setBaseHeight(0.2f).setHeightVariation(0.3f).setTemperature(0.9f).setRainfall(1.0f)));
        Biome.registerBiome(15, "mushroom_island_shore", new BiomeMushroomIsland(new BiomeProperties("MushroomIslandShore").setBaseHeight(0.0f).setHeightVariation(0.025f).setTemperature(0.9f).setRainfall(1.0f)));
        Biome.registerBiome(16, "beaches", new BiomeBeach(new BiomeProperties("Beach").setBaseHeight(0.0f).setHeightVariation(0.025f).setTemperature(0.8f).setRainfall(0.4f)));
        Biome.registerBiome(17, "desert_hills", new BiomeDesert(new BiomeProperties("DesertHills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(18, "forest_hills", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeProperties("ForestHills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(0.7f).setRainfall(0.8f)));
        Biome.registerBiome(19, "taiga_hills", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("TaigaHills").setTemperature(0.25f).setRainfall(0.8f).setBaseHeight(0.45f).setHeightVariation(0.3f)));
        Biome.registerBiome(20, "smaller_extreme_hills", new BiomeHills(BiomeHills.Type.EXTRA_TREES, new BiomeProperties("Extreme Hills Edge").setBaseHeight(0.8f).setHeightVariation(0.3f).setTemperature(0.2f).setRainfall(0.3f)));
        Biome.registerBiome(21, "jungle", new BiomeJungle(false, new BiomeProperties("Jungle").setTemperature(0.95f).setRainfall(0.9f)));
        Biome.registerBiome(22, "jungle_hills", new BiomeJungle(false, new BiomeProperties("JungleHills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(0.95f).setRainfall(0.9f)));
        Biome.registerBiome(23, "jungle_edge", new BiomeJungle(true, new BiomeProperties("JungleEdge").setTemperature(0.95f).setRainfall(0.8f)));
        Biome.registerBiome(24, "deep_ocean", new BiomeOcean(new BiomeProperties("Deep Ocean").setBaseHeight(-1.8f).setHeightVariation(0.1f)));
        Biome.registerBiome(25, "stone_beach", new BiomeStoneBeach(new BiomeProperties("Stone Beach").setBaseHeight(0.1f).setHeightVariation(0.8f).setTemperature(0.2f).setRainfall(0.3f)));
        Biome.registerBiome(26, "cold_beach", new BiomeBeach(new BiomeProperties("Cold Beach").setBaseHeight(0.0f).setHeightVariation(0.025f).setTemperature(0.05f).setRainfall(0.3f).setSnowEnabled()));
        Biome.registerBiome(27, "birch_forest", new BiomeForest(BiomeForest.Type.BIRCH, new BiomeProperties("Birch Forest").setTemperature(0.6f).setRainfall(0.6f)));
        Biome.registerBiome(28, "birch_forest_hills", new BiomeForest(BiomeForest.Type.BIRCH, new BiomeProperties("Birch Forest Hills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(0.6f).setRainfall(0.6f)));
        Biome.registerBiome(29, "roofed_forest", new BiomeForest(BiomeForest.Type.ROOFED, new BiomeProperties("Roofed Forest").setTemperature(0.7f).setRainfall(0.8f)));
        Biome.registerBiome(30, "taiga_cold", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("Cold Taiga").setBaseHeight(0.2f).setHeightVariation(0.2f).setTemperature(-0.5f).setRainfall(0.4f).setSnowEnabled()));
        Biome.registerBiome(31, "taiga_cold_hills", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("Cold Taiga Hills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(-0.5f).setRainfall(0.4f).setSnowEnabled()));
        Biome.registerBiome(32, "redwood_taiga", new BiomeTaiga(BiomeTaiga.Type.MEGA, new BiomeProperties("Mega Taiga").setTemperature(0.3f).setRainfall(0.8f).setBaseHeight(0.2f).setHeightVariation(0.2f)));
        Biome.registerBiome(33, "redwood_taiga_hills", new BiomeTaiga(BiomeTaiga.Type.MEGA, new BiomeProperties("Mega Taiga Hills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(0.3f).setRainfall(0.8f)));
        Biome.registerBiome(34, "extreme_hills_with_trees", new BiomeHills(BiomeHills.Type.EXTRA_TREES, new BiomeProperties("Extreme Hills+").setBaseHeight(1.0f).setHeightVariation(0.5f).setTemperature(0.2f).setRainfall(0.3f)));
        Biome.registerBiome(35, "savanna", new BiomeSavanna(new BiomeProperties("Savanna").setBaseHeight(0.125f).setHeightVariation(0.05f).setTemperature(1.2f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(36, "savanna_rock", new BiomeSavanna(new BiomeProperties("Savanna Plateau").setBaseHeight(1.5f).setHeightVariation(0.025f).setTemperature(1.0f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(37, "mesa", new BiomeMesa(false, false, new BiomeProperties("Mesa").setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(38, "mesa_rock", new BiomeMesa(false, true, new BiomeProperties("Mesa Plateau F").setBaseHeight(1.5f).setHeightVariation(0.025f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(39, "mesa_clear_rock", new BiomeMesa(false, false, new BiomeProperties("Mesa Plateau").setBaseHeight(1.5f).setHeightVariation(0.025f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(127, "void", new BiomeVoid(new BiomeProperties("The Void").setRainDisabled()));
        Biome.registerBiome(129, "mutated_plains", new BiomePlains(true, new BiomeProperties("Sunflower Plains").setBaseBiome("plains").setBaseHeight(0.125f).setHeightVariation(0.05f).setTemperature(0.8f).setRainfall(0.4f)));
        Biome.registerBiome(130, "mutated_desert", new BiomeDesert(new BiomeProperties("Desert M").setBaseBiome("desert").setBaseHeight(0.225f).setHeightVariation(0.25f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(131, "mutated_extreme_hills", new BiomeHills(BiomeHills.Type.MUTATED, new BiomeProperties("Extreme Hills M").setBaseBiome("extreme_hills").setBaseHeight(1.0f).setHeightVariation(0.5f).setTemperature(0.2f).setRainfall(0.3f)));
        Biome.registerBiome(132, "mutated_forest", new BiomeForest(BiomeForest.Type.FLOWER, new BiomeProperties("Flower Forest").setBaseBiome("forest").setHeightVariation(0.4f).setTemperature(0.7f).setRainfall(0.8f)));
        Biome.registerBiome(133, "mutated_taiga", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("Taiga M").setBaseBiome("taiga").setBaseHeight(0.3f).setHeightVariation(0.4f).setTemperature(0.25f).setRainfall(0.8f)));
        Biome.registerBiome(134, "mutated_swampland", new BiomeSwamp(new BiomeProperties("Swampland M").setBaseBiome("swampland").setBaseHeight(-0.1f).setHeightVariation(0.3f).setTemperature(0.8f).setRainfall(0.9f).setWaterColor(14745518)));
        Biome.registerBiome(140, "mutated_ice_flats", new BiomeSnow(true, new BiomeProperties("Ice Plains Spikes").setBaseBiome("ice_flats").setBaseHeight(0.425f).setHeightVariation(0.45000002f).setTemperature(0.0f).setRainfall(0.5f).setSnowEnabled()));
        Biome.registerBiome(149, "mutated_jungle", new BiomeJungle(false, new BiomeProperties("Jungle M").setBaseBiome("jungle").setBaseHeight(0.2f).setHeightVariation(0.4f).setTemperature(0.95f).setRainfall(0.9f)));
        Biome.registerBiome(151, "mutated_jungle_edge", new BiomeJungle(true, new BiomeProperties("JungleEdge M").setBaseBiome("jungle_edge").setBaseHeight(0.2f).setHeightVariation(0.4f).setTemperature(0.95f).setRainfall(0.8f)));
        Biome.registerBiome(155, "mutated_birch_forest", new BiomeForestMutated(new BiomeProperties("Birch Forest M").setBaseBiome("birch_forest").setBaseHeight(0.2f).setHeightVariation(0.4f).setTemperature(0.6f).setRainfall(0.6f)));
        Biome.registerBiome(156, "mutated_birch_forest_hills", new BiomeForestMutated(new BiomeProperties("Birch Forest Hills M").setBaseBiome("birch_forest_hills").setBaseHeight(0.55f).setHeightVariation(0.5f).setTemperature(0.6f).setRainfall(0.6f)));
        Biome.registerBiome(157, "mutated_roofed_forest", new BiomeForest(BiomeForest.Type.ROOFED, new BiomeProperties("Roofed Forest M").setBaseBiome("roofed_forest").setBaseHeight(0.2f).setHeightVariation(0.4f).setTemperature(0.7f).setRainfall(0.8f)));
        Biome.registerBiome(158, "mutated_taiga_cold", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("Cold Taiga M").setBaseBiome("taiga_cold").setBaseHeight(0.3f).setHeightVariation(0.4f).setTemperature(-0.5f).setRainfall(0.4f).setSnowEnabled()));
        Biome.registerBiome(160, "mutated_redwood_taiga", new BiomeTaiga(BiomeTaiga.Type.MEGA_SPRUCE, new BiomeProperties("Mega Spruce Taiga").setBaseBiome("redwood_taiga").setBaseHeight(0.2f).setHeightVariation(0.2f).setTemperature(0.25f).setRainfall(0.8f)));
        Biome.registerBiome(161, "mutated_redwood_taiga_hills", new BiomeTaiga(BiomeTaiga.Type.MEGA_SPRUCE, new BiomeProperties("Redwood Taiga Hills M").setBaseBiome("redwood_taiga_hills").setBaseHeight(0.2f).setHeightVariation(0.2f).setTemperature(0.25f).setRainfall(0.8f)));
        Biome.registerBiome(162, "mutated_extreme_hills_with_trees", new BiomeHills(BiomeHills.Type.MUTATED, new BiomeProperties("Extreme Hills+ M").setBaseBiome("extreme_hills_with_trees").setBaseHeight(1.0f).setHeightVariation(0.5f).setTemperature(0.2f).setRainfall(0.3f)));
        Biome.registerBiome(163, "mutated_savanna", new BiomeSavannaMutated(new BiomeProperties("Savanna M").setBaseBiome("savanna").setBaseHeight(0.3625f).setHeightVariation(1.225f).setTemperature(1.1f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(164, "mutated_savanna_rock", new BiomeSavannaMutated(new BiomeProperties("Savanna Plateau M").setBaseBiome("savanna_rock").setBaseHeight(1.05f).setHeightVariation(1.2125001f).setTemperature(1.0f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(165, "mutated_mesa", new BiomeMesa(true, false, new BiomeProperties("Mesa (Bryce)").setBaseBiome("mesa").setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(166, "mutated_mesa_rock", new BiomeMesa(false, true, new BiomeProperties("Mesa Plateau F M").setBaseBiome("mesa_rock").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        Biome.registerBiome(167, "mutated_mesa_clear_rock", new BiomeMesa(false, false, new BiomeProperties("Mesa Plateau M").setBaseBiome("mesa_clear_rock").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
    }

    private static void registerBiome(int id, String name, Biome biome) {
        REGISTRY.register(id, new ResourceLocation(name), biome);
        if (biome.isMutation()) {
            MUTATION_TO_BASE_ID_MAP.put(biome, Biome.getIdForBiome(REGISTRY.getObject(new ResourceLocation(biome.baseBiomeRegName))));
        }
    }

    public static enum TempCategory {
        OCEAN,
        COLD,
        MEDIUM,
        WARM;

    }

    public static class SpawnListEntry
    extends WeightedRandom.Item {
        public Class<? extends EntityLiving> entityClass;
        public int minGroupCount;
        public int maxGroupCount;

        public SpawnListEntry(Class<? extends EntityLiving> entityclassIn, int weight, int groupCountMin, int groupCountMax) {
            super(weight);
            this.entityClass = entityclassIn;
            this.minGroupCount = groupCountMin;
            this.maxGroupCount = groupCountMax;
        }

        public String toString() {
            return this.entityClass.getSimpleName() + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):" + this.itemWeight;
        }
    }

    public static class BiomeProperties {
        private final String biomeName;
        private float baseHeight = 0.1f;
        private float heightVariation = 0.2f;
        private float temperature = 0.5f;
        private float rainfall = 0.5f;
        private int waterColor = 0xFFFFFF;
        private boolean enableSnow;
        private boolean enableRain = true;
        @Nullable
        private String baseBiomeRegName;

        public BiomeProperties(String nameIn) {
            this.biomeName = nameIn;
        }

        protected BiomeProperties setTemperature(float temperatureIn) {
            if (temperatureIn > 0.1f && temperatureIn < 0.2f) {
                throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
            }
            this.temperature = temperatureIn;
            return this;
        }

        protected BiomeProperties setRainfall(float rainfallIn) {
            this.rainfall = rainfallIn;
            return this;
        }

        protected BiomeProperties setBaseHeight(float baseHeightIn) {
            this.baseHeight = baseHeightIn;
            return this;
        }

        protected BiomeProperties setHeightVariation(float heightVariationIn) {
            this.heightVariation = heightVariationIn;
            return this;
        }

        protected BiomeProperties setRainDisabled() {
            this.enableRain = false;
            return this;
        }

        protected BiomeProperties setSnowEnabled() {
            this.enableSnow = true;
            return this;
        }

        protected BiomeProperties setWaterColor(int waterColorIn) {
            this.waterColor = waterColorIn;
            return this;
        }

        protected BiomeProperties setBaseBiome(String nameIn) {
            this.baseBiomeRegName = nameIn;
            return this;
        }
    }
}

