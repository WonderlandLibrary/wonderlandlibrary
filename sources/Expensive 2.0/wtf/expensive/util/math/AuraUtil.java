package wtf.expensive.util.math;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import wtf.expensive.modules.impl.combat.AuraFunction;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.IMinecraft;

import java.lang.reflect.Array;
import java.util.Arrays;

import static net.minecraft.util.math.MathHelper.clamp;
import static net.minecraft.world.Explosion.getBlockDensity;

public class AuraUtil implements IMinecraft {
    public static boolean isNoVisible;



    private static Vector3d calculateVector(LivingEntity target, double distance) {
        double yOffset = MathHelper.clamp(mc.player.getPosYEye() - target.getPosYEye(), 0.2, target.getEyeHeight());

        return target.getPositionVec().add(0,yOffset,0);
    }
    public static Vector3d getBestVec3d(final Vector3d pos, final AxisAlignedBB axisAlignedBB) {
        double lastDistance = Double.MAX_VALUE;
        Vector3d bestVec = null;

        final double xWidth = axisAlignedBB.maxX - axisAlignedBB.minX;
        final double zWidth = axisAlignedBB.maxZ - axisAlignedBB.minZ;
        final double height = axisAlignedBB.maxY - axisAlignedBB.minY;

        for (float x = 0F; x < 1F; x += 0.1F) {
            for (float y = 0F; y < 1F; y += 0.1F) {
                for (float z = 0F; z < 1F; z += 0.1F) {

                    final Vector3d hitVec = new Vector3d(
                            axisAlignedBB.minX + xWidth * x,
                            axisAlignedBB.minY + height * y,
                            axisAlignedBB.minZ + zWidth * z
                    );

                    final double distance = pos.distanceTo(hitVec);

                    if (isHitBoxNotVisible(hitVec) && distance < lastDistance) {
                        bestVec = hitVec;
                        lastDistance = distance;
                    }
                }
            }
        }

        return bestVec;
    }

    public static boolean isHitBoxNotVisible(final Vector3d vec3d) {
        final RayTraceContext rayTraceContext = new RayTraceContext(
                mc.player.getEyePosition(1F),
                vec3d,
                RayTraceContext.BlockMode.COLLIDER,
                RayTraceContext.FluidMode.NONE,
                mc.player
        );
        final BlockRayTraceResult blockHitResult = mc.world.rayTraceBlocks(rayTraceContext);
        return blockHitResult.getType() == RayTraceResult.Type.MISS;
    }
    public static Vector3d getVector(LivingEntity target) {

        double wHalf = target.getWidth() / 2;

        double yExpand = clamp(target.getPosYEye() - target.getPosY(), 0, target.getHeight());

        double xExpand = clamp(mc.player.getPosX() - target.getPosX(), -wHalf, wHalf);
        double zExpand = clamp(mc.player.getPosZ() - target.getPosZ(), -wHalf, wHalf);

        return new Vector3d(
                target.getPosX() - mc.player.getPosX() + xExpand,
                target.getPosY() - mc.player.getPosYEye() + yExpand,
                target.getPosZ() - mc.player.getPosZ() + zExpand
        );
    }


}
