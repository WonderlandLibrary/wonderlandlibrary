package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockWall
extends Block {
    public static final PropertyBool UP = PropertyBool.create((String)"up");
    public static final PropertyBool NORTH = PropertyBool.create((String)"north");
    public static final PropertyBool EAST = PropertyBool.create((String)"east");
    public static final PropertyBool SOUTH = PropertyBool.create((String)"south");
    public static final PropertyBool WEST = PropertyBool.create((String)"west");
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create((String)"variant", EnumType.class);

    public BlockWall(Block modelBlock) {
        super(modelBlock.blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)UP, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)NORTH, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)EAST, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)SOUTH, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)WEST, (Comparable)Boolean.valueOf((boolean)false)).withProperty(VARIANT, (Comparable)EnumType.NORMAL));
        this.setHardness(modelBlock.blockHardness);
        this.setResistance(modelBlock.blockResistance / 3.0f);
        this.setStepSound(modelBlock.stepSound);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal((String)(this.getUnlocalizedName() + "." + EnumType.NORMAL.getUnlocalizedName() + ".name"));
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        boolean flag = this.canConnectTo(worldIn, pos.north());
        boolean flag1 = this.canConnectTo(worldIn, pos.south());
        boolean flag2 = this.canConnectTo(worldIn, pos.west());
        boolean flag3 = this.canConnectTo(worldIn, pos.east());
        float f = 0.25f;
        float f1 = 0.75f;
        float f2 = 0.25f;
        float f3 = 0.75f;
        float f4 = 1.0f;
        if (flag) {
            f2 = 0.0f;
        }
        if (flag1) {
            f3 = 1.0f;
        }
        if (flag2) {
            f = 0.0f;
        }
        if (flag3) {
            f1 = 1.0f;
        }
        if (flag && flag1 && !flag2 && !flag3) {
            f4 = 0.8125f;
            f = 0.3125f;
            f1 = 0.6875f;
        } else if (!flag && !flag1 && flag2 && flag3) {
            f4 = 0.8125f;
            f2 = 0.3125f;
            f3 = 0.6875f;
        }
        this.setBlockBounds(f, 0.0f, f2, f1, f4, f3);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        this.setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
        this.maxY = 1.5;
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();
        return block == Blocks.barrier ? false : (block != this && !(block instanceof BlockFenceGate) ? (block.blockMaterial.isOpaque() && block.isFullCube() ? block.blockMaterial != Material.gourd : false) : true);
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (EnumType blockwall$enumtype : EnumType.values()) {
            list.add((Object)new ItemStack(itemIn, 1, blockwall$enumtype.getMetadata()));
        }
    }

    public int damageDropped(IBlockState state) {
        return ((EnumType)state.getValue(VARIANT)).getMetadata();
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? super.shouldSideBeRendered(worldIn, pos, side) : true;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, (Comparable)EnumType.byMetadata((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumType)state.getValue(VARIANT)).getMetadata();
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty((IProperty)UP, (Comparable)Boolean.valueOf((!worldIn.isAirBlock(pos.up()) ? 1 : 0) != 0)).withProperty((IProperty)NORTH, (Comparable)Boolean.valueOf((boolean)this.canConnectTo(worldIn, pos.north()))).withProperty((IProperty)EAST, (Comparable)Boolean.valueOf((boolean)this.canConnectTo(worldIn, pos.east()))).withProperty((IProperty)SOUTH, (Comparable)Boolean.valueOf((boolean)this.canConnectTo(worldIn, pos.south()))).withProperty((IProperty)WEST, (Comparable)Boolean.valueOf((boolean)this.canConnectTo(worldIn, pos.west())));
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{UP, NORTH, EAST, WEST, SOUTH, VARIANT});
    }
}
