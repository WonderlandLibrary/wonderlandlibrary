package xyz.northclient.util.shader;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;
import xyz.northclient.util.filter.image.GaussianFilter;
import xyz.northclient.util.shader.impl.RenderCallback;
import xyz.northclient.util.shader.impl.Shader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glDisable;
import static xyz.northclient.util.shader.RenderUtil.color;
import static xyz.northclient.util.shader.RenderUtil.start;
import static xyz.northclient.util.shader.StencilUtil.mc;

public class RectUtil {

    private static final HashMap<Integer, Integer> shadowCache = new HashMap<>();

    public static void applyColor(Color color) {
        GlStateManager.color(color.getRed()/255.0f,color.getGreen()/255.0f,color.getBlue()/255.0f);
    }

    //nie uzywajcie tego to tylko do targethud rounded bo stencil jest zjebany
    public static void maszdwuchojcow(double x, double y, float radius, int color) {
        color(new Color(color));
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();

        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        glPointSize(radius * (2 * mc.gameSettings.guiScale));

        glBegin(GL_POINTS);
        glVertex2d(x, y);
        glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(final double left, final double top, final double right, final double bottom, final boolean sideways, final int startColor, final int endColor) {
        GL11.glDisable(3553);
        start();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        color(startColor);
        if (sideways) {
            GL11.glVertex2d(left, top);
            GL11.glVertex2d(left, bottom);
            color(endColor);
            GL11.glVertex2d(right, bottom);
            GL11.glVertex2d(right, top);
        }
        else {
            GL11.glVertex2d(left, top);
            color(endColor);
            GL11.glVertex2d(left, bottom);
            GL11.glVertex2d(right, bottom);
            color(startColor);
            GL11.glVertex2d(right, top);
        }
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glShadeModel(7424);
        end();
        GL11.glEnable(3553);
    }

    public static void cipaorangutana(float x, float y, float width, float height, float radius, int color) {
        maszdwuchojcow(x + radius, y + radius, radius, color);
        maszdwuchojcow(x + width - radius, y + radius, radius, color);
        maszdwuchojcow(x + radius, y + height - radius, radius, color);
        maszdwuchojcow(x + width - radius, y + height - radius, radius, color);

        Gui.pizda(x + radius, y, width - radius * 2, height, color);
        Gui.pizda(x, y + radius, width, height - radius * 2, color);
    }

    public static void begin() {
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    public static void end() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GlStateManager.popMatrix();
        Gui.drawRect(0,0,0,0,0);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
        ShaderUtil.roundedShader.radius = radius;
        ShaderUtil.roundedShader.color = color;
        ShaderUtil.roundedShader.render(x,y,width,height);
    }

    public static void drawGradientRoundedRect(float x, float y, float width, float height, float radius, Color color, Color secondColor) {
        ShaderUtil.gradientRoundedShader.radius = radius;
        ShaderUtil.gradientRoundedShader.firstColor = color;
        ShaderUtil.gradientRoundedShader.secondColor = secondColor;
        ShaderUtil.gradientRoundedShader.render(x,y,width,height);
    }

    public static void drawBloom(int x, int y, int width, int height, int blurRadius, Color color) {
        Gui.drawRect(0,0,0,0,0);
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        GlStateManager.alphaFunc(516, 0.01f);
        height = Math.max(0, height);
        width = Math.max(0, width);
        width += blurRadius * 2;
        height += blurRadius * 2;
        x -= blurRadius;
        y -= blurRadius;
        final float _X = x - 0.25f;
        final float _Y = y + 0.25f;
        final int identifier = width * height + width + color.hashCode() * blurRadius + blurRadius;
        glEnable(3553);
        glDisable(2884);
        glEnable(3008);
        glEnable(3042);
        if (shadowCache.containsKey(identifier)) {
            final int texId = shadowCache.get(identifier);
            GlStateManager.bindTexture(texId);
        }
        else {
            final BufferedImage original = new BufferedImage(width, height, 2);
            final Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, width - blurRadius * 2, height - blurRadius * 2);
            g.dispose();
            final GaussianFilter op = new GaussianFilter((float)blurRadius);
            final BufferedImage blurred = op.filter(original, null);
            final int texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
            shadowCache.put(identifier, texId);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2d(_X, _Y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2d(_X, _Y + height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2d(_X + width, _Y + height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2d(_X + width, _Y);
        GL11.glEnd();
        glDisable(3553);
        glEnable(2884);
        glDisable(3008);
        glDisable(3042);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    public static void drawRoundedBloom(int x, int y, int width, int height, int roundRadius, int blurRadius, Color color) {
        Gui.drawRect(0,0,0,0,0);
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        GlStateManager.alphaFunc(516, 0.01f);
        height = Math.max(0, height);
        width = Math.max(0, width);
        width += blurRadius * 2;
        height += blurRadius * 2;
        x -= blurRadius;
        y -= blurRadius;
        final float _X = x - 0.25f;
        final float _Y = y + 0.25f;
        final int identifier = width * height + width + color.hashCode() * blurRadius + blurRadius + roundRadius + roundRadius;
        glEnable(3553);
        glDisable(2884);
        glEnable(3008);
        glEnable(3042);
        if (shadowCache.containsKey(identifier)) {
            final int texId = shadowCache.get(identifier);
            GlStateManager.bindTexture(texId);
        }
        else {
            final BufferedImage original = new BufferedImage(width, height, 2);
            final Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRoundRect(blurRadius, blurRadius, width - blurRadius * 2, height - blurRadius * 2, roundRadius,roundRadius);
            g.dispose();
            final GaussianFilter op = new GaussianFilter((float)blurRadius);
            final BufferedImage blurred = op.filter(original, null);
            final int texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
            shadowCache.put(identifier, texId);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2d(_X, _Y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2d(_X, _Y + height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2d(_X + width, _Y + height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2d(_X + width, _Y);
        GL11.glEnd();
        glDisable(3553);
        glEnable(2884);
        glDisable(3008);
        glDisable(3042);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }


    public static void drawRect(float x, float y, float width, float height, Color color) {
        begin();

        applyColor(color);

        GL11.glBegin(GL_QUADS);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(x, y);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(x, y + height);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(x + width, y + height);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(x + width, y);
        GL11.glEnd();

        end();
    }



    public static void drawBlur(float x, float y, float width, float height,float radius){
      //  blurIt(radius,() -> drawRect(x,y,width,height,Color.white));
    }

    public static void drawRoundedBlur(float x, float y, float width, float height, float roundRadius,float radius) {
       // blurIt(radius,() -> drawRoundedRect(x,y,width,height,roundRadius,Color.white));
    }

    public static void drawRect2(float x, float y, float x1, float y1, Color color) {
        begin();

        applyColor(color);

        GL11.glBegin(GL_QUADS);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(x, y);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(x, y1);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(x1, y1);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(x1, y);
        GL11.glEnd();

        end();
    }
}
