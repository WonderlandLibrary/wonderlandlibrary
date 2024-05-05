package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class BlockAnvil
extends BlockFalling {
    public static final PropertyDirection FACING = PropertyDirection.create((String)"facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger DAMAGE = PropertyInteger.create((String)"damage", (int)0, (int)2);

    protected BlockAnvil() {
        super(Material.anvil);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)DAMAGE, (Comparable)Integer.valueOf((int)0)));
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)DAMAGE, (Comparable)Integer.valueOf((int)(meta >> 2)));
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.displayGui((IInteractionObject)new Anvil(worldIn, pos));
        }
        return true;
    }

    public int damageDropped(IBlockState state) {
        return (Integer)state.getValue((IProperty)DAMAGE);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING);
        if (enumfacing.getAxis() == EnumFacing.Axis.X) {
            this.setBlockBounds(0.0f, 0.0f, 0.125f, 1.0f, 1.0f, 0.875f);
        } else {
            this.setBlockBounds(0.125f, 0.0f, 0.0f, 0.875f, 1.0f, 1.0f);
        }
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add((Object)new ItemStack(itemIn, 1, 0));
        list.add((Object)new ItemStack(itemIn, 1, 1));
        list.add((Object)new ItemStack(itemIn, 1, 2));
    }

    protected void onStartFalling(EntityFallingBlock fallingEntity) {
        fallingEntity.setHurtEntities(true);
    }

    public void onEndFalling(World worldIn, BlockPos pos) {
        worldIn.playAuxSFX(1022, pos, 0);
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    public IBlockState getStateForEntityRender(IBlockState state) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.SOUTH);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal((int)(meta & 3))).withProperty((IProperty)DAMAGE, (Comparable)Integer.valueOf((int)((meta & 0xF) >> 2)));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
        return i |= (Integer)state.getValue((IProperty)DAMAGE) << 2;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING, DAMAGE});
    }
}
