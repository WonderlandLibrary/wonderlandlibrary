package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureStrongholdPieces.Crossing
extends StructureStrongholdPieces.Stronghold {
    private boolean field_74996_b;
    private boolean field_74997_c;
    private boolean field_74995_d;
    private boolean field_74999_h;

    public StructureStrongholdPieces.Crossing() {
    }

    public StructureStrongholdPieces.Crossing(int p_i45580_1_, Random p_i45580_2_, StructureBoundingBox p_i45580_3_, EnumFacing p_i45580_4_) {
        super(p_i45580_1_);
        this.coordBaseMode = p_i45580_4_;
        this.field_143013_d = this.getRandomDoor(p_i45580_2_);
        this.boundingBox = p_i45580_3_;
        this.field_74996_b = p_i45580_2_.nextBoolean();
        this.field_74997_c = p_i45580_2_.nextBoolean();
        this.field_74995_d = p_i45580_2_.nextBoolean();
        this.field_74999_h = p_i45580_2_.nextInt(3) > 0;
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setBoolean("leftLow", this.field_74996_b);
        tagCompound.setBoolean("leftHigh", this.field_74997_c);
        tagCompound.setBoolean("rightLow", this.field_74995_d);
        tagCompound.setBoolean("rightHigh", this.field_74999_h);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.field_74996_b = tagCompound.getBoolean("leftLow");
        this.field_74997_c = tagCompound.getBoolean("leftHigh");
        this.field_74995_d = tagCompound.getBoolean("rightLow");
        this.field_74999_h = tagCompound.getBoolean("rightHigh");
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        int i = 3;
        int j = 5;
        if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.NORTH) {
            i = 8 - i;
            j = 8 - j;
        }
        this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 5, 1);
        if (this.field_74996_b) {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, i, 1);
        }
        if (this.field_74997_c) {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, j, 7);
        }
        if (this.field_74995_d) {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, i, 1);
        }
        if (this.field_74999_h) {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, j, 7);
        }
    }

    public static StructureStrongholdPieces.Crossing func_175866_a(List<StructureComponent> p_175866_0_, Random p_175866_1_, int p_175866_2_, int p_175866_3_, int p_175866_4_, EnumFacing p_175866_5_, int p_175866_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175866_2_, (int)p_175866_3_, (int)p_175866_4_, (int)-4, (int)-3, (int)0, (int)10, (int)9, (int)11, (EnumFacing)p_175866_5_);
        return StructureStrongholdPieces.Crossing.canStrongholdGoDeeper((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175866_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureStrongholdPieces.Crossing(p_175866_6_, p_175866_1_, structureboundingbox, p_175866_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 9, 8, 10, true, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 4, 3, 0);
        if (this.field_74996_b) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 1, 0, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        }
        if (this.field_74995_d) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 3, 1, 9, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        }
        if (this.field_74997_c) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 7, 0, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        }
        if (this.field_74999_h) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 5, 7, 9, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 10, 7, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 2, 1, 8, 2, 6, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 5, 4, 4, 9, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 8, 1, 5, 8, 4, 9, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 4, 7, 3, 4, 9, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 5, 3, 3, 6, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 3, 3, 4, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 6, 3, 4, 6, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 1, 7, 7, 1, 8, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 9, 7, 1, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 7, 7, 2, 7, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 7, 4, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 7, 8, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 7, 7, 5, 9, Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.torch.getDefaultState(), 6, 5, 6, structureBoundingBoxIn);
        return true;
    }
}
