package tech.atani.client.listener.event.minecraft.render;

import net.minecraft.util.EnumParticleTypes;
import tech.atani.client.listener.event.Event;

public class EmitParticleEvent extends Event {
	public int multiplier;
	public EnumParticleTypes particleTypes;

	public EmitParticleEvent(int multiplier, EnumParticleTypes particleTypes) {
		this.multiplier = multiplier;
		this.particleTypes = particleTypes;
	}

	public EnumParticleTypes getParticleTypes() {
		return particleTypes;
	}

	public void setParticleTypes(EnumParticleTypes particleTypes) {
		this.particleTypes = particleTypes;
	}

}