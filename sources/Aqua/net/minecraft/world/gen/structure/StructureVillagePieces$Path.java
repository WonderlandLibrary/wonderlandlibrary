package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureVillagePieces.Path
extends StructureVillagePieces.Road {
    private int length;

    public StructureVillagePieces.Path() {
    }

    public StructureVillagePieces.Path(StructureVillagePieces.Start start, int p_i45562_2_, Random rand, StructureBoundingBox p_i45562_4_, EnumFacing facing) {
        super(start, p_i45562_2_);
        this.coordBaseMode = facing;
        this.boundingBox = p_i45562_4_;
        this.length = Math.max((int)p_i45562_4_.getXSize(), (int)p_i45562_4_.getZSize());
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setInteger("Length", this.length);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.length = tagCompound.getInteger("Length");
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        boolean flag = false;
        for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5)) {
            StructureComponent structurecomponent = this.getNextComponentNN((StructureVillagePieces.Start)componentIn, listIn, rand, 0, i);
            if (structurecomponent == null) continue;
            i += Math.max((int)structurecomponent.boundingBox.getXSize(), (int)structurecomponent.boundingBox.getZSize());
            flag = true;
        }
        for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5)) {
            StructureComponent structurecomponent1 = this.getNextComponentPP((StructureVillagePieces.Start)componentIn, listIn, rand, 0, j);
            if (structurecomponent1 == null) continue;
            j += Math.max((int)structurecomponent1.boundingBox.getXSize(), (int)structurecomponent1.boundingBox.getZSize());
            flag = true;
        }
        if (flag && rand.nextInt(3) > 0 && this.coordBaseMode != null) {
            switch (StructureVillagePieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 1: {
                    StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)this.boundingBox.minY, (int)this.boundingBox.minZ, (EnumFacing)EnumFacing.WEST, (int)this.getComponentType());
                    break;
                }
                case 2: {
                    StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)this.boundingBox.minY, (int)(this.boundingBox.maxZ - 2), (EnumFacing)EnumFacing.WEST, (int)this.getComponentType());
                    break;
                }
                case 3: {
                    StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)this.boundingBox.minX, (int)this.boundingBox.minY, (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)this.getComponentType());
                    break;
                }
                case 4: {
                    StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)(this.boundingBox.maxX - 2), (int)this.boundingBox.minY, (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)this.getComponentType());
                }
            }
        }
        if (flag && rand.nextInt(3) > 0 && this.coordBaseMode != null) {
            switch (StructureVillagePieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 1: {
                    StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)this.boundingBox.minY, (int)this.boundingBox.minZ, (EnumFacing)EnumFacing.EAST, (int)this.getComponentType());
                    break;
                }
                case 2: {
                    StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)this.boundingBox.minY, (int)(this.boundingBox.maxZ - 2), (EnumFacing)EnumFacing.EAST, (int)this.getComponentType());
                    break;
                }
                case 3: {
                    StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)this.boundingBox.minX, (int)this.boundingBox.minY, (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)this.getComponentType());
                    break;
                }
                case 4: {
                    StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)(this.boundingBox.maxX - 2), (int)this.boundingBox.minY, (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)this.getComponentType());
                }
            }
        }
    }

    public static StructureBoundingBox func_175848_a(StructureVillagePieces.Start start, List<StructureComponent> p_175848_1_, Random rand, int p_175848_3_, int p_175848_4_, int p_175848_5_, EnumFacing facing) {
        for (int i = 7 * MathHelper.getRandomIntegerInRange((Random)rand, (int)3, (int)5); i >= 7; i -= 7) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175848_3_, (int)p_175848_4_, (int)p_175848_5_, (int)0, (int)0, (int)0, (int)3, (int)3, (int)i, (EnumFacing)facing);
            if (StructureComponent.findIntersecting(p_175848_1_, (StructureBoundingBox)structureboundingbox) != null) continue;
            return structureboundingbox;
        }
        return null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        IBlockState iblockstate = this.func_175847_a(Blocks.gravel.getDefaultState());
        IBlockState iblockstate1 = this.func_175847_a(Blocks.cobblestone.getDefaultState());
        for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
            for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                BlockPos blockpos = new BlockPos(i, 64, j);
                if (!structureBoundingBoxIn.isVecInside((Vec3i)blockpos)) continue;
                blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos).down();
                worldIn.setBlockState(blockpos, iblockstate, 2);
                worldIn.setBlockState(blockpos.down(), iblockstate1, 2);
            }
        }
        return true;
    }
}
