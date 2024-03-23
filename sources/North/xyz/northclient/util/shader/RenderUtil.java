package xyz.northclient.util.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import xyz.northclient.theme.ColorUtil;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static xyz.northclient.util.MoveUtil.mc;

public class RenderUtil {
    public static void push() {
        GL11.glPushMatrix();
    }

    public static void pop() {
        GL11.glPopMatrix();
    }

    public static void enable(int glCap) {
        GL11.glEnable(glCap);
    }

    public static void disable(int glCap) {
        GL11.glDisable(glCap);
    }

    public static void use(int glCap, Runnable runnable) {
        GL11.glBegin(glCap);
        runnable.run();
        GL11.glEnd();
    }

    public static void vertex(float x, float y) {
        GL11.glVertex2f(x, y);
    }

    public static void vertex(double x, double y) {
        GL11.glVertex2d(x, y);
    }

    public static void vertex(int x, int y) {
        GL11.glVertex2i(x, y);
    }

    public static void blend(int one, int two) {
        GL11.glBlendFunc(one, two);
    }

    public static void lineWidth(float lineWidth) {
        GL11.glLineWidth(lineWidth);
    }

    public static void lineWidth(double lineWidth) {
        GL11.glLineWidth((float) lineWidth);
    }

    public static void color(Color color) {
        GL11.glColor4f(
                color.getRed() / 255.0F,
                color.getGreen() / 255.0F,
                color.getBlue() / 255.0F,
                color.getAlpha() / 255.0F
        );
    }

    public static void color(int color) {
        GL11.glColor4f(
            (color >> 16 & 255) / 255.0F,
            (color >> 8 & 255) / 255.0F,
            (color & 255) / 255.0F,
            (color >> 24 & 255) / 255.0F
        );
    }

    public static void start() {
        push();

        enable(GL11.GL_BLEND);

        blend(
                GL11.GL_SRC_ALPHA,
                GL11.GL_ONE_MINUS_SRC_ALPHA
        );

        disable(GL11.GL_TEXTURE_2D);
        disable(GL11.GL_CULL_FACE);

        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }

    public static void stop() {
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();

        enable(GL11.GL_CULL_FACE);
        enable(GL11.GL_TEXTURE_2D);

        disable(GL11.GL_BLEND);

        color(Color.WHITE);

        pop();
    }

    public static void rectangle(double x, double y, double width, double height, double line, boolean filled, Color color) {
        start();

        if (line > 0 && !filled)
            lineWidth(line);

        if (color != null)
            color(color);

        use(filled ? GL_QUADS : GL_LINE_LOOP, () -> {
            vertex(x, y);
            vertex(x + width, y);
            vertex(x + width, y + height);
            vertex(x, y + height);
        });

        stop();
    }

    public static void rectangle(double x, double y, double width, double height, double line, Color color) {
        rectangle(x, y, width, height, line, false, color);
    }

    public static void rectangle(double x, double y, double width, double height, Color color) {
        rectangle(x, y, width, height, 0, color);
    }

    public static void rectangle(double x, double y, double width, double height) {
        rectangle(x, y, width, height, null);
    }

    public static void polygon(double x, double y, double diameter, int sides, double line, boolean filled, Color color) {
        start();

        if (++line > 0 && !filled)
            lineWidth(line);

        if (color != null)
            color(color);

        use(filled ? GL_TRIANGLE_FAN : GL_LINE_LOOP, () -> {
            for (int i = 0; i < sides; ++i) {
                double angle = i * (Math.PI * 2) / 360;

                vertex(
                        x + (diameter * Math.cos(angle)) + diameter,
                        y + (diameter * Math.sin(angle)) + diameter
                );
            }
        });

        stop();
    }

    public static void polygon(double x, double y, double diameter, int sides, double line, Color color) {
        polygon(x, y, diameter, sides, line, false, color);
    }

    public static void polygon(double x, double y, double diameter, int sides, Color color) {
        polygon(x, y, diameter, sides, 0, color);
    }

    public static void polygon(double x, double y, double diameter, int sides) {
        polygon(x, y, diameter, sides, null);
    }

    public static void circle(double x, double y, double diameter, double line, boolean filled, Color color) {
        polygon(x, y, diameter, 180, line, filled, color);
    }

    public static void circle(double x, double y, double diameter, double line, Color color) {
        circle(x, y, diameter, line, false, color);
    }

    public static void circle(double x, double y, double diameter, Color color) {
        circle(x, y, diameter, 0, color);
    }

    public static void circle(double x, double y, double diameter) {
        circle(x, y, diameter, null);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        return createFrameBuffer(framebuffer, false);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer, boolean depth) {
        if (needsNewFramebuffer(framebuffer)) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(mc.displayWidth, mc.displayHeight, depth);
        }
        return framebuffer;
    }

    public static boolean needsNewFramebuffer(Framebuffer framebuffer) {
        return framebuffer == null || framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight;
    }


    public static double ticks = 0;
    public static long lastFrame = 0;
    public static void drawCircle(Entity entity, float partialTicks, double rad, int color, float alpha) {
        //from my friend okok
        ticks += .004 * (System.currentTimeMillis() - lastFrame);

        lastFrame = System.currentTimeMillis();

        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        GlStateManager.color(1, 1, 1, 1);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glShadeModel(GL_SMOOTH);
        GlStateManager.disableCull();

        final double x = interpolate(entity.lastTickPosX, entity.posX,  Minecraft.getMinecraft().timer.renderPartialTicks) - Minecraft.getMinecraft().getRenderManager().renderPosX;
        double y = interpolate(entity.lastTickPosY, entity.posY, Minecraft.getMinecraft().timer.renderPartialTicks) - Minecraft.getMinecraft().getRenderManager().renderPosY + Math.sin(ticks) + 1;
        final double z = interpolate(entity.lastTickPosZ, entity.posZ, Minecraft.getMinecraft().timer.renderPartialTicks) - Minecraft.getMinecraft().getRenderManager().renderPosZ;



        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth(10.5f);
        glBegin(GL_LINE_STRIP);
        GlStateManager.color(1, 1, 1, 1);
        color(ColorUtil.MainColor());
        for (int i = 0; i <= 180; i++) {
            glVertex3d(x - Math.sin(i * MathHelper.PI2 / 90) * rad, y, z + Math.cos(i * MathHelper.PI2 / 90) * rad);
        }
        glEnd();


        glShadeModel(GL_FLAT);
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        GlStateManager.enableCull();
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
        glColor4f(1f, 1f, 1f, 1f);
    }




    public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }
}
