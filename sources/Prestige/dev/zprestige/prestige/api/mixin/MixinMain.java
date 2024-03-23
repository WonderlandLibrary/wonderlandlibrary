package dev.zprestige.prestige.api.mixin;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Main.class})
public class MixinMain {

    @Inject(method={"<clinit>"}, at={@At(value="HEAD")}, cancellable=true)
    private static void clinit(CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}
