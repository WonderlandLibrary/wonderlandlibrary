package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public static class StructureVillagePieces.Start
extends StructureVillagePieces.Well {
    public WorldChunkManager worldChunkMngr;
    public boolean inDesert;
    public int terrainType;
    public StructureVillagePieces.PieceWeight structVillagePieceWeight;
    public List<StructureVillagePieces.PieceWeight> structureVillageWeightedPieceList;
    public List<StructureComponent> field_74932_i = Lists.newArrayList();
    public List<StructureComponent> field_74930_j = Lists.newArrayList();

    public StructureVillagePieces.Start() {
    }

    public StructureVillagePieces.Start(WorldChunkManager chunkManagerIn, int p_i2104_2_, Random rand, int p_i2104_4_, int p_i2104_5_, List<StructureVillagePieces.PieceWeight> p_i2104_6_, int p_i2104_7_) {
        super((StructureVillagePieces.Start)null, 0, rand, p_i2104_4_, p_i2104_5_);
        this.worldChunkMngr = chunkManagerIn;
        this.structureVillageWeightedPieceList = p_i2104_6_;
        this.terrainType = p_i2104_7_;
        BiomeGenBase biomegenbase = chunkManagerIn.getBiomeGenerator(new BlockPos(p_i2104_4_, 0, p_i2104_5_), BiomeGenBase.field_180279_ad);
        this.inDesert = biomegenbase == BiomeGenBase.desert || biomegenbase == BiomeGenBase.desertHills;
        this.func_175846_a(this.inDesert);
    }

    public WorldChunkManager getWorldChunkManager() {
        return this.worldChunkMngr;
    }
}
