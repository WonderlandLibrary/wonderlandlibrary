package tech.drainwalk.utility.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.utility.Utility;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.shader.Shader;

public class RenderUtility extends Utility {
    private static final Tessellator TESSELLATOR = Tessellator.getInstance();
    private static final BufferBuilder BUILDER = TESSELLATOR.getBuffer();

    private static final Shader ROUNDED = new Shader("drainwalk/shaders/rounded.fsh", true);
    private static final Shader ROUNDED_GRADIENT = new Shader("drainwalk/shaders/rounded_gradient.fsh", true);
    private static final Shader ROUNDED_OUTLINE = new Shader("drainwalk/shaders/rounded_outline.fsh", true);
    private static final Shader SHADOW = new Shader("drainwalk/shaders/shadow.fsh", true);
    private static final Shader CIRCLE = new Shader("drainwalk/shaders/outline_circle.fsh", true);
    private static final Shader ROUNDED_TEXTURE = new Shader("drainwalk/shaders/rounded_texture.fsh", true);


    public static void drawRoundedRect(float x, float y, float x2, float y2, float round, int color) {
        drawRoundedGradientRect(x, y, x2, y2, round, 1, color, color, color, color);
    }

    public static void drawRoundedRect(float x, float y, float x2, float y2, float round, float swapX, float swapY, int firstColor, int secondColor) {
        float[] c = ColorUtility.getRGBAf(firstColor);
        float[] c1 = ColorUtility.getRGBAf(secondColor);

        GlStateManager.color(0, 0, 0, 0);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        ROUNDED.useProgram();
        ROUNDED.setupUniform2f("size", (x2 - round) * 2, (y2 - round) * 2);
        ROUNDED.setupUniform1f("round", round);
        ROUNDED.setupUniform2f("smoothness", 0.f, 1.5f);
        ROUNDED.setupUniform2f("swap", swapX, swapY);
        ROUNDED.setupUniform4f("firstColor", c[0], c[1], c[2], c[3]);
        ROUNDED.setupUniform4f("secondColor", c1[0], c1[1], c1[2], c[3]);

        allocTextureRectangle(x, y, x2, y2);
        ROUNDED.unloadProgram();

        GlStateManager.disableBlend();
    }

    public static void drawRoundedGradientRect(float x, float y, float x2, float y2, float round, int color1, int color2, int color3, int color4) {
        drawRoundedGradientRect(x, y, x2, y2, round, 1, color1, color2, color3, color4);
    }

    public static void drawRoundedGradientRect(float x, float y, float x2, float y2, float round, float value, int color1, int color2, int color3, int color4) {
        float[] c1 = ColorUtility.getRGBAf(color1);
        float[] c2 = ColorUtility.getRGBAf(color2);
        float[] c3 = ColorUtility.getRGBAf(color3);
        float[] c4 = ColorUtility.getRGBAf(color4);

        GlStateManager.color(0, 0, 0, 0);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        ROUNDED_GRADIENT.useProgram();
        ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2, y2 * 2);
        ROUNDED_GRADIENT.setupUniform4f("round", round, round, round, round);
        ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.f, 1.5f);
        ROUNDED_GRADIENT.setupUniform1f("value", value);
        ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
        ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
        ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
        ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);

        allocTextureRectangle(x, y, x2, y2);
        ROUNDED_GRADIENT.unloadProgram();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedShadow(float x, float y, float x2, float y2, float softness, float radius, int color) {
        float[] c = ColorUtility.getRGBAf(color);
        GlStateManager.color(0, 0, 0, 0);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        SHADOW.useProgram();
        SHADOW.setupUniform2f("size", (x2 - radius) * 2, (y2 - radius) * 2);
        SHADOW.setupUniform1f("softness", softness);
        SHADOW.setupUniform1f("radius", radius);
        SHADOW.setupUniform4f("color", c[0], c[1], c[2], c[3]);

        allocTextureRectangle(x - (softness / 2f), y - (softness / 2f), x2 + (softness), y2 + (softness));
        SHADOW.unloadProgram();

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public static void drawRoundedOutlineRect(float x, float y, float x2, float y2, float round, float thickness, int color) {
        float[] c = ColorUtility.getRGBAf(color);

        GlStateManager.color(0, 0, 0, 0);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        ROUNDED_OUTLINE.useProgram();
        ROUNDED_OUTLINE.setupUniform2f("size", x2 * 2, y2 * 2);
        ROUNDED_OUTLINE.setupUniform1f("round", round);
        ROUNDED_OUTLINE.setupUniform1f("thickness", thickness);
        ROUNDED_OUTLINE.setupUniform2f("smoothness", thickness - 1.5f, thickness);
        ROUNDED_OUTLINE.setupUniform4f("color", c[0], c[1], c[2], c[3]);
        allocTextureRectangle(x, y, x2, y2);
        ROUNDED_OUTLINE.unloadProgram();
        GlStateManager.disableBlend();
    }

    public static void drawARCCircle(float x, float y, float radius, float progress, float borderThickness, int color) {
        drawARCCircle(x, y, radius, progress, 100, borderThickness, color, color);
    }

    public static void drawARCCircle(float x, float y, float radius, float progress, int change, float borderThickness, int color) {
        drawARCCircle(x, y, radius, progress, change, borderThickness, color, color);
    }

    public static void drawARCCircle(float x, float y, float radius, float progress, float borderThickness, int firstColor, int secondColor) {
        drawARCCircle(x, y, radius, progress, 100, borderThickness, firstColor, secondColor);
    }

    public static void drawARCCircle(float x, float y, float radius, float progress, int change, float borderThickness, int firstColor, int secondColor) {
        float[] c = ColorUtility.getRGBAf(firstColor);
        float[] c1 = ColorUtility.getRGBAf(secondColor);
        GlStateManager.color(0, 0, 0, 0);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        CIRCLE.useProgram();
        CIRCLE.setupUniform2f("pos", x * 2 - radius, ((Minecraft.getMinecraft().displayHeight - (radius * 2)) - (y * 2)) + radius - 1);
        CIRCLE.setupUniform1f("radius", radius);
        CIRCLE.setupUniform1f("radialSmoothness", 1.0f);
        CIRCLE.setupUniform1f("borderThickness", borderThickness);
        CIRCLE.setupUniform1f("progress", progress);
        CIRCLE.setupUniform1i("change", change);
        CIRCLE.setupUniform4f("firstColor", c[0], c[1], c[2], c[3]);
        CIRCLE.setupUniform4f("secondColor", c1[0], c1[1], c1[2], c1[3]);
        CIRCLE.setupUniform2f("gradient", 0.2f, 2f);
        allocTextureRectangle(0, 0, mc.displayWidth, mc.displayHeight);
        CIRCLE.unloadProgram();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public static void drawImage(ResourceLocation tex, float x, float y, float x2, float y2) {
        mc.getTextureManager().bindTexture(tex);
        GlStateManager.color(1, 1, 1, 1);
        allocTextureRectangle(x, y, x2, y2);
        GlStateManager.bindTexture(0);

    }

    public static void drawRoundedTexture(ResourceLocation tex,float x, float y, float x2, float y2, float round, float alpha) {
        GlStateManager.color(0, 0, 0, 0);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableAlpha();
        ROUNDED_TEXTURE.useProgram();
        ROUNDED_TEXTURE.setupUniform2f("size", (x2 - round) * 2, (y2 - round) * 2);
        ROUNDED_TEXTURE.setupUniform1f("round", round);
        ROUNDED_TEXTURE.setupUniform1f("alpha", alpha);
        drawImage(tex,x,y,x2,y2);
        ROUNDED_TEXTURE.unloadProgram();

        GlStateManager.disableBlend();
    }

    public static void allocTextureRectangle(float x, float y, float width, float height) {
        if (mc.gameSettings.ofFastRender) return;
        BUILDER.begin(7, DefaultVertexFormats.POSITION_TEX);
        BUILDER.pos(x, y, 0).tex(0, 0).endVertex();
        BUILDER.pos(x, y + height, 0).tex(0, 1).endVertex();
        BUILDER.pos(x + width, y + height, 0).tex(1, 1).endVertex();
        BUILDER.pos(x + width, y, 0).tex(1, 0).endVertex();
        TESSELLATOR.draw();
    }

    public static void drawRect(float x, float y, float width, float height, int color) {
        drawGradientRect(x, y, width, height, color, color, color, color);
    }

    public static void drawCRect(float x, float y, float width, float height, int color) {
        drawGradientRect(x, y, width - x, height, color, color, color, color);
    }

    public static void drawGradientRect(float x, float y, float width, float height, int color1, int color2, int color3, int color4) {
        float[] c1 = ColorUtility.getRGBAf(color1);
        float[] c2 = ColorUtility.getRGBAf(color2);
        float[] c3 = ColorUtility.getRGBAf(color3);
        float[] c4 = ColorUtility.getRGBAf(color4);

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        BUILDER.begin(7, DefaultVertexFormats.POSITION_COLOR);
        BUILDER.pos(x, height + y, 0.0D).color(c1[0], c1[1], c1[2], c1[3]).endVertex();
        BUILDER.pos(width + x, height + y, 0.0D).color(c2[0], c2[1], c2[2], c2[3]).endVertex();
        BUILDER.pos(width + x, y, 0.0D).color(c3[0], c3[1], c3[2], c3[3]).endVertex();
        BUILDER.pos(x, y, 0.0D).color(c4[0], c4[1], c4[2], c4[3]).endVertex();
        TESSELLATOR.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawTriangle(float x, float z, float distance, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        float pt = mc.getRenderPartialTicks();
        float playerX = (float) (mc.player.prevPosX + (mc.player.posX - mc.player.prevPosX) * pt);
        float playerZ = (float) (mc.player.prevPosZ + (mc.player.posZ - mc.player.prevPosZ) * pt);
        float playerYaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * pt;
        float radian = (float) (Math.atan2(z - playerZ, x - playerX) - Math.toRadians(playerYaw + 180));
        float degree = (float) Math.toDegrees(radian);
        float cos = MathHelper.cos(radian);
        float sin = MathHelper.sin(radian);
        float centerX = mc.displayWidth / 4f;
        float centerY = mc.displayHeight / 4f;
        GlStateManager.enableBlend();
        GlStateManager.pushMatrix();
        GlStateManager.translate(centerX + distance * cos, centerY + distance * sin, 0);
        GlStateManager.rotate(degree + 90, 0, 0, 1);
        float[] colors = ColorUtility.getRGBAf(color);
        float width = 6, height = 12;
        GlStateManager.disableTexture2D();
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        BUILDER.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
        BUILDER.pos(0, 0 - height, 0).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        BUILDER.pos(0 - width, 0, 0).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        BUILDER.pos(0, -3, 0).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        float r = Math.max(colors[0] - 0.1f, 0);
        float g = Math.max(colors[1] - 0.1f, 0);
        float b = Math.max(colors[2] - 0.1f, 0);
        BUILDER.pos(0, 0 - height, 0).color(r, g, b, colors[3]).endVertex();
        BUILDER.pos(0, -3, 0).color(r, g, b, colors[3]).endVertex();
        BUILDER.pos(0 + width, 0, 0).color(r, g, b, colors[3]).endVertex();
        TESSELLATOR.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedGradientRect(int i, int i1, int i2, int i3, int i4, int i5, String main, String textMain) {
    }
}
