package me.offeex.bloomware.mixin.render;

import me.offeex.bloomware.event.events.EventScaleFactor;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class MixinWindow {

    @Inject(method = "setScaleFactor", at = @At("HEAD"), cancellable = true)
    void onSetScaleFactor(double scaleFactor, CallbackInfo ci) {
        new EventScaleFactor(scaleFactor).post(ci);
    }
}
