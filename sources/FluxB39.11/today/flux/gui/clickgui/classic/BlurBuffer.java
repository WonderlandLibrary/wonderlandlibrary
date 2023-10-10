package today.flux.gui.clickgui.classic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import today.flux.module.implement.Render.Hud;
import today.flux.utility.TimeHelper;

public class BlurBuffer {
	private static ShaderGroup blurShader;
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static Framebuffer buffer;
	private static float lastScale;
	private static int lastScaleWidth;
	private static int lastScaleHeight;
	private static ResourceLocation shader = new ResourceLocation("shaders/post/blur.json");

	private static TimeHelper updateTimer = new TimeHelper();

	public static void initFboAndShader() {
		try {
			buffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
			buffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);

			blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), buffer, shader);
			blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void setShaderConfigs(float intensity, float blurWidth, float blurHeight) {
		blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
		blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);

		blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
		blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
	}

	public static void blurArea(float x, float y, float width, float height, boolean setupOverlay) {
		ScaledResolution scale = new ScaledResolution(mc);
		float factor = scale.getScaleFactor();
		int factor2 = scale.getScaledWidth();
		int factor3 = scale.getScaledHeight();
		if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
			initFboAndShader();
		}
		lastScale = factor;
		lastScaleWidth = factor2;
		lastScaleHeight = factor3;

		// 渲染
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		RenderUtil.doGlScissor(x, y, width, height);
		GL11.glPushMatrix();
		buffer.framebufferRenderExt(mc.displayWidth,  mc.displayHeight, true);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		if (setupOverlay) {
			mc.entityRenderer.setupOverlayRendering();
		}

		GlStateManager.enableDepth();
	}

	public static void blurRoundArea(float x, float y, float width, float height, float roundRadius, boolean setupOverlay) {
		ScaledResolution scale = new ScaledResolution(mc);
		float factor = scale.getScaleFactor();
		int factor2 = scale.getScaledWidth();
		int factor3 = scale.getScaledHeight();

		if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
			initFboAndShader();
		}

		lastScale = factor;
		lastScaleWidth = factor2;
		lastScaleHeight = factor3;

		// 渲染
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		RenderUtil.doGlScissor(x, y, width, height);
		GL11.glPushMatrix();
		buffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, true);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		if (setupOverlay) {
			mc.entityRenderer.setupOverlayRendering();
		}

		GlStateManager.enableDepth();
	}

	public static void updateBlurBuffer(boolean setupOverlay) {
		// 以60帧每秒的速度更新 FrameBuffer
		if (updateTimer.delay(1000 / 60f, true) && blurShader != null) {
			mc.getFramebuffer().unbindFramebuffer();

			setShaderConfigs(50, 0f, 1f);
			buffer.bindFramebuffer(true);

			mc.getFramebuffer().framebufferRenderExt(mc.displayWidth, mc.displayHeight, true);

			if (OpenGlHelper.shadersSupported) {
				GlStateManager.matrixMode(5890);
				GlStateManager.pushMatrix();
				GlStateManager.loadIdentity();
				blurShader.loadShaderGroup(mc.timer.renderPartialTicks);
				GlStateManager.popMatrix();
			}

			buffer.unbindFramebuffer();
			mc.getFramebuffer().bindFramebuffer(true);

			if(mc.getFramebuffer() != null && mc.getFramebuffer().depthBuffer > -1) {
				setupFBO(mc.getFramebuffer());
				mc.getFramebuffer().depthBuffer = -1;
			}

			if (setupOverlay) {
				mc.entityRenderer.setupOverlayRendering();
			}
		}
	}

	public static void setupFBO(Framebuffer fbo) {
		EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
		int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
		EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
		EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, mc.displayWidth, mc.displayHeight);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
	}

	public static boolean blurEnabled() {
		if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
			return !Hud.NoShader.getValueState();
		}
		return false;
	}
}
