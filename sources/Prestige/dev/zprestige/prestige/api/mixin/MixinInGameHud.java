package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.CrosshairEvent;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.event.impl.StatusEffectOverlayEvent;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={InGameHud.class})
public class MixinInGameHud {
    @Shadow
    public int scaledWidth;
    @Shadow
    public int scaledHeight;

    @Inject(at={@At(value="HEAD")}, method={"renderStatusEffectOverlay"}, cancellable=true)
    void renderStatusEffectOverlay(DrawContext drawContext, CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (new StatusEffectOverlayEvent().invoke()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderCrosshair"}, at={@At(value="HEAD")}, cancellable=true)
    void renderCrosshair(DrawContext drawContext, CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (new CrosshairEvent().invoke()) {
            callbackInfo.cancel();
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "render" }, cancellable = true)
    void render(DrawContext drawContext, float n, CallbackInfo callbackInfo) {
        if (!Prestige.Companion.getSelfDestructed()) {
            RenderHelper.setContext(drawContext);
            if (!new Render2DEvent(drawContext.getMatrices(), scaledWidth, scaledHeight).invoke()) {
                if (!(MinecraftClient.getInstance().currentScreen instanceof Interface)) {
                    return;
                }
            }
            callbackInfo.cancel();
        }
    }
}
