// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util.render;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import exhibition.util.misc.ChatUtil;
import java.util.Arrays;
import java.util.List;
import java.awt.Graphics;
import net.minecraft.util.StringUtils;
import java.awt.geom.Rectangle2D;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.Minecraft;
import java.awt.Color;
import java.io.InputStream;
import java.io.File;
import java.awt.RenderingHints;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.awt.image.BufferedImage;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.util.regex.Pattern;

public class NharFont
{
    private final Pattern patternControlCode;
    private final Pattern patternUnsupported;
    private Font theFont;
    private boolean antiAlias;
    private Graphics2D theGraphics;
    private FontMetrics theMetrics;
    private float fontSize;
    private int startChar;
    private int endChar;
    private float[] xPos;
    private float[] yPos;
    private BufferedImage bufferedImage;
    private float extraSpacing;
    private DynamicTexture dynamicTexture;
    private ResourceLocation resourceLocation;
    
    public NharFont(final Object font, final float size) {
        this(font, size, 0.0f);
    }
    
    public NharFont(final Object font, final float size, final float spacing) {
        this.patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OG]");
        this.patternUnsupported = Pattern.compile("(?i)\\u00A7[K-O]");
        this.antiAlias = true;
        this.extraSpacing = 0.0f;
        this.fontSize = size;
        this.startChar = 32;
        this.endChar = 255;
        this.extraSpacing = spacing;
        this.xPos = new float[this.endChar - this.startChar];
        this.yPos = new float[this.endChar - this.startChar];
        this.setupGraphics2D();
        this.createFont(font, size);
    }
    
    private void setupGraphics2D() {
        GlStateManager.pushMatrix();
        this.bufferedImage = new BufferedImage(256, 256, 2);
        this.theGraphics = (Graphics2D)this.bufferedImage.getGraphics();
        if (this.antiAlias) {
            this.theGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        else {
            this.theGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        GlStateManager.popMatrix();
    }
    
    private void createFont(final Object font, final float size) {
        GlStateManager.pushMatrix();
        try {
            if (font instanceof Font) {
                this.theFont = (Font)font;
            }
            else if (font instanceof File) {
                this.theFont = Font.createFont(0, (File)font).deriveFont(size);
            }
            else if (font instanceof InputStream) {
                this.theFont = Font.createFont(0, (InputStream)font).deriveFont(size);
            }
            else if (font instanceof String) {
                this.theFont = new Font((String)font, 0, Math.round(size));
            }
            else {
                this.theFont = new Font("Verdana", 0, Math.round(size));
            }
            this.theGraphics.setFont(this.theFont);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.theFont = new Font("Verdana", 0, Math.round(size));
            this.theGraphics.setFont(this.theFont);
        }
        this.theGraphics.setColor(new Color(255, 255, 255, 0));
        this.theGraphics.fillRect(0, 0, 256, 256);
        this.theGraphics.setColor(Color.white);
        this.theMetrics = this.theGraphics.getFontMetrics();
        float x = 5.0f;
        float y = 5.0f;
        for (int i = this.startChar; i < this.endChar; ++i) {
            this.theGraphics.drawString(Character.toString((char)i), x, y + this.theMetrics.getAscent());
            this.xPos[i - this.startChar] = x;
            this.yPos[i - this.startChar] = y - this.theMetrics.getMaxDescent();
            x += this.theMetrics.stringWidth(Character.toString((char)i)) + 2.0f;
            if (x >= 250 - this.theMetrics.getMaxAdvance()) {
                x = 5.0f;
                y += this.theMetrics.getMaxAscent() + this.theMetrics.getMaxDescent() + this.fontSize / 2.0f;
            }
        }
        final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        final String string = "font" + font.toString() + size;
        final DynamicTexture dynamicTexture = new DynamicTexture(this.bufferedImage);
        this.dynamicTexture = dynamicTexture;
        this.resourceLocation = textureManager.getDynamicTextureLocation(string, dynamicTexture);
        GlStateManager.popMatrix();
    }
    
    public void drawCenteredString(final String text, final float x, final float y, final FontType fontType, final int color, final int color2) {
        this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, fontType, color, color2);
    }
    
    public void drawStringWithShadow(final String text, final float x, final float y, final int color) {
        this.drawString(text, x, y, FontType.SHADOW_THIN, color, -16777216);
    }
    
    public void drawString(String text, final float x, final float y, final FontType fontType, final int color, final int color2) {
        GlStateManager.pushMatrix();
        try {
            text = this.stripUnsupported(text);
        }
        catch (Exception e) {
            return;
        }
        GL11.glEnable(3042);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        final String text2 = this.stripControlCodes(text);
        switch (fontType.ordinal()) {
            case 1: {
                this.drawer(text2, x + 0.5f, y, color2);
                this.drawer(text2, x - 0.5f, y, color2);
                this.drawer(text2, x, y + 0.5f, color2);
                this.drawer(text2, x, y - 0.5f, color2);
                break;
            }
            case 2: {
                this.drawer(text2, x + 0.5f, y + 0.5f, color2);
                break;
            }
            case 3: {
                this.drawer(text2, x + 1.0f, y + 1.0f, color2);
                break;
            }
            case 4: {
                this.drawer(text2, x, y + 0.5f, color2);
                break;
            }
            case 5: {
                this.drawer(text2, x, y - 0.5f, color2);
                break;
            }
        }
        this.drawer(text, x, y, color);
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }
    
    private void drawer(final String text, float x, float y, final int color) {
        y -= 5.0f;
        x *= 2.0f;
        y *= 2.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
        final float startX = x;
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) == '§' && i + 1 < text.length()) {
                final char oneMore = Character.toLowerCase(text.charAt(i + 1));
                if (oneMore == 'n') {
                    y += this.theMetrics.getAscent() + 2;
                    x = startX;
                }
                final int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
                if (colorCode < 16) {
                    try {
                        final int newColor = Minecraft.getMinecraft().fontRendererObj.colorCode[colorCode];
                        GlStateManager.color((newColor >> 16) / 255.0f, (newColor >> 8 & 0xFF) / 255.0f, (newColor & 0xFF) / 255.0f, alpha);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                else if (oneMore == 'f') {
                    GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
                }
                else if (oneMore == 'r') {
                    GlStateManager.color(red, green, blue, alpha);
                }
                else if (oneMore == 'g') {
                    GlStateManager.color(0.47f, 0.67f, 0.27f, alpha);
                }
                ++i;
            }
            else {
                try {
                    final char c = text.charAt(i);
                    this.drawChar(c, x, y);
                    x += this.getStringWidth(Character.toString(c)) * 2.0f;
                }
                catch (ArrayIndexOutOfBoundsException indexException) {
                    text.charAt(i);
                }
            }
        }
        GlStateManager.popMatrix();
    }
    
    public float getStringWidth(final String text) {
        try {
            return (float)(this.getBounds(text).getWidth() + this.extraSpacing) / 2.0f;
        }
        catch (Exception e) {
            return 0.0f;
        }
    }
    
    public float getStringHeight(final String text) {
        try {
            return (float)this.getBounds(text).getHeight() / 2.0f;
        }
        catch (Exception e) {
            return 0.0f;
        }
    }
    
    private Rectangle2D getBounds(final String text) {
        return this.theMetrics.getStringBounds(StringUtils.stripControlCodes(text), this.theGraphics);
    }
    
    private void drawChar(final char character, final float x, final float y) throws ArrayIndexOutOfBoundsException {
        GlStateManager.pushMatrix();
        final Rectangle2D bounds = this.theMetrics.getStringBounds(Character.toString(character), this.theGraphics);
        this.drawTexturedModalRect(x, y, this.xPos[character - this.startChar], this.yPos[character - this.startChar], (float)bounds.getWidth(), (float)bounds.getHeight() + this.theMetrics.getMaxDescent() + 1.0f);
        GlStateManager.popMatrix();
    }
    
    public List listFormattedStringToWidth(final String s, final int width) {
        return Arrays.asList(this.wrapFormattedStringToWidth(s, width).split("\n"));
    }
    
    String wrapFormattedStringToWidth(final String s, final float width) {
        final int wrapWidth = this.sizeStringToWidth(s, width);
        if (s.length() <= wrapWidth) {
            return s;
        }
        final String split = s.substring(0, wrapWidth);
        final String split2 = this.getFormatFromString(split) + s.substring(wrapWidth + ((s.charAt(wrapWidth) == ' ' || s.charAt(wrapWidth) == '\n') ? 1 : 0));
        try {
            return split + "\n" + this.wrapFormattedStringToWidth(split2, width);
        }
        catch (Exception e) {
            ChatUtil.printChat("Cannot wrap string to width.");
            return "";
        }
    }
    
    private int sizeStringToWidth(final String par1Str, final float par2) {
        final int var3 = par1Str.length();
        float var4 = 0.0f;
        int var5 = 0;
        int var6 = -1;
        boolean var7 = false;
        while (var5 < var3) {
            final char var8 = par1Str.charAt(var5);
            Label_0211: {
                switch (var8) {
                    case '\n': {
                        --var5;
                        break Label_0211;
                    }
                    case '§': {
                        if (var5 < var3 - 1) {
                            ++var5;
                            final char var9 = par1Str.charAt(var5);
                            if (var9 != 'l' && var9 != 'L') {
                                if (var9 == 'r' || var9 == 'R' || this.isFormatColor(var9)) {
                                    var7 = false;
                                }
                            }
                            else {
                                var7 = true;
                            }
                        }
                        break Label_0211;
                    }
                    case ' ': {
                        var6 = var5;
                    }
                    case '-': {
                        var6 = var5;
                    }
                    case '_': {
                        var6 = var5;
                    }
                    case ':': {
                        var6 = var5;
                        break;
                    }
                }
                final String text = String.valueOf(var8);
                var4 += this.getStringWidth(text);
                if (var7) {
                    ++var4;
                }
            }
            if (var8 == '\n') {
                var6 = ++var5;
            }
            else if (var4 > par2) {
                break;
            }
            ++var5;
        }
        return (var5 != var3 && var6 != -1 && var6 < var5) ? var6 : var5;
    }
    
    private String getFormatFromString(final String par0Str) {
        String var1 = "";
        int var2 = -1;
        final int var3 = par0Str.length();
        while ((var2 = par0Str.indexOf(167, var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                final char var4 = par0Str.charAt(var2 + 1);
                if (this.isFormatColor(var4)) {
                    var1 = Character.toString('§') + var4;
                }
                else {
                    if (!this.isFormatSpecial(var4)) {
                        continue;
                    }
                    var1 = var1 + Character.toString('§') + var4;
                }
            }
        }
        return var1;
    }
    
    private boolean isFormatColor(final char par0) {
        return (par0 >= '0' && par0 <= '9') || (par0 >= 'a' && par0 <= 'f') || (par0 >= 'A' && par0 <= 'F');
    }
    
    private boolean isFormatSpecial(final char par0) {
        return (par0 >= 'k' && par0 <= 'o') || (par0 >= 'K' && par0 <= 'O') || par0 == 'r' || par0 == 'R';
    }
    
    private void drawTexturedModalRect(final float x, final float y, final float textureX, final float textureY, final float width, final float height) {
        GlStateManager.pushMatrix();
        final float var7 = 0.00390625f;
        final float var8 = 0.00390625f;
        final Tessellator var9 = Tessellator.getInstance();
        final WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(x + 0.0f, y + height, 0.0, (textureX + 0.0f) * var7, (textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + height, 0.0, (textureX + width) * var7, (textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + 0.0f, 0.0, (textureX + width) * var7, (textureY + 0.0f) * var8);
        var10.addVertexWithUV(x + 0.0f, y + 0.0f, 0.0, (textureX + 0.0f) * var7, (textureY + 0.0f) * var8);
        var9.draw();
        GlStateManager.popMatrix();
    }
    
    public String stripControlCodes(final String s) {
        return this.patternControlCode.matcher(s).replaceAll("");
    }
    
    public String stripUnsupported(final String s) {
        return this.patternUnsupported.matcher(s).replaceAll("");
    }
    
    public Graphics2D getGraphics() {
        return this.theGraphics;
    }
    
    public Font getFont() {
        return this.theFont;
    }
    
    public enum FontType
    {
        NORMAL, 
        OUTLINE_THIN, 
        SHADOW_THIN, 
        SHADOW_THICK, 
        EMBOSS_TOP, 
        EMBOSS_BOTTOM;
    }
}
