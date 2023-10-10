// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.font.testfontbase;

import org.lwjgl.util.vector.Vector2f;
import java.util.Locale;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import java.awt.geom.Rectangle2D;
import java.awt.FontMetrics;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.RenderingHints;
import java.awt.Color;
import net.minecraft.util.MathHelper;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Font;

public class TTFFontRenderer
{
    private Font font;
    private String name;
    private boolean fractionalMetrics;
    private CharacterData[] regularData;
    private CharacterData[] boldData;
    private CharacterData[] italicsData;
    private int[] colorCodes;
    private static final int MARGIN = 4;
    private static final char COLOR_INVOKER = '§';
    private static int RANDOM_OFFSET;
    
    public TTFFontRenderer(final Font font, final String name) {
        this(font, 256, name);
    }
    
    public TTFFontRenderer(final Font font, final int characterCount, final String name) {
        this(font, characterCount, true, name);
    }
    
    public TTFFontRenderer(final Font font, final boolean fractionalMetrics, final String name) {
        this(font, 256, fractionalMetrics, name);
    }
    
    public TTFFontRenderer(final Font font, final int characterCount, final boolean fractionalMetrics, final String name) {
        this.fractionalMetrics = false;
        this.colorCodes = new int[32];
        this.font = font;
        this.fractionalMetrics = fractionalMetrics;
        this.name = name;
        this.regularData = this.setup(new CharacterData[characterCount], 0);
        this.boldData = this.setup(new CharacterData[characterCount], 1);
        this.italicsData = this.setup(new CharacterData[characterCount], 2);
    }
    
    private CharacterData[] setup(final CharacterData[] characterData, final int type) {
        this.generateColors();
        final Font font = this.font.deriveFont(type);
        final BufferedImage utilityImage = new BufferedImage(1, 1, 2);
        final Graphics2D utilityGraphics = (Graphics2D)utilityImage.getGraphics();
        utilityGraphics.setFont(font);
        final FontMetrics fontMetrics = utilityGraphics.getFontMetrics();
        for (int index = 0; index < characterData.length; ++index) {
            final char character = (char)index;
            final Rectangle2D characterBounds = fontMetrics.getStringBounds(character + "", utilityGraphics);
            final float width = (float)characterBounds.getWidth() + 8.0f;
            final float height = (float)characterBounds.getHeight();
            final BufferedImage characterImage = new BufferedImage(MathHelper.ceiling_double_int(width), MathHelper.ceiling_double_int(height), 2);
            final Graphics2D graphics = (Graphics2D)characterImage.getGraphics();
            graphics.setFont(font);
            graphics.setColor(new Color(255, 255, 255, 0));
            graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
            graphics.setColor(Color.WHITE);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            graphics.drawString(character + "", 4, fontMetrics.getAscent());
            final int textureId = GlStateManager.generateTexture();
            this.createTexture(textureId, characterImage);
            characterData[index] = new CharacterData(character, (float)characterImage.getWidth(), (float)characterImage.getHeight(), textureId);
        }
        return characterData;
    }
    
    private void createTexture(final int textureId, final BufferedImage image) {
        final int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                final int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte)(pixel >> 16 & 0xFF));
                buffer.put((byte)(pixel >> 8 & 0xFF));
                buffer.put((byte)(pixel & 0xFF));
                buffer.put((byte)(pixel >> 24 & 0xFF));
            }
        }
        buffer.flip();
        GlStateManager.bindTexture(textureId);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexImage2D(3553, 0, 6408, image.getWidth(), image.getHeight(), 0, 6408, 5121, buffer);
        GlStateManager.bindTexture(0);
    }
    
    public void drawString(final String text, final float x, final float y, final int color) {
        this.renderString(text, x, y, color, false);
    }
    
    public void drawStringWithShadow(final String text, final float x, final float y, final int color) {
        GL11.glTranslated(0.5, 0.5, 0.0);
        this.renderString(text, x, y, color, true);
        GL11.glTranslated(-0.5, -0.5, 0.0);
        this.renderString(text, x, y, color, false);
    }
    
    private void renderString(final String text, float x, float y, final int color, final boolean shadow) {
        if (text.length() == 0) {
            return;
        }
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 1.0);
        x -= 2.0f;
        y -= 2.0f;
        x += 0.5f;
        y += 0.5f;
        x *= 2.0f;
        y *= 2.0f;
        CharacterData[] characterData = this.regularData;
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;
        final int length = text.length();
        final float multiplier = (float)(shadow ? 4 : 1);
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GlStateManager.color(r / multiplier, g / multiplier, b / multiplier, a);
        for (int i = 0; i < length; ++i) {
            char character = text.charAt(i);
            final char previous = (i > 0) ? text.charAt(i - 1) : '.';
            if (previous != '§') {
                if (character == '§' && i < length) {
                    int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                    if (index < 16) {
                        obfuscated = false;
                        strikethrough = false;
                        underlined = false;
                        characterData = this.regularData;
                        if (index < 0 || index > 15) {
                            index = 15;
                        }
                        if (shadow) {
                            index += 16;
                        }
                        final int textColor = this.colorCodes[index];
                        GL11.glColor4d((textColor >> 16) / 255.0, (textColor >> 8 & 0xFF) / 255.0, (textColor & 0xFF) / 255.0, a);
                    }
                    else if (index == 16) {
                        obfuscated = true;
                    }
                    else if (index == 17) {
                        characterData = this.boldData;
                    }
                    else if (index == 18) {
                        strikethrough = true;
                    }
                    else if (index == 19) {
                        underlined = true;
                    }
                    else if (index == 20) {
                        characterData = this.italicsData;
                    }
                    else if (index == 21) {
                        obfuscated = false;
                        strikethrough = false;
                        underlined = false;
                        characterData = this.regularData;
                        GL11.glColor4d(1.0 * (shadow ? 0.25 : 1.0), 1.0 * (shadow ? 0.25 : 1.0), 1.0 * (shadow ? 0.25 : 1.0), a);
                    }
                }
                else if (character <= '\u00ff') {
                    if (obfuscated) {
                        character += (char)TTFFontRenderer.RANDOM_OFFSET;
                    }
                    this.drawChar(character, characterData, x, y);
                    final CharacterData charData = characterData[character];
                    if (strikethrough) {
                        this.drawLine(new Vector2f(0.0f, charData.height / 2.0f), new Vector2f(charData.width, charData.height / 2.0f), 3.0f);
                    }
                    if (underlined) {
                        this.drawLine(new Vector2f(0.0f, charData.height - 15.0f), new Vector2f(charData.width, charData.height - 15.0f), 3.0f);
                    }
                    x += charData.width - 8.0f;
                }
            }
        }
        GL11.glPopMatrix();
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GlStateManager.bindTexture(0);
    }
    
    public float getWidth(final String text) {
        float width = 0.0f;
        CharacterData[] characterData = this.regularData;
        for (int length = text.length(), i = 0; i < length; ++i) {
            final char character = text.charAt(i);
            final char previous = (i > 0) ? text.charAt(i - 1) : '.';
            if (previous != '§') {
                if (character == '§' && i < length) {
                    final int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                    if (index == 17) {
                        characterData = this.boldData;
                    }
                    else if (index == 20) {
                        characterData = this.italicsData;
                    }
                    else if (index == 21) {
                        characterData = this.regularData;
                    }
                }
                else if (character <= '\u00ff') {
                    final CharacterData charData = characterData[character];
                    width += (charData.width - 8.0f) / 2.0f;
                }
            }
        }
        return width + 2.0f;
    }
    
    public float getHeight() {
        return this.getHeight("ABCDERGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwkyz1234567890");
    }
    
    public float getHeight(final String text) {
        float height = 0.0f;
        CharacterData[] characterData = this.regularData;
        for (int length = text.length(), i = 0; i < length; ++i) {
            final char character = text.charAt(i);
            final char previous = (i > 0) ? text.charAt(i - 1) : '.';
            if (previous != '§') {
                if (character == '§' && i < length) {
                    final int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                    if (index == 17) {
                        characterData = this.boldData;
                    }
                    else if (index == 20) {
                        characterData = this.italicsData;
                    }
                    else if (index == 21) {
                        characterData = this.regularData;
                    }
                }
                else if (character <= '\u00ff') {
                    final CharacterData charData = characterData[character];
                    height = Math.max(height, charData.height);
                }
            }
        }
        return height / 2.0f - 2.0f;
    }
    
    private void drawChar(final char character, final CharacterData[] characterData, final float x, final float y) {
        final CharacterData charData = characterData[character];
        charData.bind();
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2d(x, y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2d(x, y + charData.height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2d(x + charData.width, y + charData.height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2d(x + charData.width, y);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glBindTexture(3553, 0);
    }
    
    private void drawLine(final Vector2f start, final Vector2f end, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2f(start.x, start.y);
        GL11.glVertex2f(end.x, end.y);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
    
    private void generateColors() {
        for (int i = 0; i < 32; ++i) {
            final int thingy = (i >> 3 & 0x1) * 85;
            int red = (i >> 2 & 0x1) * 170 + thingy;
            int green = (i >> 1 & 0x1) * 170 + thingy;
            int blue = (i >> 0 & 0x1) * 170 + thingy;
            if (i == 6) {
                red += 85;
            }
            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCodes[i] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
        }
    }
    
    public Font getFont() {
        return this.font;
    }
    
    public String getName() {
        return this.name;
    }
    
    static {
        TTFFontRenderer.RANDOM_OFFSET = 1;
    }
    
    class CharacterData
    {
        public char character;
        public float width;
        public float height;
        private int textureId;
        
        public CharacterData(final char character, final float width, final float height, final int textureId) {
            this.character = character;
            this.width = width;
            this.height = height;
            this.textureId = textureId;
        }
        
        public void bind() {
            GL11.glBindTexture(3553, this.textureId);
        }
    }
}
