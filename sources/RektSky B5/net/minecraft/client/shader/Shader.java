/*
 * Decompiled with CFR 0.152.
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
    private final ShaderManager manager;
    public final Framebuffer framebufferIn;
    public final Framebuffer framebufferOut;
    private final List<Object> listAuxFramebuffers = Lists.newArrayList();
    private final List<String> listAuxNames = Lists.newArrayList();
    private final List<Integer> listAuxWidths = Lists.newArrayList();
    private final List<Integer> listAuxHeights = Lists.newArrayList();
    private Matrix4f projectionMatrix;

    public Shader(IResourceManager p_i45089_1_, String p_i45089_2_, Framebuffer p_i45089_3_, Framebuffer p_i45089_4_) throws JsonException, IOException {
        this.manager = new ShaderManager(p_i45089_1_, p_i45089_2_);
        this.framebufferIn = p_i45089_3_;
        this.framebufferOut = p_i45089_4_;
    }

    public void deleteShader() {
        this.manager.deleteShader();
    }

    public void addAuxFramebuffer(String p_148041_1_, Object p_148041_2_, int p_148041_3_, int p_148041_4_) {
        this.listAuxNames.add(this.listAuxNames.size(), p_148041_1_);
        this.listAuxFramebuffers.add(this.listAuxFramebuffers.size(), p_148041_2_);
        this.listAuxWidths.add(this.listAuxWidths.size(), p_148041_3_);
        this.listAuxHeights.add(this.listAuxHeights.size(), p_148041_4_);
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

    public void setProjectionMatrix(Matrix4f p_148045_1_) {
        this.projectionMatrix = p_148045_1_;
    }

    public void loadShader(float p_148042_1_) {
        this.preLoadShader();
        this.framebufferIn.unbindFramebuffer();
        float f2 = this.framebufferOut.framebufferTextureWidth;
        float f1 = this.framebufferOut.framebufferTextureHeight;
        GlStateManager.viewport(0, 0, (int)f2, (int)f1);
        this.manager.addSamplerTexture("DiffuseSampler", this.framebufferIn);
        for (int i2 = 0; i2 < this.listAuxFramebuffers.size(); ++i2) {
            this.manager.addSamplerTexture(this.listAuxNames.get(i2), this.listAuxFramebuffers.get(i2));
            this.manager.getShaderUniformOrDefault("AuxSize" + i2).set(this.listAuxWidths.get(i2).intValue(), this.listAuxHeights.get(i2).intValue());
        }
        this.manager.getShaderUniformOrDefault("ProjMat").set(this.projectionMatrix);
        this.manager.getShaderUniformOrDefault("InSize").set(this.framebufferIn.framebufferTextureWidth, this.framebufferIn.framebufferTextureHeight);
        this.manager.getShaderUniformOrDefault("OutSize").set(f2, f1);
        this.manager.getShaderUniformOrDefault("Time").set(p_148042_1_);
        Minecraft minecraft = Minecraft.getMinecraft();
        this.manager.getShaderUniformOrDefault("ScreenSize").set(minecraft.displayWidth, minecraft.displayHeight);
        this.manager.useShader();
        this.framebufferOut.framebufferClear();
        this.framebufferOut.bindFramebuffer(false);
        GlStateManager.depthMask(false);
        GlStateManager.colorMask(true, true, true, true);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(0.0, f1, 500.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(f2, f1, 500.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(f2, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
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

    public ShaderManager getShaderManager() {
        return this.manager;
    }
}

