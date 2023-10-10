/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.geom;

import org.newdawn.slick.geom.Triangulator;

public class OverTriangulator
implements Triangulator {
    private float[][] triangles;

    public OverTriangulator(Triangulator tris) {
        this.triangles = new float[tris.getTriangleCount() * 6 * 3][2];
        int tcount = 0;
        for (int i2 = 0; i2 < tris.getTriangleCount(); ++i2) {
            float[] pt2;
            float[] pt1;
            int p2;
            float cx = 0.0f;
            float cy = 0.0f;
            for (p2 = 0; p2 < 3; ++p2) {
                float[] pt = tris.getTrianglePoint(i2, p2);
                cx += pt[0];
                cy += pt[1];
            }
            cx /= 3.0f;
            cy /= 3.0f;
            for (p2 = 0; p2 < 3; ++p2) {
                int n2 = p2 + 1;
                if (n2 > 2) {
                    n2 = 0;
                }
                pt1 = tris.getTrianglePoint(i2, p2);
                pt2 = tris.getTrianglePoint(i2, n2);
                pt1[0] = (pt1[0] + pt2[0]) / 2.0f;
                pt1[1] = (pt1[1] + pt2[1]) / 2.0f;
                this.triangles[tcount * 3 + 0][0] = cx;
                this.triangles[tcount * 3 + 0][1] = cy;
                this.triangles[tcount * 3 + 1][0] = pt1[0];
                this.triangles[tcount * 3 + 1][1] = pt1[1];
                this.triangles[tcount * 3 + 2][0] = pt2[0];
                this.triangles[tcount * 3 + 2][1] = pt2[1];
                ++tcount;
            }
            for (p2 = 0; p2 < 3; ++p2) {
                int n3 = p2 + 1;
                if (n3 > 2) {
                    n3 = 0;
                }
                pt1 = tris.getTrianglePoint(i2, p2);
                pt2 = tris.getTrianglePoint(i2, n3);
                pt2[0] = (pt1[0] + pt2[0]) / 2.0f;
                pt2[1] = (pt1[1] + pt2[1]) / 2.0f;
                this.triangles[tcount * 3 + 0][0] = cx;
                this.triangles[tcount * 3 + 0][1] = cy;
                this.triangles[tcount * 3 + 1][0] = pt1[0];
                this.triangles[tcount * 3 + 1][1] = pt1[1];
                this.triangles[tcount * 3 + 2][0] = pt2[0];
                this.triangles[tcount * 3 + 2][1] = pt2[1];
                ++tcount;
            }
        }
    }

    public void addPolyPoint(float x2, float y2) {
    }

    public int getTriangleCount() {
        return this.triangles.length / 3;
    }

    public float[] getTrianglePoint(int tri, int i2) {
        float[] pt = this.triangles[tri * 3 + i2];
        return new float[]{pt[0], pt[1]};
    }

    public void startHole() {
    }

    public boolean triangulate() {
        return true;
    }
}

