package sudo.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import sudo.events.EventParticle;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

	@Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
	public void addParticle(Particle particle, CallbackInfo ci) {
		EventParticle.Normal event = new EventParticle.Normal(particle);
		event.call();

		if (event.isCancelled()) {
			ci.cancel();
		}
	}

	@Inject(method = "addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;)V", at = @At("HEAD"), cancellable = true)
	public void addEmitter(Entity entity, ParticleEffect particleEffect, CallbackInfo callback) {
		EventParticle.Emitter event = new EventParticle.Emitter(particleEffect);
		event.call();

		if (event.isCancelled()) {
			callback.cancel();
		}
	}

	@Inject(method = "addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V", at = @At("HEAD"), cancellable = true)
	public void addEmitter_(Entity entity, ParticleEffect particleEffect, int maxAge, CallbackInfo callback) {
		EventParticle.Emitter event = new EventParticle.Emitter(particleEffect);
		event.call();

		if (event.isCancelled()) {
			callback.cancel();
		}
	}
}