/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import java.io.PrintStream;

public class FastMathCalc {
    private static final long HEX_40000000 = 0x40000000L;
    private static final String TABLE_END_DECL = "    };";
    private static final double[] FACT;
    private static final String TABLE_START_DECL;
    private static final double[][] LN_SPLIT_COEF;

    private static void splitAdd(double[] dArray, double[] dArray2, double[] dArray3) {
        dArray3[0] = dArray[0] + dArray2[0];
        dArray3[1] = dArray[1] + dArray2[1];
        FastMathCalc.resplit(dArray3);
    }

    static void splitReciprocal(double[] dArray, double[] dArray2) {
        double d = 2.384185791015625E-7;
        double d2 = 0.9999997615814209;
        if (dArray[0] == 0.0) {
            dArray[0] = dArray[1];
            dArray[1] = 0.0;
        }
        dArray2[0] = 0.9999997615814209 / dArray[0];
        dArray2[1] = (2.384185791015625E-7 * dArray[0] - 0.9999997615814209 * dArray[1]) / (dArray[0] * dArray[0] + dArray[0] * dArray[1]);
        if (dArray2[1] != dArray2[1]) {
            dArray2[1] = 0.0;
        }
        FastMathCalc.resplit(dArray2);
        for (int i = 0; i < 2; ++i) {
            double d3 = 1.0 - dArray2[0] * dArray[0] - dArray2[0] * dArray[1] - dArray2[1] * dArray[0] - dArray2[1] * dArray[1];
            dArray2[1] = dArray2[1] + (d3 *= dArray2[0] + dArray2[1]);
        }
    }

    static String format(double d) {
        if (d != d) {
            return "Double.NaN,";
        }
        return (d >= 0.0 ? "+" : "") + d + "d,";
    }

    private static void quadMult(double[] dArray, double[] dArray2, double[] dArray3) {
        double[] dArray4 = new double[2];
        double[] dArray5 = new double[2];
        double[] dArray6 = new double[2];
        FastMathCalc.split(dArray[0], dArray4);
        FastMathCalc.split(dArray2[0], dArray5);
        FastMathCalc.splitMult(dArray4, dArray5, dArray6);
        dArray3[0] = dArray6[0];
        dArray3[1] = dArray6[1];
        FastMathCalc.split(dArray2[1], dArray5);
        FastMathCalc.splitMult(dArray4, dArray5, dArray6);
        double d = dArray3[0] + dArray6[0];
        dArray3[1] = dArray3[1] - (d - dArray3[0] - dArray6[0]);
        dArray3[0] = d;
        d = dArray3[0] + dArray6[1];
        dArray3[1] = dArray3[1] - (d - dArray3[0] - dArray6[1]);
        dArray3[0] = d;
        FastMathCalc.split(dArray[1], dArray4);
        FastMathCalc.split(dArray2[0], dArray5);
        FastMathCalc.splitMult(dArray4, dArray5, dArray6);
        d = dArray3[0] + dArray6[0];
        dArray3[1] = dArray3[1] - (d - dArray3[0] - dArray6[0]);
        dArray3[0] = d;
        d = dArray3[0] + dArray6[1];
        dArray3[1] = dArray3[1] - (d - dArray3[0] - dArray6[1]);
        dArray3[0] = d;
        FastMathCalc.split(dArray[1], dArray4);
        FastMathCalc.split(dArray2[1], dArray5);
        FastMathCalc.splitMult(dArray4, dArray5, dArray6);
        d = dArray3[0] + dArray6[0];
        dArray3[1] = dArray3[1] - (d - dArray3[0] - dArray6[0]);
        dArray3[0] = d;
        d = dArray3[0] + dArray6[1];
        dArray3[1] = dArray3[1] - (d - dArray3[0] - dArray6[1]);
        dArray3[0] = d;
    }

    static void printarray(PrintStream printStream, String string, int n, double[][] dArray) {
        printStream.println(string);
        printStream.println("    { ");
        int n2 = 0;
        for (double[] dArray2 : dArray) {
            printStream.print("        {");
            for (double d : dArray2) {
                printStream.printf("%-25.25s", FastMathCalc.format(d));
            }
            printStream.println("}, // " + n2++);
        }
        printStream.println("    };");
    }

    static double slowCos(double d, double[] dArray) {
        double[] dArray2 = new double[2];
        double[] dArray3 = new double[2];
        double[] dArray4 = new double[2];
        double[] dArray5 = new double[2];
        FastMathCalc.split(d, dArray2);
        dArray3[1] = 0.0;
        dArray3[0] = 0.0;
        for (int i = FACT.length - 1; i >= 0; --i) {
            FastMathCalc.splitMult(dArray2, dArray3, dArray5);
            dArray3[0] = dArray5[0];
            dArray3[1] = dArray5[1];
            if ((i & 1) != 0) continue;
            FastMathCalc.split(FACT[i], dArray5);
            FastMathCalc.splitReciprocal(dArray5, dArray4);
            if ((i & 2) != 0) {
                dArray4[0] = -dArray4[0];
                dArray4[1] = -dArray4[1];
            }
            FastMathCalc.splitAdd(dArray3, dArray4, dArray5);
            dArray3[0] = dArray5[0];
            dArray3[1] = dArray5[1];
        }
        if (dArray != null) {
            dArray[0] = dArray3[0];
            dArray[1] = dArray3[1];
        }
        return dArray3[0] + dArray3[1];
    }

    private static void buildSinCosTables(double[] dArray, double[] dArray2, double[] dArray3, double[] dArray4, int n, double[] dArray5, double[] dArray6) {
        double[] dArray7;
        double[] dArray8;
        int n2;
        double[] dArray9 = new double[2];
        for (n2 = 0; n2 < 7; ++n2) {
            double d = (double)n2 / 8.0;
            FastMathCalc.slowSin(d, dArray9);
            dArray[n2] = dArray9[0];
            dArray2[n2] = dArray9[1];
            FastMathCalc.slowCos(d, dArray9);
            dArray3[n2] = dArray9[0];
            dArray4[n2] = dArray9[1];
        }
        for (n2 = 7; n2 < n; ++n2) {
            double[] dArray10 = new double[2];
            dArray8 = new double[2];
            dArray7 = new double[2];
            double[] dArray11 = new double[2];
            double[] dArray12 = new double[2];
            if ((n2 & 1) == 0) {
                dArray10[0] = dArray[n2 / 2];
                dArray10[1] = dArray2[n2 / 2];
                dArray8[0] = dArray3[n2 / 2];
                dArray8[1] = dArray4[n2 / 2];
                FastMathCalc.splitMult(dArray10, dArray8, dArray9);
                dArray[n2] = dArray9[0] * 2.0;
                dArray2[n2] = dArray9[1] * 2.0;
                FastMathCalc.splitMult(dArray8, dArray8, dArray7);
                FastMathCalc.splitMult(dArray10, dArray10, dArray12);
                dArray12[0] = -dArray12[0];
                dArray12[1] = -dArray12[1];
                FastMathCalc.splitAdd(dArray7, dArray12, dArray9);
                dArray3[n2] = dArray9[0];
                dArray4[n2] = dArray9[1];
                continue;
            }
            dArray10[0] = dArray[n2 / 2];
            dArray10[1] = dArray2[n2 / 2];
            dArray8[0] = dArray3[n2 / 2];
            dArray8[1] = dArray4[n2 / 2];
            dArray7[0] = dArray[n2 / 2 + 1];
            dArray7[1] = dArray2[n2 / 2 + 1];
            dArray11[0] = dArray3[n2 / 2 + 1];
            dArray11[1] = dArray4[n2 / 2 + 1];
            FastMathCalc.splitMult(dArray10, dArray11, dArray12);
            FastMathCalc.splitMult(dArray8, dArray7, dArray9);
            FastMathCalc.splitAdd(dArray9, dArray12, dArray9);
            dArray[n2] = dArray9[0];
            dArray2[n2] = dArray9[1];
            FastMathCalc.splitMult(dArray8, dArray11, dArray9);
            FastMathCalc.splitMult(dArray10, dArray7, dArray12);
            dArray12[0] = -dArray12[0];
            dArray12[1] = -dArray12[1];
            FastMathCalc.splitAdd(dArray9, dArray12, dArray9);
            dArray3[n2] = dArray9[0];
            dArray4[n2] = dArray9[1];
        }
        for (n2 = 0; n2 < n; ++n2) {
            double[] dArray13 = new double[2];
            dArray8 = new double[2];
            dArray7 = new double[]{dArray3[n2], dArray4[n2]};
            FastMathCalc.splitReciprocal(dArray7, dArray8);
            dArray13[0] = dArray[n2];
            dArray13[1] = dArray2[n2];
            FastMathCalc.splitMult(dArray13, dArray8, dArray7);
            dArray5[n2] = dArray7[0];
            dArray6[n2] = dArray7[1];
        }
    }

    static double expint(int n, double[] dArray) {
        double[] dArray2 = new double[2];
        double[] dArray3 = new double[2];
        double[] dArray4 = new double[2];
        dArray2[0] = Math.E;
        dArray2[1] = 1.4456468917292502E-16;
        FastMathCalc.split(1.0, dArray4);
        while (n > 0) {
            if ((n & 1) != 0) {
                FastMathCalc.quadMult(dArray4, dArray2, dArray3);
                dArray4[0] = dArray3[0];
                dArray4[1] = dArray3[1];
            }
            FastMathCalc.quadMult(dArray2, dArray2, dArray3);
            dArray2[0] = dArray3[0];
            dArray2[1] = dArray3[1];
            n >>= 1;
        }
        if (dArray != null) {
            dArray[0] = dArray4[0];
            dArray[1] = dArray4[1];
            FastMathCalc.resplit(dArray);
        }
        return dArray4[0] + dArray4[1];
    }

    private static void resplit(double[] dArray) {
        double d = dArray[0] + dArray[1];
        double d2 = -(d - dArray[0] - dArray[1]);
        if (d < 8.0E298 && d > -8.0E298) {
            double d3 = d * 1.073741824E9;
            dArray[0] = d + d3 - d3;
            dArray[1] = d - dArray[0] + d2;
        } else {
            double d4 = d * 9.313225746154785E-10;
            dArray[0] = (d + d4 - d) * 1.073741824E9;
            dArray[1] = d - dArray[0] + d2;
        }
    }

    static {
        TABLE_START_DECL = "    {";
        FACT = new double[]{1.0, 1.0, 2.0, 6.0, 24.0, 120.0, 720.0, 5040.0, 40320.0, 362880.0, 3628800.0, 3.99168E7, 4.790016E8, 6.2270208E9, 8.71782912E10, 1.307674368E12, 2.0922789888E13, 3.55687428096E14, 6.402373705728E15, 1.21645100408832E17};
        LN_SPLIT_COEF = new double[][]{{2.0, 0.0}, {0.6666666269302368, 3.9736429850260626E-8}, {0.3999999761581421, 2.3841857910019882E-8}, {0.2857142686843872, 1.7029898543501842E-8}, {0.2222222089767456, 1.3245471311735498E-8}, {0.1818181574344635, 2.4384203044354907E-8}, {0.1538461446762085, 9.140260083262505E-9}, {0.13333332538604736, 9.220590270857665E-9}, {0.11764700710773468, 1.2393345855018391E-8}, {0.10526403784751892, 8.251545029714408E-9}, {0.0952233225107193, 1.2675934823758863E-8}, {0.08713622391223907, 1.1430250008909141E-8}, {0.07842259109020233, 2.404307984052299E-9}, {0.08371849358081818, 1.176342548272881E-8}, {0.03058958f, 1.2958646899018938E-9}, {0.14982303977012634, 1.225743062930824E-8}};
    }

    static void printarray(PrintStream printStream, String string, int n, double[] dArray) {
        printStream.println(string + "=");
        printStream.println("    {");
        for (double d : dArray) {
            printStream.printf("        %s%n", FastMathCalc.format(d));
        }
        printStream.println("    };");
    }

    public static double slowexp(double d, double[] dArray) {
        double[] dArray2 = new double[2];
        double[] dArray3 = new double[2];
        double[] dArray4 = new double[2];
        double[] dArray5 = new double[2];
        FastMathCalc.split(d, dArray2);
        dArray3[1] = 0.0;
        dArray3[0] = 0.0;
        for (int i = FACT.length - 1; i >= 0; --i) {
            FastMathCalc.splitMult(dArray2, dArray3, dArray5);
            dArray3[0] = dArray5[0];
            dArray3[1] = dArray5[1];
            FastMathCalc.split(FACT[i], dArray5);
            FastMathCalc.splitReciprocal(dArray5, dArray4);
            FastMathCalc.splitAdd(dArray3, dArray4, dArray5);
            dArray3[0] = dArray5[0];
            dArray3[1] = dArray5[1];
        }
        if (dArray != null) {
            dArray[0] = dArray3[0];
            dArray[1] = dArray3[1];
        }
        return dArray3[0] + dArray3[1];
    }

    private FastMathCalc() {
    }

    public static double[] slowLog(double d) {
        double[] dArray = new double[2];
        double[] dArray2 = new double[2];
        double[] dArray3 = new double[2];
        double[] dArray4 = new double[2];
        FastMathCalc.split(d, dArray);
        dArray[0] = dArray[0] + 1.0;
        FastMathCalc.resplit(dArray);
        FastMathCalc.splitReciprocal(dArray, dArray4);
        dArray[0] = dArray[0] - 2.0;
        FastMathCalc.resplit(dArray);
        FastMathCalc.splitMult(dArray, dArray4, dArray3);
        dArray[0] = dArray3[0];
        dArray[1] = dArray3[1];
        FastMathCalc.splitMult(dArray, dArray, dArray2);
        dArray3[0] = LN_SPLIT_COEF[LN_SPLIT_COEF.length - 1][0];
        dArray3[1] = LN_SPLIT_COEF[LN_SPLIT_COEF.length - 1][1];
        for (int i = LN_SPLIT_COEF.length - 2; i >= 0; --i) {
            FastMathCalc.splitMult(dArray3, dArray2, dArray4);
            dArray3[0] = dArray4[0];
            dArray3[1] = dArray4[1];
            FastMathCalc.splitAdd(dArray3, LN_SPLIT_COEF[i], dArray4);
            dArray3[0] = dArray4[0];
            dArray3[1] = dArray4[1];
        }
        FastMathCalc.splitMult(dArray3, dArray, dArray4);
        dArray3[0] = dArray4[0];
        dArray3[1] = dArray4[1];
        return dArray3;
    }

    private static void splitMult(double[] dArray, double[] dArray2, double[] dArray3) {
        dArray3[0] = dArray[0] * dArray2[0];
        dArray3[1] = dArray[0] * dArray2[1] + dArray[1] * dArray2[0] + dArray[1] * dArray2[1];
        FastMathCalc.resplit(dArray3);
    }

    static double slowSin(double d, double[] dArray) {
        double[] dArray2 = new double[2];
        double[] dArray3 = new double[2];
        double[] dArray4 = new double[2];
        double[] dArray5 = new double[2];
        FastMathCalc.split(d, dArray2);
        dArray3[1] = 0.0;
        dArray3[0] = 0.0;
        for (int i = FACT.length - 1; i >= 0; --i) {
            FastMathCalc.splitMult(dArray2, dArray3, dArray5);
            dArray3[0] = dArray5[0];
            dArray3[1] = dArray5[1];
            if ((i & 1) == 0) continue;
            FastMathCalc.split(FACT[i], dArray5);
            FastMathCalc.splitReciprocal(dArray5, dArray4);
            if ((i & 2) != 0) {
                dArray4[0] = -dArray4[0];
                dArray4[1] = -dArray4[1];
            }
            FastMathCalc.splitAdd(dArray3, dArray4, dArray5);
            dArray3[0] = dArray5[0];
            dArray3[1] = dArray5[1];
        }
        if (dArray != null) {
            dArray[0] = dArray3[0];
            dArray[1] = dArray3[1];
        }
        return dArray3[0] + dArray3[1];
    }

    private static void split(double d, double[] dArray) {
        if (d < 8.0E298 && d > -8.0E298) {
            double d2 = d * 1.073741824E9;
            dArray[0] = d + d2 - d2;
            dArray[1] = d - dArray[0];
        } else {
            double d3 = d * 9.313225746154785E-10;
            dArray[0] = (d + d3 - d) * 1.073741824E9;
            dArray[1] = d - dArray[0];
        }
    }
}

