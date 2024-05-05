package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public static class StructureVillagePieces.House4Garden
extends StructureVillagePieces.Village {
    private boolean isRoofAccessible;

    public StructureVillagePieces.House4Garden() {
    }

    public StructureVillagePieces.House4Garden(StructureVillagePieces.Start start, int p_i45566_2_, Random rand, StructureBoundingBox p_i45566_4_, EnumFacing facing) {
        super(start, p_i45566_2_);
        this.coordBaseMode = facing;
        this.boundingBox = p_i45566_4_;
        this.isRoofAccessible = rand.nextBoolean();
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setBoolean("Terrace", this.isRoofAccessible);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.isRoofAccessible = tagCompound.getBoolean("Terrace");
    }

    public static StructureVillagePieces.House4Garden func_175858_a(StructureVillagePieces.Start start, List<StructureComponent> p_175858_1_, Random rand, int p_175858_3_, int p_175858_4_, int p_175858_5_, EnumFacing facing, int p_175858_7_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175858_3_, (int)p_175858_4_, (int)p_175858_5_, (int)0, (int)0, (int)0, (int)5, (int)6, (int)5, (EnumFacing)facing);
        return StructureComponent.findIntersecting(p_175858_1_, (StructureBoundingBox)structureboundingbox) != null ? null : new StructureVillagePieces.House4Garden(start, p_175858_7_, rand, structureboundingbox, facing);
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
            if (this.field_143015_k < 0) {
                return true;
            }
            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 0, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 4, 4, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 3, 4, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 1, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 2, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 3, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 1, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 2, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 3, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 1, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 2, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 3, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 1, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 2, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 3, 4, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 4, 3, 3, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 1, 1, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 1, 2, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 1, 3, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 2, 3, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 3, 3, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 3, 2, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 3, 1, 0, structureBoundingBoxIn);
        if (this.getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock().getMaterial() != Material.air) {
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, structureBoundingBoxIn);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 3, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        if (this.isRoofAccessible) {
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 5, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 2, 5, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 3, 5, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 5, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 2, 5, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 3, 5, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 3, structureBoundingBoxIn);
        }
        if (this.isRoofAccessible) {
            int i = this.getMetadataWithOffset(Blocks.ladder, 3);
            this.setBlockState(worldIn, Blocks.ladder.getStateFromMeta(i), 3, 1, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.ladder.getStateFromMeta(i), 3, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.ladder.getStateFromMeta(i), 3, 3, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.ladder.getStateFromMeta(i), 3, 4, 3, structureBoundingBoxIn);
        }
        this.setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty((IProperty)BlockTorch.FACING, (Comparable)this.coordBaseMode), 2, 3, 1, structureBoundingBoxIn);
        for (int k = 0; k < 5; ++k) {
            for (int j = 0; j < 5; ++j) {
                this.clearCurrentPositionBlocksUpwards(worldIn, j, 6, k, structureBoundingBoxIn);
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.cobblestone.getDefaultState(), j, -1, k, structureBoundingBoxIn);
            }
        }
        this.spawnVillagers(worldIn, structureBoundingBoxIn, 1, 1, 2, 1);
        return true;
    }
}
