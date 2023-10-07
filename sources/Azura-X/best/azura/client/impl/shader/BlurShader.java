package best.azura.client.impl.shader;

import best.azura.client.api.shader.ShaderProgram;
import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class BlurShader {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final ShaderProgram blurShader = new ShaderProgram("blur.vsh");

    private static Framebuffer blurBuffer = new Framebuffer(1, 1, false);

    private float radius, alpha;
    private boolean darkMode;

    public BlurShader() {
        radius = 25;
        darkMode = false;
        alpha = 1.0f;
    }

    public BlurShader(float radius, float alpha, boolean darkMode) {
        this.radius = radius;
        this.alpha = alpha;
        this.darkMode = darkMode;
    }

    public void blur() {
        this.blur(mc.getFramebuffer(), mc.getFramebuffer());
    }

    public void blur(final Framebuffer framebuffer, final Framebuffer second) {

        if (darkMode)
            RenderUtil.INSTANCE.drawRect(0, 0, mc.displayWidth, mc.displayHeight, new Color(0, 0, 0, (int) Math.max(50, radius / 100 * 200)));

        blurBuffer = RenderUtil.INSTANCE.createFramebuffer(blurBuffer);

        // horizontal blur
        blurShader.init();
        setupUniforms(1, 0);
        blurBuffer.framebufferClear();
        blurBuffer.bindFramebuffer(true);
        glBindTexture(GL_TEXTURE_2D, framebuffer.framebufferTexture);
        blurShader.renderCanvas();
        blurBuffer.unbindFramebuffer();


        // vertical blur
        blurShader.init();
        setupUniforms(0, 1);
        second.bindFramebuffer(true);
        glBindTexture(GL_TEXTURE_2D, blurBuffer.framebufferTexture);
        blurShader.renderCanvas();
        blurShader.uninit();
    }


    public void setupUniforms(float x, float y) {
        blurShader.setUniformi("originalTexture", 0);
        blurShader.setUniformf("texelSize", 1f / mc.displayWidth, 1f / mc.displayHeight);
        blurShader.setUniformf("direction", x, y);
        blurShader.setUniformf("radius", radius);
        blurShader.setUniformf("alpha", alpha);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}