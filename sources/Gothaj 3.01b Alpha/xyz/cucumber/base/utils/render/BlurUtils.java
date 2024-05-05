package xyz.cucumber.base.utils.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import xyz.cucumber.base.utils.render.shaders.Shaders;

public class BlurUtils {
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static Framebuffer framebuffer = new Framebuffer(1, 1, false);

    public static void setupUniforms(float dir1, float dir2, float radius) {
    	ScaledResolution sr = new ScaledResolution(mc);
        Shaders.blur.uniform1i(Shaders.blur.getProgramID(), "textureIn", 0);
        Shaders.blur.uniform2f(Shaders.blur.getProgramID(), "texelSize", 1.0F / (float) sr.getScaledWidth_double(), 1.0F / (float) sr.getScaledHeight_double());
        Shaders.blur.uniform2f(Shaders.blur.getProgramID(), "direction", dir1, dir2);
        
        Shaders.blur.uniform1f(Shaders.blur.getProgramID(), "radius", radius);

        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(calculateGaussianValue(i, radius / 2));
        }  

        weightBuffer.rewind();
        GL20.glUniform1(GL20.glGetUniformLocation(Shaders.blur.getProgramID(), "weights"), weightBuffer);
    }




    public static void renderBlur(float radius) {
    	ScaledResolution sr = new ScaledResolution(mc);
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);


        framebuffer = createFrameBuffer(framebuffer);

        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        Shaders.blur.startProgram();
        
        setupUniforms(1, 0, radius);

        bindTexture(mc.getFramebuffer().framebufferTexture);

        Shaders.blur.renderShader(0, 0, sr.getScaledWidth_double(), sr.getScaledHeight_double());
        framebuffer.unbindFramebuffer();
        Shaders.blur.stopProgram();

        mc.getFramebuffer().bindFramebuffer(true);
        Shaders.blur.startProgram();
        setupUniforms(0, 1, radius);

        bindTexture(framebuffer.framebufferTexture);
        Shaders.blur.renderShader(0, 0, sr.getScaledWidth_double(), sr.getScaledHeight_double());
        Shaders.blur.stopProgram();

        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
    }
  
  public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }
  
  public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
      if (framebuffer == null || framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
          if (framebuffer != null) {
              framebuffer.deleteFramebuffer();
          }
          return new Framebuffer(mc.displayWidth, mc.displayHeight, true);
      }
      return framebuffer;
  }


	public static void bindTexture(int texture) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
	}
}
