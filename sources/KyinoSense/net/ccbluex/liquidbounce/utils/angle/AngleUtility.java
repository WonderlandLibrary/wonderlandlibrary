/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.angle;

import java.util.Random;
import net.ccbluex.liquidbounce.utils.angle.Angle;
import net.ccbluex.liquidbounce.utils.angle.Mafs;
import net.ccbluex.liquidbounce.utils.vector.impl.Vector3;

public class AngleUtility {
    private float minYawSmoothing;
    private float maxYawSmoothing;
    private float minPitchSmoothing;
    private float maxPitchSmoothing;
    private Vector3<Float> delta;
    private Angle smoothedAngle;
    private final Random random;
    private float height = Mafs.getRandomInRange(1.1f, 1.8f);

    public AngleUtility(float minYawSmoothing, float maxYawSmoothing, float minPitchSmoothing, float maxPitchSmoothing) {
        this.minYawSmoothing = minYawSmoothing;
        this.maxYawSmoothing = maxYawSmoothing;
        this.minPitchSmoothing = minPitchSmoothing;
        this.maxPitchSmoothing = maxPitchSmoothing;
        this.random = new Random();
        this.delta = new Vector3<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(0.0f));
        this.smoothedAngle = new Angle(Float.valueOf(0.0f), Float.valueOf(0.0f));
    }

    public float randomFloat(float min, float max) {
        return min + this.random.nextFloat() * (max - min);
    }

    public Angle calculateAngle(Vector3<Double> destination, Vector3<Double> source) {
        Angle angles = new Angle(Float.valueOf(0.0f), Float.valueOf(0.0f));
        this.delta.setX(Float.valueOf(((Number)destination.getX()).floatValue() - ((Number)source.getX()).floatValue())).setY(Float.valueOf(((Number)destination.getY()).floatValue() + this.height - (((Number)source.getY()).floatValue() + this.height))).setZ(Float.valueOf(((Number)destination.getZ()).floatValue() - ((Number)source.getZ()).floatValue()));
        double hypotenuse = Math.hypot(((Number)this.delta.getX()).doubleValue(), ((Number)this.delta.getZ()).doubleValue());
        float yawAtan = (float)Math.atan2(((Number)this.delta.getZ()).floatValue(), ((Number)this.delta.getX()).floatValue());
        float pitchAtan = (float)Math.atan2(((Number)this.delta.getY()).floatValue(), hypotenuse);
        float deg = 57.29578f;
        float yaw = yawAtan * deg - 90.0f;
        float pitch = -(pitchAtan * deg);
        return angles.setYaw(Float.valueOf(yaw)).setPitch(Float.valueOf(pitch)).constrantAngle();
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Angle smoothAngle(Angle destination, Angle source, float pitch, float yaw) {
        return this.smoothedAngle.setYaw(Float.valueOf(source.getYaw().floatValue() - destination.getYaw().floatValue() - (Math.abs(source.getYaw().floatValue() - destination.getYaw().floatValue()) > 5.0f ? Math.abs(source.getYaw().floatValue() - destination.getYaw().floatValue()) / Math.abs(source.getYaw().floatValue() - destination.getYaw().floatValue()) * 2.0f / 2.0f : 0.0f))).setPitch(Float.valueOf(source.getPitch().floatValue() - destination.getPitch().floatValue())).constrantAngle().setYaw(Float.valueOf(source.getYaw().floatValue() - this.smoothedAngle.getYaw().floatValue() / yaw * this.randomFloat(this.minYawSmoothing, this.maxYawSmoothing))).constrantAngle().setPitch(Float.valueOf(source.getPitch().floatValue() - this.smoothedAngle.getPitch().floatValue() / pitch * this.randomFloat(this.minPitchSmoothing, this.maxPitchSmoothing))).constrantAngle();
    }
}

