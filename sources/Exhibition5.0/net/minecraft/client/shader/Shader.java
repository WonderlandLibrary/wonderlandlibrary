// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.shader;

import java.util.Iterator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.JsonException;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.IResourceManager;
import javax.vecmath.Matrix4f;
import java.util.List;

public class Shader
{
    private final ShaderManager manager;
    public final Framebuffer framebufferIn;
    public final Framebuffer framebufferOut;
    private final List listAuxFramebuffers;
    private final List listAuxNames;
    private final List listAuxWidths;
    private final List listAuxHeights;
    private Matrix4f projectionMatrix;
    private static final String __OBFID = "CL_00001042";
    
    public Shader(final IResourceManager p_i45089_1_, final String p_i45089_2_, final Framebuffer p_i45089_3_, final Framebuffer p_i45089_4_) throws JsonException {
        this.listAuxFramebuffers = Lists.newArrayList();
        this.listAuxNames = Lists.newArrayList();
        this.listAuxWidths = Lists.newArrayList();
        this.listAuxHeights = Lists.newArrayList();
        this.manager = new ShaderManager(p_i45089_1_, p_i45089_2_);
        this.framebufferIn = p_i45089_3_;
        this.framebufferOut = p_i45089_4_;
    }
    
    public void deleteShader() {
        this.manager.deleteShader();
    }
    
    public void addAuxFramebuffer(final String p_148041_1_, final Object p_148041_2_, final int p_148041_3_, final int p_148041_4_) {
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
        GlStateManager.enableTextures();
        GlStateManager.func_179144_i(0);
    }
    
    public void setProjectionMatrix(final Matrix4f p_148045_1_) {
        this.projectionMatrix = p_148045_1_;
    }
    
    public void loadShader(final float p_148042_1_) {
        this.preLoadShader();
        this.framebufferIn.unbindFramebuffer();
        final float var2 = this.framebufferOut.framebufferTextureWidth;
        final float var3 = this.framebufferOut.framebufferTextureHeight;
        GlStateManager.viewport(0, 0, (int)var2, (int)var3);
        this.manager.addSamplerTexture("DiffuseSampler", this.framebufferIn);
        for (int var4 = 0; var4 < this.listAuxFramebuffers.size(); ++var4) {
            this.manager.addSamplerTexture(this.listAuxNames.get(var4), this.listAuxFramebuffers.get(var4));
            this.manager.getShaderUniformOrDefault("AuxSize" + var4).set(this.listAuxWidths.get(var4), this.listAuxHeights.get(var4));
        }
        this.manager.getShaderUniformOrDefault("ProjMat").set(this.projectionMatrix);
        this.manager.getShaderUniformOrDefault("InSize").set(this.framebufferIn.framebufferTextureWidth, this.framebufferIn.framebufferTextureHeight);
        this.manager.getShaderUniformOrDefault("OutSize").set(var2, var3);
        this.manager.getShaderUniformOrDefault("Time").set(p_148042_1_);
        final Minecraft var5 = Minecraft.getMinecraft();
        this.manager.getShaderUniformOrDefault("ScreenSize").set(var5.displayWidth, var5.displayHeight);
        this.manager.useShader();
        this.framebufferOut.framebufferClear();
        this.framebufferOut.bindFramebuffer(false);
        GlStateManager.depthMask(false);
        GlStateManager.colorMask(true, true, true, true);
        final Tessellator var6 = Tessellator.getInstance();
        final WorldRenderer var7 = var6.getWorldRenderer();
        var7.startDrawingQuads();
        var7.func_178991_c(-1);
        var7.addVertex(0.0, var3, 500.0);
        var7.addVertex(var2, var3, 500.0);
        var7.addVertex(var2, 0.0, 500.0);
        var7.addVertex(0.0, 0.0, 500.0);
        var6.draw();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        this.manager.endShader();
        this.framebufferOut.unbindFramebuffer();
        this.framebufferIn.unbindFramebufferTexture();
        for (final Object var9 : this.listAuxFramebuffers) {
            if (var9 instanceof Framebuffer) {
                ((Framebuffer)var9).unbindFramebufferTexture();
            }
        }
    }
    
    public ShaderManager getShaderManager() {
        return this.manager;
    }
}
