/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.util;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public class GLUtil {
    private static Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();

    public static void revertGLCap(int n) {
        Boolean bl = glCapMap.get(n);
        if (bl != null) {
            if (bl.booleanValue()) {
                GL11.glEnable((int)n);
            } else {
                GL11.glDisable((int)n);
            }
        }
    }

    public static void glDisable(int n) {
        GLUtil.setGLCap(n, false);
    }

    public static void glEnable(int n) {
        GLUtil.setGLCap(n, true);
    }

    public static void revertAllCaps() {
        for (int n : glCapMap.keySet()) {
            GLUtil.revertGLCap(n);
        }
    }

    public static void setGLCap(int n, boolean bl) {
        glCapMap.put(n, GL11.glGetBoolean((int)n));
        if (bl) {
            GL11.glEnable((int)n);
        } else {
            GL11.glDisable((int)n);
        }
    }
}

