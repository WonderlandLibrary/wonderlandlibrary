package io.github.nevalackin.client.util.render;

import io.github.nevalackin.client.core.KetamineClient;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.Display;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.BufferUtils.createFloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

public final class BlurUtil {

    private static final String BLUR_FRAG_SHADER =
        "#version 120\n" +
            "\n" +
            "uniform sampler2D texture;\n" +
            "uniform sampler2D texture2;\n" +
            "uniform vec2 texelSize;\n" +
            "uniform vec2 direction;\n" +
            "uniform float radius;\n" +
            "uniform float weights[256];\n" +
            "\n" +
            "void main() {\n" +
            "    vec4 color = vec4(0.0);\n" +
            "    vec2 texCoord = gl_TexCoord[0].st;\n" +
            "    if (direction.y == 0)\n" +
            "        if (texture2D(texture2, texCoord).a == 0.0) return;\n" +
            "    for (float f = -radius; f <= radius; f++) {\n" +
            "        color += texture2D(texture, texCoord + f * texelSize * direction) * (weights[int(abs(f))]);\n" +
            "    }\n" +
            "    gl_FragColor = vec4(color.rgb, 1.0);\n" +
            "}";

    public static final String VERTEX_SHADER =
        "#version 120 \n" +
            "\n" +
            "void main() {\n" +
            "    gl_TexCoord[0] = gl_MultiTexCoord0;\n" +
            "    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n" +
            "}";

    private static final GLShader blurShader = new GLShader(VERTEX_SHADER, BLUR_FRAG_SHADER) {
        @Override
        public void setupUniforms() {
            this.setupUniform("texture");
            this.setupUniform("texture2");
            this.setupUniform("texelSize");
            this.setupUniform("radius");
            this.setupUniform("direction");
            this.setupUniform("weights");
        }

        @Override
        public void updateUniforms() {
            final float radius = 20.f;

            glUniform1i(this.getUniformLocation("texture"), 0);
            glUniform1i(this.getUniformLocation("texture2"), 20);
            glUniform1f(this.getUniformLocation("radius"), radius);

            final FloatBuffer buffer = createFloatBuffer(256);
            final float blurRadius = radius / 2f;
            for (int i = 0; i <= blurRadius; i++) {
                buffer.put(BlurUtil.calculateGaussianOffset(i, radius / 4f));
            }
            buffer.rewind();

            glUniform1(this.getUniformLocation("weights"), buffer);

            glUniform2f(this.getUniformLocation("texelSize"),
                        1.0f / Display.getWidth(),
                        1.0f / Display.getHeight());
        }
    };

    private static Framebuffer framebuffer, framebufferRender;

    public static boolean disableBlur;

    private static List<double[]> blurAreas = new ArrayList<>();

    private BlurUtil() {
    }

    public static void blurArea(final double x, final double y, final double width, final double height) {
        if (disableBlur) return;
        blurAreas.add(new double[]{x, y, width, height});
    }

    public static void onRenderGameOverlay(final Framebuffer mcFramebuffer, final ScaledResolution sr) {
        if (framebuffer == null || framebufferRender == null || blurAreas.isEmpty()) return;
        framebufferRender.framebufferClear();
        // Draw into the blurFramebuffer
        framebufferRender.bindFramebuffer(false);
        // Draw the areas to be blurred
        for (final double[] area : blurAreas) {
            DrawUtil.glDrawFilledQuad(area[0], area[1], area[2], area[3], 0xFF << 24);
        }

        blurAreas.clear();

         // Enable blending and using glBlendFuncSeparate
         final boolean restore = DrawUtil.glEnableBlend();

         // Draw the first pass

        framebuffer.bindFramebuffer(false);
        blurShader.use(); // Use shader
        onPass(1); // Set direction
        // Draw the mcFramebuffer into the framebuffer
        glDrawFramebuffer(sr, mcFramebuffer);
        glUseProgram(0); // Stop using shader

        // Draw the second pass

        mcFramebuffer.bindFramebuffer(false);
        blurShader.use(); // Use shader
        onPass(0); // Update direction
        // Set texture 20 to the framebuffer drawn into by the event
        glActiveTexture(GL_TEXTURE20);
        glBindTexture(GL_TEXTURE_2D, framebufferRender.framebufferTexture);
        glActiveTexture(GL_TEXTURE0);
        // Draw the frame buffer onto screen
        glDrawFramebuffer(sr, framebuffer);
        glUseProgram(0); // Stop using shader

        // Restore the blend state
        DrawUtil.glRestoreBlend(restore);
    }

    private static void onPass(final int pass) {
        glUniform2f(blurShader.getUniformLocation("direction"), 1 - pass, pass);
    }

    private static void glDrawFramebuffer(final ScaledResolution scaledResolution, final Framebuffer framebuffer) {
        // Bind framebuffer texture
        glBindTexture(GL_TEXTURE_2D, framebuffer.framebufferTexture);
        // Draw the frame buffer texture upside-down
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 1);
            glVertex2i(0, 0);

            glTexCoord2f(0, 0);
            glVertex2i(0, scaledResolution.getScaledHeight());

            glTexCoord2f(1, 0);
            glVertex2i(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());

            glTexCoord2f(1, 1);
            glVertex2i(scaledResolution.getScaledWidth(), 0);
        }
        glEnd();
    }

    public static void onFrameBufferResize(final int width, final int height) {
        // Delete old buffers as to not cause a memory leak
        if (framebuffer != null)
            framebuffer.deleteFramebuffer();

        if (framebufferRender != null)
            framebufferRender.deleteFramebuffer();

        // Create new Framebuffers
        // False means it doesn't allocate a depth buffer which we don't need
        framebuffer = new Framebuffer(width, height, false);
        framebufferRender = new Framebuffer(width, height, false);
    }

    static float calculateGaussianOffset(float x, float sigma) {
        final float pow = x / sigma;
        return (float) (1.0 / (Math.abs(sigma) * 2.50662827463) * Math.exp(-0.5 * pow * pow));
    }
}
