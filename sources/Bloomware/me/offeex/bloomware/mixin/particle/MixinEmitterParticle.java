package me.offeex.bloomware.mixin.particle;

import me.offeex.bloomware.client.module.visuals.Particles;
import net.minecraft.client.particle.EmitterParticle;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EmitterParticle.class)
public class MixinEmitterParticle extends NoRenderParticle {
    protected MixinEmitterParticle(ClientWorld world, double d, double e, double f) {
        super(world, d, e, f);
    }

    @Final @Shadow
    private ParticleEffect parameters;

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 16))
    public int tick(int constant) {
        Particles module = Particles.INSTANCE;
        int amount = 0;
        if (parameters.getType() == ParticleTypes.TOTEM_OF_UNDYING) amount = (int) Particles.INSTANCE.getTAmount().getValue();
        else if (parameters.getType() == ParticleTypes.BLOCK) amount = (int) Particles.INSTANCE.getBdAmount().getValue();
        return module.getEnabled() ? amount : constant;
    }
}
