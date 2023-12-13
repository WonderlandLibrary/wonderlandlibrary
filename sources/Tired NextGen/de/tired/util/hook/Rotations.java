package de.tired.util.hook;

import net.minecraft.util.MathHelper;

public class Rotations implements Cloneable {
    public static float server_yaw, server_pitch;

    public float yaw, pitch, beforePitch, beforeYaw;

    public static Rotations instance;

    public Rotations() {
        instance = this;
    }

    public Rotations normalize() {
        pitch = MathHelper.wrapDegrees(pitch);
        yaw = MathHelper.wrapDegrees(yaw);
        System.out.println("Working?");
        return this;
    }

    @Override
    public Rotations clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Rotations) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
