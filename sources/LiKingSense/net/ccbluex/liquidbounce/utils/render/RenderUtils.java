/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render;

import ad.VisualUtils;
import ad.tenacity.GLUtil;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IGlStateManager;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.ITimer;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.novoline.ScaleUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.VertexUtils;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public final class RenderUtils
extends MinecraftInstance {
    private static final Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();
    private static final int[] DISPLAY_LISTS_2D = new int[4];
    public static int deltaTime;

    public static void drawShadowImage(int x, int y, int width, int height, ResourceLocation image2) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.getTextureManager().bindTexture2(image2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)0.7f);
        RenderUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
    }

    public static void drawmage(ResourceLocation image2, int x, int y, int width, int height, float alpha) {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        int n = 0;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void drawRoundRect(float x, float y, float x1, float y1, int color) {
        RenderUtils.drawRoundedRect(x, y, x1, y1, color, color);
        GlStateManager.func_179124_c((float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawShadow2(float x, float y, float x2, float y2) {
        boolean disableAlpha;
        GL11.glPushMatrix();
        boolean enableBlend = GL11.glIsEnabled((int)3042);
        boolean bl = disableAlpha = !GL11.glIsEnabled((int)3008);
        if (!enableBlend) {
            GL11.glEnable((int)3042);
        }
        if (!disableAlpha) {
            GL11.glDisable((int)3008);
        }
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawImage(new ResourceLocation("likingsense/shadow/paneltopleft.png"), x - 9.0f, y - 9.0f, 9.0f, 9.0f);
        RenderUtils.drawImage(new ResourceLocation("likingsense/shadow/panelbottomleft.png"), x - 9.0f, y + (y2 - y), 9.0f, 9.0f);
        RenderUtils.drawImage(new ResourceLocation("likingsense/shadow/panelbottomright.png"), x + (x2 - x), y + (y2 - y), 9.0f, 9.0f);
        RenderUtils.drawImage(new ResourceLocation("likingsense/shadow/paneltopright.png"), x + (x2 - x), y - 9.0f, 9.0f, 9.0f);
        RenderUtils.drawImage(new ResourceLocation("likingsense/shadow/panelleft.png"), x - 9.0f, y, 9.0f, y2 - y);
        RenderUtils.drawImage(new ResourceLocation("likingsense/shadow/panelright.png"), x + (x2 - x), y, 9.0f, y2 - y);
        RenderUtils.drawImage(new ResourceLocation("likingsense/shadow/paneltop.png"), x, y - 9.0f, x2 - x, 9.0f);
        RenderUtils.drawImage(new ResourceLocation("likingsense/shadow/panelbottom.png"), x, y + (y2 - y), x2 - x, 9.0f);
        if (!enableBlend) {
            GL11.glDisable((int)3042);
        }
        if (!disableAlpha) {
            GL11.glEnable((int)3008);
        }
        GL11.glPopMatrix();
    }

    public static void drawFastRoundedRect(float x0, float y0, float x1, float y1, float radius, int color) {
        float f11;
        int i;
        boolean Semicircle = true;
        float f = 5.0f;
        float f2 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(color & 0xFF) / 255.0f;
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GlStateManager.func_179147_l();
        int n = 0;
        GlStateManager.func_179090_x();
        GL11.glColor4f((float)f3, (float)f4, (float)f5, (float)f2);
        GL11.glBegin((int)5);
        GL11.glVertex2f((float)(x0 + radius), (float)y0);
        GL11.glVertex2f((float)(x0 + radius), (float)y1);
        GL11.glVertex2f((float)(x1 - radius), (float)y0);
        GL11.glVertex2f((float)(x1 - radius), (float)y1);
        GL11.glEnd();
        GL11.glBegin((int)5);
        GL11.glVertex2f((float)x0, (float)(y0 + radius));
        GL11.glVertex2f((float)(x0 + radius), (float)(y0 + radius));
        GL11.glVertex2f((float)x0, (float)(y1 - radius));
        GL11.glVertex2f((float)(x0 + radius), (float)(y1 - radius));
        GL11.glEnd();
        GL11.glBegin((int)5);
        GL11.glVertex2f((float)x1, (float)(y0 + radius));
        GL11.glVertex2f((float)(x1 - radius), (float)(y0 + radius));
        GL11.glVertex2f((float)x1, (float)(y1 - radius));
        GL11.glVertex2f((float)(x1 - radius), (float)(y1 - radius));
        GL11.glEnd();
        GL11.glBegin((int)6);
        float f6 = x1 - radius;
        float f7 = y0 + radius;
        GL11.glVertex2f((float)f6, (float)f7);
        boolean j = false;
        for (i = 0; i <= 18; ++i) {
            f11 = (float)i * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 + (double)radius * Math.cos(Math.toRadians(f11)))), (float)((float)((double)f7 - (double)radius * Math.sin(Math.toRadians(f11)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x0 + radius;
        f7 = y0 + radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (i = 0; i <= 18; ++i) {
            f11 = (float)i * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 - (double)radius * Math.cos(Math.toRadians(f11)))), (float)((float)((double)f7 - (double)radius * Math.sin(Math.toRadians(f11)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x0 + radius;
        f7 = y1 - radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (i = 0; i <= 18; ++i) {
            f11 = (float)i * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 - (double)radius * Math.cos(Math.toRadians(f11)))), (float)((float)((double)f7 + (double)radius * Math.sin(Math.toRadians(f11)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x1 - radius;
        f7 = y1 - radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (i = 0; i <= 18; ++i) {
            f11 = (float)i * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 + (double)radius * Math.cos(Math.toRadians(f11)))), (float)((float)((double)f7 + (double)radius * Math.sin(Math.toRadians(f11)))));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glDisable((int)3042);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        RenderUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        VisualUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        VisualUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        VisualUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        VisualUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        VisualUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        VisualUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        VisualUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        VisualUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        RenderUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderUtils.disableGL2D();
        int n = 0;
    }

    public static void drawImage(ResourceLocation image2, float x, float y, float width, float height) {
        RenderUtils.drawImage(image2, (int)x, (int)y, (int)width, (int)height, 1.0f);
    }

    public static void drawImage(ResourceLocation resourceLocation, int n, int n2, int n3, int n4) {
        new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        int n5 = 0;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(resourceLocation);
        Gui.func_146110_a((int)n, (int)n2, (float)0.0f, (float)0.0f, (int)n3, (int)n4, (float)n3, (float)n4);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    private static float drawExhiOutlined(String text, float x, float y, float borderWidth, int borderColor, int mainColor, boolean drawText) {
        Fonts.posterama35.drawString(text, x, y - borderWidth, borderColor);
        Fonts.posterama35.drawString(text, x, y + borderWidth, borderColor);
        Fonts.posterama35.drawString(text, x - borderWidth, y, borderColor);
        Fonts.posterama35.drawString(text, x + borderWidth, y, borderColor);
        if (drawText) {
            Fonts.posterama35.drawString(text, x, y, mainColor);
        }
        return x + (float)Fonts.posterama35.getStringWidth(text) - 2.0f;
    }

    /*
     * Exception decompiling
     */
    public static void drawExhiEnchants(IItemStack stack, float x, float y) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl44 : INVOKESTATIC - null : Stack underflow
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

    private static int getMainColor(int level) {
        if (level == 4) {
            return -5636096;
        }
        return -1;
    }

    public static void drawRectBasedBorder(float x, float y, float x2, float y2, float width, int color1) {
        RenderUtils.drawRect(x - width / 2.0f, y - width / 2.0f, x2 + width / 2.0f, y + width / 2.0f, color1);
        RenderUtils.drawRect(x - width / 2.0f, y + width / 2.0f, x + width / 2.0f, y2 + width / 2.0f, color1);
        RenderUtils.drawRect(x2 - width / 2.0f, y + width / 2.0f, x2 + width / 2.0f, y2 + width / 2.0f, color1);
        RenderUtils.drawRect(x + width / 2.0f, y2 - width / 2.0f, x2 - width / 2.0f, y2 + width / 2.0f, color1);
    }

    private static int getBorderColor(int level) {
        if (level == 2) {
            return 1884684117;
        }
        if (level == 3) {
            return 0x7000AAAA;
        }
        if (level == 4) {
            return 0x70AA0000;
        }
        if (level >= 5) {
            return 1895803392;
        }
        return 0x70FFFFFF;
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, float zLevel) {
        float f = 0.00390625f;
        float f1 = 0.00390625f;
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_TEX));
        worldrenderer.pos(x + 0, y + height, zLevel).tex((float)(textureX + 0) * f, (float)(textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, zLevel).tex((float)(textureX + width) * f, (float)(textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + 0, zLevel).tex((float)(textureX + width) * f, (float)(textureY + 0) * f1).endVertex();
        worldrenderer.pos(x + 0, y + 0, zLevel).tex((float)(textureX + 0) * f, (float)(textureY + 0) * f1).endVertex();
        tessellator.draw();
    }

    public static Color getGradientOffset3(Color color1, Color color2, double offset) {
        int redPart;
        double inverse_percent;
        if (offset > 1.0) {
            inverse_percent = offset % 1.0;
            redPart = (int)offset;
            offset = redPart % 2 == 0 ? inverse_percent : 1.0 - inverse_percent;
        }
        inverse_percent = 1.0 - offset;
        redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offset);
        int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offset);
        int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static void drawTexturedRect(float x, float y, float width, float height, String image2, IScaledResolution sr) {
        GL11.glPushMatrix();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        mc.getTextureManager().bindTexture(classProvider.createResourceLocation("shaders/" + image2 + ".png"));
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Gui.func_146110_a((int)((int)x), (int)((int)y), (float)0.0f, (float)0.0f, (int)((int)width), (int)((int)height), (float)((int)width), (float)((int)height));
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GL11.glPopMatrix();
    }

    public static void drawTexturedRect(float x, float y, float width, float height, String image2) {
        boolean disableAlpha;
        GL11.glPushMatrix();
        boolean enableBlend = GL11.glIsEnabled((int)3042);
        boolean bl = disableAlpha = !GL11.glIsEnabled((int)3008);
        if (!enableBlend) {
            GL11.glEnable((int)3042);
        }
        if (!disableAlpha) {
            GL11.glDisable((int)3008);
        }
        minecraft.func_110434_K().func_110577_a(new ResourceLocation("shaders/" + image2 + ".png"));
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        if (!enableBlend) {
            GL11.glDisable((int)3042);
        }
        if (!disableAlpha) {
            GL11.glEnable((int)3008);
        }
        GL11.glPopMatrix();
    }

    public static void drawShadow(float x, float y, float width, float height) {
        RenderUtils.drawTexturedRect(x - 9.0f, y - 9.0f, 9.0f, 9.0f, "paneltopleft");
        RenderUtils.drawTexturedRect(x - 9.0f, y + height, 9.0f, 9.0f, "panelbottomleft");
        RenderUtils.drawTexturedRect(x + width, y + height, 9.0f, 9.0f, "panelbottomright");
        RenderUtils.drawTexturedRect(x + width, y - 9.0f, 9.0f, 9.0f, "paneltopright");
        RenderUtils.drawTexturedRect(x - 9.0f, y, 9.0f, height, "panelleft");
        RenderUtils.drawTexturedRect(x + width, y, 9.0f, height, "panelright");
        RenderUtils.drawTexturedRect(x, y - 9.0f, width, 9.0f, "paneltop");
        RenderUtils.drawTexturedRect(x, y + height, width, 9.0f, "panelbottom");
    }

    public static int getRainbow(int index, int offset, float bright, float st) {
        float hue = (System.currentTimeMillis() + (long)offset * (long)index) % 2000L;
        return Color.getHSBColor(hue /= 2000.0f, st, bright).getRGB();
    }

    public static int Astolfo2(int var2, float bright, float st, int index, int offset, float client) {
        double d;
        double rainbowDelay = Math.ceil(System.currentTimeMillis() + (long)(var2 * index)) / (double)offset;
        return Color.getHSBColor((double)((float)(d / (double)client)) < 0.5 ? -((float)(rainbowDelay / (double)client)) : (float)((rainbowDelay %= (double)client) / (double)client), st, bright).getRGB();
    }

    public static void autoExhibition(double x, double y, double x1, double y1, double size) {
        RenderUtils.rectangleBordered(x, y, x1 + size, y1 + size, 0.5, Colors.getColor(90), Colors.getColor(0));
        RenderUtils.rectangleBordered(x + 1.0, y + 1.0, x1 + size - 1.0, y1 + size - 1.0, 1.0, Colors.getColor(90), Colors.getColor(61));
        RenderUtils.rectangleBordered(x + 2.5, y + 2.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, Colors.getColor(61), Colors.getColor(0));
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtils.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void circle(float var0, float var1, float var2, int var3) {
        RenderUtils.arc(var0, var1, 0.0f, 360.0f, var2, var3);
    }

    public static void arc(float var0, float var1, float var2, float var3, float var4, int var5) {
        RenderUtils.arcEllipse(var0, var1, var2, var3, var4, var4, var5);
    }

    public static void arcEllipse(float var0, float var1, float var2, float var3, float var4, float var5, int var6) {
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        if (var2 > var3) {
            float var7 = var3;
            var3 = var2;
            var2 = var7;
        }
        float var8 = (float)(var6 >> 24 & 0xFF) / 255.0f;
        float var9 = (float)(var6 >> 16 & 0xFF) / 255.0f;
        float var10 = (float)(var6 >> 8 & 0xFF) / 255.0f;
        float var11 = (float)(var6 & 0xFF) / 255.0f;
        Tessellator var12 = Tessellator.func_178181_a();
        var12.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)var9, (float)var10, (float)var11, (float)var8);
        if (var8 > 0.5f) {
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)2.0f);
            GL11.glBegin((int)3);
            for (float var14 = var3; var14 >= var2; var14 -= 4.0f) {
                float var15 = (float)Math.cos((double)var14 * Math.PI / 180.0) * var4 * 1.001f;
                float var16 = (float)Math.sin((double)var14 * Math.PI / 180.0) * var5 * 1.001f;
                GL11.glVertex2f((float)(var0 + var15), (float)(var1 + var16));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
        }
        GL11.glBegin((int)6);
        for (float var18 = var3; var18 >= var2; var18 -= 4.0f) {
            float var19 = (float)Math.cos((double)var18 * Math.PI / 180.0) * var4;
            float var20 = (float)Math.sin((double)var18 * Math.PI / 180.0) * var5;
            GL11.glVertex2f((float)(var0 + var19), (float)(var1 + var20));
        }
        GL11.glEnd();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawImage(ResourceLocation image2, int x, int y, int width, int height, float alpha) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        int n = 0;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void drawImage(ResourceLocation var0, int var1, int var2, int var3, int var4, Color var5) {
        new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        int n = 0;
        GL11.glColor4f((float)((float)var5.getRed() / 255.0f), (float)((float)var5.getGreen() / 255.0f), (float)((float)var5.getBlue() / 255.0f), (float)1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(var0);
        Gui.func_146110_a((int)var1, (int)var2, (float)0.0f, (float)0.0f, (int)var3, (int)var4, (float)var3, (float)var4);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
    }

    public static void originalRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color) {
        double i;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        float z = 0.0f;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }
        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }
        double x1 = paramXStart + radius;
        double y1 = paramYStart + radius;
        double x2 = paramXEnd - radius;
        double y2 = paramYEnd - radius;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        worldrenderer.func_181668_a(9, DefaultVertexFormats.field_181705_e);
        double degree = Math.PI / 180;
        for (i = 0.0; i <= 90.0; i += 1.0) {
            worldrenderer.func_181662_b(x2 + Math.sin(i * degree) * (double)radius, y2 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        for (i = 90.0; i <= 180.0; i += 1.0) {
            worldrenderer.func_181662_b(x2 + Math.sin(i * degree) * (double)radius, y1 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        for (i = 180.0; i <= 270.0; i += 1.0) {
            worldrenderer.func_181662_b(x1 + Math.sin(i * degree) * (double)radius, y1 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        for (i = 270.0; i <= 360.0; i += 1.0) {
            worldrenderer.func_181662_b(x1 + Math.sin(i * degree) * (double)radius, y2 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void startDrawing() {
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        Minecraft.func_71410_x().field_71460_t.func_78479_a(Minecraft.func_71410_x().field_71428_T.field_194147_b, 0);
    }

    public static void doGlScissor1(float var0, float var1, float var2, float var3) {
        int var4 = RenderUtils.getScaleFactor();
        GL11.glScissor((int)((int)(var0 * (float)var4)), (int)((int)((float)Minecraft.func_71410_x().field_71440_d - var3 * (float)var4)), (int)((int)((var2 - var0) * (float)var4)), (int)((int)((var3 - var1) * (float)var4)));
    }

    public static int getScaleFactor() {
        int var0 = 1;
        boolean var1 = Minecraft.func_71410_x().func_152349_b();
        int var2 = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
        if (var2 == 0) {
            var2 = 1000;
        }
        while (var0 < var2 && Minecraft.func_71410_x().field_71443_c / (var0 + 1) >= 320 && Minecraft.func_71410_x().field_71440_d / (var0 + 1) >= 240) {
            ++var0;
        }
        if (var1 && var0 % 2 != 0 && var0 != 1) {
            --var0;
        }
        return var0;
    }

    public static void stopDrawing() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    /*
     * Exception decompiling
     */
    public static void drawHead(IResourceLocation skin, int x, int y, int width, int height) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl18 : INVOKESTATIC - null : Stack underflow
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

    /*
     * Exception decompiling
     */
    public static void drawHead2(ResourceLocation skin, int x, int y, int width, int height) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl18 : INVOKESTATIC - null : Stack underflow
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

    public static void drawEntityOnScreen(int posX, int posY, int scale, IEntityLivingBase entity) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179142_g();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)50.0);
        GlStateManager.func_179152_a((float)(-scale), (float)scale, (float)scale);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179137_b((double)0.0, (double)0.0, (double)0.0);
        float renderYawOffset = entity.getRenderYawOffset();
        float rotationYaw = entity.getRotationYaw();
        float rotationPitch = entity.getRotationPitch();
        float prevRotationYawHead = entity.getPrevRotationYawHead();
        float rotationYawHead = entity.getRotationYawHead();
        entity.setRenderYawOffset(0.0f);
        entity.setRotationYaw(0.0f);
        entity.setRotationPitch(90.0f);
        entity.setRotationYawHead(entity.getRotationYaw());
        entity.setPrevRotationYawHead(entity.getRotationYaw());
        IRenderManager rendermanager = mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        rendermanager.setRenderShadow(true);
        entity.setRenderYawOffset(renderYawOffset);
        entity.setRotationYaw(rotationYaw);
        entity.setRotationPitch(rotationPitch);
        entity.setPrevRotationYawHead(prevRotationYawHead);
        entity.setRotationYawHead(rotationYawHead);
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
    }

    public static int SkyRainbow(int var2, float st, float bright) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright).getRGB();
    }

    public static void drawRect2(double x, double y, double width, double height, int color) {
        RenderUtils.resetColor();
        GLUtil.setup2DRendering(() -> GLUtil.render(7, () -> {
            RenderUtils.color(color);
            GL11.glVertex2d((double)x, (double)y);
            GL11.glVertex2d((double)x, (double)(y + height));
            GL11.glVertex2d((double)(x + width), (double)(y + height));
            GL11.glVertex2d((double)(x + width), (double)y);
        }));
    }

    public static Color skyRainbow(int var2, float st, float bright) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright);
    }

    public static void drawScaledCustomSizeModalCircle(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0f / tileWidth;
        float f1 = 1.0f / tileHeight;
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(9, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_TEX));
        float xRadius = (float)width / 2.0f;
        float yRadius = (float)height / 2.0f;
        float uRadius = ((u + (float)uWidth) * f - u * f) / 2.0f;
        float vRadius = ((v + (float)vHeight) * f1 - v * f1) / 2.0f;
        for (int i = 0; i <= 360; i += 10) {
            double xPosOffset = Math.sin((double)i * Math.PI / 180.0);
            double yPosOffset = Math.cos((double)i * Math.PI / 180.0);
            worldrenderer.pos((double)((float)x + xRadius) + xPosOffset * (double)xRadius, (double)((float)y + yRadius) + yPosOffset * (double)yRadius, 0.0).tex((double)(u * f + uRadius) + xPosOffset * (double)uRadius, (double)(v * f1 + vRadius) + yPosOffset * (double)vRadius).endVertex();
        }
        tessellator.draw();
    }

    public static int Astolfo(int var2, float st, float bright) {
        double d;
        double currentColor = Math.ceil(System.currentTimeMillis() + (long)(var2 * 130)) / 6.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(currentColor / 360.0)) : (float)((currentColor %= 360.0) / 360.0), st, bright).getRGB();
    }

    public static int getRainbowOpaque(int seconds, float saturation, float brightness, int index) {
        float hue = (float)((System.currentTimeMillis() + (long)index) % (long)(seconds * 1000)) / (float)(seconds * 1000);
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }

    public static Color interpolateColorHue(Color color1, Color color2, float amount) {
        amount = Math.min(1.0f, Math.max(0.0f, amount));
        float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
        float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);
        Color resultColor = Color.getHSBColor(MathUtils.interpolateFloat(color1HSB[0], color2HSB[0], amount), MathUtils.interpolateFloat(color1HSB[1], color2HSB[1], amount), MathUtils.interpolateFloat(color1HSB[2], color2HSB[2], amount));
        return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(), MathUtils.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }

    public static Color interpolateColorsBackAndForth(int speed, int index, Color start, Color end, boolean trueColor) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        return trueColor ? RenderUtils.interpolateColorHue(start, end, (float)angle / 360.0f) : ColorUtil.interpolateColorC(start, end, (float)angle / 360.0f);
    }

    public static Color[] getAnalogousColor(Color color) {
        Color[] colors = new Color[2];
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float degree = 0.083333336f;
        float newHueAdded = hsb[0] + degree;
        colors[0] = new Color(Color.HSBtoRGB(newHueAdded, hsb[1], hsb[2]));
        float newHueSubtracted = hsb[0] - degree;
        colors[1] = new Color(Color.HSBtoRGB(newHueSubtracted, hsb[1], hsb[2]));
        return colors;
    }

    public static void customRounded(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float rTL, float rTR, float rBR, float rBL, int color) {
        double i;
        float z;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }
        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }
        double xTL = paramXStart + rTL;
        double yTL = paramYStart + rTL;
        double xTR = paramXEnd - rTR;
        double yTR = paramYStart + rTR;
        double xBR = paramXEnd - rBR;
        double yBR = paramYEnd - rBR;
        double xBL = paramXStart + rBL;
        double yBL = paramYEnd - rBL;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        double degree = Math.PI / 180;
        for (i = 0.0; i <= 90.0; i += 0.25) {
            GL11.glVertex2d((double)(xBR + Math.sin(i * degree) * (double)rBR), (double)(yBR + Math.cos(i * degree) * (double)rBR));
        }
        for (i = 90.0; i <= 180.0; i += 0.25) {
            GL11.glVertex2d((double)(xTR + Math.sin(i * degree) * (double)rTR), (double)(yTR + Math.cos(i * degree) * (double)rTR));
        }
        for (i = 180.0; i <= 270.0; i += 0.25) {
            GL11.glVertex2d((double)(xTL + Math.sin(i * degree) * (double)rTL), (double)(yTL + Math.cos(i * degree) * (double)rTL));
        }
        for (i = 270.0; i <= 360.0; i += 0.25) {
            GL11.glVertex2d((double)(xBL + Math.sin(i * degree) * (double)rBL), (double)(yBL + Math.cos(i * degree) * (double)rBL));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    /*
     * Exception decompiling
     */
    public static void drawExhiRect(float x, float y, float x2, float y2) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl33 : INVOKESTATIC - null : Stack underflow
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

    public static void drawExhiRect(float x, float y, float x2, float y2, float alpha) {
        RenderUtils.drawRect(x - 3.5f, y - 3.5f, x2 + 3.5f, y2 + 3.5f, new Color(0.0f, 0.0f, 0.0f, alpha).getRGB());
        RenderUtils.drawRect(x - 3.0f, y - 3.0f, x2 + 3.0f, y2 + 3.0f, new Color(0.19607843f, 0.19607843f, 0.19607843f, alpha).getRGB());
        RenderUtils.drawRect(x - 2.5f, y - 2.5f, x2 + 2.5f, y2 + 2.5f, new Color(0.101960786f, 0.101960786f, 0.101960786f, alpha).getRGB());
        RenderUtils.drawRect(x - 0.5f, y - 0.5f, x2 + 0.5f, y2 + 0.5f, new Color(0.19607843f, 0.19607843f, 0.19607843f, alpha).getRGB());
        RenderUtils.drawRect(x, y, x2, y2, new Color(0.07058824f, 0.07058824f, 0.07058824f, alpha).getRGB());
    }

    /*
     * Exception decompiling
     */
    public static void drawFilledCircle(int xx, int yy, float radius, int col) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
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

    public static void drawGradientSidewaysV(double left, double top, double right, double bottom, int col1, int col2) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        RenderUtils.quickDrawGradientSidewaysV(left, top, right, bottom, col1, col2);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void quickDrawGradientSidewaysV(double left, double top, double right, double bottom, int col1, int col2) {
        GL11.glBegin((int)7);
        RenderUtils.glColor(col1);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glVertex2d((double)left, (double)top);
        RenderUtils.glColor(col2);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glEnd();
    }

    /*
     * Exception decompiling
     */
    public static void drawFilledCircle(float xx, float yy, float radius, Color color) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
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

    /*
     * WARNING - void declaration
     */
    public static void drawFilledCircle(double x, double y, double r, int c, int id) {
        float f = (float)(c >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(c >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(c >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(c & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)9);
        if (id == 1) {
            GL11.glVertex2d((double)x, (double)y);
            for (int i = 0; i <= 90; ++i) {
                double x2 = Math.sin((double)i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double)i * 3.141526 / 180.0) * r;
                GL11.glVertex2d((double)(x - x2), (double)(y - y2));
            }
        } else if (id == 2) {
            void i;
            GL11.glVertex2d((double)x, (double)y);
            while (i <= 180) {
                double x2 = Math.sin((double)i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double)i * 3.141526 / 180.0) * r;
                GL11.glVertex2d((double)(x - x2), (double)(y - y2));
                ++i;
            }
        } else if (id == 3) {
            GL11.glVertex2d((double)x, (double)y);
            for (int i = 270; i <= 360; ++i) {
                double x2 = Math.sin((double)i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double)i * 3.141526 / 180.0) * r;
                GL11.glVertex2d((double)(x - x2), (double)(y - y2));
            }
        } else if (id == 4) {
            GL11.glVertex2d((double)x, (double)y);
            for (int i = 180; i <= 270; ++i) {
                double x2 = Math.sin((double)i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double)i * 3.141526 / 180.0) * r;
                GL11.glVertex2d((double)(x - x2), (double)(y - y2));
            }
        } else {
            for (int i = 0; i <= 360; ++i) {
                double x2 = Math.sin((double)i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double)i * 3.141526 / 180.0) * r;
                GL11.glVertex2f((float)((float)(x - x2)), (float)((float)(y - y2)));
            }
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
    }

    public static void drawRectPotion(float x, float y, float x2, float y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtils.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static double getAnimationState2(double animation, double finalState, double speed) {
        float add = (float)(0.01 * speed);
        animation = animation < finalState ? (animation + (double)add < finalState ? (animation += (double)add) : finalState) : (animation - (double)add > finalState ? (animation -= (double)add) : finalState);
        return animation;
    }

    public static void fastRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius) {
        double i;
        float z = 0.0f;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }
        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }
        double x1 = paramXStart + radius;
        double y1 = paramYStart + radius;
        double x2 = paramXEnd - radius;
        double y2 = paramYEnd - radius;
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)9);
        double degree = Math.PI / 180;
        for (i = 0.0; i <= 90.0; i += 1.0) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 90.0; i <= 180.0; i += 1.0) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 180.0; i <= 270.0; i += 1.0) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 270.0; i <= 360.0; i += 1.0) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
    }

    public static void scaleStart(float x, float y, float scale) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)x, (float)y, (float)0.0f);
        GlStateManager.func_179152_a((float)scale, (float)scale, (float)1.0f);
        GlStateManager.func_179109_b((float)(-x), (float)(-y), (float)0.0f);
    }

    public static void scaleEnd() {
        GlStateManager.func_179121_F();
    }

    public static Color injectAlpha(Color color, int alpha) {
        int n = alpha;
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void setColor(int color) {
        GL11.glColor4ub((byte)((byte)(color >> 16 & 0xFF)), (byte)((byte)(color >> 8 & 0xFF)), (byte)((byte)(color & 0xFF)), (byte)((byte)(color >> 24 & 0xFF)));
    }

    /*
     * Exception decompiling
     */
    public static int getColor(int red, int green, int blue, int alpha) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
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

    public static int getColor(int color) {
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        int a = 255;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | (a & 0xFF) << 24;
    }

    public static void drawGradientSidewaysH(double left, double top, double right, double bottom, int col1, int col2) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        RenderUtils.quickDrawGradientSidewaysH(left, top, right, bottom, col1, col2);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static int rainbow(float seconds, float saturation, float brightness, long index) {
        float hue = (float)((System.currentTimeMillis() + index) % (long)((int)(seconds * 1000.0f))) / (seconds * 1000.0f);
        return Color.HSBtoRGB(hue, saturation, brightness);
    }

    public static void quickDrawGradientSidewaysH(double left, double top, double right, double bottom, int col1, int col2) {
        GL11.glBegin((int)7);
        RenderUtils.glColor(col1);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        RenderUtils.glColor(col2);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
    }

    public static void start() {
        RenderUtils.enable(3042);
        int n = 0;
        RenderUtils.disable(3553);
        RenderUtils.disable(2884);
        GlStateManager.func_179118_c();
        GlStateManager.func_179097_i();
    }

    public static void stop() {
        GlStateManager.func_179141_d();
        GlStateManager.func_179126_j();
        RenderUtils.enable(2884);
        RenderUtils.enable(3553);
        RenderUtils.disable(3042);
        RenderUtils.color(Color.white);
    }

    public static void startSmooth() {
        RenderUtils.enable(2881);
        RenderUtils.enable(2848);
        RenderUtils.enable(2832);
    }

    public static void endSmooth() {
        RenderUtils.disable(2832);
        RenderUtils.disable(2848);
        RenderUtils.disable(2881);
    }

    public static void begin(int glMode) {
        GL11.glBegin((int)glMode);
    }

    public static void end() {
        GL11.glEnd();
    }

    public static void vertex(double x, double y) {
        GL11.glVertex2d((double)x, (double)y);
    }

    public static void translate(double x, double y) {
        GL11.glTranslated((double)x, (double)y, (double)0.0);
    }

    public static void scale(double x, double y) {
        GL11.glScaled((double)x, (double)y, (double)1.0);
    }

    public static void rotate(double x, double y, double z, double angle) {
        GL11.glRotated((double)angle, (double)x, (double)y, (double)z);
    }

    public static void gradientSideways(double x, double y, double width, double height, boolean filled, Color color1, Color color2) {
        RenderUtils.start();
        GL11.glShadeModel((int)7425);
        GlStateManager.func_179118_c();
        if (color1 != null) {
            RenderUtils.color(color1);
        }
        RenderUtils.begin(filled ? 6 : 1);
        RenderUtils.vertex(x, y);
        RenderUtils.vertex(x, y + height);
        if (color2 != null) {
            RenderUtils.color(color2);
        }
        RenderUtils.vertex(x + width, y + height);
        RenderUtils.vertex(x + width, y);
        RenderUtils.end();
        GlStateManager.func_179141_d();
        GL11.glShadeModel((int)7424);
        RenderUtils.stop();
    }

    public static void gradientSideways(double x, double y, double width, double height, Color color1, Color color2) {
        RenderUtils.gradientSideways(x, y, width, height, true, color1, color2);
    }

    public static void gradientSidewaysCentered(double x, double y, double width, double height, Color color1, Color color2) {
        RenderUtils.gradientSideways(x -= width / 2.0, y -= height / 2.0, width, height, true, color1, color2);
    }

    public static void drawCircle(float x, float y, float radius, int start, int end, Color color) {
        classProvider.getGlStateManager().enableBlend();
        classProvider.getGlStateManager().disableTexture2D();
        IGlStateManager iGlStateManager = classProvider.getGlStateManager();
        int n = 0;
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        for (float i = (float)end; i >= (float)start; i -= 4.0f) {
            GL11.glVertex2f((float)((float)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))), (float)((float)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        classProvider.getGlStateManager().enableTexture2D();
        classProvider.getGlStateManager().disableBlend();
    }

    public static void drawCircle(IEntity entity, double rad, int color, boolean shade) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        GL11.glDepthMask((boolean)false);
        GlStateManager.func_179092_a((int)516, (float)0.0f);
        if (shade) {
            GL11.glShadeModel((int)7425);
        }
        GlStateManager.func_179129_p();
        GL11.glBegin((int)5);
        double x = entity.getLastTickPosX() + (entity.getPosX() - entity.getLastTickPosX()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosX();
        double y = entity.getLastTickPosY() + (entity.getPosY() - entity.getLastTickPosY()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosY() + Math.sin((double)System.currentTimeMillis() / 200.0) + 1.0;
        double z = entity.getLastTickPosZ() + (entity.getPosZ() - entity.getLastTickPosZ()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosZ();
        float i = 0.0f;
        while ((double)i < Math.PI * 2) {
            double vecX = x + rad * Math.cos(i);
            double vecZ = z + rad * Math.sin(i);
            HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
            Color c = RenderUtils.getGradientOffset(new Color((Integer)hud.getR().get(), (Integer)hud.getG().get(), (Integer)hud.getB().get()), new Color((Integer)hud.getR2().get(), (Integer)hud.getG2().get(), (Integer)hud.getB2().get()), i);
            if (shade) {
                GL11.glColor4f((float)((float)c.getRed() / 255.0f), (float)((float)c.getGreen() / 255.0f), (float)((float)c.getBlue() / 255.0f), (float)0.0f);
                GL11.glVertex3d((double)vecX, (double)(y - Math.cos((double)System.currentTimeMillis() / 200.0) / 2.0), (double)vecZ);
                GL11.glColor4f((float)((float)c.getRed() / 255.0f), (float)((float)c.getGreen() / 255.0f), (float)((float)c.getBlue() / 255.0f), (float)0.85f);
            }
            GL11.glVertex3d((double)vecX, (double)y, (double)vecZ);
            i = (float)((double)i + 0.09817477042468103);
        }
        GL11.glEnd();
        if (shade) {
            GL11.glShadeModel((int)7424);
        }
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GlStateManager.func_179092_a((int)516, (float)0.1f);
        GlStateManager.func_179089_o();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
        GL11.glColor3f((float)255.0f, (float)255.0f, (float)255.0f);
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, boolean sideways, int startColor, int endColor) {
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)7);
        RenderUtils.color(startColor);
        if (sideways) {
            GL11.glVertex2d((double)left, (double)top);
            GL11.glVertex2d((double)left, (double)bottom);
            RenderUtils.color(endColor);
            GL11.glVertex2d((double)right, (double)bottom);
            GL11.glVertex2d((double)right, (double)top);
        } else {
            GL11.glVertex2d((double)left, (double)top);
            RenderUtils.color(endColor);
            GL11.glVertex2d((double)left, (double)bottom);
            GL11.glVertex2d((double)right, (double)bottom);
            RenderUtils.color(startColor);
            GL11.glVertex2d((double)right, (double)top);
        }
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)3553);
    }

    public static void drawArc(float n, float n2, double n3, int n4, int n5, double n6, int n7) {
        n3 *= 2.0;
        n *= 2.0f;
        n2 *= 2.0f;
        float n8 = (float)(n4 >> 24 & 0xFF) / 255.0f;
        float n9 = (float)(n4 >> 16 & 0xFF) / 255.0f;
        float n10 = (float)(n4 >> 8 & 0xFF) / 255.0f;
        float n11 = (float)(n4 & 0xFF) / 255.0f;
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n12 = 0;
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        int n13 = 0;
        int n14 = 0;
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glLineWidth((float)n7);
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)n9, (float)n10, (float)n11, (float)n8);
        GL11.glBegin((int)3);
        int n122 = n5;
        while ((double)n122 <= n6) {
            GL11.glVertex2d((double)((double)n + Math.sin((double)n122 * Math.PI / 180.0) * n3), (double)((double)n2 + Math.cos((double)n122 * Math.PI / 180.0) * n3));
            ++n122;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        int n15 = 0;
        int n16 = 0;
    }

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        int n2 = 0;
        int n3 = 0;
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        int n = 0;
        int n2 = 0;
    }

    public static void renderShadowVertical(float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges, float red, float green, float blue) {
        double alpha = startAlpha;
        GlStateManager.func_179092_a((int)516, (float)0.0f);
        GL11.glLineWidth((float)lineWidth);
        if (right) {
            for (double x = 0.5; x < (double)size; x += 0.5) {
                GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
                GL11.glBegin((int)1);
                GL11.glVertex2d((double)(posX + x), (double)(posY1 - (edges ? x : 0.0)));
                GL11.glVertex2d((double)(posX + x), (double)(posY2 + (edges ? x : 0.0)));
                GL11.glEnd();
                alpha = startAlpha - x / (double)size;
            }
        } else {
            for (double x = 0.0; x < (double)size; x += 0.5) {
                GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
                GL11.glBegin((int)1);
                GL11.glVertex2d((double)(posX - x), (double)(posY1 - (edges ? x : 0.0)));
                GL11.glVertex2d((double)(posX - x), (double)(posY2 + (edges ? x : 0.0)));
                GL11.glEnd();
                alpha = startAlpha - x / (double)size;
            }
        }
    }

    public static void drawSmoothRect(double left, double top, double right, double bottom, int color) {
        GlStateManager.func_179117_G();
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        RenderUtils.drawRect(left, top, right, bottom, color);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderUtils.drawRect(left * 2.0 - 1.0, top * 2.0, left * 2.0, bottom * 2.0 - 1.0, color);
        RenderUtils.drawRect(left * 2.0, top * 2.0 - 1.0, right * 2.0, top * 2.0, color);
        RenderUtils.drawRect(right * 2.0, top * 2.0, right * 2.0 + 1.0, bottom * 2.0 - 1.0, color);
        GL11.glDisable((int)3042);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public static void renderShadowVertical(Color c, float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges) {
        GlStateManager.func_179117_G();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2884);
        RenderUtils.renderShadowVertical(lineWidth, startAlpha, size, posX, posY1, posY2, right, edges, (float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)3008);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)3042);
    }

    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        RenderUtils.enableGL2D();
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)7);
        RenderUtils.glColor(topColor);
        GL11.glVertex2f((float)x, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        RenderUtils.glColor(bottomColor);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
        GL11.glShadeModel((int)7424);
        RenderUtils.disableGL2D();
    }

    public static void drawColorRect(double left, double top, double right, double bottom, Color color1, Color color2, Color color3, Color color4) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        RenderUtils.glColor(color2);
        GL11.glVertex2d((double)left, (double)bottom);
        RenderUtils.glColor(color3);
        GL11.glVertex2d((double)right, (double)bottom);
        RenderUtils.glColor(color4);
        GL11.glVertex2d((double)right, (double)top);
        RenderUtils.glColor(color1);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
        int n2 = 0;
    }

    public static void drawCircleESP(IEntity entity, double rad, int color, boolean shade) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        GL11.glDepthMask((boolean)false);
        GlStateManager.func_179092_a((int)516, (float)0.0f);
        if (shade) {
            GL11.glShadeModel((int)7425);
        }
        GlStateManager.func_179129_p();
        GL11.glBegin((int)5);
        double x = entity.getLastTickPosX() + (entity.getPosX() - entity.getLastTickPosX()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosX();
        double y = entity.getLastTickPosY() + (entity.getPosY() - entity.getLastTickPosY()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosY() + Math.sin((double)System.currentTimeMillis() / 200.0) + 1.0;
        double z = entity.getLastTickPosZ() + (entity.getPosZ() - entity.getLastTickPosZ()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosZ();
        float i = 0.0f;
        while ((double)i < Math.PI * 2) {
            double vecX = x + rad * Math.cos(i);
            double vecZ = z + rad * Math.sin(i);
            Color c = ColorUtils.rainbow();
            if (shade) {
                GL11.glColor4f((float)((float)c.getRed() / 255.0f), (float)((float)c.getGreen() / 255.0f), (float)((float)c.getBlue() / 255.0f), (float)0.0f);
                GL11.glVertex3d((double)vecX, (double)(y - Math.cos((double)System.currentTimeMillis() / 200.0) / 2.0), (double)vecZ);
                GL11.glColor4f((float)((float)c.getRed() / 255.0f), (float)((float)c.getGreen() / 255.0f), (float)((float)c.getBlue() / 255.0f), (float)0.85f);
            }
            GL11.glVertex3d((double)vecX, (double)y, (double)vecZ);
            i = (float)((double)i + 0.09817477042468103);
        }
        GL11.glEnd();
        if (shade) {
            GL11.glShadeModel((int)7424);
        }
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GlStateManager.func_179092_a((int)516, (float)0.1f);
        GlStateManager.func_179089_o();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
        GL11.glColor3f((float)255.0f, (float)255.0f, (float)255.0f);
    }

    public static void setAlphaLimit(float limit) {
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a((int)516, (float)((float)((double)limit * 0.01)));
    }

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }
        float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
        float var6 = (float)(color >> 16 & 0xFF) / 255.0f;
        float var7 = (float)(color >> 8 & 0xFF) / 255.0f;
        float var8 = (float)(color & 0xFF) / 255.0f;
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)var6, (float)var7, (float)var8, (float)var11);
        worldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldRenderer.pos(left, bottom, 0.0).endVertex();
        worldRenderer.pos(right, bottom, 0.0).endVertex();
        worldRenderer.pos(right, top, 0.0).endVertex();
        worldRenderer.pos(left, top, 0.0).endVertex();
        Tessellator.func_178181_a().func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        RenderUtils.glColor(color);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    /*
     * Exception decompiling
     */
    public static int loadGlTexture(BufferedImage bufferedImage) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl7 : INVOKESTATIC - null : Stack underflow
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

    public static Color getGradientOffset2(Color color1, Color color2, double gident) {
        if (gident > 1.0) {
            double f1 = gident % 1.0;
            int f2 = (int)gident;
            gident = f2 % 2 == 0 ? f1 : 1.0 - f1;
        }
        double f3 = 1.0 - gident;
        int f4 = (int)((double)color1.getRed() * f3 + (double)color2.getRed() * gident);
        int f5 = (int)((double)color1.getGreen() * f3 + (double)color2.getGreen() * gident);
        int f6 = (int)((double)color1.getBlue() * f3 + (double)color2.getBlue() * gident);
        return new Color(f4, f5, f6);
    }

    /*
     * Exception decompiling
     */
    public static boolean glEnableBlend() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl9 : ILOAD_0 - null : trying to set 0 previously set to 1
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
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

    public static void glDrawFramebuffer(int framebufferTexture, int width, int height) {
        GL11.glBindTexture((int)3553, (int)framebufferTexture);
        GL11.glDisable((int)3008);
        boolean restore = RenderUtils.glEnableBlend();
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)0.0f, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2f((float)width, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)width, (float)0.0f);
        GL11.glEnd();
        RenderUtils.glRestoreBlend(restore);
        GL11.glEnable((int)3008);
    }

    public static void glRestoreBlend(boolean wasEnabled) {
        if (!wasEnabled) {
            GL11.glDisable((int)3042);
        }
    }

    public static void drawTexturedRectWithCustomAlpha(float x, float y, float width, float height, String image2, float alpha) {
        boolean disableAlpha;
        GL11.glPushMatrix();
        boolean enableBlend = GL11.glIsEnabled((int)3042);
        boolean bl = disableAlpha = !GL11.glIsEnabled((int)3008);
        if (!enableBlend) {
            GL11.glEnable((int)3042);
        }
        if (!disableAlpha) {
            GL11.glDisable((int)3008);
        }
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
        minecraft.func_110434_K().func_110577_a(new ResourceLocation("likingsense/shadow/" + image2 + ".png"));
        RenderUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        if (!enableBlend) {
            GL11.glDisable((int)3042);
        }
        if (!disableAlpha) {
            GL11.glEnable((int)3008);
        }
        GlStateManager.func_179117_G();
        GL11.glPopMatrix();
    }

    public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        float f = 1.0f / textureWidth;
        float f1 = 1.0f / textureHeight;
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_TEX));
        worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + width) * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + width) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    public static void drawShadowWithCustomAlpha(float x, float y, float width, float height, float alpha) {
        RenderUtils.drawTexturedRectWithCustomAlpha(x - 9.0f, y - 9.0f, 9.0f, 9.0f, "paneltopleft", alpha);
        RenderUtils.drawTexturedRectWithCustomAlpha(x - 9.0f, y + height, 9.0f, 9.0f, "panelbottomleft", alpha);
        RenderUtils.drawTexturedRectWithCustomAlpha(x + width, y + height, 9.0f, 9.0f, "panelbottomright", alpha);
        RenderUtils.drawTexturedRectWithCustomAlpha(x + width, y - 9.0f, 9.0f, 9.0f, "paneltopright", alpha);
        RenderUtils.drawTexturedRectWithCustomAlpha(x - 9.0f, y, 9.0f, height, "panelleft", alpha);
        RenderUtils.drawTexturedRectWithCustomAlpha(x + width, y, 9.0f, height, "panelright", alpha);
        RenderUtils.drawTexturedRectWithCustomAlpha(x, y - 9.0f, width, 9.0f, "paneltop", alpha);
        RenderUtils.drawTexturedRectWithCustomAlpha(x, y + height, width, 9.0f, "panelbottom", alpha);
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset) {
        int redPart;
        double inverse_percent;
        if (offset > 1.0) {
            inverse_percent = offset % 1.0;
            redPart = (int)offset;
            offset = redPart % 2 == 0 ? inverse_percent : 1.0 - inverse_percent;
        }
        inverse_percent = 1.0 - offset;
        redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offset);
        int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offset);
        int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    /*
     * Exception decompiling
     */
    public static void drawRoundedRect2(float left, float top, float right, float bottom, float radius, int points, int color) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Underrun type stack
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getEntry(StackSim.java:35)
         *     at org.benf.cfr.reader.bytecode.opcode.OperationFactoryDefault.getStackTypes(OperationFactoryDefault.java:51)
         *     at org.benf.cfr.reader.bytecode.opcode.OperationFactoryDup.getStackDelta(OperationFactoryDup.java:18)
         *     at org.benf.cfr.reader.bytecode.opcode.JVMInstr.getStackDelta(JVMInstr.java:315)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:199)
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

    public static void resetColor() {
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void bindTexture(int texture) {
        GL11.glBindTexture((int)3553, (int)texture);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.field_147621_c != mc.getDisplayWidth() || framebuffer.field_147618_d != mc.getDisplayHeight()) {
            if (framebuffer != null) {
                framebuffer.func_147608_a();
            }
            return new Framebuffer(mc.getDisplayWidth(), mc.getDisplayHeight(), true);
        }
        return framebuffer;
    }

    public static void drawOutlinedString(String str, int x, int y, int color, int color2) {
        FontLoaders.F18.drawString(str, (int)((float)x - 1.0f), y, color2);
        FontLoaders.F18.drawString(str, (int)((float)x + 1.0f), y, color2);
        FontLoaders.F18.drawString(str, x, (int)((float)y + 1.0f), color2);
        FontLoaders.F18.drawString(str, x, (int)((float)y - 1.0f), color2);
        FontLoaders.F18.drawString(str, x, y, color);
    }

    public static void drawRoundedRect2(float x, float y, float width, float height, float radius, int color) {
        int i;
        float x1 = x + width;
        float y1 = y + height;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x *= 2.0f;
        y *= 2.0f;
        x1 *= 2.0f;
        y1 *= 2.0f;
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GlStateManager.func_179147_l();
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        double v = Math.PI / 180;
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + radius + MathHelper.func_76126_a((float)((float)((double)i * (Math.PI / 180)))) * (radius * -1.0f)), (double)(y + radius + MathHelper.func_76134_b((float)((float)((double)i * (Math.PI / 180)))) * (radius * -1.0f)));
        }
        while (i <= 180) {
            GL11.glVertex2d((double)(x + radius + MathHelper.func_76126_a((float)((float)((double)i * (Math.PI / 180)))) * (radius * -1.0f)), (double)(y1 - radius + MathHelper.func_76134_b((float)((float)((double)i * (Math.PI / 180)))) * (radius * -1.0f)));
            i += 3;
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + MathHelper.func_76126_a((float)((float)((double)i * (Math.PI / 180)))) * radius), (double)(y1 - radius + MathHelper.func_76134_b((float)((float)((double)i * (Math.PI / 180)))) * radius));
        }
        while (i <= 180) {
            GL11.glVertex2d((double)(x1 - radius + MathHelper.func_76126_a((float)((float)((double)i * (Math.PI / 180)))) * radius), (double)(y + radius + MathHelper.func_76134_b((float)((float)((double)i * (Math.PI / 180)))) * radius));
            i += 3;
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static float animate(float target, float current, float speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0f) {
            speed = 0.0f;
        } else if (speed > 1.0f) {
            speed = 1.0f;
        }
        float dif = Math.abs(current - target);
        float factor = dif * speed;
        current = larger ? (current += factor) : (current -= factor);
        return current;
    }

    public static boolean isHovered(float x, float y, float w, float h, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x + w && (float)mouseY >= y && (float)mouseY <= y + h;
    }

    public static void outlineRect(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtils.drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawRect(double x, double y, double x2, double y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtils.glColor(new Color(color));
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void drawBlockBox(WBlockPos blockPos, Color color, boolean outline) {
        IRenderManager renderManager = mc.getRenderManager();
        ITimer timer = mc.getTimer();
        double x = (double)blockPos.getX() - renderManager.getRenderPosX();
        double y = (double)blockPos.getY() - renderManager.getRenderPosY();
        double z = (double)blockPos.getZ() - renderManager.getRenderPosZ();
        IAxisAlignedBB axisAlignedBB = classProvider.createAxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        IBlock block = BlockUtils.getBlock(blockPos);
        if (block != null) {
            IEntityPlayerSP player = mc.getThePlayer();
            double posX = player.getLastTickPosX() + (player.getPosX() - player.getLastTickPosX()) * (double)timer.getRenderPartialTicks();
            double posY = player.getLastTickPosY() + (player.getPosY() - player.getLastTickPosY()) * (double)timer.getRenderPartialTicks();
            double posZ = player.getLastTickPosZ() + (player.getPosZ() - player.getLastTickPosZ()) * (double)timer.getRenderPartialTicks();
            axisAlignedBB = block.getSelectedBoundingBox(mc.getTheWorld(), mc.getTheWorld().getBlockState(blockPos), blockPos).expand(0.002f, 0.002f, 0.002f).offset(-posX, -posY, -posZ);
        }
        int n = 0;
        RenderUtils.enableGlCap(3042);
        int[] nArray = new int[2];
        int[] nArray2 = nArray;
        int[] nArray3 = nArray;
        int n2 = 0;
        int n3 = 0;
        RenderUtils.disableGlCap((int[])0);
        GL11.glDepthMask((boolean)false);
        RenderUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() != 255 ? color.getAlpha() : (outline ? 26 : 35));
        RenderUtils.drawFilledBox(axisAlignedBB);
        if (outline) {
            GL11.glLineWidth((float)1.0f);
            RenderUtils.enableGlCap(2848);
            RenderUtils.glColor(color);
            RenderUtils.drawSelectionBoundingBox(axisAlignedBB);
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDepthMask((boolean)true);
        RenderUtils.resetCaps();
    }

    public static void drawSelectionBoundingBox(IAxisAlignedBB boundingBox) {
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldrenderer.pos(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ()).endVertex();
        worldrenderer.pos(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMaxZ()).endVertex();
        worldrenderer.pos(boundingBox.getMaxX(), boundingBox.getMinY(), boundingBox.getMaxZ()).endVertex();
        worldrenderer.pos(boundingBox.getMaxX(), boundingBox.getMinY(), boundingBox.getMinZ()).endVertex();
        worldrenderer.pos(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ()).endVertex();
        worldrenderer.pos(boundingBox.getMinX(), boundingBox.getMaxY(), boundingBox.getMinZ()).endVertex();
        worldrenderer.pos(boundingBox.getMinX(), boundingBox.getMaxY(), boundingBox.getMaxZ()).endVertex();
        worldrenderer.pos(boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ()).endVertex();
        worldrenderer.pos(boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMinZ()).endVertex();
        worldrenderer.pos(boundingBox.getMinX(), boundingBox.getMaxY(), boundingBox.getMinZ()).endVertex();
        worldrenderer.pos(boundingBox.getMinX(), boundingBox.getMaxY(), boundingBox.getMaxZ()).endVertex();
        worldrenderer.pos(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMaxZ()).endVertex();
        worldrenderer.pos(boundingBox.getMaxX(), boundingBox.getMinY(), boundingBox.getMaxZ()).endVertex();
        worldrenderer.pos(boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ()).endVertex();
        worldrenderer.pos(boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMinZ()).endVertex();
        worldrenderer.pos(boundingBox.getMaxX(), boundingBox.getMinY(), boundingBox.getMinZ()).endVertex();
        tessellator.draw();
    }

    public static void drawEntityBox(IEntity entity, Color color, boolean outline) {
        IRenderManager renderManager = mc.getRenderManager();
        ITimer timer = mc.getTimer();
        int n = 0;
        RenderUtils.enableGlCap(3042);
        int[] nArray = new int[2];
        int[] nArray2 = nArray;
        int[] nArray3 = nArray;
        int n2 = 0;
        int n3 = 0;
        RenderUtils.disableGlCap((int[])0);
        GL11.glDepthMask((boolean)false);
        double x = entity.getLastTickPosX() + (entity.getPosX() - entity.getLastTickPosX()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosX();
        double y = entity.getLastTickPosY() + (entity.getPosY() - entity.getLastTickPosY()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosY();
        double z = entity.getLastTickPosZ() + (entity.getPosZ() - entity.getLastTickPosZ()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosZ();
        IAxisAlignedBB entityBox = entity.getEntityBoundingBox();
        IAxisAlignedBB axisAlignedBB = classProvider.createAxisAlignedBB(entityBox.getMinX() - entity.getPosX() + x - 0.05, entityBox.getMinY() - entity.getPosY() + y, entityBox.getMinZ() - entity.getPosZ() + z - 0.05, entityBox.getMaxX() - entity.getPosX() + x + 0.05, entityBox.getMaxY() - entity.getPosY() + y + 0.15, entityBox.getMaxZ() - entity.getPosZ() + z + 0.05);
        if (outline) {
            GL11.glLineWidth((float)1.0f);
            RenderUtils.enableGlCap(2848);
            RenderUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), 95);
            RenderUtils.drawSelectionBoundingBox(axisAlignedBB);
        }
        RenderUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
        RenderUtils.drawFilledBox(axisAlignedBB);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDepthMask((boolean)true);
        RenderUtils.resetCaps();
    }

    public static void drawAxisAlignedBB(IAxisAlignedBB axisAlignedBB, Color color) {
        int n = 0;
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        RenderUtils.glColor(color);
        RenderUtils.drawFilledBox(axisAlignedBB);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawPlatform(double y, Color color, double size) {
        IRenderManager renderManager = mc.getRenderManager();
        double renderY = y - renderManager.getRenderPosY();
        RenderUtils.drawAxisAlignedBB(classProvider.createAxisAlignedBB(size, renderY + 0.02, size, -size, renderY, -size), color);
    }

    public static void drawPlatform(IEntity entity, Color color) {
        IRenderManager renderManager = mc.getRenderManager();
        ITimer timer = mc.getTimer();
        double x = entity.getLastTickPosX() + (entity.getPosX() - entity.getLastTickPosX()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosX();
        double y = entity.getLastTickPosY() + (entity.getPosY() - entity.getLastTickPosY()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosY();
        double z = entity.getLastTickPosZ() + (entity.getPosZ() - entity.getLastTickPosZ()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosZ();
        IAxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().offset(-entity.getPosX(), -entity.getPosY(), -entity.getPosZ()).offset(x, y, z);
        RenderUtils.drawAxisAlignedBB(classProvider.createAxisAlignedBB(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY() + 0.2, axisAlignedBB.getMinZ(), axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY() + 0.26, axisAlignedBB.getMaxZ()), color);
    }

    public static void drawFilledBox(IAxisAlignedBB axisAlignedBB) {
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMinX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMinZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMaxY(), axisAlignedBB.getMaxZ()).endVertex();
        worldRenderer.pos(axisAlignedBB.getMaxX(), axisAlignedBB.getMinY(), axisAlignedBB.getMaxZ()).endVertex();
        tessellator.draw();
    }

    public static void quickDrawRect(float x, float y, float x2, float y2) {
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void drawRect(float x, float y, float x2, float y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        RenderUtils.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)x2, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glVertex2f((float)x, (float)y2);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void newDrawRect(float left, float top, float right, float bottom, int color) {
        if (left < right) {
            float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b((double)left, (double)bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)top, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)left, (double)top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    /*
     * Exception decompiling
     */
    public static void drawRoundedCornerRect(float x, float y, float x1, float y1, float radius) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl34 : INVOKESTATIC - null : Stack underflow
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

    private static void quickPolygonCircle(float x, float y, float xRadius, float yRadius, int start, int end, int split) {
        for (int i = end; i >= start; i -= split) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * Math.PI / 180.0) * (double)xRadius), (double)((double)y + Math.cos((double)i * Math.PI / 180.0) * (double)yRadius));
        }
    }

    public static void drawRoundedCornerRect(float x, float y, float x1, float y1, float radius, int color) {
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glDisable((int)3553);
        boolean hasCull = GL11.glIsEnabled((int)2884);
        GL11.glDisable((int)2884);
        RenderUtils.glColor(color);
        RenderUtils.drawRoundedCornerRect(x, y, x1, y1, radius);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        RenderUtils.setGlState(2884, hasCull);
    }

    public static void drawRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color) {
        RenderUtils.drawRoundedRect(paramXStart, paramYStart, paramXEnd, paramYEnd, radius, color, true);
    }

    public static boolean isHovering(int mouseX, int mouseY, float xLeft, float yUp, float xRight, float yBottom) {
        return (float)mouseX > xLeft && (float)mouseX < xRight && (float)mouseY > yUp && (float)mouseY < yBottom;
    }

    public static float getAnimationState(float animation, float finalState, float speed) {
        float add = (float)deltaTime * speed;
        animation = animation < finalState ? (animation + add < finalState ? (animation += add) : finalState) : (animation - add > finalState ? (animation -= add) : finalState);
        return animation;
    }

    public static float smoothAnimation(float ani, float finalState, float speed, float scale) {
        return RenderUtils.getAnimationState(ani, finalState, Math.max(10.0f, Math.abs(ani - finalState) * speed) * scale);
    }

    public static double getAnimationStateSmooth(double target, double current, double speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        if (target == current) {
            return target;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        current = larger ? (current + factor > target ? target : (current += factor)) : (current - factor < target ? target : (current -= factor));
        return current;
    }

    public static void drawCircle2(float x, float y, float radius, int color) {
        RenderUtils.glColor(color);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void preRenderShade() {
        GlStateManager.func_179094_E();
        GlStateManager.func_179118_c();
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179103_j((int)7425);
        GL11.glEnable((int)2848);
        int n = 0;
        GlStateManager.func_179129_p();
    }

    public static void drawArcOutline(double x, double y, double radius, double startAngle, double endAngle, float lineWidth, Color color) {
        GlStateManager.func_179094_E();
        RenderUtils.preRenderShade();
        GL11.glLineWidth((float)lineWidth);
        VertexUtils.start(3);
        for (double i = startAngle / 360.0 * 100.0; i <= endAngle / 360.0 * 100.0; i += 1.0) {
            double angle = Math.PI * 2 * i / 100.0 + Math.toRadians(180.0);
            VertexUtils.add(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, color);
        }
        VertexUtils.end();
        RenderUtils.postRenderShade();
        GlStateManager.func_179121_F();
    }

    public static void postRenderShade() {
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179103_j((int)7424);
        GL11.glDisable((int)2848);
        GlStateManager.func_179089_o();
        GlStateManager.func_179121_F();
    }

    public static void drawOutlinedRoundedRect(double x, double y, double width, double height, double radius, float linewidth, int color) {
        int i;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        double x1 = x + width;
        double y1 = y + height;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glLineWidth((float)linewidth);
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        while (i <= 180) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
            i += 3;
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        while (i <= 180) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * radius));
            i += 3;
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawGidentOutlinedRoundedRect(double x, double y, double width, double height, double radius, float linewidth) {
        int i;
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        double x1 = x + width;
        double y1 = y + height;
        int colorI = 0;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glLineWidth((float)linewidth);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        for (i = 0; i <= 90; i += 3) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(new Color((Integer)hud.getR().get(), (Integer)hud.getG().get(), (Integer)hud.getB().get()).getRGB(), new Color((Integer)hud.getR2().get(), (Integer)hud.getG2().get(), (Integer)hud.getB2().get()).getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
            ++colorI;
        }
        i = 0;
        while ((double)i <= y) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(new Color((Integer)hud.getR().get(), (Integer)hud.getG().get(), (Integer)hud.getB().get()).getRGB(), new Color((Integer)hud.getR2().get(), (Integer)hud.getG2().get(), (Integer)hud.getB2().get()).getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)x, (double)(y1 - radius - (double)i));
            ++colorI;
            i += 3;
        }
        while (i <= 180) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(new Color((Integer)hud.getR().get(), (Integer)hud.getG().get(), (Integer)hud.getB().get()).getRGB(), new Color((Integer)hud.getR2().get(), (Integer)hud.getG2().get(), (Integer)hud.getB2().get()).getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
            ++colorI;
            i += 3;
        }
        while (i <= 180) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(new Color((Integer)hud.getR().get(), (Integer)hud.getG().get(), (Integer)hud.getB().get()).getRGB(), new Color((Integer)hud.getR2().get(), (Integer)hud.getG2().get(), (Integer)hud.getB2().get()).getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)(x + radius + (double)i), (double)y1);
            ++colorI;
            i += 3;
        }
        for (i = 0; i <= 90; i += 3) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(new Color((Integer)hud.getR().get(), (Integer)hud.getG().get(), (Integer)hud.getB().get()).getRGB(), new Color((Integer)hud.getR2().get(), (Integer)hud.getG2().get(), (Integer)hud.getB2().get()).getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * radius));
            ++colorI;
        }
        for (i = 0; i <= 90; i += 3) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(new Color((Integer)hud.getR().get(), (Integer)hud.getG().get(), (Integer)hud.getB().get()).getRGB(), new Color((Integer)hud.getR2().get(), (Integer)hud.getG2().get(), (Integer)hud.getB2().get()).getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)x1, (double)(y1 - radius - (double)i));
            ++colorI;
        }
        while (i <= 180) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(new Color((Integer)hud.getR().get(), (Integer)hud.getG().get(), (Integer)hud.getB().get()).getRGB(), new Color((Integer)hud.getR2().get(), (Integer)hud.getG2().get(), (Integer)hud.getB2().get()).getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * radius));
            ++colorI;
            i += 3;
        }
        while (i <= 180) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(new Color((Integer)hud.getR().get(), (Integer)hud.getG().get(), (Integer)hud.getB().get()).getRGB(), new Color((Integer)hud.getR2().get(), (Integer)hud.getG2().get(), (Integer)hud.getB2().get()).getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)(x1 - radius - (double)i), (double)y);
            ++colorI;
            i += 3;
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawRoundedRectOutlineNew(double x, double y, double width, double height, double cornerRadius, Color color) {
        RenderUtils.drawRect(x - 0.5, y + cornerRadius, x + 0.5, y + height - cornerRadius, color.getRGB());
        RenderUtils.drawRect(x + width - 0.5, y + cornerRadius, x + width + 0.5, y + height - cornerRadius, color.getRGB());
        RenderUtils.drawRect(x + cornerRadius, y - 0.5, x + width - cornerRadius, y + 0.5, color.getRGB());
        RenderUtils.drawRect(x + cornerRadius, y + height - 0.5, x + width - cornerRadius, y + height + 0.5, color.getRGB());
        RenderUtils.drawArcOutline(x + cornerRadius, y + cornerRadius, cornerRadius, 0.0, 90.0, 2.0f, color);
        RenderUtils.drawArcOutline(x + width - cornerRadius, y + cornerRadius, cornerRadius, 270.0, 360.0, 2.0f, color);
        RenderUtils.drawArcOutline(x + width - cornerRadius, y + height - cornerRadius, cornerRadius, 180.0, 270.0, 2.0f, color);
        RenderUtils.drawArcOutline(x + cornerRadius, y + height - cornerRadius, cornerRadius, 90.0, 180.0, 2.0f, color);
    }

    public static void drawOutLineRect(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtils.drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color, boolean popPush) {
        double i;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        float z = 0.0f;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }
        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }
        double x1 = paramXStart + radius;
        double y1 = paramYStart + radius;
        double x2 = paramXEnd - radius;
        double y2 = paramYEnd - radius;
        if (popPush) {
            GL11.glPushMatrix();
        }
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        double degree = Math.PI / 180;
        for (i = 0.0; i <= 90.0; i += 1.0) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 90.0; i <= 180.0; i += 1.0) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 180.0; i <= 270.0; i += 1.0) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 270.0; i <= 360.0; i += 1.0) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        if (popPush) {
            GL11.glPopMatrix();
        }
    }

    public static void drawRect(int x, int y, int x2, int y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        RenderUtils.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2i((int)x2, (int)y);
        GL11.glVertex2i((int)x, (int)y);
        GL11.glVertex2i((int)x, (int)y2);
        GL11.glVertex2i((int)x2, (int)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void quickDrawRect(float x, float y, float x2, float y2, int color) {
        RenderUtils.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void drawRect(float x, float y, float x2, float y2, Color color) {
        RenderUtils.drawRect(x, y, x2, y2, color.getRGB());
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float width, int color1, int color2) {
        RenderUtils.drawRect(x, y, x2, y2, color2);
        RenderUtils.drawBorder(x, y, x2, y2, width, color1);
    }

    public static void drawBorder(float x, float y, float x2, float y2, float width, int color1) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        RenderUtils.glColor(color1);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void quickDrawBorderedRect(float x, float y, float x2, float y2, float width, int color1, int color2) {
        RenderUtils.quickDrawRect(x, y, x2, y2, color2);
        RenderUtils.glColor(color1);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void drawLoadingCircle(float x, float y) {
        for (int i = 0; i < 4; ++i) {
            int rot = (int)(System.nanoTime() / 5000000L * (long)i % 360L);
            RenderUtils.drawCircle(x, y, i * 10, rot - 180, rot);
        }
    }

    public static void drawCircle(float x, float y, float radius, int start, int end) {
        classProvider.getGlStateManager().enableBlend();
        classProvider.getGlStateManager().disableTexture2D();
        IGlStateManager iGlStateManager = classProvider.getGlStateManager();
        int n = 0;
        RenderUtils.glColor(Color.WHITE);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        for (float i = (float)end; i >= (float)start; i -= 4.0f) {
            GL11.glVertex2f((float)((float)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))), (float)((float)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        classProvider.getGlStateManager().enableTexture2D();
        classProvider.getGlStateManager().disableBlend();
    }

    public static void enableSmoothLine(float width) {
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)2848);
        int n2 = 0;
        int n3 = 0;
        GL11.glLineWidth((float)width);
    }

    public static void disableSmoothLine() {
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glDepthMask((boolean)true);
        GL11.glCullFace((int)1029);
        GL11.glDisable((int)2848);
        int n = 0;
        int n2 = 0;
    }

    /*
     * Exception decompiling
     */
    public static void drawFilledCircle(int xx, int yy, float radius, Color color) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
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

    public static void drawImage(IResourceLocation image2, int x, int y, int width, int height) {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        int n = 0;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.getTextureManager().bindTexture(image2);
        RenderUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void drawImage4(ResourceLocation image2, int x, int y, int width, int height) {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        int n = 0;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.getTextureManager().bindTexture2(image2);
        RenderUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void enable(int glTarget) {
        GL11.glEnable((int)glTarget);
    }

    public static void disable(int glTarget) {
        GL11.glDisable((int)glTarget);
    }

    /*
     * Exception decompiling
     */
    public static void image(ResourceLocation imageLocation, int x, int y, int width, int height) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl2 : INVOKESTATIC - null : Stack underflow
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

    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float f = 1.0f / textureWidth;
        float f1 = 1.0f / textureHeight;
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_TEX));
        worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + (float)height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + (float)width) * f, (v + (float)height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + (float)width) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    public static void glColor(int red, int green, int blue, int alpha) {
        GL11.glColor4f((float)((float)red / 255.0f), (float)((float)green / 255.0f), (float)((float)blue / 255.0f), (float)((float)alpha / 255.0f));
    }

    public static void glColor(Color color) {
        RenderUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void glColor(int hex) {
        RenderUtils.glColor(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, hex >> 24 & 0xFF);
    }

    public static void draw2D(IEntityLivingBase entity, double posX, double posY, double posZ, int color, int backgroundColor) {
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        GL11.glRotated((double)(-mc.getRenderManager().getPlayerViewY()), (double)0.0, (double)1.0, (double)0.0);
        GL11.glScaled((double)-0.1, (double)-0.1, (double)0.1);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glDepthMask((boolean)true);
        RenderUtils.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[0]);
        RenderUtils.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[1]);
        GL11.glTranslated((double)0.0, (double)(21.0 + -(entity.getEntityBoundingBox().getMaxY() - entity.getEntityBoundingBox().getMinY()) * 12.0), (double)0.0);
        RenderUtils.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[2]);
        RenderUtils.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[3]);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void draw2D(WBlockPos blockPos, int color, int backgroundColor) {
        IRenderManager renderManager = mc.getRenderManager();
        double posX = (double)blockPos.getX() + 0.5 - renderManager.getRenderPosX();
        double posY = (double)blockPos.getY() - renderManager.getRenderPosY();
        double posZ = (double)blockPos.getZ() + 0.5 - renderManager.getRenderPosZ();
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        GL11.glRotated((double)(-mc.getRenderManager().getPlayerViewY()), (double)0.0, (double)1.0, (double)0.0);
        GL11.glScaled((double)-0.1, (double)-0.1, (double)0.1);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glDepthMask((boolean)true);
        RenderUtils.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[0]);
        RenderUtils.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[1]);
        GL11.glTranslated((double)0.0, (double)9.0, (double)0.0);
        RenderUtils.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[2]);
        RenderUtils.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[3]);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void renderNameTag(String string, double x, double y, double z) {
        IRenderManager renderManager = mc.getRenderManager();
        GL11.glPushMatrix();
        GL11.glTranslated((double)(x - renderManager.getRenderPosX()), (double)(y - renderManager.getRenderPosY()), (double)(z - renderManager.getRenderPosZ()));
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-mc.getRenderManager().getPlayerViewY()), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)mc.getRenderManager().getPlayerViewX(), (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glScalef((float)-0.05f, (float)-0.05f, (float)0.05f);
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int width = Fonts.posterama35.getStringWidth(string) / 2;
        RenderUtils.drawRect(-width - 1, -1, width + 1, Fonts.posterama35.getFontHeight(), Integer.MIN_VALUE);
        Fonts.posterama35.drawString(string, -width, 1.5f, Color.WHITE.getRGB(), true);
        RenderUtils.resetCaps();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

    public static void makeScissorBox(float x, float y, float x2, float y2) {
        IScaledResolution scaledResolution = classProvider.createScaledResolution(mc);
        int factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)scaledResolution.getScaledHeight() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }

    public static void resetCaps() {
        glCapMap.forEach(RenderUtils::setGlState);
    }

    public static void enableGlCap(int cap) {
        RenderUtils.setGlCap(cap, true);
    }

    public static void enableGlCap(int ... caps) {
        for (int cap : caps) {
            RenderUtils.setGlCap(cap, true);
        }
    }

    public static void disableGlCap(int cap) {
        RenderUtils.setGlCap(cap, true);
    }

    public static void disableGlCap(int ... caps) {
        for (int cap : caps) {
            RenderUtils.setGlCap(cap, false);
        }
    }

    public static void setGlCap(int cap, boolean state) {
        glCapMap.put(cap, GL11.glGetBoolean((int)cap));
        RenderUtils.setGlState(cap, state);
    }

    public static void setGlState(int cap, boolean state) {
        if (state) {
            GL11.glEnable((int)cap);
        } else {
            GL11.glDisable((int)cap);
        }
    }

    public static void drawTriAngle(float cx, float cy, float r, float n, Color color, boolean polygon) {
        cx = (float)((double)cx * 2.0);
        cy = (float)((double)cy * 2.0);
        double b = 6.2831852 / (double)n;
        double p = Math.cos(b);
        double s = Math.sin(b);
        r = (float)((double)r * 2.0);
        double x = r;
        double y = 0.0;
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glLineWidth((float)1.0f);
        RenderUtils.enableGlCap(2848);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n2 = 0;
        GlStateManager.func_179117_G();
        RenderUtils.glColor(color);
        GlStateManager.func_179152_a((float)0.5f, (float)0.5f, (float)0.5f);
        worldrenderer.begin(polygon ? 9 : 2, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        int ii = 0;
        while ((float)ii < n) {
            worldrenderer.pos(x + (double)cx, y + (double)cy, 0.0).endVertex();
            double t2 = x;
            x = p * x - s * y;
            y = s * t2 + p * y;
            ++ii;
        }
        tessellator.draw();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawCircle(float x, float y, float radius, float lineWidth, int start, int end, Color color) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        RenderUtils.glColor(color);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        for (float i = (float)end; i >= (float)start; i -= 4.0f) {
            GL11.glVertex2f((float)((float)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))), (float)((float)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawCircle(double x, double y, double radius, float startAngle, float endAngle, int color, float lineWidth) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179118_c();
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        for (int i = (int)((double)startAngle / 360.0 * 100.0); i <= (int)((double)endAngle / 360.0 * 100.0); ++i) {
            double angle = Math.PI * 2 * (double)i / 100.0 + Math.toRadians(180.0);
            RenderUtils.color(color);
            GL11.glVertex2d((double)(x + Math.sin(angle) * radius), (double)(y + Math.cos(angle) * radius));
        }
        GL11.glEnd();
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GL11.glDisable((int)2848);
        GlStateManager.func_179121_F();
        GlStateManager.func_179117_G();
    }

    public static void color(float red, float green, float blue, float alpha) {
        GL11.glColor4f((float)(red / 255.0f), (float)(green / 255.0f), (float)(blue / 255.0f), (float)(alpha / 255.0f));
    }

    public static void color(Color color) {
        RenderUtils.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
    }

    public static void color2(Color color) {
        GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    public static void color(int color) {
        RenderUtils.color(color, (float)(color >> 24 & 0xFF) / 255.0f);
    }

    public static void color(int color, float alpha) {
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)r, (float)g, (float)b, (float)alpha);
    }

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0f / tileWidth;
        float f1 = 1.0f / tileHeight;
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_TEX));
        worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + (float)vHeight) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + (float)uWidth) * f, (v + (float)vHeight) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + (float)uWidth) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    public static void drawShadowWithCustomAlpha(double d, double d1, double d2, double d3, double alpha) {
        RenderUtils.drawShadowWithCustomAlpha((float)d, (float)d1, (float)d2, (float)d3, (float)alpha);
    }

    static {
        for (int i = 0; i < DISPLAY_LISTS_2D.length; ++i) {
            RenderUtils.DISPLAY_LISTS_2D[i] = GL11.glGenLists((int)1);
        }
        GL11.glNewList((int)DISPLAY_LISTS_2D[0], (int)4864);
        RenderUtils.quickDrawRect(-7.0f, 2.0f, -4.0f, 3.0f);
        RenderUtils.quickDrawRect(4.0f, 2.0f, 7.0f, 3.0f);
        RenderUtils.quickDrawRect(-7.0f, 0.5f, -6.0f, 3.0f);
        RenderUtils.quickDrawRect(6.0f, 0.5f, 7.0f, 3.0f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[1], (int)4864);
        RenderUtils.quickDrawRect(-7.0f, 3.0f, -4.0f, 3.3f);
        RenderUtils.quickDrawRect(4.0f, 3.0f, 7.0f, 3.3f);
        RenderUtils.quickDrawRect(-7.3f, 0.5f, -7.0f, 3.3f);
        RenderUtils.quickDrawRect(7.0f, 0.5f, 7.3f, 3.3f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[2], (int)4864);
        RenderUtils.quickDrawRect(4.0f, -20.0f, 7.0f, -19.0f);
        RenderUtils.quickDrawRect(-7.0f, -20.0f, -4.0f, -19.0f);
        RenderUtils.quickDrawRect(6.0f, -20.0f, 7.0f, -17.5f);
        RenderUtils.quickDrawRect(-7.0f, -20.0f, -6.0f, -17.5f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[3], (int)4864);
        RenderUtils.quickDrawRect(7.0f, -20.0f, 7.3f, -17.5f);
        RenderUtils.quickDrawRect(-7.3f, -20.0f, -7.0f, -17.5f);
        RenderUtils.quickDrawRect(4.0f, -20.3f, 7.3f, -20.0f);
        RenderUtils.quickDrawRect(-7.3f, -20.3f, -4.0f, -20.0f);
        GL11.glEndList();
    }
}

