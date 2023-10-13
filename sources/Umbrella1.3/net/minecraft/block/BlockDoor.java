/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoor
extends Block {
    public static final PropertyDirection FACING_PROP = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool OPEN_PROP = PropertyBool.create("open");
    public static final PropertyEnum HINGEPOSITION_PROP = PropertyEnum.create("hinge", EnumHingePosition.class);
    public static final PropertyBool POWERED_PROP = PropertyBool.create("powered");
    public static final PropertyEnum HALF_PROP = PropertyEnum.create("half", EnumDoorHalf.class);
    private static final String __OBFID = "CL_00000230";

    protected BlockDoor(Material p_i45402_1_) {
        super(p_i45402_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.NORTH)).withProperty(OPEN_PROP, Boolean.valueOf(false)).withProperty(HINGEPOSITION_PROP, (Comparable)((Object)EnumHingePosition.LEFT)).withProperty(POWERED_PROP, Boolean.valueOf(false)).withProperty(HALF_PROP, (Comparable)((Object)EnumDoorHalf.LOWER)));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return BlockDoor.func_176516_g(BlockDoor.func_176515_e(blockAccess, pos));
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        this.func_150011_b(BlockDoor.func_176515_e(access, pos));
    }

    private void func_150011_b(int p_150011_1_) {
        float var2 = 0.1875f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
        EnumFacing var3 = BlockDoor.func_176511_f(p_150011_1_);
        boolean var4 = BlockDoor.func_176516_g(p_150011_1_);
        boolean var5 = BlockDoor.func_176513_j(p_150011_1_);
        if (var4) {
            if (var3 == EnumFacing.EAST) {
                if (!var5) {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, var2);
                } else {
                    this.setBlockBounds(0.0f, 0.0f, 1.0f - var2, 1.0f, 1.0f, 1.0f);
                }
            } else if (var3 == EnumFacing.SOUTH) {
                if (!var5) {
                    this.setBlockBounds(1.0f - var2, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                } else {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, var2, 1.0f, 1.0f);
                }
            } else if (var3 == EnumFacing.WEST) {
                if (!var5) {
                    this.setBlockBounds(0.0f, 0.0f, 1.0f - var2, 1.0f, 1.0f, 1.0f);
                } else {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, var2);
                }
            } else if (var3 == EnumFacing.NORTH) {
                if (!var5) {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, var2, 1.0f, 1.0f);
                } else {
                    this.setBlockBounds(1.0f - var2, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        } else if (var3 == EnumFacing.EAST) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, var2, 1.0f, 1.0f);
        } else if (var3 == EnumFacing.SOUTH) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, var2);
        } else if (var3 == EnumFacing.WEST) {
            this.setBlockBounds(1.0f - var2, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        } else if (var3 == EnumFacing.NORTH) {
            this.setBlockBounds(0.0f, 0.0f, 1.0f - var2, 1.0f, 1.0f, 1.0f);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState var10;
        if (this.blockMaterial == Material.iron) {
            return true;
        }
        BlockPos var9 = state.getValue(HALF_PROP) == EnumDoorHalf.LOWER ? pos : pos.offsetDown();
        IBlockState iBlockState = var10 = pos.equals(var9) ? state : worldIn.getBlockState(var9);
        if (var10.getBlock() != this) {
            return false;
        }
        state = var10.cycleProperty(OPEN_PROP);
        worldIn.setBlockState(var9, state, 2);
        worldIn.markBlockRangeForRenderUpdate(var9, pos);
        worldIn.playAuxSFXAtEntity(playerIn, (Boolean)state.getValue(OPEN_PROP) != false ? 1003 : 1006, pos, 0);
        return true;
    }

    public void func_176512_a(World worldIn, BlockPos p_176512_2_, boolean p_176512_3_) {
        IBlockState var4 = worldIn.getBlockState(p_176512_2_);
        if (var4.getBlock() == this) {
            IBlockState var6;
            BlockPos var5 = var4.getValue(HALF_PROP) == EnumDoorHalf.LOWER ? p_176512_2_ : p_176512_2_.offsetDown();
            IBlockState iBlockState = var6 = p_176512_2_ == var5 ? var4 : worldIn.getBlockState(var5);
            if (var6.getBlock() == this && (Boolean)var6.getValue(OPEN_PROP) != p_176512_3_) {
                worldIn.setBlockState(var5, var6.withProperty(OPEN_PROP, Boolean.valueOf(p_176512_3_)), 2);
                worldIn.markBlockRangeForRenderUpdate(var5, p_176512_2_);
                worldIn.playAuxSFXAtEntity(null, p_176512_3_ ? 1003 : 1006, p_176512_2_, 0);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (state.getValue(HALF_PROP) == EnumDoorHalf.UPPER) {
            BlockPos var5 = pos.offsetDown();
            IBlockState var6 = worldIn.getBlockState(var5);
            if (var6.getBlock() != this) {
                worldIn.setBlockToAir(pos);
            } else if (neighborBlock != this) {
                this.onNeighborBlockChange(worldIn, var5, var6, neighborBlock);
            }
        } else {
            boolean var9 = false;
            BlockPos var10 = pos.offsetUp();
            IBlockState var7 = worldIn.getBlockState(var10);
            if (var7.getBlock() != this) {
                worldIn.setBlockToAir(pos);
                var9 = true;
            }
            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
                worldIn.setBlockToAir(pos);
                var9 = true;
                if (var7.getBlock() == this) {
                    worldIn.setBlockToAir(var10);
                }
            }
            if (var9) {
                if (!worldIn.isRemote) {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            } else {
                boolean var8;
                boolean bl = var8 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(var10);
                if ((var8 || neighborBlock.canProvidePower()) && neighborBlock != this && var8 != (Boolean)var7.getValue(POWERED_PROP)) {
                    worldIn.setBlockState(var10, var7.withProperty(POWERED_PROP, Boolean.valueOf(var8)), 2);
                    if (var8 != (Boolean)state.getValue(OPEN_PROP)) {
                        worldIn.setBlockState(pos, state.withProperty(OPEN_PROP, Boolean.valueOf(var8)), 2);
                        worldIn.markBlockRangeForRenderUpdate(pos, pos);
                        worldIn.playAuxSFXAtEntity(null, var8 ? 1003 : 1006, pos, 0);
                    }
                }
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HALF_PROP) == EnumDoorHalf.UPPER ? null : this.func_176509_j();
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return pos.getY() >= 255 ? false : World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.offsetUp());
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }

    public static int func_176515_e(IBlockAccess p_176515_0_, BlockPos p_176515_1_) {
        IBlockState var2 = p_176515_0_.getBlockState(p_176515_1_);
        int var3 = var2.getBlock().getMetaFromState(var2);
        boolean var4 = BlockDoor.func_176518_i(var3);
        IBlockState var5 = p_176515_0_.getBlockState(p_176515_1_.offsetDown());
        int var6 = var5.getBlock().getMetaFromState(var5);
        int var7 = var4 ? var6 : var3;
        IBlockState var8 = p_176515_0_.getBlockState(p_176515_1_.offsetUp());
        int var9 = var8.getBlock().getMetaFromState(var8);
        int var10 = var4 ? var3 : var9;
        boolean var11 = (var10 & 1) != 0;
        boolean var12 = (var10 & 2) != 0;
        return BlockDoor.func_176510_b(var7) | (var4 ? 8 : 0) | (var11 ? 16 : 0) | (var12 ? 32 : 0);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return this.func_176509_j();
    }

    private Item func_176509_j() {
        return this == Blocks.iron_door ? Items.iron_door : (this == Blocks.spruce_door ? Items.spruce_door : (this == Blocks.birch_door ? Items.birch_door : (this == Blocks.jungle_door ? Items.jungle_door : (this == Blocks.acacia_door ? Items.acacia_door : (this == Blocks.dark_oak_door ? Items.dark_oak_door : Items.oak_door)))));
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        BlockPos var5 = pos.offsetDown();
        if (playerIn.capabilities.isCreativeMode && state.getValue(HALF_PROP) == EnumDoorHalf.UPPER && worldIn.getBlockState(var5).getBlock() == this) {
            worldIn.setBlockToAir(var5);
        }
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (state.getValue(HALF_PROP) == EnumDoorHalf.LOWER) {
            IBlockState var4 = worldIn.getBlockState(pos.offsetUp());
            if (var4.getBlock() == this) {
                state = state.withProperty(HINGEPOSITION_PROP, var4.getValue(HINGEPOSITION_PROP)).withProperty(POWERED_PROP, var4.getValue(POWERED_PROP));
            }
        } else {
            IBlockState var4 = worldIn.getBlockState(pos.offsetDown());
            if (var4.getBlock() == this) {
                state = state.withProperty(FACING_PROP, var4.getValue(FACING_PROP)).withProperty(OPEN_PROP, var4.getValue(OPEN_PROP));
            }
        }
        return state;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(HALF_PROP, (Comparable)((Object)EnumDoorHalf.UPPER)).withProperty(HINGEPOSITION_PROP, (Comparable)((Object)((meta & 1) > 0 ? EnumHingePosition.RIGHT : EnumHingePosition.LEFT))).withProperty(POWERED_PROP, Boolean.valueOf((meta & 2) > 0)) : this.getDefaultState().withProperty(HALF_PROP, (Comparable)((Object)EnumDoorHalf.LOWER)).withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.getHorizontal(meta & 3).rotateYCCW())).withProperty(OPEN_PROP, Boolean.valueOf((meta & 4) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var3;
        int var2 = 0;
        if (state.getValue(HALF_PROP) == EnumDoorHalf.UPPER) {
            var3 = var2 | 8;
            if (state.getValue(HINGEPOSITION_PROP) == EnumHingePosition.RIGHT) {
                var3 |= 1;
            }
            if (((Boolean)state.getValue(POWERED_PROP)).booleanValue()) {
                var3 |= 2;
            }
        } else {
            var3 = var2 | ((EnumFacing)((Object)state.getValue(FACING_PROP))).rotateY().getHorizontalIndex();
            if (((Boolean)state.getValue(OPEN_PROP)).booleanValue()) {
                var3 |= 4;
            }
        }
        return var3;
    }

    protected static int func_176510_b(int p_176510_0_) {
        return p_176510_0_ & 7;
    }

    public static boolean func_176514_f(IBlockAccess p_176514_0_, BlockPos p_176514_1_) {
        return BlockDoor.func_176516_g(BlockDoor.func_176515_e(p_176514_0_, p_176514_1_));
    }

    public static EnumFacing func_176517_h(IBlockAccess p_176517_0_, BlockPos p_176517_1_) {
        return BlockDoor.func_176511_f(BlockDoor.func_176515_e(p_176517_0_, p_176517_1_));
    }

    public static EnumFacing func_176511_f(int p_176511_0_) {
        return EnumFacing.getHorizontal(p_176511_0_ & 3).rotateYCCW();
    }

    protected static boolean func_176516_g(int p_176516_0_) {
        return (p_176516_0_ & 4) != 0;
    }

    protected static boolean func_176518_i(int p_176518_0_) {
        return (p_176518_0_ & 8) != 0;
    }

    protected static boolean func_176513_j(int p_176513_0_) {
        return (p_176513_0_ & 0x10) != 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, HALF_PROP, FACING_PROP, OPEN_PROP, HINGEPOSITION_PROP, POWERED_PROP);
    }

    public static enum EnumDoorHalf implements IStringSerializable
    {
        UPPER("UPPER", 0),
        LOWER("LOWER", 1);

        private static final EnumDoorHalf[] $VALUES;
        private static final String __OBFID = "CL_00002124";

        static {
            $VALUES = new EnumDoorHalf[]{UPPER, LOWER};
        }

        private EnumDoorHalf(String p_i45726_1_, int p_i45726_2_) {
        }

        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this == UPPER ? "upper" : "lower";
        }
    }

    public static enum EnumHingePosition implements IStringSerializable
    {
        LEFT("LEFT", 0),
        RIGHT("RIGHT", 1);

        private static final EnumHingePosition[] $VALUES;
        private static final String __OBFID = "CL_00002123";

        static {
            $VALUES = new EnumHingePosition[]{LEFT, RIGHT};
        }

        private EnumHingePosition(String p_i45725_1_, int p_i45725_2_) {
        }

        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this == LEFT ? "left" : "right";
        }
    }
}

