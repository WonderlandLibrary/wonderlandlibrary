/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.shader.Framebuffer
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils.render.shader;

import java.awt.Color;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.utils.render.shader.Shader;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class FramebufferShader
extends Shader {
    protected float red;
    private static Framebuffer framebuffer;
    protected float green;
    protected float alpha = 1.0f;
    protected float quality = 1.0f;
    private boolean entityShadows;
    protected float radius = 2.0f;
    protected float blue;

    public void drawFramebuffer(Framebuffer framebuffer) {
        IScaledResolution iScaledResolution = classProvider.createScaledResolution(mc);
        GL11.glBindTexture((int)3553, (int)framebuffer.field_147617_g);
        GL11.glBegin((int)7);
        GL11.glTexCoord2d((double)0.0, (double)1.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glTexCoord2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)iScaledResolution.getScaledHeight());
        GL11.glTexCoord2d((double)1.0, (double)0.0);
        GL11.glVertex2d((double)iScaledResolution.getScaledWidth(), (double)iScaledResolution.getScaledHeight());
        GL11.glTexCoord2d((double)1.0, (double)1.0);
        GL11.glVertex2d((double)iScaledResolution.getScaledWidth(), (double)0.0);
        GL11.glEnd();
        GL20.glUseProgram((int)0);
    }

    public Framebuffer setupFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer != null) {
            framebuffer.func_147608_a();
        }
        framebuffer = new Framebuffer(mc.getDisplayWidth(), mc.getDisplayHeight(), true);
        return framebuffer;
    }

    public void stopDraw(Color color, float f, float f2) {
        mc.getGameSettings().setEntityShadows(this.entityShadows);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        mc.getFramebuffer().bindFramebuffer(true);
        this.red = (float)color.getRed() / 255.0f;
        this.green = (float)color.getGreen() / 255.0f;
        this.blue = (float)color.getBlue() / 255.0f;
        this.alpha = (float)color.getAlpha() / 255.0f;
        this.radius = f;
        this.quality = f2;
        mc.getEntityRenderer().disableLightmap();
        RenderHelper.func_74518_a();
        this.startShader();
        mc.getEntityRenderer().setupOverlayRendering();
        this.drawFramebuffer(framebuffer);
        this.stopShader();
        mc.getEntityRenderer().disableLightmap();
        classProvider.getGlStateManager().popMatrix();
        classProvider.getGlStateManager().popAttrib();
    }

    public FramebufferShader(String string) {
        super(string);
    }

    public void startDraw(float f) {
        classProvider.getGlStateManager().enableAlpha();
        classProvider.getGlStateManager().pushMatrix();
        classProvider.getGlStateManager().pushAttrib();
        framebuffer = this.setupFrameBuffer(framebuffer);
        framebuffer.func_147614_f();
        framebuffer.func_147610_a(true);
        this.entityShadows = mc.getGameSettings().getEntityShadows();
        mc.getGameSettings().setEntityShadows(false);
        mc.getEntityRenderer().setupCameraTransform(f, 0);
    }
}

