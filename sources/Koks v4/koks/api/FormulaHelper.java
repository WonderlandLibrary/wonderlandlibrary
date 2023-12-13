package koks.api;

import god.buddy.aot.BCompiler;

import java.util.Calendar;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */
public class FormulaHelper {

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public static float getProjectileMotion(double velocity, double gravity, double x, double y) {
        return (float) -Math.toDegrees(Math.atan2(Math.pow(velocity, 2) - Math.sqrt(Math.pow(velocity, 4) - gravity * (gravity * Math.pow(x, 2) + 2 * y * Math.pow(velocity, 2))), gravity * x));
    }

    public static float getPercent(float part, float whole) {
        return part * 100 / whole;
    }

    public static float getPart(float whole, float percent) {
        return whole * percent / 100;
    }

    public static float getWhole(float part, float percent) {
        return part * 100 / percent;
    }

    public static double getABC(int a, int b, int c) {
        return (-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2*a);
    }

    public static float getAverage(float... number) {
        float total = 0;
        for(float num : number) {
            total += num;
        }
        return total / number.length;
    }

    public static int[] getComputus(int year) {
        int a = year % 4;
        int b = year % 7;
        int c = year % 19;
        int k = year / 100;
        int p = k / 3;
        int q = k / 4;
        int M = (15 + k - p - q) % 30;
        int N = (4 + k - q) % 7;
        int d = (19*a + M) % 30;
        int e = (2*b + 4*c + 6*d + N) % 7;
        int easter = (22 + d + e);
        if(d == 29 && e == 6)
            easter = 50;
        if(d == 28 && e == 6 && ((11 * M + 11) % 30) < 19)
            easter = 49;
        int tempEaster = easter;
        if(easter > 31)
            easter -= 31;
        final int month = tempEaster > 31 ? Calendar.MAY : Calendar.MARCH;
        return new int[] {easter, month};
    }

}