/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.CullFace
 */
package com.sun.prism;

import com.sun.prism.Graphics;
import com.sun.prism.GraphicsResource;
import com.sun.prism.Material;
import javafx.scene.shape.CullFace;

public interface MeshView
extends GraphicsResource {
    public static final int CULL_NONE = CullFace.NONE.ordinal();
    public static final int CULL_BACK = CullFace.BACK.ordinal();
    public static final int CULL_FRONT = CullFace.FRONT.ordinal();

    public void setCullingMode(int var1);

    public void setMaterial(Material var1);

    public void setWireframe(boolean var1);

    public void setAmbientLight(float var1, float var2, float var3);

    public void setLight(int var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18);

    public void render(Graphics var1);

    public boolean isValid();
}

