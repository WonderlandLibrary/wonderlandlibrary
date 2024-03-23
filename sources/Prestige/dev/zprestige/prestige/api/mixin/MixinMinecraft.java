package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.ResolutionChangeEvent;
import dev.zprestige.prestige.client.event.impl.RunEvent;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.Window;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={MinecraftClient.class})
public class MixinMinecraft {
    @Shadow
    @Final
    public Window window;
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method={"onResolutionChanged"}, at={@At(value="TAIL")})
    void captureResize(CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        new ResolutionChangeEvent(this.window).invoke();
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    void tick(CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().player == null) {
            return;
        }
        if (new TickEvent().invoke()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"getFramerateLimit"}, at={@At(value="RETURN")}, cancellable=true)
    void getFramerateLimit(CallbackInfoReturnable callbackInfoReturnable) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        callbackInfoReturnable.setReturnValue(window.getFramerateLimit());
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    void run(CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        new RunEvent().invoke();
    }
}
