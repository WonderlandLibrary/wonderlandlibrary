package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IShaderGroup;
import java.util.List;
import net.minecraft.client.shader.ShaderGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ShaderGroup.class})
public abstract class MixinShaderGroup implements IShaderGroup {
   @Shadow
   @Final
   private List listShaders;

   @Shadow
   public abstract void createBindFramebuffers(int var1, int var2);

   @Shadow
   public abstract void loadShaderGroup(float var1);

   public List getShaders() {
      return this.listShaders;
   }
}
