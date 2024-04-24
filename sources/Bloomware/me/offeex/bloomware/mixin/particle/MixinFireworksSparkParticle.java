package me.offeex.bloomware.mixin.particle;

import me.offeex.bloomware.client.module.visuals.NoRender;
import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworksSparkParticle.class)
public class MixinFireworksSparkParticle {
    @Mixin(FireworksSparkParticle.ExplosionFactory.class)
    private static class MixinExplosionFactory {
        @Inject(method = "createParticle(Lnet/minecraft/particle/DefaultParticleType;Lnet/minecraft/client/world/ClientWorld;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
        private void buildGeometry(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, CallbackInfoReturnable<Particle> cir) {
            NoRender module = NoRender.INSTANCE;
            if (module.getEnabled() && NoRender.INSTANCE.getFireworkDust().getToggled()) cir.cancel();
        }
    }
    @Mixin(FireworksSparkParticle.FireworkParticle.class)
    private static class MixinFireworkParticle {
        @Inject(method = "addExplosionParticle", at = @At("HEAD"), cancellable = true)
        private void addExplosionParticle(double d, double e, double f, double g, double h, double i, int[] is, int[] js, boolean bl, boolean bl2, CallbackInfo ci) {
            NoRender module = NoRender.INSTANCE;
            if (module.getEnabled() && NoRender.INSTANCE.getFireworkDust().getToggled()) ci.cancel();
        }
    }
}
