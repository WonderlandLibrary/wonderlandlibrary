// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.world.gen.ChunkGeneratorEnd;

public class MapGenEndCity extends MapGenStructure
{
    private final int citySpacing = 20;
    private final int minCitySeparation = 11;
    private final ChunkGeneratorEnd endProvider;
    
    public MapGenEndCity(final ChunkGeneratorEnd endProviderIn) {
        this.endProvider = endProviderIn;
    }
    
    @Override
    public String getStructureName() {
        return "EndCity";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        final int i = chunkX;
        final int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= 19;
        }
        if (chunkZ < 0) {
            chunkZ -= 19;
        }
        int k = chunkX / 20;
        int l = chunkZ / 20;
        final Random random = this.world.setRandomSeed(k, l, 10387313);
        k *= 20;
        l *= 20;
        k += (random.nextInt(9) + random.nextInt(9)) / 2;
        l += (random.nextInt(9) + random.nextInt(9)) / 2;
        if (i == k && j == l && this.endProvider.isIslandChunk(i, j)) {
            final int i2 = getYPosForStructure(i, j, this.endProvider);
            return i2 >= 60;
        }
        return false;
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new Start(this.world, this.endProvider, this.rand, chunkX, chunkZ);
    }
    
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final BlockPos pos, final boolean findUnexplored) {
        this.world = worldIn;
        return MapGenStructure.findNearestStructurePosBySpacing(worldIn, this, pos, 20, 11, 10387313, true, 100, findUnexplored);
    }
    
    private static int getYPosForStructure(final int chunkX, final int chunkY, final ChunkGeneratorEnd generatorIn) {
        final Random random = new Random(chunkX + chunkY * 10387313);
        final Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        generatorIn.setBlocksInChunk(chunkX, chunkY, chunkprimer);
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
        return k2;
    }
    
    public static class Start extends StructureStart
    {
        private boolean isSizeable;
        
        public Start() {
        }
        
        public Start(final World worldIn, final ChunkGeneratorEnd chunkProvider, final Random random, final int chunkX, final int chunkZ) {
            super(chunkX, chunkZ);
            this.create(worldIn, chunkProvider, random, chunkX, chunkZ);
        }
        
        private void create(final World worldIn, final ChunkGeneratorEnd chunkProvider, final Random rnd, final int chunkX, final int chunkZ) {
            final Random random = new Random(chunkX + chunkZ * 10387313);
            final Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
            final int i = getYPosForStructure(chunkX, chunkZ, chunkProvider);
            if (i < 60) {
                this.isSizeable = false;
            }
            else {
                final BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i, chunkZ * 16 + 8);
                StructureEndCityPieces.startHouseTower(worldIn.getSaveHandler().getStructureTemplateManager(), blockpos, rotation, this.components, rnd);
                this.updateBoundingBox();
                this.isSizeable = true;
            }
        }
        
        @Override
        public boolean isSizeableStructure() {
            return this.isSizeable;
        }
    }
}
