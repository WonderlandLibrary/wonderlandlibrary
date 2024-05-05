/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util;

public class Box {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public Box(double x2, double y2, double z2, double x1, double y1, double z1) {
        this.minX = x2;
        this.minY = y2;
        this.minZ = z2;
        this.maxX = x1;
        this.maxY = y1;
        this.maxZ = z1;
    }
}

