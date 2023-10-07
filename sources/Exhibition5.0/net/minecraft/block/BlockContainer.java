// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public abstract class BlockContainer extends Block implements ITileEntityProvider
{
    private static final String __OBFID = "CL_00000193";
    
    protected BlockContainer(final Material materialIn) {
        super(materialIn);
        this.isBlockContainer = true;
    }
    
    @Override
    public int getRenderType() {
        return -1;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }
    
    @Override
    public boolean onBlockEventReceived(final World worldIn, final BlockPos pos, final IBlockState state, final int eventID, final int eventParam) {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        final TileEntity var6 = worldIn.getTileEntity(pos);
        return var6 != null && var6.receiveClientEvent(eventID, eventParam);
    }
}
