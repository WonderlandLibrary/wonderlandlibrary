// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import java.util.Objects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class Angle
{
    public static final Angle ZERO;
    private final float pitch;
    private final float yaw;
    private final float roll;
    
    public static Angle radians(final float pitch, final float yaw, final float roll) {
        return new Radians(pitch, yaw, roll);
    }
    
    public static Angle radians(final float pitch, final float yaw) {
        return radians(pitch, yaw, 0.0f);
    }
    
    public static Angle radians(final double pitch, final double yaw, final double roll) {
        return radians((float)AngleHelper.roundAngle(pitch), (float)AngleHelper.roundAngle(yaw), (float)AngleHelper.roundAngle(roll));
    }
    
    public static Angle radians(final double pitch, final double yaw) {
        return radians(pitch, yaw, 0.0);
    }
    
    public static Angle degrees(final float pitch, final float yaw, final float roll) {
        return new Degrees(pitch, yaw, roll);
    }
    
    public static Angle degrees(final float pitch, final float yaw) {
        return degrees(pitch, yaw, 0.0f);
    }
    
    public static Angle degrees(final double pitch, final double yaw, final double roll) {
        return degrees((float)AngleHelper.roundAngle(pitch), (float)AngleHelper.roundAngle(yaw), (float)AngleHelper.roundAngle(roll));
    }
    
    public static Angle degrees(final double pitch, final double yaw) {
        return degrees(pitch, yaw, 0.0);
    }
    
    public static Angle copy(final Angle ang) {
        return ang.newInstance(ang.getPitch(), ang.getYaw(), ang.getRoll());
    }
    
    private Angle(final float pitch, final float yaw, final float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getRoll() {
        return this.roll;
    }
    
    public Angle setPitch(final float pitch) {
        return this.newInstance(pitch, this.getYaw(), this.getRoll());
    }
    
    public Angle setYaw(final float yaw) {
        return this.newInstance(this.getPitch(), yaw, this.getRoll());
    }
    
    public Angle setRoll(final float roll) {
        return this.newInstance(this.getPitch(), this.getYaw(), roll);
    }
    
    public abstract boolean isInDegrees();
    
    public boolean isInRadians() {
        return !this.isInDegrees();
    }
    
    public Angle add(final Angle ang) {
        return this.newInstance(this.getPitch() + ang.same(this).getPitch(), this.getYaw() + ang.same(this).getYaw(), this.getRoll() + ang.same(this).getRoll());
    }
    
    public Angle add(final float p, final float y, final float r) {
        return this.add(this.newInstance(p, y, r));
    }
    
    public Angle add(final float p, final float y) {
        return this.add(p, y, 0.0f);
    }
    
    public Angle sub(final Angle ang) {
        return this.add(ang.scale(-1.0f));
    }
    
    public Angle sub(final float p, final float y, final float r) {
        return this.add(-p, -y, -r);
    }
    
    public Angle sub(final float p, final float y) {
        return this.sub(p, y, 0.0f);
    }
    
    public Angle scale(final float factor) {
        return this.newInstance(this.getPitch() * factor, this.getYaw() * factor, this.getRoll() * factor);
    }
    
    public abstract Angle normalize();
    
    public double[] getForwardVector() {
        final double kps = Math.sin(this.inRadians().getPitch());
        final double kpc = Math.cos(this.inRadians().getPitch());
        final double kys = Math.sin(this.inRadians().getYaw());
        final double kyc = Math.cos(this.inRadians().getYaw());
        return new double[] { kpc * kyc, kps, kpc * kys };
    }
    
    public Vec3d getDirectionVector() {
        final float cy = MathHelper.cos(-this.inDegrees().getYaw() * 0.017453292f - 3.1415927f);
        final float sy = MathHelper.sin(-this.inDegrees().getYaw() * 0.017453292f - 3.1415927f);
        final float cp = -MathHelper.cos(-this.inDegrees().getPitch() * 0.017453292f);
        final float sp = MathHelper.sin(-this.inDegrees().getPitch() * 0.017453292f);
        return new Vec3d((double)(sy * cp), (double)sp, (double)(cy * cp));
    }
    
    public float[] toArray() {
        return new float[] { this.getPitch(), this.getYaw(), this.getRoll() };
    }
    
    public abstract Angle inRadians();
    
    public abstract Angle inDegrees();
    
    protected Angle same(final Angle other) {
        return other.isInDegrees() ? this.inDegrees() : this.inRadians();
    }
    
    protected abstract Angle newInstance(final float p0, final float p1, final float p2);
    
    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj instanceof Angle && AngleHelper.isEqual(this, (Angle)obj));
    }
    
    @Override
    public int hashCode() {
        final Angle a = this.normalize().inDegrees();
        return Objects.hash(a.getPitch(), a.getYaw(), a.getRoll());
    }
    
    @Override
    public String toString() {
        return String.format("(%.15f, %.15f, %.15f)[%s]", this.getPitch(), this.getYaw(), this.getRoll(), this.isInRadians() ? "rad" : "deg");
    }
    
    static {
        ZERO = degrees(0.0f, 0.0f, 0.0f);
    }
    
    static class Degrees extends Angle
    {
        private Radians radians;
        
        private Degrees(final float pitch, final float yaw, final float roll) {
            super(pitch, yaw, roll, null);
            this.radians = null;
        }
        
        @Override
        public boolean isInDegrees() {
            return true;
        }
        
        @Override
        public Angle normalize() {
            return this.newInstance(AngleHelper.normalizeInDegrees(this.getPitch()), AngleHelper.normalizeInDegrees(this.getYaw()), AngleHelper.normalizeInDegrees(this.getRoll()));
        }
        
        @Override
        public Angle inRadians() {
            return (this.radians == null) ? (this.radians = (Radians)Angle.radians(Math.toRadians(this.getPitch()), Math.toRadians(this.getYaw()), Math.toRadians(this.getRoll()))) : this.radians;
        }
        
        @Override
        public Angle inDegrees() {
            return this;
        }
        
        @Override
        protected Angle newInstance(final float pitch, final float yaw, final float roll) {
            return new Degrees(pitch, yaw, roll);
        }
    }
    
    static class Radians extends Angle
    {
        private Degrees degrees;
        
        private Radians(final float pitch, final float yaw, final float roll) {
            super(pitch, yaw, roll, null);
            this.degrees = null;
        }
        
        @Override
        public boolean isInDegrees() {
            return false;
        }
        
        @Override
        public Angle normalize() {
            return this.newInstance(AngleHelper.normalizeInRadians(this.getPitch()), AngleHelper.normalizeInRadians(this.getYaw()), AngleHelper.normalizeInRadians(this.getRoll()));
        }
        
        @Override
        public Angle inRadians() {
            return this;
        }
        
        @Override
        public Angle inDegrees() {
            return (this.degrees == null) ? (this.degrees = (Degrees)Angle.degrees(Math.toDegrees(this.getPitch()), Math.toDegrees(this.getYaw()), Math.toDegrees(this.getRoll()))) : this.degrees;
        }
        
        @Override
        protected Angle newInstance(final float pitch, final float yaw, final float roll) {
            return new Radians(pitch, yaw, roll);
        }
    }
}
