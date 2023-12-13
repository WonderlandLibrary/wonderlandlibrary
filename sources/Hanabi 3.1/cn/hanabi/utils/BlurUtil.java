package cn.hanabi.utils;

import cn.hanabi.Wrapper;
import cn.hanabi.injection.interfaces.IShaderGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BlurUtil {
   private static ShaderGroup blurShader;
   private static final Minecraft mc = Minecraft.getMinecraft();
   private static Framebuffer buffer;
   private static int lastScale;
   private static int lastScaleWidth;
   private static int lastScaleHeight;
   private static final ResourceLocation shader = new ResourceLocation("shaders/post/blur.json");

   public static void initFboAndShader() {
      try {
         blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
         blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
         buffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
         buffer.setFramebufferColor(1.0F, 1.0F, 1.0F, 1.0F);
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }

   private static void setShaderConfigs(float intensity, float blurWidth, float blurHeight) {
      ((net.minecraft.client.shader.Shader)((IShaderGroup)blurShader).getShaders().get(0)).getShaderManager().getShaderUniform("Radius").set(intensity);
      ((net.minecraft.client.shader.Shader)((IShaderGroup)blurShader).getShaders().get(1)).getShaderManager().getShaderUniform("Radius").set(intensity);
      ((net.minecraft.client.shader.Shader)((IShaderGroup)blurShader).getShaders().get(0)).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
      ((net.minecraft.client.shader.Shader)((IShaderGroup)blurShader).getShaders().get(1)).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
   }

   public static void doBlur(float intensity) {
      ScaledResolution scale = new ScaledResolution(mc);
      int factor = scale.getScaleFactor();
      int factor2 = scale.getScaledWidth();
      int factor3 = scale.getScaledHeight();
      if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
         initFboAndShader();
      }

      lastScale = factor;
      lastScaleWidth = factor2;
      lastScaleHeight = factor3;
      setShaderConfigs(intensity, 0.0F, 1.0F);
      buffer.bindFramebuffer(true);
      blurShader.loadShaderGroup(Wrapper.getTimer().renderPartialTicks);
      mc.getFramebuffer().bindFramebuffer(true);
   }

   public static void doBlur(float x, float y, float width, float height, float intensity, float blurWidth, float blurHeight) {
      ScaledResolution scale = new ScaledResolution(mc);
      int factor = scale.getScaleFactor();
      int factor2 = scale.getScaledWidth();
      int factor3 = scale.getScaledHeight();
      if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
         initFboAndShader();
      }

      lastScale = factor;
      lastScaleWidth = factor2;
      lastScaleHeight = factor3;
      GL11.glScissor((int)(x * (float)factor), (int)((float)mc.displayHeight - y * (float)factor - height * (float)factor) + 1, (int)(width * (float)factor), (int)(height * (float)factor));
      GL11.glEnable(3089);
      setShaderConfigs(intensity, blurWidth, blurHeight);
      buffer.bindFramebuffer(true);
      blurShader.loadShaderGroup(Wrapper.getTimer().renderPartialTicks);
      mc.getFramebuffer().bindFramebuffer(true);
      GL11.glDisable(3089);
   }
}
