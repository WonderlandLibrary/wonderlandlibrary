package net.minecraft.client.shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

public class ShaderUniform {
   private final FloatBuffer uniformFloatBuffer;
   private final IntBuffer uniformIntBuffer;
   private final String shaderName;
   private final int uniformCount;
   private final ShaderManager shaderManager;
   private static final Logger logger = LogManager.getLogger();
   private boolean dirty;
   private final int uniformType;
   private int uniformLocation;

   public void setUniformLocation(int var1) {
      this.uniformLocation = var1;
   }

   public void set(float[] var1) {
      if (var1.length < this.uniformCount) {
         logger.warn("Uniform.set called with a too-small value array (expected " + this.uniformCount + ", got " + var1.length + "). Ignoring.");
      } else {
         this.uniformFloatBuffer.position(0);
         this.uniformFloatBuffer.put(var1);
         this.uniformFloatBuffer.position(0);
         this.markDirty();
      }

   }

   private void uploadFloatMatrix() {
      switch(this.uniformType) {
      case 8:
         OpenGlHelper.glUniformMatrix2(this.uniformLocation, true, this.uniformFloatBuffer);
         break;
      case 9:
         OpenGlHelper.glUniformMatrix3(this.uniformLocation, true, this.uniformFloatBuffer);
         break;
      case 10:
         OpenGlHelper.glUniformMatrix4(this.uniformLocation, true, this.uniformFloatBuffer);
      }

   }

   public void upload() {
      if (!this.dirty) {
      }

      this.dirty = false;
      if (this.uniformType <= 3) {
         this.uploadInt();
      } else if (this.uniformType <= 7) {
         this.uploadFloat();
      } else {
         if (this.uniformType > 10) {
            logger.warn("Uniform.upload called, but type value (" + this.uniformType + ") is not " + "a valid type. Ignoring.");
            return;
         }

         this.uploadFloatMatrix();
      }

   }

   public static int parseType(String var0) {
      byte var1 = -1;
      if (var0.equals("int")) {
         var1 = 0;
      } else if (var0.equals("float")) {
         var1 = 4;
      } else if (var0.startsWith("matrix")) {
         if (var0.endsWith("2x2")) {
            var1 = 8;
         } else if (var0.endsWith("3x3")) {
            var1 = 9;
         } else if (var0.endsWith("4x4")) {
            var1 = 10;
         }
      }

      return var1;
   }

   private void markDirty() {
      this.dirty = true;
      if (this.shaderManager != null) {
         this.shaderManager.markDirty();
      }

   }

   public ShaderUniform(String var1, int var2, int var3, ShaderManager var4) {
      this.shaderName = var1;
      this.uniformCount = var3;
      this.uniformType = var2;
      this.shaderManager = var4;
      if (var2 <= 3) {
         this.uniformIntBuffer = BufferUtils.createIntBuffer(var3);
         this.uniformFloatBuffer = null;
      } else {
         this.uniformIntBuffer = null;
         this.uniformFloatBuffer = BufferUtils.createFloatBuffer(var3);
      }

      this.uniformLocation = -1;
      this.markDirty();
   }

   public void set(Matrix4f var1) {
      this.set(var1.m00, var1.m01, var1.m02, var1.m03, var1.m10, var1.m11, var1.m12, var1.m13, var1.m20, var1.m21, var1.m22, var1.m23, var1.m30, var1.m31, var1.m32, var1.m33);
   }

   public void set(int var1, int var2, int var3, int var4) {
      this.uniformIntBuffer.position(0);
      if (this.uniformType >= 0) {
         this.uniformIntBuffer.put(0, var1);
      }

      if (this.uniformType >= 1) {
         this.uniformIntBuffer.put(1, var2);
      }

      if (this.uniformType >= 2) {
         this.uniformIntBuffer.put(2, var3);
      }

      if (this.uniformType >= 3) {
         this.uniformIntBuffer.put(3, var4);
      }

      this.markDirty();
   }

   public void func_148092_b(float var1, float var2, float var3, float var4) {
      this.uniformFloatBuffer.position(0);
      if (this.uniformType >= 4) {
         this.uniformFloatBuffer.put(0, var1);
      }

      if (this.uniformType >= 5) {
         this.uniformFloatBuffer.put(1, var2);
      }

      if (this.uniformType >= 6) {
         this.uniformFloatBuffer.put(2, var3);
      }

      if (this.uniformType >= 7) {
         this.uniformFloatBuffer.put(3, var4);
      }

      this.markDirty();
   }

   public void set(float var1, float var2) {
      this.uniformFloatBuffer.position(0);
      this.uniformFloatBuffer.put(0, var1);
      this.uniformFloatBuffer.put(1, var2);
      this.markDirty();
   }

   private void uploadInt() {
      switch(this.uniformType) {
      case 0:
         OpenGlHelper.glUniform1(this.uniformLocation, this.uniformIntBuffer);
         break;
      case 1:
         OpenGlHelper.glUniform2(this.uniformLocation, this.uniformIntBuffer);
         break;
      case 2:
         OpenGlHelper.glUniform3(this.uniformLocation, this.uniformIntBuffer);
         break;
      case 3:
         OpenGlHelper.glUniform4(this.uniformLocation, this.uniformIntBuffer);
         break;
      default:
         logger.warn("Uniform.upload called, but count value (" + this.uniformCount + ") is " + " not in the range of 1 to 4. Ignoring.");
      }

   }

   public String getShaderName() {
      return this.shaderName;
   }

   public void set(float var1) {
      this.uniformFloatBuffer.position(0);
      this.uniformFloatBuffer.put(0, var1);
      this.markDirty();
   }

   public void set(float var1, float var2, float var3) {
      this.uniformFloatBuffer.position(0);
      this.uniformFloatBuffer.put(0, var1);
      this.uniformFloatBuffer.put(1, var2);
      this.uniformFloatBuffer.put(2, var3);
      this.markDirty();
   }

   public void set(float var1, float var2, float var3, float var4) {
      this.uniformFloatBuffer.position(0);
      this.uniformFloatBuffer.put(var1);
      this.uniformFloatBuffer.put(var2);
      this.uniformFloatBuffer.put(var3);
      this.uniformFloatBuffer.put(var4);
      this.uniformFloatBuffer.flip();
      this.markDirty();
   }

   public void set(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16) {
      this.uniformFloatBuffer.position(0);
      this.uniformFloatBuffer.put(0, var1);
      this.uniformFloatBuffer.put(1, var2);
      this.uniformFloatBuffer.put(2, var3);
      this.uniformFloatBuffer.put(3, var4);
      this.uniformFloatBuffer.put(4, var5);
      this.uniformFloatBuffer.put(5, var6);
      this.uniformFloatBuffer.put(6, var7);
      this.uniformFloatBuffer.put(7, var8);
      this.uniformFloatBuffer.put(8, var9);
      this.uniformFloatBuffer.put(9, var10);
      this.uniformFloatBuffer.put(10, var11);
      this.uniformFloatBuffer.put(11, var12);
      this.uniformFloatBuffer.put(12, var13);
      this.uniformFloatBuffer.put(13, var14);
      this.uniformFloatBuffer.put(14, var15);
      this.uniformFloatBuffer.put(15, var16);
      this.markDirty();
   }

   private void uploadFloat() {
      switch(this.uniformType) {
      case 4:
         OpenGlHelper.glUniform1(this.uniformLocation, this.uniformFloatBuffer);
         break;
      case 5:
         OpenGlHelper.glUniform2(this.uniformLocation, this.uniformFloatBuffer);
         break;
      case 6:
         OpenGlHelper.glUniform3(this.uniformLocation, this.uniformFloatBuffer);
         break;
      case 7:
         OpenGlHelper.glUniform4(this.uniformLocation, this.uniformFloatBuffer);
         break;
      default:
         logger.warn("Uniform.upload called, but count value (" + this.uniformCount + ") is " + "not in the range of 1 to 4. Ignoring.");
      }

   }
}
