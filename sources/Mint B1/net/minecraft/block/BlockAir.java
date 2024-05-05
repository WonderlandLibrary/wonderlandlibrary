package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockAir extends Block
{
    private static final String __OBFID = "CL_00000190";

    protected BlockAir()
    {
        super(Material.air);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return -1;
    }

    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean canCollideCheck(final IBlockState state, final boolean p_176209_2_)
    {
        return false;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *  
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {}
}
