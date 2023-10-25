/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

public class MapGenStronghold
extends MapGenStructure {
    private final List<Biome> allowedBiomes;
    private boolean ranBiomeCheck;
    private ChunkPos[] structureCoords = new ChunkPos[128];
    private double distance = 32.0;
    private int spread = 3;

    public MapGenStronghold() {
        this.allowedBiomes = Lists.newArrayList();
        for (Biome biome : Biome.REGISTRY) {
            if (biome == null || !(biome.getBaseHeight() > 0.0f)) continue;
            this.allowedBiomes.add(biome);
        }
    }

    public MapGenStronghold(Map<String, String> p_i2068_1_) {
        this();
        for (Map.Entry<String, String> entry : p_i2068_1_.entrySet()) {
            if (entry.getKey().equals("distance")) {
                this.distance = MathHelper.getDouble(entry.getValue(), this.distance, 1.0);
                continue;
            }
            if (entry.getKey().equals("count")) {
                this.structureCoords = new ChunkPos[MathHelper.getInt(entry.getValue(), this.structureCoords.length, 1)];
                continue;
            }
            if (!entry.getKey().equals("spread")) continue;
            this.spread = MathHelper.getInt(entry.getValue(), this.spread, 1);
        }
    }

    @Override
    public String getStructureName() {
        return "Stronghold";
    }

    @Override
    public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean p_180706_3_) {
        if (!this.ranBiomeCheck) {
            this.generatePositions();
            this.ranBiomeCheck = true;
        }
        BlockPos blockpos = null;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(0, 0, 0);
        double d0 = Double.MAX_VALUE;
        for (ChunkPos chunkpos : this.structureCoords) {
            blockpos$mutableblockpos.setPos((chunkpos.x << 4) + 8, 32, (chunkpos.z << 4) + 8);
            double d1 = blockpos$mutableblockpos.distanceSq(pos);
            if (blockpos == null) {
                blockpos = new BlockPos(blockpos$mutableblockpos);
                d0 = d1;
                continue;
            }
            if (!(d1 < d0)) continue;
            blockpos = new BlockPos(blockpos$mutableblockpos);
            d0 = d1;
        }
        return blockpos;
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        if (!this.ranBiomeCheck) {
            this.generatePositions();
            this.ranBiomeCheck = true;
        }
        for (ChunkPos chunkpos : this.structureCoords) {
            if (chunkX != chunkpos.x || chunkZ != chunkpos.z) continue;
            return true;
        }
        return false;
    }

    private void generatePositions() {
        this.initializeStructureData(this.worldObj);
        int i = 0;
        for (StructureStart structurestart : this.structureMap.values()) {
            if (i >= this.structureCoords.length) continue;
            this.structureCoords[i++] = new ChunkPos(structurestart.getChunkPosX(), structurestart.getChunkPosZ());
        }
        Random random = new Random();
        random.setSeed(this.worldObj.getSeed());
        double d1 = random.nextDouble() * Math.PI * 2.0;
        int j = 0;
        int k = 0;
        int l = this.structureMap.size();
        if (l < this.structureCoords.length) {
            for (int i1 = 0; i1 < this.structureCoords.length; ++i1) {
                double d0 = 4.0 * this.distance + this.distance * (double)j * 6.0 + (random.nextDouble() - 0.5) * this.distance * 2.5;
                int j1 = (int)Math.round(Math.cos(d1) * d0);
                int k1 = (int)Math.round(Math.sin(d1) * d0);
                BlockPos blockpos = this.worldObj.getBiomeProvider().findBiomePosition((j1 << 4) + 8, (k1 << 4) + 8, 112, this.allowedBiomes, random);
                if (blockpos != null) {
                    j1 = blockpos.getX() >> 4;
                    k1 = blockpos.getZ() >> 4;
                }
                if (i1 >= l) {
                    this.structureCoords[i1] = new ChunkPos(j1, k1);
                }
                d1 += Math.PI * 2 / (double)this.spread;
                if (++k != this.spread) continue;
                k = 0;
                this.spread += 2 * this.spread / (++j + 1);
                this.spread = Math.min(this.spread, this.structureCoords.length - i1);
                d1 += random.nextDouble() * Math.PI * 2.0;
            }
        }
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        Start mapgenstronghold$start = new Start(this.worldObj, this.rand, chunkX, chunkZ);
        while (mapgenstronghold$start.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)mapgenstronghold$start.getComponents().get((int)0)).strongholdPortalRoom == null) {
            mapgenstronghold$start = new Start(this.worldObj, this.rand, chunkX, chunkZ);
        }
        return mapgenstronghold$start;
    }

    public static class Start
    extends StructureStart {
        public Start() {
        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ) {
            super(chunkX, chunkZ);
            StructureStrongholdPieces.prepareStructurePieces();
            StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            this.components.add(structurestrongholdpieces$stairs2);
            structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, this.components, random);
            List<StructureComponent> list = structurestrongholdpieces$stairs2.pendingChildren;
            while (!list.isEmpty()) {
                int i = random.nextInt(list.size());
                StructureComponent structurecomponent = list.remove(i);
                structurecomponent.buildComponent(structurestrongholdpieces$stairs2, this.components, random);
            }
            this.updateBoundingBox();
            this.markAvailableHeight(worldIn, random, 10);
        }
    }
}

