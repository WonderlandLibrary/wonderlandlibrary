package me.offeex.bloomware.mixin.screen;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.gui.screen.LoginScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClickableWidget.class)
public class MixinClickableWidget {
    @Inject(method = "isHovered", at = @At("HEAD"), cancellable = true)
    public void onRenderButton(CallbackInfoReturnable<Boolean> cir) {
        if (Bloomware.INSTANCE.getCurrentScreen() != null && !(Bloomware.INSTANCE.getCurrentScreen() instanceof LoginScreen)) cir.setReturnValue(false);
    }
}
