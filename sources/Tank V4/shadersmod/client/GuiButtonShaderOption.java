package shadersmod.client;

import net.minecraft.client.gui.GuiButton;

public class GuiButtonShaderOption extends GuiButton {
   private ShaderOption shaderOption = null;

   public GuiButtonShaderOption(int var1, int var2, int var3, int var4, int var5, ShaderOption var6, String var7) {
      super(var1, var2, var3, var4, var5, var7);
      this.shaderOption = var6;
   }

   public ShaderOption getShaderOption() {
      return this.shaderOption;
   }
}
