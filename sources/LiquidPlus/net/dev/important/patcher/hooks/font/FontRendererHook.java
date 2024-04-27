/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$Color
 *  net.minecraft.client.renderer.GlStateManager$TextureState
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.patcher.hooks.font;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import kotlin.Pair;
import net.dev.important.injection.forge.mixins.accessors.FontRendererAccessor;
import net.dev.important.injection.forge.mixins.accessors.GlStateManagerAccessor;
import net.dev.important.modules.module.modules.misc.Patcher;
import net.dev.important.patcher.hooks.font.OptiFineHook;
import net.dev.important.patcher.util.enhancement.EnhancementManager;
import net.dev.important.patcher.util.enhancement.hash.StringHash;
import net.dev.important.patcher.util.enhancement.text.CachedString;
import net.dev.important.patcher.util.enhancement.text.EnhancedFontRenderer;
import net.dev.important.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class FontRendererHook {
    public static boolean forceRefresh = false;
    public static final String characterDictionary = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
    private final EnhancedFontRenderer enhancedFontRenderer = EnhancementManager.getInstance().getEnhancement(EnhancedFontRenderer.class);
    private final FontRenderer fontRenderer;
    private final FontRendererAccessor fontRendererAccessor;
    private final Minecraft mc = Minecraft.func_71410_x();
    private OptiFineHook hook = new OptiFineHook();
    public int glTextureId = -1;
    private int texSheetDim = 256;
    private float fontTexHeight = 16 * this.texSheetDim + 128;
    private float fontTexWidth = 16 * this.texSheetDim;
    private int regularCharDim = 128;
    private boolean drawing = false;
    private static final String COLOR_RESET_PHRASE = "\u00a7r";

    public FontRendererHook(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        this.fontRendererAccessor = (FontRendererAccessor)fontRenderer;
    }

    private void establishSize() {
        Throwable throwable;
        InputStream stream;
        int regWidth = 256;
        for (int i = 0; i < 256; ++i) {
            try {
                stream = this.mc.func_110442_L().func_110536_a(new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", i))).func_110527_b();
                throwable = null;
                try {
                    regWidth = ImageIO.read(stream).getWidth();
                    break;
                }
                catch (Throwable throwable2) {
                    throwable = throwable2;
                    throw throwable2;
                }
                finally {
                    if (stream != null) {
                        if (throwable != null) {
                            try {
                                stream.close();
                            }
                            catch (Throwable throwable3) {
                                throwable.addSuppressed(throwable3);
                            }
                        } else {
                            stream.close();
                        }
                    }
                }
            }
            catch (Exception stream2) {
                continue;
            }
        }
        this.texSheetDim = regWidth;
        int specWidth = 128;
        try {
            stream = this.mc.func_110442_L().func_110536_a(this.fontRendererAccessor.getLocationFontTexture()).func_110527_b();
            throwable = null;
            try {
                specWidth = ImageIO.read(stream).getWidth();
            }
            catch (Throwable throwable4) {
                throwable = throwable4;
                throw throwable4;
            }
            finally {
                if (stream != null) {
                    if (throwable != null) {
                        try {
                            stream.close();
                        }
                        catch (Throwable throwable5) {
                            throwable.addSuppressed(throwable5);
                        }
                    } else {
                        stream.close();
                    }
                }
            }
        }
        catch (IOException e) {
            ClientUtils.getLogger().error("Failed to read font texture while establishing size.", (Throwable)e);
        }
        this.regularCharDim = specWidth;
        this.fontTexHeight = 16 * this.texSheetDim + specWidth;
        this.fontTexWidth = 16 * this.texSheetDim;
    }

    public void create() {
        this.establishSize();
        this.hook = new OptiFineHook();
        forceRefresh = false;
        if (this.glTextureId != -1) {
            GlStateManager.func_179150_h((int)this.glTextureId);
        }
        BufferedImage bufferedImage = new BufferedImage((int)this.fontTexWidth, (int)this.fontTexHeight, 2);
        for (int i = 0; i < 256; ++i) {
            try (InputStream stream = this.mc.func_110442_L().func_110536_a(new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", i))).func_110527_b();){
                bufferedImage.getGraphics().drawImage(ImageIO.read(stream), i / 16 * this.texSheetDim, i % 16 * this.texSheetDim, null);
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        try (InputStream stream = this.mc.func_110442_L().func_110536_a(this.fontRendererAccessor.getLocationFontTexture()).func_110527_b();){
            bufferedImage.getGraphics().drawImage(ImageIO.read(stream), 0, 16 * this.texSheetDim, null);
        }
        catch (IOException e) {
            ClientUtils.getLogger().error("Failed to draw texture sheet.", (Throwable)e);
        }
        this.glTextureId = new DynamicTexture(bufferedImage).func_110552_b();
    }

    private void deleteTextureId() {
        if (this.glTextureId != -1) {
            GlStateManager.func_179150_h((int)this.glTextureId);
            this.glTextureId = -1;
        }
    }

    public static String clearColorReset(String text) {
        int e;
        int startIndex;
        int endIndex = text.length();
        for (startIndex = 0; text.indexOf(COLOR_RESET_PHRASE, startIndex) == startIndex; startIndex += 2) {
        }
        while ((e = text.lastIndexOf(COLOR_RESET_PHRASE, endIndex - 1)) == endIndex - 2 && e != -1) {
            endIndex -= 2;
        }
        if (endIndex < startIndex) {
            endIndex = startIndex;
        }
        return text.substring(startIndex, endIndex);
    }

    public boolean renderStringAtPos(String text, boolean shadow) {
        boolean hasStyle;
        CachedString cachedString;
        if (this.fontRendererAccessor.getRenderEngine() == null || !((Boolean)Patcher.betterFontRenderer.get()).booleanValue()) {
            this.deleteTextureId();
            return false;
        }
        if (this.glTextureId == -1 || forceRefresh) {
            this.create();
        }
        if ((text = FontRendererHook.clearColorReset(text)).isEmpty()) {
            return false;
        }
        float posX = this.fontRendererAccessor.getPosX();
        float posY = this.fontRendererAccessor.getPosY();
        this.fontRendererAccessor.setPosY(0.0f);
        this.fontRendererAccessor.setPosX(0.0f);
        float red2 = this.fontRendererAccessor.getRed();
        float green2 = this.fontRendererAccessor.getGreen();
        float blue2 = this.fontRendererAccessor.getBlue();
        float alpha2 = this.fontRendererAccessor.getAlpha();
        GlStateManager.func_179144_i((int)this.glTextureId);
        GlStateManager.func_179109_b((float)posX, (float)posY, (float)0.0f);
        GlStateManager.TextureState[] textureStates = GlStateManagerAccessor.getTextureState();
        GlStateManager.TextureState textureState = textureStates[GlStateManagerAccessor.getActiveTextureUnit()];
        StringHash hash = new StringHash(text, red2, green2, blue2, alpha2, shadow);
        CachedString cachedString2 = cachedString = (Boolean)Patcher.betterFontRendererStringCache.get() != false ? this.enhancedFontRenderer.get(hash) : null;
        if (cachedString != null) {
            GlStateManager.func_179131_c((float)red2, (float)green2, (float)blue2, (float)alpha2);
            GlStateManager.func_179148_o((int)cachedString.getListId());
            textureState.field_179059_b = this.glTextureId;
            GlStateManager.Color colorState = GlStateManagerAccessor.getColorState();
            colorState.field_179195_a = cachedString.getLastRed();
            colorState.field_179193_b = cachedString.getLastGreen();
            colorState.field_179194_c = cachedString.getLastBlue();
            colorState.field_179192_d = cachedString.getLastAlpha();
            GlStateManager.func_179109_b((float)(-posX), (float)(-posY), (float)0.0f);
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.fontRendererAccessor.setPosX(posX + cachedString.getWidth());
            this.fontRendererAccessor.setPosY(posY + cachedString.getHeight());
            return true;
        }
        int list = 0;
        textureState.field_179059_b = this.glTextureId;
        GlStateManager.func_179117_G();
        if (((Boolean)Patcher.betterFontRendererStringCache.get()).booleanValue()) {
            list = this.enhancedFontRenderer.getGlList();
            GL11.glNewList((int)list, (int)4865);
        }
        boolean obfuscated = false;
        CachedString value = new CachedString(text, list, this.fontRendererAccessor.getPosX() - posX, this.fontRendererAccessor.getPosY() - posY);
        LinkedList<RenderPair> underline = new LinkedList<RenderPair>();
        LinkedList<RenderPair> strikethrough = new LinkedList<RenderPair>();
        value.setLastRed(red2);
        value.setLastGreen(green2);
        value.setLastBlue(blue2);
        value.setLastAlpha(alpha2);
        for (int messageChar = 0; messageChar < text.length(); ++messageChar) {
            boolean small;
            int obfuscationIndex;
            char letter = text.charAt(messageChar);
            if (letter == '\u00a7' && messageChar + 1 < text.length()) {
                int styleIndex = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(messageChar + 1));
                if (styleIndex < 16) {
                    this.fontRendererAccessor.setStrikethroughStyle(false);
                    this.fontRendererAccessor.setUnderlineStyle(false);
                    this.fontRendererAccessor.setItalicStyle(false);
                    this.fontRendererAccessor.setRandomStyle(false);
                    this.fontRendererAccessor.setBoldStyle(false);
                    if (styleIndex < 0) {
                        styleIndex = 15;
                    }
                    if (shadow) {
                        styleIndex += 16;
                    }
                    int currentColorIndex = this.fontRendererAccessor.getColorCode()[styleIndex];
                    this.fontRendererAccessor.setTextColor(currentColorIndex);
                    float colorRed = (float)(currentColorIndex >> 16) / 255.0f;
                    float colorGreen = (float)(currentColorIndex >> 8 & 0xFF) / 255.0f;
                    float colorBlue = (float)(currentColorIndex & 0xFF) / 255.0f;
                    GlStateManager.func_179131_c((float)colorRed, (float)colorGreen, (float)colorBlue, (float)alpha2);
                    value.setLastAlpha(alpha2);
                    value.setLastGreen(colorGreen);
                    value.setLastBlue(colorBlue);
                    value.setLastRed(colorRed);
                } else if (styleIndex == 16) {
                    this.fontRendererAccessor.setRandomStyle(true);
                    obfuscated = true;
                } else if (styleIndex == 17) {
                    this.fontRendererAccessor.setBoldStyle(true);
                } else if (styleIndex == 18) {
                    this.fontRendererAccessor.setStrikethroughStyle(true);
                } else if (styleIndex == 19) {
                    this.fontRendererAccessor.setUnderlineStyle(true);
                } else if (styleIndex == 20) {
                    this.fontRendererAccessor.setItalicStyle(true);
                } else {
                    this.fontRendererAccessor.setRandomStyle(false);
                    this.fontRendererAccessor.setBoldStyle(false);
                    this.fontRendererAccessor.setStrikethroughStyle(false);
                    this.fontRendererAccessor.setUnderlineStyle(false);
                    this.fontRendererAccessor.setItalicStyle(false);
                    GlStateManager.func_179131_c((float)red2, (float)green2, (float)blue2, (float)alpha2);
                    value.setLastGreen(green2);
                    value.setLastAlpha(alpha2);
                    value.setLastBlue(blue2);
                    value.setLastRed(red2);
                }
                ++messageChar;
                continue;
            }
            int n = obfuscationIndex = shadow || this.fontRendererAccessor.isRandomStyle() ? characterDictionary.indexOf(letter) : 0;
            if (this.fontRendererAccessor.isRandomStyle() && obfuscationIndex != -1) {
                char charIndex;
                float charWidthFloat = this.getCharWidthFloat(letter);
                while (charWidthFloat != this.getCharWidthFloat(charIndex = characterDictionary.charAt(obfuscationIndex = this.fontRenderer.field_78289_c.nextInt(characterDictionary.length())))) {
                }
                letter = charIndex;
            }
            boolean unicode = this.fontRenderer.func_82883_a();
            float boldWidth = this.getBoldOffset(obfuscationIndex);
            boolean bl = small = (letter == '\u0000' || obfuscationIndex == -1 || unicode) && shadow;
            if (small) {
                this.fontRendererAccessor.setPosX(this.fontRendererAccessor.getPosX() - boldWidth);
                this.fontRendererAccessor.setPosY(this.fontRendererAccessor.getPosY() - boldWidth);
            }
            float effectiveWidth = this.renderChar(letter, this.fontRendererAccessor.isItalicStyle());
            if (small) {
                this.fontRendererAccessor.setPosX(this.fontRendererAccessor.getPosX() + boldWidth);
                this.fontRendererAccessor.setPosY(this.fontRendererAccessor.getPosY() + boldWidth);
            }
            if (this.fontRendererAccessor.isBoldStyle()) {
                this.fontRendererAccessor.setPosX(this.fontRendererAccessor.getPosX() + boldWidth);
                if (small) {
                    this.fontRendererAccessor.setPosX(this.fontRendererAccessor.getPosX() - boldWidth);
                    this.fontRendererAccessor.setPosY(this.fontRendererAccessor.getPosY() - boldWidth);
                }
                this.renderChar(letter, this.fontRendererAccessor.isItalicStyle());
                this.fontRendererAccessor.setPosX(this.fontRendererAccessor.getPosX() - boldWidth);
                if (small) {
                    this.fontRendererAccessor.setPosX(this.fontRendererAccessor.getPosX() + boldWidth);
                    this.fontRendererAccessor.setPosY(this.fontRendererAccessor.getPosY() + boldWidth);
                }
                effectiveWidth += 1.0f;
            }
            if (this.fontRendererAccessor.isStrikethroughStyle()) {
                this.adjustOrAppend(strikethrough, this.fontRendererAccessor.getPosX(), effectiveWidth, value.getLastRed(), value.getLastGreen(), value.getLastBlue(), value.getLastAlpha());
            }
            if (this.fontRendererAccessor.isUnderlineStyle()) {
                this.adjustOrAppend(underline, this.fontRendererAccessor.getPosX(), effectiveWidth, value.getLastRed(), value.getLastGreen(), value.getLastBlue(), value.getLastAlpha());
            }
            this.fontRendererAccessor.setPosX(this.fontRendererAccessor.getPosX() + (float)((int)effectiveWidth));
        }
        this.endDrawing();
        boolean bl = hasStyle = underline.size() > 0 || strikethrough.size() > 0;
        if (hasStyle) {
            GlStateManager.func_179090_x();
            GL11.glBegin((int)7);
            for (RenderPair renderPair : strikethrough) {
                GlStateManager.func_179131_c((float)renderPair.red, (float)renderPair.green, (float)renderPair.blue, (float)renderPair.alpha);
                GL11.glVertex2f((float)renderPair.posX, (float)(this.fontRendererAccessor.getPosY() + 4.0f));
                GL11.glVertex2f((float)(renderPair.posX + renderPair.width), (float)(this.fontRendererAccessor.getPosY() + 4.0f));
                GL11.glVertex2f((float)(renderPair.posX + renderPair.width), (float)(this.fontRendererAccessor.getPosY() + 3.0f));
                GL11.glVertex2f((float)renderPair.posX, (float)(this.fontRendererAccessor.getPosY() + 3.0f));
            }
            for (RenderPair renderPair : underline) {
                GlStateManager.func_179131_c((float)renderPair.red, (float)renderPair.green, (float)renderPair.blue, (float)renderPair.alpha);
                GL11.glVertex2f((float)(renderPair.posX - 1.0f), (float)(this.fontRendererAccessor.getPosY() + 9.0f));
                GL11.glVertex2f((float)(renderPair.posX + renderPair.width), (float)(this.fontRendererAccessor.getPosY() + 9.0f));
                GL11.glVertex2f((float)(renderPair.posX + renderPair.width), (float)(this.fontRendererAccessor.getPosY() + 9.0f - 1.0f));
                GL11.glVertex2f((float)(renderPair.posX - 1.0f), (float)(this.fontRendererAccessor.getPosY() + 9.0f - 1.0f));
            }
            GL11.glEnd();
            GlStateManager.func_179098_w();
        }
        if (((Boolean)Patcher.betterFontRendererStringCache.get()).booleanValue()) {
            GL11.glEndList();
            this.enhancedFontRenderer.cache(hash, value);
        }
        value.setWidth(this.fontRendererAccessor.getPosX());
        this.fontRendererAccessor.setPosY(posY + value.getHeight());
        this.fontRendererAccessor.setPosX(posX + value.getWidth());
        if (obfuscated) {
            this.enhancedFontRenderer.getObfuscated().add(hash);
        }
        GlStateManager.func_179109_b((float)(-posX), (float)(-posY), (float)0.0f);
        return true;
    }

    private void adjustOrAppend(Deque<RenderPair> style, float posX, float effectiveWidth, float lastRed, float lastGreen, float lastBlue, float lastAlpha) {
        RenderPair lastStart = style.peekLast();
        if (lastStart != null && lastStart.red == lastRed && lastStart.green == lastGreen && lastStart.blue == lastBlue && lastStart.alpha == lastAlpha && lastStart.posX + lastStart.width >= posX - 1.0f) {
            lastStart.width = posX + effectiveWidth - lastStart.posX;
            return;
        }
        style.add(new RenderPair(posX, effectiveWidth, lastRed, lastGreen, lastBlue, lastAlpha));
    }

    private float getBoldOffset(int width) {
        return width == -1 || this.fontRenderer.func_82883_a() ? 0.5f : this.getOptifineBoldOffset();
    }

    private float getOptifineBoldOffset() {
        return this.hook.getOptifineBoldOffset(this.fontRenderer);
    }

    public float renderChar(char ch, boolean italic) {
        if (ch == ' ' || ch == '\u00a0') {
            return this.fontRenderer.func_82883_a() ? 4.0f : this.getCharWidthFloat(ch);
        }
        int charIndex = characterDictionary.indexOf(ch);
        return charIndex != -1 && !this.fontRenderer.func_82883_a() ? this.renderDefaultChar(charIndex, italic, ch) : this.renderUnicodeChar(ch, italic);
    }

    private float renderDefaultChar(int characterIndex, boolean italic, char ch) {
        float characterX = (float)(characterIndex % 16 * 8 * this.regularCharDim >> 7) + 0.01f;
        float characterY = (float)(((characterIndex >> 4) * 8 * this.regularCharDim >> 7) + 16 * this.texSheetDim) + 0.01f;
        boolean italicStyle = italic;
        float charWidth = this.getCharWidthFloat(ch);
        float smallCharWidth = charWidth - 0.01f;
        this.startDrawing();
        float uvHeight = 7.99f * (float)this.regularCharDim / 128.0f;
        float uvWidth = smallCharWidth * (float)this.regularCharDim / 128.0f;
        GL11.glTexCoord2f((float)(characterX / this.fontTexWidth), (float)(characterY / this.fontTexHeight));
        GL11.glVertex2f((float)(this.fontRendererAccessor.getPosX() + (float)italicStyle), (float)this.fontRendererAccessor.getPosY());
        GL11.glTexCoord2f((float)(characterX / this.fontTexWidth), (float)((characterY + uvHeight) / this.fontTexHeight));
        GL11.glVertex2f((float)(this.fontRendererAccessor.getPosX() - (float)italicStyle), (float)(this.fontRendererAccessor.getPosY() + 7.99f));
        int offset = this.regularCharDim / 128;
        GL11.glTexCoord2f((float)((characterX + uvWidth - (float)offset) / this.fontTexWidth), (float)((characterY + uvHeight) / this.fontTexHeight));
        GL11.glVertex2f((float)(this.fontRendererAccessor.getPosX() + smallCharWidth - 1.0f - (float)italicStyle), (float)(this.fontRendererAccessor.getPosY() + 7.99f));
        GL11.glTexCoord2f((float)((characterX + uvWidth - (float)offset) / this.fontTexWidth), (float)(characterY / this.fontTexHeight));
        GL11.glVertex2f((float)(this.fontRendererAccessor.getPosX() + smallCharWidth - 1.0f + (float)italicStyle), (float)this.fontRendererAccessor.getPosY());
        return charWidth;
    }

    private void startDrawing() {
        if (!this.drawing) {
            this.drawing = true;
            GL11.glBegin((int)7);
        }
    }

    private void endDrawing() {
        if (this.drawing) {
            this.drawing = false;
            GL11.glEnd();
        }
    }

    private Pair<Float, Float> getUV(char characterIndex) {
        int page = characterIndex / 256;
        int row = page >> 4;
        int column = page % 16;
        int glyphWidth = this.fontRendererAccessor.getGlyphWidth()[characterIndex] >>> 4;
        float charX = (float)(characterIndex % 16 << 4) + (float)glyphWidth + 0.05f * (float)page / 39.0f;
        float charY = (float)(((characterIndex & 0xFF) >> 4) * 16) + 0.05f * (float)page / 39.0f;
        return new Pair<Float, Float>(Float.valueOf(((float)(row * this.texSheetDim) + charX) / this.fontTexWidth), Float.valueOf(((float)(column * this.texSheetDim) + charY) / this.fontTexHeight));
    }

    private float renderUnicodeChar(char ch, boolean italic) {
        if (this.fontRendererAccessor.getGlyphWidth()[ch] == 0) {
            return 0.0f;
        }
        Pair<Float, Float> uv = this.getUV(ch);
        int glyphX = this.fontRendererAccessor.getGlyphWidth()[ch] >>> 4;
        int glyphY = this.fontRendererAccessor.getGlyphWidth()[ch] & 0xF;
        float floatGlyphX = glyphX;
        float modifiedY = (float)glyphY + 1.0f;
        float combinedGlyphSize = modifiedY - floatGlyphX - 0.02f;
        float italicStyle = italic ? 1.0f : 0.0f;
        this.startDrawing();
        float v = 15.98f * (float)this.texSheetDim / 256.0f;
        GL11.glTexCoord2f((float)uv.component1().floatValue(), (float)uv.component2().floatValue());
        GL11.glVertex2f((float)(this.fontRendererAccessor.getPosX() + italicStyle), (float)this.fontRendererAccessor.getPosY());
        GL11.glTexCoord2f((float)uv.component1().floatValue(), (float)(uv.component2().floatValue() + v / this.fontTexHeight));
        GL11.glVertex2f((float)(this.fontRendererAccessor.getPosX() - italicStyle), (float)(this.fontRendererAccessor.getPosY() + 7.99f));
        float texAdj = combinedGlyphSize + 0.5f;
        GL11.glTexCoord2f((float)(uv.component1().floatValue() + texAdj / this.fontTexHeight), (float)(uv.component2().floatValue() + v / this.fontTexHeight));
        GL11.glVertex2f((float)(this.fontRendererAccessor.getPosX() + combinedGlyphSize / 2.0f - italicStyle), (float)(this.fontRendererAccessor.getPosY() + 7.99f));
        GL11.glTexCoord2f((float)(uv.component1().floatValue() + texAdj / this.fontTexHeight), (float)uv.component2().floatValue());
        GL11.glVertex2f((float)(this.fontRendererAccessor.getPosX() + combinedGlyphSize / 2.0f + italicStyle), (float)this.fontRendererAccessor.getPosY());
        return (modifiedY - floatGlyphX) / 2.0f + 1.0f;
    }

    private float getCharWidthFloat(char c) {
        return this.hook.getCharWidth(this.fontRenderer, c);
    }

    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        Map<String, Integer> stringWidthCache = this.enhancedFontRenderer.getStringWidthCache();
        if (!((Boolean)Patcher.betterFontRenderer.get()).booleanValue()) {
            if (stringWidthCache.size() != 0) {
                stringWidthCache.clear();
            }
            return this.getUncachedWidth(text);
        }
        if (stringWidthCache.size() > 5000) {
            stringWidthCache.clear();
        }
        return stringWidthCache.computeIfAbsent(text, width -> this.getUncachedWidth(text));
    }

    private int getUncachedWidth(String text) {
        if (text == null) {
            return 0;
        }
        float width = 0.0f;
        boolean bold = false;
        for (int messageChar = 0; messageChar < text.length(); ++messageChar) {
            char character = text.charAt(messageChar);
            float characterWidth = this.getCharWidthFloat(character);
            if (characterWidth < 0.0f && messageChar < text.length() - 1) {
                if ((character = text.charAt(++messageChar)) != 'l' && character != 'L') {
                    if (character == 'r' || character == 'R') {
                        bold = false;
                    }
                } else {
                    bold = true;
                }
                characterWidth = 0.0f;
            }
            width += characterWidth;
            if (!bold || !(characterWidth > 0.0f)) continue;
            width += this.getBoldOffset(characterDictionary.indexOf(character));
        }
        return (int)width;
    }

    static class RenderPair {
        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;
        float posX;
        float width;

        public RenderPair(float posX, float width, float red2, float green2, float blue2, float alpha2) {
            this.posX = posX;
            this.width = width;
            this.red = red2;
            this.green = green2;
            this.blue = blue2;
            this.alpha = alpha2;
        }
    }
}

