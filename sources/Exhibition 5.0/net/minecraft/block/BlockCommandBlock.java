// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockCommandBlock extends BlockContainer
{
    public static final PropertyBool TRIGGERED_PROP;
    private static final String __OBFID = "CL_00000219";
    
    public BlockCommandBlock() {
        super(Material.iron);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCommandBlock.TRIGGERED_PROP, false));
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityCommandBlock();
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            final boolean var5 = worldIn.isBlockPowered(pos);
            final boolean var6 = (boolean)state.getValue(BlockCommandBlock.TRIGGERED_PROP);
            if (var5 && !var6) {
                worldIn.setBlockState(pos, state.withProperty(BlockCommandBlock.TRIGGERED_PROP, true), 4);
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            }
            else if (!var5 && var6) {
                worldIn.setBlockState(pos, state.withProperty(BlockCommandBlock.TRIGGERED_PROP, false), 4);
            }
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final TileEntity var5 = worldIn.getTileEntity(pos);
        if (var5 instanceof TileEntityCommandBlock) {
            ((TileEntityCommandBlock)var5).getCommandBlockLogic().trigger(worldIn);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 1;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final TileEntity var9 = worldIn.getTileEntity(pos);
        return var9 instanceof TileEntityCommandBlock && ((TileEntityCommandBlock)var9).getCommandBlockLogic().func_175574_a(playerIn);
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        final TileEntity var3 = worldIn.getTileEntity(pos);
        return (var3 instanceof TileEntityCommandBlock) ? ((TileEntityCommandBlock)var3).getCommandBlockLogic().getSuccessCount() : 0;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        final TileEntity var6 = worldIn.getTileEntity(pos);
        if (var6 instanceof TileEntityCommandBlock) {
            final CommandBlockLogic var7 = ((TileEntityCommandBlock)var6).getCommandBlockLogic();
            if (stack.hasDisplayName()) {
                var7.func_145754_b(stack.getDisplayName());
            }
            if (!worldIn.isRemote) {
                var7.func_175573_a(worldIn.getGameRules().getGameRuleBooleanValue("sendCommandFeedback"));
            }
        }
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockCommandBlock.TRIGGERED_PROP, (meta & 0x1) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int var2 = 0;
        if (state.getValue(BlockCommandBlock.TRIGGERED_PROP)) {
            var2 |= 0x1;
        }
        return var2;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCommandBlock.TRIGGERED_PROP });
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockCommandBlock.TRIGGERED_PROP, false);
    }
    
    static {
        TRIGGERED_PROP = PropertyBool.create("triggered");
    }
}
