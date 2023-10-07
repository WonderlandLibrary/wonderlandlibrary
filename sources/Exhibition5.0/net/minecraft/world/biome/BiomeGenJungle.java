// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.block.BlockPlanks;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;

public class BiomeGenJungle extends BiomeGenBase
{
    private boolean field_150614_aC;
    private static final String __OBFID = "CL_00000175";
    
    public BiomeGenJungle(final int p_i45379_1_, final boolean p_i45379_2_) {
        super(p_i45379_1_);
        this.field_150614_aC = p_i45379_2_;
        if (p_i45379_2_) {
            this.theBiomeDecorator.treesPerChunk = 2;
        }
        else {
            this.theBiomeDecorator.treesPerChunk = 50;
        }
        this.theBiomeDecorator.grassPerChunk = 25;
        this.theBiomeDecorator.flowersPerChunk = 4;
        if (!p_i45379_2_) {
            this.spawnableMonsterList.add(new SpawnListEntry(EntityOcelot.class, 2, 1, 1));
        }
        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random p_150567_1_) {
        return (p_150567_1_.nextInt(10) == 0) ? this.worldGeneratorBigTree : ((p_150567_1_.nextInt(2) == 0) ? new WorldGenShrub(BlockPlanks.EnumType.JUNGLE.func_176839_a(), BlockPlanks.EnumType.OAK.func_176839_a()) : ((!this.field_150614_aC && p_150567_1_.nextInt(3) == 0) ? new WorldGenMegaJungle(false, 10, 20, BlockPlanks.EnumType.JUNGLE.func_176839_a(), BlockPlanks.EnumType.JUNGLE.func_176839_a()) : new WorldGenTrees(false, 4 + p_150567_1_.nextInt(7), BlockPlanks.EnumType.JUNGLE.func_176839_a(), BlockPlanks.EnumType.JUNGLE.func_176839_a(), true)));
    }
    
    @Override
    public WorldGenerator getRandomWorldGenForGrass(final Random p_76730_1_) {
        return (p_76730_1_.nextInt(4) == 0) ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }
    
    @Override
    public void func_180624_a(final World worldIn, final Random p_180624_2_, final BlockPos p_180624_3_) {
        super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
        final int var4 = p_180624_2_.nextInt(16) + 8;
        int var5 = p_180624_2_.nextInt(16) + 8;
        int var6 = p_180624_2_.nextInt(worldIn.getHorizon(p_180624_3_.add(var4, 0, var5)).getY() * 2);
        new WorldGenMelon().generate(worldIn, p_180624_2_, p_180624_3_.add(var4, var6, var5));
        final WorldGenVines var7 = new WorldGenVines();
        for (var5 = 0; var5 < 50; ++var5) {
            var6 = p_180624_2_.nextInt(16) + 8;
            final boolean var8 = true;
            final int var9 = p_180624_2_.nextInt(16) + 8;
            var7.generate(worldIn, p_180624_2_, p_180624_3_.add(var6, 128, var9));
        }
    }
}
