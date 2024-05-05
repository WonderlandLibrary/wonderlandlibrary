package me.AveReborn.ui.shader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import me.AveReborn.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ShaderLoader {
   private int vertexShaderId;
   private int fragmentShaderId;
   private int programmId;
   private int fboTextureID;
   private int fboID;
   private int renderBufferID;
   private String vertexShaderFileName;
   private String fragmenShaderFileName;
   private int resolutionUniformId;
   private int timeUniformID;
   private int mouseUniformId;
   private int texelUniformId;
   private int frameBufferTextureId;
   private int diffuseSamperUniformID;
   private float time = 0.0F;

   public ShaderLoader(String fragmentShader, int frameBufferTextureId) {
      this.reset();
      this.vertexShaderFileName = "vertex.shader";
      this.fragmenShaderFileName = fragmentShader;
      this.frameBufferTextureId = frameBufferTextureId;
      this.generateFBO();
      this.initShaders();
   }

   private void generateFBO() {
      this.fboID = EXTFramebufferObject.glGenFramebuffersEXT();
      this.fboTextureID = GL11.glGenTextures();
      this.renderBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
      GL11.glBindTexture(3553, this.fboTextureID);
      GL11.glTexParameterf(3553, 10241, 9729.0F);
      GL11.glTexParameterf(3553, 10240, 9729.0F);
      GL11.glTexParameterf(3553, 10242, 10496.0F);
      GL11.glTexParameterf(3553, 10243, 10496.0F);
      GL11.glBindTexture(3553, 0);
      GL11.glBindTexture(3553, this.fboTextureID);
      GL11.glTexImage2D(3553, 0, '\u8058', Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, 0, 6408, 5121, (ByteBuffer)null);
      EXTFramebufferObject.glBindFramebufferEXT('\u8d40', this.fboID);
      EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8ce0', 3553, this.fboTextureID, 0);
      EXTFramebufferObject.glBindRenderbufferEXT('\u8d41', this.renderBufferID);
      EXTFramebufferObject.glRenderbufferStorageEXT('\u8d41', '\u84f9', Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
      EXTFramebufferObject.glFramebufferRenderbufferEXT('\u8d40', '\u8d20', '\u8d41', this.renderBufferID);
   }

   public int getFboTextureID() {
      return this.fboTextureID;
   }

   public void initShaders() {
      if(this.programmId == -1) {
         this.programmId = ARBShaderObjects.glCreateProgramObjectARB();

         try {
            InputStream ex;
            String fragmentShader;
            if(this.vertexShaderId == -1) {
               ex = this.getClass().getResourceAsStream(this.vertexShaderFileName);
               fragmentShader = RenderUtil.getShaderCode(new InputStreamReader(ex));
               this.vertexShaderId = RenderUtil.createShader(fragmentShader, '\u8b31');
            }

            if(this.fragmentShaderId == -1) {
               ex = this.getClass().getResourceAsStream("fragment/" + this.fragmenShaderFileName);
               fragmentShader = RenderUtil.getShaderCode(new InputStreamReader(ex));
               this.fragmentShaderId = RenderUtil.createShader(fragmentShader, '\u8b30');
            }
         } catch (Exception var3) {
            this.programmId = -1;
            this.vertexShaderId = -1;
            this.fragmentShaderId = -1;
            var3.printStackTrace();
         }

         if(this.programmId != -1) {
            ARBShaderObjects.glAttachObjectARB(this.programmId, this.vertexShaderId);
            ARBShaderObjects.glAttachObjectARB(this.programmId, this.fragmentShaderId);
            ARBShaderObjects.glLinkProgramARB(this.programmId);
            if(ARBShaderObjects.glGetObjectParameteriARB(this.programmId, '\u8b82') == 0) {
               System.err.println(this.programmId);
               return;
            }

            ARBShaderObjects.glValidateProgramARB(this.programmId);
            if(ARBShaderObjects.glGetObjectParameteriARB(this.programmId, '\u8b83') == 0) {
               System.err.println(this.programmId);
               return;
            }

            ARBShaderObjects.glUseProgramObjectARB(0);
            this.resolutionUniformId = ARBShaderObjects.glGetUniformLocationARB(this.programmId, "resolution");
            this.timeUniformID = ARBShaderObjects.glGetUniformLocationARB(this.programmId, "timeHelper");
            this.mouseUniformId = ARBShaderObjects.glGetUniformLocationARB(this.programmId, "mouse");
            this.diffuseSamperUniformID = ARBShaderObjects.glGetUniformLocationARB(this.programmId, "diffuseSamper");
            this.texelUniformId = ARBShaderObjects.glGetUniformLocationARB(this.programmId, "texel");
         }
      }

   }

   public ShaderLoader update() {
      if(this.fboID != -1 && this.renderBufferID != -1 && this.programmId != -1) {
         EXTFramebufferObject.glBindFramebufferEXT('\u8d40', this.fboID);
         int var9 = Math.max(Minecraft.getDebugFPS(), 30);
         GL11.glClear(16640);
         ARBShaderObjects.glUseProgramObjectARB(this.programmId);
         ARBShaderObjects.glUniform1iARB(this.diffuseSamperUniformID, 0);
         GL13.glActiveTexture('\u84c0');
         GL11.glEnable(3553);
         GL11.glBindTexture(3553, this.frameBufferTextureId);
         ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
         FloatBuffer resolutionBuffer = BufferUtils.createFloatBuffer(2);
         resolutionBuffer.position(0);
         resolutionBuffer.put((float)(Minecraft.getMinecraft().displayWidth / 2));
         resolutionBuffer.put((float)(Minecraft.getMinecraft().displayHeight / 2));
         resolutionBuffer.flip();
         ARBShaderObjects.glUniform2ARB(this.resolutionUniformId, resolutionBuffer);
         FloatBuffer texelSizeBuffer = BufferUtils.createFloatBuffer(2);
         texelSizeBuffer.position(0);
         texelSizeBuffer.put(1.0F / (float)Minecraft.getMinecraft().displayWidth * 2.0F);
         texelSizeBuffer.put(1.0F / (float)Minecraft.getMinecraft().displayHeight * 2.0F);
         texelSizeBuffer.flip();
         ARBShaderObjects.glUniform2ARB(this.texelUniformId, texelSizeBuffer);
         float mouseX = (float)Mouse.getX() / (float)Minecraft.getMinecraft().displayWidth;
         float mouseY = (float)Mouse.getY() / (float)Minecraft.getMinecraft().displayHeight;
         FloatBuffer mouseBuffer = BufferUtils.createFloatBuffer(2);
         mouseBuffer.position(0);
         mouseBuffer.put(mouseX);
         mouseBuffer.put(mouseY);
         mouseBuffer.flip();
         ARBShaderObjects.glUniform2ARB(this.mouseUniformId, mouseBuffer);
         this.time = (float)((double)this.time + (double)RenderUtil.delta * 0.7D);
         ARBShaderObjects.glUniform1fARB(this.timeUniformID, this.time);
         double width = (double)res.getScaledWidth();
         double height = (double)res.getScaledHeight();
         GL11.glDisable(3553);
         GL11.glBegin(4);
         GL11.glTexCoord2d(0.0D, 1.0D);
         GL11.glVertex2d(0.0D, 0.0D);
         GL11.glTexCoord2d(0.0D, 0.0D);
         GL11.glVertex2d(0.0D, height / 2.0D);
         GL11.glTexCoord2d(1.0D, 0.0D);
         GL11.glVertex2d(width / 2.0D, height / 2.0D);
         GL11.glTexCoord2d(1.0D, 0.0D);
         GL11.glVertex2d(width / 2.0D, height / 2.0D);
         GL11.glTexCoord2d(1.0D, 1.0D);
         GL11.glVertex2d(width / 2.0D, 0.0D);
         GL11.glTexCoord2d(0.0D, 1.0D);
         GL11.glVertex2d(0.0D, 0.0D);
         GL11.glEnd();
         ARBShaderObjects.glUseProgramObjectARB(0);
         return this;
      } else {
         throw new RuntimeException("Invalid IDs!");
      }
   }

   private void reset() {
      this.vertexShaderId = -1;
      this.fragmentShaderId = -1;
      this.programmId = -1;
      this.fboTextureID = -1;
      this.fboID = -1;
      this.renderBufferID = -1;
   }

   public void delete() {
      if(this.renderBufferID > -1) {
         EXTFramebufferObject.glDeleteRenderbuffersEXT(this.renderBufferID);
      }

      if(this.fboID > -1) {
         EXTFramebufferObject.glDeleteFramebuffersEXT(this.fboID);
      }

      if(this.fboTextureID > -1) {
         GL11.glDeleteTextures(this.fboTextureID);
      }

   }
}
