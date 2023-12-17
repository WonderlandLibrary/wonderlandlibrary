// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.util.WeightedRandom;
import org.apache.logging.log4j.LogManager;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import java.util.Collections;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.block.state.IBlockState;
import org.apache.logging.log4j.Logger;

public abstract class Biome
{
    private static final Logger LOGGER;
    protected static final IBlockState STONE;
    protected static final IBlockState AIR;
    protected static final IBlockState BEDROCK;
    protected static final IBlockState GRAVEL;
    protected static final IBlockState RED_SANDSTONE;
    protected static final IBlockState SANDSTONE;
    protected static final IBlockState ICE;
    protected static final IBlockState WATER;
    public static final ObjectIntIdentityMap<Biome> MUTATION_TO_BASE_ID_MAP;
    protected static final NoiseGeneratorPerlin TEMPERATURE_NOISE;
    protected static final NoiseGeneratorPerlin GRASS_COLOR_NOISE;
    protected static final WorldGenDoublePlant DOUBLE_PLANT_GENERATOR;
    protected static final WorldGenTrees TREE_FEATURE;
    protected static final WorldGenBigTree BIG_TREE_FEATURE;
    protected static final WorldGenSwamp SWAMP_FEATURE;
    public static final RegistryNamespaced<ResourceLocation, Biome> REGISTRY;
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
    public IBlockState topBlock;
    public IBlockState fillerBlock;
    public BiomeDecorator decorator;
    protected List<SpawnListEntry> spawnableMonsterList;
    protected List<SpawnListEntry> spawnableCreatureList;
    protected List<SpawnListEntry> spawnableWaterCreatureList;
    protected List<SpawnListEntry> spawnableCaveCreatureList;
    
    public static int getIdForBiome(final Biome biome) {
        return Biome.REGISTRY.getIDForObject(biome);
    }
    
    @Nullable
    public static Biome getBiomeForId(final int id) {
        return Biome.REGISTRY.getObjectById(id);
    }
    
    @Nullable
    public static Biome getMutationForBiome(final Biome biome) {
        return Biome.MUTATION_TO_BASE_ID_MAP.getByValue(getIdForBiome(biome));
    }
    
    protected Biome(final BiomeProperties properties) {
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.spawnableMonsterList = (List<SpawnListEntry>)Lists.newArrayList();
        this.spawnableCreatureList = (List<SpawnListEntry>)Lists.newArrayList();
        this.spawnableWaterCreatureList = (List<SpawnListEntry>)Lists.newArrayList();
        this.spawnableCaveCreatureList = (List<SpawnListEntry>)Lists.newArrayList();
        this.biomeName = properties.biomeName;
        this.baseHeight = properties.baseHeight;
        this.heightVariation = properties.heightVariation;
        this.temperature = properties.temperature;
        this.rainfall = properties.rainfall;
        this.waterColor = properties.waterColor;
        this.enableSnow = properties.enableSnow;
        this.enableRain = properties.enableRain;
        this.baseBiomeRegName = properties.baseBiomeRegName;
        this.decorator = this.createBiomeDecorator();
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
    
    public WorldGenAbstractTree getRandomTreeFeature(final Random rand) {
        return (rand.nextInt(10) == 0) ? Biome.BIG_TREE_FEATURE : Biome.TREE_FEATURE;
    }
    
    public WorldGenerator getRandomWorldGenForGrass(final Random rand) {
        return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }
    
    public BlockFlower.EnumFlowerType pickRandomFlower(final Random rand, final BlockPos pos) {
        return (rand.nextInt(3) > 0) ? BlockFlower.EnumFlowerType.DANDELION : BlockFlower.EnumFlowerType.POPPY;
    }
    
    public int getSkyColorByTemp(float currentTemperature) {
        currentTemperature /= 3.0f;
        currentTemperature = MathHelper.clamp(currentTemperature, -1.0f, 1.0f);
        return MathHelper.hsvToRGB(0.62222224f - currentTemperature * 0.05f, 0.5f + currentTemperature * 0.1f, 1.0f);
    }
    
    public List<SpawnListEntry> getSpawnableList(final EnumCreatureType creatureType) {
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
            default: {
                return Collections.emptyList();
            }
        }
    }
    
    public boolean getEnableSnow() {
        return this.isSnowyBiome();
    }
    
    public boolean canRain() {
        return !this.isSnowyBiome() && this.enableRain;
    }
    
    public boolean isHighHumidity() {
        return this.getRainfall() > 0.85f;
    }
    
    public float getSpawningChance() {
        return 0.1f;
    }
    
    public final float getTemperature(final BlockPos pos) {
        if (pos.getY() > 64) {
            final float f = (float)(Biome.TEMPERATURE_NOISE.getValue(pos.getX() / 8.0f, pos.getZ() / 8.0f) * 4.0);
            return this.getDefaultTemperature() - (f + pos.getY() - 64.0f) * 0.05f / 30.0f;
        }
        return this.getDefaultTemperature();
    }
    
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        this.decorator.decorate(worldIn, rand, this, pos);
    }
    
    public int getGrassColorAtPos(final BlockPos pos) {
        final double d0 = MathHelper.clamp(this.getTemperature(pos), 0.0f, 1.0f);
        final double d2 = MathHelper.clamp(this.getRainfall(), 0.0f, 1.0f);
        return ColorizerGrass.getGrassColor(d0, d2);
    }
    
    public int getFoliageColorAtPos(final BlockPos pos) {
        final double d0 = MathHelper.clamp(this.getTemperature(pos), 0.0f, 1.0f);
        final double d2 = MathHelper.clamp(this.getRainfall(), 0.0f, 1.0f);
        return ColorizerFoliage.getFoliageColor(d0, d2);
    }
    
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int x, final int z, final double noiseVal) {
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
    
    public final void generateBiomeTerrain(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int x, final int z, final double noiseVal) {
        final int i = worldIn.getSeaLevel();
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate2 = this.fillerBlock;
        int j = -1;
        final int k = (int)(noiseVal / 3.0 + 3.0 + rand.nextDouble() * 0.25);
        final int l = x & 0xF;
        final int i2 = z & 0xF;
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j2 = 255; j2 >= 0; --j2) {
            if (j2 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(i2, j2, l, Biome.BEDROCK);
            }
            else {
                final IBlockState iblockstate3 = chunkPrimerIn.getBlockState(i2, j2, l);
                if (iblockstate3.getMaterial() == Material.AIR) {
                    j = -1;
                }
                else if (iblockstate3.getBlock() == Blocks.STONE) {
                    if (j == -1) {
                        if (k <= 0) {
                            iblockstate = Biome.AIR;
                            iblockstate2 = Biome.STONE;
                        }
                        else if (j2 >= i - 4 && j2 <= i + 1) {
                            iblockstate = this.topBlock;
                            iblockstate2 = this.fillerBlock;
                        }
                        if (j2 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                            if (this.getTemperature(blockpos$mutableblockpos.setPos(x, j2, z)) < 0.15f) {
                                iblockstate = Biome.ICE;
                            }
                            else {
                                iblockstate = Biome.WATER;
                            }
                        }
                        j = k;
                        if (j2 >= i - 1) {
                            chunkPrimerIn.setBlockState(i2, j2, l, iblockstate);
                        }
                        else if (j2 < i - 7 - k) {
                            iblockstate = Biome.AIR;
                            iblockstate2 = Biome.STONE;
                            chunkPrimerIn.setBlockState(i2, j2, l, Biome.GRAVEL);
                        }
                        else {
                            chunkPrimerIn.setBlockState(i2, j2, l, iblockstate2);
                        }
                    }
                    else if (j > 0) {
                        --j;
                        chunkPrimerIn.setBlockState(i2, j2, l, iblockstate2);
                        if (j == 0 && iblockstate2.getBlock() == Blocks.SAND && k > 1) {
                            j = rand.nextInt(4) + Math.max(0, j2 - 63);
                            iblockstate2 = ((iblockstate2.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND) ? Biome.RED_SANDSTONE : Biome.SANDSTONE);
                        }
                    }
                }
            }
        }
    }
    
    public Class<? extends Biome> getBiomeClass() {
        return this.getClass();
    }
    
    public TempCategory getTempCategory() {
        if (this.getDefaultTemperature() < 0.2) {
            return TempCategory.COLD;
        }
        return (this.getDefaultTemperature() < 1.0) ? TempCategory.MEDIUM : TempCategory.WARM;
    }
    
    @Nullable
    public static Biome getBiome(final int id) {
        return getBiome(id, null);
    }
    
    public static Biome getBiome(final int biomeId, final Biome fallback) {
        final Biome biome = getBiomeForId(biomeId);
        return (biome == null) ? fallback : biome;
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
    
    public final float getDefaultTemperature() {
        return this.temperature;
    }
    
    public final int getWaterColor() {
        return this.waterColor;
    }
    
    public final boolean isSnowyBiome() {
        return this.enableSnow;
    }
    
    public static void registerBiomes() {
        registerBiome(0, "ocean", new BiomeOcean(new BiomeProperties("Ocean").setBaseHeight(-1.0f).setHeightVariation(0.1f)));
        registerBiome(1, "plains", new BiomePlains(false, new BiomeProperties("Plains").setBaseHeight(0.125f).setHeightVariation(0.05f).setTemperature(0.8f).setRainfall(0.4f)));
        registerBiome(2, "desert", new BiomeDesert(new BiomeProperties("Desert").setBaseHeight(0.125f).setHeightVariation(0.05f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(3, "extreme_hills", new BiomeHills(BiomeHills.Type.NORMAL, new BiomeProperties("Extreme Hills").setBaseHeight(1.0f).setHeightVariation(0.5f).setTemperature(0.2f).setRainfall(0.3f)));
        registerBiome(4, "forest", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeProperties("Forest").setTemperature(0.7f).setRainfall(0.8f)));
        registerBiome(5, "taiga", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("Taiga").setBaseHeight(0.2f).setHeightVariation(0.2f).setTemperature(0.25f).setRainfall(0.8f)));
        registerBiome(6, "swampland", new BiomeSwamp(new BiomeProperties("Swampland").setBaseHeight(-0.2f).setHeightVariation(0.1f).setTemperature(0.8f).setRainfall(0.9f).setWaterColor(14745518)));
        registerBiome(7, "river", new BiomeRiver(new BiomeProperties("River").setBaseHeight(-0.5f).setHeightVariation(0.0f)));
        registerBiome(8, "hell", new BiomeHell(new BiomeProperties("Hell").setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(9, "sky", new BiomeEnd(new BiomeProperties("The End").setRainDisabled()));
        registerBiome(10, "frozen_ocean", new BiomeOcean(new BiomeProperties("FrozenOcean").setBaseHeight(-1.0f).setHeightVariation(0.1f).setTemperature(0.0f).setRainfall(0.5f).setSnowEnabled()));
        registerBiome(11, "frozen_river", new BiomeRiver(new BiomeProperties("FrozenRiver").setBaseHeight(-0.5f).setHeightVariation(0.0f).setTemperature(0.0f).setRainfall(0.5f).setSnowEnabled()));
        registerBiome(12, "ice_flats", new BiomeSnow(false, new BiomeProperties("Ice Plains").setBaseHeight(0.125f).setHeightVariation(0.05f).setTemperature(0.0f).setRainfall(0.5f).setSnowEnabled()));
        registerBiome(13, "ice_mountains", new BiomeSnow(false, new BiomeProperties("Ice Mountains").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(0.0f).setRainfall(0.5f).setSnowEnabled()));
        registerBiome(14, "mushroom_island", new BiomeMushroomIsland(new BiomeProperties("MushroomIsland").setBaseHeight(0.2f).setHeightVariation(0.3f).setTemperature(0.9f).setRainfall(1.0f)));
        registerBiome(15, "mushroom_island_shore", new BiomeMushroomIsland(new BiomeProperties("MushroomIslandShore").setBaseHeight(0.0f).setHeightVariation(0.025f).setTemperature(0.9f).setRainfall(1.0f)));
        registerBiome(16, "beaches", new BiomeBeach(new BiomeProperties("Beach").setBaseHeight(0.0f).setHeightVariation(0.025f).setTemperature(0.8f).setRainfall(0.4f)));
        registerBiome(17, "desert_hills", new BiomeDesert(new BiomeProperties("DesertHills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(18, "forest_hills", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeProperties("ForestHills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(0.7f).setRainfall(0.8f)));
        registerBiome(19, "taiga_hills", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("TaigaHills").setTemperature(0.25f).setRainfall(0.8f).setBaseHeight(0.45f).setHeightVariation(0.3f)));
        registerBiome(20, "smaller_extreme_hills", new BiomeHills(BiomeHills.Type.EXTRA_TREES, new BiomeProperties("Extreme Hills Edge").setBaseHeight(0.8f).setHeightVariation(0.3f).setTemperature(0.2f).setRainfall(0.3f)));
        registerBiome(21, "jungle", new BiomeJungle(false, new BiomeProperties("Jungle").setTemperature(0.95f).setRainfall(0.9f)));
        registerBiome(22, "jungle_hills", new BiomeJungle(false, new BiomeProperties("JungleHills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(0.95f).setRainfall(0.9f)));
        registerBiome(23, "jungle_edge", new BiomeJungle(true, new BiomeProperties("JungleEdge").setTemperature(0.95f).setRainfall(0.8f)));
        registerBiome(24, "deep_ocean", new BiomeOcean(new BiomeProperties("Deep Ocean").setBaseHeight(-1.8f).setHeightVariation(0.1f)));
        registerBiome(25, "stone_beach", new BiomeStoneBeach(new BiomeProperties("Stone Beach").setBaseHeight(0.1f).setHeightVariation(0.8f).setTemperature(0.2f).setRainfall(0.3f)));
        registerBiome(26, "cold_beach", new BiomeBeach(new BiomeProperties("Cold Beach").setBaseHeight(0.0f).setHeightVariation(0.025f).setTemperature(0.05f).setRainfall(0.3f).setSnowEnabled()));
        registerBiome(27, "birch_forest", new BiomeForest(BiomeForest.Type.BIRCH, new BiomeProperties("Birch Forest").setTemperature(0.6f).setRainfall(0.6f)));
        registerBiome(28, "birch_forest_hills", new BiomeForest(BiomeForest.Type.BIRCH, new BiomeProperties("Birch Forest Hills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(0.6f).setRainfall(0.6f)));
        registerBiome(29, "roofed_forest", new BiomeForest(BiomeForest.Type.ROOFED, new BiomeProperties("Roofed Forest").setTemperature(0.7f).setRainfall(0.8f)));
        registerBiome(30, "taiga_cold", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("Cold Taiga").setBaseHeight(0.2f).setHeightVariation(0.2f).setTemperature(-0.5f).setRainfall(0.4f).setSnowEnabled()));
        registerBiome(31, "taiga_cold_hills", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("Cold Taiga Hills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(-0.5f).setRainfall(0.4f).setSnowEnabled()));
        registerBiome(32, "redwood_taiga", new BiomeTaiga(BiomeTaiga.Type.MEGA, new BiomeProperties("Mega Taiga").setTemperature(0.3f).setRainfall(0.8f).setBaseHeight(0.2f).setHeightVariation(0.2f)));
        registerBiome(33, "redwood_taiga_hills", new BiomeTaiga(BiomeTaiga.Type.MEGA, new BiomeProperties("Mega Taiga Hills").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(0.3f).setRainfall(0.8f)));
        registerBiome(34, "extreme_hills_with_trees", new BiomeHills(BiomeHills.Type.EXTRA_TREES, new BiomeProperties("Extreme Hills+").setBaseHeight(1.0f).setHeightVariation(0.5f).setTemperature(0.2f).setRainfall(0.3f)));
        registerBiome(35, "savanna", new BiomeSavanna(new BiomeProperties("Savanna").setBaseHeight(0.125f).setHeightVariation(0.05f).setTemperature(1.2f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(36, "savanna_rock", new BiomeSavanna(new BiomeProperties("Savanna Plateau").setBaseHeight(1.5f).setHeightVariation(0.025f).setTemperature(1.0f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(37, "mesa", new BiomeMesa(false, false, new BiomeProperties("Mesa").setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(38, "mesa_rock", new BiomeMesa(false, true, new BiomeProperties("Mesa Plateau F").setBaseHeight(1.5f).setHeightVariation(0.025f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(39, "mesa_clear_rock", new BiomeMesa(false, false, new BiomeProperties("Mesa Plateau").setBaseHeight(1.5f).setHeightVariation(0.025f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(127, "void", new BiomeVoid(new BiomeProperties("The Void").setRainDisabled()));
        registerBiome(129, "mutated_plains", new BiomePlains(true, new BiomeProperties("Sunflower Plains").setBaseBiome("plains").setBaseHeight(0.125f).setHeightVariation(0.05f).setTemperature(0.8f).setRainfall(0.4f)));
        registerBiome(130, "mutated_desert", new BiomeDesert(new BiomeProperties("Desert M").setBaseBiome("desert").setBaseHeight(0.225f).setHeightVariation(0.25f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(131, "mutated_extreme_hills", new BiomeHills(BiomeHills.Type.MUTATED, new BiomeProperties("Extreme Hills M").setBaseBiome("extreme_hills").setBaseHeight(1.0f).setHeightVariation(0.5f).setTemperature(0.2f).setRainfall(0.3f)));
        registerBiome(132, "mutated_forest", new BiomeForest(BiomeForest.Type.FLOWER, new BiomeProperties("Flower Forest").setBaseBiome("forest").setHeightVariation(0.4f).setTemperature(0.7f).setRainfall(0.8f)));
        registerBiome(133, "mutated_taiga", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("Taiga M").setBaseBiome("taiga").setBaseHeight(0.3f).setHeightVariation(0.4f).setTemperature(0.25f).setRainfall(0.8f)));
        registerBiome(134, "mutated_swampland", new BiomeSwamp(new BiomeProperties("Swampland M").setBaseBiome("swampland").setBaseHeight(-0.1f).setHeightVariation(0.3f).setTemperature(0.8f).setRainfall(0.9f).setWaterColor(14745518)));
        registerBiome(140, "mutated_ice_flats", new BiomeSnow(true, new BiomeProperties("Ice Plains Spikes").setBaseBiome("ice_flats").setBaseHeight(0.425f).setHeightVariation(0.45000002f).setTemperature(0.0f).setRainfall(0.5f).setSnowEnabled()));
        registerBiome(149, "mutated_jungle", new BiomeJungle(false, new BiomeProperties("Jungle M").setBaseBiome("jungle").setBaseHeight(0.2f).setHeightVariation(0.4f).setTemperature(0.95f).setRainfall(0.9f)));
        registerBiome(151, "mutated_jungle_edge", new BiomeJungle(true, new BiomeProperties("JungleEdge M").setBaseBiome("jungle_edge").setBaseHeight(0.2f).setHeightVariation(0.4f).setTemperature(0.95f).setRainfall(0.8f)));
        registerBiome(155, "mutated_birch_forest", new BiomeForestMutated(new BiomeProperties("Birch Forest M").setBaseBiome("birch_forest").setBaseHeight(0.2f).setHeightVariation(0.4f).setTemperature(0.6f).setRainfall(0.6f)));
        registerBiome(156, "mutated_birch_forest_hills", new BiomeForestMutated(new BiomeProperties("Birch Forest Hills M").setBaseBiome("birch_forest_hills").setBaseHeight(0.55f).setHeightVariation(0.5f).setTemperature(0.6f).setRainfall(0.6f)));
        registerBiome(157, "mutated_roofed_forest", new BiomeForest(BiomeForest.Type.ROOFED, new BiomeProperties("Roofed Forest M").setBaseBiome("roofed_forest").setBaseHeight(0.2f).setHeightVariation(0.4f).setTemperature(0.7f).setRainfall(0.8f)));
        registerBiome(158, "mutated_taiga_cold", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeProperties("Cold Taiga M").setBaseBiome("taiga_cold").setBaseHeight(0.3f).setHeightVariation(0.4f).setTemperature(-0.5f).setRainfall(0.4f).setSnowEnabled()));
        registerBiome(160, "mutated_redwood_taiga", new BiomeTaiga(BiomeTaiga.Type.MEGA_SPRUCE, new BiomeProperties("Mega Spruce Taiga").setBaseBiome("redwood_taiga").setBaseHeight(0.2f).setHeightVariation(0.2f).setTemperature(0.25f).setRainfall(0.8f)));
        registerBiome(161, "mutated_redwood_taiga_hills", new BiomeTaiga(BiomeTaiga.Type.MEGA_SPRUCE, new BiomeProperties("Redwood Taiga Hills M").setBaseBiome("redwood_taiga_hills").setBaseHeight(0.2f).setHeightVariation(0.2f).setTemperature(0.25f).setRainfall(0.8f)));
        registerBiome(162, "mutated_extreme_hills_with_trees", new BiomeHills(BiomeHills.Type.MUTATED, new BiomeProperties("Extreme Hills+ M").setBaseBiome("extreme_hills_with_trees").setBaseHeight(1.0f).setHeightVariation(0.5f).setTemperature(0.2f).setRainfall(0.3f)));
        registerBiome(163, "mutated_savanna", new BiomeSavannaMutated(new BiomeProperties("Savanna M").setBaseBiome("savanna").setBaseHeight(0.3625f).setHeightVariation(1.225f).setTemperature(1.1f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(164, "mutated_savanna_rock", new BiomeSavannaMutated(new BiomeProperties("Savanna Plateau M").setBaseBiome("savanna_rock").setBaseHeight(1.05f).setHeightVariation(1.2125001f).setTemperature(1.0f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(165, "mutated_mesa", new BiomeMesa(true, false, new BiomeProperties("Mesa (Bryce)").setBaseBiome("mesa").setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(166, "mutated_mesa_rock", new BiomeMesa(false, true, new BiomeProperties("Mesa Plateau F M").setBaseBiome("mesa_rock").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
        registerBiome(167, "mutated_mesa_clear_rock", new BiomeMesa(false, false, new BiomeProperties("Mesa Plateau M").setBaseBiome("mesa_clear_rock").setBaseHeight(0.45f).setHeightVariation(0.3f).setTemperature(2.0f).setRainfall(0.0f).setRainDisabled()));
    }
    
    private static void registerBiome(final int id, final String name, final Biome biome) {
        Biome.REGISTRY.register(id, new ResourceLocation(name), biome);
        if (biome.isMutation()) {
            Biome.MUTATION_TO_BASE_ID_MAP.put(biome, getIdForBiome(Biome.REGISTRY.getObject(new ResourceLocation(biome.baseBiomeRegName))));
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        STONE = Blocks.STONE.getDefaultState();
        AIR = Blocks.AIR.getDefaultState();
        BEDROCK = Blocks.BEDROCK.getDefaultState();
        GRAVEL = Blocks.GRAVEL.getDefaultState();
        RED_SANDSTONE = Blocks.RED_SANDSTONE.getDefaultState();
        SANDSTONE = Blocks.SANDSTONE.getDefaultState();
        ICE = Blocks.ICE.getDefaultState();
        WATER = Blocks.WATER.getDefaultState();
        MUTATION_TO_BASE_ID_MAP = new ObjectIntIdentityMap<Biome>();
        TEMPERATURE_NOISE = new NoiseGeneratorPerlin(new Random(1234L), 1);
        GRASS_COLOR_NOISE = new NoiseGeneratorPerlin(new Random(2345L), 1);
        DOUBLE_PLANT_GENERATOR = new WorldGenDoublePlant();
        TREE_FEATURE = new WorldGenTrees(false);
        BIG_TREE_FEATURE = new WorldGenBigTree(false);
        SWAMP_FEATURE = new WorldGenSwamp();
        REGISTRY = new RegistryNamespaced<ResourceLocation, Biome>();
    }
    
    public static class BiomeProperties
    {
        private final String biomeName;
        private float baseHeight;
        private float heightVariation;
        private float temperature;
        private float rainfall;
        private int waterColor;
        private boolean enableSnow;
        private boolean enableRain;
        @Nullable
        private String baseBiomeRegName;
        
        public BiomeProperties(final String nameIn) {
            this.baseHeight = 0.1f;
            this.heightVariation = 0.2f;
            this.temperature = 0.5f;
            this.rainfall = 0.5f;
            this.waterColor = 16777215;
            this.enableRain = true;
            this.biomeName = nameIn;
        }
        
        protected BiomeProperties setTemperature(final float temperatureIn) {
            if (temperatureIn > 0.1f && temperatureIn < 0.2f) {
                throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
            }
            this.temperature = temperatureIn;
            return this;
        }
        
        protected BiomeProperties setRainfall(final float rainfallIn) {
            this.rainfall = rainfallIn;
            return this;
        }
        
        protected BiomeProperties setBaseHeight(final float baseHeightIn) {
            this.baseHeight = baseHeightIn;
            return this;
        }
        
        protected BiomeProperties setHeightVariation(final float heightVariationIn) {
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
        
        protected BiomeProperties setWaterColor(final int waterColorIn) {
            this.waterColor = waterColorIn;
            return this;
        }
        
        protected BiomeProperties setBaseBiome(final String nameIn) {
            this.baseBiomeRegName = nameIn;
            return this;
        }
    }
    
    public static class SpawnListEntry extends WeightedRandom.Item
    {
        public Class<? extends EntityLiving> entityClass;
        public int minGroupCount;
        public int maxGroupCount;
        
        public SpawnListEntry(final Class<? extends EntityLiving> entityclassIn, final int weight, final int groupCountMin, final int groupCountMax) {
            super(weight);
            this.entityClass = entityclassIn;
            this.minGroupCount = groupCountMin;
            this.maxGroupCount = groupCountMax;
        }
        
        @Override
        public String toString() {
            return this.entityClass.getSimpleName() + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):" + this.itemWeight;
        }
    }
    
    public enum TempCategory
    {
        OCEAN, 
        COLD, 
        MEDIUM, 
        WARM;
    }
}
