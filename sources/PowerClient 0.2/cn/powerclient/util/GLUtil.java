/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.lwjgl.opengl.GL11;

public class GLUtil {
    private static final Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();

    public static void setGLCap(int cap, boolean flag) {
        glCapMap.put(cap, GL11.glGetBoolean(cap));
        if (flag) {
            GL11.glEnable(cap);
        } else {
            GL11.glDisable(cap);
        }
    }

    private static void revertGLCap(int cap) {
        Boolean origCap = glCapMap.get(cap);
        if (origCap != null) {
            if (origCap.booleanValue()) {
                GL11.glEnable(cap);
            } else {
                GL11.glDisable(cap);
            }
        }
    }

    public static void glEnable(int cap) {
        GLUtil.setGLCap(cap, true);
    }

    public static void glDisable(int cap) {
        GLUtil.setGLCap(cap, false);
    }

    public static void revertAllCaps() {
        for (Integer cap : glCapMap.keySet()) {
            GLUtil.revertGLCap(cap);
        }
    }
}

