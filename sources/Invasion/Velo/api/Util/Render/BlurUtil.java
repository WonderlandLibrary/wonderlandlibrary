package Velo.api.Util.Render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class BlurUtil {
	private static ShaderGroup blurShader;
	private static Minecraft mc = Minecraft.getMinecraft();
	private static Framebuffer buffer;
	private static int lastScale;
	private static int lastScaleWidth;
	private static int lastScaleHeight;
	private static ResourceLocation shader = new ResourceLocation("shaders/post/blur.json");

	public static void inShaderFBO() {
		try {

			blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
			blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
			buffer = blurShader.mainFramebuffer;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void shaderConfigFix(float intensity, float blurWidth, float blurHeight, float opacity) {
		try {
			blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
			blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);

			blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Opacity").set(opacity);
			blurShader.getShaders().get(1).getShaderManager().mappedShaderUniforms.values()
					.forEach(a -> System.out.println(((ShaderUniform) a).getShaderName()));
			blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Opacity").set(opacity);

		} catch (NullPointerException e) {
			// System.out.println(e);
		}

	}

	public static void blurAreaBoarder(float x, float y, float width, float height, float intensity) {
		ScaledResolution scale = new ScaledResolution(mc);
		int factor = scale.getScaleFactor();
		int factor2 = scale.getScaledWidth();
		int factor3 = scale.getScaledHeight();
		if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null
				|| blurShader == null) {
			inShaderFBO();
		}
		//blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
		
		lastScale = factor;
		lastScaleWidth = factor2;
		lastScaleHeight = factor3;

		GL11.glScissor(((int) x * factor), ((int) (mc.displayHeight - (y * factor) - height * factor)),
				((int) width * factor), ((int) (height) * factor));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		shaderConfigFix(intensity, 1, 0, 0f);
		buffer.bindFramebuffer(true);

		blurShader.loadShaderGroup(mc.timer.renderPartialTicks);

		mc.getFramebuffer().bindFramebuffer(true);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

}
