package net.minecraft.client.shader;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonBlendingMode;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShaderManager {
   private boolean isDirty;
   private final List attributes;
   private static ShaderManager staticShaderManager = null;
   private final List shaderUniforms = Lists.newArrayList();
   private final Map shaderSamplers = Maps.newHashMap();
   private static final Logger logger = LogManager.getLogger();
   private final int program;
   private final boolean useFaceCulling;
   private final List shaderUniformLocations = Lists.newArrayList();
   private final ShaderLoader vertexShaderLoader;
   private static boolean field_148000_e = true;
   private final List samplerNames = Lists.newArrayList();
   private final String programFilename;
   private final Map mappedShaderUniforms = Maps.newHashMap();
   private static int currentProgram = -1;
   private final JsonBlendingMode field_148016_p;
   private final List attribLocations;
   private final List shaderSamplerLocations = Lists.newArrayList();
   private final ShaderLoader fragmentShaderLoader;
   private static final ShaderDefault defaultShaderUniform = new ShaderDefault();

   public void deleteShader() {
      ShaderLinkHelper.getStaticShaderLinkHelper().deleteShader(this);
   }

   public void endShader() {
      OpenGlHelper.glUseProgram(0);
      currentProgram = -1;
      staticShaderManager = null;
      field_148000_e = true;

      for(int var1 = 0; var1 < this.shaderSamplerLocations.size(); ++var1) {
         if (this.shaderSamplers.get(this.samplerNames.get(var1)) != null) {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + var1);
            GlStateManager.bindTexture(0);
         }
      }

   }

   private void setupUniforms() {
      int var1 = 0;

      for(int var2 = 0; var1 < this.samplerNames.size(); ++var2) {
         String var3 = (String)this.samplerNames.get(var1);
         int var4 = OpenGlHelper.glGetUniformLocation(this.program, var3);
         if (var4 == -1) {
            logger.warn("Shader " + this.programFilename + "could not find sampler named " + var3 + " in the specified shader program.");
            this.shaderSamplers.remove(var3);
            this.samplerNames.remove(var2);
            --var2;
         } else {
            this.shaderSamplerLocations.add(var4);
         }

         ++var1;
      }

      Iterator var7 = this.shaderUniforms.iterator();

      while(var7.hasNext()) {
         ShaderUniform var8 = (ShaderUniform)var7.next();
         String var9 = var8.getShaderName();
         int var5 = OpenGlHelper.glGetUniformLocation(this.program, var9);
         if (var5 == -1) {
            logger.warn("Could not find uniform named " + var9 + " in the specified" + " shader program.");
         } else {
            this.shaderUniformLocations.add(var5);
            var8.setUniformLocation(var5);
            this.mappedShaderUniforms.put(var9, var8);
         }
      }

   }

   private void parseSampler(JsonElement var1) throws JsonException {
      JsonObject var2 = JsonUtils.getJsonObject(var1, "sampler");
      String var3 = JsonUtils.getString(var2, "name");
      if (!JsonUtils.isString(var2, "file")) {
         this.shaderSamplers.put(var3, (Object)null);
         this.samplerNames.add(var3);
      } else {
         this.samplerNames.add(var3);
      }

   }

   public ShaderUniform getShaderUniform(String var1) {
      return this.mappedShaderUniforms.containsKey(var1) ? (ShaderUniform)this.mappedShaderUniforms.get(var1) : null;
   }

   public ShaderUniform getShaderUniformOrDefault(String var1) {
      return (ShaderUniform)(this.mappedShaderUniforms.containsKey(var1) ? (ShaderUniform)this.mappedShaderUniforms.get(var1) : defaultShaderUniform);
   }

   public ShaderLoader getFragmentShaderLoader() {
      return this.fragmentShaderLoader;
   }

   private void parseUniform(JsonElement var1) throws JsonException {
      JsonObject var2 = JsonUtils.getJsonObject(var1, "uniform");
      String var3 = JsonUtils.getString(var2, "name");
      int var4 = ShaderUniform.parseType(JsonUtils.getString(var2, "type"));
      int var5 = JsonUtils.getInt(var2, "count");
      float[] var6 = new float[Math.max(var5, 16)];
      JsonArray var7 = JsonUtils.getJsonArray(var2, "values");
      if (var7.size() != var5 && var7.size() > 1) {
         throw new JsonException("Invalid amount of values specified (expected " + var5 + ", found " + var7.size() + ")");
      } else {
         int var8 = 0;

         for(Iterator var10 = var7.iterator(); var10.hasNext(); ++var8) {
            JsonElement var9 = (JsonElement)var10.next();

            try {
               var6[var8] = JsonUtils.getFloat(var9, "value");
            } catch (Exception var13) {
               JsonException var12 = JsonException.func_151379_a(var13);
               var12.func_151380_a("values[" + var8 + "]");
               throw var12;
            }
         }

         if (var5 > 1 && var7.size() == 1) {
            while(var8 < var5) {
               var6[var8] = var6[0];
               ++var8;
            }
         }

         int var14 = var5 > 1 && var5 <= 4 && var4 < 8 ? var5 - 1 : 0;
         ShaderUniform var15 = new ShaderUniform(var3, var4 + var14, var5, this);
         if (var4 <= 3) {
            var15.set((int)var6[0], (int)var6[1], (int)var6[2], (int)var6[3]);
         } else if (var4 <= 7) {
            var15.func_148092_b(var6[0], var6[1], var6[2], var6[3]);
         } else {
            var15.set(var6);
         }

         this.shaderUniforms.add(var15);
      }
   }

   public ShaderManager(IResourceManager var1, String var2) throws JsonException, IOException {
      JsonParser var3 = new JsonParser();
      ResourceLocation var4 = new ResourceLocation("shaders/program/" + var2 + ".json");
      this.programFilename = var2;
      InputStream var5 = null;

      try {
         var5 = var1.getResource(var4).getInputStream();
         JsonObject var6 = var3.parse(IOUtils.toString(var5, Charsets.UTF_8)).getAsJsonObject();
         String var22 = JsonUtils.getString(var6, "vertex");
         String var8 = JsonUtils.getString(var6, "fragment");
         JsonArray var9 = JsonUtils.getJsonArray(var6, "samplers", (JsonArray)null);
         if (var9 != null) {
            int var10 = 0;

            for(Iterator var12 = var9.iterator(); var12.hasNext(); ++var10) {
               JsonElement var11 = (JsonElement)var12.next();

               try {
                  this.parseSampler(var11);
               } catch (Exception var20) {
                  JsonException var14 = JsonException.func_151379_a(var20);
                  var14.func_151380_a("samplers[" + var10 + "]");
                  throw var14;
               }
            }
         }

         JsonArray var23 = JsonUtils.getJsonArray(var6, "attributes", (JsonArray)null);
         Iterator var13;
         if (var23 != null) {
            int var24 = 0;
            this.attribLocations = Lists.newArrayListWithCapacity(var23.size());
            this.attributes = Lists.newArrayListWithCapacity(var23.size());

            for(var13 = var23.iterator(); var13.hasNext(); ++var24) {
               JsonElement var26 = (JsonElement)var13.next();

               try {
                  this.attributes.add(JsonUtils.getString(var26, "attribute"));
               } catch (Exception var19) {
                  JsonException var15 = JsonException.func_151379_a(var19);
                  var15.func_151380_a("attributes[" + var24 + "]");
                  throw var15;
               }
            }
         } else {
            this.attribLocations = null;
            this.attributes = null;
         }

         JsonArray var25 = JsonUtils.getJsonArray(var6, "uniforms", (JsonArray)null);
         if (var25 != null) {
            int var27 = 0;

            for(Iterator var30 = var25.iterator(); var30.hasNext(); ++var27) {
               JsonElement var28 = (JsonElement)var30.next();

               try {
                  this.parseUniform(var28);
               } catch (Exception var18) {
                  JsonException var16 = JsonException.func_151379_a(var18);
                  var16.func_151380_a("uniforms[" + var27 + "]");
                  throw var16;
               }
            }
         }

         this.field_148016_p = JsonBlendingMode.func_148110_a(JsonUtils.getJsonObject(var6, "blend", (JsonObject)null));
         this.useFaceCulling = JsonUtils.getBoolean(var6, "cull", true);
         this.vertexShaderLoader = ShaderLoader.loadShader(var1, ShaderLoader.ShaderType.VERTEX, var22);
         this.fragmentShaderLoader = ShaderLoader.loadShader(var1, ShaderLoader.ShaderType.FRAGMENT, var8);
         this.program = ShaderLinkHelper.getStaticShaderLinkHelper().createProgram();
         ShaderLinkHelper.getStaticShaderLinkHelper().linkProgram(this);
         this.setupUniforms();
         if (this.attributes != null) {
            var13 = this.attributes.iterator();

            while(var13.hasNext()) {
               String var29 = (String)var13.next();
               int var31 = OpenGlHelper.glGetAttribLocation(this.program, var29);
               this.attribLocations.add(var31);
            }
         }
      } catch (Exception var21) {
         JsonException var7 = JsonException.func_151379_a(var21);
         var7.func_151381_b(var4.getResourcePath());
         throw var7;
      }

      IOUtils.closeQuietly(var5);
      this.markDirty();
   }

   public void useShader() {
      this.isDirty = false;
      staticShaderManager = this;
      this.field_148016_p.func_148109_a();
      if (this.program != currentProgram) {
         OpenGlHelper.glUseProgram(this.program);
         currentProgram = this.program;
      }

      if (this.useFaceCulling) {
         GlStateManager.enableCull();
      } else {
         GlStateManager.disableCull();
      }

      for(int var1 = 0; var1 < this.shaderSamplerLocations.size(); ++var1) {
         if (this.shaderSamplers.get(this.samplerNames.get(var1)) != null) {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + var1);
            GlStateManager.enableTexture2D();
            Object var2 = this.shaderSamplers.get(this.samplerNames.get(var1));
            int var3 = -1;
            if (var2 instanceof Framebuffer) {
               var3 = ((Framebuffer)var2).framebufferTexture;
            } else if (var2 instanceof ITextureObject) {
               var3 = ((ITextureObject)var2).getGlTextureId();
            } else if (var2 instanceof Integer) {
               var3 = (Integer)var2;
            }

            if (var3 != -1) {
               GlStateManager.bindTexture(var3);
               OpenGlHelper.glUniform1i(OpenGlHelper.glGetUniformLocation(this.program, (CharSequence)this.samplerNames.get(var1)), var1);
            }
         }
      }

      Iterator var5 = this.shaderUniforms.iterator();

      while(var5.hasNext()) {
         ShaderUniform var4 = (ShaderUniform)var5.next();
         var4.upload();
      }

   }

   public void addSamplerTexture(String var1, Object var2) {
      if (this.shaderSamplers.containsKey(var1)) {
         this.shaderSamplers.remove(var1);
      }

      this.shaderSamplers.put(var1, var2);
      this.markDirty();
   }

   public ShaderLoader getVertexShaderLoader() {
      return this.vertexShaderLoader;
   }

   public void markDirty() {
      this.isDirty = true;
   }

   public int getProgram() {
      return this.program;
   }
}
