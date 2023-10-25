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
public static class StructureStrongholdPieces.Straight
extends StructureStrongholdPieces.Stronghold {
    private boolean expandsX;
    private boolean expandsZ;

    public StructureStrongholdPieces.Straight() {
    }

    public StructureStrongholdPieces.Straight(int p_i45573_1_, Random p_i45573_2_, StructureBoundingBox p_i45573_3_, EnumFacing p_i45573_4_) {
        super(p_i45573_1_);
        this.coordBaseMode = p_i45573_4_;
        this.field_143013_d = this.getRandomDoor(p_i45573_2_);
        this.boundingBox = p_i45573_3_;
        this.expandsX = p_i45573_2_.nextInt(2) == 0;
        this.expandsZ = p_i45573_2_.nextInt(2) == 0;
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setBoolean("Left", this.expandsX);
        tagCompound.setBoolean("Right", this.expandsZ);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.expandsX = tagCompound.getBoolean("Left");
        this.expandsZ = tagCompound.getBoolean("Right");
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
        if (this.expandsX) {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 2);
        }
        if (this.expandsZ) {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 2);
        }
    }

    public static StructureStrongholdPieces.Straight func_175862_a(List<StructureComponent> p_175862_0_, Random p_175862_1_, int p_175862_2_, int p_175862_3_, int p_175862_4_, EnumFacing p_175862_5_, int p_175862_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175862_2_, (int)p_175862_3_, (int)p_175862_4_, (int)-1, (int)-1, (int)0, (int)5, (int)5, (int)7, (EnumFacing)p_175862_5_);
        return StructureStrongholdPieces.Straight.canStrongholdGoDeeper((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175862_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureStrongholdPieces.Straight(p_175862_6_, p_175862_1_, structureboundingbox, p_175862_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 4, 6, true, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
        this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 1, 2, 1, Blocks.torch.getDefaultState());
        this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 3, 2, 1, Blocks.torch.getDefaultState());
        this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 1, 2, 5, Blocks.torch.getDefaultState());
        this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 3, 2, 5, Blocks.torch.getDefaultState());
        if (this.expandsX) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 2, 0, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        }
        if (this.expandsZ) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 2, 4, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        }
        return true;
    }
}
