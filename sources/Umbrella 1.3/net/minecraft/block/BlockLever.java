/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever
extends Block {
    public static final PropertyEnum FACING = PropertyEnum.create("facing", EnumOrientation.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    private static final String __OBFID = "CL_00000264";

    protected BlockLever() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, (Comparable)((Object)EnumOrientation.NORTH)).withProperty(POWERED, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP && World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) ? true : this.func_176358_d(worldIn, pos.offset(side.getOpposite()));
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return this.func_176358_d(worldIn, pos.offsetWest()) ? true : (this.func_176358_d(worldIn, pos.offsetEast()) ? true : (this.func_176358_d(worldIn, pos.offsetNorth()) ? true : (this.func_176358_d(worldIn, pos.offsetSouth()) ? true : (World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) ? true : this.func_176358_d(worldIn, pos.offsetUp())))));
    }

    protected boolean func_176358_d(World worldIn, BlockPos p_176358_2_) {
        return worldIn.getBlockState(p_176358_2_).getBlock().isNormalCube();
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing var11;
        IBlockState var9 = this.getDefaultState().withProperty(POWERED, Boolean.valueOf(false));
        if (this.func_176358_d(worldIn, pos.offset(facing.getOpposite()))) {
            return var9.withProperty(FACING, (Comparable)((Object)EnumOrientation.func_176856_a(facing, placer.func_174811_aO())));
        }
        Iterator var10 = EnumFacing.Plane.HORIZONTAL.iterator();
        do {
            if (var10.hasNext()) continue;
            if (World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
                return var9.withProperty(FACING, (Comparable)((Object)EnumOrientation.func_176856_a(EnumFacing.UP, placer.func_174811_aO())));
            }
            return var9;
        } while ((var11 = (EnumFacing)var10.next()) == facing || !this.func_176358_d(worldIn, pos.offset(var11.getOpposite())));
        return var9.withProperty(FACING, (Comparable)((Object)EnumOrientation.func_176856_a(var11, placer.func_174811_aO())));
    }

    public static int func_176357_a(EnumFacing p_176357_0_) {
        switch (p_176357_0_) {
            case DOWN: {
                return 0;
            }
            case UP: {
                return 5;
            }
            case NORTH: {
                return 4;
            }
            case SOUTH: {
                return 3;
            }
            case WEST: {
                return 2;
            }
            case EAST: {
                return 1;
            }
        }
        return -1;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (this.func_176356_e(worldIn, pos) && !this.func_176358_d(worldIn, pos.offset(((EnumOrientation)((Object)state.getValue(FACING))).func_176852_c().getOpposite()))) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean func_176356_e(World worldIn, BlockPos p_176356_2_) {
        if (this.canPlaceBlockAt(worldIn, p_176356_2_)) {
            return true;
        }
        this.dropBlockAsItem(worldIn, p_176356_2_, worldIn.getBlockState(p_176356_2_), 0);
        worldIn.setBlockToAir(p_176356_2_);
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        float var3 = 0.1875f;
        switch ((EnumOrientation)((Object)access.getBlockState(pos).getValue(FACING))) {
            case EAST: {
                this.setBlockBounds(0.0f, 0.2f, 0.5f - var3, var3 * 2.0f, 0.8f, 0.5f + var3);
                break;
            }
            case WEST: {
                this.setBlockBounds(1.0f - var3 * 2.0f, 0.2f, 0.5f - var3, 1.0f, 0.8f, 0.5f + var3);
                break;
            }
            case SOUTH: {
                this.setBlockBounds(0.5f - var3, 0.2f, 0.0f, 0.5f + var3, 0.8f, var3 * 2.0f);
                break;
            }
            case NORTH: {
                this.setBlockBounds(0.5f - var3, 0.2f, 1.0f - var3 * 2.0f, 0.5f + var3, 0.8f, 1.0f);
                break;
            }
            case UP_Z: 
            case UP_X: {
                var3 = 0.25f;
                this.setBlockBounds(0.5f - var3, 0.0f, 0.5f - var3, 0.5f + var3, 0.6f, 0.5f + var3);
                break;
            }
            case DOWN_X: 
            case DOWN_Z: {
                var3 = 0.25f;
                this.setBlockBounds(0.5f - var3, 0.4f, 0.5f - var3, 0.5f + var3, 1.0f, 0.5f + var3);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        state = state.cycleProperty(POWERED);
        worldIn.setBlockState(pos, state, 3);
        worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, (Boolean)state.getValue(POWERED) != false ? 0.6f : 0.5f);
        worldIn.notifyNeighborsOfStateChange(pos, this);
        EnumFacing var9 = ((EnumOrientation)((Object)state.getValue(FACING))).func_176852_c();
        worldIn.notifyNeighborsOfStateChange(pos.offset(var9.getOpposite()), this);
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (((Boolean)state.getValue(POWERED)).booleanValue()) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            EnumFacing var4 = ((EnumOrientation)((Object)state.getValue(FACING))).func_176852_c();
            worldIn.notifyNeighborsOfStateChange(pos.offset(var4.getOpposite()), this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue(POWERED) != false ? 15 : 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue(POWERED) == false ? 0 : (((EnumOrientation)((Object)state.getValue(FACING))).func_176852_c() == side ? 15 : 0);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, (Comparable)((Object)EnumOrientation.func_176853_a(meta & 7))).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumOrientation)((Object)state.getValue(FACING))).func_176855_a();
        if (((Boolean)state.getValue(POWERED)).booleanValue()) {
            var3 |= 8;
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, POWERED);
    }

    public static enum EnumOrientation implements IStringSerializable
    {
        DOWN_X("DOWN_X", 0, 0, "down_x", EnumFacing.DOWN),
        EAST("EAST", 1, 1, "east", EnumFacing.EAST),
        WEST("WEST", 2, 2, "west", EnumFacing.WEST),
        SOUTH("SOUTH", 3, 3, "south", EnumFacing.SOUTH),
        NORTH("NORTH", 4, 4, "north", EnumFacing.NORTH),
        UP_Z("UP_Z", 5, 5, "up_z", EnumFacing.UP),
        UP_X("UP_X", 6, 6, "up_x", EnumFacing.UP),
        DOWN_Z("DOWN_Z", 7, 7, "down_z", EnumFacing.DOWN);

        private static final EnumOrientation[] field_176869_i;
        private final int field_176866_j;
        private final String field_176867_k;
        private final EnumFacing field_176864_l;
        private static final EnumOrientation[] $VALUES;
        private static final String __OBFID = "CL_00002102";

        static {
            field_176869_i = new EnumOrientation[EnumOrientation.values().length];
            $VALUES = new EnumOrientation[]{DOWN_X, EAST, WEST, SOUTH, NORTH, UP_Z, UP_X, DOWN_Z};
            EnumOrientation[] var0 = EnumOrientation.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumOrientation var3;
                EnumOrientation.field_176869_i[var3.func_176855_a()] = var3 = var0[var2];
            }
        }

        private EnumOrientation(String p_i45709_1_, int p_i45709_2_, int p_i45709_3_, String p_i45709_4_, EnumFacing p_i45709_5_) {
            this.field_176866_j = p_i45709_3_;
            this.field_176867_k = p_i45709_4_;
            this.field_176864_l = p_i45709_5_;
        }

        public int func_176855_a() {
            return this.field_176866_j;
        }

        public EnumFacing func_176852_c() {
            return this.field_176864_l;
        }

        public String toString() {
            return this.field_176867_k;
        }

        public static EnumOrientation func_176853_a(int p_176853_0_) {
            if (p_176853_0_ < 0 || p_176853_0_ >= field_176869_i.length) {
                p_176853_0_ = 0;
            }
            return field_176869_i[p_176853_0_];
        }

        public static EnumOrientation func_176856_a(EnumFacing p_176856_0_, EnumFacing p_176856_1_) {
            switch (p_176856_0_) {
                case DOWN: {
                    switch (p_176856_1_.getAxis()) {
                        case X: {
                            return DOWN_X;
                        }
                        case Z: {
                            return DOWN_Z;
                        }
                    }
                    throw new IllegalArgumentException("Invalid entityFacing " + p_176856_1_ + " for facing " + p_176856_0_);
                }
                case UP: {
                    switch (p_176856_1_.getAxis()) {
                        case X: {
                            return UP_X;
                        }
                        case Z: {
                            return UP_Z;
                        }
                    }
                    throw new IllegalArgumentException("Invalid entityFacing " + p_176856_1_ + " for facing " + p_176856_0_);
                }
                case NORTH: {
                    return NORTH;
                }
                case SOUTH: {
                    return SOUTH;
                }
                case WEST: {
                    return WEST;
                }
                case EAST: {
                    return EAST;
                }
            }
            throw new IllegalArgumentException("Invalid facing: " + p_176856_0_);
        }

        @Override
        public String getName() {
            return this.field_176867_k;
        }
    }
}

