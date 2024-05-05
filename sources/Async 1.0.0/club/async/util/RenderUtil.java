package club.async.util;

import club.async.interfaces.MinecraftInterface;
import club.async.util.glsl.GLSLSandboxShader;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;

public final class RenderUtil implements MinecraftInterface {

    public static long initTime;
    private static GLSLSandboxShader backgroundShader;

    public static void prepareScissorBox(double x, double y, double x2, double y2) {
        int factor = getScaledResolution().getScaleFactor();
        GL11.glScissor((int) (x * (float) factor), (int) (((float) getScaledResolution().getScaledHeight() - y2) * (float) factor), (int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
    }

    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        prepareScissorBox((double) x,(double) y,(double) x2,(double) y2);
    }

    public static ScaledResolution getScaledResolution() {
        return new ScaledResolution(mc);
    }

    public static void renderShader(int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        backgroundShader.useShader(getScaledResolution().getScaledWidth(), getScaledResolution().getScaledHeight(), mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);
        GL11.glEnd();
        // Unbind shader
        GL20.glUseProgram(0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static void initShader() {
        try {
            initTime = System.currentTimeMillis();
            backgroundShader = new GLSLSandboxShader();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load backgound shader", e);
        }
    }

    public static void drawLineGradientX(int x, int y, int x2, int y2, java.awt.Color color1, java.awt.Color color2, float div, boolean left) {
        int index = 0;
        for (int i = x; i < x2; i++) {
            Gui.drawRect(i, y, i + 1, y2, ColorUtil.getGradientOffset(color1,color2,index / (float)div));
            if (left)
                index++;
            else
                index--;
        }
    }

    public static void drawLineGradientY(int x, int y, int x2, int y2, java.awt.Color color1, java.awt.Color color2, float div, boolean left) {
        int index = 0;
        for (int i = y; i < y2; i++) {
            Gui.drawRect(x, i, x2, i + 1, ColorUtil.getGradientOffset(color1,color2,index / (float)div));
            if (left)
                index++;
            else
                index--;
        }
    }

    public static void drawStringOutlined(String text, int x, int y, int color) {
        mc.fontRendererObj.drawString(text, x + 1, y, 0);
        mc.fontRendererObj.drawString(text, x - 1, y, 0);
        mc.fontRendererObj.drawString(text, x, y + 1, 0);
        mc.fontRendererObj.drawString(text, x, y - 1, 0);
        mc.fontRendererObj.drawString(text, x, y, color);
    }

}
