package slavikcodd3r.rainbow.utils;

import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.Minecraft;
import java.awt.Color;
import java.io.InputStream;
import java.io.File;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Font;
import net.minecraft.util.ResourceLocation;
import slavikcodd3r.rainbow.command.commands.Friend;
import slavikcodd3r.rainbow.friend.FriendManager;

import java.util.regex.Pattern;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.awt.image.BufferedImage;

public class FontUtils
{
    private BufferedImage bufferedImage;
    private DynamicTexture dynamicTexture;
    private final int endChar;
    private float extraSpacing;
    private final float fontSize;
    private final Pattern patternControlCode;
    private final Pattern patternUnsupported;
    private ResourceLocation resourceLocation;
    private final int startChar;
    private Font theFont;
    private Graphics2D theGraphics;
    private FontMetrics theMetrics;
    private final float[] xPos;
    private final float[] yPos;
    
    public FontUtils(final Object font, final float size) {
        this(font, size, 0.0f);
    }
    
    public FontUtils(final Object font, final float size, final float spacing) {
        this.extraSpacing = 0.0f;
        this.patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OG]");
        this.patternUnsupported = Pattern.compile("(?i)\\u00A7[K-O]");
        this.fontSize = size;
        this.startChar = 32;
        this.endChar = 255;
        this.extraSpacing = spacing;
        this.xPos = new float[this.endChar - this.startChar];
        this.yPos = new float[this.endChar - this.startChar];
        this.setupGraphics2D();
        this.createFont(font, size);
    }
    
    private void createFont(final Object font, final float size) {
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
        }
        this.theGraphics.setFont(this.theFont);
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
    }
    
    private void drawChar(final char character, final float x, final float y) {
        final Rectangle2D bounds = this.theMetrics.getStringBounds(Character.toString(character), this.theGraphics);
        this.drawTexturedModalRect(x, y, this.xPos[character - this.startChar], this.yPos[character - this.startChar], (float)bounds.getWidth(), (float)bounds.getHeight() + this.theMetrics.getMaxDescent() + 1.0f);
    }
    
    private void drawer(final String text, float x, float y, final int color) {
        x *= 2.0f;
        y *= 2.0f;
        GlStateManager.func_179098_w();
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
                final int colorCode = Minecraft.getMinecraft().fontRendererObj.func_175064_b(oneMore);
                if (colorCode != 16777215) {
                    GlStateManager.color((colorCode >> 16) / 255.0f, (colorCode >> 8 & 0xFF) / 255.0f, (colorCode & 0xFF) / 255.0f, alpha);
                }
                else if (oneMore == 'r') {
                    GlStateManager.color(red, green, blue, alpha);
                }
                else if (oneMore == 'g') {
                    GlStateManager.color(0.3f, 0.7f, 1.0f, alpha);
                }
                ++i;
            }
            else {
                char character = text.charAt(i);
                try {
                    this.drawChar(character, x, y);
                }
                catch (ArrayIndexOutOfBoundsException exception) {
                    character = '?';
                    this.drawChar(character, x, y);
                }
                x += this.getStringWidth(Character.toString(character)) * 2.0f;
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public void drawString(String text, final float x, final float y, final int color, final int shadowColor) {
        text.replace("VeltPvP", "NoStrike");
        text.replace("Arcane", "NoStrike");
        text.replace("FaithfulMC", "NoStrike");
        text.replace("Faithful", "NoStrike");
        text.replace("SPhoenix", "Protected Faction");
        text.replace("SilverPhoenix", "Protected Faction");
        this.drawString(text, x, y, FontType.SHADOW_THICK, color, shadowColor);
    }
    
    public void drawString(String text, final float x, final float y, final FontType fontType, final int color) {
        text.replace("VeltPvP", "NoStrike");
        text.replace("Arcane", "NoStrike");
        text.replace("FaithfulMC", "NoStrike");
        text.replace("Faithful", "NoStrike");
        text.replace("SPhoenix", "Protected Faction");
        text.replace("SilverPhoenix", "Protected Faction");

        this.drawString(text, x, y, fontType, color, -16777216);
    }
    
    public void drawString(String text, final float x, final float y, final FontType fontType, final int color, final int color2) {
        text.replace("VeltPvP", "NoStrike");
        text.replace("Arcane", "NoStrike");
        text.replace("FaithfulMC", "NoStrike");
        text.replace("Faithful", "NoStrike");
        text.replace("SPhoenix", "Protected Faction");
        text.replace("SilverPhoenix", "Protected Faction");
        text = this.stripUnsupported(text);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        final String colorless = this.stripControlCodes(text);
        switch (fontType) {
            case SHADOW_THICK: {
                this.drawer(colorless, x + 1.0f, y + 1.0f, color2);
                break;
            }
            case SHADOW_THIN: {
                this.drawer(colorless, x + 0.5f, y + 0.5f, color2);
                break;
            }
            case OUTLINE_THIN: {
                this.drawer(colorless, x + 0.5f, y, color2);
                this.drawer(colorless, x - 0.5f, y, color2);
                this.drawer(colorless, x, y + 0.5f, color2);
                this.drawer(colorless, x, y - 0.5f, color2);
                break;
            }
            case EMBOSS_BOTTOM: {
                this.drawer(colorless, x, y + 0.5f, color2);
                break;
            }
            case EMBOSS_TOP: {
                this.drawer(colorless, x, y - 0.5f, color2);
                break;
            }
        }
        this.drawer(text, x, y, color);
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }
    
    private void drawTexturedModalRect(final float x, final float y, final float u, final float v, final float width, final float height) {
        final float scale = 0.0039063f;
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        final Tessellator tessellator = Tessellator.getInstance();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertexWithUV(x + 0.0f, y + height, 0.0, (u + 0.0f) * 0.0039063f, (v + height) * 0.0039063f);
        worldRenderer.addVertexWithUV(x + width, y + height, 0.0, (u + width) * 0.0039063f, (v + height) * 0.0039063f);
        worldRenderer.addVertexWithUV(x + width, y + 0.0f, 0.0, (u + width) * 0.0039063f, (v + 0.0f) * 0.0039063f);
        worldRenderer.addVertexWithUV(x + 0.0f, y + 0.0f, 0.0, (u + 0.0f) * 0.0039063f, (v + 0.0f) * 0.0039063f);
        tessellator.draw();
    }
    
    private Rectangle2D getBounds(final String text) {
        return this.theMetrics.getStringBounds(text, this.theGraphics);
    }
    
    public Font getFont() {
        return this.theFont;
    }
    
    private String getFormatFromString(final String par0Str) {
        String var1 = "";
        int var2 = -1;
        final int var3 = par0Str.length();
        while ((var2 = par0Str.indexOf(167, var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                final char var4 = par0Str.charAt(var2 + 1);
                if (this.isFormatColor(var4)) {
                    var1 = "§" + var4;
                }
                else {
                    if (!this.isFormatSpecial(var4)) {
                        continue;
                    }
                    var1 = String.valueOf(var1) + "§" + var4;
                }
            }
        }
        return var1;
    }
    
    public Graphics2D getGraphics() {
        return this.theGraphics;
    }
    
    public float getStringHeight(final String text) {
        return (float)this.getBounds(text).getHeight() / 2.0f;
    }
    
    public float getStringWidth(final String text) {
        return (float)(this.getBounds(text).getWidth() + this.extraSpacing) / 2.0f;
    }
    
    private boolean isFormatColor(final char par0) {
        return (par0 >= '0' && par0 <= '9') || (par0 >= 'a' && par0 <= 'f') || (par0 >= 'A' && par0 <= 'F');
    }
    
    private boolean isFormatSpecial(final char par0) {
        return (par0 >= 'k' && par0 <= 'o') || (par0 >= 'K' && par0 <= 'O') || par0 == 'r' || par0 == 'R';
    }
    
    public List listFormattedStringToWidth(final String s, final int width) {
        return Arrays.asList(this.wrapFormattedStringToWidth(s, (float)width).split("\n"));
    }
    
    private void setupGraphics2D() {
        this.bufferedImage = new BufferedImage(256, 256, 2);
        (this.theGraphics = (Graphics2D)this.bufferedImage.getGraphics()).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
    
    private int sizeStringToWidth(final String par1Str, final float par2) {
        final int var3 = par1Str.length();
        float var4 = 0.0f;
        int var5 = 0;
        int var6 = -1;
        boolean var7 = false;
        while (var5 < var3) {
            final char var8 = par1Str.charAt(var5);
            Label_0195: {
                switch (var8) {
                    case '\n': {
                        --var5;
                        break Label_0195;
                    }
                    case '§': {
                        if (var5 >= var3 - 1) {
                            break Label_0195;
                        }
                        ++var5;
                        final char var9 = par1Str.charAt(var5);
                        if (var9 == 'l' || var9 == 'L') {
                            var7 = true;
                            break Label_0195;
                        }
                        if (var9 == 'r' || var9 == 'R' || this.isFormatColor(var9)) {
                            var7 = false;
                        }
                        break Label_0195;
                    }
                    case ' ':
                    case '-':
                    case ':':
                    case '_': {
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
    
    public String stripControlCodes(final String s) {
        return this.patternControlCode.matcher(s).replaceAll("");
    }
    
    public String stripUnsupported(final String s) {
        return this.patternUnsupported.matcher(s).replaceAll("");
    }
    
    public String wrapFormattedStringToWidth(final String s, final float width) {
        final int wrapWidth = this.sizeStringToWidth(s, width);
        if (s.length() <= wrapWidth) {
            return s;
        }
        final String split = s.substring(0, wrapWidth);
        final String split2 = String.valueOf(this.getFormatFromString(split)) + s.substring(wrapWidth + ((s.charAt(wrapWidth) == ' ' || s.charAt(wrapWidth) == '\n') ? 1 : 0));
        try {
            return String.valueOf(split) + "\n" + this.wrapFormattedStringToWidth(split2, width);
        }
        catch (Exception e) {
            System.out.println("Cannot wrap string to width.");
            return "";
        }
    }
    
    public enum FontType
    {
        NORMAL("NORMAL", 0), 
        EMBOSS_TOP("EMBOSS_TOP", 1), 
        EMBOSS_BOTTOM("EMBOSS_BOTTOM", 2), 
        OUTLINE_THIN("OUTLINE_THIN", 3), 
        SHADOW_THICK("SHADOW_THICK", 4), 
        SHADOW_THIN("SHADOW_THIN", 5);
        
        private FontType(final String s, final int n) {
        }
    }
}
