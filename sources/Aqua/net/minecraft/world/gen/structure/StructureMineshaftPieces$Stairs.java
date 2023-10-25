package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
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
public static class StructureMineshaftPieces.Stairs
extends StructureComponent {
    public StructureMineshaftPieces.Stairs() {
    }

    public StructureMineshaftPieces.Stairs(int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing) {
        super(type);
        this.coordBaseMode = facing;
        this.boundingBox = structurebb;
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
    }

    public static StructureBoundingBox func_175812_a(List<StructureComponent> listIn, Random rand, int x, int y, int z, EnumFacing facing) {
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y - 5, z, x, y + 2, z);
        switch (StructureMineshaftPieces.1.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
            case 1: {
                structureboundingbox.maxX = x + 2;
                structureboundingbox.minZ = z - 8;
                break;
            }
            case 2: {
                structureboundingbox.maxX = x + 2;
                structureboundingbox.maxZ = z + 8;
                break;
            }
            case 3: {
                structureboundingbox.minX = x - 8;
                structureboundingbox.maxZ = z + 2;
                break;
            }
            case 4: {
                structureboundingbox.maxX = x + 8;
                structureboundingbox.maxZ = z + 2;
            }
        }
        return StructureComponent.findIntersecting(listIn, (StructureBoundingBox)structureboundingbox) != null ? null : structureboundingbox;
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        int i = this.getComponentType();
        if (this.coordBaseMode != null) {
            switch (StructureMineshaftPieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 1: {
                    StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)this.boundingBox.minX, (int)this.boundingBox.minY, (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)i);
                    break;
                }
                case 2: {
                    StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)this.boundingBox.minX, (int)this.boundingBox.minY, (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)i);
                    break;
                }
                case 3: {
                    StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)this.boundingBox.minY, (int)this.boundingBox.minZ, (EnumFacing)EnumFacing.WEST, (int)i);
                    break;
                }
                case 4: {
                    StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)this.boundingBox.minY, (int)this.boundingBox.minZ, (EnumFacing)EnumFacing.EAST, (int)i);
                }
            }
        }
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 2, 7, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 7, 2, 2, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        for (int i = 0; i < 5; ++i) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        }
        return true;
    }
}
