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
    protected int componentType;
    protected EnumFacing coordBaseMode;

    protected void randomlyRareFillWithBlocks(World world, StructureBoundingBox structureBoundingBox, int n, int n2, int n3, int n4, int n5, int n6, IBlockState iBlockState, boolean bl) {
        float f = n4 - n + 1;
        float f2 = n5 - n2 + 1;
        float f3 = n6 - n3 + 1;
        float f4 = (float)n + f / 2.0f;
        float f5 = (float)n3 + f3 / 2.0f;
        int n7 = n2;
        while (n7 <= n5) {
            float f6 = (float)(n7 - n2) / f2;
            int n8 = n;
            while (n8 <= n4) {
                float f7 = ((float)n8 - f4) / (f * 0.5f);
                int n9 = n3;
                while (n9 <= n6) {
                    float f8;
                    float f9 = ((float)n9 - f5) / (f3 * 0.5f);
                    if ((!bl || this.getBlockStateFromPos(world, n8, n7, n9, structureBoundingBox).getBlock().getMaterial() != Material.air) && (f8 = f7 * f7 + f6 * f6 + f9 * f9) <= 1.05f) {
                        this.setBlockState(world, iBlockState, n8, n7, n9, structureBoundingBox);
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
    }

    protected abstract void readStructureFromNBT(NBTTagCompound var1);

    protected StructureComponent(int n) {
        this.componentType = n;
    }

    public NBTTagCompound createStructureBaseNBT() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        nBTTagCompound.setString("id", MapGenStructureIO.getStructureComponentName(this));
        nBTTagCompound.setTag("BB", this.boundingBox.toNBTTagIntArray());
        nBTTagCompound.setInteger("O", this.coordBaseMode == null ? -1 : this.coordBaseMode.getHorizontalIndex());
        nBTTagCompound.setInteger("GD", this.componentType);
        this.writeStructureToNBT(nBTTagCompound);
        return nBTTagCompound;
    }

    protected int getXWithOffset(int n, int n2) {
        if (this.coordBaseMode == null) {
            return n;
        }
        switch (this.coordBaseMode) {
            case NORTH: 
            case SOUTH: {
                return this.boundingBox.minX + n;
            }
            case WEST: {
                return this.boundingBox.maxX - n2;
            }
            case EAST: {
                return this.boundingBox.minX + n2;
            }
        }
        return n;
    }

    public void readStructureBaseNBT(World world, NBTTagCompound nBTTagCompound) {
        int n;
        if (nBTTagCompound.hasKey("BB")) {
            this.boundingBox = new StructureBoundingBox(nBTTagCompound.getIntArray("BB"));
        }
        this.coordBaseMode = (n = nBTTagCompound.getInteger("O")) == -1 ? null : EnumFacing.getHorizontal(n);
        this.componentType = nBTTagCompound.getInteger("GD");
        this.readStructureFromNBT(nBTTagCompound);
    }

    protected void fillWithBlocks(World world, StructureBoundingBox structureBoundingBox, int n, int n2, int n3, int n4, int n5, int n6, IBlockState iBlockState, IBlockState iBlockState2, boolean bl) {
        int n7 = n2;
        while (n7 <= n5) {
            int n8 = n;
            while (n8 <= n4) {
                int n9 = n3;
                while (n9 <= n6) {
                    if (!bl || this.getBlockStateFromPos(world, n8, n7, n9, structureBoundingBox).getBlock().getMaterial() != Material.air) {
                        if (n7 != n2 && n7 != n5 && n8 != n && n8 != n4 && n9 != n3 && n9 != n6) {
                            this.setBlockState(world, iBlockState2, n8, n7, n9, structureBoundingBox);
                        } else {
                            this.setBlockState(world, iBlockState, n8, n7, n9, structureBoundingBox);
                        }
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
    }

    public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
    }

    protected int getZWithOffset(int n, int n2) {
        if (this.coordBaseMode == null) {
            return n2;
        }
        switch (this.coordBaseMode) {
            case NORTH: {
                return this.boundingBox.maxZ - n2;
            }
            case SOUTH: {
                return this.boundingBox.minZ + n2;
            }
            case WEST: 
            case EAST: {
                return this.boundingBox.minZ + n;
            }
        }
        return n2;
    }

    protected void setBlockState(World world, IBlockState iBlockState, int n, int n2, int n3, StructureBoundingBox structureBoundingBox) {
        BlockPos blockPos = new BlockPos(this.getXWithOffset(n, n3), this.getYWithOffset(n2), this.getZWithOffset(n, n3));
        if (structureBoundingBox.isVecInside(blockPos)) {
            world.setBlockState(blockPos, iBlockState, 2);
        }
    }

    protected IBlockState getBlockStateFromPos(World world, int n, int n2, int n3, StructureBoundingBox structureBoundingBox) {
        int n4;
        int n5;
        int n6 = this.getXWithOffset(n, n3);
        BlockPos blockPos = new BlockPos(n6, n5 = this.getYWithOffset(n2), n4 = this.getZWithOffset(n, n3));
        return !structureBoundingBox.isVecInside(blockPos) ? Blocks.air.getDefaultState() : world.getBlockState(blockPos);
    }

    public int getComponentType() {
        return this.componentType;
    }

    public void func_181138_a(int n, int n2, int n3) {
        this.boundingBox.offset(n, n2, n3);
    }

    protected int getYWithOffset(int n) {
        return this.coordBaseMode == null ? n : n + this.boundingBox.minY;
    }

    protected int getMetadataWithOffset(Block block, int n) {
        if (block == Blocks.rail) {
            if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.EAST) {
                if (n == 1) {
                    return 0;
                }
                return 1;
            }
        } else if (block instanceof BlockDoor) {
            if (this.coordBaseMode == EnumFacing.SOUTH) {
                if (n == 0) {
                    return 2;
                }
                if (n == 2) {
                    return 0;
                }
            } else {
                if (this.coordBaseMode == EnumFacing.WEST) {
                    return n + 1 & 3;
                }
                if (this.coordBaseMode == EnumFacing.EAST) {
                    return n + 3 & 3;
                }
            }
        } else if (block != Blocks.stone_stairs && block != Blocks.oak_stairs && block != Blocks.nether_brick_stairs && block != Blocks.stone_brick_stairs && block != Blocks.sandstone_stairs) {
            if (block == Blocks.ladder) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (n == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                    if (n == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (n == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (n == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (n == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (n == EnumFacing.EAST.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (n == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (n == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (n == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (n == EnumFacing.EAST.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                }
            } else if (block == Blocks.stone_button) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (n == 3) {
                        return 4;
                    }
                    if (n == 4) {
                        return 3;
                    }
                } else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (n == 3) {
                        return 1;
                    }
                    if (n == 4) {
                        return 2;
                    }
                    if (n == 2) {
                        return 3;
                    }
                    if (n == 1) {
                        return 4;
                    }
                } else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (n == 3) {
                        return 2;
                    }
                    if (n == 4) {
                        return 1;
                    }
                    if (n == 2) {
                        return 3;
                    }
                    if (n == 1) {
                        return 4;
                    }
                }
            } else if (block != Blocks.tripwire_hook && !(block instanceof BlockDirectional)) {
                if (block == Blocks.piston || block == Blocks.sticky_piston || block == Blocks.lever || block == Blocks.dispenser) {
                    if (this.coordBaseMode == EnumFacing.SOUTH) {
                        if (n == EnumFacing.NORTH.getIndex() || n == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.getFront(n).getOpposite().getIndex();
                        }
                    } else if (this.coordBaseMode == EnumFacing.WEST) {
                        if (n == EnumFacing.NORTH.getIndex()) {
                            return EnumFacing.WEST.getIndex();
                        }
                        if (n == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.EAST.getIndex();
                        }
                        if (n == EnumFacing.WEST.getIndex()) {
                            return EnumFacing.NORTH.getIndex();
                        }
                        if (n == EnumFacing.EAST.getIndex()) {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    } else if (this.coordBaseMode == EnumFacing.EAST) {
                        if (n == EnumFacing.NORTH.getIndex()) {
                            return EnumFacing.EAST.getIndex();
                        }
                        if (n == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.WEST.getIndex();
                        }
                        if (n == EnumFacing.WEST.getIndex()) {
                            return EnumFacing.NORTH.getIndex();
                        }
                        if (n == EnumFacing.EAST.getIndex()) {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    }
                }
            } else {
                EnumFacing enumFacing = EnumFacing.getHorizontal(n);
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (enumFacing == EnumFacing.SOUTH || enumFacing == EnumFacing.NORTH) {
                        return enumFacing.getOpposite().getHorizontalIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (enumFacing == EnumFacing.NORTH) {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }
                    if (enumFacing == EnumFacing.SOUTH) {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }
                    if (enumFacing == EnumFacing.WEST) {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }
                    if (enumFacing == EnumFacing.EAST) {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (enumFacing == EnumFacing.NORTH) {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }
                    if (enumFacing == EnumFacing.SOUTH) {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }
                    if (enumFacing == EnumFacing.WEST) {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }
                    if (enumFacing == EnumFacing.EAST) {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                }
            }
        } else if (this.coordBaseMode == EnumFacing.SOUTH) {
            if (n == 2) {
                return 3;
            }
            if (n == 3) {
                return 2;
            }
        } else if (this.coordBaseMode == EnumFacing.WEST) {
            if (n == 0) {
                return 2;
            }
            if (n == 1) {
                return 3;
            }
            if (n == 2) {
                return 0;
            }
            if (n == 3) {
                return 1;
            }
        } else if (this.coordBaseMode == EnumFacing.EAST) {
            if (n == 0) {
                return 2;
            }
            if (n == 1) {
                return 3;
            }
            if (n == 2) {
                return 1;
            }
            if (n == 3) {
                return 0;
            }
        }
        return n;
    }

    protected void placeDoorCurrentPosition(World world, StructureBoundingBox structureBoundingBox, Random random, int n, int n2, int n3, EnumFacing enumFacing) {
        BlockPos blockPos = new BlockPos(this.getXWithOffset(n, n3), this.getYWithOffset(n2), this.getZWithOffset(n, n3));
        if (structureBoundingBox.isVecInside(blockPos)) {
            ItemDoor.placeDoor(world, blockPos, enumFacing.rotateYCCW(), Blocks.oak_door);
        }
    }

    public StructureComponent() {
    }

    protected void fillWithAir(World world, StructureBoundingBox structureBoundingBox, int n, int n2, int n3, int n4, int n5, int n6) {
        int n7 = n2;
        while (n7 <= n5) {
            int n8 = n;
            while (n8 <= n4) {
                int n9 = n3;
                while (n9 <= n6) {
                    this.setBlockState(world, Blocks.air.getDefaultState(), n8, n7, n9, structureBoundingBox);
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
    }

    protected void replaceAirAndLiquidDownwards(World world, IBlockState iBlockState, int n, int n2, int n3, StructureBoundingBox structureBoundingBox) {
        int n4;
        int n5;
        int n6 = this.getXWithOffset(n, n3);
        if (structureBoundingBox.isVecInside(new BlockPos(n6, n5 = this.getYWithOffset(n2), n4 = this.getZWithOffset(n, n3)))) {
            while ((world.isAirBlock(new BlockPos(n6, n5, n4)) || world.getBlockState(new BlockPos(n6, n5, n4)).getBlock().getMaterial().isLiquid()) && n5 > 1) {
                world.setBlockState(new BlockPos(n6, n5, n4), iBlockState, 2);
                --n5;
            }
        }
    }

    protected void randomlyPlaceBlock(World world, StructureBoundingBox structureBoundingBox, Random random, float f, int n, int n2, int n3, IBlockState iBlockState) {
        if (random.nextFloat() < f) {
            this.setBlockState(world, iBlockState, n, n2, n3, structureBoundingBox);
        }
    }

    protected abstract void writeStructureToNBT(NBTTagCompound var1);

    public BlockPos getBoundingBoxCenter() {
        return new BlockPos(this.boundingBox.getCenter());
    }

    protected void clearCurrentPositionBlocksUpwards(World world, int n, int n2, int n3, StructureBoundingBox structureBoundingBox) {
        BlockPos blockPos = new BlockPos(this.getXWithOffset(n, n3), this.getYWithOffset(n2), this.getZWithOffset(n, n3));
        if (structureBoundingBox.isVecInside(blockPos)) {
            while (!world.isAirBlock(blockPos) && blockPos.getY() < 255) {
                world.setBlockState(blockPos, Blocks.air.getDefaultState(), 2);
                blockPos = blockPos.up();
            }
        }
    }

    protected boolean generateDispenserContents(World world, StructureBoundingBox structureBoundingBox, Random random, int n, int n2, int n3, int n4, List<WeightedRandomChestContent> list, int n5) {
        BlockPos blockPos = new BlockPos(this.getXWithOffset(n, n3), this.getYWithOffset(n2), this.getZWithOffset(n, n3));
        if (structureBoundingBox.isVecInside(blockPos) && world.getBlockState(blockPos).getBlock() != Blocks.dispenser) {
            world.setBlockState(blockPos, Blocks.dispenser.getStateFromMeta(this.getMetadataWithOffset(Blocks.dispenser, n4)), 2);
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof TileEntityDispenser) {
                WeightedRandomChestContent.generateDispenserContents(random, list, (TileEntityDispenser)tileEntity, n5);
            }
            return true;
        }
        return false;
    }

    protected boolean generateChestContents(World world, StructureBoundingBox structureBoundingBox, Random random, int n, int n2, int n3, List<WeightedRandomChestContent> list, int n4) {
        BlockPos blockPos = new BlockPos(this.getXWithOffset(n, n3), this.getYWithOffset(n2), this.getZWithOffset(n, n3));
        if (structureBoundingBox.isVecInside(blockPos) && world.getBlockState(blockPos).getBlock() != Blocks.chest) {
            IBlockState iBlockState = Blocks.chest.getDefaultState();
            world.setBlockState(blockPos, Blocks.chest.correctFacing(world, blockPos, iBlockState), 2);
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof TileEntityChest) {
                WeightedRandomChestContent.generateChestContents(random, list, (TileEntityChest)tileEntity, n4);
            }
            return true;
        }
        return false;
    }

    public StructureBoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    protected void func_175805_a(World world, StructureBoundingBox structureBoundingBox, Random random, float f, int n, int n2, int n3, int n4, int n5, int n6, IBlockState iBlockState, IBlockState iBlockState2, boolean bl) {
        int n7 = n2;
        while (n7 <= n5) {
            int n8 = n;
            while (n8 <= n4) {
                int n9 = n3;
                while (n9 <= n6) {
                    if (random.nextFloat() <= f && (!bl || this.getBlockStateFromPos(world, n8, n7, n9, structureBoundingBox).getBlock().getMaterial() != Material.air)) {
                        if (n7 != n2 && n7 != n5 && n8 != n && n8 != n4 && n9 != n3 && n9 != n6) {
                            this.setBlockState(world, iBlockState2, n8, n7, n9, structureBoundingBox);
                        } else {
                            this.setBlockState(world, iBlockState, n8, n7, n9, structureBoundingBox);
                        }
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
    }

    public abstract boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3);

    protected boolean isLiquidInStructureBoundingBox(World world, StructureBoundingBox structureBoundingBox) {
        int n;
        int n2 = Math.max(this.boundingBox.minX - 1, structureBoundingBox.minX);
        int n3 = Math.max(this.boundingBox.minY - 1, structureBoundingBox.minY);
        int n4 = Math.max(this.boundingBox.minZ - 1, structureBoundingBox.minZ);
        int n5 = Math.min(this.boundingBox.maxX + 1, structureBoundingBox.maxX);
        int n6 = Math.min(this.boundingBox.maxY + 1, structureBoundingBox.maxY);
        int n7 = Math.min(this.boundingBox.maxZ + 1, structureBoundingBox.maxZ);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n8 = n2;
        while (n8 <= n5) {
            n = n4;
            while (n <= n7) {
                if (world.getBlockState(mutableBlockPos.func_181079_c(n8, n3, n)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (world.getBlockState(mutableBlockPos.func_181079_c(n8, n6, n)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                ++n;
            }
            ++n8;
        }
        n8 = n2;
        while (n8 <= n5) {
            n = n3;
            while (n <= n6) {
                if (world.getBlockState(mutableBlockPos.func_181079_c(n8, n, n4)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (world.getBlockState(mutableBlockPos.func_181079_c(n8, n, n7)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                ++n;
            }
            ++n8;
        }
        n8 = n4;
        while (n8 <= n7) {
            n = n3;
            while (n <= n6) {
                if (world.getBlockState(mutableBlockPos.func_181079_c(n2, n, n8)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (world.getBlockState(mutableBlockPos.func_181079_c(n5, n, n8)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                ++n;
            }
            ++n8;
        }
        return false;
    }

    protected void fillWithRandomizedBlocks(World world, StructureBoundingBox structureBoundingBox, int n, int n2, int n3, int n4, int n5, int n6, boolean bl, Random random, BlockSelector blockSelector) {
        int n7 = n2;
        while (n7 <= n5) {
            int n8 = n;
            while (n8 <= n4) {
                int n9 = n3;
                while (n9 <= n6) {
                    if (!bl || this.getBlockStateFromPos(world, n8, n7, n9, structureBoundingBox).getBlock().getMaterial() != Material.air) {
                        blockSelector.selectBlocks(random, n8, n7, n9, n7 == n2 || n7 == n5 || n8 == n || n8 == n4 || n9 == n3 || n9 == n6);
                        this.setBlockState(world, blockSelector.getBlockState(), n8, n7, n9, structureBoundingBox);
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
    }

    public static StructureComponent findIntersecting(List<StructureComponent> list, StructureBoundingBox structureBoundingBox) {
        for (StructureComponent structureComponent : list) {
            if (structureComponent.getBoundingBox() == null || !structureComponent.getBoundingBox().intersectsWith(structureBoundingBox)) continue;
            return structureComponent;
        }
        return null;
    }

    public static abstract class BlockSelector {
        protected IBlockState blockstate = Blocks.air.getDefaultState();

        public IBlockState getBlockState() {
            return this.blockstate;
        }

        public abstract void selectBlocks(Random var1, int var2, int var3, int var4, boolean var5);
    }
}

