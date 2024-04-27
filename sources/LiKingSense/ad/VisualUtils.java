/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Timer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 *  org.lwjgl.util.glu.GLU
 */
package ad;

import ad.MathUtils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

public class VisualUtils
extends MinecraftInstance {
    private static final Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();
    private static final FloatBuffer WND_POS_BUFFER = GLAllocation.func_74529_h((int)4);
    private static final IntBuffer VIEWPORT_BUFFER = GLAllocation.func_74527_f((int)16);
    private static final FloatBuffer MODEL_MATRIX_BUFFER = GLAllocation.func_74529_h((int)16);
    private static final FloatBuffer PROJECTION_MATRIX_BUFFER = GLAllocation.func_74529_h((int)16);
    private static final IntBuffer SCISSOR_BUFFER = GLAllocation.func_74527_f((int)16);
    private static FloatBuffer colorBuffer;
    public static float deltaTime;
    static boolean drawMode;
    private static int counts;
    public static float delta;
    public static double renderPosX;
    public static double renderPosY;
    public static double renderPosZ;
    private static Frustum frustrum;
    protected float zLevel;
    private static ScaledResolution scaledResolution;
    private static int lastWidth;
    private static int lastHeight;
    private static int lastScaledWidth;
    private static int lastScaledHeight;
    private static int lastGuiScale;
    private static final int[] DISPLAY_LISTS_2D;
    private static FloatBuffer modelView;
    private static FloatBuffer projection;
    private static IntBuffer viewport;
    private static final FloatBuffer modelview;
    private static final FloatBuffer projections;
    private static final long startTime;
    public static boolean antialiiasing;
    private final Supplier<Color> colorSupplier;

    public Color getColor() {
        return this.colorSupplier.get();
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static int width() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78326_a();
    }

    public static int height() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78328_b();
    }

    public static boolean isHovered(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (double)mouseX > x && (double)mouseX < width && (double)mouseY > y && (double)mouseY < height;
    }

    public static void resetColor() {
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void prepareScissorBox(double x, double y, double x2, double y2) {
        ScaledResolution scale = new ScaledResolution(Minecraft.func_71410_x());
        int factor = scale.func_78325_e();
        GL11.glScissor((int)((int)(x * (double)factor)), (int)((int)(((double)scale.func_78328_b() - y2) * (double)factor)), (int)((int)((x2 - x) * (double)factor)), (int)((int)((y2 - y) * (double)factor)));
    }

    public static void drawBorderedRoundedRect(float x, float y, float x1, float y1, float borderSize, int borderC, int insideC) {
        VisualUtils.drawRoundedRect(x, y, x1, y1, borderSize, borderC);
        VisualUtils.drawRoundedRect(x + 0.5f, y + 0.5f, x1 - 0.5f, y1 - 0.5f, borderSize, insideC);
    }

    public static void drawBorderedRoundedRect(float x, float y, float x1, float y1, float radius, float borderSize, int borderC, int insideC) {
        VisualUtils.drawRoundedRect(x, y, x1, y1, radius, borderC);
        VisualUtils.drawRoundedRect(x + borderSize, y + borderSize, x1 - borderSize, y1 - borderSize, radius, insideC);
    }

    public static float getAnimationState(float animation, float finalState, float speed) {
        float add = deltaTime * speed;
        animation = animation < finalState ? (animation + add < finalState ? (animation = animation + add) : finalState) : (animation - add > finalState ? (animation = animation - add) : finalState);
        return animation;
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

    public static float smoothAnimation(float ani, float finalState, float speed, float scale) {
        return VisualUtils.getAnimationState(ani, finalState, Math.max(10.0f, Math.abs(ani - finalState) * speed) * scale);
    }

    public static void drawImage(ResourceLocation image2, float x, float y, float width, float height, float alpha) {
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        GL11.glDepthMask((boolean)false);
        int n = 0;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        VisualUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask((boolean)true);
        GlStateManager.func_179084_k();
        GlStateManager.func_179126_j();
        GlStateManager.func_179117_G();
    }

    public static void drawImage(ResourceLocation image2, float x, float y, float width, float height, int color) {
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        GL11.glDepthMask((boolean)false);
        int n = 0;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        VisualUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask((boolean)true);
        GlStateManager.func_179084_k();
        GlStateManager.func_179126_j();
        GlStateManager.func_179117_G();
    }

    public static void drawImage(ResourceLocation image2, int x, int y, int width, int height, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        int n = 0;
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getRed() / 255.0f), (float)1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static double getAnimationState1(double animation, double finalState, double speed) {
        if (animation == finalState || speed == 0.0) {
            return animation;
        }
        double minSpeed = speed / 6.0;
        double maxSpeed = speed * 2.5;
        double sp33d = Math.pow(Math.abs(finalState - animation), 1.2) * 1.8;
        sp33d = Math.max(minSpeed, Math.min(maxSpeed, sp33d));
        float add = (float)((double)delta * sp33d);
        animation = animation < finalState ? (animation + (double)add < finalState ? (animation += (double)add) : finalState) : (animation - (double)add > finalState ? (animation -= (double)add) : finalState);
        return animation;
    }

    public static void drawCircle(Entity e, float PartialTicks, double drawPercent) {
        VisualUtils.mc2.field_71460_t.func_175072_h();
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)2884);
        GL11.glShadeModel((int)7425);
        VisualUtils.mc2.field_71460_t.func_175072_h();
        AxisAlignedBB bb = e.func_174813_aQ();
        float radius = (float)((bb.field_72336_d - bb.field_72340_a + (bb.field_72334_f - bb.field_72339_c)) * 0.5);
        double height = bb.field_72337_e - bb.field_72338_b;
        EntityLivingBase it = (EntityLivingBase)e;
        double x = it.field_70142_S + (it.field_70165_t - it.field_70142_S) * (double)PartialTicks - VisualUtils.mc2.func_175598_ae().field_78730_l;
        double y = it.field_70137_T + (it.field_70163_u - it.field_70137_T) * (double)PartialTicks - VisualUtils.mc2.func_175598_ae().field_78731_m + height * drawPercent;
        double z = it.field_70136_U + (it.field_70161_v - it.field_70136_U) * (double)PartialTicks - VisualUtils.mc2.func_175598_ae().field_78728_n;
        VisualUtils.mc2.field_71460_t.func_175072_h();
        GL11.glBegin((int)8);
        double eased = height / 3.0 * ((drawPercent > 0.5 ? 1.0 - drawPercent : drawPercent) * (double)(drawMode ? -1 : 1));
        for (int i = 0; i <= 360; i += 5) {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.5f);
            GL11.glVertex3d((double)(x - Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)y, (double)(z + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.0f);
            GL11.glVertex3d((double)(x - Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)(y + eased), (double)(z + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)2884);
        GL11.glShadeModel((int)7424);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static void drawCircle2(Entity e, float PartialTicks, double drawPercent) {
        VisualUtils.mc2.field_71460_t.func_175072_h();
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2929);
        AxisAlignedBB bb = e.func_174813_aQ();
        float radius = (float)((bb.field_72336_d - bb.field_72340_a + (bb.field_72334_f - bb.field_72339_c)) * 0.5);
        double height = bb.field_72337_e - bb.field_72338_b;
        EntityLivingBase it = (EntityLivingBase)e;
        double x = it.field_70142_S + (it.field_70165_t - it.field_70142_S) * (double)PartialTicks - VisualUtils.mc2.func_175598_ae().field_78730_l;
        double y = it.field_70137_T + (it.field_70163_u - it.field_70137_T) * (double)PartialTicks - VisualUtils.mc2.func_175598_ae().field_78731_m + height * drawPercent;
        double z = it.field_70136_U + (it.field_70161_v - it.field_70136_U) * (double)PartialTicks - VisualUtils.mc2.func_175598_ae().field_78728_n;
        VisualUtils.mc2.field_71460_t.func_175072_h();
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        for (int i = 0; i <= 360; i += 10) {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glVertex3d((double)(x - Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)y, (double)(z + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static Color getColorAnimationState(Color animation, Color finalState, double speed) {
        float add = (float)((double)deltaTime * speed);
        float animationr = animation.getRed();
        float animationg = animation.getGreen();
        float animationb = animation.getBlue();
        float finalStater = finalState.getRed();
        float finalStateg = finalState.getGreen();
        float finalStateb = finalState.getBlue();
        float finalStatea = finalState.getAlpha();
        float f = animationr < finalStater ? (animationr + add < finalStater ? (animationr = animationr + add) : finalStater) : (animationr = animationr - add > finalStater ? (animationr = animationr - add) : finalStater);
        float f2 = animationg < finalStateg ? (animationg + add < finalStateg ? (animationg = animationg + add) : finalStateg) : (animationg = animationg - add > finalStateg ? (animationg = animationg - add) : finalStateg);
        animationb = animationb < finalStateb ? (animationb + add < finalStateb ? (animationb = animationb + add) : finalStateb) : (animationb - add > finalStateb ? (animationb = animationb - add) : finalStateb);
        animationr /= 255.0f;
        animationg /= 255.0f;
        animationb /= 255.0f;
        finalStatea /= 255.0f;
        if (animationr > 1.0f) {
            animationr = 1.0f;
        }
        if (animationg > 1.0f) {
            animationg = 1.0f;
        }
        if (animationb > 1.0f) {
            animationb = 1.0f;
        }
        if (finalStatea > 1.0f) {
            finalStatea = 1.0f;
        }
        return new Color(animationr, animationg, animationb, finalStatea);
    }

    public static void drawLine(float x, float y, float x2, float y2, Color color) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)1.0f);
        GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        bufferbuilder.func_181668_a(1, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b((double)x, (double)y, 0.0).func_181675_d();
        bufferbuilder.func_181662_b((double)x2, (double)y2, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GL11.glDisable((int)2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    /*
     * Exception decompiling
     */
    public static void drawImage2(ResourceLocation image, float x, float y, int width, int height) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl29 : INVOKESTATIC - null : Stack underflow
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
    public static void drawImage3(ResourceLocation image, float x, float y, int width, int height, float r, float g, float b, float al) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl29 : INVOKESTATIC - null : Stack underflow
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
    public static int FadeRainbow(int var2, float bright, float st) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl15 : INVOKESPECIAL - null : Stack underflow
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

    public static void drawPoses(Color color, List<Vec3d> positions) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2929);
        VisualUtils.mc2.field_71460_t.func_175072_h();
        GL11.glBegin((int)3);
        RenderUtils.glColor(color);
        double renderPosX = VisualUtils.mc2.func_175598_ae().field_78730_l;
        double renderPosY = VisualUtils.mc2.func_175598_ae().field_78731_m;
        double renderPosZ = VisualUtils.mc2.func_175598_ae().field_78728_n;
        for (Vec3d pos : positions) {
            GL11.glVertex3d((double)(pos.field_72450_a - renderPosX), (double)(pos.field_72448_b - renderPosY), (double)(pos.field_72449_c - renderPosZ));
        }
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
        GL11.glEnd();
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
        int n2 = 0;
    }

    public static void drawHorizontalLine(float x, float y, float x1, float thickness, int color) {
        VisualUtils.rectangle(x, y, x1, y + thickness, color);
    }

    public static void drawVerticalLine(float x, float y, float y1, float thickness, int color) {
        VisualUtils.rectangle(x, y, x + thickness, y1, color);
    }

    public static void shadow(Entity player, double x, double y, double z, double range, int s, int color) {
        GL11.glPushMatrix();
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glDisable((int)2929);
        GL11.glEnable((int)2848);
        GL11.glDepthMask((boolean)true);
        GlStateManager.func_179137_b((double)x, (double)y, (double)z);
        GlStateManager.func_179131_c((float)0.1f, (float)0.1f, (float)0.1f, (float)0.75f);
        GlStateManager.func_179114_b((float)180.0f, (float)90.0f, (float)0.0f, (float)2.0f);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)90.0f, (float)90.0f);
        GlStateManager.func_179117_G();
        VisualUtils.glColor(color);
        GL11.glBegin((int)2);
        Cylinder c = new Cylinder();
        c.setDrawStyle(100011);
        c.draw((float)(range - 0.45), (float)(range - 0.5), 0.0f, s, 0);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static void cylinder(Entity player, double x, double y, double z, double range, int s, int color) {
        GL11.glPushMatrix();
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glDisable((int)2929);
        GL11.glEnable((int)2848);
        GL11.glDepthMask((boolean)true);
        GlStateManager.func_179137_b((double)x, (double)y, (double)z);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)0.75f);
        GlStateManager.func_179114_b((float)180.0f, (float)90.0f, (float)0.0f, (float)2.0f);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)90.0f, (float)90.0f);
        GlStateManager.func_179117_G();
        VisualUtils.glColor(color);
        GL11.glBegin((int)2);
        Cylinder c = new Cylinder();
        c.setDrawStyle(100011);
        c.draw((float)(range - 0.5), (float)(range - 0.5), 0.0f, s, 0);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static void NdrawCircle(float cx, float cy, float r, int num_segments, float width, int color) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        float theta = (float)(6.2831852 / (double)num_segments);
        float p = (float)Math.cos(theta);
        float s = (float)Math.sin(theta);
        float x = r *= 2.0f;
        float y = 0.0f;
        VisualUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glLineWidth((float)width);
        GlStateManager.func_179117_G();
        VisualUtils.glColor(color);
        GL11.glBegin((int)2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f((float)(x + cx), (float)(y + cy));
            float t2 = x;
            x = p * x - s * y;
            y = s * t2 + p * y;
        }
        VisualUtils.glColor(color);
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        VisualUtils.disableGL2D();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static void drawESPCircle(float cx, float cy, float r, int num_segments, int color) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        float theta = (float)(6.2831852 / (double)num_segments);
        float p = (float)Math.cos(theta);
        float s = (float)Math.sin(theta);
        float x = r *= 2.0f;
        float y = 0.0f;
        VisualUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179117_G();
        VisualUtils.glColor(color);
        GL11.glBegin((int)2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f((float)(x + cx), (float)(y + cy));
            float t2 = x;
            x = p * x - s * y;
            y = s * t2 + p * y;
        }
        Colors.getColor(-1);
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        VisualUtils.disableGL2D();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static void updateView() {
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
    }

    public static Vec3d WorldToScreen(Vec3d position) {
        FloatBuffer screenPositions = BufferUtils.createFloatBuffer((int)3);
        boolean result = GLU.gluProject((float)((float)position.field_72450_a), (float)((float)position.field_72448_b), (float)((float)position.field_72449_c), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenPositions);
        if (result) {
            return new Vec3d((double)screenPositions.get(0), (double)((float)Display.getHeight() - screenPositions.get(1)), (double)screenPositions.get(2));
        }
        return null;
    }

    public static void drawNewRect(float left, float top, float right, float bottom, int color) {
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
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b((double)left, (double)bottom, 0.0).func_181675_d();
        bufferbuilder.func_181662_b((double)right, (double)bottom, 0.0).func_181675_d();
        bufferbuilder.func_181662_b((double)right, (double)top, 0.0).func_181675_d();
        bufferbuilder.func_181662_b((double)left, (double)top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static Vec3d getEntityRenderPosition(Entity entity) {
        return new Vec3d(VisualUtils.getEntityRenderX(entity), VisualUtils.getEntityRenderY(entity), VisualUtils.getEntityRenderZ(entity));
    }

    public static double getEntityRenderX(Entity entity) {
        return entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)Minecraft.func_71410_x().field_71428_T.field_194147_b - VisualUtils.mc2.func_175598_ae().field_78725_b;
    }

    public static double getEntityRenderY(Entity entity) {
        return entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)Minecraft.func_71410_x().field_71428_T.field_194147_b - VisualUtils.mc2.func_175598_ae().field_78726_c;
    }

    public static double getEntityRenderZ(Entity entity) {
        return entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)Minecraft.func_71410_x().field_71428_T.field_194147_b - VisualUtils.mc2.func_175598_ae().field_78723_d;
    }

    public static int RedRainbow() {
        int RainbowColor;
        int c = RainbowColor = VisualUtils.rainbow(System.nanoTime(), ++counts, 0.5f).getRGB();
        Color col = new Color(c);
        return new Color((float)col.getRed() / 150.0f, 0.09803922f, 0.09803922f).getRGB();
    }

    public static void chamsColor(Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
    }

    public static int getNormalRainbow(int delay, float sat, float brg) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), sat, brg).getRGB();
    }

    public static void rectTexture(float x, float y, float w, float h, ResourceLocation texture, int color) {
        if (texture == null) {
            return;
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        x = Math.round(x);
        w = Math.round(w);
        y = Math.round(y);
        h = Math.round(h);
        float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
        float var6 = (float)(color >> 16 & 0xFF) / 255.0f;
        float var7 = (float)(color >> 8 & 0xFF) / 255.0f;
        float var8 = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)var6, (float)var7, (float)var8, (float)var11);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3553);
        mc2.func_110434_K().func_110577_a(texture);
        float tw = w / w / (w / w);
        float th = h / h / (h / h);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glTexCoord2f((float)0.0f, (float)th);
        GL11.glVertex2f((float)x, (float)(y + h));
        GL11.glTexCoord2f((float)tw, (float)th);
        GL11.glVertex2f((float)(x + w), (float)(y + h));
        GL11.glTexCoord2f((float)tw, (float)0.0f);
        GL11.glVertex2f((float)(x + w), (float)y);
        GL11.glEnd();
        GL11.glDisable((int)3553);
        GL11.glDisable((int)3042);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
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
        for (i = 0.0; i <= 90.0; i += 0.25) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 90.0; i <= 180.0; i += 0.25) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 180.0; i <= 270.0; i += 0.25) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 270.0; i <= 360.0; i += 0.25) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
    }

    public static void kaMark(Entity entity, Color color) {
        RenderManager renderManager = mc2.func_175598_ae();
        Timer timer = VisualUtils.mc2.field_71428_T;
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_194147_b - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_194147_b - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_194147_b - renderManager.field_78723_d;
        AxisAlignedBB axisAlignedBB = entity.func_174813_aQ().func_72317_d(-entity.field_70165_t, -entity.field_70163_u, -entity.field_70161_v).func_72317_d(x, y, z);
        VisualUtils.drawAxisAlignedBB(new AxisAlignedBB(axisAlignedBB.field_72340_a - 0.025, axisAlignedBB.field_72337_e - 0.35, axisAlignedBB.field_72339_c - 0.025, axisAlignedBB.field_72336_d + 0.025, axisAlignedBB.field_72337_e - 0.275, axisAlignedBB.field_72334_f + 0.025), color);
    }

    public static void MoondrawRect(double x, double y, double width, double height, int color) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        VisualUtils.MoondrawRect2(x, y, x + width, y + height, color);
    }

    public static void MoondrawRect2(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            int i = (int)left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            int j = (int)top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(left, bottom, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(right, bottom, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(right, top, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(left, top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawESPCircle(float cx, float cy, float r, float n, Color color) {
        GL11.glPushMatrix();
        cx = (float)((double)cx * 2.0);
        cy = (float)((double)cy * 2.0);
        double b = 6.2831852 / (double)n;
        double p = Math.cos(b);
        double s = Math.sin(b);
        r = (float)((double)r * 2.0);
        double x = r;
        double y = 0.0;
        VisualUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179117_G();
        VisualUtils.glColor(color);
        GL11.glBegin((int)2);
        int ii = 0;
        while ((float)ii < n) {
            GL11.glVertex2f((float)((float)x + cx), (float)((float)y + cy));
            double t2 = x;
            x = p * x - s * y;
            y = s * t2 + p * y;
            ++ii;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        VisualUtils.disableGL2D();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static boolean isHovering(int n, int n2, float n3, float n4, float n5, float n6) {
        return (float)n > n3 && (float)n < n5 && (float)n2 > n4 && (float)n2 < n6;
    }

    public static void drawblock2(double a, double a2, double a3, int a4, int a5, float a6) {
        float a7 = (float)(a4 >> 24 & 0xFF) / 255.0f;
        float a8 = (float)(a4 >> 16 & 0xFF) / 255.0f;
        float a9 = (float)(a4 >> 8 & 0xFF) / 255.0f;
        float a10 = (float)(a4 & 0xFF) / 255.0f;
        float a11 = (float)(a5 >> 24 & 0xFF) / 255.0f;
        float a12 = (float)(a5 >> 16 & 0xFF) / 255.0f;
        float a13 = (float)(a5 >> 8 & 0xFF) / 255.0f;
        float a14 = (float)(a5 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)a8, (float)a9, (float)a10, (float)a7);
        VisualUtils.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glLineWidth((float)a6);
        GL11.glColor4f((float)a12, (float)a13, (float)a14, (float)a11);
        VisualUtils.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawVLine(float x, float y, float x1, float y1, float width, int color) {
        if (width <= 0.0f) {
            return;
        }
        GL11.glPushMatrix();
        VisualUtils.pre3D();
        float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
        float var12 = (float)(color >> 16 & 0xFF) / 255.0f;
        float var13 = (float)(color >> 8 & 0xFF) / 255.0f;
        float var14 = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179141_d();
        GlStateManager.func_179147_l();
        int shade = GL11.glGetInteger((int)2900);
        GlStateManager.func_179103_j((int)7425);
        GL11.glColor4f((float)var12, (float)var13, (float)var14, (float)var11);
        float line = GL11.glGetFloat((int)2849);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)x, (double)y, (double)0.0);
        GL11.glVertex3d((double)x1, (double)y1, (double)0.0);
        GL11.glEnd();
        GlStateManager.func_179103_j((int)shade);
        GL11.glLineWidth((float)line);
        VisualUtils.post3D();
        GL11.glPopMatrix();
    }

    /*
     * Exception decompiling
     */
    public static void setupRender(boolean start) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl16 : ILOAD_0 - null : trying to set 0 previously set to 2
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

    public static double[] convertTo2D(double x, double y, double z) {
        double[] dArray;
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int)3);
        IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer((int)16);
        FloatBuffer projection = BufferUtils.createFloatBuffer((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenCoords);
        if (result) {
            double[] dArray2 = new double[3];
            dArray2[0] = screenCoords.get(0);
            dArray2[1] = (float)Display.getHeight() - screenCoords.get(1);
            dArray = dArray2;
            dArray2[2] = screenCoords.get(2);
        } else {
            dArray = null;
        }
        return dArray;
    }

    public static double interpolate(double newPos, double oldPos) {
        return oldPos + (newPos - oldPos) * (double)Minecraft.func_71410_x().field_71428_T.field_194147_b;
    }

    public static void pre() {
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        int n = 0;
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glShadeModel((int)7425);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)2896);
        GL11.glDepthMask((boolean)false);
        int n2 = 0;
    }

    public static void post3D() {
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void enableGL3D(float lineWidth) {
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
        GL11.glLineWidth((float)lineWidth);
    }

    public static void disableGL3D() {
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
    public static void drawHead(ResourceLocation skin, int width, int height) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl16 : INVOKESTATIC - null : Stack underflow
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

    public static void setColor(int colorHex) {
        float alpha = (float)(colorHex >> 24 & 0xFF) / 255.0f;
        float red = (float)(colorHex >> 16 & 0xFF) / 255.0f;
        float green = (float)(colorHex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(colorHex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)(alpha == 0.0f ? 1.0f : alpha));
    }

    public static void drawWolframEntityESP(EntityLivingBase entity, int rgb, double posX, double posY, double posZ) {
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        GL11.glRotatef((float)(-entity.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
        VisualUtils.setColor(rgb);
        VisualUtils.enableGL3D(1.0f);
        Cylinder c = new Cylinder();
        GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        c.setDrawStyle(100011);
        Cylinder cylinder = c;
        float f = 0.5f;
        float f2 = 0.5f;
        float f3 = entity.field_70131_O + 0.1f;
        int n = 0;
        VisualUtils.disableGL3D();
        GL11.glPopMatrix();
    }

    public static void drawTriangle(float x, float y, float x2, float y2, float x3, float y3, Color color) {
        GL11.glEnable((int)2848);
        VisualUtils.glColor(color);
        GL11.glBegin((int)4);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glVertex2f((float)x3, (float)y3);
        GL11.glEnd();
        GL11.glDisable((int)2848);
    }

    public static void glColor114514(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
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

    public static void drawTriAngle(float cx, float cy, float r, float n, int color) {
        GL11.glPushMatrix();
        cx = (float)((double)cx * 2.0);
        cy = (float)((double)cy * 2.0);
        double b = Math.PI * 2 / (double)n;
        double p = Math.cos(b);
        double s = Math.sin(b);
        double x = (double)r * 2.0;
        double y = 0.0;
        VisualUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179117_G();
        VisualUtils.glColor114514(color);
        GL11.glBegin((int)2);
        int ii = 0;
        while ((float)ii < n) {
            GL11.glVertex2d((double)(x + (double)cx), (double)(y + (double)cy));
            double t2 = x;
            x = p * x - s * y;
            y = s * t2 + p * y;
            ++ii;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        VisualUtils.disableGL2D();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static void triangle(float x1, float y1, float x2, float y2, float x3, float y3, int fill) {
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        float var11 = (float)(fill >> 24 & 0xFF) / 255.0f;
        float var6 = (float)(fill >> 16 & 0xFF) / 255.0f;
        float var7 = (float)(fill >> 8 & 0xFF) / 255.0f;
        float var8 = (float)(fill & 0xFF) / 255.0f;
        Tessellator var9 = Tessellator.func_178181_a();
        BufferBuilder var10 = var9.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)var6, (float)var7, (float)var8, (float)var11);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)6);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x3, (float)y3);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static int drawText(String text, int width, int height, float size) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a((float)size, (float)size, (float)size);
        int length = VisualUtils.mc2.field_71466_p.func_78276_b(text, Math.round((float)width / size), Math.round((float)height / size), -1);
        GlStateManager.func_179121_F();
        return length;
    }

    public static int getRGB(int r, int g, int b) {
        return VisualUtils.getRGB(r, g, b, 255);
    }

    public static int getRGB(int r, int g, int b, int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) << 0;
    }

    public static int getRGB(int rgb) {
        return 0xFF000000 | rgb;
    }

    public static int getRed(int rgb) {
        return rgb >> 16 & 0xFF;
    }

    public static int getGreen(int rgb) {
        return rgb >> 8 & 0xFF;
    }

    public static int getBlue(int rgb) {
        return rgb >> 0 & 0xFF;
    }

    public static void drawCheck(double x, double y, int lineWidth, int color) {
        VisualUtils.start2D();
        GL11.glPushMatrix();
        GL11.glLineWidth((float)lineWidth);
        VisualUtils.setColor(new Color(color));
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)(x + 2.0), (double)(y + 3.0));
        GL11.glVertex2d((double)(x + 6.0), (double)(y - 2.0));
        GL11.glEnd();
        GL11.glPopMatrix();
        VisualUtils.stop2D();
    }

    /*
     * Exception decompiling
     */
    public static void drawRect2(float x, float y, float x2, float y2, int color, boolean shadow) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl7 : ILOAD - null : trying to set 2 previously set to 0
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

    public static void start2D() {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
    }

    public static void stop2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void setColor(Color color) {
        float alpha = (float)(color.getRGB() >> 24 & 0xFF) / 255.0f;
        float red = (float)(color.getRGB() >> 16 & 0xFF) / 255.0f;
        float green = (float)(color.getRGB() >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color.getRGB() & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void drawArrow(double x, double y, int lineWidth, int color, double length) {
        VisualUtils.start2D();
        GL11.glPushMatrix();
        GL11.glLineWidth((float)lineWidth);
        VisualUtils.setColor(new Color(color));
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)(x + 3.0), (double)(y + length));
        GL11.glVertex2d((double)(x + 6.0), (double)y);
        GL11.glEnd();
        GL11.glPopMatrix();
        VisualUtils.stop2D();
    }

    public static void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179086_m((int)256);
        RenderHelper.func_74519_b();
        Minecraft.func_71410_x().func_175599_af().field_77023_b = -150.0f;
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179145_e();
        GlStateManager.func_179126_j();
        Minecraft.func_71410_x().func_175599_af().func_180450_b(stack, x, y);
        RenderItem renderItem = Minecraft.func_71410_x().func_175599_af();
        Minecraft.func_71410_x();
        renderItem.func_175030_a(VisualUtils.mc2.field_71466_p, stack, x, y);
        Minecraft.func_71410_x().func_175599_af().field_77023_b = 0.0f;
        RenderHelper.func_74518_a();
        GlStateManager.func_179129_p();
        GlStateManager.func_179141_d();
        GlStateManager.func_179084_k();
        GlStateManager.func_179140_f();
        double s = 0.5;
        GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.func_179097_i();
        GlStateManager.func_179126_j();
        GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.func_179121_F();
    }

    public static Color reAlpha(Color cIn, float alpha) {
        return new Color((float)cIn.getRed() / 255.0f, (float)cIn.getGreen() / 255.0f, (float)cIn.getBlue() / 255.0f, (float)cIn.getAlpha() / 255.0f * alpha);
    }

    public static void drawtargethudRect(double d, double e, double g, double h, int color, int i) {
        VisualUtils.drawRect(d + 1.0, e, g - 1.0, h, color);
        VisualUtils.drawRect(d, e + 1.0, d + 1.0, h - 1.0, color);
        VisualUtils.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        VisualUtils.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        VisualUtils.drawRect(g - 1.0, e + 1.0, g - 0.5, e + 0.5, color);
        VisualUtils.drawRect(g - 1.0, e + 1.0, g, h - 1.0, color);
        VisualUtils.drawRect(d + 1.0, h - 1.0, d + 0.5, h - 0.5, color);
        VisualUtils.drawRect(g - 1.0, h - 1.0, g - 0.5, h - 0.5, color);
    }

    public static void drawblock(double a, double a2, double a3, int a4, int a5, float a6) {
        float a7 = (float)(a4 >> 24 & 0xFF) / 255.0f;
        float a8 = (float)(a4 >> 16 & 0xFF) / 255.0f;
        float a9 = (float)(a4 >> 8 & 0xFF) / 255.0f;
        float a10 = (float)(a4 & 0xFF) / 255.0f;
        float a11 = (float)(a5 >> 24 & 0xFF) / 255.0f;
        float a12 = (float)(a5 >> 16 & 0xFF) / 255.0f;
        float a13 = (float)(a5 >> 8 & 0xFF) / 255.0f;
        float a14 = (float)(a5 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)a8, (float)a9, (float)a10, (float)a7);
        VisualUtils.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glLineWidth((float)a6);
        GL11.glColor4f((float)a12, (float)a13, (float)a14, (float)a11);
        VisualUtils.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    /*
     * Exception decompiling
     */
    public static void drawFace(int x, int y, float scale, AbstractClientPlayer target) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl25 : INVOKESTATIC - null : Stack underflow
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

    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(mc2);
        int factor = scale.func_78325_e();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)scale.func_78328_b() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }

    public static void drawBordered(double x2, double y2, double width, double height, double length, int innerColor, int outerColor) {
        VisualUtils.drawRect(x2, y2, x2 + width, y2 + height, innerColor);
        VisualUtils.drawRect(x2 - length, y2, x2, y2 + height, outerColor);
        VisualUtils.drawRect(x2 - length, y2 - length, x2 + width, y2, outerColor);
        VisualUtils.drawRect(x2 + width, y2 - length, x2 + width + length, y2 + height + length, outerColor);
        VisualUtils.drawRect(x2 - length, y2 + height, x2 + width, y2 + height + length, outerColor);
    }

    public static void drawRoundRect(float d, float e, float g, float h, int color) {
        VisualUtils.drawRect(d + 1.0f, e, g, h, color);
        VisualUtils.drawRect((double)d, (double)e + 0.75, (double)d, (double)h, color);
        VisualUtils.drawRect((double)d, (double)(e + 1.0f), (double)(d + 1.0f), (double)h - 0.5, color);
        VisualUtils.drawRect((double)d - 0.75, (double)e + 1.5, (double)d, (double)h - 1.25, color);
    }

    public static void autoExhibition(double x, double y, double x1, double y1, double size) {
        VisualUtils.rectangleBordered(x, y, x1 + size, y1 + size, 0.5, Colors.getColor(90), Colors.getColor(0));
        VisualUtils.rectangleBordered(x + 1.0, y + 1.0, x1 + size - 1.0, y1 + size - 1.0, 1.0, Colors.getColor(90), Colors.getColor(61));
        VisualUtils.rectangleBordered(x + 2.5, y + 2.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, Colors.getColor(61), Colors.getColor(0));
    }

    public static void drawCheckMark(float x, float y, int width, int color) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        int n2 = 0;
        GL11.glLineWidth((float)1.5f);
        GL11.glBegin((int)3);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)((double)(x + (float)width) - 6.5), (double)(y + 3.0f));
        GL11.glVertex2d((double)((double)(x + (float)width) - 11.5), (double)(y + 10.0f));
        GL11.glVertex2d((double)((double)(x + (float)width) - 13.5), (double)(y + 8.0f));
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawRoundedRect(int left, int top, int right, int bottom, int smooth, Color color) {
        Gui.func_73734_a((int)(left + smooth), (int)top, (int)(right - smooth), (int)bottom, (int)color.getRGB());
        Gui.func_73734_a((int)left, (int)(top + smooth), (int)right, (int)(bottom - smooth), (int)color.getRGB());
        VisualUtils.drawFilledCircle(left + smooth, top + smooth, (float)smooth, color);
        VisualUtils.drawFilledCircle(right - smooth, top + smooth, (float)smooth, color);
        VisualUtils.drawFilledCircle(right - smooth, bottom - smooth, (float)smooth, color);
        VisualUtils.drawFilledCircle(left + smooth, bottom - smooth, (float)smooth, color);
    }

    public static void drawLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        minX -= Minecraft.func_71410_x().func_175598_ae().field_78725_b;
        minY -= Minecraft.func_71410_x().func_175598_ae().field_78726_c;
        minZ -= Minecraft.func_71410_x().func_175598_ae().field_78723_d;
        maxX -= Minecraft.func_71410_x().func_175598_ae().field_78725_b;
        maxY -= Minecraft.func_71410_x().func_175598_ae().field_78726_c;
        maxZ -= Minecraft.func_71410_x().func_175598_ae().field_78723_d;
        int n = 0;
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)3.0f);
        GL11.glColor4d((double)0.0, (double)1.0, (double)0.0, (double)0.15f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)0.5);
        bufferbuilder.func_181668_a(1, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(minX, minY, minZ).func_181675_d();
        bufferbuilder.func_181662_b(maxX, maxY, maxZ).func_181675_d();
        tessellator.func_78381_a();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawTracerLine(double x, double y, double z, float red, float green, float blue, float alpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)lineWdith);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)2);
        GL11.glVertex3d((double)0.0, (double)(0.0 + (double)Minecraft.func_71410_x().field_71439_g.func_70047_e()), (double)0.0);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static boolean ifdrawRect(int x1, int y1, float x2, int y2, int mousex, int mousey, int color) {
        if (mousex > x1 && (float)mousex < x2 && mousey > y1 && mousey < y2) {
            GL11.glPushMatrix();
            GlStateManager.func_179147_l();
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            int n = 0;
            GL11.glEnable((int)2848);
            GL11.glPushMatrix();
            VisualUtils.color(color);
            GL11.glBegin((int)7);
            GL11.glVertex2d((double)x2, (double)y1);
            GL11.glVertex2d((double)x1, (double)y1);
            GL11.glVertex2d((double)x1, (double)y2);
            GL11.glVertex2d((double)x2, (double)y2);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2848);
            GlStateManager.func_179084_k();
            GL11.glPopMatrix();
            return true;
        }
        return false;
    }

    public static void stopDrawing() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
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

    public static void post() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glColor3d((double)1.0, (double)1.0, (double)1.0);
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return VisualUtils.isInViewFrustrum(entity.func_174813_aQ()) || entity.field_70158_ak;
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.func_71410_x().func_175606_aa();
        frustrum.func_78547_a(current.field_70165_t, current.field_70163_u, current.field_70161_v);
        return frustrum.func_78546_a(bb);
    }

    public static void doGlScissor(int x, int y, int width, int height2) {
        int scaleFactor = 1;
        int k = VisualUtils.mc2.field_71474_y.field_74335_Z;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && VisualUtils.mc2.field_71443_c / (scaleFactor + 1) >= 320 && VisualUtils.mc2.field_71440_d / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor((int)(x * scaleFactor), (int)(VisualUtils.mc2.field_71440_d - (y + height2) * scaleFactor), (int)(width * scaleFactor), (int)(height2 * scaleFactor));
    }

    public static void doGlScissor(float x, float y, float windowWidth2, float windowHeight2) {
        int scaleFactor = 1;
        float k = VisualUtils.mc2.field_71474_y.field_74335_Z;
        if (k == 0.0f) {
            k = 1000.0f;
        }
        while ((float)scaleFactor < k && VisualUtils.mc2.field_71443_c / (scaleFactor + 1) >= 320 && VisualUtils.mc2.field_71440_d / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor((int)((int)(x * (float)scaleFactor)), (int)((int)((float)VisualUtils.mc2.field_71440_d - (y + windowHeight2) * (float)scaleFactor)), (int)((int)(windowWidth2 * (float)scaleFactor)), (int)((int)(windowHeight2 * (float)scaleFactor)));
    }

    public static void customRounded(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float rTL, float rTR, float rBR, float rBL, int color) {
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

    public static Color arrayRainbow(int offset) {
        return new Color(Color.HSBtoRGB((float)((double)((float)(System.currentTimeMillis() - startTime) / 10000.0f) + Math.sin((double)((float)(System.currentTimeMillis() - startTime) / 100.0f % 50.0f) + (double)offset) / 50.0 * 1.6) % 1.0f, 0.5f, 1.0f));
    }

    public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        Gui.func_152125_a((int)x, (int)y, (float)u, (float)v, (int)uWidth, (int)vHeight, (int)width, (int)height, (float)tileWidth, (float)tileHeight);
    }

    public static void drawRect1(float g, float h, float i, float j, int col1) {
        float f2 = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f22 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f22, (float)f3, (float)f4, (float)f2);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)i, (double)h);
        GL11.glVertex2d((double)g, (double)h);
        GL11.glVertex2d((double)g, (double)j);
        GL11.glVertex2d((double)i, (double)j);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void circle1(float x, float y, float radius, int fill) {
        VisualUtils.arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void circle1(float x, float y, float radius, Color fill) {
        VisualUtils.arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void arc1(float x, float y, float start, float end, float radius, int color) {
        VisualUtils.arcEllipse1(x, y, start, end, radius, radius, color);
    }

    public static void arcEllipse1(float x, float y, float start, float end, float w, float h, int color) {
        float ldy;
        float ldx;
        float i;
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        if (start > end) {
            float temp = end;
            end = start;
            start = temp;
        }
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        if (alpha > 0.5f) {
            GL11.glEnable((int)2881);
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)2.0f);
            GL11.glBegin((int)3);
            for (i = end; i >= start; i -= 4.0f) {
                ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w * 1.001f;
                ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
            GL11.glDisable((int)2881);
        }
        GL11.glBegin((int)6);
        for (i = end; i >= start; i -= 4.0f) {
            ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w;
            ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h;
            GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
        }
        GL11.glEnd();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    /*
     * Exception decompiling
     */
    public static void drawFilledCircle1(float xx, float yy, float radius, int col) {
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
     * Exception decompiling
     */
    public static void drawFilledCircle1(int xx, int yy, float radius, Color color) {
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

    public static void outlineRect(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        VisualUtils.drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.drawRect(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.drawRect(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.drawRect(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
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
        RenderUtils.mc2.func_110434_K().func_110577_a(new ResourceLocation("likingsense/shadow/" + image2 + ".png"));
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        if (!enableBlend) {
            GL11.glDisable((int)3042);
        }
        if (!disableAlpha) {
            GL11.glEnable((int)3008);
        }
        GL11.glPopMatrix();
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)1.0f);
        VisualUtils.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        VisualUtils.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawEntityESP(double x, double y, double z, double x1, double y1, double z1, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)1.0f);
        VisualUtils.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x1, y1, z1));
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        VisualUtils.drawBoundingBox(new AxisAlignedBB(x, y, z, x1, y1, z1));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawEntityESP(AxisAlignedBB axisAlignedBB, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)1.0f);
        VisualUtils.drawOutlinedBoundingBox(axisAlignedBB);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        VisualUtils.drawBoundingBox(axisAlignedBB);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        float f = 1.0f / textureWidth;
        float f2 = 1.0f / textureHeight;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        bufferbuilder.func_181662_b((double)x, (double)(y + height), 0.0).func_187315_a((double)(u * f), (double)((v + height) * f2)).func_181675_d();
        bufferbuilder.func_181662_b((double)(x + width), (double)(y + height), 0.0).func_187315_a((double)((u + width) * f), (double)((v + height) * f2)).func_181675_d();
        bufferbuilder.func_181662_b((double)(x + width), (double)y, 0.0).func_187315_a((double)((u + width) * f), (double)(v * f2)).func_181675_d();
        bufferbuilder.func_181662_b((double)x, (double)y, 0.0).func_187315_a((double)(u * f), (double)(v * f2)).func_181675_d();
        tessellator.func_78381_a();
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public static void drawShadowWithCustomAlpha(float x, float y, float width, float height, float alpha) {
        VisualUtils.drawTexturedRectWithCustomAlpha(x - 9.0f, y - 9.0f, 9.0f, 9.0f, "paneltopleft", alpha);
        VisualUtils.drawTexturedRectWithCustomAlpha(x - 9.0f, y + height, 9.0f, 9.0f, "panelbottomleft", alpha);
        VisualUtils.drawTexturedRectWithCustomAlpha(x + width, y + height, 9.0f, 9.0f, "panelbottomright", alpha);
        VisualUtils.drawTexturedRectWithCustomAlpha(x + width, y - 9.0f, 9.0f, 9.0f, "paneltopright", alpha);
        VisualUtils.drawTexturedRectWithCustomAlpha(x - 9.0f, y, 9.0f, height, "panelleft", alpha);
        VisualUtils.drawTexturedRectWithCustomAlpha(x + width, y, 9.0f, height, "panelright", alpha);
        VisualUtils.drawTexturedRectWithCustomAlpha(x, y - 9.0f, width, 9.0f, "paneltop", alpha);
        VisualUtils.drawTexturedRectWithCustomAlpha(x, y + height, width, 9.0f, "panelbottom", alpha);
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
        mc2.func_110434_K().func_110577_a(new ResourceLocation("likingsense/shadow/" + image2 + ".png"));
        VisualUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        if (!enableBlend) {
            GL11.glDisable((int)3042);
        }
        if (!disableAlpha) {
            GL11.glEnable((int)3008);
        }
        GlStateManager.func_179117_G();
        GL11.glPopMatrix();
    }

    public static void drawShadow(float x, float y, float width, float height) {
        VisualUtils.drawTexturedRect(x - 9.0f, y - 9.0f, 9.0f, 9.0f, "paneltopleft");
        VisualUtils.drawTexturedRect(x - 9.0f, y + height, 9.0f, 9.0f, "panelbottomleft");
        VisualUtils.drawTexturedRect(x + width, y + height, 9.0f, 9.0f, "panelbottomright");
        VisualUtils.drawTexturedRect(x + width, y - 9.0f, 9.0f, 9.0f, "paneltopright");
        VisualUtils.drawTexturedRect(x - 9.0f, y, 9.0f, height, "panelleft");
        VisualUtils.drawTexturedRect(x + width, y, 9.0f, height, "panelright");
        VisualUtils.drawTexturedRect(x, y - 9.0f, width, 9.0f, "paneltop");
        VisualUtils.drawTexturedRect(x, y + height, width, 9.0f, "panelbottom");
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(1, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void glColor(Color color, int alpha) {
        VisualUtils.glColor(color, (float)alpha / 255.0f);
    }

    public static void draw2D(EntityLivingBase entity, double posX, double posY, double posZ, int color, int backgroundColor) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)posZ);
        GlStateManager.func_179114_b((float)(-VisualUtils.mc2.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)-0.1, (double)-0.1, (double)0.1);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GlStateManager.func_179132_a((boolean)true);
        VisualUtils.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[0]);
        VisualUtils.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[1]);
        GlStateManager.func_179137_b((double)0.0, (double)(21.0 + -(entity.func_174813_aQ().field_72337_e - entity.func_174813_aQ().field_72338_b) * 12.0), (double)0.0);
        VisualUtils.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[2]);
        VisualUtils.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[3]);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GlStateManager.func_179121_F();
    }

    public static void draw2D(BlockPos blockPos, int color, int backgroundColor) {
        RenderManager renderManager = mc2.func_175598_ae();
        double posX = (double)blockPos.func_177958_n() + 0.5 - renderManager.field_78725_b;
        double posY = (double)blockPos.func_177956_o() - renderManager.field_78726_c;
        double posZ = (double)blockPos.func_177952_p() + 0.5 - renderManager.field_78723_d;
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)posZ);
        GlStateManager.func_179114_b((float)(-VisualUtils.mc2.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)-0.1, (double)-0.1, (double)0.1);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GlStateManager.func_179132_a((boolean)true);
        VisualUtils.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[0]);
        VisualUtils.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[1]);
        GlStateManager.func_179109_b((float)0.0f, (float)9.0f, (float)0.0f);
        VisualUtils.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[2]);
        VisualUtils.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[3]);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GlStateManager.func_179121_F();
    }

    public static void clearCaps() {
        glCapMap.clear();
    }

    public static void drawBorder(float x, float y, float x2, float y2, float width, int color1) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        VisualUtils.glColor(color1);
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

    public static void drawOutLineRect(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        VisualUtils.drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.drawRect(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.drawRect(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.drawRect(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    /*
     * Exception decompiling
     */
    public static ByteBuffer readImageToBuffer(BufferedImage bufferedImage) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl10 : INVOKEVIRTUAL - null : Stack underflow
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
    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl16 : INVOKEVIRTUAL - null : Stack underflow
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

    public static void quickDrawRect(float x, float y, float x2, float y2, Color color) {
        VisualUtils.quickDrawRect(x, y, x2, y2, color.getRGB());
    }

    public static void drawRect(float x, float y, float x2, float y2, Color color) {
        VisualUtils.drawRect(x, y, x2, y2, color.getRGB());
    }

    public static void drawEntityBox(Entity entity, Color color, boolean outline, boolean box, float outlineWidth) {
        RenderManager renderManager = mc2.func_175598_ae();
        Timer timer = VisualUtils.mc2.field_71428_T;
        int n = 0;
        VisualUtils.enableGlCap(3042);
        int[] nArray = new int[2];
        int[] nArray2 = nArray;
        int[] nArray3 = nArray;
        int n2 = 0;
        int n3 = 0;
        VisualUtils.disableGlCap((int[])0);
        GL11.glDepthMask((boolean)false);
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_194147_b - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_194147_b - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_194147_b - renderManager.field_78723_d;
        AxisAlignedBB entityBox = entity.func_174813_aQ();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.field_72340_a - entity.field_70165_t + x - 0.05, entityBox.field_72338_b - entity.field_70163_u + y, entityBox.field_72339_c - entity.field_70161_v + z - 0.05, entityBox.field_72336_d - entity.field_70165_t + x + 0.05, entityBox.field_72337_e - entity.field_70163_u + y + 0.15, entityBox.field_72334_f - entity.field_70161_v + z + 0.05);
        if (outline) {
            GL11.glLineWidth((float)outlineWidth);
            VisualUtils.enableGlCap(2848);
            VisualUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), box ? 170 : 255);
            VisualUtils.drawSelectionBoundingBox(axisAlignedBB);
        }
        if (box) {
            VisualUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
            VisualUtils.drawFilledBox(axisAlignedBB);
        }
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        VisualUtils.resetCaps();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawRoundedCornerRect(float x, float y, float x1, float y1, float radius, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        VisualUtils.glColor(color);
        VisualUtils.drawRoundedCornerRect(x, y, x1, y1, radius);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
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

    public static void drawGradientSidewaysH(double left, double top, double right, double bottom, int col1, int col2) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        VisualUtils.quickDrawGradientSidewaysH(left, top, right, bottom, col1, col2);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void quickDrawGradientSidewaysH(double left, double top, double right, double bottom, int col1, int col2) {
        GL11.glBegin((int)7);
        VisualUtils.glColor(col1);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        VisualUtils.glColor(col2);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
    }

    public static void quickDrawGradientSidewaysV(double left, double top, double right, double bottom, int col1, int col2) {
        GL11.glBegin((int)7);
        VisualUtils.glColor(col1);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glVertex2d((double)left, (double)top);
        VisualUtils.glColor(col2);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glEnd();
    }

    public static void renderCircle(double x, double y, double radius, int color) {
        VisualUtils.renderCircle(x, y, 0.0, 360.0, radius - 1.0, color);
    }

    public static void renderCircle(double x, double y, double start, double end, double radius, int color) {
        VisualUtils.renderCircle(x, y, start, end, radius, radius, color);
    }

    public static void renderCircle(double x, double y, double start, double end, double w, double h, int color) {
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        VisualUtils.glColor(color);
        VisualUtils.quickRenderCircle(x, y, start, end, w, h);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void connectPoints(float xOne, float yOne, float xTwo, float yTwo) {
        GL11.glPushMatrix();
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.8f);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)0.5f);
        GL11.glBegin((int)1);
        GL11.glVertex2f((float)xOne, (float)yOne);
        GL11.glVertex2f((float)xTwo, (float)yTwo);
        GL11.glEnd();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    /*
     * Exception decompiling
     */
    public static void drawFilledCircle2(float xx, float yy, float radius, Color color) {
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

    public static Color skyRainbow(int var2, float st, float bright) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright);
    }

    public static Color skyRainbow(int var2, float bright, float st, double speed) {
        double d;
        double v1 = Math.ceil((double)System.currentTimeMillis() / speed + (double)((long)var2 * 109L)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright);
    }

    private static void quickPolygonCircle(float x, float y, float xRadius, float yRadius, int start, int end, int split) {
        for (int i = end; i >= start; i -= split) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * Math.PI / 180.0) * (double)xRadius), (double)((double)y + Math.cos((double)i * Math.PI / 180.0) * (double)yRadius));
        }
    }

    /*
     * Exception decompiling
     */
    public static void drawCircleRect(float x, float y, float x1, float y1, float radius, int color) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl48 : INVOKESTATIC - null : Stack underflow
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

    /*
     * Exception decompiling
     */
    public static void drawFilledCircle2(int xx, int yy, float radius, Color color) {
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

    public static int Astolfo(int var2) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static void drawNoFullCircle(float x, float y, float radius, int fill) {
        VisualUtils.arc233(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void arc233(float x, float y, float start, float end, float radius, int color) {
        VisualUtils.arcEllipse233(x, y, start, end, radius, radius, color);
    }

    public static void arcEllipse233(float x, float y, float start, float end, float w, float h, int color) {
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        if (start > end) {
            float temp = end;
            end = start;
            start = temp;
        }
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        if (alpha > 0.5f) {
            GL11.glEnable((int)2881);
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)1.5f);
            GL11.glBegin((int)3);
            for (float i = end; i >= start; i -= 4.0f) {
                float ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w * 1.001f;
                float ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
            GL11.glDisable((int)2881);
        }
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawOutlinedString(String str, int x, int y, int color, int color2) {
        Minecraft mc = Minecraft.func_71410_x();
        VisualUtils.mc2.field_71466_p.func_78276_b(str, (int)((float)x - 1.0f), y, color2);
        VisualUtils.mc2.field_71466_p.func_78276_b(str, (int)((float)x + 1.0f), y, color2);
        VisualUtils.mc2.field_71466_p.func_78276_b(str, x, (int)((float)y + 1.0f), color2);
        VisualUtils.mc2.field_71466_p.func_78276_b(str, x, (int)((float)y - 1.0f), color2);
        VisualUtils.mc2.field_71466_p.func_78276_b(str, x, y, color);
    }

    public static void HanaBidrawRect(float x1, float y1, float x2, float y2, int color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        VisualUtils.color(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y1);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glVertex2d((double)x1, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
        int n2 = 0;
    }

    public static void color(int color) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
    }

    /*
     * Exception decompiling
     */
    public static void quickDrawHead(ResourceLocation skin, int x, int y, int width, int height) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl13 : INVOKESTATIC - null : Stack underflow
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

    public static void drawRect2(double x, double y, double x2, double y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        VisualUtils.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawGradientSidewaysV(double left, double top, double right, double bottom, int col1, int col2) {
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
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
        int n2 = 0;
    }

    public static void enableDepth() {
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
    }

    public static void disableDepth() {
        GL11.glDepthMask((boolean)false);
        GL11.glDisable((int)2929);
    }

    public static void enableBlending() {
        GL11.glEnable((int)3042);
        int n = 0;
    }

    public static void drawEntityBox(Entity entity, Color color, boolean outline) {
        RenderManager renderManager = mc2.func_175598_ae();
        Timer timer = VisualUtils.mc2.field_71428_T;
        int n = 0;
        VisualUtils.enableGlCap(3042);
        int[] nArray = new int[2];
        int[] nArray2 = nArray;
        int[] nArray3 = nArray;
        int n2 = 0;
        int n3 = 0;
        VisualUtils.disableGlCap((int[])0);
        GL11.glDepthMask((boolean)false);
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_194147_b - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_194147_b - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_194147_b - renderManager.field_78723_d;
        AxisAlignedBB entityBox = entity.func_174813_aQ();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.field_72340_a - entity.field_70165_t + x - 0.05, entityBox.field_72338_b - entity.field_70163_u + y, entityBox.field_72339_c - entity.field_70161_v + z - 0.05, entityBox.field_72336_d - entity.field_70165_t + x + 0.05, entityBox.field_72337_e - entity.field_70163_u + y + 0.15, entityBox.field_72334_f - entity.field_70161_v + z + 0.05);
        if (outline) {
            GL11.glLineWidth((float)1.0f);
            VisualUtils.enableGlCap(2848);
            VisualUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), 95);
            VisualUtils.drawSelectionBoundingBox(axisAlignedBB);
        }
        VisualUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
        VisualUtils.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        VisualUtils.resetCaps();
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, EntityLivingBase entity) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179142_g();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)50.0);
        GlStateManager.func_179152_a((float)(-scale), (float)scale, (float)scale);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179137_b((double)0.0, (double)0.0, (double)0.0);
        float renderYawOffset = entity.field_70761_aq;
        float rotationYaw = entity.field_70177_z;
        float rotationPitch = entity.field_70125_A;
        float prevRotationYawHead = entity.field_70758_at;
        float rotationYawHead = entity.field_70759_as;
        entity.field_70761_aq = 0.0f;
        entity.field_70177_z = 0.0f;
        entity.field_70125_A = 90.0f;
        entity.field_70759_as = entity.field_70177_z;
        entity.field_70758_at = entity.field_70177_z;
        RenderManager rendermanager = mc2.func_175598_ae();
        rendermanager.func_178631_a(180.0f);
        rendermanager.func_178633_a(false);
        rendermanager.func_178633_a(true);
        entity.field_70761_aq = renderYawOffset;
        entity.field_70177_z = rotationYaw;
        entity.field_70125_A = rotationPitch;
        entity.field_70758_at = prevRotationYawHead;
        entity.field_70759_as = rotationYawHead;
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
    }

    public static void drawEntityBox2(Entity entity, Color color, boolean outline, boolean box, float outlineWidth) {
        RenderManager renderManager = mc2.func_175598_ae();
        Timer timer = VisualUtils.mc2.field_71428_T;
        int n = 0;
        VisualUtils.enableGlCap(3042);
        int[] nArray = new int[2];
        int[] nArray2 = nArray;
        int[] nArray3 = nArray;
        int n2 = 0;
        int n3 = 0;
        VisualUtils.disableGlCap((int[])0);
        GL11.glDepthMask((boolean)false);
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_194147_b - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_194147_b - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_194147_b - renderManager.field_78723_d;
        AxisAlignedBB entityBox = entity.func_174813_aQ();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.field_72340_a - entity.field_70165_t + x - 0.05, entityBox.field_72338_b - entity.field_70163_u + y, entityBox.field_72339_c - entity.field_70161_v + z - 0.05, entityBox.field_72336_d - entity.field_70165_t + x + 0.05, entityBox.field_72337_e - entity.field_70163_u + y + 0.15, entityBox.field_72334_f - entity.field_70161_v + z + 0.05);
        if (outline) {
            GL11.glLineWidth((float)outlineWidth);
            VisualUtils.enableGlCap(2848);
            VisualUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), box ? 170 : 255);
            VisualUtils.drawSelectionBoundingBox(axisAlignedBB);
        }
        if (box) {
            VisualUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
            VisualUtils.drawFilledBox(axisAlignedBB);
        }
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        VisualUtils.resetCaps();
    }

    public static void drawCircle(float x, float y, float radius, Color color) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        VisualUtils.glColor(Color.WHITE);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)3);
        for (float i = 180.0f; i >= -180.0f; i -= 4.0f) {
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glVertex2f((float)((float)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))), (float)((float)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawCircleD(float x, float y, float radius, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean line = GL11.glIsEnabled((int)2848);
        boolean texture = GL11.glIsEnabled((int)3553);
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        if (!line) {
            GL11.glEnable((int)2848);
        }
        if (texture) {
            GL11.glDisable((int)3553);
        }
        int n = 0;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
        }
        GL11.glEnd();
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (!line) {
            GL11.glDisable((int)2848);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
    }

    public static void drawCircleFull(float x, float y, float radius, float Bord, Color color) {
        VisualUtils.drawCircle(x, y, radius + 0.15f, color);
        VisualUtils.drawCircleD(x, y, radius, color.getRGB());
    }

    public static void drawAxisAlignedBB(AxisAlignedBB axisAlignedBB, Color color, boolean outline, boolean box, float outlineWidth) {
        int n = 0;
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)outlineWidth);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        VisualUtils.glColor(color);
        if (outline) {
            GL11.glLineWidth((float)outlineWidth);
            VisualUtils.enableGlCap(2848);
            VisualUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), 95);
            VisualUtils.drawSelectionBoundingBox(axisAlignedBB);
        }
        if (box) {
            VisualUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
            VisualUtils.drawFilledBox(axisAlignedBB);
        }
        GlStateManager.func_179117_G();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawAxisAlignedBB(AxisAlignedBB axisAlignedBB, Color color) {
        int n = 0;
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        VisualUtils.glColor(color);
        VisualUtils.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawPlatform(double y, Color color, double size) {
        RenderManager renderManager = mc2.func_175598_ae();
        double renderY = y - renderManager.field_78726_c;
        VisualUtils.drawAxisAlignedBB(new AxisAlignedBB(size, renderY + 0.02, size, -size, renderY, -size), color);
    }

    public static void drawPlatform(Entity entity, Color color) {
        RenderManager renderManager = mc2.func_175598_ae();
        Timer timer = VisualUtils.mc2.field_71428_T;
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_194147_b - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_194147_b - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_194147_b - renderManager.field_78723_d;
        AxisAlignedBB axisAlignedBB = entity.func_174813_aQ().func_72317_d(-entity.field_70165_t, -entity.field_70163_u, -entity.field_70161_v).func_72317_d(x, y, z);
        VisualUtils.drawAxisAlignedBB(new AxisAlignedBB(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e + 0.2, axisAlignedBB.field_72339_c, axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e + 0.26, axisAlignedBB.field_72334_f), color);
    }

    public static void drawFilledBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        bufferbuilder.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawRectBlur(float x, float y, float x2, float y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        VisualUtils.glColor(color);
        VisualUtils.quickDrawRect(x, y, x2, y2);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawBorderedRect2(float x, float y, float x2, float y2, float width, int color1, int color2) {
        VisualUtils.drawRectBlur(x, y, x2, y2, color2);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        VisualUtils.glColor(color1);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawLoadingCircle(float x, float y) {
        for (int i = 0; i < 4; ++i) {
            int rot = (int)(System.nanoTime() / 5000000L * (long)i % 360L);
            VisualUtils.drawCircle(x, y, i * 10, rot - 180, rot);
        }
    }

    public static void drawCircle(float x, float y, float radius, int start, int end) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        VisualUtils.glColor(Color.WHITE);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        for (float i = (float)end; i >= (float)start; i -= 4.0f) {
            GL11.glVertex2f((float)((float)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))), (float)((float)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
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
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        bufferbuilder.func_181668_a(9, DefaultVertexFormats.field_181705_e);
        double degree = Math.PI / 180;
        for (i = 0.0; i <= 90.0; i += 1.0) {
            bufferbuilder.func_181662_b(x2 + Math.sin(i * degree) * (double)radius, y2 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        for (i = 90.0; i <= 180.0; i += 1.0) {
            bufferbuilder.func_181662_b(x2 + Math.sin(i * degree) * (double)radius, y1 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        for (i = 180.0; i <= 270.0; i += 1.0) {
            bufferbuilder.func_181662_b(x1 + Math.sin(i * degree) * (double)radius, y1 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        for (i = 270.0; i <= 360.0; i += 1.0) {
            bufferbuilder.func_181662_b(x1 + Math.sin(i * degree) * (double)radius, y2 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void glColor(int red, int green, int blue, int alpha) {
        GlStateManager.func_179131_c((float)((float)red / 255.0f), (float)((float)green / 255.0f), (float)((float)blue / 255.0f), (float)((float)alpha / 255.0f));
    }

    public static void glColor(Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void glColor(int hex, int alpha) {
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)((float)alpha / 255.0f));
    }

    public static void glColor(int hex, float alpha) {
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void glColor(Color color, float alpha) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void renderNameTag(String string, double x, double y, double z) {
        RenderManager renderManager = mc2.func_175598_ae();
        GL11.glPushMatrix();
        GL11.glTranslated((double)(x - renderManager.field_78725_b), (double)(y - renderManager.field_78726_c), (double)(z - renderManager.field_78723_d));
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-VisualUtils.mc2.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)VisualUtils.mc2.func_175598_ae().field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glScalef((float)-0.05f, (float)-0.05f, (float)0.05f);
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int width = Fonts.font35.getStringWidth(string) / 2;
        Fonts.font35.drawString(string, -width, 1.5f, Color.WHITE.getRGB(), true);
        VisualUtils.resetCaps();
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
        ScaledResolution scaledResolution = new ScaledResolution(mc2);
        int factor = scaledResolution.func_78325_e();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)scaledResolution.func_78328_b() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }

    public static void resetCaps() {
        glCapMap.forEach(VisualUtils::setGlState);
    }

    public static void enableGlCap(int cap) {
        VisualUtils.setGlCap(cap, true);
    }

    public static void enableGlCap(int ... caps) {
        for (int cap : caps) {
            VisualUtils.setGlCap(cap, true);
        }
    }

    public static void disableGlCap(int cap) {
        VisualUtils.setGlCap(cap, true);
    }

    public static void disableGlCap(int ... caps) {
        for (int cap : caps) {
            VisualUtils.setGlCap(cap, false);
        }
    }

    public static void setGlCap(int cap, boolean state) {
        glCapMap.put(cap, GL11.glGetBoolean((int)cap));
        VisualUtils.setGlState(cap, state);
    }

    public static void setGlState(int cap, boolean state) {
        if (state) {
            GL11.glEnable((int)cap);
        } else {
            GL11.glDisable((int)cap);
        }
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

    public static double getAnimationState2(double animation, double finalState, double speed) {
        float add = (float)(0.01 * speed);
        animation = animation < finalState ? (animation + (double)add < finalState ? (animation += (double)add) : finalState) : (animation - (double)add > finalState ? (animation -= (double)add) : finalState);
        return animation;
    }

    public static void drawScaledCustomSizeModalCircle(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0f / tileWidth;
        float f1 = 1.0f / tileHeight;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(9, DefaultVertexFormats.field_181707_g);
        float xRadius = (float)width / 2.0f;
        float yRadius = (float)height / 2.0f;
        float uRadius = ((u + (float)uWidth) * f - u * f) / 2.0f;
        float vRadius = ((v + (float)vHeight) * f1 - v * f1) / 2.0f;
        for (int i = 0; i <= 360; i += 10) {
            double xPosOffset = Math.sin((double)i * Math.PI / 180.0);
            double yPosOffset = Math.cos((double)i * Math.PI / 180.0);
            bufferbuilder.func_181662_b((double)((float)x + xRadius) + xPosOffset * (double)xRadius, (double)((float)y + yRadius) + yPosOffset * (double)yRadius, 0.0).func_187315_a((double)(u * f + uRadius) + xPosOffset * (double)uRadius, (double)(v * f1 + vRadius) + yPosOffset * (double)vRadius).func_181675_d();
        }
        tessellator.func_78381_a();
    }

    /*
     * Exception decompiling
     */
    public static void drawLimitedCircle(float lx, float ly, float x2, float y2, int xx, int yy, float radius, Color color) {
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

    /*
     * Exception decompiling
     */
    public static void targetHudRect(double x, double y, double x1, double y1, double size) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl22 : INVOKESTATIC - null : Stack underflow
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
    public static void targetHudRect1(double x, double y, double x1, double y1, double size) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl28 : INVOKESTATIC - null : Stack underflow
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

    public static void f(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        VisualUtils.drawRect(x, y, x2, y2, col2);
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glLineWidth((float)l1);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static int getRainbow(int speed, int offset, float s) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor(hue /= (float)speed, s, 1.0f).getRGB();
    }

    public static void drawRoundedRectangle(double left, double top, double right, double bottom, double radius, int color) {
        int i;
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        left *= 2.0;
        top *= 2.0;
        right *= 2.0;
        bottom *= 2.0;
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        VisualUtils.glColor(color);
        GL11.glBegin((int)9);
        for (i = 0; i <= 90; ++i) {
            GL11.glVertex2d((double)(left + radius + Math.sin((double)i * Math.PI / 180.0) * radius * -1.0), (double)(top + radius + Math.cos((double)i * Math.PI / 180.0) * radius * -1.0));
        }
        while (i <= 180) {
            GL11.glVertex2d((double)(left + radius + Math.sin((double)i * Math.PI / 180.0) * radius * -1.0), (double)(bottom - radius + Math.cos((double)i * Math.PI / 180.0) * radius * -1.0));
            ++i;
        }
        for (i = 0; i <= 90; ++i) {
            GL11.glVertex2d((double)(right - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(bottom - radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        while (i <= 180) {
            GL11.glVertex2d((double)(right - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(top + radius + Math.cos((double)i * Math.PI / 180.0) * radius));
            ++i;
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
    }

    public static void drawRectAlpha(float g, float h, float i, float j, int col1, float alpha) {
        float f2 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)alpha);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)i, (double)h);
        GL11.glVertex2d((double)g, (double)h);
        GL11.glVertex2d((double)g, (double)j);
        GL11.glVertex2d((double)i, (double)j);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    /*
     * Exception decompiling
     */
    public static void outlineOne() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl26 : INVOKESTATIC - null : Stack underflow
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

    public static Color transparency(int color, double alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, (float)alpha);
    }

    /*
     * Exception decompiling
     */
    public static void outlineTwo() {
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

    /*
     * Exception decompiling
     */
    public static void outlineThree() {
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

    public static void outlineFour() {
        GL11.glEnable((int)10754);
        GL11.glPolygonOffset((float)1.0f, (float)-2000000.0f);
        OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)240.0f, (float)240.0f);
    }

    public static void outlineFive() {
        GL11.glPolygonOffset((float)1.0f, (float)2000000.0f);
        GL11.glDisable((int)10754);
        GL11.glDisable((int)2960);
        GL11.glDisable((int)2848);
        int n = 0;
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)3008);
        GL11.glPopAttrib();
    }

    public static void drawFineBorderedRect(int x, int y, int x1, int y1, int bord, int color) {
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        VisualUtils.drawRect2((x *= 2) + 1, (y *= 2) + 1, x1 *= 2, y1 *= 2, color);
        VisualUtils.drawVerticalLine(x, y, y1, bord);
        VisualUtils.drawVerticalLine(x1, y, y1, bord);
        VisualUtils.drawHorizontalLine(x + 1, y, x1, bord);
        VisualUtils.drawHorizontalLine(x, y1, x1 + 1, bord);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
    }

    public static void drawHorizontalLine(int x, int y, int width, int color) {
        VisualUtils.drawRect((float)x, (float)y, (float)width, (float)(y + 1), color);
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
        VisualUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        VisualUtils.drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
        VisualUtils.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
        VisualUtils.drawHLine(x, x1 - 1.0f, y, borderC);
        VisualUtils.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
        VisualUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        VisualUtils.disableGL2D();
    }

    /*
     * Exception decompiling
     */
    public static void enableLighting() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl27 : INVOKESTATIC - null : Stack underflow
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

    public static void disableLighting() {
        OpenGlHelper.func_77473_a((int)OpenGlHelper.field_77476_b);
        GL11.glDisable((int)3553);
        OpenGlHelper.func_77473_a((int)OpenGlHelper.field_77478_a);
        GL11.glEnable((int)3042);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
    }

    /*
     * Exception decompiling
     */
    public static void drawRoundedImage(int x, int y, int width, int height, ResourceLocation rec) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl8 : INVOKESTATIC - null : Stack underflow
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
    public static void drawRoundedImage(int x, int y, int width, int height, ResourceLocation rec, float opacity) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl11 : INVOKESTATIC - null : Stack underflow
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

    public static void drawOutlineRect(double x, double y, double x2, double y2, double scale, int color) {
        VisualUtils.drawRect(x, y, x + scale, y2, color);
        VisualUtils.drawRect(x, y, x2, y + scale, color);
        VisualUtils.drawRect(x2, y, x2 + scale, y2, color);
        VisualUtils.drawRect(x, y2, x2 + scale, y2 + scale, color);
    }

    public static void drawGlow(double x, double y, double size, int col, int col2) {
        int times = (int)size;
        for (int i = 0; i < times; ++i) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179137_b((double)x, (double)y, (double)0.0);
            GL11.glRotated((double)(360.0f / (float)times * (float)i), (double)0.0, (double)0.0, (double)1.0);
            VisualUtils.drawGradientRect(0.0, 0.0, (double)(360.0f / (float)times * 6.0f), 0.0 + size, col, col2);
            GlStateManager.func_179137_b((double)(-x), (double)(-y), (double)0.0);
            GL11.glRotated((double)(-(360.0f / (float)times) * (float)i), (double)0.0, (double)0.0, (double)1.0);
            GlStateManager.func_179121_F();
        }
    }

    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        VisualUtils.enableGL2D();
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)7);
        VisualUtils.glColor(topColor);
        GL11.glVertex2f((float)x, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        VisualUtils.glColor(bottomColor);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
        GL11.glShadeModel((int)7425);
        VisualUtils.disableGL2D();
    }

    public static void drawGradientRect(double d, double e, double f, double g, int topColor, int bottomColor) {
        VisualUtils.enableGL2D();
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)3);
        VisualUtils.glColor(topColor);
        GL11.glVertex2d((double)d, (double)g);
        GL11.glVertex2d((double)f, (double)g);
        VisualUtils.glColor(bottomColor);
        GL11.glVertex2d((double)f, (double)e);
        GL11.glVertex2d((double)d, (double)e);
        GL11.glEnd();
        GL11.glShadeModel((int)7425);
        VisualUtils.disableGL2D();
    }

    public static void drawBorderedRect(double left, double top, double right, double bottom, float width, int borderColour, int colour) {
        GlStateManager.func_179094_E();
        VisualUtils.drawRect(left, top, right, bottom, colour);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        VisualUtils.glColor(borderColour);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void drawGradientBorderedRect(float x, float y, float x1, float y1, float lineWidth, int border, int bottom, int top) {
        VisualUtils.enableGL2D();
        VisualUtils.drawGradientRect(x, y, x1, y1, top, bottom);
        VisualUtils.glColor(border);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glVertex2f((float)x, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        VisualUtils.disableGL2D();
    }

    public static void beginGl() {
        GlStateManager.func_179094_E();
        RenderHelper.func_74519_b();
        GlStateManager.func_179140_f();
        GlStateManager.func_179147_l();
        int n = 0;
        GlStateManager.func_179097_i();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179090_x();
        if (antialiiasing) {
            GL11.glEnable((int)2848);
        }
        GL11.glLineWidth((float)1.0f);
    }

    public static void endGl() {
        GL11.glLineWidth((float)2.0f);
        if (antialiiasing) {
            GL11.glDisable((int)2848);
        }
        GlStateManager.func_179098_w();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179084_k();
        GlStateManager.func_179145_e();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderHelper.func_74518_a();
        GlStateManager.func_179121_F();
    }

    public static void drawLines(AxisAlignedBB boundingBox) {
        GL11.glPushMatrix();
        GL11.glBegin((int)2);
        GL11.glVertex3d((double)boundingBox.field_72340_a, (double)boundingBox.field_72338_b, (double)boundingBox.field_72339_c);
        GL11.glVertex3d((double)boundingBox.field_72340_a, (double)boundingBox.field_72337_e, (double)boundingBox.field_72334_f);
        GL11.glVertex3d((double)boundingBox.field_72336_d, (double)boundingBox.field_72338_b, (double)boundingBox.field_72339_c);
        GL11.glVertex3d((double)boundingBox.field_72336_d, (double)boundingBox.field_72337_e, (double)boundingBox.field_72334_f);
        GL11.glVertex3d((double)boundingBox.field_72336_d, (double)boundingBox.field_72338_b, (double)boundingBox.field_72334_f);
        GL11.glVertex3d((double)boundingBox.field_72340_a, (double)boundingBox.field_72337_e, (double)boundingBox.field_72334_f);
        GL11.glVertex3d((double)boundingBox.field_72336_d, (double)boundingBox.field_72338_b, (double)boundingBox.field_72339_c);
        GL11.glVertex3d((double)boundingBox.field_72340_a, (double)boundingBox.field_72337_e, (double)boundingBox.field_72339_c);
        GL11.glVertex3d((double)boundingBox.field_72336_d, (double)boundingBox.field_72338_b, (double)boundingBox.field_72339_c);
        GL11.glVertex3d((double)boundingBox.field_72340_a, (double)boundingBox.field_72338_b, (double)boundingBox.field_72334_f);
        GL11.glVertex3d((double)boundingBox.field_72336_d, (double)boundingBox.field_72337_e, (double)boundingBox.field_72339_c);
        GL11.glVertex3d((double)boundingBox.field_72340_a, (double)boundingBox.field_72337_e, (double)boundingBox.field_72334_f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void drawBorderedRefinedRect(float x, float y, float x1, float y1, float lineWidth, int inside, int border) {
        VisualUtils.enableGL2D();
        VisualUtils.drawRect(x, y, x1, y1, inside);
        VisualUtils.glColor(border);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glVertex2f((float)x, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        VisualUtils.disableGL2D();
    }

    /*
     * Exception decompiling
     */
    public static void drawHead(AbstractClientPlayer target, int x, int y, int width, int height) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl21 : INVOKESTATIC - null : Stack underflow
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

    public static void drawLineToPosition(double x, double y, double z, int color) {
        Minecraft mc = Minecraft.func_71410_x();
        double renderPosXDelta = x - VisualUtils.mc2.func_175598_ae().field_78730_l;
        double renderPosYDelta = y - VisualUtils.mc2.func_175598_ae().field_78731_m;
        double renderPosZDelta = z - VisualUtils.mc2.func_175598_ae().field_78728_n;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glLineWidth((float)1.0f);
        float blockPos9 = (float)(VisualUtils.mc2.field_71439_g.field_70165_t - x);
        float blockPos7 = (float)(VisualUtils.mc2.field_71439_g.field_70163_u - y);
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        float f4 = (float)(color >> 24 & 0xFF) / 255.0f;
        GL11.glColor4f((float)f, (float)f2, (float)f3, (float)f4);
        GL11.glLoadIdentity();
        boolean previousState = VisualUtils.mc2.field_71474_y.field_74336_f;
        VisualUtils.mc2.field_71474_y.field_74336_f = false;
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)0.0, (double)VisualUtils.mc2.field_71439_g.func_70047_e(), (double)0.0);
        GL11.glVertex3d((double)renderPosXDelta, (double)renderPosYDelta, (double)renderPosZDelta);
        GL11.glVertex3d((double)renderPosXDelta, (double)renderPosYDelta, (double)renderPosZDelta);
        GL11.glEnd();
        VisualUtils.mc2.field_71474_y.field_74336_f = previousState;
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawVerticalLine(int x, int y, int height, int color) {
        VisualUtils.drawRect2(x, y, x + 1, height, color);
    }

    public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
        int i;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        double x2 = x + width;
        double y2 = y + height;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x *= 2.0;
        y *= 2.0;
        x2 *= 2.0;
        y2 *= 2.0;
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        while (i <= 180) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y2 - radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
            i += 3;
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x2 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y2 - radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        while (i <= 180) {
            GL11.glVertex2d((double)(x2 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * radius));
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

    public static void drawRoundedRectNew(double x, double y, double width, double height, double radius, int color) {
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
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * radius * -1.0), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * radius * -1.0));
        }
        while (i <= 180) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * radius * -1.0), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * radius * -1.0));
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

    public static void illlIIIIiii(float x, float y, float start, float end, float w, float h, int color, float lineWidth) {
        if (start > end) {
            float temp = end;
            end = start;
            start = temp;
        }
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        for (float i = end; i >= start; i -= 4.0f) {
            GL11.glVertex2d((double)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)w * 1.001), (double)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)h * 1.001));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawBorderRect(float x, float y, float x2, float y2, float round, int color) {
        VisualUtils.drawRect(x += round / 2.0f + 0.5f, y += round / 2.0f + 0.5f, x2 -= round / 2.0f + 0.5f, y2 -= round / 2.0f + 0.5f, color);
        VisualUtils.circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        VisualUtils.circle(x + round / 2.0f, y2 - round / 2.0f - 0.2f, round, color);
        VisualUtils.circle(x + round / 2.0f, y + round / 2.0f, round, color);
        VisualUtils.circle(x2 - round / 2.0f, y2 - round / 2.0f - 0.2f, round, color);
        VisualUtils.drawRect(x - round / 2.0f - 0.5f, y + round / 2.0f, x2, y2 - round / 2.0f, color);
        VisualUtils.drawRect(x, y + round / 2.0f, x2 + round / 2.0f + 0.5f, y2 - round / 2.0f, color);
        VisualUtils.drawRect(x + round / 2.0f, y - round / 2.0f - 0.5f, x2 - round / 2.0f, y2 - round / 2.0f, color);
        VisualUtils.drawRect(x + round / 2.0f, y, x2 - round / 2.0f, y2 + round / 2.0f + 0.5f, color);
    }

    public static void arc3(float x, float y, float start, float end, float radius, Color color) {
        VisualUtils.arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void stopGlScissor() {
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
    }

    public static int getRainbowOpaque(int get, float get1, float get2, int i) {
        return 0;
    }

    public static int SkyRainbow(int i, float get, float get1) {
        return 0;
    }

    public static void drawRoundedRect3(float x, float y, float x2, float y2, float round, int color, int mode) {
        float rectX = x;
        float rectY = y;
        float rectX2 = x2;
        float rectY2 = y2;
        x += (float)((double)(round / 2.0f) + 0.5);
        y += (float)((double)(round / 2.0f) + 0.5);
        x2 -= (float)((double)(round / 2.0f) + 0.5);
        y2 -= (float)((double)(round / 2.0f) + 0.5);
        if (mode == 1) {
            VisualUtils.drawRect(x, rectY, rectX2, rectY2, color);
        } else {
            VisualUtils.drawRect(rectX, rectY, x2, rectY2, color);
        }
        VisualUtils.circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        VisualUtils.circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
        VisualUtils.circle(x + round / 2.0f, y + round / 2.0f, round, color);
        VisualUtils.circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        VisualUtils.drawRect((float)((int)(x - round / 2.0f - 0.5f)), (float)((int)(y + round / 2.0f)), (float)((int)x2), (float)((int)(y2 - round / 2.0f)), color);
        VisualUtils.drawRect((float)((int)x), (float)((int)(y + round / 2.0f)), (float)((int)(x2 + round / 2.0f + 0.5f)), (float)((int)(y2 - round / 2.0f)), color);
        VisualUtils.drawRect((float)((int)(x + round / 2.0f)), (float)((int)(y - round / 2.0f - 0.5f)), (float)((int)(x2 - round / 2.0f)), (float)((int)(y2 - round / 2.0f)), color);
        VisualUtils.drawRect((float)((int)(x + round / 2.0f)), (float)((int)y), (float)((int)(x2 - round / 2.0f)), (float)((int)(y2 + round / 2.0f + 0.5f)), color);
    }

    public static void drawFastRoundedRect(float x0, float y0, float x1, float y1, float radius, int color) {
        int j;
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
        for (j = 0; j <= 18; ++j) {
            float f8 = (float)j * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 + (double)radius * Math.cos(Math.toRadians(f8)))), (float)((float)((double)f7 - (double)radius * Math.sin(Math.toRadians(f8)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x0 + radius;
        f7 = y0 + radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (j = 0; j <= 18; ++j) {
            float f9 = (float)j * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 - (double)radius * Math.cos(Math.toRadians(f9)))), (float)((float)((double)f7 - (double)radius * Math.sin(Math.toRadians(f9)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x0 + radius;
        f7 = y1 - radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (j = 0; j <= 18; ++j) {
            float f10 = (float)j * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 - (double)radius * Math.cos(Math.toRadians(f10)))), (float)((float)((double)f7 + (double)radius * Math.sin(Math.toRadians(f10)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x1 - radius;
        f7 = y1 - radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (j = 0; j <= 18; ++j) {
            float f11 = (float)j * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 + (double)radius * Math.cos(Math.toRadians(f11)))), (float)((float)((double)f7 + (double)radius * Math.sin(Math.toRadians(f11)))));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glDisable((int)3042);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    /*
     * Exception decompiling
     */
    public static void drawFullCircle(float x, float y, float radius, int color, int outSideColor) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl130 : FLOAD - null : trying to set 1 previously set to 2
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

    public static void drawFace(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation skin = target.func_110306_p();
            Minecraft.func_71410_x().func_110434_K().func_110577_a(skin);
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Gui.func_152125_a((int)x, (int)y, (float)u, (float)v, (int)uWidth, (int)vHeight, (int)width, (int)height, (float)tileWidth, (float)tileHeight);
            GL11.glDisable((int)3042);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawRoundedRect31(float x, float y, float x1, float y1, int borderC, int insideC) {
        VisualUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        VisualUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        VisualUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        VisualUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        VisualUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        VisualUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        VisualUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        VisualUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        VisualUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        VisualUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        VisualUtils.disableGL2D();
        int n = 0;
    }

    public static void drawRoundRect4(float x, float y, float x1, float y1, int color) {
        VisualUtils.drawRoundedRect31(x, y, x1, y1, color, color);
        GlStateManager.func_179124_c((float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void startGlScissor(int x, int y, int width, int height) {
        int scaleFactor = new ScaledResolution(mc2).func_78325_e();
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        GL11.glScissor((int)(x * scaleFactor), (int)(VisualUtils.mc2.field_71440_d - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)((height += 14) * scaleFactor));
    }

    public static void arcIiiilllIIiii(float x, float y, float start, float end, float radius, int color, float lineWidth) {
        VisualUtils.illlIIIIiii(x, y, start, end, radius, radius, color, lineWidth);
    }

    /*
     * Exception decompiling
     */
    public static void drawHead(ResourceLocation skin, int x, int y, int width, int height) {
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

    public static int getRainbow(int index, int offset, float bright, float st) {
        float hue = (System.currentTimeMillis() + (long)offset * (long)index) % 2000L;
        return Color.getHSBColor(hue /= 2000.0f, st, bright).getRGB();
    }

    public static int Astolfo(int var2, float bright, float st, int index, int offset, float client) {
        double d;
        double rainbowDelay = Math.ceil(System.currentTimeMillis() + (long)(var2 * index)) / (double)offset;
        return Color.getHSBColor((double)((float)(d / (double)client)) < 0.5 ? -((float)(rainbowDelay / (double)client)) : (float)((rainbowDelay %= (double)client) / (double)client), st, bright).getRGB();
    }

    /*
     * Exception decompiling
     */
    public static void drawShadow(int x, int y, int width, int height) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl14 : INVOKESTATIC - null : Stack underflow
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

    public static void drawTexturedRect(int x, int y, int width, int height, String image2, ScaledResolution sr) {
        GL11.glPushMatrix();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        mc2.func_110434_K().func_110577_a(new ResourceLocation("likingsense/shadow/" + image2 + ".png"));
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GL11.glPopMatrix();
    }

    VisualUtils(Supplier<Color> colorSupplier) {
        this.colorSupplier = colorSupplier;
    }

    /*
     * Exception decompiling
     */
    public static Color fade(Color color) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl3 : INVOKESTATIC - null : Stack underflow
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

    public static Color fade(Color color, int index, int count, float customValue) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % customValue - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static Color fade2(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 10000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    /*
     * Exception decompiling
     */
    public static void drawSuperCircle(float x, float y, float radius, int color) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl98 : BIPUSH - null : trying to set 1 previously set to 2
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

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        VisualUtils.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        VisualUtils.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawRectBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        VisualUtils.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        VisualUtils.rectangle(x + width, y, x1 - width, y + width, borderColor);
        VisualUtils.rectangle(x, y, x + width, y1, borderColor);
        VisualUtils.rectangle(x1 - width, y, x1, y1, borderColor);
        VisualUtils.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
    }

    public static void drawRect(float x, float y, float x2, float y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        VisualUtils.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawLeftRounded(double left, double top, double right, double bottom, double radius, int color) {
        int i;
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        left *= 2.0;
        top *= 2.0;
        right *= 2.0;
        bottom *= 2.0;
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        VisualUtils.glColor(color);
        GL11.glBegin((int)9);
        for (i = 0; i <= 90; ++i) {
            GL11.glVertex2d((double)(left + radius + Math.sin((double)i * Math.PI / 180.0) * radius * -1.0), (double)(top + radius + Math.cos((double)i * Math.PI / 180.0) * radius * -1.0));
        }
        while (i <= 180) {
            GL11.glVertex2d((double)(left + radius + Math.sin((double)i * Math.PI / 180.0) * radius * -1.0), (double)(bottom - radius + Math.cos((double)i * Math.PI / 180.0) * radius * -1.0));
            ++i;
        }
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
    }

    public static Color fade(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static void colorRGBA(int color) {
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)r, (float)g, (float)b, (float)a);
    }

    public static void drawFillRectangle(double x, double y, double width, double height) {
        GlStateManager.func_179147_l();
        int n = 0;
        GL11.glDisable((int)3553);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glEnd();
    }

    public static void drawCirclePart(double x, double y, float fromAngle, float toAngle, float radius, int slices) {
        GlStateManager.func_179147_l();
        GL11.glBegin((int)6);
        GL11.glVertex2d((double)x, (double)y);
        float increment = (toAngle - fromAngle) / (float)slices;
        for (int i = 0; i <= slices; ++i) {
            float angle = fromAngle + (float)i * increment;
            float dX = MathHelper.func_76126_a((float)angle);
            float dY = MathHelper.func_76134_b((float)angle);
            GL11.glVertex2d((double)(x + (double)(dX * radius)), (double)(y + (double)(dY * radius)));
        }
        GL11.glEnd();
    }

    public static int getOppositeColor(int color) {
        int R = color & 0xFF;
        int G = color >> 8 & 0xFF;
        int B = color >> 16 & 0xFF;
        int A = color >> 24 & 0xFF;
        R = 255 - R;
        G = 255 - G;
        B = 255 - B;
        return R + (G << 8) + (B << 16) + (A << 24);
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        VisualUtils.drawRect(x, y, x2, y2, col2);
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glLineWidth((float)l1);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)255.0f);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void quickRenderCircle(double x, double y, double start, double end, double w, double h) {
        if (start > end) {
            double temp = end;
            end = start;
            start = temp;
        }
        GL11.glBegin((int)6);
        GL11.glVertex2d((double)x, (double)y);
        for (double i = end; i >= start; i -= 4.0) {
            double ldx = Math.cos(i * Math.PI / 180.0) * w;
            double ldy = Math.sin(i * Math.PI / 180.0) * h;
            GL11.glVertex2d((double)(x + ldx), (double)(y + ldy));
        }
        GL11.glVertex2d((double)x, (double)y);
        GL11.glEnd();
    }

    public static void quickDrawRect(float x, float y, float x2, float y2, int color) {
        VisualUtils.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void quickDrawRect(float x, float y, float x2, float y2) {
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
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

    public static Color rainbow(long time, float count, float fade) {
        float hue = ((float)time + (1.0f + count) * 2.0E8f) / 1.0E10f % 1.0f;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        Color c = new Color((int)color);
        return new Color((float)c.getRed() / 255.0f * fade, (float)c.getGreen() / 255.0f * fade, (float)c.getBlue() / 255.0f * fade, (float)c.getAlpha() / 255.0f);
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
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)var6, (float)var7, (float)var8, (float)var11);
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(left, bottom, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(right, bottom, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(right, top, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(left, top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawFancy(double d, double e, double f2, double f3, int paramColor) {
        float alpha = (float)(paramColor >> 24 & 0xFF) / 255.0f;
        float red = (float)(paramColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(paramColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(paramColor & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GL11.glPushMatrix();
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)(f2 + 1.300000011920929), (double)e);
        GL11.glVertex2d((double)(d + 1.0), (double)e);
        GL11.glVertex2d((double)(d - 1.300000011920929), (double)f3);
        GL11.glVertex2d((double)(f2 - 1.0), (double)f3);
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
        GL11.glDisable((int)2832);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void Gamesense(double x, double y, double x1, double y1, double size, float color1, float color2, float color3) {
        VisualUtils.rectangleBordered(x, y, x1 + size, y1 + size, 0.5, Colors.getColor(90), Colors.getColor(0));
        VisualUtils.rectangleBordered(x + 1.0, y + 1.0, x1 + size - 1.0, y1 + size - 1.0, 1.0, Colors.getColor(90), Colors.getColor(61));
        VisualUtils.rectangleBordered(x + 2.5, y + 2.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, Colors.getColor(61), Colors.getColor(0));
        VisualUtils.drawGradientSideways(x + size * 3.0, y + 3.0, x1 - size * 2.0, y + 4.0, (int)color2, (int)color3);
    }

    public static int reAlpha(int n, float n2) {
        Color color = new Color(n);
        return new Color(0.003921569f * (float)color.getRed(), 0.003921569f * (float)color.getGreen(), 0.003921569f * (float)color.getBlue(), n2).getRGB();
    }

    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), 0.8f, 0.7f);
    }

    public static void drawScaledCustomSizeModalRect(double x, double y, float u, float v, double uWidth, double vHeight, double width, double height, float tileWidth, float tileHeight) {
        float f2 = 1.0f / tileWidth;
        float f1 = 1.0f / tileHeight;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        bufferbuilder.func_181662_b(x, y + height, 0.0).func_187315_a((double)(u * f2), (double)((v + (float)vHeight) * f1)).func_181675_d();
        bufferbuilder.func_181662_b(x + width, y + height, 0.0).func_187315_a((double)((u + (float)uWidth) * f2), (double)((v + (float)vHeight) * f1)).func_181675_d();
        bufferbuilder.func_181662_b(x + width, y, 0.0).func_187315_a((double)((u + (float)uWidth) * f2), (double)(v * f1)).func_181675_d();
        bufferbuilder.func_181662_b(x, y, 0.0).func_187315_a((double)(u * f2), (double)(v * f1)).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0f / tileWidth;
        float f1 = 1.0f / tileHeight;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        bufferbuilder.func_181662_b((double)x, (double)(y + height), 0.0).func_187315_a((double)(u * f), (double)((v + (float)vHeight) * f1)).func_181675_d();
        bufferbuilder.func_181662_b((double)(x + width), (double)(y + height), 0.0).func_187315_a((double)((u + (float)uWidth) * f), (double)((v + (float)vHeight) * f1)).func_181675_d();
        bufferbuilder.func_181662_b((double)(x + width), (double)y, 0.0).func_187315_a((double)((u + (float)uWidth) * f), (double)(v * f1)).func_181675_d();
        bufferbuilder.func_181662_b((double)x, (double)y, 0.0).func_187315_a((double)(u * f), (double)(v * f1)).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void quickDrawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
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
    }

    /*
     * Exception decompiling
     */
    public static void drawMatrixRound(Entity entity, float partialTicks, double rad) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl44 : DLOAD - null : trying to set 4 previously set to 0
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

    public static void drawRect(double x, double y, double x2, double y2, int color) {
        Gui.func_73734_a((int)((int)x), (int)((int)y), (int)((int)x2), (int)((int)y2), (int)color);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        VisualUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        VisualUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawRoundedRect(float n, float n2, float n3, float n4, int n5, int n6) {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n7 = 0;
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        int n8 = 0;
        int n9 = 0;
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        VisualUtils.drawVLine(n *= 2.0f, (n2 *= 2.0f) + 1.0f, (n4 *= 2.0f) - 2.0f, n5);
        VisualUtils.drawVLine((n3 *= 2.0f) - 1.0f, n2 + 1.0f, n4 - 2.0f, n5);
        VisualUtils.drawHLine(n + 2.0f, n3 - 3.0f, n2, n5);
        VisualUtils.drawHLine(n + 2.0f, n3 - 3.0f, n4 - 1.0f, n5);
        VisualUtils.drawHLine(n + 1.0f, n + 1.0f, n2 + 1.0f, n5);
        VisualUtils.drawHLine(n3 - 2.0f, n3 - 2.0f, n2 + 1.0f, n5);
        VisualUtils.drawHLine(n3 - 2.0f, n3 - 2.0f, n4 - 2.0f, n5);
        VisualUtils.drawHLine(n + 1.0f, n + 1.0f, n4 - 2.0f, n5);
        VisualUtils.drawRect(n + 1.0f, n2 + 1.0f, n3 - 1.0f, n4 - 1.0f, n6);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        int n10 = 0;
        int n11 = 0;
    }

    public static void drawRoundedRect2(float x, float y, float x2, float y2, float round, int color) {
        VisualUtils.drawRect((float)((int)(x += (float)((double)(round / 2.0f) + 0.5))), (float)((int)(y += (float)((double)(round / 2.0f) + 0.5))), (float)((int)(x2 -= (float)((double)(round / 2.0f) + 0.5))), (float)((int)(y2 -= (float)((double)(round / 2.0f) + 0.5))), color);
        VisualUtils.circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        VisualUtils.circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
        VisualUtils.circle(x + round / 2.0f, y + round / 2.0f, round, color);
        VisualUtils.circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        VisualUtils.drawRect((float)((int)(x - round / 2.0f - 0.5f)), (float)((int)(y + round / 2.0f)), (float)((int)x2), (float)((int)(y2 - round / 2.0f)), color);
        VisualUtils.drawRect((float)((int)x), (float)((int)(y + round / 2.0f)), (float)((int)(x2 + round / 2.0f + 0.5f)), (float)((int)(y2 - round / 2.0f)), color);
        VisualUtils.drawRect((float)((int)(x + round / 2.0f)), (float)((int)(y - round / 2.0f - 0.5f)), (float)((int)(x2 - round / 2.0f)), (float)((int)(y2 - round / 2.0f)), color);
        VisualUtils.drawRect((float)((int)(x + round / 2.0f)), (float)((int)y), (float)((int)(x2 - round / 2.0f)), (float)((int)(y2 + round / 2.0f + 0.5f)), color);
    }

    public static void ACircle(float x, float y, float start, float end, float w, float h, int color, float lineWidth) {
        if (start > end) {
            float temp = end;
            end = start;
            start = temp;
        }
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        for (float i = end; i >= start; i -= 4.0f) {
            GL11.glVertex2d((double)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)w * 1.001), (double)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)h * 1.001));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
    }

    public static void BCricle(float x, float y, float start, float end, float radius, int color, float lineWidth) {
        VisualUtils.ACircle(x, y, start, end, radius, radius, color, lineWidth);
    }

    public static void glColor(int cl) {
        float alpha = (float)(cl >> 24 & 0xFF) / 255.0f;
        float red = (float)(cl >> 16 & 0xFF) / 255.0f;
        float green = (float)(cl >> 8 & 0xFF) / 255.0f;
        float blue = (float)(cl & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        VisualUtils.glColor(col1);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        VisualUtils.glColor(col2);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void drawOutFullCircle(float x, float y, float radius, int fill, float lineWidth) {
        VisualUtils.BCricle(x, y, 0.0f, 360.0f, radius, fill, lineWidth);
    }

    public static void drawOutFullCircle(float x, float y, float radius, int fill, float lineWidth, float start, float end) {
        VisualUtils.BCricle(x, y, start, end, radius, fill, lineWidth);
    }

    /*
     * Exception decompiling
     */
    public static void drawFilledCircle(int xx, int yy, float radius, Color col) {
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
     * Exception decompiling
     */
    public static void drawFilledCircle(float xx, float yy, float radius, Color col) {
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

    /*
     * Exception decompiling
     */
    public static void drawFilledCircle(float xx, float yy, float radius, int col) {
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
     * Exception decompiling
     */
    public static void drawFilledCircle(int xx, int yy, float radius, int col, int xLeft, int yAbove, int xRight, int yUnder) {
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

    public static double getAnimationState(double n, double n2, double n3) {
        float n4 = (float)((double)delta * n3);
        n = n < n2 ? (n + (double)n4 < n2 ? (n += (double)n4) : n2) : (n - (double)n4 > n2 ? (n -= (double)n4) : n2);
        return n;
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

    public static void drawImage(ResourceLocation image2, int x, int y, int width, int height) {
        VisualUtils.drawImage(image2, x, y, width, height, 1.0f);
    }

    public static void drawImage(ResourceLocation image2, float x, float y, float width, float height) {
        VisualUtils.drawImage(image2, (int)x, (int)y, (int)width, (int)height, 1.0f);
    }

    public static void drawCustomImage(int x, int y, int width, int height, ResourceLocation image2) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        int n = 0;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void arcEllipse(float x, float y, float start, float end, float w, float h, int color) {
        float ldy;
        float ldx;
        float i;
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
        float var6 = (float)(color >> 16 & 0xFF) / 255.0f;
        float var7 = (float)(color >> 8 & 0xFF) / 255.0f;
        float var8 = (float)(color & 0xFF) / 255.0f;
        Tessellator var9 = Tessellator.func_178181_a();
        BufferBuilder var10 = var9.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)var6, (float)var7, (float)var8, (float)var11);
        if (var11 > 0.5f) {
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)2.0f);
            GL11.glBegin((int)3);
            for (i = end; i >= start; i -= 4.0f) {
                ldx = (float)Math.cos((double)i * Math.PI / 180.0) * (w * 1.001f);
                ldy = (float)Math.sin((double)i * Math.PI / 180.0) * (h * 1.001f);
                GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
        }
        GL11.glBegin((int)6);
        for (i = end; i >= start; i -= 4.0f) {
            ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w;
            ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h;
            GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
        }
        GL11.glEnd();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void arcEllipse(float x, float y, float start, float end, float w, float h, Color color) {
        float ldy;
        float ldx;
        float i;
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        Tessellator var9 = Tessellator.func_178181_a();
        BufferBuilder var10 = var9.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        if ((float)color.getAlpha() > 0.5f) {
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)2.0f);
            GL11.glBegin((int)3);
            for (i = end; i >= start; i -= 4.0f) {
                ldx = (float)Math.cos((double)i * Math.PI / 180.0) * (w * 1.001f);
                ldy = (float)Math.sin((double)i * Math.PI / 180.0) * (h * 1.001f);
                GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
        }
        GL11.glBegin((int)6);
        for (i = end; i >= start; i -= 4.0f) {
            ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w;
            ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h;
            GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
        }
        GL11.glEnd();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void arc(float x, float y, float start, float end, float radius, int color) {
        VisualUtils.arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arc(float x, float y, float start, float end, float radius, Color color) {
        VisualUtils.arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void circle(float x, float y, float radius, int fill) {
        GL11.glEnable((int)3042);
        VisualUtils.arc(x, y, 0.0f, 360.0f, radius, fill);
        GL11.glDisable((int)3042);
    }

    public static void drawArc(float x1, float y1, double r, int color, int startPoint, double arc, int linewidth) {
        r *= 2.0;
        x1 *= 2.0f;
        y1 *= 2.0f;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        int n2 = 0;
        int n3 = 0;
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glLineWidth((float)linewidth);
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)3);
        int i = startPoint;
        while ((double)i <= arc) {
            double x = Math.sin((double)i * Math.PI / 180.0) * r;
            double y = Math.cos((double)i * Math.PI / 180.0) * r;
            GL11.glVertex2d((double)((double)x1 + x), (double)((double)y1 + y));
            ++i;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        int n4 = 0;
        int n5 = 0;
    }

    public static void color(int color, float alpha) {
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)r, (float)g, (float)b, (float)alpha);
    }

    public static void circle(float x, float y, float radius, Color fill) {
        VisualUtils.arc(x, y, 0.0f, 360.0f, radius, fill);
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

    public static void drawRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color) {
        VisualUtils.drawRoundedRect(paramXStart, paramYStart, paramXEnd, paramYEnd, radius, color, true);
    }

    private static float drawExhiOutlined(String text, float x, float y, float borderWidth, int borderColor, int mainColor, boolean drawText) {
        Fonts.font40.drawString(text, x, y - borderWidth, borderColor);
        Fonts.font40.drawString(text, x, y + borderWidth, borderColor);
        Fonts.font40.drawString(text, x - borderWidth, y, borderColor);
        Fonts.font40.drawString(text, x + borderWidth, y, borderColor);
        if (drawText) {
            Fonts.font40.drawString(text, x, y, mainColor);
        }
        return x + (float)Fonts.font40.getStringWidth(text) - 2.0f;
    }

    private static int getMainColor(int level) {
        if (level == 4) {
            return -5636096;
        }
        return -1;
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

    public static void drawCircle(float x, float y, float radius, float lineWidth, int start, int end, Color color) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        VisualUtils.glColor(color);
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

    public static void drawCircle(float x, float y, float radius, float lineWidth, int start, int end) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        VisualUtils.glColor(Color.WHITE);
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

    public static void drawTriAngle(float cx, float cy, float r, float n, Color color, boolean polygon) {
        cx = (float)((double)cx * 2.0);
        cy = (float)((double)cy * 2.0);
        double b = 6.2831852 / (double)n;
        double p = Math.cos(b);
        double s = Math.sin(b);
        r = (float)((double)r * 2.0);
        double x = r;
        double y = 0.0;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GL11.glLineWidth((float)1.0f);
        VisualUtils.enableGlCap(2848);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n2 = 0;
        GlStateManager.func_179117_G();
        VisualUtils.glColor(color);
        GlStateManager.func_179152_a((float)0.5f, (float)0.5f, (float)0.5f);
        bufferbuilder.func_181668_a(polygon ? 9 : 2, DefaultVertexFormats.field_181705_e);
        int ii = 0;
        while ((float)ii < n) {
            bufferbuilder.func_181662_b(x + (double)cx, y + (double)cy, 0.0).func_181675_d();
            double t2 = x;
            x = p * x - s * y;
            y = s * t2 + p * y;
            ++ii;
        }
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawRectBasedBorder(float x, float y, float x2, float y2, float width, int color1) {
        VisualUtils.drawRect(x - width / 2.0f, y - width / 2.0f, x2 + width / 2.0f, y + width / 2.0f, color1);
        VisualUtils.drawRect(x - width / 2.0f, y + width / 2.0f, x + width / 2.0f, y2 + width / 2.0f, color1);
        VisualUtils.drawRect(x2 - width / 2.0f, y + width / 2.0f, x2 + width / 2.0f, y2 + width / 2.0f, color1);
        VisualUtils.drawRect(x + width / 2.0f, y2 - width / 2.0f, x2 - width / 2.0f, y2 + width / 2.0f, color1);
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
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b((double)left, (double)bottom, 0.0).func_181675_d();
        bufferbuilder.func_181662_b((double)right, (double)bottom, 0.0).func_181675_d();
        bufferbuilder.func_181662_b((double)right, (double)top, 0.0).func_181675_d();
        bufferbuilder.func_181662_b((double)left, (double)top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void newDrawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(left, bottom, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(right, bottom, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(right, top, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(left, top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawRectBasedBorder(double x, double y, double x2, double y2, double width, int color1) {
        VisualUtils.newDrawRect(x - width / 2.0, y - width / 2.0, x2 + width / 2.0, y + width / 2.0, color1);
        VisualUtils.newDrawRect(x - width / 2.0, y + width / 2.0, x + width / 2.0, y2 + width / 2.0, color1);
        VisualUtils.newDrawRect(x2 - width / 2.0, y + width / 2.0, x2 + width / 2.0, y2 + width / 2.0, color1);
        VisualUtils.newDrawRect(x + width / 2.0, y2 - width / 2.0, x2 - width / 2.0, y2 + width / 2.0, color1);
    }

    public static void drawExhiRect(float x, float y, float x2, float y2, float alpha) {
        VisualUtils.drawRect(x - 3.5f, y - 3.5f, x2 + 3.5f, y2 + 3.5f, new Color(0.0f, 0.0f, 0.0f, alpha).getRGB());
        VisualUtils.drawRect(x - 3.0f, y - 3.0f, x2 + 3.0f, y2 + 3.0f, new Color(0.19607843f, 0.19607843f, 0.19607843f, alpha).getRGB());
        VisualUtils.drawRect(x - 2.5f, y - 2.5f, x2 + 2.5f, y2 + 2.5f, new Color(0.101960786f, 0.101960786f, 0.101960786f, alpha).getRGB());
        VisualUtils.drawRect(x - 0.5f, y - 0.5f, x2 + 0.5f, y2 + 0.5f, new Color(0.19607843f, 0.19607843f, 0.19607843f, alpha).getRGB());
        VisualUtils.drawRect(x, y, x2, y2, new Color(0.07058824f, 0.07058824f, 0.07058824f, alpha).getRGB());
    }

    public static void drawMosswareRect(float x, float y, float x2, float y2, float width, int color1, int color2) {
        VisualUtils.drawRect(x, y, x2, y2, color2);
        VisualUtils.drawBorder(x, y, x2, y2, width, color1);
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, float zLevel) {
        float f = 0.00390625f;
        float f1 = 0.00390625f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        bufferbuilder.func_181662_b((double)(x + 0), (double)(y + height), (double)zLevel).func_187315_a((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).func_181675_d();
        bufferbuilder.func_181662_b((double)(x + width), (double)(y + height), (double)zLevel).func_187315_a((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).func_181675_d();
        bufferbuilder.func_181662_b((double)(x + width), (double)(y + 0), (double)zLevel).func_187315_a((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).func_181675_d();
        bufferbuilder.func_181662_b((double)(x + 0), (double)(y + 0), (double)zLevel).func_187315_a((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void MdrawRect(double d, double e, double g, double h, int color) {
        if (d < g) {
            int i = (int)d;
            d = g;
            g = i;
        }
        if (e < h) {
            int j = (int)e;
            e = h;
            h = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(d, h, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(g, h, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(g, e, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(d, e, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawOutlinedRect(float x, float y, float width, float height, float lineSize, int lineColor) {
        RenderUtils.drawRect(x, y, width, y + lineSize, lineColor);
        RenderUtils.drawRect(x, height - lineSize, width, height, lineColor);
        RenderUtils.drawRect(x, y + lineSize, x + lineSize, height - lineSize, lineColor);
        RenderUtils.drawRect(width - lineSize, y + lineSize, width, height - lineSize, lineColor);
    }

    public static void drawEntityOnScreen(double posX, double posY, float scale, EntityLivingBase entity) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179142_g();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)50.0);
        GlStateManager.func_179152_a((float)(-scale), (float)scale, (float)scale);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179137_b((double)0.0, (double)0.0, (double)0.0);
        RenderManager rendermanager = mc2.func_175598_ae();
        rendermanager.func_178631_a(180.0f);
        rendermanager.func_178633_a(false);
        rendermanager.func_178633_a(true);
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
    }

    public static double progressiveAnimation(double now, double desired, double speed) {
        double dif = Math.abs(now - desired);
        int fps = Minecraft.func_175610_ah();
        if (dif > 0.0) {
            double animationSpeed = MathUtils.roundToDecimalPlace(Math.min(10.0, Math.max(0.05, 144.0 / (double)fps * (dif / 10.0) * speed)), 0.05);
            if (dif != 0.0 && dif < animationSpeed) {
                animationSpeed = dif;
            }
            if (now < desired) {
                return now + animationSpeed;
            }
            if (now > desired) {
                return now - animationSpeed;
            }
        }
        return now;
    }

    public static float calculateCompensation(float target, float current, long delta, double speed) {
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (delta > 1000L) {
            delta = 16L;
        }
        if ((double)diff > speed) {
            float f;
            double xD = speed * (double)delta / 16.0 < 0.5 ? 0.5 : speed * (double)delta / 16.0;
            current = (float)((double)current - xD);
            if (f < target) {
                current = target;
            }
        } else if ((double)diff < -speed) {
            float f;
            double xD = speed * (double)delta / 16.0 < 0.5 ? 0.5 : speed * (double)delta / 16.0;
            current = (float)((double)current + xD);
            if (f > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public static double transition(double now, double desired, double speed) {
        double dif = Math.abs(now - desired);
        int fps = Minecraft.func_175610_ah();
        if (dif > 0.0) {
            double animationSpeed = MathUtils.roundToDecimalPlace(Math.min(10.0, Math.max(0.0625, 144.0 / (double)fps * (dif / 10.0) * speed)), 0.0625);
            if (dif != 0.0 && dif < animationSpeed) {
                animationSpeed = dif;
            }
            if (now < desired) {
                return now + animationSpeed;
            }
            if (now > desired) {
                return now - animationSpeed;
            }
        }
        return now;
    }

    public static ScaledResolution getScaledResolution() {
        int displayWidth = Display.getWidth();
        int displayHeight = Display.getHeight();
        int guiScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
        if (displayWidth != lastScaledWidth || displayHeight != lastScaledHeight || guiScale != lastGuiScale) {
            lastScaledWidth = displayWidth;
            lastScaledHeight = displayHeight;
            lastGuiScale = guiScale;
            scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
            return scaledResolution;
        }
        return scaledResolution;
    }

    public static void startScissorBox(ScaledResolution sr, int x, int y, int width, int height) {
        int sf = sr.func_78325_e();
        GL11.glScissor((int)(x * sf), (int)((sr.func_78328_b() - (y + height)) * sf), (int)(width * sf), (int)(height * sf));
    }

    public static int darker(int color, float factor) {
        int r = (int)((float)(color >> 16 & 0xFF) * factor);
        int g = (int)((float)(color >> 8 & 0xFF) * factor);
        int b = (int)((float)(color & 0xFF) * factor);
        int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | (a & 0xFF) << 24;
    }

    public static int darker(int color) {
        return VisualUtils.darker(color, 0.6f);
    }

    static {
        counts = 0;
        frustrum = new Frustum();
        DISPLAY_LISTS_2D = new int[4];
        for (int i = 0; i < DISPLAY_LISTS_2D.length; ++i) {
            VisualUtils.DISPLAY_LISTS_2D[i] = GL11.glGenLists((int)1);
        }
        GL11.glNewList((int)DISPLAY_LISTS_2D[0], (int)4864);
        VisualUtils.quickDrawRect(-7.0f, 2.0f, -4.0f, 3.0f);
        VisualUtils.quickDrawRect(4.0f, 2.0f, 7.0f, 3.0f);
        VisualUtils.quickDrawRect(-7.0f, 0.5f, -6.0f, 3.0f);
        VisualUtils.quickDrawRect(6.0f, 0.5f, 7.0f, 3.0f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[1], (int)4864);
        VisualUtils.quickDrawRect(-7.0f, 3.0f, -4.0f, 3.3f);
        VisualUtils.quickDrawRect(4.0f, 3.0f, 7.0f, 3.3f);
        VisualUtils.quickDrawRect(-7.3f, 0.5f, -7.0f, 3.3f);
        VisualUtils.quickDrawRect(7.0f, 0.5f, 7.3f, 3.3f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[2], (int)4864);
        VisualUtils.quickDrawRect(4.0f, -20.0f, 7.0f, -19.0f);
        VisualUtils.quickDrawRect(-7.0f, -20.0f, -4.0f, -19.0f);
        VisualUtils.quickDrawRect(6.0f, -20.0f, 7.0f, -17.5f);
        VisualUtils.quickDrawRect(-7.0f, -20.0f, -6.0f, -17.5f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[3], (int)4864);
        VisualUtils.quickDrawRect(7.0f, -20.0f, 7.3f, -17.5f);
        VisualUtils.quickDrawRect(-7.3f, -20.0f, -7.0f, -17.5f);
        VisualUtils.quickDrawRect(4.0f, -20.3f, 7.3f, -20.0f);
        VisualUtils.quickDrawRect(-7.3f, -20.3f, -4.0f, -20.0f);
        GL11.glEndList();
        modelView = BufferUtils.createFloatBuffer((int)16);
        projection = BufferUtils.createFloatBuffer((int)16);
        viewport = BufferUtils.createIntBuffer((int)16);
        modelview = GLAllocation.func_74529_h((int)16);
        projections = GLAllocation.func_74529_h((int)16);
        startTime = System.currentTimeMillis();
        antialiiasing = false;
    }

    public static class R2DUtils {
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

        public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
            R2DUtils.enableGL2D();
            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
            R2DUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
            R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
            R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
            R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
            R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
            R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
            R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
            R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
            R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
            GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
            R2DUtils.disableGL2D();
            int n = 0;
        }

        public static void drawRect(double x2, double y2, double x1, double y1, int color) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(color);
            R2DUtils.drawRect(x2, y2, x1, y1);
            R2DUtils.disableGL2D();
        }

        private static void drawRect(double x2, double y2, double x1, double y1) {
            GL11.glBegin((int)7);
            GL11.glVertex2d((double)x2, (double)y1);
            GL11.glVertex2d((double)x1, (double)y1);
            GL11.glVertex2d((double)x1, (double)y2);
            GL11.glVertex2d((double)x2, (double)y2);
            GL11.glEnd();
        }

        public static void glColor(int hex) {
            float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
            float red = (float)(hex >> 16 & 0xFF) / 255.0f;
            float green = (float)(hex >> 8 & 0xFF) / 255.0f;
            float blue = (float)(hex & 0xFF) / 255.0f;
            GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        }

        public static void drawRect(float x, float y, float x1, float y1, int color) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(color);
            R2DUtils.drawRect(x, y, x1, y1);
            R2DUtils.disableGL2D();
        }

        public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(borderColor);
            R2DUtils.drawRect(x + width, y, x1 - width, y + width);
            R2DUtils.drawRect(x, y, x + width, y1);
            R2DUtils.drawRect(x1 - width, y, x1, y1);
            R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
            R2DUtils.disableGL2D();
        }

        public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
            R2DUtils.enableGL2D();
            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
            R2DUtils.drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
            R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
            R2DUtils.drawHLine(x, x1 - 1.0f, y, borderC);
            R2DUtils.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
            R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
            GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
            R2DUtils.disableGL2D();
        }

        public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
            R2DUtils.enableGL2D();
            GL11.glShadeModel((int)7425);
            GL11.glBegin((int)7);
            R2DUtils.glColor(topColor);
            GL11.glVertex2f((float)x, (float)y1);
            GL11.glVertex2f((float)x1, (float)y1);
            R2DUtils.glColor(bottomColor);
            GL11.glVertex2f((float)x1, (float)y);
            GL11.glVertex2f((float)x, (float)y);
            GL11.glEnd();
            GL11.glShadeModel((int)7424);
            R2DUtils.disableGL2D();
        }

        public static void drawHLine(float x, float y, float x1, int y1) {
            if (y < x) {
                float var5 = x;
                x = y;
                y = var5;
            }
            R2DUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
        }

        public static void drawVLine(float x, float y, float x1, int y1) {
            if (x1 < y) {
                float var5 = y;
                y = x1;
                x1 = var5;
            }
            R2DUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
        }

        public static void drawHLine(float x, float y, float x1, int y1, int y2) {
            if (y < x) {
                float var5 = x;
                x = y;
                y = var5;
            }
            R2DUtils.drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
        }

        public static void drawRect(float x, float y, float x1, float y1) {
            GL11.glBegin((int)7);
            GL11.glVertex2f((float)x, (float)y1);
            GL11.glVertex2f((float)x1, (float)y1);
            GL11.glVertex2f((float)x1, (float)y);
            GL11.glVertex2f((float)x, (float)y);
            GL11.glEnd();
        }
    }
}

