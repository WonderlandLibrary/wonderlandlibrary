/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util;

public class Location2D {
    double x;
    double y;

    public Location2D(double x2, double y2) {
        this.x = x2;
        this.y = y2;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double distance(Location2D Location) {
        double a2 = Math.abs(this.x - Location.getX());
        double b2 = Math.abs(this.y - Location.getY());
        double c2 = Math.sqrt(a2 * a2 + b2 * b2);
        return c2;
    }

    public String getPointString() {
        return "(" + this.x + " | " + this.y + ")";
    }
}

