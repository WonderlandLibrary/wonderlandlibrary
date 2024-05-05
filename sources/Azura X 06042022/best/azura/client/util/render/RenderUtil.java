package best.azura.client.util.render;

import best.azura.client.impl.ui.Texture;
import best.azura.client.util.textures.JordanTextureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public enum RenderUtil {

    INSTANCE;

    private final Minecraft mc = Minecraft.getMinecraft();

    public void color(int color) {
        color(new Color(color, true));
    }

    public void color(Color color) {
        color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public void color(int red, int green, int blue) {
        color(red, green, blue, 255);
    }

    public void color(float red, float green, float blue, float alpha) {
        glColor4f(red / 255f, green / 255f, blue / 255f, alpha / 255f);
    }

    public float[] convertToRGBA(int color) {
        return new float[]{(float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, (float) (color >> 24 & 255) / 255.0F};
    }

    public void scaleFix(double scale) {
        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        double scaling = scale / scaleFactor;
        GlStateManager.scale(scaling, scaling, scaling);
    }

    public void invertScaleFix(double scale) {
        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        GlStateManager.scale(scaleFactor * scale, scaleFactor * scale, scaleFactor * scale);
    }

    public void renderBox(double x, double y, double z, double width, double height, Color color, boolean outline) {
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_TEXTURE_2D);
        quads(width, height, color, x, y, z);
        if (!outline) {
            return;
        }
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_TEXTURE_2D);
        glBegin(GL_LINE_LOOP);
        glVertex3d(x + width, y, z + width);
        glVertex3d(x + width, y + height, z + width);
        glEnd();
        glBegin(GL_LINE_LOOP);
        glVertex3d(x + width, y, z - width);
        glVertex3d(x + width, y + height, z - width);
        glEnd();
        glBegin(GL_LINE_LOOP);
        glVertex3d(x - width, y, z - width);
        glVertex3d(x - width, y + height, z - width);
        glEnd();
        glBegin(GL_LINE_LOOP);
        glVertex3d(x - width, y, z + width);
        glVertex3d(x - width, y + height, z + width);
        glEnd();
        glBegin(GL_LINE_LOOP);
        glVertex3d(x + width, y, z - width);
        glVertex3d(x + width, y, z + width);
        glEnd();
        glBegin(GL_LINE_LOOP);
        glVertex3d(x - width, y, z - width);
        glVertex3d(x - width, y, z + width);
        glEnd();
        glBegin(GL_LINE_LOOP);
        glVertex3d(x + width, y, z + width);
        glVertex3d(x - width, y, z + width);
        glEnd();
        glBegin(GL_LINE_LOOP);
        glVertex3d(x + width, y, z - width);
        glVertex3d(x - width, y, z - width);
        glEnd();
        glBegin(GL_LINE_LOOP);
        glVertex3d(x + width, y + height, z - width);
        glVertex3d(x + width, y + height, z + width);
        glEnd();
        glBegin(GL_LINE_LOOP);
        glVertex3d(x - width, y + height, z - width);
        glVertex3d(x - width, y + height, z + width);
        glEnd();
        glBegin(GL_LINE_LOOP);
        glVertex3d(x + width, y + height, z + width);
        glVertex3d(x - width, y + height, z + width);
        glEnd();
        glBegin(GL_LINE_LOOP);
        glVertex3d(x + width, y + height, z - width);
        glVertex3d(x - width, y + height, z - width);
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glColor4f(1, 1, 1, 1);
    }

    public void draw3DLine(double x1, double y1, double z1, double x2, double y2, double z2, float lineWidth, Color color) {
        x1 = x1 - RenderManager.renderPosX;
        y1 = y1 - RenderManager.renderPosY;
        z1 = z1 - RenderManager.renderPosZ;
        x2 = x2 - RenderManager.renderPosX;
        y2 = y2 - RenderManager.renderPosY;
        z2 = z2 - RenderManager.renderPosZ;
        render3DLine(x1, y1, z1, x2, y2, z2, lineWidth, color);
    }

    public void draw3DLine(double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
        draw3DLine(x1, y1, z1, x2, y2, z2, 1.0f, color);
    }

    public void render3DLine(double x1, double y1, double z1, double x2, double y2, double z2, float lineWidth, Color color) {
        color(color);
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_TEXTURE_2D);
        glBegin(GL_LINE_LOOP);
        glVertex3d(x1, y1, z1);
        glVertex3d(x2, y2, z2);
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        color(-1);
    }

    public void render3DLine(double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
        render3DLine(x1, y1, z1, x2, y2, z2, 1.0f, color);
    }

    public void drawBox(double xIn, double yIn, double zIn, double width, double height, Color color, boolean outline) {
        double x = xIn + (0.0) * Minecraft.getMinecraft().timer.renderPartialTicks - RenderManager.renderPosX;
        double y = yIn + (0.0) * Minecraft.getMinecraft().timer.renderPartialTicks - RenderManager.renderPosY;
        double z = zIn + (0.0) * Minecraft.getMinecraft().timer.renderPartialTicks - RenderManager.renderPosZ;
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        quads(width, height, color, x, y, z);
    }

    private void quads(double width, double height, Color color, double x, double y, double z) {
        float alpha = (color.getRGB() >> 24 & 0xFF) / 255.0F;
        float red = (color.getRGB() >> 16 & 0xFF) / 255.0F;
        float green = (color.getRGB() >> 8 & 0xFF) / 255.0F;
        float blue = (color.getRGB() & 0xFF) / 255.0F;
        glColor4f(red, green, blue, alpha);
        glBegin(GL_QUADS);
        glVertex3d(x - width, y, z + width);
        glVertex3d(x + width, y, z + width);
        glVertex3d(x + width, y + height, z + width);
        glVertex3d(x - width, y + height, z + width);
        glVertex3d(x + width, y, z + width);
        glVertex3d(x - width, y, z + width);
        glVertex3d(x - width, y + height, z + width);
        glVertex3d(x + width, y + height, z + width);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(x - width, y, z - width);
        glVertex3d(x + width, y, z - width);
        glVertex3d(x + width, y + height, z - width);
        glVertex3d(x - width, y + height, z - width);
        glVertex3d(x + width, y, z - width);
        glVertex3d(x - width, y, z - width);
        glVertex3d(x - width, y + height, z - width);
        glVertex3d(x + width, y + height, z - width);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(x + width, y, z - width);
        glVertex3d(x + width, y, z + width);
        glVertex3d(x + width, y + height, z + width);
        glVertex3d(x + width, y + height, z - width);
        glVertex3d(x + width, y, z + width);
        glVertex3d(x + width, y, z - width);
        glVertex3d(x + width, y + height, z - width);
        glVertex3d(x + width, y + height, z + width);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(x - width, y, z - width);
        glVertex3d(x - width, y, z + width);
        glVertex3d(x - width, y + height, z + width);
        glVertex3d(x - width, y + height, z - width);
        glVertex3d(x - width, y, z + width);
        glVertex3d(x - width, y, z - width);
        glVertex3d(x - width, y + height, z - width);
        glVertex3d(x - width, y + height, z + width);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(x + width, y + height, z - width);
        glVertex3d(x + width, y + height, z + width);
        glVertex3d(x - width, y + height, z + width);
        glVertex3d(x - width, y + height, z - width);
        glVertex3d(x + width, y + height, z + width);
        glVertex3d(x + width, y + height, z - width);
        glVertex3d(x - width, y + height, z - width);
        glVertex3d(x - width, y + height, z + width);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(x + width, y, z - width);
        glVertex3d(x + width, y, z + width);
        glVertex3d(x - width, y, z + width);
        glVertex3d(x - width, y, z - width);
        glVertex3d(x + width, y, z + width);
        glVertex3d(x + width, y, z - width);
        glVertex3d(x - width, y, z - width);
        glVertex3d(x - width, y, z + width);
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
    }

    public void drawTexture(String location, double x, double y, double x1, double y1) {
        Texture texture = JordanTextureUtil.createTexture(new ResourceLocation(location));
        if (texture != null) {
            texture = new Texture(texture.getBufferedImage(), true);
            Gui.drawRect(0, 0, 0, 0, -1);
            GlStateManager.resetColor();
            GlStateManager.color(1, 1, 1, 1);
            texture.bind();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            drawTexture(x, y, x1, y1);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            texture.unbind();
        }
    }

    public void drawTexture(double x, double y, double x1, double y1) {
        double x0 = x, y0 = y;
        if (x < x1) {
            x = x1;
            x1 = x0;
        }
        if (y > y1) {
            y = y1;
            y1 = y0;
        }
        glBegin(GL_QUADS);
        glTexCoord2f(1, 0);
        glVertex2d(x, y);
        glTexCoord2f(0, 0);
        glVertex2d(x1, y);
        glTexCoord2f(0, 1);
        glVertex2d(x1, y1);
        glTexCoord2f(1, 1);
        glVertex2d(x, y1);
        glEnd();
    }

    public boolean isHovered(double x, double y, double width, double height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public boolean isHoveredScaled(double x, double y, double width, double height, int mouseX, int mouseY, double scaling) {
        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        mouseX *= scaleFactor * scaling;
        mouseY *= scaleFactor * scaling;
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public void drawGradientRectSideways(double x, double y, double w, double h, int startColor, int endColor) {
        rect(x, y, w, h, startColor, endColor, GL_QUADS);
    }

    public void drawGradientRect(double x, double y, double w, double h, int startColor, int endColor) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_QUADS);
        color(endColor);
        glVertex2d(x, y);
        color(startColor);
        glVertex2d(x, h);
        glVertex2d(w, h);
        color(endColor);
        glVertex2d(w, y);
        glEnd();
        glShadeModel(GL_FLAT);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    public void drawHollowGradientRectSideways(double x, double y, double w, double h, int startColor, int endColor) {
        rect(x, y, w, h, startColor, endColor, GL_LINE_LOOP);
    }

    private void rect(double x, double y, double w, double h, int startColor, int endColor, int mode) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glBegin(mode);
        color(startColor);
        glVertex2d(x, y);
        glVertex2d(x, h);
        color(endColor);
        glVertex2d(w, h);
        glVertex2d(w, y);
        glEnd();
        glShadeModel(GL_FLAT);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    public void drawHollowGradientRect(double x, double y, double w, double h, int startColor, int endColor, float width) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glLineWidth(width);
        glBegin(GL_LINE_LOOP);
        color(endColor);
        glVertex2d(x, y);
        color(startColor);
        glVertex2d(x, h);
        glVertex2d(w, h);
        color(endColor);
        glVertex2d(w, y);
        glEnd();
        glShadeModel(GL_FLAT);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    public void drawRect(double x, double y, double w, double h, Color color) {
        drawRect(x, y, w, h, color.getRGB());
    }

    public void drawHollowRect(double x, double y, double w, double h, float lineWidth, int color) {
        if (w < x) {
            double i = x;
            x = w;
            w = i;
        }
        if (h < y) {
            double j = h;
            h = y;
            y = j;
        }
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        color(color);
        glLineWidth(lineWidth);
        glBegin(GL_LINE_LOOP);
        glVertex2d(x, y);
        glVertex2d(x, h);
        glVertex2d(w, h);
        glVertex2d(w, y);
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
    }

    public void drawHollowRect(double x, double y, double w, double h, float lineWidth, Color color) {
        drawHollowRect(x, y, w, h, color.getRGB());
    }

    public void drawHollowRect(double x, double y, double w, double h, int color) {
        drawHollowRect(x, y, w, h, 1.0f, color);
    }

    public void drawHollowRect(double x, double y, double w, double h, Color color) {
        drawHollowRect(x, y, w, h, color.getRGB());
    }

    public void drawCorneredRect(double x, double y, double w, double h, float lineWidth, int color) {
        if (w < x) {
            double i = x;
            x = w;
            w = i;
        }
        if (h < y) {
            double j = h;
            h = y;
            y = j;
        }
        double width = w - x, height = h - y;
        drawLine(x, y, x + width / 4, y, lineWidth, color);
        drawLine(x, h, x + width / 4, h, lineWidth, color);
        drawLine(x, y, x, y + height / 4, lineWidth, color);
        drawLine(w, y, w, y + height / 4, lineWidth, color);
        drawLine(x, h, x, h - height / 4, lineWidth, color);
        drawLine(w, h, w, h - height / 4, lineWidth, color);
        drawLine(w, y, w - width / 4, y, lineWidth, color);
        drawLine(w, h, w - width / 4, h, lineWidth, color);
    }

    public void drawLineRect(double x, double y, double w, double h, double lineWidth, int color) {
        drawRect(x, y, w, y + lineWidth, color);
        drawRect(x, y, x + lineWidth, h, color);
        drawRect(w - lineWidth, y, w, h, color);
        drawRect(x, h - lineWidth, w, h, color);
    }

    public void drawLineRect(double x, double y, double w, double h, double lineWidth, Color color) {
        drawLineRect(x, y, w, h, lineWidth, color.getRGB());
    }

    public void drawLineRect(double x, double y, double w, double h, Color color) {
        drawLineRect(x, y, w, h, color.getRGB());
    }

    public void drawLineRect(double x, double y, double w, double h, int color) {
        drawLineRect(x, y, w, h, 1.0, color);
    }

    public void drawRect(double x, double y, double w, double h, int color) {
        if (w < x) {
            double i = x;
            x = w;
            w = i;
        }
        if (h < y) {
            double j = h;
            h = y;
            y = j;
        }
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        color(color);
        glLineWidth(1.0f);
        glBegin(GL_QUADS);
        glVertex2d(x, y);
        glVertex2d(x, h);
        glVertex2d(w, h);
        glVertex2d(w, y);
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
    }

    public void drawRoundedRect(double x, double y, double width, double height, double cornerRadius, Color color) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_BLEND);
        color(color);
        glBegin(GL_POLYGON);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        for (int i = 0; i <= 90; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        cornerX = x + width - cornerRadius;
        cornerY = y + cornerRadius;
        for (int i = 90; i <= 180; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        cornerX = x + cornerRadius;
        cornerY = y + cornerRadius;
        for (int i = 180; i <= 270; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        cornerX = x + cornerRadius;
        cornerY = y + height - cornerRadius;
        for (int i = 270; i <= 360; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
    }

    public void drawHollowRoundedRect(double x, double y, double width, double height, double cornerRadius, Color color) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_BLEND);
        color(color);
        glLineWidth(1.0f);
        glBegin(GL_LINE_LOOP);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        for (int i = 0; i <= 90; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        cornerX = x + width - cornerRadius;
        cornerY = y + cornerRadius;
        glBegin(GL_LINE_LOOP);
        for (int i = 90; i <= 180; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        cornerX = x + cornerRadius;
        cornerY = y + cornerRadius;
        glBegin(GL_LINE_LOOP);
        for (int i = 180; i <= 270; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        cornerX = x + cornerRadius;
        cornerY = y + height - cornerRadius;
        glBegin(GL_LINE_LOOP);
        for (int i = 270; i <= 360; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        drawLine(x + cornerRadius, y, x + width - cornerRadius, y, 1.0f, color.getRGB());
        drawLine(x + cornerRadius, y + height, x + width - cornerRadius, y + height, 1.0f, color.getRGB());
        drawLine(x, y + cornerRadius, x, y + height - cornerRadius, 1.0f, color.getRGB());
        drawLine(x + width, y + cornerRadius, x + width, y + height - cornerRadius, 1.0f, color.getRGB());
    }

    public void drawLine(double x, double y, double x1, double y1, float lineWidth, int color) {
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        color(color);
        glLineWidth(lineWidth);
        glBegin(GL_LINE_LOOP);
        glVertex2d(x, y);
        glVertex2d(x1, y1);
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
    }

    public void prepareScissorBox(double x, double y, double x2, double y2, ScaledResolution scaledResolution) {
        final int factor = scaledResolution.getScaleFactor();
        glScissor((int) (x * (float) factor), (int) (((float) scaledResolution.getScaledHeight() - y2) * (float) factor), (int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
    }

    public Color modifiedAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(alpha, 255));
    }

    public void drawCircle(double x, double y, double radius, Color color) {
        drawArc(x, y, radius, 0, 360, color.getRGB());
    }

    public void drawArc(double x, double y, double radius, int startAngle, int endAngle, int color) {
        glEnable(GL_BLEND);
        glShadeModel(GL_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        color(color);
        glEnable(GL_POLYGON_SMOOTH);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        glBegin(GL_POLYGON);
        {
            glVertex2d(x, y);
            for (int i = startAngle; i <= endAngle; i++) {
                glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * radius, y + Math.cos(i * Math.PI / 180.0D) * radius);
            }
        }
        glEnd();
        glBegin(GL_POLYGON);
        {
            glVertex2d(x, y);
            for (int i = startAngle; i <= endAngle; i++) {
                glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * radius, y + Math.cos(i * Math.PI / 180.0D) * radius);
            }
            glEnd();
        }
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
        glDisable(GL_BLEND);
        glDisable(GL_POLYGON_SMOOTH);
        glEnable(GL_TEXTURE_2D);
    }


    public Framebuffer createFramebuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        }
        return framebuffer;
    }

    public void renderFrameBuffer(Framebuffer framebuffer) {
        int p_178038_1_ = mc.displayWidth, p_178038_2_ = mc.displayHeight;
        GlStateManager.colorMask(true, true, true, false);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        RenderUtil.INSTANCE.scaleFix(1);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        framebuffer.bindFramebufferTexture();
        float f = (float) p_178038_1_;
        float f1 = (float) p_178038_2_;
        float f2 = (float) framebuffer.framebufferWidth / (float) framebuffer.framebufferTextureWidth;
        float f3 = (float) framebuffer.framebufferHeight / (float) framebuffer.framebufferTextureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(f, f1, 0.0D).tex(f2, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(f, 0.0D, 0.0D).tex(f2, f3).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, f3).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        framebuffer.unbindFramebufferTexture();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.disableLighting();
        RenderUtil.INSTANCE.invertScaleFix(1);
    }
}
