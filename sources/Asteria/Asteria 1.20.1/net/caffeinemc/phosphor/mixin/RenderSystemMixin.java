package net.caffeinemc.phosphor.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.caffeinemc.phosphor.gui.ImguiLoader;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Inject(at = @At("HEAD"), method = "flipFrame")
    private static void runTickTail(CallbackInfo ci) {
        MinecraftClient.getInstance().getProfiler().push("ImGui Render");
        ImguiLoader.onFrameRender();
        MinecraftClient.getInstance().getProfiler().pop();
    }
}
