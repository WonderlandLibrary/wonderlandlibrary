/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public abstract class StructureComponent {
    protected StructureBoundingBox boundingBox;
    protected EnumFacing coordBaseMode;
    protected int componentType;

    public StructureComponent() {
    }

    protected StructureComponent(int type) {
        this.componentType = type;
    }

    public NBTTagCompound createStructureBaseNBT() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("id", MapGenStructureIO.getStructureComponentName(this));
        nbttagcompound.setTag("BB", this.boundingBox.toNBTTagIntArray());
        nbttagcompound.setInteger("O", this.coordBaseMode == null ? -1 : this.coordBaseMode.getHorizontalIndex());
        nbttagcompound.setInteger("GD", this.componentType);
        this.writeStructureToNBT(nbttagcompound);
        return nbttagcompound;
    }

    protected abstract void writeStructureToNBT(NBTTagCompound var1);

    public void readStructureBaseNBT(World worldIn, NBTTagCompound tagCompound) {
        int i2;
        if (tagCompound.hasKey("BB")) {
            this.boundingBox = new StructureBoundingBox(tagCompound.getIntArray("BB"));
        }
        this.coordBaseMode = (i2 = tagCompound.getInteger("O")) == -1 ? null : EnumFacing.getHorizontal(i2);
        this.componentType = tagCompound.getInteger("GD");
        this.readStructureFromNBT(tagCompound);
    }

    protected abstract void readStructureFromNBT(NBTTagCompound var1);

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
    }

    public abstract boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3);

    public StructureBoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public int getComponentType() {
        return this.componentType;
    }

    public static StructureComponent findIntersecting(List<StructureComponent> listIn, StructureBoundingBox boundingboxIn) {
        for (StructureComponent structurecomponent : listIn) {
            if (structurecomponent.getBoundingBox() == null || !structurecomponent.getBoundingBox().intersectsWith(boundingboxIn)) continue;
            return structurecomponent;
        }
        return null;
    }

    public BlockPos getBoundingBoxCenter() {
        return new BlockPos(this.boundingBox.getCenter());
    }

    protected boolean isLiquidInStructureBoundingBox(World worldIn, StructureBoundingBox boundingboxIn) {
        int i2 = Math.max(this.boundingBox.minX - 1, boundingboxIn.minX);
        int j2 = Math.max(this.boundingBox.minY - 1, boundingboxIn.minY);
        int k2 = Math.max(this.boundingBox.minZ - 1, boundingboxIn.minZ);
        int l2 = Math.min(this.boundingBox.maxX + 1, boundingboxIn.maxX);
        int i1 = Math.min(this.boundingBox.maxY + 1, boundingboxIn.maxY);
        int j1 = Math.min(this.boundingBox.maxZ + 1, boundingboxIn.maxZ);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k1 = i2; k1 <= l2; ++k1) {
            for (int l1 = k2; l1 <= j1; ++l1) {
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, j2, l1)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (!worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, i1, l1)).getBlock().getMaterial().isLiquid()) continue;
                return true;
            }
        }
        for (int i22 = i2; i22 <= l2; ++i22) {
            for (int k22 = j2; k22 <= i1; ++k22) {
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i22, k22, k2)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (!worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i22, k22, j1)).getBlock().getMaterial().isLiquid()) continue;
                return true;
            }
        }
        for (int j22 = k2; j22 <= j1; ++j22) {
            for (int l22 = j2; l22 <= i1; ++l22) {
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i2, l22, j22)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (!worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l2, l22, j22)).getBlock().getMaterial().isLiquid()) continue;
                return true;
            }
        }
        return false;
    }

    protected int getXWithOffset(int x2, int z2) {
        if (this.coordBaseMode == null) {
            return x2;
        }
        switch (this.coordBaseMode) {
            case NORTH: 
            case SOUTH: {
                return this.boundingBox.minX + x2;
            }
            case WEST: {
                return this.boundingBox.maxX - z2;
            }
            case EAST: {
                return this.boundingBox.minX + z2;
            }
        }
        return x2;
    }

    protected int getYWithOffset(int y2) {
        return this.coordBaseMode == null ? y2 : y2 + this.boundingBox.minY;
    }

    protected int getZWithOffset(int x2, int z2) {
        if (this.coordBaseMode == null) {
            return z2;
        }
        switch (this.coordBaseMode) {
            case NORTH: {
                return this.boundingBox.maxZ - z2;
            }
            case SOUTH: {
                return this.boundingBox.minZ + z2;
            }
            case WEST: 
            case EAST: {
                return this.boundingBox.minZ + x2;
            }
        }
        return z2;
    }

    protected int getMetadataWithOffset(Block blockIn, int meta) {
        if (blockIn == Blocks.rail) {
            if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.EAST) {
                if (meta == 1) {
                    return 0;
                }
                return 1;
            }
        } else if (blockIn instanceof BlockDoor) {
            if (this.coordBaseMode == EnumFacing.SOUTH) {
                if (meta == 0) {
                    return 2;
                }
                if (meta == 2) {
                    return 0;
                }
            } else {
                if (this.coordBaseMode == EnumFacing.WEST) {
                    return meta + 1 & 3;
                }
                if (this.coordBaseMode == EnumFacing.EAST) {
                    return meta + 3 & 3;
                }
            }
        } else if (blockIn != Blocks.stone_stairs && blockIn != Blocks.oak_stairs && blockIn != Blocks.nether_brick_stairs && blockIn != Blocks.stone_brick_stairs && blockIn != Blocks.sandstone_stairs) {
            if (blockIn == Blocks.ladder) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (meta == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                    if (meta == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (meta == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (meta == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (meta == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (meta == EnumFacing.EAST.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (meta == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (meta == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (meta == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (meta == EnumFacing.EAST.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                }
            } else if (blockIn == Blocks.stone_button) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (meta == 3) {
                        return 4;
                    }
                    if (meta == 4) {
                        return 3;
                    }
                } else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (meta == 3) {
                        return 1;
                    }
                    if (meta == 4) {
                        return 2;
                    }
                    if (meta == 2) {
                        return 3;
                    }
                    if (meta == 1) {
                        return 4;
                    }
                } else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (meta == 3) {
                        return 2;
                    }
                    if (meta == 4) {
                        return 1;
                    }
                    if (meta == 2) {
                        return 3;
                    }
                    if (meta == 1) {
                        return 4;
                    }
                }
            } else if (blockIn != Blocks.tripwire_hook && !(blockIn instanceof BlockDirectional)) {
                if (blockIn == Blocks.piston || blockIn == Blocks.sticky_piston || blockIn == Blocks.lever || blockIn == Blocks.dispenser) {
                    if (this.coordBaseMode == EnumFacing.SOUTH) {
                        if (meta == EnumFacing.NORTH.getIndex() || meta == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.getFront(meta).getOpposite().getIndex();
                        }
                    } else if (this.coordBaseMode == EnumFacing.WEST) {
                        if (meta == EnumFacing.NORTH.getIndex()) {
                            return EnumFacing.WEST.getIndex();
                        }
                        if (meta == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.EAST.getIndex();
                        }
                        if (meta == EnumFacing.WEST.getIndex()) {
                            return EnumFacing.NORTH.getIndex();
                        }
                        if (meta == EnumFacing.EAST.getIndex()) {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    } else if (this.coordBaseMode == EnumFacing.EAST) {
                        if (meta == EnumFacing.NORTH.getIndex()) {
                            return EnumFacing.EAST.getIndex();
                        }
                        if (meta == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.WEST.getIndex();
                        }
                        if (meta == EnumFacing.WEST.getIndex()) {
                            return EnumFacing.NORTH.getIndex();
                        }
                        if (meta == EnumFacing.EAST.getIndex()) {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    }
                }
            } else {
                EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (enumfacing == EnumFacing.SOUTH || enumfacing == EnumFacing.NORTH) {
                        return enumfacing.getOpposite().getHorizontalIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (enumfacing == EnumFacing.NORTH) {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.SOUTH) {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.WEST) {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.EAST) {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (enumfacing == EnumFacing.NORTH) {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.SOUTH) {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.WEST) {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.EAST) {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                }
            }
        } else if (this.coordBaseMode == EnumFacing.SOUTH) {
            if (meta == 2) {
                return 3;
            }
            if (meta == 3) {
                return 2;
            }
        } else if (this.coordBaseMode == EnumFacing.WEST) {
            if (meta == 0) {
                return 2;
            }
            if (meta == 1) {
                return 3;
            }
            if (meta == 2) {
                return 0;
            }
            if (meta == 3) {
                return 1;
            }
        } else if (this.coordBaseMode == EnumFacing.EAST) {
            if (meta == 0) {
                return 2;
            }
            if (meta == 1) {
                return 3;
            }
            if (meta == 2) {
                return 1;
            }
            if (meta == 3) {
                return 0;
            }
        }
        return meta;
    }

    protected void setBlockState(World worldIn, IBlockState blockstateIn, int x2, int y2, int z2, StructureBoundingBox boundingboxIn) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x2, z2), this.getYWithOffset(y2), this.getZWithOffset(x2, z2));
        if (boundingboxIn.isVecInside(blockpos)) {
            worldIn.setBlockState(blockpos, blockstateIn, 2);
        }
    }

    protected IBlockState getBlockStateFromPos(World worldIn, int x2, int y2, int z2, StructureBoundingBox boundingboxIn) {
        int k2;
        int j2;
        int i2 = this.getXWithOffset(x2, z2);
        BlockPos blockpos = new BlockPos(i2, j2 = this.getYWithOffset(y2), k2 = this.getZWithOffset(x2, z2));
        return !boundingboxIn.isVecInside(blockpos) ? Blocks.air.getDefaultState() : worldIn.getBlockState(blockpos);
    }

    protected void fillWithAir(World worldIn, StructureBoundingBox structurebb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int i2 = minY; i2 <= maxY; ++i2) {
            for (int j2 = minX; j2 <= maxX; ++j2) {
                for (int k2 = minZ; k2 <= maxZ; ++k2) {
                    this.setBlockState(worldIn, Blocks.air.getDefaultState(), j2, i2, k2, structurebb);
                }
            }
        }
    }

    protected void fillWithBlocks(World worldIn, StructureBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, IBlockState boundaryBlockState, IBlockState insideBlockState, boolean existingOnly) {
        for (int i2 = yMin; i2 <= yMax; ++i2) {
            for (int j2 = xMin; j2 <= xMax; ++j2) {
                for (int k2 = zMin; k2 <= zMax; ++k2) {
                    if (existingOnly && this.getBlockStateFromPos(worldIn, j2, i2, k2, boundingboxIn).getBlock().getMaterial() == Material.air) continue;
                    if (i2 != yMin && i2 != yMax && j2 != xMin && j2 != xMax && k2 != zMin && k2 != zMax) {
                        this.setBlockState(worldIn, insideBlockState, j2, i2, k2, boundingboxIn);
                        continue;
                    }
                    this.setBlockState(worldIn, boundaryBlockState, j2, i2, k2, boundingboxIn);
                }
            }
        }
    }

    protected void fillWithRandomizedBlocks(World worldIn, StructureBoundingBox boundingboxIn, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean alwaysReplace, Random rand, BlockSelector blockselector) {
        for (int i2 = minY; i2 <= maxY; ++i2) {
            for (int j2 = minX; j2 <= maxX; ++j2) {
                for (int k2 = minZ; k2 <= maxZ; ++k2) {
                    if (alwaysReplace && this.getBlockStateFromPos(worldIn, j2, i2, k2, boundingboxIn).getBlock().getMaterial() == Material.air) continue;
                    blockselector.selectBlocks(rand, j2, i2, k2, i2 == minY || i2 == maxY || j2 == minX || j2 == maxX || k2 == minZ || k2 == maxZ);
                    this.setBlockState(worldIn, blockselector.getBlockState(), j2, i2, k2, boundingboxIn);
                }
            }
        }
    }

    protected void func_175805_a(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockstate1, IBlockState blockstate2, boolean p_175805_13_) {
        for (int i2 = minY; i2 <= maxY; ++i2) {
            for (int j2 = minX; j2 <= maxX; ++j2) {
                for (int k2 = minZ; k2 <= maxZ; ++k2) {
                    if (!(rand.nextFloat() <= chance) || p_175805_13_ && this.getBlockStateFromPos(worldIn, j2, i2, k2, boundingboxIn).getBlock().getMaterial() == Material.air) continue;
                    if (i2 != minY && i2 != maxY && j2 != minX && j2 != maxX && k2 != minZ && k2 != maxZ) {
                        this.setBlockState(worldIn, blockstate2, j2, i2, k2, boundingboxIn);
                        continue;
                    }
                    this.setBlockState(worldIn, blockstate1, j2, i2, k2, boundingboxIn);
                }
            }
        }
    }

    protected void randomlyPlaceBlock(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int x2, int y2, int z2, IBlockState blockstateIn) {
        if (rand.nextFloat() < chance) {
            this.setBlockState(worldIn, blockstateIn, x2, y2, z2, boundingboxIn);
        }
    }

    protected void randomlyRareFillWithBlocks(World worldIn, StructureBoundingBox boundingboxIn, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockstateIn, boolean p_180777_10_) {
        float f2 = maxX - minX + 1;
        float f1 = maxY - minY + 1;
        float f22 = maxZ - minZ + 1;
        float f3 = (float)minX + f2 / 2.0f;
        float f4 = (float)minZ + f22 / 2.0f;
        for (int i2 = minY; i2 <= maxY; ++i2) {
            float f5 = (float)(i2 - minY) / f1;
            for (int j2 = minX; j2 <= maxX; ++j2) {
                float f6 = ((float)j2 - f3) / (f2 * 0.5f);
                for (int k2 = minZ; k2 <= maxZ; ++k2) {
                    float f8;
                    float f7 = ((float)k2 - f4) / (f22 * 0.5f);
                    if (p_180777_10_ && this.getBlockStateFromPos(worldIn, j2, i2, k2, boundingboxIn).getBlock().getMaterial() == Material.air || !((f8 = f6 * f6 + f5 * f5 + f7 * f7) <= 1.05f)) continue;
                    this.setBlockState(worldIn, blockstateIn, j2, i2, k2, boundingboxIn);
                }
            }
        }
    }

    protected void clearCurrentPositionBlocksUpwards(World worldIn, int x2, int y2, int z2, StructureBoundingBox structurebb) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x2, z2), this.getYWithOffset(y2), this.getZWithOffset(x2, z2));
        if (structurebb.isVecInside(blockpos)) {
            while (!worldIn.isAirBlock(blockpos) && blockpos.getY() < 255) {
                worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
                blockpos = blockpos.up();
            }
        }
    }

    protected void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x2, int y2, int z2, StructureBoundingBox boundingboxIn) {
        int k2;
        int j2;
        int i2 = this.getXWithOffset(x2, z2);
        if (boundingboxIn.isVecInside(new BlockPos(i2, j2 = this.getYWithOffset(y2), k2 = this.getZWithOffset(x2, z2)))) {
            while ((worldIn.isAirBlock(new BlockPos(i2, j2, k2)) || worldIn.getBlockState(new BlockPos(i2, j2, k2)).getBlock().getMaterial().isLiquid()) && j2 > 1) {
                worldIn.setBlockState(new BlockPos(i2, j2, k2), blockstateIn, 2);
                --j2;
            }
        }
    }

    protected boolean generateChestContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x2, int y2, int z2, List<WeightedRandomChestContent> listIn, int max) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x2, z2), this.getYWithOffset(y2), this.getZWithOffset(x2, z2));
        if (boundingBoxIn.isVecInside(blockpos) && worldIn.getBlockState(blockpos).getBlock() != Blocks.chest) {
            IBlockState iblockstate = Blocks.chest.getDefaultState();
            worldIn.setBlockState(blockpos, Blocks.chest.correctFacing(worldIn, blockpos, iblockstate), 2);
            TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof TileEntityChest) {
                WeightedRandomChestContent.generateChestContents(rand, listIn, (TileEntityChest)tileentity, max);
            }
            return true;
        }
        return false;
    }

    protected boolean generateDispenserContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x2, int y2, int z2, int meta, List<WeightedRandomChestContent> listIn, int max) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x2, z2), this.getYWithOffset(y2), this.getZWithOffset(x2, z2));
        if (boundingBoxIn.isVecInside(blockpos) && worldIn.getBlockState(blockpos).getBlock() != Blocks.dispenser) {
            worldIn.setBlockState(blockpos, Blocks.dispenser.getStateFromMeta(this.getMetadataWithOffset(Blocks.dispenser, meta)), 2);
            TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof TileEntityDispenser) {
                WeightedRandomChestContent.generateDispenserContents(rand, listIn, (TileEntityDispenser)tileentity, max);
            }
            return true;
        }
        return false;
    }

    protected void placeDoorCurrentPosition(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x2, int y2, int z2, EnumFacing facing) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x2, z2), this.getYWithOffset(y2), this.getZWithOffset(x2, z2));
        if (boundingBoxIn.isVecInside(blockpos)) {
            ItemDoor.placeDoor(worldIn, blockpos, facing.rotateYCCW(), Blocks.oak_door);
        }
    }

    public void func_181138_a(int p_181138_1_, int p_181138_2_, int p_181138_3_) {
        this.boundingBox.offset(p_181138_1_, p_181138_2_, p_181138_3_);
    }

    public static abstract class BlockSelector {
        protected IBlockState blockstate = Blocks.air.getDefaultState();

        public abstract void selectBlocks(Random var1, int var2, int var3, int var4, boolean var5);

        public IBlockState getBlockState() {
            return this.blockstate;
        }
    }
}

