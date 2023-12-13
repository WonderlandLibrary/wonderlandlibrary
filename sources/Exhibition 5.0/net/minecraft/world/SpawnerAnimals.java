// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

import java.util.List;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Sets;
import java.util.Set;

public final class SpawnerAnimals
{
    private static final int field_180268_a;
    private final Set eligibleChunksForSpawning;
    private static final String __OBFID = "CL_00000152";
    
    public SpawnerAnimals() {
        this.eligibleChunksForSpawning = Sets.newHashSet();
    }
    
    public int findChunksForSpawning(final WorldServer p_77192_1_, final boolean p_77192_2_, final boolean p_77192_3_, final boolean p_77192_4_) {
        if (!p_77192_2_ && !p_77192_3_) {
            return 0;
        }
        this.eligibleChunksForSpawning.clear();
        int var5 = 0;
        for (final EntityPlayer var7 : p_77192_1_.playerEntities) {
            if (!var7.func_175149_v()) {
                final int var8 = MathHelper.floor_double(var7.posX / 16.0);
                final int var9 = MathHelper.floor_double(var7.posZ / 16.0);
                final byte var10 = 8;
                for (int var11 = -var10; var11 <= var10; ++var11) {
                    for (int var12 = -var10; var12 <= var10; ++var12) {
                        final boolean var13 = var11 == -var10 || var11 == var10 || var12 == -var10 || var12 == var10;
                        final ChunkCoordIntPair var14 = new ChunkCoordIntPair(var11 + var8, var12 + var9);
                        if (!this.eligibleChunksForSpawning.contains(var14)) {
                            ++var5;
                            if (!var13 && p_77192_1_.getWorldBorder().contains(var14)) {
                                this.eligibleChunksForSpawning.add(var14);
                            }
                        }
                    }
                }
            }
        }
        int var15 = 0;
        final BlockPos var16 = p_77192_1_.getSpawnPoint();
        final EnumCreatureType[] var17 = EnumCreatureType.values();
        final int var9 = var17.length;
        for (final EnumCreatureType var19 : var17) {
            if ((!var19.getPeacefulCreature() || p_77192_3_) && (var19.getPeacefulCreature() || p_77192_2_) && (!var19.getAnimal() || p_77192_4_)) {
                final int var12 = p_77192_1_.countEntities(var19.getCreatureClass());
                final int var20 = var19.getMaxNumberOfCreature() * var5 / SpawnerAnimals.field_180268_a;
                if (var12 <= var20) {
                Label_0352:
                    while (true) {
                        for (final ChunkCoordIntPair var22 : this.eligibleChunksForSpawning) {
                            final BlockPos var23 = func_180621_a(p_77192_1_, var22.chunkXPos, var22.chunkZPos);
                            final int var24 = var23.getX();
                            final int var25 = var23.getY();
                            final int var26 = var23.getZ();
                            final Block var27 = p_77192_1_.getBlockState(var23).getBlock();
                            if (!var27.isNormalCube()) {
                                int var28 = 0;
                                for (int var29 = 0; var29 < 3; ++var29) {
                                    int var30 = var24;
                                    int var31 = var25;
                                    int var32 = var26;
                                    final byte var33 = 6;
                                    BiomeGenBase.SpawnListEntry var34 = null;
                                    IEntityLivingData var35 = null;
                                    for (int var36 = 0; var36 < 4; ++var36) {
                                        var30 += p_77192_1_.rand.nextInt(var33) - p_77192_1_.rand.nextInt(var33);
                                        var31 += p_77192_1_.rand.nextInt(1) - p_77192_1_.rand.nextInt(1);
                                        var32 += p_77192_1_.rand.nextInt(var33) - p_77192_1_.rand.nextInt(var33);
                                        final BlockPos var37 = new BlockPos(var30, var31, var32);
                                        final float var38 = var30 + 0.5f;
                                        final float var39 = var32 + 0.5f;
                                        if (!p_77192_1_.func_175636_b(var38, var31, var39, 24.0) && var16.distanceSq(var38, var31, var39) >= 576.0) {
                                            if (var34 == null) {
                                                var34 = p_77192_1_.func_175734_a(var19, var37);
                                                if (var34 == null) {
                                                    break;
                                                }
                                            }
                                            if (p_77192_1_.func_175732_a(var19, var34, var37) && func_180267_a(EntitySpawnPlacementRegistry.func_180109_a(var34.entityClass), p_77192_1_, var37)) {
                                                EntityLiving var40;
                                                try {
                                                    var40 = var34.entityClass.getConstructor(World.class).newInstance(p_77192_1_);
                                                }
                                                catch (Exception var41) {
                                                    var41.printStackTrace();
                                                    return var15;
                                                }
                                                var40.setLocationAndAngles(var38, var31, var39, p_77192_1_.rand.nextFloat() * 360.0f, 0.0f);
                                                if (var40.getCanSpawnHere() && var40.handleLavaMovement()) {
                                                    var35 = var40.func_180482_a(p_77192_1_.getDifficultyForLocation(new BlockPos(var40)), var35);
                                                    if (var40.handleLavaMovement()) {
                                                        ++var28;
                                                        p_77192_1_.spawnEntityInWorld(var40);
                                                    }
                                                    if (var28 >= var40.getMaxSpawnedInChunk()) {
                                                        continue Label_0352;
                                                    }
                                                }
                                                var15 += var28;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return var15;
    }
    
    protected static BlockPos func_180621_a(final World worldIn, final int p_180621_1_, final int p_180621_2_) {
        final Chunk var3 = worldIn.getChunkFromChunkCoords(p_180621_1_, p_180621_2_);
        final int var4 = p_180621_1_ * 16 + worldIn.rand.nextInt(16);
        final int var5 = p_180621_2_ * 16 + worldIn.rand.nextInt(16);
        final int var6 = MathHelper.func_154354_b(var3.getHeight(new BlockPos(var4, 0, var5)) + 1, 16);
        final int var7 = worldIn.rand.nextInt((var6 > 0) ? var6 : (var3.getTopFilledSegment() + 16 - 1));
        return new BlockPos(var4, var7, var5);
    }
    
    public static boolean func_180267_a(final EntityLiving.SpawnPlacementType p_180267_0_, final World worldIn, final BlockPos p_180267_2_) {
        if (!worldIn.getWorldBorder().contains(p_180267_2_)) {
            return false;
        }
        final Block var3 = worldIn.getBlockState(p_180267_2_).getBlock();
        if (p_180267_0_ == EntityLiving.SpawnPlacementType.IN_WATER) {
            return var3.getMaterial().isLiquid() && worldIn.getBlockState(p_180267_2_.offsetDown()).getBlock().getMaterial().isLiquid() && !worldIn.getBlockState(p_180267_2_.offsetUp()).getBlock().isNormalCube();
        }
        final BlockPos var4 = p_180267_2_.offsetDown();
        if (!World.doesBlockHaveSolidTopSurface(worldIn, var4)) {
            return false;
        }
        final Block var5 = worldIn.getBlockState(var4).getBlock();
        final boolean var6 = var5 != Blocks.bedrock && var5 != Blocks.barrier;
        return var6 && !var3.isNormalCube() && !var3.getMaterial().isLiquid() && !worldIn.getBlockState(p_180267_2_.offsetUp()).getBlock().isNormalCube();
    }
    
    public static void performWorldGenSpawning(final World worldIn, final BiomeGenBase p_77191_1_, final int p_77191_2_, final int p_77191_3_, final int p_77191_4_, final int p_77191_5_, final Random p_77191_6_) {
        final List var7 = p_77191_1_.getSpawnableList(EnumCreatureType.CREATURE);
        if (!var7.isEmpty()) {
            while (p_77191_6_.nextFloat() < p_77191_1_.getSpawningChance()) {
                final BiomeGenBase.SpawnListEntry var8 = (BiomeGenBase.SpawnListEntry)WeightedRandom.getRandomItem(worldIn.rand, var7);
                final int var9 = var8.minGroupCount + p_77191_6_.nextInt(1 + var8.maxGroupCount - var8.minGroupCount);
                IEntityLivingData var10 = null;
                int var11 = p_77191_2_ + p_77191_6_.nextInt(p_77191_4_);
                int var12 = p_77191_3_ + p_77191_6_.nextInt(p_77191_5_);
                final int var13 = var11;
                final int var14 = var12;
                for (int var15 = 0; var15 < var9; ++var15) {
                    boolean var16 = false;
                    for (int var17 = 0; !var16 && var17 < 4; ++var17) {
                        final BlockPos var18 = worldIn.func_175672_r(new BlockPos(var11, 0, var12));
                        if (func_180267_a(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, var18)) {
                            EntityLiving var19;
                            try {
                                var19 = var8.entityClass.getConstructor(World.class).newInstance(worldIn);
                            }
                            catch (Exception var20) {
                                var20.printStackTrace();
                                continue;
                            }
                            var19.setLocationAndAngles(var11 + 0.5f, var18.getY(), var12 + 0.5f, p_77191_6_.nextFloat() * 360.0f, 0.0f);
                            worldIn.spawnEntityInWorld(var19);
                            var10 = var19.func_180482_a(worldIn.getDifficultyForLocation(new BlockPos(var19)), var10);
                            var16 = true;
                        }
                        for (var11 += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5), var12 += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5); var11 < p_77191_2_ || var11 >= p_77191_2_ + p_77191_4_ || var12 < p_77191_3_ || var12 >= p_77191_3_ + p_77191_4_; var11 = var13 + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5), var12 = var14 + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5)) {}
                    }
                }
            }
        }
    }
    
    static {
        field_180268_a = (int)Math.pow(17.0, 2.0);
    }
}
