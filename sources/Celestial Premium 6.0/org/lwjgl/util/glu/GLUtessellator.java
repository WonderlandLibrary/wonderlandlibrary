/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.glu;

import org.lwjgl.util.glu.GLUtessellatorCallback;

public interface GLUtessellator {
    public void gluDeleteTess();

    public void gluTessProperty(int var1, double var2);

    public void gluGetTessProperty(int var1, double[] var2, int var3);

    public void gluTessNormal(double var1, double var3, double var5);

    public void gluTessCallback(int var1, GLUtessellatorCallback var2);

    public void gluTessVertex(double[] var1, int var2, Object var3);

    public void gluTessBeginPolygon(Object var1);

    public void gluTessBeginContour();

    public void gluTessEndContour();

    public void gluTessEndPolygon();

    public void gluBeginPolygon();

    public void gluNextContour(int var1);

    public void gluEndPolygon();
}

