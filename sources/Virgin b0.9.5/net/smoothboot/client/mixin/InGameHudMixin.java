package net.smoothboot.client.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.smoothboot.client.hud.vghud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
    @Mixin(InGameHud.class)
    public class InGameHudMixin {
        @Inject(method = "render", at = @At("RETURN"), cancellable = true)
        public void renderHud(DrawContext context, float tickDelta, CallbackInfo ci) {
            vghud.render(context, tickDelta);
            vghud.renderArrayList(context);
        }
    }
