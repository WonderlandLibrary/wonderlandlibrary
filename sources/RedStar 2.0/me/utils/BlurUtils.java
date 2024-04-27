package me.utils;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

public class BlurUtils {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static ShaderGroup shaderGroup;
    private static Framebuffer frbuffer;
    private static Framebuffer framebuffer;
    private static Framebuffer frameBuffer;
    private static int lastFactor;
    private static int lastWidth;
    private static int lastHeight;
    private static float lastX;
    private static float lastY;
    private static float lastW;
    private static float lastH;
    private static float lastStrength;
    private static ResourceLocation blurShader;

    public static void init() {
        try {
            shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), blurShader);
            shaderGroup.createBindFramebuffers(BlurUtils.mc.displayWidth, BlurUtils.mc.displayHeight);
            framebuffer = BlurUtils.shaderGroup.mainFramebuffer;
            frbuffer = shaderGroup.getFramebufferRaw("result");
        }
        catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void setValues(float strength, float x, float y, float w, float h, float width, float height) {
        if (strength == lastStrength && lastX == x && lastY == y && lastW == w && lastH == h) {
            return;
        }
        lastStrength = strength;
        lastX = x;
        lastY = y;
        lastW = w;
        lastH = h;
        for (int i = 0; i < 2; ++i) {
            ((Shader)BlurUtils.shaderGroup.listShaders.get(i)).getShaderManager().getShaderUniform("Radius").set(strength);
            ((Shader)BlurUtils.shaderGroup.listShaders.get(i)).getShaderManager().getShaderUniform("BlurXY").set(x, height - y - h);
            ((Shader)BlurUtils.shaderGroup.listShaders.get(i)).getShaderManager().getShaderUniform("BlurCoord").set(w, h);
        }
    }

    public static void blurArea(float x, float y, float x2, float y2, float blurStrength) {
        int height;
        int width;
        ScaledResolution scaledResolution;
        int scaleFactor;
        float z;
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return;
        }
        if (x > x2) {
            z = x;
            x = x2;
            x2 = z;
        }
        if (y > y2) {
            z = y;
            y2 = y = y2;
        }
        if (BlurUtils.sizeHasChanged(scaleFactor = (scaledResolution = new ScaledResolution(mc)).getScaleFactor(), width = scaledResolution.getScaledWidth(), height = scaledResolution.getScaledHeight()) || framebuffer == null || frbuffer == null || shaderGroup == null) {
            BlurUtils.init();
        }
        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;
        float _w = x2 - x;
        float _h = y2 - y;
        BlurUtils.setValues(blurStrength, x, y, _w, _h, width, height);
        framebuffer.bindFramebuffer(true);
        shaderGroup.render(BlurUtils.mc.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
        Stencil.write(false);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderUtils.quickDrawRect(x, y, x2, y2);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        Stencil.erase(true);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.pushMatrix();
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        GlStateManager.disableDepth();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        frbuffer.bindFramebufferTexture();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f = width;
        float f1 = height;
        float f2 = (float)BlurUtils.frbuffer.framebufferWidth / (float)BlurUtils.frbuffer.framebufferTextureWidth;
        float f3 = (float)BlurUtils.frbuffer.framebufferHeight / (float)BlurUtils.frbuffer.framebufferTextureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferrender = tessellator.getBuffer();
        bufferrender.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferrender.pos(0.0, (double)f1, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        bufferrender.pos((double)f, (double)f1, 0.0).tex((double)f2, 0.0).color(255, 255, 255, 255).endVertex();
        bufferrender.pos((double)f, 0.0, 0.0).tex((double)f2, (double)f3).color(255, 255, 255, 255).endVertex();
        bufferrender.pos(0.0, 0.0, 0.0).tex(0.0, (double)f3).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        frbuffer.unbindFramebufferTexture();
        GlStateManager.enableDepth();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        Stencil.dispose();
        GlStateManager.enableAlpha();
    }

    public static void preCustomBlur(float blurStrength, float x, float y, float x2, float y2, boolean renderClipLayer) {
        int height;
        int width;
        ScaledResolution scaledResolution;
        int scaleFactor;
        float z;
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return;
        }
        if (x > x2) {
            z = x;
            x = x2;
            x2 = z;
        }
        if (y > y2) {
            z = y;
            y2 = y = y2;
        }
        if (BlurUtils.sizeHasChanged(scaleFactor = (scaledResolution = new ScaledResolution(mc)).getScaleFactor(), width = scaledResolution.getScaledWidth(), height = scaledResolution.getScaledHeight()) || framebuffer == null || frbuffer == null || shaderGroup == null) {
            BlurUtils.init();
        }
        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;
        float _w = x2 - x;
        float _h = y2 - y;
        BlurUtils.setValues(blurStrength, x, y, _w, _h, width, height);
        framebuffer.bindFramebuffer(true);
        shaderGroup.render(BlurUtils.mc.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
        Stencil.write(renderClipLayer);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
    }

    public static void postCustomBlur() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        Stencil.erase(true);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.pushMatrix();
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        GlStateManager.disableDepth();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        frbuffer.bindFramebufferTexture();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f = width;
        float f1 = height;
        float f2 = (float)BlurUtils.frbuffer.framebufferWidth / (float)BlurUtils.frbuffer.framebufferTextureWidth;
        float f3 = (float)BlurUtils.frbuffer.framebufferHeight / (float)BlurUtils.frbuffer.framebufferTextureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, (double)f1, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)f, (double)f1, 0.0).tex((double)f2, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)f, 0.0, 0.0).tex((double)f2, (double)f3).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, (double)f3).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        frbuffer.unbindFramebufferTexture();
        GlStateManager.enableDepth();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        Stencil.dispose();
        GlStateManager.enableAlpha();
    }

    public static void blurAreaRounded(float x, float y, float x2, float y2, float rad, float blurStrength) {
        int height;
        int width;
        ScaledResolution scaledResolution;
        int scaleFactor;
        float z;
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return;
        }
        if (x > x2) {
            z = x;
            x = x2;
            x2 = z;
        }
        if (y > y2) {
            z = y;
            y2 = y = y2;
        }
        if (BlurUtils.sizeHasChanged(scaleFactor = (scaledResolution = new ScaledResolution(mc)).getScaleFactor(), width = scaledResolution.getScaledWidth(), height = scaledResolution.getScaledHeight()) || framebuffer == null || frbuffer == null || shaderGroup == null) {
            BlurUtils.init();
        }
        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;
        float _w = x2 - x;
        float _h = y2 - y;
        BlurUtils.setValues(blurStrength, x, y, _w, _h, width, height);
        framebuffer.bindFramebuffer(true);
        shaderGroup.render(BlurUtils.mc.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
        Stencil.write(false);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderUtils.fastRoundedRect(x, y, x2, y2, rad);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        Stencil.erase(true);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.pushMatrix();
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        GlStateManager.disableDepth();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        frbuffer.bindFramebufferTexture();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f = width;
        float f1 = height;
        float f2 = (float)BlurUtils.frbuffer.framebufferWidth / (float)BlurUtils.frbuffer.framebufferTextureWidth;
        float f3 = (float)BlurUtils.frbuffer.framebufferHeight / (float)BlurUtils.frbuffer.framebufferTextureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, (double)f1, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)f, (double)f1, 0.0).tex((double)f2, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)f, 0.0, 0.0).tex((double)f2, (double)f3).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, (double)f3).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        frbuffer.unbindFramebufferTexture();
        GlStateManager.enableDepth();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        Stencil.dispose();
        GlStateManager.enableAlpha();
    }

    private static boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return lastFactor != scaleFactor || lastWidth != width || lastHeight != height;
    }

    static {
        lastStrength = 5.0f;
        blurShader = new ResourceLocation("pride/blurarea.json");
    }
}
