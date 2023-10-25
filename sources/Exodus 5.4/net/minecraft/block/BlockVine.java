/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine
extends Block {
    public static final PropertyBool EAST;
    public static final PropertyBool[] ALL_FACES;
    public static final PropertyBool NORTH;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    public static final PropertyBool UP;

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, UP, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (!world.isRemote && !this.recheckGrownSides(world, blockPos, iBlockState)) {
            this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            world.setBlockToAir(blockPos);
        }
    }

    public static int getNumGrownFaces(IBlockState iBlockState) {
        int n = 0;
        PropertyBool[] propertyBoolArray = ALL_FACES;
        int n2 = ALL_FACES.length;
        int n3 = 0;
        while (n3 < n2) {
            PropertyBool propertyBool = propertyBoolArray[n3];
            if (iBlockState.getValue(propertyBool).booleanValue()) {
                ++n;
            }
            ++n3;
        }
        return n;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer entityPlayer, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity) {
        if (!world.isRemote && entityPlayer.getCurrentEquippedItem() != null && entityPlayer.getCurrentEquippedItem().getItem() == Items.shears) {
            entityPlayer.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            BlockVine.spawnAsEntity(world, blockPos, new ItemStack(Blocks.vine, 1, 0));
        } else {
            super.harvestBlock(world, entityPlayer, blockPos, iBlockState, tileEntity);
        }
    }

    private boolean recheckGrownSides(World world, BlockPos blockPos, IBlockState iBlockState) {
        IBlockState iBlockState2 = iBlockState;
        for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
            IBlockState iBlockState3;
            PropertyBool propertyBool = BlockVine.getPropertyFor(enumFacing);
            if (!iBlockState.getValue(propertyBool).booleanValue() || this.canPlaceOn(world.getBlockState(blockPos.offset(enumFacing)).getBlock()) || (iBlockState3 = world.getBlockState(blockPos.up())).getBlock() == this && iBlockState3.getValue(propertyBool).booleanValue()) continue;
            iBlockState = iBlockState.withProperty(propertyBool, false);
        }
        if (BlockVine.getNumGrownFaces(iBlockState) == 0) {
            return false;
        }
        if (iBlockState2 != iBlockState) {
            world.setBlockState(blockPos, iBlockState, 2);
        }
        return true;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        IBlockState iBlockState = this.getDefaultState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false);
        return enumFacing.getAxis().isHorizontal() ? iBlockState.withProperty(BlockVine.getPropertyFor(enumFacing.getOpposite()), true) : iBlockState;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (!world.isRemote && world.rand.nextInt(4) == 0) {
            int n = 4;
            int n2 = 5;
            boolean bl = false;
            int n3 = -n;
            block0: while (n3 <= n) {
                int n4 = -n;
                while (n4 <= n) {
                    int n5 = -1;
                    while (n5 <= 1) {
                        if (world.getBlockState(blockPos.add(n3, n5, n4)).getBlock() == this && --n2 <= 0) {
                            bl = true;
                            break block0;
                        }
                        ++n5;
                    }
                    ++n4;
                }
                ++n3;
            }
            EnumFacing enumFacing = EnumFacing.random(random);
            BlockPos blockPos2 = blockPos.up();
            if (enumFacing == EnumFacing.UP && blockPos.getY() < 255 && world.isAirBlock(blockPos2)) {
                if (!bl) {
                    IBlockState iBlockState2 = iBlockState;
                    for (EnumFacing enumFacing2 : EnumFacing.Plane.HORIZONTAL) {
                        if (!random.nextBoolean() && this.canPlaceOn(world.getBlockState(blockPos2.offset(enumFacing2)).getBlock())) continue;
                        iBlockState2 = iBlockState2.withProperty(BlockVine.getPropertyFor(enumFacing2), false);
                    }
                    if (iBlockState2.getValue(NORTH).booleanValue() || iBlockState2.getValue(EAST).booleanValue() || iBlockState2.getValue(SOUTH).booleanValue() || iBlockState2.getValue(WEST).booleanValue()) {
                        world.setBlockState(blockPos2, iBlockState2, 2);
                    }
                }
            } else if (enumFacing.getAxis().isHorizontal() && !iBlockState.getValue(BlockVine.getPropertyFor(enumFacing)).booleanValue()) {
                if (!bl) {
                    BlockPos blockPos3 = blockPos.offset(enumFacing);
                    Block block = world.getBlockState(blockPos3).getBlock();
                    if (block.blockMaterial == Material.air) {
                        EnumFacing enumFacing3 = enumFacing.rotateY();
                        EnumFacing enumFacing4 = enumFacing.rotateYCCW();
                        boolean bl2 = iBlockState.getValue(BlockVine.getPropertyFor(enumFacing3));
                        boolean bl3 = iBlockState.getValue(BlockVine.getPropertyFor(enumFacing4));
                        BlockPos blockPos4 = blockPos3.offset(enumFacing3);
                        BlockPos blockPos5 = blockPos3.offset(enumFacing4);
                        if (bl2 && this.canPlaceOn(world.getBlockState(blockPos4).getBlock())) {
                            world.setBlockState(blockPos3, this.getDefaultState().withProperty(BlockVine.getPropertyFor(enumFacing3), true), 2);
                        } else if (bl3 && this.canPlaceOn(world.getBlockState(blockPos5).getBlock())) {
                            world.setBlockState(blockPos3, this.getDefaultState().withProperty(BlockVine.getPropertyFor(enumFacing4), true), 2);
                        } else if (bl2 && world.isAirBlock(blockPos4) && this.canPlaceOn(world.getBlockState(blockPos.offset(enumFacing3)).getBlock())) {
                            world.setBlockState(blockPos4, this.getDefaultState().withProperty(BlockVine.getPropertyFor(enumFacing.getOpposite()), true), 2);
                        } else if (bl3 && world.isAirBlock(blockPos5) && this.canPlaceOn(world.getBlockState(blockPos.offset(enumFacing4)).getBlock())) {
                            world.setBlockState(blockPos5, this.getDefaultState().withProperty(BlockVine.getPropertyFor(enumFacing.getOpposite()), true), 2);
                        } else if (this.canPlaceOn(world.getBlockState(blockPos3.up()).getBlock())) {
                            world.setBlockState(blockPos3, this.getDefaultState(), 2);
                        }
                    } else if (block.blockMaterial.isOpaque() && block.isFullCube()) {
                        world.setBlockState(blockPos, iBlockState.withProperty(BlockVine.getPropertyFor(enumFacing), true), 2);
                    }
                }
            } else if (blockPos.getY() > 1) {
                BlockPos blockPos6 = blockPos.down();
                IBlockState iBlockState3 = world.getBlockState(blockPos6);
                Block block = iBlockState3.getBlock();
                if (block.blockMaterial == Material.air) {
                    IBlockState iBlockState4 = iBlockState;
                    for (EnumFacing enumFacing5 : EnumFacing.Plane.HORIZONTAL) {
                        if (!random.nextBoolean()) continue;
                        iBlockState4 = iBlockState4.withProperty(BlockVine.getPropertyFor(enumFacing5), false);
                    }
                    if (iBlockState4.getValue(NORTH).booleanValue() || iBlockState4.getValue(EAST).booleanValue() || iBlockState4.getValue(SOUTH).booleanValue() || iBlockState4.getValue(WEST).booleanValue()) {
                        world.setBlockState(blockPos6, iBlockState4, 2);
                    }
                } else if (block == this) {
                    IBlockState iBlockState5 = iBlockState3;
                    for (EnumFacing enumFacing6 : EnumFacing.Plane.HORIZONTAL) {
                        PropertyBool propertyBool = BlockVine.getPropertyFor(enumFacing6);
                        if (!random.nextBoolean() || !iBlockState.getValue(propertyBool).booleanValue()) continue;
                        iBlockState5 = iBlockState5.withProperty(propertyBool, true);
                    }
                    if (iBlockState5.getValue(NORTH).booleanValue() || iBlockState5.getValue(EAST).booleanValue() || iBlockState5.getValue(SOUTH).booleanValue() || iBlockState5.getValue(WEST).booleanValue()) {
                        world.setBlockState(blockPos6, iBlockState5, 2);
                    }
                }
            }
        }
    }

    @Override
    public int colorMultiplier(IBlockAccess iBlockAccess, BlockPos blockPos, int n) {
        return iBlockAccess.getBiomeGenForCoords(blockPos).getFoliageColorAtPos(blockPos);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public BlockVine() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos blockPos, EnumFacing enumFacing) {
        switch (enumFacing) {
            case UP: {
                return this.canPlaceOn(world.getBlockState(blockPos.up()).getBlock());
            }
            case NORTH: 
            case SOUTH: 
            case WEST: 
            case EAST: {
                return this.canPlaceOn(world.getBlockState(blockPos.offset(enumFacing.getOpposite())).getBlock());
            }
        }
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        if (iBlockState.getValue(SOUTH).booleanValue()) {
            n |= 1;
        }
        if (iBlockState.getValue(WEST).booleanValue()) {
            n |= 2;
        }
        if (iBlockState.getValue(NORTH).booleanValue()) {
            n |= 4;
        }
        if (iBlockState.getValue(EAST).booleanValue()) {
            n |= 8;
        }
        return n;
    }

    static {
        UP = PropertyBool.create("up");
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
        ALL_FACES = new PropertyBool[]{UP, NORTH, SOUTH, WEST, EAST};
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        return null;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return null;
    }

    public static PropertyBool getPropertyFor(EnumFacing enumFacing) {
        switch (enumFacing) {
            case UP: {
                return UP;
            }
            case NORTH: {
                return NORTH;
            }
            case SOUTH: {
                return SOUTH;
            }
            case EAST: {
                return EAST;
            }
            case WEST: {
                return WEST;
            }
        }
        throw new IllegalArgumentException(enumFacing + " is an invalid choice");
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        float f = 0.0625f;
        float f2 = 1.0f;
        float f3 = 1.0f;
        float f4 = 1.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        boolean bl = false;
        if (iBlockAccess.getBlockState(blockPos).getValue(WEST).booleanValue()) {
            f5 = Math.max(f5, 0.0625f);
            f2 = 0.0f;
            f3 = 0.0f;
            f6 = 1.0f;
            f4 = 0.0f;
            f7 = 1.0f;
            bl = true;
        }
        if (iBlockAccess.getBlockState(blockPos).getValue(EAST).booleanValue()) {
            f2 = Math.min(f2, 0.9375f);
            f5 = 1.0f;
            f3 = 0.0f;
            f6 = 1.0f;
            f4 = 0.0f;
            f7 = 1.0f;
            bl = true;
        }
        if (iBlockAccess.getBlockState(blockPos).getValue(NORTH).booleanValue()) {
            f7 = Math.max(f7, 0.0625f);
            f4 = 0.0f;
            f2 = 0.0f;
            f5 = 1.0f;
            f3 = 0.0f;
            f6 = 1.0f;
            bl = true;
        }
        if (iBlockAccess.getBlockState(blockPos).getValue(SOUTH).booleanValue()) {
            f4 = Math.min(f4, 0.9375f);
            f7 = 1.0f;
            f2 = 0.0f;
            f5 = 1.0f;
            f3 = 0.0f;
            f6 = 1.0f;
            bl = true;
        }
        if (!bl && this.canPlaceOn(iBlockAccess.getBlockState(blockPos.up()).getBlock())) {
            f3 = Math.min(f3, 0.9375f);
            f6 = 1.0f;
            f2 = 0.0f;
            f5 = 1.0f;
            f4 = 0.0f;
            f7 = 1.0f;
        }
        this.setBlockBounds(f2, f3, f4, f5, f6, f7);
    }

    @Override
    public int getRenderColor(IBlockState iBlockState) {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        return iBlockState.withProperty(UP, iBlockAccess.getBlockState(blockPos.up()).getBlock().isBlockNormalCube());
    }

    @Override
    public boolean isReplaceable(World world, BlockPos blockPos) {
        return true;
    }

    private boolean canPlaceOn(Block block) {
        return block.isFullCube() && block.blockMaterial.blocksMovement();
    }

    @Override
    public int getBlockColor() {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(SOUTH, (n & 1) > 0).withProperty(WEST, (n & 2) > 0).withProperty(NORTH, (n & 4) > 0).withProperty(EAST, (n & 8) > 0);
    }
}

