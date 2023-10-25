package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

static abstract class ComponentScatteredFeaturePieces.Feature
extends StructureComponent {
    protected int scatteredFeatureSizeX;
    protected int scatteredFeatureSizeY;
    protected int scatteredFeatureSizeZ;
    protected int field_74936_d = -1;

    public ComponentScatteredFeaturePieces.Feature() {
    }

    protected ComponentScatteredFeaturePieces.Feature(Random p_i2065_1_, int p_i2065_2_, int p_i2065_3_, int p_i2065_4_, int p_i2065_5_, int p_i2065_6_, int p_i2065_7_) {
        super(0);
        this.scatteredFeatureSizeX = p_i2065_5_;
        this.scatteredFeatureSizeY = p_i2065_6_;
        this.scatteredFeatureSizeZ = p_i2065_7_;
        this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(p_i2065_1_);
        switch (ComponentScatteredFeaturePieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
            case 1: 
            case 2: {
                this.boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_, p_i2065_4_, p_i2065_2_ + p_i2065_5_ - 1, p_i2065_3_ + p_i2065_6_ - 1, p_i2065_4_ + p_i2065_7_ - 1);
                break;
            }
            default: {
                this.boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_, p_i2065_4_, p_i2065_2_ + p_i2065_7_ - 1, p_i2065_3_ + p_i2065_6_ - 1, p_i2065_4_ + p_i2065_5_ - 1);
            }
        }
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("Width", this.scatteredFeatureSizeX);
        tagCompound.setInteger("Height", this.scatteredFeatureSizeY);
        tagCompound.setInteger("Depth", this.scatteredFeatureSizeZ);
        tagCompound.setInteger("HPos", this.field_74936_d);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        this.scatteredFeatureSizeX = tagCompound.getInteger("Width");
        this.scatteredFeatureSizeY = tagCompound.getInteger("Height");
        this.scatteredFeatureSizeZ = tagCompound.getInteger("Depth");
        this.field_74936_d = tagCompound.getInteger("HPos");
    }

    protected boolean func_74935_a(World worldIn, StructureBoundingBox p_74935_2_, int p_74935_3_) {
        if (this.field_74936_d >= 0) {
            return true;
        }
        int i = 0;
        int j = 0;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
            for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
                blockpos$mutableblockpos.set(l, 64, k);
                if (!p_74935_2_.isVecInside((Vec3i)blockpos$mutableblockpos)) continue;
                i += Math.max((int)worldIn.getTopSolidOrLiquidBlock((BlockPos)blockpos$mutableblockpos).getY(), (int)worldIn.provider.getAverageGroundLevel());
                ++j;
            }
        }
        if (j == 0) {
            return false;
        }
        this.field_74936_d = i / j;
        this.boundingBox.offset(0, this.field_74936_d - this.boundingBox.minY + p_74935_3_, 0);
        return true;
    }
}
