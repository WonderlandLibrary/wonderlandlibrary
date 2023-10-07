// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStone;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.GeneratorBushFeature;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class BiomeDecorator
{
    protected World currentWorld;
    protected Random randomGenerator;
    protected BlockPos field_180294_c;
    protected ChunkProviderSettings field_180293_d;
    protected WorldGenerator clayGen;
    protected WorldGenerator sandGen;
    protected WorldGenerator gravelAsSandGen;
    protected WorldGenerator dirtGen;
    protected WorldGenerator gravelGen;
    protected WorldGenerator field_180296_j;
    protected WorldGenerator field_180297_k;
    protected WorldGenerator field_180295_l;
    protected WorldGenerator coalGen;
    protected WorldGenerator ironGen;
    protected WorldGenerator goldGen;
    protected WorldGenerator field_180299_p;
    protected WorldGenerator field_180298_q;
    protected WorldGenerator lapisGen;
    protected WorldGenFlowers yellowFlowerGen;
    protected WorldGenerator mushroomBrownGen;
    protected WorldGenerator mushroomRedGen;
    protected WorldGenerator bigMushroomGen;
    protected WorldGenerator reedGen;
    protected WorldGenerator cactusGen;
    protected WorldGenerator waterlilyGen;
    protected int waterlilyPerChunk;
    protected int treesPerChunk;
    protected int flowersPerChunk;
    protected int grassPerChunk;
    protected int deadBushPerChunk;
    protected int mushroomsPerChunk;
    protected int reedsPerChunk;
    protected int cactiPerChunk;
    protected int sandPerChunk;
    protected int sandPerChunk2;
    protected int clayPerChunk;
    protected int bigMushroomsPerChunk;
    public boolean generateLakes;
    private static final String __OBFID = "CL_00000164";
    
    public BiomeDecorator() {
        this.clayGen = new WorldGenClay(4);
        this.sandGen = new WorldGenSand(Blocks.sand, 7);
        this.gravelAsSandGen = new WorldGenSand(Blocks.gravel, 6);
        this.yellowFlowerGen = new WorldGenFlowers(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION);
        this.mushroomBrownGen = new GeneratorBushFeature(Blocks.brown_mushroom);
        this.mushroomRedGen = new GeneratorBushFeature(Blocks.red_mushroom);
        this.bigMushroomGen = new WorldGenBigMushroom();
        this.reedGen = new WorldGenReed();
        this.cactusGen = new WorldGenCactus();
        this.waterlilyGen = new WorldGenWaterlily();
        this.flowersPerChunk = 2;
        this.grassPerChunk = 1;
        this.sandPerChunk = 1;
        this.sandPerChunk2 = 3;
        this.clayPerChunk = 1;
        this.generateLakes = true;
    }
    
    public void func_180292_a(final World worldIn, final Random p_180292_2_, final BiomeGenBase p_180292_3_, final BlockPos p_180292_4_) {
        if (this.currentWorld != null) {
            throw new RuntimeException("Already decorating");
        }
        this.currentWorld = worldIn;
        final String var5 = worldIn.getWorldInfo().getGeneratorOptions();
        if (var5 != null) {
            this.field_180293_d = ChunkProviderSettings.Factory.createFactory(var5).func_177864_b();
        }
        else {
            this.field_180293_d = ChunkProviderSettings.Factory.createFactory("").func_177864_b();
        }
        this.randomGenerator = p_180292_2_;
        this.field_180294_c = p_180292_4_;
        this.dirtGen = new WorldGenMinable(Blocks.dirt.getDefaultState(), this.field_180293_d.field_177789_I);
        this.gravelGen = new WorldGenMinable(Blocks.gravel.getDefaultState(), this.field_180293_d.field_177785_M);
        this.field_180296_j = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT_PROP, BlockStone.EnumType.GRANITE), this.field_180293_d.field_177796_Q);
        this.field_180297_k = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT_PROP, BlockStone.EnumType.DIORITE), this.field_180293_d.field_177792_U);
        this.field_180295_l = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT_PROP, BlockStone.EnumType.ANDESITE), this.field_180293_d.field_177800_Y);
        this.coalGen = new WorldGenMinable(Blocks.coal_ore.getDefaultState(), this.field_180293_d.field_177844_ac);
        this.ironGen = new WorldGenMinable(Blocks.iron_ore.getDefaultState(), this.field_180293_d.field_177848_ag);
        this.goldGen = new WorldGenMinable(Blocks.gold_ore.getDefaultState(), this.field_180293_d.field_177828_ak);
        this.field_180299_p = new WorldGenMinable(Blocks.redstone_ore.getDefaultState(), this.field_180293_d.field_177836_ao);
        this.field_180298_q = new WorldGenMinable(Blocks.diamond_ore.getDefaultState(), this.field_180293_d.field_177814_as);
        this.lapisGen = new WorldGenMinable(Blocks.lapis_ore.getDefaultState(), this.field_180293_d.field_177822_aw);
        this.genDecorations(p_180292_3_);
        this.currentWorld = null;
        this.randomGenerator = null;
    }
    
    protected void genDecorations(final BiomeGenBase p_150513_1_) {
        this.generateOres();
        for (int var2 = 0; var2 < this.sandPerChunk2; ++var2) {
            final int var3 = this.randomGenerator.nextInt(16) + 8;
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            this.sandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.func_175672_r(this.field_180294_c.add(var3, 0, var4)));
        }
        for (int var2 = 0; var2 < this.clayPerChunk; ++var2) {
            final int var3 = this.randomGenerator.nextInt(16) + 8;
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            this.clayGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.func_175672_r(this.field_180294_c.add(var3, 0, var4)));
        }
        for (int var2 = 0; var2 < this.sandPerChunk; ++var2) {
            final int var3 = this.randomGenerator.nextInt(16) + 8;
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            this.gravelAsSandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.func_175672_r(this.field_180294_c.add(var3, 0, var4)));
        }
        int var2 = this.treesPerChunk;
        if (this.randomGenerator.nextInt(10) == 0) {
            ++var2;
        }
        for (int var3 = 0; var3 < var2; ++var3) {
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(16) + 8;
            final WorldGenAbstractTree var6 = p_150513_1_.genBigTreeChance(this.randomGenerator);
            var6.func_175904_e();
            final BlockPos var7 = this.currentWorld.getHorizon(this.field_180294_c.add(var4, 0, var5));
            if (var6.generate(this.currentWorld, this.randomGenerator, var7)) {
                var6.func_180711_a(this.currentWorld, this.randomGenerator, var7);
            }
        }
        for (int var3 = 0; var3 < this.bigMushroomsPerChunk; ++var3) {
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(16) + 8;
            this.bigMushroomGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getHorizon(this.field_180294_c.add(var4, 0, var5)));
        }
        for (int var3 = 0; var3 < this.flowersPerChunk; ++var3) {
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(16) + 8;
            final int var8 = this.randomGenerator.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(var4, 0, var5)).getY() + 32);
            final BlockPos var7 = this.field_180294_c.add(var4, var8, var5);
            final BlockFlower.EnumFlowerType var9 = p_150513_1_.pickRandomFlower(this.randomGenerator, var7);
            final BlockFlower var10 = var9.func_176964_a().func_180346_a();
            if (var10.getMaterial() != Material.air) {
                this.yellowFlowerGen.setGeneratedBlock(var10, var9);
                this.yellowFlowerGen.generate(this.currentWorld, this.randomGenerator, var7);
            }
        }
        for (int var3 = 0; var3 < this.grassPerChunk; ++var3) {
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(16) + 8;
            final int var8 = this.randomGenerator.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            p_150513_1_.getRandomWorldGenForGrass(this.randomGenerator).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var8, var5));
        }
        for (int var3 = 0; var3 < this.deadBushPerChunk; ++var3) {
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(16) + 8;
            final int var8 = this.randomGenerator.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            new WorldGenDeadBush().generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var8, var5));
        }
        for (int var3 = 0; var3 < this.waterlilyPerChunk; ++var3) {
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(16) + 8;
            final int var8 = this.randomGenerator.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            BlockPos var7;
            BlockPos var11;
            for (var7 = this.field_180294_c.add(var4, var8, var5); var7.getY() > 0; var7 = var11) {
                var11 = var7.offsetDown();
                if (!this.currentWorld.isAirBlock(var11)) {
                    break;
                }
            }
            this.waterlilyGen.generate(this.currentWorld, this.randomGenerator, var7);
        }
        for (int var3 = 0; var3 < this.mushroomsPerChunk; ++var3) {
            if (this.randomGenerator.nextInt(4) == 0) {
                final int var4 = this.randomGenerator.nextInt(16) + 8;
                final int var5 = this.randomGenerator.nextInt(16) + 8;
                final BlockPos var12 = this.currentWorld.getHorizon(this.field_180294_c.add(var4, 0, var5));
                this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, var12);
            }
            if (this.randomGenerator.nextInt(8) == 0) {
                final int var4 = this.randomGenerator.nextInt(16) + 8;
                final int var5 = this.randomGenerator.nextInt(16) + 8;
                final int var8 = this.randomGenerator.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
                final BlockPos var7 = this.field_180294_c.add(var4, var8, var5);
                this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, var7);
            }
        }
        if (this.randomGenerator.nextInt(4) == 0) {
            final int var3 = this.randomGenerator.nextInt(16) + 8;
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(var3, 0, var4)).getY() * 2);
            this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var3, var5, var4));
        }
        if (this.randomGenerator.nextInt(8) == 0) {
            final int var3 = this.randomGenerator.nextInt(16) + 8;
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(var3, 0, var4)).getY() * 2);
            this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var3, var5, var4));
        }
        for (int var3 = 0; var3 < this.reedsPerChunk; ++var3) {
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(16) + 8;
            final int var8 = this.randomGenerator.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var8, var5));
        }
        for (int var3 = 0; var3 < 10; ++var3) {
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(16) + 8;
            final int var8 = this.randomGenerator.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var8, var5));
        }
        if (this.randomGenerator.nextInt(32) == 0) {
            final int var3 = this.randomGenerator.nextInt(16) + 8;
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(var3, 0, var4)).getY() * 2);
            new WorldGenPumpkin().generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var3, var5, var4));
        }
        for (int var3 = 0; var3 < this.cactiPerChunk; ++var3) {
            final int var4 = this.randomGenerator.nextInt(16) + 8;
            final int var5 = this.randomGenerator.nextInt(16) + 8;
            final int var8 = this.randomGenerator.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            this.cactusGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var8, var5));
        }
        if (this.generateLakes) {
            for (int var3 = 0; var3 < 50; ++var3) {
                final BlockPos var13 = this.field_180294_c.add(this.randomGenerator.nextInt(16) + 8, this.randomGenerator.nextInt(this.randomGenerator.nextInt(248) + 8), this.randomGenerator.nextInt(16) + 8);
                new WorldGenLiquids(Blocks.flowing_water).generate(this.currentWorld, this.randomGenerator, var13);
            }
            for (int var3 = 0; var3 < 20; ++var3) {
                final BlockPos var13 = this.field_180294_c.add(this.randomGenerator.nextInt(16) + 8, this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8), this.randomGenerator.nextInt(16) + 8);
                new WorldGenLiquids(Blocks.flowing_lava).generate(this.currentWorld, this.randomGenerator, var13);
            }
        }
    }
    
    protected void genStandardOre1(final int p_76795_1_, final WorldGenerator p_76795_2_, int p_76795_3_, int p_76795_4_) {
        if (p_76795_4_ < p_76795_3_) {
            final int var5 = p_76795_3_;
            p_76795_3_ = p_76795_4_;
            p_76795_4_ = var5;
        }
        else if (p_76795_4_ == p_76795_3_) {
            if (p_76795_3_ < 255) {
                ++p_76795_4_;
            }
            else {
                --p_76795_3_;
            }
        }
        for (int var5 = 0; var5 < p_76795_1_; ++var5) {
            final BlockPos var6 = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(p_76795_4_ - p_76795_3_) + p_76795_3_, this.randomGenerator.nextInt(16));
            p_76795_2_.generate(this.currentWorld, this.randomGenerator, var6);
        }
    }
    
    protected void genStandardOre2(final int p_76793_1_, final WorldGenerator p_76793_2_, final int p_76793_3_, final int p_76793_4_) {
        for (int var5 = 0; var5 < p_76793_1_; ++var5) {
            final BlockPos var6 = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(p_76793_4_) + this.randomGenerator.nextInt(p_76793_4_) + p_76793_3_ - p_76793_4_, this.randomGenerator.nextInt(16));
            p_76793_2_.generate(this.currentWorld, this.randomGenerator, var6);
        }
    }
    
    protected void generateOres() {
        this.genStandardOre1(this.field_180293_d.field_177790_J, this.dirtGen, this.field_180293_d.field_177791_K, this.field_180293_d.field_177784_L);
        this.genStandardOre1(this.field_180293_d.field_177786_N, this.gravelGen, this.field_180293_d.field_177787_O, this.field_180293_d.field_177797_P);
        this.genStandardOre1(this.field_180293_d.field_177795_V, this.field_180297_k, this.field_180293_d.field_177794_W, this.field_180293_d.field_177801_X);
        this.genStandardOre1(this.field_180293_d.field_177799_R, this.field_180296_j, this.field_180293_d.field_177798_S, this.field_180293_d.field_177793_T);
        this.genStandardOre1(this.field_180293_d.field_177802_Z, this.field_180295_l, this.field_180293_d.field_177846_aa, this.field_180293_d.field_177847_ab);
        this.genStandardOre1(this.field_180293_d.field_177845_ad, this.coalGen, this.field_180293_d.field_177851_ae, this.field_180293_d.field_177853_af);
        this.genStandardOre1(this.field_180293_d.field_177849_ah, this.ironGen, this.field_180293_d.field_177832_ai, this.field_180293_d.field_177834_aj);
        this.genStandardOre1(this.field_180293_d.field_177830_al, this.goldGen, this.field_180293_d.field_177840_am, this.field_180293_d.field_177842_an);
        this.genStandardOre1(this.field_180293_d.field_177838_ap, this.field_180299_p, this.field_180293_d.field_177818_aq, this.field_180293_d.field_177816_ar);
        this.genStandardOre1(this.field_180293_d.field_177812_at, this.field_180298_q, this.field_180293_d.field_177826_au, this.field_180293_d.field_177824_av);
        this.genStandardOre2(this.field_180293_d.field_177820_ax, this.lapisGen, this.field_180293_d.field_177807_ay, this.field_180293_d.field_177805_az);
    }
}
