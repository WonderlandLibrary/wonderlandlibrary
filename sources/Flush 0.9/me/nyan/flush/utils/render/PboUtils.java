package me.nyan.flush.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL21.GL_PIXEL_PACK_BUFFER;

public class PboUtils {
    private final static Minecraft mc = Minecraft.getMinecraft();
    private final int[] pbos;
    private final ByteBuffer[] data;

    private final int[] areaWidth, areaHeight;

    private final int[] lastWidth, lastHeight;
    private final float[] lastFactor;

    public PboUtils(int count) {
        pbos = new int[count];
        data = new ByteBuffer[count];
        areaWidth = new int[count];
        areaHeight = new int[count];
        lastWidth = new int[count];
        lastHeight = new int[count];
        lastFactor = new float[count];
    }

    public ByteBuffer getPixelsBGRA(int i) {
        ByteBuffer pixels;
        bind(i);
        ByteBuffer buffer = glMapBuffer(GL_PIXEL_PACK_BUFFER, GL_READ_ONLY, data[i]);
        if (buffer != null) {
            pixels = buffer.asReadOnlyBuffer();
            glUnmapBuffer(GL_PIXEL_PACK_BUFFER);
        } else {
            pixels = ByteBuffer.allocate(areaWidth[i] * areaHeight[i] * 4);
        }
        unbind();
        return pixels;
    }

    public int getPixelColor(int i, int x, int y) {
        int color;
        bind(i);
        ByteBuffer buffer = glMapBuffer(GL_PIXEL_PACK_BUFFER, GL_READ_ONLY, data[i]);
        if (buffer != null) {
            int index = (y * areaWidth[i] + x) * 4;
            int     r = buffer.get(index + 2),
                    g = buffer.get(index + 1),
                    b = buffer.get(index),
                    a = buffer.get(index + 3);
            color = ColorUtils.getColor(r, g, b, a);
            glUnmapBuffer(GL_PIXEL_PACK_BUFFER);
        } else {
            color = 0xFF000000;
        }
        unbind();
        return color;
    }

    public int getAverageColor(int i) {
        int alpha = 0;
        int red = 0;
        int green = 0;
        int blue = 0;

        bind(i);
        ByteBuffer buffer = glMapBuffer(GL_PIXEL_PACK_BUFFER, GL_READ_ONLY, data[i]);
        if (buffer != null) {
            int pixels = buffer.capacity() / 4;
            for (int y = areaHeight[i] - 1; y >= 0; y--) {
                for (int x = 0; x < areaWidth[i]; x++) {
                    int index = (y * areaWidth[i] + x) * 4;
                    alpha += buffer.get(index + 3) & 0xFF;
                    red += buffer.get(index + 2) & 0xFF;
                    green += buffer.get(index + 1) & 0xFF;
                    blue += buffer.get(index) & 0xFF;
                }
            }
            glUnmapBuffer(GL_PIXEL_PACK_BUFFER);

            alpha /= pixels;
            red /= pixels;
            green /= pixels;
            blue /= pixels;
        }
        unbind();
        return ColorUtils.getColor(red, green, blue, alpha);
    }

    public IntBuffer getPixels(int i) {
        IntBuffer pixels;
        bind(i);
        ByteBuffer buffer = glMapBuffer(GL_PIXEL_PACK_BUFFER, GL_READ_ONLY, data[i]);
        if (buffer != null) {
            int capacity = buffer.capacity();
            pixels = IntBuffer.allocate(capacity / 4);

            for (int y = areaHeight[i] - 1; y >= 0; y--) {
                for (int x = 0; x < areaWidth[i]; x++) {
                    int index = (y * areaWidth[i] + x) * 4;
                    int     r = buffer.get(index + 2),
                            g = buffer.get(index + 1),
                            b = buffer.get(index),
                            a = buffer.get(index + 3);
                    pixels.put(ColorUtils.getColor(r, g, b, a));
                }
            }
            glUnmapBuffer(GL_PIXEL_PACK_BUFFER);
        } else {
            pixels = IntBuffer.allocate(areaWidth[i] * areaHeight[i]);
        }
        unbind();
        return pixels;
    }

    public void init(int i, double width, double height) {
        ScaledResolution scale = new ScaledResolution(mc);
        float factor = scale.getScaleFactor();
        if (mc.displayWidth == lastWidth[i] && mc.displayHeight == lastHeight[i] && factor == lastFactor[i]) {
            return;
        }

        int unscaledWidth = (int) (width * factor);
        int unscaledHeight = (int) (height * factor);
        areaWidth[i] = unscaledWidth;
        areaHeight[i] = unscaledHeight;

        int pbo = pbos[i];
        if (pbo == 0) {
            pbo = OpenGlHelper.glGenBuffers();
            pbos[i] = pbo;
        }

        int size = unscaledWidth * unscaledHeight * 4;
        data[i] = BufferUtils.createByteBuffer(size);

        OpenGlHelper.glBindBuffer(GL_PIXEL_PACK_BUFFER, pbo);
        OpenGlHelper.glBufferData(GL_PIXEL_PACK_BUFFER, data[i], GL_STREAM_READ);
        OpenGlHelper.glBindBuffer(GL_PIXEL_PACK_BUFFER, 0);

        lastWidth[i] = mc.displayWidth;
        lastHeight[i] = mc.displayHeight;
        lastFactor[i] = factor;
    }

    public void update(int i, double x, double y) {
        ScaledResolution scale = new ScaledResolution(mc);
        float factor = scale.getScaleFactor();

        int unscaledX = (int) (x * factor);
        int unscaledY = (int) ((scale.getScaledHeight() - y) * factor);

        bind(i);
        glReadPixels(unscaledX, unscaledY, areaWidth[i], areaHeight[i], GL_BGRA, GL_UNSIGNED_BYTE, 0);
        unbind();
    }

    public void bind(int i) {
        OpenGlHelper.glBindBuffer(GL_PIXEL_PACK_BUFFER, pbos[i]);
    }

    public void unbind() {
        OpenGlHelper.glBindBuffer(GL_PIXEL_PACK_BUFFER, 0);
    }

    public int getPBO(int i) {
        return pbos[i];
    }
}
