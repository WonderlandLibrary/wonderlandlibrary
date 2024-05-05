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
public static class StructureStrongholdPieces.Prison
extends StructureStrongholdPieces.Stronghold {
    public StructureStrongholdPieces.Prison() {
    }

    public StructureStrongholdPieces.Prison(int p_i45576_1_, Random p_i45576_2_, StructureBoundingBox p_i45576_3_, EnumFacing p_i45576_4_) {
        super(p_i45576_1_);
        this.coordBaseMode = p_i45576_4_;
        this.field_143013_d = this.getRandomDoor(p_i45576_2_);
        this.boundingBox = p_i45576_3_;
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
    }

    public static StructureStrongholdPieces.Prison func_175860_a(List<StructureComponent> p_175860_0_, Random p_175860_1_, int p_175860_2_, int p_175860_3_, int p_175860_4_, EnumFacing p_175860_5_, int p_175860_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175860_2_, (int)p_175860_3_, (int)p_175860_4_, (int)-1, (int)-1, (int)0, (int)9, (int)5, (int)11, (EnumFacing)p_175860_5_);
        return StructureStrongholdPieces.Prison.canStrongholdGoDeeper((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175860_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureStrongholdPieces.Prison(p_175860_6_, p_175860_1_, structureboundingbox, p_175860_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 4, 10, true, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 10, 3, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 3, 1, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 3, 4, 3, 3, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 7, 4, 3, 7, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 4, 3, 9, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 4, 4, 3, 6, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 5, 7, 3, 5, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), 4, 3, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), 4, 3, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 8, structureBoundingBoxIn);
        return true;
    }
}
