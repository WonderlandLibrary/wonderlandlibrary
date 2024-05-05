package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;

public static class StructureNetherBridgePieces.Corridor
extends StructureNetherBridgePieces.Piece {
    private boolean field_111021_b;

    public StructureNetherBridgePieces.Corridor() {
    }

    public StructureNetherBridgePieces.Corridor(int p_i45615_1_, Random p_i45615_2_, StructureBoundingBox p_i45615_3_, EnumFacing p_i45615_4_) {
        super(p_i45615_1_);
        this.coordBaseMode = p_i45615_4_;
        this.boundingBox = p_i45615_3_;
        this.field_111021_b = p_i45615_2_.nextInt(3) == 0;
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.field_111021_b = tagCompound.getBoolean("Chest");
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setBoolean("Chest", this.field_111021_b);
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        this.getNextComponentX((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 0, 1, true);
    }

    public static StructureNetherBridgePieces.Corridor func_175879_a(List<StructureComponent> p_175879_0_, Random p_175879_1_, int p_175879_2_, int p_175879_3_, int p_175879_4_, EnumFacing p_175879_5_, int p_175879_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175879_2_, (int)p_175879_3_, (int)p_175879_4_, (int)-1, (int)0, (int)0, (int)5, (int)7, (int)5, (EnumFacing)p_175879_5_);
        return StructureNetherBridgePieces.Corridor.isAboveGround((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175879_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor(p_175879_6_, p_175879_1_, structureboundingbox, p_175879_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 4, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 1, 4, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 3, 4, 4, 3, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 3, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 1, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 4, 3, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        if (this.field_111021_b && structureBoundingBoxIn.isVecInside((Vec3i)new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3)))) {
            this.field_111021_b = false;
            this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 3, 2, 3, field_111019_a, 2 + randomIn.nextInt(4));
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        for (int i = 0; i <= 4; ++i) {
            for (int j = 0; j <= 4; ++j) {
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
            }
        }
        return true;
    }
}
