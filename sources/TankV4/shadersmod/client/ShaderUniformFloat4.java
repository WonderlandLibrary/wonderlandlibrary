package shadersmod.client;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderUniformFloat4 extends ShaderUniformBase {
   private float[] values = new float[4];

   public ShaderUniformFloat4(String var1) {
      super(var1);
   }

   protected void onProgramChanged() {
      this.values[0] = 0.0F;
      this.values[1] = 0.0F;
      this.values[2] = 0.0F;
      this.values[3] = 0.0F;
   }

   public void setValue(float var1, float var2, float var3, float var4) {
      if (this.getLocation() >= 0 && (this.values[0] != var1 || this.values[1] != var2 || this.values[2] != var3 || this.values[3] != var4)) {
         ARBShaderObjects.glUniform4fARB(this.getLocation(), var1, var2, var3, var4);
         Shaders.checkGLError(this.getName());
         this.values[0] = var1;
         this.values[1] = var2;
         this.values[2] = var3;
         this.values[3] = var4;
      }

   }

   public float[] getValues() {
      return this.values;
   }
}
