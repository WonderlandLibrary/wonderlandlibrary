// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import net.minecraft.item.ItemDoor;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.block.material.Material;
import net.minecraft.util.Vec3i;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoor;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import java.util.Random;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class StructureComponent
{
    protected StructureBoundingBox boundingBox;
    protected EnumFacing coordBaseMode;
    protected int componentType;
    private static final String __OBFID = "CL_00000511";
    
    public StructureComponent() {
    }
    
    protected StructureComponent(final int p_i2091_1_) {
        this.componentType = p_i2091_1_;
    }
    
    public NBTTagCompound func_143010_b() {
        final NBTTagCompound var1 = new NBTTagCompound();
        var1.setString("id", MapGenStructureIO.func_143036_a(this));
        var1.setTag("BB", this.boundingBox.func_151535_h());
        var1.setInteger("O", (this.coordBaseMode == null) ? -1 : this.coordBaseMode.getHorizontalIndex());
        var1.setInteger("GD", this.componentType);
        this.writeStructureToNBT(var1);
        return var1;
    }
    
    protected abstract void writeStructureToNBT(final NBTTagCompound p0);
    
    public void func_143009_a(final World worldIn, final NBTTagCompound p_143009_2_) {
        if (p_143009_2_.hasKey("BB")) {
            this.boundingBox = new StructureBoundingBox(p_143009_2_.getIntArray("BB"));
        }
        final int var3 = p_143009_2_.getInteger("O");
        this.coordBaseMode = ((var3 == -1) ? null : EnumFacing.getHorizontal(var3));
        this.componentType = p_143009_2_.getInteger("GD");
        this.readStructureFromNBT(p_143009_2_);
    }
    
    protected abstract void readStructureFromNBT(final NBTTagCompound p0);
    
    public void buildComponent(final StructureComponent p_74861_1_, final List p_74861_2_, final Random p_74861_3_) {
    }
    
    public abstract boolean addComponentParts(final World p0, final Random p1, final StructureBoundingBox p2);
    
    public StructureBoundingBox getBoundingBox() {
        return this.boundingBox;
    }
    
    public int getComponentType() {
        return this.componentType;
    }
    
    public static StructureComponent findIntersecting(final List p_74883_0_, final StructureBoundingBox p_74883_1_) {
        for (final StructureComponent var3 : p_74883_0_) {
            if (var3.getBoundingBox() != null && var3.getBoundingBox().intersectsWith(p_74883_1_)) {
                return var3;
            }
        }
        return null;
    }
    
    public BlockPos func_180776_a() {
        return new BlockPos(this.boundingBox.func_180717_f());
    }
    
    protected boolean isLiquidInStructureBoundingBox(final World worldIn, final StructureBoundingBox p_74860_2_) {
        final int var3 = Math.max(this.boundingBox.minX - 1, p_74860_2_.minX);
        final int var4 = Math.max(this.boundingBox.minY - 1, p_74860_2_.minY);
        final int var5 = Math.max(this.boundingBox.minZ - 1, p_74860_2_.minZ);
        final int var6 = Math.min(this.boundingBox.maxX + 1, p_74860_2_.maxX);
        final int var7 = Math.min(this.boundingBox.maxY + 1, p_74860_2_.maxY);
        final int var8 = Math.min(this.boundingBox.maxZ + 1, p_74860_2_.maxZ);
        for (int var9 = var3; var9 <= var6; ++var9) {
            for (int var10 = var5; var10 <= var8; ++var10) {
                if (worldIn.getBlockState(new BlockPos(var9, var4, var10)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (worldIn.getBlockState(new BlockPos(var9, var7, var10)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
            }
        }
        for (int var9 = var3; var9 <= var6; ++var9) {
            for (int var10 = var4; var10 <= var7; ++var10) {
                if (worldIn.getBlockState(new BlockPos(var9, var10, var5)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (worldIn.getBlockState(new BlockPos(var9, var10, var8)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
            }
        }
        for (int var9 = var5; var9 <= var8; ++var9) {
            for (int var10 = var4; var10 <= var7; ++var10) {
                if (worldIn.getBlockState(new BlockPos(var3, var10, var9)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (worldIn.getBlockState(new BlockPos(var6, var10, var9)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    protected int getXWithOffset(final int p_74865_1_, final int p_74865_2_) {
        if (this.coordBaseMode == null) {
            return p_74865_1_;
        }
        switch (SwitchEnumFacing.field_176100_a[this.coordBaseMode.ordinal()]) {
            case 1:
            case 2: {
                return this.boundingBox.minX + p_74865_1_;
            }
            case 3: {
                return this.boundingBox.maxX - p_74865_2_;
            }
            case 4: {
                return this.boundingBox.minX + p_74865_2_;
            }
            default: {
                return p_74865_1_;
            }
        }
    }
    
    protected int getYWithOffset(final int p_74862_1_) {
        return (this.coordBaseMode == null) ? p_74862_1_ : (p_74862_1_ + this.boundingBox.minY);
    }
    
    protected int getZWithOffset(final int p_74873_1_, final int p_74873_2_) {
        if (this.coordBaseMode == null) {
            return p_74873_2_;
        }
        switch (SwitchEnumFacing.field_176100_a[this.coordBaseMode.ordinal()]) {
            case 1: {
                return this.boundingBox.maxZ - p_74873_2_;
            }
            case 2: {
                return this.boundingBox.minZ + p_74873_2_;
            }
            case 3:
            case 4: {
                return this.boundingBox.minZ + p_74873_1_;
            }
            default: {
                return p_74873_2_;
            }
        }
    }
    
    protected int getMetadataWithOffset(final Block p_151555_1_, final int p_151555_2_) {
        if (p_151555_1_ == Blocks.rail) {
            if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.EAST) {
                if (p_151555_2_ == 1) {
                    return 0;
                }
                return 1;
            }
        }
        else if (p_151555_1_ instanceof BlockDoor) {
            if (this.coordBaseMode == EnumFacing.SOUTH) {
                if (p_151555_2_ == 0) {
                    return 2;
                }
                if (p_151555_2_ == 2) {
                    return 0;
                }
            }
            else {
                if (this.coordBaseMode == EnumFacing.WEST) {
                    return p_151555_2_ + 1 & 0x3;
                }
                if (this.coordBaseMode == EnumFacing.EAST) {
                    return p_151555_2_ + 3 & 0x3;
                }
            }
        }
        else if (p_151555_1_ != Blocks.stone_stairs && p_151555_1_ != Blocks.oak_stairs && p_151555_1_ != Blocks.nether_brick_stairs && p_151555_1_ != Blocks.stone_brick_stairs && p_151555_1_ != Blocks.sandstone_stairs) {
            if (p_151555_1_ == Blocks.ladder) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (p_151555_2_ == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (p_151555_2_ == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.EAST.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (p_151555_2_ == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.EAST.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                }
            }
            else if (p_151555_1_ == Blocks.stone_button) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (p_151555_2_ == 3) {
                        return 4;
                    }
                    if (p_151555_2_ == 4) {
                        return 3;
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (p_151555_2_ == 3) {
                        return 1;
                    }
                    if (p_151555_2_ == 4) {
                        return 2;
                    }
                    if (p_151555_2_ == 2) {
                        return 3;
                    }
                    if (p_151555_2_ == 1) {
                        return 4;
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (p_151555_2_ == 3) {
                        return 2;
                    }
                    if (p_151555_2_ == 4) {
                        return 1;
                    }
                    if (p_151555_2_ == 2) {
                        return 3;
                    }
                    if (p_151555_2_ == 1) {
                        return 4;
                    }
                }
            }
            else if (p_151555_1_ != Blocks.tripwire_hook && !(p_151555_1_ instanceof BlockDirectional)) {
                if (p_151555_1_ == Blocks.piston || p_151555_1_ == Blocks.sticky_piston || p_151555_1_ == Blocks.lever || p_151555_1_ == Blocks.dispenser) {
                    if (this.coordBaseMode == EnumFacing.SOUTH) {
                        if (p_151555_2_ == EnumFacing.NORTH.getIndex() || p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.getFront(p_151555_2_).getOpposite().getIndex();
                        }
                    }
                    else if (this.coordBaseMode == EnumFacing.WEST) {
                        if (p_151555_2_ == EnumFacing.NORTH.getIndex()) {
                            return EnumFacing.WEST.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.EAST.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.WEST.getIndex()) {
                            return EnumFacing.NORTH.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.EAST.getIndex()) {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    }
                    else if (this.coordBaseMode == EnumFacing.EAST) {
                        if (p_151555_2_ == EnumFacing.NORTH.getIndex()) {
                            return EnumFacing.EAST.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.WEST.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.WEST.getIndex()) {
                            return EnumFacing.NORTH.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.EAST.getIndex()) {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    }
                }
            }
            else {
                final EnumFacing var3 = EnumFacing.getHorizontal(p_151555_2_);
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (var3 == EnumFacing.SOUTH || var3 == EnumFacing.NORTH) {
                        return var3.getOpposite().getHorizontalIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (var3 == EnumFacing.NORTH) {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.SOUTH) {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.WEST) {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.EAST) {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (var3 == EnumFacing.NORTH) {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.SOUTH) {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.WEST) {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.EAST) {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                }
            }
        }
        else if (this.coordBaseMode == EnumFacing.SOUTH) {
            if (p_151555_2_ == 2) {
                return 3;
            }
            if (p_151555_2_ == 3) {
                return 2;
            }
        }
        else if (this.coordBaseMode == EnumFacing.WEST) {
            if (p_151555_2_ == 0) {
                return 2;
            }
            if (p_151555_2_ == 1) {
                return 3;
            }
            if (p_151555_2_ == 2) {
                return 0;
            }
            if (p_151555_2_ == 3) {
                return 1;
            }
        }
        else if (this.coordBaseMode == EnumFacing.EAST) {
            if (p_151555_2_ == 0) {
                return 2;
            }
            if (p_151555_2_ == 1) {
                return 3;
            }
            if (p_151555_2_ == 2) {
                return 1;
            }
            if (p_151555_2_ == 3) {
                return 0;
            }
        }
        return p_151555_2_;
    }
    
    protected void func_175811_a(final World worldIn, final IBlockState p_175811_2_, final int p_175811_3_, final int p_175811_4_, final int p_175811_5_, final StructureBoundingBox p_175811_6_) {
        final BlockPos var7 = new BlockPos(this.getXWithOffset(p_175811_3_, p_175811_5_), this.getYWithOffset(p_175811_4_), this.getZWithOffset(p_175811_3_, p_175811_5_));
        if (p_175811_6_.func_175898_b(var7)) {
            worldIn.setBlockState(var7, p_175811_2_, 2);
        }
    }
    
    protected IBlockState func_175807_a(final World worldIn, final int p_175807_2_, final int p_175807_3_, final int p_175807_4_, final StructureBoundingBox p_175807_5_) {
        final int var6 = this.getXWithOffset(p_175807_2_, p_175807_4_);
        final int var7 = this.getYWithOffset(p_175807_3_);
        final int var8 = this.getZWithOffset(p_175807_2_, p_175807_4_);
        return p_175807_5_.func_175898_b(new BlockPos(var6, var7, var8)) ? worldIn.getBlockState(new BlockPos(var6, var7, var8)) : Blocks.air.getDefaultState();
    }
    
    protected void fillWithAir(final World worldIn, final StructureBoundingBox p_74878_2_, final int p_74878_3_, final int p_74878_4_, final int p_74878_5_, final int p_74878_6_, final int p_74878_7_, final int p_74878_8_) {
        for (int var9 = p_74878_4_; var9 <= p_74878_7_; ++var9) {
            for (int var10 = p_74878_3_; var10 <= p_74878_6_; ++var10) {
                for (int var11 = p_74878_5_; var11 <= p_74878_8_; ++var11) {
                    this.func_175811_a(worldIn, Blocks.air.getDefaultState(), var10, var9, var11, p_74878_2_);
                }
            }
        }
    }
    
    protected void func_175804_a(final World worldIn, final StructureBoundingBox p_175804_2_, final int p_175804_3_, final int p_175804_4_, final int p_175804_5_, final int p_175804_6_, final int p_175804_7_, final int p_175804_8_, final IBlockState p_175804_9_, final IBlockState p_175804_10_, final boolean p_175804_11_) {
        for (int var12 = p_175804_4_; var12 <= p_175804_7_; ++var12) {
            for (int var13 = p_175804_3_; var13 <= p_175804_6_; ++var13) {
                for (int var14 = p_175804_5_; var14 <= p_175804_8_; ++var14) {
                    if (!p_175804_11_ || this.func_175807_a(worldIn, var13, var12, var14, p_175804_2_).getBlock().getMaterial() != Material.air) {
                        if (var12 != p_175804_4_ && var12 != p_175804_7_ && var13 != p_175804_3_ && var13 != p_175804_6_ && var14 != p_175804_5_ && var14 != p_175804_8_) {
                            this.func_175811_a(worldIn, p_175804_10_, var13, var12, var14, p_175804_2_);
                        }
                        else {
                            this.func_175811_a(worldIn, p_175804_9_, var13, var12, var14, p_175804_2_);
                        }
                    }
                }
            }
        }
    }
    
    protected void fillWithRandomizedBlocks(final World worldIn, final StructureBoundingBox p_74882_2_, final int p_74882_3_, final int p_74882_4_, final int p_74882_5_, final int p_74882_6_, final int p_74882_7_, final int p_74882_8_, final boolean p_74882_9_, final Random p_74882_10_, final BlockSelector p_74882_11_) {
        for (int var12 = p_74882_4_; var12 <= p_74882_7_; ++var12) {
            for (int var13 = p_74882_3_; var13 <= p_74882_6_; ++var13) {
                for (int var14 = p_74882_5_; var14 <= p_74882_8_; ++var14) {
                    if (!p_74882_9_ || this.func_175807_a(worldIn, var13, var12, var14, p_74882_2_).getBlock().getMaterial() != Material.air) {
                        p_74882_11_.selectBlocks(p_74882_10_, var13, var12, var14, var12 == p_74882_4_ || var12 == p_74882_7_ || var13 == p_74882_3_ || var13 == p_74882_6_ || var14 == p_74882_5_ || var14 == p_74882_8_);
                        this.func_175811_a(worldIn, p_74882_11_.func_180780_a(), var13, var12, var14, p_74882_2_);
                    }
                }
            }
        }
    }
    
    protected void func_175805_a(final World worldIn, final StructureBoundingBox p_175805_2_, final Random p_175805_3_, final float p_175805_4_, final int p_175805_5_, final int p_175805_6_, final int p_175805_7_, final int p_175805_8_, final int p_175805_9_, final int p_175805_10_, final IBlockState p_175805_11_, final IBlockState p_175805_12_, final boolean p_175805_13_) {
        for (int var14 = p_175805_6_; var14 <= p_175805_9_; ++var14) {
            for (int var15 = p_175805_5_; var15 <= p_175805_8_; ++var15) {
                for (int var16 = p_175805_7_; var16 <= p_175805_10_; ++var16) {
                    if (p_175805_3_.nextFloat() <= p_175805_4_ && (!p_175805_13_ || this.func_175807_a(worldIn, var15, var14, var16, p_175805_2_).getBlock().getMaterial() != Material.air)) {
                        if (var14 != p_175805_6_ && var14 != p_175805_9_ && var15 != p_175805_5_ && var15 != p_175805_8_ && var16 != p_175805_7_ && var16 != p_175805_10_) {
                            this.func_175811_a(worldIn, p_175805_12_, var15, var14, var16, p_175805_2_);
                        }
                        else {
                            this.func_175811_a(worldIn, p_175805_11_, var15, var14, var16, p_175805_2_);
                        }
                    }
                }
            }
        }
    }
    
    protected void func_175809_a(final World worldIn, final StructureBoundingBox p_175809_2_, final Random p_175809_3_, final float p_175809_4_, final int p_175809_5_, final int p_175809_6_, final int p_175809_7_, final IBlockState p_175809_8_) {
        if (p_175809_3_.nextFloat() < p_175809_4_) {
            this.func_175811_a(worldIn, p_175809_8_, p_175809_5_, p_175809_6_, p_175809_7_, p_175809_2_);
        }
    }
    
    protected void func_180777_a(final World worldIn, final StructureBoundingBox p_180777_2_, final int p_180777_3_, final int p_180777_4_, final int p_180777_5_, final int p_180777_6_, final int p_180777_7_, final int p_180777_8_, final IBlockState p_180777_9_, final boolean p_180777_10_) {
        final float var11 = p_180777_6_ - p_180777_3_ + 1;
        final float var12 = p_180777_7_ - p_180777_4_ + 1;
        final float var13 = p_180777_8_ - p_180777_5_ + 1;
        final float var14 = p_180777_3_ + var11 / 2.0f;
        final float var15 = p_180777_5_ + var13 / 2.0f;
        for (int var16 = p_180777_4_; var16 <= p_180777_7_; ++var16) {
            final float var17 = (var16 - p_180777_4_) / var12;
            for (int var18 = p_180777_3_; var18 <= p_180777_6_; ++var18) {
                final float var19 = (var18 - var14) / (var11 * 0.5f);
                for (int var20 = p_180777_5_; var20 <= p_180777_8_; ++var20) {
                    final float var21 = (var20 - var15) / (var13 * 0.5f);
                    if (!p_180777_10_ || this.func_175807_a(worldIn, var18, var16, var20, p_180777_2_).getBlock().getMaterial() != Material.air) {
                        final float var22 = var19 * var19 + var17 * var17 + var21 * var21;
                        if (var22 <= 1.05f) {
                            this.func_175811_a(worldIn, p_180777_9_, var18, var16, var20, p_180777_2_);
                        }
                    }
                }
            }
        }
    }
    
    protected void clearCurrentPositionBlocksUpwards(final World worldIn, final int p_74871_2_, final int p_74871_3_, final int p_74871_4_, final StructureBoundingBox p_74871_5_) {
        BlockPos var6 = new BlockPos(this.getXWithOffset(p_74871_2_, p_74871_4_), this.getYWithOffset(p_74871_3_), this.getZWithOffset(p_74871_2_, p_74871_4_));
        if (p_74871_5_.func_175898_b(var6)) {
            while (!worldIn.isAirBlock(var6) && var6.getY() < 255) {
                worldIn.setBlockState(var6, Blocks.air.getDefaultState(), 2);
                var6 = var6.offsetUp();
            }
        }
    }
    
    protected void func_175808_b(final World worldIn, final IBlockState p_175808_2_, final int p_175808_3_, final int p_175808_4_, final int p_175808_5_, final StructureBoundingBox p_175808_6_) {
        final int var7 = this.getXWithOffset(p_175808_3_, p_175808_5_);
        int var8 = this.getYWithOffset(p_175808_4_);
        final int var9 = this.getZWithOffset(p_175808_3_, p_175808_5_);
        if (p_175808_6_.func_175898_b(new BlockPos(var7, var8, var9))) {
            while ((worldIn.isAirBlock(new BlockPos(var7, var8, var9)) || worldIn.getBlockState(new BlockPos(var7, var8, var9)).getBlock().getMaterial().isLiquid()) && var8 > 1) {
                worldIn.setBlockState(new BlockPos(var7, var8, var9), p_175808_2_, 2);
                --var8;
            }
        }
    }
    
    protected boolean func_180778_a(final World worldIn, final StructureBoundingBox p_180778_2_, final Random p_180778_3_, final int p_180778_4_, final int p_180778_5_, final int p_180778_6_, final List p_180778_7_, final int p_180778_8_) {
        final BlockPos var9 = new BlockPos(this.getXWithOffset(p_180778_4_, p_180778_6_), this.getYWithOffset(p_180778_5_), this.getZWithOffset(p_180778_4_, p_180778_6_));
        if (p_180778_2_.func_175898_b(var9) && worldIn.getBlockState(var9).getBlock() != Blocks.chest) {
            final IBlockState var10 = Blocks.chest.getDefaultState();
            worldIn.setBlockState(var9, Blocks.chest.func_176458_f(worldIn, var9, var10), 2);
            final TileEntity var11 = worldIn.getTileEntity(var9);
            if (var11 instanceof TileEntityChest) {
                WeightedRandomChestContent.generateChestContents(p_180778_3_, p_180778_7_, (IInventory)var11, p_180778_8_);
            }
            return true;
        }
        return false;
    }
    
    protected boolean func_175806_a(final World worldIn, final StructureBoundingBox p_175806_2_, final Random p_175806_3_, final int p_175806_4_, final int p_175806_5_, final int p_175806_6_, final int p_175806_7_, final List p_175806_8_, final int p_175806_9_) {
        final BlockPos var10 = new BlockPos(this.getXWithOffset(p_175806_4_, p_175806_6_), this.getYWithOffset(p_175806_5_), this.getZWithOffset(p_175806_4_, p_175806_6_));
        if (p_175806_2_.func_175898_b(var10) && worldIn.getBlockState(var10).getBlock() != Blocks.dispenser) {
            worldIn.setBlockState(var10, Blocks.dispenser.getStateFromMeta(this.getMetadataWithOffset(Blocks.dispenser, p_175806_7_)), 2);
            final TileEntity var11 = worldIn.getTileEntity(var10);
            if (var11 instanceof TileEntityDispenser) {
                WeightedRandomChestContent.func_177631_a(p_175806_3_, p_175806_8_, (TileEntityDispenser)var11, p_175806_9_);
            }
            return true;
        }
        return false;
    }
    
    protected void func_175810_a(final World worldIn, final StructureBoundingBox p_175810_2_, final Random p_175810_3_, final int p_175810_4_, final int p_175810_5_, final int p_175810_6_, final EnumFacing p_175810_7_) {
        final BlockPos var8 = new BlockPos(this.getXWithOffset(p_175810_4_, p_175810_6_), this.getYWithOffset(p_175810_5_), this.getZWithOffset(p_175810_4_, p_175810_6_));
        if (p_175810_2_.func_175898_b(var8)) {
            ItemDoor.func_179235_a(worldIn, var8, p_175810_7_.rotateYCCW(), Blocks.oak_door);
        }
    }
    
    public abstract static class BlockSelector
    {
        protected IBlockState field_151562_a;
        private static final String __OBFID = "CL_00000512";
        
        protected BlockSelector() {
            this.field_151562_a = Blocks.air.getDefaultState();
        }
        
        public abstract void selectBlocks(final Random p0, final int p1, final int p2, final int p3, final boolean p4);
        
        public IBlockState func_180780_a() {
            return this.field_151562_a;
        }
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_176100_a;
        private static final String __OBFID = "CL_00001969";
        
        static {
            field_176100_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_176100_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_176100_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_176100_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_176100_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
