package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.common.Phosphor;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.caffeinemc.phosphor.api.event.events.KeyPressEvent;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKeyPress(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        Phosphor.EVENTBUS.post(KeyPressEvent.get(key, scancode, action, window));
    }
}
