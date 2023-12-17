// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.World;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;

public class BiomeForest extends Biome
{
    protected static final WorldGenBirchTree SUPER_BIRCH_TREE;
    protected static final WorldGenBirchTree BIRCH_TREE;
    protected static final WorldGenCanopyTree ROOF_TREE;
    private final Type type;
    
    public BiomeForest(final Type typeIn, final BiomeProperties properties) {
        super(properties);
        this.type = typeIn;
        this.decorator.treesPerChunk = 10;
        this.decorator.grassPerChunk = 2;
        if (this.type == Type.FLOWER) {
            this.decorator.treesPerChunk = 6;
            this.decorator.flowersPerChunk = 100;
            this.decorator.grassPerChunk = 1;
            this.spawnableCreatureList.add(new SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        }
        if (this.type == Type.NORMAL) {
            this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 4));
        }
        if (this.type == Type.ROOFED) {
            this.decorator.treesPerChunk = -999;
        }
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(final Random rand) {
        if (this.type == Type.ROOFED && rand.nextInt(3) > 0) {
            return BiomeForest.ROOF_TREE;
        }
        if (this.type != Type.BIRCH && rand.nextInt(5) != 0) {
            return (rand.nextInt(10) == 0) ? BiomeForest.BIG_TREE_FEATURE : BiomeForest.TREE_FEATURE;
        }
        return BiomeForest.BIRCH_TREE;
    }
    
    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(final Random rand, final BlockPos pos) {
        if (this.type == Type.FLOWER) {
            final double d0 = MathHelper.clamp((1.0 + BiomeForest.GRASS_COLOR_NOISE.getValue(pos.getX() / 48.0, pos.getZ() / 48.0)) / 2.0, 0.0, 0.9999);
            final BlockFlower.EnumFlowerType blockflower$enumflowertype = BlockFlower.EnumFlowerType.values()[(int)(d0 * BlockFlower.EnumFlowerType.values().length)];
            return (blockflower$enumflowertype == BlockFlower.EnumFlowerType.BLUE_ORCHID) ? BlockFlower.EnumFlowerType.POPPY : blockflower$enumflowertype;
        }
        return super.pickRandomFlower(rand, pos);
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        if (this.type == Type.ROOFED) {
            this.addMushrooms(worldIn, rand, pos);
        }
        int i = rand.nextInt(5) - 3;
        if (this.type == Type.FLOWER) {
            i += 2;
        }
        this.addDoublePlants(worldIn, rand, pos, i);
        super.decorate(worldIn, rand, pos);
    }
    
    protected void addMushrooms(final World p_185379_1_, final Random p_185379_2_, final BlockPos p_185379_3_) {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                final int k = i * 4 + 1 + 8 + p_185379_2_.nextInt(3);
                final int l = j * 4 + 1 + 8 + p_185379_2_.nextInt(3);
                final BlockPos blockpos = p_185379_1_.getHeight(p_185379_3_.add(k, 0, l));
                if (p_185379_2_.nextInt(20) == 0) {
                    final WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
                    worldgenbigmushroom.generate(p_185379_1_, p_185379_2_, blockpos);
                }
                else {
                    final WorldGenAbstractTree worldgenabstracttree = this.getRandomTreeFeature(p_185379_2_);
                    worldgenabstracttree.setDecorationDefaults();
                    if (worldgenabstracttree.generate(p_185379_1_, p_185379_2_, blockpos)) {
                        worldgenabstracttree.generateSaplings(p_185379_1_, p_185379_2_, blockpos);
                    }
                }
            }
        }
    }
    
    protected void addDoublePlants(final World p_185378_1_, final Random p_185378_2_, final BlockPos p_185378_3_, final int p_185378_4_) {
        for (int i = 0; i < p_185378_4_; ++i) {
            final int j = p_185378_2_.nextInt(3);
            if (j == 0) {
                BiomeForest.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SYRINGA);
            }
            else if (j == 1) {
                BiomeForest.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.ROSE);
            }
            else if (j == 2) {
                BiomeForest.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.PAEONIA);
            }
            for (int k = 0; k < 5; ++k) {
                final int l = p_185378_2_.nextInt(16) + 8;
                final int i2 = p_185378_2_.nextInt(16) + 8;
                final int j2 = p_185378_2_.nextInt(p_185378_1_.getHeight(p_185378_3_.add(l, 0, i2)).getY() + 32);
                if (BiomeForest.DOUBLE_PLANT_GENERATOR.generate(p_185378_1_, p_185378_2_, new BlockPos(p_185378_3_.getX() + l, j2, p_185378_3_.getZ() + i2))) {
                    break;
                }
            }
        }
    }
    
    @Override
    public Class<? extends Biome> getBiomeClass() {
        return BiomeForest.class;
    }
    
    @Override
    public int getGrassColorAtPos(final BlockPos pos) {
        final int i = super.getGrassColorAtPos(pos);
        return (this.type == Type.ROOFED) ? ((i & 0xFEFEFE) + 2634762 >> 1) : i;
    }
    
    static {
        SUPER_BIRCH_TREE = new WorldGenBirchTree(false, true);
        BIRCH_TREE = new WorldGenBirchTree(false, false);
        ROOF_TREE = new WorldGenCanopyTree(false);
    }
    
    public enum Type
    {
        NORMAL, 
        FLOWER, 
        BIRCH, 
        ROOFED;
    }
}
