package net.smoothboot.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.smoothboot.client.Virginclient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

	@Mixin(MinecraftClient.class)
	public class VirginMixin {
		@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
		public void onTick(CallbackInfo ci) {
			Virginclient.INSTANCE.onTick();
		}

		@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V", shift = At.Shift.BEFORE))
		private void init(RunArgs args, CallbackInfo ci) {
			Virginclient.init();
		}
	}