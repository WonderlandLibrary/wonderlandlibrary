package cn.hanabi.injection.mixins;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({Render.class})
public abstract class MixinRender {
   @Final
   @Shadow
   protected RenderManager renderManager;

   @Shadow
   public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
   }

   @Shadow
   protected abstract boolean bindEntityTexture(Entity var1);
}
