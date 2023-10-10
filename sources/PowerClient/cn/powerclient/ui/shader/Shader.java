/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.shader;

import me.AveReborn.ui.shader.ShaderLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;

public class Shader {
    private Framebuffer frameBuffer;
    private ShaderLoader clientShader;
    private static Minecraft mc = Minecraft.getMinecraft();
    private String fragmentShader;

    public Shader(String fragmentShader) {
        this.fragmentShader = fragmentShader;
    }

    public void startShader() {
        ScaledResolution sr2;
        if (Shader.mc.gameSettings.guiScale != 2 && Shader.mc.currentScreen == null) {
            Shader.mc.gameSettings.guiScale = 2;
        }
        if (this.frameBuffer == null) {
            this.frameBuffer = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, false);
        } else if (this.frameBuffer.framebufferWidth != Minecraft.getMinecraft().displayWidth || this.frameBuffer.framebufferHeight != Minecraft.getMinecraft().displayHeight) {
            this.frameBuffer.unbindFramebuffer();
            this.frameBuffer = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, false);
            if (this.clientShader != null) {
                this.clientShader.delete();
                sr2 = new ScaledResolution(mc);
                this.clientShader = new ShaderLoader(this.fragmentShader, this.frameBuffer.framebufferTexture);
            }
        }
        if (this.clientShader == null) {
            sr2 = new ScaledResolution(mc);
            this.clientShader = new ShaderLoader(this.fragmentShader, this.frameBuffer.framebufferTexture);
        }
        GL11.glMatrixMode(5888);
        RenderHelper.enableStandardItemLighting();
        this.frameBuffer.bindFramebuffer(false);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClear(16640);
    }

    public void stopShader() {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.clientShader.update();
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        ScaledResolution sr2 = new ScaledResolution(mc);
        GL11.glEnable(3553);
        GL11.glBindTexture(3553, this.clientShader.getFboTextureID());
        GL11.glBegin(4);
        GL11.glTexCoord2d(0.0, 1.0);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glTexCoord2d(0.0, 0.0);
        double x2 = 0.0;
        double y2 = 0.0;
        double width = sr2.getScaledWidth();
        double height = sr2.getScaledHeight();
        GL11.glVertex2d(x2, y2 + height * 2.0);
        GL11.glTexCoord2d(1.0, 0.0);
        GL11.glVertex2d(x2 + width * 2.0, y2 + height * 2.0);
        GL11.glTexCoord2d(1.0, 0.0);
        GL11.glVertex2d(x2 + width * 2.0, y2 + height * 2.0);
        GL11.glTexCoord2d(1.0, 1.0);
        GL11.glVertex2d(x2 + width * 2.0, y2);
        GL11.glTexCoord2d(0.0, 1.0);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glEnd();
    }

    public void deleteShader() {
        try {
            this.clientShader.delete();
            this.frameBuffer.unbindFramebuffer();
            this.frameBuffer.unbindFramebufferTexture();
            this.clientShader = null;
            this.frameBuffer = null;
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}

