/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  org.lwjgl.opengl.GL11
 */
package ad.novoline.font;

import ad.novoline.font.FontRenderer;
import java.awt.Font;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public final class SimpleFontRenderer
implements FontRenderer {
    private static final int[] COLOR_CODES = SimpleFontRenderer.setupMinecraftColorCodes();
    private static final String COLORS = "0123456789abcdefklmnor";
    private static final char COLOR_PREFIX = '\u00a7';
    private static final short CHARS = 256;
    private static final float IMG_SIZE = 512.0f;
    private static final float CHAR_OFFSET = 0.0f;
    private final CharData[] charData = new CharData[256];
    private final CharData[] boldChars = new CharData[256];
    private final CharData[] italicChars = new CharData[256];
    private final CharData[] boldItalicChars = new CharData[256];
    private final Font awtFont;
    private final boolean antiAlias;
    private final boolean fractionalMetrics;
    private DynamicTexture texturePlain;
    private DynamicTexture textureBold;
    private DynamicTexture textureItalic;
    private DynamicTexture textureItalicBold;
    private int fontHeight = -1;

    private SimpleFontRenderer(Font awtFont, boolean antiAlias, boolean fractionalMetrics) {
        this.awtFont = awtFont;
        this.antiAlias = antiAlias;
        this.fractionalMetrics = fractionalMetrics;
        this.setupBoldItalicFonts();
    }

    static FontRenderer create(Font font, boolean antiAlias, boolean fractionalMetrics) {
        return new SimpleFontRenderer(font, antiAlias, fractionalMetrics);
    }

    public static FontRenderer create(Font font) {
        Font font2 = font;
        return 0;
    }

    private DynamicTexture setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
        return new DynamicTexture(this.generateFontImage(font, antiAlias, fractionalMetrics, chars));
    }

    /*
     * Exception decompiling
     */
    private BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl6 : INVOKESPECIAL - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private void setupBoldItalicFonts() {
        this.texturePlain = this.setupTexture(this.awtFont, this.antiAlias, this.fractionalMetrics, this.charData);
        this.textureBold = this.setupTexture(this.awtFont.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.textureItalic = this.setupTexture(this.awtFont.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.textureItalicBold = this.setupTexture(this.awtFont.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    @Override
    public float drawString(CharSequence text, double x, double y, int color, boolean dropShadow) {
        if (dropShadow) {
            float shadowWidth = this.drawStringInternal(text, x + 0.5, y + 0.5, color, true);
            return Math.max(shadowWidth, this.drawStringInternal(text, x, y, color, false));
        }
        return this.drawStringInternal(text, x, y, color, false);
    }

    @Override
    public float drawString(CharSequence text, float x, float y, int color, boolean dropShadow) {
        if (dropShadow) {
            float shadowWidth = this.drawStringInternal(text, (double)x + 0.5, (double)y + 0.5, color, true);
            return Math.max(shadowWidth, this.drawStringInternal(text, x, y, color, false));
        }
        return this.drawStringInternal(text, x, y, color, false);
    }

    /*
     * Exception decompiling
     */
    private float drawStringInternal(CharSequence text, double x, double y, int color, boolean shadow) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl124 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public String trimStringToWidth(CharSequence text, int width, boolean reverse) {
        StringBuilder builder = new StringBuilder();
        float f = 0.0f;
        int i = reverse ? text.length() - 1 : 0;
        int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;
        for (int k = i; k >= 0 && k < text.length() && f < (float)width; k += j) {
            char c0 = text.charAt(k);
            float f1 = this.stringWidth(String.valueOf(c0));
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (f1 < 0.0f) {
                flag = true;
            } else {
                f += f1;
                if (flag1) {
                    f += 1.0f;
                }
            }
            if (f > (float)width) break;
            if (reverse) {
                builder.insert(0, c0);
                continue;
            }
            builder.append(c0);
        }
        return builder.toString();
    }

    @Override
    public int stringWidth(CharSequence text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7' && i + 1 < size) {
                int colorIndex = COLORS.indexOf(text.charAt(i + 1));
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                } else if (colorIndex == 17) {
                    bold = true;
                    currentData = italic ? this.boldItalicChars : this.boldChars;
                } else if (colorIndex == 20) {
                    italic = true;
                    currentData = bold ? this.boldItalicChars : this.italicChars;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i;
                continue;
            }
            if (character >= currentData.length) continue;
            width += currentData[character].width - (character == ' ' ? 8 : 9);
        }
        return width / 2;
    }

    @Override
    public float charWidth(char s) {
        return (this.charData[s].width - 8) / 2;
    }

    public CharData[] getCharData() {
        return this.charData;
    }

    private static int[] setupMinecraftColorCodes() {
        int[] colorCodes = new int[32];
        for (int i = 0; i < 32; ++i) {
            int noClue = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + noClue;
            int green = (i >> 1 & 1) * 170 + noClue;
            int blue = (i & 1) * 170 + noClue;
            if (i == 6) {
                red += 85;
            }
            if (i >= 16) {
                red >>= 2;
                green >>= 2;
                blue >>= 2;
            }
            colorCodes[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
        return colorCodes;
    }

    private static void drawChar(CharData[] chars, char c, float x, float y) {
        SimpleFontRenderer.drawQuad(x, y, chars[c].width, chars[c].height, chars[c].storedX, chars[c].storedY, chars[c].width, chars[c].height);
    }

    private static void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
        float renderSRCX = srcX / 512.0f;
        float renderSRCY = srcY / 512.0f;
        float renderSRCWidth = srcWidth / 512.0f;
        float renderSRCHeight = srcHeight / 512.0f;
        GL11.glTexCoord2f((float)(renderSRCX + renderSRCWidth), (float)renderSRCY);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glTexCoord2f((float)renderSRCX, (float)renderSRCY);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glTexCoord2f((float)renderSRCX, (float)(renderSRCY + renderSRCHeight));
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glTexCoord2f((float)renderSRCX, (float)(renderSRCY + renderSRCHeight));
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glTexCoord2f((float)(renderSRCX + renderSRCWidth), (float)(renderSRCY + renderSRCHeight));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glTexCoord2f((float)(renderSRCX + renderSRCWidth), (float)renderSRCY);
        GL11.glVertex2d((double)(x + width), (double)y);
    }

    private static void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

    @Override
    public String getName() {
        return this.awtFont.getFamily();
    }

    @Override
    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    @Override
    public boolean isAntiAlias() {
        return this.antiAlias;
    }

    @Override
    public boolean isFractionalMetrics() {
        return this.fractionalMetrics;
    }

    private static class CharData {
        private int width;
        private int height;
        private int storedX;
        private int storedY;

        private CharData() {
        }

        static /* synthetic */ int access$102(CharData x0, int x1) {
            x0.width = x1;
            return x0.width;
        }

        static /* synthetic */ int access$202(CharData x0, int x1) {
            x0.height = x1;
            return x0.height;
        }

        static /* synthetic */ int access$302(CharData x0, int x1) {
            x0.storedX = x1;
            return x0.storedX;
        }

        static /* synthetic */ int access$402(CharData x0, int x1) {
            x0.storedY = x1;
            return x0.storedY;
        }
    }
}

