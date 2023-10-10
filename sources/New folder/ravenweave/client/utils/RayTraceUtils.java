package ravenweave.client.utils;

import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import static ravenweave.client.main.Raven.mc;

public class RayTraceUtils {
    // This method was protected in the Entity class, so I pasted it and made it readable
    static final float PI_OVER_180 = (float) Math.PI / 180F;
    public static float degreesToRadians(float degrees) {
        return degrees * PI_OVER_180;
    }
    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float var3 = MathHelper.cos(-degreesToRadians(yaw));
        float var4 = MathHelper.sin(-degreesToRadians(yaw) );
        float var5 = -MathHelper.cos(-degreesToRadians(pitch));
        float var6 = MathHelper.sin(-degreesToRadians(pitch));
        return new Vec3(var4 * var5, var6, var3 * var5);
    }

    // Modified from Entity#rayTrace to use custom angles
    public static MovingObjectPosition customRayTrace(double reach, float pitch, float yaw) {
        Vec3 origin = mc.getRenderViewEntity().getPositionEyes(1.0F);
        Vec3 angleVec = getVectorForRotation(pitch, yaw);
        Vec3 end = origin.addVector(angleVec.xCoord * reach, angleVec.yCoord * reach, angleVec.zCoord * reach);
        return mc.theWorld.rayTraceBlocks(origin, end, false, false, true);
    }
}
