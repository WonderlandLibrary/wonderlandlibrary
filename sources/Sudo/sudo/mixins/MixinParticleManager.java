package sudo.mixins;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import sudo.Client;
import sudo.events.EventParticles;
import sudo.module.ModuleManager;
import sudo.module.render.NoRender;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ParticleManager.class)
public abstract class MixinParticleManager {
    @Shadow @Nullable protected abstract <T extends ParticleEffect> Particle createParticle(T parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);
    
	private static NoRender noRenderModule = ModuleManager.INSTANCE.getModule(NoRender.class);

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> info) {
        EventParticles event = new EventParticles(parameters);
        Client.EventBus.post(event);
        if (noRenderModule.isEnabled()) {
        	if (noRenderModule.explosion.isEnabled()) {
	            if (parameters.getType() == ParticleTypes.EXPLOSION) info.setReturnValue(createParticle(parameters, x, y, z, velocityX, velocityY, velocityZ));
	            else info.cancel();
	        }
        }
    }
}
