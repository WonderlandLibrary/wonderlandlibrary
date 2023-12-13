package shadersmod.client;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderUniformInt extends ShaderUniformBase {
   private int value = -1;

   public ShaderUniformInt(String var1) {
      super(var1);
   }

   protected void onProgramChanged() {
      this.value = -1;
   }

   public void setValue(int var1) {
      if (this.getLocation() >= 0 && this.value != var1) {
         ARBShaderObjects.glUniform1iARB(this.getLocation(), var1);
         Shaders.checkGLError(this.getName());
         this.value = var1;
      }

   }

   public int getValue() {
      return this.value;
   }
}
