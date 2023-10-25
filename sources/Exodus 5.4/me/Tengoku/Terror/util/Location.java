/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class Location {
    private double y;
    private double z;
    private float pitch;
    private double x;
    private float yaw;

    public Location(double d, double d2, double d3, float f, float f2) {
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.yaw = f;
        this.pitch = f2;
    }

    public double getY() {
        return this.y;
    }

    public Location add(double d, double d2, double d3) {
        this.x += d;
        this.y += d2;
        this.z += d3;
        return this;
    }

    public float getYaw() {
        return this.yaw;
    }

    public double distanceToY(Location location) {
        double d = location.y - this.y;
        return Math.sqrt(d * d);
    }

    public Location setPitch(float f) {
        this.pitch = f;
        return this;
    }

    public double getZ() {
        return this.z;
    }

    public Location(BlockPos blockPos) {
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public Location(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public Location subtract(double d, double d2, double d3) {
        this.x -= d;
        this.y -= d2;
        this.z -= d3;
        return this;
    }

    public Location setYaw(float f) {
        this.yaw = f;
        return this;
    }

    public Location add(int n, int n2, int n3) {
        this.x += (double)n;
        this.y += (double)n2;
        this.z += (double)n3;
        return this;
    }

    public Location(int n, int n2, int n3) {
        this.x = n;
        this.y = n2;
        this.z = n3;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public Location setX(double d) {
        this.x = d;
        return this;
    }

    public Location subtract(int n, int n2, int n3) {
        this.x -= (double)n;
        this.y -= (double)n2;
        this.z -= (double)n3;
        return this;
    }

    public static Location fromBlockPos(BlockPos blockPos) {
        return new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public Location setZ(double d) {
        this.z = d;
        return this;
    }

    public Block getBlock() {
        Minecraft.getMinecraft();
        return Minecraft.theWorld.getBlockState(this.toBlockPos()).getBlock();
    }

    public Location setY(double d) {
        this.y = d;
        return this;
    }

    public double distanceTo(Location location) {
        double d = location.x - this.x;
        double d2 = location.z - this.z;
        double d3 = location.y - this.y;
        return Math.sqrt(d * d + d3 * d3 + d2 * d2);
    }

    public float getPitch() {
        return this.pitch;
    }

    public double getX() {
        return this.x;
    }

    public BlockPos toBlockPos() {
        return new BlockPos(this.getX(), this.getY(), this.getZ());
    }
}

