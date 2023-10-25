/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.ps;

import com.sun.prism.GraphicsResource;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface Shader
extends GraphicsResource {
    public void enable();

    public void disable();

    public boolean isValid();

    public void setConstant(String var1, int var2);

    public void setConstant(String var1, int var2, int var3);

    public void setConstant(String var1, int var2, int var3, int var4);

    public void setConstant(String var1, int var2, int var3, int var4, int var5);

    public void setConstants(String var1, IntBuffer var2, int var3, int var4);

    public void setConstant(String var1, float var2);

    public void setConstant(String var1, float var2, float var3);

    public void setConstant(String var1, float var2, float var3, float var4);

    public void setConstant(String var1, float var2, float var3, float var4, float var5);

    public void setConstants(String var1, FloatBuffer var2, int var3, int var4);
}

