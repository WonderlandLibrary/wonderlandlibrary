package net.minecraft.client.shader;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import de.violence.gui.VSetting;
import de.violence.module.Module;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

public class ShaderGroup {
   private Framebuffer mainFramebuffer;
   private IResourceManager resourceManager;
   private String shaderGroupName;
   private final List listShaders = Lists.newArrayList();
   private final Map mapFramebuffers = Maps.newHashMap();
   private final List listFramebuffers = Lists.newArrayList();
   private Matrix4f projectionMatrix;
   private int mainFramebufferWidth;
   private int mainFramebufferHeight;
   private float field_148036_j;
   private float field_148037_k;
   private String renderingFor;

   public ShaderGroup(TextureManager p_i1050_1_, IResourceManager p_i1050_2_, Framebuffer p_i1050_3_, ResourceLocation p_i1050_4_) throws JsonException, IOException, JsonSyntaxException {
      this.resourceManager = p_i1050_2_;
      this.mainFramebuffer = p_i1050_3_;
      this.field_148036_j = 0.0F;
      this.field_148037_k = 0.0F;
      this.mainFramebufferWidth = p_i1050_3_.framebufferWidth;
      this.mainFramebufferHeight = p_i1050_3_.framebufferHeight;
      this.shaderGroupName = p_i1050_4_.toString();
      this.resetProjectionMatrix();
      this.renderingFor = "unknown";
      this.parseGroup(p_i1050_1_, p_i1050_4_);
   }

   public void setRenderingFor(String renderingFor) {
      this.renderingFor = renderingFor;
   }

   public void parseGroup(TextureManager p_152765_1_, ResourceLocation p_152765_2_) throws JsonException, IOException, JsonSyntaxException {
      JsonParser jsonparser = new JsonParser();
      InputStream inputstream = null;

      try {
         IResource exception2 = this.resourceManager.getResource(p_152765_2_);
         inputstream = exception2.getInputStream();
         JsonObject var22 = jsonparser.parse(IOUtils.toString(inputstream, Charsets.UTF_8)).getAsJsonObject();
         JsonArray jsonarray1;
         int j;
         JsonElement jsonelement1;
         Iterator var10;
         JsonException jsonexception2;
         if(JsonUtils.isJsonArray(var22, "targets")) {
            jsonarray1 = var22.getAsJsonArray("targets");
            j = 0;

            for(var10 = jsonarray1.iterator(); var10.hasNext(); ++j) {
               jsonelement1 = (JsonElement)var10.next();

               try {
                  this.initTarget(jsonelement1);
               } catch (Exception var19) {
                  jsonexception2 = JsonException.func_151379_a(var19);
                  jsonexception2.func_151380_a("targets[" + j + "]");
                  throw jsonexception2;
               }
            }
         }

         if(JsonUtils.isJsonArray(var22, "passes")) {
            jsonarray1 = var22.getAsJsonArray("passes");
            j = 0;

            for(var10 = jsonarray1.iterator(); var10.hasNext(); ++j) {
               jsonelement1 = (JsonElement)var10.next();

               try {
                  this.parsePass(p_152765_1_, jsonelement1);
               } catch (Exception var18) {
                  jsonexception2 = JsonException.func_151379_a(var18);
                  jsonexception2.func_151380_a("passes[" + j + "]");
                  throw jsonexception2;
               }
            }
         }
      } catch (Exception var20) {
         JsonException jsonexception = JsonException.func_151379_a(var20);
         jsonexception.func_151381_b(p_152765_2_.getResourcePath());
         throw jsonexception;
      } finally {
         IOUtils.closeQuietly(inputstream);
      }

   }

   private void initTarget(JsonElement p_148027_1_) throws JsonException {
      if(JsonUtils.isString(p_148027_1_)) {
         this.addFramebuffer(p_148027_1_.getAsString(), this.mainFramebufferWidth, this.mainFramebufferHeight);
      } else {
         JsonObject jsonobject = JsonUtils.getJsonObject(p_148027_1_, "target");
         String s = JsonUtils.getString(jsonobject, "name");
         int i = JsonUtils.getInt(jsonobject, "width", this.mainFramebufferWidth);
         int j = JsonUtils.getInt(jsonobject, "height", this.mainFramebufferHeight);
         if(this.mapFramebuffers.containsKey(s)) {
            throw new JsonException(s + " is already defined");
         }

         this.addFramebuffer(s, i, j);
      }

   }

   private void parsePass(TextureManager p_152764_1_, JsonElement p_152764_2_) throws JsonException, IOException {
      JsonObject jsonobject = JsonUtils.getJsonObject(p_152764_2_, "pass");
      String s = JsonUtils.getString(jsonobject, "name");
      String s1 = JsonUtils.getString(jsonobject, "intarget");
      String s2 = JsonUtils.getString(jsonobject, "outtarget");
      Framebuffer framebuffer = this.getFramebuffer(s1);
      Framebuffer framebuffer1 = this.getFramebuffer(s2);
      if(framebuffer == null) {
         throw new JsonException("Input target \'" + s1 + "\' does not exist");
      } else if(framebuffer1 == null) {
         throw new JsonException("Output target \'" + s2 + "\' does not exist");
      } else {
         Shader shader = this.addShader(s, framebuffer, framebuffer1);
         JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "auxtargets", (JsonArray)null);
         if(jsonarray != null) {
            int jsonarray1 = 0;

            for(Iterator jsonelement1 = jsonarray.iterator(); jsonelement1.hasNext(); ++jsonarray1) {
               JsonElement l = (JsonElement)jsonelement1.next();

               try {
                  JsonObject exception1 = JsonUtils.getJsonObject(l, "auxtarget");
                  String var30 = JsonUtils.getString(exception1, "name");
                  String jsonexception1 = JsonUtils.getString(exception1, "id");
                  Framebuffer framebuffer2 = this.getFramebuffer(jsonexception1);
                  if(framebuffer2 == null) {
                     ResourceLocation resourcelocation = new ResourceLocation("textures/effect/" + jsonexception1 + ".png");

                     try {
                        this.resourceManager.getResource(resourcelocation);
                     } catch (FileNotFoundException var24) {
                        throw new JsonException("Render target or texture \'" + jsonexception1 + "\' does not exist");
                     }

                     p_152764_1_.bindTexture(resourcelocation);
                     ITextureObject itextureobject = p_152764_1_.getTexture(resourcelocation);
                     int j = JsonUtils.getInt(exception1, "width");
                     int k = JsonUtils.getInt(exception1, "height");
                     boolean flag = JsonUtils.getBoolean(exception1, "bilinear");
                     if(flag) {
                        GL11.glTexParameteri(3553, 10241, 9729);
                        GL11.glTexParameteri(3553, 10240, 9729);
                     } else {
                        GL11.glTexParameteri(3553, 10241, 9728);
                        GL11.glTexParameteri(3553, 10240, 9728);
                     }

                     shader.addAuxFramebuffer(var30, Integer.valueOf(itextureobject.getGlTextureId()), j, k);
                  } else {
                     shader.addAuxFramebuffer(var30, framebuffer2, framebuffer2.framebufferTextureWidth, framebuffer2.framebufferTextureHeight);
                  }
               } catch (Exception var25) {
                  JsonException exception = JsonException.func_151379_a(var25);
                  exception.func_151380_a("auxtargets[" + jsonarray1 + "]");
                  throw exception;
               }
            }
         }

         JsonArray var26 = JsonUtils.getJsonArray(jsonobject, "uniforms", (JsonArray)null);
         if(var26 != null) {
            int var27 = 0;

            for(Iterator var29 = var26.iterator(); var29.hasNext(); ++var27) {
               JsonElement var28 = (JsonElement)var29.next();

               try {
                  this.initUniform(var28);
               } catch (Exception var23) {
                  JsonException var31 = JsonException.func_151379_a(var23);
                  var31.func_151380_a("uniforms[" + var27 + "]");
                  throw var31;
               }
            }
         }

      }
   }

   private void initUniform(JsonElement p_148028_1_) throws JsonException {
      JsonObject jsonobject = JsonUtils.getJsonObject(p_148028_1_, "uniform");
      String s = JsonUtils.getString(jsonobject, "name");
      ShaderUniform shaderuniform = ((Shader)this.listShaders.get(this.listShaders.size() - 1)).getShaderManager().getShaderUniform(s);
      if(shaderuniform == null) {
         throw new JsonException("Uniform \'" + s + "\' does not exist");
      } else {
         float[] afloat = new float[4];
         int i = 0;

         for(Iterator var8 = JsonUtils.getJsonArray(jsonobject, "values").iterator(); var8.hasNext(); ++i) {
            JsonElement jsonelement = (JsonElement)var8.next();

            try {
               afloat[i] = JsonUtils.getFloat(jsonelement, "value") - 0.55F;
            } catch (Exception var11) {
               JsonException jsonexception = JsonException.func_151379_a(var11);
               jsonexception.func_151380_a("values[" + i + "]");
               throw jsonexception;
            }
         }

         switch(i) {
         case 0:
         default:
            break;
         case 1:
            shaderuniform.set(afloat[0]);
            break;
         case 2:
            shaderuniform.set(afloat[0], afloat[1]);
            break;
         case 3:
            shaderuniform.set(afloat[0], afloat[1], afloat[2]);
            break;
         case 4:
            shaderuniform.set(afloat[0], afloat[1], afloat[2], afloat[3]);
         }

      }
   }

   public Framebuffer getFramebufferRaw(String p_177066_1_) {
      return (Framebuffer)this.mapFramebuffers.get(p_177066_1_);
   }

   public void addFramebuffer(String p_148020_1_, int p_148020_2_, int p_148020_3_) {
      Framebuffer framebuffer = new Framebuffer(p_148020_2_, p_148020_3_, true);
      framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
      this.mapFramebuffers.put(p_148020_1_, framebuffer);
      if(p_148020_2_ == this.mainFramebufferWidth && p_148020_3_ == this.mainFramebufferHeight) {
         this.listFramebuffers.add(framebuffer);
      }

   }

   public void deleteShaderGroup() {
      Iterator var2 = this.mapFramebuffers.values().iterator();

      while(var2.hasNext()) {
         Framebuffer shader = (Framebuffer)var2.next();
         shader.deleteFramebuffer();
      }

      var2 = this.listShaders.iterator();

      while(var2.hasNext()) {
         Shader shader1 = (Shader)var2.next();
         shader1.deleteShader();
      }

      this.listShaders.clear();
   }

   public Shader addShader(String p_148023_1_, Framebuffer p_148023_2_, Framebuffer p_148023_3_) throws JsonException, IOException {
      Shader shader = new Shader(this.resourceManager, p_148023_1_, p_148023_2_, p_148023_3_);
      this.listShaders.add(this.listShaders.size(), shader);
      return shader;
   }

   private void resetProjectionMatrix() {
      this.projectionMatrix = new Matrix4f();
      this.projectionMatrix.setIdentity();
      this.projectionMatrix.m00 = 2.0F / (float)this.mainFramebuffer.framebufferTextureWidth;
      this.projectionMatrix.m11 = 2.0F / (float)(-this.mainFramebuffer.framebufferTextureHeight);
      this.projectionMatrix.m22 = -0.0020001999F;
      this.projectionMatrix.m33 = 1.0F;
      this.projectionMatrix.m03 = -1.0F;
      this.projectionMatrix.m13 = 1.0F;
      this.projectionMatrix.m23 = -1.0001999F;
   }

   public void createBindFramebuffers(int width, int height) {
      this.mainFramebufferWidth = this.mainFramebuffer.framebufferTextureWidth;
      this.mainFramebufferHeight = this.mainFramebuffer.framebufferTextureHeight;
      this.resetProjectionMatrix();
      Iterator var4 = this.listShaders.iterator();

      while(var4.hasNext()) {
         Shader framebuffer = (Shader)var4.next();
         framebuffer.setProjectionMatrix(this.projectionMatrix);
      }

      var4 = this.listFramebuffers.iterator();

      while(var4.hasNext()) {
         Framebuffer framebuffer1 = (Framebuffer)var4.next();
         framebuffer1.createBindFramebuffer(width, height);
      }

   }

   public void loadShaderGroup(float partialTicks) {
      if(partialTicks < this.field_148037_k) {
         this.field_148036_j += 1.0F - this.field_148037_k;
         this.field_148036_j += partialTicks;
      } else {
         this.field_148036_j += partialTicks - this.field_148037_k;
      }

      for(this.field_148037_k = partialTicks; this.field_148036_j > 20.0F; this.field_148036_j -= 20.0F) {
         ;
      }

      int blurs = 0;
      Iterator var4 = this.listShaders.iterator();

      while(true) {
         Shader shader;
         do {
            do {
               do {
                  do {
                     if(!var4.hasNext()) {
                        return;
                     }

                     shader = (Shader)var4.next();
                     ++blurs;
                     shader.setRenderingFor(this.renderingFor);
                  } while(shader.getShaderManager().getFragmentShaderLoader().getShaderFilename().equalsIgnoreCase("blur") && VSetting.getByName("Mode", Module.getByName("ESP")).getActiveMode().equalsIgnoreCase("Outline") && blurs == 3 && Module.getByName("ESP").isToggled() && this.renderingFor.equalsIgnoreCase("entityOutline"));
               } while(shader.getShaderManager().getFragmentShaderLoader().getShaderFilename().equalsIgnoreCase("blit") && blurs == 3 && VSetting.getByName("Mode", Module.getByName("ESP")).getActiveMode().equalsIgnoreCase("Inside") && Module.getByName("ESP").isToggled() && this.renderingFor.equalsIgnoreCase("entityOutline"));
            } while(shader.getShaderManager().getFragmentShaderLoader().getShaderFilename().equalsIgnoreCase("blur") && blurs == 3 && Module.getByName("StorageESP").isToggled() && VSetting.getByName("Mode", Module.getByName("StorageESP")).getActiveMode().equalsIgnoreCase("Outline") && this.renderingFor.equalsIgnoreCase("storageOutline"));
         } while(shader.getShaderManager().getFragmentShaderLoader().getShaderFilename().equalsIgnoreCase("blit") && blurs == 3 && Module.getByName("StorageESP").isToggled() && VSetting.getByName("Mode", Module.getByName("StorageESP")).getActiveMode().equalsIgnoreCase("Inside") && this.renderingFor.equalsIgnoreCase("storageOutline"));

         shader.loadShader(this.field_148036_j / 20.0F);
      }
   }

   public final String getShaderGroupName() {
      return this.shaderGroupName;
   }

   private Framebuffer getFramebuffer(String p_148017_1_) {
      return p_148017_1_ == null?null:(p_148017_1_.equals("minecraft:main")?this.mainFramebuffer:(Framebuffer)this.mapFramebuffers.get(p_148017_1_));
   }
}
