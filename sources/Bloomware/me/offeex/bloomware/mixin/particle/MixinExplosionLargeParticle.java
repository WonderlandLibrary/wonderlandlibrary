package me.offeex.bloomware.mixin.particle;

import me.offeex.bloomware.client.module.visuals.NoRender;
import me.offeex.bloomware.event.events.EventParticle;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExplosionLargeParticle.class)
public abstract class MixinExplosionLargeParticle extends SpriteBillboardParticle {
    protected MixinExplosionLargeParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
    }

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        new EventParticle(this, velocityX, velocityY, velocityZ).post();
    }

    @Mixin(ExplosionLargeParticle.Factory.class)
    public static class MixinExplosionLargeParticleFactory {
        @Inject(method = "createParticle(Lnet/minecraft/particle/DefaultParticleType;Lnet/minecraft/client/world/ClientWorld;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
        public void createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, CallbackInfoReturnable<Particle> cir) {
            if (NoRender.INSTANCE.getEnabled() && NoRender.INSTANCE.getExplosions().getToggled()) cir.setReturnValue(null);
        }
    }
}
