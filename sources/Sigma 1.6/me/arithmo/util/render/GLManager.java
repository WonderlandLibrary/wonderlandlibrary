/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL15
 */
package me.arithmo.util.render;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public final class GLManager {
    public static List<Integer> textureIds = new ArrayList<Integer>();
    public static List<Integer> vbos = new ArrayList<Integer>();
    private static int lastMouseX;
    private static int lastMouseY;
    private static final Random random;

    private GLManager() {
    }

    public static void glScissor(int[] rect) {
        GLManager.glScissor(rect[0], rect[1], rect[0] + rect[2], rect[1] + rect[3]);
    }

    public static void glScissor(float x, float y, float x1, float y1) {
        int factor = GLManager.getScaleFactor();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)((float)Minecraft.getMinecraft().displayHeight - y1 * (float)factor)), (int)((int)((x1 - x) * (float)factor)), (int)((int)((y1 - y) * (float)factor)));
    }

    public static int genTexture() {
        int textureId = GL11.glGenTextures();
        textureIds.add(textureId);
        return textureId;
    }

    public static int applyTexture(int texId, File file, int filter, int wrap) throws IOException {
        GLManager.applyTexture(texId, ImageIO.read(file), filter, wrap);
        return texId;
    }

    public static int applyTexture(int texId, BufferedImage image, int filter, int wrap) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer((int)(image.getWidth() * image.getHeight() * 4));
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte)(pixel >> 16 & 255));
                buffer.put((byte)(pixel >> 8 & 255));
                buffer.put((byte)(pixel & 255));
                buffer.put((byte)(pixel >> 24 & 255));
            }
        }
        buffer.flip();
        GLManager.applyTexture(texId, image.getWidth(), image.getHeight(), buffer, filter, wrap);
        return texId;
    }

    public static int applyTexture(int texId, int width, int height, ByteBuffer pixels, int filter, int wrap) {
        GL11.glBindTexture((int)3553, (int)texId);
        GL11.glTexParameteri((int)3553, (int)10241, (int)filter);
        GL11.glTexParameteri((int)3553, (int)10240, (int)filter);
        GL11.glTexParameteri((int)3553, (int)10242, (int)wrap);
        GL11.glTexParameteri((int)3553, (int)10243, (int)wrap);
        GL11.glPixelStorei((int)3317, (int)1);
        GL11.glTexImage2D((int)3553, (int)0, (int)32856, (int)width, (int)height, (int)0, (int)6408, (int)5121, (ByteBuffer)pixels);
        GL11.glBindTexture((int)3553, (int)0);
        return texId;
    }

    public static int genVbo() {
        int id = GL15.glGenBuffers();
        vbos.add(id);
        GL15.glBindBuffer((int)34962, (int)id);
        return id;
    }

    public static void cleanup() {
        GL15.glBindBuffer((int)34962, (int)0);
        GL11.glBindTexture((int)3553, (int)0);
        Iterator<Integer> iterator = textureIds.iterator();
        while (iterator.hasNext()) {
            int texId = iterator.next();
            GL11.glDeleteTextures((int)texId);
        }
        iterator = vbos.iterator();
        while (iterator.hasNext()) {
            int vbo = iterator.next();
            GL15.glDeleteBuffers((int)vbo);
        }
    }

    public static int getMouseX() {
        return Mouse.getX() * GLManager.getScreenWidth() / Minecraft.getMinecraft().displayWidth;
    }

    public static int getMouseDX() {
        return GLManager.getMouseX() - lastMouseX;
    }

    public static int getMouseY() {
        return GLManager.getScreenHeight() - Mouse.getY() * GLManager.getScreenHeight() / Minecraft.getMinecraft().displayHeight - 1;
    }

    public static int getMouseDY() {
        return GLManager.getMouseY() - lastMouseY;
    }

    public static int getScreenWidth() {
        return Minecraft.getMinecraft().displayWidth / GLManager.getScaleFactor();
    }

    public static int getScreenHeight() {
        return Minecraft.getMinecraft().displayHeight / GLManager.getScaleFactor();
    }

    public static int getScaleFactor() {
        int scaleFactor = 1;
        boolean isUnicode = Minecraft.getMinecraft().isUnicode();
        int scaleSetting = Minecraft.getMinecraft().gameSettings.guiScale;
        if (scaleSetting == 0) {
            scaleSetting = 1000;
        }
        while (scaleFactor < scaleSetting && Minecraft.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Minecraft.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        if (isUnicode && scaleFactor % 2 != 0 && scaleFactor != 1) {
            --scaleFactor;
        }
        return scaleFactor;
    }

    public static void glColor(float red, float green, float blue, float alpha) {
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(Color color) {
        GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void glColor(Color color, float alpha) {
        GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, alpha);
    }

    public static void glColor(int color) {
        GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, (float)(color >> 24 & 255) / 255.0f);
    }

    public static void glColor(int color, float alpha) {
        GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, alpha);
    }

    public static Color getRandomColor(int saturationRandom, float luminance) {
        float hue = random.nextFloat();
        float saturation = ((float)random.nextInt(saturationRandom) + (float)saturationRandom) / (float)saturationRandom + (float)saturationRandom;
        return Color.getHSBColor(hue, saturation, luminance);
    }

    public static Color getHSBColor(float hue, float saturation, float luminance) {
        return Color.getHSBColor(hue, saturation, luminance);
    }

    public static Color getRandomColor() {
        return GLManager.getRandomColor(1000, 0.6f);
    }

    public static void update() {
        lastMouseX = GLManager.getMouseX();
        lastMouseY = GLManager.getMouseY();
    }

    static {
        random = new Random();
    }
}

