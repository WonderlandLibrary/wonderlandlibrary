// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.math;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import javax.vecmath.Vector2f;
import ru.tuskevich.util.Utility;

public class RotationUtility implements Utility
{
    public static Vector2f getDeltaForCoord(final Vector2f rot, final Vec3d point) {
        Minecraft.getMinecraft();
        final EntityPlayerSP client = Minecraft.player;
        final double x = point.x - client.posX;
        final double y = point.y - client.getPositionEyes(1.0f).y;
        final double z = point.z - client.posZ;
        final double dst = Math.sqrt(Math.pow(x, 2.0) + Math.pow(z, 2.0));
        final float yawToTarget = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90.0);
        final float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(y, dst)));
        final float yawDelta = MathHelper.wrapDegrees(yawToTarget - rot.x);
        final float pitchDelta = pitchToTarget - rot.y;
        return new Vector2f(yawDelta, pitchDelta);
    }
    
    public static org.lwjgl.util.vector.Vector2f getRotationForCoord(final Vec3d point) {
        Minecraft.getMinecraft();
        final EntityPlayerSP client = Minecraft.player;
        final double x = point.x - client.posX;
        final double y = point.y - client.getPositionEyes(1.0f).y;
        final double z = point.z - client.posZ;
        final double dst = Math.sqrt(Math.pow(x, 2.0) + Math.pow(z, 2.0));
        final float yawToTarget = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90.0);
        final float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(y, dst)));
        return new org.lwjgl.util.vector.Vector2f(yawToTarget, pitchToTarget);
    }
}
