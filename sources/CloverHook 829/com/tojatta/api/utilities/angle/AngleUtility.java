package com.tojatta.api.utilities.angle;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import java.util.Random;

import com.tojatta.api.utilities.vector.impl.Vector3;

import club.cloverhook.utils.Mafs;

/**
 * Created by Tojatta on 12/17/2016.
 * 
 * https://gitlab.com/Tojatta
 */
public class AngleUtility {

    private float minYawSmoothing, maxYawSmoothing, minPitchSmoothing, maxPitchSmoothing;
    private Vector3<Float> delta;
    private Angle smoothedAngle;
    private final Random random;
    private float height = Mafs.getRandomInRange(1.1F, 1.8F);

    public AngleUtility(float minYawSmoothing, float maxYawSmoothing, float minPitchSmoothing, float maxPitchSmoothing) {
        this.minYawSmoothing = minYawSmoothing;
        this.maxYawSmoothing = maxYawSmoothing;
        this.minPitchSmoothing = minPitchSmoothing;
        this.maxPitchSmoothing = maxPitchSmoothing;
        random = new Random();
        delta = new Vector3<>(0F, 0F, 0F);
        smoothedAngle = new Angle(0F, 0F);
    }

    public float randomFloat(float min, float max) {
        return min + (random.nextFloat() * (max - min));
    }

    public Angle calculateAngle(Vector3<Double> destination, Vector3<Double> source) {

        Angle angles = new Angle(0F, 0F);

        delta
                .setX(destination.getX().floatValue() - source.getX().floatValue())
                .setY((destination.getY().floatValue() + height) - (source.getY().floatValue() + height))
                .setZ(destination.getZ().floatValue() - source.getZ().floatValue());

        double hypotenuse = Math.hypot(delta.getX().doubleValue(), delta.getZ().doubleValue());

        float yawAtan = ((float) Math.atan2(delta.getZ().floatValue(), delta.getX().floatValue()));
        float pitchAtan = ((float) Math.atan2(delta.getY().floatValue(), hypotenuse));

        float deg = ((float) (180 / Math.PI));

        float yaw = ((yawAtan * deg) - 90F);
        float pitch = -(pitchAtan * deg);

        return angles.setYaw(yaw).setPitch(pitch).constrantAngle();
    }
    public Angle calculateAngle2(Vector3<Integer> destination, Vector3<Double> source) {

        Angle angles = new Angle(0F, 0F);

        delta
                .setX(destination.getX().floatValue() - source.getX().floatValue())
                .setY((destination.getY().floatValue() + height) - (source.getY().floatValue() + height))
                .setZ(destination.getZ().floatValue() - source.getZ().floatValue());

        double hypotenuse = Math.hypot(delta.getX().doubleValue(), delta.getZ().doubleValue());

        float yawAtan = ((float) Math.atan2(delta.getZ().floatValue(), delta.getX().floatValue()));
        float pitchAtan = ((float) Math.atan2(delta.getY().floatValue(), hypotenuse));

        float deg = ((float) (180 / Math.PI));

        float yaw = ((yawAtan * deg) - 90);
        float pitch = -(pitchAtan * deg);

        return angles.setYaw(yaw).setPitch(pitch).constrantAngle();
    }
    public void setHeight(float height) {
        this.height = height;
    }

    public Angle smoothAngle(Angle destination, Angle source, float pitch, float yaw) {
        return smoothedAngle
                .setYaw(source.getYaw() - destination.getYaw() - (Math.abs(source.getYaw() - destination.getYaw()) > 5 ? Math.abs(source.getYaw() - destination.getYaw()) / Math.abs(source.getYaw() - destination.getYaw()) / 2 : 0))
                .setPitch(source.getPitch() - destination.getPitch())
                .constrantAngle()
                .setYaw(source.getYaw() - smoothedAngle.getYaw() / yaw * randomFloat(minYawSmoothing, maxYawSmoothing))
                .constrantAngle()
                .setPitch(source.getPitch() - smoothedAngle.getPitch() / pitch * randomFloat(minPitchSmoothing, maxPitchSmoothing))
                .constrantAngle();
    }
}
