package me.utils.fluxfont;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RapeMasterFontManager {
    private static final int[] colorCode = new int[32];
    public static final RapeMasterFontManager F11;
    public static final RapeMasterFontManager F12;
    public static final RapeMasterFontManager F13;
    public static final RapeMasterFontManager F14;
    public static final RapeMasterFontManager F15;
    public static final RapeMasterFontManager F16;
    public static final RapeMasterFontManager F18;
    public static final RapeMasterFontManager F22;
    public static final RapeMasterFontManager F24;
    public static final RapeMasterFontManager F35;
    public static final RapeMasterFontManager F40;
    private final byte[][] charwidth = new byte[256][];
    private final int[] textures = new int[256];
    private final FontRenderContext context = new FontRenderContext(new AffineTransform(), true, true);
    private Font font = null;
    private float size = 0.0f;
    private int fontWidth = 0;
    private int fontHeight = 0;
    private int textureWidth = 0;
    private int textureHeight = 0;

    public final float drawCenteredString(String text, float x, float y, int color) {
        return this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public final float drawCenteredStringNoFormat(String text, float x, float y, int color) {
        return this.drawStringNoFormat(text, x - (float)(this.getStringWidth(text) / 2), y, color, false);
    }

    public final void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        this.drawStringWithShadow(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    private static final String processString(String text) {
        String str = "";
        for (char c : text.toCharArray()) {
            if (c >= '썐' && c <= '' || c == '⚽') continue;
            str = str + c;
        }
        text = str.replace("§r", "").replace('▬', '=').replace('❤', '♥').replace('⋆', '☆').replace('☠', '☆').replace('✰', '☆').replace("✫", "☆").replace("✙", "+");
        text = text.replace('⬅', '←').replace('⬆', '↑').replace('⬇', '↓').replace('➡', '→').replace('⬈', '↗').replace('⬋', '↙').replace('⬉', '↖').replace('⬊', '↘');
        return text;
    }

    public RapeMasterFontManager(Font font) {
        this.font = font;
        this.size = font.getSize2D();
        Arrays.fill(this.textures, -1);
        Rectangle2D maxBounds = font.getMaxCharBounds(this.context);
        this.fontWidth = (int)Math.ceil(maxBounds.getWidth());
        this.fontHeight = (int)Math.ceil(maxBounds.getHeight());
        if (this.fontWidth > 127 || this.fontHeight > 127) {
            throw new IllegalArgumentException("Font size to large!");
        }
        this.textureWidth = this.resizeToOpenGLSupportResolution(this.fontWidth * 16);
        this.textureHeight = this.resizeToOpenGLSupportResolution(this.fontHeight * 16);
    }

    public static final Font getFont() {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("pride/font/wqy_microhei.ttf")).getInputStream();
            font = Font.createFont(0, is);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = Font.decode("default");
        }
        return font;
    }

    public RapeMasterFontManager() {
    }

    public final int getFontHeight() {
        return this.fontHeight / 2;
    }

    protected final int drawChar(char chr, float x, float y) {
        int region = chr >> 8;
        int id = chr & 0xFF;
        int xTexCoord = (id & 0xF) * this.fontWidth;
        int yTexCoord = (id >> 4) * this.fontHeight;
        byte width = this.getOrGenerateCharWidthMap(region)[id];
        GlStateManager.bindTexture((int)this.getOrGenerateCharTexture(region));
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        GL11.glBegin((int)7);
        GL11.glTexCoord2d((double)this.wrapTextureCoord(xTexCoord, this.textureWidth), (double)this.wrapTextureCoord(yTexCoord, this.textureHeight));
        GL11.glVertex2f((float)x, (float)y);
        GL11.glTexCoord2d((double)this.wrapTextureCoord(xTexCoord, this.textureWidth), (double)this.wrapTextureCoord(yTexCoord + this.fontHeight, this.textureHeight));
        GL11.glVertex2f((float)x, (float)(y + (float)this.fontHeight));
        GL11.glTexCoord2d((double)this.wrapTextureCoord(xTexCoord + width, this.textureWidth), (double)this.wrapTextureCoord(yTexCoord + this.fontHeight, this.textureHeight));
        GL11.glVertex2f((float)(x + (float)width), (float)(y + (float)this.fontHeight));
        GL11.glTexCoord2d((double)this.wrapTextureCoord(xTexCoord + width, this.textureWidth), (double)this.wrapTextureCoord(yTexCoord, this.textureHeight));
        GL11.glVertex2f((float)(x + (float)width), (float)y);
        GL11.glEnd();
        return width;
    }

    public int drawString(String str, float x, float y, int color) {
        return this.drawString(str, x, y, color, false);
    }

    public final int drawStringNoFormat(String str, float x, float y, int color, boolean darken) {
        str = str.replace("▬", "=");
        y -= 2.0f;
        x *= 2.0f;
        y *= 2.0f;
        y -= 2.0f;
        int offset = 0;
        if (darken) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        if (a == 0.0f) {
            a = 1.0f;
        }
        GlStateManager.color((float)r, (float)g, (float)b, (float)a);
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            char chr = chars[i];
            if (chr == '§' && i != chars.length - 1) {
                if ((color = "0123456789abcdef".indexOf(chars[++i])) == -1 || !darken) continue;
                color |= 0x10;
                continue;
            }
            offset += this.drawChar(chr, x + (float)offset, y);
        }
        GL11.glPopMatrix();
        return offset;
    }

    public final int drawString(String str, float x, float y, int color, boolean darken) {
        str = str.replace("▬", "=");
        y -= 2.0f;
        x *= 2.0f;
        y *= 2.0f;
        y -= 2.0f;
        int offset = 0;
        if (darken) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        if (a == 0.0f) {
            a = 1.0f;
        }
        GlStateManager.color((float)r, (float)g, (float)b, (float)a);
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            char chr = chars[i];
            if (chr == '§' && i != chars.length - 1) {
                if ((color = "0123456789abcdef".indexOf(chars[++i])) == -1) continue;
                if (darken) {
                    color |= 0x10;
                }
                color = colorCode[color];
                r = (float)(color >> 16 & 0xFF) / 255.0f;
                g = (float)(color >> 8 & 0xFF) / 255.0f;
                b = (float)(color & 0xFF) / 255.0f;
                GlStateManager.color((float)r, (float)g, (float)b, (float)a);
                continue;
            }
            offset += this.drawChar(chr, x + (float)offset, y);
        }
        GL11.glPopMatrix();
        return offset;
    }

    public final int getStringWidth(String str) {
        int width = 0;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            char chr = chars[i];
            if (chr == '§' && i != chars.length - 1) continue;
            width += this.getOrGenerateCharWidthMap(chr >> 8)[chr & 0xFF];
        }
        return width / 2;
    }

    public final float getSize() {
        return this.size;
    }

    private final int generateCharTexture(int id) {
        int textureId = GL11.glGenTextures();
        int offset = id << 8;
        BufferedImage img = new BufferedImage(this.textureWidth, this.textureHeight, 2);
        Graphics2D g = (Graphics2D)img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setFont(this.font);
        g.setColor(Color.WHITE);
        FontMetrics fontMetrics = g.getFontMetrics();
        for (int y = 0; y < 16; ++y) {
            for (int x = 0; x < 16; ++x) {
                String chr = String.valueOf((char)(y << 4 | x | offset));
                g.drawString(chr, x * this.fontWidth, y * this.fontHeight + fontMetrics.getAscent());
            }
        }
        GL11.glBindTexture((int)3553, (int)textureId);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glTexImage2D((int)3553, (int)0, (int)6408, (int)this.textureWidth, (int)this.textureHeight, (int)0, (int)6408, (int)5121, (ByteBuffer)RapeMasterFontManager.imageToBuffer(img));
        return textureId;
    }

    private final int getOrGenerateCharTexture(int id) {
        if (this.textures[id] == -1) {
            this.textures[id] = this.generateCharTexture(id);
            return this.textures[id];
        }
        return this.textures[id];
    }

    private final int resizeToOpenGLSupportResolution(int size) {
        int power = 0;
        while (size > 1 << power) {
            ++power;
        }
        return 1 << power;
    }

    private final byte[] generateCharWidthMap(int id) {
        int offset = id << 8;
        byte[] widthmap = new byte[256];
        for (int i = 0; i < widthmap.length; ++i) {
            widthmap[i] = (byte)Math.ceil(this.font.getStringBounds(String.valueOf((char)(i | offset)), this.context).getWidth());
        }
        return widthmap;
    }

    private final byte[] getOrGenerateCharWidthMap(int id) {
        if (this.charwidth[id] == null) {
            this.charwidth[id] = this.generateCharWidthMap(id);
            return this.charwidth[id];
        }
        return this.charwidth[id];
    }

    private final double wrapTextureCoord(int coord, int size) {
        return (double)coord / (double)size;
    }

    private static final ByteBuffer imageToBuffer(BufferedImage img) {
        int[] arr = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * arr.length);
        for (int i : arr) {
            buf.putInt(i << 8 | i >> 24 & 0xFF);
        }
        buf.flip();
        return buf;
    }

    protected final void finalize() throws Throwable {
        for (int textureId : this.textures) {
            if (textureId == -1) continue;
            GL11.glDeleteTextures((int)textureId);
        }
    }

    public final void drawStringWithShadow(String newstr, float i, float i1, int rgb) {
        this.drawString(newstr, i + 1.0f, i1 + 1.0f, rgb, true);
        this.drawString(newstr, i, i1, rgb);
    }

    public final void drawLimitedString(String text, float x, float y, int color, float maxWidth) {
        this.drawLimitedStringWithAlpha(text, x, y, color, (float)(color >> 24 & 0xFF) / 255.0f, maxWidth);
    }

    public final void drawLimitedStringWithAlpha(String text, float x, float y, int color, float alpha, float maxWidth) {
        y *= 2.0f;
        float originalX = x *= 2.0f;
        float curWidth = 0.0f;
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        boolean wasBlend = GL11.glGetBoolean((int)3042);
        GlStateManager.enableAlpha();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3553);
        int currentColor = color;
        char[] characters = text.toCharArray();
        int index = 0;
        for (char c : characters) {
            if (c == '\r') {
                x = originalX;
            }
            if (c == '\n') {
                y += (float)this.getFontHeight() * 2.0f;
            }
            if (c != '§' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '§')) {
                if (index >= 1 && characters[index - 1] == '§') continue;
                GL11.glPushMatrix();
                this.drawString(Character.toString(c), x, y, RenderUtils.reAlpha(currentColor, alpha), false);
                GL11.glPopMatrix();
                curWidth += (float)this.getStringWidth(Character.toString(c)) * 2.0f;
                x += (float)this.getStringWidth(Character.toString(c)) * 2.0f;
                if (curWidth > maxWidth) {
                    break;
                }
            } else if (c == ' ') {
                x += (float)this.getStringWidth(" ");
            } else if (c == '§' && index != characters.length - 1) {
                int codeIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                if (codeIndex < 0) continue;
                if (codeIndex < 16) {
                    currentColor = colorCode[codeIndex];
                } else if (codeIndex == 21) {
                    currentColor = Color.WHITE.getRGB();
                }
            }
            ++index;
        }
        if (!wasBlend) {
            GL11.glDisable((int)3042);
        }
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public final void drawOutlinedString(String str, float x, float y, int internalCol, int externalCol) {
        this.drawString(str, x - 0.5f, y, externalCol);
        this.drawString(str, x + 0.5f, y, externalCol);
        this.drawString(str, x, y - 0.5f, externalCol);
        this.drawString(str, x, y + 0.5f, externalCol);
        this.drawString(str, x, y, internalCol);
    }

    static {
        Font font = RapeMasterFontManager.getFont();
        F11 = new RapeMasterFontManager(font.deriveFont(11.0f));
        F12 = new RapeMasterFontManager(font.deriveFont(12.0f));
        F13 = new RapeMasterFontManager(font.deriveFont(13.0f));
        F14 = new RapeMasterFontManager(font.deriveFont(14.0f));
        F15 = new RapeMasterFontManager(font.deriveFont(15.0f));
        F16 = new RapeMasterFontManager(font.deriveFont(16.0f));
        F18 = new RapeMasterFontManager(font.deriveFont(18.0f));
        F22 = new RapeMasterFontManager(font.deriveFont(22.0f));
        F24 = new RapeMasterFontManager(font.deriveFont(24.0f));
        F35 = new RapeMasterFontManager(font.deriveFont(35.0f));
        F40 = new RapeMasterFontManager(font.deriveFont(40.0f));
        for (int i = 0; i < 32; ++i) {
            int base = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + base;
            int g = (i >> 1 & 1) * 170 + base;
            int b = (i & 1) * 170 + base;
            if (i == 6) {
                r += 85;
            }
            if (i >= 16) {
                r /= 4;
                g /= 4;
                b /= 4;
            }
            RapeMasterFontManager.colorCode[i] = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
        }
    }
}
