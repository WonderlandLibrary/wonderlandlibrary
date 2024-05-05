package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureMineshaftPieces.Cross
extends StructureComponent {
    private EnumFacing corridorDirection;
    private boolean isMultipleFloors;

    public StructureMineshaftPieces.Cross() {
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        tagCompound.setBoolean("tf", this.isMultipleFloors);
        tagCompound.setInteger("D", this.corridorDirection.getHorizontalIndex());
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        this.isMultipleFloors = tagCompound.getBoolean("tf");
        this.corridorDirection = EnumFacing.getHorizontal((int)tagCompound.getInteger("D"));
    }

    public StructureMineshaftPieces.Cross(int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing) {
        super(type);
        this.corridorDirection = facing;
        this.boundingBox = structurebb;
        this.isMultipleFloors = structurebb.getYSize() > 3;
    }

    public static StructureBoundingBox func_175813_a(List<StructureComponent> listIn, Random rand, int x, int y, int z, EnumFacing facing) {
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y, z, x, y + 2, z);
        if (rand.nextInt(4) == 0) {
            structureboundingbox.maxY += 4;
        }
        switch (StructureMineshaftPieces.1.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
            case 1: {
                structureboundingbox.minX = x - 1;
                structureboundingbox.maxX = x + 3;
                structureboundingbox.minZ = z - 4;
                break;
            }
            case 2: {
                structureboundingbox.minX = x - 1;
                structureboundingbox.maxX = x + 3;
                structureboundingbox.maxZ = z + 4;
                break;
            }
            case 3: {
                structureboundingbox.minX = x - 4;
                structureboundingbox.minZ = z - 1;
                structureboundingbox.maxZ = z + 3;
                break;
            }
            case 4: {
                structureboundingbox.maxX = x + 4;
                structureboundingbox.minZ = z - 1;
                structureboundingbox.maxZ = z + 3;
            }
        }
        return StructureComponent.findIntersecting(listIn, (StructureBoundingBox)structureboundingbox) != null ? null : structureboundingbox;
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        int i = this.getComponentType();
        switch (StructureMineshaftPieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.corridorDirection.ordinal()]) {
            case 1: {
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX + 1), (int)this.boundingBox.minY, (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)i);
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)this.boundingBox.minY, (int)(this.boundingBox.minZ + 1), (EnumFacing)EnumFacing.WEST, (int)i);
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)this.boundingBox.minY, (int)(this.boundingBox.minZ + 1), (EnumFacing)EnumFacing.EAST, (int)i);
                break;
            }
            case 2: {
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX + 1), (int)this.boundingBox.minY, (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)i);
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)this.boundingBox.minY, (int)(this.boundingBox.minZ + 1), (EnumFacing)EnumFacing.WEST, (int)i);
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)this.boundingBox.minY, (int)(this.boundingBox.minZ + 1), (EnumFacing)EnumFacing.EAST, (int)i);
                break;
            }
            case 3: {
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX + 1), (int)this.boundingBox.minY, (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)i);
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX + 1), (int)this.boundingBox.minY, (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)i);
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)this.boundingBox.minY, (int)(this.boundingBox.minZ + 1), (EnumFacing)EnumFacing.WEST, (int)i);
                break;
            }
            case 4: {
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX + 1), (int)this.boundingBox.minY, (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)i);
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX + 1), (int)this.boundingBox.minY, (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)i);
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)this.boundingBox.minY, (int)(this.boundingBox.minZ + 1), (EnumFacing)EnumFacing.EAST, (int)i);
            }
        }
        if (this.isMultipleFloors) {
            if (rand.nextBoolean()) {
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX + 1), (int)(this.boundingBox.minY + 3 + 1), (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)i);
            }
            if (rand.nextBoolean()) {
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)(this.boundingBox.minY + 3 + 1), (int)(this.boundingBox.minZ + 1), (EnumFacing)EnumFacing.WEST, (int)i);
            }
            if (rand.nextBoolean()) {
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)(this.boundingBox.minY + 3 + 1), (int)(this.boundingBox.minZ + 1), (EnumFacing)EnumFacing.EAST, (int)i);
            }
            if (rand.nextBoolean()) {
                StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX + 1), (int)(this.boundingBox.minY + 3 + 1), (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)i);
            }
        }
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        if (this.isMultipleFloors) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        } else {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
        for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
            for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                if (this.getBlockStateFromPos(worldIn, i, this.boundingBox.minY - 1, j, structureBoundingBoxIn).getBlock().getMaterial() != Material.air) continue;
                this.setBlockState(worldIn, Blocks.planks.getDefaultState(), i, this.boundingBox.minY - 1, j, structureBoundingBoxIn);
            }
        }
        return true;
    }
}
