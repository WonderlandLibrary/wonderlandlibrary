package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.World;

public class BlockBeacon
extends BlockContainer {
    public BlockBeacon() {
        super(Material.glass, MapColor.diamondColor);
        this.setHardness(3.0f);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBeacon();
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBeacon) {
            playerIn.displayGUIChest((IInventory)((TileEntityBeacon)tileentity));
            playerIn.triggerAchievement(StatList.field_181730_N);
        }
        return true;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public int getRenderType() {
        return 3;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileentity;
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasDisplayName() && (tileentity = worldIn.getTileEntity(pos)) instanceof TileEntityBeacon) {
            ((TileEntityBeacon)tileentity).setName(stack.getDisplayName());
        }
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBeacon) {
            ((TileEntityBeacon)tileentity).updateBeacon();
            worldIn.addBlockEvent(pos, (Block)this, 1, 0);
        }
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public static void updateColorAsync(World worldIn, BlockPos glassPos) {
        HttpUtil.field_180193_a.submit((Runnable)new /* Unavailable Anonymous Inner Class!! */);
    }
}
