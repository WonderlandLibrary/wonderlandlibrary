package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockStandingSign
extends BlockSign {
    public static final PropertyInteger ROTATION = PropertyInteger.create((String)"rotation", (int)0, (int)15);

    public BlockStandingSign() {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)ROTATION, (Comparable)Integer.valueOf((int)0)));
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)ROTATION, (Comparable)Integer.valueOf((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue((IProperty)ROTATION);
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{ROTATION});
    }
}
