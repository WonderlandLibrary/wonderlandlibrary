// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Random;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.MathHelper;
import java.util.Map;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import java.util.List;

public class MapGenStronghold extends MapGenStructure
{
    private final List<Biome> allowedBiomes;
    private boolean ranBiomeCheck;
    private ChunkPos[] structureCoords;
    private double distance;
    private int spread;
    
    public MapGenStronghold() {
        this.structureCoords = new ChunkPos[128];
        this.distance = 32.0;
        this.spread = 3;
        this.allowedBiomes = (List<Biome>)Lists.newArrayList();
        for (final Biome biome : Biome.REGISTRY) {
            if (biome != null && biome.getBaseHeight() > 0.0f) {
                this.allowedBiomes.add(biome);
            }
        }
    }
    
    public MapGenStronghold(final Map<String, String> p_i2068_1_) {
        this();
        for (final Map.Entry<String, String> entry : p_i2068_1_.entrySet()) {
            if (entry.getKey().equals("distance")) {
                this.distance = MathHelper.getDouble(entry.getValue(), this.distance, 1.0);
            }
            else if (entry.getKey().equals("count")) {
                this.structureCoords = new ChunkPos[MathHelper.getInt(entry.getValue(), this.structureCoords.length, 1)];
            }
            else {
                if (!entry.getKey().equals("spread")) {
                    continue;
                }
                this.spread = MathHelper.getInt(entry.getValue(), this.spread, 1);
            }
        }
    }
    
    @Override
    public String getStructureName() {
        return "Stronghold";
    }
    
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final BlockPos pos, final boolean findUnexplored) {
        if (!this.ranBiomeCheck) {
            this.generatePositions();
            this.ranBiomeCheck = true;
        }
        BlockPos blockpos = null;
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(0, 0, 0);
        double d0 = Double.MAX_VALUE;
        for (final ChunkPos chunkpos : this.structureCoords) {
            blockpos$mutableblockpos.setPos((chunkpos.x << 4) + 8, 32, (chunkpos.z << 4) + 8);
            final double d2 = blockpos$mutableblockpos.distanceSq(pos);
            if (blockpos == null) {
                blockpos = new BlockPos(blockpos$mutableblockpos);
                d0 = d2;
            }
            else if (d2 < d0) {
                blockpos = new BlockPos(blockpos$mutableblockpos);
                d0 = d2;
            }
        }
        return blockpos;
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(final int chunkX, final int chunkZ) {
        if (!this.ranBiomeCheck) {
            this.generatePositions();
            this.ranBiomeCheck = true;
        }
        for (final ChunkPos chunkpos : this.structureCoords) {
            if (chunkX == chunkpos.x && chunkZ == chunkpos.z) {
                return true;
            }
        }
        return false;
    }
    
    private void generatePositions() {
        this.initializeStructureData(this.world);
        int i = 0;
        for (final StructureStart structurestart : this.structureMap.values()) {
            if (i < this.structureCoords.length) {
                this.structureCoords[i++] = new ChunkPos(structurestart.getChunkPosX(), structurestart.getChunkPosZ());
            }
        }
        final Random random = new Random();
        random.setSeed(this.world.getSeed());
        double d1 = random.nextDouble() * 3.141592653589793 * 2.0;
        int j = 0;
        int k = 0;
        final int l = this.structureMap.size();
        if (l < this.structureCoords.length) {
            for (int i2 = 0; i2 < this.structureCoords.length; ++i2) {
                final double d2 = 4.0 * this.distance + this.distance * j * 6.0 + (random.nextDouble() - 0.5) * this.distance * 2.5;
                int j2 = (int)Math.round(Math.cos(d1) * d2);
                int k2 = (int)Math.round(Math.sin(d1) * d2);
                final BlockPos blockpos = this.world.getBiomeProvider().findBiomePosition((j2 << 4) + 8, (k2 << 4) + 8, 112, this.allowedBiomes, random);
                if (blockpos != null) {
                    j2 = blockpos.getX() >> 4;
                    k2 = blockpos.getZ() >> 4;
                }
                if (i2 >= l) {
                    this.structureCoords[i2] = new ChunkPos(j2, k2);
                }
                d1 += 6.283185307179586 / this.spread;
                if (++k == this.spread) {
                    ++j;
                    k = 0;
                    this.spread += 2 * this.spread / (j + 1);
                    this.spread = Math.min(this.spread, this.structureCoords.length - i2);
                    d1 += random.nextDouble() * 3.141592653589793 * 2.0;
                }
            }
        }
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        Start mapgenstronghold$start;
        for (mapgenstronghold$start = new Start(this.world, this.rand, chunkX, chunkZ); mapgenstronghold$start.getComponents().isEmpty() || mapgenstronghold$start.getComponents().get(0).strongholdPortalRoom == null; mapgenstronghold$start = new Start(this.world, this.rand, chunkX, chunkZ)) {}
        return mapgenstronghold$start;
    }
    
    public static class Start extends StructureStart
    {
        public Start() {
        }
        
        public Start(final World worldIn, final Random random, final int chunkX, final int chunkZ) {
            super(chunkX, chunkZ);
            StructureStrongholdPieces.prepareStructurePieces();
            final StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            this.components.add(structurestrongholdpieces$stairs2);
            structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, this.components, random);
            final List<StructureComponent> list = structurestrongholdpieces$stairs2.pendingChildren;
            while (!list.isEmpty()) {
                final int i = random.nextInt(list.size());
                final StructureComponent structurecomponent = list.remove(i);
                structurecomponent.buildComponent(structurestrongholdpieces$stairs2, this.components, random);
            }
            this.updateBoundingBox();
            this.markAvailableHeight(worldIn, random, 10);
        }
    }
}
