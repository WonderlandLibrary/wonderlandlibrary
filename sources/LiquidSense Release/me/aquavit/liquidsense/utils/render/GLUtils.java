package me.aquavit.liquidsense.utils.render;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public class GLUtils {
    private static Map glCapMap = new HashMap();

    public static void setGLCap(int cap, boolean flag) {
        glCapMap.put(cap, GL11.glGetBoolean(cap));
        if (flag) {
            GL11.glEnable(cap);
        } else {
            GL11.glDisable(cap);
        }

    }

    public static void revertGLCap(int cap) {
        Boolean origCap = (Boolean)glCapMap.get(cap);
        if (origCap != null) {
            if (origCap) {
                GL11.glEnable(cap);
            } else {
                GL11.glDisable(cap);
            }
        }

    }

    public static void glEnable(int cap) {
        setGLCap(cap, true);
    }

    public static void glDisable(int cap) {
        setGLCap(cap, false);
    }

    public static void revertAllCaps() {
        Iterator localIterator = glCapMap.keySet().iterator();

        while(localIterator.hasNext()) {
            int cap = (Integer)localIterator.next();
            revertGLCap(cap);
        }

    }
}
