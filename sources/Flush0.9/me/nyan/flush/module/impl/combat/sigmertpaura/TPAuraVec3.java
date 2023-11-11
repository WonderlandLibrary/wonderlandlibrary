package me.nyan.flush.module.impl.combat.sigmertpaura;

import net.minecraft.util.Vec3;

public class TPAuraVec3 {
    private final double x;
    private final double y;
    private final double z;

    public TPAuraVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public TPAuraVec3 addVector(double x, double y, double z) {
        return new TPAuraVec3(this.x + x, this.y + y, this.z + z);
    }

    public TPAuraVec3 floor() {
        return new TPAuraVec3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }

    public double squareDistanceTo(TPAuraVec3 v) {
        return Math.pow(v.x - this.x, 2) + Math.pow(v.y - this.y, 2) + Math.pow(v.z - this.z, 2);
    }

    public TPAuraVec3 add(TPAuraVec3 v) {
        return addVector(v.getX(), v.getY(), v.getZ());
    }

    public Vec3 mc() {
        return new Vec3(x, y, z);
    }
}