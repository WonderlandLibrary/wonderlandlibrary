package sudo.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import sudo.Client;
//import sudo.core.managers.ConfigManager;
import sudo.module.ModuleManager;
import sudo.module.render.ESP;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void onTick(CallbackInfo ci) {
		Client.fabricInteractManager.onTick();
	}

	@Inject(method = "run", at = @At("HEAD"), cancellable = true)
	public void onRun(CallbackInfo ci){
		Client.fabricInteractManager.onRun();
	}
	
	@Inject(method = "getWindowTitle", at = @At("HEAD"), cancellable = true)
    public void getWindowTitle(CallbackInfoReturnable<String> ci) {
        ci.setReturnValue("Sudo client");
    }
	
	@SuppressWarnings("static-access")
	@Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
	private void onHasOutline(Entity entity, CallbackInfoReturnable<Boolean> info){
		if(ModuleManager.INSTANCE.getModule(ESP.class).isEnabled() && ModuleManager.INSTANCE.getModule(ESP.class).mode.is("Glow") && ModuleManager.INSTANCE.getModule(ESP.class).shouldRenderEntity(entity)) {
			info.setReturnValue(true);
		}
	}
	
	@Inject(at = @At("TAIL"), method = "scheduleStop")
	public void onShutdown(CallbackInfo ci) {
//		ConfigManager.saveConfig();
	}
//
//	public void saveConfig() {
//		Client.logger.warn("Saving config");
//		Objects.requireNonNull(Client.configManager).saveAll();
//		Client.logger.warn("Config saved!");
//	}
}
