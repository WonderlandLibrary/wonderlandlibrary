/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import java.io.PrintStream;

public class FastMathCalc {
    private static final long HEX_40000000 = 0x40000000L;
    private static final double[] FACT;
    private static final double[][] LN_SPLIT_COEF;
    private static final String TABLE_START_DECL = "    {";
    private static final String TABLE_END_DECL = "    };";

    private FastMathCalc() {
    }

    private static void buildSinCosTables(double[] SINE_TABLE_A, double[] SINE_TABLE_B, double[] COSINE_TABLE_A, double[] COSINE_TABLE_B, int SINE_TABLE_LEN, double[] TANGENT_TABLE_A, double[] TANGENT_TABLE_B) {
        double[] as;
        double[] ys;
        int i;
        double[] result = new double[2];
        for (i = 0; i < 7; ++i) {
            double x = (double)i / 8.0;
            FastMathCalc.slowSin(x, result);
            SINE_TABLE_A[i] = result[0];
            SINE_TABLE_B[i] = result[1];
            FastMathCalc.slowCos(x, result);
            COSINE_TABLE_A[i] = result[0];
            COSINE_TABLE_B[i] = result[1];
        }
        while (i < SINE_TABLE_LEN) {
            double[] xs = new double[2];
            ys = new double[2];
            as = new double[2];
            double[] bs = new double[2];
            double[] temps = new double[2];
            if ((i & 1) == 0) {
                xs[0] = SINE_TABLE_A[i / 2];
                xs[1] = SINE_TABLE_B[i / 2];
                ys[0] = COSINE_TABLE_A[i / 2];
                ys[1] = COSINE_TABLE_B[i / 2];
                FastMathCalc.splitMult(xs, ys, result);
                SINE_TABLE_A[i] = result[0] * 2.0;
                SINE_TABLE_B[i] = result[1] * 2.0;
                FastMathCalc.splitMult(ys, ys, as);
                FastMathCalc.splitMult(xs, xs, temps);
                temps[0] = -temps[0];
                temps[1] = -temps[1];
                FastMathCalc.splitAdd(as, temps, result);
                COSINE_TABLE_A[i] = result[0];
                COSINE_TABLE_B[i] = result[1];
            } else {
                xs[0] = SINE_TABLE_A[i / 2];
                xs[1] = SINE_TABLE_B[i / 2];
                ys[0] = COSINE_TABLE_A[i / 2];
                ys[1] = COSINE_TABLE_B[i / 2];
                as[0] = SINE_TABLE_A[i / 2 + 1];
                as[1] = SINE_TABLE_B[i / 2 + 1];
                bs[0] = COSINE_TABLE_A[i / 2 + 1];
                bs[1] = COSINE_TABLE_B[i / 2 + 1];
                FastMathCalc.splitMult(xs, bs, temps);
                FastMathCalc.splitMult(ys, as, result);
                FastMathCalc.splitAdd(result, temps, result);
                SINE_TABLE_A[i] = result[0];
                SINE_TABLE_B[i] = result[1];
                FastMathCalc.splitMult(ys, bs, result);
                FastMathCalc.splitMult(xs, as, temps);
                temps[0] = -temps[0];
                temps[1] = -temps[1];
                FastMathCalc.splitAdd(result, temps, result);
                COSINE_TABLE_A[i] = result[0];
                COSINE_TABLE_B[i] = result[1];
            }
            ++i;
        }
        for (i = 0; i < SINE_TABLE_LEN; ++i) {
            double[] xs = new double[2];
            ys = new double[2];
            as = new double[]{COSINE_TABLE_A[i], COSINE_TABLE_B[i]};
            FastMathCalc.splitReciprocal(as, ys);
            xs[0] = SINE_TABLE_A[i];
            xs[1] = SINE_TABLE_B[i];
            FastMathCalc.splitMult(xs, ys, as);
            TANGENT_TABLE_A[i] = as[0];
            TANGENT_TABLE_B[i] = as[1];
        }
    }

    static double slowCos(double x, double[] result) {
        double[] xs = new double[2];
        double[] ys = new double[2];
        double[] facts = new double[2];
        double[] as = new double[2];
        FastMathCalc.split(x, xs);
        ys[1] = 0.0;
        ys[0] = 0.0;
        for (int i = FACT.length - 1; i >= 0; --i) {
            FastMathCalc.splitMult(xs, ys, as);
            ys[0] = as[0];
            ys[1] = as[1];
            if ((i & 1) != 0) continue;
            FastMathCalc.split(FACT[i], as);
            FastMathCalc.splitReciprocal(as, facts);
            if ((i & 2) != 0) {
                facts[0] = -facts[0];
                facts[1] = -facts[1];
            }
            FastMathCalc.splitAdd(ys, facts, as);
            ys[0] = as[0];
            ys[1] = as[1];
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
        }
        return ys[0] + ys[1];
    }

    static double slowSin(double x, double[] result) {
        double[] xs = new double[2];
        double[] ys = new double[2];
        double[] facts = new double[2];
        double[] as = new double[2];
        FastMathCalc.split(x, xs);
        ys[1] = 0.0;
        ys[0] = 0.0;
        for (int i = FACT.length - 1; i >= 0; --i) {
            FastMathCalc.splitMult(xs, ys, as);
            ys[0] = as[0];
            ys[1] = as[1];
            if ((i & 1) == 0) continue;
            FastMathCalc.split(FACT[i], as);
            FastMathCalc.splitReciprocal(as, facts);
            if ((i & 2) != 0) {
                facts[0] = -facts[0];
                facts[1] = -facts[1];
            }
            FastMathCalc.splitAdd(ys, facts, as);
            ys[0] = as[0];
            ys[1] = as[1];
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
        }
        return ys[0] + ys[1];
    }

    public static double slowexp(double x, double[] result) {
        double[] xs = new double[2];
        double[] ys = new double[2];
        double[] facts = new double[2];
        double[] as = new double[2];
        FastMathCalc.split(x, xs);
        ys[1] = 0.0;
        ys[0] = 0.0;
        for (int i = FACT.length - 1; i >= 0; --i) {
            FastMathCalc.splitMult(xs, ys, as);
            ys[0] = as[0];
            ys[1] = as[1];
            FastMathCalc.split(FACT[i], as);
            FastMathCalc.splitReciprocal(as, facts);
            FastMathCalc.splitAdd(ys, facts, as);
            ys[0] = as[0];
            ys[1] = as[1];
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
        }
        return ys[0] + ys[1];
    }

    private static void split(double d, double[] split) {
        if (d < 8.0E298 && d > -8.0E298) {
            double a = d * 1.073741824E9;
            split[0] = d + a - a;
            split[1] = d - split[0];
        } else {
            double a = d * 9.313225746154785E-10;
            split[0] = (d + a - d) * 1.073741824E9;
            split[1] = d - split[0];
        }
    }

    private static void resplit(double[] a) {
        double c = a[0] + a[1];
        double d = -(c - a[0] - a[1]);
        if (c < 8.0E298 && c > -8.0E298) {
            double z = c * 1.073741824E9;
            a[0] = c + z - z;
            a[1] = c - a[0] + d;
        } else {
            double z = c * 9.313225746154785E-10;
            a[0] = (c + z - c) * 1.073741824E9;
            a[1] = c - a[0] + d;
        }
    }

    private static void splitMult(double[] a, double[] b, double[] ans) {
        ans[0] = a[0] * b[0];
        ans[1] = a[0] * b[1] + a[1] * b[0] + a[1] * b[1];
        FastMathCalc.resplit(ans);
    }

    private static void splitAdd(double[] a, double[] b, double[] ans) {
        ans[0] = a[0] + b[0];
        ans[1] = a[1] + b[1];
        FastMathCalc.resplit(ans);
    }

    static void splitReciprocal(double[] in, double[] result) {
        double b = 2.384185791015625E-7;
        double a = 0.9999997615814209;
        if (in[0] == 0.0) {
            in[0] = in[1];
            in[1] = 0.0;
        }
        result[0] = 0.9999997615814209 / in[0];
        result[1] = (2.384185791015625E-7 * in[0] - 0.9999997615814209 * in[1]) / (in[0] * in[0] + in[0] * in[1]);
        if (result[1] != result[1]) {
            result[1] = 0.0;
        }
        FastMathCalc.resplit(result);
        for (int i = 0; i < 2; ++i) {
            double err = 1.0 - result[0] * in[0] - result[0] * in[1] - result[1] * in[0] - result[1] * in[1];
            result[1] = result[1] + (err *= result[0] + result[1]);
        }
    }

    private static void quadMult(double[] a, double[] b, double[] result) {
        double[] xs = new double[2];
        double[] ys = new double[2];
        double[] zs = new double[2];
        FastMathCalc.split(a[0], xs);
        FastMathCalc.split(b[0], ys);
        FastMathCalc.splitMult(xs, ys, zs);
        result[0] = zs[0];
        result[1] = zs[1];
        FastMathCalc.split(b[1], ys);
        FastMathCalc.splitMult(xs, ys, zs);
        double tmp = result[0] + zs[0];
        result[1] = result[1] - (tmp - result[0] - zs[0]);
        result[0] = tmp;
        tmp = result[0] + zs[1];
        result[1] = result[1] - (tmp - result[0] - zs[1]);
        result[0] = tmp;
        FastMathCalc.split(a[1], xs);
        FastMathCalc.split(b[0], ys);
        FastMathCalc.splitMult(xs, ys, zs);
        tmp = result[0] + zs[0];
        result[1] = result[1] - (tmp - result[0] - zs[0]);
        result[0] = tmp;
        tmp = result[0] + zs[1];
        result[1] = result[1] - (tmp - result[0] - zs[1]);
        result[0] = tmp;
        FastMathCalc.split(a[1], xs);
        FastMathCalc.split(b[1], ys);
        FastMathCalc.splitMult(xs, ys, zs);
        tmp = result[0] + zs[0];
        result[1] = result[1] - (tmp - result[0] - zs[0]);
        result[0] = tmp;
        tmp = result[0] + zs[1];
        result[1] = result[1] - (tmp - result[0] - zs[1]);
        result[0] = tmp;
    }

    static double expint(int p, double[] result) {
        double[] xs = new double[2];
        double[] as = new double[2];
        double[] ys = new double[2];
        xs[0] = Math.E;
        xs[1] = 1.4456468917292502E-16;
        FastMathCalc.split(1.0, ys);
        while (p > 0) {
            if ((p & 1) != 0) {
                FastMathCalc.quadMult(ys, xs, as);
                ys[0] = as[0];
                ys[1] = as[1];
            }
            FastMathCalc.quadMult(xs, xs, as);
            xs[0] = as[0];
            xs[1] = as[1];
            p >>= 1;
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
            FastMathCalc.resplit(result);
        }
        return ys[0] + ys[1];
    }

    public static double[] slowLog(double xi) {
        double[] x = new double[2];
        double[] x2 = new double[2];
        double[] y = new double[2];
        double[] a = new double[2];
        FastMathCalc.split(xi, x);
        x[0] = x[0] + 1.0;
        FastMathCalc.resplit(x);
        FastMathCalc.splitReciprocal(x, a);
        x[0] = x[0] - 2.0;
        FastMathCalc.resplit(x);
        FastMathCalc.splitMult(x, a, y);
        x[0] = y[0];
        x[1] = y[1];
        FastMathCalc.splitMult(x, x, x2);
        y[0] = LN_SPLIT_COEF[LN_SPLIT_COEF.length - 1][0];
        y[1] = LN_SPLIT_COEF[LN_SPLIT_COEF.length - 1][1];
        for (int i = LN_SPLIT_COEF.length - 2; i >= 0; --i) {
            FastMathCalc.splitMult(y, x2, a);
            y[0] = a[0];
            y[1] = a[1];
            FastMathCalc.splitAdd(y, LN_SPLIT_COEF[i], a);
            y[0] = a[0];
            y[1] = a[1];
        }
        FastMathCalc.splitMult(y, x, a);
        y[0] = a[0];
        y[1] = a[1];
        return y;
    }

    static void printarray(PrintStream out, String name, int expectedLen, double[][] array2d) {
        out.println(name);
        out.println("    { ");
        int i = 0;
        for (double[] array : array2d) {
            out.print("        {");
            for (double d : array) {
                out.printf("%-25.25s", FastMathCalc.format(d));
            }
            out.println("}, // " + i++);
        }
        out.println(TABLE_END_DECL);
    }

    static void printarray(PrintStream out, String name, int expectedLen, double[] array) {
        out.println(name + "=");
        out.println(TABLE_START_DECL);
        for (double d : array) {
            out.printf("        %s%n", FastMathCalc.format(d));
        }
        out.println(TABLE_END_DECL);
    }

    static String format(double d) {
        if (d != d) {
            return "Double.NaN,";
        }
        return (d >= 0.0 ? "+" : "") + Double.toString(d) + "d,";
    }

    /*
     * Exception decompiling
     */
    static {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Underrun type stack
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getEntry(StackSim.java:35)
         *     at org.benf.cfr.reader.bytecode.opcode.OperationFactoryDefault.getStackTypes(OperationFactoryDefault.java:51)
         *     at org.benf.cfr.reader.bytecode.opcode.OperationFactoryDup.getStackDelta(OperationFactoryDup.java:18)
         *     at org.benf.cfr.reader.bytecode.opcode.JVMInstr.getStackDelta(JVMInstr.java:315)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:199)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }
}

