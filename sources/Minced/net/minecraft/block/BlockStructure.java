// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumBlockRenderType;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.block.properties.PropertyEnum;

public class BlockStructure extends BlockContainer
{
    public static final PropertyEnum<TileEntityStructure.Mode> MODE;
    
    public BlockStructure() {
        super(Material.IRON, MapColor.SILVER);
        this.setDefaultState(this.blockState.getBaseState());
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityStructure();
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityStructure && ((TileEntityStructure)tileentity).usedBy(playerIn);
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        if (!worldIn.isRemote) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityStructure) {
                final TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity;
                tileentitystructure.createdBy(placer);
            }
        }
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockStructure.MODE, TileEntityStructure.Mode.DATA);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockStructure.MODE, TileEntityStructure.Mode.getById(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockStructure.MODE).getModeId();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockStructure.MODE });
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityStructure) {
                final TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity;
                final boolean flag = worldIn.isBlockPowered(pos);
                final boolean flag2 = tileentitystructure.isPowered();
                if (flag && !flag2) {
                    tileentitystructure.setPowered(true);
                    this.trigger(tileentitystructure);
                }
                else if (!flag && flag2) {
                    tileentitystructure.setPowered(false);
                }
            }
        }
    }
    
    private void trigger(final TileEntityStructure p_189874_1_) {
        switch (p_189874_1_.getMode()) {
            case SAVE: {
                p_189874_1_.save(false);
                break;
            }
            case LOAD: {
                p_189874_1_.load(false);
                break;
            }
            case CORNER: {
                p_189874_1_.unloadStructure();
                break;
            }
        }
    }
    
    static {
        MODE = PropertyEnum.create("mode", TileEntityStructure.Mode.class);
    }
}
