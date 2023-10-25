/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.glu;

public interface GLUtessellatorCallback {
    public void begin(int var1);

    public void beginData(int var1, Object var2);

    public void edgeFlag(boolean var1);

    public void edgeFlagData(boolean var1, Object var2);

    public void vertex(Object var1);

    public void vertexData(Object var1, Object var2);

    public void end();

    public void endData(Object var1);

    public void combine(double[] var1, Object[] var2, float[] var3, Object[] var4);

    public void combineData(double[] var1, Object[] var2, float[] var3, Object[] var4, Object var5);

    public void error(int var1);

    public void errorData(int var1, Object var2);
}

