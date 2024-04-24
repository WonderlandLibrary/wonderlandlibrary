package me.offeex.bloomware.mixin.render;

import me.offeex.bloomware.client.module.visuals.DamageTint;
import me.offeex.bloomware.client.module.visuals.NoRender;
import me.offeex.bloomware.event.events.EventRender;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinIngameHud {

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    public void renderHudPre(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
        new EventRender.HUD(matrixStack, tickDelta).post(ci);
    }

    @Inject(method = "render", at = @At(value = "RETURN"))
    public void renderHudPost(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
        new EventRender.HUD(matrixStack, tickDelta).shift().post();
    }

    @Inject(at = @At("HEAD"), method = "renderOverlay", cancellable = true)
    private void onRenderOverlay(MatrixStack matrices, Identifier texture, float opacity, CallbackInfo ci) {
        NoRender.INSTANCE.handleOverlays(texture, ci);
    }

    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void renderSpyglassOverlay(MatrixStack matrices, float scale, CallbackInfo ci) {
        NoRender module = NoRender.INSTANCE;
        if (module.getEnabled() && NoRender.INSTANCE.getSpyGlassScope().getToggled()) ci.cancel();
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void renderVignetteOverlay(MatrixStack matrices, Entity entity, CallbackInfo ci) {
        NoRender module = NoRender.INSTANCE;
        if (module.getEnabled() && NoRender.INSTANCE.getVignette().getToggled()) ci.cancel();
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPortalOverlay(MatrixStack matrices, float nauseaStrength, CallbackInfo ci) {
        NoRender module = NoRender.INSTANCE;
        if (module.getEnabled() && NoRender.INSTANCE.getPortal().getToggled()) ci.cancel();
    }

    @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;interactionManager:Lnet/minecraft/client/network/ClientPlayerInteractionManager;", ordinal = 0))
    private void renderVignette(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        DamageTint.INSTANCE.draw();
    }

	@Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
	private void onRenderScoreboard(MatrixStack matrixStack, ScoreboardObjective scoreboardObjective, CallbackInfo ci) {
		if (NoRender.INSTANCE.getEnabled() && NoRender.INSTANCE.getScoreboard().getToggled()) ci.cancel();
	}
}