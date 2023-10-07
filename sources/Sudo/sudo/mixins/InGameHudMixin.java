package sudo.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import sudo.module.ModuleManager;
import sudo.module.render.NoOverlay;
import sudo.ui.Hud;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(method = "render", at = @At("RETURN"), cancellable = true)
	public void renderHud(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		Hud.render(matrices, tickDelta);
	}

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
	private void renderStatusEffectOverlay(MatrixStack matrices, CallbackInfo info) {
    	info.cancel();
	}
	
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/util/Identifier;F)V", ordinal = 0))
    private void onRenderPumpkinOverlay(Args args) {
        if (ModuleManager.INSTANCE.getModule(NoOverlay.class).isEnabled()) args.set(1, 0f);
    }
    
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/util/Identifier;F)V", ordinal = 1))
    private void onRenderPowderedSnowOverlay(Args args) {
        if (ModuleManager.INSTANCE.getModule(NoOverlay.class).isEnabled()) args.set(1, 0f);
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderVignetteOverlay(Entity entity, CallbackInfo info) {
        if (ModuleManager.INSTANCE.getModule(NoOverlay.class).isEnabled()) info.cancel();
    }
    
    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderSpyglassOverlay(float scale, CallbackInfo info) {
        if (ModuleManager.INSTANCE.getModule(NoOverlay.class).isEnabled()) info.cancel();
    }
    
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void renderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo info) {
//    	info.cancel();
    }
}