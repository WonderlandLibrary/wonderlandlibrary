package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

/*
 * Exception performing whole class analysis ignored.
 */
static abstract class StructureVillagePieces.Village
extends StructureComponent {
    protected int field_143015_k = -1;
    private int villagersSpawned;
    private boolean isDesertVillage;

    public StructureVillagePieces.Village() {
    }

    protected StructureVillagePieces.Village(StructureVillagePieces.Start start, int type) {
        super(type);
        if (start != null) {
            this.isDesertVillage = start.inDesert;
        }
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("HPos", this.field_143015_k);
        tagCompound.setInteger("VCount", this.villagersSpawned);
        tagCompound.setBoolean("Desert", this.isDesertVillage);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        this.field_143015_k = tagCompound.getInteger("HPos");
        this.villagersSpawned = tagCompound.getInteger("VCount");
        this.isDesertVillage = tagCompound.getBoolean("Desert");
    }

    protected StructureComponent getNextComponentNN(StructureVillagePieces.Start start, List<StructureComponent> p_74891_2_, Random rand, int p_74891_4_, int p_74891_5_) {
        if (this.coordBaseMode != null) {
            switch (StructureVillagePieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 1: {
                    return StructureVillagePieces.access$100((StructureVillagePieces.Start)start, p_74891_2_, (Random)rand, (int)(this.boundingBox.minX - 1), (int)(this.boundingBox.minY + p_74891_4_), (int)(this.boundingBox.minZ + p_74891_5_), (EnumFacing)EnumFacing.WEST, (int)this.getComponentType());
                }
                case 2: {
                    return StructureVillagePieces.access$100((StructureVillagePieces.Start)start, p_74891_2_, (Random)rand, (int)(this.boundingBox.minX - 1), (int)(this.boundingBox.minY + p_74891_4_), (int)(this.boundingBox.minZ + p_74891_5_), (EnumFacing)EnumFacing.WEST, (int)this.getComponentType());
                }
                case 3: {
                    return StructureVillagePieces.access$100((StructureVillagePieces.Start)start, p_74891_2_, (Random)rand, (int)(this.boundingBox.minX + p_74891_5_), (int)(this.boundingBox.minY + p_74891_4_), (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)this.getComponentType());
                }
                case 4: {
                    return StructureVillagePieces.access$100((StructureVillagePieces.Start)start, p_74891_2_, (Random)rand, (int)(this.boundingBox.minX + p_74891_5_), (int)(this.boundingBox.minY + p_74891_4_), (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)this.getComponentType());
                }
            }
        }
        return null;
    }

    protected StructureComponent getNextComponentPP(StructureVillagePieces.Start start, List<StructureComponent> p_74894_2_, Random rand, int p_74894_4_, int p_74894_5_) {
        if (this.coordBaseMode != null) {
            switch (StructureVillagePieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 1: {
                    return StructureVillagePieces.access$100((StructureVillagePieces.Start)start, p_74894_2_, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)(this.boundingBox.minY + p_74894_4_), (int)(this.boundingBox.minZ + p_74894_5_), (EnumFacing)EnumFacing.EAST, (int)this.getComponentType());
                }
                case 2: {
                    return StructureVillagePieces.access$100((StructureVillagePieces.Start)start, p_74894_2_, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)(this.boundingBox.minY + p_74894_4_), (int)(this.boundingBox.minZ + p_74894_5_), (EnumFacing)EnumFacing.EAST, (int)this.getComponentType());
                }
                case 3: {
                    return StructureVillagePieces.access$100((StructureVillagePieces.Start)start, p_74894_2_, (Random)rand, (int)(this.boundingBox.minX + p_74894_5_), (int)(this.boundingBox.minY + p_74894_4_), (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)this.getComponentType());
                }
                case 4: {
                    return StructureVillagePieces.access$100((StructureVillagePieces.Start)start, p_74894_2_, (Random)rand, (int)(this.boundingBox.minX + p_74894_5_), (int)(this.boundingBox.minY + p_74894_4_), (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)this.getComponentType());
                }
            }
        }
        return null;
    }

    protected int getAverageGroundLevel(World worldIn, StructureBoundingBox p_74889_2_) {
        int i = 0;
        int j = 0;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
            for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
                blockpos$mutableblockpos.set(l, 64, k);
                if (!p_74889_2_.isVecInside((Vec3i)blockpos$mutableblockpos)) continue;
                i += Math.max((int)worldIn.getTopSolidOrLiquidBlock((BlockPos)blockpos$mutableblockpos).getY(), (int)worldIn.provider.getAverageGroundLevel());
                ++j;
            }
        }
        if (j == 0) {
            return -1;
        }
        return i / j;
    }

    protected static boolean canVillageGoDeeper(StructureBoundingBox p_74895_0_) {
        return p_74895_0_ != null && p_74895_0_.minY > 10;
    }

    protected void spawnVillagers(World worldIn, StructureBoundingBox p_74893_2_, int p_74893_3_, int p_74893_4_, int p_74893_5_, int p_74893_6_) {
        if (this.villagersSpawned < p_74893_6_) {
            int l;
            int k;
            int j;
            for (int i = this.villagersSpawned; i < p_74893_6_ && p_74893_2_.isVecInside((Vec3i)new BlockPos(j = this.getXWithOffset(p_74893_3_ + i, p_74893_5_), k = this.getYWithOffset(p_74893_4_), l = this.getZWithOffset(p_74893_3_ + i, p_74893_5_))); ++i) {
                ++this.villagersSpawned;
                EntityVillager entityvillager = new EntityVillager(worldIn);
                entityvillager.setLocationAndAngles((double)j + 0.5, (double)k, (double)l + 0.5, 0.0f, 0.0f);
                entityvillager.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos((Entity)entityvillager)), (IEntityLivingData)null);
                entityvillager.setProfession(this.func_180779_c(i, entityvillager.getProfession()));
                worldIn.spawnEntityInWorld((Entity)entityvillager);
            }
        }
    }

    protected int func_180779_c(int p_180779_1_, int p_180779_2_) {
        return p_180779_2_;
    }

    protected IBlockState func_175847_a(IBlockState p_175847_1_) {
        if (this.isDesertVillage) {
            if (p_175847_1_.getBlock() == Blocks.log || p_175847_1_.getBlock() == Blocks.log2) {
                return Blocks.sandstone.getDefaultState();
            }
            if (p_175847_1_.getBlock() == Blocks.cobblestone) {
                return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
            }
            if (p_175847_1_.getBlock() == Blocks.planks) {
                return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
            }
            if (p_175847_1_.getBlock() == Blocks.oak_stairs) {
                return Blocks.sandstone_stairs.getDefaultState().withProperty((IProperty)BlockStairs.FACING, p_175847_1_.getValue((IProperty)BlockStairs.FACING));
            }
            if (p_175847_1_.getBlock() == Blocks.stone_stairs) {
                return Blocks.sandstone_stairs.getDefaultState().withProperty((IProperty)BlockStairs.FACING, p_175847_1_.getValue((IProperty)BlockStairs.FACING));
            }
            if (p_175847_1_.getBlock() == Blocks.gravel) {
                return Blocks.sandstone.getDefaultState();
            }
        }
        return p_175847_1_;
    }

    protected void setBlockState(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
        IBlockState iblockstate = this.func_175847_a(blockstateIn);
        super.setBlockState(worldIn, iblockstate, x, y, z, boundingboxIn);
    }

    protected void fillWithBlocks(World worldIn, StructureBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, IBlockState boundaryBlockState, IBlockState insideBlockState, boolean existingOnly) {
        IBlockState iblockstate = this.func_175847_a(boundaryBlockState);
        IBlockState iblockstate1 = this.func_175847_a(insideBlockState);
        super.fillWithBlocks(worldIn, boundingboxIn, xMin, yMin, zMin, xMax, yMax, zMax, iblockstate, iblockstate1, existingOnly);
    }

    protected void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
        IBlockState iblockstate = this.func_175847_a(blockstateIn);
        super.replaceAirAndLiquidDownwards(worldIn, iblockstate, x, y, z, boundingboxIn);
    }

    protected void func_175846_a(boolean p_175846_1_) {
        this.isDesertVillage = p_175846_1_;
    }
}
