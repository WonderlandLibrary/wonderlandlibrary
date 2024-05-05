package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;

public static class StructureNetherBridgePieces.End
extends StructureNetherBridgePieces.Piece {
    private int fillSeed;

    public StructureNetherBridgePieces.End() {
    }

    public StructureNetherBridgePieces.End(int p_i45621_1_, Random p_i45621_2_, StructureBoundingBox p_i45621_3_, EnumFacing p_i45621_4_) {
        super(p_i45621_1_);
        this.coordBaseMode = p_i45621_4_;
        this.boundingBox = p_i45621_3_;
        this.fillSeed = p_i45621_2_.nextInt();
    }

    public static StructureNetherBridgePieces.End func_175884_a(List<StructureComponent> p_175884_0_, Random p_175884_1_, int p_175884_2_, int p_175884_3_, int p_175884_4_, EnumFacing p_175884_5_, int p_175884_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175884_2_, (int)p_175884_3_, (int)p_175884_4_, (int)-1, (int)-3, (int)0, (int)5, (int)10, (int)8, (EnumFacing)p_175884_5_);
        return StructureNetherBridgePieces.End.isAboveGround((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175884_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureNetherBridgePieces.End(p_175884_6_, p_175884_1_, structureboundingbox, p_175884_5_) : null;
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.fillSeed = tagCompound.getInteger("Seed");
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setInteger("Seed", this.fillSeed);
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        Random random = new Random((long)this.fillSeed);
        for (int i = 0; i <= 4; ++i) {
            for (int j = 3; j <= 4; ++j) {
                int k = random.nextInt(8);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, i, j, 0, i, j, k, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            }
        }
        int l = random.nextInt(8);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 0, 5, l, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        l = random.nextInt(8);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 0, 4, 5, l, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        for (l = 0; l <= 4; ++l) {
            int i1 = random.nextInt(5);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, l, 2, 0, l, 2, i1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        }
        for (l = 0; l <= 4; ++l) {
            for (int j1 = 0; j1 <= 1; ++j1) {
                int k1 = random.nextInt(3);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, l, j1, 0, l, j1, k1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            }
        }
        return true;
    }
}
