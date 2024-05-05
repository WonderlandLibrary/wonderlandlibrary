package slavikcodd3r.rainbow.utils;

import java.math.RoundingMode;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.math.BigDecimal;

public final class MathUtils
{
    public static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int getMiddle(final int i, final int i1) {
        return (i + i1) / 2;
    }
    
    public static double getMiddle(final double i, final double i1) {
        return (i + i1) / 2.0;
    }
    
    public static float getAngleDifference(final float direction, final float rotationYaw) {
        final float phi = Math.abs(rotationYaw - direction) % 360.0f;
        final float distance = (phi > 180.0f) ? (360.0f - phi) : phi;
        return distance;
    }
    
    public static float[] getRotations(final Entity entity) {
        final Vec3 localPos = Wrapper.getPlayer().getPositionVector();
        final Vec3 entityPos = entity.getPositionVector();
        final Vec3 diffPos = entityPos.subtract(localPos.xCoord, 0.0, localPos.zCoord);
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + (elb.getEyeHeight() - 0.4) - (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
        }
        else {
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
        }
        final double dist = MathHelper.sqrt_double(Math.pow(diffPos.xCoord, 2.0) + Math.pow(diffPos.zCoord, 2.0));
        final float yaw = (float)Math.toDegrees(Math.atan2(diffPos.xCoord, diffPos.zCoord)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, dist)));
        return new float[] { yaw, pitch };
    }
}
