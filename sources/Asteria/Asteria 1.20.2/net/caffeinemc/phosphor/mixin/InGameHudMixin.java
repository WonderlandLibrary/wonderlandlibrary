package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.api.event.events.HudRenderEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        Phosphor.EVENTBUS.post(HudRenderEvent.get(context, tickDelta));
    }
}
