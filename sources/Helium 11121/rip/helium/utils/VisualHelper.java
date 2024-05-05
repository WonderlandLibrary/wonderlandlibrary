package rip.helium.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import java.awt.*;

import net.minecraft.client.renderer.culling.Frustum;
import org.lwjgl.opengl.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;

public class VisualHelper
{
    private static final Frustum frustum;

    public static double interp(final double newPos, final double oldPos) {
        return oldPos + (newPos - oldPos) * Minecraft.getMinecraft().timer.renderPartialTicks;
    }

    public static double calculateSpeedByTime(final long delta, final double speed) {
        return delta * speed * 0.06;
    }

    public static void drawRect(final double x, final double y, final double width, final double height, final int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public static int withTransparency(final int rgb, final float alpha) {
        final float r = (rgb >> 16 & 0xFF) / 255.0f;
        final float g = (rgb >> 8 & 0xFF) / 255.0f;
        final float b = (rgb & 0xFF) / 255.0f;
        return new Color(r, g, b, alpha).getRGB();
    }

    public static int getRGBFromHex(final int hex) {
        return 0xFF000000 | hex;
    }

    public static void setGLConstants(final boolean disableDepth) {
        if (disableDepth) {
            GL11.glDisable(2929);
        }
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }

    public static void unsetGLConstants() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0, 1.0, 1.0);
    }

    public static void cuboid(final AxisAlignedBB aabb) {
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
        GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
        GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
        GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
        GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
        GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
        GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
        GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);
        GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
        GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
        GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);
        GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
        GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
        GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);
        GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
        GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
        GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
        GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
        GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
        GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
        GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
        GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
        GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
        GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static boolean isInFrustumView(final Entity ent) {
        final Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        VisualHelper.frustum.setPosition(interp(current.posX, current.lastTickPosX), interp(current.posY, current.lastTickPosY), interp(current.posZ, current.lastTickPosZ));
        return VisualHelper.frustum.isBoundingBoxInFrustum(ent.getEntityBoundingBox()) || ent.ignoreFrustumCheck;
    }

    static {
        frustum = new Frustum();
    }
}
