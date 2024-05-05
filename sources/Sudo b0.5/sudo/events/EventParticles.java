package sudo.events;

import net.minecraft.particle.ParticleEffect;

import sudo.core.event.Event;

public class EventParticles extends Event {
    public ParticleEffect particle;

    public EventParticles(ParticleEffect particle) {
        this.setCancelled(false);
        this.particle = particle;
    }
}