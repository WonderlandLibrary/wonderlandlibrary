// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTaiga1;

public class BiomeGenTaiga extends BiomeGenBase
{
    private static final WorldGenTaiga1 field_150639_aC;
    private static final WorldGenTaiga2 field_150640_aD;
    private static final WorldGenMegaPineTree field_150641_aE;
    private static final WorldGenMegaPineTree field_150642_aF;
    private static final WorldGenBlockBlob field_150643_aG;
    private int field_150644_aH;
    private static final String __OBFID = "CL_00000186";
    
    public BiomeGenTaiga(final int p_i45385_1_, final int p_i45385_2_) {
        super(p_i45385_1_);
        this.field_150644_aH = p_i45385_2_;
        this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 8, 4, 4));
        this.theBiomeDecorator.treesPerChunk = 10;
        if (p_i45385_2_ != 1 && p_i45385_2_ != 2) {
            this.theBiomeDecorator.grassPerChunk = 1;
            this.theBiomeDecorator.mushroomsPerChunk = 1;
        }
        else {
            this.theBiomeDecorator.grassPerChunk = 7;
            this.theBiomeDecorator.deadBushPerChunk = 1;
            this.theBiomeDecorator.mushroomsPerChunk = 3;
        }
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random p_150567_1_) {
        return ((this.field_150644_aH == 1 || this.field_150644_aH == 2) && p_150567_1_.nextInt(3) == 0) ? ((this.field_150644_aH != 2 && p_150567_1_.nextInt(13) != 0) ? BiomeGenTaiga.field_150641_aE : BiomeGenTaiga.field_150642_aF) : ((p_150567_1_.nextInt(3) == 0) ? BiomeGenTaiga.field_150639_aC : BiomeGenTaiga.field_150640_aD);
    }
    
    @Override
    public WorldGenerator getRandomWorldGenForGrass(final Random p_76730_1_) {
        return (p_76730_1_.nextInt(5) > 0) ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }
    
    @Override
    public void func_180624_a(final World worldIn, final Random p_180624_2_, final BlockPos p_180624_3_) {
        if (this.field_150644_aH == 1 || this.field_150644_aH == 2) {
            for (int var4 = p_180624_2_.nextInt(3), var5 = 0; var5 < var4; ++var5) {
                final int var6 = p_180624_2_.nextInt(16) + 8;
                final int var7 = p_180624_2_.nextInt(16) + 8;
                final BlockPos var8 = worldIn.getHorizon(p_180624_3_.add(var6, 0, var7));
                BiomeGenTaiga.field_150643_aG.generate(worldIn, p_180624_2_, var8);
            }
        }
        BiomeGenTaiga.field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.FERN);
        for (int var4 = 0; var4 < 7; ++var4) {
            final int var5 = p_180624_2_.nextInt(16) + 8;
            final int var6 = p_180624_2_.nextInt(16) + 8;
            final int var7 = p_180624_2_.nextInt(worldIn.getHorizon(p_180624_3_.add(var5, 0, var6)).getY() + 32);
            BiomeGenTaiga.field_180280_ag.generate(worldIn, p_180624_2_, p_180624_3_.add(var5, var7, var6));
        }
        super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random p_180622_2_, final ChunkPrimer p_180622_3_, final int p_180622_4_, final int p_180622_5_, final double p_180622_6_) {
        if (this.field_150644_aH == 1 || this.field_150644_aH == 2) {
            this.topBlock = Blocks.grass.getDefaultState();
            this.fillerBlock = Blocks.dirt.getDefaultState();
            if (p_180622_6_ > 1.75) {
                this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
            }
            else if (p_180622_6_ > -0.95) {
                this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
            }
        }
        this.func_180628_b(worldIn, p_180622_2_, p_180622_3_, p_180622_4_, p_180622_5_, p_180622_6_);
    }
    
    @Override
    protected BiomeGenBase createMutatedBiome(final int p_180277_1_) {
        return (this.biomeID == BiomeGenBase.megaTaiga.biomeID) ? new BiomeGenTaiga(p_180277_1_, 2).func_150557_a(5858897, true).setBiomeName("Mega Spruce Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25f, 0.8f).setHeight(new Height(this.minHeight, this.maxHeight)) : super.createMutatedBiome(p_180277_1_);
    }
    
    static {
        field_150639_aC = new WorldGenTaiga1();
        field_150640_aD = new WorldGenTaiga2(false);
        field_150641_aE = new WorldGenMegaPineTree(false, false);
        field_150642_aF = new WorldGenMegaPineTree(false, true);
        field_150643_aG = new WorldGenBlockBlob(Blocks.mossy_cobblestone, 0);
    }
}
