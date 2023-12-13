package keystrokesmod.client.utils;

import org.lwjgl.opengl.GL11;

public class GuiCircleUtils {

    public static void drawCircle(int x, int y, int radius, int color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glColor4f((float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F,
                (float) (color & 255) / 255.0F, (float) (color >> 24 & 255) / 255.0F);

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for (int i = 0; i <= 360; i++) {
            double xPoint = x + Math.cos(Math.toRadians(i)) * radius;
            double yPoint = y + Math.sin(Math.toRadians(i)) * radius;
            GL11.glVertex2d(xPoint, yPoint);
        }
        GL11.glEnd();

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
}
