package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

public class MapGenStronghold
extends MapGenStructure {
    private List<BiomeGenBase> field_151546_e;
    private boolean ranBiomeCheck;
    private ChunkCoordIntPair[] structureCoords = new ChunkCoordIntPair[3];
    private double field_82671_h = 32.0;
    private int field_82672_i = 3;

    public MapGenStronghold() {
        this.field_151546_e = Lists.newArrayList();
        for (BiomeGenBase biomegenbase : BiomeGenBase.getBiomeGenArray()) {
            if (biomegenbase == null || !(biomegenbase.minHeight > 0.0f)) continue;
            this.field_151546_e.add((Object)biomegenbase);
        }
    }

    public MapGenStronghold(Map<String, String> p_i2068_1_) {
        this();
        for (Map.Entry entry : p_i2068_1_.entrySet()) {
            if (((String)entry.getKey()).equals((Object)"distance")) {
                this.field_82671_h = MathHelper.parseDoubleWithDefaultAndMax((String)((String)entry.getValue()), (double)this.field_82671_h, (double)1.0);
                continue;
            }
            if (((String)entry.getKey()).equals((Object)"count")) {
                this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax((String)((String)entry.getValue()), (int)this.structureCoords.length, (int)1)];
                continue;
            }
            if (!((String)entry.getKey()).equals((Object)"spread")) continue;
            this.field_82672_i = MathHelper.parseIntWithDefaultAndMax((String)((String)entry.getValue()), (int)this.field_82672_i, (int)1);
        }
    }

    public String getStructureName() {
        return "Stronghold";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        if (!this.ranBiomeCheck) {
            Random random = new Random();
            random.setSeed(this.worldObj.getSeed());
            double d0 = random.nextDouble() * Math.PI * 2.0;
            int i = 1;
            for (int j = 0; j < this.structureCoords.length; ++j) {
                double d1 = (1.25 * (double)i + random.nextDouble()) * this.field_82671_h * (double)i;
                int k = (int)Math.round((double)(Math.cos((double)d0) * d1));
                int l = (int)Math.round((double)(Math.sin((double)d0) * d1));
                BlockPos blockpos = this.worldObj.getWorldChunkManager().findBiomePosition((k << 4) + 8, (l << 4) + 8, 112, this.field_151546_e, random);
                if (blockpos != null) {
                    k = blockpos.getX() >> 4;
                    l = blockpos.getZ() >> 4;
                }
                this.structureCoords[j] = new ChunkCoordIntPair(k, l);
                d0 += Math.PI * 2 * (double)i / (double)this.field_82672_i;
                if (j != this.field_82672_i) continue;
                i += 2 + random.nextInt(5);
                this.field_82672_i += 1 + random.nextInt(2);
            }
            this.ranBiomeCheck = true;
        }
        for (ChunkCoordIntPair chunkcoordintpair : this.structureCoords) {
            if (chunkX != chunkcoordintpair.chunkXPos || chunkZ != chunkcoordintpair.chunkZPos) continue;
            return true;
        }
        return false;
    }

    protected List<BlockPos> getCoordList() {
        ArrayList list = Lists.newArrayList();
        for (ChunkCoordIntPair chunkcoordintpair : this.structureCoords) {
            if (chunkcoordintpair == null) continue;
            list.add((Object)chunkcoordintpair.getCenterBlock(64));
        }
        return list;
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        Start mapgenstronghold$start = new Start(this.worldObj, this.rand, chunkX, chunkZ);
        while (mapgenstronghold$start.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)mapgenstronghold$start.getComponents().get((int)0)).strongholdPortalRoom == null) {
            mapgenstronghold$start = new Start(this.worldObj, this.rand, chunkX, chunkZ);
        }
        return mapgenstronghold$start;
    }
}
