// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockOldLog;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.block.state.IBlockState;

public class BiomeJungle extends Biome
{
    private final boolean isEdge;
    private static final IBlockState JUNGLE_LOG;
    private static final IBlockState JUNGLE_LEAF;
    private static final IBlockState OAK_LEAF;
    
    public BiomeJungle(final boolean isEdgeIn, final BiomeProperties properties) {
        super(properties);
        this.isEdge = isEdgeIn;
        if (isEdgeIn) {
            this.decorator.treesPerChunk = 2;
        }
        else {
            this.decorator.treesPerChunk = 50;
        }
        this.decorator.grassPerChunk = 25;
        this.decorator.flowersPerChunk = 4;
        if (!isEdgeIn) {
            this.spawnableMonsterList.add(new SpawnListEntry(EntityOcelot.class, 2, 1, 1));
        }
        this.spawnableCreatureList.add(new SpawnListEntry(EntityParrot.class, 40, 1, 2));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(final Random rand) {
        if (rand.nextInt(10) == 0) {
            return BiomeJungle.BIG_TREE_FEATURE;
        }
        if (rand.nextInt(2) == 0) {
            return new WorldGenShrub(BiomeJungle.JUNGLE_LOG, BiomeJungle.OAK_LEAF);
        }
        return (!this.isEdge && rand.nextInt(3) == 0) ? new WorldGenMegaJungle(false, 10, 20, BiomeJungle.JUNGLE_LOG, BiomeJungle.JUNGLE_LEAF) : new WorldGenTrees(false, 4 + rand.nextInt(7), BiomeJungle.JUNGLE_LOG, BiomeJungle.JUNGLE_LEAF, true);
    }
    
    @Override
    public WorldGenerator getRandomWorldGenForGrass(final Random rand) {
        return (rand.nextInt(4) == 0) ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        final int i = rand.nextInt(16) + 8;
        final int j = rand.nextInt(16) + 8;
        int k = rand.nextInt(worldIn.getHeight(pos.add(i, 0, j)).getY() * 2);
        new WorldGenMelon().generate(worldIn, rand, pos.add(i, k, j));
        final WorldGenVines worldgenvines = new WorldGenVines();
        for (int j2 = 0; j2 < 50; ++j2) {
            k = rand.nextInt(16) + 8;
            final int l = 128;
            final int i2 = rand.nextInt(16) + 8;
            worldgenvines.generate(worldIn, rand, pos.add(k, 128, i2));
        }
    }
    
    static {
        JUNGLE_LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
        JUNGLE_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
        OAK_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
    }
}
