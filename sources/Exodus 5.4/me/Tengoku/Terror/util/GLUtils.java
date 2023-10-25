/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.util;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;

public final class GLUtils {
    private static final List depth = new ArrayList();

    public static void render() {
        GLUtils.render(514);
    }

    public static void endSmooth() {
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
        GL11.glEnable((int)2832);
    }

    public static void render(int n) {
        GL11.glDepthFunc((int)n);
        GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
    }

    public static double interpolate(double d, double d2, double d3) {
        return d2 + (d - d2) * d3;
    }

    public static void post() {
        GL11.glDepthFunc((int)((Integer)depth.get(0)));
        depth.remove(0);
    }

    public static void startSmooth() {
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glHint((int)3153, (int)4354);
    }

    public static void mask() {
        depth.add(0, GL11.glGetInteger((int)2932));
        GL11.glEnable((int)6145);
        GL11.glDepthMask((boolean)true);
        GL11.glDepthFunc((int)513);
        GL11.glColorMask((boolean)false, (boolean)false, (boolean)false, (boolean)true);
    }

    public static void pre() {
        if (depth.isEmpty()) {
            GL11.glClearDepth((double)1.0);
            GL11.glClear((int)256);
        }
    }
}

