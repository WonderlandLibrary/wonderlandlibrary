// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3i;
import java.util.Collection;
import com.google.common.collect.Lists;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.util.Rotation;
import java.util.Arrays;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.biome.Biome;
import java.util.List;

public class WoodlandMansion extends MapGenStructure
{
    private final int featureSpacing = 80;
    private final int minFeatureSeparation = 20;
    public static final List<Biome> ALLOWED_BIOMES;
    private final ChunkGeneratorOverworld provider;
    
    public WoodlandMansion(final ChunkGeneratorOverworld providerIn) {
        this.provider = providerIn;
    }
    
    @Override
    public String getStructureName() {
        return "Mansion";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(final int chunkX, final int chunkZ) {
        int i = chunkX;
        int j = chunkZ;
        if (chunkX < 0) {
            i = chunkX - 79;
        }
        if (chunkZ < 0) {
            j = chunkZ - 79;
        }
        int k = i / 80;
        int l = j / 80;
        final Random random = this.world.setRandomSeed(k, l, 10387319);
        k *= 80;
        l *= 80;
        k += (random.nextInt(60) + random.nextInt(60)) / 2;
        l += (random.nextInt(60) + random.nextInt(60)) / 2;
        if (chunkX == k && chunkZ == l) {
            final boolean flag = this.world.getBiomeProvider().areBiomesViable(chunkX * 16 + 8, chunkZ * 16 + 8, 32, WoodlandMansion.ALLOWED_BIOMES);
            if (flag) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final BlockPos pos, final boolean findUnexplored) {
        this.world = worldIn;
        final BiomeProvider biomeprovider = worldIn.getBiomeProvider();
        return (biomeprovider.isFixedBiome() && biomeprovider.getFixedBiome() != Biomes.ROOFED_FOREST) ? null : MapGenStructure.findNearestStructurePosBySpacing(worldIn, this, pos, 80, 20, 10387319, true, 100, findUnexplored);
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new Start(this.world, this.provider, this.rand, chunkX, chunkZ);
    }
    
    static {
        ALLOWED_BIOMES = Arrays.asList(Biomes.ROOFED_FOREST, Biomes.MUTATED_ROOFED_FOREST);
    }
    
    public static class Start extends StructureStart
    {
        private boolean isValid;
        
        public Start() {
        }
        
        public Start(final World p_i47235_1_, final ChunkGeneratorOverworld p_i47235_2_, final Random p_i47235_3_, final int p_i47235_4_, final int p_i47235_5_) {
            super(p_i47235_4_, p_i47235_5_);
            this.create(p_i47235_1_, p_i47235_2_, p_i47235_3_, p_i47235_4_, p_i47235_5_);
        }
        
        private void create(final World p_191092_1_, final ChunkGeneratorOverworld p_191092_2_, final Random p_191092_3_, final int p_191092_4_, final int p_191092_5_) {
            final Rotation rotation = Rotation.values()[p_191092_3_.nextInt(Rotation.values().length)];
            final ChunkPrimer chunkprimer = new ChunkPrimer();
            p_191092_2_.setBlocksInChunk(p_191092_4_, p_191092_5_, chunkprimer);
            int i = 5;
            int j = 5;
            if (rotation == Rotation.CLOCKWISE_90) {
                i = -5;
            }
            else if (rotation == Rotation.CLOCKWISE_180) {
                i = -5;
                j = -5;
            }
            else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                j = -5;
            }
            final int k = chunkprimer.findGroundBlockIdx(7, 7);
            final int l = chunkprimer.findGroundBlockIdx(7, 7 + j);
            final int i2 = chunkprimer.findGroundBlockIdx(7 + i, 7);
            final int j2 = chunkprimer.findGroundBlockIdx(7 + i, 7 + j);
            final int k2 = Math.min(Math.min(k, l), Math.min(i2, j2));
            if (k2 < 60) {
                this.isValid = false;
            }
            else {
                final BlockPos blockpos = new BlockPos(p_191092_4_ * 16 + 8, k2 + 1, p_191092_5_ * 16 + 8);
                final List<WoodlandMansionPieces.MansionTemplate> list = (List<WoodlandMansionPieces.MansionTemplate>)Lists.newLinkedList();
                WoodlandMansionPieces.generateMansion(p_191092_1_.getSaveHandler().getStructureTemplateManager(), blockpos, rotation, list, p_191092_3_);
                this.components.addAll(list);
                this.updateBoundingBox();
                this.isValid = true;
            }
        }
        
        @Override
        public void generateStructure(final World worldIn, final Random rand, final StructureBoundingBox structurebb) {
            super.generateStructure(worldIn, rand, structurebb);
            final int i = this.boundingBox.minY;
            for (int j = structurebb.minX; j <= structurebb.maxX; ++j) {
                for (int k = structurebb.minZ; k <= structurebb.maxZ; ++k) {
                    final BlockPos blockpos = new BlockPos(j, i, k);
                    if (!worldIn.isAirBlock(blockpos) && this.boundingBox.isVecInside(blockpos)) {
                        boolean flag = false;
                        for (final StructureComponent structurecomponent : this.components) {
                            if (structurecomponent.boundingBox.isVecInside(blockpos)) {
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            for (int l = i - 1; l > 1; --l) {
                                final BlockPos blockpos2 = new BlockPos(j, l, k);
                                if (!worldIn.isAirBlock(blockpos2) && !worldIn.getBlockState(blockpos2).getMaterial().isLiquid()) {
                                    break;
                                }
                                worldIn.setBlockState(blockpos2, Blocks.COBBLESTONE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
        }
        
        @Override
        public boolean isSizeableStructure() {
            return this.isValid;
        }
    }
}
