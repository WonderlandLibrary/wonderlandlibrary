package me.earth.earthhack.impl.util.render;

import me.earth.earthhack.api.util.Globals;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Interpolation implements Globals
{
    public static Vec3d interpolatedEyePos()
    {
        return mc.player.getPositionEyes(mc.getRenderPartialTicks());
    }

    public static Vec3d interpolatedEyeVec()
    {
        return mc.player.getLook(mc.getRenderPartialTicks());
    }

    public static AxisAlignedBB interpolatePos(BlockPos pos, float height)
    {
        return new AxisAlignedBB(
                pos.getX() - mc.getRenderManager().viewerPosX,
                pos.getY() - mc.getRenderManager().viewerPosY,
                pos.getZ() - mc.getRenderManager().viewerPosZ,
                pos.getX() - mc.getRenderManager().viewerPosX + 1,
                pos.getY() - mc.getRenderManager().viewerPosY + height,
                pos.getZ() - mc.getRenderManager().viewerPosZ + 1);
    }

    public static AxisAlignedBB interpolateAxis(AxisAlignedBB bb)
    {
        return new AxisAlignedBB(
                bb.minX - mc.getRenderManager().viewerPosX,
                bb.minY - mc.getRenderManager().viewerPosY,
                bb.minZ - mc.getRenderManager().viewerPosZ,
                bb.maxX - mc.getRenderManager().viewerPosX,
                bb.maxY - mc.getRenderManager().viewerPosY,
                bb.maxZ - mc.getRenderManager().viewerPosZ);
    }

}
