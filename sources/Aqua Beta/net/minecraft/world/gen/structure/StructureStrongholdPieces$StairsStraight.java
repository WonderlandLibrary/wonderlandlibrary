package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureStrongholdPieces.StairsStraight
extends StructureStrongholdPieces.Stronghold {
    public StructureStrongholdPieces.StairsStraight() {
    }

    public StructureStrongholdPieces.StairsStraight(int p_i45572_1_, Random p_i45572_2_, StructureBoundingBox p_i45572_3_, EnumFacing p_i45572_4_) {
        super(p_i45572_1_);
        this.coordBaseMode = p_i45572_4_;
        this.field_143013_d = this.getRandomDoor(p_i45572_2_);
        this.boundingBox = p_i45572_3_;
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
    }

    public static StructureStrongholdPieces.StairsStraight func_175861_a(List<StructureComponent> p_175861_0_, Random p_175861_1_, int p_175861_2_, int p_175861_3_, int p_175861_4_, EnumFacing p_175861_5_, int p_175861_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175861_2_, (int)p_175861_3_, (int)p_175861_4_, (int)-1, (int)-7, (int)0, (int)5, (int)11, (int)8, (EnumFacing)p_175861_5_);
        return StructureStrongholdPieces.StairsStraight.canStrongholdGoDeeper((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175861_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureStrongholdPieces.StairsStraight(p_175861_6_, p_175861_1_, structureboundingbox, p_175861_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 10, 7, true, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 7, 0);
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 7);
        int i = this.getMetadataWithOffset(Blocks.stone_stairs, 2);
        for (int j = 0; j < 6; ++j) {
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 1, 6 - j, 1 + j, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 2, 6 - j, 1 + j, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 3, 6 - j, 1 + j, structureBoundingBoxIn);
            if (j >= 5) continue;
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5 - j, 1 + j, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 5 - j, 1 + j, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 5 - j, 1 + j, structureBoundingBoxIn);
        }
        return true;
    }
}
