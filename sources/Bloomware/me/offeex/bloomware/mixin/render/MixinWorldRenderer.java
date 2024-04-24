package me.offeex.bloomware.mixin.render;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import me.offeex.bloomware.client.module.visuals.BarrierView;
import me.offeex.bloomware.client.module.visuals.BoxHighlight;
import me.offeex.bloomware.client.module.visuals.NoRender;
import me.offeex.bloomware.event.events.EventRender;
import net.minecraft.block.BlockState;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    @Shadow
    protected abstract ParticlesMode getRandomParticleSpawnChance(boolean bl);

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    private boolean onDrawBlockOutline(WorldRenderer instance, MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double x, double y, double z, BlockPos blockPos, BlockState blockState) {
        EventRender.CrosshairTarget event = new EventRender.CrosshairTarget(matrixStack, vertexConsumer, entity, x, y, z, blockPos, blockState).post();
        return event.getCanceled();
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void renderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci) {
        NoRender module = NoRender.INSTANCE;
        if (module.getEnabled() && NoRender.INSTANCE.getWeather().getToggled()) ci.cancel();
    }

    @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
    private void onDrawHighlightedBlockOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo info) {
        if (BoxHighlight.INSTANCE.getEnabled()) info.cancel();
    }

    @Redirect(method = "spawnParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;getRandomParticleSpawnChance(Z)Lnet/minecraft/client/option/ParticlesMode;"))
    private ParticlesMode renderBarrier(WorldRenderer instance, boolean bl) {
        return BarrierView.INSTANCE.getEnabled() ? ParticlesMode.ALL : getRandomParticleSpawnChance(bl);
    }
}