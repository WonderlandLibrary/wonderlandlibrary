package me.offeex.bloomware.mixin.input;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.offeex.bloomware.event.events.EventInput;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding {
    @ModifyReturnValue(method = "isPressed", at = @At("RETURN"))
    boolean onIsPressed(boolean pressed) {
        EventInput.Binding event = new EventInput.Binding((KeyBinding) (Object) this, pressed).post();
        return event.getPressed();
    }
}
