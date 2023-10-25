/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.BoundingBox
 *  javafx.geometry.Bounds
 *  javafx.geometry.Point2D
 *  javafx.geometry.Point3D
 */
package com.sun.javafx.geometry;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public final class BoundsUtils {
    private BoundsUtils() {
    }

    private static double min4(double d, double d2, double d3, double d4) {
        return Math.min(Math.min(d, d2), Math.min(d3, d4));
    }

    private static double min8(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        return Math.min(BoundsUtils.min4(d, d2, d3, d4), BoundsUtils.min4(d5, d6, d7, d8));
    }

    private static double max4(double d, double d2, double d3, double d4) {
        return Math.max(Math.max(d, d2), Math.max(d3, d4));
    }

    private static double max8(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        return Math.max(BoundsUtils.max4(d, d2, d3, d4), BoundsUtils.max4(d5, d6, d7, d8));
    }

    public static Bounds createBoundingBox(Point2D point2D, Point2D point2D2, Point2D point2D3, Point2D point2D4, Point2D point2D5, Point2D point2D6, Point2D point2D7, Point2D point2D8) {
        if (point2D == null || point2D2 == null || point2D3 == null || point2D4 == null || point2D5 == null || point2D6 == null || point2D7 == null || point2D8 == null) {
            return null;
        }
        double d = BoundsUtils.min8(point2D.getX(), point2D2.getX(), point2D3.getX(), point2D4.getX(), point2D5.getX(), point2D6.getX(), point2D7.getX(), point2D8.getX());
        double d2 = BoundsUtils.max8(point2D.getX(), point2D2.getX(), point2D3.getX(), point2D4.getX(), point2D5.getX(), point2D6.getX(), point2D7.getX(), point2D8.getX());
        double d3 = BoundsUtils.min8(point2D.getY(), point2D2.getY(), point2D3.getY(), point2D4.getY(), point2D5.getY(), point2D6.getY(), point2D7.getY(), point2D8.getY());
        double d4 = BoundsUtils.max8(point2D.getY(), point2D2.getY(), point2D3.getY(), point2D4.getY(), point2D5.getY(), point2D6.getY(), point2D7.getY(), point2D8.getY());
        return new BoundingBox(d, d3, d2 - d, d4 - d3);
    }

    public static Bounds createBoundingBox(Point3D point3D, Point3D point3D2, Point3D point3D3, Point3D point3D4, Point3D point3D5, Point3D point3D6, Point3D point3D7, Point3D point3D8) {
        if (point3D == null || point3D2 == null || point3D3 == null || point3D4 == null || point3D5 == null || point3D6 == null || point3D7 == null || point3D8 == null) {
            return null;
        }
        double d = BoundsUtils.min8(point3D.getX(), point3D2.getX(), point3D3.getX(), point3D4.getX(), point3D5.getX(), point3D6.getX(), point3D7.getX(), point3D8.getX());
        double d2 = BoundsUtils.max8(point3D.getX(), point3D2.getX(), point3D3.getX(), point3D4.getX(), point3D5.getX(), point3D6.getX(), point3D7.getX(), point3D8.getX());
        double d3 = BoundsUtils.min8(point3D.getY(), point3D2.getY(), point3D3.getY(), point3D4.getY(), point3D5.getY(), point3D6.getY(), point3D7.getY(), point3D8.getY());
        double d4 = BoundsUtils.max8(point3D.getY(), point3D2.getY(), point3D3.getY(), point3D4.getY(), point3D5.getY(), point3D6.getY(), point3D7.getY(), point3D8.getY());
        double d5 = BoundsUtils.min8(point3D.getZ(), point3D2.getZ(), point3D3.getZ(), point3D4.getZ(), point3D5.getZ(), point3D6.getZ(), point3D7.getZ(), point3D8.getZ());
        double d6 = BoundsUtils.max8(point3D.getZ(), point3D2.getZ(), point3D3.getZ(), point3D4.getZ(), point3D5.getZ(), point3D6.getZ(), point3D7.getZ(), point3D8.getZ());
        return new BoundingBox(d, d3, d5, d2 - d, d4 - d3, d6 - d5);
    }

    public static Bounds createBoundingBox(Point2D point2D, Point2D point2D2, Point2D point2D3, Point2D point2D4) {
        if (point2D == null || point2D2 == null || point2D3 == null || point2D4 == null) {
            return null;
        }
        double d = BoundsUtils.min4(point2D.getX(), point2D2.getX(), point2D3.getX(), point2D4.getX());
        double d2 = BoundsUtils.max4(point2D.getX(), point2D2.getX(), point2D3.getX(), point2D4.getX());
        double d3 = BoundsUtils.min4(point2D.getY(), point2D2.getY(), point2D3.getY(), point2D4.getY());
        double d4 = BoundsUtils.max4(point2D.getY(), point2D2.getY(), point2D3.getY(), point2D4.getY());
        return new BoundingBox(d, d3, d2 - d, d4 - d3);
    }
}

