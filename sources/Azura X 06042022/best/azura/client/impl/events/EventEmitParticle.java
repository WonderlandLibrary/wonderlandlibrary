package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;import net.minecraft.util.EnumParticleTypes;

public class EventEmitParticle implements NamedEvent {
	public int multiplier;
	public EnumParticleTypes particleTypes;

	public EventEmitParticle(int multiplier, EnumParticleTypes particleTypes) {
		this.multiplier = multiplier;
		this.particleTypes = particleTypes;
	}

	public EnumParticleTypes getParticleTypes() {
		return particleTypes;
	}

	public void setParticleTypes(EnumParticleTypes particleTypes) {
		this.particleTypes = particleTypes;
	}

	@Override
	public String name() {
		return "emitParticle";
	}
}