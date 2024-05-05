package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;

public static class StructureNetherBridgePieces.Throne
extends StructureNetherBridgePieces.Piece {
    private boolean hasSpawner;

    public StructureNetherBridgePieces.Throne() {
    }

    public StructureNetherBridgePieces.Throne(int p_i45611_1_, Random p_i45611_2_, StructureBoundingBox p_i45611_3_, EnumFacing p_i45611_4_) {
        super(p_i45611_1_);
        this.coordBaseMode = p_i45611_4_;
        this.boundingBox = p_i45611_3_;
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.hasSpawner = tagCompound.getBoolean("Mob");
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setBoolean("Mob", this.hasSpawner);
    }

    public static StructureNetherBridgePieces.Throne func_175874_a(List<StructureComponent> p_175874_0_, Random p_175874_1_, int p_175874_2_, int p_175874_3_, int p_175874_4_, int p_175874_5_, EnumFacing p_175874_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175874_2_, (int)p_175874_3_, (int)p_175874_4_, (int)-2, (int)0, (int)0, (int)7, (int)8, (int)9, (EnumFacing)p_175874_6_);
        return StructureNetherBridgePieces.Throne.isAboveGround((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175874_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureNetherBridgePieces.Throne(p_175874_5_, p_175874_1_, structureboundingbox, p_175874_6_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        BlockPos blockpos;
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 6, 7, 7, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 5, 1, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 1, 5, 2, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 2, 5, 3, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 3, 5, 4, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 1, 4, 2, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 0, 5, 4, 2, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 2, 1, 5, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 2, 5, 5, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 3, 0, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 5, 3, 6, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 8, 5, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 1, 6, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 5, 6, 3, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 3, 0, 6, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 6, 3, 6, 6, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 8, 5, 7, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 8, 8, 4, 8, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        if (!this.hasSpawner && structureBoundingBoxIn.isVecInside((Vec3i)(blockpos = new BlockPos(this.getXWithOffset(3, 5), this.getYWithOffset(5), this.getZWithOffset(3, 5))))) {
            this.hasSpawner = true;
            worldIn.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
            TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof TileEntityMobSpawner) {
                ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityName("Blaze");
            }
        }
        for (int i = 0; i <= 6; ++i) {
            for (int j = 0; j <= 6; ++j) {
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
            }
        }
        return true;
    }
}
