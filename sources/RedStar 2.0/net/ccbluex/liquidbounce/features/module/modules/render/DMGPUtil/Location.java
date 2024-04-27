package net.ccbluex.liquidbounce.features.module.modules.render.DMGPUtil;

import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.minecraft.entity.EntityLivingBase;

public class Location {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public Location(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public Location(WBlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public Location(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public Location(EntityLivingBase entity) {
        this.x = entity.field_70165_t;
        this.y = entity.field_70163_u;
        this.z = entity.field_70161_v;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public Location add(int x, int y, int z) {
        this.x += (double)x;
        this.y += (double)y;
        this.z += (double)z;
        return this;
    }

    public Location add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public double getX() {
        return this.x;
    }

    public Location setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return this.y;
    }

    public Location setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return this.z;
    }

    public Location setZ(double z) {
        this.z = z;
        return this;
    }

    public float getYaw() {
        return this.yaw;
    }

    public Location setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public float getPitch() {
        return this.pitch;
    }

    public Location setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public double distanceTo(Location loc) {
        double dx = loc.x - this.x;
        double dz = loc.z - this.z;
        double dy = loc.y - this.y;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
