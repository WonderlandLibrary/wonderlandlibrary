/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public final class SpawnerAnimals {
    private static final int MOB_COUNT_DIV = (int)Math.pow(17.0, 2.0);
    private final Set<ChunkCoordIntPair> eligibleChunksForSpawning = Sets.newHashSet();

    /*
     * Exception decompiling
     */
    public int findChunksForSpawning(WorldServer p_77192_1_, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean p_77192_4_) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [5[UNCONDITIONALDOLOOP]], but top level block is 8[FORLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:306)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:159)
         *     at java.lang.Thread.run(Thread.java:748)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    protected static BlockPos getRandomChunkPosition(World worldIn, int x, int z) {
        Chunk chunk = worldIn.getChunkFromChunkCoords(x, z);
        int i = x * 16 + worldIn.rand.nextInt(16);
        int j = z * 16 + worldIn.rand.nextInt(16);
        int k = MathHelper.func_154354_b(chunk.getHeight(new BlockPos(i, 0, j)) + 1, 16);
        int l = worldIn.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
        return new BlockPos(i, l, j);
    }

    public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType p_180267_0_, World worldIn, BlockPos pos) {
        if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        }
        Block block = worldIn.getBlockState(pos).getBlock();
        if (p_180267_0_ == EntityLiving.SpawnPlacementType.IN_WATER) {
            if (!block.getMaterial().isLiquid()) return false;
            if (!worldIn.getBlockState(pos.down()).getBlock().getMaterial().isLiquid()) return false;
            if (worldIn.getBlockState(pos.up()).getBlock().isNormalCube()) return false;
            return true;
        }
        BlockPos blockpos = pos.down();
        if (!World.doesBlockHaveSolidTopSurface(worldIn, blockpos)) {
            return false;
        }
        Block block1 = worldIn.getBlockState(blockpos).getBlock();
        if (block1 == Blocks.bedrock) return false;
        if (block1 == Blocks.barrier) return false;
        boolean bl = true;
        boolean flag = bl;
        if (!flag) return false;
        if (block.isNormalCube()) return false;
        if (block.getMaterial().isLiquid()) return false;
        if (worldIn.getBlockState(pos.up()).getBlock().isNormalCube()) return false;
        return true;
    }

    public static void performWorldGenSpawning(World worldIn, BiomeGenBase p_77191_1_, int p_77191_2_, int p_77191_3_, int p_77191_4_, int p_77191_5_, Random p_77191_6_) {
        List<BiomeGenBase.SpawnListEntry> list = p_77191_1_.getSpawnableList(EnumCreatureType.CREATURE);
        if (list.isEmpty()) return;
        block2: while (p_77191_6_.nextFloat() < p_77191_1_.getSpawningChance()) {
            BiomeGenBase.SpawnListEntry biomegenbase$spawnlistentry = WeightedRandom.getRandomItem(worldIn.rand, list);
            int i = biomegenbase$spawnlistentry.minGroupCount + p_77191_6_.nextInt(1 + biomegenbase$spawnlistentry.maxGroupCount - biomegenbase$spawnlistentry.minGroupCount);
            IEntityLivingData ientitylivingdata = null;
            int j = p_77191_2_ + p_77191_6_.nextInt(p_77191_4_);
            int k = p_77191_3_ + p_77191_6_.nextInt(p_77191_5_);
            int l = j;
            int i1 = k;
            int j1 = 0;
            while (true) {
                if (j1 >= i) continue block2;
                boolean flag = false;
                for (int k1 = 0; !flag && k1 < 4; ++k1) {
                    BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
                    if (SpawnerAnimals.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
                        EntityLiving entityliving;
                        try {
                            entityliving = biomegenbase$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldIn);
                        }
                        catch (Exception exception) {
                            exception.printStackTrace();
                            continue;
                        }
                        entityliving.setLocationAndAngles((float)j + 0.5f, blockpos.getY(), (float)k + 0.5f, p_77191_6_.nextFloat() * 360.0f, 0.0f);
                        worldIn.spawnEntityInWorld(entityliving);
                        ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                        flag = true;
                    }
                    j += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);
                    k += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);
                    while (j < p_77191_2_ || j >= p_77191_2_ + p_77191_4_ || k < p_77191_3_ || k >= p_77191_3_ + p_77191_4_) {
                        j = l + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);
                        k = i1 + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);
                    }
                }
                ++j1;
            }
            break;
        }
        return;
    }
}

