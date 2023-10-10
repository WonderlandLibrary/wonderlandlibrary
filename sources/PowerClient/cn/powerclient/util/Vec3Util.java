/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util;

import java.util.ArrayList;
import net.minecraft.util.Vec3;

public class Vec3Util {
    private Vec3 vec1;
    private Vec3 vec2;
    private Vec3 vec3;
    private double offsetX;
    private double offsetY;
    private double offsetZ;

    public Vec3Util(Vec3 start, Vec3 end) {
        this.vec1 = start;
        this.vec3 = end;
        this.init(start, end);
    }

    public Vec3Util(Vec3 vector, float yaw, float pitch, double length) {
        this.vec1 = vector;
        double calculatedX = Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        double calculatedY = Math.sin(Math.toRadians(pitch));
        double calculatedZ = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        double x2 = calculatedX * length + vector.xCoord;
        double y2 = calculatedY * length + vector.yCoord;
        double z2 = calculatedZ * length + vector.zCoord;
        this.vec3 = new Vec3(x2, y2, z2);
        this.init(this.vec1, this.vec3);
    }

    private void init(Vec3 start, Vec3 end) {
        this.vec2 = this.calculate(start, end);
        this.offsetX = end.xCoord - start.xCoord;
        this.offsetY = end.yCoord - start.yCoord;
        this.offsetZ = end.zCoord - start.zCoord;
    }

    private Vec3 calculate(Vec3 v1, Vec3 v2) {
        double x2 = (v1.xCoord + v1.yCoord) / 2.0;
        double y2 = (v1.yCoord + v2.yCoord) / 2.0;
        double z2 = (v1.zCoord + v2.zCoord) / 2.0;
        return new Vec3(x2, y2, z2);
    }

    public double getOffsetX() {
        return this.offsetX;
    }

    public double getOffsetY() {
        return this.offsetY;
    }

    public double getOffsetZ() {
        return this.offsetZ;
    }

    public Vec3 getStartVector() {
        return this.vec1;
    }

    public double getLength(Vec3 vector1, Vec3 vector2) {
        return vector1.distanceTo(vector2);
    }

    public ArrayList<Vec3> getIntervalTimer(double interval) {
        int intervaL = (int)(this.getStartVector().distanceTo(this.getEndVector()) / interval) + 1;
        return this.calculateInterval(intervaL);
    }

    private ArrayList<Vec3> calculateInterval(int interval) {
        ArrayList<Vec3> points = new ArrayList<Vec3>();
        double xOff = this.getOffsetX() / (double)(--interval);
        double yOff = this.getOffsetY() / (double)interval;
        double zOff = this.getOffsetZ() / (double)interval;
        int i2 = 0;
        while (i2 <= interval) {
            double xOffset = xOff * (double)i2;
            double yOffset = yOff * (double)i2;
            double zOffset = zOff * (double)i2;
            points.add(new Vec3(this.getStartVector().xCoord + xOffset, this.getStartVector().yCoord + yOffset, this.getStartVector().zCoord + zOffset));
            ++i2;
        }
        return points;
    }

    public Vec3 getEndVector() {
        return this.vec3;
    }
}

