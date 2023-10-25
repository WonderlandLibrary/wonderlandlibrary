/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.render.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import org.celestial.client.helpers.render.shader.ShaderHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class FramebufferShader
extends ShaderHelper {
    public Minecraft mc = Minecraft.getMinecraft();
    public static Framebuffer framebuffer;
    public float red;
    public float green;
    public float blue;
    public float alpha = Float.intBitsToFloat(Float.floatToIntBits(14.2064905f) ^ 0x7EE34DC9);
    public float radius = Float.intBitsToFloat(Float.floatToIntBits(0.8626796f) ^ 0x7F5CD892);
    public float quality = Float.intBitsToFloat(Float.floatToIntBits(15.053767f) ^ 0x7EF0DC3B);
    public boolean entityShadows;

    public FramebufferShader(String fragmentShader) {
        super(fragmentShader);
    }

    public void startDraw(float partialTicks) {
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        framebuffer = this.setupFrameBuffer(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        this.entityShadows = this.mc.gameSettings.entityShadows;
        this.mc.gameSettings.entityShadows = false;
        this.mc.entityRenderer.setupCameraTransform(partialTicks, 0);
    }

    public void stopDraw(float red, float green, float blue, float alpha, float radius, float quality) {
        this.mc.gameSettings.entityShadows = this.entityShadows;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.mc.getFramebuffer().bindFramebuffer(true);
        this.red = red / Float.intBitsToFloat(Float.floatToIntBits(0.0318851f) ^ 0x7E7D99F3);
        this.green = green / Float.intBitsToFloat(Float.floatToIntBits(0.010246678f) ^ 0x7F58E1AF);
        this.blue = blue / Float.intBitsToFloat(Float.floatToIntBits(0.010522887f) ^ 0x7F536830);
        this.alpha = alpha / Float.intBitsToFloat(Float.floatToIntBits(0.05317917f) ^ 0x7E26D267);
        this.radius = radius;
        this.quality = quality;
        this.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        this.startShader();
        this.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(framebuffer);
        this.stopShader();
        this.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public Framebuffer setupFrameBuffer(Framebuffer frameBuffer) {
        if (frameBuffer != null) {
            frameBuffer.deleteFramebuffer();
        }
        frameBuffer = new Framebuffer(this.mc.displayWidth, this.mc.displayHeight, true);
        return frameBuffer;
    }

    public void drawFramebuffer(Framebuffer framebuffer) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        GL11.glBindTexture(3553, framebuffer.framebufferTexture);
        GL11.glBegin(7);
        GL11.glTexCoord2d(Double.longBitsToDouble(Double.doubleToLongBits(1.7921236082576344E308) ^ 0x7FEFE69EB44D9FE1L), Double.longBitsToDouble(Double.doubleToLongBits(4.899133169559449) ^ 0x7FE398B65D9806D1L));
        GL11.glVertex2d(Double.longBitsToDouble(Double.doubleToLongBits(3.7307361562967813E307) ^ 0x7FCA9050299687CBL), Double.longBitsToDouble(Double.doubleToLongBits(7.56781900945177E307) ^ 0x7FDAF13C89C9BE29L));
        GL11.glTexCoord2d(Double.longBitsToDouble(Double.doubleToLongBits(1.0409447193540338E308) ^ 0x7FE28788CB57BFECL), Double.longBitsToDouble(Double.doubleToLongBits(4.140164300258766E307) ^ 0x7FCD7A9C5BA7C45BL));
        GL11.glVertex2d(Double.longBitsToDouble(Double.doubleToLongBits(1.3989301333159067E308) ^ 0x7FE8E6DB3F70C542L), scaledResolution.getScaledHeight());
        GL11.glTexCoord2d(Double.longBitsToDouble(Double.doubleToLongBits(52.314008345000495) ^ 0x7FBA28316CEA395FL), Double.longBitsToDouble(Double.doubleToLongBits(1.3534831910786353E308) ^ 0x7FE817C1C68E7C69L));
        GL11.glVertex2d(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        GL11.glTexCoord2d(Double.longBitsToDouble(Double.doubleToLongBits(4.557588341026122) ^ 0x7FE23AF870255A34L), Double.longBitsToDouble(Double.doubleToLongBits(23.337335758793085) ^ 0x7FC7565BA2E3C9A3L));
        GL11.glVertex2d(scaledResolution.getScaledWidth(), Double.longBitsToDouble(Double.doubleToLongBits(1.5123382114342209E308) ^ 0x7FEAEBA6CA1CFB74L));
        GL11.glEnd();
        GL20.glUseProgram(0);
    }
}

