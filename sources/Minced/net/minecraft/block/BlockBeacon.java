// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.WorldServer;
import net.minecraft.init.Blocks;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.stats.StatList;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockBeacon extends BlockContainer
{
    public BlockBeacon() {
        super(Material.GLASS, MapColor.DIAMOND);
        this.setHardness(3.0f);
        this.setCreativeTab(CreativeTabs.MISC);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityBeacon();
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBeacon) {
            playerIn.displayGUIChest((IInventory)tileentity);
            playerIn.addStat(StatList.BEACON_INTERACTION);
        }
        return true;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityBeacon) {
                ((TileEntityBeacon)tileentity).setName(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBeacon) {
            ((TileEntityBeacon)tileentity).updateBeacon();
            worldIn.addBlockEvent(pos, this, 1, 0);
        }
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    public static void updateColorAsync(final World worldIn, final BlockPos glassPos) {
        HttpUtil.DOWNLOADER_EXECUTOR.submit((Runnable)new Runnable() {
            @Override
            public void run() {
                final Chunk chunk = worldIn.getChunk(glassPos);
                for (int i = glassPos.getY() - 1; i >= 0; --i) {
                    final BlockPos blockpos = new BlockPos(glassPos.getX(), i, glassPos.getZ());
                    if (!chunk.canSeeSky(blockpos)) {
                        break;
                    }
                    final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    if (iblockstate.getBlock() == Blocks.BEACON) {
                        ((WorldServer)worldIn).addScheduledTask(new Runnable() {
                            @Override
                            public void run() {
                                final TileEntity tileentity = worldIn.getTileEntity(blockpos);
                                if (tileentity instanceof TileEntityBeacon) {
                                    ((TileEntityBeacon)tileentity).updateBeacon();
                                    worldIn.addBlockEvent(blockpos, Blocks.BEACON, 1, 0);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
