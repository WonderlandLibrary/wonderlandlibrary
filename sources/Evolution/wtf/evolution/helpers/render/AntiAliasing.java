package wtf.evolution.helpers.render;

import org.lwjgl.opengl.GL11;

public class AntiAliasing {

    public static void hook(boolean line, boolean polygon, boolean point) {
        if (line) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        }
        if (polygon) {
            GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
            GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        }
        if (point) {
            GL11.glEnable(GL11.GL_POINT_SMOOTH);
            GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, GL11.GL_NICEST);
        }
    }

    public static void unhook(boolean line, boolean polygon, boolean point) {
        if (line) {
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }
        if (polygon) {
            GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
            GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        }
        if (point) {
            GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, GL11.GL_DONT_CARE);
            GL11.glDisable(GL11.GL_POINT_SMOOTH);
        }
    }

}
