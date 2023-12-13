package net.minecraft.client.shader;

import java.io.IOException;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.util.JsonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShaderLinkHelper {
   private static final Logger logger = LogManager.getLogger();
   private static ShaderLinkHelper staticShaderLinkHelper;

   public int createProgram() throws JsonException {
      int var1 = OpenGlHelper.glCreateProgram();
      if (var1 <= 0) {
         throw new JsonException("Could not create shader program (returned program ID " + var1 + ")");
      } else {
         return var1;
      }
   }

   public void deleteShader(ShaderManager var1) {
      var1.getFragmentShaderLoader().deleteShader(var1);
      var1.getVertexShaderLoader().deleteShader(var1);
      OpenGlHelper.glDeleteProgram(var1.getProgram());
   }

   public void linkProgram(ShaderManager var1) throws IOException {
      var1.getFragmentShaderLoader().attachShader(var1);
      var1.getVertexShaderLoader().attachShader(var1);
      OpenGlHelper.glLinkProgram(var1.getProgram());
      int var2 = OpenGlHelper.glGetProgrami(var1.getProgram(), OpenGlHelper.GL_LINK_STATUS);
      if (var2 == 0) {
         logger.warn("Error encountered when linking program containing VS " + var1.getVertexShaderLoader().getShaderFilename() + " and FS " + var1.getFragmentShaderLoader().getShaderFilename() + ". Log output:");
         logger.warn(OpenGlHelper.glGetProgramInfoLog(var1.getProgram(), 32768));
      }

   }

   public static void setNewStaticShaderLinkHelper() {
      staticShaderLinkHelper = new ShaderLinkHelper();
   }

   public static ShaderLinkHelper getStaticShaderLinkHelper() {
      return staticShaderLinkHelper;
   }
}
