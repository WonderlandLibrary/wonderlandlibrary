/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderManager;
import net.minecraft.client.util.JsonException;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.EntityESP;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
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

    public Shader(IResourceManager resourceManager, String programName, Framebuffer framebufferInIn, Framebuffer framebufferOutIn) throws JsonException, IOException {
        this.manager = new ShaderManager(resourceManager, programName);
        this.framebufferIn = framebufferInIn;
        this.framebufferOut = framebufferOutIn;
    }

    public void deleteShader() {
        this.manager.deleteShader();
    }

    public void addAuxFramebuffer(String auxName, Object auxFramebufferIn, int width, int height) {
        this.listAuxNames.add(this.listAuxNames.size(), auxName);
        this.listAuxFramebuffers.add(this.listAuxFramebuffers.size(), auxFramebufferIn);
        this.listAuxWidths.add(this.listAuxWidths.size(), width);
        this.listAuxHeights.add(this.listAuxHeights.size(), height);
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

    public void setProjectionMatrix(Matrix4f projectionMatrixIn) {
        this.projectionMatrix = projectionMatrixIn;
    }

    /*
     * WARNING - void declaration
     */
    public void loadShader(float p_148042_1_) {
        this.preLoadShader();
        this.framebufferIn.unbindFramebuffer();
        float f = this.framebufferOut.framebufferTextureWidth;
        float f1 = this.framebufferOut.framebufferTextureHeight;
        GlStateManager.viewport(0, 0, (int)f, (int)f1);
        this.manager.addSamplerTexture("DiffuseSampler", this.framebufferIn);
        for (int i = 0; i < this.listAuxFramebuffers.size(); ++i) {
            this.manager.addSamplerTexture(this.listAuxNames.get(i), this.listAuxFramebuffers.get(i));
            this.manager.getShaderUniformOrDefault("AuxSize" + i).set(this.listAuxWidths.get(i).intValue(), this.listAuxHeights.get(i).intValue());
        }
        this.manager.getShaderUniformOrDefault("ProjMat").set(this.projectionMatrix);
        this.manager.getShaderUniformOrDefault("InSize").set(this.framebufferIn.framebufferTextureWidth, this.framebufferIn.framebufferTextureHeight);
        this.manager.getShaderUniformOrDefault("OutSize").set(f, f1);
        this.manager.getShaderUniformOrDefault("Time").set(p_148042_1_);
        Minecraft minecraft = Minecraft.getMinecraft();
        this.manager.getShaderUniformOrDefault("ScreenSize").set(minecraft.displayWidth, minecraft.displayHeight);
        this.manager.useShader();
        if (Celestial.instance.featureManager.getFeatureByClass(EntityESP.class).getState() && EntityESP.glow.getCurrentValue()) {
            void var8_16;
            int oneColor = EntityESP.glowColor.getColor();
            int color = 0;
            String string = EntityESP.glowMode.currentMode;
            int n = -1;
            switch (string.hashCode()) {
                case 2021122027: {
                    if (!string.equals("Client")) break;
                    boolean bl = false;
                    break;
                }
                case 2029746065: {
                    if (!string.equals("Custom")) break;
                    boolean bl = true;
                    break;
                }
                case 961091784: {
                    if (!string.equals("Astolfo")) break;
                    int n2 = 2;
                    break;
                }
                case -1656737386: {
                    if (!string.equals("Rainbow")) break;
                    int n3 = 3;
                }
            }
            switch (var8_16) {
                case 0: {
                    color = ClientHelper.getClientColor().getRGB();
                    break;
                }
                case 1: {
                    color = oneColor;
                    break;
                }
                case 2: {
                    color = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                    break;
                }
                case 3: {
                    color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                }
            }
            if (this.manager.programFilename.equals("entity_outline")) {
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                GL13.glActiveTexture(33986);
                GL11.glBindTexture(3553, minecraft.getFramebuffer().framebufferTexture);
                ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(this.manager.getProgram(), "mc"), 2);
                ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(this.manager.getProgram(), "radius"), (int)EntityESP.glowRadius.getCurrentValue());
                ARBShaderObjects.glUniform3fARB(ARBShaderObjects.glGetUniformLocationARB(this.manager.getProgram(), "color"), (float)new Color(color).getRed() / 255.0f, (float)new Color(color).getBlue() / 255.0f, (float)new Color(color).getGreen() / 255.0f);
                GL13.glActiveTexture(33984);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();
            }
        }
        this.framebufferOut.framebufferClear();
        this.framebufferOut.bindFramebuffer(false);
        GlStateManager.depthMask(false);
        GlStateManager.colorMask(true, true, true, true);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(0.0, f1, 500.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(f, f1, 500.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(f, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        this.manager.endShader();
        this.framebufferOut.unbindFramebuffer();
        this.framebufferIn.unbindFramebufferTexture();
        for (Object e : this.listAuxFramebuffers) {
            if (!(e instanceof Framebuffer)) continue;
            ((Framebuffer)e).unbindFramebufferTexture();
        }
    }

    public ShaderManager getShaderManager() {
        return this.manager;
    }
}

