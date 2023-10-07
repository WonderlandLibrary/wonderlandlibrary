package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.IdentityHashMap;
import java.util.Map;

public class BlockAir extends Block
{
    private static final Map mapOriginalOpacity = new IdentityHashMap();
    public static boolean collision = false;
    public static int collisionMaxY = -1;

    protected BlockAir()
    {
        super(Material.air);
    }

    /**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public int getRenderType()
    {
        return -1;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        final Minecraft mc = Minecraft.getMinecraft();
        if (collision && pos.getY() < collisionMaxY && mc.thePlayer != null
                && mc.thePlayer.getDistance(pos.getX() + 0.5, mc.thePlayer.posY, pos.getZ() + 0.5) < 1) {
            return new AxisAlignedBB((double)pos.getX() + this.getBlockBoundsMinX(),
                    (double)pos.getY() + this.getBlockBoundsMinY(),
                    (double)pos.getZ() + this.getBlockBoundsMinZ(),
                    (double)pos.getX() + this.getBlockBoundsMaxX(),
                    (double)pos.getY() + this.getBlockBoundsMaxY(),
                    (double)pos.getZ() + this.getBlockBoundsMaxZ());
        }
        return null;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
    {
        return false;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    public boolean isReplaceable(World worldIn, BlockPos pos)
    {
        return true;
    }

    public static void setLightOpacity(Block p_setLightOpacity_0_, int p_setLightOpacity_1_)
    {
        if (!mapOriginalOpacity.containsKey(p_setLightOpacity_0_))
        {
            mapOriginalOpacity.put(p_setLightOpacity_0_, Integer.valueOf(p_setLightOpacity_0_.lightOpacity));
        }

        p_setLightOpacity_0_.lightOpacity = p_setLightOpacity_1_;
    }

    public static void restoreLightOpacity(Block p_restoreLightOpacity_0_)
    {
        if (mapOriginalOpacity.containsKey(p_restoreLightOpacity_0_))
        {
            int i = ((Integer)mapOriginalOpacity.get(p_restoreLightOpacity_0_)).intValue();
            setLightOpacity(p_restoreLightOpacity_0_, i);
        }
    }
}
