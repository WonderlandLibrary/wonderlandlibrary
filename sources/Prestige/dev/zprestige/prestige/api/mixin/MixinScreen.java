package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.BackgroundEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Screen.class})
public class MixinScreen {
    @Inject(method={"renderBackgroundTexture"}, at={@At(value="HEAD")}, cancellable=true)
    void renderBackgroundTexture(DrawContext drawContext, CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (new BackgroundEvent().invoke()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderBackground"}, at={@At(value="HEAD")}, cancellable=true)
    void renderBackground(DrawContext drawContext, CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (new BackgroundEvent().invoke()) {
            callbackInfo.cancel();
        }
    }
}
