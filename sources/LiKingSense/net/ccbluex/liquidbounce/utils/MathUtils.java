/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmOverloads
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\r\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J/\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\u0012\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00070\u00072\u0006\u0010\t\u001a\u00020\u0004H\u0007\u00a2\u0006\u0002\u0010\nJ'\u0010\u000b\u001a\u00020\u00042\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007\u00a2\u0006\u0002\u0010\u000eJ5\u0010\u000f\u001a\u00020\u00042\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0015J&\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u001d\u001a\u00020\u0004J5\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00070\u00072\u0012\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00070\u00072\u0006\u0010\u001f\u001a\u00020\u0017H\u0007\u00a2\u0006\u0002\u0010 J5\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\u0006\u0010\t\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\"J\u000e\u0010#\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\u0004J_\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00070\u00072\u0012\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00070\u00072\u0006\u0010&\u001a\u00020\u00042\b\b\u0002\u0010'\u001a\u00020\u00172\b\b\u0002\u0010(\u001a\u00020\u00172\u0014\b\u0002\u0010)\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00070*H\u0007\u00a2\u0006\u0002\u0010+J\u001a\u0010,\u001a\u00020-*\u00020\u00042\u0006\u0010.\u001a\u00020\u00042\u0006\u0010/\u001a\u00020\u0004J\u001a\u0010,\u001a\u00020-*\u00020\u00152\u0006\u0010.\u001a\u00020\u00152\u0006\u0010/\u001a\u00020\u0015J\u001a\u0010,\u001a\u00020-*\u00020\u00172\u0006\u0010.\u001a\u00020\u00172\u0006\u0010/\u001a\u00020\u0017J\n\u00100\u001a\u00020\u0004*\u00020\u0004J\n\u00101\u001a\u00020\u0004*\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u00062"}, d2={"Lnet/ccbluex/liquidbounce/utils/MathUtils;", "", "()V", "DEGREES_TO_RADIANS", "", "RADIANS_TO_DEGREES", "calcCurvePoint", "", "points", "t", "([[Ljava/lang/Double;D)[Ljava/lang/Double;", "distanceSq", "a", "b", "([Ljava/lang/Double;[Ljava/lang/Double;)D", "distanceToSegmentSq", "p", "v", "w", "([Ljava/lang/Double;[Ljava/lang/Double;[Ljava/lang/Double;)D", "gaussian", "", "x", "", "sigma", "getDistance", "x1", "y1", "x2", "y2", "getPointsOnCurve", "num", "([[Ljava/lang/Double;I)[[Ljava/lang/Double;", "lerp", "([Ljava/lang/Double;[Ljava/lang/Double;D)[Ljava/lang/Double;", "radians", "degrees", "simplifyPoints", "epsilon", "start", "end", "outPoints", "", "([[Ljava/lang/Double;DIILjava/util/List;)[[Ljava/lang/Double;", "inRange", "", "base", "range", "toDegrees", "toRadians", "LiKingSense"})
public final class MathUtils {
    public static final double DEGREES_TO_RADIANS = Math.PI / 180;
    public static final double RADIANS_TO_DEGREES = 57.29577951308232;
    public static final MathUtils INSTANCE;

    public final double radians(double degrees) {
        return degrees * Math.PI / (double)180;
    }

    public final double getDistance(double x1, double y1, double x2, double y2) {
        double d = x1 - x2;
        double d2 = 2;
        boolean bl = false;
        double d3 = Math.pow(d, d2);
        d = y1 - y2;
        d2 = 2;
        double d4 = d3;
        bl = false;
        double d5 = Math.pow(d, d2);
        d = d4 + d5;
        boolean bl2 = false;
        return Math.sqrt(d);
    }

    @NotNull
    public final Double[] lerp(@NotNull Double[] a, @NotNull Double[] b, double t2) {
        Intrinsics.checkParameterIsNotNull((Object)a, (String)"a");
        Intrinsics.checkParameterIsNotNull((Object)b, (String)"b");
        return new Double[]{a[0] + (b[0] - a[0]) * t2, a[1] + (b[1] - a[1]) * t2};
    }

    public final double distanceSq(@NotNull Double[] a, @NotNull Double[] b) {
        Intrinsics.checkParameterIsNotNull((Object)a, (String)"a");
        Intrinsics.checkParameterIsNotNull((Object)b, (String)"b");
        double d = a[0] - b[0];
        int n = 2;
        boolean bl = false;
        double d2 = Math.pow(d, n);
        d = a[1] - b[1];
        n = 2;
        double d3 = d2;
        bl = false;
        double d4 = Math.pow(d, n);
        return d3 + d4;
    }

    public final double distanceToSegmentSq(@NotNull Double[] p, @NotNull Double[] v, @NotNull Double[] w) {
        Intrinsics.checkParameterIsNotNull((Object)p, (String)"p");
        Intrinsics.checkParameterIsNotNull((Object)v, (String)"v");
        Intrinsics.checkParameterIsNotNull((Object)w, (String)"w");
        double l2 = this.distanceSq(v, w);
        if (l2 == 0.0) {
            return this.distanceSq(p, v);
        }
        return this.distanceSq(p, this.lerp(v, w, RangesKt.coerceAtLeast((double)RangesKt.coerceAtMost((double)(((p[0] - v[0]) * (w[0] - v[0]) + (p[1] - v[1]) * (w[1] - v[1])) / l2), (double)1.0), (double)0.0)));
    }

    public final boolean inRange(float $this$inRange, float base, float range) {
        float f = base - range;
        float f2 = base + range;
        float f3 = $this$inRange;
        return f3 >= f && f3 <= f2;
    }

    public final boolean inRange(int $this$inRange, int base, int range) {
        int n = $this$inRange;
        return base - range <= n && base + range >= n;
    }

    public final boolean inRange(double $this$inRange, double base, double range) {
        double d = base - range;
        double d2 = base + range;
        double d3 = $this$inRange;
        return d3 >= d && d3 <= d2;
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @NotNull
    public static final Double[] calcCurvePoint(@NotNull Double[][] points, double t2) {
        Double[] doubleArray;
        Intrinsics.checkParameterIsNotNull((Object)points, (String)"points");
        int n = 0;
        List cpoints = new ArrayList();
        n = 0;
        int n2 = ((Object[])points).length - 1;
        while (n < n2) {
            void i;
            cpoints.add(INSTANCE.lerp(points[i], points[i + true], t2));
            ++i;
        }
        if (cpoints.size() == 1) {
            doubleArray = (Double[])cpoints.get(0);
        } else {
            Collection $this$toTypedArray$iv = cpoints;
            boolean $i$f$toTypedArray = false;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            T[] TArray = thisCollection$iv.toArray((T[])new Double[0][]);
            if (TArray == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            doubleArray = MathUtils.calcCurvePoint((Double[][])TArray, t2);
        }
        return doubleArray;
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @NotNull
    public static final Double[][] getPointsOnCurve(@NotNull Double[][] points, int num) {
        Intrinsics.checkParameterIsNotNull((Object)points, (String)"points");
        int n = 0;
        List cpoints = new ArrayList();
        n = 0;
        int n2 = num;
        while (n < n2) {
            void i;
            double t2 = (double)i / ((double)num - 1.0);
            cpoints.add(MathUtils.calcCurvePoint(points, t2));
            ++i;
        }
        Collection $this$toTypedArray$iv = cpoints;
        boolean $i$f$toTypedArray = false;
        Collection thisCollection$iv = $this$toTypedArray$iv;
        T[] TArray = thisCollection$iv.toArray((T[])new Double[0][]);
        if (TArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (Double[][])TArray;
    }

    public final double toRadians(double $this$toRadians) {
        return $this$toRadians * (Math.PI / 180);
    }

    public final double toDegrees(double $this$toDegrees) {
        return $this$toDegrees * 57.29577951308232;
    }

    public final float gaussian(int x, float sigma) {
        float s = sigma * sigma * (float)2;
        float f = (float)Math.PI * s;
        float f2 = 1.0f;
        boolean bl = false;
        float f3 = (float)Math.sqrt(f);
        f = (float)(-(x * x)) / s;
        f2 /= f3;
        bl = false;
        f3 = (float)Math.exp(f);
        return f2 * f3;
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @JvmOverloads
    @NotNull
    public static final Double[][] simplifyPoints(@NotNull Double[][] points, double epsilon, int start, int end, @NotNull List<Double[]> outPoints) {
        Intrinsics.checkParameterIsNotNull((Object)points, (String)"points");
        Intrinsics.checkParameterIsNotNull(outPoints, (String)"outPoints");
        Double[] s = points[start];
        Double[] e = points[end - 1];
        double maxDistSq = 0.0;
        int maxNdx = 1;
        int n = start + 1;
        int n2 = end - 1;
        while (n < n2) {
            void i;
            double distSq = INSTANCE.distanceToSegmentSq(points[i], s, e);
            if (distSq > maxDistSq) {
                maxDistSq = distSq;
                maxNdx = i;
            }
            ++i;
        }
        if (Math.sqrt(maxDistSq) > epsilon) {
            MathUtils.simplifyPoints(points, epsilon, start, maxNdx + 1, outPoints);
            MathUtils.simplifyPoints(points, epsilon, maxNdx, end, outPoints);
        } else {
            outPoints.add(s);
            outPoints.add(e);
        }
        Collection $this$toTypedArray$iv = outPoints;
        boolean $i$f$toTypedArray = false;
        Collection thisCollection$iv = $this$toTypedArray$iv;
        T[] TArray = thisCollection$iv.toArray((T[])new Double[0][]);
        if (TArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (Double[][])TArray;
    }

    public static /* synthetic */ Double[][] simplifyPoints$default(Double[][] doubleArray, double d, int n, int n2, List list, int n3, Object object) {
        if ((n3 & 4) != 0) {
            n = 0;
        }
        if ((n3 & 8) != 0) {
            n2 = ((Object[])doubleArray).length;
        }
        if ((n3 & 0x10) != 0) {
            boolean bl = false;
            list = new ArrayList();
        }
        return MathUtils.simplifyPoints(doubleArray, d, n, n2, list);
    }

    @JvmStatic
    @JvmOverloads
    @NotNull
    public static final Double[][] simplifyPoints(@NotNull Double[][] points, double epsilon, int start, int end) {
        return MathUtils.simplifyPoints$default(points, epsilon, start, end, null, 16, null);
    }

    @JvmStatic
    @JvmOverloads
    @NotNull
    public static final Double[][] simplifyPoints(@NotNull Double[][] points, double epsilon, int start) {
        return MathUtils.simplifyPoints$default(points, epsilon, start, 0, null, 24, null);
    }

    /*
     * Exception decompiling
     */
    @JvmStatic
    @JvmOverloads
    @NotNull
    public static final Double[][] simplifyPoints(@NotNull Double[][] points, double epsilon) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl4 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
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

    private MathUtils() {
    }

    static {
        MathUtils mathUtils;
        INSTANCE = mathUtils = new MathUtils();
    }
}

