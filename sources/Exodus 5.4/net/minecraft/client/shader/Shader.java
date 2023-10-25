/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.util.vector.Matrix4f
 */
package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderManager;
import net.minecraft.client.util.JsonException;
import org.lwjgl.util.vector.Matrix4f;

public class Shader {
    private final List<Integer> listAuxHeights;
    private final List<String> listAuxNames;
    public final Framebuffer framebufferIn;
    private final ShaderManager manager;
    public final Framebuffer framebufferOut;
    private final List<Object> listAuxFramebuffers = Lists.newArrayList();
    private final List<Integer> listAuxWidths;
    private Matrix4f projectionMatrix;

    public void addAuxFramebuffer(String string, Object object, int n, int n2) {
        this.listAuxNames.add(this.listAuxNames.size(), string);
        this.listAuxFramebuffers.add(this.listAuxFramebuffers.size(), object);
        this.listAuxWidths.add(this.listAuxWidths.size(), n);
        this.listAuxHeights.add(this.listAuxHeights.size(), n2);
    }

    public Shader(IResourceManager iResourceManager, String string, Framebuffer framebuffer, Framebuffer framebuffer2) throws IOException, JsonException {
        this.listAuxNames = Lists.newArrayList();
        this.listAuxWidths = Lists.newArrayList();
        this.listAuxHeights = Lists.newArrayList();
        this.manager = new ShaderManager(iResourceManager, string);
        this.framebufferIn = framebuffer;
        this.framebufferOut = framebuffer2;
    }

    public void loadShader(float f) {
        this.preLoadShader();
        this.framebufferIn.unbindFramebuffer();
        float f2 = this.framebufferOut.framebufferTextureWidth;
        float f3 = this.framebufferOut.framebufferTextureHeight;
        GlStateManager.viewport(0, 0, (int)f2, (int)f3);
        this.manager.addSamplerTexture("DiffuseSampler", this.framebufferIn);
        int n = 0;
        while (n < this.listAuxFramebuffers.size()) {
            this.manager.addSamplerTexture(this.listAuxNames.get(n), this.listAuxFramebuffers.get(n));
            this.manager.getShaderUniformOrDefault("AuxSize" + n).set(this.listAuxWidths.get(n).intValue(), this.listAuxHeights.get(n).intValue());
            ++n;
        }
        this.manager.getShaderUniformOrDefault("ProjMat").set(this.projectionMatrix);
        this.manager.getShaderUniformOrDefault("InSize").set(this.framebufferIn.framebufferTextureWidth, this.framebufferIn.framebufferTextureHeight);
        this.manager.getShaderUniformOrDefault("OutSize").set(f2, f3);
        this.manager.getShaderUniformOrDefault("Time").set(f);
        Minecraft minecraft = Minecraft.getMinecraft();
        this.manager.getShaderUniformOrDefault("ScreenSize").set(minecraft.displayWidth, Minecraft.displayHeight);
        this.manager.useShader();
        this.framebufferOut.framebufferClear();
        this.framebufferOut.bindFramebuffer(false);
        GlStateManager.depthMask(false);
        GlStateManager.colorMask(true, true, true, true);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(0.0, f3, 500.0).color(255, 255, 255, 255).endVertex();
        worldRenderer.pos(f2, f3, 500.0).color(255, 255, 255, 255).endVertex();
        worldRenderer.pos(f2, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
        worldRenderer.pos(0.0, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        this.manager.endShader();
        this.framebufferOut.unbindFramebuffer();
        this.framebufferIn.unbindFramebufferTexture();
        for (Object object : this.listAuxFramebuffers) {
            if (!(object instanceof Framebuffer)) continue;
            ((Framebuffer)object).unbindFramebufferTexture();
        }
    }

    public void setProjectionMatrix(Matrix4f matrix4f) {
        this.projectionMatrix = matrix4f;
    }

    public ShaderManager getShaderManager() {
        return this.manager;
    }

    public void deleteShader() {
        this.manager.deleteShader();
    }

    private void preLoadShader() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.disableFog();
        GlStateManager.disableLighting();
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(0);
    }
}

