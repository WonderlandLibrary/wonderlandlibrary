package tech.atani.client.utility.render.shader;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import cn.muyang.nativeobfuscator.Native;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import tech.atani.client.utility.interfaces.Methods;

@Native
public class TextureRenderer implements Methods {

    public static void drawTexture(float x, float y, float width, float height) {
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(x, y);
        glTexCoord2f(0, 1);
        glVertex2f(x, y + height);
        glTexCoord2f(1, 1);
        glVertex2f(x + width, y + height);
        glTexCoord2f(1, 0);
        glVertex2f(x + width, y);
        glEnd();

    }

    public static void drawTexture(Framebuffer fr) {
        GlStateManager.pushMatrix();
        ScaledResolution sr = new ScaledResolution(mc);
        final float width = (float) sr.getScaledWidth_double();
        final float height = (float) sr.getScaledHeight_double();
        float f2 = (float) fr.framebufferWidth / (float) fr.framebufferTextureWidth;
        float f3 = (float) fr.framebufferHeight / (float) fr.framebufferTextureHeight;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, (double) (float) height, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos((double) (float) width, (double) (float) height, 0.0D).tex((double) f2, 0.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos((double) (float) width, 0.0D, 0.0D).tex((double) f2, (double) f3).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double) f3).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void renderFRFscreen(final Framebuffer framebuffer) {
        if (mc.gameSettings.ofFastRender) return;
        ScaledResolution sr = new ScaledResolution(mc);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2d(0, 1);
            GL11.glVertex2d(0, 0);
            GL11.glTexCoord2d(0, 0);
            GL11.glVertex2d(0, sr.getScaledHeight());
            GL11.glTexCoord2d(1, 0);
            GL11.glVertex2d(sr.getScaledWidth(), sr.getScaledHeight());
            GL11.glTexCoord2d(1, 1);
            GL11.glVertex2d(sr.getScaledWidth(), 0);
        }
        GL11.glEnd();
    }

}