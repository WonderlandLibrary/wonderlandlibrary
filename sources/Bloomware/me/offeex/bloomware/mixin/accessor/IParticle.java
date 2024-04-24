package me.offeex.bloomware.mixin.accessor;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface IParticle {
    @Accessor("gravityStrength")
    void setGravityStrength(float gravityStrength);
}
