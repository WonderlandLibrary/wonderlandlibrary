/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CubicApproximator {
    private float accuracy;
    private float maxCubicSize;

    public CubicApproximator(float f, float f2) {
        this.accuracy = f;
        this.maxCubicSize = f2;
    }

    public void setAccuracy(float f) {
        this.accuracy = f;
    }

    public float getAccuracy() {
        return this.accuracy;
    }

    public void setMaxCubicSize(float f) {
        this.maxCubicSize = f;
    }

    public float getMaxCubicSize() {
        return this.maxCubicSize;
    }

    public float approximate(List<QuadCurve2D> list, List<CubicCurve2D> list2, CubicCurve2D cubicCurve2D) {
        float f = CubicApproximator.getApproxError(cubicCurve2D);
        if (f < this.accuracy) {
            list2.add(cubicCurve2D);
            list.add(this.approximate(cubicCurve2D));
            return f;
        }
        this.SplitCubic(list2, new float[]{cubicCurve2D.x1, cubicCurve2D.y1, cubicCurve2D.ctrlx1, cubicCurve2D.ctrly1, cubicCurve2D.ctrlx2, cubicCurve2D.ctrly2, cubicCurve2D.x2, cubicCurve2D.y2});
        return this.approximate(list2, list);
    }

    public float approximate(List<QuadCurve2D> list, CubicCurve2D cubicCurve2D) {
        ArrayList<CubicCurve2D> arrayList = new ArrayList<CubicCurve2D>();
        return this.approximate(list, arrayList, cubicCurve2D);
    }

    private QuadCurve2D approximate(CubicCurve2D cubicCurve2D) {
        return new QuadCurve2D(cubicCurve2D.x1, cubicCurve2D.y1, (3.0f * cubicCurve2D.ctrlx1 - cubicCurve2D.x1 + 3.0f * cubicCurve2D.ctrlx2 - cubicCurve2D.x2) / 4.0f, (3.0f * cubicCurve2D.ctrly1 - cubicCurve2D.y1 + 3.0f * cubicCurve2D.ctrly2 - cubicCurve2D.y2) / 4.0f, cubicCurve2D.x2, cubicCurve2D.y2);
    }

    private float approximate(List<CubicCurve2D> list, List<QuadCurve2D> list2) {
        QuadCurve2D quadCurve2D = this.approximate(list.get(0));
        float f = CubicApproximator.compareCPs(list.get(0), CubicApproximator.elevate(quadCurve2D));
        list2.add(quadCurve2D);
        for (int i = 1; i < list.size(); ++i) {
            quadCurve2D = this.approximate(list.get(i));
            float f2 = CubicApproximator.compareCPs(list.get(i), CubicApproximator.elevate(quadCurve2D));
            if (f2 > f) {
                f = f2;
            }
            list2.add(quadCurve2D);
        }
        return f;
    }

    private static CubicCurve2D elevate(QuadCurve2D quadCurve2D) {
        return new CubicCurve2D(quadCurve2D.x1, quadCurve2D.y1, (quadCurve2D.x1 + 2.0f * quadCurve2D.ctrlx) / 3.0f, (quadCurve2D.y1 + 2.0f * quadCurve2D.ctrly) / 3.0f, (2.0f * quadCurve2D.ctrlx + quadCurve2D.x2) / 3.0f, (2.0f * quadCurve2D.ctrly + quadCurve2D.y2) / 3.0f, quadCurve2D.x2, quadCurve2D.y2);
    }

    private static float compare(CubicCurve2D cubicCurve2D, CubicCurve2D cubicCurve2D2) {
        float f;
        float f2 = Math.abs(cubicCurve2D.x1 - cubicCurve2D2.x1);
        if (f2 < (f = Math.abs(cubicCurve2D.y1 - cubicCurve2D2.y1))) {
            f2 = f;
        }
        if (f2 < (f = Math.abs(cubicCurve2D.ctrlx1 - cubicCurve2D2.ctrlx1))) {
            f2 = f;
        }
        if (f2 < (f = Math.abs(cubicCurve2D.ctrly1 - cubicCurve2D2.ctrly1))) {
            f2 = f;
        }
        if (f2 < (f = Math.abs(cubicCurve2D.ctrlx2 - cubicCurve2D2.ctrlx2))) {
            f2 = f;
        }
        if (f2 < (f = Math.abs(cubicCurve2D.ctrly2 - cubicCurve2D2.ctrly2))) {
            f2 = f;
        }
        if (f2 < (f = Math.abs(cubicCurve2D.x2 - cubicCurve2D2.x2))) {
            f2 = f;
        }
        if (f2 < (f = Math.abs(cubicCurve2D.y2 - cubicCurve2D2.y2))) {
            f2 = f;
        }
        return f2;
    }

    private static float getApproxError(float[] arrf) {
        float f = (-3.0f * arrf[2] + arrf[0] + 3.0f * arrf[4] - arrf[6]) / 6.0f;
        float f2 = (-3.0f * arrf[3] + arrf[1] + 3.0f * arrf[5] - arrf[7]) / 6.0f;
        if (f < f2) {
            f = f2;
        }
        if (f < (f2 = (3.0f * arrf[2] - arrf[0] - 3.0f * arrf[4] + arrf[6]) / 6.0f)) {
            f = f2;
        }
        if (f < (f2 = (3.0f * arrf[3] - arrf[1] - 3.0f * arrf[5] + arrf[7]) / 6.0f)) {
            f = f2;
        }
        return f;
    }

    public static float getApproxError(CubicCurve2D cubicCurve2D) {
        return CubicApproximator.getApproxError(new float[]{cubicCurve2D.x1, cubicCurve2D.y1, cubicCurve2D.ctrlx1, cubicCurve2D.ctrly1, cubicCurve2D.ctrlx2, cubicCurve2D.ctrly2, cubicCurve2D.x2, cubicCurve2D.y2});
    }

    private static float compareCPs(CubicCurve2D cubicCurve2D, CubicCurve2D cubicCurve2D2) {
        float f;
        float f2 = Math.abs(cubicCurve2D.ctrlx1 - cubicCurve2D2.ctrlx1);
        if (f2 < (f = Math.abs(cubicCurve2D.ctrly1 - cubicCurve2D2.ctrly1))) {
            f2 = f;
        }
        if (f2 < (f = Math.abs(cubicCurve2D.ctrlx2 - cubicCurve2D2.ctrlx2))) {
            f2 = f;
        }
        if (f2 < (f = Math.abs(cubicCurve2D.ctrly2 - cubicCurve2D2.ctrly2))) {
            f2 = f;
        }
        return f2;
    }

    private void ProcessMonotonicCubic(List<CubicCurve2D> list, float[] arrf) {
        float f;
        float f2;
        float[] arrf2 = new float[8];
        float f3 = f2 = arrf[0];
        float f4 = f = arrf[1];
        for (int i = 2; i < 8; i += 2) {
            f3 = f3 > arrf[i] ? arrf[i] : f3;
            f2 = f2 < arrf[i] ? arrf[i] : f2;
            f4 = f4 > arrf[i + 1] ? arrf[i + 1] : f4;
            f = f < arrf[i + 1] ? arrf[i + 1] : f;
        }
        if (f2 - f3 > this.maxCubicSize || f - f4 > this.maxCubicSize || CubicApproximator.getApproxError(arrf) > this.accuracy) {
            arrf2[6] = arrf[6];
            arrf2[7] = arrf[7];
            arrf2[4] = (arrf[4] + arrf[6]) / 2.0f;
            arrf2[5] = (arrf[5] + arrf[7]) / 2.0f;
            float f5 = (arrf[2] + arrf[4]) / 2.0f;
            float f6 = (arrf[3] + arrf[5]) / 2.0f;
            arrf2[2] = (f5 + arrf2[4]) / 2.0f;
            arrf2[3] = (f6 + arrf2[5]) / 2.0f;
            arrf[2] = (arrf[0] + arrf[2]) / 2.0f;
            arrf[3] = (arrf[1] + arrf[3]) / 2.0f;
            arrf[4] = (arrf[2] + f5) / 2.0f;
            arrf[5] = (arrf[3] + f6) / 2.0f;
            arrf[6] = arrf2[0] = (arrf[4] + arrf2[2]) / 2.0f;
            arrf[7] = arrf2[1] = (arrf[5] + arrf2[3]) / 2.0f;
            this.ProcessMonotonicCubic(list, arrf);
            this.ProcessMonotonicCubic(list, arrf2);
        } else {
            list.add(new CubicCurve2D(arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5], arrf[6], arrf[7]));
        }
    }

    public void SplitCubic(List<CubicCurve2D> list, float[] arrf) {
        int n;
        int n2;
        float[] arrf2 = new float[4];
        float[] arrf3 = new float[3];
        float[] arrf4 = new float[2];
        int n3 = 0;
        if ((arrf[0] > arrf[2] || arrf[2] > arrf[4] || arrf[4] > arrf[6]) && (arrf[0] < arrf[2] || arrf[2] < arrf[4] || arrf[4] < arrf[6])) {
            arrf3[2] = -arrf[0] + 3.0f * arrf[2] - 3.0f * arrf[4] + arrf[6];
            arrf3[1] = 2.0f * (arrf[0] - 2.0f * arrf[2] + arrf[4]);
            arrf3[0] = -arrf[0] + arrf[2];
            n2 = QuadCurve2D.solveQuadratic(arrf3, arrf4);
            for (n = 0; n < n2; ++n) {
                if (!(arrf4[n] > 0.0f) || !(arrf4[n] < 1.0f)) continue;
                arrf2[n3++] = arrf4[n];
            }
        }
        if ((arrf[1] > arrf[3] || arrf[3] > arrf[5] || arrf[5] > arrf[7]) && (arrf[1] < arrf[3] || arrf[3] < arrf[5] || arrf[5] < arrf[7])) {
            arrf3[2] = -arrf[1] + 3.0f * arrf[3] - 3.0f * arrf[5] + arrf[7];
            arrf3[1] = 2.0f * (arrf[1] - 2.0f * arrf[3] + arrf[5]);
            arrf3[0] = -arrf[1] + arrf[3];
            n2 = QuadCurve2D.solveQuadratic(arrf3, arrf4);
            for (n = 0; n < n2; ++n) {
                if (!(arrf4[n] > 0.0f) || !(arrf4[n] < 1.0f)) continue;
                arrf2[n3++] = arrf4[n];
            }
        }
        if (n3 > 0) {
            Arrays.sort(arrf2, 0, n3);
            this.ProcessFirstMonotonicPartOfCubic(list, arrf, arrf2[0]);
            for (n2 = 1; n2 < n3; ++n2) {
                float f = arrf2[n2] - arrf2[n2 - 1];
                if (!(f > 0.0f)) continue;
                this.ProcessFirstMonotonicPartOfCubic(list, arrf, f / (1.0f - arrf2[n2 - 1]));
            }
        }
        this.ProcessMonotonicCubic(list, arrf);
    }

    private void ProcessFirstMonotonicPartOfCubic(List<CubicCurve2D> list, float[] arrf, float f) {
        float[] arrf2 = new float[8];
        arrf2[0] = arrf[0];
        arrf2[1] = arrf[1];
        float f2 = arrf[2] + f * (arrf[4] - arrf[2]);
        float f3 = arrf[3] + f * (arrf[5] - arrf[3]);
        arrf2[2] = arrf[0] + f * (arrf[2] - arrf[0]);
        arrf2[3] = arrf[1] + f * (arrf[3] - arrf[1]);
        arrf2[4] = arrf2[2] + f * (f2 - arrf2[2]);
        arrf2[5] = arrf2[3] + f * (f3 - arrf2[3]);
        arrf[4] = arrf[4] + f * (arrf[6] - arrf[4]);
        arrf[5] = arrf[5] + f * (arrf[7] - arrf[5]);
        arrf[2] = f2 + f * (arrf[4] - f2);
        arrf[3] = f3 + f * (arrf[5] - f3);
        arrf[0] = arrf2[6] = arrf2[4] + f * (arrf[2] - arrf2[4]);
        arrf[1] = arrf2[7] = arrf2[5] + f * (arrf[3] - arrf2[5]);
        this.ProcessMonotonicCubic(list, arrf2);
    }
}

