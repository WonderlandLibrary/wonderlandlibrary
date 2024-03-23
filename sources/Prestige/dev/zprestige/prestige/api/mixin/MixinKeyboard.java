package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.KeyEvent;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Keyboard.class})
public class MixinKeyboard {
    @Inject(method = { "onKey" }, at = { @At("HEAD") }, cancellable = true)
    void onKey(long n, int n2, int n3, int n4, int n5, CallbackInfo callbackInfo) {
        if (!Prestige.Companion.getSelfDestructed()) {
            if (n2 != -1) {
                if (new KeyEvent(n2, n4).invoke()) {
                    callbackInfo.cancel();
                }
            }
        }
    }
}
