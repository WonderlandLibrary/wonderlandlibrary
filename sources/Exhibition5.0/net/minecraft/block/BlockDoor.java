// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockDoor extends Block
{
    public static final PropertyDirection FACING_PROP;
    public static final PropertyBool OPEN_PROP;
    public static final PropertyEnum HINGEPOSITION_PROP;
    public static final PropertyBool POWERED_PROP;
    public static final PropertyEnum HALF_PROP;
    private static final String __OBFID = "CL_00000230";
    
    protected BlockDoor(final Material p_i45402_1_) {
        super(p_i45402_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDoor.FACING_PROP, EnumFacing.NORTH).withProperty(BlockDoor.OPEN_PROP, false).withProperty(BlockDoor.HINGEPOSITION_PROP, EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED_PROP, false).withProperty(BlockDoor.HALF_PROP, EnumDoorHalf.LOWER));
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess blockAccess, final BlockPos pos) {
        return func_176516_g(func_176515_e(blockAccess, pos));
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        this.func_150011_b(func_176515_e(access, pos));
    }
    
    private void func_150011_b(final int p_150011_1_) {
        final float var2 = 0.1875f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
        final EnumFacing var3 = func_176511_f(p_150011_1_);
        final boolean var4 = func_176516_g(p_150011_1_);
        final boolean var5 = func_176513_j(p_150011_1_);
        if (var4) {
            if (var3 == EnumFacing.EAST) {
                if (!var5) {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, var2);
                }
                else {
                    this.setBlockBounds(0.0f, 0.0f, 1.0f - var2, 1.0f, 1.0f, 1.0f);
                }
            }
            else if (var3 == EnumFacing.SOUTH) {
                if (!var5) {
                    this.setBlockBounds(1.0f - var2, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                }
                else {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, var2, 1.0f, 1.0f);
                }
            }
            else if (var3 == EnumFacing.WEST) {
                if (!var5) {
                    this.setBlockBounds(0.0f, 0.0f, 1.0f - var2, 1.0f, 1.0f, 1.0f);
                }
                else {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, var2);
                }
            }
            else if (var3 == EnumFacing.NORTH) {
                if (!var5) {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, var2, 1.0f, 1.0f);
                }
                else {
                    this.setBlockBounds(1.0f - var2, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
        else if (var3 == EnumFacing.EAST) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, var2, 1.0f, 1.0f);
        }
        else if (var3 == EnumFacing.SOUTH) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, var2);
        }
        else if (var3 == EnumFacing.WEST) {
            this.setBlockBounds(1.0f - var2, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        else if (var3 == EnumFacing.NORTH) {
            this.setBlockBounds(0.0f, 0.0f, 1.0f - var2, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (this.blockMaterial == Material.iron) {
            return true;
        }
        final BlockPos var9 = (state.getValue(BlockDoor.HALF_PROP) == EnumDoorHalf.LOWER) ? pos : pos.offsetDown();
        final IBlockState var10 = pos.equals(var9) ? state : worldIn.getBlockState(var9);
        if (var10.getBlock() != this) {
            return false;
        }
        state = var10.cycleProperty(BlockDoor.OPEN_PROP);
        worldIn.setBlockState(var9, state, 2);
        worldIn.markBlockRangeForRenderUpdate(var9, pos);
        worldIn.playAuxSFXAtEntity(playerIn, ((boolean)state.getValue(BlockDoor.OPEN_PROP)) ? 1003 : 1006, pos, 0);
        return true;
    }
    
    public void func_176512_a(final World worldIn, final BlockPos p_176512_2_, final boolean p_176512_3_) {
        final IBlockState var4 = worldIn.getBlockState(p_176512_2_);
        if (var4.getBlock() == this) {
            final BlockPos var5 = (var4.getValue(BlockDoor.HALF_PROP) == EnumDoorHalf.LOWER) ? p_176512_2_ : p_176512_2_.offsetDown();
            final IBlockState var6 = (p_176512_2_ == var5) ? var4 : worldIn.getBlockState(var5);
            if (var6.getBlock() == this && (boolean)var6.getValue(BlockDoor.OPEN_PROP) != p_176512_3_) {
                worldIn.setBlockState(var5, var6.withProperty(BlockDoor.OPEN_PROP, p_176512_3_), 2);
                worldIn.markBlockRangeForRenderUpdate(var5, p_176512_2_);
                worldIn.playAuxSFXAtEntity(null, p_176512_3_ ? 1003 : 1006, p_176512_2_, 0);
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (state.getValue(BlockDoor.HALF_PROP) == EnumDoorHalf.UPPER) {
            final BlockPos var5 = pos.offsetDown();
            final IBlockState var6 = worldIn.getBlockState(var5);
            if (var6.getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
            else if (neighborBlock != this) {
                this.onNeighborBlockChange(worldIn, var5, var6, neighborBlock);
            }
        }
        else {
            boolean var7 = false;
            final BlockPos var8 = pos.offsetUp();
            final IBlockState var9 = worldIn.getBlockState(var8);
            if (var9.getBlock() != this) {
                worldIn.setBlockToAir(pos);
                var7 = true;
            }
            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
                worldIn.setBlockToAir(pos);
                var7 = true;
                if (var9.getBlock() == this) {
                    worldIn.setBlockToAir(var8);
                }
            }
            if (var7) {
                if (!worldIn.isRemote) {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            }
            else {
                final boolean var10 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(var8);
                if ((var10 || neighborBlock.canProvidePower()) && neighborBlock != this && var10 != (boolean)var9.getValue(BlockDoor.POWERED_PROP)) {
                    worldIn.setBlockState(var8, var9.withProperty(BlockDoor.POWERED_PROP, var10), 2);
                    if (var10 != (boolean)state.getValue(BlockDoor.OPEN_PROP)) {
                        worldIn.setBlockState(pos, state.withProperty(BlockDoor.OPEN_PROP, var10), 2);
                        worldIn.markBlockRangeForRenderUpdate(pos, pos);
                        worldIn.playAuxSFXAtEntity(null, var10 ? 1003 : 1006, pos, 0);
                    }
                }
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return (state.getValue(BlockDoor.HALF_PROP) == EnumDoorHalf.UPPER) ? null : this.func_176509_j();
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3 start, final Vec3 end) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return pos.getY() < 255 && (World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.offsetUp()));
    }
    
    @Override
    public int getMobilityFlag() {
        return 1;
    }
    
    public static int func_176515_e(final IBlockAccess p_176515_0_, final BlockPos p_176515_1_) {
        final IBlockState var2 = p_176515_0_.getBlockState(p_176515_1_);
        final int var3 = var2.getBlock().getMetaFromState(var2);
        final boolean var4 = func_176518_i(var3);
        final IBlockState var5 = p_176515_0_.getBlockState(p_176515_1_.offsetDown());
        final int var6 = var5.getBlock().getMetaFromState(var5);
        final int var7 = var4 ? var6 : var3;
        final IBlockState var8 = p_176515_0_.getBlockState(p_176515_1_.offsetUp());
        final int var9 = var8.getBlock().getMetaFromState(var8);
        final int var10 = var4 ? var3 : var9;
        final boolean var11 = (var10 & 0x1) != 0x0;
        final boolean var12 = (var10 & 0x2) != 0x0;
        return func_176510_b(var7) | (var4 ? 8 : 0) | (var11 ? 16 : 0) | (var12 ? 32 : 0);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return this.func_176509_j();
    }
    
    private Item func_176509_j() {
        return (this == Blocks.iron_door) ? Items.iron_door : ((this == Blocks.spruce_door) ? Items.spruce_door : ((this == Blocks.birch_door) ? Items.birch_door : ((this == Blocks.jungle_door) ? Items.jungle_door : ((this == Blocks.acacia_door) ? Items.acacia_door : ((this == Blocks.dark_oak_door) ? Items.dark_oak_door : Items.oak_door)))));
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn) {
        final BlockPos var5 = pos.offsetDown();
        if (playerIn.capabilities.isCreativeMode && state.getValue(BlockDoor.HALF_PROP) == EnumDoorHalf.UPPER && worldIn.getBlockState(var5).getBlock() == this) {
            worldIn.setBlockToAir(var5);
        }
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getValue(BlockDoor.HALF_PROP) == EnumDoorHalf.LOWER) {
            final IBlockState var4 = worldIn.getBlockState(pos.offsetUp());
            if (var4.getBlock() == this) {
                state = state.withProperty(BlockDoor.HINGEPOSITION_PROP, var4.getValue(BlockDoor.HINGEPOSITION_PROP)).withProperty(BlockDoor.POWERED_PROP, var4.getValue(BlockDoor.POWERED_PROP));
            }
        }
        else {
            final IBlockState var4 = worldIn.getBlockState(pos.offsetDown());
            if (var4.getBlock() == this) {
                state = state.withProperty(BlockDoor.FACING_PROP, var4.getValue(BlockDoor.FACING_PROP)).withProperty(BlockDoor.OPEN_PROP, var4.getValue(BlockDoor.OPEN_PROP));
            }
        }
        return state;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return ((meta & 0x8) > 0) ? this.getDefaultState().withProperty(BlockDoor.HALF_PROP, EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGEPOSITION_PROP, ((meta & 0x1) > 0) ? EnumHingePosition.RIGHT : EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED_PROP, (meta & 0x2) > 0) : this.getDefaultState().withProperty(BlockDoor.HALF_PROP, EnumDoorHalf.LOWER).withProperty(BlockDoor.FACING_PROP, EnumFacing.getHorizontal(meta & 0x3).rotateYCCW()).withProperty(BlockDoor.OPEN_PROP, (meta & 0x4) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3;
        if (state.getValue(BlockDoor.HALF_PROP) == EnumDoorHalf.UPPER) {
            var3 = (var2 | 0x8);
            if (state.getValue(BlockDoor.HINGEPOSITION_PROP) == EnumHingePosition.RIGHT) {
                var3 |= 0x1;
            }
            if (state.getValue(BlockDoor.POWERED_PROP)) {
                var3 |= 0x2;
            }
        }
        else {
            var3 = (var2 | ((EnumFacing)state.getValue(BlockDoor.FACING_PROP)).rotateY().getHorizontalIndex());
            if (state.getValue(BlockDoor.OPEN_PROP)) {
                var3 |= 0x4;
            }
        }
        return var3;
    }
    
    protected static int func_176510_b(final int p_176510_0_) {
        return p_176510_0_ & 0x7;
    }
    
    public static boolean func_176514_f(final IBlockAccess p_176514_0_, final BlockPos p_176514_1_) {
        return func_176516_g(func_176515_e(p_176514_0_, p_176514_1_));
    }
    
    public static EnumFacing func_176517_h(final IBlockAccess p_176517_0_, final BlockPos p_176517_1_) {
        return func_176511_f(func_176515_e(p_176517_0_, p_176517_1_));
    }
    
    public static EnumFacing func_176511_f(final int p_176511_0_) {
        return EnumFacing.getHorizontal(p_176511_0_ & 0x3).rotateYCCW();
    }
    
    protected static boolean func_176516_g(final int p_176516_0_) {
        return (p_176516_0_ & 0x4) != 0x0;
    }
    
    protected static boolean func_176518_i(final int p_176518_0_) {
        return (p_176518_0_ & 0x8) != 0x0;
    }
    
    protected static boolean func_176513_j(final int p_176513_0_) {
        return (p_176513_0_ & 0x10) != 0x0;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDoor.HALF_PROP, BlockDoor.FACING_PROP, BlockDoor.OPEN_PROP, BlockDoor.HINGEPOSITION_PROP, BlockDoor.POWERED_PROP });
    }
    
    static {
        FACING_PROP = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
        OPEN_PROP = PropertyBool.create("open");
        HINGEPOSITION_PROP = PropertyEnum.create("hinge", EnumHingePosition.class);
        POWERED_PROP = PropertyBool.create("powered");
        HALF_PROP = PropertyEnum.create("half", EnumDoorHalf.class);
    }
    
    public enum EnumDoorHalf implements IStringSerializable
    {
        UPPER("UPPER", 0), 
        LOWER("LOWER", 1);
        
        private static final EnumDoorHalf[] $VALUES;
        private static final String __OBFID = "CL_00002124";
        
        private EnumDoorHalf(final String p_i45726_1_, final int p_i45726_2_) {
        }
        
        @Override
        public String toString() {
            return this.getName();
        }
        
        @Override
        public String getName() {
            return (this == EnumDoorHalf.UPPER) ? "upper" : "lower";
        }
        
        static {
            $VALUES = new EnumDoorHalf[] { EnumDoorHalf.UPPER, EnumDoorHalf.LOWER };
        }
    }
    
    public enum EnumHingePosition implements IStringSerializable
    {
        LEFT("LEFT", 0), 
        RIGHT("RIGHT", 1);
        
        private static final EnumHingePosition[] $VALUES;
        private static final String __OBFID = "CL_00002123";
        
        private EnumHingePosition(final String p_i45725_1_, final int p_i45725_2_) {
        }
        
        @Override
        public String toString() {
            return this.getName();
        }
        
        @Override
        public String getName() {
            return (this == EnumHingePosition.LEFT) ? "left" : "right";
        }
        
        static {
            $VALUES = new EnumHingePosition[] { EnumHingePosition.LEFT, EnumHingePosition.RIGHT };
        }
    }
}
