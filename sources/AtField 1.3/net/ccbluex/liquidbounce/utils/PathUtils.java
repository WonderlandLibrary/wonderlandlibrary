/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 */
package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3d;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class PathUtils
extends MinecraftInstance {
    private static double getDistance(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d - d4;
        double d8 = d2 - d5;
        double d9 = d3 - d6;
        return Math.sqrt(d7 * d7 + d8 * d8 + d9 * d9);
    }

    public static List findPath(double d, double d2, double d3, double d4) {
        ArrayList<Vector3d> arrayList = new ArrayList<Vector3d>();
        double d5 = Math.ceil(PathUtils.getDistance(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ(), d, d2, d3) / d4);
        double d6 = d - mc.getThePlayer().getPosX();
        double d7 = d2 - mc.getThePlayer().getPosY();
        double d8 = d3 - mc.getThePlayer().getPosZ();
        for (double d9 = 1.0; d9 <= d5; d9 += 1.0) {
            arrayList.add(new Vector3d(mc.getThePlayer().getPosX() + d6 * d9 / d5, mc.getThePlayer().getPosY() + d7 * d9 / d5, mc.getThePlayer().getPosZ() + d8 * d9 / d5));
        }
        return arrayList;
    }

    public static List findBlinkPath(double d, double d2, double d3) {
        ArrayList<Vector3d> arrayList = new ArrayList<Vector3d>();
        double d4 = mc.getThePlayer().getPosX();
        double d5 = mc.getThePlayer().getPosY();
        double d6 = mc.getThePlayer().getPosZ();
        double d7 = Math.abs(d4 - d) + Math.abs(d5 - d2) + Math.abs(d6 - d3);
        int n = 0;
        while (d7 > 0.0) {
            d7 = Math.abs(d4 - d) + Math.abs(d5 - d2) + Math.abs(d6 - d3);
            double d8 = d4 - d;
            double d9 = d5 - d2;
            double d10 = d6 - d3;
            double d11 = !(n & true) ? 0.4 : 0.1;
            double d12 = Math.min(Math.abs(d8), d11);
            if (d8 < 0.0) {
                d4 += d12;
            }
            if (d8 > 0.0) {
                d4 -= d12;
            }
            double d13 = Math.min(Math.abs(d9), 0.25);
            if (d9 < 0.0) {
                d5 += d13;
            }
            if (d9 > 0.0) {
                d5 -= d13;
            }
            double d14 = Math.min(Math.abs(d10), d11);
            if (d10 < 0.0) {
                d6 += d14;
            }
            if (d10 > 0.0) {
                d6 -= d14;
            }
            arrayList.add(new Vector3d(d4, d5, d6));
            ++n;
        }
        return arrayList;
    }
}

