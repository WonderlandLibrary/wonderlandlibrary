package net.minecraft.world.gen.structure;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

public static abstract class StructureOceanMonumentPieces.Piece
extends StructureComponent {
    protected static final IBlockState field_175828_a = Blocks.prismarine.getStateFromMeta(BlockPrismarine.ROUGH_META);
    protected static final IBlockState field_175826_b = Blocks.prismarine.getStateFromMeta(BlockPrismarine.BRICKS_META);
    protected static final IBlockState field_175827_c = Blocks.prismarine.getStateFromMeta(BlockPrismarine.DARK_META);
    protected static final IBlockState field_175824_d = field_175826_b;
    protected static final IBlockState field_175825_e = Blocks.sea_lantern.getDefaultState();
    protected static final IBlockState field_175822_f = Blocks.water.getDefaultState();
    protected static final int field_175823_g = StructureOceanMonumentPieces.Piece.func_175820_a(2, 0, 0);
    protected static final int field_175831_h = StructureOceanMonumentPieces.Piece.func_175820_a(2, 2, 0);
    protected static final int field_175832_i = StructureOceanMonumentPieces.Piece.func_175820_a(0, 1, 0);
    protected static final int field_175829_j = StructureOceanMonumentPieces.Piece.func_175820_a(4, 1, 0);
    protected StructureOceanMonumentPieces.RoomDefinition field_175830_k;

    protected static final int func_175820_a(int p_175820_0_, int p_175820_1_, int p_175820_2_) {
        return p_175820_1_ * 25 + p_175820_2_ * 5 + p_175820_0_;
    }

    public StructureOceanMonumentPieces.Piece() {
        super(0);
    }

    public StructureOceanMonumentPieces.Piece(int p_i45588_1_) {
        super(p_i45588_1_);
    }

    public StructureOceanMonumentPieces.Piece(EnumFacing p_i45589_1_, StructureBoundingBox p_i45589_2_) {
        super(1);
        this.coordBaseMode = p_i45589_1_;
        this.boundingBox = p_i45589_2_;
    }

    protected StructureOceanMonumentPieces.Piece(int p_i45590_1_, EnumFacing p_i45590_2_, StructureOceanMonumentPieces.RoomDefinition p_i45590_3_, int p_i45590_4_, int p_i45590_5_, int p_i45590_6_) {
        super(p_i45590_1_);
        this.coordBaseMode = p_i45590_2_;
        this.field_175830_k = p_i45590_3_;
        int i = p_i45590_3_.field_175967_a;
        int j = i % 5;
        int k = i / 5 % 5;
        int l = i / 25;
        this.boundingBox = p_i45590_2_ != EnumFacing.NORTH && p_i45590_2_ != EnumFacing.SOUTH ? new StructureBoundingBox(0, 0, 0, p_i45590_6_ * 8 - 1, p_i45590_5_ * 4 - 1, p_i45590_4_ * 8 - 1) : new StructureBoundingBox(0, 0, 0, p_i45590_4_ * 8 - 1, p_i45590_5_ * 4 - 1, p_i45590_6_ * 8 - 1);
        switch (StructureOceanMonumentPieces.1.$SwitchMap$net$minecraft$util$EnumFacing[p_i45590_2_.ordinal()]) {
            case 1: {
                this.boundingBox.offset(j * 8, l * 4, -(k + p_i45590_6_) * 8 + 1);
                break;
            }
            case 2: {
                this.boundingBox.offset(j * 8, l * 4, k * 8);
                break;
            }
            case 3: {
                this.boundingBox.offset(-(k + p_i45590_6_) * 8 + 1, l * 4, j * 8);
                break;
            }
            default: {
                this.boundingBox.offset(k * 8, l * 4, j * 8);
            }
        }
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
    }

    protected void func_181655_a(World p_181655_1_, StructureBoundingBox p_181655_2_, int p_181655_3_, int p_181655_4_, int p_181655_5_, int p_181655_6_, int p_181655_7_, int p_181655_8_, boolean p_181655_9_) {
        for (int i = p_181655_4_; i <= p_181655_7_; ++i) {
            for (int j = p_181655_3_; j <= p_181655_6_; ++j) {
                for (int k = p_181655_5_; k <= p_181655_8_; ++k) {
                    if (p_181655_9_ && this.getBlockStateFromPos(p_181655_1_, j, i, k, p_181655_2_).getBlock().getMaterial() == Material.air) continue;
                    if (this.getYWithOffset(i) >= p_181655_1_.getSeaLevel()) {
                        this.setBlockState(p_181655_1_, Blocks.air.getDefaultState(), j, i, k, p_181655_2_);
                        continue;
                    }
                    this.setBlockState(p_181655_1_, field_175822_f, j, i, k, p_181655_2_);
                }
            }
        }
    }

    protected void func_175821_a(World worldIn, StructureBoundingBox p_175821_2_, int p_175821_3_, int p_175821_4_, boolean p_175821_5_) {
        if (p_175821_5_) {
            this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 0, 0, p_175821_4_ + 0, p_175821_3_ + 2, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 5, 0, p_175821_4_ + 0, p_175821_3_ + 8 - 1, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 0, p_175821_3_ + 4, 0, p_175821_4_ + 2, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 5, p_175821_3_ + 4, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 2, p_175821_3_ + 4, 0, p_175821_4_ + 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 5, p_175821_3_ + 4, 0, p_175821_4_ + 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 2, 0, p_175821_4_ + 3, p_175821_3_ + 2, 0, p_175821_4_ + 4, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 5, 0, p_175821_4_ + 3, p_175821_3_ + 5, 0, p_175821_4_ + 4, field_175826_b, field_175826_b, false);
        } else {
            this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 0, 0, p_175821_4_ + 0, p_175821_3_ + 8 - 1, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
        }
    }

    protected void func_175819_a(World worldIn, StructureBoundingBox p_175819_2_, int p_175819_3_, int p_175819_4_, int p_175819_5_, int p_175819_6_, int p_175819_7_, int p_175819_8_, IBlockState p_175819_9_) {
        for (int i = p_175819_4_; i <= p_175819_7_; ++i) {
            for (int j = p_175819_3_; j <= p_175819_6_; ++j) {
                for (int k = p_175819_5_; k <= p_175819_8_; ++k) {
                    if (this.getBlockStateFromPos(worldIn, j, i, k, p_175819_2_) != field_175822_f) continue;
                    this.setBlockState(worldIn, p_175819_9_, j, i, k, p_175819_2_);
                }
            }
        }
    }

    protected boolean func_175818_a(StructureBoundingBox p_175818_1_, int p_175818_2_, int p_175818_3_, int p_175818_4_, int p_175818_5_) {
        int i = this.getXWithOffset(p_175818_2_, p_175818_3_);
        int j = this.getZWithOffset(p_175818_2_, p_175818_3_);
        int k = this.getXWithOffset(p_175818_4_, p_175818_5_);
        int l = this.getZWithOffset(p_175818_4_, p_175818_5_);
        return p_175818_1_.intersectsWith(Math.min((int)i, (int)k), Math.min((int)j, (int)l), Math.max((int)i, (int)k), Math.max((int)j, (int)l));
    }

    protected boolean func_175817_a(World worldIn, StructureBoundingBox p_175817_2_, int p_175817_3_, int p_175817_4_, int p_175817_5_) {
        int k;
        int j;
        int i = this.getXWithOffset(p_175817_3_, p_175817_5_);
        if (p_175817_2_.isVecInside((Vec3i)new BlockPos(i, j = this.getYWithOffset(p_175817_4_), k = this.getZWithOffset(p_175817_3_, p_175817_5_)))) {
            EntityGuardian entityguardian = new EntityGuardian(worldIn);
            entityguardian.setElder(true);
            entityguardian.heal(entityguardian.getMaxHealth());
            entityguardian.setLocationAndAngles((double)i + 0.5, (double)j, (double)k + 0.5, 0.0f, 0.0f);
            entityguardian.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos((Entity)entityguardian)), (IEntityLivingData)null);
            worldIn.spawnEntityInWorld((Entity)entityguardian);
            return true;
        }
        return false;
    }
}
