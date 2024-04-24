package me.offeex.bloomware.mixin.render;

import me.offeex.bloomware.client.module.visuals.NoRender;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantingTableBlockEntityRenderer.class)
public class MixinEnchantingTableBlockEntityRenderer {
    @Inject(method = "render*", at = @At("HEAD"), cancellable = true)
    public void render(CallbackInfo ci) {
        NoRender module = NoRender.INSTANCE;
        if (module.getEnabled() && NoRender.INSTANCE.getEnchantedTable().getToggled()) ci.cancel();
    }
}
