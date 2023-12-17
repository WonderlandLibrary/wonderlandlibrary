// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.shader;

import java.util.Iterator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import net.minecraft.client.util.JsonException;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.IResourceManager;
import org.lwjgl.util.vector.Matrix4f;
import java.util.List;

public class Shader
{
    private final ShaderManager manager;
    public final Framebuffer framebufferIn;
    public final Framebuffer framebufferOut;
    private final List<Object> listAuxFramebuffers;
    private final List<String> listAuxNames;
    private final List<Integer> listAuxWidths;
    private final List<Integer> listAuxHeights;
    private Matrix4f projectionMatrix;
    
    public Shader(final IResourceManager resourceManager, final String programName, final Framebuffer framebufferInIn, final Framebuffer framebufferOutIn) throws JsonException, IOException {
        this.listAuxFramebuffers = (List<Object>)Lists.newArrayList();
        this.listAuxNames = (List<String>)Lists.newArrayList();
        this.listAuxWidths = (List<Integer>)Lists.newArrayList();
        this.listAuxHeights = (List<Integer>)Lists.newArrayList();
        this.manager = new ShaderManager(resourceManager, programName);
        this.framebufferIn = framebufferInIn;
        this.framebufferOut = framebufferOutIn;
    }
    
    public void deleteShader() {
        this.manager.deleteShader();
    }
    
    public void addAuxFramebuffer(final String auxName, final Object auxFramebufferIn, final int width, final int height) {
        this.listAuxNames.add(this.listAuxNames.size(), auxName);
        this.listAuxFramebuffers.add(this.listAuxFramebuffers.size(), auxFramebufferIn);
        this.listAuxWidths.add(this.listAuxWidths.size(), width);
        this.listAuxHeights.add(this.listAuxHeights.size(), height);
    }
    
    private void preRender() {
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
    
    public void setProjectionMatrix(final Matrix4f projectionMatrixIn) {
        this.projectionMatrix = projectionMatrixIn;
    }
    
    public void render(final float partialTicks) {
        this.preRender();
        this.framebufferIn.unbindFramebuffer();
        final float f = (float)this.framebufferOut.framebufferTextureWidth;
        final float f2 = (float)this.framebufferOut.framebufferTextureHeight;
        GlStateManager.viewport(0, 0, (int)f, (int)f2);
        this.manager.addSamplerTexture("DiffuseSampler", this.framebufferIn);
        for (int i = 0; i < this.listAuxFramebuffers.size(); ++i) {
            this.manager.addSamplerTexture(this.listAuxNames.get(i), this.listAuxFramebuffers.get(i));
            this.manager.getShaderUniformOrDefault("AuxSize" + i).set(this.listAuxWidths.get(i), this.listAuxHeights.get(i));
        }
        this.manager.getShaderUniformOrDefault("ProjMat").set(this.projectionMatrix);
        this.manager.getShaderUniformOrDefault("InSize").set((float)this.framebufferIn.framebufferTextureWidth, (float)this.framebufferIn.framebufferTextureHeight);
        this.manager.getShaderUniformOrDefault("OutSize").set(f, f2);
        this.manager.getShaderUniformOrDefault("Time").set(partialTicks);
        final Minecraft minecraft = Minecraft.getMinecraft();
        this.manager.getShaderUniformOrDefault("ScreenSize").set((float)minecraft.displayWidth, (float)minecraft.displayHeight);
        this.manager.useShader();
        this.framebufferOut.framebufferClear();
        this.framebufferOut.bindFramebuffer(false);
        GlStateManager.depthMask(false);
        GlStateManager.colorMask(true, true, true, true);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(0.0, f2, 500.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(f, f2, 500.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(f, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        this.manager.endShader();
        this.framebufferOut.unbindFramebuffer();
        this.framebufferIn.unbindFramebufferTexture();
        for (final Object object : this.listAuxFramebuffers) {
            if (object instanceof Framebuffer) {
                ((Framebuffer)object).unbindFramebufferTexture();
            }
        }
    }
    
    public ShaderManager getShaderManager() {
        return this.manager;
    }
}
