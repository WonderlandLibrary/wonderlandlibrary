package graphite.api.util.render.font;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Made by MemesValkyrie for Faint.
 * <p>
 * Just a little fontrenderer for minecraft I wrote. Should work with any font size
 * without any graphical glitches, but because of this setup takes forever. Feel free to make any edits.
 * <p>
 * Created by Zeb on 12/19/2016.
 */
public class TTFFontRenderer implements FontRenderer {

    private static final char COLOR_INVOKER = '\247';
    private static final Random RANDOM = new Random();
    private final Font font;
    public final CharacterData[] charData = new CharacterData[1105];
    private final int[] colorCodes = new int[32];
    private final int margin;
    private final boolean antiAlias;
    private final boolean fractionalMetrics;

    public TTFFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        generateColors();

        this.font = font;
        this.margin = 6;
        this.antiAlias = antiAlias;
        this.fractionalMetrics = fractionalMetrics;

        for (int i = 0; i < charData.length; i++) {
            char c = (char) i;
            if (isValid(c)) setup(c);
        }
    }

    @Override
    public int drawString(String text, float x, float y, int color) {
        renderString(text, x, y, color, false);
        return 0;
    }

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        GL11.glTranslated(0.5, 0.5, 0);
        renderString(text, x, y, color, true);
        GL11.glTranslated(-0.5, -0.5, 0);
        renderString(text, x, y, color, false);
        return 0;
    }

    @Override
    public float getWidth(String text) {
        if (text == null || text.length() == 0)
            return 0;

        float width = 0;
        CharacterData[] characterData = charData;
        int length = text.length();

        for (int i = 0; i < length; i++) {
            char character = text.charAt(i);

            if (character == COLOR_INVOKER ||
                    (i > 0 ? text.charAt(i - 1) : '.') == COLOR_INVOKER ||
                    !isValid(character)) continue;

            CharacterData charData = characterData[character];

            width += (charData.width - (2 * margin)) / 2;
        }

        return width;
    }

    @Override
    public float getHeight(String text) {
        float height = 0;

        CharacterData[] characterData = charData;

        int length = text.length();

        for (int i = 0; i < length; i++) {
            char character = text.charAt(i);
            if ((i > 0 ? text.charAt(i - 1) : '.') == COLOR_INVOKER ||
                    character == COLOR_INVOKER ||
                    !isValid(character)) continue;

            CharacterData charData = characterData[character];
            height = Math.max(height, charData.height);
        }

        return (height - margin) / 2;
    }

    private void setup(char character) {
        BufferedImage utilityImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D utilityGraphics = (Graphics2D) utilityImage.getGraphics();
        utilityGraphics.setFont(font);
        FontMetrics fontMetrics = utilityGraphics.getFontMetrics();
        Rectangle2D characterBounds = fontMetrics.getStringBounds(String.valueOf(character), utilityGraphics);
        BufferedImage characterImage = new BufferedImage(
                (int) StrictMath.ceil(characterBounds.getWidth() + (2 * margin)),
                (int) StrictMath.ceil(characterBounds.getHeight()),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) characterImage.getGraphics();
        graphics.setFont(font);
        graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
        graphics.setColor(Color.WHITE);

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                this.antiAlias
                ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                this.antiAlias
                ? RenderingHints.VALUE_ANTIALIAS_ON
                : RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                this.fractionalMetrics
                ? RenderingHints.VALUE_FRACTIONALMETRICS_ON
                : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);

        graphics.drawString(String.valueOf(character), margin, fontMetrics.getAscent());

        int textureId = GL11.glGenTextures();
        createTexture(textureId, characterImage);

        charData[character] = new CharacterData(characterImage.getWidth(), characterImage.getHeight(), textureId);
    }

    private void createTexture(int textureId, BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];

        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];

                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
    }

    private void renderString(CharSequence text, float x, float y, int color, boolean shadow) {
        if (text == null || text.length() == 0) return;

        GL11.glPushMatrix();

        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (color == 0x20FFFFFF) {
            color = 0xFFAFAFAF;
        }

        GL11.glScaled(0.5, 0.5, 1);

        x -= (float) margin / 2;
        y -= 2;

        x *= 2;
        y *= 2;

        CharacterData[] characterData = charData;

        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;

        int length = text.length();

        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        GlStateManager.enableBlend();
        GL11.glColor4f(shadow ? (float) (12 >> 16 & 255) / 255.0F : r, shadow ? (float) (12 >> 8 & 255) / 255.0F : g, shadow ? (float) (14 & 255) / 255.0F : b, a);

        for (int i = 0; i < length; i++) {
            char character = text.charAt(i);
            char previous = i > 0 ? text.charAt(i - 1) : '.';
            if (previous == COLOR_INVOKER) continue;
            if (character == COLOR_INVOKER) {
                int index = "0123456789ABCDEFKLMNOR".indexOf(text.charAt(i + 1));
                if (index < 16) {
                    obfuscated = false;
                    strikethrough = false;
                    underlined = false;
                    characterData = charData;
                    if (index < 0) index = 15;
                    if (shadow) index += 16;
                    int textColor = this.colorCodes[index];
                    GL11.glColor4f((textColor >> 16) / 255.0F,
                            (textColor >> 8 & 255) / 255.0F,
                            (textColor & 255) / 255.0F,
                            a);
                } else if (index == 16)
                    obfuscated = true;
                else if (index == 18)
                    strikethrough = true;
                else if (index == 19)
                    underlined = true;
                else {
                    obfuscated = false;
                    strikethrough = false;
                    underlined = false;

                    characterData = charData;

                    GL11.glColor4d(1, 1, 1, a);
                }
            } else {
                if (!isValid(character))
                    continue;

                if (obfuscated)
                    character += (int) (RANDOM.nextInt(Math.max(0, charData.length - character)));

                final CharacterData charData = characterData[character];

                drawChar(charData, x, y);

                if(strikethrough)
                    drawLine(new Vector2f(0, charData.height / 2f), new Vector2f(charData.width, charData.height / 2f));

                if(underlined)
                    drawLine(new Vector2f(0, charData.height - 15), new Vector2f(charData.width, charData.height - 15));

                x += charData.width - (2 * margin);
            }
        }

        GL11.glPopMatrix();

        GL11.glColor4d(1, 1, 1, 1);

        GlStateManager.bindTexture(0);
    }

    private boolean isValid(char c) {
        return c > 10 && c < charData.length && c != 127;
    }

    public void drawChar(CharacterData characterData, float x, float y) {
        characterData.bind();

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2d(x, y);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2d(x, y + characterData.height);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2d(x + characterData.width, y + characterData.height);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2d(x + characterData.width, y);
        }
        GL11.glEnd();
    }

    private void drawLine(Vector2f start, Vector2f end){
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(3);
        GL11.glBegin(GL11.GL_LINES);
        {
            GL11.glVertex2f(start.x, start.y);
            GL11.glVertex2f(end.x, end.y);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void generateColors() {
        for (int i = 0; i < 32; i++) {
            int thingy = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + thingy;
            int green = (i >> 1 & 1) * 170 + thingy;
            int blue = (i & 1) * 170 + thingy;
            if (i == 6) red += 85;
            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
    }

    public static class CharacterData {
        private final int textureId;
        public float width;
        public float height;

        private CharacterData(float width, float height, int textureId) {
            this.width = width;
            this.height = height;
            this.textureId = textureId;
        }

        public void bind() {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        }
    }
}