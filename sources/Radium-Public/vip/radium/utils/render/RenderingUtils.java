package vip.radium.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import vip.radium.gui.font.FontRenderer;
import vip.radium.utils.MathUtils;
import vip.radium.utils.Wrapper;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public final class RenderingUtils {

    private static final double DOUBLE_PI = Math.PI * 2.0D;

    private static final Frustum FRUSTUM = new Frustum();

    private static int lastScaledWidth;
    private static int lastScaledHeight;
    private static int lastGuiScale;
    private static ScaledResolution scaledResolution;

    private static int lastWidth;
    private static int lastHeight;
    private static LockedResolution lockedResolution;

    private RenderingUtils() {
    }

    public static boolean isBBInFrustum(AxisAlignedBB aabb) {
        EntityPlayerSP player = Wrapper.getPlayer();
        FRUSTUM.setPosition(player.posX, player.posY, player.posZ);
        return FRUSTUM.isBoundingBoxInFrustum(aabb);
    }

    public static void drawGradientRect(double left, double top, double right, double bottom,
                                        boolean sideways,
                                        int startColor, int endColor) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OGLUtils.enableBlending();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);

        OGLUtils.color(startColor);
        if (sideways) {
            GL11.glVertex2d(left, top);
            GL11.glVertex2d(left, bottom);
            OGLUtils.color(endColor);
            GL11.glVertex2d(right, bottom);
            GL11.glVertex2d(right, top);
        } else {
            GL11.glVertex2d(left, top);
            OGLUtils.color(endColor);
            GL11.glVertex2d(left, bottom);
            GL11.glVertex2d(right, bottom);
            OGLUtils.color(startColor);
            GL11.glVertex2d(right, top);
        }

        GL11.glEnd();
        GL11.glDisable(GL_BLEND);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static LockedResolution getLockedResolution() {
        int width = Display.getWidth();
        int height = Display.getHeight();

        if (width != lastWidth ||
            height != lastHeight) {
            lastWidth = width;
            lastHeight = height;
            return lockedResolution = new LockedResolution(width / LockedResolution.SCALE_FACTOR, height / LockedResolution.SCALE_FACTOR);
        }

        return lockedResolution;
    }

    public static ScaledResolution getScaledResolution() {
        int displayWidth = Display.getWidth();
        int displayHeight = Display.getHeight();
        int guiScale = Wrapper.getGameSettings().guiScale;

        if (displayWidth != lastScaledWidth ||
            displayHeight != lastScaledHeight ||
            guiScale != lastGuiScale) {
            lastScaledWidth = displayWidth;
            lastScaledHeight = displayHeight;
            lastGuiScale = guiScale;
            return scaledResolution = new ScaledResolution(Wrapper.getMinecraft());
        }

        return scaledResolution;
    }

    public static int getColorFromPercentage(float percentage) {
        return Color.HSBtoRGB(Math.min(1.0F, Math.max(0.0F, percentage)) / 3, 0.9F, 0.9F);
    }

    public static int getRainbowFromEntity(long currentMillis, int speed, int offset, boolean invert, float alpha) {
        float time = ((currentMillis + (offset * 300L)) % speed) / (float) speed;
        int rainbow = Color.HSBtoRGB(invert ? 1.0F - time : time, 0.9F, 0.9F);
        int r = (rainbow >> 16) & 0xFF;
        int g = (rainbow >> 8) & 0xFF;
        int b = rainbow & 0xFF;
        int a = (int) (alpha * 255.0F);
        return ((a & 0xFF) << 24) |
            ((r & 0xFF) << 16) |
            ((g & 0xFF) << 8) |
            (b & 0xFF);
    }

    public static int getRainbow(long currentMillis, int speed, int offset) {
        return getRainbow(currentMillis, speed, offset, 1.0F);
    }

    public static int getRainbow(long currentMillis, int speed, int offset, float alpha) {
        int rainbow = Color.HSBtoRGB(1.0F - ((currentMillis + (offset * 100)) % speed) / (float) speed,
                                     0.9F, 0.9F);
        int r = (rainbow >> 16) & 0xFF;
        int g = (rainbow >> 8) & 0xFF;
        int b = rainbow & 0xFF;
        int a = (int) (alpha * 255.0F);
        return ((a & 0xFF) << 24) |
            ((r & 0xFF) << 16) |
            ((g & 0xFF) << 8) |
            (b & 0xFF);
    }

    public static void drawAndRotateArrow(float x, float y, float size, boolean rotate) {
        glPushMatrix();
        glTranslatef(x, y, 1.0F);
        OGLUtils.enableBlending();
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth(1.0F);
        glDisable(GL_TEXTURE_2D);
        glBegin(GL_TRIANGLES);
        if (rotate) {
            glVertex2f(size, size / 2);
            glVertex2f(size / 2, 0);
            glVertex2f(0, size / 2);
        } else {
            glVertex2f(0, 0);
            glVertex2f(size / 2, size / 2);
            glVertex2f(size, 0);
        }
        glEnd();
        glEnable(GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
    }

    public static double progressiveAnimation(double now, double desired, double speed) {
        double dif = Math.abs(now - desired);

        final int fps = Minecraft.getDebugFPS();

        if (dif > 0) {
            double animationSpeed = MathUtils.roundToDecimalPlace(Math.min(
                10.0D, Math.max(0.05D, (144.0D / fps) * (dif / 10) * speed)), 0.05D);

            if (dif != 0 && dif < animationSpeed)
                animationSpeed = dif;

            if (now < desired)
                return now + animationSpeed;
            else if (now > desired)
                return now - animationSpeed;
        }

        return now;
    }

    public static double linearAnimation(double now, double desired, double speed) {
        double dif = Math.abs(now - desired);

        final int fps = Minecraft.getDebugFPS();

        if (dif > 0) {
            double animationSpeed = MathUtils.roundToDecimalPlace(Math.min(
                10.0D, Math.max(0.005D, (144.0D / fps) * speed)), 0.005D);

            if (dif != 0 && dif < animationSpeed)
                animationSpeed = dif;

            if (now < desired)
                return now + animationSpeed;
            else if (now > desired)
                return now - animationSpeed;
        }

        return now;
    }

    public static int alphaComponent(int color, int alphaComp) {
        final int r = (color >> 16 & 0xFF);
        final int g = (color >> 8 & 0xFF);
        final int b = (color & 0xFF);

        return ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF) |
                ((alphaComp & 0xFF) << 24);
    }

    public static int darkerClamped(int color, float factor) {
        int r = (int) Math.max(0, Math.min(255, (color >> 16 & 0xFF) * factor));
        int g = (int) Math.max(0, Math.min(255, (color >> 8 & 0xFF) * factor));
        int b = (int) Math.max(0, Math.min(255, (color & 0xFF) * factor));
        int a = Math.max(0, Math.min(255, color >> 24 & 0xFF));


        return ((r & 0xFF) << 16) |
            ((g & 0xFF) << 8) |
            (b & 0xFF) |
            ((a & 0xFF) << 24);
    }

    public static int darker(final int color, final float factor) {
        final int r = (int) ((color >> 16 & 0xFF) * factor);
        final int g = (int) ((color >> 8 & 0xFF) * factor);
        final int b = (int) ((color & 0xFF) * factor);
        final int a = color >> 24 & 0xFF;

        return ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF) |
                ((a & 0xFF) << 24);
    }


    public static int darker(int color) {
        return darker(color, 0.6F);
    }

    public static void drawOutlinedString(FontRenderer fr, String s, float x, float y, int color, int outlineColor) {
        fr.drawString(s, x - 0.5F, y, outlineColor);
        fr.drawString(s, x, y - 0.5F, outlineColor);
        fr.drawString(s, x + 0.5F, y, outlineColor);
        fr.drawString(s, x, y + 0.5F, outlineColor);
        fr.drawString(s, x, y, color);
    }

    public static void drawImage(float x,
                                 float y,
                                 float width,
                                 float height,
                                 float r,
                                 float g,
                                 float b,
                                 ResourceLocation image) {
        Wrapper.getMinecraft().getTextureManager().bindTexture(image);
        float f = 1.0F / width;
        float f1 = 1.0F / height;
        glColor4f(r, g, b, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D)
            .tex(0.0D, height * f1)
            .endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D)
            .tex(width * f, height * f1)
            .endVertex();
        worldrenderer.pos(x + width, y, 0.0D)
            .tex(width * f, 0.0D)
            .endVertex();
        worldrenderer.pos(x, y, 0.0D)
            .tex(0.0D, 0.0D)
            .endVertex();
        tessellator.draw();
    }

    public static int fadeBetween(int startColor, int endColor, float progress) {
        if (progress > 1)
            progress = 1 - progress % 1;

        return fadeTo(startColor, endColor, progress);
    }

    public static int fadeBetween(int startColor, int endColor) {
        return fadeBetween(startColor, endColor, (System.currentTimeMillis() % 2000) / 1000.0F);
    }

    public static int fadeTo(int startColor, int endColor, float progress) {
        float invert = 1.0F - progress;
        int r = (int) ((startColor >> 16 & 0xFF) * invert +
                (endColor >> 16 & 0xFF) * progress);
        int g = (int) ((startColor >> 8 & 0xFF) * invert +
                (endColor >> 8 & 0xFF) * progress);
        int b = (int) ((startColor & 0xFF) * invert +
                (endColor & 0xFF) * progress);
        int a = (int) ((startColor >> 24 & 0xFF) * invert +
                (endColor >> 24 & 0xFF) * progress);
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }

    public static void drawLoop(float x,
                                float y,
                                double radius,
                                int points,
                                float width,
                                int color,
                                boolean filled) {
        glDisable(GL_TEXTURE_2D);
        glLineWidth(width);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        OGLUtils.color(color);
        int smooth = filled ? GL_POLYGON_SMOOTH : GL_LINE_SMOOTH;
        glEnable(smooth);
        glHint(filled ? GL_POLYGON_SMOOTH_HINT : GL_LINE_SMOOTH_HINT, GL_NICEST);
        glBegin(filled ? GL_TRIANGLE_FAN : GL_LINE_LOOP);
        for (int i = 0; i < points; i++) {
            if (filled) {
                final double cs = i * Math.PI / 180;
                final double ps = (i - 1) * Math.PI / 180;

                glVertex2d(x + Math.cos(ps) * radius, y + -Math.sin(ps) * radius);
                glVertex2d(x + Math.cos(cs) * radius, y + -Math.sin(cs) * radius);
                glVertex2d(x, y);
            } else {
                glVertex2d(x + radius * Math.cos(i * DOUBLE_PI / points),
                        y + radius * Math.sin(i * DOUBLE_PI / points));
            }
        }
        glEnd();
        glDisable(smooth);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
    }


//    public static void drawLinesAroundPlayer(Entity entity,
//                                             double radius,
//                                             float partialTicks,
//                                             int points,
//                                             boolean outline,
//                                             int color) {
//        glPushMatrix();
//        glDisable(GL_TEXTURE_2D);
//        glEnable(GL_LINE_SMOOTH);
//        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
//        glDisable(GL_DEPTH_TEST);
//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        glDisable(GL_DEPTH_TEST);
//        final double x = RenderingUtils.interpolate(entity.prevPosX, entity.posX, partialTicks) - RenderManager.viewerPosX;
//        final double y = RenderingUtils.interpolate(entity.prevPosY, entity.posY, partialTicks) - RenderManager.viewerPosY;
//        final double z = RenderingUtils.interpolate(entity.prevPosZ, entity.posZ, partialTicks) - RenderManager.viewerPosZ;
//
//        if (outline) {
//            glLineWidth(6.0F);
//            OGLUtils.color(0x80000000);
//
//            glBegin(GL_LINE_STRIP);
//            for (int i = 0; i <= points; i++)
//                glVertex3d(
//                        x + radius * Math.cos(i * DOUBLE_PI / points),
//                        y,
//                        z + radius * Math.sin(i * DOUBLE_PI / points));
//            glEnd();
//        }
//
//        glLineWidth(3.0F);
//        OGLUtils.color(color);
//
//        glBegin(GL_LINE_STRIP);
//        for (int i = 0; i <= points; i++)
//            glVertex3d(
//                    x + radius * Math.cos(i * DOUBLE_PI / points),
//                    y,
//                    z + radius * Math.sin(i * DOUBLE_PI / points));
//        glEnd();
//        glDepthMask(true);
//        glDisable(GL_BLEND);
//        glEnable(GL_DEPTH_TEST);
//        glDisable(GL_LINE_SMOOTH);
//        glEnable(GL_DEPTH_TEST);
//        glEnable(GL_TEXTURE_2D);
//        glPopMatrix();
//    }

    public static double interpolate(double old,
                                     double now,
                                     float partialTicks) {
        return old + (now - old) * partialTicks;
    }

    public static float interpolate(float old,
                                    float now,
                                    float partialTicks) {

        return old + (now - old) * partialTicks;
    }

    public static void drawGuiBackground(int width, int height) {
        Gui.drawRect(0, 0, width, height, 0xFF282C34);
    }
}
