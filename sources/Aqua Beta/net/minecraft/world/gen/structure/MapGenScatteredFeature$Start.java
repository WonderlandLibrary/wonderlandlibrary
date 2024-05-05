package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.StructureStart;

public static class MapGenScatteredFeature.Start
extends StructureStart {
    public MapGenScatteredFeature.Start() {
    }

    public MapGenScatteredFeature.Start(World worldIn, Random p_i2060_2_, int p_i2060_3_, int p_i2060_4_) {
        super(p_i2060_3_, p_i2060_4_);
        BiomeGenBase biomegenbase = worldIn.getBiomeGenForCoords(new BlockPos(p_i2060_3_ * 16 + 8, 0, p_i2060_4_ * 16 + 8));
        if (biomegenbase != BiomeGenBase.jungle && biomegenbase != BiomeGenBase.jungleHills) {
            if (biomegenbase == BiomeGenBase.swampland) {
                ComponentScatteredFeaturePieces.SwampHut componentscatteredfeaturepieces$swamphut = new ComponentScatteredFeaturePieces.SwampHut(p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
                this.components.add((Object)componentscatteredfeaturepieces$swamphut);
            } else if (biomegenbase == BiomeGenBase.desert || biomegenbase == BiomeGenBase.desertHills) {
                ComponentScatteredFeaturePieces.DesertPyramid componentscatteredfeaturepieces$desertpyramid = new ComponentScatteredFeaturePieces.DesertPyramid(p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
                this.components.add((Object)componentscatteredfeaturepieces$desertpyramid);
            }
        } else {
            ComponentScatteredFeaturePieces.JunglePyramid componentscatteredfeaturepieces$junglepyramid = new ComponentScatteredFeaturePieces.JunglePyramid(p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
            this.components.add((Object)componentscatteredfeaturepieces$junglepyramid);
        }
        this.updateBoundingBox();
    }
}
