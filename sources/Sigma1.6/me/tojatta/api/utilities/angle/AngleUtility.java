/*
 * Decompiled with CFR 0_122.
 */
package me.tojatta.api.utilities.angle;

import java.util.Random;
import me.tojatta.api.utilities.angle.Angle;
import me.tojatta.api.utilities.vector.Vector;
import me.tojatta.api.utilities.vector.impl.Vector3;

public class AngleUtility {
    private float minYawSmoothing;
    private float maxYawSmoothing;
    private float minPitchSmoothing;
    private float maxPitchSmoothing;
    private Vector3<Float> delta;
    private Angle smoothedAngle;
    private final Random random;

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
        float height = 1.5f;
        this.delta.setX(Float.valueOf(destination.getX().floatValue() - source.getX().floatValue())).setY(Float.valueOf(destination.getY().floatValue() + height - (source.getY().floatValue() + height))).setZ(Float.valueOf(destination.getZ().floatValue() - source.getZ().floatValue()));
        double hypotenuse = Math.hypot(this.delta.getX().doubleValue(), this.delta.getZ().doubleValue());
        float yawAtan = (float)Math.atan2(this.delta.getZ().floatValue(), this.delta.getX().floatValue());
        float pitchAtan = (float)Math.atan2(this.delta.getY().floatValue(), hypotenuse);
        float deg = 57.29578f;
        float yaw = yawAtan * deg - 90.0f;
        float pitch = - pitchAtan * deg;
        return angles.setYaw(Float.valueOf(yaw)).setPitch(Float.valueOf(pitch)).constrantAngle();
    }

    public Angle smoothAngle(Angle destination, Angle source) {
        return this.smoothedAngle.setYaw(Float.valueOf(source.getYaw().floatValue() - destination.getYaw().floatValue())).setPitch(Float.valueOf(source.getPitch().floatValue() - destination.getPitch().floatValue())).constrantAngle().setYaw(Float.valueOf(source.getYaw().floatValue() - this.smoothedAngle.getYaw().floatValue() / 100.0f * this.randomFloat(this.minYawSmoothing, this.maxYawSmoothing))).setPitch(Float.valueOf(source.getPitch().floatValue() - this.smoothedAngle.getPitch().floatValue() / 100.0f * this.randomFloat(this.minPitchSmoothing, this.maxPitchSmoothing))).constrantAngle();
    }
}

