package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.api.event.events.MouseMoveEvent;
import net.caffeinemc.phosphor.api.event.events.MousePressEvent;
import net.caffeinemc.phosphor.api.event.events.MouseUpdateEvent;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.gui.AsteriaMenu;
import net.caffeinemc.phosphor.gui.AsteriaNewMenu;
import net.caffeinemc.phosphor.gui.CategoryTab;
import net.caffeinemc.phosphor.gui.ImguiLoader;
import net.caffeinemc.phosphor.module.modules.client.AsteriaSettingsModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onCursorPos", at = @At("HEAD"))
    private void onMouseMove(long window, double mouseX, double mouseY, CallbackInfo ci) {
        if (window == this.client.getWindow().getHandle())
            Phosphor.EVENTBUS.post(MouseMoveEvent.get(mouseX, mouseY));
    }

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void onMouseUpdate(CallbackInfo ci) {
        Phosphor.EVENTBUS.post(MouseUpdateEvent.get());
    }

    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        AsteriaSettingsModule asteria = Phosphor.moduleManager().getModule(AsteriaSettingsModule.class);
        if (asteria != null && asteria.isEnabled()) {
            ci.cancel();
        }

        Phosphor.EVENTBUS.post(MousePressEvent.get(button, action));
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        AsteriaSettingsModule asteria = Phosphor.moduleManager().getModule(AsteriaSettingsModule.class);
        if (asteria != null && asteria.isEnabled()) {
            double scrollY = vertical * 30;

            if (ImguiLoader.isRendered(AsteriaNewMenu.getInstance())) {
                AsteriaNewMenu.getInstance().scrollY -= scrollY;
            } else if (ImguiLoader.isRendered(AsteriaMenu.getInstance())) {
                for (CategoryTab categoryTab : AsteriaMenu.getInstance().tabs) {
                    if (categoryTab.isWindowFocused()) categoryTab.scrollY -= scrollY;
                }
            }

            ci.cancel();
        }
    }
}
