package cn.hanabi.injection.mixins;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({Render.class})
abstract class MixinRenderer {
   @Shadow
   protected abstract boolean bindEntityTexture(Entity var1);
}
