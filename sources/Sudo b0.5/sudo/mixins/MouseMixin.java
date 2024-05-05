package sudo.mixins;

import org.lwjgl.glfw.GLFW;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Mouse;
import sudo.module.ModuleManager;
import sudo.module.movement.ClickTP;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    public void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (action == GLFW.GLFW_PRESS && button == 1) {
        	if (ModuleManager.INSTANCE.getModule(ClickTP.class).isEnabled()) {
            	ClickTP.clickTPaction();
        	}
        }
    }
}