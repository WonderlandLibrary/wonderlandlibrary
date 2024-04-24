package me.offeex.bloomware.mixin.particle;

import me.offeex.bloomware.client.module.visuals.Particles;
import net.minecraft.client.particle.ExplosionEmitterParticle;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ExplosionEmitterParticle.class)
public abstract class MixinExplosionEmitterParticle extends NoRenderParticle {
    protected MixinExplosionEmitterParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 6))
    public int tick(int constant) {
        Particles module = Particles.INSTANCE;
        return module.getEnabled() ? (int) Particles.INSTANCE.getEAmount().getValue() : constant;
    }
}
