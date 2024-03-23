package dev.zprestige.prestige.client.util.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zprestige.prestige.client.shader.impl.GradientGlowShader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

//#SKIDDED
public class RenderUtil {

    public static GradientGlowShader shader;

    public static void renderItem(ItemStack itemStack, float f, float f2, float f3, boolean bl) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        RenderSystem.disableDepthTest();
        matrixStack.push();
        matrixStack.scale(f3, f3, 1);
        RenderHelper.getContext().drawItem(itemStack, (int)(f / f3), (int)(f2 / f3));
        if (bl) {
            RenderHelper.getContext().drawItemInSlot(MinecraftClient.getInstance().textRenderer, itemStack, (int)(f / f3), (int)(f2 / f3));
        }
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.enableDepthTest();
    }

    public static void renderCircleOutline(float f, float f2, float f3, Color color) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        for (int i = 0; i < 360; i++) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)(f + Math.sin(i * Math.PI / 180) * f3), (float)(f2 + Math.cos(i * Math.PI / 180) * f3), 0).next();
        }
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, 0).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderTexturedRect(float f, float f2, float f3, float f4, Identifier identifier, Color color) {
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        renderTexturedQuad(f, f2, f3, f4, identifier);
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    public static void renderTexturedQuad(float f, float f2, float f3, float f4, Identifier identifier) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrixStack.translate(f, f2, 0.0);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, identifier);
        RenderSystem.enableDepthTest();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), 0, 0, 0).texture(0, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), 0, f4, 0).texture(0, 1).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f4, 0).texture(1, 1).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, 0, 0).texture(1, 0).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderColoredQuad(float f, float f2, float f3, float f4, Color color) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f4, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f4, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f2, 0).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderTexturedQuad(Identifier identifier, float f, float f2, float f3, float f4, float f5, int n, int n2, int n3, int n4) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, identifier);
        RenderSystem.enableDepthTest();
        renderTexturedQuad(f, f + (float)n, f2, f2 + (float)n2, f3, n, n2, f4, f5, n3, n4);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        matrixStack.pop();
    }

    public static void renderTexturedQuad(float f, float f2, float f3, float f4, float f5, int n, int n2, float f6, float f7, int n3, int n4) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        renderTexturedQuad(matrixStack.peek().getPositionMatrix(), f, f2, f3, f4, f5, f6 / (float)n3, (f6 + (float)n) / (float)n3, f7 / (float)n4, (f7 + (float)n2) / (float)n4);
    }

    private static void renderTexturedQuad(Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, f, f4, f5).texture(f6, f9).next();
        bufferBuilder.vertex(matrix4f, f2, f4, f5).texture(f7, f9).next();
        bufferBuilder.vertex(matrix4f, f2, f3, f5).texture(f7, f8).next();
        bufferBuilder.vertex(matrix4f, f, f3, f5).texture(f6, f8).next();
        bufferBuilder.clear();
        tessellator.draw();
    }

    public static void renderColoredQuad(float f, float f2, float f3, float f4, boolean bl, boolean bl2, boolean bl3, boolean bl4, float f5) {
        Color color = new Color(0, 0, 0, 0);
        Color color2 = new Color(0, 0, 0, f5);
        renderColoredQuad(f, f2, f3, f4, bl ? color2 : color, bl2 ? color2 : color, bl3 ? color2 : color, bl4 ? color2 : color);
    }

    public static void renderColoredQuad(float f, float f2, float f3, float f4, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, 0).color((float)topLeft.getRed() / 255, (float)topLeft.getGreen() / 255, (float)topLeft.getBlue() / 255, (float)topLeft.getAlpha() / 255).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f4, 0).color((float)bottomLeft.getRed() / 255, (float)bottomLeft.getGreen() / 255, (float)bottomLeft.getBlue() / 255, (float)bottomLeft.getAlpha() / 255).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f4, 0).color((float)bottomRight.getRed() / 255, (float)bottomRight.getGreen() / 255, (float)bottomRight.getBlue() / 255, (float)bottomRight.getAlpha() / 255).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f2, 0).color((float)topRight.getRed() / 255, (float)topRight.getGreen() / 255, (float)topRight.getBlue() / 255, (float)topRight.getAlpha() / 255).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderRoundedRectOutline(float f, float f2, float f3, float f4, Color color, float f5) {
        renderRoundedRect(f, f2, f3, f4, f5, color, VertexFormat.DrawMode.DEBUG_LINE_STRIP);
    }

    static void renderRoundedRect(float f, float f2, float f3, float f4, float f5, Color color, VertexFormat.DrawMode drawMode) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        double d = Math.PI;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(drawMode, VertexFormats.POSITION);
        for (int i = 0; i < 90; i++) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)((f + f5) + Math.sin(i * d / 180) * f5 * -1), (float)((f2 + f5) + Math.cos(i * d / 180) * f5 * -1), 0).next();
        }
        for (int i = 90; i < 180; i++) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)((f + f5) + Math.sin(i * d / 180) * f5 * (double)-1), (float)((f4 - f5) + Math.cos(i * d / 180) * f5 * -1), 0).next();
        }
        for (int i = 0; i < 90; i++) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)((f3 - f5) + Math.sin(i * d / 180) * f5), (float)((f4 - f5) + Math.cos(i * d / 180) * f5), 0).next();
        }
        for (int i = 90; i < 180; i++) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)((f3 - f5) + Math.sin(i * d / 180) * f5), (float)((f2 + f5) + Math.cos(i * d / 180) * f5), 0).next();
        }
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderColoredRoundedRect(float f, float f2, float f3, float f4, float f5, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight) {
        double d = Math.PI;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        GL11.glLineWidth(0.1f);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        for (int i = 0; i < 90; i++) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)((f + f5) + Math.sin(i * d / 180) * f5 * -1), (float)((f2 + f5) + Math.cos(i * d / 180) * f5 * -1), 0).color((float)topLeft.getRed() / 255, (float)topLeft.getGreen() / 255, (float)topLeft.getBlue() / 255, (float)topLeft.getAlpha() / 255).next();
        }
        for (int i = 90; i < 180; i++) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)((f + f5) + Math.sin(i * d / 180) * f5 * (double)-1), (float)((f4 - f5) + Math.cos(i * d / 180) * f5 * -1), 0).color((float)bottomLeft.getRed() / 255, (float)bottomLeft.getGreen() / 255, (float)bottomLeft.getBlue() / 255, (float)bottomLeft.getAlpha() / 255).next();
        }
        for (int i = 0; i < 90; i++) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)((f3 - f5) + Math.sin(i * d / 180) * f5), (float)((f4 - f5) + Math.cos(i * d / 180) * f5), 0).color((float)bottomRight.getRed() / 255, (float)bottomRight.getGreen() / 255, (float)bottomRight.getBlue() / 255, (float)bottomRight.getAlpha() / 255).next();
        }
        for (int i = 90; i < 180; i++) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)((f3 - f5) + Math.sin(i * d / 180) * f5), (float)((f2 + f5) + Math.cos(i * d / 180) * f5), 0).color((float)topRight.getRed() / 255, (float)topRight.getGreen() / 255, (float)topRight.getBlue() / 255, (float)topRight.getAlpha() / 255).next();
        }
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderGradient(float f, float f2, float f3, float f4, Color color, float f5) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Color color2 = new Color(1, 1, 1, f5);
        Color color3 = new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, f5);
        Color color4 = new Color(0, 0, 0, 0);
        Color color5 = new Color(0, 0, 0, f5);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, 0).color((float)color2.getRed() / 255, (float)color2.getGreen() / 255, (float)color2.getBlue() / 255, (float)color2.getAlpha() / 255).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f4, 0).color((float)color2.getRed() / 255, (float)color2.getGreen() / 255, (float)color2.getBlue() / 255, (float)color2.getAlpha() / 255).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f4, 0).color((float)color3.getRed() / 255, (float)color3.getGreen() / 255, (float)color3.getBlue() / 255, (float)color3.getAlpha() / 255).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f2, 0).color((float)color3.getRed() / 255, (float)color3.getGreen() / 255, (float)color3.getBlue() / 255, (float)color3.getAlpha() / 255).next();
        tessellator.draw();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, 0).color((float)color4.getRed() / 255, (float)color4.getGreen() / 255, (float)color4.getBlue() / 255, (float)color4.getAlpha() / 255).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f4, 0).color((float)color5.getRed() / 255, (float)color5.getGreen() / 255, (float)color5.getBlue() / 255, (float)color5.getAlpha() / 255).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f4, 0).color((float)color5.getRed() / 255, (float)color5.getGreen() / 255, (float)color5.getBlue() / 255, (float)color5.getAlpha() / 255).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f2, 0).color((float)color4.getRed() / 255, (float)color4.getGreen() / 255, (float)color4.getBlue() / 255, (float)color4.getAlpha() / 255).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderFilledCircle(float f, float f2, float f3, Color color) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
        for (int i = 0; i < 360; i++) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)(f + Math.sin(i * Math.PI / 180) * f3), (float)(f2 + Math.cos(i * Math.PI / 180) * f3), 0).next();
        }
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderCircularGradient(float f, float f2, float f3, float f4, Color color, float f5) {
        double d = Math.PI;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(0.1f);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        for (int i = 0; i < 90; i++) {
            bufferBuilder.vertex(f + f5 + Math.sin(i * d / 180.0) * f5 * -1.0, f2 + f5 + Math.cos(i * d / 180.0) * f5 * -1.0, 0.0).next();
        }
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f4, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f4, 0).next();
        for (int i = 90; i < 180; i++) {
            bufferBuilder.vertex(f3 - f5 + Math.sin(i * d / 180.0f) * f5, f2 + f5 + Math.cos(i * d / 180.0f) * f5, 0.0).next();
        }
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f + f5, f2, 0).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderCrossed(float f, float f2, float f3, float f4, Color color) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f4, 0).next();
        tessellator.draw();
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f4, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f2, 0).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    //I won't fix this kys
    public static void renderColoredEllipseBorder(float f, float f2, float f3, float f4, Color color, float f5) {
        double d = Math.PI;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(0.1f);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        for (int i = 0; i < 90; i++) {
            bufferBuilder.vertex(f + f5 + Math.sin(i * d / 180) * f5 * -1, f2 + f5 + Math.cos(i * d / 180) * f5 * -1, 0).next();
        }
        for (int i = 90; i < 180; i++) {
            bufferBuilder.vertex(f + f5 + Math.sin(i * d / 180) * f5 * -1, f4 - f5 + Math.cos(i * d / 180) * f5 * -1, 0).next();
        }
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f4, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f2, 0).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderColoredRectangleOutline(float f, float f2, float f3, float f4, Color color) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f4, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f4, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f3, f2, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, 0).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderArrows(float f, float f2, float f3, Color color) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f + 2.5f, f2 + 2.5f * f3, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f + 5.0f, f2, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f + 2.5f, f2 + 2.5f * f3, 0).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void renderShaderRect(MatrixStack matrixStack, Color color, Color color2, Color color3, Color color4, float f, float f2, float f3, float f4, float f5, float f6) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f - 10, f2 - 10, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f - 10, f2 + f4 + 20, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f + f3 + 20, f2 + f4 + 20, 0).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f + f3 + 20, f2 - 10, 0).next();
        shader.setParameters(f, f2, f3, f4, f5, f6, color, color2, color3, color4);
        shader.use();
        tessellator.draw();
        RenderSystem.disableBlend();
    }

    public static void renderRoundedRect(float f, float f2, float f3, float f4, Color color, float f5) {
        renderRoundedRect(f, f2, f3, f4, f5, color, VertexFormat.DrawMode.TRIANGLE_FAN);
    }

    public static void renderFilledBox(float f, float f2, float f3, float f4, float f5, float f6, Color color) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f5, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f5, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f5, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f2, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f2, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f5, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f2, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f2, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f5, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f5, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f5, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f6).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void renderOutlinedBox(float f, float f2, float f3, float f4, float f5, float f6, Color color) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f2, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f5, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f5, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f2, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f2, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f5, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f6).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f2, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f4, f5, f3).next();
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), f, f5, f3).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void renderColoredEllipse3D(float f, float f2, float f3, float f4, Color color) {
        double d = Math.PI * 2;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.defaultBlendFunc();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        for (int i = 0; i <= 100; ++i) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)(f + f4 * Math.cos(i * d / 100)), f2, (float)(f3 + f4 * Math.sin(i * d / 100))).next();
        }
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
    }

    public static void renderLines(ArrayList<Vec3d> arrayList, Color color) {
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.lineWidth(1);
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        for (Vec3d vec3d : arrayList) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), (float)vec3d.x, (float)vec3d.y, (float)vec3d.z).next();
        }
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void setScissorRegion(float f, float f2, float f3, float f4) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        int n;
        if (screen == null) {
            n = 0;
        } else {
            n = screen.height - (int)f4;
        }
        double d = MinecraftClient.getInstance().getWindow().getScaleFactor();
        GL11.glScissor((int)(f * d), (int)(n * d), (int)((f3 - f) * d), (int)((f4 - f2) * d));
        GL11.glEnable(3089);
    }

    public static void setCameraAction() {
        Camera camera = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().camera;
        if (camera != null) {
            MatrixStack matrixStack = RenderHelper.getMatrixStack();
            matrixStack.push();
            Vec3d vec3d = camera.getPos();
            matrixStack.translate(-vec3d.x, -vec3d.y, -vec3d.z);
        }
    }

    public static Color getThemeColor(Color color, int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f = Math.abs(((float)(System.currentTimeMillis() % 2000) / 1000 + (float)n / (float)n2 * 2) % 2 - 1);
        fArray[2] = 0.25f + 0.75f * f % 2;
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }

    public static Color getColor(int n, float f) {
        return new Color((float) (14 + n) / 255, (float) (14 + n) / 255, (float) (14 + n) / 255, MathHelper.clamp(f, 0, 1));
    }

    public static Color getColor(Color color, float f) {
        return new Color((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, MathHelper.clamp(f, 0, 1));
    }

    public static Color getColor(float f, float f2) {
        return new Color(f, f, f, MathHelper.clamp(f2, 0, 1));
    }

    public static Vec3d worldSpaceToScreenSpace(Vec3d pos) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera camera = mc.getEntityRenderDispatcher().camera;
        int displayHeight = mc.getWindow().getHeight();
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
        Vector3f target = new Vector3f();

        double deltaX = pos.x - camera.getPos().x;
        double deltaY = pos.y - camera.getPos().y;
        double deltaZ = pos.z - camera.getPos().z;

        Vector4f transformedCoordinates = new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, 1.f).mul(RenderHelper.getPositionMatrix());

        Matrix4f matrixProj = new Matrix4f(RenderHelper.getProjectionMatrix());
        Matrix4f matrixModel = new Matrix4f(RenderHelper.getModelViewMatrix());

        matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);

        return new Vec3d(target.x / mc.getWindow().getScaleFactor(), (displayHeight - target.y) / mc.getWindow().getScaleFactor(), target.z);
    }


    public static Vec3d getEntityPos(Entity entity) {
        double d = lerpTickDelta(entity.getX(), entity.prevX);
        double d2 = lerpTickDelta(entity.getY(), entity.prevY);
        double d3 = lerpTickDelta(entity.getZ(), entity.prevZ);
        return new Vec3d(d, d2, d3);
    }

    private static double lerpTickDelta(double d, double d2) {
        return d2 + (d - d2) * MinecraftClient.getInstance().getTickDelta();
    }
}
