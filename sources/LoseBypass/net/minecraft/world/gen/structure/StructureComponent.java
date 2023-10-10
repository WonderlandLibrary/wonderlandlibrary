/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.structure;

import java.util.Iterator;
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
        int i;
        if (tagCompound.hasKey("BB")) {
            this.boundingBox = new StructureBoundingBox(tagCompound.getIntArray("BB"));
        }
        this.coordBaseMode = (i = tagCompound.getInteger("O")) == -1 ? null : EnumFacing.getHorizontal(i);
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
        StructureComponent structurecomponent;
        Iterator<StructureComponent> iterator = listIn.iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while ((structurecomponent = iterator.next()).getBoundingBox() == null || !structurecomponent.getBoundingBox().intersectsWith(boundingboxIn));
        return structurecomponent;
    }

    public BlockPos getBoundingBoxCenter() {
        return new BlockPos(this.boundingBox.getCenter());
    }

    protected boolean isLiquidInStructureBoundingBox(World worldIn, StructureBoundingBox boundingboxIn) {
        int i = Math.max(this.boundingBox.minX - 1, boundingboxIn.minX);
        int j = Math.max(this.boundingBox.minY - 1, boundingboxIn.minY);
        int k = Math.max(this.boundingBox.minZ - 1, boundingboxIn.minZ);
        int l = Math.min(this.boundingBox.maxX + 1, boundingboxIn.maxX);
        int i1 = Math.min(this.boundingBox.maxY + 1, boundingboxIn.maxY);
        int j1 = Math.min(this.boundingBox.maxZ + 1, boundingboxIn.maxZ);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int k1 = i;
        while (true) {
            if (k1 > l) break;
            for (int l1 = k; l1 <= j1; ++l1) {
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, j, l1)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (!worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, i1, l1)).getBlock().getMaterial().isLiquid()) continue;
                return true;
            }
            ++k1;
        }
        int i2 = i;
        while (true) {
            if (i2 > l) break;
            for (int k2 = j; k2 <= i1; ++k2) {
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i2, k2, k)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (!worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i2, k2, j1)).getBlock().getMaterial().isLiquid()) continue;
                return true;
            }
            ++i2;
        }
        int j2 = k;
        while (j2 <= j1) {
            for (int l2 = j; l2 <= i1; ++l2) {
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i, l2, j2)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (!worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l, l2, j2)).getBlock().getMaterial().isLiquid()) continue;
                return true;
            }
            ++j2;
        }
        return false;
    }

    protected int getXWithOffset(int x, int z) {
        if (this.coordBaseMode == null) {
            return x;
        }
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
            case 1: 
            case 2: {
                return this.boundingBox.minX + x;
            }
            case 3: {
                return this.boundingBox.maxX - z;
            }
            case 4: {
                return this.boundingBox.minX + z;
            }
        }
        return x;
    }

    protected int getYWithOffset(int y) {
        int n;
        if (this.coordBaseMode == null) {
            n = y;
            return n;
        }
        n = y + this.boundingBox.minY;
        return n;
    }

    protected int getZWithOffset(int x, int z) {
        if (this.coordBaseMode == null) {
            return z;
        }
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
            case 1: {
                return this.boundingBox.maxZ - z;
            }
            case 2: {
                return this.boundingBox.minZ + z;
            }
            case 3: 
            case 4: {
                return this.boundingBox.minZ + x;
            }
        }
        return z;
    }

    protected int getMetadataWithOffset(Block blockIn, int meta) {
        if (blockIn == Blocks.rail) {
            if (this.coordBaseMode != EnumFacing.WEST) {
                if (this.coordBaseMode != EnumFacing.EAST) return meta;
            }
            if (meta != 1) return 1;
            return 0;
        }
        if (blockIn instanceof BlockDoor) {
            if (this.coordBaseMode == EnumFacing.SOUTH) {
                if (meta == 0) {
                    return 2;
                }
                if (meta != 2) return meta;
                return 0;
            }
            if (this.coordBaseMode == EnumFacing.WEST) {
                return meta + 1 & 3;
            }
            if (this.coordBaseMode != EnumFacing.EAST) return meta;
            return meta + 3 & 3;
        }
        if (blockIn != Blocks.stone_stairs && blockIn != Blocks.oak_stairs && blockIn != Blocks.nether_brick_stairs && blockIn != Blocks.stone_brick_stairs && blockIn != Blocks.sandstone_stairs) {
            if (blockIn == Blocks.ladder) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (meta == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                    if (meta != EnumFacing.SOUTH.getIndex()) return meta;
                    return EnumFacing.NORTH.getIndex();
                }
                if (this.coordBaseMode == EnumFacing.WEST) {
                    if (meta == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (meta == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (meta == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (meta != EnumFacing.EAST.getIndex()) return meta;
                    return EnumFacing.SOUTH.getIndex();
                }
                if (this.coordBaseMode != EnumFacing.EAST) return meta;
                if (meta == EnumFacing.NORTH.getIndex()) {
                    return EnumFacing.EAST.getIndex();
                }
                if (meta == EnumFacing.SOUTH.getIndex()) {
                    return EnumFacing.WEST.getIndex();
                }
                if (meta == EnumFacing.WEST.getIndex()) {
                    return EnumFacing.NORTH.getIndex();
                }
                if (meta != EnumFacing.EAST.getIndex()) return meta;
                return EnumFacing.SOUTH.getIndex();
            }
            if (blockIn == Blocks.stone_button) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (meta == 3) {
                        return 4;
                    }
                    if (meta != 4) return meta;
                    return 3;
                }
                if (this.coordBaseMode == EnumFacing.WEST) {
                    if (meta == 3) {
                        return 1;
                    }
                    if (meta == 4) {
                        return 2;
                    }
                    if (meta == 2) {
                        return 3;
                    }
                    if (meta != 1) return meta;
                    return 4;
                }
                if (this.coordBaseMode != EnumFacing.EAST) return meta;
                if (meta == 3) {
                    return 2;
                }
                if (meta == 4) {
                    return 1;
                }
                if (meta == 2) {
                    return 3;
                }
                if (meta != 1) return meta;
                return 4;
            }
            if (blockIn != Blocks.tripwire_hook && !(blockIn instanceof BlockDirectional)) {
                if (blockIn != Blocks.piston && blockIn != Blocks.sticky_piston && blockIn != Blocks.lever) {
                    if (blockIn != Blocks.dispenser) return meta;
                }
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (meta == EnumFacing.NORTH.getIndex()) return EnumFacing.getFront(meta).getOpposite().getIndex();
                    if (meta != EnumFacing.SOUTH.getIndex()) return meta;
                    return EnumFacing.getFront(meta).getOpposite().getIndex();
                }
                if (this.coordBaseMode == EnumFacing.WEST) {
                    if (meta == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (meta == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (meta == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (meta != EnumFacing.EAST.getIndex()) return meta;
                    return EnumFacing.SOUTH.getIndex();
                }
                if (this.coordBaseMode != EnumFacing.EAST) return meta;
                if (meta == EnumFacing.NORTH.getIndex()) {
                    return EnumFacing.EAST.getIndex();
                }
                if (meta == EnumFacing.SOUTH.getIndex()) {
                    return EnumFacing.WEST.getIndex();
                }
                if (meta == EnumFacing.WEST.getIndex()) {
                    return EnumFacing.NORTH.getIndex();
                }
                if (meta != EnumFacing.EAST.getIndex()) return meta;
                return EnumFacing.SOUTH.getIndex();
            }
            EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
            if (this.coordBaseMode == EnumFacing.SOUTH) {
                if (enumfacing == EnumFacing.SOUTH) return enumfacing.getOpposite().getHorizontalIndex();
                if (enumfacing != EnumFacing.NORTH) return meta;
                return enumfacing.getOpposite().getHorizontalIndex();
            }
            if (this.coordBaseMode == EnumFacing.WEST) {
                if (enumfacing == EnumFacing.NORTH) {
                    return EnumFacing.WEST.getHorizontalIndex();
                }
                if (enumfacing == EnumFacing.SOUTH) {
                    return EnumFacing.EAST.getHorizontalIndex();
                }
                if (enumfacing == EnumFacing.WEST) {
                    return EnumFacing.NORTH.getHorizontalIndex();
                }
                if (enumfacing != EnumFacing.EAST) return meta;
                return EnumFacing.SOUTH.getHorizontalIndex();
            }
            if (this.coordBaseMode != EnumFacing.EAST) return meta;
            if (enumfacing == EnumFacing.NORTH) {
                return EnumFacing.EAST.getHorizontalIndex();
            }
            if (enumfacing == EnumFacing.SOUTH) {
                return EnumFacing.WEST.getHorizontalIndex();
            }
            if (enumfacing == EnumFacing.WEST) {
                return EnumFacing.NORTH.getHorizontalIndex();
            }
            if (enumfacing != EnumFacing.EAST) return meta;
            return EnumFacing.SOUTH.getHorizontalIndex();
        }
        if (this.coordBaseMode == EnumFacing.SOUTH) {
            if (meta == 2) {
                return 3;
            }
            if (meta != 3) return meta;
            return 2;
        }
        if (this.coordBaseMode == EnumFacing.WEST) {
            if (meta == 0) {
                return 2;
            }
            if (meta == 1) {
                return 3;
            }
            if (meta == 2) {
                return 0;
            }
            if (meta != 3) return meta;
            return 1;
        }
        if (this.coordBaseMode != EnumFacing.EAST) return meta;
        if (meta == 0) {
            return 2;
        }
        if (meta == 1) {
            return 3;
        }
        if (meta == 2) {
            return 1;
        }
        if (meta != 3) return meta;
        return 0;
    }

    protected void setBlockState(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (!boundingboxIn.isVecInside(blockpos)) return;
        worldIn.setBlockState(blockpos, blockstateIn, 2);
    }

    protected IBlockState getBlockStateFromPos(World worldIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
        IBlockState iBlockState;
        int k;
        int j;
        int i = this.getXWithOffset(x, z);
        BlockPos blockpos = new BlockPos(i, j = this.getYWithOffset(y), k = this.getZWithOffset(x, z));
        if (!boundingboxIn.isVecInside(blockpos)) {
            iBlockState = Blocks.air.getDefaultState();
            return iBlockState;
        }
        iBlockState = worldIn.getBlockState(blockpos);
        return iBlockState;
    }

    protected void fillWithAir(World worldIn, StructureBoundingBox structurebb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        int i = minY;
        while (i <= maxY) {
            for (int j = minX; j <= maxX; ++j) {
                for (int k = minZ; k <= maxZ; ++k) {
                    this.setBlockState(worldIn, Blocks.air.getDefaultState(), j, i, k, structurebb);
                }
            }
            ++i;
        }
    }

    protected void fillWithBlocks(World worldIn, StructureBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, IBlockState boundaryBlockState, IBlockState insideBlockState, boolean existingOnly) {
        int i = yMin;
        block0: while (i <= yMax) {
            int j = xMin;
            while (true) {
                if (j <= xMax) {
                } else {
                    ++i;
                    continue block0;
                }
                for (int k = zMin; k <= zMax; ++k) {
                    if (existingOnly && this.getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() == Material.air) continue;
                    if (i != yMin && i != yMax && j != xMin && j != xMax && k != zMin && k != zMax) {
                        this.setBlockState(worldIn, insideBlockState, j, i, k, boundingboxIn);
                        continue;
                    }
                    this.setBlockState(worldIn, boundaryBlockState, j, i, k, boundingboxIn);
                }
                ++j;
            }
            break;
        }
        return;
    }

    protected void fillWithRandomizedBlocks(World worldIn, StructureBoundingBox boundingboxIn, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean alwaysReplace, Random rand, BlockSelector blockselector) {
        int i = minY;
        block0: while (i <= maxY) {
            int j = minX;
            while (true) {
                if (j <= maxX) {
                } else {
                    ++i;
                    continue block0;
                }
                for (int k = minZ; k <= maxZ; ++k) {
                    if (alwaysReplace && this.getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() == Material.air) continue;
                    blockselector.selectBlocks(rand, j, i, k, i == minY || i == maxY || j == minX || j == maxX || k == minZ || k == maxZ);
                    this.setBlockState(worldIn, blockselector.getBlockState(), j, i, k, boundingboxIn);
                }
                ++j;
            }
            break;
        }
        return;
    }

    protected void func_175805_a(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockstate1, IBlockState blockstate2, boolean p_175805_13_) {
        int i = minY;
        block0: while (i <= maxY) {
            int j = minX;
            while (true) {
                if (j <= maxX) {
                } else {
                    ++i;
                    continue block0;
                }
                for (int k = minZ; k <= maxZ; ++k) {
                    if (!(rand.nextFloat() <= chance) || p_175805_13_ && this.getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() == Material.air) continue;
                    if (i != minY && i != maxY && j != minX && j != maxX && k != minZ && k != maxZ) {
                        this.setBlockState(worldIn, blockstate2, j, i, k, boundingboxIn);
                        continue;
                    }
                    this.setBlockState(worldIn, blockstate1, j, i, k, boundingboxIn);
                }
                ++j;
            }
            break;
        }
        return;
    }

    protected void randomlyPlaceBlock(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int x, int y, int z, IBlockState blockstateIn) {
        if (!(rand.nextFloat() < chance)) return;
        this.setBlockState(worldIn, blockstateIn, x, y, z, boundingboxIn);
    }

    protected void randomlyRareFillWithBlocks(World worldIn, StructureBoundingBox boundingboxIn, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockstateIn, boolean p_180777_10_) {
        float f = maxX - minX + 1;
        float f1 = maxY - minY + 1;
        float f2 = maxZ - minZ + 1;
        float f3 = (float)minX + f / 2.0f;
        float f4 = (float)minZ + f2 / 2.0f;
        int i = minY;
        while (i <= maxY) {
            float f5 = (float)(i - minY) / f1;
            for (int j = minX; j <= maxX; ++j) {
                float f6 = ((float)j - f3) / (f * 0.5f);
                for (int k = minZ; k <= maxZ; ++k) {
                    float f8;
                    float f7 = ((float)k - f4) / (f2 * 0.5f);
                    if (p_180777_10_ && this.getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() == Material.air || !((f8 = f6 * f6 + f5 * f5 + f7 * f7) <= 1.05f)) continue;
                    this.setBlockState(worldIn, blockstateIn, j, i, k, boundingboxIn);
                }
            }
            ++i;
        }
    }

    protected void clearCurrentPositionBlocksUpwards(World worldIn, int x, int y, int z, StructureBoundingBox structurebb) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (!structurebb.isVecInside(blockpos)) return;
        while (!worldIn.isAirBlock(blockpos)) {
            if (blockpos.getY() >= 255) return;
            worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
            blockpos = blockpos.up();
        }
    }

    protected void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
        int k;
        int j;
        int i = this.getXWithOffset(x, z);
        if (!boundingboxIn.isVecInside(new BlockPos(i, j = this.getYWithOffset(y), k = this.getZWithOffset(x, z)))) return;
        while (true) {
            if (!worldIn.isAirBlock(new BlockPos(i, j, k))) {
                if (!worldIn.getBlockState(new BlockPos(i, j, k)).getBlock().getMaterial().isLiquid()) return;
            }
            if (j <= 1) return;
            worldIn.setBlockState(new BlockPos(i, j, k), blockstateIn, 2);
            --j;
        }
    }

    protected boolean generateChestContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, List<WeightedRandomChestContent> listIn, int max) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (!boundingBoxIn.isVecInside(blockpos)) return false;
        if (worldIn.getBlockState(blockpos).getBlock() == Blocks.chest) return false;
        IBlockState iblockstate = Blocks.chest.getDefaultState();
        worldIn.setBlockState(blockpos, Blocks.chest.correctFacing(worldIn, blockpos, iblockstate), 2);
        TileEntity tileentity = worldIn.getTileEntity(blockpos);
        if (!(tileentity instanceof TileEntityChest)) return true;
        WeightedRandomChestContent.generateChestContents(rand, listIn, (TileEntityChest)tileentity, max);
        return true;
    }

    protected boolean generateDispenserContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, int meta, List<WeightedRandomChestContent> listIn, int max) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (!boundingBoxIn.isVecInside(blockpos)) return false;
        if (worldIn.getBlockState(blockpos).getBlock() == Blocks.dispenser) return false;
        worldIn.setBlockState(blockpos, Blocks.dispenser.getStateFromMeta(this.getMetadataWithOffset(Blocks.dispenser, meta)), 2);
        TileEntity tileentity = worldIn.getTileEntity(blockpos);
        if (!(tileentity instanceof TileEntityDispenser)) return true;
        WeightedRandomChestContent.generateDispenserContents(rand, listIn, (TileEntityDispenser)tileentity, max);
        return true;
    }

    protected void placeDoorCurrentPosition(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, EnumFacing facing) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (!boundingBoxIn.isVecInside(blockpos)) return;
        ItemDoor.placeDoor(worldIn, blockpos, facing.rotateYCCW(), Blocks.oak_door);
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

