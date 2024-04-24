package me.offeex.bloomware.mixin.particle;
import me.offeex.bloomware.event.events.EventParticle;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TotemParticle.class)
public abstract class MixinTotemParticle extends AnimatedParticle {
    protected MixinTotemParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteProvider, float g) {
        super(clientWorld, d, e, f, spriteProvider, g);
    }

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        new EventParticle(this, velocityX, velocityY, velocityZ).post();
    }
}
