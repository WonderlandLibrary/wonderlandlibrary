package dev.zprestige.prestige.client.ui.font;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zprestige.prestige.client.Prestige;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.binary.Base64;
import org.joml.Matrix4f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

//#SKIDDED
public class FontRenderer {
    private final float fontSize;
    private final int startChar;
    private final int endChar;
    private final float[] xPos;
    private final float[] yPos;
    private Font font;
    private Graphics2D graphics;
    private FontMetrics metrics;
    private BufferedImage bufferedImage;
    private Identifier resourceLocation;

    public FontRenderer(InputStream font) {
        this(font, 18F);
    }

    public FontRenderer(InputStream font, float size) {
        this.fontSize = size;
        this.startChar = 32;
        this.endChar = 255;
        this.xPos = new float[this.endChar - this.startChar];
        this.yPos = new float[this.endChar - this.startChar];
        setupGraphics2D();
        createFont(font, size);
    }

    private static NativeImage readTexture(String textureBase64) {
        try {
            byte[] imgBytes = Base64.decodeBase64(textureBase64);
            ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
            return NativeImage.read(bais);
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    private void setupGraphics2D() {
        this.bufferedImage = new BufferedImage(256, 256, 2);
        this.graphics = ((Graphics2D) this.bufferedImage.getGraphics());
        this.graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    }

    private void createFont(InputStream font, float size) {
        try {
            this.font = Font.createFont(0, font).deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.graphics.setFont(this.font);
        this.graphics.setColor(new Color(255, 255, 255, 0));
        this.graphics.fillRect(0, 0, 256, 256);
        this.graphics.setColor(Color.white);
        this.metrics = this.graphics.getFontMetrics();

        float x = 5.0F;
        float y = 5.0F;
        for (int i = this.startChar; i < this.endChar; i++) {
            this.graphics.drawString(Character.toString((char) i), x, y + this.metrics.getAscent());
            this.xPos[(i - this.startChar)] = x;
            this.yPos[(i - this.startChar)] = (y - this.metrics.getMaxDescent());
            x += this.metrics.stringWidth(Character.toString((char) i)) + 2.0F;
            if (x >= 250 - this.metrics.getMaxAdvance()) {
                x = 5.0F;
                y += this.metrics.getMaxAscent() + this.metrics.getMaxDescent() + this.fontSize / 2.0F;
            }
        }
        String base64 = imageToBase64String(bufferedImage);
        this.setResourceLocation(base64);
    }

    private String imageToBase64String(BufferedImage image) {
        String ret;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", bos);
            byte[] bytes = bos.toByteArray();
            Base64 encoder = new Base64();
            ret = encoder.encodeAsString(bytes);
            ret = ret.replace(System.lineSeparator(), "");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return ret;
    }

    public void setResourceLocation(String base64) {
        NativeImage image = readTexture(base64);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                imgNew.setColor(x, y, image.getColor(x, y));
            }
        }

        image.close();
        this.resourceLocation = new Identifier("prestige", "font/font.ttf");
        applyTexture(resourceLocation, imgNew);
    }

    private void applyTexture(Identifier identifier, NativeImage nativeImage) {
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, new NativeImageBackedTexture(nativeImage)));
    }

    public void drawString(MatrixStack matrixStack, String text, float x, float y, Color color, Color color2, boolean idk) {
        matrixStack.push();
        RenderSystem.depthFunc((int)519);
        RenderSystem.enableBlend();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        if (idk) {
            this.drawer(matrixStack, text, x + 0.5f, y + 0.5f, color2);
        }
        this.drawer(matrixStack, text, x, y, color);
        matrixStack.scale(1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public void drawString(String text, float x, float y, Color color) {
        this.drawString(text, x, y, color, true);
    }

    private void drawString(String text, float x, float y, Color color, boolean idk) {
        int n = Math.min(187, color.getAlpha());
        MatrixStack matrixStack = Prestige.Companion.getFontManager().getMatrixStack();
        this.drawString(matrixStack, text, x, y, color, new Color(0, 0, 0, n == -1 ? color.getAlpha() : n), idk);
    }

    private void drawer(MatrixStack matrixStack, String text, float x, float y, Color color) {
        StringBuilder finalText = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c >= this.startChar && c <= this.endChar) finalText.append(c);
            else finalText.append("?");
        }
        text = finalText.toString();
        x *= 2.0F;
        y *= 2.0F;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, this.resourceLocation);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShaderColor((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        for (int i = 0; i < text.length(); i++) {
            try {
                char c = text.charAt(i);
                drawChar(matrixStack, c, x, y);
                x += getStringWidth(Character.toString(c)) * 2.0F;
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }
        //bufferBuilder.end();
        tessellator.draw();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public final float getStringWidth(String text) {
        return (float) (getBounds(text).getWidth()) / 2.0F;
    }

    public float getStringHeight() {
        return (float)this.getBounds("W").getHeight() / 2.0f;
    }

    private Rectangle2D getBounds(String text) {
        return this.metrics.getStringBounds(text, this.graphics);
    }

    private void drawChar(MatrixStack matrixStack, char character, float x, float y) throws ArrayIndexOutOfBoundsException {
        Rectangle2D bounds = this.metrics.getStringBounds(Character.toString(character), this.graphics);
        drawTexturedModalRect(matrixStack, x, y, this.xPos[(character - this.startChar)], this.yPos[(character - this.startChar)], (float) bounds.getWidth(), (float) bounds.getHeight() + this.metrics.getMaxDescent() + 1.0F);
    }

    private void drawTexturedModalRect(MatrixStack matrixStack, float x, float y, float u, float v, float width, float height) {
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        float scale = 0.0039063F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.vertex(matrix4f, x + 0.0F, y + height, 0.0f).texture((u + 0.0F) * scale, (v + height) * scale).next();
        bufferBuilder.vertex(matrix4f, x + width, y + height, 0.0f).texture((u + width) * scale, (v + height) * scale).next();
        bufferBuilder.vertex(matrix4f, x + width, y + 0.0F, 0.0f).texture((u + width) * scale, (v + 0.0F) * scale).next();
        bufferBuilder.vertex(matrix4f, x + 0.0F, y + 0.0F, 0.0f).texture((u + 0.0F) * scale, (v + 0.0F) * scale).next();
    }
}
