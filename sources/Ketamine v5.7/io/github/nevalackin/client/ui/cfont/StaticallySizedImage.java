package io.github.nevalackin.client.ui.cfont;

import io.github.nevalackin.client.util.render.DrawUtil;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL42.glTexStorage2D;

public final class StaticallySizedImage {

    private final int textureId;
    private final int width, height;

    public StaticallySizedImage(final BufferedImage image, final boolean useMipMap, final int numMinMaps) {
        this.textureId = glGenTextures();
        this.width = image.getWidth();
        this.height = image.getHeight();

        int[] pixels = new int[this.width * this.height];

        image.getRGB(0, 0, this.width, this.height,
                     pixels, 0, this.width);

        final ByteBuffer buffer = BufferUtils.createByteBuffer(this.width * this.height * 4);

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int pixel = pixels[y * this.width + x];

                buffer.put((byte) 0xFF); // Hard code as white to save some performance
                buffer.put((byte) 0xFF);
                buffer.put((byte) 0xFF);
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        glBindTexture(GL_TEXTURE_2D, this.textureId);
        // Mip maps!
        glTexStorage2D(GL_TEXTURE_2D, useMipMap ? numMinMaps : 1, GL_RGBA8,
                       this.width, this.height);
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0,
                        this.width, this.height,
                        GL_BGRA, GL_UNSIGNED_BYTE, buffer);

        if (useMipMap) {
            glGenerateMipmap(GL_TEXTURE_2D);
            // use mip map for minification
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        } else {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        }

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, 0.0f);
        // use linear filtering for magnification
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // Upload texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                     this.width, this.height, 0,
                     GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.textureId);
    }

    public void draw(final double x,
                     final double y) {
        this.draw(x, y, this.width, this.height);
    }

    public void draw(final double x,
                     final double y,
                     final double width,
                     final double height) {
        // Bind texture
        this.bind();
        // Enable blending
        glEnable(GL_BLEND);
        // Start drawing
        glBegin(GL_QUADS);
        {
            // Specify uv tex coord
            glTexCoord2d(0, 0);
            // to vertex
            glVertex2d(x, y);

            glTexCoord2d(0, 1);
            glVertex2d(x, y + height);

            glTexCoord2d(1, 1);
            glVertex2d(x + width, y + height);

            glTexCoord2d(1, 0);
            glVertex2d(x + width, y);
        }
        // Draw texture
        glEnd();
        // Disable blending
        glDisable(GL_BLEND);
    }

    public void draw(final double x,
                     final double y,
                     final double width,
                     final double height,
                     final int colour) {
        // Bind texture
        this.bind();
        // set color
        DrawUtil.glColour(colour);
        // Enable blending
        glEnable(GL_BLEND);
        // Start drawing
        glBegin(GL_QUADS);
        {
            // Specify uv tex coord
            glTexCoord2d(0, 0);
            // to vertex
            glVertex2d(x, y);

            glTexCoord2d(0, 1);
            glVertex2d(x, y + height);

            glTexCoord2d(1, 1);
            glVertex2d(x + width, y + height);

            glTexCoord2d(1, 0);
            glVertex2d(x + width, y);
        }
        // Draw texture
        glEnd();
        // Disable blending
        glDisable(GL_BLEND);
    }
}
