package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IRenderManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({RenderManager.class})
public class MixinRenderManager implements IRenderManager {
   @Shadow
   private double renderPosX;
   @Shadow
   private double renderPosY;
   @Shadow
   private double renderPosZ;

   public double getRenderPosX() {
      return this.renderPosX;
   }

   public double getRenderPosY() {
      return this.renderPosY;
   }

   public double getRenderPosZ() {
      return this.renderPosZ;
   }
}
