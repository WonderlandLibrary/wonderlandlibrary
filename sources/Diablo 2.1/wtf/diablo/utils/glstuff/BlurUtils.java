package wtf.diablo.utils.glstuff;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import wtf.diablo.module.impl.Render.Hud;
import wtf.diablo.utils.render.RenderUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.BufferUtils.createFloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static wtf.diablo.utils.Util.mc;

public class BlurUtils {

    public static double intensity = 15;

    private static final List<double[]> blur = new ArrayList<>();

    private static Framebuffer framebuffer, framebufferRender;
    static ScaledResolution lastSr;
    static ScaledResolution scaledresolution;

    /*
        Not My Shaders :(
    */

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

    private static final Shader mainShader = new Shader(VERTEX_SHADER,BLUR_FRAG_SHADER){
        @Override
        public void addUniforms() {
            this.setupUniform("texture");
            this.setupUniform("texture2");
            this.setupUniform("radius");
            this.setupUniform("texelSize");
            this.setupUniform("direction");
            this.setupUniform("weights");
        }
        @Override
        public void updateUniforms() {
            final float radius = (float) intensity;

            glUniform1i(this.getUniform("texture"), 0);
            glUniform1i(this.getUniform("texture2"), 20);
            glUniform1f(this.getUniform("radius"), radius);

            final FloatBuffer buffer = createFloatBuffer(256);
            final float blurRadius = radius / 2f;
            for (int i = 0; i <= blurRadius; i++) {
                buffer.put(calculateGaussianOffset(i, radius / 4f));
            }
            buffer.rewind();

            glUniform1(this.getUniform("weights"), buffer);

            glUniform2f(this.getUniform("texelSize"),
                    1.0f / Display.getWidth(),
                    1.0f / Display.getHeight());
        }
    };

    /*
    * Draws Blur Area
    */
    public static void draw(double x, double y, double width, double height) {
        blur.add(new double[]{x, y, width, height,0});
    }

    /*
     * Draws Blur Area with 'round' Parameter
     */
    public static void drawRounded(double x, double y, double width, double height, double round) {
        blur.add(new double[]{x, y, width, height,round});
    }

    public static void render(ScaledResolution sr){
        if(framebuffer == null || blur.isEmpty() || framebufferRender == null)
            return;
        Framebuffer mcFrame = Minecraft.getMinecraft().getFramebuffer();
        framebufferRender.framebufferClear();
        framebufferRender.bindFramebuffer(false);

        for(double[] loc : blur) {
            RenderUtil.drawRoundedRect(loc[0],loc[1], loc[2],loc[3], loc[4], -1);
        }
        blur.clear();

        GL11.glEnable(GL_BLEND);

        //Horizontal
        framebuffer.bindFramebuffer(false);
        mainShader.updateProgram();
        setDirection(1);
        glDrawFramebuffer(sr, mcFrame);
        glUseProgram(0);

        //Vertical
        mcFrame.bindFramebuffer(false);
        mainShader.updateProgram();
        setDirection(0);
        glActiveTexture(GL_TEXTURE20);
        glBindTexture(GL_TEXTURE_2D, framebufferRender.framebufferTexture);
        glActiveTexture(GL_TEXTURE0);
        glDrawFramebuffer(sr, framebuffer);
        glUseProgram(0);
    }

    public static void resize(int width, int height) {
        GlStateManager.pushMatrix();
        if (framebuffer != null)
            framebuffer.deleteFramebuffer();

        if (framebufferRender != null)
            framebufferRender.deleteFramebuffer();

        framebuffer = new Framebuffer(width, height, false);
        framebufferRender = new Framebuffer(width, height, false);
        GlStateManager.popMatrix();
    }

    private static void glDrawFramebuffer(ScaledResolution sr, Framebuffer framebuffer) {
        glBindTexture(GL_TEXTURE_2D, framebuffer.framebufferTexture);
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 1);
            glVertex2i(0, 0);

            glTexCoord2f(0, 0);
            glVertex2i(0, sr.getScaledHeight());

            glTexCoord2f(1, 0);
            glVertex2i(sr.getScaledWidth(), sr.getScaledHeight());

            glTexCoord2f(1, 1);
            glVertex2i(sr.getScaledWidth(), 0);
        }
        glEnd();
    }

    private static void setDirection(int direction) {
        glUniform2f(mainShader.getUniform("direction"), 1 - direction, direction);
    }

    static float calculateGaussianOffset(float x, float sigma) {
        final float pow = x / sigma;
        return (float) (1.0 / (Math.abs(sigma) * 2.50662827463) * Math.exp(-0.5 * pow * pow));
    }

    public static void drawWholeScreen() {
        if (Hud.blurOn.getValue()) {
            draw(0,0,mc.displayWidth,mc.displayHeight);
        }
    }

    public static void updateBlur() {
        try {
            if(lastSr == null || lastSr.getScaledWidth() != scaledresolution.getScaledWidth() || lastSr.getScaledHeight() != scaledresolution.getScaledHeight()) {
                lastSr = scaledresolution;
                BlurUtils.resize(mc.displayWidth, mc.displayHeight);
            }

            BlurUtils.render(new ScaledResolution(mc));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
