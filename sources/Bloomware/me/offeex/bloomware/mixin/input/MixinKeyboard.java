package me.offeex.bloomware.mixin.input;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.gui.screen.ClickGUI;
import me.offeex.bloomware.api.gui.screen.HudEditor;
import me.offeex.bloomware.event.events.EventInput;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(Keyboard.class)
public class MixinKeyboard {

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        boolean whitelist = Bloomware.INSTANCE.getMc().currentScreen == null || Bloomware.INSTANCE.getMc().currentScreen instanceof ClickGUI || Bloomware.INSTANCE.getMc().currentScreen instanceof HudEditor;
        if (!whitelist) return;
        switch (action) {
            case 0 -> new EventInput.Key.Release(key, scanCode).post(ci);
            case 1 -> new EventInput.Key.Press(key, scanCode).post(ci);
            case 2 -> new EventInput.Key.Hold(key, scanCode).post(ci);
        }
    }
}