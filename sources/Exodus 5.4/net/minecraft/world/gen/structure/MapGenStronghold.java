/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

public class MapGenStronghold
extends MapGenStructure {
    private boolean ranBiomeCheck;
    private ChunkCoordIntPair[] structureCoords = new ChunkCoordIntPair[3];
    private int field_82672_i = 3;
    private List<BiomeGenBase> field_151546_e = Lists.newArrayList();
    private double field_82671_h = 32.0;

    @Override
    protected List<BlockPos> getCoordList() {
        ArrayList arrayList = Lists.newArrayList();
        ChunkCoordIntPair[] chunkCoordIntPairArray = this.structureCoords;
        int n = this.structureCoords.length;
        int n2 = 0;
        while (n2 < n) {
            ChunkCoordIntPair chunkCoordIntPair = chunkCoordIntPairArray[n2];
            if (chunkCoordIntPair != null) {
                arrayList.add(chunkCoordIntPair.getCenterBlock(64));
            }
            ++n2;
        }
        return arrayList;
    }

    public MapGenStronghold(Map<String, String> map) {
        this();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals("distance")) {
                this.field_82671_h = MathHelper.parseDoubleWithDefaultAndMax(entry.getValue(), this.field_82671_h, 1.0);
                continue;
            }
            if (entry.getKey().equals("count")) {
                this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.structureCoords.length, 1)];
                continue;
            }
            if (!entry.getKey().equals("spread")) continue;
            this.field_82672_i = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.field_82672_i, 1);
        }
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int n, int n2) {
        Object object;
        if (!this.ranBiomeCheck) {
            object = new Random();
            ((Random)object).setSeed(this.worldObj.getSeed());
            double d = ((Random)object).nextDouble() * Math.PI * 2.0;
            int n3 = 1;
            int n4 = 0;
            while (n4 < this.structureCoords.length) {
                double d2 = (1.25 * (double)n3 + ((Random)object).nextDouble()) * this.field_82671_h * (double)n3;
                int n5 = (int)Math.round(Math.cos(d) * d2);
                int n6 = (int)Math.round(Math.sin(d) * d2);
                BlockPos blockPos = this.worldObj.getWorldChunkManager().findBiomePosition((n5 << 4) + 8, (n6 << 4) + 8, 112, this.field_151546_e, (Random)object);
                if (blockPos != null) {
                    n5 = blockPos.getX() >> 4;
                    n6 = blockPos.getZ() >> 4;
                }
                this.structureCoords[n4] = new ChunkCoordIntPair(n5, n6);
                d += Math.PI * 2 * (double)n3 / (double)this.field_82672_i;
                if (n4 == this.field_82672_i) {
                    n3 += 2 + ((Random)object).nextInt(5);
                    this.field_82672_i += 1 + ((Random)object).nextInt(2);
                }
                ++n4;
            }
            this.ranBiomeCheck = true;
        }
        ChunkCoordIntPair[] chunkCoordIntPairArray = this.structureCoords;
        int n7 = this.structureCoords.length;
        int n8 = 0;
        while (n8 < n7) {
            object = chunkCoordIntPairArray[n8];
            if (n == ((ChunkCoordIntPair)object).chunkXPos && n2 == ((ChunkCoordIntPair)object).chunkZPos) {
                return true;
            }
            ++n8;
        }
        return false;
    }

    public MapGenStronghold() {
        BiomeGenBase[] biomeGenBaseArray = BiomeGenBase.getBiomeGenArray();
        int n = biomeGenBaseArray.length;
        int n2 = 0;
        while (n2 < n) {
            BiomeGenBase biomeGenBase = biomeGenBaseArray[n2];
            if (biomeGenBase != null && biomeGenBase.minHeight > 0.0f) {
                this.field_151546_e.add(biomeGenBase);
            }
            ++n2;
        }
    }

    @Override
    protected StructureStart getStructureStart(int n, int n2) {
        Start start = new Start(this.worldObj, this.rand, n, n2);
        while (start.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)start.getComponents().get((int)0)).strongholdPortalRoom == null) {
            start = new Start(this.worldObj, this.rand, n, n2);
        }
        return start;
    }

    @Override
    public String getStructureName() {
        return "Stronghold";
    }

    public static class Start
    extends StructureStart {
        public Start() {
        }

        public Start(World world, Random random, int n, int n2) {
            super(n, n2);
            StructureStrongholdPieces.prepareStructurePieces();
            StructureStrongholdPieces.Stairs2 stairs2 = new StructureStrongholdPieces.Stairs2(0, random, (n << 4) + 2, (n2 << 4) + 2);
            this.components.add(stairs2);
            stairs2.buildComponent(stairs2, this.components, random);
            List<StructureComponent> list = stairs2.field_75026_c;
            while (!list.isEmpty()) {
                int n3 = random.nextInt(list.size());
                StructureComponent structureComponent = list.remove(n3);
                structureComponent.buildComponent(stairs2, this.components, random);
            }
            this.updateBoundingBox();
            this.markAvailableHeight(world, random, 10);
        }
    }
}

